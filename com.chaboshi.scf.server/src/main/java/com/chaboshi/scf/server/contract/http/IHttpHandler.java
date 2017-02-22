package com.chaboshi.scf.server.contract.http;

import com.chaboshi.scf.server.annotation.OperationContract;
import com.chaboshi.scf.server.annotation.ServiceContract;

@ServiceContract
public interface IHttpHandler {

  /**
   * http get request
   * 
   * @param context HttpContext
   */
  @OperationContract
  public void get(HttpContext context);

  /**
   * http post request
   * 
   * @param context HttpContext
   */
  @OperationContract
  public void post(HttpContext context);

  /**
   * http put request
   * 
   * @param context HttpContext
   */
  @OperationContract
  public void put(HttpContext context);

  /**
   * http delete request
   * 
   * @param context HttpContext
   */
  @OperationContract
  public void delete(HttpContext context);

  /**
   * http head request
   * 
   * @param context HttpContext
   */
  @OperationContract
  public void head(HttpContext context);

}