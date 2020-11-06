package com.wallpaper.event.core.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wallpaper.event.core.mission.Mission;

/**
 * author: Coolspan
 * time: 2017/3/13 15:52
 * describe: 动态壁纸视图
 */
public class LiveWallpaperView extends SurfaceView implements SurfaceHolder.Callback {

    private Paint mPaint;

    public LiveWallpaperView(Context context,SurfaceHolder surfaceHolder) {
        super(context);
        this.surfaceHolder = surfaceHolder;
        surfaceHolder.addCallback(this);
        this.initPaintConfig();
    }

    private void initPaintConfig() {
        this.setKeepScreenOn(true);
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextSize(150);
    }





    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        holder.removeCallback(this);
        drawSurfaceView(holder);
    }

    public boolean flag;
    public void surfaceChanged(Mission wallpaper,SurfaceHolder holder, int format, int width, int height) {
        this.wallpaper = wallpaper;
        drawSurfaceView(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        drawSurfaceView(holder);
    }

    private Mission wallpaper;

    private void drawSurfaceView(SurfaceHolder holder) {
        if (wallpaper != null) {
            Canvas localCanvas = holder.lockCanvas();
            if (localCanvas != null) {
                Rect rect = new Rect();
                rect.left = rect.top = 0;
                rect.bottom = localCanvas.getHeight();
                rect.right = localCanvas.getWidth();
//                localCanvas.drawColor(wallpaper.color);
                mPaint.setColor(Color.BLACK);
                localCanvas.drawColor(wallpaper.getWallpaper().color);
                localCanvas.drawText(wallpaper.getWallpaper().content, 100,rect.bottom/2,mPaint);
                long vailTime = wallpaper.getValidPeriod();
                localCanvas.drawText("有效期："+ vailTime/1000+"秒",100,rect.bottom/2 - 200,mPaint);

                holder.unlockCanvasAndPost(localCanvas);
            }
        } else {
            Canvas localCanvas = holder.lockCanvas();
            if (localCanvas != null) {
                localCanvas.drawRGB(0, 1, 1);
                localCanvas.drawText("hahahah",0,0,mPaint);
                holder.unlockCanvasAndPost(localCanvas);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private SurfaceHolder surfaceHolder;

    @Override
    public SurfaceHolder getHolder() {
        if (surfaceHolder != null) {
            return surfaceHolder;
        }
        return super.getHolder();
    }


}
