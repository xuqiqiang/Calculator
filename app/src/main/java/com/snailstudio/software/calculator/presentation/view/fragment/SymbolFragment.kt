package com.snailstudio.software.calculator.presentation.view.fragment

import android.app.Activity
import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.snailstudio.library.baseview.PopMenu
import com.snailstudio.library.baseview.PopMenu.OnPopMenuItemClickListener
import com.snailstudio.library.baseview.ToastMaster
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.databinding.FragmentCreateFunctionBinding
import com.snailstudio.software.calculator.databinding.FragmentCreateValueBinding
import com.snailstudio.software.calculator.presentation.internal.di.components.CalculatorComponent
import com.snailstudio.software.calculator.presentation.presenter.SymbolPresenter
import com.snailstudio.software.calculator.presentation.view.SymbolView
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity.Companion.INTENT_EXTRA_PARAM_SYMBOL_TYPE
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity.Companion.SYMBOL_TYPE_VALUE
import org.jetbrains.anko.onClick
import org.jetbrains.anko.singleLine
import java.lang.reflect.Method
import javax.inject.Inject

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class SymbolFragment(val bundle: Bundle?)//private val type: Int?, private val id: Int?)
    : BaseBindingFragment<ViewDataBinding>(), SymbolView {

    private val type: Int? = bundle?.getInt(INTENT_EXTRA_PARAM_SYMBOL_TYPE, SYMBOL_TYPE_VALUE)
    private val id: Int? = bundle?.getInt(SymbolActivity.INTENT_EXTRA_PARAM_SYMBOL_ID, -1)
    /**
     * Show an message

     * @param message A string representing an message.
     */
    override fun showMessage(message: String) {
        ToastMaster.showToast(activity, message)
    }

    override fun getEditText(): EditText? {
        if (type == SymbolActivity.SYMBOL_TYPE_VALUE)
            return (mBinding as FragmentCreateValueBinding).content
        else
            return (mBinding as FragmentCreateFunctionBinding).content
    }

    override fun getFormTextView(): TextView? {
        if (type == SymbolActivity.SYMBOL_TYPE_VALUE)
            return null
        else
            return (mBinding as FragmentCreateFunctionBinding).form
    }

    override fun getNameEditText(): EditText? {
        if (type == SymbolActivity.SYMBOL_TYPE_VALUE)
            return (mBinding as FragmentCreateValueBinding).name
        else
            return (mBinding as FragmentCreateFunctionBinding).name
    }

    override fun getNumSeekBar(): SeekBar? {
        if (type == SymbolActivity.SYMBOL_TYPE_VALUE)
            return null
        else
            return (mBinding as FragmentCreateFunctionBinding).SeekBarNum
    }

    override fun getNumTextView(): TextView? {
        if (type == SymbolActivity.SYMBOL_TYPE_VALUE)
            return null
        else
            return (mBinding as FragmentCreateFunctionBinding).num
    }

    @Inject
    lateinit var mSymbolPresenter: SymbolPresenter

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?,
                                   savedInstanceState: Bundle?): ViewDataBinding {
        if (type == SymbolActivity.SYMBOL_TYPE_VALUE)
            return FragmentCreateValueBinding.inflate(inflater, container, false)
        else
            return FragmentCreateFunctionBinding.inflate(inflater, container, false)
    }

    override fun initView() {

        var editText: EditText? = null
        var nameEditText: EditText? = null
        var number: Array<Button>? = null
        var symbol: Array<Button>? = null
        var function: Array<Button>? = null
        if (type == SymbolActivity.SYMBOL_TYPE_VALUE) {
            activity.title = getString(R.string.title_new_value)
            with(mBinding as FragmentCreateValueBinding) {
                number = arrayOf(button0, button1, button2, button3, button4, button5,
                        button6, button7, button8, button9)
                symbol = arrayOf(buttonPlus, buttonSubtract, buttonMultiply, buttonDivide,
                        buttonDot, ButtonComma, buttonKakkoL, buttonKakkoR)
                function = arrayOf(buttonUp, buttonDown, buttonLeft, buttonRight, buttonDel,
                        buttonClear, buttonSym)
                editText = content
                nameEditText = name
            }
        } else {
            activity.title = getString(R.string.title_new_function)
            with(mBinding as FragmentCreateFunctionBinding) {
                number = arrayOf(button0, button1, button2, button3, button4, button5,
                        button6, button7, button8, button9, buttonA, buttonB, buttonC, buttonD, buttonE)
                symbol = arrayOf(buttonPlus, buttonSubtract, buttonMultiply, buttonDivide,
                        buttonDot, ButtonComma, buttonKakkoL, buttonKakkoR)
                function = arrayOf(buttonUp, buttonDown, buttonLeft, buttonRight, buttonDel,
                        buttonClear, buttonSym)
                editText = content
                nameEditText = name

                SeekBarNum.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                                   fromUser: Boolean) {
                        val parameterNum = progress + 1
                        num.text = "(" + parameterNum + "个)"
                        mSymbolPresenter.setParameterNum(parameterNum)
                        mSymbolPresenter.refreshForm()

                        var i: Int = 0
                        while (i <= progress) {
                            number!![10 + i].setBackgroundResource(R.drawable.buttons1)
                            number!![10 + i].isEnabled = true
                            i++
                        }
                        while (i < 5) {
                            number!![10 + i].setBackgroundResource(R.drawable.key_unenable)
                            number!![10 + i].isEnabled = false
                            i++
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
                })
            }
        }

        nameEditText!!.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                mSymbolPresenter.refreshForm()
            }

            override fun afterTextChanged(arg0: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }
        })

        if (android.os.Build.VERSION.SDK_INT > 10) {
            editText?.customSelectionActionModeCallback = object : ActionMode.Callback {

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode?) {
                    TODO("not implemented")
                }

                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    return false
                }

            }
        }

        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText?.inputType = InputType.TYPE_NULL
        } else {
            activity.window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            try {
                val cls = EditText::class.java
                val setShowSoftInputOnFocus: Method
                setShowSoftInputOnFocus = cls.getMethod(
                        "setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
                setShowSoftInputOnFocus.isAccessible = true
                setShowSoftInputOnFocus.invoke(editText, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        editText?.singleLine = false
        editText?.isCursorVisible = true


        for (i in 0..number!!.size - 1)
            number!![i].onClick {
                mSymbolPresenter.onButtonNumberClick(i)
            }

        for (i in 0..symbol!!.size - 1)
            symbol!![i].onClick {
                mSymbolPresenter.onButtonSymbolClick(symbol!![i].tag.toString())
            }

        for (i in 0..function!!.size - 1)
            function!![i].onClick {
                mSymbolPresenter.onButtonFunctionClick(i)
            }

    }

    override fun onTitleBack() {
        mSymbolPresenter.onButtonBackClick()
    }

    override fun initPopmenu(context: Context,
                             popMenu_itemList: ArrayList<String>,
                             popMenu_itemListDrawable: ArrayList<Int>): PopMenu.OnPopMenuItemClickListener? {
        popMenu_itemList.add(context.getString(R.string.menu_reset))
        popMenu_itemList.add(context.getString(R.string.menu_finish))

        popMenu_itemListDrawable.add(R.drawable.menu_delete)
        popMenu_itemListDrawable.add(R.drawable.menu_save)

        return OnPopMenuItemClickListener {
            which ->
            when (which) {
                0 -> {
                    mSymbolPresenter.onButtonResetClick()
                }
                1 -> {
                    mSymbolPresenter.onButtonFinishClick()
                }
            }
            true
        }
    }

    /**
     * Get a [Context].
     */
    override fun context(): Activity = activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getComponent(CalculatorComponent::class.java).inject(this)
        this.getComponent(CalculatorComponent::class.java).inject(mSymbolPresenter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mSymbolPresenter.setView(this)
        this.mSymbolPresenter.onCreate(bundle)
    }

    override fun onStart() {
        super.onStart()
        this.mSymbolPresenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        this.mSymbolPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.mSymbolPresenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        this.mSymbolPresenter.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        this.mSymbolPresenter.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }


}
