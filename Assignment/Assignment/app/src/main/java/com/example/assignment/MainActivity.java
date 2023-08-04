package com.example.assignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.assignment.adapter.NasaAdapter;
import com.example.assignment.retrofit.ApiRetrofitImage;
import com.example.assignment.retrofit.ApiRetrofitNasa;
import com.example.assignment.databinding.ActivityMainBinding;
import com.example.assignment.models.ImageNasa;
import com.example.assignment.models.Status;
import com.example.assignment.socket.ServerSocket;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //Khai báo handle để nhận massage
    private Handler handler;
    //Khai báo dữ liệu nhận massage tương ứng
    private static final int MESSAGE_PROCESS = 100;
    //Khai báo Viewbinding để gọi id không cần khai báo trường gọi id dài như trước
    private ActivityMainBinding binding;
    //Khai báo executor, tạo 2 luồng
    private ExecutorService service = Executors.newFixedThreadPool(2);
    //Tạo đối tượng socket lớp ServerSocket
    private ServerSocket socket = new ServerSocket();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sử dụng để inflate layout của activity_main
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //được gọi để thiết lập nội dung hiển thị cho activity
        setContentView(binding.getRoot());

        //gọi đến handler để nhận thay đổi
        listenerHandler();

        //Khởi tạo server soket cổng 9999, đợi client kết nối
        socket.serverSocket();
    }

    //Sự kiến ấn ảnh Nasa tiến hành get và post dữ liệu
    public void callApiImageUrl(View view) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                //Khai báo dữ liệu đếm server đã được đẩy bao nhiêu
                final int[] finalI = {0};
                //Sử dụng vòng for để dùng execute
                for (int i = 0; i < 10; i++) {
                    //Tạo đối tượng random
                    Random random = new Random();
                    //Khai báo dữ liệu ngày nhỏ, lớn nhất, ngày nhất định
                    int minDay = 0, maxDay = 0;
                    String date = null;
                    //Tự động import
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        //Bắt đầu từ ngày
                        minDay = (int) LocalDate.of(2023, 6, 1).toEpochDay();
                        //Kết thúc ngày
                        maxDay = (int) LocalDate.of(2023, 7, 20).toEpochDay();
                        //Tạo 1 ngày ngẫu nhiên
                        long randomDay = minDay + random.nextInt(maxDay - minDay);
                        //gán giá trị bằng 1 ngày nhất định
                        date = LocalDate.ofEpochDay(randomDay).toString();
                    }

                    //Gọi interface Retrofit để lấy dữ liệu Nasa với key và ngày đã tạo
                    ApiRetrofitNasa.apiRetrofit.getNasaUrl("4wFDvU2iu1udqcs9SNqJHQrIPkqcSBBQqzuJDKil", date).enqueue(new Callback<ImageNasa>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(Call<ImageNasa> call, Response<ImageNasa> response) {
                            if (response.isSuccessful()) {
                                //Lấy thành công tăng đếm, sửa dữ liệu, convert Base64 để đẩy server
                                finalI[0]++;
                                response.body().setUrl(convert(response.body().getUrl()));
                                //gọi phương thức Post dữ liệu lên server
                                PostApiUrl(response.body(), finalI[0]);
                            }
                        }

                        @Override
                        public void onFailure(Call<ImageNasa> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Call Api Nasa Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //Phương thức post dữ liệu
    private void PostApiUrl(ImageNasa body, int i) {
        //Gọi interface Retrofit để đẩy dữ liệu lên Nodejs
        ApiRetrofitImage.apiRetrofit.postNasaUrl(body).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                //Kết nối thành công
                if (response.isSuccessful()) {
                    //Đẩy thành công gọi handler truyền đối tượng massage mới
                    Message message = new Message();
                    message.what = MESSAGE_PROCESS;
                    message.arg1 = i;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Call Api Server Post Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Phương thức convert Base64 cho link ảnh Nasa
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convert(String url) {
        //Trả về dạng chuỗi Base64
        return Base64.getEncoder().encodeToString(url.getBytes());
    }

    //Khởi tạo phương thức để nhận thay đổi
    private void listenerHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //Nơi nhận các massage
                switch (msg.what) {
                    case MESSAGE_PROCESS:
                        //Gọi socket gửi về client số ảnh đã lưu
                        socket.postSocket("Lưu thành công ảnh " + msg.arg1 + " về NodeJs");
                        //Lấy dữ liệu set lại adapter với mỗi lầm lưu được
                        getApiUrl();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Undefined message", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    //Phương thức get dữ liệu
    private void getApiUrl() {
        //Gọi interface Retrofit để lấy dữ liệu Nodejs
        ApiRetrofitImage.apiRetrofit.getNasaUrl().enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                //Kết nối thành công
                if (response.isSuccessful()) {
                    //Custom Listview sử dụng adapter, sử dụng iew
                    binding.listview.setAdapter(new NasaAdapter(response.body().getObject()));
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Call Api Server Get Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}