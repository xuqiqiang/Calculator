package com.snailstudio.library.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import static com.snailstudio.library.utils.Cache.readBoolean;
import static com.snailstudio.library.utils.Cache.writeBoolean;

public class ShortcutUtils {

    public static boolean hasShortcut(Context context, String name) {
        boolean isInstallShortcut = false;
        final ContentResolver cr = context.getContentResolver();
        final String AUTHORITY = "com.android.launcher.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/favorites?notify=true");

        Cursor c = cr.query(CONTENT_URI,
                new String[] { "title", "iconResource" }, "title=?",
                new String[] { name }, null);

        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        return isInstallShortcut;
    }

    /**
     * 为程序创建桌面快捷方式
     */
    public static void createShortcut(Context context, String name,
            String className, int resourceId) {
        Intent shortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");

        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        shortcut.putExtra("duplicate", false); // 不允许重复创建

        Intent shortcutIntent = new Intent();
        shortcutIntent.setClassName(context, className);

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        // 快捷方式的图标
        ShortcutIconResource iconRes = ShortcutIconResource.fromContext(
                context, resourceId);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        context.sendBroadcast(shortcut);
        Toast.makeText(context, "已创建“" + name + "”桌面快捷方式", Toast.LENGTH_SHORT)
                .show();
    }

    public static void createShortcut(Context context) {
        createShortcut(context,
                "com.snailstdio.software.xsdk.ad.gdt.GDTSplashActivity");
    }

    public static void createShortcut(Context context, String className) {
        if (readBoolean("hasCreateShortcut", true)) {
            writeBoolean("hasCreateShortcut", false);
            ShortcutUtils.createShortcut(
                    context,
                    context.getString(context.getResources().getIdentifier(
                            "app_name", "string", context.getPackageName())),
                    className,
                    context.getResources().getIdentifier("icon", "drawable",
                            context.getPackageName()));

        }
    }

}