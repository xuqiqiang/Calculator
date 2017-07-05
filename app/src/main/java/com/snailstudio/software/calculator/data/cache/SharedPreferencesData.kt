/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.cache

import com.snailstudio.library.utils.Cache.*

/**
 * Created by xuqiqiang on 2016/04/17.
 */
object SharedPreferencesData {
    val KEY_POINT_SUM = "pointSum"
    val KEY_POINT_SUM_DEFAULT = 4
    val KEY_POINT_SUM_MIN = 0
    val KEY_POINT_SUM_MAX = 16

    val KEY_MAKE = "make"
    val KEY_MAKE_DEFAULT = 0

    val KEY_RESPOND = "respond"
    val RESPOND_HUMAN = 0
    val RESPOND_BUTTON = 1
    val RESPOND_VIBRATOR = 2
    val KEY_RESPOND_DEFAULT = RESPOND_HUMAN

    val KEY_VIBRATOR_TIME = "vibratorTime"
    val KEY_VIBRATOR_TIME_DEFAULT = 20
    val KEY_VIBRATOR_TIME_MIN = 5
    val KEY_VIBRATOR_TIME_MAX = 100

    val KEY_AUTO_SAVE = "autoSave"
    val KEY_AUTO_SAVE_DEFAULT = false

    var pointSum: Int = readInt(KEY_POINT_SUM, KEY_POINT_SUM_DEFAULT)
        set(value) {
            field = value
            writeInt(KEY_POINT_SUM, value)
        }

    var respond: Int = readInt(KEY_RESPOND, KEY_RESPOND_DEFAULT)
        set(value) {
            field = value
            writeInt(KEY_RESPOND, value)
        }

    var make: Int = readInt(KEY_MAKE, KEY_MAKE_DEFAULT)
        set(value) {
            field = value
            writeInt(KEY_MAKE, value)
        }

    var vibratorTime: Int = readInt(KEY_VIBRATOR_TIME, KEY_VIBRATOR_TIME_DEFAULT)
        set(value) {
            field = value
            writeInt(KEY_VIBRATOR_TIME, value)
        }

    var autoSave: Boolean = readBoolean(KEY_AUTO_SAVE, KEY_AUTO_SAVE_DEFAULT)
        set(value) {
            field = value
            writeBoolean(KEY_AUTO_SAVE, value)
        }
}