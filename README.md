# Indexed Fast Scroller Library
<p align="center">
<a href="https://twitter.com/EliasFazel10296" rel="follow"><img src="https://img.shields.io/badge/Twitter-@EliasFazel10296-blue.svg?style=social&logo=twitter" alt="Twitter" data-canonical-src="https://img.shields.io/badge/Twitter-@EliasFazel10296-blue.svg?style=flat" style="max-width:100%;"></a>
</p>

# Android <br/>
A Library to Add Index with Popup Indicator in The Left - Right - Bottom of Recycler View.
Classes & Functions inside the Library has Clear Document & It is Easy to Implement with Options to Customize the UI.

~ Installation <br/>
Download This Project & Import IndexedFastScrollerLibrary Module to your Android Studio Project.

~ Implementation <br/>
(You Can Also Check The Sample Application To Understand How To Setup Nested RecyclerView) <br/>
Load Your List Data Completely & Then Add Below Code <br/>

⚠ Enable Coroutine. Add This To Your Application Level Gradle <br/>
```
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3' <br/>
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3' <br/>
```

<br/>

```
/*Indexed Popup Fast Scroller*/
val indexedFastScrollerFactory = IndexedFastScrollerFactory(
  indexData, 
  indexSide = IndexSide.BOTTOM, 
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
indexedFastScroller.setupIndex().await() 
/*Indexed Popup Fast Scroller*/ 
```

**Phone Demonstration FastScroller Popup Index** <br/>

![Phone Demonstration FastScroller Popup Index](https://github.com/EliasFazel10296/IndexedFastScrollerLibrary/blob/master/ScreenshotDemonstration/PhoneDemonstrationFastScrollerPopupIndex.jpg)

**Watch Demonstration FastScroller Popup Index** <br/>

![Watch Demonstration FastScroller Popup Index](https://github.com/EliasFazel10296/IndexedFastScrollerLibrary/blob/master/ScreenshotDemonstration/WatchDemonstrationFastScrollerPopupIndex.jpg)
