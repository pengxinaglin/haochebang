package com.haoche51.checker.constants;

import com.haoche51.checker.R;

public class TaskConstants {


    //    public static final String SERVICE_CALL = "01059059075";
    public static final String MY_BUYERCLUES_PAGE = "mybuyer_clues";
//    public static final String UPLOADING_TASK_PAGE = "uploading_task";//验车任务上传中页签
//    public static final String ADD_NEW_UPLOADING_CHECKTASK = "add_new_uploading_checktask";//添加新的验车任务

    /**
     * 推送消息中任务类型
     */
    public static final int MESSAGE_CHECK_TASK = 0;//车检任务
    public static final int MESSAGE_BACK_TASK = 4;//收车任务
    public static final int MESSAGE_FINE_NOTIFY = 5;//罚款通知
    public static final int MESSAGE_FIND_CAR = 8;//找车需求
//    public static final int BACKTASK_STATUS_DEPOSIT_FAIL = 1; //收车待跟进
//    public static final int BACKTASK_STATUS_PAY_DEPOSIT = 2; //收车已预约
//    public static final int BACKTASK_STATUS_HAS_DEPOSIT = 3; //收车其他状态
//    public static final int BACKTASK_STATUS_PAY_BALANCE = 4; //整备所有状态
//    public static final int BACKTASK_STATUS_BACK_OVER = 5; //线下售出库存中
//    public static final int BACKTASK_STATUS_SOLD_WAIT_TRANS = 6;//线下售出其他状态

    //当前筛选任务的状态任务进行状态. 0待检测 1取消 2检测中 3待上传 4成功检测 (该状态仅用于接口,未作存储)
    public static final int CHECK_STATUS_PENDING = 0;
    public static final int CHECK_STATUS_CANCEL = 1;
    public static final int CHECK_STATUS_ONGOING = 2;
    public static final int CHECK_STATUS_TOUPLOAD = 3;
    public static final int CHECK_STATUS_SUCCESS = 4;
    //请求服务器筛选条件 1待处理(包含待检测、检测中、待上传) 2已完成(包含成功检测、取消)
    public static final int REQUEST_CHECK_STATUS_PENDING = 1;
    public static final int REQUEST_CHECK_STATUS_FINISH = 2;

    //与服务器同步状态， 0 待检 1 完成
    public static final int CHECK_TASK_PENDING = 0;
    public static final int CHECK_TASK_ACHIEVE = 1;
    //    // 本地缓存状态
//    public static final int CHECK_TASK_READY = 1;
//    public static final int CHECK_TASK_FINISH = 0;
//    //服务器取消字段
//    public static final int CHECK_TASK_CANCEL = 1;
    // source of task
    public static final int CHECK_TASK_MAINSITE = 1; // 主站
    public static final int CHECK_TASK_SPIDER = 2; // 爬虫
    public static final int CHECK_TASK_ASSISTANCE = 3; // 帮检
    public static final int CHECK_TASK_RECHECK = 4; // 收车复检
    public static final int CHECK_TASK_CHANNEL = 5; // 渠寄
    public static final int CHECK_TASK_EXHIBITION = 6; // 展厅

    //取消验车是否上门
    public static final int CHECK_ONSITE_ISNO = 0; // 默认没有上门
    public static final int CHECK_ONSITE_ACCIDENT = 1; // 事故车上门
    public static final int CHECK_ONSITE_OTHER = 2; // 其他上门
    // 帮检任务状态
    public static final int HELP_CHECK_PENDING = 0;  // 待检
    public static final int HELP_CHECK_FAILED = 1; // 取消
    public static final int HELP_CHECK_SUCCESS = 2; // 完成
    public static final int HELP_CHECK_DEAL = 3; // 成交未传报告
    //    public static final int HELP_CHECK_FINISH = 4; // 成交未传报告
    // 帮检任务码
    public static final int HELP_CHECK = 0x999;//帮检结果页


