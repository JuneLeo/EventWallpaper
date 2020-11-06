package com.wallpaper.event.core.wallpaper;

import android.graphics.Color;

public class Wallpaper {
    public String title;
    public String content;
    public int color = Color.RED;
    private int category;//消息分类
    public Wallpaper(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
