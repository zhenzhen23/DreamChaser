package com.example.dreamchaser.Fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dreamchaser.MainActivity
import com.example.dreamchaser.R
import kotlinx.android.synthetic.main.fragment_pomodoro.*
import java.util.*
import java.util.concurrent.TimeUnit

class PomodoroFragment : Fragment() {

    private var isPaused = false
    private var isCancelled = false
    private var resumeFromMillis: Long = 0
    private var isBreak = false
    private var count: Int = 0
    private var focusTime = TimeUnit.MINUTES.toMillis(25)
    private var shortBreakTime = TimeUnit.MINUTES.toMillis(5)
    private var longBreakTime = TimeUnit.MINUTES.toMillis(15)
    //Thread.sleep(3_000)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pomodoro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mainActivity: MainActivity = activity as MainActivity

        btnStart.setOnClickListener { view ->

            mainActivity.disableBottomNav()

            if (isBreak) {
                tvTimeTitle.text = "Take a break"

                if (isPaused) {
                    breakTimer(resumeFromMillis, 1000).start()

                    isCancelled = false
                    isPaused = false

                    btnStart.isEnabled = false
                    btnPause.isEnabled = true
                    btnReset.isEnabled = true
                } else {
                    breakTimer(focusTime + 500, 1000).start()

                    isCancelled = false
                    isPaused = false

                    btnStart.isEnabled = false
                    btnPause.isEnabled = true
                    btnReset.isEnabled = true
                }
            } else {
                tvTimeTitle.text = "Focus your work"

                if (isPaused) {
                    workTimer(resumeFromMillis, 1000).start()

                    isCancelled = false
                    isPaused = false

                    btnStart.isEnabled = false
                    btnPause.isEnabled = true
                    btnReset.isEnabled = true
                } else {
                    workTimer(focusTime + 500, 1000).start()

                    isCancelled = false
                    isPaused = false

                    btnStart.isEnabled = false
                    btnPause.isEnabled = true
                    btnReset.isEnabled = true
                }
            }
        }

        btnPause.setOnClickListener { view ->

            tvTimeTitle.text = "Timer paused"
            isPaused = true
            isCancelled = false

            btnStart.isEnabled = true
            btnPause.isEnabled = false
        }

        btnReset.setOnClickListener { view ->

            mainActivity.enableBottomNav()

            tvTimeTitle.text = "Click Start to start your term"
            tvTimer.text = "25 : 00"
            tvRound.text = "Round 1"
            isPaused = false
            isCancelled = true
            isBreak = false
            count = 0

            btnStart.isEnabled = true
            btnPause.isEnabled = false
            btnReset.isEnabled = false
        }
    }

    private fun workTimer(duration: Long, countDownInterval: Long): CountDownTimer {
        return object : CountDownTimer(duration, countDownInterval) {

            override fun onTick(l: Long) {
                var sDuration: String = String.format(
                    Locale.ENGLISH,
                    "%02d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes(l),
                    TimeUnit.MILLISECONDS.toSeconds(l) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
                )

                if (isPaused) {
                    resumeFromMillis = l
                    cancel()
                } else if (isCancelled) {
                    cancel()
                } else {
                    if (activity != null) {
                        tvTimer.text = sDuration
                    }
                }
            }

            override fun onFinish() {
                isBreak = true
                //TimeUnit.MINUTES.toMillis(1)
                if (count == 3) {
                    if (activity != null) {
                        tvTimeTitle.text = "Take a long break"
                    }
                    breakTimer(longBreakTime + 500, 1000).start()
                } else {
                    if (activity != null) {
                        tvTimeTitle.text = "Take a short break"
                    }
                    breakTimer(shortBreakTime + 500, 1000).start()

                }
            }
        }
    }

    private fun breakTimer(duration: Long, countDownInterval: Long): CountDownTimer {
        return object : CountDownTimer(duration, countDownInterval) {
            override fun onTick(l: Long) {
                var sDuration: String = String.format(
                    Locale.ENGLISH,
                    "%02d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes(l),
                    TimeUnit.MILLISECONDS.toSeconds(l) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
                )

                if (isPaused) {
                    resumeFromMillis = l
                    cancel()
                } else if (isCancelled) {
                    cancel()
                } else {
                    if (activity != null) {
                        tvTimer.text = sDuration
                    }
                }
            }

            override fun onFinish() {
                count++
                isBreak = false
                isCancelled = false

                if (activity != null) {
                    tvRound.text = "Round ${count + 1}"
                }

                if (count < 4) {
                    if (activity != null) {
                        tvTimeTitle.text = "Focus your work"
                    }
                    workTimer(focusTime + 500, 1000).start()
                } else {
                    if (activity != null) {
                        tvRound.text = "finish all round"
                        tvTimeTitle.text = "Done!!!"
                    }
                }
            }
        }
    }
}