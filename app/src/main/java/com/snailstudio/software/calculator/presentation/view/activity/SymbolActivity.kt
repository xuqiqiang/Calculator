package com.snailstudio.software.calculator.presentation.view.activity

import android.content.Context
import android.content.Intent
import com.snailstudio.software.calculator.presentation.view.fragment.SymbolFragment

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class SymbolActivity : BaseTitleBarActivity() {

    companion object {
        val INTENT_EXTRA_PARAM_SYMBOL_TYPE = "org.android10.INTENT_PARAM_SYMBOL_TYPE"
        val INTENT_EXTRA_PARAM_SYMBOL_ID = "org.android10.INTENT_PARAM_SYMBOL_ID"

        val SYMBOL_TYPE_VALUE = 0
        val SYMBOL_TYPE_FUNCTION = 1

        val SYMBOL_ID_NONE = -1
        fun getCallingIntent(context: Context, type: Int, id: Int): Intent {
            val callingIntent = Intent(context, SymbolActivity::class.java)
            callingIntent.putExtra(INTENT_EXTRA_PARAM_SYMBOL_TYPE, type)
            callingIntent.putExtra(INTENT_EXTRA_PARAM_SYMBOL_ID, id)
            return callingIntent
        }
    }

    override fun initFragment() = SymbolFragment(intent.extras)
}
