package com.chaboshi.scf.server.performance.monitorweb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * udp client for send msg
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 *         <a href="http://blog.58.com/spat/">blog</a>
 *         <a href="http://www.58.com">website</a>
 * 
 */
public class MonitorUDPClient {

  @SuppressWarnings("unused")
  private String encode;

  private DatagramSocket sock = null;

  private InetSocketAddress addr = null;

  public static MonitorUDPClient getInstrance(String ip, int port, String encode) throws SocketException {
    MonitorUDPClient client = new MonitorUDPClient();
    client.encode = encode;
    client.sock = new DatagramSocket();
    client.addr = new InetSocketAddress(ip, port);
    return client;
  }

  private MonitorUDPClient() {

  }

  public void close() {
    sock.close();
  }

  /**
   * send udp msg
   * 
   * @param msg
   */
  public void send(String msg, String encoding) throws Exception {
    byte[] buf = msg.getBytes(encoding);
    send(buf);
  }

  public void send(String msg) throws Exception {
    byte[] buf = msg.getBytes("utf-8");
    send(buf);
  }

  public void send(byte[] buf) throws IOException {
    DatagramPacket dp = new DatagramPacket(buf, buf.length, addr);
    sock.send(dp);
  }
}
