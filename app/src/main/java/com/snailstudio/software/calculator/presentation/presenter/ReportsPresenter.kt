package com.snailstudio.software.calculator.presentation.presenter

import android.content.Context
import android.os.Bundle
import com.snailstudio.library.baseview.CustomDialog
import com.snailstudio.library.utils.Cache.writeInt
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.domain.interactor.DefaultObserver
import com.snailstudio.software.calculator.domain.interactor.GetReports
import com.snailstudio.software.calculator.presentation.internal.di.PerActivity
import com.snailstudio.software.calculator.presentation.view.ReportsView
import javax.inject.Inject

/**
 * [Presenter] that controls communication between views and models of the presentation
 * layer.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@PerActivity
class ReportsPresenter @Inject
constructor() : Presenter {

    @Inject
    lateinit var getReportsUseCase: GetReports

    private var mContext: Context? = null

    override fun onSelectSymbol(symbol: String) {
    }

    private var mReportsView: ReportsView? = null

    fun setView(view: ReportsView) {
        this.mReportsView = view
        this.mContext = mReportsView?.context()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getReportsUseCase.execute(GetReportsObserver(), Any())
    }

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy() {
        this.mReportsView = null
        this.getReportsUseCase.dispose()
    }

    fun onButtonClearClick() {
        CustomDialog.Builder(mContext).setTitle(R.string.prompt)
                .setMessage("是否清空记录")
                .setPositiveButton(R.string.ok,
                        { dialog, _ ->
                            writeInt("history", 0)
                            mReportsView!!.initContent("")
                            dialog.cancel()
                        })
                .setNegativeButton(R.string.cancel, null).create().show()
    }

    private inner class GetReportsObserver : DefaultObserver<String>() {

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(reports: String) {
            mReportsView!!.initContent(reports.trim())
        }

    }

}
