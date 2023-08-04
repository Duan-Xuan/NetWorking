package com.example.appthu2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.appthu2.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    //Khai báo Viewbinding để gọi id không cần khai báo trường gọi id dài như trước
    private ActivityMainBinding binding;
    //Khai báo handle để nhận massage
    private Handler _handler;
    //Khai báo dữ liệu nhận massage tương ứng
    private static final int MESSAGE_PROCESS = 100;
    //Khai báo list nhận dữ liệu, gửi dữ liệu của socket
    private List<String> list = new ArrayList<>();
    // thay đổi localhost thành ip theo ip của server
    private String serverHost = "localhost";
    //Khai báo socket kết nối
    private Socket socketOfClient;
    //Khai báo BufferedReader để đọc dữ liệu
    private BufferedReader br;
    //Khai báo BufferedReader để đọc viết liệu
    private BufferedWriter bw;
    //khai báo executor tạo 2 luồng
    private ExecutorService service = Executors.newFixedThreadPool(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sử dụng để inflate layout của activity_main_lap6
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //được gọi để thiết lập nội dung hiển thị cho activity
        setContentView(binding.getRoot());

        //gọi đến handler để nhận thay đổi
        listenerHandler();

        //sử dụng luồng executor kết nối socket
        service.execute(new Runnable() {
            @Override
            public void run() {
                //kiểm tra ngoại lệ
                try {
                    // gửi request kết nối đến server với cổng 9999
                    socketOfClient = new Socket(serverHost, 9999);
                    //khai báo đối tượng đọc dữ liệu từ server
                    br = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
                    //khai báo đối tượng viết dữ liệu từ client
                    bw = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
                    //gọi phương thức nhận dữ liệu từ server hiển thị client
                    getSocket();
                } catch (
                        UnknownHostException e) {
                    Log.d("err", "Dont know about this host " + serverHost);
                } catch (
                        IOException e) {
                    Log.d("err", "Could not get I/O");
                }
            }
        });
    }

    //Phương thức nhận dữ liệu server
    private void getSocket() {
        //kiểm tra ngoại lệ
        try {
            //khai báo dữ liệu
            String responseLine;
            // đọc message
            while ((responseLine = br.readLine()) != null) {
                //Thêm vào list với dữ liệu nhận được
                list.add(responseLine);
                //gán giá trị để hiện dialog
                textDialog = responseLine;
                //Tạo massage gửi đến handler
                Message message = new Message();
                //handler nhận case MESSAGE_PROCESS
                message.what = MESSAGE_PROCESS;
                //gửi handdler
                _handler.sendMessage(message);
            }
            // kết thúc
            br.close();
        } catch (UnknownHostException e) {
            Log.d("err", "Dont know about this host " + serverHost);
        } catch (IOException e) {
            Log.d("err", "Could not get I/O");
        }
    }

    //Phương thức gửi dữ liệu lên server
    public void postSocket(View view) {
        //sử dụng luồng excutor để gửi server
        service.execute(new Runnable() {
            @Override
            public void run() {
                //nhận dữ liệu với Viewbinding nhận từ ô nhập
                String chat = binding.editSocket.getText().toString();
                //thêm vào list để client gửi
                list.add("Client: " + chat);
                //gửi massage cho handler
                Message message = new Message();
                //handler nhận case MESSAGE_PROCESS
                message.what = MESSAGE_PROCESS;
                //gửi handdler
                _handler.sendMessage(message);
                try {
                    // tạo message được gửi từ Client
                    bw.write("Client: " + chat);
                    // kết thúc dòng message
                    bw.newLine();
                    // đẩy message đi
                    bw.flush();
                } catch (Exception e) {
                    Log.d("err", "Server 0 disconnected");
                    e.printStackTrace();
                }
            }
        });
    }

    //khởi tạo phương thức để nhận thay đổi
    private void listenerHandler() {

        //nơi nhận các massage
        _handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_PROCESS:
                        //Với mỗi lần nhận và gửi cập nhật lại ô nhập(trống)
                        binding.editSocket.setText("");
                        //Nhận massage cập nhật lại adapter
                        listAdapter();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Undefined message", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    //Phương thức custom adapter hiển thị cho list view
    private void listAdapter() {
        //sử dụng Viewbinding gọi đến list view
        binding.listItemSocket.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
    }
}