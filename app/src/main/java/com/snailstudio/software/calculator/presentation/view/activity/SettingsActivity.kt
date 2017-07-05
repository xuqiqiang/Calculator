package com.snailstudio.software.calculator.presentation.view.activity

import android.content.Context
import android.content.Intent
import com.snailstudio.software.calculator.presentation.view.fragment.SettingsFragment

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class SettingsActivity : BaseTitleBarActivity() {

    companion object {
        fun getCallingIntent(context: Context)
                = Intent(context, SettingsActivity::class.java)
    }

    override fun initFragment() = SettingsFragment()
}
