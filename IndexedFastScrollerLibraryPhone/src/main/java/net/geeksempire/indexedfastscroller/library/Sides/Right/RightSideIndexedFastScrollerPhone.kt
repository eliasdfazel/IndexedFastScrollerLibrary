/*
 * Copyright © 2021 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 3/25/21 9:31 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Sides.Right

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
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactoryPhone
import net.geeksempire.indexedfastscroller.library.Factory.calculateNavigationBarHeight
import net.geeksempire.indexedfastscroller.library.Factory.calculateStatusBarHeight
import net.geeksempire.indexedfastscroller.library.Factory.convertToDp
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Right.Extensions.setupRightIndex
import net.geeksempire.indexedfastscroller.library.databinding.RightFastScrollerIndexViewPhoneBinding
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
 * @param indexedFastScrollerFactoryPhone Change Default Value Or Just Pass IndexedFastScrollerFactory()
 **/
class RightSideIndexedFastScrollerPhone(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val rootView: ViewGroup,
    private val nestedScrollView: ScrollView,
    private val recyclerView: RecyclerView,
    private val indexedFastScrollerFactoryPhone: IndexedFastScrollerFactoryPhone) {

    private val rightFastScrollerIndexViewPhoneBinding: RightFastScrollerIndexViewPhoneBinding = RightFastScrollerIndexViewPhoneBinding.inflate(layoutInflater)

    private var statusBarHeight = calculateStatusBarHeight(context.resources)
    private var navigationBarBarHeight = calculateNavigationBarHeight(context.resources)

    private val finalPopupVerticalOffset: Int =
        indexedFastScrollerFactoryPhone.popupVerticalOffset.convertToDp(context)

    private val finalPopupHorizontalOffset: Int =
        indexedFastScrollerFactoryPhone.popupHorizontalOffset.convertToDp(context)

    init {
        Log.d(this@RightSideIndexedFastScrollerPhone.javaClass.simpleName, "*** Indexed Fast Scroller Initialized ***")
    }

    fun initializeIndexView(): Deferred<RightSideIndexedFastScrollerPhone> = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

        rightFastScrollerIndexViewPhoneBinding.indexView.removeAllViews()

        setupRightIndex(
            context,
            rootView,
            rightFastScrollerIndexViewPhoneBinding,
            indexedFastScrollerFactoryPhone,
            finalPopupHorizontalOffset
        ).loadIndexData(indexedFastScrollerFactoryPhone.listOfNewCharOfItemsForIndex).await()

        this@RightSideIndexedFastScrollerPhone
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

        var sideIndexItem = layoutInflater.inflate(R.layout.right_fast_scroller_side_index_item_phone, null) as TextView

        mapIndexFirstItem.keys.forEach { indexText ->
            sideIndexItem = layoutInflater.inflate(R.layout.right_fast_scroller_side_index_item_phone, null) as TextView
            sideIndexItem.text = indexText.toUpperCase(Locale.getDefault())

            sideIndexItem.typeface = indexedFastScrollerFactoryPhone.indexItemFont
            sideIndexItem.setTextColor(indexedFastScrollerFactoryPhone.indexItemTextColor)
            sideIndexItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, indexedFastScrollerFactoryPhone.indexItemSize)

            rightFastScrollerIndexViewPhoneBinding.indexView.addView(sideIndexItem)
        }

        val finalTextView = sideIndexItem

        /* *** */
        delay(777)
        /* *** */

        var upperRange = (rightFastScrollerIndexViewPhoneBinding.indexView.y - finalTextView.height).toInt()

        for (number in 0 until rightFastScrollerIndexViewPhoneBinding.indexView.childCount) {
            val indexText = (rightFastScrollerIndexViewPhoneBinding.indexView.getChildAt(number) as TextView).text.toString()
            val indexRange = (rightFastScrollerIndexViewPhoneBinding.indexView.getChildAt(number).y + rightFastScrollerIndexViewPhoneBinding.indexView.y + finalTextView.height).toInt()

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

        rightFastScrollerIndexViewPhoneBinding.nestedIndexScrollView.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                android.R.anim.fade_in
            )
        )
        rightFastScrollerIndexViewPhoneBinding.nestedIndexScrollView.visibility = View.VISIBLE

        val popupIndexOffsetY = (
                statusBarHeight
                        + navigationBarBarHeight
                        + finalPopupVerticalOffset).toFloat()

        rightFastScrollerIndexViewPhoneBinding.nestedIndexScrollView.setOnTouchListener { view, motionEvent ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (indexedFastScrollerFactoryPhone.popupEnable) {
                        val indexText = mapRangeIndex[motionEvent.y.toInt()]

                        if (indexText != null) {

                            rightFastScrollerIndexViewPhoneBinding.popupIndex.y = motionEvent.rawY - popupIndexOffsetY

                            rightFastScrollerIndexViewPhoneBinding.popupIndex.text = indexText
                            rightFastScrollerIndexViewPhoneBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_in
                                )
                            )
                            rightFastScrollerIndexViewPhoneBinding.popupIndex.visibility = View.VISIBLE
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (indexedFastScrollerFactoryPhone.popupEnable) {
                        val indexText = mapRangeIndex[motionEvent.y.toInt()]

                        if (indexText != null) {
                            if (!rightFastScrollerIndexViewPhoneBinding.popupIndex.isShown) {
                                rightFastScrollerIndexViewPhoneBinding.popupIndex.startAnimation(
                                    AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                                )
                                rightFastScrollerIndexViewPhoneBinding.popupIndex.visibility = View.VISIBLE
                            }

                            rightFastScrollerIndexViewPhoneBinding.popupIndex.y = motionEvent.rawY - popupIndexOffsetY

                            rightFastScrollerIndexViewPhoneBinding.popupIndex.text = indexText

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem[mapRangeIndex[motionEvent.y.toInt()]] ?: 0
                                ).y.toInt()
                            )

                        } else {
                            if (rightFastScrollerIndexViewPhoneBinding.popupIndex.isShown) {
                                rightFastScrollerIndexViewPhoneBinding.popupIndex.startAnimation(
                                    AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
                                )
                                rightFastScrollerIndexViewPhoneBinding.popupIndex.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (indexedFastScrollerFactoryPhone.popupEnable) {
                        if (rightFastScrollerIndexViewPhoneBinding.popupIndex.isShown) {

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem.get(mapRangeIndex[motionEvent.y.toInt()]) ?: 0
                                ).y.toInt()
                            )

                            rightFastScrollerIndexViewPhoneBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_out
                                )
                            )
                            rightFastScrollerIndexViewPhoneBinding.popupIndex.visibility = View.INVISIBLE
                        }
                    } else {

                        nestedScrollView.smoothScrollTo(
                            0,
                            recyclerView.getChildAt(
                                mapIndexFirstItem.get(mapRangeIndex[motionEvent.y.toInt()]) ?: 0
                            ).y.toInt()
                        )
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (indexedFastScrollerFactoryPhone.popupEnable) {
                        if (rightFastScrollerIndexViewPhoneBinding.popupIndex.isShown) {

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem.get(mapRangeIndex[motionEvent.y.toInt()]) ?: 0
                                ).y.toInt()
                            )

                            rightFastScrollerIndexViewPhoneBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_out
                                )
                            )
                            rightFastScrollerIndexViewPhoneBinding.popupIndex.visibility = View.INVISIBLE
                        }
                    } else {

                        nestedScrollView.smoothScrollTo(
                            0,
                            recyclerView.getChildAt(
                                mapIndexFirstItem.get(mapRangeIndex[motionEvent.y.toInt()]) ?: 0
                            ).y.toInt()
                        )
                    }
                }
            }

            true
        }
    }
}