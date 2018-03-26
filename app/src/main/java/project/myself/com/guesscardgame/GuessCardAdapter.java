package project.myself.com.guesscardgame;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * @类说明 猜牌适配器
 */

public class GuessCardAdapter extends RecyclerView.Adapter<GuessCardAdapter.GuessCardViewHolder> {

    private List<Integer> imagesList;
    public static final int SHOW_CARD_FOWARD = 0;
    public static final int SHOW_CARD_BACK = 1;
    private int showCardType = -1;


    public GuessCardAdapter(List<Integer> imagesList) {
        this.imagesList = imagesList;
    }

    @Override
    public GuessCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guess_card, parent, false);
        return new GuessCardViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return imagesList == null ? 0 : imagesList.size();
    }


    public void setImages(List<Integer> imagesList) {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    public void setForwardImageAlpha(int type) {
        this.showCardType = type;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(GuessCardViewHolder holder, int position) {
        holder.ivCard.setImageResource(imagesList.get(position));
        holder.ivCardForward.setAlpha(0f);

        if (showCardType == SHOW_CARD_FOWARD)
            holder.ivCardForward.setAlpha(0f);
        else if (showCardType == SHOW_CARD_BACK){
            holder.ivCardForward.setAlpha(1f);
            holder.ivCardForward.setImageResource(R.mipmap.picture7);
        }


    }

    class GuessCardViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCard;
        ImageView ivCardForward;

        public GuessCardViewHolder(View itemView) {
            super(itemView);
            ivCard = (ImageView) itemView.findViewById(R.id.iv_card);
            ivCardForward = (ImageView) itemView.findViewById(R.id.iv_card_forward);
        }
    }
}
