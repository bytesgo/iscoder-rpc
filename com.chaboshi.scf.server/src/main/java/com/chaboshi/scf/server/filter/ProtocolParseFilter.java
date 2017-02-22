package com.chaboshi.scf.server.filter;

import com.chaboshi.scf.protocol.sdp.ResetProtocol;
import com.chaboshi.scf.protocol.sfp.enumeration.PlatformType;
import com.chaboshi.scf.protocol.sfp.v1.Protocol;
import com.chaboshi.scf.server.IFilter;
import com.chaboshi.scf.server.contract.context.ExecFilterType;
import com.chaboshi.scf.server.contract.context.Global;
import com.chaboshi.scf.server.contract.context.SCFContext;
import com.chaboshi.scf.server.contract.context.SCFResponse;
import com.chaboshi.scf.server.contract.context.SecureContext;
import com.chaboshi.scf.server.contract.context.ServerStateType;
import com.chaboshi.scf.server.contract.context.ServerType;

/**
 * A filter for parse protocol from byte[]
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public class ProtocolParseFilter implements IFilter {

  @Override
  public void filter(SCFContext context) throws Exception {
    if (context.getServerType() == ServerType.TCP) {
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
      Protocol protocol = Protocol.fromBytes(context.getScfRequest().getRequestBuffer(), global.getGlobalSecureIsRights(), desKeyByte);
      context.getScfRequest().setProtocol(protocol);
      /**
       * 服务重启直接返回 备注：暂支持java客户端
       */
      if (Global.getSingleton().getServerState() == ServerStateType.Reboot && protocol.getPlatformType() == PlatformType.Java) {
        SCFResponse response = new SCFResponse();
        ResetProtocol rp = new ResetProtocol();
        rp.setMsg("This server is reboot!");
        protocol.setSdpEntity(rp);
        response.setResponseBuffer(protocol.toBytes(global.getGlobalSecureIsRights(), desKeyByte));
        context.setScfResponse(response);
        context.setExecFilter(ExecFilterType.None);
        context.setDoInvoke(false);
      }
    }
  }

  @Override
  public int getPriority() {
    return 50;
  }

}
