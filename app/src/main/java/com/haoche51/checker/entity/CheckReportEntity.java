package com.haoche51.checker.entity;

import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.util.ControlDisplayUtil;
import com.haoche51.checker.util.HCLogUtil;

public class CheckReportEntity extends BaseEntity {
    private int id = 0;
    private int check_appointment_id = 0;
    private int vehicle_source_id = 0;
    private int check_user_id = 0;
    private String car_num = "";
    private int register_time = 0;
    private int anual_valid_time = 0;
    private int tins_valid_time = 0;
    private int ins_valid_time = 0;
    private int trasfer_record = 0;
    private int vehicle_brand_id = 0;
    private int vehicle_class_id = 0;
    private int vehicle_model_id = 0;
    private int gearbox = 0;
    private String emissions = "";
    private float miles;
    private int engine_oil = 0;
    private int cylinder = 0;
    private int radiator = 0;
    private int electrode_pillar = 0;
    private int electrolyte = 0;
    private int engine_belt = 0;
    private int tubing = 0;
    private int water_pipe = 0;
    private int harness = 0;
    private int engine_oil_location = 0;
    private int breaker_oil = 0;
    private int steering_oil = 0;
    private int engine_hood = 0;
    private int rf_fender = 0;
    private int rf_door = 0;
    private int rr_door = 0;
    private int rr_fender = 0;
    private int decklid = 0;
    private int lr_fender = 0;
    private int lr_door = 0;
    private int lf_door = 0;
    private int lf_fender = 0;
    private int vehicle_roof = 0;
    private int f_bumper = 0;
    private int r_bumper = 0;
    private int l_mirror = 0;
    private int r_mirror = 0;
    private int lf_headlight = 0;
    private int rf_headlight = 0;
    private int l_taillight = 0;
    private int r_taillight = 0;
    private int f_windshield = 0;
    private int r_windshield = 0;
    private int lf_glass = 0;
    private int lr_glass = 0;
    private int rf_glass = 0;
    private int rr_glass = 0;
    private int dashboard_platform = 0;
    private int center_control = 0;
    private int steering_wheel = 0;
    private int gear_handler = 0;
    private int cigar_lighter = 0;
    private int glove_box = 0;
    private int vehicle_roof_inner = 0; //车顶棚
    private int m_sunblind = 0;
    private int a_subblind = 0;
    private int rear_view = 0;
    private int driver_seat = 0;
    private int copilot_seat = 0;
    private int rear_seat = 0;
    private int center_armrest = 0;
    private int lf_trim_plate = 0;
    private int rf_trim_plate = 0;
    private int lr_trim_plate = 0;
    private int rr_trim_plate = 0;
    private int lf_armrest = 0;
    private int lr_armrest = 0;
    private int rf_armrest = 0;
    private int rl_armrest = 0;
    private int a_trim_plate = 0;
    private int b_trim_plate = 0;
    private int c_trim_plate = 0;
    private int trunk = 0;
    private String rubber_strip = "";
    private int vehicle_smell = 0;
    private String dashboard = "";
    private String reading_lamp = "";
    private String wiper = "";
    private String window_lifter = "";
    private String air_condition = "";
    private String sound_system = "";
    private String handbrake = "";
    private String central_lock = "";
    private String skylight = "";
    private String central_dispaly = "";
    private String gps = "";
    private String reverse_sensor = "";
    private String m_electric_seat = "";
    private String a_electric_seat = "";
    private String dashboard_light = "";
    private String abs = "";
    private String air_bag = "";
    private String safty_belt = "";
    private String jack = "";
    private String fire_extinguiher = "";
    private String mark_delta = "";
    private String high_beams = "";
    private String dipped_beams = "";
    private String fog_light = "";
    private String steering_lamp = "";
    private String taillight = "";
    private String break_lamp = "";
    private String driving_light = "";
    private String tire_uniformity = "";
    private String breakpads_thickness = "";
    private String tread = "";
    private int spare_tire = 0;
    private int vehicle_start = 0;
    private int engine_staility = 0;
    private int idle_state = 0;
    private int neutral_state = 0;
    private int exhaust_color = 0;
    private int fault_gear = 0;
    private String transaction_place = "";
    private String seller_jobs = "";
    private String buy_comment = "";
    private String vehicle_advantage = "";
    private String vehicle_usage = "";
    private String vehicle_maintenance = "";
    private String oil_consumption = "";
    private String additional_configuration = "";
    private String checker_comment = "";
    private String final_comment = "";
    private int create_time = 0;
    private String out_pics = "";
    private String inner_pics = "";
    private String detail_pics = "";
    private String defect_pics = "";
    private int transfer_times = 0;
    private String vehicle_name = "";
    private String vehicle_brand_name = "";
    private String vehicle_series_name = "";
    private float seller_price = 0;//报价
    private float cheap_price = 0;//底价
    private float eval_price;//个人成交价
    private float eval_price_21;//21天成交价
    private int show_status;//价格过高，暂不上线
    private int view_registration = -1; // 0 没看到 1 看到。 -1 为初始值，第一次进入评估报告
    private String seller_name = "";
    private String checker_name = "";
    private String seller_phone = "";
    private int emissions_standard = 0;
    private String backup_phone = "";
    private int drive_mode = 0;  //1 四驱  2前驱  3后驱
    private int seats_num;
    private String transfer_reason = ""; //为什么有过户记录
    private String car_story = ""; //您的爱车有没有什么故事
    private String app_version; //app版本号

