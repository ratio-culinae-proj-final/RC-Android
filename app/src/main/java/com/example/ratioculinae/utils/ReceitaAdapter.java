package com.example.ratioculinae.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratioculinae.R;
import com.example.ratioculinae.models.Receita;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReceitaAdapter extends RecyclerView.Adapter<ReceitaAdapter.ReceitaViewHolder> {

    private final List<Receita> receitas;
    private final OnReceitaClickListener listener;

    public interface OnReceitaClickListener {
        void onClick(Receita receita);
    }

    public ReceitaAdapter(List<Receita> receitas, OnReceitaClickListener listener) {
        this.receitas = receitas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReceitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_receita_item, parent, false);
        return new ReceitaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceitaViewHolder holder, int position) {
        Receita r = receitas.get(position);
        holder.nome.setText(r.getNome());
        if (!r.getImagem().isEmpty()) {
            Picasso.get().load(r.getImagem()).into(holder.imagem);
        } else {
            holder.imagem.setImageResource(R.drawable.placeholder_receita);
        }

        holder.itemView.setOnClickListener(v -> listener.onClick(r));
    }

    @Override
    public int getItemCount() {
        return receitas.size();
    }

    static class ReceitaViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        ImageView imagem;

        public ReceitaViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvCardNomeReceita);
            imagem = itemView.findViewById(R.id.imgCardReceita);
        }
    }
}
