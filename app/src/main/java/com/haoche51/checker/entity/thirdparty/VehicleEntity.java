package com.haoche51.checker.entity.thirdparty;

import com.haoche51.checker.entity.VehicleBrandEntity;
import com.haoche51.checker.entity.VehicleModelEntity;
import com.haoche51.checker.entity.VehicleSeriesEntity;

/**
 * Created by xuhaibo on 15/9/17.
 */
public class VehicleEntity {

	private VehicleModelEntity auto_model;
	private VehicleBrandEntity auto_brand;
	private VehicleSeriesEntity auto_series;

	public void setAuto_model(VehicleModelEntity auto_model) {
		this.auto_model = auto_model;
	}

	public void setAuto_brand(VehicleBrandEntity auto_brand) {
		this.auto_brand = auto_brand;
	}

	public void setAuto_series(VehicleSeriesEntity auto_series) {
		this.auto_series = auto_series;
	}
	public VehicleModelEntity getAuto_model() {
		return auto_model;
	}

	public VehicleBrandEntity getAuto_brand() {
		return auto_brand;
	}

	public VehicleSeriesEntity getAuto_series() {
		return auto_series;
	}
}
