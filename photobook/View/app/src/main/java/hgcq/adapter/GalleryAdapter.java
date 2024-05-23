package hgcq.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hgcq.config.NetworkClient;
import hgcq.view.R;
import hgcq.view.activity.Photo;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder> {
    private Context context;
    private List<String> photoUrls;
    private String serverIp;
    private String date;
    private String title;
    private String content;

    public GalleryAdapter(Context context, List<String> photoUrls, String date, String title, String content) {
        this.context = context;
        this.photoUrls = photoUrls;
        this.serverIp = NetworkClient.getInstance(context).getServerIp();
        this.date = date;
        this.title = title;
        this.content = content;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Glide.with(context)
                .load(serverIp + photoUrls.get(position))
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Photo.class);
            intent.putExtra("position", position);
            intent.putExtra("date", date);
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return photoUrls.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photoItem);
        }
    }
}