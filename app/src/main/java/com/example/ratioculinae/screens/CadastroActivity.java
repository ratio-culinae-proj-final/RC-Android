package com.example.ratioculinae.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText nomeField;
    private EditText emailField;
    private EditText senhaField;
    private Button registerButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        inicializarTelaCadastro();
    }

    private void inicializarTelaCadastro() {
        nomeField = findViewById(R.id.etNome);
        emailField = findViewById(R.id.etEmail);
        senhaField = findViewById(R.id.etSenha);
        registerButton = findViewById(R.id.btnRegistrar);

        registerButton.setOnClickListener(v -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {

        db = AppDatabase.getInstance(getApplicationContext());

        String nome = nomeField.getText().toString();
        String email = emailField.getText().toString();
        String senha = senhaField.getText().toString();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@")) {
            Toast.makeText(this, "Digite um e-mail válido!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senha.length() < 8) {
            Toast.makeText(this, "A senha deve ter no mínimo 8 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

        String uuid = java.util.UUID.randomUUID().toString();
        Usuario novoUsuario = new Usuario(uuid,nome,email,senha);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Usuario userExistente = db.usuarioDAO().buscarPorEmail(email);
                if (userExistente == null) {
                    db.usuarioDAO().criarUsuario(novoUsuario);
                    runOnUiThread(() -> {
                        Toast.makeText(CadastroActivity.this,"Usuário cadastrado com sucesso!",Toast.LENGTH_LONG).show();

                        Intent goToLoginPage = new Intent(CadastroActivity.this, LoginActivity.class);
                        startActivity(goToLoginPage);

                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(CadastroActivity.this, "Email já cadastrado. Faça o login!", Toast.LENGTH_LONG).show();
                    });
                }
            }
        }).start();
    }
}