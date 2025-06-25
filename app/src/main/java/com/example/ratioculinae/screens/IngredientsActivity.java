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

    private ListView ingredientes;
    private Button adicionar, editar;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingredients);
        inicializarIngredientes();
        configurarIntents();

    }

    private void configurarIntents() {
        adicionar.setOnClickListener(v -> {
            startActivity(new Intent(this, AddIngredientesActivity.class));
        });

        /*Como posso fazer a edição mds?*/
        editar.setOnClickListener(v -> {
            startActivity(new Intent(this, EditIngredientesActivity.class));
        });
    }

    private void inicializarIngredientes() {
        adicionar = findViewById(R.id.btnAddIngredientes);
        editar = findViewById(R.id.btnEditIngredientes);
        ingredientes = findViewById(R.id.listIngredientes);
        db = AppDatabase.getInstance(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                listar();
            }
        }).start();
    }

    private void listar() {
                List<Ingrediente> listagem = db.ingredientesDAO().listarIngredientes();
                for (Ingrediente i: listagem) {
                    Log.d("DEBUG_INGREDIENTE", "ID: " + i.getId() + " NOME: " + i.getNome() + " QUANTIDADE: " + i.getQuantidade());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<Ingrediente> adapter = new ArrayAdapter<>(IngredientsActivity.this,
                                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                                listagem);
                        ingredientes.setAdapter(adapter);
                    }
                });
    }
}