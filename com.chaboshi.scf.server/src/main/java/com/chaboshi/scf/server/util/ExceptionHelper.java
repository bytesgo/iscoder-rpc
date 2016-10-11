package com.chaboshi.scf.server.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import com.chaboshi.scf.protocol.sdp.ExceptionProtocol;
import com.chaboshi.scf.protocol.utility.ProtocolConst;

/**
 * create error protocol
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a> <a href="http://www.58.com">website</a>
 * 
 */
public class ExceptionHelper {

  /**
   * create Error message body
   * 
   * @param sfe
   * @return
   */
  public static ExceptionProtocol createError(ServiceFrameException sfe) {
    ExceptionProtocol error = new ExceptionProtocol();
    if (sfe != null) {
      if (sfe.getState() == null) {
        sfe.setState(ErrorState.OtherException);
      }
      error.setErrorCode(sfe.getState().getStateNum());

      StringBuilder sbError = new StringBuilder();
      sbError.append("error num:");
      sbError.append(System.nanoTime());
      sbError.append("--state:");
      sbError.append(sfe.getState().toString());

      sbError.append("--fromIP:");
      if (sfe.getFromIP() != null) {
        sbError.append(sfe.getFromIP());
      }
      sbError.append("--toIP:");
      if (sfe.getToIP() != null) {
        sbError.append(sfe.getToIP());
      }

      sbError.append("--Message:");

      if (sfe.getMessage() != null) {
        sbError.append(sfe.getMessage());
      }

      sbError.append(getStackTrace(sfe));

      error.setErrorMsg(sbError.toString());
      error.setFromIP(sfe.getFromIP());
      error.setToIP(sfe.getToIP());
    }
    return error;
  }

  /**
   * create Error message body
   * 
   * @param state
   * @param fromIP
   * @param toIP
   * @return
   */
  public static ExceptionProtocol createError(ErrorState state, String fromIP, String toIP) {
    ExceptionProtocol error = new ExceptionProtocol();
    if (state == null) {
      state = ErrorState.OtherException;
    }
    error.setErrorCode(state.getStateNum());
    error.setErrorMsg("error num:" + System.nanoTime() + "--state:" + state.toString());
    error.setFromIP(fromIP);
    error.setToIP(toIP);
    return error;
  }

  /**
   * create Error message body
   * 
   * @param state
   * @param fromIP
   * @param toIP
   * @param e
   * @return
   */
  public static ExceptionProtocol createError(ErrorState state, String fromIP, String toIP, Exception e) {
    ExceptionProtocol error = new ExceptionProtocol();
    if (state == null) {
      state = ErrorState.OtherException;
    }
    error.setErrorCode(state.getStateNum());

    StringBuilder sbError = new StringBuilder();
    sbError.append("error num:" + System.nanoTime());
    sbError.append("--state:");
    sbError.append(state.toString());
    if (e != null) {
      sbError.append("--Message:");

      if (e.getMessage() != null) {
        sbError.append(e.getMessage());
      }

      StackTraceElement[] trace = e.getStackTrace();
      if (trace != null) {
        for (int i = 0; i < trace.length; i++) {
          sbError.append(trace[i].toString());
          sbError.append("---");
        }
      }
    }
    error.setErrorMsg(sbError.toString());

    error.setFromIP(fromIP);
    error.setToIP(toIP);
    return error;
  }

  /**
   * create Error message body
   * 
   * @param e
   * @return
   */
  public static ExceptionProtocol createError(Throwable e) {
    ExceptionProtocol error = new ExceptionProtocol();
    error.setErrorCode(ErrorState.OtherException.getStateNum());

    StringBuilder sbError = new StringBuilder();
    sbError.append("error num:" + System.nanoTime());
    sbError.append("--state:");
    sbError.append(ErrorState.OtherException.toString());
    if (e != null) {
      sbError.append("--Message:");

      if (e.getMessage() != null) {
        sbError.append(e.getMessage());
      }

      StackTraceElement[] trace = e.getStackTrace();
      if (trace != null) {
        for (int i = 0; i < trace.length; i++) {
          sbError.append(trace[i].toString());
          sbError.append("---");
        }
      }
    }
    error.setErrorMsg(sbError.toString());
    error.setFromIP("");
    error.setToIP("");
    return error;
  }

  /**
   * create Error byte[] protocol
   * 
   * @param e
   * @param version
   * @return
   */
  public static byte[] createErrorProtocol() {
    byte[] pByte = new byte[ProtocolConst.P_START_TAG.length + ProtocolConst.P_END_TAG.length + 1];
    System.arraycopy(ProtocolConst.P_START_TAG, 0, pByte, 0, ProtocolConst.P_START_TAG.length);
    pByte[ProtocolConst.P_START_TAG.length] = 0;
    System.arraycopy(ProtocolConst.P_END_TAG, 0, pByte, ProtocolConst.P_END_TAG.length + 1, ProtocolConst.P_END_TAG.length);
    return pByte;
  }

  /**
   * get exception stack trace
   * 
   * @param e
   * @return
   */
  public static String getStackTrace(Throwable e) {
    String stackTrace = "";
    Writer writer = null;
    PrintWriter printWriter = null;
    try {
      writer = new StringWriter();
      printWriter = new PrintWriter(writer);
      e.printStackTrace(printWriter);
      stackTrace = writer.toString();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (printWriter != null) {
        try {
          printWriter.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

      if (writer != null) {
        try {
          writer.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    return stackTrace;
  }
}
