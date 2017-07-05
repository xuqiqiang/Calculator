package com.snailstudio.software.calculator.presentation.presenter

import android.app.Activity
import android.os.Bundle
import com.snailstudio.library.baseview.CustomDialog
import com.snailstudio.library.logutils.LogUtils
import com.snailstudio.library.utils.Cache.readInt
import com.snailstudio.library.utils.Cache.readString
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.insertString
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.letters
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.removeString
import com.snailstudio.software.calculator.data.entity.calculator.Speaker
import com.snailstudio.software.calculator.domain.exception.DefaultErrorBundle
import com.snailstudio.software.calculator.domain.exception.ErrorBundle
import com.snailstudio.software.calculator.domain.interactor.DefaultObserver
import com.snailstudio.software.calculator.domain.interactor.GetSymbols
import com.snailstudio.software.calculator.domain.interactor.SaveSymbol
import com.snailstudio.software.calculator.presentation.exception.ErrorMessageFactory
import com.snailstudio.software.calculator.presentation.internal.di.PerActivity
import com.snailstudio.software.calculator.presentation.model.Symbol
import com.snailstudio.software.calculator.presentation.view.SymbolView
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity.Companion.INTENT_EXTRA_PARAM_SYMBOL_ID
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity.Companion.INTENT_EXTRA_PARAM_SYMBOL_TYPE
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity.Companion.SYMBOL_ID_NONE
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity.Companion.SYMBOL_TYPE_VALUE
import com.snailstudio.software.calculator.presentation.view.dialog.SymbolDialog
import javax.inject.Inject

