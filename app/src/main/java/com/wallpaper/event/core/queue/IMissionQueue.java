package com.wallpaper.event.core.queue;

import com.wallpaper.event.core.mission.Mission;

public interface IMissionQueue {

    void push(Mission mission);

    Mission poll();

    boolean remove(Mission mission);

    void clear();

    boolean isEmpty();

    interface MissionQueueMonitor {
        void monitor(int count);
    }

}
