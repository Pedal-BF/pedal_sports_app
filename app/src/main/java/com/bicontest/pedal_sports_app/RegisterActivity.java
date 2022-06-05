package com.bicontest.pedal_sports_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;                  // 파이어베이스 인증

    private DatabaseReference mDatabaseRef;              // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd;                   // 회원가입 입력필드
    private Button mBtnRegister;                         //회원가입 버튼

    // 비밀번호 정규식: 영어 대/소문자, 숫자, 특수문자 조합 8~16자
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Pedal");

        mEtEmail = findViewById(R.id.et_email);             // 이메일 입력 필드
        mEtPwd = findViewById(R.id.et_pwd);                 // 비밀번호 입력 필드
        mBtnRegister = findViewById(R.id.btn_register);     // 회원가입 버튼

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                // 필수 정보 입력 확인
                if(checkIsFull(strEmail, strPwd)) {
                    createUser(strEmail, strPwd); // 회원가입
                }
            }
        });
    }

    // 필수 정보 입력 확인
    private boolean checkIsFull(String strEmail, String strPwd) {
        // 이메일 미입력
        if(TextUtils.isEmpty(strEmail)) {
            Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        //비밀번호 유효성 검사
        if(!isValidPasswd(strPwd)) {
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
    // 이메일 인증 확인
    private boolean checkEmail(String strEmail, String strPwd) {
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

        // 회원 탈퇴
        // firebaseUser.delete();                                                        // Authentication에서 계정 정보 삭제
        // mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).removeValue(); // Realtime Datebase에서 계정 정보 삭제

        return true;
    }

    // 회원가입 진행
    private void createUser(String strEmail, String strPwd) {
        // Firebase Auth 진행
        mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                    UserAccount account = new UserAccount();
                    account.setIdToken(firebaseUser.getUid());
                    account.setEmailId(firebaseUser.getEmail());
                    account.setPassword(strPwd);

                    // setValue: database에 insert (삽입) 행위
                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                    checkEmail(strEmail, strPwd);

                    //Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); // 로그인 페이지로 이동
                    startActivity(intent);

                } else {
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}