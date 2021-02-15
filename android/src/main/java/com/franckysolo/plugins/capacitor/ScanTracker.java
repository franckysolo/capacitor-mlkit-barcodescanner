package com.franckysolo.plugins.capacitor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.google.mlkit.vision.barcode.Barcode;

import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;

public class ScanTracker extends View {
    private static final String TAG = "BarcodeScanTrackerView";
    private final Object lock = new Object();
    Context mContext;
    private Barcode mBarcode;
    private RectF mRect;
    private Paint mPaint;
    private Paint tPaint;
    private ImageProxy mImage;

    /**
     * Create a scan tracker
     * @param context The view context
     */
    public ScanTracker(Context context) {
        super(context);
        mContext = context;
        initPainter();
    }

    /**
     * Create a scan tracker
     *
     * @param context The scan tracker view context
     * @param attrs View attributes
     */
    public ScanTracker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPainter();
    }

    /**
     * Init paint
     */
    private void initPainter() {
        mRect = new RectF();

        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4.0f);

        tPaint = new Paint();
        tPaint.setColor(Color.GREEN);
        tPaint.setTextSize(54.0f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mBarcode != null) {
            // try to lock but seems to have no effect
            synchronized (lock) {
                if (mBarcode.getBoundingBox() != null) {
                    mRect.set(mBarcode.getBoundingBox());
                    float scaleX = (float)getWidth() / (float)mImage.getHeight();
                    float scaleY = (float)getHeight() / (float)mImage.getWidth();
                    mRect.left *= scaleX;
                    mRect.right *= scaleX;
                    mRect.top *= scaleY;
                    mRect.bottom *= scaleY;
                    canvas.drawRect(mRect, mPaint);
                    canvas.drawText(mBarcode.getRawValue(), mRect.left, mRect.top - 4.0f, tPaint);
                }
            }
        }
    }

    /**
     * Track barcode with image proxy when redraw
     *
     * @param barcode    The detected barcode
     * @param mediaImage The image proxy
     */
    public void track(Barcode barcode, ImageProxy mediaImage) {
        mBarcode = barcode;
        mImage = mediaImage;
        invalidate();
    }
}
