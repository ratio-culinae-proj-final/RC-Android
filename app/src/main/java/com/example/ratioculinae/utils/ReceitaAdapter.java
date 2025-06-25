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
        // Garantir que o layout respeita match_parent
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_receita_item, parent, false);

        // Ajuste de segurança extra para garantir que preencha a tela no ViewPager2
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(layoutParams);

        return new ReceitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceitaViewHolder holder, int position) {
        Receita r = receitas.get(position);
        holder.nome.setText(r.getNome());

        if (r.getImagem() != null && !r.getImagem().isEmpty()) {
            Picasso.get()
                    .load(r.getImagem())
                    .placeholder(R.drawable.placeholder_receita)
                    .error(R.drawable.placeholder_receita)
                    .into(holder.imagem);
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
