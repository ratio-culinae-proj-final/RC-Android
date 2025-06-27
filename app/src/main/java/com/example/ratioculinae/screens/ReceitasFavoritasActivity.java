package com.example.ratioculinae.screens;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Receita;
import com.example.ratioculinae.models.ReceitaFavorita;
import com.example.ratioculinae.utils.FavoritasAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

public class ReceitasFavoritasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receitas_favoritas);

        RecyclerView recyclerView = findViewById(R.id.recyclerFavoritas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<ReceitaFavorita> favoritas = db.receitaFavoritaDao().listar();

            runOnUiThread(() -> {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                FavoritasAdapter adapter = new FavoritasAdapter(favoritas, this, (receita, favoritaRef) -> {
                    mostrarModalDetalhesFavorita(receita, favoritaRef);
                });
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }

    private void mostrarModalDetalhesFavorita(Receita receita, ReceitaFavorita receitaFavoritaRef) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.modal_detalhes_favorita);

        TextView nome = dialog.findViewById(R.id.tvNomeModalFavorita);
        TextView ingredientes = dialog.findViewById(R.id.tvIngredientesModalFavorita);
        TextView modoPreparo = dialog.findViewById(R.id.tvModoModalFavorita);
        TextView dificuldade = dialog.findViewById(R.id.tvDificuldadeModalFavorita);
        TextView tempo = dialog.findViewById(R.id.tvTempoModalFavorita);
        ImageView imagem = dialog.findViewById(R.id.imgModalFavorita);
        Button btnRemover = dialog.findViewById(R.id.btnRemoverFavorita);

        nome.setText(receita.getNome());
        modoPreparo.setText(receita.getModoPreparo());
        dificuldade.setText("Dificuldade: " + receita.getDificuldade());
        tempo.setText("Tempo: " + receita.getTempoPreparo());

        StringBuilder sb = new StringBuilder();
        if (receita.getIngredientes() != null) {
            for (int i = 0; i < receita.getIngredientes().length(); i++) {
                try {
                    sb.append("• ").append(receita.getIngredientes().getString(i)).append("\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        ingredientes.setText(sb.toString());

        if (receita.getImagem() != null && receita.getImagem().startsWith("http")) {
            Picasso.get().load(receita.getImagem()).into(imagem);
        } else {
            imagem.setImageResource(R.drawable.placeholder_receita);
        }

        btnRemover.setOnClickListener(v -> {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                db.receitaFavoritaDao().deletar(receitaFavoritaRef);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Receita removida!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    recreate();
                });
            }).start();
        });

        dialog.show();
    }
}