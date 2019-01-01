package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
import com.weimore.base.BaseAdapter;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.databinding.ActivityMapSearchBinding;
import com.weimore.util.L;
import com.weimore.util.ToastUtil;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public class MapSearchActivity extends AppCompatActivity {

    private ActivityMapSearchBinding mBinding;
    private SuggestionSearch mSuggestionSearch = null;
    private String mCity;
    private SuggestionAdapter mAdapter;

    public static void startActivity(Context context,String city){
        Intent intent = new Intent(context,MapSearchActivity.class);
        intent.putExtra("city",city);
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
        if(TextUtils.isEmpty(mCity)){
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
        mBinding.etAddress.setOnClickListener(v ->{
            if(TextUtils.isEmpty(mBinding.etAddress.getText())||"".equals(mBinding.etAddress.getText().toString())){
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

    private class SuggestionAdapter extends BaseAdapter<SuggestionResult.SuggestionInfo>{
        @Override
        protected int getItemLayoutResId() {
            return 0;
        }

        @Override
        protected BaseHolder<SuggestionResult.SuggestionInfo> getViewHolder(View view) {
            return new SuggestionHolder(view,this);
        }
    }

    class SuggestionHolder extends BaseAdapter.BaseHolder<SuggestionResult.SuggestionInfo>{

        public SuggestionHolder(View itemView, BaseAdapter<SuggestionResult.SuggestionInfo> adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void bind(SuggestionResult.SuggestionInfo item, int position) {

            itemView.setOnClickListener(v->{
                MapDemoActivity.startActivity(MapSearchActivity.this,item);
                finish();
            });
        }
    }
}
