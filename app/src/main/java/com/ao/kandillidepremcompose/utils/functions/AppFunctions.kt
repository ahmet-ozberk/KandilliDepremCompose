package com.ao.kandillidepremcompose.utils.functions

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun resizeDrawable(drawable: Drawable, width: Int, height: Int): Bitmap {
    val bitmap = (drawable as BitmapDrawable).bitmap
    return Bitmap.createScaledBitmap(bitmap, width, height, false)
}