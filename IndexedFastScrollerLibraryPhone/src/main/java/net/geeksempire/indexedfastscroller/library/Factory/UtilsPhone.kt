/*
 * Copyright © 2021 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 3/25/21 9:31 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Factory

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

fun Float.convertToDp(context: Context) : Int {

    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            this@convertToDp,
            context.resources.displayMetrics).toInt()
}

fun Int.convertToDp(context: Context) : Int {

    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            this@convertToDp.toFloat(),
            context.resources.displayMetrics).toInt()
}

fun calculateStatusBarHeight(resources: Resources) : Int {
    var statusBarHeight = 0

    val resourceIdNavigationBar: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceIdNavigationBar > 0) {
        statusBarHeight = resources.getDimensionPixelSize(resourceIdNavigationBar)
    }

    return statusBarHeight
}


fun calculateNavigationBarHeight(resources: Resources) : Int {
    var navigationBarHeight = 0

    val resourceIdStatusBar: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceIdStatusBar > 0) {
        navigationBarHeight = resources.getDimensionPixelSize(resourceIdStatusBar)
    }

    return navigationBarHeight
}

fun displayX(context: Context): Int {
    return context.resources.displayMetrics.widthPixels
}

fun displayY(context: Context): Int {
    return context.resources.displayMetrics.heightPixels
}