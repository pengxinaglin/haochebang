package com.haoche51.checker.DAO;

import android.content.ContentValues;
import android.database.Cursor;

import com.haoche51.checker.entity.BaseEntity;
import com.haoche51.checker.entity.LiYangVehicleEntity;

import java.util.List;

/**
 * Created by yangming on 2015/11/18.
 */
public class LiYangVehicleDAO extends BaseDAO {

  private static LiYangVehicleDAO dao = new LiYangVehicleDAO();
  public static final String TABLE_NAME = "liyang_vehicle_model";
  public static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
      + "model_id text NOT NULL DEFAULT '' ,"//COMMENT 'Level ID'
      + "f1 text NOT NULL DEFAULT '' ,"//COMMENT '厂家'
      + "f2 text NOT NULL DEFAULT '' ,"//COMMENT '品牌'
      + "f3 text NOT NULL DEFAULT '' ,"//COMMENT '车系'
      + "f4 text NOT NULL DEFAULT '' ,"//COMMENT '车型'
      + "f5 text NOT NULL DEFAULT '' ,"//COMMENT '销售名称'
      + "f6 text NOT NULL DEFAULT '' ,"//COMMENT '年款'
      + "f7 text NOT NULL DEFAULT '' ,"//COMMENT '排放标准'
      + "f8 text NOT NULL DEFAULT '' ,"//COMMENT '车辆类型'
      + "f9 text NOT NULL DEFAULT '' ,"//COMMENT '车辆级别'
      + "f10 text NOT NULL DEFAULT '' ,"//COMMENT '指导价格'
      + "f11 text NOT NULL DEFAULT '' ,"//COMMENT '上市年份'
      + "f12 text NOT NULL DEFAULT '' ,"//COMMENT '上市月份'
      + "f13 text NOT NULL DEFAULT '' ,"//COMMENT '生产年份'
      + "f14 text NOT NULL DEFAULT '' ,"//COMMENT '停产年份'
      + "f15 text NOT NULL DEFAULT '' ,"//COMMENT '生产状态'
      + "f16 text NOT NULL DEFAULT '' ,"//COMMENT '国别'
      + "f17 text NOT NULL DEFAULT '' ,"//COMMENT '国产合资进口'
      + "f18 text NOT NULL DEFAULT '' ,"//COMMENT '气缸容积'
      + "f19 text NOT NULL DEFAULT '' ,"//COMMENT '排量(升)'
      + "f20 text NOT NULL DEFAULT '' ,"//COMMENT '进气形式'
      + "f21 text NOT NULL DEFAULT '' ,"//COMMENT '燃油类型'
      + "f22 text NOT NULL DEFAULT '' ,"//COMMENT '燃油标号'
      + "f23 text NOT NULL DEFAULT '' ,"//COMMENT '最大马力(Ps)'
      + "f24 text NOT NULL DEFAULT '' ,"//COMMENT '最大功率(kW)'
      + "f25 text NOT NULL DEFAULT '' ,"//COMMENT '最大功率转速(rpm)'
      + "f26 text NOT NULL DEFAULT '' ,"//COMMENT '最大扭矩(N·m)'
      + "f27 text NOT NULL DEFAULT '' ,"//COMMENT '最大扭矩转速(rpm)'
      + "f28 text NOT NULL DEFAULT '' ,"//COMMENT '气缸排列形式'
      + "f29 text NOT NULL DEFAULT '' ,"//COMMENT '气缸数(个)'
      + "f30 text NOT NULL DEFAULT '' ,"//COMMENT '每缸气门数(个)'
      + "f31 text NOT NULL DEFAULT '' ,"//COMMENT '压缩比'
      + "f32 text NOT NULL DEFAULT '' ,"//COMMENT '供油方式'
      + "f33 text NOT NULL DEFAULT '' ,"//COMMENT '工信部综合油耗(L)'
      + "f34 text NOT NULL DEFAULT '' ,"//COMMENT '加速时间（0-100km/h)'
      + "f35 text NOT NULL DEFAULT '' ,"//COMMENT '最高车速(km/h)'
      + "f36 text NOT NULL DEFAULT '' ,"//COMMENT '变速箱类型'
      + "f37 text NOT NULL DEFAULT '' ,"//COMMENT '变速器描述'
      + "f38 text NOT NULL DEFAULT '' ,"//COMMENT '档位数'
      + "f39 text NOT NULL DEFAULT '' ,"//COMMENT '前制动器类型'
      + "f40 text NOT NULL DEFAULT '' ,"//COMMENT '后制动器类型'
      + "f41 text NOT NULL DEFAULT '' ,"//COMMENT '前悬挂类型'
      + "f42 text NOT NULL DEFAULT '' ,"//COMMENT '后悬挂类型'
      + "f43 text NOT NULL DEFAULT '' ,"//COMMENT '转向机形式'
      + "f44 text NOT NULL DEFAULT '' ,"//COMMENT '助力类型'
      + "f45 text NOT NULL DEFAULT '' ,"//COMMENT '驱动形式'
      + "f46 text NOT NULL DEFAULT '' ,"//COMMENT '车身型式'
      + "f47 text NOT NULL DEFAULT '' ,"//COMMENT '长(mm)'
      + "f48 text NOT NULL DEFAULT '' ,"//COMMENT '宽(mm)'
      + "f49 text NOT NULL DEFAULT '' ,"//COMMENT '高(mm)'
      + "f50 text NOT NULL DEFAULT '' ,"//COMMENT '轴距(mm)'
      + "f51 text NOT NULL DEFAULT '' ,"//COMMENT '前轮距(mm)'
      + "f52 text NOT NULL DEFAULT '' ,"//COMMENT '后轮距(mm)'
      + "f53 text NOT NULL DEFAULT '' ,"//COMMENT '整备质量(Kg)'
      + "f54 text NOT NULL DEFAULT '' ,"//COMMENT '最大载重质量(kg)'
      + "f55 text NOT NULL DEFAULT '' ,"//COMMENT '油箱容积(L)'
      + "f56 text NOT NULL DEFAULT '' ,"//COMMENT '行李厢容积(L)'
      + "f57 text NOT NULL DEFAULT '' ,"//COMMENT '车门数'
      + "f58 text NOT NULL DEFAULT '' ,"//COMMENT '座位数(个)'
      + "f59 text NOT NULL DEFAULT '' ,"//COMMENT '前轮胎规格'
      + "f60 text NOT NULL DEFAULT '' ,"//COMMENT '后轮胎规格'
      + "f61 text NOT NULL DEFAULT '' ,"//COMMENT '前轮毂规格'
      + "f62 text NOT NULL DEFAULT '' ,"//COMMENT '后轮毂规格'
      + "f63 text NOT NULL DEFAULT '' ,"//COMMENT '轮毂材料'
      + "f64 text NOT NULL DEFAULT '' ,"//COMMENT '备胎规格'
      + "f65 text NOT NULL DEFAULT '' ,"//COMMENT '驾驶座安全气囊;'
      + "f66 text NOT NULL DEFAULT '' ,"//COMMENT '副驾驶安全气囊;'
      + "f67 text NOT NULL DEFAULT '' ,"//COMMENT '前排侧气囊;'
      + "f68 text NOT NULL DEFAULT '' ,"//COMMENT '后排侧气囊;'
      + "f69 text NOT NULL DEFAULT '' ,"//COMMENT '前排头部气囊(气帘);'
      + "f70 text NOT NULL DEFAULT '' ,"//COMMENT '后排头部气囊(气帘);'
      + "f71 text NOT NULL DEFAULT '' ,"//COMMENT '膝部气囊;'
      + "f72 text NOT NULL DEFAULT '' ,"//COMMENT '胎压监测装置;
      + "f73 text NOT NULL DEFAULT '' ,"//COMMENT '零胎压继续行驶;'
      + "f74 text NOT NULL DEFAULT '' ,"//COMMENT '安全带未系提示;'
      + "f75 text NOT NULL DEFAULT '' ,"//COMMENT 'ISOFIX儿童座椅接口;'
      + "f76 text NOT NULL DEFAULT '' ,"//COMMENT 'LATCH座椅接口;'
      + "f77 text NOT NULL DEFAULT '' ,"//COMMENT '发动机电子防盗;'
      + "f78 text NOT NULL DEFAULT '' ,"//COMMENT '中控锁;'
      + "f79 text NOT NULL DEFAULT '' ,"//COMMENT '遥控钥匙;'
      + "f80 text NOT NULL DEFAULT '' ,"//COMMENT '无钥匙启动系统;'
      + "f81 text NOT NULL DEFAULT '' ,"//COMMENT 'ABS防抱死;'
      + "f82 text NOT NULL DEFAULT '' ,"//COMMENT '制动力分配(EBD/CBC等);'
      + "f83 text NOT NULL DEFAULT '' ,"//COMMENT '刹车辅助(EBA/BAS/BA等);'
      + "f84 text NOT NULL DEFAULT '' ,"//COMMENT '牵引力控制(ASR/TCS/TRC等);'
      + "f85 text NOT NULL DEFAULT '' ,"//COMMENT '车身稳定控制(ESP/DSC/VSC等);'
      + "f86 text NOT NULL DEFAULT '' ,"//COMMENT '自动驻车/上坡辅助;'
      + "f87 text NOT NULL DEFAULT '' ,"//COMMENT '陡坡缓降;'
      + "f88 text NOT NULL DEFAULT '' ,"//COMMENT '可变悬挂;'
      + "f89 text NOT NULL DEFAULT '' ,"//COMMENT '空气悬挂;'
      + "f90 text NOT NULL DEFAULT '' ,"//COMMENT '可变转向比;'
      + "f91 text NOT NULL DEFAULT '' ,"//COMMENT '并线辅助;'
      + "f92 text NOT NULL DEFAULT '' ,"//COMMENT '主动刹车;'
      + "f93 text NOT NULL DEFAULT '' ,"//COMMENT '主动转向系统;'
      + "f94 text NOT NULL DEFAULT '' ,"//COMMENT '真皮方向盘;'
      + "f95 text NOT NULL DEFAULT '' ,"//COMMENT '方向盘上下调节;'
      + "f96 text NOT NULL DEFAULT '' ,"//COMMENT '方向盘前后调节;'
      + "f97 text NOT NULL DEFAULT '' ,"//COMMENT '方向盘电动调节;'
      + "f98 text NOT NULL DEFAULT '' ,"//COMMENT '多功能方向盘;'
      + "f99 text NOT NULL DEFAULT '' ,"//COMMENT '方向盘换挡;'
      + "f100 text NOT NULL DEFAULT '' ,"//COMMENT '真皮座椅;'
      + "f101 text NOT NULL DEFAULT '' ,"//COMMENT '运动座椅;'
      + "f102 text NOT NULL DEFAULT '' ,"//COMMENT '座椅高低调节;'
      + "f103 text NOT NULL DEFAULT '' ,"//COMMENT '腰部支撑调节;'
      + "f104 text NOT NULL DEFAULT '' ,"//COMMENT '肩部支撑调节;'
      + "f105 text NOT NULL DEFAULT '' ,"//COMMENT '驾驶座座椅电动调节;'
      + "f106 text NOT NULL DEFAULT '' ,"//COMMENT '副驾驶座座椅电动调节;'
      + "f107 text NOT NULL DEFAULT '' ,"//COMMENT '第二排靠背角度调节;'
      + "f108 text NOT NULL DEFAULT '' ,"//COMMENT '第二排座椅移动;'
      + "f109 text NOT NULL DEFAULT '' ,"//COMMENT '后排座椅电动调节;'
      + "f110 text NOT NULL DEFAULT '' ,"//COMMENT '电动座椅记忆;'
      + "f111 text NOT NULL DEFAULT '' ,"//COMMENT '前排座椅加热;'
      + "f112 text NOT NULL DEFAULT '' ,"//COMMENT '后排座椅加热;'
      + "f113 text NOT NULL DEFAULT '' ,"//COMMENT '座椅通风;'
      + "f114 text NOT NULL DEFAULT '' ,"//COMMENT '座椅按摩;'
      + "f115 text NOT NULL DEFAULT '' ,"//COMMENT '后排座椅整体放倒;'
      + "f116 text NOT NULL DEFAULT '' ,"//COMMENT '后排座椅比例放倒;'
      + "f117 text NOT NULL DEFAULT '' ,"//COMMENT '第三排座椅;'
      + "f118 text NOT NULL DEFAULT '' ,"//COMMENT '前座中央扶手;'
      + "f119 text NOT NULL DEFAULT '' ,"//COMMENT '后座中央扶手;'
      + "f120 text NOT NULL DEFAULT '' ,"//COMMENT '后排杯架;'
      + "f121 text NOT NULL DEFAULT '' ,"//COMMENT '车内氛围灯;'
      + "f122 text NOT NULL DEFAULT '' ,"//COMMENT '后风挡遮阳帘;'
      + "f123 text NOT NULL DEFAULT '' ,"//COMMENT '后排侧遮阳帘;'
      + "f124 text NOT NULL DEFAULT '' ,"//COMMENT '遮阳板化妆镜;'
      + "f125 text NOT NULL DEFAULT '' ,"//COMMENT '电动后备箱;'
      + "f126 text NOT NULL DEFAULT '' ,"//COMMENT '运动外观套件;'
      + "f127 text NOT NULL DEFAULT '' ,"//COMMENT '电动吸合门;'
      + "f128 text NOT NULL DEFAULT '' ,"//COMMENT '电动天窗;'
      + "f129 text NOT NULL DEFAULT '' ,"//COMMENT '全景天窗;'
      + "f130 text NOT NULL DEFAULT '' ,"//COMMENT '氙气大灯;'
      + "f131 text NOT NULL DEFAULT '' ,"//COMMENT 'LED大灯;'
      + "f132 text NOT NULL DEFAULT '' ,"//COMMENT '日间行车灯;'
      + "f133 text NOT NULL DEFAULT '' ,"//COMMENT '自动头灯;'
      + "f134 text NOT NULL DEFAULT '' ,"//COMMENT '转向头灯;'
      + "f135 text NOT NULL DEFAULT '' ,"//COMMENT '前雾灯;'
      + "f136 text NOT NULL DEFAULT '' ,"//COMMENT '大灯高度可调;'
      + "f137 text NOT NULL DEFAULT '' ,"//COMMENT '大灯清洗装置;'
      + "f138 text NOT NULL DEFAULT '' ,"//COMMENT '前电动车窗;'
      + "f139 text NOT NULL DEFAULT '' ,"//COMMENT '后电动车窗;'
      + "f140 text NOT NULL DEFAULT '' ,"//COMMENT '车窗防夹手功能;'
      + "f141 text NOT NULL DEFAULT '' ,"//COMMENT '隔热玻璃;'
      + "f142 text NOT NULL DEFAULT '' ,"//COMMENT '后视镜电动调节;'
      + "f143 text NOT NULL DEFAULT '' ,"//COMMENT '后视镜加热;'
      + "f144 text NOT NULL DEFAULT '' ,"//COMMENT '后视镜自动防眩目;'
      + "f145 text NOT NULL DEFAULT '' ,"//COMMENT '后视镜电动折叠;'
      + "f146 text NOT NULL DEFAULT '' ,"//COMMENT '后视镜记忆;'
      + "f147 text NOT NULL DEFAULT '' ,"//COMMENT '后雨刷;'
      + "f148 text NOT NULL DEFAULT '' ,"//COMMENT '感应雨刷;'
      + "f149 text NOT NULL DEFAULT '' ,"//COMMENT '定速巡航;'
      + "f150 text NOT NULL DEFAULT '' ,"//COMMENT '泊车辅助;'
      + "f151 text NOT NULL DEFAULT '' ,"//COMMENT '倒车视频影像;'
      + "f152 text NOT NULL DEFAULT '' ,"//COMMENT '行车电脑显示屏;'
      + "f153 text NOT NULL DEFAULT '' ,"//COMMENT 'HUD抬头数字显示;'
      + "f154 text NOT NULL DEFAULT '' ,"//COMMENT 'GPS导航;'
      + "f155 text NOT NULL DEFAULT '' ,"//COMMENT '定位互动服务;'
      + "f156 text NOT NULL DEFAULT '' ,"//COMMENT '中控台彩色大屏;'
      + "f157 text NOT NULL DEFAULT '' ,"//COMMENT '人机交互系统;'
      + "f158 text NOT NULL DEFAULT '' ,"//COMMENT '内置硬盘;'
      + "f159 text NOT NULL DEFAULT '' ,"//COMMENT '蓝牙/车载电话;'
      + "f160 text NOT NULL DEFAULT '' ,"//COMMENT '车载电视;'
      + "f161 text NOT NULL DEFAULT '' ,"//COMMENT '后排液晶屏;'
      + "f162 text NOT NULL DEFAULT '' ,"//COMMENT '外接音源接口(AUX/USB/iPod等);'
      + "f163 text NOT NULL DEFAULT '' ,"//COMMENT '音频支持MP3;'
      + "f164 text NOT NULL DEFAULT '' ,"//COMMENT '单碟CD;'
      + "f165 text NOT NULL DEFAULT '' ,"//COMMENT '多碟CD;'
      + "f166 text NOT NULL DEFAULT '' ,"//COMMENT '虚拟多碟CD;'
      + "f167 text NOT NULL DEFAULT '' ,"//COMMENT '单碟DVD;'
      + "f168 text NOT NULL DEFAULT '' ,"//COMMENT '多碟DVD;'
      + "f169 text NOT NULL DEFAULT '' ,"//COMMENT '扬声器数量'
      + "f170 text NOT NULL DEFAULT '' ,"//COMMENT '空调;'
      + "f171 text NOT NULL DEFAULT '' ,"//COMMENT '自动空调;'
      + "f172 text NOT NULL DEFAULT '' ,"//COMMENT '后排独立空调;'
      + "f173 text NOT NULL DEFAULT '' ,"//COMMENT '后座出风口;'
      + "f174 text NOT NULL DEFAULT '' ,"//COMMENT '温度分区控制;'
      + "f175 text NOT NULL DEFAULT '' ,"//COMMENT '空气调节/花粉过滤;'
      + "f176 text NOT NULL DEFAULT '' ,"//COMMENT '车载冰箱;'
      + "f177 text NOT NULL DEFAULT '' ,"//COMMENT '自动泊车入位;'
      + "f178 text NOT NULL DEFAULT '' ,"//COMMENT '夜视系统;'
      + "f179 text NOT NULL DEFAULT '' ,"//COMMENT '中控液晶屏分屏显示;'
      + "f180 text NOT NULL DEFAULT '' ,"//COMMENT '自适应巡航;'
      + "f181 text NOT NULL DEFAULT '' ,"//COMMENT '全景摄像头;'
      + "f182 text NOT NULL DEFAULT '' ,"//COMMENT '倒车雷达;'
      + "f183 text NOT NULL DEFAULT '' ,"//COMMENT '车载信息服务;'
      + "vehicle_name text NOT NULL DEFAULT '' "//COMMENT '车名'
      + ");";

