/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/4/20 3:32 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import net.geeksempire.indexedfastscroller.library.Factory.IndexSide
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactoryPhone
import net.geeksempire.indexedfastscroller.library.Sides.Bottom.BottomSideIndexedFastScrollerPhone
import net.geeksempire.indexedfastscroller.library.Sides.Left.LeftSideIndexedFastScrollerPhone
import net.geeksempire.indexedfastscroller.library.Sides.Right.RightSideIndexedFastScrollerPhone

/**
 * Enabling ViewBinding Is Highly Recommended.
 * Just Add The Below Configuration To Your App Level Gradle In Android Section
 * viewBinding { enabled = true }
 *
 *
 * @param rootView Instance Of Root (Base) View In Your Layout
 * @param nestedScrollView Follow This Hierarchy: ScrollView -> RelativeLayout -> RecyclerView
 * @param recyclerView Instance Of A RecyclerView That You Want To Populate With Items
 *
 *
 * @param indexedFastScrollerFactoryPhone Change Default Value Or Just Pass IndexedFastScrollerFactory()
 **/
class IndexedFastScrollerPhone(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val rootView: ViewGroup,
    private val nestedScrollView: ScrollView,
    private val recyclerView: RecyclerView,
    private val indexedFastScrollerFactoryPhone: IndexedFastScrollerFactoryPhone) {

    init {
        Log.d(this@IndexedFastScrollerPhone.javaClass.simpleName, "*** Indexed Fast Scroller Initialized ***")
    }

    fun setupIndex(): Deferred<IndexedFastScrollerPhone> = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

        when (indexedFastScrollerFactoryPhone.indexSide) {
            IndexSide.RIGHT -> {
                Log.d(this@IndexedFastScrollerPhone.javaClass.simpleName, "*** Right Side Index ***")

                val rightSideIndexedFastScrollerPhone: RightSideIndexedFastScrollerPhone = RightSideIndexedFastScrollerPhone(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactoryPhone
                )
                rightSideIndexedFastScrollerPhone.initializeIndexView().await()

            }
            IndexSide.LEFT -> {
                Log.d(this@IndexedFastScrollerPhone.javaClass.simpleName, "*** Left Side Index ***")

                val leftSideIndexedFastScrollerPhone: LeftSideIndexedFastScrollerPhone = LeftSideIndexedFastScrollerPhone(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactoryPhone
                )
                leftSideIndexedFastScrollerPhone.initializeIndexView().await()

            }
            IndexSide.BOTTOM -> {
                Log.d(this@IndexedFastScrollerPhone.javaClass.simpleName, "*** Bottom Side Index ***")

                val bottomSideIndexedFastScrollerPhone: BottomSideIndexedFastScrollerPhone = BottomSideIndexedFastScrollerPhone(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactoryPhone
                )
                bottomSideIndexedFastScrollerPhone.initializeIndexView().await()

            }
            else -> {

                val rightSideIndexedFastScrollerPhone: RightSideIndexedFastScrollerPhone = RightSideIndexedFastScrollerPhone(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactoryPhone
                )
                rightSideIndexedFastScrollerPhone.initializeIndexView().await()

            }
        }

        this@IndexedFastScrollerPhone
    }
}