    //1是    0否   默认为0
    private int water_tank = 0; //塑料水箱框架整体更换
    private int metal_tank = 0; //金属水箱框架轻微缺陷
    private int headlight_frame = 0; //大灯框架轻微缺陷
    private int fender_liner = 0; //翼子板内衬轻微缺陷
    private int back_floor = 0; //后底板轻微缺陷
    private int back_coaming = 0; //后围板轻微缺陷

    private int engine_sound = 0; //发动机异响
    private int engine_spill = 0; //发动机轻微渗油

    private int mortgage = 0; //有抵押
    private int into_bj = 0; //外转京
    private int company_account = 0; //公户
    private int operation = 0; //营运车辆

    private String vin_code;//根据VIN 码获取车鉴定报告
    private int suspected_agent = 0; //疑似车商
    private int exclusive = 0;//独家车源
    private String liyang_model_id = "";//liyang ID
    private String liyang_brand_nane = "";//liyang brand
    private String liyang_series_name = "";//liyang series
    private String liyang_model_name = "";//liyang model
    private String check_source; //0普通任务 1转单任务 2帮检任务
    private int complete_check; //完成检测 用于本地标识
    private int is_channel;//是否是渠寄车源 1是 0否
    private String audio_url;//音频
    private String video_url;//视频

    public static CheckReportEntity createReport(CheckTaskEntity mTask) {
        if (mTask == null) return null;
        int taskId = mTask.getId();
        if (!CheckReportDAO.getInstance().existsByTask(taskId)) {
            CheckReportEntity mReport = new CheckReportEntity();
            mReport.setVin_code(mTask.getVin_code());
            mReport.setVehicle_model_id(mTask.getModel_id());
            mReport.setVehicle_name(mTask.getVehicle_name());
            mReport.setCheck_source(ControlDisplayUtil.getInstance().getCheckSourceText(mTask.getCheck_source()));
            mReport.setVehicle_brand_id(mTask.getBrand_id());
            mReport.setVehicle_class_id(mTask.getSeries_id());
            mReport.setCreate_time((int) (System.currentTimeMillis() / 1000));
            mReport.setCheck_appointment_id(mTask.getId());
            mReport.setCheck_user_id(mTask.getCheck_user_id());
            mReport.setChecker_name(mTask.getCheck_user_name());
            mReport.setSuspected_agent(mTask.getSuspected_agent());
            int reportId = (int) CheckReportDAO.getInstance().insert(mReport);
            if (reportId > 0) {
                mReport.setId(reportId);
                return mReport;
            }
        } else {
            return CheckReportDAO.getInstance().getByTaskId(taskId);
        }
        return null;
    }

    public String getCar_num() {
        return car_num;
    }

    public void setCar_num(String car_num) {
        this.car_num = car_num;
    }

    public int getRegister_time() {
        return register_time;
    }

    public void setRegister_time(int register_time) {
        this.register_time = register_time;
    }

    public int getAnual_valid_time() {
        return anual_valid_time;
    }

    public void setAnual_valid_time(int anual_valid_time) {
        this.anual_valid_time = anual_valid_time;
    }

    public int getTins_valid_time() {
        return tins_valid_time;
    }

    public void setTins_valid_time(int tins_valid_time) {
        this.tins_valid_time = tins_valid_time;
    }

    public int getIns_valid_time() {
        return ins_valid_time;
    }

