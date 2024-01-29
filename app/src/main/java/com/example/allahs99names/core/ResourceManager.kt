package com.example.allahs99names.core

import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

interface ResourceManager {

    fun getQuantityString(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any): String

    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String

    fun getStringArray(@ArrayRes resId: Int): Array<out String>

    fun getColor(@ColorRes resId: Int): Int

    fun getDrawable(@DrawableRes resId: Int): Drawable

    fun getIntArray(@ArrayRes resId: Int): IntArray

    fun obtainTypedArray(@ArrayRes resId: Int): TypedArray
}