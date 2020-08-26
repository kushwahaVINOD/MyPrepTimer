package com.windroid.mypreptimer

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var isPaused:Boolean = false
    private var isStoped:Boolean = false
    private var resumeFrom:Long = 0
    var beepSpinner: Spinner? = null
    private var mp: MediaPlayer?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        beepSpinner = findViewById(R.id.beepSpinner)
        val songlist = resources.getStringArray(R.array.sondlist)
        var millisInFuture:Long
        var countDownInterval:Long
        if(beepSpinner != null){
            val adapter =ArrayAdapter(this, android.R.layout.simple_spinner_item, songlist)
            beepSpinner!!.adapter = adapter
        }


        beepSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                when(position){
                    0 -> mp = android.media.MediaPlayer.create(applicationContext, R.raw.next)
                    1 -> mp = android.media.MediaPlayer.create(applicationContext, R.raw.beep1)
                    2 -> mp = android.media.MediaPlayer.create(applicationContext, R.raw.beep2)
                }



            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        b_start.setOnClickListener(){
            millisInFuture = duration.text.toString().toLong()*60000
            countDownInterval = beepevery.text.toString().toLong()*60000

            timer(millisInFuture, countDownInterval).start()
            isPaused = false
            isStoped = false
            b_start.isEnabled = false
            b_stop.isEnabled = true
            b_pause.isEnabled = true
        }

        b_resume.setOnClickListener(){
            countDownInterval = beepevery.text.toString().toLong()*60000
            timer(resumeFrom, countDownInterval).start()
            isPaused = false
            isStoped = false
            b_start.isEnabled = false
            b_stop.isEnabled = true
            b_pause.isEnabled = true
        }

        b_pause.setOnClickListener(){
            isPaused = true
            isStoped = false
            b_pause.isEnabled = false
            b_start.isEnabled = false
            b_stop.isEnabled = false
            b_reset.isEnabled = true
        }

        b_stop.setOnClickListener(){
            isStoped = true
            isPaused = false
            b_pause.isEnabled = false
            b_start.isEnabled = true
            b_stop.isEnabled = false
            b_reset.isEnabled = true


        }

        b_reset.setOnClickListener(){
            isStoped = true
            isPaused = false
            b_pause.isEnabled = false
            b_start.isEnabled = true
            b_stop.isEnabled = false
            b_reset.isEnabled = true

            duration.text.clear()
            beepevery.text.clear()


        }

    }


    private fun timer(millisInFuture:Long,countDownInterval:Long): CountDownTimer {
        return object: CountDownTimer(millisInFuture,countDownInterval){
            override fun onTick(millisUntilFinished: Long){
                val timeRemaining = "Minutes remaining\n${millisUntilFinished/60000}"

                if (isPaused){
                    alert.text = "Paused"
                    // To ensure start timer from paused time
                    resumeFrom = millisUntilFinished
                    mp!!.pause()
                    cancel()
                }else if (isStoped){
                    alert.text = "Stopped.(Cancelled)"
                    cancel()
                }else{
                    alert.text = timeRemaining
                    mp!!.start()
                }
            }

            override fun onFinish() {
                alert.text = "Done"

            }
        }

    }
}