package com.durzoflint.mrpuncture_shop.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.durzoflint.mrpuncture_shop.R;

import java.util.List;

public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder> {

    private final RecyclerViewItemClickListener listener;
    private List<Store> list;
    private Context context;

    public Recycler_View_Adapter(List<Store> list, Context context, RecyclerViewItemClickListener
            listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_request,
                parent, false);
        return new View_Holder(v, listener);
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        holder.name.setText(list.get(position).name);
        holder.status.setText(list.get(position).status);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, Store stores) {
        list.add(position, stores);
        notifyItemInserted(position);
    }

    public void remove(Store stores) {
        int position = list.indexOf(stores);

        list.remove(position);
        notifyItemRemoved(position);
    }

}