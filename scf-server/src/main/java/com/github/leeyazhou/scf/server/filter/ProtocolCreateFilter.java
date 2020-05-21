package com.github.leeyazhou.scf.server.filter;
  

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.scf.protocol.sfp.Protocol;
import com.github.leeyazhou.scf.server.contract.context.Global;
import com.github.leeyazhou.scf.server.contract.context.SCFContext;
import com.github.leeyazhou.scf.server.contract.context.SCFResponse;
import com.github.leeyazhou.scf.server.contract.context.SecureContext;
import com.github.leeyazhou.scf.server.contract.context.ServerType;

/**
 * A filter for create protocol from byte[]
 * 
 */
public class ProtocolCreateFilter implements IFilter {

  private static final Logger logger = LoggerFactory.getLogger(ProtocolCreateFilter.class);

  @Override
  public void filter(SCFContext context) throws Exception {
    try {
      if (context.getServerType() == ServerType.TCP) {
        Protocol protocol = context.getScfRequest().getProtocol();
        byte[] desKeyByte = null;
        String desKeyStr = null;
        boolean bool = false;

        Global global = Global.getSingleton();
        if (global != null) {
          // 判断当前服务启用权限认证
          if (global.getGlobalSecureIsRights()) {
            SecureContext securecontext = global.getGlobalSecureContext(context.getChannel().getNettyChannel());
            bool = securecontext.isRights();
            if (bool) {
              desKeyStr = securecontext.getDesKey();
            }
          }
        }

        if (desKeyStr != null) {
          desKeyByte = desKeyStr.getBytes("utf-8");
        }

        if (context.getScfResponse() == null) {
          SCFResponse respone = new SCFResponse();
          context.setScfResponse(respone);
        }
        context.getScfResponse().setResponseBuffer(protocol.toBytes(Global.getSingleton().getGlobalSecureIsRights(), desKeyByte));
      }
    } catch (Exception ex) {
      System.out.println(context);
      logger.error("Server ProtocolCreateFilter error!");
    }
  }

  @Override
  public int getPriority() {
    return 50;
  }

}