    //默认显示的任务条数
    public static final int DEFAULT_SHOWTASK_COUNT = 10;

    public static final String ACACHE_CHECK_TASK = "check_task";//验车任务
    public static final String ACACHE_CHECK_FINISH_TASK = "check_finish_task";//验车完成任务


    //毁约操作状态 0初始 1待确认 2已确认
//    public static final int CANCEL_TRANS_STATUS_DEFAULT = 0;
    public static final int CANCEL_TRANS_STATUS_NOTCONFIRM = 1;
    public static final int CANCEL_TRANS_STATUS_CONFIRMED = 2;

    // 线索状态 0初始 1处理中 2成功 3无效(提交时判断) 4无效(帮买标记)
    public static final int CLUESSTATUS_DEFAULT = 0;
    public static final int CLUESSTATUS_PROCESSING = 1;
    public static final int CLUESSTATUS_SUCCESS = 2;
    public static final int CLUESSTATUS_SUBMIT_INVALID = 3;
    public static final int CLUESSTATUS_INVALID = 4;


    public static final int PREPAY_SELECT_PHOTO = 0x889;//支付定金选择照片


    /**
     * 任务上传状态
     */
    public static final String UPLOAD_STATUS_STOP = "中断";
    public static final String UPLOAD_STATUS_WAITING = "排队中";
    public static final String UPLOAD_STATUS_PHOTO_UPLOADING = "上传图片中";
    public static final String UPLOAD_STATUS_VIDEO_COMPRESSING = "压缩视频中";
    public static final String UPLOAD_STATUS_VIDEO_UPLOADING = "上传视频中";
    public static final String UPLOAD_STATUS_AUDIO_UPLOADING = "上传音频中";
    public static final String UPLOAD_STATUS_REPORT_UPLOADING = "上传报告中";
    public static final String UPLOAD_STATUS_REPORT_FINISHED = "已完成";

    /**
     * 任务上传失败原因
     */
    public static final String UPLOAD_FAILED_NO_NETWORK = "网络连接没有打开";
    public static final String UPLOAD_FAILED_NETWORK_TIMEOUT = "网络连接超时";
    public static final String UPLOAD_FAILED_FIND_PHOTO = "本地图片丢失";
    public static final String UPLOAD_FAILED_FIND_VIDEO = "本地视频丢失";
    public static final String UPLOAD_FAILED_COMPRESS_VIDEO = "压缩视频失败";
    public static final String UPLOAD_FAILED_FIND_AUDIO = "本地音频丢失";
    public static final String UPLOAD_FAILED_FIND_REPORT = "本地报告丢失";

    /**
     * 任务上传失败操作
     */
    public static final String UPLOAD_FAILED_OPERATE_RECOMPRESS = "请连接SD卡，去待处理界面重新上传";
    public static final String UPLOAD_FAILED_OPERATE_SETTING = "请打开网络，系统会自动进行重试";
    public static final String UPLOAD_FAILED_OPERATE_RETRY = "请点击重试";

    /**
     * 任务帮检状态
     * 1主站 2爬虫 3帮检 4收车复检 5渠道寄售 6展厅
     */
    public static final String CHECK_SOURCE_ASSISTANCE = "帮检";
    public static final String CHECK_SOURCE_RECHECK = "回购";
    public static final String CHECK_SOURCE_CHANNEL = "渠寄";
    public static final String CHECK_SOURCE_NORMAL = "";//默认

