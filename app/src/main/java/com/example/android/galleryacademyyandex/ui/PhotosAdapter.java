package com.example.android.galleryacademyyandex.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.galleryacademyyandex.R;
import com.example.android.galleryacademyyandex.model.dto.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private static List<Photo> mItemList;
    private final Context mContext;

    public PhotosAdapter(@NonNull Context context, List<Photo> itemList) {
        mItemList = itemList;
        mContext = context;
    }

    @NonNull
    @Override
    public PhotosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Photo photo = mItemList.get(position);
        setImagePicasso(photo, holder.cardImage, holder);
        holder.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PhotoActivity.class);
                intent.putExtra(PhotoActivity.INTENT_PHOTO, photo.getSrc().getLarge());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mItemList == null) return 0;
        return mItemList.size();
    }

    private void setImagePicasso(Photo photo, ImageView cardImage, ViewHolder holder) {
        Picasso.with(holder.cardImage.getContext())
                .load(photo.getSrc().getMedium() + "&w=600")
                .placeholder(R.drawable.placeholder_image)
                .into(cardImage);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView cardImage;

        ViewHolder(@NonNull View view) {
            super(view);
            cardImage = view.findViewById(R.id.iv_photo);
        }
    }
}