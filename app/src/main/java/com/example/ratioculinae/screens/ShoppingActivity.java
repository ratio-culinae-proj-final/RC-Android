package com.example.ratioculinae.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Compra;

import java.util.List;

public class ShoppingActivity extends AppCompatActivity {
    private EditText produto, quantidade;
    private Button adicionar;
    private LinearLayout lista;
    private ImageButton btnVoltar;
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

    private void inicializarComponentes() {
        produto = findViewById(R.id.comprasProduto);
        quantidade = findViewById(R.id.comprasQuantidadde);
        adicionar = findViewById(R.id.comprasAdd);
        lista = findViewById(R.id.listaLayout);
        btnVoltar = findViewById(R.id.btnVoltar);
        db = AppDatabase.getInstance(getApplicationContext());
    }

    private void configurarListeners() {
        adicionar.setOnClickListener(v -> salvarProduto());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void carregarLista() {
        new Thread(() -> {
            List<Compra> listaCompras = db.compraDAO().listarCompras();

            runOnUiThread(() -> {
                lista.removeAllViews();
                for (Compra item : listaCompras) {
                    adicionarItemNaTela(item);
                }
            });
        }).start();
    }

    private void salvarProduto() {
        String nomeProduto = produto.getText().toString().trim();
        String quantidadeStr = quantidade.getText().toString().trim();

        if (nomeProduto.isEmpty() || quantidadeStr.isEmpty()) {
            exibirMensagem("É necessário preencher o nome e a quantidade.");
            return;
        }

        int qtd = Integer.parseInt(quantidadeStr);
        Compra novaCompra = new Compra(nomeProduto, qtd);

        new Thread(() -> {
            long novoId = db.compraDAO().insert(novaCompra);
            novaCompra.setId((int) novoId);

            runOnUiThread(() -> {
                exibirMensagem("Produto adicionado!");
                adicionarItemNaTela(novaCompra);
                produto.setText("");
                quantidade.setText("");
            });
        }).start();
    }

    private void adicionarItemNaTela(Compra item) {
        RadioButton rb = new RadioButton(this);
        rb.setText(item.getProdutoQuantidade());
        rb.setId(item.getId());
        lista.addView(rb);

        rb.setOnClickListener(v -> {
            new Thread(() -> {
                db.compraDAO().delete(item);
                runOnUiThread(() -> lista.removeView(rb));
            }).start();
        });
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}
