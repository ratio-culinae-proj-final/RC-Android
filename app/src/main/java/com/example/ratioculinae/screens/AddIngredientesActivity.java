package com.example.ratioculinae.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Ingrediente;

public class AddIngredientesActivity extends AppCompatActivity {
    private EditText nome, quantiddade;
    private Button adicionar;

    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_ingredientes);
        inicializarIngredientes();
    }

    private void inicializarIngredientes() {
        nome = findViewById(R.id.idNomeIngrediente);
        quantiddade = findViewById(R.id.idQauntidadeIngrediente);
        adicionar = findViewById(R.id.btnSalvarAdd);
        db = AppDatabase.getInstance(getApplicationContext());
        adicionar.setOnClickListener(v -> {
            salvar();
        });

    }

    private void salvar() {
        String getNomeTela = nome.getText().toString();
        int getQuantidadeTela = Integer.parseInt(quantiddade.getText().toString());
        Ingrediente ingrediente = new Ingrediente(getNomeTela, getQuantidadeTela);

        new Thread(new Runnable() {
            @Override
            public void run() {
                db.ingredientesDAO().insert(ingrediente);
                startActivity(new Intent(AddIngredientesActivity.this, IngredientsActivity.class));
            }
        }).start();


    }
}