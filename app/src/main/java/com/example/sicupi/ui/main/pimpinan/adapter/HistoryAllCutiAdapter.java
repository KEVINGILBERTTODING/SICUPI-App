package com.example.sicupi.ui.main.pimpinan.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicupi.R;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.util.Constants;

import java.util.ArrayList;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_cuti_pimpinan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvJenisCuti.setText(cutiModelList.get(holder.getAdapterPosition()).getKeterangan());
        holder.tvMulai.setText(cutiModelList.get(holder.getAdapterPosition()).getMulaiCuti());
        holder.tvSelesai.setText(cutiModelList.get(holder.getAdapterPosition()).getAkhirCuti());
        holder.tvNama.setText(cutiModelList.get(holder.getAdapterPosition()).getNama());



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

    public void filter(ArrayList<CutiModel> filteredList) {
        cutiModelList = filteredList;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvJenisCuti, tvMulai, tvSelesai, tvNama;
        ImageView icStatus;
        LinearLayout lrPerihal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJenisCuti = itemView.findViewById(R.id.tvJenisCuti);
            tvMulai = itemView.findViewById(R.id.tvMulaiCuti);
            tvSelesai = itemView.findViewById(R.id.tvSelesaiCuti);
            icStatus = itemView.findViewById(R.id.icStatus);
            tvNama = itemView.findViewById(R.id.tvNama);
            lrPerihal = itemView.findViewById(R.id.linar_perihal);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.layout_detail_cuti_history_pimpinan);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView tvJenisCuti, tvStatusCuti, tvTanggalMulai, tvTanggalSelesai, tvPerihal;
            CardView cvStatus;
            Button btnDownloadLampiran;
            tvJenisCuti = dialog.findViewById(R.id.tvJenisCuti);
            tvStatusCuti = dialog.findViewById(R.id.tvStatus);
            tvTanggalMulai = dialog.findViewById(R.id.tvTglAwal);
            tvTanggalSelesai = dialog.findViewById(R.id.tvTglSelesai);
            tvPerihal = dialog.findViewById(R.id.tvPerihal);
            cvStatus = dialog.findViewById(R.id.cvCutiStatus);
            btnDownloadLampiran = dialog.findViewById(R.id.btnDownloadLampiran);

            tvJenisCuti.setText(cutiModelList.get(getAdapterPosition()).getKeterangan());
            tvTanggalMulai.setText(cutiModelList.get(getAdapterPosition()).getMulaiCuti());
            tvTanggalSelesai.setText(cutiModelList.get(getAdapterPosition()).getAkhirCuti());
            tvPerihal.setText(cutiModelList.get(getAdapterPosition()).getPerihal());

            if (cutiModelList.get(getAdapterPosition()).getKeterangan().equals("Cuti Sakit < 14")) {
                btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadSuratLampiran(
                                String.valueOf(cutiModelList.get(getAdapterPosition()).getCutiId()), "kurang"
                        );
                    }
                });

            }else  if (cutiModelList.get(getAdapterPosition()).getKeterangan().equals("Cuti Sakit > 14")) {
                btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadSuratLampiran(
                                String.valueOf(cutiModelList.get(getAdapterPosition()).getCutiId()), "lebih"
                        );
                    }
                });

            }else  if (cutiModelList.get(getAdapterPosition()).getKeterangan().equals("Cuti Melahirkan")) {

              btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      downloadSuratLampiran(
                              String.valueOf(cutiModelList.get(getAdapterPosition()).getCutiId()), "melahirkan"
                      );
                  }
              });

            }else  if (cutiModelList.get(getAdapterPosition()).getKeterangan().equals("Cuti Alasan Penting")) {
                btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadSuratLampiran(
                                String.valueOf(cutiModelList.get(getAdapterPosition()).getCutiId()), "penting"
                        );
                    }
                });

            }

            if (cutiModelList.get(getAdapterPosition()).getVerifikasi().equals(1)) {
                tvStatusCuti.setText("Disetujui");

                cvStatus.setCardBackgroundColor(context.getColor(R.color.green));
            } else if (cutiModelList.get(getAdapterPosition()).getVerifikasi().equals(2)) {
                tvStatusCuti.setText("Ditolak");

                cvStatus.setCardBackgroundColor(context.getColor(R.color.red));
            }else {
                tvStatusCuti.setText("Diproses");

                cvStatus.setCardBackgroundColor(context.getColor(R.color.yellow));
            }




            dialog.show();



        }
    }

    private void downloadSuratLampiran(String cutiId, String jenis) {
        String url = Constants.URLF_DONWLOAD_LAMPIRAN_CUTI + "/" + cutiId + "/" + jenis;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }


}
