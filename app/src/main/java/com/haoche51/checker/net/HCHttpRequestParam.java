package com.haoche51.checker.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.haoche51.checker.Checker;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CheckCarClueEntity;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.entity.CheckTaskEntity;
import com.haoche51.checker.entity.HCBDloactionEntity;
import com.haoche51.checker.entity.HostlingTaskEntity;
import com.haoche51.checker.entity.LocalCheckerEntity;
import com.haoche51.checker.entity.MerchantDetailEntity;
import com.haoche51.checker.entity.OfferReferEntity;
import com.haoche51.checker.entity.OfflineSoldEntity;
import com.haoche51.checker.entity.PaymentRecordEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.entity.PurchaseClueEntity;
import com.haoche51.checker.entity.PurchaseSuccessEntity;
import com.haoche51.checker.entity.PurchaseTaskEntity;
import com.haoche51.checker.entity.PurchaseTaskShortEntity;
import com.haoche51.checker.entity.ReCheckTaskEntity;
import com.haoche51.checker.entity.RevisitRecordEntity;
import com.haoche51.checker.entity.StockAttentionEntity;
import com.haoche51.checker.entity.TempTaskEntity;
import com.haoche51.checker.entity.VehicleSeriesEntity;
import com.haoche51.checker.entity.VehicleSourceEntity;
import com.haoche51.checker.entity.VehicleSubscribeRuleEntity;
import com.haoche51.checker.entity.transaction.ChannelVehicleSourceEntity;
import com.haoche51.checker.util.DESUtil;
import com.haoche51.checker.util.DeviceInfoUtil;
import com.haoche51.checker.util.HCBeanUtil;
import com.haoche51.checker.util.HCLogUtil;
import com.haoche51.checker.util.NetInfoUtil;
import com.haoche51.checker.util.UnixTimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuhaibo on 15/8/25.
 */
public class HCHttpRequestParam {
    public static final String TAG = "HCHttpRequestParam";
    // TODO
    public static final String TOKEN = "haoche51@572_checker_app_server";

    /**
     * login
     *
     * @param username
     * @param password
     * @return
     */
    public static Map<String, Object> loginForEncode(String username, String password
            , String device_id, String device_name, String os_v, String app_v) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("pwd", password);
        params.put("device_id", device_id);
        params.put("device_name", device_name);
        params.put("os_v", os_v);
        params.put("app_v", app_v);
        return getRequest(HttpConstants.ACTION_LOGIN_ENCODE, params);
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    public static Map<String, Object> changePwd(String oldPwd, String newPwd) {
        Map<String, Object> params = new HashMap<String, Object>();
        int checkerId = GlobalData.userDataHelper.getChecker().getId();
        params.put("employee_id", checkerId);
        params.put("old_passwd", oldPwd);
        params.put("new_passwd", newPwd);
        return getRequest(HttpConstants.ACTION_CHANGE_PWD, params);
    }

    /**
     * 绑定百度推送
     */
    public static Map<String, Object> bind(String userId, String channelId) {
        int checker_id = GlobalData.userDataHelper.getChecker().getId();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", checker_id);
        params.put("bd_user_id", userId);
        params.put("bd_channel_id", channelId);
        return getRequest(HttpConstants.ACTION_BIND_BAIDU_PUSH, params);
    }

    /**
     * 取消百度推送绑定
     *
     * @return
     */
    public static Map<String, Object> unbind() {
        int pushId = GlobalData.userDataHelper.getPushId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("push_id", pushId);
        return getRequest(HttpConstants.ACTION_UNBIND_BAIDUPUSH, params);
    }

