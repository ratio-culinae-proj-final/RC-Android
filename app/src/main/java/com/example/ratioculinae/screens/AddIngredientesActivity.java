package com.example.ratioculinae.screens;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.database.relations.UsuarioIngredienteRef;
import com.example.ratioculinae.models.Ingrediente;
import com.example.ratioculinae.models.Usuario;
import com.example.ratioculinae.utils.SpeechHelper;
import com.example.ratioculinae.utils.UsuarioLogadoHelper;

import java.util.ArrayList;
import java.util.Locale;

public class AddIngredientesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH = 1;

    private EditText nomeInput;
    private EditText quantidadeInput;
    private Button btnSalvar;
    private ImageButton btnMicrofone;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_ingredientes);

        inicializarComponentes();
        configurarListeners();
    }

    private void inicializarComponentes() {
        nomeInput = findViewById(R.id.idNomeIngrediente);
        quantidadeInput = findViewById(R.id.idQauntidadeIngrediente);
        btnSalvar = findViewById(R.id.btnSalvarAdd);
        btnMicrofone = findViewById(R.id.btnMicrofone);
        db = AppDatabase.getInstance(getApplicationContext());
    }

    private void configurarListeners() {
        btnSalvar.setOnClickListener(v -> salvarIngrediente());
        btnMicrofone.setOnClickListener(v -> iniciarReconhecimentoVoz());
    }

    private void salvarIngrediente() {
        String nome = nomeInput.getText().toString().trim();
        String quantidadeStr = quantidadeInput.getText().toString().trim();

        if (nome.isEmpty() || quantidadeStr.isEmpty()) {
            exibirMensagem("Preencha todos os campos");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
        } catch (NumberFormatException e) {
            exibirMensagem("Quantidade inválida");
            return;
        }

        Ingrediente ingrediente = new Ingrediente(nome, quantidade);

        new Thread(() -> {
            // Inserir ingrediente
            long ingredienteId = db.ingredientesDAO().insert(ingrediente);

            // Recuperar usuário logado
            Usuario usuario = UsuarioLogadoHelper.getUsuarioLogado(this);
            if (usuario != null) {
                // Criar relação
                UsuarioIngredienteRef ref = new UsuarioIngredienteRef();
                ref.usuarioId = usuario.uuid;
                ref.ingredienteId = (int) ingredienteId;

                db.usuarioIngredienteDao().insert(ref);
            }

            runOnUiThread(() -> {
                exibirMensagem("Ingrediente salvo com sucesso!");
                startActivity(new Intent(this, IngredientsActivity.class));
            });
        }).start();
    }


    private void iniciarReconhecimentoVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga o nome e a quantidade do ingrediente, por exemplo: 'tomates 3' ou '3 tomates'");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH);
        } catch (Exception e) {
            exibirMensagem("Erro ao abrir reconhecimento de voz");
        }
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String fala = result.get(0);
                String[] partes = SpeechHelper.extrairQuantidadeENomeComRegex(fala);

                quantidadeInput.setText(partes[0]);
                nomeInput.setText(partes[1]);
            }
        }
    }
}
