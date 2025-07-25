package com.fadli.aplikasilaundry.view.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fadli.aplikasilaundry.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    private OnItemClickListener listener;
    List<ModelMenu> menuList;
    Context context;

    public MenuAdapter(Context context, List<ModelMenu> items) {
        this.context = context;
        this.menuList = items;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu, parent, false);
        return new MenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MenuAdapter.ViewHolder holder, int position) {
        final ModelMenu data = menuList.get(position);

        holder.imageMenu.setImageResource(data.getImageDrawable());
        holder.tvTitle.setText(data.getTvTitle());
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cvMenu;
        public TextView tvTitle;
        public ImageView imageMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            cvMenu = itemView.findViewById(R.id.cvMenu);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imageMenu = itemView.findViewById(R.id.imageMenu);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(menuList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ModelMenu modelMenu);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}