    public static final String BINDLE_NEW_TASK = "newtask";//新任务
    public static final String BINDLE_DETAIL_TASK = "detailTask";//任务详情
    public static final String BINDLE_FRAGMENT_INDEX = "index";//验车界面的标签页索引
    public static final String BINDLE_UPLOAD_TASK = "uploadTask";//验车界面的标签页索引
    public static final String BINDLE_BACK_LIFE = "bundle_back_life";//复活任务
    public static final String BINDLE_BREAK_CONTRACT = "bundle_break_contract";//毁约
    public static final String BINDLE_TASK_SUCCESS = "bindle_task_success";//收车成功
    public static final String BINDLE_ORDER_TO_DOOR = "bindle_order_to_door";//预约上门
    public static final String BINDLE_CHANGE_REMARK = "bindle_change_remark";//修改备注
    public static final String BINDLE_TRANSFER_TO_OTHER = "bindle_transfer";//转单
    public static final String BINDLE_TASK_FAILED = "bindle_task_failed";//任务失败
    public static final String BINDLE_ADD_ITEM = "bindle_add_item";//添加任务
    public static final String BINDLE_IS_ASSIGN = "isAssign";//是否来自待分配界面
    public static final String BINDLE_RESORTING_TASK = "resorting_task";
    public static final String BINDLE_COMMIT_HOSTLING = "bindle_commit_hostling";//提交整备
    public static final String BINDLE_NO_HOSTLING = "bindle_no_hostling";//无需整备
    public static final String BINDLE_ADD_REVISIT_RECORD = "bindle_add_revisit_record";//添加任务
    //标识不限制车系
    public static final String BINDLE_UNLIMITED_CLASS = "unlimited_class";
    public static final String INTENT_EXTRA_ID = "id";//验车任务ID
    public static final String INTENT_EXTRA_POSITION = "position";//验车任务在列表中的位置

    /**
     * 上传任务对应的界面
     */
    public static final int FRAGMENT_PENDING_TASK = 0;
    public static final int FRAGMENT_UPLOAD_TASK = 1;
    public static final int FRAGMENT_FINISH_TASK = 2;

    /**
     * 上传对应的网速
     */
    public static final int UPLOAD_NET_SPEED = 2;

    /**
     * 任务列表中每个任务默认具有的上传机会：3（包括2次失败后重试）
     */
    public static final int DEFAULT_UPLOAD_CHANCE = 3;

    /**
     * 通知栏消息提示
     */
    public static final String NOTICE_UPLOAD_TICKER = "好车邦来新消息啦，请注意查收";

    /**
     * 通知栏消息提示
     */
    public static final int NOTICE_NOTIY_ID = 3001;

    public static final int NOTICE_NOTIY_ID_NET = 3002;

    /**
     * 收车相关
     */
    //1:待跟进、2：进行中、3：已完成 4、待分配
    public static final int REQUEST_PURCHASE_DEFAULT = 1;
    public static final int REQUEST_PURCHASE_UNDERWAY = 2;
    public static final int REQUEST_PURCHASE_COMPLETE = 3;
    public static final int REQUEST_PURCHASE_ASSIGNED = 4;

    //crm 任务状态
//    public static final int TASK_STATUS_INVALID = -1;//任务未生效
//    public static final int TASK_STATUS_WAIT_ALLOCATE = 0; //待分配
//    public static final int TASK_STATUS_WAIT_APPOINT = 1; //待处理
//    public static final int TASK_STATUS_HAS_APPOINT = 2; //已预约
//    public static final int TASK_STATUS_WAIT_CHECK = 3; //待城市经理审核
//    public static final int TASK_STATUS_CHECK_FAIL = 4; //城市经理审核拒绝
    public static final int TASK_STATUS_FAIL = 5; //任务失败
    public static final int TASK_STATUS_CHECKED = 6; //已审核
    public static final int TASK_STATUS_HAS_STOCK = 7; //已入库
    //  public static final int TASK_STATUS_HAS_DEPOSIT = 8; //已付定金
    public static final int TASK_STATUS_BREAK_CONTRACT_TO_CONFIRM = 9; //毁约待确认
    public static final int TASK_STATUS_BREAK_CONTRACT_PASS = 10; //毁约通过
    public static final int TASK_STATUS_PURCHASE_COMPLETE = 15; //收购完成

