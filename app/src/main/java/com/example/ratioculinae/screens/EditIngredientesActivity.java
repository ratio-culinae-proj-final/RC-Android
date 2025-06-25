package com.example.ratioculinae.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Ingrediente;

public class EditIngredientesActivity extends AppCompatActivity {
    private EditText nome, quantidade;
    private Button  cancelar, salvar;

    private ImageView deletar;

    private AppDatabase db;
    private Ingrediente ingrediente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_ingredientes);
        inicializarIngredientes();
        receberItem();
    }

    private void receberItem() {
        int ingredienteId = getIntent().getIntExtra("ingrediente_id", -1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ingrediente = db.ingredientesDAO().buscarIngrediente(ingredienteId);
                if (ingrediente != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nome.setText(ingrediente.getNome());
                            quantidade.setText(String.valueOf(ingrediente.getQuantidade()));
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EditIngredientesActivity.this, "Ingrediente não encontrado", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
            }
        }).start();
    }

    private void inicializarIngredientes() {
        nome = findViewById(R.id.idNovoNome);
        quantidade = findViewById(R.id.idNovaQuantidade);
        deletar = findViewById(R.id.btnDelete);
        cancelar = findViewById(R.id.btnCancelarEdit);
        salvar = findViewById(R.id.btnSalvarEdit);
        db = AppDatabase.getInstance(getApplicationContext());

        salvar.setOnClickListener(v -> {
            editar();
        });

        deletar.setOnClickListener(v -> {
            apagar();
        });

        cancelar.setOnClickListener(v -> {
            finish();
        });

    }

    private void apagar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.ingredientesDAO().apagar(ingrediente);
                startActivity(new Intent(EditIngredientesActivity.this, IngredientsActivity.class));
            }
        }).start();
        Toast.makeText(this, "Deletado com sucesso!", Toast.LENGTH_LONG).show();
    }
    private void editar() {
        ingrediente.setNome(nome.getText().toString());
        ingrediente.setQuantidade(Integer.parseInt(quantidade.getText().toString()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                db.ingredientesDAO().atualizar(ingrediente);
                startActivity(new Intent(EditIngredientesActivity.this, IngredientsActivity.class));
            }
        }).start();
    }
}