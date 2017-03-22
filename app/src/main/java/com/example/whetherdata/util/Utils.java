package com.example.whetherdata.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class Utils {

	private static final String TAG = Utils.class.getSimpleName();

	/**
     * Crops a circle out of the thumbnail photo.
     */
    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Config.ARGB_8888);

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        float halfWidth = bitmap.getWidth()/2;
        float halfHeight = bitmap.getHeight()/2;

        canvas.drawCircle(halfWidth, halfHeight, Math.max(halfWidth, halfHeight), paint);
        
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
