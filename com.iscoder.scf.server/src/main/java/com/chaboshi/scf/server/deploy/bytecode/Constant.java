package com.chaboshi.scf.server.deploy.bytecode;

import com.chaboshi.common.annotation.OperationContract;
import com.chaboshi.common.annotation.ServiceBehavior;
import com.chaboshi.common.entity.ErrorState;
import com.chaboshi.common.entity.KeyValuePair;
import com.chaboshi.common.entity.Out;
import com.chaboshi.scf.protocol.sdp.RequestProtocol;
import com.chaboshi.scf.server.contract.context.IProxyFactory;
import com.chaboshi.scf.server.contract.context.IProxyStub;
import com.chaboshi.scf.server.contract.context.SCFContext;
import com.chaboshi.scf.server.contract.context.SCFResponse;
import com.chaboshi.scf.server.core.convert.Convert;
import com.chaboshi.scf.server.core.convert.ConvertFacotry;
import com.chaboshi.scf.server.exception.ServiceFrameException;

/**
 * 
 * 
 */
public class Constant {

  /**
   * service contract config xml
   */
  public static final String SERVICE_CONTRACT = "serviceframe.xml";

  /**
   * out parameter name
   */
  public static final String OUT_PARAM = Out.class.getName();

  /**
   * IProxyStub class name
   */
  public static final String IPROXYSTUB_CLASS_NAME = IProxyStub.class.getName();

  /**
   * SCFContext class name
   */
  public static final String SCFCONTEXT_CLASS_NAME = SCFContext.class.getName();

  /**
   * SCFRequest class name
   */
  public static final String SCFRESPONSE_CLASS_NAME = SCFResponse.class.getName();

  /**
   * ServiceFrameException class name
   */
  public static final String SERVICEFRAMEEXCEPTION_CLASS_NAME = ServiceFrameException.class.getName();

  /**
   * Request protocol class name
   */
  public static final String REQUEST_PROTOCOL_CLASS_NAME = RequestProtocol.class.getName();

  /**
   * IConvert class name
   */
  public static final String ICONVERT_CLASS_NAME = Convert.class.getName();

  /**
   * ConvertFactory class name
   */
  public static final String CONVERT_FACTORY_CLASS_NAME = ConvertFacotry.class.getName();

  /**
   * KeyValuePair protocol class name
   */
  public static final String KEYVALUEPAIR_PROTOCOL_CLASS_NAME = KeyValuePair.class.getName();

  /**
   * ErrorState class name
   */
  public static final String ERRORSTATE_CLASS_NAME = ErrorState.class.getName();

  /**
   * IProxyFactory class name
   */
  public static final String IPROXYFACTORY_CLASS_NAME = IProxyFactory.class.getName();

  /**
   * OperationContract class name
   */
  public static final String OPERATIONCONTRACT_CLASS_NAME = OperationContract.class.getName();

  /**
   * ServiceBehavior class name
   */
  public static final String SERVICEBEHAVIOR_CLASS_NAME = ServiceBehavior.class.getName();

  /**
   * ServiceContract class name
   */
  public static final String SERVICECONTRACT_CLASS_NAME = ContractInfo.class.getName();
}