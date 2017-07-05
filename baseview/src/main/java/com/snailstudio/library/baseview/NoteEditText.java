package com.snailstudio.library.baseview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.snailstudio.library.utils.DisplayUtils;
import com.snailstudio.library.utils.ReflectionUtils;
import com.snailstudio.library.utils.ScreenInfo;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.snailstudio.library.utils.Cache.readBoolean;
import static com.snailstudio.library.utils.Cache.readFloat;
import static com.snailstudio.library.utils.Cache.readInt;
import static com.snailstudio.library.utils.Cache.writeFloat;
import static com.snailstudio.library.utils.Cache.writeInt;

public class NoteEditText extends AppCompatEditText {

    public static final String KEY_TEXT_SIZE = "textSize";
    public static final String KEY_SHOW_LINE = "showLine";
    public static final String KEY_SHOW_LINE_NUMBER = "showLineNumber";
    public static final float DEFAULT_VALUE_TEXT_SIZE = 20;
    public static final boolean DEFAULT_VALUE_SHOW_LINE = true;
    public static final boolean DEFAULT_VALUE_SHOW_LINE_NUMBER = true;
    private static final int LINE_OFFSET = 50;
    float displayPaddingLeft;
    private Context context;
    private boolean initialized;
    // 画笔 用来画下划线
    private Paint paint;
    private Paint mNumPaint;
    private Paint mRectPaint;
    private int mNumberLength;
    private float mTextSize; //sp
    private ScrollView mScrollView;
    private String KEY_NUMBER_LENGTH = "_numberLength";
    private boolean showLine;
    private boolean showLineNumber;
    private int mPaddingLeft;

