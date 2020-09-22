package com.example.audio

import android.media.*
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.*
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val TAG = "audio-rec"
    private val recFileName = "testkjs.raw"
    private var storageDir: File? = null
    private lateinit var recFile: File
    private var recRunning: Boolean = false

    private fun recordAudio() {
        try {
            storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            recFile = File(storageDir.toString() + "/" + recFileName)
        } catch (e: IOException) {
            Log.d(TAG, "Error creating recFile: $e")
            // Error occrured creating file
        }
        try {
            val outputStream = FileOutputStream(recFile)
            val bufferedOutputStream = BufferedOutputStream(outputStream)
            val dataOutputStream = DataOutputStream(bufferedOutputStream)

            val minBufferSize = AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            val aFormat = AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(44100)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build()

            val recorder = AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.MIC)
                .setAudioFormat(aFormat)
                .setBufferSizeInBytes(minBufferSize)
                .build()

            val audioData = ByteArray(minBufferSize)

            recorder.startRecording()

            while (recRunning) {
                val numofBytes = recorder.read(audioData, 0, minBufferSize)
                if (numofBytes > 0) {
                    dataOutputStream.write(audioData)
                }
            }
            recorder.stop()
            dataOutputStream.close()

        } catch (e: IOException) {
            Log.d(TAG, "Error in outputStream: $e")
            // Some error happened
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recordAudio()

        button.setOnClickListener {
            when(recRunning){
                false -> {
                    recordingStatus.text = "Recording"
                    recRunning = true
                }
                true -> {
                    recordingStatus.text = "Start recording"
                    recRunning = false
                }
            }
    }

    /*
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
    }*/

}

    private fun playAudio(track: URL, sel: String) {
        val mediaPlayer1: MediaPlayer? = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            // setOnCompletionListener { txt1.text = sel }
            setDataSource(track.toString())
            prepare()
            start()
        }
    }
}