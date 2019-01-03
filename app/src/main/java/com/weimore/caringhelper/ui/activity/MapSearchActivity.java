package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
import com.weimore.base.BaseAdapter;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.databinding.ActivityMapSearchBinding;
import com.weimore.util.L;
import com.weimore.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public class MapSearchActivity extends AppCompatActivity {

    private ActivityMapSearchBinding mBinding;
    private PoiSearch mPoiSearch = PoiSearch.newInstance();
    private SuggestionSearch mSuggestionSearch = null;
    private String mCity;
    private SuggestionAdapter mAdapter;

    public static void startActivity(Context context, String city) {
        Intent intent = new Intent(context, MapSearchActivity.class);
        intent.putExtra("city", city);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map_search);
        init();
    }

    private void init() {
        mCity = getIntent().getStringExtra("city");
        if (TextUtils.isEmpty(mCity)) {
            ToastUtil.showShort("错误的城市信息");
            finish();
        }
        mAdapter = new SuggestionAdapter();
        mBinding.recyclerAddress.setAdapter(mAdapter);

        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(res -> {
            if (res != null && res.getAllSuggestions() != null) {
                L.e(new Gson().toJson(res.getAllSuggestions()));
                mAdapter.clear();
                mAdapter.setData(res.getAllSuggestions());
            }
        });
        mBinding.tvConfirm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBinding.etAddress.getText()) || "".equals(mBinding.etAddress.getText().toString().trim())) {
                ToastUtil.showShort("请输入要搜索的地址");
                return;
            }
            String keyword = mBinding.etAddress.getText().toString();
            mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().city(mCity).keyword(keyword));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuggestionSearch.destroy();
    }

    private class SuggestionAdapter extends BaseAdapter<SuggestionResult.SuggestionInfo> {


        @Override
        protected int getItemLayoutResId() {
            return R.layout.item_suggest_location;
        }

        @Override
        protected BaseHolder<SuggestionResult.SuggestionInfo> getViewHolder(View view) {
            return new SuggestionHolder(view, this);
        }
    }

    class SuggestionHolder extends BaseAdapter.BaseHolder<SuggestionResult.SuggestionInfo> {

        @BindView(R.id.tv_city)
        TextView tvCity;
        @BindView(R.id.tv_district)
        TextView tvDistrict;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_label)
        TextView tvLabel;

        public SuggestionHolder(View itemView, BaseAdapter<SuggestionResult.SuggestionInfo> adapter) {
            super(itemView, adapter);
            ButterKnife.bind(this,itemView);
        }

        @Override
        protected void bind(SuggestionResult.SuggestionInfo item, int position) {
            tvCity.setText(item.city);
            tvDistrict.setText(item.district);
            tvAddress.setText(item.key);
            itemView.setOnClickListener(v -> {
                MapDemoActivity.startActivity(MapSearchActivity.this, item);
                finish();
            });
        }
    }
}
