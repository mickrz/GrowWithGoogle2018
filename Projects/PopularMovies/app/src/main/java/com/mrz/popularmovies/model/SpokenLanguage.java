package com.mrz.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpokenLanguage implements Parcelable
{

    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Parcelable.Creator<SpokenLanguage> CREATOR = new Creator<SpokenLanguage>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SpokenLanguage createFromParcel(Parcel in) {
            return new SpokenLanguage(in);
        }

        public SpokenLanguage[] newArray(int size) {
            return (new SpokenLanguage[size]);
        }

    }
            ;

    protected SpokenLanguage(Parcel in) {
        this.iso6391 = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public SpokenLanguage() {
    }

    /**
     *
     * @param iso6391
     * @param name
     */
    public SpokenLanguage(String iso6391, String name) {
        super();
        this.iso6391 = iso6391;
        this.name = name;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iso6391);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}