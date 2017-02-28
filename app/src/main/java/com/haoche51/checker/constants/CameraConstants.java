package com.haoche51.checker.constants;

public class CameraConstants {

	public static final String FOLDER_PREFIX = "好车";
	// 拍摄模式
	public static final int CAMERA_STANDARD_MODE = 1;
	public static final int CAMERA_FREE_MODE = 2;
	public static final int CAMERA_SCRATCH_MODE = 3;
	public static final int OUT_INDEX = 12;//外观图索引
	public static final int INNER_INDEX = 30;//内饰图索引

	public final static String[] defaultindicatorText = {
		"尾箱",
		"备胎",
		"引擎盖全景*",//2
		"引擎特写",
		"铭牌*",//4
		"右前45度车头",
		"正车头*",//6
		"底盘全景*",//7
		"左前45度车头*", //8
		"前灯*",//9
		"左侧底大边",
		"正侧*",//11
		"左后45度车尾",
		"正车尾*",//13
		"右后45度车尾*",//14
		"尾灯",
		"车顶漆膜厚度特写*",//TODO new 16
		"右侧底大边",
		"轮胎*",//18
		"轮胎花纹*",//19
		"车钥匙*",//20
		"一键启动*",//21
		"方向盘*",//22
		"仪表盘*",//23
		"中控特写*",//24
		"排挡手柄*",//25
		"座椅调节方式",
		"安全带根部特写",//TODO new
		"天窗*",//28
		"主驾车门控件*",//29
		"左前门*",//30
		"焊点特写",//TODO new
		"左前门驾驶位空间*",//32
		"左后门",
		"左后门后排空间",
		"内饰全景*",//35
		"驾驶位全景",
		"副驾驶位全景",
		"评估师自拍"
	};

	//TODO new
	public final static int[] upload_order = {
		29,
		35,
		30,
		31,
		37,
		2,
		1,
		32,
		0,
		7,
		33,
		3,
		4,
		5,
		6,
		8,
		9,//TODO new 车顶漆膜
		34,
		10,
		11,
		36,
		17,
		15,
		16,
		18,
		19,
		21,
		22,//TODO new 安全带根部特写特写
		20,
		23,
		24,
		25,//TODO new 外框焊点
		26,
		27,
		28,
		12,
		13,
		14,
		38
	};

	public final static int[] PHOTO_ENNUM = { // 按照上传顺序分配枚举 
		222,// '后备箱',
		308,//306 备胎
		301,
		302,
		309,//308 铭牌
		103,
		102,
		305,//底盘全景
		101,
		109,//108,前灯
		306,//304 左侧底大边
		104,
		105,
		106,
		107,
		110,//109, 尾灯
		113,//TODO new 车顶漆膜
		307,//右侧底大边
		111,//轮胎
		112,//轮胎花纹
		310,//车钥匙
		208,//一键启动
		205,//方向盘
		209,//仪表盘
		203,//中控特写
		213,//档把
		215,//座椅调节方式
		223,//TODO new 安全带根部
		216,//天窗
		217,//主车门控件
		218,//'左前门',
		224,//TODO new 外框焊点
		219,//'左前门空间,
		220,//'左后门,
		221,//'左后门空间',,
		201,
		202,
		204,//203,副驾驶位全景
		0//309评估师自拍
	};
}