/**
 * [Presenter] that controls communication between views and models of the presentation
 * layer.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@PerActivity
class SymbolPresenter @Inject
constructor(private val mSpeaker: Speaker) : Presenter {

    private var mSymbolView: SymbolView? = null

    private var mContext: Activity? = null

    var mSymbolDialog: SymbolDialog? = null

    var type: Int? = 0

    var mId: Int? = SYMBOL_ID_NONE

    var parameterNumber: Int = 0

    var name: String? = null

    var input: String? = null

    var isChanged: Boolean = false

    @Inject
    lateinit var getSymbolsUseCase: GetSymbols

    @Inject
    lateinit var saveSymbolUseCase: SaveSymbol

    fun setView(view: SymbolView) {
        this.mSymbolView = view
        this.mContext = mSymbolView?.context()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.type = savedInstanceState?.getInt(INTENT_EXTRA_PARAM_SYMBOL_TYPE, SYMBOL_TYPE_VALUE)
        this.mId = savedInstanceState?.getInt(INTENT_EXTRA_PARAM_SYMBOL_ID, SYMBOL_ID_NONE)
        initialize()
    }

    fun initialize() {
        val selection: Int
        if (mId == SYMBOL_ID_NONE) {
            if (type == SymbolActivity.SYMBOL_TYPE_VALUE)
                name = "a"
            else
                name = "f"
            parameterNumber = 1
            input = ""
            selection = 0
        } else {
            if (type == SymbolActivity.SYMBOL_TYPE_VALUE) {
                name = readString("new_value_" + mId + "_name", "???")
                input = readString("new_value_" + mId + "_content", "???")
            } else {
                name = readString("new_function_" + mId + "_name", "???")
                parameterNumber = readInt("new_function_" + mId + "_num", 0)
                input = readString("new_function_" + mId + "_content", "???")
            }
            selection = input!!.length
        }

        mSymbolView?.getEditText()?.setText(input)
        mSymbolView?.getEditText()?.setSelection(selection)

        mSymbolView?.getNameEditText()?.setText(name)

        mSymbolView?.getNumSeekBar()?.max = 4
        mSymbolView?.getNumSeekBar()?.progress = parameterNumber - 1

        mSymbolView?.getNumTextView()?.text = "(" + parameterNumber + "个)"
    }

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy() {
        this.mSymbolView = null
        this.getSymbolsUseCase.dispose()
        this.saveSymbolUseCase.dispose()
    }

    fun setParameterNum(parameterNumber: Int) {
        this.parameterNumber = parameterNumber
    }

    fun refreshForm() {
        name = mSymbolView?.getNameEditText()?.text.toString()
        var str = "形式:$name("
        var i: Int = 0

        while (i < parameterNumber) {
            str += letters[i] + ","
            i++
        }// "a"+(i+1)+",";
        str = str.substring(0, str.length - 1)
        str += ")"
        mSymbolView?.getFormTextView()?.text = str
    }

    fun inputText(str: String) {
        isChanged = true

        val et = mSymbolView?.getEditText()
        var selection = et?.selectionStart!!

        input = insertString(input!!, selection, str)

        et.setText(input)

        selection += str.length
        if (selection > input!!.length) {
            selection = input!!.length
        }
        et.setSelection(selection)
        et.requestFocus()
    }

    fun onButtonNumberClick(number: Int) {
        var str = number.toString()
        if (number >= 10)
            str = letters[number - 10]
        inputText(str)
        mSpeaker.playString(number.toString())
    }

    fun onButtonSymbolClick(symbol: String) {
        if (letters.contains(symbol)) {
            this.mSymbolView?.showMessage(mContext!!.getString(R.string.exception_symbol_name_conflict))
            return
        }
        inputText(symbol)
        mSpeaker.playString(symbol)
    }

    override fun onSelectSymbol(symbol: String) {
        onButtonSymbolClick(symbol)
    }

    fun onButtonFunctionClick(id: Int) {
        mSpeaker.playString(null)
        var j: Int
        val et = mSymbolView?.getEditText()
        var selection = et?.selectionStart!!

        when (id) {
            0 -> {
                j = selection - 1
                while (j > 0 && input!![j - 1] != '\n') {
                    j--
                }
                if (j < 0)
                    j = 0
                selection = j
                et.setSelection(selection)
            }
            1 -> {
                j = selection + 1
                while (j < input!!.length && input!![j - 1] != '\n') {
                    j++
                }
                if (j > input!!.length)
                    j = input!!.length
                selection = j
                et.setSelection(selection)
            }
            2 -> if (selection > 0) {
                selection--
                et.setSelection(selection)
            }
            3 -> if (selection < input!!.length) {
                selection++
                et.setSelection(selection)
            }
            4 -> if (selection > 0) {
                selection--
                input = removeString(input!!, selection)
                et.setText(input)
                et.setSelection(selection)
            }
            5 -> {
                input = ""
                et.setText(input)
                isChanged = true
            }
            6 -> {
                getSymbolsUseCase.execute(GetSymbolsObserver(), "null")
            }
        }
        et.requestFocus()
    }

    fun onButtonBackClick() {
        if (!isChanged) {
            mContext!!.finish()
            return
        }
        CustomDialog.Builder(mContext).setTitle(R.string.prompt)
                .setMessage("是否保存修改")
                .setPositiveButton(R.string.yes,
                        { dialog, _ ->
                            onButtonFinishClick()
                            dialog.cancel()
                        })
                .setNegativeButton(R.string.no,
                        { dialog, _ ->
                            mContext!!.finish()
                            dialog.cancel()
                        })
                .setExtraButton(R.string.cancel, null).create().show()
    }

    fun onButtonResetClick() {
        CustomDialog.Builder(mContext).setTitle(R.string.prompt)
                .setMessage("是否重置输入的内容")
                .setPositiveButton(R.string.ok,
                        { dialog, _ ->
                            initialize()
                            isChanged = false
                            dialog.cancel()
                        })
                .setNegativeButton(R.string.cancel, null).create().show()
    }

    fun onButtonFinishClick() {
        saveSymbolUseCase.execute(SaveSymbolsObserver(),
                SaveSymbol.Params.forUser(name, input, type, mId, parameterNumber))
    }

    private inner class GetSymbolsObserver : DefaultObserver<List<Symbol>>() {

        override fun onError(e: Throwable) = LogUtils.e(e, "GetSymbols error")

        override fun onNext(symbols: List<Symbol>) {
            if (mSymbolDialog == null)
                mSymbolDialog = SymbolDialog(mContext, this@SymbolPresenter)
            mSymbolDialog!!.show(symbols)
        }

    }

    private inner class SaveSymbolsObserver : DefaultObserver<String>() {

        override fun onComplete() {
            mContext?.setResult(Activity.RESULT_OK)
            mContext?.finish()
        }

        override fun onError(e: Throwable) {
            this@SymbolPresenter.showErrorMessage(DefaultErrorBundle(e as Exception))
        }

        override fun onNext(message: String) {
            this@SymbolPresenter.mSymbolView?.showMessage(message)
        }
    }

    private fun showErrorMessage(errorBundle: ErrorBundle) {
        val errorMessage = ErrorMessageFactory.create(mContext!!,
                errorBundle.getException())
        this.mSymbolView?.showMessage(errorMessage)
    }
}
