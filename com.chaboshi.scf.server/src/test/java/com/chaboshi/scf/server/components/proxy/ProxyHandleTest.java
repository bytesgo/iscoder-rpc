package com.chaboshi.scf.server.components.proxy;

import junit.framework.TestCase;

//import java.util.ArrayList;
//import java.util.List;
//
//import com.bj58.sfft.servicedemo.entity.Comment;
//import com.bj58.sfft.servicedemo.entity.Info;
//import com.bj58.sfft.serviceframe.contract.proxy.IProxyHandle;
//import com.bj58.sfft.serviceframe.core.proxy.ProxyHandle;
//import com.bj58.sfft.serviceframe.protocol.CreateHelper;
//import com.bj58.sfft.serviceframe.protocol.KeyValuePair;
//import com.bj58.sfft.serviceframe.protocol.ParseHelper;
//import com.bj58.sfft.serviceframe.protocol.enumeration.Compress;
//import com.bj58.sfft.serviceframe.protocol.enumeration.Platform;
//import com.bj58.sfft.serviceframe.protocol.enumeration.Serializ;
//import com.bj58.sfft.serviceframe.protocol.messagebody.datatype.Request;
//import com.bj58.sfft.serviceframe.protocol.messagebody.datatype.Response;
//import com.bj58.sfft.serviceframe.protocol.protocolentity.ProtocolEntityBase;

