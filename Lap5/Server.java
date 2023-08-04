package com.example.lap2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.lap2.databinding.ActivityMainLap5Binding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity_Lap5 extends AppCompatActivity {

    //Khai báo Viewbinding để gọi id không cần khai báo trường gọi id dài như trước
    private ActivityMainLap5Binding binding;
    //Khai báo handle để nhận massage
    private Handler _handler;
    //Khai báo dữ liệu nhận massage tương ứng
    private static final int MESSAGE_PROCESS = 100;
    //Khai báo list nhận dữ liệu, gửi dữ liệu của socket
    private List<String> list = new ArrayList<>();
    //Khai báo socket kết nối
    private Socket socketOfClient;
    //Khai báo BufferedReader để đọc dữ liệu
    private BufferedReader br;
    //Khai báo BufferedReader để đọc viết liệu
    private BufferedWriter bw;
    //khai báo ServerSocket để tạo cổng kết nối
    private ServerSocket listener;
    //khai báo executor, tạo 2 luồng
    private ExecutorService service = Executors.newFixedThreadPool(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lap5);

        //sử dụng để inflate layout của activity_main_lap6
        binding = ActivityMainLap5Binding.inflate(getLayoutInflater());
        //được gọi để thiết lập nội dung hiển thị cho activity
        setContentView(binding.getRoot());

        //gọi đến handler để nhận thay đổi
        listenerHandler();

        //sử dụng luồng executor tạo server
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //khởi tạo cổng socket 9999
                    listener = new ServerSocket(9999);
                    //vòng lặp
                    while (true) {
                        //đợi người dùng kết nối
                        Socket servSocket = listener.accept();
                        //hiện thị system để biết kết nối chưa
                        System.out.println(servSocket);
                        //khai báo đối tượng đọc dữ liệu từ server
                        br = new BufferedReader(new InputStreamReader(servSocket.getInputStream()));
                        //khai báo đối tượng viết dữ liệu từ client
                        bw = new BufferedWriter(new OutputStreamWriter(servSocket.getOutputStream()));
                        //gọi đến phương thức lấy dữ liệu được nhận
                        getSocket();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    // dừng lắng nghe client
                    try {
                        listener.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    //Phương thức nhận dữ liệu client
    private void getSocket() {
        //kiểm tra ngoại lệ
        try {
            //khai báo dữ liệu
            String responseLine;
            // đọc message
            while ((responseLine = br.readLine()) != null) {
                //Thêm vào list với dữ liệu nhận được
                list.add(responseLine);
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
            Log.d("err", "Dont know about this host");
        } catch (IOException e) {
            Log.d("err", "Could not get I/O");
        }
    }

    //Phương thức gửi dữ liệu lên client khi ấn gửi
    public void postSocket(View view) {
        //Sử dụng luồng executor để gửi đến client
        service.execute(new Runnable() {
            @Override
            public void run() {
                //nhận dữ liệu với Viewbinding nhận từ ô nhập
                String chat = binding.editSocket.getText().toString();
                list.add("Server: " + chat);
                //gửi massage cho handler
                Message message = new Message();
                //handler nhận case MESSAGE_PROCESS
                message.what = MESSAGE_PROCESS;
                //gửi handdler
                _handler.sendMessage(message);
                try {
                    // tạo message được gửi từ server
                    bw.write("Server: " + chat);
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
                        Toast.makeText(MainActivity_Lap5.this, "Undefined message", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    //Phương thức custom adapter hiển thị cho list view
    private void listAdapter() {
        //sử dụng Viewbinding gọi đến list view
        binding.listItemSocket.setAdapter(new ArrayAdapter<>(MainActivity_Lap5.this, android.R.layout.simple_list_item_1, list));
    }
}