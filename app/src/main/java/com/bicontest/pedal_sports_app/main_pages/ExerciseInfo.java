package com.bicontest.pedal_sports_app.main_pages;

public class ExerciseInfo {
    public String YoutubeURL;      // 유튜브 링크
    public String YoutubeTitle;    // 유튜브 제목
    public String LargeCategory;   // 대분류
    public String MiddleCategory;  // 중분류
    public String SubCategory;     // 소분류

    public ExerciseInfo(String LargeCategory, String MiddleCategory, String SubCategory, String YoutubeTitle, String YoutubeURL) {
        this.LargeCategory = LargeCategory;
        this.MiddleCategory = MiddleCategory;
        this.SubCategory = SubCategory;
        this.YoutubeTitle = YoutubeTitle;
        this.YoutubeURL = YoutubeURL;
    }

    /*public ExerciseInfo(String youtubeURL, String youtubeTitle) {
        this.mYoutubeURL = youtubeURL;
        this.mYoutubeURL = youtubeTitle;
    }*/

    public String getYoutubeURL() {
        return YoutubeURL;
    }

    public void setYoutubeURL(String YoutubeURL) {
        this.YoutubeURL = YoutubeURL;
    }

    public String getYoutubeTitle() {
        return YoutubeTitle;
    }

    public void setYoutubeTitle(String YoutubeTitle) {
        this.YoutubeTitle = YoutubeTitle;
    }
}