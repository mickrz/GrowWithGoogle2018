###### Title: Lesson 3 - Connect to the Internet / Subject: Grow with Google Challenge Scholarship: Android Dev
###### Author: M. Rzucidlo / mfrstatements@yahoo.com / Web: https://github.com/mickrz/
---

### __ Grow with Google Challenge Scholarship: Android Dev __

** Note: Some images and text were copied from the class lessons. **

# Lesson 3 - Connect to the Internet

## Logging
* Levels:
  - WTF (What a terrible failure...?)
    - for errors that should never ever ever happen
    - should never, ever, ever use it
    - may force device to halt and output a debug report
    - may be best to use to impress your friends
  - ERROR
    - always preserved in release versions
  - WARN
    - always preserved in release versions
  - INFO
    - always preserved in release versions
  - DEBUG
  - VERBOSE
* standard form: `LOG.x(String TAG, String message);`
  - x = 'e' for ERROR, 'w' for WARN, 'i' for INFO, 'd' for DEBUG, 'v' for VEBOSE
  - TAG can be any string you want
    - common stategy is to use class name
    - easier to debug as all apps dump log messages into same bucket

## Resources
### What is the res Directory?
* The res directory put things such as
  - images
  - strings
  - layouts
* Included in every Android project like below.

![A](img/screen-shot-2016-11-18-at-12.12.40-pm.png)
  
* Inside of the res directory, are sub folder for the following types of resources. You may have a subset of these directories, depending on the types of resources you're using in your app  

