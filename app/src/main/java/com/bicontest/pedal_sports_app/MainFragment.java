package com.bicontest.pedal_sports_app;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

public class MainFragment extends Fragment {
    // 서울올림픽기념국민체육진흥공단_국민체력100_운동처방_동영상정보
    private String SeoulExerciseKey = "7VgAbrUNHG0BQOPUubAEEkOT45PoaRK6TR92eLuGBsfqyhspb%2BY1oOyrwqIeWXYGrSVw9vMreaGpnekwpR8pGw%3D%3D";   // 인증키 (개인이 받아와야함)
    private String SeoulExerciseLimitPage = "248";  // 248
    private String SeoulExerciseJson;        // 파싱한 데이터를 저장할 변수
    private JSONObject SeoulJSONObject;
    private JSONArray SeoulJSONArray;

    private ImageView youtubeImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // fragment에서 findViewById를 하기 위한 세팅
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //https://youtu.be/MfZf-hV9mPw
        String youtubeThumbnail = "https://img.youtube.com/vi/MfZf-hV9mPw/default.jpg";
        youtubeImage = view.findViewById(R.id.adImage);
        try {
            Uri uri = Uri.parse(youtubeThumbnail);
            youtubeImage.setImageURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        // 서울올림픽기념국민체육진흥공단_국민체력100_운동처방_동영상정보 API에서 JSON 형태로 데이터 받아오기
        try {
            URL url = new URL("https://api.odcloud.kr/api/15084814/v1/uddi:3f8d6b98-0082-4792-92a8-90d40ecc4bce?page=1&perPage=" + SeoulExerciseLimitPage +"&serviceKey=" + SeoulExerciseKey );

            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            SeoulExerciseJson = bf.readLine();
            bf.close();

            SeoulJSONObject = new JSONObject(SeoulExerciseJson);
            String contents = SeoulJSONObject.getString("data");
            SeoulJSONArray = new JSONArray(contents);

            Log.v("API", "log test");

        }catch(Exception e) {
            e.printStackTrace();
        }*/

        return view;
    }
}