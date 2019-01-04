package com.weimore.caringhelper.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weimore.base.BaseActivity;
import com.weimore.base.BaseAdapter;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.dao.helper.ContactBeanHelper;
import com.weimore.caringhelper.databinding.ActivityContactBinding;
import com.weimore.caringhelper.entity.Contact;
import com.weimore.caringhelper.ui.contract.ContactContract;
import com.weimore.caringhelper.ui.presenter.ContactPresenter;
import com.weimore.caringhelper.utils.callback.ExcelUtils;
import com.weimore.caringhelper.utils.callback.MyCallback;
import com.weimore.config.SmartRefreshConfig;
import com.weimore.util.PermissionUtil;
import com.weimore.widget.DoubleDialog;
import com.weimore.widget.EditDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Weimore
 *         2019/1/3.
 *         description:
 */

public class ContactActivity extends BaseActivity<ContactContract.Presenter> implements ContactContract.View {

    private ActivityContactBinding mBinding;
    private ContactAdapter mAdapter;
    private DoubleDialog mDeleteDialog;
    private EditDialog mEditDialog;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ContactActivity.class));
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_contact;
    }

    @Override
    public boolean dataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact);
        return true;
    }

    @NonNull
    @Override
    public ContactContract.Presenter getPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public void initView() {
        mBinding.toolbar.hideLeftImage();
        mAdapter = new ContactAdapter();
        mDeleteDialog = new DoubleDialog(this);
        mEditDialog = new EditDialog(this);

        mDeleteDialog.setContent("确定要删除该白名单用户？");
        mBinding.recyclerContact.setAdapter(mAdapter);
        SmartRefreshConfig.defaultConfig(this).into(mBinding.refreshLayout);
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getMPresenter().getContactInfo();
            }
        });
        mBinding.tvContact.setOnClickListener(v -> {
            PermissionUtil.permissionRequest(this, new PermissionUtil.PermissionsCallback() {
                @Override
                public void success() {
                    showLoading();
                    getMPresenter().getContactFromPhone();
                }
            }, Manifest.permission.READ_CONTACTS);
        });

        mBinding.tvAdd.setOnClickListener(v -> {
            mEditDialog.confirmListener(v12 -> {
                if (TextUtils.isEmpty(mEditDialog.getNameText())) {
                    showToast("请输入联系用户姓名");
                    return;
                }
                if (TextUtils.isEmpty(mEditDialog.getPhoneText())) {
                    showToast("请输入号码");
                    return;
                }
                Contact contact = new Contact(mEditDialog.getNameText(), mEditDialog.getPhoneText());
                if (ContactBeanHelper.queryOneByPhone(contact.getPhoneNo()) != null) {
                    showToast("该号码已在白名单中");
                    return;
                }
                ContactBeanHelper.insertData(contact);
                getMPresenter().insertOrUpdateData(contact);
                mAdapter.addData(contact);
                mEditDialog.dismiss();
                showToast("添加成功");
            });
            mEditDialog.show();
        });

        mBinding.tvExcel.setOnClickListener(v -> {
            ExcelUtils.fileChoose(ContactActivity.this);
        });
    }

    @Override
    public void initData() {
        showLoading();
        getMPresenter().getContactInfo();
    }

    @Override
    public void setContactInfo(List<Contact> contactList) {
        dismissLoading();
        if (contactList != null) {
            mAdapter.setData(contactList);
            mBinding.refreshLayout.finishRefresh();

        }
    }

    @Override
    public void addPhoneContactInfoSuccess() {
        showLoading();
        getMPresenter().getContactInfo();
    }

    private class ContactAdapter extends BaseAdapter<Contact> {

        @Override
        protected int getItemLayoutResId() {
            return R.layout.item_contact;
        }

        @Override
        protected BaseHolder<Contact> getViewHolder(View view) {
            return new ContactHolder(view, this);
        }
    }

    class ContactHolder extends BaseAdapter.BaseHolder<Contact> {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_phone_num)
        TextView tvPhoneNum;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.tv_delete)
        TextView tvDelete;

        public ContactHolder(View itemView, BaseAdapter<Contact> adapter) {
            super(itemView, adapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void bind(Contact item, int position) {
            tvName.setText(item.getName());
            tvPhoneNum.setText(item.getPhoneNo());
            tvEdit.setOnClickListener(v -> {
                mEditDialog.setNameAndPhone(item.getName(), item.getPhoneNo());
                mEditDialog.confirmListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(mEditDialog.getNameText())) {
                            showToast("请输入联系用户姓名");
                            return;
                        }
                        if (TextUtils.isEmpty(mEditDialog.getPhoneText())) {
                            showToast("请输入号码");
                            return;
                        }
                        Contact contact = new Contact(mEditDialog.getNameText(), mEditDialog.getPhoneText());
                        Contact otherContact = ContactBeanHelper.queryOneByPhone(contact.getPhoneNo());
                        if (otherContact != null && !otherContact.getId().equals(item.getId())) {
                            showToast("该号码已在白名单中");
                            return;
                        } else {
                            contact.setId(item.getId());
                            ContactBeanHelper.updateData(contact);
                            getMPresenter().insertOrUpdateData(contact);
                        }
                        mAdapter.changeItem(position, contact);
                        mEditDialog.dismiss();
                        showToast("修改成功");
                    }
                });
                mEditDialog.show();
            });
            tvDelete.setOnClickListener(v -> {
                mDeleteDialog.confirmListener(v1 -> {
                    getMPresenter().deleteData(item);
                    ContactBeanHelper.deleteData(item);
                    mAdapter.removeItem(position);
                    mDeleteDialog.dismiss();
                });
                mDeleteDialog.show();
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ExcelUtils.onActivityResult(this, requestCode, resultCode, data, new MyCallback<Boolean>() {
            @Override
            public void callback(Boolean aBoolean) {
                if (aBoolean) {
                    getMPresenter().syncContactList();
                    showToast("数据导入成功");
                    getMPresenter().getContactInfo();
                } else {
                    showToast("数据导入失败");
                }
            }
        });
    }
}