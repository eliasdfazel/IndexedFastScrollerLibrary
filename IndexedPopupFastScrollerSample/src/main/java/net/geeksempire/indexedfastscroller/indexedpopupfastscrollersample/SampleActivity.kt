/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/9/20 12:30 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.indexedpopupfastscrollersample

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.geeksempire.indexedfastscroller.indexedpopupfastscrollersample.databinding.ActivitySampleViewsBinding
import net.geeksempire.indexedfastscroller.library.Factory.IndexSide
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactory
import net.geeksempire.indexedfastscroller.library.IndexedFastScroller
import java.util.*
import kotlin.collections.ArrayList

class SampleActivity : AppCompatActivity() {

    private lateinit var activitySampleViewsBinding: ActivitySampleViewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySampleViewsBinding = ActivitySampleViewsBinding.inflate(layoutInflater)
        setContentView(activitySampleViewsBinding.root)

        val layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        layoutManager.isSmoothScrollbarEnabled = true
        activitySampleViewsBinding.recyclerViewList.layoutManager = layoutManager

        setupAdapterData()
    }

    private fun setupAdapterData() = CoroutineScope(Dispatchers.Main).launch {

        val indexData = ArrayList<String>()
        val adapterItemData = ArrayList<String>()

        val applicationData = packageManager.getInstalledApplications(PackageManager.GET_META_DATA).slice(
            IntRange(0, packageManager.getInstalledApplications(PackageManager.GET_META_DATA).size/2)
        )
        Collections.sort(applicationData, ApplicationInfo.DisplayNameComparator(packageManager))

        applicationData.forEach {

            packageManager.getLaunchIntentForPackage(it.packageName)?.let { intent ->

                val appName = packageManager.getApplicationLabel(it).toString()

                adapterItemData.add(appName)

                indexData.add(appName[0].toString())
            }
        }

        withContext(Dispatchers.Main) {

            val sampleRecyclerViewAdapter = SampleRecyclerViewAdapter(applicationContext, adapterItemData)

            activitySampleViewsBinding.recyclerViewList.adapter = sampleRecyclerViewAdapter
            sampleRecyclerViewAdapter.notifyDataSetChanged()
        }

        /*Indexed Popup Fast Scroller*/
        val indexedFastScrollerFactory = IndexedFastScrollerFactory(
            indexSide = IndexSide.RIGHT,
            popupEnable = true
        )
        val indexedFastScroller: IndexedFastScroller = IndexedFastScroller(
            context = applicationContext,
            layoutInflater = layoutInflater,
            rootView = activitySampleViewsBinding.MainView,
            nestedScrollView = activitySampleViewsBinding.nestedScrollView,
            recyclerView = activitySampleViewsBinding.recyclerViewList,
            indexedFastScrollerFactory = indexedFastScrollerFactory
        )
        indexedFastScroller.initializeIndexView().await()
            .loadIndexData(listOfNewCharOfItemsForIndex = indexData).await()
        /*Indexed Popup Fast Scroller*/
    }
}
