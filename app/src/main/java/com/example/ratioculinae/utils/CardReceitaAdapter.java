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
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardReceitaAdapter extends RecyclerView.Adapter<CardReceitaAdapter.ViewHolder> {

    private final List<Receita> receitas;
    private final Context context;
    private final OnCardClickListener listener;

    public interface OnCardClickListener {
        void onClick(Receita receita);
    }

    public CardReceitaAdapter(List<Receita> receitas, Context context, OnCardClickListener listener) {
        this.receitas = receitas;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_receita_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receita r = receitas.get(position);
        holder.nome.setText(r.getNome());
        Picasso.get()
                .load(r.getImagem())
                .placeholder(R.drawable.placeholder_receita)
                .into(holder.img);

        holder.itemView.setOnClickListener(v -> listener.onClick(r));
    }

    @Override
    public int getItemCount() {
        return receitas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvCardNomeReceita);
            img = itemView.findViewById(R.id.imgCardReceita);
        }
    }
}
