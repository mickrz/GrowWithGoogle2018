package com.mrz.popularmovies.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TMDBReviewResults implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<TMDBReview> results = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    public final static Parcelable.Creator<TMDBReview> CREATOR = new Creator<TMDBReview>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TMDBReview createFromParcel(Parcel in) {
            return new TMDBReview(in);
        }

        public TMDBReview[] newArray(int size) {
            return (new TMDBReview[size]);
        }

    }
            ;

    protected TMDBReviewResults(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, (com.mrz.popularmovies.model.TMDBReview.class.getClassLoader()));
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalResults = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public TMDBReviewResults() {
    }

    /**
     *
     * @param id
     * @param results
     * @param totalResults
     * @param page
     * @param totalPages
     */
    public TMDBReviewResults(Integer id, Integer page, List<TMDBReview> results, Integer totalPages, Integer totalResults) {
        super();
        this.id = id;
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public int getSize() { return results.size(); }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<TMDBReview> getResults() {
        return results;
    }

    public void setResults(List<TMDBReview> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(page);
        dest.writeList(results);
        dest.writeValue(totalPages);
        dest.writeValue(totalResults);
    }

    public int describeContents() {
        return 0;
    }

}