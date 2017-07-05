package com.snailstudio.software.calculator.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.snailstudio.software.calculator.databinding.FragmentHelpBinding

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class HelpFragment
    : BaseBindingFragment<FragmentHelpBinding>() {

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?,
                                   savedInstanceState: Bundle?): FragmentHelpBinding {
        return FragmentHelpBinding.inflate(inflater, container, false)
    }

    override fun initView() {}

}