    public void setIns_valid_time(int ins_valid_time) {
        this.ins_valid_time = ins_valid_time;
    }

    public int getTrasfer_record() {
        return trasfer_record;
    }

    public void setTrasfer_record(int trasfer_record) {
        this.trasfer_record = trasfer_record;
    }

    public int getVehicle_brand_id() {
        return vehicle_brand_id;
    }

    public void setVehicle_brand_id(int vehicle_brand_id) {
        this.vehicle_brand_id = vehicle_brand_id;
    }

    public int getVehicle_class_id() {
        return vehicle_class_id;
    }

    public void setVehicle_class_id(int vehicle_class_id) {
        this.vehicle_class_id = vehicle_class_id;
    }

    public int getVehicle_model_id() {
        return vehicle_model_id;
    }

    public void setVehicle_model_id(int vehicle_model_id) {
        this.vehicle_model_id = vehicle_model_id;
    }

    public int getGearbox() {
        return gearbox;
    }

    public void setGearbox(int gearbox) {
        this.gearbox = gearbox;
    }

    public String getEmissions() {
        return emissions;
    }

    public void setEmissions(String emissions) {
        this.emissions = emissions;
    }

    public float getMiles() {
        return miles;
    }

    public void setMiles(float miles) {
        this.miles = miles;
    }

    public int getEngine_oil() {
        return engine_oil;
    }

    public void setEngine_oil(int engine_oil) {
        this.engine_oil = engine_oil;
    }

    public int getCylinder() {
        return cylinder;
    }

    public void setCylinder(int cylinder) {
        this.cylinder = cylinder;
    }

    public int getRadiator() {
        return radiator;
    }

    public void setRadiator(int radiator) {
        this.radiator = radiator;
    }

    public int getElectrode_pillar() {
        return electrode_pillar;
    }

    public void setElectrode_pillar(int electrode_pillar) {
        this.electrode_pillar = electrode_pillar;
    }

    public int getElectrolyte() {
        return electrolyte;
    }

    public void setElectrolyte(int electrolyte) {
        this.electrolyte = electrolyte;
    }

    public int getEngine_belt() {
        return engine_belt;
    }

    public void setEngine_belt(int engine_belt) {
        this.engine_belt = engine_belt;
    }

    public int getTubing() {
        return tubing;
    }

    public void setTubing(int tubing) {
        this.tubing = tubing;
    }

    public int getWater_pipe() {
        return water_pipe;
    }

    public void setWater_pipe(int water_pipe) {
        this.water_pipe = water_pipe;
    }

    public int getHarness() {
        return harness;
    }

    public void setHarness(int harness) {
        this.harness = harness;
    }

    public int getEngine_oil_location() {
        return engine_oil_location;
    }

    public void setEngine_oil_location(int engine_oil_location) {
        this.engine_oil_location = engine_oil_location;
    }

    public int getBreaker_oil() {
        return breaker_oil;
    }

    public void setBreaker_oil(int breaker_oil) {
        this.breaker_oil = breaker_oil;
    }

    public int getSteering_oil() {
        return steering_oil;
    }

    public void setSteering_oil(int steering_oil) {
        this.steering_oil = steering_oil;
    }

    public int getEngine_hood() {
        return engine_hood;
    }

    public void setEngine_hood(int engine_hood) {
        this.engine_hood = engine_hood;
    }

    public int getRf_fender() {
        return rf_fender;
    }

    public void setRf_fender(int rf_fender) {
        this.rf_fender = rf_fender;
    }

    public int getRf_door() {
        return rf_door;
    }

    public void setRf_door(int rf_door) {
        this.rf_door = rf_door;
    }

    public int getRr_door() {
        return rr_door;
    }

    public void setRr_door(int rr_door) {
        this.rr_door = rr_door;
    }

    public int getRr_fender() {
        return rr_fender;
    }

    public void setRr_fender(int rr_fender) {
        this.rr_fender = rr_fender;
    }

    public int getDecklid() {
        return decklid;
    }

    public void setDecklid(int decklid) {
        this.decklid = decklid;
    }

    public int getLr_fender() {
        return lr_fender;
    }

    public void setLr_fender(int lr_fender) {
        this.lr_fender = lr_fender;
    }

    public int getLr_door() {
        return lr_door;
    }

    public void setLr_door(int lr_door) {
        this.lr_door = lr_door;
    }

    public int getLf_door() {
        return lf_door;
    }

