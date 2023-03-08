package com.example.asynctaskproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class ImageFragment extends Fragment {
    Button readButton;
    ImageView imageView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false); }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readButton = view.findViewById(R.id.readbutton);
        imageView = view.findViewById(R.id.imageView);

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ONCLICK", "IMAGE SHOULD BE LOADING");
                String imageUrl = getString(R.string.imageURL);
                ImageDownloader imageDownloader = new ImageDownloader(getActivity());
                imageDownloader.execute(imageUrl);
            }});

    }
}