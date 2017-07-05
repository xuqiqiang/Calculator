package com.snailstudio.software.calculator.presentation.presenter

import android.os.Bundle

/**
 * Interface representing a Presenter0 in a model view presenter (MVP) pattern.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
interface Presenter {
    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onCreate() or onViewCreated() method.
     */
    fun onCreate(savedInstanceState: Bundle?)
    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onStart() method.
     */
    fun onStart()
    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onResume() method.
     */
    fun onResume()

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onPause() method.
     */
    fun onPause()

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onStop() method.
     */
    fun onStop()

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onDestroy() method.
     */
    fun onDestroy()

    fun onSelectSymbol(symbol: String)

}