    public void setLf_door(int lf_door) {
        this.lf_door = lf_door;
    }

    public int getLf_fender() {
        return lf_fender;
    }

    public void setLf_fender(int lf_fender) {
        this.lf_fender = lf_fender;
    }

    public int getVehicle_roof() {
        return vehicle_roof;
    }

    public void setVehicle_roof(int vehicle_roof) {
        this.vehicle_roof = vehicle_roof;
    }

    public int getF_bumper() {
        return f_bumper;
    }

    public void setF_bumper(int f_bumper) {
        this.f_bumper = f_bumper;
    }

    public int getR_bumper() {
        return r_bumper;
    }

    public void setR_bumper(int r_bumper) {
        this.r_bumper = r_bumper;
    }

    public int getL_mirror() {
        return l_mirror;
    }

    public void setL_mirror(int l_mirror) {
        this.l_mirror = l_mirror;
    }

    public int getR_mirror() {
        return r_mirror;
    }

    public void setR_mirror(int r_mirror) {
        this.r_mirror = r_mirror;
    }

    public int getLf_headlight() {
        return lf_headlight;
    }

    public void setLf_headlight(int lf_headlight) {
        this.lf_headlight = lf_headlight;
    }

    public int getRf_headlight() {
        return rf_headlight;
    }

    public void setRf_headlight(int rf_headlight) {
        this.rf_headlight = rf_headlight;
    }

    public int getL_taillight() {
        return l_taillight;
    }

    public void setL_taillight(int l_taillight) {
        this.l_taillight = l_taillight;
    }

    public int getR_taillight() {
        return r_taillight;
    }

    public void setR_taillight(int r_taillight) {
        this.r_taillight = r_taillight;
    }

    public int getF_windshield() {
        return f_windshield;
    }

    public void setF_windshield(int f_windshield) {
        this.f_windshield = f_windshield;
    }

    public int getR_windshield() {
        return r_windshield;
    }

    public void setR_windshield(int r_windshield) {
        this.r_windshield = r_windshield;
    }

    public int getLf_glass() {
        return lf_glass;
    }

    public void setLf_glass(int lf_glass) {
        this.lf_glass = lf_glass;
    }

    public int getLr_glass() {
        return lr_glass;
    }

    public void setLr_glass(int lr_glass) {
        this.lr_glass = lr_glass;
    }

    public int getRf_glass() {
        return rf_glass;
    }

    public void setRf_glass(int rf_glass) {
        this.rf_glass = rf_glass;
    }

    public int getRr_glass() {
        return rr_glass;
    }

    public void setRr_glass(int rr_glass) {
        this.rr_glass = rr_glass;
    }

    public int getDashboard_platform() {
        return dashboard_platform;
    }

    public void setDashboard_platform(int dashboard_platform) {
        this.dashboard_platform = dashboard_platform;
    }

    public int getCenter_control() {
        return center_control;
    }

    public void setCenter_control(int center_control) {
        this.center_control = center_control;
    }

    public int getSteering_wheel() {
        return steering_wheel;
    }

    public void setSteering_wheel(int steering_wheel) {
        this.steering_wheel = steering_wheel;
    }

    public int getGear_handler() {
        return gear_handler;
    }

    public void setGear_handler(int gear_handler) {
        this.gear_handler = gear_handler;
    }

    public int getCigar_lighter() {
        return cigar_lighter;
    }

    public void setCigar_lighter(int cigar_lighter) {
        this.cigar_lighter = cigar_lighter;
    }

    public int getGlove_box() {
        return glove_box;
    }

    public void setGlove_box(int glove_box) {
        this.glove_box = glove_box;
    }

    public int getVehicle_roof_inner() {
        return vehicle_roof_inner;
    }

    public void setVehicle_roof_inner(int vehicle_roof_inner) {
        this.vehicle_roof_inner = vehicle_roof_inner;
    }

    public int getM_sunblind() {
        return m_sunblind;
    }

    public void setM_sunblind(int m_sunblind) {
        this.m_sunblind = m_sunblind;
    }

    public int getA_subblind() {
        return a_subblind;
    }

    public void setA_subblind(int a_subblind) {
        this.a_subblind = a_subblind;
    }

    public int getRear_view() {
        return rear_view;
    }

    public void setRear_view(int rear_view) {
        this.rear_view = rear_view;
    }

    public int getDriver_seat() {
        return driver_seat;
    }

