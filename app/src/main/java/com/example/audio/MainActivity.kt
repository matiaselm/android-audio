package com.example.audio

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sound1: URL = URL("https://freesound.org/data/previews/140/140822_2238878-hq.mp3")
        val sound2: URL = URL("https://freesound.org/data/previews/140/140827_2238878-hq.mp3")
        val sound3: URL = URL("https://freesound.org/data/previews/140/140789_2238878-hq.mp3")

        lifecycleScope.launch(Dispatchers.Main) {
            val ft = async(Dispatchers.IO) { playAudio(sound1, "first") }
            val st = async(Dispatchers.IO) { playAudio(sound2, "second") }
            val tt = async(Dispatchers.IO) { playAudio(sound3, "third") }

            ft.await()
            st.await()
            tt.await()
        }
    }

    private fun playAudio(track: URL, sel: String) {
        val mediaPlayer1: MediaPlayer? = MediaPlayer().apply {
            setAudioAttributes(
                    AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            )
            setOnCompletionListener { txt1.text = sel }
            setDataSource(track.toString())
            prepare()
            start()
        }
    }
}