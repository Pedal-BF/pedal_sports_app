package com.bicontest.pedal_sports_app.main_pages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bicontest.pedal_sports_app.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Bitmap bitmap;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item;
        TextView txt_main;
        //TextView txt_sub;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView_item = (ImageView) itemView.findViewById(R.id.youtube_image);
            txt_main = (TextView) itemView.findViewById(R.id.youtube_title);
            //txt_sub = (TextView) itemView.findViewById(R.id.txt_sub);
        }
    }

    private ArrayList<RecyclerViewItem> mList = null;

    public RecyclerViewAdapter(ArrayList<RecyclerViewItem> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.thumbnail_recycler_item, parent, false);
        RecyclerViewAdapter.ViewHolder vh = new RecyclerViewAdapter.ViewHolder(view);
        return vh;
    }

    // 유튜브 링크에서 key 값 추출
    public String youtubeKeyParse(String youtubeUrl) {
        String[] splited = youtubeUrl.split("/"); // splited[3]에 youtube key? 들어있음

        return splited[3];
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        RecyclerViewItem item = mList.get(position);

        holder.txt_main.setText(item.getMainText());  // 영상 제목
        //holder.txt_sub.setText(item.getSubText());

        Log.println(Log.DEBUG, "debug", "----------------------------------------------------------------");
        Log.println(Log.DEBUG, "Data", item.getImgURL() + " " + item.getMainText());

        // 영상 썸네일
        Thread mTread = new Thread() {
            @Override
            public void run() {
                try {
                    //Log.println(Log.DEBUG,"debug", "----------------------------------------------------------------");
                    String youtubeThumbnail = "https://img.youtube.com/vi/" + youtubeKeyParse(item.getImgURL()) + "/sddefault.jpg";  // 유튜브 영상 url을 썸네일 url로 변환
                    URL url = new URL(youtubeThumbnail);

                    // Youtube에서 이미지를 가져오고 ImageView에 저장할 Bitmap 생성
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setDoInput(true); // 서버로부터 응답 수신
                    conn.connect();

                    InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mTread.start();

        try {
            // join()을 호출하여, 별도의 작업 Thread가 작업을 완료할 때까지 메인 Thread가 기다리게 함
            mTread.join();

            // 작업 Thread에서 이미지 불러오는 작업을 완료한 뒤, 메인 Thread에서 ImageView에 이미지 지정
            holder.imgView_item.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