    /**
     * 支付状态
     */
    public static final int STOCK_PAY_STATUS_UNPAY = 0; //未付款
    public static final int STOCK_PAY_STATUS_PART = 5; //部分付款
//    public static final int STOCK_PAY_STATUS_PAYED = 10; //已付款

    public static final String ACACHE_PURCHASE_DEFAULT = "purchase_default";//收车待跟进任务
    public static final String ACACHE_PURCHASE_UNDERWAY = "purchase_underway";//跟进中任务
    public static final String ACACHE_PURCHASE_COMPLETE = "purchase_complete";//已完成任务

    /**
     * 整备相关
     */
    //1:待准备、2：准备中、3：已完成
    public static final int REQUEST_HOSTLING_DEFAULT = 1;
    public static final int REQUEST_HOSTLING_UNDERWAY = 2;
    public static final int REQUEST_HOSTLING_COMPLETE = 3;
    public static final String ACACHE_HOSTLING_DEFAULT = "hostling_default";//待整备任务
    public static final String ACACHE_HOSTLING_UNDERWAY = "hostling_underway";//整备中任务
    public static final String ACACHE_HOSTLING_COMPLETE = "hostling_complete";//已完成任务


    public static final int REPAIR_STATUS_WAIT = 1;//待整备
    public static final int REPAIR_STATUS_NONE = 2;//无需整备
    public static final int REPAIR_STATUS_ING = 3;//整备中
    public static final int REPAIR_STATUS_OVER = 4;//整备完成
    public static final int REPAIR_STATUS_WAIT_FIRST_CHECK = 10;//待初审
    public static final int REPAIR_STATUS_WAIT_NEXT_CHECK = 15;//待复审


    /**
     * 线下售出相关
     */
    //1：库存中、2：出售中、3：已售出
    public static final int REQUEST_OFFLINESOLD_STOCK = 1;
    public static final int REQUEST_OFFLINESOLD_SALE = 2;
    public static final int REQUEST_OFFLINESOLD_COMPLETE = 3;
    public static final String ACACHE_OFFLINESOLD_STOCK = "offlinesold_stock";//库存中
    public static final String ACACHE_OFFLINESOLD_SALE = "offlinesold_sale";//出售中
    public static final String ACACHE_OFFLINESOLD_COMPLETE = "offlinesold_complete";//已完成

    //    public static final int FINANCE_STATUS_DEPOSIT_FAIL = 1; //支付定金审核失败
//    public static final int FINANCE_STATUS_PAY_DEPOSIT = 5; //等待支付定金
//    public static final int FINANCE_STATUS_HAS_DEPOSIT = 10; //已支付定金
//    public static final int FINANCE_STATUS_PAY_BALANCE = 15; //等待支付尾款
//    public static final int FINANCE_STATUS_BACK_OVER = 20; //收购完成
    public static final int FINANCE_STATUS_SOLD_WAIT_TRANS = 25;//售出转账待地收确认
//    public static final int FINANCE_STATUS_SOLD_TRANS_CONFIRM = 30; //售出回款待确认
//    public static final int FINANCE_STATUS_COMPLETE = 35; //售出完成

    /**
     * 渠寄
     */
    public static final String ACACHE_BUSINESS_LIST = "business_list";//渠寄商家列表
    public static final String ACACHE_VEHICLE_SOURCE_LIST = "vehicle_source_list";//渠寄车源列表
    public static final String ACACHE_FIND_CAR = "find_car";//找车需求
    public static final String RESPONSE_VIN_VALID = "0";//VIN码有效，不重复

    /**
     * 获取复检人员请求code
     */
    public static final int REQUEST_GET_RECHECK_PERSON = 101;

    /**
     * 获取品牌车系请求code
     */
    public static final int REQUEST_GET_BRAND_CLASS = 103;


    /**
     * 获取地销人员请求code
     */
    public static final int REQUEST_GET_SELLER_PERSON = 105;

    /**
     * 照片
     */
    public static final int REQUEST_SELECT_PHOTO = 104;

