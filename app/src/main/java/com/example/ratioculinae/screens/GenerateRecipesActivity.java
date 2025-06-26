package com.example.ratioculinae.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.database.dao.UsuarioIngredienteDao;
import com.example.ratioculinae.models.Ingrediente;
import com.example.ratioculinae.models.Usuario;
import com.example.ratioculinae.utils.UsuarioLogadoHelper;

import java.util.ArrayList;
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

        switchPreferencias.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (usuario != null) {
                usuario.setUsarPreferencias(isChecked);
                Executors.newSingleThreadExecutor().execute(() ->
                        db.usuarioDAO().update(usuario)
                );
            }
        });
    }

    private void carregarUsuario() {
        Executors.newSingleThreadExecutor().execute(() -> {
            usuario = UsuarioLogadoHelper.getUsuarioLogado(this);

            if (usuario != null) {
                carregarIngredientesDoUsuario(usuario.uuid);
                runOnUiThread(() -> switchPreferencias.setChecked(usuario.getUsarPreferencias()));
            } else {
                runOnUiThread(() ->
                        Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                );
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
                runOnUiThread(() ->
                        Toast.makeText(this, "Nenhum ingrediente cadastrado.", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            String ingredientesStr = ingredientesDoUsuario.stream()
                    .map(Ingrediente::getNome)
                    .collect(Collectors.joining(", "));

            boolean usarPreferencias = usuario.getUsarPreferencias();
            String preferencias = null;

            if (usarPreferencias && usuario.getPreferenciasAlimentares() != null) {
                preferencias = usuario.getPreferenciasAlimentares();
            }

            Intent intent = new Intent(GenerateRecipesActivity.this, SugestoesReceitasActivity.class);
            intent.putExtra("ingredientes", ingredientesStr);
            intent.putExtra("preferencias", preferencias);
            startActivity(intent);
        });
    }
}