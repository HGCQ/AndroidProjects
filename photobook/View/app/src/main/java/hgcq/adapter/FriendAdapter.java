package hgcq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hgcq.controller.MemberController;
import hgcq.model.dto.MemberDTO;
import hgcq.view.R;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private Context context;
    private List<MemberDTO> friendList;
    private OnFriendClickListener listener;

    public FriendAdapter(Context context, List<MemberDTO> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    public interface OnFriendClickListener {
        void onButtonClick(String name, String email);
    }

    public void setOnFriendClickListener(OnFriendClickListener listener) {
        this.listener = listener;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;
        public Button deleteButton;

        public FriendViewHolder(View view) {
            super(view);
//            name = view.findViewById(R.id.name);
//            email = view.findViewById(R.id.email);
//            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View friendView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_friend, parent, false);
//        return new FriendViewHolder(friendView);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        MemberDTO friend = friendList.get(position);
        holder.name.setText(friend.getName());
        holder.email.setText(friend.getEmail());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onButtonClick(friend.getName(), friend.getEmail());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }
}
