package com.haoche51.checker.constants;

public class EquipmentConstants {

    public static final String[] EQUIPMENT_PART = {"仪表盘", "车内阅读灯", "雨刷",
            "车窗升降", "空调", "音响系统", "手刹", "中控锁", "天窗", "中控显示屏", "GPS导航", "倒车雷达",
            "倒车影像", "主驾电动座椅", "副驾电动座椅",};

    public static final String[] SAFTY_PART = {"仪表提示灯", "ABS", "安全气囊", "安全带",
            "千斤顶", "灭火器", "三角标"};

    public static final String[] LIGHT_PART = {"远光灯", "近光灯", "雾灯", "转向灯",
            "尾灯", "刹车灯", "日间行车灯"};
    /**
     * 有，无
     */
    public static final int NO_EQUIPMENT = 0;
    public static final int HAS_EQUIPMENT = 1;
    /**
     * 正常、异常
     */
    public static final int EQUIPMENT_NORMAL = 0;
    public static final int EQUIPMENT_ABNORMAL = 1;

    /**
     * 备胎状态
     */
    public static final int TIRE_NO = 0;//无
    public static final int TIRE_NIEW = 1;//全新
    public static final int TIRE_NORMAL_WEAR = 2;//正常磨损
    public static final int TIRE_HEAVY_WEAR = 3;//严重磨损

}
