package com.haoche51.checker.entity;

import java.util.List;

public class Brand {
  private String sortLetter = "";
  private int brandId;// Id for brand
  private String brandName;
  private boolean selected = false;
  private List<String> series_ids;

  public Brand() {
  }

  public Brand(int brandId) {
    this.brandId = brandId;
  }

  public Brand(int brandId, String brandName) {
    this.brandId = brandId;
    this.brandName = brandName;
  }

  public List<String> getSeries_ids() {
    return series_ids;
  }

  public void setSeries_ids(List<String> series_ids) {
    this.series_ids = series_ids;
  }

  public int getBrandId() {
    return brandId;
  }

  public void setBrandId(int brandId) {
    this.brandId = brandId;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String getSortLetter() {
    return sortLetter;
  }

  public void setSortLetter(String sortLetter) {
    this.sortLetter = sortLetter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Brand)) return false;

    Brand brand = (Brand) o;

    if (getBrandId() != brand.getBrandId()) return false;
    if (isSelected() != brand.isSelected()) return false;
    if (getSortLetter() != null ? !getSortLetter().equals(brand.getSortLetter()) : brand.getSortLetter() != null)
      return false;
    if (getBrandName() != null ? !getBrandName().equals(brand.getBrandName()) : brand.getBrandName() != null)
      return false;
    return !(getSeries_ids() != null ? !getSeries_ids().equals(brand.getSeries_ids()) : brand.getSeries_ids() != null);

  }

  @Override
  public int hashCode() {
    int result = getSortLetter() != null ? getSortLetter().hashCode() : 0;
    result = 31 * result + getBrandId();
    result = 31 * result + (getBrandName() != null ? getBrandName().hashCode() : 0);
    result = 31 * result + (isSelected() ? 1 : 0);
    result = 31 * result + (getSeries_ids() != null ? getSeries_ids().hashCode() : 0);
    return result;
  }
}
