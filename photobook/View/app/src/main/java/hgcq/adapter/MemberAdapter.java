package hgcq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hgcq.controller.EventController;
import hgcq.model.dto.MemberDTO;
import hgcq.view.R;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<MemberDTO> memberList;

    public MemberAdapter(List<MemberDTO> memberList) {
        this.memberList = memberList;
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MemberViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
        }
    }

    public void addFriend(MemberDTO newFriend) {
        this.memberList.add(newFriend);
        notifyItemInserted(memberList.size() - 1);
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View eventView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(eventView);

    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        MemberDTO member = memberList.get(position);
        holder.name.setText(member.getName());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}

