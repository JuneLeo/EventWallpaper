package com.wallpaper.event.core.queue;

import android.graphics.Color;

import com.wallpaper.event.core.mission.Mission;
import com.wallpaper.event.core.wallpaper.Wallpaper;

public class WallpaperMissionQueue extends MissionQueue {
    Mission defaultWallpaperMission;

    public WallpaperMissionQueue(MissionQueueMonitor missionQueueMonitor) {
        super(missionQueueMonitor);
        Wallpaper wallpaper = new Wallpaper("我是默认壁纸","我是默认壁纸");
        wallpaper.color = Color.GRAY;
        defaultWallpaperMission = new DefaultWallpaperMission(new Mission.Builder(wallpaper)
                .perShowTime(Long.MAX_VALUE)
                .weight(Integer.MAX_VALUE));

    }

    @Override
    public Mission poll() {
        Mission mission = super.poll();
        if (mission == null){
            mission = defaultWallpaperMission;
        }
        return mission;
    }

    public static class DefaultWallpaperMission extends Mission{


        public DefaultWallpaperMission(Builder builder) {
            super(builder);
        }

        @Override
        public boolean isExceptionPull() {
            return false;
        }
    }
}
