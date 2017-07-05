package com.snailstudio.software.calculator.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.databinding.ItemSymbolBinding
import com.snailstudio.software.calculator.presentation.model.Symbol
import org.jetbrains.anko.backgroundResource

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class SymbolAdapter(private val mSymbols: List<Symbol>,
                    var mCanCreateSymbol: Boolean?) : BaseBindingAdapter<ItemSymbolBinding>() {
    override fun getItemCount(): Int {
        var size = mSymbols.size
        if (mCanCreateSymbol!!)
            size++
        return size
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemSymbolBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        with(holder.binding) {
            if (holder.adapterPosition < mSymbols.size) {
                symbol = mSymbols[holder.adapterPosition].name
                gridItemBtn.backgroundResource = R.drawable.buttons1
            } else {
                symbol = "+"
                gridItemBtn.backgroundResource = R.drawable.buttons3
            }
            executePendingBindings()
        }

    }

    override fun getEventView(holder: DataBoundViewHolder<ItemSymbolBinding>): View?
            = holder.binding.gridItemBtn

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataBoundViewHolder<ItemSymbolBinding> {
        return DataBoundViewHolder(
                ItemSymbolBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


}