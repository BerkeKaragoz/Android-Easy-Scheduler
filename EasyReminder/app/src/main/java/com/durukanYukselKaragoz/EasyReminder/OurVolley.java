package com.durukanYukselKaragoz.EasyReminder;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class OurVolley {
    RequestQueue queue;
    Context context;

    public OurVolley(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    // Image Request by using Volley
    public void requestForBinaryData(String urlString) {
        // To request binary data
        // To download an image from a remote server, ImageRequest can be used
        ImageRequest imageRequest = new ImageRequest(urlString,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ((MainActivity)context).setBitmapImage(response);

                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "ERROR",
                                Toast.LENGTH_SHORT).show();
                        error.printStackTrace();

                    }
                });
        queue.add(imageRequest);
    }
}
