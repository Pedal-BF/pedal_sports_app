package com.bicontest.pedal_sports_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainFragment extends Fragment {

    private ImageView adSlideImage;  // 슬라이드 광고 부분 이미지
    Bitmap bitmap, bitmap2;

    private ImageView recommedImage; // 추천 영상 이미지

    private String[] imagesUrl = new String[] {
            "https://img.youtube.com/vi/MfZf-hV9mPw/sddefault.jpg",
            "https://img.youtube.com/vi/auU6iQ5v_bU/sddefault.jpg",
            "https://img.youtube.com/vi/rsaERcPAKWQ/sddefault.jpg",
            "https://img.youtube.com/vi/XnXaUt2TvPo/sddefault.jpg",
            "https://img.youtube.com/vi/_ka09IuBOf4/sddefault.jpg"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // fragment에서 findViewById를 하기 위한 세팅
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        adSlideImage = v.findViewById(R.id.ad_slide);
        recommedImage = v.findViewById(R.id.recommed_image);

        //https://youtu.be/MfZf-hV9mPw
        //String youtubeThumbnail= "https://img.youtube.com/vi/MfZf-hV9mPw/default.jpg";
        Thread mTread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://img.youtube.com/vi/MfZf-hV9mPw/sddefault.jpg");

                    // Youtube에서 이미지를 가져오고 ImageView에 저장할 Bitmap 생성
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setDoInput(true); // 서버로부터 응답 수신
                    conn.connect();

                    InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mTread.start();

        try {
            // join()을 호출하여, 별도의 작업 Thread가 작업을 완료할 때까지 메인 Thread가 기다리게 함
            mTread.join();

            // 작업 Thread에서 이미지 불러오는 작업을 완료한 뒤, 메인 Thread에서 ImageView에 이미지 지정
            adSlideImage.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recommendImageSet();

        return v;
    }
    public void recommendImageSet() {
        Thread mTread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imagesUrl[1]);

                    // Youtube에서 이미지를 가져오고 ImageView에 저장할 Bitmap 생성
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setDoInput(true); // 서버로부터 응답 수신
                    conn.connect();

                    InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                    bitmap2 = BitmapFactory.decodeStream(is); // Bitmap으로 변환
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mTread.start();

        try {
            // join()을 호출하여, 별도의 작업 Thread가 작업을 완료할 때까지 메인 Thread가 기다리게 함
            mTread.join();

            // 작업 Thread에서 이미지 불러오는 작업을 완료한 뒤, 메인 Thread에서 ImageView에 이미지 지정
            recommedImage.setImageBitmap(bitmap2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}