    public void setDriver_seat(int driver_seat) {
        this.driver_seat = driver_seat;
    }

    public int getCopilot_seat() {
        return copilot_seat;
    }

    public void setCopilot_seat(int copilot_seat) {
        this.copilot_seat = copilot_seat;
    }

    public int getRear_seat() {
        return rear_seat;
    }

    public void setRear_seat(int rear_seat) {
        this.rear_seat = rear_seat;
    }

    public int getCenter_armrest() {
        return center_armrest;
    }

    public void setCenter_armrest(int center_armrest) {
        this.center_armrest = center_armrest;
    }

    public int getLf_trim_plate() {
        return lf_trim_plate;
    }

    public void setLf_trim_plate(int lf_trim_plate) {
        this.lf_trim_plate = lf_trim_plate;
    }

    public int getRf_trim_plate() {
        return rf_trim_plate;
    }

    public void setRf_trim_plate(int rf_trim_plate) {
        this.rf_trim_plate = rf_trim_plate;
    }

    public int getLr_trim_plate() {
        return lr_trim_plate;
    }

    public void setLr_trim_plate(int lr_trim_plate) {
        this.lr_trim_plate = lr_trim_plate;
    }

    public int getRr_trim_plate() {
        return rr_trim_plate;
    }

    public void setRr_trim_plate(int rr_trim_plate) {
        this.rr_trim_plate = rr_trim_plate;
    }

    public int getLf_armrest() {
        return lf_armrest;
    }

    public void setLf_armrest(int lf_armrest) {
        this.lf_armrest = lf_armrest;
    }

    public int getLr_armrest() {
        return lr_armrest;
    }

    public void setLr_armrest(int lr_armrest) {
        this.lr_armrest = lr_armrest;
    }

    public int getRf_armrest() {
        return rf_armrest;
    }

    public void setRf_armrest(int rf_armrest) {
        this.rf_armrest = rf_armrest;
    }

    public int getRl_armrest() {
        return rl_armrest;
    }

    public void setRl_armrest(int rl_armrest) {
        this.rl_armrest = rl_armrest;
    }

    public int getA_trim_plate() {
        return a_trim_plate;
    }

    public void setA_trim_plate(int a_trim_plate) {
        this.a_trim_plate = a_trim_plate;
    }

    public int getB_trim_plate() {
        return b_trim_plate;
    }

    public void setB_trim_plate(int b_trim_plate) {
        this.b_trim_plate = b_trim_plate;
    }

    public int getC_trim_plate() {
        return c_trim_plate;
    }

    public void setC_trim_plate(int c_trim_plate) {
        this.c_trim_plate = c_trim_plate;
    }

    public int getTrunk() {
        return trunk;
    }

    public void setTrunk(int trunk) {
        this.trunk = trunk;
    }

    public String getRubber_strip() {
        return rubber_strip;
    }

    public void setRubber_strip(String rubber_strip) {
        this.rubber_strip = rubber_strip;
    }

    public int getVehicle_smell() {
        return vehicle_smell;
    }

    public void setVehicle_smell(int vehicle_smell) {
        this.vehicle_smell = vehicle_smell;
    }

    public String getDashboard() {
        return dashboard;
    }

    public void setDashboard(String dashboard) {
        this.dashboard = dashboard;
    }

    public String getReading_lamp() {
        return reading_lamp;
    }

    public void setReading_lamp(String reading_lamp) {
        this.reading_lamp = reading_lamp;
    }

    public String getWiper() {
        return wiper;
    }

    public void setWiper(String wiper) {
        this.wiper = wiper;
    }

    public String getWindow_lifter() {
        return window_lifter;
    }

    public void setWindow_lifter(String window_lifter) {
        this.window_lifter = window_lifter;
    }

    public String getAir_condition() {
        return air_condition;
    }

    public void setAir_condition(String air_condition) {
        this.air_condition = air_condition;
    }

    public String getSound_system() {
        return sound_system;
    }

    public void setSound_system(String sound_system) {
        this.sound_system = sound_system;
    }

    public String getHandbrake() {
        return handbrake;
    }

    public void setHandbrake(String handbrake) {
        this.handbrake = handbrake;
    }

    public String getCentral_lock() {
        return central_lock;
    }

    public void setCentral_lock(String central_lock) {
        this.central_lock = central_lock;
    }

    public String getSkylight() {
        return skylight;
    }

    public void setSkylight(String skylight) {
        this.skylight = skylight;
    }

