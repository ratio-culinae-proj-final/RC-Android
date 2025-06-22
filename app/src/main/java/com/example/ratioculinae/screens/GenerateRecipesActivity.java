package com.example.ratioculinae.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Usuario;
import com.example.ratioculinae.utils.UsuarioLogadoHelper;

import java.util.List;
import java.util.concurrent.Executors;

public class GenerateRecipesActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchPreferencias;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_recipes);

        switchPreferencias = findViewById(R.id.switchPreferencias);
        Button btnEditarPreferencias = findViewById(R.id.btnEditarPreferencias);
        Button btnBuscarReceitas = findViewById(R.id.btnBuscarReceitas);
        ListView listaIngredientes = findViewById(R.id.listaIngredientes);

        carregarUsuario();

        btnEditarPreferencias.setOnClickListener(v ->
                startActivity(new Intent(this, PreferenciasAlimentaresActivity.class)));

        btnBuscarReceitas.setOnClickListener(v -> buscarReceitas());
    }

    private void carregarUsuario() {
        Executors.newSingleThreadExecutor().execute(() -> {
            usuario = UsuarioLogadoHelper.getUsuarioLogado(this);
            // Carregar a lista de ingredientes do usuário aqui
        });
    }

    private void buscarReceitas() {
        boolean usarPreferencias = switchPreferencias.isChecked();

        String preferencias = null;
        if (usarPreferencias && usuario != null) {
            preferencias = usuario.getPreferenciasAlimentares();
        }

        // Aqui você pode passar a string de preferências para a IA, LangChain, etc.
        // Exemplo:
        if (preferencias != null) {
            // Enviar ingredientes + filtro para IA
        } else {
            // Enviar apenas ingredientes
        }

        // Exemplo mock:
        // mostrarReceitasNaTela(List<Receita> receitas);
    }
}
