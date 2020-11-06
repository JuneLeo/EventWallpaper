package com.wallpaper.event.core.handle;

import com.wallpaper.event.core.mission.Mission;

public interface IMissionHandler {

    void start();

    void handle(Mission mission);

    void stop();

    void release();

    void moveToNext();

    interface MissionHandlerListener {
        void handler(Mission mission);
    }
}
