package com.iscoder.scf.server.util;

import java.util.HashMap;
import java.util.Map;

import com.iscoder.scf.server.IInit;
import com.iscoder.scf.server.contract.context.SCFChannel;

public class MethodMapping implements IInit {

  private static final Map<SCFChannel, HashMap<String, String>> channelMap = new HashMap<SCFChannel, HashMap<String, String>>();

  @Override
  public void init() {
    // TODO 从授权文件中加载方法名到methodMap
  }

  public static boolean check(SCFChannel channel, String methodName) {
    HashMap<String, String> map = channelMap.get(channel);
    if (map != null) {
      return map.containsKey(methodName);
    }

    return false;
  }
}