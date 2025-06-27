package com.example.ratioculinae.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.mut_jaeryo.circletimer.CircleTimer;

public class TemporizadorActivity extends AppCompatActivity {

    private ImageButton btnPlayPause, btnReiniciar, btnVoltar;
    private CircleTimer circleTimer;

    private Handler handler = new Handler();
    private boolean isRunning = false;

    private final int tempoMaximo = 3600; // 1 hora
    private int tempoRestante = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporizador);
        inicializarTela();
    }

    private void inicializarTela() {
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        btnVoltar = findViewById(R.id.btnVoltar);
        circleTimer = findViewById(R.id.circleTimer);

        // Começa do zero
        circleTimer.setMaximumTime(tempoMaximo);
        circleTimer.setInitPosition(0);
        tempoRestante = 0;

        circleTimer.setBaseTimerEndedListener(() -> {
            isRunning = false;
            tempoRestante = 0;
            btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_24);
        });

        configurarBotoes();
    }

    private void configurarBotoes() {
        btnPlayPause.setOnClickListener(view -> {
            if (!isRunning) {
                circleTimer.start();
                isRunning = true;
                tempoRestante = tempoMaximo;
                iniciarContagemDecrescente();
                btnPlayPause.setImageResource(R.drawable.ic_pause_foreground);
            } else {
                circleTimer.stop();
                isRunning = false;
                handler.removeCallbacksAndMessages(null);
                btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_24);
            }
        });

        btnReiniciar.setOnClickListener(view -> {
            circleTimer.reset();
            circleTimer.setInitPosition(0);
            tempoRestante = 0;
            isRunning = false;
            handler.removeCallbacksAndMessages(null);
            btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_24);
        });

        btnVoltar.setOnClickListener(v -> {
            Intent goBack = new Intent(TemporizadorActivity.this, HomePageActivity.class);
            startActivity(goBack);
        });
    }

    private void iniciarContagemDecrescente() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRunning && tempoRestante > 0) {
                    tempoRestante--;
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
}
