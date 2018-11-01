package com.durzoflint.mrpuncture_shop.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.durzoflint.mrpuncture_shop.R;

import java.lang.ref.WeakReference;

public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View
        .OnLongClickListener {

    TextView name;
    TextView status;
    LinearLayout store;

    private WeakReference<RecyclerViewItemClickListener> listenerRef;

    View_Holder(View itemView, RecyclerViewItemClickListener listener) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        status = itemView.findViewById(R.id.status);
        store = itemView.findViewById(R.id.store);
        listenerRef = new WeakReference<>(listener);

        store.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listenerRef.get().onClick(view, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}