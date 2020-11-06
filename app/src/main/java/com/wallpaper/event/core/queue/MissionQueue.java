package com.wallpaper.event.core.queue;

import android.os.Handler;
import android.os.Looper;

import com.wallpaper.event.core.mission.Mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MissionQueue implements IMissionQueue {
    private List<Mission> missions = new CopyOnWriteArrayList<>();
    private MissionQueueMonitor missionQueueMonitor;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public MissionQueue(MissionQueueMonitor missionQueueMonitor) {
        this.missionQueueMonitor = missionQueueMonitor;
    }

    @Override
    public void push(Mission mission) {
        if (!missions.contains(mission)) {
            missions.add(mission);
            monitor();
        }
        sort();
    }

    private void sort() {
//        Collections.sort(missions, new Comparator<Mission>() {
//            @Override
//            public int compare(Mission o1, Mission o2) {
//                return Integer.compare(o1.getWeight(), o2.getWeight());
//            }
//        });
    }

    private void monitor() {
        if (missionQueueMonitor != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    missionQueueMonitor.monitor(missions.size());
                }
            });
        }
    }

    @Override
    public Mission poll() {
        if (missions.isEmpty()) {
            return null;
        }
        Mission mission = missions.remove(0);
        monitor();
        return mission;
    }

    @Override
    public boolean remove(Mission mission) {
        boolean remove = missions.remove(mission);
        monitor();
        return remove;
    }

    @Override
    public void clear() {
        missions.clear();
        monitor();
    }

    @Override
    public boolean isEmpty() {
        return missions.isEmpty();
    }
}
