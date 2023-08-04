package com.example.assignment.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment.MainActivity;
import com.example.assignment.R;
import com.example.assignment.models.ImageNasa;
import com.example.assignment.models.Status;
import com.example.assignment.retrofit.ApiRetrofitImage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NasaAdapter extends BaseAdapter {
    //Khai báo list
    private List<ImageNasa> list;

    //Khai báo contrustor
    public NasaAdapter(List<ImageNasa> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    //Viết code tại phương thức này
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Khai báo lớp
        ViewHolder viewHolder;
        //Kiẻm tra view có null không
        if (view == null) {
            //Khai báo đối tượng
            viewHolder = new ViewHolder();
            //Chọn layout custom
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_nasa, viewGroup, false);
            //Khai báo trường id
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.date = view.findViewById(R.id.date);
            viewHolder.copyright = view.findViewById(R.id.explanation);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //Set cho mỗi custom
        viewHolder.title.setText(list.get(i).getTitle());
        viewHolder.date.setText(list.get(i).getDate());
        viewHolder.copyright.setText(list.get(i).getCopyright());
        //Ấn custom
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tạo AlertDialog để chọn sửa, xóa
                AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
                //Title của dialog
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn muốn... " + list.get(i).getTitle());
                //Nút sửa dữ liệu tương ứng
                builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Khai báo đối tượng dialog
                        Dialog dia_log = new Dialog(viewGroup.getContext());
                        //Loại bỏ tiêu đề
                        dia_log.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        //Set layout dialog
                        dia_log.setContentView(R.layout.dialog_nasa);
                        //Trỏ id dialog
                        EditText title = dia_log.findViewById(R.id.title), date = dia_log.findViewById(R.id.date), copyright = dia_log.findViewById(R.id.copyright);
                        Button updater = dia_log.findViewById(R.id.update), out = dia_log.findViewById(R.id.out);
                        //Gán giá trị của list vào edit tương ứng
                        title.setText(list.get(i).getTitle());
                        date.setText(list.get(i).getDate());
                        copyright.setText(list.get(i).getCopyright());
                        //Sự kiện ấn nút Update
                        updater.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Khai báo đối tượng nasa
                                ImageNasa nasa = list.get(i);
                                //Sửa giá trị trước khi gửi server
                                nasa.setTitle(title.getText().toString());
                                nasa.setDate(date.getText().toString());
                                nasa.setCopyright(copyright.getText().toString());
                                //Gọi interface Retrofit để cập nhật dữ liệu Nodejs
                                ApiRetrofitImage.apiRetrofit.updateNasaUrl(list.get(i)).enqueue(new Callback<Status>() {
                                    @Override
                                    public void onResponse(Call<Status> call, Response<Status> response) {
                                        //Kết nối thành công
                                        if (response.isSuccessful()) {
                                            //Update list tương ứng khi thành công
                                            list.get(i).setTitle(nasa.getTitle());
                                            list.get(i).setDate(nasa.getDate());
                                            list.get(i).setCopyright(nasa.getCopyright());
                                            //Cập nhật lại list
                                            notifyDataSetChanged();

                                            //Thông báo kết quả
                                            Toast.makeText(viewGroup.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            //Thoát dialog
                                            dia_log.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Status> call, Throwable t) {
                                        Toast.makeText(viewGroup.getContext(), "Call Api Server Update Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        //Sự kiện ấn thoát dialog
                        out.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Thoát dialog
                                dia_log.dismiss();
                            }
                        });
                        //Show dialog
                        dia_log.show();
                    }
                });

                //Nút xóa dữ liệu tương ứng
                builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Gọi interface Retrofit để xóa dữ liệu Nodejs
                        ApiRetrofitImage.apiRetrofit.deleteNasaUrl(list.get(i).getId()).enqueue(new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {
                                //Kết nối thành công
                                if (response.isSuccessful()) {
                                    //Xóa list tương ứng khi thành công
                                    list.remove(i);
                                    //Cập nhật lại list
                                    notifyDataSetChanged();

                                    //Thông báo kết quả
                                    Toast.makeText(viewGroup.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {
                                Toast.makeText(viewGroup.getContext(), "Call Api Server Delete Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                //hiển thị dialog
                builder.show();
            }
        });
        return view;
    }

    //Lớp khai báo
    private static class ViewHolder {
        TextView title, date, copyright;
    }
}
