package com.weimore.base

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import com.weimore.widget.LoadingDialog

/**
 * @author Weimore
2018/11/16.
description:
 */
abstract class BaseActivity<P : IBasePresenter> : AppCompatActivity(), IBaseView {

    open lateinit var mUnBinder:Unbinder
    open var mPresenter: P? = null

    private lateinit var mDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!dataBinding()){
            setContentView(getLayoutRes())
        }
        mUnBinder = ButterKnife.bind(this)
        mPresenter = getPresenter()
        mPresenter?.getIntent(intent)
        mDialog = LoadingDialog(this)
        initView()
        Looper.myQueue().addIdleHandler {
            initData()
            return@addIdleHandler false
        }
    }

    override fun onDestroy() {
        mDialog.dismiss()
        mPresenter?.detach()
        mPresenter = null
        mUnBinder.unbind()
        super.onDestroy()
    }

    open fun dataBinding():Boolean = false

    open fun initView() {
    }

    open fun initData() {
    }

    override fun getContext(): Context {
        return this;
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun dismissLoading() {
        mDialog.dismiss()
    }

    abstract fun getLayoutRes(): Int

    abstract fun getPresenter(): P


}