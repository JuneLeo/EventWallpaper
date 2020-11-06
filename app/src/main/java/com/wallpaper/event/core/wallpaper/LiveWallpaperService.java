package com.wallpaper.event.core.wallpaper;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.wallpaper.event.core.MissionManager;
import com.wallpaper.event.core.mission.Mission;

public class LiveWallpaperService extends WallpaperService {
    private Context context;

    private LiveWallpaperEngine liveWallpaperEngine;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Engine onCreateEngine() {
        this.context = this;
        this.liveWallpaperEngine = new LiveWallpaperEngine();
        return this.liveWallpaperEngine;
    }

    private class LiveWallpaperEngine extends LiveWallpaperService.Engine {

        private LiveWallpaperView liveWallpaperView;

        private final SurfaceHolder surfaceHolder;

        MissionManager missionManager =  MissionManager.getInstance();

        Mission wallpaper;

        private Handler mHandler = new Handler();

        MissionManager.MissionMonitor missionMonitor = new MissionManager.MissionMonitor() {
            @Override
            public void handle(Mission mission) {
                wallpaper = mission;
                liveWallpaperView.surfaceChanged(wallpaper,surfaceHolder, -1, liveWallpaperView.getWidth(), liveWallpaperView.getHeight());
            }

            @Override
            public void monitor(int count) {

            }
        };


        GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                missionManager.moveToNext();
                return super.onDoubleTap(e);
            }
        });

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            missionManager.addMissionMonitor(missionMonitor);
            missionManager.resume();

        }

        public LiveWallpaperEngine() {
            this.surfaceHolder = getSurfaceHolder();
            this.liveWallpaperView = new LiveWallpaperView(LiveWallpaperService.this.getBaseContext(),surfaceHolder);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {//ICE_CREAM_SANDWICH_MR1  15
                return;
            } else {
                setOffsetNotificationsEnabled(true);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            gestureDetector.onTouchEvent(event);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            missionManager.resume();
            if (this.liveWallpaperView != null) {
                this.liveWallpaperView.surfaceCreated(holder);
            } else {
                //nothing
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            if (liveWallpaperView != null) {
                liveWallpaperView.surfaceChanged(wallpaper,holder, format, width, height);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
//            Log.d("song",visible?"显示壁纸":"隐藏壁纸");
            if (visible) {
                missionManager.resume();
            } else {
                missionManager.pause();
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);

            if (this.liveWallpaperView != null) {
                this.liveWallpaperView.surfaceDestroyed(holder);
            } else {
                //nothing
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            missionManager.removeMissionMonitor(missionMonitor);
        }
    }
}
