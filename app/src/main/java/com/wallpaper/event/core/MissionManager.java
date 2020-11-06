package com.wallpaper.event.core;

import com.wallpaper.event.core.handle.IMissionHandler;
import com.wallpaper.event.core.handle.MissionHandleProxy;
import com.wallpaper.event.core.mission.Mission;
import com.wallpaper.event.core.queue.IMissionQueue;
import com.wallpaper.event.core.handle.MissionHandler;
import com.wallpaper.event.core.queue.MissionQueue;
import com.wallpaper.event.core.queue.WallpaperMissionQueue;

import java.util.ArrayList;
import java.util.List;

public class MissionManager {

    private static MissionManager missionManager = new MissionManager();

    public static MissionManager getInstance(){
        return missionManager;
    }

    List<MissionMonitor> monitorList = new ArrayList<>();
    IMissionHandler.MissionHandlerListener missionHandlerListener = mission -> {
        if (monitorList.isEmpty()) {
            return;
        }
        for (MissionMonitor missionMonitor : monitorList) {
            missionMonitor.handle(mission);
        }
    };

    IMissionQueue.MissionQueueMonitor missionQueueMonitor = count -> {
        if (monitorList.isEmpty()) {
            return;
        }
        for (MissionMonitor missionMonitor : monitorList) {
            missionMonitor.monitor(count);
        }
    };

    IMissionHandler missionHandler;

    public MissionManager() {
        MissionQueue missionQueue = new WallpaperMissionQueue(missionQueueMonitor);
        MissionHandler missionHandler = new MissionHandler(missionQueue,missionHandlerListener);
        this.missionHandler = new MissionHandleProxy(missionHandler);
    }

    /**
     * push消息入口
     * @param mission
     */
    public void push(Mission mission) {
        missionHandler.handle(mission);
    }

    /**
     * 开始消息分发
     */
    public void resume() {
        missionHandler.start();
    }
    /**
     * 停止消息分发
     */
    public void pause() {
        missionHandler.stop();
    }

    public void addMissionMonitor(MissionMonitor missionMonitor) {
        if (!monitorList.contains(missionMonitor)) {
            monitorList.add(missionMonitor);
        }
    }

    public void removeMissionMonitor(MissionMonitor missionMonitor) {
        monitorList.remove(missionMonitor);
    }

    /**
     * 清除消息
     */
    public void clear() {
        missionHandler.stop();
        monitorList.clear();

    }

    public void moveToNext() {
        missionHandler.moveToNext();
    }

    public interface MissionMonitor {

        void handle(Mission mission);

        void monitor(int count);
    }

}
