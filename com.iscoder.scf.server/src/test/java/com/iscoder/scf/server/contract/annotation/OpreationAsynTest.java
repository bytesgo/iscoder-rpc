package com.iscoder.scf.server.contract.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.iscoder.scf.common.annotation.OperationAsyn;
import com.iscoder.scf.common.annotation.ServiceContract;

public class OpreationAsynTest {
  public static void main(String[] args) {
    try {
      Class<?> cla = Class.forName("com.iscoder.scf.server.contract.annotation.OpreTest");
      Method[] method = cla.getDeclaredMethods();
      for (Method m : method) {
        OperationAsyn oa = m.getAnnotation(OperationAsyn.class);
        Type[] typeAry = m.getGenericParameterTypes();
        Class<?>[] clazz = m.getParameterTypes();
        String sn = "";
        StringBuffer strBuff = new StringBuffer();
        if (oa != null) {
          for (int i = 0; i < clazz.length; i++) {
            String itemName = typeAry[i].toString().replaceAll(clazz[i].getCanonicalName().replaceAll("\\[", "").replaceAll("\\]", ""), "")
                .replaceAll("\\<", "").replaceAll("\\>", "");

            if (typeAry[i].toString().lastIndexOf(">") == -1) {
              sn = clazz[i].getSimpleName();
            } else {
              sn = clazz[i].getCanonicalName();
              sn = sn.substring(sn.lastIndexOf(".") + 1);

              if (itemName.indexOf(",") == -1) {
                itemName = itemName.substring(itemName.lastIndexOf(".") + 1);
                sn = sn + "<" + itemName + ">";
              } else {
                String[] genericItem = typeAry[i].toString().replaceAll(clazz[i].getCanonicalName(), "").replaceAll("\\<", "")
                    .replaceAll("\\>", "").split(",");
                sn = sn + "<" + genericItem[0].substring(genericItem[0].lastIndexOf(".") + 1) + ","
                    + genericItem[1].substring(genericItem[1].lastIndexOf(".") + 1) + ">";
              }
            }
            strBuff.append(sn);
          }
          System.out.println(strBuff.toString());
        }
      }

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}

@ServiceContract
interface OpreTest {
  @OperationAsyn
  public void f(Map<String, Integer[]> map, List<String> list, int x);

  @OperationAsyn
  public void f(int x, String str, List<String> list, String[] a);
}