    public NoteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        setTextCursor();
    }

    public static float getNumberTextWidth(Paint paint) {
        float[] widths = new float[1];
        paint.getTextWidths("0", widths);
        return widths[0];
    }

    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    private void setTextCursor() {
        try {
            Method createEditorIfNeeded = ReflectionUtils.getMethod("android.widget.TextView", "createEditorIfNeeded", (Class<?>) null);
            if (createEditorIfNeeded != null) {
                createEditorIfNeeded.setAccessible(true);
                createEditorIfNeeded.invoke(this, new Object[0]);
                Field editor = ReflectionUtils.getField("android.widget.TextView", "mEditor");
                if (editor != null) {
                    editor.setAccessible(true);
                    Field cursorDrawable = ReflectionUtils.getField("android.widget.Editor", "mCursorDrawable");
                    if (cursorDrawable != null) {
                        cursorDrawable.setAccessible(true);
                        Array.set(cursorDrawable.get(editor.get(this)), 0, new LineSpaceCursorDrawable(context, this));
                        Array.set(cursorDrawable.get(editor.get(this)), 1, new LineSpaceCursorDrawable(context, this));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        showLine = readBoolean(KEY_SHOW_LINE, DEFAULT_VALUE_SHOW_LINE);
        if (showLine) {
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(DisplayUtils.dip2px(context, 0.5f));
            paint.setColor(context.getResources().getColor(R.color.note_edittext_line_color));
            // 开启抗锯齿 较耗内存
            paint.setAntiAlias(true);
        }
        showLineNumber = readBoolean(KEY_SHOW_LINE_NUMBER, DEFAULT_VALUE_SHOW_LINE_NUMBER);
        if (showLineNumber) {
            mNumPaint = new Paint();
            mNumPaint.setStyle(Paint.Style.FILL);
            mNumPaint.setColor(context.getResources().getColor(R.color.note_edittext_line_number_color));
            // 开启抗锯齿 较耗内存
            mNumPaint.setAntiAlias(true);


            mPaddingLeft = getPaddingLeft();

            mRectPaint = new Paint();
            mRectPaint.setStyle(Paint.Style.FILL);
            mRectPaint.setColor(context.getResources().getColor(R.color.note_edittext_line_number_rect_color));//Color.rgb(153, 148, 252));
        }
        displayPaddingLeft = DisplayUtils.dip2px(context, 5);
        setTextSize(readFloat(KEY_TEXT_SIZE, DEFAULT_VALUE_TEXT_SIZE));
    }

    public void setScrollView(ScrollView scrollView) {
        mScrollView = scrollView;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        try {
            if (!showLine && !showLineNumber)
                return;

            if (!initialized)
                return;

            // 得到总行数
            int lineCount = getLineCount();
            // 得到每行的高度
            int lineHeight = getLineHeight();

            int lineStart = 0;

            if (mScrollView != null) {
                lineStart = (mScrollView.getScrollY() - getPaddingTop())
                        / lineHeight - LINE_OFFSET;
                lineStart = lineStart >= 0 ? lineStart : 0;
            }
            int lineEnd = lineStart
                    + ScreenInfo.getInstance().getHeightPixels() / lineHeight
                    + LINE_OFFSET * 2;

            if (showLineNumber) {

                int numberLength = getNumberLength(lineCount);
                numberLength = numberLength > 2 ? numberLength : 2;
                if (numberLength != mNumberLength) {
//                    LogUtils.d("numberLength != mNumberLength lineCount:"
//                            + lineCount + " getVisibility:"
//                            + this.getVisibility());
                    mNumberLength = numberLength;
                    writeInt(KEY_NUMBER_LENGTH,
                            mNumberLength);
                    adjustPadding();
                }

                canvas.drawRect(new Rect(0, mScrollView.getScrollY()
                        - (int) DisplayUtils.dip2px(context, 600), mPaddingLeft
                        - (int) displayPaddingLeft, mScrollView.getScrollY()
                        + ScreenInfo.getInstance().getHeightPixels()
                        + (int) DisplayUtils.dip2px(context, 600)), mRectPaint);

                float textWidth = getNumberTextWidth(mNumPaint);

                for (int i = lineStart; i < lineEnd; i++) {
                    int x = (int) (mPaddingLeft - displayPaddingLeft
                            - DisplayUtils.dip2px(context, 15) / 2 - getNumberLength(i + 1)
                            * textWidth);
                    int y = ((i + 1) * getLineHeight()) - (getLineHeight() / 8);
                    canvas.drawText(String.valueOf(i + 1), x, y, mNumPaint);

                }
            }

            if (showLine) {
                // 根据行数循环画线
                for (int i = lineStart; i < lineEnd; i++) {
                    int lineY = (i + 1) * lineHeight;
                    canvas.drawLine(
                            0,
                            this.getPaddingTop()
                                    - ViewUtils.getLineSpacingExtra(context,
                                    this) / 2 + lineY,
                            this.getWidth(),
                            this.getPaddingTop()
                                    - ViewUtils.getLineSpacingExtra(context,
                                    this) / 2 + lineY, paint);
                }
            }
        } finally {
            super.onDraw(canvas);
        }

    }

    private void adjustPadding() {
        mPaddingLeft = (int) (DisplayUtils.dip2px(context, 15)
                + displayPaddingLeft + mNumberLength * getNumberTextWidth(mNumPaint));
        setPadding(mPaddingLeft, getPaddingTop(), getPaddingRight(),
                getPaddingBottom());
    }

    public void initText(CharSequence text) {
        //if (!TextUtils.isEmpty(text))
        setText(text);
        initialized = true;
    }

    @Override
    public void setTextSize(float size) {
//        Logger.d("mTextSize"+mTextSize);
//        Logger.d("mTextSize size:"+size);
//        Logger.d("mTextSize"+Calculator.equals(size, mTextSize));
        if (!equals(size, mTextSize)) {
            super.setTextSize(size);
            mTextSize = size;
//            Logger.d("mTextSize"+mTextSize);
            if (showLineNumber) {
                mNumPaint.setTextSize(getTextSize());
                adjustPadding();
            }
            writeFloat(KEY_TEXT_SIZE, mTextSize);
        }
    }

    public boolean equals(float a, float b) {
        return a <= b + 0.000001f && a >= b - 0.000001f;
    }

    public float getTextSizeSP() {
        return mTextSize;
    }

    public void initNumberLength(String filePath) {
        if (showLineNumber) {
            KEY_NUMBER_LENGTH = filePath + KEY_NUMBER_LENGTH;
//            Logger.d("KEY_NUMBER_LENGTH:"+KEY_NUMBER_LENGTH);
            mNumberLength = readInt(KEY_NUMBER_LENGTH, getNumberLength(getLineCount()));
            adjustPadding();
        }
    }

    private int getNumberLength(int n) {
        int length = 0;
        while (n > 0) {
            n = n / 10;
            length++;
        }
        return length;
    }

}