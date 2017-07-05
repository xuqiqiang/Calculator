package com.snailstudio.library.baseview

import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.snailstudio.library.utils.DisplayUtils

/**
 * 设置数值的对话框

 * @author xqq
 */
class SeekbarDialog(private val mContext: Context, title: String, help: String, now: Int, private val min: Int,
                    private val max: Int) {
    private var customBuilder: CustomDialog.Builder? = null
    private var et: EditText? = null
    private var onCustomSeekBarChangeListener: OnCustomSeekBarChangeListener? = null

    init {

        customBuilder = CustomDialog.Builder(mContext)
        customBuilder?.setTitle(title)?.setContentView(initView(help, now))

    }

    private fun initView(help: String, now: Int): View {
        val view = LinearLayout(mContext)
        view.orientation = LinearLayout.VERTICAL
        val sb = SeekBar(mContext)
        sb.max = max - min
        sb.progress = now

        sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                et!!.setText((sb.progress + min).toString() + "")
                if (onCustomSeekBarChangeListener != null)
                    onCustomSeekBarChangeListener!!.onProgressChanged(sb
                            .progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        view.addView(sb, LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

        val view_point = LinearLayout(mContext)
        view_point.gravity = Gravity.CENTER
        view_point.orientation = LinearLayout.HORIZONTAL

        val tv = TextView(mContext)
        tv.textSize = 20f
        tv.setTextColor(0xff000000.toInt())
        tv.text = help

        view_point.addView(tv, LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

        et = EditText(mContext)
        et!!.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        et!!.setText(now.toString() + "")
        et!!.setTextColor(0xff000000.toInt())
        et!!.setBackgroundColor(0xffffffff.toInt())
        val padding = DisplayUtils.dip2px(mContext, 8f).toInt()
        et!!.setPadding(padding, padding, padding, padding)
        view_point.addView(et, LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

        sb.isFocusable = true

        view.addView(view_point, LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT))
        return view
    }

    val text: String
        get() = et!!.text.toString()

    val number: Int
        get() {
            var n = min - 1
            try {
                n = Integer.parseInt(text)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            return n
        }

    fun setOnClickListener(on_positive_listener: OnClickListener,
                           on_negative_listener: OnClickListener?) : SeekbarDialog {
        customBuilder!!.setPositiveButton(R.string.ok, on_positive_listener)
        if (on_negative_listener != null) {
            customBuilder!!.setNegativeButton(R.string.cancel,
                    on_negative_listener)
            customBuilder!!.setOnKeyBackListener(on_negative_listener)
        } else {
            customBuilder!!.setNegativeButton(R.string.cancel, null)
        }
        return this
    }

    fun setOnClickListener(listener: OnSeekbarDialogClickListener?) : SeekbarDialog {
        customBuilder!!.setPositiveButton(R.string.ok, OnClickListener { dialog, _ ->
            val n = number
            if (n < min || n > max) {
                ToastMaster.showToast(mContext, R.string.input_error)
                return@OnClickListener
            }
            listener?.onEnsure(n)
            dialog.cancel()
        })
        val onNegativeListener = OnClickListener { dialog, which ->
            listener?.onCancel()
            dialog.cancel()
        }
        customBuilder!!.setNegativeButton(R.string.cancel, onNegativeListener)
        customBuilder!!.setOnKeyBackListener(onNegativeListener)
        return this
    }

    fun setOnCustomSeekBarChangeListener(
            onCustomSeekBarChangeListener: OnCustomSeekBarChangeListener) {
        this.onCustomSeekBarChangeListener = onCustomSeekBarChangeListener
    }

    fun setEditText(str: String) {
        et!!.setText(str)
    }

    fun show() {
        val dialog = customBuilder!!.create()
        dialog.show()

        dialog.window!!.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }

    interface OnSeekbarDialogClickListener0 {
        fun onEnsure(number: Int)

        fun onCancel()
    }

    open class OnSeekbarDialogClickListener {
        open fun onEnsure(number: Int) {
            // no-op by default.
        }

        open fun onCancel() {
            // no-op by default.
        }
    }

    interface OnCustomSeekBarChangeListener {
        fun onProgressChanged(progress: Int)
    }
}