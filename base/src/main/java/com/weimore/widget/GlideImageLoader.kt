package com.weimore.wiget

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.youth.banner.loader.ImageLoader

/**
 * @author Weimore
2018/11/20.
description:
 */
class GlideImageLoader:ImageLoader(){

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)
    }

}