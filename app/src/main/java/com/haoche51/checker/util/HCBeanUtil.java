package com.haoche51.checker.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 16/2/15.
 */
public class HCBeanUtil {
  /**
   * 将对象转成map
   */
  public static Map<String, Object> convertBeanToMap(Object bean) {
    Field[] fields = bean.getClass().getDeclaredFields();
    HashMap<String, Object> data = new HashMap<String, Object>();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        data.put(field.getName(), field.get(bean));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return data;
  }
}
