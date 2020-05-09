/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/9/20 1:08 PM
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
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactory
import net.geeksempire.indexedfastscroller.library.Sides.Bottom.BottomSideIndexedFastScroller
import net.geeksempire.indexedfastscroller.library.Sides.Left.LeftSideIndexedFastScroller
import net.geeksempire.indexedfastscroller.library.Sides.Right.RightSideIndexedFastScroller

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
 * @param indexedFastScrollerFactory Change Default Value Or Just Pass IndexedFastScrollerFactory()
 **/
class IndexedFastScroller(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val rootView: ViewGroup,
    private val nestedScrollView: ScrollView,
    private val recyclerView: RecyclerView,
    private val indexedFastScrollerFactory: IndexedFastScrollerFactory) {

    init {
        Log.d(this@IndexedFastScroller.javaClass.simpleName, "*** Indexed Fast Scroller Initialized ***")
    }

    fun setupIndex(): Deferred<IndexedFastScroller> = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

        when (indexedFastScrollerFactory.indexSide) {
            IndexSide.RIGHT -> {
                Log.d(this@IndexedFastScroller.javaClass.simpleName, "*** Right Side Index ***")

                val rightSideIndexedFastScroller: RightSideIndexedFastScroller = RightSideIndexedFastScroller(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactory
                )
                rightSideIndexedFastScroller.initializeIndexView().await()

            }
            IndexSide.LEFT -> {
                Log.d(this@IndexedFastScroller.javaClass.simpleName, "*** Left Side Index ***")

                val leftSideIndexedFastScroller: LeftSideIndexedFastScroller = LeftSideIndexedFastScroller(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactory
                )
                leftSideIndexedFastScroller.initializeIndexView().await()

            }
            IndexSide.BOTTOM -> {
                Log.d(this@IndexedFastScroller.javaClass.simpleName, "*** Bottom Side Index ***")

                val bottomSideIndexedFastScroller: BottomSideIndexedFastScroller = BottomSideIndexedFastScroller(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactory
                )
                bottomSideIndexedFastScroller.initializeIndexView().await()

            }
            else -> {

                val rightSideIndexedFastScroller: RightSideIndexedFastScroller = RightSideIndexedFastScroller(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactory
                )
                rightSideIndexedFastScroller.initializeIndexView().await()

            }
        }

        this@IndexedFastScroller
    }
}