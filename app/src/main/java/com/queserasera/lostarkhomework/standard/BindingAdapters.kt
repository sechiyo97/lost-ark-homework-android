package com.queserasera.lostarkhomework.standard

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * Created by seheelee on 2021-04-02.
 */

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("goneUnless")
    fun goneUnless(view: View, visible: Boolean?) {
        println("visible? $visible")
        view.visibility = if (visible == true) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter(value = ["imageSrc", "placeholder"], requireAll = false)
    fun setImageSrc(imageView: ImageView, src: String?, placeholder: Drawable?) =
        imageView.setImage(src, placeholder)
}