package com.hugo.xdaily.entry;

import java.util.List;

/**
 * @auther Hugo
 * Created on 2016/6/2 9:59.
 */
public class LatestStory {

    private String date;

    private List<Stroy> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Stroy> getStories() {
        return stories;
    }

    public void setStories(List<Stroy> stories) {
        this.stories = stories;
    }

}
