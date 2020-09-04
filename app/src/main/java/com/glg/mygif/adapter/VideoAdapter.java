package com.glg.mygif.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glg.mygif.R;
import com.glg.mygif.entity.RecommendEntity;

import java.util.List;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: videoAdapter
 * @author: gao
 * @date: 2020/8/6 16:31
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private RecommendEntity.ListBean listBean;

    //private Bitmap bitmap;

    private List<RecommendEntity.ListBean> itemList;

    //private AsyncTask myAsyncTask;

    private Context context;
    private Fragment fragment;
    private Bundle bundle;

    public void setItemList(List<RecommendEntity.ListBean> itemList) {
        this.itemList = itemList;
    }

    public VideoAdapter(Fragment fragment, List<RecommendEntity.ListBean> itemList) {
        this.fragment = fragment;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        bundle = ActivityOptions.makeSceneTransitionAnimation(fragment.getActivity(),
                                new Pair<View, String>(v.findViewById(R.id.imageView), "sharedElement"),
                                new Pair<View, String>(v.findViewById(R.id.textView), "sharedElement1")).toBundle();
                    }
                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        listBean = itemList.get(holder.getLayoutPosition());

//        try {
//            myAsyncTask = new GetBitmap();
//            myAsyncTask.execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        holder.imageView.setImageBitmap(bitmap);

        Glide.with(fragment).load(listBean.getCoverurl()).into(holder.imageView);

        holder.textView.setText(listBean.getName());
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendEntity.ListBean listBean = itemList.get(holder.getLayoutPosition());
                if(listBean.isLike()) {
                    listBean.setLikeCount(listBean.getLikeCount() - 1);
                    listBean.setLike(false);
                } else {
                    listBean.setLikeCount(listBean.getLikeCount() + 1);
                    listBean.setLike(true);
                }
                notifyItemChanged(holder.getLayoutPosition());
            }
        });
        if(listBean.isLike()) {
            holder.like.setImageResource(R.drawable.like_red);
            holder.like_count.setText(String.valueOf(listBean.getLikeCount()));
        } else {
            holder.like.setImageResource(R.drawable.like);
            holder.like_count.setText(String.valueOf(listBean.getLikeCount()));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;
        TextView textView;
        ImageView like;
        TextView like_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            like = itemView.findViewById(R.id.like);
            like_count = itemView.findViewById(R.id.like_count);
        }
    }

//    private class GetBitmap extends AsyncTask{
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            try {
//                HttpURLConnection httpConnection = (HttpURLConnection) (new URL(listBean.getCoverurl()).openConnection());
//                httpConnection.setDoInput(true);
//                httpConnection.connect();
//                InputStream inputStream = httpConnection.getInputStream();
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                inputStream.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

    private ImagesAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(ImagesAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public Bundle getBundle() {
        return bundle;
    }
}