  private static final String[] COLUMNS = {
      "model_id",
      "f1",
      "f2",
      "f3",
      "f4",
      "f5",
      "f6",
      "f7",
      "f8",
      "f9",
      "f10",
      "f11",
      "f12",
      "f13",
      "f14",
      "f15",
      "f16",
      "f17",
      "f18",
      "f19",
      "f20",
      "f21",
      "f22",
      "f23",
      "f24",
      "f25",
      "f26",
      "f27",
      "f28",
      "f29",
      "f30",
      "f31",
      "f32",
      "f33",
      "f34",
      "f35",
      "f36",
      "f37",
      "f38",
      "f39",
      "f40",
      "f41",
      "f42",
      "f43",
      "f44",
      "f45",
      "f46",
      "f47",
      "f48",
      "f49",
      "f50",
      "f51",
      "f52",
      "f53",
      "f54",
      "f55",
      "f56",
      "f57",
      "f58",
      "f59",
      "f60",
      "f61",
      "f62",
      "f63",
      "f64",
      "f65",
      "f66",
      "f67",
      "f68",
      "f69",
      "f70",
      "f71",
      "f72",
      "f73",
      "f74",
      "f75",
      "f76",
      "f77",
      "f78",
      "f79",
      "f80",
      "f81",
      "f82",
      "f83",
      "f84",
      "f85",
      "f86",
      "f87",
      "f88",
      "f89",
      "f90",
      "f91",
      "f92",
      "f93",
      "f94",
      "f95",
      "f96",
      "f97",
      "f98",
      "f99",
      "f100",
      "f101",
      "f102",
      "f103",
      "f104",
      "f105",
      "f106",
      "f107",
      "f108",
      "f109",
      "f110",
      "f111",
      "f112",
      "f113",
      "f114",
      "f115",
      "f116",
      "f117",
      "f118",
      "f119",
      "f120",
      "f121",
      "f122",
      "f123",
      "f124",
      "f125",
      "f126",
      "f127",
      "f128",
      "f129",
      "f130",
      "f131",
      "f132",
      "f133",
      "f134",
      "f135",
      "f136",
      "f137",
      "f138",
      "f139",
      "f140",
      "f141",
      "f142",
      "f143",
      "f144",
      "f145",
      "f146",
      "f147",
      "f148",
      "f149",
      "f150",
      "f151",
      "f152",
      "f153",
      "f154",
      "f155",
      "f156",
      "f157",
      "f158",
      "f159",
      "f160",
      "f161",
      "f162",
      "f163",
      "f164",
      "f165",
      "f166",
      "f167",
      "f168",
      "f169",
      "f170",
      "f171",
      "f172",
      "f173",
      "f174",
      "f175",
      "f176",
      "f177",
      "f178",
      "f179",
      "f180",
      "f181",
      "f182",
      "f183",
      "vehicle_name"
  };

