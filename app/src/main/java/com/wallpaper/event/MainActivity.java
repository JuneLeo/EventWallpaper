package com.wallpaper.event;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wallpaper.event.core.mission.Mission;
import com.wallpaper.event.core.mission.MissionFactory;
import com.wallpaper.event.core.MissionManager;
import com.wallpaper.event.core.wallpaper.LiveWallpaperService;

public class MainActivity extends AppCompatActivity {
    MissionManager missionManager = MissionManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        missionManager.addMissionMonitor(new MissionManager.MissionMonitor() {
            @Override
            public void handle(Mission mission) {
                ((TextView) findViewById(R.id.tv_content)).setText(mission.getWallpaper().content + ",剩余时长："+ mission.getValidPeriod());
            }

            @Override
            public void monitor(int count) {
                ((TextView) findViewById(R.id.tv_count)).setText(String.format("消息池中的消息数量为：%s",String.valueOf(count)));
            }
        });
    }
    public void lowClick(View view) {
        Mission mission = MissionFactory.create(Mission.WEIGHT_LOW, Color.RED);
        missionManager.push(mission);
    }

    public void highClick(View view) {
        Mission mission = MissionFactory.create(Mission.WEIGHT_HIGH,Color.BLUE);
        missionManager.push(mission);
    }



    public void normalClick(View view) {
        Mission mission = MissionFactory.create(Mission.WEIGHT_DEFAULT,Color.GREEN);
        missionManager.push(mission);
    }

    public void loopClick(View view) {
        Mission mission = MissionFactory.createVail(10000,Color.DKGRAY);
        missionManager.push(mission);
    }

    public void loopClick20(View view) {
        Mission mission = MissionFactory.createVail(20000,Color.YELLOW);
        missionManager.push(mission);
    }


    public void setWallpaper(View view) {
        try {
            Intent localIntent = new Intent();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {//ICE_CREAM_SANDWICH_MR1  15
                localIntent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);//android.service.wallpaper.CHANGE_LIVE_WALLPAPER
                //android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT
                localIntent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT
                        , new ComponentName(getApplicationContext().getPackageName()
                                , LiveWallpaperService.class.getCanonicalName()));
            } else {
                localIntent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);//android.service.wallpaper.LIVE_WALLPAPER_CHOOSER
            }
            startActivityForResult(localIntent, 1);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}