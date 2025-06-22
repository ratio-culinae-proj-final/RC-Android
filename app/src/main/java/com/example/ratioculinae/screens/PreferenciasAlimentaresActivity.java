package com.example.ratioculinae.screens;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Usuario;
import com.example.ratioculinae.utils.UsuarioLogadoHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class PreferenciasAlimentaresActivity extends AppCompatActivity {

    private CheckBox cbVegetariano, cbVegano, cbSemGluten, cbSemLactose, cbDiabetico;
    private Button btnSalvar;
    private Usuario usuario;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias_alimentares);

        db = AppDatabase.getInstance(this);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        cbVegetariano = findViewById(R.id.checkboxVegetariano);
        cbVegano = findViewById(R.id.checkboxVegano);
        cbSemGluten = findViewById(R.id.checkboxSemGluten);
        cbSemLactose = findViewById(R.id.checkboxSemLactose);
        cbDiabetico = findViewById(R.id.checkboxDiabetico);
        btnSalvar = findViewById(R.id.btnSalvarPreferencias);

        carregarUsuario();

        btnSalvar.setOnClickListener(v -> {
            String preferencias = capturarPreferencias();
            usuario.setPreferenciasAlimentares(preferencias);
            salvarUsuario(usuario);
        });
    }

    private void carregarUsuario() {
        Executors.newSingleThreadExecutor().execute(() -> {
            usuario = UsuarioLogadoHelper.getUsuarioLogado(this);
            if (usuario != null) {
                new Handler(Looper.getMainLooper()).post(this::carregarPreferenciasSalvas);
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erro ao carregar usuário logado.", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void salvarUsuario(Usuario u) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.usuarioDAO().update(u);
            runOnUiThread(() -> {
                Toast.makeText(this, "Preferências salvas com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private String capturarPreferencias() {
        List<String> selecionadas = new ArrayList<>();

        if (cbVegetariano.isChecked()) selecionadas.add("Vegetariano");
        if (cbVegano.isChecked()) selecionadas.add("Vegano");
        if (cbSemGluten.isChecked()) selecionadas.add("Sem Glúten");
        if (cbSemLactose.isChecked()) selecionadas.add("Sem Lactose");
        if (cbDiabetico.isChecked()) selecionadas.add("Diabético");

        return String.join(",", selecionadas);
    }

    private void carregarPreferenciasSalvas() {
        if (usuario.getPreferenciasAlimentares() == null) return;

        String[] preferencias = usuario.getPreferenciasAlimentares().split(",");
        for (String p : preferencias) {
            switch (p.trim()) {
                case "Vegetariano": cbVegetariano.setChecked(true); break;
                case "Vegano": cbVegano.setChecked(true); break;
                case "Sem Glúten": cbSemGluten.setChecked(true); break;
                case "Sem Lactose": cbSemLactose.setChecked(true); break;
                case "Diabético": cbDiabetico.setChecked(true); break;
            }
        }
    }
}