  public static LiYangVehicleDAO getInstance() {
    return dao;
  }

  @Override
  protected String getTableName() {
    return TABLE_NAME;
  }

  @Override
  protected ContentValues getContentValues(BaseEntity entity) {
    LiYangVehicleEntity vehicleEntity = (LiYangVehicleEntity) entity;
    ContentValues values = new ContentValues();
    values.put(COLUMNS[0], vehicleEntity.getModel_id());
    values.put(COLUMNS[1], vehicleEntity.getF1());
    values.put(COLUMNS[2], vehicleEntity.getF2());
    values.put(COLUMNS[3], vehicleEntity.getF3());
    values.put(COLUMNS[4], vehicleEntity.getF4());
    values.put(COLUMNS[5], vehicleEntity.getF5());
    values.put(COLUMNS[6], vehicleEntity.getF6());
    values.put(COLUMNS[7], vehicleEntity.getF7());
    values.put(COLUMNS[8], vehicleEntity.getF8());
    values.put(COLUMNS[9], vehicleEntity.getF9());
    values.put(COLUMNS[10], vehicleEntity.getF10());
    values.put(COLUMNS[11], vehicleEntity.getF11());
    values.put(COLUMNS[12], vehicleEntity.getF12());
    values.put(COLUMNS[13], vehicleEntity.getF13());
    values.put(COLUMNS[14], vehicleEntity.getF14());
    values.put(COLUMNS[15], vehicleEntity.getF15());
    values.put(COLUMNS[16], vehicleEntity.getF16());
    values.put(COLUMNS[17], vehicleEntity.getF17());
    values.put(COLUMNS[18], vehicleEntity.getF18());
    values.put(COLUMNS[19], vehicleEntity.getF19());
    values.put(COLUMNS[20], vehicleEntity.getF20());
    values.put(COLUMNS[21], vehicleEntity.getF21());
    values.put(COLUMNS[22], vehicleEntity.getF22());
    values.put(COLUMNS[23], vehicleEntity.getF23());
    values.put(COLUMNS[24], vehicleEntity.getF24());
    values.put(COLUMNS[25], vehicleEntity.getF25());
    values.put(COLUMNS[26], vehicleEntity.getF26());
    values.put(COLUMNS[27], vehicleEntity.getF27());
    values.put(COLUMNS[28], vehicleEntity.getF28());
    values.put(COLUMNS[29], vehicleEntity.getF29());
    values.put(COLUMNS[30], vehicleEntity.getF30());
    values.put(COLUMNS[31], vehicleEntity.getF31());
    values.put(COLUMNS[32], vehicleEntity.getF32());
    values.put(COLUMNS[33], vehicleEntity.getF33());
    values.put(COLUMNS[34], vehicleEntity.getF34());
    values.put(COLUMNS[35], vehicleEntity.getF35());
    values.put(COLUMNS[36], vehicleEntity.getF36());
    values.put(COLUMNS[37], vehicleEntity.getF37());
    values.put(COLUMNS[38], vehicleEntity.getF38());
    values.put(COLUMNS[39], vehicleEntity.getF39());
    values.put(COLUMNS[40], vehicleEntity.getF40());
    values.put(COLUMNS[41], vehicleEntity.getF41());
    values.put(COLUMNS[42], vehicleEntity.getF42());
    values.put(COLUMNS[43], vehicleEntity.getF43());
    values.put(COLUMNS[44], vehicleEntity.getF44());
    values.put(COLUMNS[45], vehicleEntity.getF45());
    values.put(COLUMNS[46], vehicleEntity.getF46());

    values.put(COLUMNS[47], vehicleEntity.getF47());
    values.put(COLUMNS[48], vehicleEntity.getF48());
    values.put(COLUMNS[49], vehicleEntity.getF49());
    values.put(COLUMNS[50], vehicleEntity.getF50());
    values.put(COLUMNS[51], vehicleEntity.getF51());
    values.put(COLUMNS[52], vehicleEntity.getF52());
    values.put(COLUMNS[53], vehicleEntity.getF53());
    values.put(COLUMNS[54], vehicleEntity.getF54());
    values.put(COLUMNS[55], vehicleEntity.getF55());
    values.put(COLUMNS[56], vehicleEntity.getF56());

    values.put(COLUMNS[57], vehicleEntity.getF57());
    values.put(COLUMNS[58], vehicleEntity.getF58());
    values.put(COLUMNS[59], vehicleEntity.getF59());
    values.put(COLUMNS[60], vehicleEntity.getF60());
    values.put(COLUMNS[61], vehicleEntity.getF61());
    values.put(COLUMNS[62], vehicleEntity.getF62());
    values.put(COLUMNS[63], vehicleEntity.getF63());
    values.put(COLUMNS[64], vehicleEntity.getF64());
    values.put(COLUMNS[65], vehicleEntity.getF65());
    values.put(COLUMNS[66], vehicleEntity.getF66());
    values.put(COLUMNS[67], vehicleEntity.getF67());
    values.put(COLUMNS[68], vehicleEntity.getF68());
    values.put(COLUMNS[69], vehicleEntity.getF69());
    values.put(COLUMNS[70], vehicleEntity.getF70());
    values.put(COLUMNS[71], vehicleEntity.getF71());
    values.put(COLUMNS[72], vehicleEntity.getF72());
    values.put(COLUMNS[73], vehicleEntity.getF73());
    values.put(COLUMNS[74], vehicleEntity.getF74());
    values.put(COLUMNS[75], vehicleEntity.getF75());
    values.put(COLUMNS[76], vehicleEntity.getF76());
    values.put(COLUMNS[77], vehicleEntity.getF77());
    values.put(COLUMNS[78], vehicleEntity.getF78());
    values.put(COLUMNS[79], vehicleEntity.getF79());
    values.put(COLUMNS[80], vehicleEntity.getF80());
    values.put(COLUMNS[81], vehicleEntity.getF81());
    values.put(COLUMNS[82], vehicleEntity.getF82());
    values.put(COLUMNS[83], vehicleEntity.getF83());
    values.put(COLUMNS[84], vehicleEntity.getF84());
    values.put(COLUMNS[85], vehicleEntity.getF85());
    values.put(COLUMNS[86], vehicleEntity.getF86());
    values.put(COLUMNS[87], vehicleEntity.getF87());
    values.put(COLUMNS[88], vehicleEntity.getF88());
    values.put(COLUMNS[89], vehicleEntity.getF89());
    values.put(COLUMNS[90], vehicleEntity.getF90());
    values.put(COLUMNS[91], vehicleEntity.getF91());
    values.put(COLUMNS[92], vehicleEntity.getF92());
    values.put(COLUMNS[93], vehicleEntity.getF93());
    values.put(COLUMNS[94], vehicleEntity.getF94());
    values.put(COLUMNS[95], vehicleEntity.getF95());
    values.put(COLUMNS[96], vehicleEntity.getF96());
    values.put(COLUMNS[97], vehicleEntity.getF97());
    values.put(COLUMNS[98], vehicleEntity.getF98());
    values.put(COLUMNS[99], vehicleEntity.getF99());
    values.put(COLUMNS[100], vehicleEntity.getF100());
    values.put(COLUMNS[101], vehicleEntity.getF101());
    values.put(COLUMNS[102], vehicleEntity.getF102());
    values.put(COLUMNS[103], vehicleEntity.getF103());
    values.put(COLUMNS[104], vehicleEntity.getF104());
    values.put(COLUMNS[105], vehicleEntity.getF105());
    values.put(COLUMNS[106], vehicleEntity.getF106());
    values.put(COLUMNS[107], vehicleEntity.getF107());
    values.put(COLUMNS[108], vehicleEntity.getF108());
    values.put(COLUMNS[109], vehicleEntity.getF109());
    values.put(COLUMNS[110], vehicleEntity.getF110());
    values.put(COLUMNS[111], vehicleEntity.getF111());
    values.put(COLUMNS[112], vehicleEntity.getF112());
    values.put(COLUMNS[113], vehicleEntity.getF113());
    values.put(COLUMNS[114], vehicleEntity.getF114());
    values.put(COLUMNS[115], vehicleEntity.getF115());
    values.put(COLUMNS[116], vehicleEntity.getF116());
    values.put(COLUMNS[117], vehicleEntity.getF117());
    values.put(COLUMNS[118], vehicleEntity.getF118());
    values.put(COLUMNS[119], vehicleEntity.getF119());
    values.put(COLUMNS[120], vehicleEntity.getF120());
    values.put(COLUMNS[121], vehicleEntity.getF121());
    values.put(COLUMNS[122], vehicleEntity.getF122());
    values.put(COLUMNS[123], vehicleEntity.getF123());
    values.put(COLUMNS[124], vehicleEntity.getF124());
    values.put(COLUMNS[125], vehicleEntity.getF125());
    values.put(COLUMNS[126], vehicleEntity.getF126());
    values.put(COLUMNS[127], vehicleEntity.getF127());
    values.put(COLUMNS[128], vehicleEntity.getF128());
    values.put(COLUMNS[129], vehicleEntity.getF129());
    values.put(COLUMNS[130], vehicleEntity.getF130());
    values.put(COLUMNS[131], vehicleEntity.getF131());
    values.put(COLUMNS[132], vehicleEntity.getF132());
    values.put(COLUMNS[133], vehicleEntity.getF133());
    values.put(COLUMNS[134], vehicleEntity.getF134());
    values.put(COLUMNS[135], vehicleEntity.getF135());
    values.put(COLUMNS[136], vehicleEntity.getF136());
    values.put(COLUMNS[137], vehicleEntity.getF137());
    values.put(COLUMNS[138], vehicleEntity.getF138());
    values.put(COLUMNS[139], vehicleEntity.getF139());
    values.put(COLUMNS[140], vehicleEntity.getF140());
    values.put(COLUMNS[141], vehicleEntity.getF141());
    values.put(COLUMNS[142], vehicleEntity.getF142());
    values.put(COLUMNS[143], vehicleEntity.getF143());
    values.put(COLUMNS[144], vehicleEntity.getF144());
    values.put(COLUMNS[145], vehicleEntity.getF145());
    values.put(COLUMNS[146], vehicleEntity.getF146());
    values.put(COLUMNS[147], vehicleEntity.getF147());
    values.put(COLUMNS[148], vehicleEntity.getF148());
    values.put(COLUMNS[149], vehicleEntity.getF149());
    values.put(COLUMNS[150], vehicleEntity.getF150());
    values.put(COLUMNS[151], vehicleEntity.getF151());
    values.put(COLUMNS[152], vehicleEntity.getF152());
    values.put(COLUMNS[153], vehicleEntity.getF153());
    values.put(COLUMNS[154], vehicleEntity.getF154());
    values.put(COLUMNS[155], vehicleEntity.getF155());
    values.put(COLUMNS[156], vehicleEntity.getF156());
    values.put(COLUMNS[157], vehicleEntity.getF157());
    values.put(COLUMNS[158], vehicleEntity.getF158());
    values.put(COLUMNS[159], vehicleEntity.getF159());
    values.put(COLUMNS[160], vehicleEntity.getF160());
    values.put(COLUMNS[161], vehicleEntity.getF161());
    values.put(COLUMNS[162], vehicleEntity.getF162());
    values.put(COLUMNS[163], vehicleEntity.getF163());
    values.put(COLUMNS[164], vehicleEntity.getF164());
    values.put(COLUMNS[165], vehicleEntity.getF165());
    values.put(COLUMNS[166], vehicleEntity.getF166());
    values.put(COLUMNS[167], vehicleEntity.getF167());
    values.put(COLUMNS[168], vehicleEntity.getF168());
    values.put(COLUMNS[169], vehicleEntity.getF169());
    values.put(COLUMNS[170], vehicleEntity.getF170());
    values.put(COLUMNS[171], vehicleEntity.getF171());
    values.put(COLUMNS[172], vehicleEntity.getF172());
    values.put(COLUMNS[173], vehicleEntity.getF173());
    values.put(COLUMNS[174], vehicleEntity.getF174());
    values.put(COLUMNS[175], vehicleEntity.getF175());
    values.put(COLUMNS[176], vehicleEntity.getF176());
    values.put(COLUMNS[177], vehicleEntity.getF177());
    values.put(COLUMNS[178], vehicleEntity.getF178());
    values.put(COLUMNS[179], vehicleEntity.getF179());
    values.put(COLUMNS[180], vehicleEntity.getF180());
    values.put(COLUMNS[181], vehicleEntity.getF181());
    values.put(COLUMNS[182], vehicleEntity.getF182());
    values.put(COLUMNS[183], vehicleEntity.getF183());
    values.put(COLUMNS[184], vehicleEntity.getVehicle_name());

    return values;
  }

