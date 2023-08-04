package com.example.assignment.socket;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocket {
    //Khai báo BufferedReader để viết liệu
    private BufferedWriter bw;
    //khai báo ServerSocket để tạo cổng kết nối
    private java.net.ServerSocket listener;
    //Khai báo executor, tạo 2 luồng
    private ExecutorService service = Executors.newFixedThreadPool(2);

    public void serverSocket() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //khởi tạo cổng socket 9999
                    listener = new java.net.ServerSocket(9999);
                    //vòng lặp
                    while (true) {
                        //đợi người dùng kết nối
                        Socket servSocket = listener.accept();
                        //hiện thị system để biết kết nối chưa
                        System.out.println(servSocket);
                        //khai báo đối tượng viết dữ liệu từ client
                        bw = new BufferedWriter(new OutputStreamWriter(servSocket.getOutputStream()));
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

    //Phương thức gửi dữ liệu về client khi ấn gửi
    public void postSocket(String chat) {
        //Sử dụng luồng executor để gửi đến client
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Tạo message được gửi từ server
                    bw.write(chat);
                    // Kết thúc dòng message
                    bw.newLine();
                    // Đẩy message đi
                    bw.flush();
                } catch (Exception e) {
                    Log.d("err", "Server 0 disconnected");
                    e.printStackTrace();
                }
            }
        });
    }
}