    public String getCentral_dispaly() {
        return central_dispaly;
    }

    public void setCentral_dispaly(String central_dispaly) {
        this.central_dispaly = central_dispaly;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getReverse_sensor() {
        return reverse_sensor;
    }

    public void setReverse_sensor(String reverse_sensor) {
        this.reverse_sensor = reverse_sensor;
    }

    public String getM_electric_seat() {
        return m_electric_seat;
    }

    public void setM_electric_seat(String m_electric_seat) {
        this.m_electric_seat = m_electric_seat;
    }

    public String getA_electric_seat() {
        return a_electric_seat;
    }

    public void setA_electric_seat(String a_electric_seat) {
        this.a_electric_seat = a_electric_seat;
    }

    public String getDashboard_light() {
        return dashboard_light;
    }

    public void setDashboard_light(String dashboard_light) {
        this.dashboard_light = dashboard_light;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public String getAir_bag() {
        return air_bag;
    }

    public void setAir_bag(String air_bag) {
        this.air_bag = air_bag;
    }

    public String getSafty_belt() {
        return safty_belt;
    }

    public void setSafty_belt(String safty_belt) {
        this.safty_belt = safty_belt;
    }

    public String getJack() {
        return jack;
    }

    public void setJack(String jack) {
        this.jack = jack;
    }

    public String getFire_extinguiher() {
        return fire_extinguiher;
    }

    public void setFire_extinguiher(String fire_extinguiher) {
        this.fire_extinguiher = fire_extinguiher;
    }

    public String getMark_delta() {
        return mark_delta;
    }

    public void setMark_delta(String mark_delta) {
        this.mark_delta = mark_delta;
    }

    public String getHigh_beams() {
        return high_beams;
    }

    public void setHigh_beams(String high_beams) {
        this.high_beams = high_beams;
    }

    public String getDipped_beams() {
        return dipped_beams;
    }

    public void setDipped_beams(String dipped_beams) {
        this.dipped_beams = dipped_beams;
    }

    public String getFog_light() {
        return fog_light;
    }

    public void setFog_light(String fog_light) {
        this.fog_light = fog_light;
    }

    public String getSteering_lamp() {
        return steering_lamp;
    }

    public void setSteering_lamp(String steering_lamp) {
        this.steering_lamp = steering_lamp;
    }

    public String getTaillight() {
        return taillight;
    }

    public void setTaillight(String taillight) {
        this.taillight = taillight;
    }

    public String getBreak_lamp() {
        return break_lamp;
    }

    public void setBreak_lamp(String break_lamp) {
        this.break_lamp = break_lamp;
    }

    public String getDriving_light() {
        return driving_light;
    }

    public void setDriving_light(String driving_light) {
        this.driving_light = driving_light;
    }

    public String getTire_uniformity() {
        return tire_uniformity;
    }

    public void setTire_uniformity(String tire_uniformity) {
        this.tire_uniformity = tire_uniformity;
    }

    public String getBreakpads_thickness() {
        return breakpads_thickness;
    }

    public void setBreakpads_thickness(String breakpads_thickness) {
        this.breakpads_thickness = breakpads_thickness;
    }

    public String getTread() {
        return tread;
    }

    public void setTread(String tread) {
        this.tread = tread;
    }

    public int getSpare_tire() {
        return spare_tire;
    }

    public void setSpare_tire(int spare_tire) {
        this.spare_tire = spare_tire;
    }

    public int getVehicle_start() {
        return vehicle_start;
    }

    public void setVehicle_start(int vehicle_start) {
        this.vehicle_start = vehicle_start;
    }

    public int getEngine_staility() {
        return engine_staility;
    }

    public void setEngine_staility(int engine_staility) {
        this.engine_staility = engine_staility;
    }

    public int getIdle_state() {
        return idle_state;
    }

    public void setIdle_state(int idle_state) {
        this.idle_state = idle_state;
    }

    public int getNeutral_state() {
        return neutral_state;
    }

    public void setNeutral_state(int neutral_state) {
        this.neutral_state = neutral_state;
    }

    public int getExhaust_color() {
        return exhaust_color;
    }

    public void setExhaust_color(int exhaust_color) {
        this.exhaust_color = exhaust_color;
    }

    public int getFault_gear() {
        return fault_gear;
    }

    public void setFault_gear(int fault_gear) {
        this.fault_gear = fault_gear;
    }

    public String getTransaction_place() {
        return transaction_place;
    }

    public void setTransaction_place(String transaction_place) {
        this.transaction_place = transaction_place;
    }

    public String getSeller_jobs() {
        return seller_jobs;
    }

    public void setSeller_jobs(String seller_jobs) {
        this.seller_jobs = seller_jobs;
    }

    public String getBuy_comment() {
        return buy_comment;
    }

    public void setBuy_comment(String buy_comment) {
        this.buy_comment = buy_comment;
    }

    public String getVehicle_advantage() {
        return vehicle_advantage;
    }

    public void setVehicle_advantage(String vehicle_advantage) {
        this.vehicle_advantage = vehicle_advantage;
    }

    public String getVehicle_usage() {
        return vehicle_usage;
    }

    public void setVehicle_usage(String vehicle_usage) {
        this.vehicle_usage = vehicle_usage;
    }

    public String getVehicle_maintenance() {
        return vehicle_maintenance;
    }

    public void setVehicle_maintenance(String vehicle_maintenance) {
        this.vehicle_maintenance = vehicle_maintenance;
    }

    public String getOil_consumption() {
        return oil_consumption;
    }

    public void setOil_consumption(String oil_consumption) {
        this.oil_consumption = oil_consumption;
    }

    public String getAdditional_configuration() {
        return additional_configuration;
    }

    public void setAdditional_configuration(String additional_configuration) {
        this.additional_configuration = additional_configuration;
    }

    public String getChecker_comment() {
        return checker_comment;
    }

    public void setChecker_comment(String checker_comment) {
        this.checker_comment = checker_comment;
    }

    public String getFinal_comment() {
        return final_comment;
    }

    public void setFinal_comment(String final_comment) {
        this.final_comment = final_comment;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getOut_pics() {
        return out_pics;
    }

    public void setOut_pics(String out_pics) {
        this.out_pics = out_pics;
    }

    public String getInner_pics() {
        return inner_pics;
    }

    public void setInner_pics(String inner_pics) {
        this.inner_pics = inner_pics;
    }

    public String getDetail_pics() {
        return detail_pics;
    }

    public void setDetail_pics(String detail_pics) {
        this.detail_pics = detail_pics;
    }

    public String getDefect_pics() {
        return defect_pics;
    }

    public void setDefect_pics(String defect_pics) {
        this.defect_pics = defect_pics;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCheck_appointment_id() {
        return check_appointment_id;
    }

    public void setCheck_appointment_id(int check_appointment_id) {
        this.check_appointment_id = check_appointment_id;
    }

    public int getVehicle_source_id() {
        return vehicle_source_id;
    }

    public void setVehicle_source_id(int vehicle_source_id) {
        this.vehicle_source_id = vehicle_source_id;
    }

    public int getTransfer_times() {
        return transfer_times;
    }

    public void setTransfer_times(int transfer_times) {
        this.transfer_times = transfer_times;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVehicle_brand_name() {
        return vehicle_brand_name;
    }

    public void setVehicle_brand_name(String vehicle_brand_name) {
        this.vehicle_brand_name = vehicle_brand_name;
    }

    public String getVehicle_series_name() {
        return vehicle_series_name;
    }

    public void setVehicle_series_name(String vehicle_series_name) {
        this.vehicle_series_name = vehicle_series_name;
    }

    public int getView_registration() {
        return view_registration;
    }

    public void setView_registration(int view_registration) {
        this.view_registration = view_registration;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getChecker_name() {
        return checker_name;
    }

    public void setChecker_name(String checker_name) {
        this.checker_name = checker_name;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public void setSeller_phone(String seller_phone) {
        this.seller_phone = seller_phone;
    }

    public int getEmissions_standard() {
        return emissions_standard;
    }

    public void setEmissions_standard(int emissions_standard) {
        this.emissions_standard = emissions_standard;
    }

    public String getBackup_phone() {
        return backup_phone;
    }

    public void setBackup_phone(String backup_phone) {
        this.backup_phone = backup_phone;
    }

    public int getDrive_mode() {
        return drive_mode;
    }

    public void setDrive_mode(int drive_mode) {
        this.drive_mode = drive_mode;
    }

    public int getSeats_num() {
        return seats_num;
    }

    public void setSeats_num(int seats_num) {
        this.seats_num = seats_num;
    }

    public String getTransfer_reason() {
        return transfer_reason;
    }

    public void setTransfer_reason(String transfer_reason) {
        this.transfer_reason = transfer_reason;
    }

    public String getCar_story() {
        return car_story;
    }

    public void setCar_story(String car_story) {
        this.car_story = car_story;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public int getWater_tank() {
        return water_tank;
    }

    public void setWater_tank(int water_tank) {
        this.water_tank = water_tank;
    }

    public int getMetal_tank() {
        return metal_tank;
    }

    public void setMetal_tank(int metal_tank) {
        this.metal_tank = metal_tank;
    }

    public int getHeadlight_frame() {
        return headlight_frame;
    }

    public void setHeadlight_frame(int headlight_frame) {
        this.headlight_frame = headlight_frame;
    }

    public int getFender_liner() {
        return fender_liner;
    }

    public void setFender_liner(int fender_liner) {
        this.fender_liner = fender_liner;
    }

    public int getBack_floor() {
        return back_floor;
    }

    public void setBack_floor(int back_floor) {
        this.back_floor = back_floor;
    }

    public int getBack_coaming() {
        return back_coaming;
    }

    public void setBack_coaming(int back_coaming) {
        this.back_coaming = back_coaming;
    }

    public int getEngine_sound() {
        return engine_sound;
    }

    public void setEngine_sound(int engine_sound) {
        this.engine_sound = engine_sound;
    }

    public int getEngine_spill() {
        return engine_spill;
    }

    public void setEngine_spill(int engine_spill) {
        this.engine_spill = engine_spill;
    }

    public int getMortgage() {
        return mortgage;
    }

    public void setMortgage(int mortgage) {
        this.mortgage = mortgage;
    }

    public int getInto_bj() {
        return into_bj;
    }

    public void setInto_bj(int into_bj) {
        this.into_bj = into_bj;
    }

    public int getCompany_account() {
        return company_account;
    }

    public void setCompany_account(int company_account) {
        this.company_account = company_account;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getSuspected_agent() {
        return suspected_agent;
    }

    public void setSuspected_agent(int suspected_agent) {
        this.suspected_agent = suspected_agent;
    }

    public int getCheck_user_id() {
        return check_user_id;
    }

    public void setCheck_user_id(int check_user_id) {
        this.check_user_id = check_user_id;
    }

    public int getShow_status() {
        return show_status;
    }

    public void setShow_status(int show_status) {
        this.show_status = show_status;
    }

    public float getEval_price() {
        return eval_price;
    }

    public void setEval_price(float eval_price) {
        this.eval_price = eval_price;
    }

    public float getEval_price_21() {
        return eval_price_21;
    }

    public void setEval_price_21(float eval_price_21) {
        this.eval_price_21 = eval_price_21;
    }

    public float getSeller_price() {
        return seller_price;
    }

    public void setSeller_price(float seller_price) {
        this.seller_price = seller_price;
    }

    public float getCheap_price() {
        return cheap_price;
    }

    public void setCheap_price(float cheap_price) {
        this.cheap_price = cheap_price;
    }

    public String getVin_code() {
        return vin_code;
    }

    public void setVin_code(String vin_code) {
        this.vin_code = vin_code;
    }

    public int getExclusive() {
        return exclusive;
    }

    public void setExclusive(int exclusive) {
        this.exclusive = exclusive;
    }

    public String getLiyang_model_id() {
        return liyang_model_id;
    }

    public void setLiyang_model_id(String liyang_model_id) {
        this.liyang_model_id = liyang_model_id;
    }

    public String getLiyang_brand_nane() {
        return liyang_brand_nane;
    }

    public void setLiyang_brand_nane(String liyang_brand_nane) {
        this.liyang_brand_nane = liyang_brand_nane;
    }

    public String getLiyang_series_name() {
        return liyang_series_name;
    }

    public void setLiyang_series_name(String liyang_series_name) {
        this.liyang_series_name = liyang_series_name;
    }

    public String getLiyang_model_name() {
        return liyang_model_name;
    }

    public void setLiyang_model_name(String liyang_model_name) {
        this.liyang_model_name = liyang_model_name;
    }

    public String getCheck_source() {
        return check_source;
    }

    public void setCheck_source(String check_source) {
        this.check_source = check_source;
    }

    public int getComplete_check() {
        return complete_check;
    }

    public void setComplete_check(int complete_check) {
        this.complete_check = complete_check;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getIs_channel() {
        return is_channel;
    }

    public void setIs_channel(int is_channel) {
        this.is_channel = is_channel;
    }
}
