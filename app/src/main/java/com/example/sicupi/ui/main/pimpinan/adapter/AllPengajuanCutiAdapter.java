package com.example.sicupi.ui.main.pimpinan.adapter;

import android.app.AlertDialog;
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
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PegawaiService;
import com.example.sicupi.data.api.PimpinanService;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.util.Constants;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPengajuanCutiAdapter extends RecyclerView.Adapter<AllPengajuanCutiAdapter.ViewHolder> {
    Context context;
    List<CutiModel> cutiModelList;
    private AlertDialog progressDialog;
    PimpinanService pimpinanService;

    public AllPengajuanCutiAdapter(Context context, List<CutiModel> cutiModelList) {
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
            lrPerihal = itemView.findViewById(R.id.linar_perihal);
            tvNama = itemView.findViewById(R.id.tvNama);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.layout_detail_cuti_pimpinan);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView tvJenisCuti, tvTanggalMulai, tvTanggalSelesai, tvPerihal, tvNamaPengaju;
            Button btnDownloadLampiran, btnTolak, btnSetuju;
            tvJenisCuti = dialog.findViewById(R.id.tvJenisCuti);
            tvTanggalMulai = dialog.findViewById(R.id.tvTglAwal);
            tvTanggalSelesai = dialog.findViewById(R.id.tvTglSelesai);
            tvPerihal = dialog.findViewById(R.id.tvPerihal);
            btnSetuju = dialog.findViewById(R.id.btnSetuju);
            btnTolak = dialog.findViewById(R.id.btnTolak);
            tvNamaPengaju = dialog.findViewById(R.id.tvNamaPegawai);
            btnDownloadLampiran = dialog.findViewById(R.id.btnDownloadLampiran);
            pimpinanService = ApiConfig.getClient().create(PimpinanService.class);

            tvJenisCuti.setText(cutiModelList.get(getAdapterPosition()).getKeterangan());
            tvTanggalMulai.setText(cutiModelList.get(getAdapterPosition()).getMulaiCuti());
            tvTanggalSelesai.setText(cutiModelList.get(getAdapterPosition()).getAkhirCuti());

            tvNamaPengaju.setText(cutiModelList.get(getAdapterPosition()).getNama());

            if (cutiModelList.get(getAdapterPosition()).getKeterangan().equals("Cuti Sakit < 14")) {
                btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadSuratLampiran(
                                String.valueOf(cutiModelList.get(getAdapterPosition()).getCutiId()), "kurang"
                        );
                    }
                });
                tvPerihal.setText(cutiModelList.get(getAdapterPosition()).getPerihal());

            }
            else  if (cutiModelList.get(getAdapterPosition()).getKeterangan().equals("Cuti Sakit > 14")) {
                btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadSuratLampiran(
                                String.valueOf(cutiModelList.get(getAdapterPosition()).getCutiId()), "lebih"
                        );
                    }
                });
                tvPerihal.setText(cutiModelList.get(getAdapterPosition()).getPerihal());

            }else  if (cutiModelList.get(getAdapterPosition()).getKeterangan().equals("Cuti Melahirkan")) {

              btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      downloadSuratLampiran(
                              String.valueOf(cutiModelList.get(getAdapterPosition()).getCutiId()), "melahirkan"
                      );
                  }
              });
              tvPerihal.setText("-");

            }else  if (cutiModelList.get(getAdapterPosition()).getKeterangan().equals("Cuti Alasan Penting")) {
                btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadSuratLampiran(
                                String.valueOf(cutiModelList.get(getAdapterPosition()).getCutiId()), "penting"
                        );
                    }
                });
                tvPerihal.setText(cutiModelList.get(getAdapterPosition()).getPerihal());

            }
            dialog.show();

            btnTolak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar("Loading", "Menyimpan data...", true);
                    pimpinanService.tolakCuti(cutiModelList.get(getAdapterPosition()).getKodePegawai(),
                            String.valueOf(cutiModelList.get(getAdapterPosition()).getCutiId())).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.isSuccessful() && response.body().getStatus() == 200) {
                                showToast("success", "Berhasil menolak cuti");
                                cutiModelList.remove(getAdapterPosition());
                                notifyDataSetChanged();
                                dialog.dismiss();
                                showProgressBar("sds", "dsdss", false);
                            }else {
                                showProgressBar("sds", "dsdss", false);
                                showToast("error", "Terjadi kesalahan");

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {

                            showProgressBar("sds", "dsdss", false);
                            showToast("error", "Tidak ada koneksi internet");
                        }
                    });
                }
            });

            btnSetuju.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar("Loading", "Menyimpan data...", true);
                    pimpinanService.setujuCuti(cutiModelList.get(getAdapterPosition()).getKodePegawai(),
                            String.valueOf(cutiModelList.get(getAdapterPosition()).getCutiId())).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.isSuccessful() && response.body().getStatus() == 200) {
                                showToast("success", "Berhasil menyetujui cuti");
                                cutiModelList.remove(getAdapterPosition());
                                notifyDataSetChanged();
                                dialog.dismiss();
                                showProgressBar("sds", "dsdss", false);
                            }else {
                                showProgressBar("sds", "dsdss", false);
                                showToast("error", "Terjadi kesalahan");

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {

                            showProgressBar("sds", "dsdss", false);
                            showToast("error", "Tidak ada koneksi internet");
                        }
                    });
                }
            });



        }
    }

    private void downloadSuratLampiran(String cutiId, String jenis) {
        String url = Constants.URLF_DONWLOAD_LAMPIRAN_CUTI + "/" + cutiId + "/" + jenis;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }


    private void showToast(String jenis, String message) {
        if (jenis.equals("success")) {
            Toasty.success(context, message, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(context, message, Toasty.LENGTH_SHORT).show();
        }
    }


    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);
                progressDialog = builder.create();
            }
            progressDialog.show(); // Menampilkan progress dialog
        } else {
            // Menyembunyikan progress dialog jika ada
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }


}
