/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/4/20 3:35 AM
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
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactoryWatch
import net.geeksempire.indexedfastscroller.library.Sides.Bottom.BottomSideIndexedFastScrollerWatch
import net.geeksempire.indexedfastscroller.library.Sides.Left.LeftSideIndexedFastScrollerWatch
import net.geeksempire.indexedfastscroller.library.Sides.Right.RightSideIndexedFastScrollerWatch

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
 * @param indexedFastScrollerFactoryWatch Change Default Value Or Just Pass IndexedFastScrollerFactory()
 **/
class IndexedFastScrollerWatch(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val rootView: ViewGroup,
    private val nestedScrollView: ScrollView,
    private val recyclerView: RecyclerView,
    private val indexedFastScrollerFactoryWatch: IndexedFastScrollerFactoryWatch) {

    init {
        Log.d(this@IndexedFastScrollerWatch.javaClass.simpleName, "*** Indexed Fast Scroller Initialized ***")
    }

    fun setupIndex(): Deferred<IndexedFastScrollerWatch> = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

        when (indexedFastScrollerFactoryWatch.indexSide) {
            IndexSide.RIGHT -> {
                Log.d(this@IndexedFastScrollerWatch.javaClass.simpleName, "*** Right Side Index ***")

                val rightSideIndexedFastScrollerWatch: RightSideIndexedFastScrollerWatch = RightSideIndexedFastScrollerWatch(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactoryWatch
                )
                rightSideIndexedFastScrollerWatch.initializeIndexView().await()

            }
            IndexSide.LEFT -> {
                Log.d(this@IndexedFastScrollerWatch.javaClass.simpleName, "*** Left Side Index ***")

                val leftSideIndexedFastScrollerWatch: LeftSideIndexedFastScrollerWatch = LeftSideIndexedFastScrollerWatch(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactoryWatch
                )
                leftSideIndexedFastScrollerWatch.initializeIndexView().await()

            }
            IndexSide.BOTTOM -> {
                Log.d(this@IndexedFastScrollerWatch.javaClass.simpleName, "*** Bottom Side Index ***")

                val bottomSideIndexedFastScrollerWatch: BottomSideIndexedFastScrollerWatch = BottomSideIndexedFastScrollerWatch(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactoryWatch
                )
                bottomSideIndexedFastScrollerWatch.initializeIndexView().await()

            }
            else -> {

                val rightSideIndexedFastScrollerWatch: RightSideIndexedFastScrollerWatch = RightSideIndexedFastScrollerWatch(
                    context,
                    layoutInflater,
                    rootView,
                    nestedScrollView,
                    recyclerView,
                    indexedFastScrollerFactoryWatch
                )
                rightSideIndexedFastScrollerWatch.initializeIndexView().await()

            }
        }

        this@IndexedFastScrollerWatch
    }
}