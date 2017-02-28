package com.haoche51.checker.net;

/**
 * Created by xuhaibo on 15/8/25.
 */
public class HttpConstants {
    public static final int GET_LIST_REFRESH = 5;//刷新
    public static final int GET_LIST_LOADMORE = 6;//加载更多

    /**
     * 登录action 带加密
     */
    public static final String ACTION_LOGIN_ENCODE = "login";
    /**
     * 修改密码action
     */
    public static final String ACTION_CHANGE_PWD = "change_passwd";
    /**
     * 修改车检任务预约时间action
     */
    public static final String ACTION_CHANGE_CHECKTIME = "change_checktime";
    /**
     * 绑定百度推送
     */
    public static final String ACTION_BIND_BAIDU_PUSH = "bind_baidu_push";
    /**
     * 取消绑定action
     */
    public static final String ACTION_UNBIND_BAIDUPUSH = "unbind_baidu_push";
    /**
     * 上传报告
     */
    public static final String ACTION_UPLOAD_REPORT = "upload_check_report";

    /**
     * 取消验车任务
     */
    public static final String ACTION_CANCEL_CHECKTASK = "cancel_check_task";
    /**
     * 新增买家线索
     */
    public static final String ACTION_ADD_BUYERLEAD = "add_buyerlead";
    /**
     * 获取买家推荐列表
     */
    public static final String ACTION_GET_BUYERLEAD_LIST = "new_get_buyerlead_list";
    /**
     * 上传评估师地理位置信息
     */
    public static final String ACTION_UPLOAD_CHECKER_POSITION = "update_checker_position";

    /**
     * 完成帮检任务接口
     */
    public static final String ACTION_COMPLETE_HELPCHECK = "help_check_complete";
    /**
     * 获取个人分享数据
     */
    public static final String ACTION_GET_SELF_SHARE_STAT = "get_self_share_stat";
    /**
     * 获取分享总排行
     */
    public static final String ACTION_GET_SHARE_STAT = "get_share_stat";
    /**
     * 获取七牛token
     */
    public static final String ACTION_GET_QINIU_TOKEN = "get_qiniu_token";


    /*获取消息列表信息(new)*/
    public static final String ACTION_GET_MESSAGE = "get_messages";
    /*获取验车任务列表(new)支持根据状态查询*/
    public static final String ACTION_GET_CHECKTASK_LIST_NEW = "get_checktask_list_new";
    /*获取单个验车任务(new)*/
    public static final String ACTION_GET_CHECKTASK = "get_checktask";

    /**
     * 获取订阅条件/getSubscribeRule
     */
    public static final String ACTION_GET_SUBSCRIBE_RULE = "get_sub_rules";
    /**
     * 设置订阅条件/setSubscribeRule
     */
    public static final String ACTION_SET_SUBSCRIBE_RULE = "set_sub_rules";


    /**
     * 设置客户备注　//　TODO(后期废除)
     */
    public static final String ACTION_SET_BUYER_COMMENT = "set_buyer_comment";


    /**
     * 根据电话和登陆id 获取通话记录
     */
    public static final String ACTION_GET_PHONE_RECORD_LIST = "get_phone_record_list";

    /**
     * 根据VIN 码获取车鉴定报告
     */
    public static final String ACTION_GET_CJD_REPORT = "get_cjd_report";

    /**
     * 在车检任务中增加，修改评估师备注
     */
    public static final String ACTION_ADD_VEHICLECHECK_COMMENT = "add_vehiclecheck_comment";
    /**
     * 获取立洋车型列表信息
     */
    public static final String ACTION_GET_LIYANG_MODEL_LIST = "get_liyang_model_list";

    /**
     * 验车任务转单
     */
    public static final String ACTION_CHANGE_CHECKER = "change_checker";

    /**
     * 获取本地评估师列表
     */
    public static final String ACTION_GET_CHECKERLIST = "get_checker_list";

    /**
     * 获取登陆用户的权限
     */
    public static final String ACTION_GET_USER_RIGHT = "get_userright";

