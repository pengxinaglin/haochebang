package com.haoche51.checker.DAO;

import android.content.ContentValues;
import android.database.Cursor;

import com.haoche51.checker.entity.BaseEntity;
import com.haoche51.checker.entity.CheckReportEntity;

import java.util.List;

public class CheckReportDAO extends BaseDAO {
	private static CheckReportDAO dao = new CheckReportDAO();

	public static CheckReportDAO getInstance() {
		return dao;
	}

	public static final String TABLE_NAME = "check_report";
	public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
		+ "id integer primary key autoincrement ,"
		+ "check_appointment_id not null ,"
		+ "vehicle_source_id not null,"
		+ "check_user_id not null,"
		+ "car_num text not null default '',"
		+ "register_time integer not null,"
		+ "anual_valid_time integer not null ,"
		+ "tins_valid_time integer not null,"
		+ "ins_valid_time integer not null,"
		+ "trasfer_record integer not null,"
		+ "vehicle_brand_id integer not null,"
		+ "vehicle_class_id integer not null,"
		+ "vehicle_model_id integer not null,"
		+ "gearbox integer not null,"
		+ "emissions text not null,"
		+ "miles float not null,"
		+ "engine_oil integer not null ,"
		+ "cylinder integer not null,"
		+ "radiator integer not null,"
		+ "electrode_pillar integer not null,"
		+ "electrolyte integer not null,"
		+ "engine_belt integer not null,"
		+ "tubing integer not null,"
		+ "water_pipe integer not null,"
		+ "harness integer not null,"
		+ "engine_oil_location integer not null,"
		+ "breaker_oil integer not null,"
		+ "steering_oil integer not null,"
		+ "engine_hood integer not null,"
		+ "rf_fender integer not null,"
		+ "rf_door integer not null,"
		+ "rr_door integer not null,"
		+ "rr_fender integer not null,"
		+ "decklid integer not null,"
		+ "lr_fender integer not null,"
		+ "lr_door integer not null,"
		+ "lf_door integer not null,"
		+ "lf_fender integer not null,"
		+ "vehicle_roof integer not null,"
		+ "f_bumper integer not null,"
		+ "r_bumper integer not null,"
		+ "l_mirror integer not null,"
		+ "r_mirror integer not null,"
		+ "lf_headlight integer not null,"
		+ "rf_headlight integer not null,"
		+ "l_taillight integer not null,"
		+ "r_taillight integer not null,"
		+ "f_windshield integer not null,"
		+ "r_windshield integer not null,"
		+ "lf_glass integer not null,"
		+ "lr_glass integer not null,"
		+ "rf_glass integer not null,"
		+ "rr_glass integer not null,"
		+ "dashboard_platform integer not null,"
		+ "center_control integer not null,"
		+ "steering_wheel integer not null,"
		+ "gear_handler integer not null,"
		+ "cigar_lighter integer not null,"
		+ "glove_box integer not null,"
		+ "vehicle_roof_inner  integer not null,"
		+ "m_sunblind integer not null,"
		+ "a_subblind integer not null,"
		+ "rear_view integer not null,"
		+ "driver_seat integer not null,"
		+ "copilot_seat integer not null,"
		+ "rear_seat integer not null,"
		+ "center_armrest integer not null,"
		+ "lf_trim_plate integer not null,"
		+ "rf_trim_plate integer not null,"
		+ "lr_trim_plate integer not null,"
		+ "rr_trim_plate integer not null,"
		+ "lf_armrest integer not null,"
		+ "lr_armerest integer not null,"
		+ "rf_armerest integer not null,"
		+ "rl_armerest integer not null,"
		+ "a_trim_plate integer not null,"
		+ "b_trim_plate integer not null ,"
		+ "c_trim_plate integer not null,"
		+ "trunk integer not null,"
		+ "rubber_strip text not null,"
		+ "vehicle_smell integer not null,"
		+ "dashboard text not null,"
		+ "reading_lamp text not null,"
		+ "wiper text not null,"
		+ "window_lifter text not null,"
		+ "air_condition text not null,"
		+ "sound_system text not null,"
		+ "handbrake text not null,"
		+ "central_lock text not null,"
		+ "skylight text not null,"
		+ "central_dispaly text not null,"
		+ "gps  text not null,"
		+ "reverse_sensor text not null,"
		+ "m_electric_seat text not null,"
		+ "a_electric_seat text not null,"
		+ "dashboard_light text not null,"
		+ "abs text not null,"
		+ "air_bag text not null,"
		+ "safty_belt text not null,"
		+ "jack text not null,"
		+ "fire_extinguiher text not null,"
		+ "mark_delta text not null,"
		+ "high_beams text not null,"
		+ "dipped_beams text not null,"
		+ "fog_light text not null,"
		+ "steering_lamp text not null,"
		+ "taillight text not null,"
		+ "break_lamp text not null,"
		+ "driving_light text not null,"
		+ "tire_uniformity text not null,"
		+ "breakpads_thickness text not null,"
		+ "tread text not null,"
		+ "spare_tire integer not null,"
		+ "vehicle_start integer not null,"
		+ "engine_staility integer not null,"
		+ "idle_state integer not null,"
		+ "neutral_state integer not null,"
		+ "exhaust_color integer not null,"
		+ "fault_gear integer not null,"
		+ "transaction_place  text not null,"
		+ "seller_jobs text not null,"
		+ "buy_comment text not null,"
		+ "vehicle_advantage text not null,"
		+ "vehicle_usage text not null,"
		+ "vehicle_maintenance text not null,"
		+ "oil_consumption text not null,"
		+ "additional_configuration text not null,"
		+ "checker_comment text not null,"
		+ "final_comment text not null,"
		+ "create_time integer not null ,"
		+ "out_pics text not null ,"
		+ "inner_pics text not null ,"
		+ "detail_pics text not null ,"
		+ "defect_pics text not null ,"
		+ "transfer_times int not null default 0,"
		+ "vehicle_name text not null default '',"
		+ "vehicle_brand_name text not null default '',"
		+ "vehicle_series_name text not null default '',"
		+ "cheap_price float not null default 0,"
		+ "view_registration not null default 0,"
		+ "seller_name text not null default '',"
		+ "seller_phone text not null default '',"
		+ "checker_name text not null default '',"
		+ "emissions_standard integer not null default 0,"
		+ "backup_phone text default '',"
		+ "drive_mode integer not null default 0,"
		+ "seats_num integer not null default 0,"
		+ "transfer_reason text not null default '',"
		+ "car_story text not null default '',"
		+ "app_version text default '',"
		+ "water_tank integer not null default 0,"
		+ "metal_tank integer not null default 0,"
		+ "headlight_frame integer not null default 0,"
		+ "fender_liner integer not null default 0,"
		+ "back_floor integer not null default 0,"
		+ "back_coaming integer not null default 0,"
		+ "engine_sound integer not null default 0,"
		+ "engine_spill integer not null default 0,"
		+ "mortgage integer not null default 0,"
		+ "into_bj integer not null default 0,"
		+ "company_account integer not null default 0,"
		+ "operation integer not null default 0,"
		+ "suspected_agent integer not null default 0,"
		+ "eval_price float not null default 0,"
		+ "eval_price_21 float not null default 0,"
		+ "show_status integer default 0,"
		+ "seller_price float not null default 0,"
		+ "vin_code text default '',"
		+ "exclusive integer default 0,"
		+ "liyang_brand_name text default '',"
		+ "liyang_series_name text default '',"
		+ "liyang_model_name text default '',"
		+ "liyang_model_id text default '',"
		+ "check_source text default '',"
		+ "complete_check integer default 0,"
		+ "is_channel integer default 0,"
		+ "audio_url text default '',"
		+ "video_url text default ''"
		+ ");";

