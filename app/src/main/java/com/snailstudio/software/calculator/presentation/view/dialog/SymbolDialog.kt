package com.snailstudio.software.calculator.presentation.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snailstudio.library.baseview.CustomDialog
import com.snailstudio.library.utils.DisplayUtils
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.databinding.ViewRecyclerBinding
import com.snailstudio.software.calculator.presentation.model.Symbol
import com.snailstudio.software.calculator.presentation.presenter.CalculatorPresenter
import com.snailstudio.software.calculator.presentation.presenter.Presenter
import com.snailstudio.software.calculator.presentation.view.adapter.SymbolAdapter
import org.jetbrains.anko.backgroundResource
import javax.inject.Inject

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class SymbolDialog @Inject
internal constructor(context: Context?,
                     var mPresenter: Presenter?)
    : BaseDialog<ViewRecyclerBinding>(context, R.style.CustomDialog) {

    var mSymbols: List<Symbol> = ArrayList()

    override fun createDataBinding(savedInstanceState: Bundle?): ViewRecyclerBinding {
//        val binding = DataBindingUtil.inflate<ViewRecyclerBinding>(
//                LayoutInflater.from(mContext), R.layout.view_recycler, null, false)
        val binding = ViewRecyclerBinding.inflate(
                LayoutInflater.from(mContext), null, false)
        setContentView(binding!!.root, ViewGroup.LayoutParams(
                DisplayUtils.dip2px(context, 295f).toInt(),
                DisplayUtils.dip2px(context, 400f).toInt()))
        return binding
    }

    override fun initView(savedInstanceState: Bundle?) {
        with(mBinding) {
            paddingValue = DisplayUtils.dip2px(context, 5f).toInt()
            outLayout.backgroundResource = R.drawable.symbol_bg
            recyclerView.addItemDecoration(
                    SpacesItemDecoration(DisplayUtils.dip2px(context, 2f).toInt()))
            recyclerView.layoutManager = GridLayoutManager(context, 4)

            val adapter = SymbolAdapter(mSymbols, mPresenter is CalculatorPresenter)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener {
                it ->
                if (it < mSymbols.size) {
                    mPresenter?.onSelectSymbol(mSymbols[it].showName)
                    dismiss()
                } else {
                    if (mPresenter is CalculatorPresenter) {
                        (mPresenter as CalculatorPresenter).showCreateSymbolOptions()
                    }
                }
            }
            adapter.setOnItemLongClickListener {
                it ->
                if (it < mSymbols.size) {
                    val customBuilder = CustomDialog.Builder(context).setTitle(R.string.prompt)
                            .setMessage(mSymbols[it].help)
                            .setPositiveButton(R.string.ok, null)
                    if (mPresenter is CalculatorPresenter &&
                            !mSymbols[it].isNative) {
                        customBuilder.setExtraButton("修改",
                                { dialog, _ ->
                                    (mPresenter as CalculatorPresenter).createSymbol(
                                            mSymbols[it].type,
                                            mSymbols[it].id
                                    )
                                    dialog.cancel()
                                })
                                .setNegativeButton("删除",
                                        { dialog, _ ->
                                            (mPresenter as CalculatorPresenter)
                                                    .dialogDeleteSymbol(mSymbols[it])
                                            dialog.cancel()
                                        })
                    }
                    customBuilder.create().show()
                }
                true
            }
        }
    }

    internal inner class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = space
        }
    }

    fun show(symbols: List<Symbol>) {
        (mSymbols as ArrayList<Symbol>).apply {
            clear()
            addAll(symbols)
        }
        super.show()
    }

    override fun onRefresh() = mBinding.recyclerView.adapter.notifyDataSetChanged()

    /**
     * This method will be invoked when the dialog is dismissed.

     * @param dialog The dialog that was dismissed will be passed into the
     * *            method.
     */
    override fun onDismiss(dialog: DialogInterface?) {
    }
}