package com.gather_excellent_help.utils.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

/**
 * Created by Dapeng Fang on 2018/1/3.
 */

public class MyImageSpan extends ImageSpan {


    public MyImageSpan(Bitmap b) {
        super(b);
    }

    public MyImageSpan(Bitmap b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public MyImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public MyImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public MyImageSpan(Drawable d) {
        super(d);
    }

    public MyImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public MyImageSpan(Drawable d, String source) {
        super(d, source);
    }

    public MyImageSpan(Drawable d, String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public MyImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public MyImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public MyImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public MyImageSpan(Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();

        int transY = 0;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        } else if (mVerticalAlignment == ALIGN_BOTTOM) {
            transY = bottom - b.getBounds().bottom;
        } else {
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            transY = (y + fm.descent + y + fm.ascent) / 2
                    - b.getBounds().bottom / 2;
        }

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

}
