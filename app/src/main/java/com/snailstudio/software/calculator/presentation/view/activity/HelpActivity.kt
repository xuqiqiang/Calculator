package com.snailstudio.software.calculator.presentation.view.activity

import android.content.Context
import android.content.Intent
import com.snailstudio.software.calculator.presentation.view.fragment.HelpFragment

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class HelpActivity : BaseTitleBarActivity() {

    companion object {
        fun getCallingIntent(context: Context)
                = Intent(context, HelpActivity::class.java)
    }

    override fun initFragment() = HelpFragment()
}
