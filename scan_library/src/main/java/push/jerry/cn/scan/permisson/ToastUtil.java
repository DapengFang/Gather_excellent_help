package push.jerry.cn.scan.permisson;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void show(Context context, String msg) {
        show(context, msg, true);
    }

    public static void show(Context context, int resid) {
        show(context, resid, true);
    }

    public static void show(Context context, String msg, boolean isShort) {
        try {
            if (isShort) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void show(Context context, int resid, boolean isShort) {
        try {
            if (isShort) {
                Toast.makeText(context, resid, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, resid, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
