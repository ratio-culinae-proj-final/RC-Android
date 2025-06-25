package com.example.ratioculinae.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Ingrediente;

import java.util.List;

public class IngredientsActivity extends AppCompatActivity {

    private ListView listViewIngredientes;
    private Button btnAdicionar;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingredients);

        inicializarComponentes();
        configurarAcoes();
        carregarIngredientes();
    }

    private void inicializarComponentes() {
        listViewIngredientes = findViewById(R.id.listIngredientes);
        btnAdicionar = findViewById(R.id.btnAddIngredientes);
        db = AppDatabase.getInstance(getApplicationContext());
    }

    private void configurarAcoes() {
        btnAdicionar.setOnClickListener(v -> {
            startActivity(new Intent(this, AddIngredientesActivity.class));
        });

        listViewIngredientes.setOnItemClickListener((parent, view, position, id) -> {
            Ingrediente ingredienteSelecionado = (Ingrediente) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, EditIngredientesActivity.class);
            intent.putExtra("ingrediente_id", ingredienteSelecionado.getId());
            startActivity(intent);
        });
    }

    private void carregarIngredientes() {
        new Thread(() -> {
            List<Ingrediente> lista = db.ingredientesDAO().listarIngredientes();
            for (Ingrediente i : lista) {
                Log.d("DEBUG_INGREDIENTE", "ID: " + i.getId() + " NOME: " + i.getNome() + " QUANTIDADE: " + i.getQuantidade());
            }

            runOnUiThread(() -> {
                ArrayAdapter<Ingrediente> adapter = new ArrayAdapter<>(
                        IngredientsActivity.this,
                        R.layout.item_ingrediente,  // layout customizado
                        lista
                );
                listViewIngredientes.setAdapter(adapter);
            });
        }).start();
    }
}