  @Override
  protected BaseEntity getEntityFromCursor(Cursor mCursor) {
    LiYangVehicleEntity vehicleEntity = new LiYangVehicleEntity();
    vehicleEntity.setModel_id(mCursor.getString(0));
    vehicleEntity.setF1(mCursor.getString(1));
    vehicleEntity.setF2(mCursor.getString(2));
    vehicleEntity.setF3(mCursor.getString(3));
    vehicleEntity.setF4(mCursor.getString(4));
    vehicleEntity.setF5(mCursor.getString(5));
    vehicleEntity.setF6(mCursor.getString(6));
    vehicleEntity.setF7(mCursor.getString(7));
    vehicleEntity.setF8(mCursor.getString(8));
    vehicleEntity.setF9(mCursor.getString(9));
    vehicleEntity.setF10(mCursor.getString(10));
    vehicleEntity.setF11(mCursor.getString(11));
    vehicleEntity.setF12(mCursor.getString(12));
    vehicleEntity.setF13(mCursor.getString(13));
    vehicleEntity.setF14(mCursor.getString(14));
    vehicleEntity.setF15(mCursor.getString(15));
    vehicleEntity.setF16(mCursor.getString(16));
    vehicleEntity.setF17(mCursor.getString(17));
    vehicleEntity.setF18(mCursor.getString(18));
    vehicleEntity.setF19(mCursor.getString(19));
    vehicleEntity.setF20(mCursor.getString(20));
    vehicleEntity.setF21(mCursor.getString(21));
    vehicleEntity.setF22(mCursor.getString(22));
    vehicleEntity.setF23(mCursor.getString(23));
    vehicleEntity.setF24(mCursor.getString(24));
    vehicleEntity.setF25(mCursor.getString(25));
    vehicleEntity.setF26(mCursor.getString(26));
    vehicleEntity.setF27(mCursor.getString(27));
    vehicleEntity.setF28(mCursor.getString(28));
    vehicleEntity.setF29(mCursor.getString(29));
    vehicleEntity.setF30(mCursor.getString(30));
    vehicleEntity.setF31(mCursor.getString(31));
    vehicleEntity.setF32(mCursor.getString(32));
    vehicleEntity.setF33(mCursor.getString(33));
    vehicleEntity.setF34(mCursor.getString(34));
    vehicleEntity.setF35(mCursor.getString(35));
    vehicleEntity.setF36(mCursor.getString(36));
    vehicleEntity.setF37(mCursor.getString(37));
    vehicleEntity.setF38(mCursor.getString(38));
    vehicleEntity.setF39(mCursor.getString(39));
    vehicleEntity.setF40(mCursor.getString(40));
    vehicleEntity.setF41(mCursor.getString(41));
    vehicleEntity.setF42(mCursor.getString(42));
    vehicleEntity.setF43(mCursor.getString(43));
    vehicleEntity.setF44(mCursor.getString(44));
    vehicleEntity.setF45(mCursor.getString(45));
    vehicleEntity.setF46(mCursor.getString(46));
    vehicleEntity.setF47(mCursor.getString(47));
    vehicleEntity.setF48(mCursor.getString(48));
    vehicleEntity.setF49(mCursor.getString(49));
    vehicleEntity.setF50(mCursor.getString(50));
    vehicleEntity.setF51(mCursor.getString(51));
    vehicleEntity.setF52(mCursor.getString(52));
    vehicleEntity.setF53(mCursor.getString(53));
    vehicleEntity.setF54(mCursor.getString(54));
    vehicleEntity.setF55(mCursor.getString(55));
    vehicleEntity.setF56(mCursor.getString(56));
    vehicleEntity.setF57(mCursor.getString(57));
    vehicleEntity.setF58(mCursor.getString(58));
    vehicleEntity.setF59(mCursor.getString(59));
    vehicleEntity.setF60(mCursor.getString(60));
    vehicleEntity.setF61(mCursor.getString(61));
    vehicleEntity.setF62(mCursor.getString(62));
    vehicleEntity.setF63(mCursor.getString(63));
    vehicleEntity.setF64(mCursor.getString(64));
    vehicleEntity.setF65(mCursor.getString(65));
    vehicleEntity.setF66(mCursor.getString(66));
    vehicleEntity.setF67(mCursor.getString(67));
    vehicleEntity.setF68(mCursor.getString(68));
    vehicleEntity.setF69(mCursor.getString(69));
    vehicleEntity.setF70(mCursor.getString(70));
    vehicleEntity.setF71(mCursor.getString(71));
    vehicleEntity.setF72(mCursor.getString(72));
    vehicleEntity.setF73(mCursor.getString(73));
    vehicleEntity.setF74(mCursor.getString(74));
    vehicleEntity.setF75(mCursor.getString(75));
    vehicleEntity.setF76(mCursor.getString(76));
    vehicleEntity.setF77(mCursor.getString(77));
    vehicleEntity.setF78(mCursor.getString(78));
    vehicleEntity.setF79(mCursor.getString(79));
    vehicleEntity.setF80(mCursor.getString(80));
    vehicleEntity.setF81(mCursor.getString(81));
    vehicleEntity.setF82(mCursor.getString(82));
    vehicleEntity.setF83(mCursor.getString(83));
    vehicleEntity.setF84(mCursor.getString(84));
    vehicleEntity.setF85(mCursor.getString(85));
    vehicleEntity.setF86(mCursor.getString(86));
    vehicleEntity.setF87(mCursor.getString(87));
    vehicleEntity.setF88(mCursor.getString(88));
    vehicleEntity.setF89(mCursor.getString(89));
    vehicleEntity.setF90(mCursor.getString(90));
    vehicleEntity.setF91(mCursor.getString(91));
    vehicleEntity.setF92(mCursor.getString(92));
    vehicleEntity.setF93(mCursor.getString(93));
    vehicleEntity.setF94(mCursor.getString(94));
    vehicleEntity.setF95(mCursor.getString(95));
    vehicleEntity.setF96(mCursor.getString(96));
    vehicleEntity.setF97(mCursor.getString(97));
    vehicleEntity.setF98(mCursor.getString(98));
    vehicleEntity.setF99(mCursor.getString(99));
    vehicleEntity.setF100(mCursor.getString(100));
    vehicleEntity.setF101(mCursor.getString(101));
    vehicleEntity.setF102(mCursor.getString(102));
    vehicleEntity.setF103(mCursor.getString(103));
    vehicleEntity.setF104(mCursor.getString(104));
    vehicleEntity.setF105(mCursor.getString(105));
    vehicleEntity.setF106(mCursor.getString(106));
    vehicleEntity.setF107(mCursor.getString(107));
    vehicleEntity.setF108(mCursor.getString(108));
    vehicleEntity.setF109(mCursor.getString(109));
    vehicleEntity.setF110(mCursor.getString(110));
    vehicleEntity.setF111(mCursor.getString(111));
    vehicleEntity.setF112(mCursor.getString(112));
    vehicleEntity.setF113(mCursor.getString(113));
    vehicleEntity.setF114(mCursor.getString(114));
    vehicleEntity.setF115(mCursor.getString(115));
    vehicleEntity.setF116(mCursor.getString(116));
    vehicleEntity.setF117(mCursor.getString(117));
    vehicleEntity.setF118(mCursor.getString(118));
    vehicleEntity.setF119(mCursor.getString(119));
    vehicleEntity.setF120(mCursor.getString(120));
    vehicleEntity.setF121(mCursor.getString(121));
    vehicleEntity.setF122(mCursor.getString(122));
    vehicleEntity.setF123(mCursor.getString(123));
    vehicleEntity.setF124(mCursor.getString(124));
    vehicleEntity.setF125(mCursor.getString(125));
    vehicleEntity.setF126(mCursor.getString(126));
    vehicleEntity.setF127(mCursor.getString(127));
    vehicleEntity.setF128(mCursor.getString(128));
    vehicleEntity.setF129(mCursor.getString(129));
    vehicleEntity.setF130(mCursor.getString(130));
    vehicleEntity.setF131(mCursor.getString(131));
    vehicleEntity.setF132(mCursor.getString(132));
    vehicleEntity.setF133(mCursor.getString(133));
    vehicleEntity.setF134(mCursor.getString(134));
    vehicleEntity.setF135(mCursor.getString(135));
    vehicleEntity.setF136(mCursor.getString(136));
    vehicleEntity.setF137(mCursor.getString(137));
    vehicleEntity.setF138(mCursor.getString(138));
    vehicleEntity.setF139(mCursor.getString(139));
    vehicleEntity.setF140(mCursor.getString(140));
    vehicleEntity.setF141(mCursor.getString(141));
    vehicleEntity.setF142(mCursor.getString(142));
    vehicleEntity.setF143(mCursor.getString(143));
    vehicleEntity.setF144(mCursor.getString(144));
    vehicleEntity.setF145(mCursor.getString(145));
    vehicleEntity.setF146(mCursor.getString(146));
    vehicleEntity.setF147(mCursor.getString(147));
    vehicleEntity.setF148(mCursor.getString(148));
    vehicleEntity.setF149(mCursor.getString(149));
    vehicleEntity.setF150(mCursor.getString(150));
    vehicleEntity.setF151(mCursor.getString(151));
    vehicleEntity.setF152(mCursor.getString(152));
    vehicleEntity.setF153(mCursor.getString(153));
    vehicleEntity.setF154(mCursor.getString(154));
    vehicleEntity.setF155(mCursor.getString(155));
    vehicleEntity.setF156(mCursor.getString(156));
    vehicleEntity.setF157(mCursor.getString(157));
    vehicleEntity.setF158(mCursor.getString(158));
    vehicleEntity.setF159(mCursor.getString(159));
    vehicleEntity.setF160(mCursor.getString(160));
    vehicleEntity.setF161(mCursor.getString(161));
    vehicleEntity.setF162(mCursor.getString(162));
    vehicleEntity.setF163(mCursor.getString(163));
    vehicleEntity.setF164(mCursor.getString(164));
    vehicleEntity.setF165(mCursor.getString(165));
    vehicleEntity.setF166(mCursor.getString(166));
    vehicleEntity.setF167(mCursor.getString(167));
    vehicleEntity.setF168(mCursor.getString(168));
    vehicleEntity.setF169(mCursor.getString(169));
    vehicleEntity.setF170(mCursor.getString(170));
    vehicleEntity.setF171(mCursor.getString(171));
    vehicleEntity.setF172(mCursor.getString(172));
    vehicleEntity.setF173(mCursor.getString(173));
    vehicleEntity.setF174(mCursor.getString(174));
    vehicleEntity.setF175(mCursor.getString(175));
    vehicleEntity.setF176(mCursor.getString(176));
    vehicleEntity.setF177(mCursor.getString(177));
    vehicleEntity.setF178(mCursor.getString(178));
    vehicleEntity.setF179(mCursor.getString(179));
    vehicleEntity.setF180(mCursor.getString(180));
    vehicleEntity.setF181(mCursor.getString(181));
    vehicleEntity.setF182(mCursor.getString(182));
    vehicleEntity.setF183(mCursor.getString(183));
    vehicleEntity.setVehicle_name(mCursor.getString(184));

    return vehicleEntity;
  }

  @Override
  protected String[] getColumns() {
    return COLUMNS;
  }

  @Override
  protected String getDefaultOrderby() {
    return null;
  }

  public List<LiYangVehicleEntity> get(String where) {
    List<LiYangVehicleEntity> list = (List<LiYangVehicleEntity>) super.query(where, null, null, null, null);
    return list;
  }

}
