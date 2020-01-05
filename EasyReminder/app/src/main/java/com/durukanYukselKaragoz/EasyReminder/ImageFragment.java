package com.durukanYukselKaragoz.EasyReminder;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    ImageView imageView;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.imageView);
    }

    public void changeImage(String category){
        if(category.equalsIgnoreCase("OTHER"))
            imageView.setImageResource(R.drawable.other);
        if(category.equalsIgnoreCase("BIRTHDAY"))
            imageView.setImageResource(R.drawable.birthday);
        if(category.equalsIgnoreCase("BUSINESS"))
            imageView.setImageResource(R.drawable.business);
        if(category.equalsIgnoreCase("MEETING"))
            imageView.setImageResource(R.drawable.meeting);
    }
}