//@SuppressWarnings("unchecked")
public class ProxyHandleTest extends TestCase {
  //
  // private static IProxyHandle proxyHandle = null;
  //
  // static {
  // //ClassPath.ProxyFactoryPath =
  // "com.bj58.sfft.serviceframe.proxy.demo.ProxyFactory";
  // proxyHandle = new ProxyHandle();
  // UDPClient.IsSendMsg = false;
  // }
  //
  //
  // public void testInvoke_JSON_GetInfoById() throws Exception {
  // Request request = new Request();
  // request.setLookup("InfoProviderWithIndex");
  // request.setMethodName("getInfoById");
  // List<KeyValuePair<String,Object>> kvList = new
  // ArrayList<KeyValuePair<String,Object>>();
  // kvList.add(new KeyValuePair<String,Object>("long","101001"));
  // request.setParaKVList(kvList);
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JSON, request);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  // Info info =
  // (Info)com.bj58.sfft.json.Helper.toJava(response.getResult().toString(),
  // Info.class, Info.class);
  //
  // junit.framework.Assert.assertEquals(101001, info.getInfoID());
  // junit.framework.Assert.assertEquals("infoTitle", info.getTitle());
  // junit.framework.Assert.assertEquals("infoContent", info.getContent());
  // }
  //
  //
  //
  // public void testInvoke_JSON_OutPara() throws Exception {
  // Request request = new Request();
  // request.setLookup("InfoProviderWithIndex");
  // request.setMethodName("getInfoList");
  // List<KeyValuePair<String,Object>> kvList = new
  // ArrayList<KeyValuePair<String,Object>>();
  // kvList.add(new KeyValuePair<String,Object>("String","101001"));
  // kvList.add(new KeyValuePair<String,Object>("Integer",0));
  // request.setParaKVList(kvList);
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JSON, request);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  //
  //
  // List<Info> infoList = new ArrayList<Info>();
  // infoList =
  // (List<Info>)com.bj58.sfft.json.Helper.toJava(response.getResult().toString(),
  // infoList.getClass(), Info.class);
  //
  // junit.framework.Assert.assertEquals(101001, infoList.get(0).getInfoID());
  // junit.framework.Assert.assertEquals(101002, infoList.get(1).getInfoID());
  // junit.framework.Assert.assertEquals(101003, infoList.get(2).getInfoID());
  // }
  //
  //
  //
  // public void testInvoke_JAVA_OutPara() throws Exception {
  // Request server = new Request();
  // server.setLookup("InfoProviderWithIndex");
  // server.setMethodName("getInfoList");
  // List<KeyValuePair<String,Object>> kvList = new
  // ArrayList<KeyValuePair<String,Object>>();
  // kvList.add(new KeyValuePair<String,Object>("String","101001"));
  // kvList.add(new KeyValuePair<String,Object>("Integer",0));
  // server.setParaKVList(kvList);
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JavaByte, server);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  //
  //
  //
  // List<Info> infoList = (List<Info>)response.getResult();
  //
  //
  // junit.framework.Assert.assertEquals(101001, infoList.get(0).getInfoID());
  // junit.framework.Assert.assertEquals(101002, infoList.get(1).getInfoID());
  // junit.framework.Assert.assertEquals(101003, infoList.get(2).getInfoID());
  // }
  //
  //
  // public void testInvoke_JSON_EmptyFun() throws Exception {
  // Request server = new Request();
  // server.setLookup("InfoProviderWithIndex");
  // server.setMethodName("emptyFun");
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JSON, server);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  //
  // junit.framework.Assert.assertEquals("null",
  // response.getResult().toString());
  // junit.framework.Assert.assertEquals(0, response.getOutpara().length);
  // }
  //
  //
  //
  //
  // public void testInvoke_JAVA_GetInfoById() throws Exception {
  // Request server = new Request();
  // server.setLookup("InfoProviderWithIndex");
  // server.setMethodName("getInfoById");
  // List<KeyValuePair<String,Object>> kvList = new
  // ArrayList<KeyValuePair<String,Object>>();
  // kvList.add(new KeyValuePair<String,Object>("long","101001"));
  // server.setParaKVList(kvList);
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JavaByte, server);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  // Info info = (Info)response.getResult();
  //
  // junit.framework.Assert.assertEquals(101001, info.getInfoID());
  // junit.framework.Assert.assertEquals("infoTitle", info.getTitle());
  // junit.framework.Assert.assertEquals("infoContent", info.getContent());
  // }
  //
  //
  // public void testInvoke_JAVA_EmptyFun() throws Exception {
  // Request server = new Request();
  // server.setLookup("InfoProviderWithIndex");
  // server.setMethodName("emptyFun");
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JavaByte, server);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  //
  // junit.framework.Assert.assertEquals(null, response.getResult());
  // junit.framework.Assert.assertEquals(null, response.getOutpara());
  // }
  //
  //
  //
  //
  //
  //
  //
  // public void testInvoke_JSON_addCommentArray() throws Exception {
  // Request server = new Request();
  // server.setLookup("CommentService");
  // server.setMethodName("addComment");
  // List<KeyValuePair<String,Object>> kvList = new
  // ArrayList<KeyValuePair<String,Object>>();
  // Comment[] commentAry = new Comment[]{new Comment(101001,"c1"), new
  // Comment(101002,"c2")};
  // kvList.add(new KeyValuePair<String,Object>("Comment[]",commentAry));
  // server.setParaKVList(kvList);
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JSON, server);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  //
  // junit.framework.Assert.assertEquals("null",
  // response.getResult().toString());
  // junit.framework.Assert.assertEquals(0, response.getOutpara().length);
  // }
  //
  //
  //
  //
  //
  // public void testInvoke_JSON_addCommentList() throws Exception {
  // Request server = new Request();
  // server.setLookup("CommentService");
  // server.setMethodName("addComment");
  // List<KeyValuePair<String,Object>> kvList = new
  // ArrayList<KeyValuePair<String,Object>>();
  // List<Comment> commentList = new ArrayList<Comment>();
  // commentList.add(new Comment(101001,"c1"));
  // commentList.add(new Comment(101002,"c2"));
  // kvList.add(new KeyValuePair<String,Object>("List<Comment>",commentList));
  // server.setParaKVList(kvList);
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JSON, server);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //// System.out.println(protocol);
  //// System.out.println("==========================");
  //// System.out.println(result);
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  // //System.out.println(response.getResult().toString());
  //
  // junit.framework.Assert.assertEquals("null",
  // response.getResult().toString());
  // junit.framework.Assert.assertEquals(0, response.getOutpara().length);
  // }
  //
  //
  //
  //
  //
  //
  // public void testInvoke_JSON_testOutPara() throws Exception {
  // Request server = new Request();
  // server.setLookup("TestService");
  // server.setMethodName("testOutPara");
  //
  // List<KeyValuePair<String,Object>> kvList = new
  // ArrayList<KeyValuePair<String,Object>>();
  // kvList.add(new KeyValuePair<String,Object>("Comment",new
  // Comment(101001,"c1")));
  // kvList.add(new KeyValuePair<String,Object>("String","hello world"));
  // kvList.add(new KeyValuePair<String,Object>("Long",123L));
  // server.setParaKVList(kvList);
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JSON, server);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //// System.out.println(protocol);
  //// System.out.println("==========================");
  //// System.out.println(result);
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  //
  // List<Comment> commentList = new ArrayList<Comment>();
  // commentList =
  // (List<Comment>)com.bj58.sfft.json.Helper.toJava(response.getResult().toString(),
  // commentList.getClass(), Comment.class);
  // junit.framework.Assert.assertEquals(101001,
  // commentList.get(0).getCommentID());
  // junit.framework.Assert.assertEquals(101002,
  // commentList.get(1).getCommentID());
  // junit.framework.Assert.assertEquals(101003,
  // commentList.get(2).getCommentID());
  //
  //
  // Comment outComment =
  // (Comment)com.bj58.sfft.json.Helper.toJava(response.getOutpara()[0].toString(),
  // Comment.class);
  // junit.framework.Assert.assertEquals("c1", outComment.getContent());
  //
  // Long outLong =
  // (Long)com.bj58.sfft.json.Helper.toJava(response.getOutpara()[1].toString(),
  // Long.class);
  // junit.framework.Assert.assertEquals("123", outLong.toString());
  // }
  //
  //
  //
  // public void testInvoke_JSON_testAllClassType() throws Exception {
  // Request server = new Request();
  // server.setLookup("TestService");
  // server.setMethodName("testAllClassType");
  //
  // //public Long testAllClassType(Integer p1,Long p2,Short p3,Float p4,Boolean
  // p5,Double p6,Character p7,Byte p8, String p9);
  // List<KeyValuePair<String,Object>> kvList = new
  // ArrayList<KeyValuePair<String,Object>>();
  // kvList.add(new KeyValuePair<String,Object>("Integer",123));
  // kvList.add(new KeyValuePair<String,Object>("Long",123L));
  // kvList.add(new KeyValuePair<String,Object>("Short",123));
  // kvList.add(new KeyValuePair<String,Object>("Float",123F));
  // kvList.add(new KeyValuePair<String,Object>("Boolean",true));
  // kvList.add(new KeyValuePair<String,Object>("Double",123D));
  // kvList.add(new KeyValuePair<String,Object>("Character",'\42'));
  // kvList.add(new KeyValuePair<String,Object>("Byte",123));
  // kvList.add(new KeyValuePair<String,Object>("String","hi"));
  // server.setParaKVList(kvList);
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JSON, server);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  // Long rst =
  // (Long)com.bj58.sfft.json.Helper.toJava(response.getResult().toString(),
  // Long.class);
  // junit.framework.Assert.assertEquals("123", rst.toString());
  // }
  //
  //
  //
  // public void testInvoke_JSON_testAllBaseType() throws Exception {
  // Request server = new Request();
  // server.setLookup("TestService");
  // server.setMethodName("testAllBaseType");
  //
  // //public Long testAllClassType(Integer p1,Long p2,Short p3,Float p4,Boolean
  // p5,Double p6,Character p7,Byte p8, String p9);
  // List<KeyValuePair<String,Object>> kvList = new
  // ArrayList<KeyValuePair<String,Object>>();
  // kvList.add(new KeyValuePair<String,Object>("int",123));
  // kvList.add(new KeyValuePair<String,Object>("long",123L));
  // kvList.add(new KeyValuePair<String,Object>("short",123));
  // kvList.add(new KeyValuePair<String,Object>("float",123F));
  // kvList.add(new KeyValuePair<String,Object>("boolean",true));
  // kvList.add(new KeyValuePair<String,Object>("double",123D));
  // kvList.add(new KeyValuePair<String,Object>("char",'\42'));
  // kvList.add(new KeyValuePair<String,Object>("byte",123));
  // server.setParaKVList(kvList);
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JSON, server);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  // Double rst =
  // (Double)com.bj58.sfft.json.Helper.toJava(response.getResult().toString(),
  // Double.class);
  // junit.framework.Assert.assertEquals("111111.0", rst.toString());
  // }
  //
  //
  //
  // public void testInvoke_JSON_returnInteger() throws Exception {
  // Request server = new Request();
  // server.setLookup("TestService");
  // server.setMethodName("returnInteger");
  //
  // ProtocolEntityBase pe = createProtocol(Serializ.JSON, server);
  // String protocol = pe.toString();
  // String result = proxyHandle.invoke(protocol, "127.0.0.1", "127.0.0.1");
  // System.out.println(protocol);
  // System.out.println("==========================");
  // System.out.println(result);
  //
  //
  // ParseHelper ph = new ParseHelper(result);
  // Response response = (Response)ph.parse();
  // Integer rst =
  // (Integer)com.bj58.sfft.json.Helper.toJava(response.getResult().toString(),
  // Integer.class);
  // junit.framework.Assert.assertEquals(null, rst);
  // }
  //
  //
  //
  // public void testReplace(){
  // String str = "java.uitl.List<Info>";
  // str = str.replaceAll("<.*?>", "");
  // junit.framework.Assert.assertEquals("java.uitl.List", str);
  //
  // str = "com.bj58.sfft.imc.entity.Info";
  // str = str.replaceAll("<.*?>", "");
  // junit.framework.Assert.assertEquals("com.bj58.sfft.imc.entity.Info", str);
  //
  // }
  //
  //
  // public ProtocolEntityBase createProtocol(Serializ s, Request request)
  // throws Exception {
  //
  // //////��ʹ��Ĭ��ֵ,ȫ���ֶ��趨////////
  // CreateHelper ch = new CreateHelper();
  // //�������к�
  // ch.setSerialNo(1234);
  // //���÷�����
  // ch.setServiceNum(1);
  // //������Ϣ��
  // ch.setMsgBody(request);
  //
  // //�����Ƿ���ӷֽ��
  // ch.setAddDelimiter(false);
  // //����ѹ���㷨
  // ch.setCompress(Compress.UnCompress);
  // //��������ƽ̨
  // ch.setPlatform(Platform.Java);
  // //����Э��汾
  // ch.setProtocolVersion(1);
  // //�������л�����
  // ch.setSerializ(s);
  //
  // return ch.createPE();
  // }
}