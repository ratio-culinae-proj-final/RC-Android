package com.example.ratioculinae.screens;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Receita;
import com.example.ratioculinae.models.ReceitaFavorita;
import com.example.ratioculinae.utils.ReceitaAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

    private ProgressDialog progressDialog;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build();

    private final String urlAPI = "http://10.0.2.2:5000/sugerir_receitas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugestoes_receitas);

        viewPager = findViewById(R.id.viewPagerReceitas);
        receitaAdapter = new ReceitaAdapter(listaReceitas, this::mostrarModalDetalhes);
        viewPager.setAdapter(receitaAdapter);

        ImageButton btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

        String ingredientes = getIntent().getStringExtra("ingredientes");
        String preferencias = getIntent().getStringExtra("preferencias");

        if (ingredientes == null || ingredientes.isEmpty()) {
            Toast.makeText(this, "Nenhum ingrediente recebido.", Toast.LENGTH_SHORT).show();
            return;
        }

        buscarReceitas(ingredientes, preferencias);
    }

    private void buscarReceitas(String ingredientes, String preferencias) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buscando receitas...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JSONObject json = new JSONObject();
        try {
            json.put("ingredientes", ingredientes);
            if (preferencias != null && !preferencias.isEmpty()) {
                json.put("preferencias", preferencias);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                progressDialog.dismiss();
                Toast.makeText(this, "Erro ao criar JSON", Toast.LENGTH_SHORT).show();
            });
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
                Log.e("SugestoesReceitas", "Erro na requisição HTTP", e);
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(SugestoesReceitasActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> progressDialog.dismiss());

                if (!response.isSuccessful()) {
                    Log.e("SugestoesReceitas", "Resposta HTTP falhou: " + response.code());
                    runOnUiThread(() ->
                            Toast.makeText(SugestoesReceitasActivity.this, "Erro do servidor: " + response.code(), Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                String resposta = response.body().string();
                try {
                    JSONObject obj = new JSONObject(resposta);

                    if (!obj.has("receitas")) {
                        runOnUiThread(() ->
                                Toast.makeText(SugestoesReceitasActivity.this, "Resposta inválida da API", Toast.LENGTH_SHORT).show()
                        );
                        return;
                    }

                    JSONArray array = obj.getJSONArray("receitas");

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

                    runOnUiThread(() -> {
                        if (listaReceitas.isEmpty()) {
                            Toast.makeText(SugestoesReceitasActivity.this, "Nenhuma receita encontrada.", Toast.LENGTH_SHORT).show();
                        } else {
                            receitaAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    Log.e("SugestoesReceitas", "Erro ao interpretar resposta da API: " + resposta, e);
                    runOnUiThread(() ->
                            Toast.makeText(SugestoesReceitasActivity.this, "Erro ao interpretar a resposta", Toast.LENGTH_SHORT).show()
                    );
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
        Button btnFavoritar = dialog.findViewById(R.id.btnFavoritar);

        btnFavoritar.setOnClickListener(v -> {
            Gson gson = new Gson();
            String json = gson.toJson(receita);
            ReceitaFavorita favorita = new ReceitaFavorita(json);
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                db.receitaFavoritaDao().inserir(favorita);
            }).start();

            Toast.makeText(this, "Receita favoritada!", Toast.LENGTH_SHORT).show();
        });

        nome.setText(receita.getNome());

        String modo = receita.getModoPreparo();
        String modoFormatado = modo.replaceAll("(\\d+\\s*\\.)", "\n$1").trim();
        modoFormatado = modoFormatado.replaceFirst("^\\n", "");
        modoPreparo.setText(modoFormatado);

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

        // Carrega imagem
        if (receita.getImagem() != null && receita.getImagem().startsWith("http")) {
            Picasso.get().load(receita.getImagem()).into(imagem);
        } else {
            Log.w("SugestoesReceitas", "Imagem inválida ou ausente para receita: " + receita.getNome());
            imagem.setImageResource(R.drawable.placeholder_receita);
        }

        dialog.show();
    }
}