/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.entity.calculator

import android.content.Context.VIBRATOR_SERVICE
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR2
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.os.Vibrator
import android.util.Log
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.RESPOND_BUTTON
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.RESPOND_HUMAN
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.RESPOND_VIBRATOR
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.respond
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.vibratorTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by xuqiqiang on 2016/04/17.
 */
@Singleton
class Speaker
@Inject
constructor(private val context: android.content.Context) {
    private var sp: SoundPool? = null

    private var vibrator: Vibrator? = null

    private var initCompleted: Boolean = false

    private val mHandler: Handler

    val mHandlerThread: HandlerThread

    init {
        initSoundPool()

        vibrator = context
                .getSystemService(VIBRATOR_SERVICE) as Vibrator

        mHandlerThread = HandlerThread("CalculatorSpeaker")
        mHandlerThread.start()

        mHandler = object : Handler(mHandlerThread.looper) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                handleMessages(msg)
            }
        }

    }

    fun handleMessages(msg: Message?) {
        val str = msg!!.obj as String?
        when (msg.what) {
            0 -> {
                playString(str)
            }
            1 -> {
                readString(str)
            }
        }
    }

    @android.annotation.TargetApi(JELLY_BEAN_MR2)
    fun onDestroy() {
        if (SDK_INT >= JELLY_BEAN_MR2)
            mHandlerThread.quitSafely()
    }

    private fun initSoundPool() {
        object : Thread() {
            override fun run() {

                sp = android.media.SoundPool(4, AudioManager.STREAM_MUSIC, 100)
                button = sp!!.load(context, R.raw.button, 1)
                equal = sp!!.load(context, R.raw.equal, 1)
                num = IntArray(10)
                for (i in 0..9) {
                    num[i] = sp!!.load(context, R.raw.num_0 + i, 1)
                }

                ten = sp!!.load(context, R.raw.num_ten, 1)
                hundred = sp!!.load(context, R.raw.num_hundred, 1)
                thousand = sp!!.load(context, R.raw.num_thousand, 1)
                tenthousand = sp!!.load(context, R.raw.num_tenthousand, 1)
                hundredmillion = sp!!.load(context, R.raw.num_hundredmillion, 1)

                plus = sp!!.load(context, R.raw.plus, 1)
                sub = sp!!.load(context, R.raw.minus, 1)
                mul = sp!!.load(context, R.raw.multiply, 1)
                div = sp!!.load(context, R.raw.divide, 1)
                bracketL = sp!!.load(context, R.raw.leftbracket, 1)
                bracketR = sp!!.load(context, R.raw.rightbracket, 1)

                point = sp!!.load(context, R.raw.point, 1)
                power = sp!!.load(context, R.raw.power, 1)

                E = sp!!.load(context, R.raw.e, 1)
                factorial = sp!!.load(context, R.raw.factorial, 1)
                fu = sp!!.load(context, R.raw.fuhao, 1)
                initCompleted = true

            }
        }.start()
    }

    fun playString(str: String?) {

        mHandler.post {
            if (respond == RESPOND_VIBRATOR) {
                vibrator!!.vibrate(vibratorTime.toLong())
                return@post
            }

            if (!initCompleted)
                return@post
            if (respond == RESPOND_BUTTON ||
                    str == null || str.length != 1)
                sp!!.play(button, 1f, 1f, 0, 0, 1f)
            else if (respond == RESPOND_HUMAN)
                playChar(str[0])
        }

    }

    private fun playChar(ch: Char) {
        if (ch in '0'..'9') {
            sp!!.play(num[ch - '0'], 1f, 1f, 0, 0, 1f)
            return
        }

        when (ch) {
            '=' -> sp!!.play(equal, 1f, 1f, 0, 0, 1f)
            '+' -> sp!!.play(plus, 1f, 1f, 0, 0, 1f)
            '-' -> sp!!.play(sub, 1f, 1f, 0, 0, 1f)
            '×' -> sp!!.play(mul, 1f, 1f, 0, 0, 1f)
            '÷' -> sp!!.play(div, 1f, 1f, 0, 0, 1f)
            '.' -> sp!!.play(point, 1f, 1f, 0, 0, 1f)
            ',' -> sp!!.play(button, 1f, 1f, 0, 0, 1f)
            '(' -> sp!!.play(bracketL, 1f, 1f, 0, 0, 1f)
            ')' -> sp!!.play(bracketR, 1f, 1f, 0, 0, 1f)
            'E' -> sp!!.play(E, 1f, 1f, 0, 0, 1f)
            '!' -> sp!!.play(factorial, 1f, 1f, 0, 0, 1f)
        }
    }

    fun readString(str: String?) {
        mHandler.post {

            if (respond == RESPOND_VIBRATOR) {
                vibrator!!.vibrate(vibratorTime.toLong())
                return@post
            }
            if (!initCompleted)
                return@post
            if (respond == RESPOND_BUTTON) {
                sp!!.play(button, 1f, 1f, 0, 0, 1f)
                return@post
            }
            if (str.isNullOrEmpty() ||
                    str!!.indexOf(' ') != -1 ||
                    str.indexOf('\n') != -1 ||
                    str[0] !in '0'..'9') {
                sp!!.play(equal, 1f, 1f, 0, 0, 1f)
                return@post
            }

            var i: Int = 0
            val pointId: Int
            while (i < str.length && str[i] != '.') {
                i++
            }
            pointId = i
            playMedia(R.raw.equal, str, 0, pointId, true)
        }
    }

    private fun playMedia(str: String, id: Int, pointId: Int,
                          read0: Boolean) {

        if (id >= str.length)
            return

        if (str[id] == '0' && !read0) {
            Log.v("playMedia1", ((pointId - 2 - id) % 4).toString() + "")
            if (id < pointId - 1 && (pointId - 2 - id) % 4 == 3) {
                var sound = R.raw.num_tenthousand
                if ((pointId - 1 - id) % 8 == 0)
                    sound = R.raw.num_hundredmillion

                var i: Int = id + 1
                while (i < str.length && str[i] == '0'
                        && str[i] != '.' && (pointId - 2 - i) % 4 != 3) {
                    i++
                }

                if (i == str.length || str[i] == '.'
                        || (pointId - 2 - i) % 4 == 3)
                    playMedia(sound, str, id + 1, pointId, false)
                else
                    playMedia(sound, str, id + 1, pointId, true)
            } else
                playMedia(str, id + 1, pointId, false)
            return
        }

        var sound = R.raw.num_0
        if (str[id] == '-')
            sound = R.raw.fuhao
        else if (str[id] == '.')
            sound = R.raw.point
        else if (str[id] in '0'..'9') {
            sound = R.raw.num_0 + (str[id] - '0')
        }

        val mp = MediaPlayer.create(context, sound)

        /*val listenerCompletion = OnCompletionListener { mp ->
            mHandler.postDelayed({
                mp.release()

                if (str[id] in '1'..'9' // 0不说位数
                        && id < pointId - 1) {
                    val bit = (pointId - 2 - id) % 4
                    var sound = R.raw.num_ten// ten;"12340234.0001"
                    if (bit == 1)
                        sound = R.raw.num_hundred
                    else if (bit == 2)
                        sound = R.raw.num_thousand
                    else if (bit == 3) {
                        sound = R.raw.num_tenthousand
                        if ((pointId - 1 - id) % 8 == 0)
                            sound = R.raw.num_hundredmillion
                    }

                    Log.v("playMedia1", bit.toString() + "")

                    var i: Int = id + 1
                    while (i < str.length
                            && str[i] == '0'
                            && str[i] != '.'
                            && (pointId - 2 - i) % 4 != 3) {
                        i++
                    }

                    Log.v("playMedia1", i.toString() + "")

                    if (i == str.length || str[i] == '.'
                            || (pointId - 2 - i) % 4 == 3)
                        playMedia(sound, str, id + 1, pointId, false)
                    else
                        playMedia(sound, str, id + 1, pointId, true)
                } else if (str[id] == '0' && id < pointId - 1) {
                    playMedia(str, id + 1, pointId, false)
                } else
                    playMedia(str, id + 1, pointId, true)
            }, 10)
        }
        mp.setOnCompletionListener(listenerCompletion)*/
        mp.isLooping = false
        try {
            mp.seekTo(0)
            mp.start()

            mHandler.postDelayed({
                mp.release()

                if (str[id] in '1'..'9' // 0不说位数
                        && id < pointId - 1) {
                    val bit = (pointId - 2 - id) % 4
                    var sound = R.raw.num_ten// ten;"12340234.0001"
                    if (bit == 1)
                        sound = R.raw.num_hundred
                    else if (bit == 2)
                        sound = R.raw.num_thousand
                    else if (bit == 3) {
                        sound = R.raw.num_tenthousand
                        if ((pointId - 1 - id) % 8 == 0)
                            sound = R.raw.num_hundredmillion
                    }

                    Log.v("playMedia1", bit.toString() + "")

                    var i: Int = id + 1
                    while (i < str.length
                            && str[i] == '0'
                            && str[i] != '.'
                            && (pointId - 2 - i) % 4 != 3) {
                        i++
                    }

                    Log.v("playMedia1", i.toString() + "")

                    if (i == str.length || str[i] == '.'
                            || (pointId - 2 - i) % 4 == 3)
                        playMedia(sound, str, id + 1, pointId, false)
                    else
                        playMedia(sound, str, id + 1, pointId, true)
                } else if (str[id] == '0' && id < pointId - 1) {
                    playMedia(str, id + 1, pointId, false)
                } else
                    playMedia(str, id + 1, pointId, true)
            }, mp.duration.toLong())
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    private fun playMedia(sound: Int, str: String, id: Int,
                          pointId: Int, read0: Boolean) {
        val mp = MediaPlayer.create(context, sound)
        /*val listenerCompletion = OnCompletionListener { mp ->
            mHandler.postDelayed({
                mp.release()
                playMedia(str, id, pointId, read0)
            }, 100)
        }
        mp.setOnCompletionListener(listenerCompletion)*/
        mp.isLooping = false
        try {
            mp.seekTo(0)
            mp.start()
            mHandler.postDelayed({
                mp.release()
                playMedia(str, id, pointId, read0)
            }, mp.duration.toLong())
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    companion object {
        var button: Int = 0
        var equal: Int = 0
        lateinit var num: IntArray
        var ten: Int = 0
        var hundred: Int = 0
        var thousand: Int = 0
        var tenthousand: Int = 0
        var hundredmillion: Int = 0
        var plus: Int = 0
        var sub: Int = 0
        var mul: Int = 0
        var div: Int = 0
        var point: Int = 0
        var bracketL: Int = 0
        var bracketR: Int = 0
        var power: Int = 0
        var E: Int = 0
        var factorial: Int = 0
        var fu: Int = 0
    }
}