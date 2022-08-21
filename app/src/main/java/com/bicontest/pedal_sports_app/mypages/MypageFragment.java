package com.bicontest.pedal_sports_app.mypages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import com.bicontest.pedal_sports_app.R;
import com.bicontest.pedal_sports_app.UserInfoUpdataActivity;
import com.bicontest.pedal_sports_app.users_sign.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MypageFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성
    public static MypageFragment newInstance() {
        return new MypageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mypage, container, false); // fragment에서 findViewById를 하기 위한 세팅

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Pedal");

        // 회원 정보 수정
        Button btn_userinfo = v.findViewById(R.id.btn_userinfoupdate);
        btn_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getActivity()로 MainActivity의 replaceFragment를 불러옴
                //새로 불러온 Fragment의 Instance를 Main으로 전달
                Intent intent = new Intent(getActivity(), UserInfoUpdataActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

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