    /**
     * 修改验车时间
     *
     * @param taskId
     * @param checkTime
     * @param reason
     * @return
     */
    public static Map<String, Object> changeCheckTime(int taskId, int checkTime, String reason) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", taskId);
        params.put("time", checkTime);
        params.put("checker_reappoint_reason", reason);
        return getRequest(HttpConstants.ACTION_CHANGE_CHECKTIME, params);
    }

    /**
     * 上传报告
     *
     * @param checkReport
     * @return
     */
    public static Map<String, Object> uploadCheckReport(CheckReportEntity checkReport) {
        Map<String, Object> params = HCBeanUtil.convertBeanToMap(checkReport);
        return getRequest(HttpConstants.ACTION_UPLOAD_REPORT, params);
    }

    /**
     * 取消验车任务
     *
     * @param taskId
     * @param reason
     * @param onSite
     * @param urlList
     * @param remark
     * @return
     */
    public static Map<String, Object> cancelCheckTask(int taskId, String reason, int onSite, List<String> urlList, String remark) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", taskId);
        params.put("reason", reason);
        params.put("onsite", onSite);
        params.put("accident_image", urlList);
        params.put("accident_comment", remark);
        return getRequest(HttpConstants.ACTION_CANCEL_CHECKTASK, params);
    }


    /**
     * 获取线索列表信息
     *
     * @param page
     * @param page_size
     * @return
     */
    public static Map<String, Object> getBuyerleadList(int page, int page_size) {
        Checker checker = GlobalData.userDataHelper.getChecker();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", checker.getId());
        params.put("page", page);
        params.put("page_size", page_size);
        return getRequest(HttpConstants.ACTION_GET_BUYERLEAD_LIST2, params);
    }

    /**
     * 上传百度定位
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> getBaiduUploadParams(HCBDloactionEntity entity) {
        Map<String, Object> params = new HashMap<String, Object>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        params.put("check_user_id", checker.getId());
        params.put("check_user_name", checker.getName());
        params.put("longitude", entity.getLongitude());
        params.put("latitude", entity.getLatitude());
        long time = System.currentTimeMillis() / 1000;
        params.put("create_time", time);
        return getRequest(HttpConstants.ACTION_UPLOAD_CHECKER_POSITION, params);
    }


    /**
     * 帮检完成接口
     *  
     */
    public static Map<String, Object> helpCheckComplete(CheckTaskEntity entity, int commission, String seller_name, String seller_phone, String buyer_name, String buyer_phone, int transfer_time, String transfer_place, int price, int prepay) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", entity.getId());
        params.put("check_user_id", entity.getCheck_user_id());
        params.put("check_user_name", entity.getCheck_user_name());
        params.put("type", entity.getHelp_check_status());
        params.put("commission", commission);
        params.put("seller_name", seller_name);
        params.put("seller_phone", seller_phone);
        params.put("transfer_time", transfer_time);
        params.put("transfer_place", transfer_place);
        params.put("price", price);
        params.put("prepay", prepay);
        return getRequest(HttpConstants.ACTION_COMPLETE_HELPCHECK, params);
    }


    /**
     * 获取个人每月分享统计接口
     */
    public static Map<String, Object> getSelfShareStat(int check_user_id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("check_user_id", check_user_id);
        return getRequest(HttpConstants.ACTION_GET_SELF_SHARE_STAT, params);
    }

    /**
     * 获取分享总量
     */
    public static Map<String, Object> getShareStat() {
        Map<String, Object> params = new HashMap<String, Object>();
        return getRequest(HttpConstants.ACTION_GET_SHARE_STAT, params);
    }

    /**
     * 获取七牛upload token
     *
     * @param uploadType 上传类型：1、图片 2、音视频
     */
    public static Map<String, Object> getUploadToken(int uploadType) {
        int checker_id = GlobalData.userDataHelper.getChecker().getId();
        Map<String, Object> params = new HashMap<>();
        params.put("check_user_id", checker_id);
        if (uploadType == PictureConstants.UPLOAD_TYPE_PHOTO) {
            params.put("scope", "haoche51");
        } else {
            params.put("scope", "haoche51video");
        }
        return getRequest(HttpConstants.ACTION_GET_QINIU_TOKEN, params);
    }

    /**
     * 添加买家线索
     *
     * @return Map<String, Object>
     */
    public static Map<String, Object> addBuyerClues(String phone, String content) {
        Checker checker = GlobalData.userDataHelper.getChecker();
        Map<String, Object> params = new HashMap<>();
        params.put("check_user_id", checker.getId());
        params.put("check_user_name", checker.getName());
        params.put("buyer_phone", phone);
        params.put("content", content);
        return getRequest(HttpConstants.ACTION_ADD_BUYERLEAD, params);
    }

    /**
     * 获取消息列表信息
     */
    public static Map<String, Object> getMessageList(int page, int pageSize) {
        int checker_id = GlobalData.userDataHelper.getChecker().getId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("check_user_id", checker_id);
        params.put("page", page);
        params.put("page_size", pageSize);
        return getRequest(HttpConstants.ACTION_GET_MESSAGE, params);
    }


    /**
     * 分页获取车检任务列表（new）,可根据任务状态查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    public static Map<String, Object> getCheckTaskListNew(int page, int pageSize, int request_check_status, String order) {
        int checker_id = GlobalData.userDataHelper.getChecker().getId();
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> where = new HashMap<String, Object>();
        where.put("check_user_id", checker_id);
        //1待处理(包含待检测、检测中、待上传) 2已完成(包含成功检测、取消)
        where.put("request_check_status", request_check_status);
        params.put("where", where);
        params.put("page", page);
        params.put("limit", pageSize);
        if (!TextUtils.isEmpty(order))
            params.put("order", order);
        return getRequest(HttpConstants.ACTION_GET_CHECKTASK_LIST_NEW, params);
    }

    /**
     * 根据id 获取车检任务(new)
     *
     * @param taskId
     * @return
     */
    public static Map<String, Object> getCheckTask(int taskId) {
        int checker_id = GlobalData.userDataHelper.getChecker().getId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("check_user_id", checker_id);
        params.put("id", taskId);
        return getRequest(HttpConstants.ACTION_GET_CHECKTASK, params);
    }


    /**
     * 根据买家电话获取买家订阅车源的条件
     *
     * @param buyerPhone
     * @return
     */
    public static Map<String, Object> getSubscribeRule(String buyerPhone) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("buyer_phone", buyerPhone);
        int id = GlobalData.userDataHelper.getChecker().getId();
        String name = GlobalData.userDataHelper.getChecker().getName();
        Map<String, Object> customServiceInfo = new HashMap<String, Object>();
        customServiceInfo.put("id", id);
        customServiceInfo.put("name", name);
        params.put("custom_service_info", customServiceInfo);
        return getRequest(HttpConstants.ACTION_GET_SUBSCRIBE_RULE, params);
    }


    public static Map<String, Object> setSubscribeRule(String buyerPhone, VehicleSubscribeRuleEntity.SubscribeRuleEntity subscribeRuleEntity, String comment, int level) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("buyer_phone", buyerPhone);
        int id = GlobalData.userDataHelper.getChecker().getId();
        String name = GlobalData.userDataHelper.getChecker().getName();
        Map<String, Object> customServiceInfo = new HashMap<String, Object>();
        customServiceInfo.put("id", id);
        customServiceInfo.put("name", name);
        params.put("custom_service_info", customServiceInfo);
        Map<String, Object> subscribeRule = new HashMap<String, Object>();
        List<Integer> listSeries = new ArrayList<>();
        for (VehicleSeriesEntity seriesEntity : subscribeRuleEntity.getSubscribe_series()) {
            listSeries.add(seriesEntity.getId());
        }
        subscribeRule.put("subscribe_series", listSeries);
        subscribeRule.put("emission", subscribeRuleEntity.getEmission());
        subscribeRule.put("gearbox", subscribeRuleEntity.getGearbox());
        if (subscribeRuleEntity.getPrice() != null) {
            subscribeRule.put("price", subscribeRuleEntity.getPrice());
        }
        if (subscribeRuleEntity.getEmission_value() != null) {
            subscribeRule.put("emission_value", subscribeRuleEntity.getEmission_value());
        }
        if (subscribeRuleEntity.getVehicle_color_type() != null && subscribeRuleEntity.getVehicle_color_type().size() > 0) {
            subscribeRule.put("vehicle_color_type", subscribeRuleEntity.getVehicle_color_type());
        } else {
            List<Integer> list = new ArrayList<>();
            list.add(-1);
            subscribeRule.put("vehicle_color_type", list);
        }
        if (subscribeRuleEntity.getVehicle_structure() != null && subscribeRuleEntity.getVehicle_structure().size() > 0) {
            subscribeRule.put("vehicle_structure", subscribeRuleEntity.getVehicle_structure());
        } else {
            List<Integer> list = new ArrayList<>();
            list.add(-1);
            subscribeRule.put("vehicle_structure", list);
        }
        subscribeRule.put("year", subscribeRuleEntity.getYear());
        params.put("subscribe_rule", subscribeRule);
        params.put("comment", comment);
        if (level > 0) {
            params.put("level", level);
        }
        return getRequest(HttpConstants.ACTION_SET_SUBSCRIBE_RULE, params);
    }

    /**
     * 设置备注
     */
    public static Map<String, Object> setBuyerComment(String phone, String comment, int level) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("buyer_phone", phone);
        params.put("comment", comment);
        params.put("level", level);
        Map<String, Object> custom_service_info = new HashMap<>();
        custom_service_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        custom_service_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("custom_service_info", custom_service_info);
        return getRequest(HttpConstants.ACTION_SET_BUYER_COMMENT, params);
    }


    /**
     * 根据电话和登陆id 获取通话记录
     */
    public static Map<String, Object> getPhoneRecordList(int pageIndex, int pageSize, String phone, String order) {
        Map<String, Object> params = new HashMap<>();
        params.put("check_user_id", GlobalData.userDataHelper.getChecker().getId());
        params.put("phone", phone);
        params.put("page_size", pageSize);
        params.put("page", pageIndex);
        if (TextUtils.isEmpty(order))
            order = "create_time desc";
        params.put("order", order);
        return getRequest(HttpConstants.ACTION_GET_PHONE_RECORD_LIST, params);
    }

    /**
     * 根据VIN 码获取车鉴定报告
     */
    public static Map<String, Object> getCjdReport(long vehicle_check_id, String vin_code) {
        Map<String, Object> params = new HashMap<>();
        params.put("check_user_id", GlobalData.userDataHelper.getChecker().getId());
        params.put("vehicle_check_id", vehicle_check_id);
        params.put("vin_code", vin_code);
        Checker checker = GlobalData.userDataHelper.getChecker();
        params.put("username", checker.getUsername());
        params.put("pwd", GlobalData.userDataHelper.getLoginPwd());
        return getRequest(HttpConstants.ACTION_GET_CJD_REPORT, params);
    }

    /**
     * 根据VIN 码和发动机编号获取车鉴定报告
     */
    public static Map<String, Object> getCjdReport(long vehicle_check_id, String vin_code, String engine_code) {
        Map<String, Object> params = new HashMap<>();
        params.put("check_user_id", GlobalData.userDataHelper.getChecker().getId());
        params.put("vehicle_check_id", vehicle_check_id);
        params.put("vin_code", vin_code);
        params.put("engine", engine_code);
        Checker checker = GlobalData.userDataHelper.getChecker();
        params.put("username", checker.getUsername());
        params.put("pwd", GlobalData.userDataHelper.getLoginPwd());
        return getRequest(HttpConstants.ACTION_GET_CJD_REPORT, params);
    }

    /**
     * 在车检任务中增加，修改评估师备注
     */
    public static Map<String, Object> addVehiclecheckComment(long taskId, String comment) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        params.put("checker_comment", comment);
        return getRequest(HttpConstants.ACTION_ADD_VEHICLECHECK_COMMENT, params);
    }

    /**
     * 根据VIN 码获取立洋车型列表信息
     */
    public static Map<String, Object> getLiYangModelList(String vinCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("vin_code", vinCode);
        params.put("crm_user_id", GlobalData.userDataHelper.getChecker().getId());
        return getRequest(HttpConstants.ACTION_GET_LIYANG_MODEL_LIST, params);
    }

    /**
     * 获取评估师列表
     * 'city_id'    => 获取指定城市评估师  默认0
     * 'flag_all'  => false | true 获取列表是否包含自己 默认false
     */
    public static Map<String, Object> getCheckerList(int city_id, boolean flag_all) {
        Map<String, Object> params = new HashMap<>();
        params.put("check_user_id", GlobalData.userDataHelper.getChecker().getId());
        params.put("city_id", city_id);
        params.put("flag_all", flag_all);
        return getRequest(HttpConstants.ACTION_GET_CHECKERLIST, params);
    }

    /**
     * 验车任务转单
     *
     * @param vehicle_check_id      =>  INT, //验车任务id
     * @param new_checker_user_id   => INT, //接受转单的新评估师的crm_user_id
     * @param new_checker_user_name => String, //接受转单的新评估师姓名
     */
    public static Map<String, Object> changeChecker(long vehicle_check_id, long new_checker_user_id, String new_checker_user_name) {
        Map<String, Object> params = new HashMap<>();
        params.put("vehicle_check_id", vehicle_check_id);
        params.put("check_user_id", GlobalData.userDataHelper.getChecker().getId());//发起转单的人id
        params.put("check_user_name", GlobalData.userDataHelper.getChecker().getName());//发起转单的人姓名
        params.put("new_check_user_id", new_checker_user_id);
        params.put("new_check_user_name", new_checker_user_name);
        return getRequest(HttpConstants.ACTION_CHANGE_CHECKER, params);
    }


    /**
     * 获取登录用户权限
     *
     * @return
     */
    public static Map<String, Object> getUserRight() {
        Map<String, Object> params = new HashMap<>();
        params.put("employee_id", GlobalData.userDataHelper.getChecker().getId());//user ID
        return getRequest(HttpConstants.ACTION_GET_USER_RIGHT, params);
    }

    /**
     * 开始验车 new
     *
     * @return
     */
    public static Map<String, Object> startCheckTask(int taskId) {
        Map<String, Object> params = new HashMap<>();
        params.put("check_user_id", GlobalData.userDataHelper.getChecker().getId());//user ID
        params.put("id", taskId);
        return getRequest(HttpConstants.ACTION_START_CHECK_TASK, params);
    }

    /**
     * 验车完成，待上传
     *
     * @return
     */
    public static Map<String, Object> completeCheckReport(int taskId) {
        Map<String, Object> params = new HashMap<>();
        params.put("check_user_id", GlobalData.userDataHelper.getChecker().getId());//user ID
        params.put("id", taskId);
        return getRequest(HttpConstants.ACTION_COMPLETE_CHECK_REPORT, params);
    }


    /**
     * 获取收车任务列表
     * 'type' => int, //要获取的列表类型（1:待跟进、2：进行中、3：已完成）
     * 'limit' => int, //获取多少条。不传默认 10
     * 'page' => int, //获取第几页。默认为 0
     * 'user_info' => array( //地收信息
     * 'id' => int, //crm_user_id
     * 'name' => string, //crm_user_name
     * )
     */
    public static Map<String, Object> getBackTaskList(int page, int type
            , int limit, String search) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("limit", limit);
        params.put("page", page);
        params.put("search", search);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_BACK_TASK_LIST, params);
    }


    /**
     * 获取收车任务详情
     * 'task_id' => int, //收车任务的id
     * 'user_info' => array(
     * 'id' => 1, //crm用户id
     * 'name' => '李四', //crm用户姓名
     * )
     */
    public static Map<String, Object> getBackTaskDetail(int task_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", task_id);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_BACK_TASK_DETAIL, params);
    }


    /**
     * 收车详情填写备注
     * 'task_id' => int, //收车任务id
     * 'old_crm_user_id' => int, //转单之前跟进该任务的地收的crm用户id
     * 'old_crm_user_name' => string, //转单之前跟进该任务的地收的crm用户姓名
     * 'new_crm_user_id' => int, //新的跟进该任务的地收的crm用户id
     * 'new_crm_user_name' => string, //新的跟进该任务的地收的crm用户姓名
     * 'user_info' => array( //地收信息
     * 'id' => int, //crm_user_id
     * 'name' => string, //crm_user_name
     * )
     */
    public static Map<String, Object> changeBackWorker(int task_id, int old_crm_user_id, String old_crm_user_name, int new_crm_user_id, String new_crm_user_name) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", task_id);
        params.put("old_crm_user_id", old_crm_user_id);
        params.put("old_crm_user_name", old_crm_user_name);
        params.put("new_crm_user_id", new_crm_user_id);
        params.put("new_crm_user_name", new_crm_user_name);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_CHANGE_BACK_WORKER, params);
    }


    /**
     * 验车处添加收车线索
     *
     * @return
     * @author wfx
     */
    public static Map<String, Object> addCheckerPurchaseClue(PurchaseClueEntity purchaseClue) {
        Map<String, Object> params = new HashMap<>();
        if (purchaseClue == null) {
            return params;
        }
        params.put("remark", purchaseClue.getRemark());
        params.put("check_id", purchaseClue.getCheck_id());
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_ADD_BACK_TASK, params);
    }



    /**
     * 添加收车线索
     *
     * @return
     * @author wfx
     */
    public static Map<String, Object> addPurchaseClue(PurchaseClueEntity purchaseClue) {
        Map<String, Object> params = new HashMap<>();
        if (purchaseClue == null) {
            return params;
        }
        params.put("seller_name", purchaseClue.getSeller_name());
        params.put("seller_phone", purchaseClue.getSeller_phone());
        params.put("brand_id", purchaseClue.getBrand_id());
        params.put("brand_name", purchaseClue.getBrand_name());
        params.put("class_id", purchaseClue.getClass_id());
        params.put("class_name", purchaseClue.getClass_name());
        params.put("remark", purchaseClue.getRemark());
//        params.put("check_id", purchaseClue.getCheck_id());
        params.put("vehicle_source_id", purchaseClue.getVehicle_source_id());
        params.put("city_id", purchaseClue.getCity_id());
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", purchaseClue.getId());
        userInfo.put("name", purchaseClue.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_ADD_BACK_TASK, params);
    }


    /**
     * 添加收车失败原因
     *
     * @author wfx
     */
    public static Map<String, Object> addPurchaseFailed(PurchaseTaskEntity purchaseTask) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", purchaseTask.getTask_id());
        params.put("reason", purchaseTask.getFail_reason());
        params.put("hope_price", purchaseTask.getHope_price());
        params.put("peer_price", purchaseTask.getPeer_price());
        params.put("our_price", purchaseTask.getOur_price());
        params.put("sell_cycle", purchaseTask.getSell_cycle());
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_FAIL_TASK, params);
    }


    /**
     * 添加预约上门记录
     *
     * @author wfx
     */
    public static Map<String, Object> addOrderToDoor(PurchaseTaskShortEntity purchaseTaskShort) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", purchaseTaskShort.getTask_id());
        params.put("appoint_time", UnixTimeUtil.getUnixTime(purchaseTaskShort.getAppoint_time()));
        params.put("appoint_address", purchaseTaskShort.getAppoint_address());
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_APPOINT_TASK, params);
    }


    /**
     * 获取同城市正常工作的地收列表
     * 'user_info' => array( //地收信息
     * 'id' => int, //crm_user_id
     * 'name' => string, //crm_user_name
     * ),
     */
    public static Map<String, Object> getBackSameCityWorker() {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_BACK_SAME_CITY_WORKER, params);
    }


    /**
     * 获取整备列表
     * 'type'  = int, //获取某个列表(1:待整备、2：整备中、3：已完成)
     * 'limit' => int, //获取多少条。不传默认 10
     * 'page' => int, //获取第几页。默认为 0
     * 'user_info' => array( //地收信息
     * 'id' => int, //crm_user_id
     * 'name' => string, //crm_user_name
     * ),
     */
    public static Map<String, Object> getBackRepairList(int page, int type
            , int limit,String search) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("limit", limit);
        params.put("page", page);
        params.put("search", search);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_BACK_REPAIR_LIST, params);
    }

    /**
     * 获取整备详情
     * 'repair_id'  = int, //获取某个列表(1:待整备、2：整备中、3：已完成)
     * 'user_info' => array( //地收信息
     * 'id' => int, //crm_user_id
     * 'name' => string, //crm_user_name
     * )
     */
    public static Map<String, Object> getBackRepair(int stock_id, int task_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("stock_id", stock_id);
        params.put("task_id", task_id);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_BACK_REPAIR, params);
    }


    /**
     * 获取线下售出车源列表
     * 'type' => 'int', //获取列表的类型。（1：库存中、2：出售中、3：已售出)
     * 'limit' => int, //获取多少条。不传默认 10
     * 'page' => int, //获取第几页。默认为 0
     * 'user_info' => array( //地收信息
     * 'id' => int, //crm_user_id
     * 'name' => string, //crm_user_name
     * ),
     */
    public static Map<String, Object> getBackStockList(int page, int type
            , int limit, String search) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("limit", limit);
        params.put("page", page);
        params.put("search", search);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_BACK_STOCK_LIST, params);
    }

    /**
     * 无需整备
     * 'stock_id' => int, //库存id
     * 'user_info' => array( //地收信息
     * 'id' => int, //crm_user_id
     * 'name' => string, //crm_user_name
     * ),
     */
    public static Map<String, Object> backNoNeedRepair(int stock_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("stock_id", stock_id);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_BACK_NO_NEED_REPAIR, params);
    }

    /**
     * 提交整备
     * 'stock_id'  = int, //库存车辆的库存id
     * 'project' => array( //整备项目
     * array(
     * 'pre_images' => array(xxxx), //整备前的照片
     * 'name' => string, //整备项名称
     * 'expect_free' => int, //预计费用
     * ),
     * array(
     * 'pre_images' => array(xxxx), //整备前的照片
     * 'name' => string, //整备项名称
     * 'expect_free' => int, //预计费用
     * ),
     * 'user_info' => array( //地收信息
     * 'id' => int, //crm_user_id
     * 'name' => string, //crm_user_name
     * ),
     * )
     */
    public static Map<String, Object> addBackRepair(int stock_id, int is_edit, int repair_pick_up_time, List<HostlingTaskEntity.ProjectEntity> project) {
        Map<String, Object> params = new HashMap<>();
        params.put("stock_id", stock_id);
        params.put("is_edit", is_edit);
        params.put("repair_pick_up_time", repair_pick_up_time);
        params.put("project", project);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_ADD_BACK_REPAIR, params);
    }

    /**
     * 获取线下售出车源详情
     */
    public static Map<String, Object> getBacksellapiDetail(int stock_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("stock_id", stock_id);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_BACKSELLAPI_GET_DETAIL, params);
    }

    /**
     * 根据车系获取年份信息
     *
     * @param classId 车系id
     * @author wfx
     */
    public static Map<String, Object> getYearByClass(int classId) {
        Map<String, Object> params = new HashMap<>();
        params.put("class_id", classId);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_GET_YEAR_BY_CLASS, params);
    }

    /**
     * 根据年份获取车型信息
     *
     * @param classId 车系id
     * @param year    年份
     * @author wfx
     */
    public static Map<String, Object> getTypeByYear(int classId, String year) {
        Map<String, Object> params = new HashMap<>();
        params.put("class_id", classId);
        params.put("year", year);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_GET_TYPE_BY_YEAR, params);
    }

    /**
     * 获取当前用户下对应的所有品牌
     *
     * @author wfx
     */
    public static Map<String, Object> getAllBrands() {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_ALL_BRANDS, params);
    }


    /**
     * 根据品牌获取车系
     *
     * @param brandId 品牌id
     * @author wfx
     */
    public static Map<String, Object> getClassByBrand(int brandId) {
        Map<String, Object> params = new HashMap<>();
        params.put("brand_id", brandId);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_GET_ALL_CLASS_BY_BRAND, params);
    }

    /**
     * 发起复检
     *
     * @author wfx
     */
    public static Map<String, Object> startReCheck(ReCheckTaskEntity reCheckTask) {
        Map<String, Object> params = new HashMap<>();
        params.put("appoint_time", reCheckTask.getRecheck_time());
        params.put("appoint_address", reCheckTask.getRecheck_place());
        params.put("check_user_id", reCheckTask.getRecheck_user_id());
        params.put("check_user_name", reCheckTask.getRecheck_user_name());
        params.put("seller_name", reCheckTask.getSeller_name());
        params.put("seller_phone", reCheckTask.getSeller_phone());
        params.put("task_id", reCheckTask.getTask_id());
        VehicleSourceEntity vehicleSource = reCheckTask.getVehicleSource();
        if (vehicleSource != null) {
            params.put("brand_id", vehicleSource.getBrand_id());
            params.put("brand_name", vehicleSource.getBrand_name());
            params.put("class_id", vehicleSource.getSeries_id());
            params.put("class_name", vehicleSource.getSeries_name());
            params.put("vehicle_id", vehicleSource.getModel_id());
            params.put("vehicle_name", vehicleSource.getModel_name());
            params.put("year", vehicleSource.getYear());
        }

        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_START_RECHECK, params);
    }

    /**
     * 提交收车成功信息
     *
     * @param purchaseSuccess 收车成功实体
     * @author wfx
     */
    public static Map<String, Object> commitPurchaseSuccess(PurchaseSuccessEntity purchaseSuccess) {
        Map<String, Object> params = new HashMap<>();
        params.put("vin_code", purchaseSuccess.getVin());
        params.put("task_id", purchaseSuccess.getTaskId());
        VehicleSourceEntity vehicleSource = purchaseSuccess.getVehicleSource();
        if (vehicleSource != null) {
            params.put("brand_id", vehicleSource.getBrand_id());
            params.put("brand_name", vehicleSource.getBrand_name());
            params.put("class_id", vehicleSource.getSeries_id());
            params.put("class_name", vehicleSource.getSeries_name());
            params.put("vehicle_id", vehicleSource.getModel_id());
            params.put("vehicle_name", vehicleSource.getModel_name());
            params.put("year", vehicleSource.getYear());
        }


        params.put("is_surrender", purchaseSuccess.getHasBxsy());
        params.put("surrender_time", purchaseSuccess.getBxdqTime());
        params.put("plate_time", purchaseSuccess.getRegistTime());
        params.put("mile", purchaseSuccess.getShowMile());
        params.put("transfer_count", purchaseSuccess.getTransferTimes());
        params.put("back_price", purchaseSuccess.getPurchasePrice());
        params.put("sell_price", purchaseSuccess.getOfferPrice());
//		params.put("cheap_price", purchaseSuccess.getLowPrice());
        params.put("transfer_free", purchaseSuccess.getTransferFee());
        params.put("introduce_free", purchaseSuccess.getReferralFee());
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        Map<String, Object> images = new HashMap<>();
        List<String> otherPhoto=new ArrayList<>();
        List<PhotoEntity> photoEntities = purchaseSuccess.getPhotoList();
        if(photoEntities!=null && photoEntities.size()>0){
            images.put("left_pre", photoEntities.get(6).getUrl());
            images.put("right_pre", photoEntities.get(7).getUrl());
            images.put("inside_img", photoEntities.get(8).getUrl());
            images.put("card_img", photoEntities.get(0).getUrl());
            images.put("license", photoEntities.get(1).getUrl());
            images.put("mark_img", photoEntities.get(2).getUrl());
            images.put("bank_img", photoEntities.get(3).getUrl());
            images.put("contract", photoEntities.get(4).getUrl());
            images.put("sign_img", photoEntities.get(5).getUrl());
            images.put("ahead_img", photoEntities.get(9).getUrl());//正前方图片
            images.put("side_img", photoEntities.get(10).getUrl());//正侧面图片
            if(photoEntities.size()>11){//如果超过11张
                PhotoEntity tmpPhoto;
                for(int i=11;i<photoEntities.size();i++){
                    tmpPhoto=photoEntities.get(i);
                    if(!TextUtils.isEmpty(tmpPhoto.getUrl())){
                        otherPhoto.add(tmpPhoto.getUrl());
                    }
                }
            }

        }
        params.put("images", images);
        params.put("other_images", otherPhoto);

        params.put("back_remark", purchaseSuccess.getRemark());
        return getRequest(HttpConstants.ACTION_COMMIT_PURCHASE_SUCCESS, params);
    }

    /**
     * 线下售出提交
     *
     * @author wfx
     */
    public static Map<String, Object> commitOfflineSold(OfflineSoldEntity offlineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("stock_id", offlineEntity.getStock_id());
        params.put("buyer_name", offlineEntity.getBuyer_name());
        params.put("buyer_phone", offlineEntity.getBuyer_phone());
        params.put("sold_price", offlineEntity.getSold_price());
        params.put("is_transfer", offlineEntity.getIs_transfer());
        params.put("transfer_free_payer", offlineEntity.getTransfer_free_payer());
        params.put("transfer_free", offlineEntity.getTransfer_free());
        params.put("sold_remark", offlineEntity.getSold_remark());
        params.put("canal", offlineEntity.getSale_channel());

        Map<String, Object> image = new HashMap<>();
        List<PhotoEntity> photoList = offlineEntity.getPhotoList();
        image.put("card_img", photoList.get(0).getUrl());
        image.put("contract", photoList.get(1).getUrl());
        image.put("combine", photoList.get(2).getUrl());
        params.put("image", image);

        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_COMMIT_OFFLINE_SOLD, params);
    }

    /**
     * 线下售出确认转账
     *
     * @author wfx
     */
    public static Map<String, Object> confirmOfflineSold(OfflineSoldEntity offlineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("stock_id", offlineEntity.getStock_id());
        params.put("trans_way", offlineEntity.getTrans_way());
        params.put("trans_image", offlineEntity.getPhotoPathList());

        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_CONFIRM_OFFLINE_SOLD, params);
    }


    /**
     * 完成整备
     *
     * @author wfx
     */
    public static Map<String, Object> completeHostling(HostlingTaskEntity hostlingTaskEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("stock_id", hostlingTaskEntity.getStock_id());
        params.put("project", hostlingTaskEntity.getProject());

        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_COMPLETE_HOSTLING, params);
    }

    /**
     * 区分收车相关的推送
     */
    public static Map<String, Object> getBackPushDetail(int task_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", task_id);

        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_BACK_PUSH_DETAIL, params);
    }

    /**
     * 获取渠寄车源列表
     */
    public static Map<String, Object> getQujiVehicleSource() {
        Map<String, Object> params = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        params.put("crm_user_id", checker.getId());
        return getRequest(HttpConstants.ACTION_QUJI_GET_VEHICLE_SOURCE, params);
    }


    /**
     * 获取车辆成交回购数据
     *
     * @author wfx
     */
    public static Map<String, Object> getCheckPopupData() {
        Map<String, Object> params = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        params.put("check_user_id", checker.getId());

        return getRequest(HttpConstants.ACTION_GET_CHECK_POPUP_DATA, params);
    }

    /**
     * 评估师申请临时任务
     *
     * @author wfx
     */
    public static Map<String, Object> applyTempCheckTask(TempTaskEntity tempTaskEntity) {
        Map<String, Object> params = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        params.put("check_user_id", checker.getId());
        params.put("check_user_name", checker.getName());
        params.put("min_time", tempTaskEntity.getIdleStartTime());
        params.put("max_time", tempTaskEntity.getIdleEndTime());
        params.put("place", tempTaskEntity.getLocation());
        params.put("place_lat", tempTaskEntity.getLatitude());
        params.put("place_lng", tempTaskEntity.getLongitude());
        return getRequest(HttpConstants.ACTION_APPLY_TEMP_CHECK_TASK, params);
    }

    /**
     * 修改验车任务
     *
     * @author pxl
     */
    public static Map<String, Object> updateCheckTask(int taskId, String appointment_place, double place_lat, double place_lng) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        Map<String, Object> fields = new HashMap<>();
        fields.put("appointment_place", appointment_place);
        fields.put("place_lat", place_lat);
        fields.put("place_lng", place_lng);
        params.put("fields", fields);
        return getRequest(HttpConstants.ACTION_UPDATE_CHECK_TASK, params);
    }


    /**
     * 获取商家详情信息
     *
     * @param merchantId 商家id
     * @author wfx
     */
    public static Map<String, Object> getMerchantDetail(int merchantId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", merchantId);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_GET_MERCHANT_DETAIL, params);
    }


    /**
     * 获取车源详情信息
     *
     * @param vehicleId 车源id
     * @author wfx
     */
    public static Map<String, Object> getVehicleDetail(int vehicleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", vehicleId);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_GET_VEHICLE_SOURCE_DETAIL, params);
    }

    /**
     * 获取同城渠寄人员列表
     *
     * @author wfx
     */
    public static Map<String, Object> getSameCityChannelUser() {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_GET_SAME_CITY_CHANNEL_USER, params);
    }

    /**
     * 变更商家维护人
     *
     * @param merchantDetail 商家详情
     * @author wfx
     */
    public static Map<String, Object> updateMaintainer(MerchantDetailEntity merchantDetail, LocalCheckerEntity chooseChecker) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", merchantDetail.getId());
        params.put("old_user_id", merchantDetail.getCrm_user_id());
        params.put("old_user_name", merchantDetail.getCrm_user_name());
        params.put("new_user_id", chooseChecker.getId());
        params.put("new_user_name", chooseChecker.getName());
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_UPDATE_MAINTAINER, params);
    }


    /**
     * 获取同城渠寄人员列表
     *
     * @param merchantId 商家id
     * @author wfx
     */
    public static Map<String, Object> maintainMerchant(int merchantId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", merchantId);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_MAINTAIN_MERCHANT, params);
    }


    /**
     * 获取渠寄商家列表
     * 'keyword' => string, //搜索关键字、不匹配手机号、或电话。就按照 商家名称来模糊查询
     * 'crm_user_id' => int, //非必传、维护人id筛选。需要筛选的时候上传本参数
     * 'limit' => int, //一页多少条， 不传默认为 10
     * 'page' => int, //不传默认为 0
     * 'user_info' => array( //操作人crm信息
     * 'id' => 118, //crm_user_id
     * 'name' => '姓名', //crm_user_name
     * )
     */
    public static Map<String, Object> getBusinessList(int page, int limit, String keyword, int crm_user_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("crm_user_id", crm_user_id);
        params.put("limit", limit);
        params.put("page", page);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_DEALERAPI_GETLIST, params);
    }

    /**
     * 获取渠寄车源列表
     * 'limit' => int, //获取多少条。不传 默认10条
     * 'page' => int, //第几页。不传 默认0（表示第一页)
     * 'keyword' => string/int, //目前支持按照编号搜索。例如：HC12345、hc12345、12345
     * 'crm_user_id' => int, //非必传、维护人id筛选
     * 'brand_id' => int, //非必传、品牌筛选
     * 'class_id' => int, //非必传、车系筛选
     * 'user_info' => array( //操作人的crm信息
     * 'id' => 118, //crm_user_id
     * 'name' => '姓名', //crm_user_name
     * )
     */
    public static Map<String, Object> getVehicleSourceList(int page, int limit, String keyword, int crm_user_id, int brand_id, int class_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("page", page);
        params.put("keyword", keyword);
        params.put("crm_user_id", crm_user_id);
        params.put("brand_id", brand_id);
        params.put("class_id", class_id);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_DEALCARAPI_GETLIST, params);
    }


    /**
     * 获取渠寄商家车源列表
     * 'dealer_id' => int, //商家id
     * 'limit' => int, //获取多少条。不传默认 10
     * 'page' => int, //获取第几页。默认为 0
     * 'user_info' => array(
     * 'id' => int, //crm_user_id
     * 'name' => string, //crm_user_name
     * )
     */
    public static Map<String, Object> getMerchantVehicleSourceList(int dealer_id, int page, int limit, String keyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("dealer_id", dealer_id);
        params.put("limit", limit);
        params.put("page", page);
        params.put("keyword", keyword);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_DEALCARAPI_DEALCARLIST, params);
    }

    /**
     * 渠寄添加商家
     * 'id' => int, //商家id（如果是编辑商家信息就上传、否则不需要）
     * 'name'  => string, //商家名称
     * 'address' => string, //商家地址
     * 'contact_name' => string, //商家联系人姓名
     * 'contact_phone' => 18559947120, //商家联系人电话
     * 'user_info' => array( //添加商家的人的crm信息
     * 'id' => 118, //crm_user_id
     * 'name' => '姓名', //crm_user_name
     * ),
     */
    public static Map<String, Object> addDealer(int id, String name, String address, String contact_name, String contact_phone) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("address", address);
        params.put("contact_name", contact_name);
        params.put("contact_phone", contact_phone);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_DEALERAPI_ADD, params);
    }

    /**
     * 渠寄添加商家
     * 'dealer_id' => int, //商家id
     * 'brand_id' => int, //品牌id
     * 'brand_name'  => string, //品牌名称
     * 'class_id' => int, //车系id
     * 'class_name' => string, //车系名称
     * 'vehicle_id' => int, //车型id
     * 'vehicle_name' => string, //车型名称
     * 'year' => 2014, //年份
     * 'seller_name' => string, //车主姓名
     * 'seller_phone' => 18559947120, //车主联系电话
     * 'appoint_time' => int, //预约验车时间戳
     * 'appoint_place' => string, //预约验车地点
     * 'place_lat' => 1234.432, //地点纬度,
     * 'place_lng' => 3445.982, //地点经度,
     * 'user_info' => array( //操作人的crm信息
     * 'id' => 118, //crm_user_id
     * 'name' => '姓名', //crm_user_name
     * ),
     * ),
     */
