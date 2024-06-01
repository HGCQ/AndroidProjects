package hgcq.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hgcq.model.dto.MemberDTO;
import hgcq.view.R;

public class EventInviteAdapter extends RecyclerView.Adapter<EventInviteAdapter.EventInviteViewHolder> {

    private List<MemberDTO> friendList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public EventInviteAdapter(List<MemberDTO> friendList) {
        this.friendList = friendList;
    }

    public static class EventInviteViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public EventInviteViewHolder(View view, OnItemClickListener listener) {
            super(view);
            name = view.findViewById(R.id.name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v, position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public EventInviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View eventInviteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new EventInviteViewHolder(eventInviteView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventInviteViewHolder holder, int position) {
        MemberDTO friend = friendList.get(position);
        holder.name.setText(friend.getName());
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }
}
