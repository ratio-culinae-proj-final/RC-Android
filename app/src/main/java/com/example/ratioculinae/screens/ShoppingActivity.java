package com.example.ratioculinae.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ratioculinae.R;

public class ShoppingActivity extends AppCompatActivity {
    private EditText produto, quantidade;
    private Button adicionar;
    private RadioGroup lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping);

        inicializarComponentes();
        configurarListeners();
    }

    private void configurarListeners() {
        adicionar.setOnClickListener(v -> salvarProduto());
    }

    private void salvarProduto() {

    }

    private void inicializarComponentes() {
        produto = findViewById(R.id.comprasProduto);
        quantidade = findViewById(R.id.comprasQuantidadde);
        adicionar = findViewById(R.id.comprasAdd);
        lista = findViewById(R.id.listaCompras);

    }
}