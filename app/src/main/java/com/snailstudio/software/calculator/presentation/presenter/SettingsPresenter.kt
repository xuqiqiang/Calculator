package com.snailstudio.software.calculator.presentation.presenter

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import com.snailstudio.library.baseview.CustomDialog
import com.snailstudio.library.baseview.SeekbarDialog
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.KEY_POINT_SUM_MAX
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.KEY_POINT_SUM_MIN
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.KEY_VIBRATOR_TIME_MAX
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.KEY_VIBRATOR_TIME_MIN
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.RESPOND_VIBRATOR
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.autoSave
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.make
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.pointSum
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.respond
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.vibratorTime
import com.snailstudio.software.calculator.presentation.internal.di.PerActivity
import com.snailstudio.software.calculator.presentation.view.SettingsView
import javax.inject.Inject

/**
 * [Presenter] that controls communication between views and models of the presentation
 * layer.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@PerActivity
class SettingsPresenter @Inject
constructor() : Presenter {

    private var mContext: Activity? = null

    override fun onSelectSymbol(symbol: String) {
    }

    private var mSettingsView: SettingsView? = null

    fun setView(view: SettingsView) {
        this.mSettingsView = view
        this.mContext = mSettingsView?.context()
    }

    fun refreshView() {
        this.mSettingsView!!.refreshView(autoSave, respond == RESPOND_VIBRATOR)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        refreshView()
    }

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy() {
        this.mSettingsView = null
    }

    fun onCbAutoSaveCheck(b: Boolean) {
        autoSave = b
    }

    fun onButtonPointSumClick() {
        SeekbarDialog(mContext!!,
                mContext!!.getString(R.string.help_set_point_sum),
                mContext!!.getString(R.string.help_set_detail_point_sum), pointSum,
                KEY_POINT_SUM_MIN,
                KEY_POINT_SUM_MAX).setOnClickListener(object : SeekbarDialog.OnSeekbarDialogClickListener() {
            override fun onEnsure(number: Int) {
                pointSum = number
            }
        }).show()
    }

    fun onButtonMakeClick() {
        val listener = DialogInterface.OnClickListener { dialog, which ->
            make = which
            dialog.cancel()
        }
        val customBuilder = CustomDialog.Builder(
                mContext)
        customBuilder.setTitle(R.string.keytools)
                .setItems(R.array.make_select, make, listener)
                .setPositiveButton(null, null)
                .setNegativeButton(null, null).create().show()
    }

    fun onButtonKeyRespondClick() {
        val listener = DialogInterface.OnClickListener { dialog, which ->
            respond = which
            refreshView()
            dialog.cancel()
        }
        val customBuilder = CustomDialog.Builder(
                mContext)
        customBuilder.setTitle(R.string.keytools)
                .setItems(R.array.respond_select, respond, listener)
                .setPositiveButton(null, null)
                .setNegativeButton(null, null).create().show()
    }

    fun onButtonVibratorTimeClick() {
        SeekbarDialog(mContext!!,
                mContext!!.getString(R.string.help_set_vibrator_time),
                mContext!!.getString(R.string.help_set_detail_vibrator_time), vibratorTime,
                KEY_VIBRATOR_TIME_MIN,
                KEY_VIBRATOR_TIME_MAX).setOnClickListener(object : SeekbarDialog.OnSeekbarDialogClickListener() {
            override fun onEnsure(number: Int) {
                vibratorTime = number
            }
        }).show()
    }

}
