package com.example.ratioculinae.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Compra;

import java.util.List;

public class ShoppingActivity extends AppCompatActivity {
    private EditText produto, quantidade;
    private Button adicionar;
    private LinearLayout lista;
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping);

        inicializarComponentes();
        configurarListeners();
        carregarLista();
    }

    private void carregarLista() {

        new Thread(() -> {

            List<Compra> listaCl = db.compraDAO().listarCompras();

            for (Compra item : listaCl) {
                RadioButton rb = new RadioButton(ShoppingActivity.this);
                rb.setText(item.getProdutoQuantidade());
                rb.setId(item.getId());
                lista.addView(rb);
                rb.setOnClickListener(v -> {
                    new  Thread(new Runnable() {
                        @Override
                        public void run() {
                            db.compraDAO().delete(item);
                            runOnUiThread(() -> lista.removeView(rb));
                        }
                    }).start();
                });
            }
        }).start();
    }

    private void configurarListeners() {
        adicionar.setOnClickListener(v -> {
            salvarProduto();
        });
    }

    private void salvarProduto() {
        String setProduto = produto.getText().toString();
        String quantidadeGet = quantidade.getText().toString().trim();
        Compra item = new Compra();

        if (setProduto.isEmpty() || quantidadeGet.isEmpty()) {
            exibirMensagem("É necessario o nome do produto e a quantiddade faltante");
            return;
        }
        int setQuantidade = Integer.parseInt(quantidadeGet);

        Compra produtoCl = new Compra(setProduto, setQuantidade);
        new Thread(() -> {
            db.compraDAO().insert(produtoCl);

            runOnUiThread(() -> {
                exibirMensagem("Inserido com sucesso!");
                startActivity(new Intent(ShoppingActivity.this, ShoppingActivity.class));
            });
        }).start();
    }

    private void inicializarComponentes() {
        produto = findViewById(R.id.comprasProduto);
        quantidade = findViewById(R.id.comprasQuantidadde);
        adicionar = findViewById(R.id.comprasAdd);
        lista = findViewById(R.id.listaLayout);
        db = AppDatabase.getInstance(getApplicationContext());
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}