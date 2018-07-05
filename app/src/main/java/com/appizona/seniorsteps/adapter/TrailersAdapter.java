package com.appizona.seniorsteps.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appizona.seniorsteps.R;
import com.appizona.seniorsteps.callback.OnTrailerClickListener;
import com.appizona.seniorsteps.model.Trailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersHolder> {

    private Context mContext;
    private List<Trailer> list;
    private OnTrailerClickListener onTrailerClickListener;

    public TrailersAdapter(Context mContext, List<Trailer> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setOnTrailerClickListener(OnTrailerClickListener listener) {
        this.onTrailerClickListener = listener;
    }

    @Override
    public TrailersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_trailer, parent, false);
        return new TrailersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersHolder holder, int position) {

        final Trailer trailer = list.get(position);

        holder.trailerTitle.setText(trailer.getName());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTrailerClickListener.onTrailerClicked(trailer.getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TrailersHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_title)
        TextView trailerTitle;
        @BindView(R.id.root)
        LinearLayout root;

        public TrailersHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