    /**
     * 开始验车
     */
    public static final String ACTION_START_CHECK_TASK = "start_checktask";

    /**
     * 验车完成 待上传
     */
    public static final String ACTION_COMPLETE_CHECK_REPORT = "complete_check_report";


    /**
     * 获取收车任务列表
     */
    public static final String ACTION_GET_BACK_TASK_LIST = "get_back_task_list";

    /**
     * 获取收车任务详情
     */
    public static final String ACTION_GET_BACK_TASK_DETAIL = "get_back_task_detail";

    /**
     * 收车任务转单
     */
    public static final String ACTION_CHANGE_BACK_WORKER = "change_back_worker";


    /**
     * 任务失败提交接口
     */
    public static final String ACTION_FAIL_TASK = "back_task_fail";

    /**
     * 获取同城市正常工作的地收列表
     */
    public static final String ACTION_GET_BACK_SAME_CITY_WORKER = "get_back_same_city_worker";


    /**
     * 预约上门提交接口
     */
    public static final String ACTION_APPOINT_TASK = "appoint_back_task";

    /**
     * 获取准备任务列表
     */
    public static final String ACTION_GET_BACK_REPAIR_LIST = "get_back_repair_list";
    /**
     * 修改整备获取原来整备信息的接口
     */
    public static final String ACTION_GET_BACK_REPAIR = "get_back_repair";
    /**
     * 无需整备
     */
    public static final String ACTION_BACK_NO_NEED_REPAIR = "back_no_need_repair";
    /**
     * 提交整备
     */
    public static final String ACTION_ADD_BACK_REPAIR = "add_back_repair";
    /**
     * 获取线下售出车源列表
     */
    public static final String ACTION_GET_BACK_STOCK_LIST = "get_back_stock_list";
    /**
     * 获取线下售出车源详情
     */
    public static final String ACTION_BACKSELLAPI_GET_DETAIL = "backsellapi_get_detail";

    /**
     * 根据车系获取年份信息
     */
    public static final String ACTION_GET_YEAR_BY_CLASS = "vehicleapi_get_year_by_class";

    /**
     * 根据年份获取车型信息
     */
    public static final String ACTION_GET_TYPE_BY_YEAR = "vehicleapi_get_vehicle_by_year";

    /**
     * 发起复检
     */
    public static final String ACTION_START_RECHECK = "launch_back_check";

    /**
     * 获取当前用户对应的所有品牌
     */
    public static final String ACTION_ALL_BRANDS = "vehicleapi_get_brand";
    /**
     * 根据品牌获取车系
     */
    public static final String ACTION_GET_ALL_CLASS_BY_BRAND = "vehicleapi_get_class_by_brand";
    /**
     * 收车成功
     */
    public static final String ACTION_COMMIT_PURCHASE_SUCCESS = "back_task_success";
    /**
     * 提交线下售出
     */
    public static final String ACTION_COMMIT_OFFLINE_SOLD = "backsellapi_sub_deal_order";
    /**
     * 线下售出确认转账
     */
    public static final String ACTION_CONFIRM_OFFLINE_SOLD = "backsellapi_confirm_trans";
    /**
     * 完成整备
     */
    public static final String ACTION_COMPLETE_HOSTLING = "back_repair_over";
    /**
     * 区分收车相关推送类型
     */
    public static final String ACTION_BACK_PUSH_DETAIL = "back_push_detail";

    /**
     * 获取渠寄车源
     */
    public static final String ACTION_QUJI_GET_VEHICLE_SOURCE = "quji_get_vehicle_source";

    /**
     * 获取验车悬浮窗数据
     */
    public static final String ACTION_GET_CHECK_POPUP_DATA = "checker_get_stat";

    /**
     * 评估师申请临时任务
     */
    public static final String ACTION_APPLY_TEMP_CHECK_TASK = "apply_temp_check_task";

