package com.bicontest.pedal_sports_app.mypages;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bicontest.pedal_sports_app.R;
import com.bicontest.pedal_sports_app.UserInfoUpdataActivity;
import com.bicontest.pedal_sports_app.users_sign.LoginActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MypageFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private FirebaseStorage mFirebaseStorage; // storage 사용시 gradle에서 뭔갈 해야 함

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
        mFirebaseStorage = FirebaseStorage.getInstance("gs://pedal-sports-app.appspot.com");

        // 프로필 사진 필드
        ImageView mIvProfileImg = v.findViewById(R.id.profile_img);
        StorageReference storageRef = mFirebaseStorage.getReference();
        String filename = "profile" + mFirebaseAuth.getUid() + ".jpg";
        storageRef.child("profile_img/" + filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri)
                        .into(mIvProfileImg);
            }
        });

        // 닉네임 필드
        EditText mEtNickname = v.findViewById(R.id.profile_nickname);
        mEtNickname.setFocusable(false);            // 입력창 수정 불가
        mEtNickname.setClickable(false);            // 입력창 수정 불가
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Nickname = snapshot.child("UserAccount").child(mFirebaseAuth.getUid()).child("nickname").getValue(String.class);
                mEtNickname.setText(Nickname);      // 데이터를 입력창에 출력
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 아이디 필드
        EditText mEtId = v.findViewById(R.id.profile_id);
        mEtId.setFocusable(false);      // 입력창 수정 불가
        mEtId.setClickable(false);      // 입력창 수정 불가
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Id = snapshot.child("UserAccount").child(mFirebaseAuth.getUid()).child("userId").getValue(String.class);
                mEtId.setText(Id);      // 데이터를 입력창에 출력
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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