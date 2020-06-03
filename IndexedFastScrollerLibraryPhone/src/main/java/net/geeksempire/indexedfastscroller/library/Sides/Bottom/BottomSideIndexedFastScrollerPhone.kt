/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/3/20 9:23 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Sides.Bottom

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactory
import net.geeksempire.indexedfastscroller.library.Factory.convertToDp
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Bottom.Extensions.setupBottomIndex
import net.geeksempire.indexedfastscroller.library.databinding.BottomFastScrollerIndexViewPhoneBinding
import java.util.*
import kotlin.collections.LinkedHashMap


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
class BottomSideIndexedFastScrollerPhone(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val rootView: ViewGroup,
    private val nestedScrollView: ScrollView,
    private val recyclerView: RecyclerView,
    private val indexedFastScrollerFactory: IndexedFastScrollerFactory) {

    private val bottomFastScrollerIndexViewPhoneBinding: BottomFastScrollerIndexViewPhoneBinding = BottomFastScrollerIndexViewPhoneBinding.inflate(layoutInflater)

    private val finalPopupVerticalOffset: Int =
        indexedFastScrollerFactory.popupVerticalOffset.convertToDp(context)

    private val finalPopupHorizontalOffset: Int =
        indexedFastScrollerFactory.popupHorizontalOffset.convertToDp(context)

    init {
        Log.d(this@BottomSideIndexedFastScrollerPhone.javaClass.simpleName, "*** Indexed Fast Scroller Initialized ***")
    }

    fun initializeIndexView(): Deferred<BottomSideIndexedFastScrollerPhone> = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

        bottomFastScrollerIndexViewPhoneBinding.indexView.removeAllViews()

        setupBottomIndex(
            context,
            rootView,
            bottomFastScrollerIndexViewPhoneBinding,
            indexedFastScrollerFactory,
            finalPopupVerticalOffset
        ).loadIndexData(indexedFastScrollerFactory.listOfNewCharOfItemsForIndex).await()

        this@BottomSideIndexedFastScrollerPhone
    }

    /**
     * When Populating Your List Get First Char Of Each Item Title By itemTextTitle.substring(0, 1).toUpperCase(Locale.getDefault()).
     * & Add It To A ArrayList<String>.
     * Then Pass It As...
     *
     * @param listOfNewCharOfItemsForIndex ArrayList<String>
     **/
    private fun loadIndexData(listOfNewCharOfItemsForIndex: ArrayList<String>) = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

        val mapIndexFirstItem: LinkedHashMap<String, Int> = LinkedHashMap<String, Int>()
        val mapIndexLastItem: LinkedHashMap<String, Int> = LinkedHashMap<String, Int>()

        val mapRangeIndex: LinkedHashMap<Int, String> = LinkedHashMap<Int, String>()

        withContext(Dispatchers.Default) {

            listOfNewCharOfItemsForIndex.forEachIndexed { indexNumber, indexText ->

                val finalIndexText = indexText.toUpperCase(Locale.getDefault())

                /*Avoid Duplication*/
                if (mapIndexFirstItem[finalIndexText] == null) {
                    mapIndexFirstItem[finalIndexText] = indexNumber
                }

                mapIndexLastItem[finalIndexText] = indexNumber

            }

        }

        var sideIndexItem = layoutInflater.inflate(R.layout.bottom_fast_scroller_side_index_item_phone, null) as TextView

        mapIndexFirstItem.keys.forEach { indexText ->
            sideIndexItem = layoutInflater.inflate(R.layout.bottom_fast_scroller_side_index_item_phone, null) as TextView
            sideIndexItem.text = indexText.toUpperCase(Locale.getDefault())

            sideIndexItem.typeface = indexedFastScrollerFactory.indexItemFont
            sideIndexItem.setTextColor(indexedFastScrollerFactory.indexItemTextColor)
            sideIndexItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, indexedFastScrollerFactory.indexItemSize)

            bottomFastScrollerIndexViewPhoneBinding.indexView.addView(sideIndexItem)
        }

        val finalTextView = sideIndexItem

        /* *** */
        delay(777)
        /* *** */

        var upperRange = (bottomFastScrollerIndexViewPhoneBinding.indexView.x - finalTextView.width).toInt()

        for (number in 0 until bottomFastScrollerIndexViewPhoneBinding.indexView.childCount) {
            val indexText = (bottomFastScrollerIndexViewPhoneBinding.indexView.getChildAt(number) as TextView).text.toString()
            val indexRange = (bottomFastScrollerIndexViewPhoneBinding.indexView.getChildAt(number).x + bottomFastScrollerIndexViewPhoneBinding.indexView.x + finalTextView.width).toInt()

            for (jRange in upperRange..indexRange) {
                mapRangeIndex[jRange] = indexText
            }

            upperRange = indexRange
        }

        this@async.launch {

            setupFastScrollingIndexing(
                mapIndexFirstItem,
                    mapRangeIndex
            )
        }.join()
    }

    /**
     * Setup Popup View Of Index With Touch On List Of Index
     **/
    @SuppressLint("ClickableViewAccessibility")
    private fun setupFastScrollingIndexing(
        mapIndexFirstItem: LinkedHashMap<String, Int>,
        mapRangeIndex: LinkedHashMap<Int, String>) {

        bottomFastScrollerIndexViewPhoneBinding.nestedIndexScrollView.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                android.R.anim.fade_in
            )
        )
        bottomFastScrollerIndexViewPhoneBinding.nestedIndexScrollView.visibility = View.VISIBLE

        val popupIndexOffsetX = (
                finalPopupHorizontalOffset
                        + bottomFastScrollerIndexViewPhoneBinding.popupIndex.width/2
                ).toFloat()

        bottomFastScrollerIndexViewPhoneBinding.nestedIndexScrollView.setOnTouchListener { view, motionEvent ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (indexedFastScrollerFactory.popupEnable) {
                        val indexText = mapRangeIndex[motionEvent.x.toInt()]

                        if (indexText != null) {
                            bottomFastScrollerIndexViewPhoneBinding.popupIndex.x = motionEvent.rawX - popupIndexOffsetX
                            bottomFastScrollerIndexViewPhoneBinding.popupIndex.text = indexText
                            bottomFastScrollerIndexViewPhoneBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_in
                                )
                            )
                            bottomFastScrollerIndexViewPhoneBinding.popupIndex.visibility = View.VISIBLE
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (indexedFastScrollerFactory.popupEnable) {
                        val indexText = mapRangeIndex[motionEvent.x.toInt()]

                        if (indexText != null) {
                            if (!bottomFastScrollerIndexViewPhoneBinding.popupIndex.isShown) {
                                bottomFastScrollerIndexViewPhoneBinding.popupIndex.startAnimation(
                                    AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                                )
                                bottomFastScrollerIndexViewPhoneBinding.popupIndex.visibility = View.VISIBLE
                            }

                            bottomFastScrollerIndexViewPhoneBinding.popupIndex.x = motionEvent.rawX - popupIndexOffsetX
                            bottomFastScrollerIndexViewPhoneBinding.popupIndex.text = indexText

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem[mapRangeIndex[motionEvent.x.toInt()]] ?: 0
                                ).y.toInt()
                            )

                        } else {
                            if (bottomFastScrollerIndexViewPhoneBinding.popupIndex.isShown) {
                                bottomFastScrollerIndexViewPhoneBinding.popupIndex.startAnimation(
                                    AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
                                )
                                bottomFastScrollerIndexViewPhoneBinding.popupIndex.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (indexedFastScrollerFactory.popupEnable) {
                        if (bottomFastScrollerIndexViewPhoneBinding.popupIndex.isShown) {

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem.get(mapRangeIndex[motionEvent.x.toInt()]) ?: 0
                                ).y.toInt()
                            )

                            bottomFastScrollerIndexViewPhoneBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_out
                                )
                            )
                            bottomFastScrollerIndexViewPhoneBinding.popupIndex.visibility = View.INVISIBLE
                        }
                    } else {

                        nestedScrollView.smoothScrollTo(
                            0,
                            recyclerView.getChildAt(
                                mapIndexFirstItem.get(mapRangeIndex[motionEvent.x.toInt()]) ?: 0
                            ).y.toInt()
                        )
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (indexedFastScrollerFactory.popupEnable) {
                        if (bottomFastScrollerIndexViewPhoneBinding.popupIndex.isShown) {

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem.get(mapRangeIndex[motionEvent.x.toInt()]) ?: 0
                                ).y.toInt()
                            )

                            bottomFastScrollerIndexViewPhoneBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_out
                                )
                            )
                            bottomFastScrollerIndexViewPhoneBinding.popupIndex.visibility = View.INVISIBLE
                        }
                    } else {

                        nestedScrollView.smoothScrollTo(
                            0,
                            recyclerView.getChildAt(
                                mapIndexFirstItem.get(mapRangeIndex[motionEvent.x.toInt()]) ?: 0
                            ).y.toInt()
                        )
                    }
                }
            }

            true
        }
    }
}