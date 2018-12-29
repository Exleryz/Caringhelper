package com.weimore.caringhelper.widget

import android.content.Context
import android.support.v7.app.AppCompatDialog
import android.text.TextUtils
import android.view.Gravity
import android.view.WindowManager
import com.weimore.caringhelper.R
import com.weimore.caringhelper.entity.ProductCategory
import com.weimore.util.DimenUtil
import com.weimore.util.ToastUtil
import kotlinx.android.synthetic.main.dialog_product_category.*

/**
 * @author Weimore
2018/12/21.
description:
 */
class ProductCategoryDialog :AppCompatDialog{

    private var listener:ProductCategoryListener?=null

    constructor(context: Context?) : this(context,R.style.WhiteBackGroundDialog)

    constructor(context: Context?, theme: Int) : super(context, theme){
        setContentView(R.layout.dialog_product_category)
        val lp = window!!.attributes
        lp.width = (DimenUtil.getScreenWidth() * 0.7).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        window!!.attributes = lp
        initView()
    }

    fun setConfirmListener(listener: ProductCategoryListener){
        this.listener = listener
    }

    private fun initView() {
        tv_cancel.setOnClickListener({
            dismiss()
        })
        tv_confirm.setOnClickListener({
            if(TextUtils.isEmpty(et_goods_name.text)||"".equals(et_goods_name.text.trim())){
                ToastUtil.showShort("商品类名不能为空")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(et_priority.text)||"".equals(et_priority.text.trim())){
                ToastUtil.showShort("请设置优先级")
                return@setOnClickListener
            }
            listener?.createCategory(
                    ProductCategory(productCategoryName = et_goods_name.text.toString()
                            ,priority = et_priority.text.toString().toInt()
                    )
            )
            dismiss()
        })
    }

    override fun dismiss() {
        super.dismiss()
        et_goods_name.text.clear()
        et_priority.text.clear()
    }

    interface ProductCategoryListener{

        fun createCategory(productCategory: ProductCategory)

    }

}