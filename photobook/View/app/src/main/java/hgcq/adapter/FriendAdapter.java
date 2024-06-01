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

    private MemberController mc; // MemberController 인스턴스
    private Context context; // 액티비티의 컨텍스트
    private List<MemberDTO> friendList; // 친구 목록

    /**
     * FriendAdapter 생성자
     * context 액티비티의 컨텍스트
     * friendList 친구 목록
     */
    public FriendAdapter(Context context, List<MemberDTO> friendList) {
        this.friendList = friendList;
        mc = new MemberController(context); // MemberController 초기화
        this.context = context; // 컨텍스트 설정
    }

    /**
     * FriendViewHolder 클래스
     * 친구 아이템의 뷰 홀더
     */
    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView name; // 친구 이름을 표시하는 TextView
        public ImageButton delete; // 친구 삭제 버튼

        public FriendViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name); // 뷰에서 이름 TextView 찾기
            delete = view.findViewById(R.id.delete); // 뷰에서 삭제 버튼 찾기
        }
    }

    /**
     * 새로운 친구 추가
     *  newFriend 새로운 친구 정보
     */
    public void addFriend(MemberDTO newFriend) {
        this.friendList.add(newFriend); // 새로운 친구 추가
        notifyItemInserted(friendList.size() - 1); // RecyclerView에 새로운 아이템 추가 알림
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View friendView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false); // 친구 아이템 뷰 생성
        return new FriendViewHolder(friendView); // 새로운 FriendViewHolder 반환
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        final int pos = position; // 현재 위치 저장
        MemberDTO member = friendList.get(pos); // 현재 위치의 친구 정보 가져오기
        holder.name.setText(member.getName()); // 이름 TextView에 친구 이름 설정
        holder.delete.setOnClickListener( // 삭제 버튼에 클릭 리스너 설정
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mc.deleteFriend(member, new Callback<ResponseBody>() { // MemberController를 사용하여 친구 삭제 요청
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) { // 성공적으로 응답을 받았을 때
                                    friendList.remove(position); // 친구 목록에서 삭제
                                    notifyItemRemoved(position); // RecyclerView에서 아이템 삭제 알림
                                    notifyItemRangeChanged(position, friendList.size()); // 아이템 범위 변경 알림
                                    Toast.makeText(context, "친구 삭제 완료!", Toast.LENGTH_SHORT).show(); // 삭제 완료 메시지 표시
                                } else {
                                    Toast.makeText(context, "친구 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show(); // 삭제 실패 메시지 표시
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show(); // 네트워크 오류 메시지 표시
                            }
                        });
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return friendList.size(); // 친구 목록의 크기 반환
    }
}
