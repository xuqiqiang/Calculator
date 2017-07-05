/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.navigation

import android.app.Fragment
import android.content.Context
import com.snailstudio.software.calculator.presentation.internal.di.PerActivity
import com.snailstudio.software.calculator.presentation.view.activity.HelpActivity
import com.snailstudio.software.calculator.presentation.view.activity.ReportsActivity
import com.snailstudio.software.calculator.presentation.view.activity.SettingsActivity
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity
import javax.inject.Inject

/**
 * Class used to navigate through the application.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@PerActivity
class Navigator @Inject
constructor()//empty
{
    companion object {
        val INTENT_REQUEST_CODE_ADD_SYMBOL = 1
    }

    /**
     * Goes to the settings screen.

     * @param context A Context needed to open the destiny activity.
     */
    fun navigateToSymbol(context: Context?, fragment: Fragment?, type: Int, id: Int) {
        if (context != null) {
            val intentToLaunch = SymbolActivity.getCallingIntent(context, type, id)
            fragment!!.startActivityForResult(intentToLaunch, INTENT_REQUEST_CODE_ADD_SYMBOL)
        }
    }

    /**
     * Goes to the settings screen.

     * @param context A Context needed to open the destiny activity.
     */
    fun navigateToSettings(context: Context?) {
        if (context != null) {
            val intentToLaunch = SettingsActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }

    /**
     * Goes to the settings screen.

     * @param context A Context needed to open the destiny activity.
     */
    fun navigateToReports(context: Context?) {
        if (context != null) {
            val intentToLaunch = ReportsActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }

    /**
     * Goes to the settings screen.

     * @param context A Context needed to open the destiny activity.
     */
    fun navigateToHelp(context: Context?) {
        if (context != null) {
            val intentToLaunch = HelpActivity.getCallingIntent(context)
            context.startActivity(intentToLaunch)
        }
    }
}
