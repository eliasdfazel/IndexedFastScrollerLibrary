/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/8/20 10:19 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.indexedpopupfastscrollersample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.geeksempire.indexedfastscroller.indexedpopupfastscrollersample.databinding.ActivitySampleViewsBinding

class SampleActivity : AppCompatActivity() {

    private lateinit var activitySampleViewsBinding: ActivitySampleViewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySampleViewsBinding = ActivitySampleViewsBinding.inflate(layoutInflater)
        setContentView(activitySampleViewsBinding.root)
    }
}
