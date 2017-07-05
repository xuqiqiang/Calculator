package com.snailstudio.software.calculator.presentation.presenter

import android.app.Activity
import android.app.Fragment
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.util.Log
import com.snailstudio.library.baseview.CustomDialog
import com.snailstudio.library.logutils.LogUtils
import com.snailstudio.library.utils.Cache.*
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.insertString
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.isNumPoint
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.removeSpace
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.removeString
import com.snailstudio.software.calculator.data.entity.calculator.Speaker
import com.snailstudio.software.calculator.domain.interactor.*
import com.snailstudio.software.calculator.presentation.exception.ErrorMessageFactory
import com.snailstudio.software.calculator.presentation.internal.di.PerActivity
import com.snailstudio.software.calculator.presentation.model.Symbol
import com.snailstudio.software.calculator.presentation.navigation.Navigator
import com.snailstudio.software.calculator.presentation.view.CalculatorView
import com.snailstudio.software.calculator.presentation.view.activity.MainActivity
import com.snailstudio.software.calculator.presentation.view.dialog.SymbolDialog
import javax.inject.Inject

/**
 * [Presenter] that controls communication between views and models of the presentation
 * layer.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@PerActivity
class CalculatorPresenter @Inject
constructor(private val mSpeaker: Speaker) : Presenter {

    @Inject
    lateinit var navigator: Navigator

    private var mCalculatorView: CalculatorView? = null

    private var mContext: Context? = null

    @Inject
    lateinit var distinguishUseCase: Distinguish

    @Inject
    lateinit var calculateUseCase: Calculate

    @Inject
    lateinit var getSymbolsUseCase: GetSymbols

    @Inject
    lateinit var deleteSymbolUseCase: DeleteSymbol

    @Inject
    lateinit var saveReportUseCase: SaveReport

    var mSymbolDialog: SymbolDialog? = null

    var input: String? = null
    var content: String? = null
    var pre_input: String? = null

    var pre_content_len: Int = 0

    fun setView(view: CalculatorView) {
        this.mCalculatorView = view
        this.mContext = mCalculatorView?.context()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        readCache()
    }

    override fun onStart() {}

    override fun onResume() {}

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK &&
                requestCode == Navigator.INTENT_REQUEST_CODE_ADD_SYMBOL) {
            refreshSymbolDialog()
        }
    }

    private fun refreshSymbolDialog() {
        if (mSymbolDialog != null && mSymbolDialog!!.isShowing)
            getSymbolsUseCase.execute(GetSymbolsObserver(), Any())
    }

    override fun onPause() {}

    override fun onStop() {
        writeCache()
    }

    override fun onDestroy() {
        this.mCalculatorView = null
        this.distinguishUseCase.dispose()
        this.calculateUseCase.dispose()
        this.getSymbolsUseCase.dispose()
        this.deleteSymbolUseCase.dispose()
    }

    fun readCache() {
        input = removeSpace(readString("input", ""))
        content = removeSpace(readString("content", ""))

        pre_content_len = readInt("pre_content_len", 0)
        pre_input = readString("pre_input", "")

        mCalculatorView?.getEditText()?.setText(content)
        mCalculatorView?.getEditText()?.setSelection(content!!.length)
    }

    fun writeCache() {
        writeString("input", removeSpace(input))
        writeString("content", removeSpace(content))
        writeInt("pre_content_len", pre_content_len)
        writeString("pre_input", pre_input)
    }

    fun inputText(str: String) {
        val et = mCalculatorView?.getEditText()
        var selection = et?.selectionStart!!
        if (selection < pre_content_len) {
            selection = content!!.length
        }
        input = insertString(input!!, selection - pre_content_len, str)
        content = insertString(content!!, selection, str)
        et.setText(content)
        selection += str.length
        if (selection > content!!.length) {
            selection = content!!.length
        }
        et.setSelection(selection)
    }

    fun onButtonNumberClick(number: Int) {
        inputText(number.toString())
        mSpeaker.playString(number.toString())
    }

    fun onButtonSymbolClick(symbol: String) {
        inputText(symbol)
        mSpeaker.playString(symbol)
    }

    override fun onSelectSymbol(symbol: String) = onButtonSymbolClick(symbol)

    fun onButtonFunctionClick(id: Int) {
        mSpeaker.playString(null)

        var j: Int
        val et = mCalculatorView?.getEditText()
        var selection = et?.selectionStart!!

        when (id) {
            0 -> {
                j = selection - 1
                while (j > 0 && content!![j - 1] != '\n') {
                    j--
                }
                if (j < 0)
                    j = 0
                selection = j
                et.setSelection(selection)
            }
            1 -> {
                j = selection + 1
                while (j < content!!.length && content!![j - 1] != '\n') {
                    j++
                }
                if (j > content!!.length)
                    j = content!!.length
                selection = j
                et.setSelection(selection)
            }
            2 -> if (selection > 0) {
                selection--
                et.setSelection(selection)
            }
            3 -> if (selection < content!!.length) {
                selection++
                et.setSelection(selection)
            }
            4 -> if (selection > pre_content_len) {
                selection--
                Log.v(selection.toString(), "Selection")
                Log.v(input + "", "input")
                Log.v(content + "", "content")
                input = removeString(input!!, selection - pre_content_len)
                content = removeString(content!!, selection)

                et.setText(content)

                et.setSelection(selection)
            }
            5 -> {
                input = ""
                content = ""
                pre_content_len = 0
                et.setText(content)
            }
            6 -> {
                getSymbolsUseCase.execute(GetSymbolsObserver(), Any())
            }
            7 -> {
                val clipboard = mContext?.getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager
                if (clipboard.text == null)
                    return
                val str = clipboard.text.toString()
                if (!str.isNullOrEmpty()) {
                    inputText(str)
                }
            }
            8 -> {
                showCreateSymbolOptions()
            }
            9 -> {
                showCopyOptions()
            }
            10 -> {
                showTools()
            }
        }
    }

    fun dialogDeleteSymbol(symbol: Symbol) {
        CustomDialog.Builder(mContext).setTitle(R.string.prompt)
                .setMessage("是否删除" + symbol.name + "?")
                .setPositiveButton(R.string.ok,
                        { dialog, _ ->
                            deleteSymbolUseCase.execute(DeleteSymbolObserver(),
                                    DeleteSymbol.Params.forUser(symbol))
                            dialog.cancel()
                        })
                .setNegativeButton(R.string.cancel, null).create().show()
    }

    fun createSymbol(type: Int, id: Int) {
        this.navigator.navigateToSymbol(mContext, mCalculatorView as Fragment, type, id)
    }

    fun showCreateSymbolOptions() {
        val listener = DialogInterface.OnClickListener { dialog, which ->
            createSymbol(which, -1)
            dialog.cancel()
        }
        val customBuilder = CustomDialog.Builder(
                mContext)
        customBuilder.setTitle("自定义")
                .setItems(R.array.symbol_select, listener)
                .setPositiveButton(null, null)
                .setNegativeButton(null, null).create().show()
    }

    fun showCopyOptions() {
        val listener = DialogInterface.OnClickListener { dialog, which ->
            copyInput(which)
            dialog.cancel()
        }
        val customBuilder = CustomDialog.Builder(
                mContext)
        customBuilder.setTitle(R.string.keycopy)
                .setItems(R.array.copy_select, listener)
                .setPositiveButton(null, null)
                .setNegativeButton(null, null).create().show()
    }

    fun showTools() {
        if (mContext is MainActivity) {
            (mContext as MainActivity).mBinding
                    .drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun onNavigationItemSelected(id: Int): Boolean {
        when (id) {
            R.id.nav_reports -> {
                this.navigator.navigateToReports(mContext)
            }
            R.id.nav_help -> {
                this.navigator.navigateToHelp(mContext)
            }
            R.id.nav_settings -> {
                this.navigator.navigateToSettings(mContext)
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        return true
    }

    fun onToolSelected(which: Int) {
        when (which) {
            0 -> {
                this.navigator.navigateToReports(mContext)
            }
            1 -> {
                this.navigator.navigateToHelp(mContext)
            }
            2 -> {
                this.navigator.navigateToSettings(mContext)
            }
            3 -> {
                if (mContext is MainActivity) {
                    (mContext as MainActivity).mBinding
                            .drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }
    }

    fun showToolsDialog() {

        val listener = DialogInterface.OnClickListener { dialog, which ->
            onToolSelected(which)
            dialog.cancel()
        }
        val customBuilder = CustomDialog.Builder(
                mContext)
        customBuilder.setTitle(R.string.keytools)
                .setItems(R.array.tools_select, listener)
                .setPositiveButton(null, null)
                .setNegativeButton(null, null).create().show()
    }

    fun onButtonEqualClick() {
        if (input?.trim()!!.isNotEmpty()) {
            this.distinguishUseCase.execute(DistinguishObserver(),
                    Distinguish.Params.forUser(input!!))
        }
    }

    private fun copyInput(type: Int) {

        var result: String?

        var i: Int
        val start: Int
        val end: Int

        when (type) {
            0 -> {
                val et = mCalculatorView?.getEditText()
                val Selection = et?.selectionStart!!

                i = Selection - 1
                while (i >= 0 && isNumPoint(content!![i])) {
                    i--
                }
                start = i + 1

                i = Selection
                while (i < content!!.length && isNumPoint(content!![i])) {
                    i++
                }
                end = i

                if (start == end) {
                    mCalculatorView!!.showMessage("请将光标放在数值上进行复制！")
                    return
                }

                result = content!!.substring(start, end)

                val clipboard = mContext!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.text = result

                mCalculatorView!!.showMessage(result + " 已复制！")
            }
            1 -> {
                val et = mCalculatorView?.getEditText()
                val Selection = et?.selectionStart!!

                i = Selection - 1
                while (i >= 0 && content!![i] != '\n') {
                    i--
                }
                start = i + 1

                i = Selection
                while (i < content!!.length && content!![i] != '\n') {
                    i++
                }
                end = i

                result = content!!.substring(start, end)

                val clipboard = mContext!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.text = result

                mCalculatorView!!.showMessage(result + " 已复制！")
            }
            2 -> {
                result = content

                val clipboard = mContext!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.text = result

                mCalculatorView!!.showMessage("整个文本已复制！")

            }
        }
    }

    private fun reportResult(result: String) {
        this.saveReportUseCase.execute(DefaultObserver<String>(),
                SaveReport.Params.forUser(input!!, result))
    }

    private fun onCalculateComplete() {
        pre_input = input
        input = ""
        val et = mCalculatorView?.getEditText()
        et?.setText(content)
        pre_content_len = content!!.length
        et!!.setSelection(pre_content_len)
    }

    private inner class DistinguishObserver : DefaultObserver<String>() {

        override fun onError(e: Throwable) {
            e.printStackTrace()
            val errorMessage = "\n" + ErrorMessageFactory.create(mContext!!,
                    e as Exception) + "\n"
            reportResult(errorMessage)
            content += errorMessage
            onCalculateComplete()
        }

        override fun onNext(str: String) {
            LogUtils.d("onNext:" + str)
            calculateUseCase.execute(CalculateObserver(),
                    Calculate.Params.forUser(str))
        }
    }

    private inner class CalculateObserver : DefaultObserver<String>() {

        override fun onComplete() {
            onCalculateComplete()
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
            val errorMessage = "\n" + ErrorMessageFactory.create(mContext!!,
                    e as Exception) + "\n"
            reportResult(errorMessage)
            content += errorMessage
            onCalculateComplete()
        }

        override fun onNext(str: String) {
            LogUtils.d("onNext:" + str)
            val result = "\n=" + str + "\n"
            reportResult(result)
            content += result
            mSpeaker.readString(str)
        }
    }

    private inner class GetSymbolsObserver : DefaultObserver<List<Symbol>>() {

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(symbols: List<Symbol>) {
            if (mSymbolDialog == null)
                mSymbolDialog = SymbolDialog(mContext, this@CalculatorPresenter)
            mSymbolDialog!!.show(symbols)
        }

    }

    private inner class DeleteSymbolObserver : DefaultObserver<String>() {

        override fun onComplete() {
            refreshSymbolDialog()
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(result: String) {
            LogUtils.d(result)
        }

    }
}
