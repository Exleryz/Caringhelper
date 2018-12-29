package com.weimore.base

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.weimore.widget.LoadingDialog

/**
 * @author Weimore
2018/12/19.
description:
 */
abstract class BaseFragment<P : IBasePresenter> : Fragment(), IBaseView {

    open var mContext: Context? = null
    open var mPresenter: P? = null
    private var mUnBinder: Unbinder? = null
    private var mDialog: LoadingDialog? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = getPresenter()
        mPresenter?.getIntent(activity?.intent!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!dataBinding()) {
            val view = inflater.inflate(getLayoutRes(), container)
            mUnBinder = ButterKnife.bind(this, view)
            mDialog = LoadingDialog(mContext!!)
            return view
        }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Looper.myQueue().addIdleHandler {
            initData()
            return@addIdleHandler false
        }
    }

    override fun onDestroyView() {
        mDialog?.dismiss()
        mDialog = null
        mUnBinder?.unbind()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mPresenter?.detach()
        mPresenter = null
        super.onDestroy()
    }

    open fun dataBinding(): Boolean = false

    open fun initView() {
    }


    open fun initData() {
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun getContext(): Context? {
        return mContext
    }

    override fun showLoading() {
        mDialog?.show()
    }

    override fun dismissLoading() {
        mDialog?.dismiss()
    }

    abstract fun getLayoutRes(): Int

    abstract fun getPresenter(): P

}