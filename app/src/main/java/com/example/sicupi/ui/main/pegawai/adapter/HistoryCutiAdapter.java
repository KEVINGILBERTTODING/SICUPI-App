package com.example.sicupi.ui.main.pegawai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicupi.R;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.util.Constants;

import java.util.List;

public class HistoryCutiAdapter extends RecyclerView.Adapter<HistoryCutiAdapter.ViewHolder> {
    Context context;
    List<CutiModel> cutiModelList;

    public HistoryCutiAdapter(Context context, List<CutiModel> cutiModelList) {
        this.context = context;
        this.cutiModelList = cutiModelList;
    }

    @NonNull
    @Override
    public HistoryCutiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_history_cuti, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryCutiAdapter.ViewHolder holder, int position) {
        holder.tvJenisCuti.setText(cutiModelList.get(holder.getAdapterPosition()).getKeterangan());
        holder.tvMulai.setText(cutiModelList.get(holder.getAdapterPosition()).getMulaiCuti());
        holder.tvSelesai.setText(cutiModelList.get(holder.getAdapterPosition()).getAkhirCuti());

    }

    @Override
    public int getItemCount() {
        return cutiModelList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJenisCuti, tvMulai, tvSelesai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJenisCuti = itemView.findViewById(R.id.tvJenisCuti);
            tvMulai = itemView.findViewById(R.id.tvMulaiCuti);
            tvSelesai = itemView.findViewById(R.id.tvSelesaiCuti);
        }
    }
}
