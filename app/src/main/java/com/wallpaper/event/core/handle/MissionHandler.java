package com.wallpaper.event.core.handle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.wallpaper.event.core.mission.Mission;
import com.wallpaper.event.core.queue.IMissionQueue;

public class MissionHandler implements IMissionHandler {
    private IMissionQueue missionQueue;
    private MissionHandlerListener missionHandlerListener;
    Mission currentMission;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            handleMission();
            return true;
        }
    });
    private static final int MESSAGE_HANDLE = 1;
    private boolean isEnable;

    public MissionHandler(IMissionQueue missionQueue, MissionHandlerListener missionHandlerListener) {
        this.missionQueue = missionQueue;
        this.missionHandlerListener = missionHandlerListener;
    }

    @Override
    public void handle(Mission mission) {
        if (!isEnable()) {
            missionQueue.push(mission); //消息不处理直接push到队列
            return;
        }
        if (currentMission == null) { //当前没有消息直接处理
            currentMission = mission;
            if (isLooper(currentMission)) { //如果可以轮训加入到消息池中
                pushAndDecreaseWeight(currentMission);
            }
            dispatcherMission(currentMission); //处理消息
            return;
        } else {
            if (mission.highMission(currentMission)) { //如果新的消息优先级高
                missionQueue.push(currentMission); // 讲正在展示的消息重新放入到消息池中
                currentMission = mission; //设置新消息为当前的消息
                if (isLooper(currentMission)) {  //如果新的消息可以轮训，加入到消息池中
                    pushAndDecreaseWeight(currentMission);
                }
                dispatcherMission(currentMission); //处理消息
                return;
            }
        }
        missionQueue.push(mission);
    }

    @Override
    public void stop() {
        isEnable = false;
        handler.removeMessages(MESSAGE_HANDLE);
        if (currentMission != null && currentMission.isExceptionPull()) {
            missionQueue.push(currentMission);
            currentMission = null;
        }
    }

    @Override
    public void release() {
        stop();
        missionQueue.clear();
    }

    @Override
    public void moveToNext() {
        if (currentMission != null && currentMission.canHandRemove()) {
            missionQueue.remove(currentMission);
        }
        handleMission();
    }

    @Override
    public void start() {
        if (isExecuting()) {
            Log.d("song", "消息扩散正在执行中");
            return;
        }
        isEnable = true;
        handleMission();
    }

    private void handleMission() {
        currentMission = null;
        if (!isEnable()) { //是否开启消息扩散
            Log.d("song", "没有开启扩散消息");
            return;
        }

        currentMission = missionQueue.poll();

        if (currentMission == null) {
            Log.d("song", "消息为空");
            return;
        }
        if (isLooper(currentMission)) { // 如果是可以轮训的并且在有效期内
            pushAndDecreaseWeight(currentMission);
        }
        Log.d("song", "开始扩散消息");
        dispatcherMission(currentMission);
    }

    private boolean isLooper(Mission currentMission) {
        if (currentMission.isLooper() && currentMission.isValidPeriod()) {
            return true;
        }
        return false;
    }

    /**
     * 是否正在执行任务
     *
     * @return
     */
    private boolean isExecuting() {
        return handler.hasMessages(MESSAGE_HANDLE);
    }


    /**
     * 消息处理是否开启
     *
     * @return
     */
    private boolean isEnable() {
        return isEnable;
    }

    private void pushAndDecreaseWeight(Mission currentMission) {
        currentMission.decreaseWeight();
        missionQueue.push(currentMission);
    }

    private void dispatcherMission(Mission currentMission) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                missionHandlerListener.handler(currentMission);
                handler.removeMessages(MESSAGE_HANDLE);
                handler.sendEmptyMessageDelayed(MESSAGE_HANDLE, currentMission.getPerShowTime());
            }
        });
    }


}