#### Different Resource Directories
* Info can be found [here](https://developer.android.com/guide/topics/resources/providing-resources.html)

#### Some Common Resource Types
Name | What's Stored Here
--- | ---
values | XML files that contain simple values, such as string or integers
drawable | A bunch of visual files, including Bitmap file types and shapes. More information is [here](https://developer.android.com/guide/topics/resources/drawable-resource.html)
layouts	| XML layouts for your app

#### Other Resource Types
Name | What's stored here
--- | ---
animator | XML files for property animations
anim | XML files for tween animations
color | XML files that define state list colors
mipmap | Drawable files for launcher icons
menu | XML files that define application menus
raw	| Resource file for arbitrary files saved in their raw form. For example, you could put audio files here. (You might also be interested in the [assets](https://developer.android.com/reference/android/content/res/AssetManager.html) folder, depending on how you use that audio)
xml	| Arbitrary XML; if you have XML configuration files, this is a good place to put them

### Why Resources
* Always keep things like images and layouts separate in the **res** folder. 
* Keeping resource files and values independent helps easily maintain them if update is needed
* The Android Framework also easily allows for alternative resources that support specific device configurations such as different languages or screen sizes.
* Providing a customized experience for users from different locations or on different devices becomes increasingly important as more of the world comes online and more devices come on the market.

### Using Resources in XML and Java
* When we say `setContentView(R.layout.activity_main)`, we are referencing a resource (the `activity_main.xml`) file to use as the layout of `MainActivity`.
* That magical looking `R.layout` part of the expression above is actually a static class that is generated for us to reference resources in Java code.
* This is all described in the [Android Layouts Primer](https://classroom.udacity.com/courses/ud851/lessons/93affc67-3f0b-4f9b-b3a4-a7a26f241a86/concepts/cdbfd437-de24-4903-8f01-37c29427cb38).

#### Working with strings.xml
* In Java, you can get a String saved in **res -> values -> strings.xml** by calling the `getString` method. 
* If you’re in an Activity, you can just call `getString`, and pass in the String resource ID. 
* The String resource ID can be found in the **strings.xml** XML. For example, let's look at Sunshine's strings.xml file:
```
    <string name="today">Today</string>

    <!-- For labelling tomorrow's forecast [CHAR LIMIT=15] -->
    <string name="tomorrow">Tomorrow</string>

    <!-- Date format [CHAR LIMIT=NONE] -->
    <string name="format_full_friendly_date">
        <xliff:g id="month">%1$s</xliff:g>, <xliff:g id="day">%2$s</xliff:g>
    </string>
```

* The id of the String with the value "Today" is `today` and the id of the String with the value `<xliff:g id="month">%1$s</xliff:g>, <xliff:g id="day">%2$s</xliff:g>` is `format_full_friendly_date`

If you wanted to reference the **Today** string, you would reference it in Java by doing something like this:

```
String myString = getString(R.string.today);
```

In XML, you can access a String by using the @string accessor method. For the same String defined above, you could access it like this:

```
<TextView text=”@string/today” />
```

For more information on String Resources check out the [documentation](https://developer.android.com/guide/topics/resources/string-resource.html).

## On Menus
[Toast Reference #1](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwjE7f2soeDYAhVCRN8KHa1rAssQFggnMAA&url=https%3A%2F%2Fdeveloper.android.com%2Fguide%2Ftopics%2Fui%2Fnotifiers%2Ftoasts.html&usg=AOvVaw02eKwkhaf_PT35n2qqNIMt)

[Toast Reference #2](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=2&cad=rja&uact=8&ved=0ahUKEwjE7f2soeDYAhVCRN8KHa1rAssQFgguMAE&url=https%3A%2F%2Fdeveloper.android.com%2Freference%2Fandroid%2Fwidget%2FToast.html&usg=AOvVaw1i12CDhnJLgtyQJ4yETXZF)

[Toast Example](https://www.javatpoint.com/android-toast-example)

```
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSeleted = item.getItemId();
        if (menuItemThatWasSeleted == R.id.action_search) {
            Context context = MainActivity.this;
            String msg = "Search clicked";
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
```

## Build our Url
[Url class documentation](https://developer.android.com/reference/java/net/URL.html)

```
    final static String GITHUB_BASE_URL =
            "https://api.github.com/search/repositories";

    final static String PARAM_QUERY = "q";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";

    /**
     * Builds the URL used to query Github.
     *
     * @param githubSearchQuery The keyword that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String githubSearchQuery) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon().
                appendQueryParameter(PARAM_QUERY, githubSearchQuery).
                appendQueryParameter(PARAM_SORT, sortBy).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
```

## Fetching an HTTP Request
* In order to make an HTTP connection, call `url.openConnection();`
  - This doesn't talk to network yet
  - only creates HTTP connection object
  - at this point:
    - can set request fields
    - add header fields
    - change properties of the connection
* Next step is to get an input stream from the open connection `InputStream in = urlConnection.getInputStream();`
* Read contents of input stream
  - many ways to accomplish in Java
  - here uses scanner
    - used to tokenize streams
    - simple to use
    - relatively fast
    - using delimiter '\A' forces scanner to read entire contents of stream from beginning into next tokenized stream
      - buffer data, pulling data in small chunks
      - content size is not required so code needs to be able to handle differentsized buffers
      - code will handle alloc/dealloc of buffers as needed
      - handles character encoding
        UTF-8 (default for JSON/Javascript) to UTF-16 (default for Android)
* links for more info:
  - [Read/convert an InputStream to a String](https://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string)
  - [OkHttp](http://square.github.io/okhttp/)
  - [JSON example](https://stackoverflow.com/questions/42612426/how-to-get-values-from-json-string)

## Permissions
* When each APK is installed, given its own unique Linux user id
* Each app runs in its own instance of Android runtime
* completely sandboxed to protect against data leakage or affecting otehr apps
* permissions are declared in app's manifest
* [System Permissions](https://developer.android.com/guide/topics/permissions/index.html)
* Best practice is to request minimum amount of permissions
* Determine if a new permission is needed, is there another way to achieve the same goal
* While declaring a permission is required for using the camera, directly dialing the phone, and directly accessing the contact database - you can do each of these things by using a system app as an intermediator. Because the camera app, dialer, and contacts app will be used to provide the data you request - users have the chance to cancel the action you initiated, giving them runtime ability to refuse your app access. Your app can only access the user’s location if it explicitly declares a uses-permission.
* Example: add `<uses-permission android:name="android.permission.INTERNET" />` to AndroidManifest.xml

## Thread Basics
* Networking must not be on the main thread
  - run last on secondary execution thread
* After 5 seconds of waiting, Android will pop up a prompt to close the app

## AsyncTask
* Allows you to run a task on the background thread while publishing results to the UI thread
* UI thread has a msg queue and a handler that allows you to send as process runnable objects and msgs often from other threads
* Three types uses by AsyncTask:
  - Params: Parameter type sent to the task upon execution
  - Progress: Type published to update progress during the background computation
  - Result: The type of result of the background computation
* Those three parameters correspond to three primary functions that can be overridden in AsyncTask
  - doInBackground
  - onProgressUpdate
  - onPostExecute
  - onPreExecute
* To execute AsyncTask
  - call execute with the parameters to be set to the background task
  - then runs through several steps
    - calls onPreExecute on the UI thread (to do initialization)
    - calls doInBackground on another thread (for long running task)
      - only method that must be overridden to use the class
      - called with parameters you pass to the execution function
    - call publishProgess (as many times as needed) if you want to update some UI with the progress from long running task 
    - finally return the result when you're task running in doInBackground is complete
      - this causes onPostExecute to be called on the UI thread with the result returned
* AsyncTask Example:
```
    public class SomeTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL url = params[0];
            String results = null;
            try {
                results = NetworkUtils.getResponse(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {
            if (results != null && !results.equals("")) {
                doSomethingCool();
                mSomeTextView.setText(results);
            } else {
                showErrorMessage();
            }
        }
    }
```
* [Android Documentation](https://developer.android.com/reference/android/os/AsyncTask.html)

## Refresh Bad!
* progress bar is ok for debugging, bad for final product