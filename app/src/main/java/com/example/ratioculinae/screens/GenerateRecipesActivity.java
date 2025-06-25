package com.example.ratioculinae.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import android.widget.ArrayAdapter;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.database.dao.UsuarioIngredienteDao;
import com.example.ratioculinae.models.Ingrediente;
import com.example.ratioculinae.models.Usuario;
import com.example.ratioculinae.utils.UsuarioLogadoHelper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class GenerateRecipesActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchPreferencias;
    private Usuario usuario;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_recipes);

        switchPreferencias = findViewById(R.id.switchPreferencias);
        Button btnEditarPreferencias = findViewById(R.id.btnEditarPreferencias);
        Button btnBuscarReceitas = findViewById(R.id.btnBuscarReceitas);
        ListView listaIngredientes = findViewById(R.id.listaIngredientes);

        db = AppDatabase.getInstance(getApplicationContext());

        carregarUsuario();

        btnEditarPreferencias.setOnClickListener(v ->
                startActivity(new Intent(this, PreferenciasAlimentaresActivity.class)));

        btnBuscarReceitas.setOnClickListener(v -> buscarReceitas());
    }

    private void carregarUsuario() {
        Executors.newSingleThreadExecutor().execute(() -> {
            usuario = UsuarioLogadoHelper.getUsuarioLogado(this);

            if (usuario != null) {
                carregarIngredientesDoUsuario(usuario.uuid);
            }
        });
    }

    private void carregarIngredientesDoUsuario(String usuarioId) {
        UsuarioIngredienteDao ingredienteDao = db.usuarioIngredienteDao();
        List<Ingrediente> ingredientes = ingredienteDao.getIngredientesByUsuario(usuarioId);

        List<String> nomesIngredientes = new ArrayList<>();
        for (Ingrediente i : ingredientes) {
            nomesIngredientes.add(i.getNome());
        }

        runOnUiThread(() -> {
            ListView listaIngredientes = findViewById(R.id.listaIngredientes);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    nomesIngredientes
            );
            listaIngredientes.setAdapter(adapter);
        });
    }

    private void buscarReceitas() {
        if (usuario == null) {
            Toast.makeText(this, "Usuário não carregado ainda.", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            UsuarioIngredienteDao ingredienteDao = db.usuarioIngredienteDao();
            List<Ingrediente> ingredientesDoUsuario = ingredienteDao.getIngredientesByUsuario(usuario.uuid);

            if (ingredientesDoUsuario == null || ingredientesDoUsuario.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(this, "Nenhum ingrediente cadastrado.", Toast.LENGTH_SHORT).show());
                return;
            }

            String ingredientesStr = ingredientesDoUsuario.stream()
                    .map(Ingrediente::getNome)
                    .collect(Collectors.joining(", "));

            boolean usarPreferencias = switchPreferencias.isChecked();
            String preferencias = null;

            if (usarPreferencias && usuario.getPreferenciasAlimentares() != null) {
                preferencias = usuario.getPreferenciasAlimentares();
            }

            String prompt;
            if (preferencias != null && !preferencias.isEmpty()) {
                prompt = ingredientesStr + ", " + preferencias;
            } else {
                prompt = ingredientesStr;
            }

            // Passar o prompt via Intent para SugestoesReceitasActivity
            Intent intent = new Intent(GenerateRecipesActivity.this, SugestoesReceitasActivity.class);
            intent.putExtra("ingredientes", prompt);
            startActivity(intent);
        });
    }
}
