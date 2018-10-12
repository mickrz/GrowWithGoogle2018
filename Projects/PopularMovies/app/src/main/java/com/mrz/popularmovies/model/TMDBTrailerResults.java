package com.mrz.popularmovies.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TMDBTrailerResults implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TMDBTrailer> results = null;
    public final static Parcelable.Creator<TMDBTrailerResults> CREATOR = new Creator<TMDBTrailerResults>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TMDBTrailerResults createFromParcel(Parcel in) {
            return new TMDBTrailerResults(in);
        }

        public TMDBTrailerResults[] newArray(int size) {
            return (new TMDBTrailerResults[size]);
        }

    }
            ;

    protected TMDBTrailerResults(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, (TMDBTrailerResults.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public TMDBTrailerResults() {
    }

    /**
     *
     * @param id
     * @param results
     */
    public TMDBTrailerResults(Integer id, List<TMDBTrailer> results) {
        super();
        this.id = id;
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TMDBTrailer> getResults() {
        return results;
    }

    public int getSize() { return results.size(); }

    public void setResults(List<TMDBTrailer> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

}