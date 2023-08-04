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
import com.example.assignment.databinding.ActivityMainRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityRegister extends AppCompatActivity {

    //Khai báo FirebaseAuth
    private FirebaseAuth auth;
    //Khai báo Viewbinding để gọi id không cần khai báo trường gọi id dài như trước
    private ActivityMainRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        //Khởi tạo một phiên bản của lớp FirebaseAuth
        auth = FirebaseAuth.getInstance();
        //Sử dụng để inflate layout của activity_main_login
        binding = ActivityMainRegisterBinding.inflate(getLayoutInflater());
        //Được gọi để thiết lập nội dung hiển thị cho activity
        setContentView(binding.getRoot());
    }

    //Sự kiện ấn nút register
    public void register(View view) {
        //Khởi tạo hai chuỗi email và pass bằng cách lấy giá trị từ hai trường văn bản email và password nhờ Viewbinding
        String email = binding.email.getText().toString(), pass = binding.password.getText().toString();
        //Kiểm tra dữ liệu trống
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(MainActivityRegister.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        //được gọi để đăng ký người dùng với email và mật khẩu đã nhập
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivityRegister.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //một thông báo sẽ được hiển thị để thông báo cho người dùng
                if (!task.isSuccessful()) {
                    //thông báo sẽ được hiển lỗi khiến người dùng không thể đăng nhập
                    Toast.makeText(MainActivityRegister.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    //thông báo sẽ được hiển ký thành công
                    Toast.makeText(MainActivityRegister.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    //Chạy màn hình login
                    startActivity(new Intent(MainActivityRegister.this, MainActivityLogin.class));
                }
            }
        });
    }

    //Sự kiến ấn nút back
    public void back(View view) {
        onBackPressed();
    }
}