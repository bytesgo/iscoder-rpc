/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.proxy.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.chaboshi.scf.client.proxy.ServiceProxy;
import com.chaboshi.scf.client.utility.logger.ILog;
import com.chaboshi.scf.client.utility.logger.LogFactory;
import com.chaboshi.scf.server.contract.annotation.AnnotationUtil;
import com.chaboshi.scf.server.contract.annotation.OperationContract;
import com.chaboshi.scf.server.contract.entity.Out;

/**
 * MethodCaller
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class MethodCaller {

  private String serviceName;
  private String lookup;
  private static ILog logger = LogFactory.getLogger(MethodCaller.class);

  public MethodCaller(String serviceName, String lookup) {
    this.serviceName = serviceName;
    this.lookup = lookup;
  }

  @SuppressWarnings("unchecked")
  public Object doMethodCall(Object[] args, Method methodInfo) throws Exception, Throwable {
    Type[] typeAry = methodInfo.getGenericParameterTypes();// ex:java.util.Map<java.lang.String,
                                                           // java.lang.String>
    Class<?>[] clsAry = methodInfo.getParameterTypes();// ex:java.util.Map
    if (args == null) {
      args = new Object[0];
    }
    if (args.length != typeAry.length) {
      logger.error("argument count error!");
      throw new Exception("argument count error!");
    }

    ServiceProxy proxy = ServiceProxy.getProxy(serviceName);
    Parameter[] paras = null;
    List<Integer> outParas = new ArrayList<Integer>();

    boolean syn = true;
    ReceiveHandler receiveHandler = null;
    int parasLength = 0;

    if (typeAry != null) {
      if ((typeAry.length >= 1) && (args[typeAry.length - 1] instanceof ReceiveHandler)) {
        syn = false;
        receiveHandler = (ReceiveHandler) args[typeAry.length - 1];
        parasLength = typeAry.length - 1;

      } else {
        parasLength = typeAry.length;
      }
      paras = new Parameter[parasLength];
      for (int i = 0; i < parasLength; i++) {
        if (args[i] instanceof Out) {
          paras[i] = new Parameter(args[i], clsAry[i], typeAry[i], ParaType.Out);
          outParas.add(i);
        } else {
          paras[i] = new Parameter(args[i], clsAry[i], typeAry[i], ParaType.In);
        }
      }
    }

    Parameter returnPara = new Parameter(null, methodInfo.getReturnType(), methodInfo.getGenericReturnType());
    String methodName = methodInfo.getName();
    OperationContract ann = methodInfo.getAnnotation(OperationContract.class);
    if (ann != null) {
      if (!ann.methodName().equals(AnnotationUtil.DEFAULT_VALUE)) {
        methodName = "$" + ann.methodName();
      }
    }
    if (syn) {
      InvokeResult<?> result = proxy.invoke(returnPara, lookup, methodName, paras);
      if (result != null && result.getOutPara() != null) {
        for (int i = 0; i < outParas.size() && i < result.getOutPara().length; i++) {
          Object op = args[outParas.get(i)];
          if (op instanceof Out) {
            ((Out<Object>) op).setOutPara(result.getOutPara()[i]);
          }
        }
      }
      return result.getResult();
    } else {
      proxy.invoke(returnPara, lookup, methodName, paras, receiveHandler);
      return null;
    }
  }
}