	private static final String[] COLUMNS = {
		"id",
		"check_appointment_id",
		"vehicle_source_id",
		"check_user_id",
		"car_num",
		"register_time",
		"anual_valid_time",
		"tins_valid_time",
		"ins_valid_time",
		"trasfer_record",
		"vehicle_brand_id",
		"vehicle_class_id",
		"vehicle_model_id",
		"gearbox",
		"emissions",
		"miles",
		"engine_oil",
		"cylinder",
		"radiator",
		"electrode_pillar",
		"electrolyte",
		"engine_belt",
		"tubing",
		"water_pipe",
		"harness",
		"engine_oil_location",
		"breaker_oil",
		"steering_oil",
		"engine_hood",
		"rf_fender",
		"rf_door",
		"rr_door",
		"rr_fender",
		"decklid",
		"lr_fender",
		"lr_door",
		"lf_door",
		"lf_fender",
		"vehicle_roof",
		"f_bumper",
		"r_bumper",
		"l_mirror",
		"r_mirror",
		"lf_headlight",
		"rf_headlight",
		"l_taillight",
		"r_taillight",
		"f_windshield",
		"r_windshield",
		"lf_glass",
		"lr_glass",
		"rf_glass",
		"rr_glass",
		"dashboard_platform",
		"center_control",
		"steering_wheel",
		"gear_handler",
		"cigar_lighter",
		"glove_box",
		"vehicle_roof_inner",
		"m_sunblind",
		"a_subblind",
		"rear_view",
		"driver_seat",
		"copilot_seat",
		"rear_seat",
		"center_armrest",
		"lf_trim_plate",
		"rf_trim_plate",
		"lr_trim_plate",
		"rr_trim_plate",
		"lf_armrest",
		"lr_armerest",
		"rf_armerest",
		"rl_armerest",
		"a_trim_plate",
		"b_trim_plate",
		"c_trim_plate",
		"trunk",
		"rubber_strip",
		"vehicle_smell",
		"dashboard",
		"reading_lamp",
		"wiper",
		"window_lifter",
		"air_condition",
		"sound_system",
		"handbrake",
		"central_lock",
		"skylight",
		"central_dispaly",
		"gps",
		"reverse_sensor",
		"m_electric_seat",
		"a_electric_seat",
		"dashboard_light",
		"abs",
		"air_bag",
		"safty_belt",
		"jack",
		"fire_extinguiher",
		"mark_delta",
		"high_beams",
		"dipped_beams",
		"fog_light",
		"steering_lamp",
		"taillight",
		"break_lamp",
		"driving_light",
		"tire_uniformity",
		"breakpads_thickness",
		"tread",
		"spare_tire",
		"vehicle_start",
		"engine_staility",
		"idle_state",
		"neutral_state",
		"exhaust_color",
		"fault_gear",
		"transaction_place",
		"seller_jobs",
		"buy_comment",
		"vehicle_advantage",
		"vehicle_usage",
		"vehicle_maintenance",
		"oil_consumption",
		"additional_configuration",
		"checker_comment",
		"final_comment",
		"create_time",
		"out_pics",
		"inner_pics",
		"detail_pics",
		"defect_pics",
		"transfer_times",
		"vehicle_name",
		"vehicle_brand_name",
		"vehicle_series_name",
		"cheap_price",
		"view_registration",
		"seller_name",
		"seller_phone",
		"checker_name",
		"emissions_standard",
		"backup_phone",
		"drive_mode",
		"seats_num",
		"transfer_reason",
		"car_story",
		"app_version",
		"water_tank",
		"metal_tank",
		"headlight_frame",
		"fender_liner",
		"back_floor",
		"back_coaming",
		"engine_sound",
		"engine_spill",
		"mortgage",
		"into_bj",
		"company_account",
		"operation",
		"suspected_agent",
		"eval_price",
		"eval_price_21",
		"show_status",
		"seller_price",
		"vin_code",
		"exclusive",
		"liyang_brand_name",
		"liyang_series_name",
		"liyang_model_name",
		"liyang_model_id",
		"check_source",
		"complete_check",
		"is_channel",
		"audio_url",
		"video_url"
	};

