package com.wallpaper.event.core.mission;

import com.wallpaper.event.core.wallpaper.Wallpaper;

public class Mission {

    private int weight;//消息权重
    private boolean loop;// 是否轮训
    private long validPeriod;//有效期
    private long perShowTime; //每次展示时长
    private Wallpaper wallpaper;

    public Mission(Builder builder) {
        weight = builder.weight;
        loop = builder.loop;
        validPeriod = builder.validPeriod;
        perShowTime = builder.perShowTime;
        this.wallpaper = builder.wallpaper;
    }

    public static final int WEIGHT_HIGH = 1;
    public static final int WEIGHT_DEFAULT = 5;
    public static final int WEIGHT_LOW = 10;
    public static final int WEIGHT_INCREMENTAL = 5;
    public static final long DEFAULT_PER_SHOW_TIME = 3000;

    public Wallpaper getWallpaper() {
        return wallpaper;
    }

    public long getPerShowTime() {
        return perShowTime;
    }

    public void decreaseWeight() {
        weight += WEIGHT_INCREMENTAL;
    }

    public int getWeight() {
        return weight;
    }

    public long getValidPeriod() {
        long gap = validPeriod - System.currentTimeMillis();
        return gap > 0 ? gap : 0;
    }

    public boolean isValidPeriod() {
        return validPeriod > System.currentTimeMillis();
    }

    public boolean isLooper() {
        return loop;
    }

    /**
     * 只要是新发的消息优先级   大于或者等于 当前消息的优先级  就应该被替换
     * @param mission
     * @return
     */
    public boolean highMission(Mission mission) {
        return this.weight <= mission.weight;
    }

    public boolean isExceptionPull() {
        return true;
    }

    /**
     * 是否可以手动移除
     * @return
     */
    public boolean canHandRemove(){
        return true;
    }


    public static class Builder {

        private Wallpaper wallpaper;
        private int weight = WEIGHT_DEFAULT;//消息权重
        private int category;//消息分类
        private boolean loop = false;// 是否轮训
        private long validPeriod;//有效期
        private long perShowTime = DEFAULT_PER_SHOW_TIME; //每次展示时长


        public Builder(Wallpaper wallpaper) {
            this.wallpaper = wallpaper;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder loop(boolean loop) {
            this.loop = loop;
            return this;
        }

        public Builder validPeriod(long validPeriod) {
            this.validPeriod = System.currentTimeMillis() + validPeriod;
            return this;
        }

        public Builder perShowTime(long perShowTime) {
            this.perShowTime = perShowTime;
            return this;
        }

        public Mission build() {
            return new Mission(this);
        }
    }


}
