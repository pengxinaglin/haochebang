package com.haoche51.checker.constants;

public class EngineConstants {

	public static final String [] ENGINE_CHECK_LIST = { 
		"机油有无冷却液混入",
		"缸盖外有无机油渗漏",
		"散热器格珊有无破损",
		"蓄电池电极柱有无腐蚀",
		"蓄电池电解液有无渗漏",
		"发动机皮带有无老化",
		"油管有无老化裂痕",
		"水管有无老化裂痕",
		"线束有无老化裂痕",
		"机油位是否正常",
		"剎车油位是否正常",
		"转向油位有无异常"
		};
	public static final int ENGINE_NORMAL = 0; //正常
	
	public static final int ENGINE_SLIGHT = 1; //轻微
	
	public static final int ENGINE_SERIOUS = 2;//严重
	
}