//    public static Map<String, Object> addVehicleSource(int dealer_id, int brand_id, String brand_name, int class_id,
//                                                       String class_name, int vehicle_id, String vehicle_name, String year, String seller_name, String seller_phone,
//                                                       String appoint_time, String appoint_place, double place_lat, double place_lng) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("dealer_id", dealer_id);
//        params.put("brand_id", brand_id);
//        params.put("brand_name", brand_name);
//        params.put("class_id", class_id);
//        params.put("class_name", class_name);
//        params.put("vehicle_id", vehicle_id);
//        params.put("vehicle_name", vehicle_name);
//        params.put("year", year);
//        params.put("seller_name", seller_name);
//        params.put("seller_phone", seller_phone);
//        params.put("appoint_time", UnixTimeUtil.getModifyUnixTime(appoint_time));
//        params.put("appoint_place", appoint_place);
//        params.put("place_lat", place_lat);
//        params.put("place_lng", place_lng);
//        Map<String, Object> user_info = new HashMap<>();
//        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
//        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
//        params.put("user_info", user_info);
//        return getRequest(HttpConstants.ACTION_DEALCARAPI_ADD, params);
//    }

    /**
     * 渠寄添加车源
     *
     * @param vehicleSourceEntity
     * @return
     */
    public static Map<String, Object> addVehicleSource(ChannelVehicleSourceEntity vehicleSourceEntity) {
        Map<String, Object> params = new HashMap<>();
        if (vehicleSourceEntity == null) {
            return params;
        }
        if (!TextUtils.isEmpty(vehicleSourceEntity.getVin_code())) {
            params.put("vin_code", vehicleSourceEntity.getVin_code());
        }

        params.put("dealer_id", vehicleSourceEntity.getDealer_id());
        params.put("brand_id", vehicleSourceEntity.getBrand_id());
        params.put("brand_name", vehicleSourceEntity.getBrand_name());
        params.put("class_id", vehicleSourceEntity.getClass_id());
        params.put("class_name", vehicleSourceEntity.getClass_name());
        params.put("vehicle_id", vehicleSourceEntity.getVehicle_id());
        params.put("vehicle_name", vehicleSourceEntity.getVehicle_name());
        params.put("year", vehicleSourceEntity.getYear());
        params.put("seller_name", vehicleSourceEntity.getSeller_name());
        params.put("seller_phone", vehicleSourceEntity.getSeller_phone());
        params.put("appoint_time", UnixTimeUtil.getModifyUnixTime(vehicleSourceEntity.getAppoint_time()));
        params.put("appoint_place", vehicleSourceEntity.getAppoint_place());
        params.put("place_lat", vehicleSourceEntity.getPlace_lat());
        params.put("place_lng", vehicleSourceEntity.getPlace_lng());
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_DEALCARAPI_ADD, params);
    }

    /**
     * 校验VIN码
     *
     * @param vinCode vin码
     * @author wfx
     */
    public static Map<String, Object> validVIN(String vinCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("vin_code", vinCode);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_VALID_VIN, params);
    }

    /**
     * 获取同城渠寄人员列表
     *
     * @param vehicleSourceId
     * @param offerPrice
     * @param lowPrice
     * @author wfx
     */
    public static Map<String, Object> changePrice(int vehicleSourceId, float offerPrice, float lowPrice) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", vehicleSourceId);
        params.put("sell_price", offerPrice);
        params.put("cheap_price", lowPrice);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_CHANGE_PRICE, params);
    }

    /**
     * 下线车源
     *
     * @param vehicleSourceId 车源id
     * @param reason          原因
     * @author wfx
     */
    public static Map<String, Object> offlineCar(int vehicleSourceId, String reason) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", vehicleSourceId);
        params.put("reason", reason);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_OFFLINE_CAR, params);
    }


    /**
     * 确认在售
     *
     * @param vehicleSourceId 车源id
     * @author wfx
     */
    public static Map<String, Object> confirmSell(int vehicleSourceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", vehicleSourceId);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_CONFIRM_SELL, params);
    }


    /**
     * 使用旧报告立即上线
     *
     * @param purchaseTaskEntity
     * @return
     * @author wfx
     */
    public static Map<String, Object> immediateOnline(PurchaseTaskEntity purchaseTaskEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", purchaseTaskEntity.getTask_id());
        params.put("seller_name", purchaseTaskEntity.getSeller_name());
        params.put("seller_phone", purchaseTaskEntity.getSeller_phone());
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_BACKTASKAPI_OLDREPORTONLINE, params);
    }

    /**
     * 添加验车车源推荐
     *
     * @return
     * @author wfx
     */
    public static Map<String, Object> addVehicleRecom(CheckCarClueEntity purchaseClue, int cityId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", purchaseClue.getSeller_name());
        params.put("phone", purchaseClue.getSeller_phone());
        params.put("brand_id", purchaseClue.getBrand_id());
        params.put("class_id", purchaseClue.getClass_id());
        params.put("user_id", purchaseClue.getId());
        params.put("city_id", cityId);
        return getRequest(HttpConstants.ACTION_ADD_VEHICLE_RECOM, params);
    }


    /**
     * 分页获取车源推荐列表
     *
     * @param page     页码
     * @param pageSize 每页的大小
     * @return
     * @author wfx
     */
    public static Map<String, Object> getVehicleRecomByPage(int page, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("limit", pageSize);
        params.put("user_id", GlobalData.userDataHelper.getChecker().getId());
        return getRequest(HttpConstants.ACTION_GET_VEHICLE_RECOM_LIST, params);
    }

    /**
     * 获取车源推荐奖金
     *
     * @return
     * @author wfx
     */
