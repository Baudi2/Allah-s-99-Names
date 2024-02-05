package com.example.allahs99names.core

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceManagerImpl @Inject constructor(@ApplicationContext private val context: Context) : ResourceManager {

    override fun getColor(resId: Int): Int = ContextCompat.getColor(context, resId)

    override fun getQuantityString(resId: Int, quantity: Int, vararg formatArgs: Any): String {
        return context.resources.getQuantityString(resId, quantity, *formatArgs)
    }

    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg formatArgs: Any): String = context.getString(resId, *formatArgs)

    override fun getStringArray(@ArrayRes resId: Int): Array<out String> = context.resources.getStringArray(resId)

    override fun getDrawable(@DrawableRes resId: Int): Drawable = ContextCompat.getDrawable(context, resId)!!

    override fun getIntArray(@ArrayRes resId: Int): IntArray = context.resources.getIntArray(resId)

    override fun obtainTypedArray(resId: Int): TypedArray = context.resources.obtainTypedArray(resId)
}
