package com.fadli.aplikasilaundry.view.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fadli.aplikasilaundry.R;
import com.fadli.aplikasilaundry.model.ModelLaundry;
import com.fadli.aplikasilaundry.utils.FunctionHelper;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<ModelLaundry> modelInputList;
    Context mContext;
    HistoryAdapterCallback mAdapterCallback;

    public HistoryAdapter(Context context, List<ModelLaundry> modelInputList, HistoryAdapterCallback adapterCallback) {
        this.mContext = context;
        this.modelInputList = modelInputList;
        this.mAdapterCallback = adapterCallback;
    }

    public void setDataAdapter(List<ModelLaundry> items) {
        modelInputList.clear();
        modelInputList.addAll(items);
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        final ModelLaundry data = modelInputList.get(position);

        holder.tvTitle.setText(data.getNamaJasa());
        holder.tvDate.setText(FunctionHelper.getToday());
        holder.tvItems.setText(data.getItems() + " Items");
        holder.tvPrice.setText(FunctionHelper.rupiahFormat(data.getHarga()));
    }

    @Override
    public int getItemCount() {
        return modelInputList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle, tvDate, tvItems, tvPrice;
        public ImageView imageDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvItems = itemView.findViewById(R.id.tvItems);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imageDelete = itemView.findViewById(R.id.imageDelete);

            imageDelete.setOnClickListener(view -> {
                ModelLaundry modelLaundry = modelInputList.get(getAdapterPosition());
                mAdapterCallback.onDelete(modelLaundry);
            });
        }
    }

    public interface HistoryAdapterCallback {
        void onDelete(ModelLaundry modelLaundry);
    }

}