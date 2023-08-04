package com.example.lap2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.lap2.databinding.ActivityMainLap6Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity_Lap6 extends AppCompatActivity {

    //Khai báo FirebaseAuth
    private FirebaseAuth auth;
    //Khai báo Viewbinding để gọi id không cần khai báo trường gọi id dài như trước
    private ActivityMainLap6Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lap6);
        //khởi tạo một phiên bản của lớp FirebaseAuth
        auth = FirebaseAuth.getInstance();
        //sử dụng để inflate layout của activity_main_lap6
        binding = ActivityMainLap6Binding.inflate(getLayoutInflater());
        //được gọi để thiết lập nội dung hiển thị cho activity
        setContentView(binding.getRoot());
    }

    //Sự kiện khi ấn login
    public void login(View view) {
        //khởi tạo hai chuỗi email và pass bằng cách lấy giá trị từ hai trường văn bản idEmail và idPassword nhờ Viewbinding trước đó
        String email = binding.idEmail.getText().toString(), pass = binding.idPassword.getText().toString();
        //Kiểm tra dữ liệu trống
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(MainActivity_Lap6.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        //được gọi để xác thực người dùng với email và mật khẩu đã nhập
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity_Lap6.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //một thông báo sẽ được hiển thị để thông báo cho người dùng
                if (!task.isSuccessful()) {
                    //thông báo sẽ được hiển lỗi khiến người dùng không thể đăng nhập
                    Toast.makeText(MainActivity_Lap6.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    //thông báo sẽ được hiển đăng nhập thành công
                    Toast.makeText(MainActivity_Lap6.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Sự kiện khi ấn register
    public void register(View view) {
        //khởi tạo hai chuỗi email và pass bằng cách lấy giá trị từ hai trường văn bản idEmail và idPassword nhờ Viewbinding trước đó
        String email = binding.idEmail.getText().toString(), pass = binding.idPassword.getText().toString();
        //Kiểm tra dữ liệu trống
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(MainActivity_Lap6.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        //được gọi để đăng ký người dùng với email và mật khẩu đã nhập
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity_Lap6.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //một thông báo sẽ được hiển thị để thông báo cho người dùng
                if (!task.isSuccessful()) {
                    //thông báo sẽ được hiển lỗi khiến người dùng không thể đăng ký
                    Toast.makeText(MainActivity_Lap6.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    //thông báo sẽ được hiển đăng ký thành công
                    Toast.makeText(MainActivity_Lap6.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}