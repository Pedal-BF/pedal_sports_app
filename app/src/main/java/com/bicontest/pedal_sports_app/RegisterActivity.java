package com.bicontest.pedal_sports_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;                                          // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;                                      // 실시간 데이터베이스

    private EditText mEtEmail, mEtPwd, mEtPwdCheck, mEtNickname, mEtId, mEtName; // 회원가입 입력필드
    private Button mBtnEmailCheck;                                               // 이메일 인증 버튼
    private Button mBtnIdCheck;                                                  // 아이디 중복 확인 버튼
    private Button mBtnPwdCheck;                                                 // 비밀번호 확인 버튼
    private Button mBtnNicknameCheck;                                            // 닉네임 중복 확인 버튼
    private Button mBtnRegister;                                                 // 회원가입 버튼

    private String samplePass = "check1234&";                                    // 이메일 인증용 임시 비번
    private Boolean checkEmail = false;                                          // 이메일 인증 확인

    // 비밀번호 정규식: 영어 대/소문자, 숫자, 특수문자 조합 8~16자
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Pedal");

        mEtEmail = findViewById(R.id.et_email);                 // 이메일 입력 필드
        mEtId = findViewById(R.id.et_id);                       // 아이디 입력 필드
        mEtPwd = findViewById(R.id.et_pwd);                     // 비밀번호 입력 필드
        mEtPwdCheck = findViewById(R.id.et_pwdcheck);           // 비밀번호 재입력 필드
        mEtName = findViewById(R.id.et_name);                   // 이름 입력 필드
        mEtNickname = findViewById(R.id.et_nickname);           // 닉네임 입력 필드

        mBtnEmailCheck = findViewById(R.id.btn_emailcheck);         // 이메일 인증 버튼
        mBtnIdCheck = findViewById(R.id.btn_idcheck);               // 아이디 중복 확인 버튼
        mBtnPwdCheck = findViewById(R.id.btn_pwdcheck);             // 비밀번호 확인 버튼
        mBtnNicknameCheck = findViewById(R.id.btn_nicknamecheck);   // 닉네임 중복 확인 버튼
        mBtnRegister = findViewById(R.id.btn_register);             // 회원가입 버튼

        // 아이디 중복 확인 버튼 클릭 시
        mBtnIdCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mEtId.length() == 0) {
                            Toast.makeText(RegisterActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if (!snapshot.exists()) {
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        // 닉네임 중복 확인 버튼 클릭 시
        mBtnNicknameCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mEtNickname.length() == 0) {
                            Toast.makeText(RegisterActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if (snapshot.child(mEtNickname.getText().toString()).exists()) {
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        // 비밀번호 확인 버튼 클릭 시
        mBtnPwdCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEtPwd.length() == 0 || mEtPwdCheck.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (!mEtPwd.getText().toString().equals(mEtPwdCheck.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    mEtPwdCheck.setText("");
                    mEtPwdCheck.requestFocus();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 이메일 인증 버튼 클릭 시
        mBtnEmailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = mEtEmail.getText().toString();
                String strId = mEtId.getText().toString();
                String strPwd = samplePass; // 이메일 인증용 임시 비밀번호
                String strName = mEtName.getText().toString();
                String strNickname = mEtNickname.getText().toString();

                // 이메일 입력 확인
                if(!TextUtils.isEmpty(strEmail)) {
                    createUser(strEmail, strId, strPwd, strName, strNickname);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 회원가입 버튼 클릭 시
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString();
                String strId = mEtId.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strName = mEtName.getText().toString();
                String strNickname = mEtNickname.getText().toString();

                // 필수 정보 입력 확인
                if(checkIsFull(strEmail, strId, strPwd, strName, strNickname)) {
                    // 이메일 인증 확인을 위한 로그인
                    mFirebaseAuth.signInWithEmailAndPassword(strEmail, samplePass);
                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                    
                    // 이메일 인증이 완료된 경우 탈퇴 후 재가입
                    if(firebaseUser.isEmailVerified()) {
                        checkEmail = true;

                        firebaseUser.delete();                                                        // Authentication에서 계정 정보 삭제
                        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).removeValue(); // Realtime Datebase에서 계정 정보 삭제

                        createUser(strEmail, strId, strPwd, strName, strNickname); // 회원가입
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "이메일 인증이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // 필수 정보 입력 확인
    private boolean checkIsFull(String strEmail, String strId, String strPwd, String strName, String strNickname) {
        // 이메일 미입력
        if(TextUtils.isEmpty(strEmail)) {
            Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 아이디 미입력
        if (TextUtils.isEmpty(strId)) {
            Toast.makeText(RegisterActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 비밀번호 유효성 검사
        if(!isValidPasswd(strPwd)) {
            return false;
        }
        // 비밀번호 미입력
        if (TextUtils.isEmpty(strPwd)) {
            Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        // 이름 미입력
        if (TextUtils.isEmpty(strName)) {
            Toast.makeText(RegisterActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 닉네임 미입력
        if (TextUtils.isEmpty(strNickname)) {
            Toast.makeText(RegisterActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    // 비밀번호 유효성 검사: 정규식 검증
    private boolean isValidPasswd(String strPwd) {
        if (strPwd.isEmpty()) {
            // 비밀번호 미입력
            Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!PASSWORD_PATTERN.matcher(strPwd).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(RegisterActivity.this, "비밀번호를 알파벳, 숫자, 특수문자 조합의 8~16자로 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 이메일 인증 메일 전송
    private void checkEmail(String strEmail, String strPwd) {
        // 로그인
        mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd);
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "인증 메일을 전송했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // 로그아웃
        mFirebaseAuth.signOut();
    }

    // 회원가입 진행
    private void createUser(String strEmail, String strId, String strPwd, String strName, String strNickname) {
        // Firebase Auth 진행
        mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                    UserAccount account = new UserAccount();
                    account.setIdToken(firebaseUser.getUid());
                    account.setEmailId(firebaseUser.getEmail());
                    account.setUserId(strId);
                    account.setPassword(strPwd);
                    account.setName(strName);
                    account.setNickname(strNickname);

                    // setValue: database에 insert (삽입) 행위
                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                    // 이메일 인증이 완료된 경우 회원가입 성공 메시지를 띄우고 로그인 페이지로 이동
                    if(checkEmail) {
                        Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); // 로그인 페이지로 이동
                        startActivity(intent);
                    }
                    // 이메일 인증이 완료되지 않은 경우 이메일 인증 메일 전송
                    else {
                        // 이메일 인증 메일 전송
                        checkEmail(strEmail, strPwd);
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}