    /**
     * 交易照片
     */
    public static final int REQUEST_SELECT_DEAL_PHOTO = 106;

    /**
     * 发帖照片
     */
    public static final int REQUEST_SELECT_POST_PHOTO = 107;

    /**
     * 发帖照片
     */
    public static final int REQUEST_SELECT_MORE_POST_PHOTO = 108;

    /**
     * 临时存储根目录
     */
    public static final String TEMP_HOME_PATH = "checker";

    /**
     * 待上传图片的临时存储目录
     */
    public static final String TEMP_IMAGES_PATH = "images";

    /**
     * 待上传图片断点的临时存储目录
     */
    public static final String TEMP_RECORD_PATH = "recorder";

    /**
     * 待上传视频的临时存储目录
     */
    public static final String TEMP_VIDEO_PATH = "video";

    /**
     * 待上传音频的临时存储目录
     */
    public static final String TEMP_AUDIO_RECORD_PATH = "audioRecord";

    /**
     * 日志的存储目录
     */
    public static final String TEMP_LOG_PATH = "log";

    /**
     * apk的下载目录
     */
//    public static final String TEMP_DOWNLOAD_PATH = "download";

    /**
     * 是
     */
    public static final int YES = 1;

    /**
     * 买家身份证
     */
    public static final int REQUEST_BUYER_ID_CARD = 10;

    /**
     * 合同
     */
    public static final int REQUEST_CONTRACT = 11;

    /**
     * 三人合影
     */
    public static final int REQUEST_THREE_PEOPLE = 12;

    /**
     * 毁约退款请求
     */
    public static final int REQUEST_BREAK_CONTRACT = 13;

    /**
     * 整备
     */
    public static final String INTENTY_REPAIR = "intent_repair";

    /**
     * 图片url前缀
     */
    public static final String PHOTO_PREFIX = "http://image1.haoche51.com/";

	/*----------------   渠寄相关           -----------------------------*/
    /**
     * 变更维护
     */
    public static final int REQUEST_UPDATE_MERCHANT = 102;

    /**
     * 添加车源推荐
     */
    public static final String BINDLE_ADD_VEHICLE_RECOM = "add_vehicle_recom";

    /**
     * 车源推荐缓存
     */
    public static final String ACACHE_VEHICLE_RECOM = "vehicle_recom";

    /**
     * 车源推荐状态
     * 0待审核 1线索无效 2待处理 3成功返利
     */
    public static final String VEHICLE_STATUS_CHECK_PENDING = "待审核";
    public static final String VEHICLE_STATUS_CLUE_INVALID = "线索无效";
    public static final String VEHICLE_STATUS_TO_OPERATE = "待处理";
    public static final String VEHICLE_STATUS_SUCCESS_RETURN = "成功返利";


    /**
     * 查询验车报告记录
     */
    public static final String ACACHE_MAINTENANCE_RECORDS = "maintenance_records";

    /**
     * 交易照片的标签
     */
    public static final int[] DEAL_PHOTO_TAGS = {R.string.hc_id_card, R.string.hc_driving_liense, R.string.hc_regist_certificate,
            R.string.hc_bank_card, R.string.hc_contract, R.string.hc_signature_photo};


    /**
     * 发帖照片的标签
     */
    public static final int[] POST_PHOTO_TAGS = {R.string.hc_left_front_45, R.string.hc_right_behind_45,R.string.hc_inner_full_shot,R.string.hc_ahead,R.string.hc_ahead_side};


    /**
     * 申请付款
     */
    public static final String ACACHE_APPLY_PAYMENT = "apply_payment";

    /**
     * 回访记录
     */
    public static final String ACACHE_VISIT_RECORD = "visit_record";

    /**
     * 收车任务最后一次付款的申请状态：付款申请中
     */
    public static final int APPLY_PAY_STATUS_APLYING = 1;

    /**
     * 收车任务最后一次付款的申请状态：付款拒绝
     */
    public static final int APPLY_PAY_STATUS_REJECT = 2;


