package project.myself.com.guesscardgame;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @类说明 猜牌结果适配器
 */

public class GuessCardResultShowAdapter extends RecyclerView.Adapter<GuessCardResultShowAdapter.GuessCardResultViewHolder> {

    private List<GuessCardResult> list;

    public GuessCardResultShowAdapter(List<GuessCardResult> list) {
        this.list = list;
    }

    @Override
    public GuessCardResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guess_card_result, parent, false);
        return new GuessCardResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuessCardResultViewHolder holder, int position) {

        GuessCardResult data = list.get(position);
        holder.tvResult.setText(data.getResult());

        int whiteNumber = data.getWhiteNumber();
        int blackNumber = data.getBlackNumber();

        //白色标志
        for (int i = 0; i < whiteNumber && whiteNumber > 0; i++) {
            ImageView iv = new ImageView(holder.itemView.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = DensityUtil.dip2px(holder.itemView.getContext(), 2);
            iv.setImageResource(R.drawable.flag_white);
            iv.setLayoutParams(params);
            holder.llWhiteContainer.addView(iv);
        }

        //黑色标记
        for (int i = 0; i < blackNumber && blackNumber > 0; i++) {
            ImageView iv = new ImageView(holder.itemView.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = DensityUtil.dip2px(holder.itemView.getContext(), 2);
            iv.setImageResource(R.drawable.flag_black);
            iv.setLayoutParams(params);
            holder.llBlackContainer.addView(iv);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class GuessCardResultViewHolder extends RecyclerView.ViewHolder {

        TextView tvResult;
        LinearLayout llWhiteContainer;
        LinearLayout llBlackContainer;

        public GuessCardResultViewHolder(View itemView) {
            super(itemView);
            tvResult = (TextView) itemView.findViewById(R.id.tv_result);
            llWhiteContainer = (LinearLayout) itemView.findViewById(R.id.ll_white_container);
            llBlackContainer = (LinearLayout) itemView.findViewById(R.id.ll_black_container);
        }
    }
}
