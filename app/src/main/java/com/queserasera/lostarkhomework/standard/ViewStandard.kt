package com.queserasera.lostarkhomework.standard

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by seheelee on 2021-04-02.
 */

fun ImageView.setImage(src: String?, placeholder: Drawable? = null) {
    println("src is $src")
    Glide.with(this).load(src).placeholder(placeholder).into(this)
}