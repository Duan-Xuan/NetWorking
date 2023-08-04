package com.example.assignment.filebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.assignment.MainActivity;
import com.example.assignment.R;
import com.example.assignment.databinding.ActivityMainLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityLogin extends AppCompatActivity {

    //Khai báo FirebaseAuth
    private FirebaseAuth auth;
    //Khai báo Viewbinding để gọi id không cần khai báo trường gọi id dài như trước
    private ActivityMainLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        //Khởi tạo một phiên bản của lớp FirebaseAuth
        auth = FirebaseAuth.getInstance();
        //Sử dụng để inflate layout của activity_main_login
        binding = ActivityMainLoginBinding.inflate(getLayoutInflater());
        //Được gọi để thiết lập nội dung hiển thị cho activity
        setContentView(binding.getRoot());
    }

    //Sự kiện ấnn nút login
    public void login(View view) {
        //Khởi tạo hai chuỗi email và pass bằng cách lấy giá trị từ hai trường văn bản email và password nhờ Viewbinding
        String email = binding.email.getText().toString(), pass = binding.password.getText().toString();
        //Kiểm tra dữ liệu trống
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(MainActivityLogin.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        //được gọi để xác thực người dùng với email và mật khẩu đã nhập
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivityLogin.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //một thông báo sẽ được hiển thị để thông báo cho người dùng
                if (!task.isSuccessful()) {
                    //thông báo sẽ được hiển lỗi khiến người dùng không thể đăng nhập
                    Toast.makeText(MainActivityLogin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    //thông báo sẽ được hiển đăng nhập thành công
                    Toast.makeText(MainActivityLogin.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    //Chạy màn hình HackNasa
                    startActivity(new Intent(MainActivityLogin.this, MainActivity.class));
                }
            }
        });
    }

    public void register(View view) {
        startActivity(new Intent(MainActivityLogin.this, MainActivityRegister.class));
    }
}