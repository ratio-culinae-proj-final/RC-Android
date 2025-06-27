package com.example.ratioculinae.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratioculinae.R;
import com.example.ratioculinae.models.Receita;
import com.example.ratioculinae.models.ReceitaFavorita;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritasAdapter extends RecyclerView.Adapter<FavoritasAdapter.ViewHolder> {

    private final List<ReceitaFavorita> favoritas;
    private final Context context;

    public interface OnReceitaClickListener {
        void onReceitaClick(Receita receita, ReceitaFavorita receitaFavoritaRef);
    }

    private final OnReceitaClickListener listener;

    public FavoritasAdapter(List<ReceitaFavorita> favoritas, Context context, OnReceitaClickListener listener) {
        this.favoritas = favoritas;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receita_salva, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReceitaFavorita fav = favoritas.get(position);
        Receita receita = new Gson().fromJson(fav.jsonReceita, Receita.class);

        holder.txtNome.setText(receita.getNome());
        holder.txtDificuldade.setText("Dificuldade: " + receita.getDificuldade());

        if (receita.getImagem() != null && receita.getImagem().startsWith("http")) {
            Picasso.get().load(receita.getImagem()).into(holder.imgReceita);
        } else {
            holder.imgReceita.setImageResource(R.drawable.placeholder_receita);
        }

        holder.itemView.setOnClickListener(v -> listener.onReceitaClick(receita, fav));
    }

    @Override
    public int getItemCount() {
        return favoritas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtDificuldade;
        ImageView imgReceita;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeReceita);
            txtDificuldade = itemView.findViewById(R.id.txtDificuldadeReceita);
            imgReceita = itemView.findViewById(R.id.imgReceita);
        }
    }
}

