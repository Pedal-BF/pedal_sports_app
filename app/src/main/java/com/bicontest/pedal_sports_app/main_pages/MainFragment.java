package com.bicontest.pedal_sports_app.main_pages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bicontest.pedal_sports_app.MainActivity;
import com.bicontest.pedal_sports_app.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    //https://github.com/sayyam/carouselview

    // 상수 선언
    private final int allExerciseNum = 247; // 총 영상 갯수
    private final int advertiseNum = 5;     // 광고 영상 갯수
    private final int recommendNum = 7;     // 추천 영상 갯수

    private View v;

    // 슬라이드 광고 부분 이미지
    private ImageView adSlideImage;
    private Bitmap bitmap;

    // private ImageView recommedImage; // 추천 영상 이미지

    // 운동 정보를 저장해둘 클래스
    public ExerciseInfo[] mExerciseInfo = new ExerciseInfo[allExerciseNum];

    private String[] youtubeUrls = {
            "https://youtu.be/6AiSi3E3ifs",
            "https://youtu.be/2gHcoelhHw0",
            "https://youtu.be/CRMpWponGIc",
            "https://youtu.be/6ulvd_mw_uo",
            "https://youtu.be/6ies7bJfYRs"
    };

    // 파이어베이스 DB 접근
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    
    // 영상 목록 수평 리스트
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

        //setExerciseData(); // API에서 운동 데이터 받아서 파이어베이스에 저장 - 저장해둠
        //setAdvertiseVideo(); // 광고로 보여줄 영상 index 저장 - 저장해둠

        // 파이어베이스에서 운동 데이터 받아오기
        getExerciseThread.start();

        // 데이터 가져오는 동안 기다리기
        try {
            getExerciseThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Log.println(Log.DEBUG, "debug", "----------------------------------------------------------------");
        //Log.println(Log.DEBUG, "Data", mExerciseInfo[0].getYoutubeURL() + " " + mExerciseInfo[0].getYoutubeTitle());

        adSlideImage = v.findViewById(R.id.ad_slide);

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

        setRecommendVideo(); // 추천 영상 정보 세팅

        // 리스트에 youtube Url, 제목 정보 전달
        /*for(int i = 0; i < 5; i++){
            addItem(youtubeUrls[i], "Test");
        }*/

        mRecyclerViewAdapter = new RecyclerViewAdapter(mList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // 수직 리스트
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false)); // 수평 리스트

        return v;
    }

    // 파이어베이스에서 데이터 가져와서 클래스에 저장
    Thread getExerciseThread = new Thread(new Runnable() {
        @Override
        public void run() {
            database = FirebaseDatabase.getInstance();                         // 데이터베이스 선언, 할당
            myRef = database.getReference("Pedal").child("ExerciseData");

            myRef.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(int i = 0; i < allExerciseNum; i++) {
                                String mLargeCategory = snapshot.child(Integer.toString(i)).child("LargeCategory").getValue().toString();
                                String mMiddleCategory = snapshot.child(Integer.toString(i)).child("MiddleCategory").getValue().toString();
                                String mSubCategory = snapshot.child(Integer.toString(i)).child("SubCategory").getValue().toString();
                                String mYoutubeTitle = snapshot.child(Integer.toString(i)).child("YoutubeTitle").getValue().toString();
                                String mYoutubeURL = snapshot.child(Integer.toString(i)).child("YoutubeURL").getValue().toString();

                                mExerciseInfo[i] = new ExerciseInfo(mLargeCategory, mMiddleCategory, mSubCategory, mYoutubeTitle, mYoutubeURL);
                                //Log.println(Log.DEBUG, "debug", "----------------------------------------------------------------");
                                //Log.println(Log.DEBUG, "Data", mExerciseInfo[i].YoutubeURL + " " + mExerciseInfo[i].YoutubeTitle);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
            );
        }
    });

    // 파이어베이스에서 데이터 가져와서 메인페이지에 보여주기
    public void setRecommendVideo() {
        Thread getRecommendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 파이어베이스에서 데이터 가져오는 스레드가 종료될 때까지 기다리기
                try {
                    getExerciseThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.println(Log.DEBUG, "debug", "----------------------------------------------------------------");
                Log.println(Log.DEBUG, "Data", "Check Recommend Thread Start!!!");

                database = FirebaseDatabase.getInstance();         // 데이터베이스 선언, 할당
                myRef = database.getReference("Pedal").child("ShowExerciseIndex").child("RecommendIndex");

                int[] recommendIndexs = new int[recommendNum];

                // 추천 영상 index 저장해둔 곳에서 index들 받아오기
                myRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Log.println(Log.DEBUG,"debug", "----------------------------------------------------------------");
                                //Log.println(Log.DEBUG,"debug",  snapshot.getValue().toString());
                                for(int i = 0; i < recommendNum; i++) {
                                    recommendIndexs[i] = Integer.parseInt(snapshot.child(Integer.toString(i)).getValue().toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );
                //Log.println(Log.DEBUG, "debug", "----------------------------------------------------------------");
                //Log.println(Log.DEBUG, "Data", mExerciseInfo[0]);
            }
        });
        getRecommendThread.start();

        // 데이터 가져오는 동안 로딩화면 뜨도록 -> 안 해주면 빈 화면이 될 수도
        /*try {
            getExerciseThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    // 추천 영상 수평 리스트에 필요한 부분
    public void firstInit(){
        mRecyclerView = (RecyclerView) v.findViewById(R.id.thumbnail_recyclerview);
        mList = new ArrayList<>();
    }

    // 추천 영상 수평 리스트에 유튜브 썸네일, 제목 추가
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

    // 추천 영상&광고 영상으로 띄울 영상 index 번호 임의로 저장
    /*public void setAdvertiseVideo() {
        // 파이어베이스 DB에 운동 정보 추가
        // Write a message to the database
        database = FirebaseDatabase.getInstance();         // 데이터베이스 선언, 할당
        myRef = database.getReference("Pedal").child("ShowExerciseIndex");

        // 광고 영상으로 띄울 영상 index 저장 = 임의로 아무 번호나 5개
        myRef.child("AdvertiseIndex").child("0").setValue("2");
        myRef.child("AdvertiseIndex").child("1").setValue("27");
        myRef.child("AdvertiseIndex").child("2").setValue("104");
        myRef.child("AdvertiseIndex").child("3").setValue("98");
        myRef.child("AdvertiseIndex").child("4").setValue("135");

        // 추천 영상으로 띄울 영상 index 저장 = 임의로 아무 번호나 7개 -> 나중에는 추천 알고리즘 통해서 추천할 수 있도록 변경
        myRef.child("RecommendIndex").child("0").setValue("1");
        myRef.child("RecommendIndex").child("1").setValue("2");
        myRef.child("RecommendIndex").child("2").setValue("3");
        myRef.child("RecommendIndex").child("3").setValue("4");
        myRef.child("RecommendIndex").child("4").setValue("5");
        myRef.child("RecommendIndex").child("5").setValue("6");
        myRef.child("RecommendIndex").child("6").setValue("7");
    }*/

    // 운동 API에서 데이터 가져와서 Firebase Database에 저장
    /*
    public void setExerciseData() {
        Thread apiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String serviceKey = "7VgAbrUNHG0BQOPUubAEEkOT45PoaRK6TR92eLuGBsfqyhspb%2BY1oOyrwqIeWXYGrSVw9vMreaGpnekwpR8pGw%3D%3D";
                String limitPage = "247"; // 최대 247

                String urlAddress = "https://api.odcloud.kr/api/15084814/v1/uddi:3f8d6b98-0082-4792-92a8-90d40ecc4bce"
                        + "?page=1&perPage=" + limitPage + "&serviceKey=" + serviceKey;

                try {
                    URL url = new URL(urlAddress);

                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();
                    while(line != null) {
                        buffer.append(line + "\n");
                        line = reader.readLine();
                    }

                    String jsonData = buffer.toString();

                    // jsonDate를 JSONObject의 형태로 변환
                    JSONObject jsonObj = new JSONObject(jsonData);

                    // JSONObject에서 "data"의 JSONArray 추출
                    JSONArray content = (JSONArray)jsonObj.get("data");

                    // 리스트에 정보 전달
                    for(int i = 0; i < content.length(); i++) {
                        // i번째 정보 가져오기
                        JSONObject contentCut = content.getJSONObject(i);

                        // 운동 정보 추출
                        String mLargeCategory = contentCut.getString("대분류");
                        String mMiddleCategory = contentCut.getString("중분류");
                        String mSubCategory = contentCut.getString("소분류");
                        String mYoutubeTitle = contentCut.getString("제목");
                        String mYoutubeURL = contentCut.getString("동영상주소");

                        //og.println(Log.DEBUG,"Data", mYoutubeURL + " " + mYoutubeTitle + " " + mLargeCategory);

                        // 파이어베이스 DB에 운동 정보 추가
                        // Write a message to the database
                        database = FirebaseDatabase.getInstance();         // 데이터베이스 선언, 할당
                        myRef = database.getReference("Pedal").child("ExerciseData").child(Integer.toString(i));

                        myRef.child("LargeCategory").setValue(mLargeCategory);   // 대분류
                        myRef.child("MiddleCategory").setValue(mMiddleCategory); // 중분류
                        myRef.child("SubCategory").setValue(mSubCategory);       // 소분류
                        myRef.child("YoutubeTitle").setValue(mYoutubeTitle);     // 유튜브 제목
                        myRef.child("YoutubeURL").setValue(mYoutubeURL);         // 유튜브 링크
                    }

                    //Log.println(Log.DEBUG,"Test", "----------------------------------------------------------------");
                    //Log.println(Log.DEBUG,"Data", content.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        apiThread.start();
    }*/
}