    /**
     * 收车任务最后一次付款的申请状态：财务已付款
     */
    public static final int APPLY_PAY_STATUS_PAYED = 3;

    public static final String KEY_INTENT_EXTRA_RESULT_VEHICLE = "key_intent_extra_result_vehicle";
    public static final int KEY_INTENT_REQUEST_CODE_FILTER_LIYANG = 1;

    public static final int MAX_COMMENT_TEXT_LENGTH = 100;//备注最大长度

    /**
     * 回购审批
     */
    public static final String ACACHE_REPURCHASE_APPROVE = "repurchase_approve";

    /**
     * 交易审批
     */
    public static final String ACACHE_DEAL_APPROVE = "deal_approve";

    /**
     * 整备审批
     */
    public static final String ACACHE_HOSTLING_APPROVE = "hostling_approve";

    /**
     * 是否审批
     */
    public static final String BINDLE_IS_APPROVE = "is_approve";

    /**
     * 市场行情
     */
    public static final String BINDLE_MARKET_CONDITION = "market_condition";

    /**
     * 新车报价
     */
    public static final String ACACHE_NEW_CAR_OFFER = "new_car_offer";

    /**
     * 二手车报价
     */
    public static final String ACACHE_SECOND_HAND_CAR_OFFER = "second_hand_car_offer";

    public static final String LOG_TAG = "OKHttpManager";
    public static final String NORMAL_REQUEST_PARAMS = "req";

    /*---------------自动更新 start ---------------------------*/
    //fins页面的广播
    public static final String ACTION_FINISH_MAIN = "action_finish_main";
    /**
     * 1、关闭对话框
     */
    public static final int OP_DIS_DOWN_DIALOG = 1;

    /**
     * 1、卖车神器
     */
//    public static final int APP_MCSQ = 1;

    /**
     * 2、好车邦
     */
    public static final int APP_HCB = 2;

    /**
     * 1、普通更新
     */
    public static final int UPDATE_TYPE_NORMAL = 1;

    /**
     * 2、紧急更新
     */
//    public static final int UPDATE_TYPE_FORCE = 2;

    /**
     * 下载id
     */
    public static final String KEY_DOWNLOAD_ID = "download_id";

    public static final String SP_UPDATE_TYPE = "update_type";

    public static final String BINDLE_VERSION_NAME = "version_name";

    public static final String BINDLE_UPDATE_CONTENT = "update_content";
    /*---------------自动更新 end ---------------------------*/
    public static final String ACACHE_RECOMMEND_BUYER = "recommend_buyer";//买家推荐
    public static final String ACACHE_RECOMMEND_PURCHASE = "recommend_purchase";//回购推荐
    public static final String ACACHE_RECOMMEND_CHECK = "recommend_check";//验车推荐

    /*--------------------  找车需求 start -------------------*/
    public static final String BINDLE_BANK_CODE = "bank_code";
    public static final String BINDLE_FIND_CAR = "find_car";
    /**
     * 状态文本值：10待匹配  20已匹配   30无法匹配
     */
    public static final int FIND_CAR_STATUS_WAIT_MATCH = 10;//待匹配
    public static final int FIND_CAR_STATUS_MATCHED = 20;//已匹配
    public static final int FIND_CAR_STATUS_NO_MATCH = 30;//无法匹配
    /*--------------------  找车需求 end -------------------*/


    /*--------------- 获取CRM wap页URL start  ------------------------------------*/
    public static final int CRM_URL_CAR_REVISIT = 1;//车源回访
    public static final int CRM_URL_DXSALER_SALARY = 2;//地销工资
    public static final int CRM_URL_CHECKER_SALARY = 3;//评估师工资
    public static final int CRM_URL_PURCHASE_SALARY = 4;//回购工资
    public static final int CRM_URL_PURCHASE_REPORT = 5;//工作简报
    /*--------------- 获取CRM wap页URL end ------------------------------------*/
}