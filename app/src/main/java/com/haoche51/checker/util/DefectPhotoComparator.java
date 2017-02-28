package com.haoche51.checker.util;

import com.haoche51.checker.entity.PhotoEntity;

import java.util.Comparator;

/**
 * Created by mac on 15/9/16.
 */
public class DefectPhotoComparator implements Comparator<PhotoEntity> {

  //实现 （1，1）、（1，2）、（2，1）、（2，2）、（3，1）、（3，2）排序
  @Override
  public int compare(PhotoEntity p1, PhotoEntity p2) {
    return p1.getPosition_x() < p2.getPosition_x()
      ? -1
      : p1.getPosition_x() > p2.getPosition_x()
      ? 1
      : p1.getPosition_y() < p2.getPosition_y()
      ? -1
      : p1.getPosition_y() > p2.getPosition_y()
      ? 1
      : 0;
  }
}