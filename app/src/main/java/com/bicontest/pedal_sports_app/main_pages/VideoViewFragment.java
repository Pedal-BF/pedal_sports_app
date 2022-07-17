package com.bicontest.pedal_sports_app.main_pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bicontest.pedal_sports_app.R;

public class VideoViewFragment extends Fragment {

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성
    public static VideoViewFragment newInstance() {
        return new VideoViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_view, container, false);

        return v;
    }
}