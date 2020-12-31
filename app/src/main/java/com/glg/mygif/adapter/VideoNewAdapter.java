package com.glg.mygif.adapter;

import android.app.ActivityOptions;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.glg.mygif.R;
import com.glg.mygif.entity.VideoEntity;

import java.util.List;

/**
 * Description:
 *
 * @package: com.glg.mygif.adapter
 * @className: VideoNewAdapter
 * @author: gao
 * @date: 2020/9/11 16:25
 */
public class VideoNewAdapter extends RecyclerView.Adapter<VideoNewAdapter.ViewHolder> {

    private List<VideoEntity.ResultBean> resultBeanList;

    private VideoEntity.ResultBean resultBean;

    private Fragment fragment;

    private Bundle bundle;

    private int width;

    public VideoNewAdapter(Fragment fragment, List<VideoEntity.ResultBean> resultBeanList, int width) {
        this.fragment = fragment;
        this.resultBeanList = resultBeanList;
        this.width = width;
    }

    @NonNull
    @Override
    public VideoNewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent,false);
        final VideoNewAdapter.ViewHolder holder = new VideoNewAdapter.ViewHolder(view);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        bundle = ActivityOptions.makeSceneTransitionAnimation(fragment.getActivity(),
                                new Pair<View, String>(v, "sharedElement")).toBundle();
                    }
                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoNewAdapter.ViewHolder holder, int position) {
        resultBean = resultBeanList.get(holder.getLayoutPosition());
        RequestBuilder<Drawable> load = Glide.with(fragment).load(resultBean.getThumbnail());
        int originalWidth = load.getOverrideWidth();
        int originalHeight = load.getOverrideHeight();
        System.out.println(originalWidth + "=================" + originalHeight);
        load.override(width, calculateHeight(originalWidth, originalHeight)).into(holder.imageView);
        Glide.with(fragment).load(resultBean.getHeader()).transform(new CircleCrop()).into(holder.header);
        holder.name.setText(resultBean.getName());
        holder.like_count.setText(resultBean.getUp());
        holder.textView.setText(resultBean.getText());
        if(resultBean.isLike()) {
            holder.like.setImageResource(R.drawable.like_red);
        } else {
            holder.like.setImageResource(R.drawable.like);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoEntity.ResultBean resultBean = resultBeanList.get(holder.getLayoutPosition());
                if(!resultBean.isLike()) {
                    resultBean.setUp(String.valueOf(Integer.parseInt(resultBean.getUp()) + 1));
                    resultBean.setLike(true);
                    notifyItemChanged(holder.getLayoutPosition());
                } else {
                    resultBean.setUp(String.valueOf(Integer.parseInt(resultBean.getUp()) - 1));
                    resultBean.setLike(false);
                    notifyItemChanged(holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultBeanList.size();
    }

    public List<VideoEntity.ResultBean> getResultBeanList() {
        return resultBeanList;
    }

    public void setResultBeanList(List<VideoEntity.ResultBean> resultBeanList) {
        this.resultBeanList = resultBeanList;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;
        TextView textView;
        ImageView header;
        ImageView like;
        TextView name;
        TextView like_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            header = itemView.findViewById(R.id.header);
            name = itemView.findViewById(R.id.name);
            like_count = itemView.findViewById(R.id.like_count);
            like = itemView.findViewById(R.id.like);
        }
    }

    private ImagesAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(ImagesAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private int calculateHeight(int originalWidth, int originalHeight) {
        return width * originalHeight / originalWidth;
    }
}
