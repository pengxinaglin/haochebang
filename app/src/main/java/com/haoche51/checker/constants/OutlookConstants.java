package com.haoche51.checker.constants;

public class OutlookConstants {
	/**
	 * 覆盖件
	 */
    public static final String [] COVERING_PART = {
		"引擎盖",
		"右前翼子板",
		"右前门",
		"右后门",
		"右后翼子板",
		"行李箱盖",
		"左后翼子板",
		"左后门",
		"左前门",
		"左前翼子板",
		"车顶",
	};
    /**
     * 外观件
     */
    public static final String[] OUTER_PART = {
    	"前保险杠",
    	"后保险杠",
    	"左反光镜",
    	"右反光镜",
    	"左前大灯",
    	"右前大灯",
    	"左后尾灯",
    	"右后尾灯",
    };
    /**
     * 玻璃检查
     */
    public static final String[] GLASS_PART = {
    	"前挡风玻璃",
    	"后挡风玻璃",
    	"左前门玻璃",
    	"左后门玻璃",
    	"右前门玻璃",
    	"右后门玻璃",
    };
    
    public static final int COVERING_NORMAL = 0;
    public static final int COVERING_PAINTING = 1;
    public static final int COVERING_METALREPAIR = 2;
    public static final int COVERING_REPLACE = 3;
    
    public static final int OUTLOOK_NORMAL = 0;
    public static final int OUTLOOK_SCRATCH = 1;
    public static final int OUTLOOK_BROKEN = 2;
    public static final int OUTLOOK_PAINTING = 3;
    public static final int OUTLOOK_REPLACE = 4;
    
    public static final int GLASS_NORMAL =0;
    public static final int GLASS_CRACKS=1;
    public static final int GLASS_BROKEN =2;
    public static final int GLASS_CHANGE = 3;

}