	private static final String DEFAULT_ORDER_BY = null;

	@Override
	protected String getTableName() {
		return TABLE_NAME;

	}

	@Override
	protected ContentValues getContentValues(BaseEntity entity) {
		CheckReportEntity checkReport = (CheckReportEntity) entity;
		ContentValues mValues = new ContentValues();
		mValues.put(COLUMNS[1], checkReport.getCheck_appointment_id());
		mValues.put(COLUMNS[2], checkReport.getVehicle_source_id());
		mValues.put(COLUMNS[3], checkReport.getCheck_user_id());
		mValues.put(COLUMNS[4], checkReport.getCar_num());
		mValues.put(COLUMNS[5], checkReport.getRegister_time());
		mValues.put(COLUMNS[6], checkReport.getAnual_valid_time());
		mValues.put(COLUMNS[7], checkReport.getTins_valid_time());
		mValues.put(COLUMNS[8], checkReport.getIns_valid_time());
		mValues.put(COLUMNS[9], checkReport.getTrasfer_record());
		mValues.put(COLUMNS[10], checkReport.getVehicle_brand_id());
		mValues.put(COLUMNS[11], checkReport.getVehicle_class_id());
		mValues.put(COLUMNS[12], checkReport.getVehicle_model_id());
		mValues.put(COLUMNS[13], checkReport.getGearbox());
		mValues.put(COLUMNS[14], checkReport.getEmissions());
		mValues.put(COLUMNS[15], checkReport.getMiles());
		mValues.put(COLUMNS[16], checkReport.getEngine_oil());
		mValues.put(COLUMNS[17], checkReport.getCylinder());
		mValues.put(COLUMNS[18], checkReport.getRadiator());
		mValues.put(COLUMNS[19], checkReport.getElectrode_pillar());
		mValues.put(COLUMNS[20], checkReport.getElectrolyte());
		mValues.put(COLUMNS[21], checkReport.getEngine_belt());
		mValues.put(COLUMNS[22], checkReport.getTubing());
		mValues.put(COLUMNS[23], checkReport.getWater_pipe());
		mValues.put(COLUMNS[24], checkReport.getHarness());
		mValues.put(COLUMNS[25], checkReport.getEngine_oil_location());
		mValues.put(COLUMNS[26], checkReport.getBreaker_oil());
		mValues.put(COLUMNS[27], checkReport.getSteering_oil());
		mValues.put(COLUMNS[28], checkReport.getEngine_hood());
		mValues.put(COLUMNS[29], checkReport.getRf_fender());
		mValues.put(COLUMNS[30], checkReport.getRf_door());
		mValues.put(COLUMNS[31], checkReport.getRr_door());
		mValues.put(COLUMNS[32], checkReport.getRr_fender());
		mValues.put(COLUMNS[33], checkReport.getDecklid());
		mValues.put(COLUMNS[34], checkReport.getLr_fender());
		mValues.put(COLUMNS[35], checkReport.getLr_door());
		mValues.put(COLUMNS[36], checkReport.getLf_door());
		mValues.put(COLUMNS[37], checkReport.getLf_fender());
		mValues.put(COLUMNS[38], checkReport.getVehicle_roof());
		mValues.put(COLUMNS[39], checkReport.getF_bumper());
		mValues.put(COLUMNS[40], checkReport.getR_bumper());
		mValues.put(COLUMNS[41], checkReport.getL_mirror());
		mValues.put(COLUMNS[42], checkReport.getR_mirror());
		mValues.put(COLUMNS[43], checkReport.getLf_headlight());
		mValues.put(COLUMNS[44], checkReport.getRf_headlight());
		mValues.put(COLUMNS[45], checkReport.getL_taillight());
		mValues.put(COLUMNS[46], checkReport.getR_taillight());
		mValues.put(COLUMNS[47], checkReport.getF_windshield());
		mValues.put(COLUMNS[48], checkReport.getR_windshield());
		mValues.put(COLUMNS[49], checkReport.getLf_glass());
		mValues.put(COLUMNS[50], checkReport.getLr_glass());
		mValues.put(COLUMNS[51], checkReport.getRf_glass());
		mValues.put(COLUMNS[52], checkReport.getRr_glass());
		mValues.put(COLUMNS[53], checkReport.getDashboard_platform());
		mValues.put(COLUMNS[54], checkReport.getCenter_control());
		mValues.put(COLUMNS[55], checkReport.getSteering_wheel());
		mValues.put(COLUMNS[56], checkReport.getGear_handler());
		mValues.put(COLUMNS[57], checkReport.getCigar_lighter());
		mValues.put(COLUMNS[58], checkReport.getGlove_box());
		mValues.put(COLUMNS[59], checkReport.getVehicle_roof_inner());
		mValues.put(COLUMNS[60], checkReport.getM_sunblind());
		mValues.put(COLUMNS[61], checkReport.getA_subblind());
		mValues.put(COLUMNS[62], checkReport.getRear_view());
		mValues.put(COLUMNS[63], checkReport.getDriver_seat());
		mValues.put(COLUMNS[64], checkReport.getCopilot_seat());
		mValues.put(COLUMNS[65], checkReport.getRear_seat());
		mValues.put(COLUMNS[66], checkReport.getCenter_armrest());
		mValues.put(COLUMNS[67], checkReport.getLf_trim_plate());
		mValues.put(COLUMNS[68], checkReport.getRf_trim_plate());
		mValues.put(COLUMNS[69], checkReport.getLr_trim_plate());
		mValues.put(COLUMNS[70], checkReport.getRr_trim_plate());
		mValues.put(COLUMNS[71], checkReport.getLf_armrest());
		mValues.put(COLUMNS[72], checkReport.getLr_armrest());
		mValues.put(COLUMNS[73], checkReport.getRf_armrest());
		mValues.put(COLUMNS[74], checkReport.getRl_armrest());
		mValues.put(COLUMNS[75], checkReport.getA_trim_plate());
		mValues.put(COLUMNS[76], checkReport.getB_trim_plate());
		mValues.put(COLUMNS[77], checkReport.getC_trim_plate());
		mValues.put(COLUMNS[78], checkReport.getTrunk());
		mValues.put(COLUMNS[79], checkReport.getRubber_strip());
		mValues.put(COLUMNS[80], checkReport.getVehicle_smell());
		mValues.put(COLUMNS[81], checkReport.getDashboard());
		mValues.put(COLUMNS[82], checkReport.getReading_lamp());
		mValues.put(COLUMNS[83], checkReport.getWiper());
		mValues.put(COLUMNS[84], checkReport.getWindow_lifter());
		mValues.put(COLUMNS[85], checkReport.getAir_condition());
		mValues.put(COLUMNS[86], checkReport.getSound_system());
		mValues.put(COLUMNS[87], checkReport.getHandbrake());
		mValues.put(COLUMNS[88], checkReport.getCentral_lock());
		mValues.put(COLUMNS[89], checkReport.getSkylight());
		mValues.put(COLUMNS[90], checkReport.getCentral_dispaly());
		mValues.put(COLUMNS[91], checkReport.getGps());
		mValues.put(COLUMNS[92], checkReport.getReverse_sensor());
		mValues.put(COLUMNS[93], checkReport.getM_electric_seat());
		mValues.put(COLUMNS[94], checkReport.getA_electric_seat());
		mValues.put(COLUMNS[95], checkReport.getDashboard_light());
		mValues.put(COLUMNS[96], checkReport.getAbs());
		mValues.put(COLUMNS[97], checkReport.getAir_bag());
		mValues.put(COLUMNS[98], checkReport.getSafty_belt());
		mValues.put(COLUMNS[99], checkReport.getJack());
		mValues.put(COLUMNS[100], checkReport.getFire_extinguiher());
		mValues.put(COLUMNS[101], checkReport.getMark_delta());
		mValues.put(COLUMNS[102], checkReport.getHigh_beams());
		mValues.put(COLUMNS[103], checkReport.getDipped_beams());
		mValues.put(COLUMNS[104], checkReport.getFog_light());
		mValues.put(COLUMNS[105], checkReport.getSteering_lamp());
		mValues.put(COLUMNS[106], checkReport.getTaillight());
		mValues.put(COLUMNS[107], checkReport.getBreak_lamp());
		mValues.put(COLUMNS[108], checkReport.getDriving_light());
		mValues.put(COLUMNS[109], checkReport.getTire_uniformity());
		mValues.put(COLUMNS[110], checkReport.getBreakpads_thickness());
		mValues.put(COLUMNS[111], checkReport.getTread());
		mValues.put(COLUMNS[112], checkReport.getSpare_tire());
		mValues.put(COLUMNS[113], checkReport.getVehicle_start());
		mValues.put(COLUMNS[114], checkReport.getEngine_staility());
		mValues.put(COLUMNS[115], checkReport.getIdle_state());
		mValues.put(COLUMNS[116], checkReport.getNeutral_state());
		mValues.put(COLUMNS[117], checkReport.getExhaust_color());
		mValues.put(COLUMNS[118], checkReport.getFault_gear());
		mValues.put(COLUMNS[119], checkReport.getTransaction_place());
		mValues.put(COLUMNS[120], checkReport.getSeller_jobs());
		mValues.put(COLUMNS[121], checkReport.getBuy_comment());
		mValues.put(COLUMNS[122], checkReport.getVehicle_advantage());
		mValues.put(COLUMNS[123], checkReport.getVehicle_usage());
		mValues.put(COLUMNS[124], checkReport.getVehicle_maintenance());
		mValues.put(COLUMNS[125], checkReport.getOil_consumption());
		mValues.put(COLUMNS[126], checkReport.getAdditional_configuration());
		mValues.put(COLUMNS[127], checkReport.getChecker_comment());
		mValues.put(COLUMNS[128], checkReport.getFinal_comment());
		mValues.put(COLUMNS[129], checkReport.getCreate_time());
		mValues.put(COLUMNS[130], checkReport.getOut_pics());
		mValues.put(COLUMNS[131], checkReport.getInner_pics());
		mValues.put(COLUMNS[132], checkReport.getDetail_pics());
		mValues.put(COLUMNS[133], checkReport.getDefect_pics());
		mValues.put(COLUMNS[134], checkReport.getTransfer_times());
		mValues.put(COLUMNS[135], checkReport.getVehicle_name());
		mValues.put(COLUMNS[136], checkReport.getVehicle_brand_name());
		mValues.put(COLUMNS[137], checkReport.getVehicle_series_name());
		mValues.put(COLUMNS[138], checkReport.getCheap_price());
		mValues.put(COLUMNS[139], checkReport.getView_registration());
		mValues.put(COLUMNS[140], checkReport.getSeller_name());
		mValues.put(COLUMNS[141], checkReport.getSeller_phone());
		mValues.put(COLUMNS[142], checkReport.getChecker_name());
		mValues.put(COLUMNS[143], checkReport.getEmissions_standard());
		mValues.put(COLUMNS[144], checkReport.getBackup_phone());
		mValues.put(COLUMNS[145], checkReport.getDrive_mode());
		mValues.put(COLUMNS[146], checkReport.getSeats_num());
		mValues.put(COLUMNS[147], checkReport.getTransfer_reason());
		mValues.put(COLUMNS[148], checkReport.getCar_story());
		mValues.put(COLUMNS[149], checkReport.getApp_version());
		mValues.put(COLUMNS[150], checkReport.getWater_tank());
		mValues.put(COLUMNS[151], checkReport.getMetal_tank());
		mValues.put(COLUMNS[152], checkReport.getHeadlight_frame());
		mValues.put(COLUMNS[153], checkReport.getFender_liner());
		mValues.put(COLUMNS[154], checkReport.getBack_floor());
		mValues.put(COLUMNS[155], checkReport.getBack_coaming());
		mValues.put(COLUMNS[156], checkReport.getEngine_sound());
		mValues.put(COLUMNS[157], checkReport.getEngine_spill());
		mValues.put(COLUMNS[158], checkReport.getMortgage());
		mValues.put(COLUMNS[159], checkReport.getInto_bj());
		mValues.put(COLUMNS[160], checkReport.getCompany_account());
		mValues.put(COLUMNS[161], checkReport.getOperation());
		mValues.put(COLUMNS[162], checkReport.getSuspected_agent());
		mValues.put(COLUMNS[163], checkReport.getEval_price());
		mValues.put(COLUMNS[164], checkReport.getEval_price_21());
		mValues.put(COLUMNS[165], checkReport.getShow_status());
		mValues.put(COLUMNS[166], checkReport.getSeller_price());
		mValues.put(COLUMNS[167], checkReport.getVin_code());
		mValues.put(COLUMNS[168], checkReport.getExclusive());
		mValues.put(COLUMNS[169], checkReport.getLiyang_brand_nane());
		mValues.put(COLUMNS[170], checkReport.getLiyang_series_name());
		mValues.put(COLUMNS[171], checkReport.getLiyang_model_name());
		mValues.put(COLUMNS[172], checkReport.getLiyang_model_id());
		mValues.put(COLUMNS[173], checkReport.getCheck_source());
		mValues.put(COLUMNS[174], checkReport.getComplete_check());
		mValues.put(COLUMNS[175], checkReport.getIs_channel());
		mValues.put(COLUMNS[176], checkReport.getAudio_url());
		mValues.put(COLUMNS[177], checkReport.getVideo_url());
		return mValues;
	}

