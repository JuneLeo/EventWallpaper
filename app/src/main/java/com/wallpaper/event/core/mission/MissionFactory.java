package com.wallpaper.event.core.mission;

import com.wallpaper.event.core.mission.Mission;
import com.wallpaper.event.core.wallpaper.Wallpaper;

public class MissionFactory {

    public static int count = 0;
    public static Mission create(int weight,int color){
       Wallpaper wallpaper = new Wallpaper("消息系统","我是消息"+ (++count));
        wallpaper.color = color;
        return new Mission.Builder(wallpaper)
                .weight(weight)
                .build();
    }


    public static Mission createVail(long vailTime,int color){
        Wallpaper wallpaper = new Wallpaper("消息系统","我是消息"+ (++count));
        wallpaper.color = color;
        return new Mission.Builder(wallpaper)
                .loop(true)
                .validPeriod(vailTime)
                .build();
    }

}
