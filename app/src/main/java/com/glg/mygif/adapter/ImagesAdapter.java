package com.glg.mygif.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glg.mygif.R;
import com.glg.mygif.entity.ImageItem;

import java.util.List;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: ImagesAdapter
 * @author: gao
 * @date: 2020/8/10 18:55
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private List<ImageItem> list;

    //private Bitmap bitmap;

    private ImageItem imageItem;

    private Fragment fragment;

    public ImagesAdapter(Fragment fragment, List<ImageItem> list) {
        this.fragment = fragment;
        this.list = list;
    }

    public void setList(List<ImageItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        final ImagesAdapter.ViewHolder holder = new ImagesAdapter.ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
//        holder.setIsRecyclable(false);
        imageItem = list.get(holder.getLayoutPosition());

//        try {
//            new GetBitmap().execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        holder.imageView.setImageBitmap(bitmap);

        Glide.with(fragment).load("https://cn.bing.com/" + imageItem.getUrl()).centerCrop().into(holder.imageView);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageItem imageItem = list.get(holder.getLayoutPosition());
                if(imageItem.isLike()) {
                    imageItem.setLikeCount(imageItem.getLikeCount()-1);
                    imageItem.setLike(false);
                    ImagesAdapter.this.notifyItemChanged(holder.getLayoutPosition());
                    //System.out.println(position + "..." + holder.getLayoutPosition() + "..." + holder.getAdapterPosition() + "..." + holder.getOldPosition() + "..." + holder.getPosition());
                } else {
                    imageItem.setLikeCount(imageItem.getLikeCount()+1);
                    imageItem.setLike(true);
                    ImagesAdapter.this.notifyItemChanged(holder.getLayoutPosition());
                    //System.out.println(position + "..." + holder.getLayoutPosition() + "..." + holder.getAdapterPosition() + "..." + holder.getOldPosition() + "..." + holder.getPosition());
                }
            }
        });

        if(!imageItem.isLike()) {
            holder.like.setImageResource(R.drawable.like);
        } else {
            holder.like.setImageResource(R.drawable.like_red);
        }

        holder.like_count.setText(String.valueOf(imageItem.getLikeCount()));
        holder.textView.setText(imageItem.getCopyright());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        ImageView like;
        TextView like_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            like = itemView.findViewById(R.id.like);
            like_count = itemView.findViewById(R.id.like_count);
        }
    }

//    private class GetBitmap extends AsyncTask {
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            try {
//                HttpURLConnection httpConnection = (HttpURLConnection) (new URL("https://cn.bing.com/" + imageItem.getUrl()).openConnection());
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

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
