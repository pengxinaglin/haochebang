package com.haoche51.checker.entity;

/**
 * Created by mac on 15/11/13.
 */
public class RspVinCodeEntity {
  private String vin_code;
  private String report_url;
  private String report_pdf;

  public String getVin_code() {
    return vin_code;
  }

  public void setVin_code(String vin_code) {
    this.vin_code = vin_code;
  }

  public String getReport_url() {
    return report_url;
  }

  public void setReport_url(String report_url) {
    this.report_url = report_url;
  }

  public String getReport_pdf() {
    return report_pdf;
  }

  public void setReport_pdf(String report_pdf) {
    this.report_pdf = report_pdf;
  }
}