    /**
     * 修改验车任务
     */
    public static final String ACTION_UPDATE_CHECK_TASK = "update_check_task";

    /**
     * 获取商家详情
     */
    public static final String ACTION_GET_MERCHANT_DETAIL = "dealerapi_getdetail";

    /**
     * 获取车源详情
     */
    public static final String ACTION_GET_VEHICLE_SOURCE_DETAIL = "dealcarapi_getdetail";

    /**
     * 获取同城渠寄人员
     */
    public static final String ACTION_GET_SAME_CITY_CHANNEL_USER = "dealerapi_getsamecityuser";

    /**
     * 变更维护人
     */
    public static final String ACTION_UPDATE_MAINTAINER = "dealerapi_changeuser";

    /**
     * 维护商家
     */
    public static final String ACTION_MAINTAIN_MERCHANT = "dealerapi_maintain";

    /**
     * 获取渠寄商家列表
     */
    public static final String ACTION_DEALERAPI_GETLIST = "dealerapi_getlist";
    /**
     * 获取渠寄车源列表
     */
    public static final String ACTION_DEALCARAPI_GETLIST = "dealcarapi_getlist";

    /**
     * 添加渠寄商家
     */
    public static final String ACTION_DEALERAPI_ADD = "dealerapi_add";

    /**
     * 添加渠寄车源
     */
    public static final String ACTION_DEALCARAPI_ADD = "dealcarapi_add";

    /**
     * 调整报价和底价
     */
    public static final String ACTION_CHANGE_PRICE = "dealcarapi_changeprice";

    /**
     * 下线车源
     */
    public static final String ACTION_OFFLINE_CAR = "dealcarapi_offlinecar";

    /**
     * 确认在售
     */
    public static final String ACTION_CONFIRM_SELL = "dealcarapi_confirmsell";

    /**
     * 获取商家车源列表
     */
    public static final String ACTION_DEALCARAPI_DEALCARLIST = "dealcarapi_dealcarlist";

    /**
     * 收车——上架车源
     */
    public static final String ACTION_BACKTASKAPI_OLDREPORTONLINE = "backtaskapi_oldreportonline";


    /**
     * 车源推荐——添加车源推荐
     */
    public static final String ACTION_ADD_VEHICLE_RECOM = "crmcheckerapi_addvehiclelead";

    /**
     * 车源推荐——获取车源推荐列表
     */
    public static final String ACTION_GET_VEHICLE_RECOM_LIST = "crmcheckerapi_getmyleadlist";

    /**
     * 车源推荐——获取车源推荐奖金
     */
//    public static final String ACTION_GET_VEHICLE_RECOM_BONUS = "crmcheckerapi_getmysubsidy";

    /**
     * 收车——复活任务
     */
    public static final String ACTION_BACKTASK_BACKLIFE = "backtaskapi_backlife";

    /**
     * 毁约退款
     */
    public static final String ACTION_BREAK_APPOINT = "backtaskapi_breakappoint";

    /**
     * 库存盘点获取车源信息
     */
    public static final String ACTION_BACKREPAIRAPI_GETCHECKSTOCK = "backrepairapi_getcheckstock";

    /**
     * 库存盘点刷新时间
     */
    public static final String ACTION_BACKREPAIRAPI_CHECKSTOCK = "backrepairapi_checkstock";

    /**
     * 收车——根据车源id查询车源标题
     */
    public static final String ACTION_GET_SOURCETITLE = "backtaskapi_getsourcetitle";
    /**
     * 收车——根据vin码查询保养记录
     */
    public static final String ACTION_BACKTASKAPI_GETCHEJIANDINGREPORT = "backtaskapi_getchejiandingreport";

    /**
     * 根据车型id获取车型信息
     */
    public static final String ACTION_GET_MODEL_BY_ID = "vehicleapi_getmodelbyid";


    /**
     * C2C售出提交
     */
    public static final String ACTION_C2C_SOLD = "backsellapi_c2csold";


