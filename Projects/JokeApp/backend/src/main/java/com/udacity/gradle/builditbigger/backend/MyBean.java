package com.udacity.gradle.builditbigger.backend;

import com.mrz.javajokelib.MyJokes;

/** The object model for the data we are sending through endpoints */
public class MyBean {

    private String myData;

    public String getData() {
        //return myData;
        return new MyJokes().pickOne();
    }

    public void setData(String data) {
        myData = data;
    }
}