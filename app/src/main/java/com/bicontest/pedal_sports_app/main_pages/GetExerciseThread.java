package com.bicontest.pedal_sports_app.main_pages;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bicontest.pedal_sports_app.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// 파이어베이스에서 데이터 가져와서 클래스에 저장
public class GetExerciseThread implements Runnable {

    // 파이어베이스 DB 접근
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    // 운동 정보를 저장해둘 클래스
    private ExerciseInfo[] mExerciseInfo = new ExerciseInfo[Constants.allExerciseNum];

    @Override
    public void run() {
        database = FirebaseDatabase.getInstance();                         // 데이터베이스 선언, 할당
        myRef = database.getReference("Pedal").child("ExerciseData");

        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(int i = 0; i < Constants.allExerciseNum; i++) {
                            String mLargeCategory = snapshot.child(Integer.toString(i)).child("LargeCategory").getValue().toString();
                            String mMiddleCategory = snapshot.child(Integer.toString(i)).child("MiddleCategory").getValue().toString();
                            String mSubCategory = snapshot.child(Integer.toString(i)).child("SubCategory").getValue().toString();
                            String mYoutubeTitle = snapshot.child(Integer.toString(i)).child("YoutubeTitle").getValue().toString();
                            String mYoutubeURL = snapshot.child(Integer.toString(i)).child("YoutubeURL").getValue().toString();

                            mExerciseInfo[i] = new ExerciseInfo(mLargeCategory, mMiddleCategory, mSubCategory, mYoutubeTitle, mYoutubeURL);
                            Log.println(Log.DEBUG, "debug", "----------------------------------------------------------------");
                            Log.println(Log.DEBUG, "Data", mExerciseInfo[i].YoutubeURL + " " + mExerciseInfo[i].YoutubeTitle);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
}
