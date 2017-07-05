package com.snailstudio.software.calculator.presentation.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import com.snailstudio.library.baseview.ToastMaster
import com.snailstudio.software.calculator.databinding.FragmentCalculatorBinding
import com.snailstudio.software.calculator.presentation.internal.di.components.CalculatorComponent
import com.snailstudio.software.calculator.presentation.presenter.CalculatorPresenter
import com.snailstudio.software.calculator.presentation.view.CalculatorView
import kotlinx.android.synthetic.main.fragment_calculator.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.singleLine
import java.lang.reflect.Method
import javax.inject.Inject

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class CalculatorFragment : BaseBindingFragment<FragmentCalculatorBinding>(), CalculatorView {

    @Inject
    lateinit var mCalculatorPresenter: CalculatorPresenter

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?,
                                   savedInstanceState: Bundle?): FragmentCalculatorBinding {
        return FragmentCalculatorBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        with(mBinding) {
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


            // 获得按键
            val number = arrayOf(button0, button1, button2, button3, button4, button5,
                    button6, button7, button8, button9)
            val symbol = arrayOf(buttonPlus, buttonSubtract, buttonMultiply, buttonDivide,
                    buttonDot, ButtonComma, buttonKakkoL, buttonKakkoR)
            val function = arrayOf(buttonUp, buttonDown, buttonLeft, buttonRight, buttonDel,
                    buttonClear, buttonSym, buttonFun, buttonDef, buttonRet, buttonHis)


            if (android.os.Build.VERSION.SDK_INT <= 10) {
                editText.inputType = InputType.TYPE_NULL
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

            editText.singleLine = false
            editText.isCursorVisible = true


            for (i in 0..number.size - 1)
                number[i]?.onClick {
                    mCalculatorPresenter.onButtonNumberClick(i)
                }

            for (i in 0..symbol.size - 1)
                symbol[i]?.onClick {
                    mCalculatorPresenter.onButtonSymbolClick(symbol[i]?.tag.toString())
                }

            for (i in 0..function.size - 1)
                function[i]?.onClick {
                    mCalculatorPresenter.onButtonFunctionClick(i)
                }

            buttonEqual.onClick {
                mCalculatorPresenter.onButtonEqualClick()
            }
        }
    }

    /**
     * Get a [Context].
     */
    override fun context(): Context = activity

    override fun getEditText(): EditText? = editText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getComponent(CalculatorComponent::class.java).inject(this)
        this.getComponent(CalculatorComponent::class.java).inject(mCalculatorPresenter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mCalculatorPresenter.setView(this)
        this.mCalculatorPresenter.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        this.mCalculatorPresenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        this.mCalculatorPresenter.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        this.mCalculatorPresenter.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        this.mCalculatorPresenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        this.mCalculatorPresenter.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        this.mCalculatorPresenter.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * Show an message

     * @param message A string representing an message.
     */
    override fun showMessage(message: String) {
        ToastMaster.showToast(activity, message)
    }

    fun onNavigationItemSelected(id: Int)
            = mCalculatorPresenter.onNavigationItemSelected(id)

}
