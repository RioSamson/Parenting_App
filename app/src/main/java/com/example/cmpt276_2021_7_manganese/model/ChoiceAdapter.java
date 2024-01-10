package com.example.cmpt276_2021_7_manganese.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cmpt276_2021_7_manganese.FlipCoinActivity;
import com.example.cmpt276_2021_7_manganese.R;
import java.util.List;

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.CViewHoleer> {
    private Context context;
    private List<CoinResult> coinResults;

    public ChoiceAdapter(Context context, List<CoinResult> coinResults) {
        this.context = context;
        this.coinResults = coinResults;
    }

    @NonNull
    @Override
    public CViewHoleer onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_choice,parent,false);
        CViewHoleer holder = new CViewHoleer(view);
        return holder;
    }

    @Override
    public void onBindViewHolder( CViewHoleer holder, int position) {
        CoinResult result = coinResults.get(position);
        holder.time.setText(result.time);
        holder.result.setText(result.result);
        holder.choice.setText(result.currentChose);
        holder.user.setText(result.user);
        if (!coinResults.get(position).currentChose.equals("--")){
            Glide.with(this.context).load(coinResults.get(position).photo).placeholder(R.mipmap.default_head)
                    .error(R.mipmap.default_head).into(holder.user_header);
        }
    }

    @Override
    public int getItemCount() {
        return coinResults==null?0:coinResults.size();
    }

    class CViewHoleer extends RecyclerView.ViewHolder{
        private TextView choice,result,time,user;
        private ImageView user_header;
        public CViewHoleer(View itemView) {
            super(itemView);
            choice = itemView.findViewById(R.id.choice);
            result = itemView.findViewById(R.id.result);
            time = itemView.findViewById(R.id.time);
            user = itemView.findViewById(R.id.user);
            user_header = itemView.findViewById(R.id.user_header);
        }
    }
}
