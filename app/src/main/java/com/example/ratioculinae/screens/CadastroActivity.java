package com.example.ratioculinae.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private EditText nomeField;
    private EditText emailField;
    private EditText senhaField;
    private Button registerButton;
    private AppDatabase db;
    private FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();
        db = AppDatabase.getInstance(getApplicationContext());

        registerButton.setOnClickListener(v -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String nome = nomeField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String senha = senhaField.getText().toString().trim();

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

        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String uuid = firebaseUser != null ? firebaseUser.getUid() : java.util.UUID.randomUUID().toString();

                        Usuario novoUsuario = new Usuario(uuid, nome, email);

                        new Thread(() -> {
                            Usuario existente = db.usuarioDAO().buscarPorEmail(email);
                            if (existente == null) {
                                db.usuarioDAO().criarUsuario(novoUsuario);
                            }
                            runOnUiThread(() -> {
                                Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CadastroActivity.this, LoginActivity.class));
                                finish();
                            });
                        }).start();
                    } else {
                        Toast.makeText(CadastroActivity.this, "Erro ao cadastrar: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
