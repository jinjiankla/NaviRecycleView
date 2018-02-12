package com.example.android.recyclerview;

/**
 * @author jinjian
 * @class describe
 * @mail jinjian02@corp.netease.com
 * @time 2018/2/12 下午5:10
 */


public enum RecyclePos {
    SHOWHIDE("显示/隐藏", 8), TITLE1("介绍", 18), TITLE2("老师", 28), TITLE3("大纲", 38);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private RecyclePos(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (RecyclePos c : RecyclePos.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
