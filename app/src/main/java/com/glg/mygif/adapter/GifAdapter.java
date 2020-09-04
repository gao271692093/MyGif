package com.glg.mygif.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glg.mygif.entity.GifItem;
import com.glg.mygif.R;
import com.glg.mygif.activity.GifActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: GifAdapter
 * @author: gao
 * @date: 2020/8/6 23:30
 */
class GifAdapter extends RecyclerView.Adapter<GifAdapter.ViewHolder> implements View.OnClickListener {

    private List<GifItem> list;

    private GifItem gifItem;

    private Bitmap bitmap;

    private Context context;

    public GifAdapter(Context context, List<GifItem> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, null);
        GifAdapter.ViewHolder holder = new GifAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        gifItem = list.get(position);
        try {
            new GetBitmap().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        holder.imageView.setImageBitmap(bitmap);
        //holder.imageView.setOnClickListener(this);
        holder.textView.setText(gifItem.getNickname());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, GifActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("gifUrl", gifItem.getGif());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            //textView = itemView.findViewById(R.id.textView);
        }
    }

    private class GetBitmap extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) (new URL(gifItem.getCover()).openConnection());
                httpConnection.setDoInput(true);
                httpConnection.connect();
                InputStream inputStream = httpConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
