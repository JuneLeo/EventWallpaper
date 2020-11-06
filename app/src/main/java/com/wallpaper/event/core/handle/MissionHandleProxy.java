package com.wallpaper.event.core.handle;

import com.wallpaper.event.core.mission.Mission;
import com.wallpaper.event.core.queue.IMissionQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MissionHandleProxy implements IMissionHandler{

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    IMissionHandler iMissionHandler;
    public MissionHandleProxy(IMissionHandler missionHandler) {
        iMissionHandler = missionHandler;
    }


    @Override
    public void start() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                iMissionHandler.start();
            }
        });
    }

    @Override
    public void handle(Mission mission) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                iMissionHandler.handle(mission);
            }
        });

    }

    @Override
    public void stop() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                iMissionHandler.stop();
            }
        });
    }

    @Override
    public void release() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                iMissionHandler.release();
            }
        });
    }
}
