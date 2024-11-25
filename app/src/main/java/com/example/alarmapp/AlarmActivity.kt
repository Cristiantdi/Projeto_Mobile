package com.example.alarmapp

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar a barra de ação
        supportActionBar?.show()


        mediaPlayer = MediaPlayer.create(this, R.raw.despertador)
        mediaPlayer.start()

        // Parar a música e finalizar a atividade quando o botão for pressionado
        binding.btnStopAlarm.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.release()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}