    /**
     * 添加评估师任务
     */
    public static final String ACTION_ADD_CHECK_TASK_FROM_APP = "crmcheckerapi_addchecktaskfromapp";

    /**
     * 查询保养记录
     */
    public static final String ACTION_SEARCH_MAINTAINRECORD = "crmcheckerapi_searchmaintainrecord";

    /**
     * 获取同城市正常工作的地销列表
     */
    public static final String ACTION_GET_SAME_CITY_SALER = "backtaskapi_getsamecitysaler";

    /**
     * 根据车源编号查询详情页链接
     */
    public static final String ACTION_GET_DETAILSURL = "crmcheckerapi_getdetailsurl";


    /**
     * 申请付款
     */
    public static final String ACTION_APPLY_PAY = "backtaskapi_applypay";

    /**
     * 付款记录列表
     */
    public static final String ACTION_GET_APPLY_RECORDS_LIST = "backtaskapi_payhistory";

    /**
     * 获取库存车源关注度
     */
    public static final String ACTION_GET_STOCK_ATTENTION = "backrepairapi_careinfo";

    /**
     * 申请调价
     */
    public static final String ACTION_APPLY_CHANGE_PRICE = "backrepairapi_applychangeprice";

    /**
     * 添加回访记录
     */
    public static final String ACTION_ADD_REVISIT_RECORD = "backtaskapi_addvisit";

    /**
     * 获取回访记录列表
     */
    public static final String ACTION_GET_REVISIT_LIST = "backtaskapi_getvisitlist";

    /**
     * 获取回购审批列表
     */
    public static final String ACTION_GET_AUDIT_LIST = "backtaskapi_getauditlist";

    /**
     * 获取交易审批列表
     */
    public static final String ACTION_GET_DEAL_LIST = "transactionapi_getauditlist";

    /**
     * 获取城市列表
     */
    public static final String ACTION_GET_CITY_LIST = "backtaskapi_getcitylist";

    /**
     * 报价参考
     */
    public static final String ACTION_OFFER_REFFER = "backtaskapi_searchprice";

    /**
     * 获取新车报价列表
     */
    public static final String ACTION_NEW_CAR_LIST = "backtaskapi_getnewcarlist";

    /**
     * 获取二手车报价列表
     */
    public static final String ACTION_SECOND_HAND_LIST = "backtaskapi_getsecondhandlist";


    /**
     * 校验app版本是否为最新
     */
    public static final String ACTION_CHECK_VERSION = "crmcommonapi_validversion";

    /**
     * 获取在线城市
     */
    public static final String ACTION_GET_ONLINE_CITY = "crmcommondata_getonlinecity";

    /**
     * 获某个人添加的收车任务列表
     */
    public static final String ACTION_GET_ONES_BACK_TASK = "backtaskapi_getonestask";

    /**
     * 添加回购线索
     */
    public static final String ACTION_ADD_BACK_TASK = "add_back_task";

    /**
     * 获取买家推荐列表
     */
    public static final String ACTION_GET_BUYERLEAD_LIST2 = "get_buyerlead_list";

    /**
     * 校验VIN码
     */
    public static final String ACTION_VALID_VIN = "dealcarapi_validvin";

    /**
     * 获取找车需求列表
     */
    public static final String ACTION_GET_FIND_CAR_LIST = "dealrequestapi_getlist";

    /**
     * 无法匹配
     */
    public static final String ACTION_UN_MATCH = "dealrequestapi_unmatch";

    /**
     * 创建带看
     */
    public static final String ACTION_MAKE_APPOINTMENT = "dealrequestapi_makeappointment";

    /**
     * 获取CRM url
     */
    public static final String ACTION_GET_CRM_URL = "get_crm_url";


    /**
     * 获取售出渠道列表
     */
    public static final String ACTION_GET_SOLD_CHANNEL = "backsellapi_getsoldcanal";

    /**
     * 获取整备审批列表
     */
    public static final String ACTION_GET_HOSTING_APPROVE_LIST = "backrepairapi_getrepairauditlist";
}
