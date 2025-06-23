package com.example.ratioculinae.utils;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class TemporizadorManager {

    private TextView tvTempo;
    private SeekBar seekBarTempo;
    private ImageButton btnPlayPause;
    private CountDownTimer countDownTimer;

    private long tempoRestante = 0;
    private long tempoOriginal = 0;
    private boolean isRunning = false;

    public TemporizadorManager(TextView tvTempo, SeekBar seekBarTempo, ImageButton btnPlayPause) {
        this.tvTempo = tvTempo;
        this.seekBarTempo = seekBarTempo;
        this.btnPlayPause = btnPlayPause;
    }

    // Configura o SeekBar
    public void configurarSeekBar() {
        seekBarTempo.setMax(3600);
        seekBarTempo.setProgress(0);

        // Atualiza o tempo conforme o SeekBar
        seekBarTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                atualizarTempoDisplay(progress);
                tempoOriginal = progress * 1000;
                tempoRestante = tempoOriginal;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Alterna entre Iniciar/Pausar
    public void toggleTemporizador() {
        if (isRunning) {
            pausarTemporizador();
        } else {
            iniciarTemporizador();
        }
    }

    // Inicia o temporizador
    private void iniciarTemporizador() {
        countDownTimer = new CountDownTimer(tempoRestante, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tempoRestante = millisUntilFinished;
                atualizarTempoDisplay((int) (tempoRestante / 1000));
            }

            @Override
            public void onFinish() {
                tvTempo.setText("00:00");
            }
        }.start();

        isRunning = true;
        btnPlayPause.setImageDrawable(ContextCompat.getDrawable(btnPlayPause.getContext(), android.R.drawable.ic_media_pause));
    }

    // Pausa o temporizador
    private void pausarTemporizador() {
        countDownTimer.cancel();
        isRunning = false;
        btnPlayPause.setImageDrawable(ContextCompat.getDrawable(btnPlayPause.getContext(), android.R.drawable.ic_media_play));
    }

    // Para e reseta o temporizador
    public void pararTemporizador() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        tempoRestante = 0;
        atualizarTempoDisplay(0);
        seekBarTempo.setProgress(0);
        isRunning = false;
        btnPlayPause.setImageDrawable(ContextCompat.getDrawable(btnPlayPause.getContext(), android.R.drawable.ic_media_play));
    }

    // Reinicia o temporizador
    public void reiniciarTemporizador() {
        if (isRunning) {
            countDownTimer.cancel();
        }
        tempoRestante = tempoOriginal;
        atualizarTempoDisplay((int) (tempoRestante / 1000));
        seekBarTempo.setProgress((int) (tempoRestante / 1000));
        isRunning = false;
        btnPlayPause.setImageDrawable(ContextCompat.getDrawable(btnPlayPause.getContext(), android.R.drawable.ic_media_play));
    }

    // Atualiza o display de tempo
    private void atualizarTempoDisplay(int segundos) {
        int minutos = segundos / 60;
        segundos = segundos % 60;
        tvTempo.setText(String.format("%02d:%02d", minutos, segundos));
    }
}
