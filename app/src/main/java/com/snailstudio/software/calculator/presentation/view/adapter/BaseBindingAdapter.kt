/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.view.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by xuqiqiang on 2016/04/17.
 */
abstract class BaseBindingAdapter<B : ViewDataBinding> : RecyclerView.Adapter<DataBoundViewHolder<B>>() {
    var mOnClickListener: ((pos: Int) -> Unit)? = null
    var mOnLongClickListener: ((pos: Int) -> Boolean)? = null

    override fun onBindViewHolder(holder: DataBoundViewHolder<B>, position: Int) {
        getEventView(holder)?.setOnClickListener {
            mOnClickListener?.invoke(holder.adapterPosition)
        }

        getEventView(holder)?.setOnLongClickListener {
            mOnLongClickListener?.invoke(holder.adapterPosition)!!
        }
    }

    open fun getEventView(holder: DataBoundViewHolder<B>): View? = holder.binding.root

    fun setOnItemClickListener(listener: ((pos: Int) -> Unit)) {
        mOnClickListener = listener
    }

    fun setOnItemLongClickListener(listener: ((pos: Int) -> Boolean)) {
        mOnLongClickListener = listener
    }

}