package com.juandiegovqdev.flickrandroidapp.models;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juandiegovqdev.flickrandroidapp.R;
import com.juandiegovqdev.flickrandroidapp.utils.DeviceData;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class Item extends AbstractItem<Item, Item.ViewHolder> {

    public String title;
    public String link;
    public FlickrModel.Media media;
    public String dateTaken;
    public String description;
    public String published;
    public String author;
    public String authorId;
    public String tags;
    public long id;

    public long getId() {
        try {
            id = Long.valueOf(Uri.parse(link).getLastPathSegment() + "");
        } catch (NullPointerException npe) {
            id = new Random().nextLong();
            npe.printStackTrace();
        }
        return id;
    }

    @Override
    public int getType() {
        return R.id.recycler_view;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.grid_item;
    }

    @Override
    public void bindView(Item.ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        Picasso.get().load(media.m).into(holder.imageView);
        holder.titleTv.setText(title);
        holder.authorTv.setText(author);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTv;
        TextView authorTv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            titleTv = itemView.findViewById(R.id.title);
            authorTv = itemView.findViewById(R.id.author);
            imageView.getLayoutParams().width = DeviceData.getInstance().getDisplayWidth() / 2;
            imageView.getLayoutParams().height = DeviceData.getInstance().getDisplayWidth() / 2;
        }
    }
}
