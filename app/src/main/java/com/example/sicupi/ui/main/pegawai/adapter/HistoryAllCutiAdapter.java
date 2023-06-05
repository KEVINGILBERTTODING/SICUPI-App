package com.example.sicupi.ui.main.pegawai.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicupi.R;
import com.example.sicupi.data.model.CutiModel;

import java.util.List;

public class HistoryAllCutiAdapter extends RecyclerView.Adapter<HistoryAllCutiAdapter.ViewHolder> {
    Context context;
    List<CutiModel> cutiModelList;

    public HistoryAllCutiAdapter(Context context, List<CutiModel> cutiModelList) {
        this.context = context;
        this.cutiModelList = cutiModelList;
    }

    @NonNull
    @Override
    public HistoryAllCutiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_cuti, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAllCutiAdapter.ViewHolder holder, int position) {
        holder.tvJenisCuti.setText(cutiModelList.get(holder.getAdapterPosition()).getKeterangan());
        holder.tvMulai.setText(cutiModelList.get(holder.getAdapterPosition()).getMulaiCuti());
        holder.tvSelesai.setText(cutiModelList.get(holder.getAdapterPosition()).getAkhirCuti());

        if (cutiModelList.get(holder.getAdapterPosition()).getVerifikasi() == 1) {
            holder.icStatus.setImageDrawable(context.getDrawable(R.drawable.ic_setuju));
        }else if (cutiModelList.get(holder.getAdapterPosition()).getVerifikasi() == 2) {
            holder.icStatus.setImageDrawable(context.getDrawable(R.drawable.ic_tolak));
        }else {
            holder.icStatus.setImageDrawable(context.getDrawable(R.drawable.ic_proses));
        }

    }

    @Override
    public int getItemCount() {
        return cutiModelList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJenisCuti, tvMulai, tvSelesai;
        ImageView icStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJenisCuti = itemView.findViewById(R.id.tvJenisCuti);
            tvMulai = itemView.findViewById(R.id.tvMulaiCuti);
            tvSelesai = itemView.findViewById(R.id.tvSelesaiCuti);
            icStatus = itemView.findViewById(R.id.icStatus);
        }
    }


}
