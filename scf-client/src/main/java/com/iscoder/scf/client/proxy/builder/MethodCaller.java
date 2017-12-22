package com.iscoder.scf.client.proxy.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iscoder.scf.client.proxy.ServiceProxy;
import com.iscoder.scf.common.annotation.AnnotationUtil;
import com.iscoder.scf.common.annotation.OperationContract;
import com.iscoder.scf.common.entity.Out;

/**
 * MethodCaller
 */
public class MethodCaller {

  private String serviceName;
  private String lookup;
  private static final Logger logger = LoggerFactory.getLogger(MethodCaller.class);

  public MethodCaller(String serviceName, String lookup) {
    this.serviceName = serviceName;
    this.lookup = lookup;
  }

  @SuppressWarnings("unchecked")
  public Object doMethodCall(Object[] args, Method methodInfo) throws Exception, Throwable {
    Type[] argsTypes = methodInfo.getGenericParameterTypes();// ex:java.util.Map<java.lang.String,
                                                           // java.lang.String>
    Class<?>[] clsAry = methodInfo.getParameterTypes();// ex:java.util.Map
    if (args == null) {
      args = new Object[0];
    }
    if (args.length != argsTypes.length) {
      logger.error("argument count error!");
      throw new Exception("argument count error!");
    }

    ServiceProxy proxy = ServiceProxy.getProxy(serviceName);
    Parameter[] paras = null;
    List<Integer> outParas = new ArrayList<Integer>();

    boolean syn = true;
    ReceiveHandler receiveHandler = null;
    int parasLength = 0;

    if (argsTypes != null) {
      if ((argsTypes.length >= 1) && (args[argsTypes.length - 1] instanceof ReceiveHandler)) {
        syn = false;
        receiveHandler = (ReceiveHandler) args[argsTypes.length - 1];
        parasLength = argsTypes.length - 1;
      } else {
        parasLength = argsTypes.length;
      }
      paras = new Parameter[parasLength];
      for (int i = 0; i < parasLength; i++) {
        if (args[i] instanceof Out) {
          paras[i] = new Parameter(args[i], clsAry[i], argsTypes[i], ParaType.Out);
          outParas.add(i);
        } else {
          paras[i] = new Parameter(args[i], clsAry[i], argsTypes[i], ParaType.In);
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
