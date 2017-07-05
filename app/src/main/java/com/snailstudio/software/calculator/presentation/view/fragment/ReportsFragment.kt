package com.snailstudio.software.calculator.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import com.snailstudio.library.baseview.PopMenu
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.databinding.FragmentReportsBinding
import com.snailstudio.software.calculator.presentation.internal.di.components.CalculatorComponent
import com.snailstudio.software.calculator.presentation.presenter.ReportsPresenter
import com.snailstudio.software.calculator.presentation.view.ReportsView
import org.jetbrains.anko.singleLine
import java.lang.reflect.Method
import javax.inject.Inject

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class ReportsFragment
    : BaseBindingFragment<FragmentReportsBinding>(), ReportsView {

    override fun initContent(reports: String) {
        with(mBinding) {
            content.initText(reports)
            content.setSelection(reports.length)
        }
    }

    @Inject
    lateinit var mReportsPresenter: ReportsPresenter

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?,
                                   savedInstanceState: Bundle?): FragmentReportsBinding {
        return FragmentReportsBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        with(mBinding) {
            content.setScrollView(scrollview)

            if (android.os.Build.VERSION.SDK_INT <= 10) {
                content.inputType = InputType.TYPE_NULL
            } else {
                activity.window.setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                try {
                    val cls = EditText::class.java
                    val setShowSoftInputOnFocus: Method
                    setShowSoftInputOnFocus = cls.getMethod(
                            "setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
                    setShowSoftInputOnFocus.isAccessible = true
                    setShowSoftInputOnFocus.invoke(content, false)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            content.singleLine = false
            content.isCursorVisible = true
        }
    }

    /**
     * Get a [Context].
     */
    override fun context(): Context? = activity

    override fun initPopmenu(context: Context,
                             popMenu_itemList: ArrayList<String>,
                             popMenu_itemListDrawable: ArrayList<Int>): PopMenu.OnPopMenuItemClickListener? {
        popMenu_itemList.add(context.getString(R.string.menu_clear))

        popMenu_itemListDrawable.add(R.drawable.menu_delete)

        return PopMenu.OnPopMenuItemClickListener {
            which ->
            when (which) {
                0 -> {
                    mReportsPresenter.onButtonClearClick()
                }
            }
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getComponent(CalculatorComponent::class.java).inject(this)
        this.getComponent(CalculatorComponent::class.java).inject(mReportsPresenter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mReportsPresenter.setView(this)
        this.mReportsPresenter.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        this.mReportsPresenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        this.mReportsPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.mReportsPresenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        this.mReportsPresenter.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        this.mReportsPresenter.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }


}
