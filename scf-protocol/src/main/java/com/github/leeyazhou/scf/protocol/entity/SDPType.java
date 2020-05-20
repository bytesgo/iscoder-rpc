package com.github.leeyazhou.scf.protocol.entity;

import com.github.leeyazhou.scf.core.exception.ExceptionProtocol;
import com.github.leeyazhou.scf.protocol.sdp.HandclaspProtocol;
import com.github.leeyazhou.scf.protocol.sdp.RequestProtocol;
import com.github.leeyazhou.scf.protocol.sdp.ResetProtocol;
import com.github.leeyazhou.scf.protocol.sdp.ResponseProtocol;

public enum SDPType {
  /**
   * Reset:服务重启协议 ;Handclasp:权限认证协议
   */
  Response(1), Request(2), Exception(3), Config(4), Handclasp(5), Reset(6);

  private final int num;

  public int getNum() {
    return num;
  }

  private SDPType(int num) {
    this.num = num;
  }

  public static SDPType getSDPType(int num) throws java.lang.Exception {
    for (SDPType type : SDPType.values()) {
      if (type.getNum() == num) {
        return type;
      }
    }
    throw new Exception("末知的SDP:" + num);
  }

  /**
   * 
   * @param clazz
   * @return
   * @throws Exception
   */
  public static SDPType getSDPType(Class<?> clazz) throws Exception {
    if (clazz == RequestProtocol.class) {
      return SDPType.Request;
    } else if (clazz == ResponseProtocol.class) {
      return SDPType.Response;
    } else if (clazz == ExceptionProtocol.class) {
      return SDPType.Exception;
    } else if (clazz == HandclaspProtocol.class) {
      return SDPType.Handclasp;
    } else if (clazz == ResetProtocol.class) {
      return SDPType.Reset;
    }
    throw new Exception("末知的SDP:" + clazz.getName());
  }

  /**
   * 
   * @param type
   * @return
   * @throws Exception
   */
  public static Class<?> getSDPClass(SDPType type) throws Exception {
    if (type == SDPType.Request) {
      return RequestProtocol.class;
    } else if (type == SDPType.Response) {
      return ResponseProtocol.class;
    } else if (type == SDPType.Exception) {
      return ExceptionProtocol.class;
    } else if (type == SDPType.Handclasp) {
      return HandclaspProtocol.class;
    } else if (type == SDPType.Reset) {
      return ResetProtocol.class;
    }
    throw new Exception("末知的SDP:" + type);
  }

  public static SDPType getSDPType(Object obj) {
    if (obj instanceof RequestProtocol) {
      return SDPType.Request;
    } else if (obj instanceof ResponseProtocol) {
      return SDPType.Response;
    } else if (obj instanceof HandclaspProtocol) {
      return SDPType.Handclasp;
    } else if (obj instanceof ResetProtocol) {
      return SDPType.Reset;
    } else {
      return SDPType.Exception;
    }
  }
}
