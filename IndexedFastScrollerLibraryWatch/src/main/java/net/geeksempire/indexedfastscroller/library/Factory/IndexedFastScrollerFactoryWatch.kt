/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/18/20 9:12 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Factory

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable

/**
 * When Populating Your List Get First Char Of Each Item Title By itemTextTitle.substring(0, 1).toUpperCase(Locale.getDefault()).
 * & Add It To A ArrayList<String>.
 * Then Pass It As...
 *
 * @param listOfNewCharOfItemsForIndex ArrayList<String>
 *
 *
 * @param rootPaddingTop Set Padding For Root View Group
 *
 * @param rootPaddingBottom Set Padding For Root View Group
 *
 * @param rootPaddingStart Set Padding For Root View Group
 *
 * @param rootPaddingEnd Set Padding For Root View Group
 *
 *
 *
 * @param popupEnable Enable Popup View Of Index Text
 *
 * @param popupVerticalOffset Set Integer Number Of Offset For Popup View Of Index Text. It Will Automatically Convert It To DP. Default Value Is 7dp.
 *
 * @param popupHorizontalOffset Set Integer Number Of Offset For Popup View Of Index Text. It Will Automatically Convert It To DP. Default Value Is 7dp.
 *
 * @param popupTextColor Set Color Of Text For Popup View.
 *
 * @param popupBackgroundShape Set A Drawable As Background Of Popup View Of Index Text.
 *
 *
 *
 * @param indexItemTextColor Set Color Of Text For Popup View.
 **/

object IndexSide {
        const val RIGHT = 0
        const val LEFT = 1
        const val BOTTOM = 2
}

data class IndexedFastScrollerFactory(
        /**
         * When Populating Your List Get First Char Of Each Item Title By itemTextTitle.substring(0, 1).toUpperCase(Locale.getDefault()).
         * & Add It To A ArrayList<String>.
         * Then Pass It As...
         **/
        val listOfNewCharOfItemsForIndex: ArrayList<String>,

        /**
         * Set Where To Put Index View IndexSide.RIGHT - IndexSide.LEFT - IndexSide.BOTTOM
         **/
        var indexSide: Int = IndexSide.RIGHT,

        /**
         * Set Padding For Root View Group
         **/
        var rootPaddingTop: Int = 0,
        /**
         * Set Padding For Root View Group
         **/
        var rootPaddingBottom: Int = 0,
        /**
         * Set Padding For Root View Group
         **/
        var rootPaddingStart: Int = 0,
        /**
         * Set Padding For Root View Group
         **/
        var rootPaddingEnd: Int = 0,



        /**
         * Enable Popup View Of Index Text.
         **/
        var popupEnable: Boolean = true,
        /**
         * Set Integer Number Of Offset For Popup View Of Index Text.
         * It Will Automatically Convert It To DP.
         *
         * Default Value Is 7dp.
         **/
        var popupVerticalOffset: Float = 7F,
        /**
         * Set Integer Number Of Offset For Popup View Of Index Text.
         * It Will Automatically Convert It To DP.
         *
         * Default Value Is 1dp.
         **/
        var popupHorizontalOffset: Float = 1F,
        /**
         * Set Color Of Text For Popup View.
         **/
        var popupTextColor: Int = Color.WHITE,
        /**
         * Set Typeface (Font) Of Text For Popup View.
         **/
        var popupTextFont: Typeface = Typeface.MONOSPACE,
        /**
         * Set Size Of Text For Popup View.
         **/
        var popupTextSize: Float = 29F,
        /**
         * Set A Drawable As Background Of Popup View Of Index Text.
         **/
        var popupBackgroundShape: Drawable? = null,
        /**
         * Set Tint Of Background Of Popup View Of Index Text.
         **/
        var popupBackgroundTint: Int = Color.BLACK,



        /**
         * Set Color Of Text For Item In Index View.
         **/
        var indexItemTextColor: Int = Color.MAGENTA,
        /**
         * Set Typeface (Font) Of Text For Item In Index View.
         **/
        var indexItemFont: Typeface = Typeface.MONOSPACE,
        /**
         * Set Size Of Text For Item In Index View.
         **/
        var indexItemSize: Float = 13F
)