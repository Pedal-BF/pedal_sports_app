package com.bicontest.pedal_sports_app.main_pages;

public class ExerciseInfo {
    public String mYoutubeURL;      // 유튜브 링크
    public String mYoutubeTitle;    // 유튜브 제목
    //public String mLargeCategory;   // 대분류
    //public String mMiddleCategory;  // 중분류
    //public String mSubCategory;     // 소분류

    /*public ExerciseInfo(String LargeCategory, String MiddleCategory, String SubCategory, String YoutubeTitle, String YoutubeURL) {
        this.mLargeCategory = LargeCategory;
        this.mMiddleCategory = MiddleCategory;
        this.mSubCategory = SubCategory;
        this.mYoutubeTitle = YoutubeTitle;
        this.mYoutubeURL = YoutubeURL;
    }*/
    public String getYoutubeURL() {
        return mYoutubeURL;
    }

    public void setYoutubeURL(String youtubeURL) {
        this.mYoutubeURL = youtubeURL;
    }

    public String getYoutubeTitle() {
        return mYoutubeTitle;
    }

    public void setYoutubeTitle(String youtubeTitle) {
        this.mYoutubeTitle = youtubeTitle;
    }
}