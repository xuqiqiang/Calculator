package com.snailstudio.library.logutils.config;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.snailstudio.library.logutils.LogUtils;
import com.snailstudio.library.logutils.adapter.LogAdapter;
import com.snailstudio.library.logutils.adapter.NativeLogAdapter;
import com.snailstudio.library.logutils.adapter.StressLogAdapter;
import com.snailstudio.library.logutils.utils.FormatDate;
import com.snailstudio.library.logutils.utils.Utils;

/**
 * Created by xuqiqiang on 2017/6/2.
 */
public class LogConfig {

    public boolean debug;
    public int level;
    public String tag;
    public LogAdapter logAdapter;
    public boolean codeInfo;
    public Context context;
    public String dirPath;
    public String defaultDate;
    public boolean reportCrash;

    private LogConfig(Builder builder) {
        this.debug = builder.debug;
        this.level = builder.level;
        this.tag = builder.tag;
        this.logAdapter = builder.logAdapter;
        if (this.logAdapter == null) {
            if (!builder.stress)
                this.logAdapter = new NativeLogAdapter();
            else
                this.logAdapter = new StressLogAdapter();
        }
        this.codeInfo = builder.codeInfo;
        this.context = builder.context;
        this.dirPath = builder.dirPath;
        this.defaultDate = builder.defaultDate;
        this.reportCrash = builder.reportCrash;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public boolean enableWrite() {
        return context != null && !TextUtils.isEmpty(dirPath);
    }

    public static class Builder {

        boolean debug;
        int level;
        String tag;
        boolean stress;
        LogAdapter logAdapter;
        boolean codeInfo;
        Context context;
        String dirPath;
        String defaultDate;
        boolean reportCrash;

        public Builder() {
            this.debug = true;
            this.level = Log.VERBOSE;
            this.tag = LogUtils.TAG;
            this.stress = true;
            this.codeInfo = true;
            this.defaultDate();
            this.reportCrash = true;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Builder tag(Object obj) {
            this.tag = Utils.getClassInfoByObject(obj);
            return this;
        }

        public Builder logAdapter(LogAdapter logAdapter) {
            this.logAdapter = logAdapter;
            return this;
        }

        public Builder stress(boolean stress) {
            this.stress = stress;
            return this;
        }

        public Builder codeInfo(boolean codeInfo) {
            this.codeInfo = codeInfo;
            return this;
        }

        public Builder enableWrite(Context context, String dirPath) {
            if (context == null)
                throw new IllegalStateException("can't enable write, context is null");
            if (TextUtils.isEmpty(dirPath))
                throw new IllegalStateException("can't enable write, dirPath is null");
            this.context = context;
            this.dirPath = dirPath;
            return this;
        }

        public Builder defaultDate() {
            this.defaultDate = FormatDate.getFormatDate();
            return this;
        }

        public Builder reportCrash(boolean reportCrash) {
            this.reportCrash = reportCrash;
            return this;
        }

        public LogConfig build() {
            return new LogConfig(this);
        }
    }
}
