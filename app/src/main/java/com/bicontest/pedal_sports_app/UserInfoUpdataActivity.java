package com.bicontest.pedal_sports_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ObbInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserInfoUpdataActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mFirebaseStorage;

    private ImageView mIvProfile;                                                                         // 프로필 사진 필드
    private EditText mEtName, mEtIdChange, mEtNicknameChange, mEtPwdChange, mEtPwdCheckChange, mEtEmail;  // 회원정보 수정 입력 필드
    private Button mBtnProfileChange;                                                                     // 프로필 사진 수정 버튼
    private Button mBtnIdChange;                                                                          // 아이디 수정 버튼
    private Button mBtnNicknameChange;                                                                    // 닉네임 수정 버튼
    private Button mBtnPwdCheckChange;                                                                    // 비밀번호 중복 확인 버튼
    private Button mBtnUserInfoUpdataok;                                                                  // 회원정보 수정 버튼
    private Button mBtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_updata);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Pedal");
        mFirebaseStorage = FirebaseStorage.getInstance();

        mIvProfile = findViewById(R.id.iv_profile);                                // 프로필 사진

        mEtEmail = findViewById(R.id.et_email);                                    // 이메일 필드
        mEtIdChange = findViewById(R.id.et_idChange);                              // 아이디 수정 필드
        mEtPwdChange = findViewById(R.id.et_pwdChange);                            // 비밀번호 수정 필드
        mEtPwdCheckChange = findViewById(R.id.et_pwdcheckChange);                  // 비밀번호 재입력 필드
        mEtName = findViewById(R.id.et_name);                                      // 이름 필드
        mEtNicknameChange = findViewById(R.id.et_nicknameChange);                  // 닉네임 수정 필드

        mBtnProfileChange = findViewById(R.id.btn_profileChange);                  // 프로필 사진 변경 버튼
        mBtnIdChange = findViewById(R.id.btn_idChange);                            // 아이디 변경 버튼
        mBtnPwdCheckChange = findViewById(R.id.btn_pwdcheckChange);                // 비밀번호 중복 확인 버튼
        mBtnNicknameChange = findViewById(R.id.btn_nicknameChange);                // 닉네임 변경 버튼
        mBtnUserInfoUpdataok = findViewById(R.id.btn_userinfoupdataok);            // 회원정보 변경 버튼
        mBtnCancel = findViewById(R.id.btn_cancel);                                // 변경 취소 버튼

        // 프로필 사진 필드
//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                getIntent().putExtra("Image", )
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        // 이름 입력 필드
        mEtName.setFocusable(false);    // 입력창 수정 불가
        mEtName.setClickable(false);    // 입력창 수정 불가
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name = snapshot.child("UserAccount").child(mFirebaseAuth.getUid()).child("name").getValue(String.class);
                mEtName.setText(Name);      // 데이터를 입력창에 출력
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 아이디 입력 필드
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ID = snapshot.child("UserAccount").child(mFirebaseAuth.getUid()).child("userId").getValue(String.class);
                mEtEmail.setText(ID);    // 데이터를 입력창에 출력
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 비밀번호 입력 필드
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String PW = snapshot.child("UserAccount").child(mFirebaseAuth.getUid()).child("password").getValue(String.class);
                mEtEmail.setText(PW);    // 데이터를 입력창에 출력
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 닉네임 입력 필드
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Nick = snapshot.child("UserAccount").child(mFirebaseAuth.getUid()).child("nickname").getValue(String.class);
                mEtEmail.setText(Nick);    // 데이터를 입력창에 출력
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 이메일 입력 필드
        mEtEmail.setFocusable(false);   // 입력창 수정 불가
        mEtEmail.setClickable(false);   // 입력창 수정 불가
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Email = snapshot.child("UserAccount").child(mFirebaseAuth.getUid()).child("emailId").getValue(String.class);
                mEtEmail.setText(Email);    // 데이터를 입력창에 출력
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 아이디 변경 버튼 클릭 시
        mBtnIdChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 변경할 아이디
                String changeName = mEtIdChange.getText().toString();

                if (mEtIdChange.length() == 0) {
                    Toast.makeText(UserInfoUpdataActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Map<String, Object> idchangeMap = new HashMap<String, Object>();
                    idchangeMap.put("userId", changeName);   // 데이터 저장
                    mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).updateChildren(idchangeMap);

                    Toast.makeText(UserInfoUpdataActivity.this, "아이디가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 비밀번호 중복 확인 버튼 클릭 시
        mBtnPwdCheckChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef.child("UserAccount").child("idToken").child("password").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 변경할 비밀번호
                        String changePw = mEtPwdChange.getText().toString();

                        if (mEtPwdChange.length() == 0 || mEtPwdCheckChange.length() == 0) {

                            Toast.makeText(UserInfoUpdataActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if (!mEtPwdChange.getText().toString().equals(mEtPwdCheckChange.getText().toString())) {
                            Toast.makeText(UserInfoUpdataActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            mEtPwdCheckChange.setText("");
                            mEtPwdCheckChange.requestFocus();
                        }
                        else {
                            Map<String, Object> passwordMap = new HashMap<String, Object>();
                            passwordMap.put("password", changePw);  // 데이터 저장
                            mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).updateChildren(passwordMap);

                            Toast.makeText(UserInfoUpdataActivity.this, "비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(UserInfoUpdataActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        // 닉네임 변경 버튼 클릭 시
        mBtnNicknameChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 변경할 닉네임
                String changeNickname = mEtNicknameChange.getText().toString();

                if (mEtNicknameChange.length() == 0) {
                    Toast.makeText(UserInfoUpdataActivity.this, "닉네임 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Map<String, Object> nickchangeMap = new HashMap<String, Object>();
                    nickchangeMap.put("nickname", changeNickname);   // 데이터 저장
                    mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).updateChildren(nickchangeMap);

                    Toast.makeText(UserInfoUpdataActivity.this, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 프로필 사진 수정 버튼 클릭 시
        mBtnProfileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미지 불러오기 위해 갤러리에 접근
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityIntent.launch(intent);
            }
        });

        // 정보 수정 버튼 클릭 시
        mBtnUserInfoUpdataok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 취소 버튼 클릭 시
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                // 갤러리에서 이미지 불러온 후
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        try {
                            Uri uri = result.getData().getData();
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            mIvProfile.setImageBitmap(bitmap);
                            uploadToFirebase(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    // 파이어베이스 이미지 업로드
    private void uploadToFirebase(Uri uri) {
        StorageReference storageRef = mFirebaseStorage.getReference();
        StorageReference mountainsRef = storageRef.child("profile.jpg");
        StorageReference mountainImagesRef = storageRef.child("image/mountains.jpg");

        mountainsRef.getName().equals(mountainImagesRef.getName());     // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());     // false
    }
}