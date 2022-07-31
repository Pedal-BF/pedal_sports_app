package com.bicontest.pedal_sports_app.main_pages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bicontest.pedal_sports_app.MainActivity;
import com.bicontest.pedal_sports_app.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    View v;
    private ImageView adSlideImage;  // 슬라이드 광고 부분 이미지
    Bitmap bitmap, bitmap2;

    private ImageView recommedImage; // 추천 영상 이미지

    private String[] youtubeUrls = {
            "https://youtu.be/6AiSi3E3ifs",
            "https://youtu.be/2gHcoelhHw0",
            "https://youtu.be/CRMpWponGIc",
            "https://youtu.be/6ulvd_mw_uo",
            "https://youtu.be/6ies7bJfYRs"
    };
    private String excerciseData;

    private RecyclerView mRecyclerView;
    private ArrayList<RecyclerViewItem> mList;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // fragment에서 findViewById를 하기 위한 세팅
        v = inflater.inflate(R.layout.fragment_main, container, false);

        adSlideImage = v.findViewById(R.id.ad_slide);

        new Thread(new Runnable() {
            @Override
            public void run() {
                excerciseData = getExcerciseInfo();
                Log.println(Log.DEBUG,"Test", "----------------------------------------------------------------");
                Log.println(Log.DEBUG,"Data", excerciseData);
            }
        }).start();

        advertiseImageSet();

        // 이미지 클릭 시 상세 영상 페이지로 이동
        adSlideImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getActivity()로 MainActivity의 replaceFragment를 불러옴
                // 새로 불러올 Fragment의 Instance를 Main으로 전달
                ((MainActivity)getActivity()).replaceFragment(VideoViewFragment.newInstance());
                return true;
            }
        });

        // 수평 리스트
        firstInit();

        // 리스트에 youtube Url, 제목 정보 전달
        for(int i = 0; i < 5; i++){
            addItem(youtubeUrls[i], "Test");
        }

        mRecyclerViewAdapter = new RecyclerViewAdapter(mList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // 수직 리스트
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false)); // 수평 리스트

        return v;
    }

    // 운동 API에서 데이터 가져오기 = Parsing
    public String getExcerciseInfo() {
        StringBuffer buffer = new StringBuffer();

        String serviceKey = "7VgAbrUNHG0BQOPUubAEEkOT45PoaRK6TR92eLuGBsfqyhspb%2BY1oOyrwqIeWXYGrSVw9vMreaGpnekwpR8pGw%3D%3D";
        String limitPage = "10"; // 최대 248

        String queryUrl = "https://api.odcloud.kr/api/15084814/v1/uddi:3f8d6b98-0082-4792-92a8-90d40ecc4bce"
                + "?page=1&perPage=" + limitPage + "&serviceKey=" + serviceKey;

        try {
            URL url = new URL(queryUrl);        // 문자열로 된 url을 URL 객체로 생성
            InputStream is = url.openStream();  // url 위치로 입력 스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));  // inputstream으로부터 xml 입력 받기

            String tag = xpp.getName();
            /*while(!tag.equals("data")) {
                xpp.next();
                tag = xpp.getName();
            }*/

            //xpp.next();
            buffer.append(tag);
            Log.println(Log.DEBUG,"Test", "----------------------------------------------------------------");
            Log.println(Log.DEBUG,"Tag", "test");

       } catch (Exception e) {
            // TODO Auto-generated catch block e.printStackTrace();
            e.printStackTrace();
            Log.println(Log.DEBUG,"Test", "----------------------------------------------------------------");
            Log.println(Log.DEBUG,"Error", "API ERROR");
        }

        return buffer.toString();  // StringBuffer 문자열 객체 반환
    }

    // 영상 수평 리스트에 필요한 부분
    public void firstInit(){
        mRecyclerView = (RecyclerView) v.findViewById(R.id.thumbnail_recyclerview);
        mList = new ArrayList<>();
    }

    // 수평 리스트에 유튜브 썸네일, 제목 추가
    public void addItem(String imgURL, String mainText){
        RecyclerViewItem item = new RecyclerViewItem();

        item.setImgURL(imgURL);
        item.setMainText(mainText);
        //item.setSubText(subText);

        mList.add(item);
    }
    
    // 유튜브 링크에서 key 값 추출
    public String youtubeKeyParse(String youtubeUrl) {
        String[] splited = youtubeUrl.split("/"); // splited[3]에 youtube key? 들어있음

        return splited[3];
    }

    // 광고 리스트
    public void advertiseImageSet() {
        Thread mTread = new Thread() {
            @Override
            public void run() {
                try {
                    //Log.println(Log.DEBUG,"debug", "----------------------------------------------------------------");
                    // 유튜브 영상 url을 썸네일 url로 변환
                    String youtubeThumbnail = "https://img.youtube.com/vi/" + youtubeKeyParse(youtubeUrls[0]) + "/sddefault.jpg";
                    URL url = new URL(youtubeThumbnail);

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
    }
}