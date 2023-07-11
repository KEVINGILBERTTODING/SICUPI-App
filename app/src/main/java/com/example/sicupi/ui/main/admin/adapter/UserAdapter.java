package com.example.sicupi.ui.main.admin.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicupi.R;
import com.example.sicupi.data.model.UserModel;
import com.example.sicupi.ui.main.admin.user.AdmineEditPegawaiFragment;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<UserModel> userModelList;

    public UserAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        holder.tvNama.setText(userModelList.get(holder.getAdapterPosition()).getNama());
        holder.tvJabatan.setText(userModelList.get(holder.getAdapterPosition()).getJabatan());
        holder.tvKodePegawai.setText(userModelList.get(holder.getAdapterPosition()).getKodePegawai());

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public void filter(ArrayList<UserModel> filteredList) {
        userModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvNama, tvJabatan, tvKodePegawai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvJabatan = itemView.findViewById(R.id.tvJabatan);
            tvKodePegawai = itemView.findViewById(R.id.tvKodePegawai);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            Fragment fragment = new AdmineEditPegawaiFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id", userModelList.get(getAdapterPosition()).getKodePegawai());
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameAdmin, fragment).addToBackStack(null)
                    .commit();

        }
    }
}
