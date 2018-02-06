###### Title: Lesson 4 - Recyclerview / Subject: Grow with Google Challenge Scholarship: Android Dev
###### Author: M. Rzucidlo / mfrstatements@yahoo.com / Web: https://github.com/mickrz/
---

### __ Grow with Google Challenge Scholarship: Android Dev __

** Note: Some images and text were copied from the class lessons. **

** Note: [Required for labs](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration.html) **

** Note: In order to get the labs to compile based on the dependency version of RecyclerView to compile without any issues, make the versions match shown below. The other simpler option is to just change the version of Recycler to match `com.android.support:appcompat___` **

```
android {
    compileSdkVersion 27 <---
    //buildToolsVersion "25.0.2" <---

    defaultConfig {
        applicationId "com.example.android.recyclerview"
        minSdkVersion 14 <---
        targetSdkVersion 27 <---
        versionCode 1
        versionName "1.0"
    }
    buildTypes {
        release {
            minifyEnabled false
        }
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:27.0.2' <---

    // TODO (1) Add RecyclerView dependency
    compile 'com.android.support:recyclerview-v7:27.0.2' <---
}
```
 

# Lesson 4 - Recyclerview

## Introduction

## Why RecyclerView?
* So far load data in a single scrolling text view
* Doesn't allow for the formatting needed for the final version of the app
* main user interface contains a list of mostly similar items
* RecyclerView is lists made of a series of views for each image and text block
* Allows to make a rich experience containing different font sizes and images
* Although only a region of list is viewable, code has to create and lay out  and populate all of the off-screen views also
* Any images that are displayed have to be loaded into memory
* reasonable for small lists with minimal formatting, but large lists may have the app run out of memory and impractical
* For each item in list, each interaction would have to be handled by assigning click handlers
* The setup time would impair the user experience

## How RecyclerView Works
* Instead of creating items on-the-fly when scrolling and causing glitches and performance problems, some items are kept in a queue or recycling bin for re-use
* When scrolling, RecyclerView returns one of hte previously created items
* Code then binds the item with the new content, then it can be scrolled in
*  Views that are scrolled out are then put back on to the queue for re-use

## Using RecyclerView
* Almost every part of RecyclerView is completely modular
* RecyclerView has an adapter that is used to provide the RecyclerView with the new views when needed
* Adapter is used to bind new data from the data source to the views
* Adapter sends the views to a RecyclerView in an object called a ViewHolder
* ViewHolder contains a reference to the root view object for the item
* It's expected to use it to cache the view objects represented in layout to make it less costly to update the views with the new data
* This way, `FindAViewId()` gets called only for each data view whenever that new item is created and not each time you want to populate the views in the item with data
* Layout Manager then tells the RecyclerView how to layout all the views

## Why ViewHolders?
* It's important to use view holders to cache view objects that will be populated with data or images.
* When RecyclerView is first being populated, call `findViewById()` for each view that will be showing data from the adapter.

## Quiz
* Let’s say that each item in your RecyclerView list contains four individual data views, and you don't cache these views in a ViewHolder. If eight items fit on screen, approximately how many extra findViewById() calls will be made if you scroll through 30 items?
* In addition to the eight items that fit on screen, assume that two extra items are needed for smooth scrolling.
  - With only eight items on screen, we would need at least eight item views, but the question says that we need two extras for smooth scrolling, which means 10 items total. 10 items times 4 individual data views per item means 40 calls to FindViewById. And we could cache these views in a ViewHolder to fill our RecyclerView and then access them later when we scroll and recycle views. If we didn’t use a ViewHolder, we would have to call FindViewById 4 times for each of the 30 items we scroll through, that’s 30x4 or 120 calls to FindViewById.
  - So the difference between using a ViewHolder and not using one is 120-40 calls to FindViewById. 80 extra calls for not using a ViewHolder! Android phones today are so fast, that you probably wouldn't notice this optimization, but it will give you slightly better battery usage, which could be noticeable if you are scrolling through very large lists. So it’s best to use a ViewHolder.

## Items and ViewHolders
* First step is to add dependency and adding recyclerview to the layout
* Next step is to craft items that will be displayed in it and create a viewholder to use to store references to that item
* items can be displayed horizontally or vertically
* px = dp * ( dpi / 160 )
* SP worked like DP but also scaled based upon user preferences, typically text size preferences
* To scale text to scale up with preference which is important for accessibility, make sure to use SP for fonts and wudgets that have hard-coded sizes that contain fonts

## RecyclerView and Adapters
* With item layout and ViewHolder for RecyclerView, time to hook up to a data source using an adapter
* Adapter called by RecyclerView to
  - create new items in form of ViewHolders
  - populates or binds items with data
  - returns information about the data
    - how many items there are in a given data source
    - where data came from
      - ArrayList
      - JSON result of a network request
      - any other data source modelled
* Adapter requires three functions to be overridden
  - onCreateViewHolder
    - called when RecyclerView instantiates new ViewHolder instance
  - onBindViewHolder
    - called when RecyclerView wants to populate the view with data from the model so user can see it (effectively binding it to the data source)
  - getItemCount
    - returns the number of items in data source
* Sequence of calls
  - What number of items to display?
    - may be asked multiple times during layout process
    - must be fast operation
  - Create ViewHolder objects
    - in process, inflate individual item views to their corresponding XML or create in code
    - As many ViewHolders necessary will be created to display whichever option requires fewer ViewHolders
      - all of the items 
      - or fill and scroll the screen
    - responsible for creating the views
  - After each ViewHolder is created, the RecyclerView calls onBindViewHolder to populate each item with data
  - When scrolling, the RecyclerView will reuse the ViewHolders asking the adapter to bind new data to them
* The adapter does create ViewHolder objects and inflates item views in its onCreateViewHolder function, it also returns the number of items in the data source, and binds data from the data source to each item (even if we pass the responsibility to the ViewHolder). It doesn't cache views associated with each item (that's the job of the ViewHolder class) nor does it recycle them; that’s what our RecyclerView does.

## RecyclerView Layout Manager
* last required element to implement to wire together the RecyclerView is LayoutManager
* Whereas a ViewHolder determines how an individual entry is displayed, LayoutManager determines how the collection of items is displayed
* LayoutManager is key part of the way recycling works in RecyclerView, since it determines when o recycle views that are no longer visible to the user
* Android currently comes with three implementations of LayoutManager to fit most needs
  - LayoutLinearManager - allows items to scroll vertically (default) or horizontally
  - GridLayoutManager
    - subclass of LayoutLinearManager
    -  arranges items in a grid that can also scroll horizontally or vertically
  - StaggeredGridLayoutManager
    - displays an offset of grid items
    - used for applications that have views of varying dimensions
  - if none of these satisfy your needs, you can extend directly from LayoutManager to create your own


## Misc. Lab Notes:
* Add this to create a RecyclerView layout
```
    <android.support.v7.app.AlertController.RecycleListView
        android:id="@+id/rv_some_name"
        android:layout_width="match_parent"
        android:layout_height="match_parent"></android.support.v7.app.AlertController.RecycleListView>
```


## Other Students' posts
* [Barebones example](https://github.com/AlexisGarcia1/recycler-view-android/tree/master/java/a2room/recyclerview)
* [Understanding RecyclerView](https://discussions.udacity.com/t/understanding-recyclerview/552739/3)
* [Lesson 4 (RecyclerView) notes](https://discussions.udacity.com/t/lesson-4-recycler-view-notes/533863/4)