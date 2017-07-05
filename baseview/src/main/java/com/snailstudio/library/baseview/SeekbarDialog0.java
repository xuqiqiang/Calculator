package com.snailstudio.library.baseview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.snailstudio.library.utils.DisplayUtils;

/**
 * 设置数值的对话框
 *
 * @author xqq
 */
public class SeekbarDialog0 {

    private int min;
    private int max;
    private CustomDialog.Builder customBuilder = null;
    private Context mContext;
    private EditText et;
    private OnCustomSeekBarChangeListener onCustomSeekBarChangeListener = null;

    public SeekbarDialog0(Context context, String title, String help, int now, int min,
                          int max) {

        this.mContext = context;

        this.min = min;
        this.max = max;

        customBuilder = new CustomDialog.Builder(context);
        customBuilder.setTitle(title).setContentView(initView(help, now));

    }

    private View initView(String help, int now) {
        LinearLayout view = new LinearLayout(mContext);
        view.setOrientation(LinearLayout.VERTICAL);
        final SeekBar sb = new SeekBar(mContext);
        sb.setMax(max - min);
        sb.setProgress(now);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                et.setText((sb.getProgress() + min) + "");
                if (onCustomSeekBarChangeListener != null)
                    onCustomSeekBarChangeListener.onProgressChanged(sb
                            .getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        view.addView(sb, new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        LinearLayout view_point = new LinearLayout(mContext);
        view_point.setGravity(Gravity.CENTER);
        view_point.setOrientation(LinearLayout.HORIZONTAL);

        TextView tv = new TextView(mContext);
        tv.setTextSize(20);
        tv.setTextColor(0xff000000);
        tv.setText(help);

        view_point.addView(tv, new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        et = new EditText(mContext);
        et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        et.setText(now + "");
        et.setTextColor(0xff000000);
        et.setBackgroundColor(0xffffffff);
        int padding = (int) DisplayUtils.dip2px(mContext, 8);
        et.setPadding(padding, padding, padding, padding);
        view_point.addView(et, new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        sb.setFocusable(true);

        view.addView(view_point, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        return view;
    }

    public String getText() {
        return et.getText().toString();
    }

    public int getNumber() {
        int n = min - 1;
        try {
            n = Integer.parseInt(getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n;
    }

    public void setOnClickListener(OnClickListener on_positive_listener,
                                   OnClickListener on_negative_listener) {
        customBuilder.setPositiveButton(R.string.ok, on_positive_listener);
        if (on_negative_listener != null) {
            customBuilder.setNegativeButton(R.string.cancel,
                    on_negative_listener);
            customBuilder.setOnKeyBackListener(on_negative_listener);
        } else {
            customBuilder.setNegativeButton(R.string.cancel, null);
        }
    }

    public void setOnClickListener(final OnSeekbarDialogClickListener listener) {
        customBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int n = getNumber();
                if (n < min || n > max) {
                    ToastMaster.showToast(mContext, R.string.input_error);
                    return;
                }
                if (listener != null)
                    listener.onEnsure(n);
                dialog.cancel();
            }
        });
        DialogInterface.OnClickListener onNegativeListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onCancel();
                dialog.cancel();
            }
        };
        customBuilder.setNegativeButton(R.string.cancel, onNegativeListener);
        customBuilder.setOnKeyBackListener(onNegativeListener);
    }

    public void setOnCustomSeekBarChangeListener(
            OnCustomSeekBarChangeListener onCustomSeekBarChangeListener) {
        this.onCustomSeekBarChangeListener = onCustomSeekBarChangeListener;
    }

    public void setEditText(String str) {
        et.setText(str);
    }

    public void show() {
        CustomDialog dialog = customBuilder.create();
        dialog.show();

        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public interface OnSeekbarDialogClickListener {
        public void onEnsure(int number);

        public void onCancel();
    }

    public interface OnCustomSeekBarChangeListener {
        public void onProgressChanged(int progress);
    }
}