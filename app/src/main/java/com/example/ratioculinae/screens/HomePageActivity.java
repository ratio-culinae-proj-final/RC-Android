package com.example.ratioculinae.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Usuario;

public class HomePageActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button btnIngredientes, btnGerador, btnVisualizar, btnTemporizador, btnCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        inicializarHomePage();
        carregarUsuarioLogado();
        configurarIntents();
    }

    private void inicializarHomePage() {
        welcomeText = findViewById(R.id.textBemVindo);
        btnIngredientes = findViewById(R.id.btnIngredientes);
        btnGerador = findViewById(R.id.btnGerador);
        btnVisualizar = findViewById(R.id.btnVisualizar);
        btnTemporizador = findViewById(R.id.btnTemporizador);
        btnCompras = findViewById(R.id.btnCompras);
    }

    private void carregarUsuarioLogado() {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String uuid = preferences.getString("uuid", null);

        if (uuid != null) {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                Usuario usuario = db.usuarioDAO().getUsuarioByUuid(uuid);

                if (usuario != null) {
                    runOnUiThread(() -> {
                        String saudacao = getString(R.string.welcome_message, usuario.nome);
                        welcomeText.setText(saudacao);
                    });
                }
            }).start();
        }
    }

    private void configurarIntents() {
        btnIngredientes.setOnClickListener(v -> {
            startActivity(new Intent(this, IngredientsActivity.class));
        });

        btnGerador.setOnClickListener(v -> {
            startActivity(new Intent(this, GenerateRecipesActivity.class));
        });

        btnVisualizar.setOnClickListener(v -> {
            // startActivity(new Intent(this, VisualizarReceitasActivity.class));
        });

        btnTemporizador.setOnClickListener(v -> {
            startActivity(new Intent(this, TemporizadorActivity.class));
        });

        btnCompras.setOnClickListener(v -> {
            startActivity(new Intent(this, ShoppingActivity.class));
        });
    }
}
