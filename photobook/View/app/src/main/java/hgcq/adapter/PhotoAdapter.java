package hgcq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ImageViewHolder> {
    private Context context;
    private List<String> photoUrls;
    private final String serverIp = "서버 주소";

    public PhotoAdapter(Context context, List<String> photoUrls) {
        this.context = context;
        this.photoUrls = photoUrls;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View photoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_layout, parent, false);
//        return new ImageViewHolder(photoView);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String photoUrl = serverIp + photoUrls.get(position);
        Glide.with(context).load(photoUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photoUrls.size();
    }
}