	@Override
	protected BaseEntity getEntityFromCursor(Cursor mCursor) {
		CheckReportEntity checkReport = new CheckReportEntity();
		checkReport.setId(mCursor.getInt(0));
		checkReport.setCheck_appointment_id(mCursor.getInt(1));
		checkReport.setVehicle_source_id(mCursor.getInt(2));
		checkReport.setCheck_user_id(mCursor.getInt(3));
		checkReport.setCar_num(mCursor.getString(4));
		checkReport.setRegister_time(mCursor.getInt(5));
		checkReport.setAnual_valid_time(mCursor.getInt(6));
		checkReport.setTins_valid_time(mCursor.getInt(7));
		checkReport.setIns_valid_time(mCursor.getInt(8));
		checkReport.setTrasfer_record(mCursor.getInt(9));
		checkReport.setVehicle_brand_id(mCursor.getInt(10));
		checkReport.setVehicle_class_id(mCursor.getInt(11));
		checkReport.setVehicle_model_id(mCursor.getInt(12));
		checkReport.setGearbox(mCursor.getInt(13));
		checkReport.setEmissions(mCursor.getString(14));
		checkReport.setMiles(mCursor.getFloat(15));
		checkReport.setEngine_oil(mCursor.getInt(16));
		checkReport.setCylinder(mCursor.getInt(17));
		checkReport.setRadiator(mCursor.getInt(18));
		checkReport.setElectrode_pillar(mCursor.getInt(19));
		checkReport.setElectrolyte(mCursor.getInt(20));
		checkReport.setEngine_belt(mCursor.getInt(21));
		checkReport.setTubing(mCursor.getInt(22));
		checkReport.setWater_pipe(mCursor.getInt(23));
		checkReport.setHarness(mCursor.getInt(24));
		checkReport.setEngine_oil_location(mCursor.getInt(25));
		checkReport.setBreaker_oil(mCursor.getInt(26));
		checkReport.setSteering_oil(mCursor.getInt(27));
		checkReport.setEngine_hood(mCursor.getInt(28));
		checkReport.setRf_fender(mCursor.getInt(29));
		checkReport.setRf_door(mCursor.getInt(30));
		checkReport.setRr_door(mCursor.getInt(31));
		checkReport.setRr_fender(mCursor.getInt(32));
		checkReport.setDecklid(mCursor.getInt(33));
		checkReport.setLr_fender(mCursor.getInt(34));
		checkReport.setLr_door(mCursor.getInt(35));
		checkReport.setLf_door(mCursor.getInt(36));
		checkReport.setLf_fender(mCursor.getInt(37));
		checkReport.setVehicle_roof(mCursor.getInt(38));
		checkReport.setF_bumper(mCursor.getInt(39));
		checkReport.setR_bumper(mCursor.getInt(40));
		checkReport.setL_mirror(mCursor.getInt(41));
		checkReport.setR_mirror(mCursor.getInt(42));
		checkReport.setLf_headlight(mCursor.getInt(43));
		checkReport.setRf_headlight(mCursor.getInt(44));
		checkReport.setL_taillight(mCursor.getInt(45));
		checkReport.setR_taillight(mCursor.getInt(46));
		checkReport.setF_windshield(mCursor.getInt(47));
		checkReport.setR_windshield(mCursor.getInt(48));
		checkReport.setLf_glass(mCursor.getInt(49));
		checkReport.setLr_glass(mCursor.getInt(50));
		checkReport.setRf_glass(mCursor.getInt(51));
		checkReport.setRr_glass(mCursor.getInt(52));
		checkReport.setDashboard_platform(mCursor.getInt(53));
		checkReport.setCenter_control(mCursor.getInt(54));
		checkReport.setSteering_wheel(mCursor.getInt(55));
		checkReport.setGear_handler(mCursor.getInt(56));
		checkReport.setCigar_lighter(mCursor.getInt(57));
		checkReport.setGlove_box(mCursor.getInt(58));
		checkReport.setVehicle_roof_inner(mCursor.getInt(59));
		checkReport.setM_sunblind(mCursor.getInt(60));
		checkReport.setA_subblind(mCursor.getInt(61));
		checkReport.setRear_view(mCursor.getInt(62));
		checkReport.setDriver_seat(mCursor.getInt(63));
		checkReport.setCopilot_seat(mCursor.getInt(64));
		checkReport.setRear_seat(mCursor.getInt(65));
		checkReport.setCenter_armrest(mCursor.getInt(66));
		checkReport.setLf_trim_plate(mCursor.getInt(67));
		checkReport.setRf_trim_plate(mCursor.getInt(68));
		checkReport.setLr_trim_plate(mCursor.getInt(69));
		checkReport.setRr_trim_plate(mCursor.getInt(70));
		checkReport.setLf_armrest(mCursor.getInt(71));
		checkReport.setLr_armrest(mCursor.getInt(72));
		checkReport.setRf_armrest(mCursor.getInt(73));
		checkReport.setRl_armrest(mCursor.getInt(74));
		checkReport.setA_trim_plate(mCursor.getInt(75));
		checkReport.setB_trim_plate(mCursor.getInt(76));
		checkReport.setC_trim_plate(mCursor.getInt(77));
		checkReport.setTrunk(mCursor.getInt(78));
		checkReport.setRubber_strip(mCursor.getString(79));
		checkReport.setVehicle_smell(mCursor.getInt(80));
		checkReport.setDashboard(mCursor.getString(81));
		checkReport.setReading_lamp(mCursor.getString(82));
		checkReport.setWiper(mCursor.getString(83));
		checkReport.setWindow_lifter(mCursor.getString(84));
		checkReport.setAir_condition(mCursor.getString(85));
		checkReport.setSound_system(mCursor.getString(86));
		checkReport.setHandbrake(mCursor.getString(87));
		checkReport.setCentral_lock(mCursor.getString(88));
		checkReport.setSkylight(mCursor.getString(89));
		checkReport.setCentral_dispaly(mCursor.getString(90));
		checkReport.setGps(mCursor.getString(91));
		checkReport.setReverse_sensor(mCursor.getString(92));
		checkReport.setM_electric_seat(mCursor.getString(93));
		checkReport.setA_electric_seat(mCursor.getString(94));
		checkReport.setDashboard_light(mCursor.getString(95));
		checkReport.setAbs(mCursor.getString(96));
		checkReport.setAir_bag(mCursor.getString(97));
		checkReport.setSafty_belt(mCursor.getString(98));
		checkReport.setJack(mCursor.getString(99));
		checkReport.setFire_extinguiher(mCursor.getString(100));
		checkReport.setMark_delta(mCursor.getString(101));
		checkReport.setHigh_beams(mCursor.getString(102));
		checkReport.setDipped_beams(mCursor.getString(103));
		checkReport.setFog_light(mCursor.getString(104));
		checkReport.setSteering_lamp(mCursor.getString(105));
		checkReport.setTaillight(mCursor.getString(106));
		checkReport.setBreak_lamp(mCursor.getString(107));
		checkReport.setDriving_light(mCursor.getString(108));
		checkReport.setTire_uniformity(mCursor.getString(109));
		checkReport.setBreakpads_thickness(mCursor.getString(110));
		checkReport.setTread(mCursor.getString(111));
		checkReport.setSpare_tire(mCursor.getInt(112));
		checkReport.setVehicle_start(mCursor.getInt(113));
		checkReport.setEngine_staility(mCursor.getInt(114));
		checkReport.setIdle_state(mCursor.getInt(115));
		checkReport.setNeutral_state(mCursor.getInt(116));
		checkReport.setExhaust_color(mCursor.getInt(117));
		checkReport.setFault_gear(mCursor.getInt(118));
		checkReport.setTransaction_place(mCursor.getString(119));
		checkReport.setSeller_jobs(mCursor.getString(120));
		checkReport.setBuy_comment(mCursor.getString(121));
		checkReport.setVehicle_advantage(mCursor.getString(122));
		checkReport.setVehicle_usage(mCursor.getString(123));
		checkReport.setVehicle_maintenance(mCursor.getString(124));
		checkReport.setOil_consumption(mCursor.getString(125));
		checkReport.setAdditional_configuration(mCursor.getString(126));
		checkReport.setChecker_comment(mCursor.getString(127));
		checkReport.setFinal_comment(mCursor.getString(128));
		checkReport.setCreate_time(mCursor.getInt(129));
		checkReport.setOut_pics(mCursor.getString(130));
		checkReport.setInner_pics(mCursor.getString(131));
		checkReport.setDetail_pics(mCursor.getString(132));
		checkReport.setDefect_pics(mCursor.getString(133));
		checkReport.setTransfer_times(mCursor.getInt(134));
		checkReport.setVehicle_name(mCursor.getString(135));
		checkReport.setVehicle_brand_name(mCursor.getString(136));
		checkReport.setVehicle_series_name(mCursor.getString(137));
		checkReport.setCheap_price(mCursor.getFloat(138));
		checkReport.setView_registration(mCursor.getInt(139));
		checkReport.setSeller_name(mCursor.getString(140));
		checkReport.setSeller_phone(mCursor.getString(141));
		checkReport.setChecker_name(mCursor.getString(142));
		checkReport.setEmissions_standard(mCursor.getInt(143));
		checkReport.setBackup_phone(mCursor.getString(144));
		checkReport.setDrive_mode(mCursor.getInt(145));
		checkReport.setSeats_num(mCursor.getInt(146));
		checkReport.setTransfer_reason(mCursor.getString(147));
		checkReport.setCar_story(mCursor.getString(148));
		checkReport.setApp_version(mCursor.getString(149));
		checkReport.setWater_tank(mCursor.getInt(150));
		checkReport.setMetal_tank(mCursor.getInt(151));
		checkReport.setHeadlight_frame(mCursor.getInt(152));
		checkReport.setFender_liner(mCursor.getInt(153));
		checkReport.setBack_floor(mCursor.getInt(154));
		checkReport.setBack_coaming(mCursor.getInt(155));
		checkReport.setEngine_sound(mCursor.getInt(156));
		checkReport.setEngine_spill(mCursor.getInt(157));
		checkReport.setMortgage(mCursor.getInt(158));
		checkReport.setInto_bj(mCursor.getInt(159));
		checkReport.setCompany_account(mCursor.getInt(160));
		checkReport.setOperation(mCursor.getInt(161));
		checkReport.setSuspected_agent(mCursor.getInt(162));
		checkReport.setEval_price(mCursor.getFloat(163));
		checkReport.setEval_price_21(mCursor.getFloat(164));
		checkReport.setShow_status(mCursor.getInt(165));
		checkReport.setSeller_price(mCursor.getFloat(166));
		checkReport.setVin_code(mCursor.getString(167));
		checkReport.setExclusive(mCursor.getInt(168));
		checkReport.setLiyang_brand_nane(mCursor.getString(169));
		checkReport.setLiyang_series_name(mCursor.getString(170));
		checkReport.setLiyang_model_name(mCursor.getString(171));
		checkReport.setLiyang_model_id(mCursor.getString(172));
		checkReport.setCheck_source(mCursor.getString(173));
		checkReport.setComplete_check(mCursor.getInt(174));
		checkReport.setIs_channel(mCursor.getInt(175));
		checkReport.setAudio_url(mCursor.getString(176));
		checkReport.setVideo_url(mCursor.getString(177));
		return checkReport;
	}

	@Override
	protected String[] getColumns() {
		return COLUMNS;
	}

	@Override
	protected String getDefaultOrderby() {
		return DEFAULT_ORDER_BY;
	}

	@Override
	public CheckReportEntity get(int id) {
		return (CheckReportEntity) super.get(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CheckReportEntity> get(int offset, int limit) {
		return (List<CheckReportEntity>) super.get(offset, limit);
	}

	public CheckReportEntity getByTaskId(int taskId) {
		return (CheckReportEntity) get("check_appointment_id=" + taskId, null, null, null);
	}

	public boolean existsByTask(int taskId) {
		int count = getCount("check_appointment_id=" + taskId);
		return count > 0;
	}

	/**
	 * 根据报告ID删除本地报告
	 *
	 * @param reportId 报告ID
	 * @return
	 */
	public void deleteByReportId(int reportId) {
		mDb.execSQL("delete from check_report where id =" + reportId);
	}
}
