package com.bicontest.pedal_sports_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MypageFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mypage, container, false); // fragment에서 findViewById를 하기 위한 세팅

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Pedal");

        // 로그아웃
        Button btn_logout = v.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.signOut();

                Toast.makeText(getActivity(), "로그아웃", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), LoginActivity.class); // 로그인 페이지로 이동
                startActivity(intent);
            }
        });

        // 회원 탈퇴
        Button btn_userdelete = v.findViewById(R.id.btn_userdelete);
        btn_userdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                firebaseUser.delete();                                                        // Authentication에서 계정 정보 삭제
                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).removeValue(); // Realtime Datebase에서 계정 정보 삭제

                Toast.makeText(getActivity(), "회원 탈퇴", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), LoginActivity.class); // 로그인 페이지로 이동
                startActivity(intent);
            }
        });

        return v;
    }
}