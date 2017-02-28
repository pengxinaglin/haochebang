package com.haoche51.checker.entity;

/**
 * Created by yangming on 2015/12/1.
 */
public class UserRightEntity {

  private Integer sort;
  private Integer code;
  private String name;
  private String rights;
  private String urlPath;
  private Integer parentID;
  /**
   * 1:普通权限,2:中级权限,4:高级权限
     注意：判断时，不要用等于，要用大于。
   */
  private Integer right;
  private String customJson;
  private String path;
  private Integer id;
  private String url;//获取CRM wap页的url

  public Integer getSort() {
    return sort;
  }

  public void setSort(Integer sort) {
    this.sort = sort;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRights() {
    return rights;
  }

  public void setRights(String rights) {
    this.rights = rights;
  }

  public String getUrlPath() {
    return urlPath;
  }

  public void setUrlPath(String urlPath) {
    this.urlPath = urlPath;
  }

  public Integer getParentID() {
    return parentID;
  }

  public void setParentID(Integer parentID) {
    this.parentID = parentID;
  }

  public Integer getRight() {
    return right;
  }

  public void setRight(Integer right) {
    this.right = right;
  }

  public String getCustomJson() {
    return customJson;
  }

  public void setCustomJson(String customJson) {
    this.customJson = customJson;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
