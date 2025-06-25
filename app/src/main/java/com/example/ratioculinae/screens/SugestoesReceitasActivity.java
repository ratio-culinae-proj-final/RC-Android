package com.example.ratioculinae.screens;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ratioculinae.R;
import com.example.ratioculinae.utils.ReceitaAdapter;
import com.example.ratioculinae.models.Receita;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SugestoesReceitasActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ReceitaAdapter receitaAdapter;
    private final ArrayList<Receita> listaReceitas = new ArrayList<>();
    private final OkHttpClient client = new OkHttpClient();
    private final String urlAPI = "http://10.0.2.2:5000/sugerir_receitas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugestoes_receitas);

        viewPager = findViewById(R.id.viewPagerReceitas);
        receitaAdapter = new ReceitaAdapter(listaReceitas, this::mostrarModalDetalhes);
        viewPager.setAdapter(receitaAdapter);

        String ingredientes = getIntent().getStringExtra("ingredientes");
        buscarReceitas(ingredientes);
    }

    private void buscarReceitas(String ingredientes) {
        JSONObject json = new JSONObject();
        try {
            json.put("ingredientes", ingredientes);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao criar JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(urlAPI)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(SugestoesReceitasActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() ->
                            Toast.makeText(SugestoesReceitasActivity.this, "Erro do servidor", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                String resposta = response.body().string();
                try {
                    JSONObject obj = new JSONObject(resposta);
                    JSONArray array = obj.getJSONArray("receitas");  // <-- Corrigido aqui!

                    listaReceitas.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject r = array.getJSONObject(i);
                        Receita receita = new Receita(
                                r.optString("nome"),
                                r.optJSONArray("ingredientes"),
                                r.optString("modo_preparo"),
                                r.optString("dificuldade"),
                                r.optString("tempo_preparo"),
                                r.optString("imagem")
                        );
                        listaReceitas.add(receita);
                    }

                    runOnUiThread(() -> receitaAdapter.notifyDataSetChanged());

                } catch (JSONException e) {
                    runOnUiThread(() ->
                            Toast.makeText(SugestoesReceitasActivity.this, "Erro ao interpretar a resposta", Toast.LENGTH_SHORT).show()
                    );
                    e.printStackTrace();
                }
            }
        });
    }

    private void mostrarModalDetalhes(Receita receita) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.modal_detalhes_receita);

        TextView nome = dialog.findViewById(R.id.tvNomeModal);
        TextView ingredientes = dialog.findViewById(R.id.tvIngredientesModal);
        TextView modoPreparo = dialog.findViewById(R.id.tvModoModal);
        TextView dificuldade = dialog.findViewById(R.id.tvDificuldadeModal);
        TextView tempo = dialog.findViewById(R.id.tvTempoModal);
        ImageView imagem = dialog.findViewById(R.id.imgModal);

        nome.setText(receita.getNome());
        modoPreparo.setText(receita.getModoPreparo());
        dificuldade.setText("Dificuldade: " + receita.getDificuldade());
        tempo.setText("Tempo: " + receita.getTempoPreparo());

        // Ingredientes como string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < receita.getIngredientes().length(); i++) {
            try {
                sb.append("• ").append(receita.getIngredientes().getString(i)).append("\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ingredientes.setText(sb.toString());

        if (!receita.getImagem().isEmpty()) {
            Picasso.get().load(receita.getImagem()).into(imagem);
        } else {
            imagem.setImageResource(R.drawable.placeholder_receita);
        }

        dialog.show();
    }
}
