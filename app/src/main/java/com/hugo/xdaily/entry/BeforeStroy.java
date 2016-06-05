package com.hugo.xdaily.entry;

import java.util.List;

/**
 * @auther Hugo
 * Created on 2016/6/4 13:31.
 */
public class BeforeStroy {

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

    public static class StoriesBean {

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
