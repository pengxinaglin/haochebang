package com.haoche51.checker.util;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.constants.CompreConstants;
import com.haoche51.checker.constants.EngineConstants;
import com.haoche51.checker.constants.EquipmentConstants;
import com.haoche51.checker.constants.OutlookConstants;
import com.haoche51.checker.constants.StartupConstants;
import com.haoche51.checker.constants.TrimConstants;
import com.haoche51.checker.item.TireData;

public class StatusInfoUtils {

    /**
     * 发动机仓状态
     *
     * @param status
     * @return
     */
    public static String getEngineStatus(int status) {
        switch (status) {
            case EngineConstants.ENGINE_NORMAL:
                return GlobalData.resourceHelper.getArray(R.array.engine_status)[0];
            case EngineConstants.ENGINE_SLIGHT:
                return GlobalData.resourceHelper.getArray(R.array.engine_status)[1];
            case EngineConstants.ENGINE_SERIOUS:
                return GlobalData.resourceHelper.getArray(R.array.engine_status)[2];
        }
        return null;
    }

    /**
     * 覆盖件状态
     *
     * @param status
     * @return
     */
    public static String getCoverStatus(int status) {
        switch (status) {
            case OutlookConstants.COVERING_NORMAL:
                return GlobalData.resourceHelper.getArray(R.array.cover_status)[0];
            case OutlookConstants.COVERING_PAINTING:
                return GlobalData.resourceHelper.getArray(R.array.cover_status)[1];
            case OutlookConstants.COVERING_METALREPAIR:
                return GlobalData.resourceHelper.getArray(R.array.cover_status)[2];
            case OutlookConstants.COVERING_REPLACE:
                return GlobalData.resourceHelper.getArray(R.array.cover_status)[3];
        }
        return null;
    }

    /**
     * 外观件状态
     *
     * @param status
     * @return
     */
    public static String getOuterStatus(int status) {
        switch (status) {
            case OutlookConstants.OUTLOOK_NORMAL:
                return GlobalData.resourceHelper.getArray(R.array.outer_status)[0];
            case OutlookConstants.OUTLOOK_SCRATCH:
                return GlobalData.resourceHelper.getArray(R.array.outer_status)[1];
            case OutlookConstants.OUTLOOK_BROKEN:
                return GlobalData.resourceHelper.getArray(R.array.outer_status)[2];
            case OutlookConstants.OUTLOOK_PAINTING:
                return GlobalData.resourceHelper.getArray(R.array.outer_status)[3];
            case OutlookConstants.OUTLOOK_REPLACE:
                return GlobalData.resourceHelper.getArray(R.array.outer_status)[4];
        }
        return null;
    }

    /**
     * 玻璃状态
     *
     * @param status
     * @return
     */
    public static String getGlassStatus(int status) {
        switch (status) {
            case OutlookConstants.GLASS_NORMAL:
                return GlobalData.resourceHelper.getArray(R.array.glass_status)[0];
            case OutlookConstants.GLASS_CRACKS:
                return GlobalData.resourceHelper.getArray(R.array.glass_status)[1];
            case OutlookConstants.GLASS_BROKEN:
                return GlobalData.resourceHelper.getArray(R.array.glass_status)[2];
            case OutlookConstants.GLASS_CHANGE:
                return GlobalData.resourceHelper.getArray(R.array.glass_status)[3];
        }
        return null;
    }


    /**
     * 综合检测标签
     *
     * @param status
     * @return
     */
    public static String getCompreStatus(int status) {
        switch (status) {
            case CompreConstants.COMPRE_OK:
                return GlobalData.resourceHelper.getArray(R.array.compre_status)[0];
            case CompreConstants.COMPRE_NO:
                return GlobalData.resourceHelper.getArray(R.array.compre_status)[1];
        }
        return null;
    }

    /**
     * 获取启动项
     *
     * @param status
     * @return
     */
    public static String getStartupStatus(int status) {
        switch (status) {
            case StartupConstants.STARTUP_NORMAL:
                return GlobalData.resourceHelper.getArray(R.array.startup_status)[0];
            case StartupConstants.STARTUP_ABNORMAL:
                return GlobalData.resourceHelper.getArray(R.array.startup_status)[1];
        }
        return null;
    }

    /**
     * 获取内饰状态
     *
     * @param status
     * @return
     */
    public static String getTrimStatus(int status) {
        switch (status) {
            case TrimConstants.NORMAL:
                return GlobalData.resourceHelper.getArray(R.array.trim_status)[0];
            case TrimConstants.STAIN:
                return GlobalData.resourceHelper.getArray(R.array.trim_status)[1];
            case TrimConstants.AGEING:
                return GlobalData.resourceHelper.getArray(R.array.trim_status)[2];
            case TrimConstants.DAMAGED:
                return GlobalData.resourceHelper.getArray(R.array.trim_status)[3];
        }
        return null;
    }

    /**
     * 获取其他状态
     */
    public static String getOtherStatus(int status) {
        switch (status) {
            case TrimConstants.OTHER_NORMAL:
                return GlobalData.resourceHelper.getArray(R.array.other_status)[0];
            case TrimConstants.OTHER_EXCEPTION:
                return GlobalData.resourceHelper.getArray(R.array.other_status)[1];
        }
        return null;
    }

    public static String getEquipmentStatus(int status) {
        StringBuilder sb = new StringBuilder();
        switch (status) {
            case EquipmentConstants.EQUIPMENT_NORMAL:
                sb.append("正常");
                break;
            case EquipmentConstants.EQUIPMENT_ABNORMAL:
                sb.append("异常");
                break;
        }
        return sb.toString();
    }

    /**
     * 获取刹车片数值
     *
     * @param tire
     * @return
     */
    public static String getTireData(TireData tire) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(tire.getLf())).append("|")
                .append(String.valueOf(tire.getLr())).append("|")
                .append(String.valueOf(tire.getRf())).append("|")
                .append(String.valueOf(tire.getRr()));
        return sb.toString();
    }

    public static String getViewTaskStatus(int status) {
        return GlobalData.resourceHelper.getArray(R.array.view_vehicle_status)[status];
    }


    public static String getSmellStatus(int status) {
        switch (status) {
            case 0:
                return "正常";
            case 1:
                return "异常";
        }
        return null;
    }


    /**
     * 获取备胎状态
     *
     * @param status
     * @return
     */
    public static String getTireStatus(int status) {
        if (status >= 0 && status <= 3) {
            return GlobalData.resourceHelper.getArray(R.array.tire_status)[status];
        }

        return null;
    }
}