//    public static Map<String, Object> getVehicleRecomBonus() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("user_id", GlobalData.userDataHelper.getChecker().getId());
//        return getRequest(HttpConstants.ACTION_GET_VEHICLE_RECOM_BONUS, params);
//    }


    /**
     * 收车任务复活任务
     *
     * @return
     */
    public static Map<String, Object> backLifeTask(int task_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", task_id);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_BACKTASK_BACKLIFE, params);
    }

    /**
     * 毁约退款
     *
     * @param purchaseTaskEntity
     * @return
     * @author wfx
     */
    public static Map<String, Object> breakContract(PurchaseTaskEntity purchaseTaskEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", purchaseTaskEntity.getTask_id());
        params.put("need_back_amount", purchaseTaskEntity.getNeed_back_amount());
        params.put("real_back_amount", purchaseTaskEntity.getReal_back_amount());
        params.put("remark", purchaseTaskEntity.getBreak_remark());
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_BREAK_APPOINT, params);
    }

    /**
     * 库存盘点获取车源信息
     */
    public static Map<String, Object> getCheckStock(String vehicle_source_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("vehicle_source_id", vehicle_source_id);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_BACKREPAIRAPI_GETCHECKSTOCK, params);
    }

    /**
     * 库存盘点刷新时间
     */
    public static Map<String, Object> checkStock(String vehicle_source_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("vehicle_source_id", vehicle_source_id);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_BACKREPAIRAPI_CHECKSTOCK, params);
    }


    /**
     * 根据车源id查询车源标题
     *
     * @return
     */
    public static Map<String, Object> getVehicleSourceTitle(int vehicle_source_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("vehicle_source_id", vehicle_source_id);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_GET_SOURCETITLE, params);
    }

    /**
     * 根据vin码查询收车任务的车辆保养记录
     *
     * @return
     */
    public static Map<String, Object> getMaintenanceRecords(int taskId, String vinCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", taskId);
        params.put("vin_code", vinCode);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        params.put("username", checker.getUsername());
        params.put("pwd", GlobalData.userDataHelper.getLoginPwd());
        return getRequest(HttpConstants.ACTION_BACKTASKAPI_GETCHEJIANDINGREPORT, params);
    }


    /**
     * 根据vin码和发动机号查询收车任务的车辆保养记录
     *
     * @return
     */
    public static Map<String, Object> getMaintenanceRecords(int taskId, String vinCode, String engineCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", taskId);
        params.put("vin_code", vinCode);
        params.put("engine", engineCode);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        params.put("username", checker.getUsername());
        params.put("pwd", GlobalData.userDataHelper.getLoginPwd());
        return getRequest(HttpConstants.ACTION_BACKTASKAPI_GETCHEJIANDINGREPORT, params);
    }

    /**
     * 根据车型id获取车源信息
     *
     * @param modelId
     * @return
     * @author wfx
     */
    public static Map<String, Object> getVehicleModelById(int modelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("model_id", modelId);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_GET_MODEL_BY_ID, params);
    }

    /**
     * 添加评估师任务
     * 'name' => '张山',
     * 'phone' => '13800138000',
     * 'brand_id' => '33',
     * 'brand_name' => '奥迪',
     * 'class_id' => 692,
     * 'class_name' => '奥迪A4L',
     * 'year' => 2016,
     * 'model_id' => 23924,
     * 'model_name' => '35 TFSI 自动标准型',
     * 'city_id' => 12,
     * 'place' => '北京市海淀区西二旗-地铁站',
     * 'place_lng' => '116.32',
     * 'place_lat' => '40.0581',
     * 'appointment_time' => '1462723200',
     * 'comment' => '验车备注',
     * 'checker_info' => array (
     * 'id' => 118,
     * 'name' => '测试评估',
     * ),
     * 'user_info' => array (
     * 'id' => 118,
     * 'name' => '测试',
     * ),
     *
     * @author pxl
     */
    public static Map<String, Object> addCheckTask(String name, String phone, VehicleSourceEntity vehicleSourceEntity, int city_id, String place, double place_lng, double place_lat,
                                                   int appointment_time, String comment, LocalCheckerEntity checkerEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("phone", phone);
        params.put("brand_id", vehicleSourceEntity.getBrand_id());
        params.put("brand_name", vehicleSourceEntity.getBrand_name());
        params.put("class_id", vehicleSourceEntity.getSeries_id());
        params.put("class_name", vehicleSourceEntity.getSeries_name());
        params.put("year", vehicleSourceEntity.getYear());
        params.put("model_id", vehicleSourceEntity.getModel_id());
        params.put("model_name", vehicleSourceEntity.getModel_name());
        params.put("city_id", city_id);
        params.put("place", place);
        params.put("place_lng", place_lng);
        params.put("place_lat", place_lat);
        params.put("appointment_time", appointment_time);
        params.put("comment", comment);
        //选择执行任务的评估师
        Map<String, Object> checker_info = new HashMap<>();
        checker_info.put("id", checkerEntity.getId());
        checker_info.put("name", checkerEntity.getName());
        params.put("checker_info", checker_info);
        //添加人信息
        Map<String, Object> user_info = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        user_info.put("id", checker.getId());
        user_info.put("name", checker.getName());
        params.put("user_info", user_info);

        return getRequest(HttpConstants.ACTION_ADD_CHECK_TASK_FROM_APP, params);
    }

    /**
     * 根据车源编号/vin码查询保养记录
     * 'search' => 'LSGGF53W2FH150765',
     * 'user_info' => array(
     * 'id' => 员工ID
     * 'name' => 员工姓名
     * )
     *
     * @author pxl
     */
    public static Map<String, Object> searchReport(String search) {
        Map<String, Object> params = new HashMap<>();
        params.put("search", search);
        //添加人信息
        Map<String, Object> user_info = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        user_info.put("id", checker.getId());
        user_info.put("name", checker.getName());
        user_info.put("username", checker.getUsername());
        user_info.put("pwd", GlobalData.userDataHelper.getLoginPwd());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_SEARCH_MAINTAINRECORD, params);
    }

    /**
     * 根据车源编号查询验车报告
     * 'vehicle_source_id' => '123456',
     * 'username' => 'test',
     * 'pwd' => 'test',
     * 'user_info' => array(
     * 'id' => 员工ID
     * 'name' => 员工姓名
     * )
     * );
     *
     * @author pxl
     */
    public static Map<String, Object> getDetailsurl(int vehicle_source_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("vehicle_source_id", vehicle_source_id);

        Checker checker = GlobalData.userDataHelper.getChecker();
        params.put("username", checker.getUsername());
        params.put("pwd", GlobalData.userDataHelper.getLoginPwd());

        //添加人信息
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", checker.getId());
        user_info.put("name", checker.getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_DETAILSURL, params);
    }

    /**
     * C2C售出提交
     *
     * @author wfx
     */
    public static Map<String, Object> c2cSold(OfflineSoldEntity offlineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("stock_id", offlineEntity.getStock_id());
        params.put("buyer_name", offlineEntity.getBuyer_name());
        params.put("buyer_phone", offlineEntity.getBuyer_phone());
        params.put("saler_id", offlineEntity.getSaler_id());
        params.put("saler_name", offlineEntity.getSaler_name());

        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);

        return getRequest(HttpConstants.ACTION_C2C_SOLD, params);
    }

    /**
     * 获取同城地销列表
     */
    public static Map<String, Object> getSameCitySalerList() {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_GET_SAME_CITY_SALER, params);
    }

    /**
     * 申请付款
     *
     * @param paymentRecord
     * @return
     * @author wfx
     */
    public static Map<String, Object> applyInfo(PaymentRecordEntity paymentRecord) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", paymentRecord.getTask_id());
        params.put("account_num", paymentRecord.getAccount_num());
        params.put("account_user", paymentRecord.getAccount_user());
        params.put("account_bank", paymentRecord.getAccount_bank());
        params.put("price_type", paymentRecord.getPrice_type());
        params.put("price", paymentRecord.getPrice());
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_APPLY_PAY, params);
    }


    /**
     * 分页获取付款记录列表
     *
     * @param taskId 收车任务id
     * @return
     * @author wfx
     */
    public static Map<String, Object> getPayRecords(int taskId) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", taskId);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_GET_APPLY_RECORDS_LIST, params);
    }

    /**
     * 获取库存车源关注度
     *
     * @param stockId 库存id
     * @return
     * @author wfx
     */
    public static Map<String, Object> getStockAttention(int stockId) {
        Map<String, Object> params = new HashMap<>();
        params.put("stock_id", stockId);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_GET_STOCK_ATTENTION, params);
    }

    /**
     * 申请调价
     *
     * @return
     * @author wfx
     */
    public static Map<String, Object> getApplyToAdjustPrice(StockAttentionEntity stockAttention) {
        Map<String, Object> params = new HashMap<>();
        if (stockAttention == null) {
            return params;
        }
        params.put("stock_id", stockAttention.getStock_id());
        params.put("new_seller_price", stockAttention.getNew_seller_price());
//		params.put("new_cheap_price", stockAttention.getNew_cheap_price());
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_APPLY_CHANGE_PRICE, params);
    }

    /**
     * 添加回访记录
     *
     * @return
     * @author wfx
     */
    public static Map<String, Object> addRevisitRecord(RevisitRecordEntity revisitRecord) {
        Map<String, Object> params = new HashMap<>();
        if (revisitRecord == null) {
            return params;
        }
        params.put("task_id", revisitRecord.getTask_id());
        params.put("next_visit", revisitRecord.getNext_visit());
        params.put("reason", revisitRecord.getReason());
        params.put("sold_time", revisitRecord.getSold_time());
        params.put("decide_user", revisitRecord.getDecide_user());
        params.put("expect_price", revisitRecord.getExpect_price());
        params.put("peer_price", revisitRecord.getPeer_price());
        params.put("other", revisitRecord.getOther());
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_ADD_REVISIT_RECORD, params);
    }

    /**
     * 获取回访记录列表(无需分页，全取)
     *
     * @param taskId 收车任务id
     * @return
     * @author wfx
     */
    public static Map<String, Object> getRevisitRecords(int taskId) {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", taskId);
        Map<String, Object> userInfo = new HashMap<>();
        Checker checker = GlobalData.userDataHelper.getChecker();
        userInfo.put("id", checker.getId());
        userInfo.put("name", checker.getName());
        params.put("user_info", userInfo);
        return getRequest(HttpConstants.ACTION_GET_REVISIT_LIST, params);
    }

    /**
     * 获取回购审批列表
     *
     * @param page  页码
     * @param limit 条数
     * @author wfx
     */
    public static Map<String, Object> getRepurchaseAproveList(int page, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("page", page);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);

        Map<String, Object> login_info = new HashMap<>();
        login_info.put("username", GlobalData.userDataHelper.getChecker().getUsername());
        login_info.put("pwd", GlobalData.userDataHelper.getLoginPwd());
        params.put("login_info", login_info);
        return getRequest(HttpConstants.ACTION_GET_AUDIT_LIST, params);
    }


    /**
     * 获取整备审批列表
     *
     * @param page  页码
     * @param limit 条数
     * @author wfx
     */
    public static Map<String, Object> getHostlingAproveList(int page, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("page", page);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);

        Map<String, Object> login_info = new HashMap<>();
        login_info.put("username", GlobalData.userDataHelper.getChecker().getUsername());
        login_info.put("pwd", GlobalData.userDataHelper.getLoginPwd());
        params.put("login_info", login_info);
        return getRequest(HttpConstants.ACTION_GET_HOSTING_APPROVE_LIST, params);
    }

    /**
     * 获取交易审批列表
     *
     * @param page  页码
     * @param limit 条数
     * @author wfx
     */
    public static Map<String, Object> getDealAproveList(int page, int limit,String search) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("page", page);
        params.put("where", null);
        params.put("keyword", search);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        params.put("username", GlobalData.userDataHelper.getChecker().getUsername());
        params.put("pwd", GlobalData.userDataHelper.getLoginPwd());
        return getRequest(HttpConstants.ACTION_GET_DEAL_LIST, params);
    }

    /**
     * 获取城市列表
     *
     * @author wfx
     */
    public static Map<String, Object> geCityList() {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_CITY_LIST, params);
    }

    /**
     * 报价参考
     *
     * @author wfx
     */
    public static Map<String, Object> offerReffer(OfferReferEntity offerEntity) {
        Map<String, Object> params = new HashMap<>();
        if (offerEntity != null) {
            params.put("city_id", offerEntity.getCity_id());
            params.put("brand_id", offerEntity.getBrand_id());
            params.put("class_id", offerEntity.getClass_id());
            params.put("model_id", offerEntity.getModel_id());
            params.put("vehicle_year", offerEntity.getVehicle_year());
            params.put("plate_time", offerEntity.getPlate_time());
            params.put("mile", offerEntity.getMile());
        }
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_OFFER_REFFER, params);
    }

    /**
     * 获取新车报价列表
     *
     * @author wfx
     */
    public static Map<String, Object> getNewCarOfferList(OfferReferEntity offerEntity) {
        Map<String, Object> params = new HashMap<>();
        if (offerEntity != null) {
            params.put("brand_id", offerEntity.getBrand_id());
            params.put("class_id", offerEntity.getClass_id());
            params.put("model_id", offerEntity.getModel_id());
            params.put("vehicle_year", offerEntity.getVehicle_year());
        }
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_NEW_CAR_LIST, params);
    }

    /**
     * 获取二手车报价列表
     *
     * @param page  页码
     * @param limit 条数
     * @author wfx
     */
    public static Map<String, Object> getSecondHandCarList(OfferReferEntity offerEntity, int page, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("page", page);
        if (offerEntity != null) {
            params.put("brand_id", offerEntity.getBrand_id());
            params.put("class_id", offerEntity.getClass_id());
            params.put("model_id", offerEntity.getModel_id());
            params.put("vehicle_year", offerEntity.getVehicle_year());
            params.put("mile", offerEntity.getMile());
            params.put("plate_time", offerEntity.getPlate_time());
            params.put("order", offerEntity.getOrder());
            params.put("source", offerEntity.getSource());
        }
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_SECOND_HAND_LIST, params);
    }


    /**
     * 检测当前版本是否为最新版本
     *
     * @author wfx
     */
    public static Map<String, Object> checkVersion(int version) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", TaskConstants.APP_HCB);//1：卖车神器、2：好车帮
        params.put("version", version);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_CHECK_VERSION, params);
    }


    /**
     * 获取在线城市
     *
     * @return
     */
    public static Map<String, Object> getOnlineCity() {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_ONLINE_CITY, params);
    }

    /**
     * 添加买家线索
     *
     * @return Map<String, Object>
     */
    public static Map<String, Object> addBuyerClues(String phone, String content, int cityId) {
        Checker user = GlobalData.userDataHelper.getChecker();
        Map<String, Object> params = new HashMap<>();
        params.put("check_user_id", user.getId());
        params.put("check_user_name", user.getName());
        params.put("buyer_phone", phone);
        params.put("content", content);
        params.put("city_id", cityId);
        return getRequest(HttpConstants.ACTION_ADD_BUYERLEAD, params);
    }

    /**
     * 获某个人添加的收车任务列表
     * limit' => int, //获取多少条。不传默认 10
     * 'page' => int, //获取第几页。默认为 0
     * 'user_info' => array( //操作人信息
     * 'id' => int, //crm_user_id
     * 'name' => string, //crm_user_name
     * )
     */
    public static Map<String, Object> getPurchaseleadList(int page, int page_size) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("limit", page_size);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        params.put("a_v", DeviceInfoUtil.getAppVersion());
        return getRequest(HttpConstants.ACTION_GET_ONES_BACK_TASK, params);
    }

    /**
     * 获取找车需求列表
     *
     * @author wfx
     */
    public static Map<String, Object> getFindCarList(int page, int page_size) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("limit", page_size);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_FIND_CAR_LIST, params);
    }

    /**
     * 无法匹配
     *
     * @author wfx
     */
    public static Map<String, Object> unMatch(int requestId) {
        Map<String, Object> params = new HashMap<>();
        params.put("require_id", requestId);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_UN_MATCH, params);
    }


    /**
     * 创建带看
     *
     * @author wfx
     */
    public static Map<String, Object> makeAppointment(int requestId) {
        Map<String, Object> params = new HashMap<>();
        params.put("require_id", requestId);
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_MAKE_APPOINTMENT, params);
    }

    /**
     * 获取CRM wap页入口
     * @param type 页面类型：

     * @author wfx
     */
    public static Map<String, Object> getCRMUrl(int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("pwd", GlobalData.userDataHelper.getLoginPwd());
        params.put("username", GlobalData.userDataHelper.getLoginName());
        params.put("type", type);
        return getRequest(HttpConstants.ACTION_GET_CRM_URL, params);
    }

    /**
     *  获取售出渠道列表
     * @author wfx
     */
    public static Map<String, Object> getSaleChannelList(){
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> user_info = new HashMap<>();
        user_info.put("id", GlobalData.userDataHelper.getChecker().getId());
        user_info.put("name", GlobalData.userDataHelper.getChecker().getName());
        params.put("user_info", user_info);
        return getRequest(HttpConstants.ACTION_GET_SOLD_CHANNEL, params);
    }

    /**
     * 封装请求参数String
     *
     * @param action
     * @param params
     * @return
     */
    private static Map<String, Object> getRequest(String action, Map<String, Object> params) {
        Map<String, Object> msg = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        msg.put("action", action);
        msg.put("params", params);

        request.put("action", action);
        request.put("token", TOKEN);
        request.put("stat", getRequestStat());
//        if (action != HttpConstants.ACTION_LOGIN_ENCODE) {
        if (!HttpConstants.ACTION_LOGIN_ENCODE.equals(action)) {
            String msgEncode = "";
            try {
                msgEncode = DESUtil.encryptDES(new Gson().toJson(msg), GlobalData.userDataHelper.getChecker().getApp_token());
                HCLogUtil.d(TAG, "msg---msgEncode -->" + msgEncode);
                String msgDecode = DESUtil.decryptDES(msgEncode, GlobalData.userDataHelper.getChecker().getApp_token());
//        HCLogUtil.d(TAG, "msg---msgDecode -->" + msgDecode);
                longLog(msgDecode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            request.put("msg", msgEncode);
            request.put("app_token", GlobalData.userDataHelper.getChecker().getApp_token());
        } else {
            request.put("msg", msg);
        }
        return request;
    }

    public static void longLog(String tempData) {
        try {
            final int len = tempData.length();
            final int div = 2000;
            int count = len / div;
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    HCLogUtil.d(TAG, "msg---msgDecode -->" + tempData.substring(i * div, (i + 1) * div));
                }
                int mode = len % div;
                if (mode > 0) {
                    HCLogUtil.d(TAG, "msg---msgDecode -->" + tempData.substring(div * count, len));
                }
            } else {
                HCLogUtil.d(TAG, tempData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取手机当前状态信息
     *
     * @return
     */
    private static Map<String, Object> getRequestStat() {
        Map<String, Object> stat = new HashMap<>();
        stat.put("a_v", DeviceInfoUtil.getAppVersion());
        stat.put("s_v", DeviceInfoUtil.getOSVersion());
        stat.put("p_t", DeviceInfoUtil.getPhoneType());
        stat.put("n_s", NetInfoUtil.getNetworkType());
        return stat;
    }
}
