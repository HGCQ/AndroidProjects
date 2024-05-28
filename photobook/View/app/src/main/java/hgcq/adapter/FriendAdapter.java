package hgcq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hgcq.controller.MemberController;
import hgcq.model.dto.MemberDTO;
import hgcq.view.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private MemberController mc;
    private Context context;
    private List<MemberDTO> friendList;

    public FriendAdapter(Callback<List<MemberDTO>> context, List<MemberDTO> friendList) {
        this.friendList = friendList;
        mc = new MemberController((Context) context);
        this.context = (Context) context;
    }
//qweqeqweasdasdas
    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;
        public ImageButton delete;

        public FriendViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            email = view.findViewById(R.id.email);
            delete = view.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
        }
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View friendView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(friendView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        final int pos=position;
        MemberDTO member = friendList.get(pos);
        holder.name.setText(member.getName());
        holder.email.setText(member.getEmail());
        holder.delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mc.deleteFriend(member, new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    friendList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, friendList.size());
                                    Toast.makeText(context, "친구 삭제 완료!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "친구 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

        );

    }



    @Override
    public int getItemCount() {
        return friendList.size();
    }
}

