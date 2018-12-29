package com.weimore.config;

import android.content.Context;

import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

/**
 * @author Weimore
 *         2018/4/13.
 *         description:
 */

public class SmartRefreshConfig {
    private boolean refreshEnable;
    private boolean loadMoreEnable;
    private boolean autoLaodMoreEnable;
    private boolean headerTranslationContentEnable;
    private RefreshHeader refreshHeader;

    public SmartRefreshConfig setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
        return this;
    }

    public SmartRefreshConfig setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
        return this;
    }

    public SmartRefreshConfig setHeader(RefreshHeader header) {
        refreshHeader = header;
        return this;
    }

    public SmartRefreshConfig setAutoLoadMoreEnable(boolean autoLaodMoreEnable){
        this.autoLaodMoreEnable = autoLaodMoreEnable;
        return this;
    }

    public SmartRefreshConfig setHeaderTranslationContentEnable(boolean headerTranslationContentEnable) {
        this.headerTranslationContentEnable = headerTranslationContentEnable;
        return this;
    }

    public static SmartRefreshConfig defaultConfig(Context context) {
        return new SmartRefreshConfig()
                .setLoadMoreEnable(false)
                .setRefreshEnable(true)
                .setAutoLoadMoreEnable(false)
                .setHeaderTranslationContentEnable(true)
                .setHeader(new WaterDropHeader(context));
    }

    public static SmartRefreshConfig defaultConfig(Context context, RefreshHeader refreshHeader) {
        return new SmartRefreshConfig()
                .setLoadMoreEnable(false)
                .setRefreshEnable(true)
                .setAutoLoadMoreEnable(false)
                .setHeaderTranslationContentEnable(true)
                .setHeader(refreshHeader);
    }

    public void into(SmartRefreshLayout smartRefreshLayout) {
        smartRefreshLayout.setEnableRefresh(refreshEnable)
                .setEnableLoadMore(loadMoreEnable)
                .setEnableAutoLoadMore(autoLaodMoreEnable)
                .setEnableHeaderTranslationContent(headerTranslationContentEnable)
                .setRefreshHeader(refreshHeader);
    }

}
