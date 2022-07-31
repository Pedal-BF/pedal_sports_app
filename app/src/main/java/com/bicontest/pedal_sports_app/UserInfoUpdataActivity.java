package com.bicontest.pedal_sports_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

public class UserInfoUpdataActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    private ImageView mIvProfile;                                                                   // 프로필 사진 필드
    private EditText mEtName, mEtIdChange, mEtNickname, mEtPwdChange, mEtPwdCheckChange, mEtEmail;  // 회원정보 수정 입력 필드
    private Button mBtnProfileChange;                                                               // 프로필 사진 수정 버튼
    private Button mBtnIdCheck;                                                                     // 아이디 수정 버튼
    private Button mBtnNicknameChange;                                                              // 닉네임 수정 버튼
    private Button mBtnPwdCheckChange;                                                              // 비밀번호 중복 확인 버튼
    private Button mBtnUserInfoData;                                                                // 회원정보 수정 버튼
    private Button mBtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_updata);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Pedal");

        mIvProfile = findViewById(R.id.iv_profile);                                // 프로필 사진

        mEtEmail = findViewById(R.id.et_email);                                    // 이메일 수정 필드
        mEtIdChange = findViewById(R.id.et_idChange);                              // 아이디 수정 필드
        mEtPwdChange = findViewById(R.id.et_pwdcheck);                             // 비밀번호 수정 필드
        mEtPwdCheckChange = findViewById(R.id.et_pwdcheckChange);                  // 비밀번호 재입력 필드
        mEtName = findViewById(R.id.et_name);                                      // 이름 수정 필드
        mEtNickname = findViewById(R.id.et_nicknameChange);                        // 닉네임 수정 필드

        mBtnProfileChange = findViewById(R.id.btn_userinfodata);                   // 프로필 사진 수정 버튼
        mBtnIdCheck = findViewById(R.id.btn_idcheck);                              // 아이디 수정 버튼
        mBtnPwdCheckChange = findViewById(R.id.btn_pwdcheck);                      // 비밀번호 중복 확인 버튼
        mBtnNicknameChange = findViewById(R.id.btn_nicknamecheck);                 // 닉네임 수정 버튼
        mBtnUserInfoData = findViewById(R.id.btn_userinfoupdate);                  // 회원정보 수정 버튼
        mBtnCancel = findViewById(R.id.btn_cancel);

        // 프로필 사진 수정 버튼 클릭 시
        mBtnProfileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // 이미지 불러오기 위해 갤러리에 접근
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityIntent.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                // 갤러리에서 이미지 불러온 후
                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent intent = result.getData();
//                        Uri uri = intent.getData();
//                        mIvProfile.setImageURI(uri);
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            mIvProfile.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
}