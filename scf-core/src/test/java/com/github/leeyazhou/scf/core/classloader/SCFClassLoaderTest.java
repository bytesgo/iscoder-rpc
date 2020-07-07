///**
// * 
// */
//package com.github.leeyazhou.scf.core.classloader;
//
//import java.lang.reflect.Method;
//import java.net.URL;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
///**
// * @author leeyazhou
// *
// */
//public class SCFClassLoaderTest {
//  private static final String baseURL = "/opt/code/git/iscoder-rpc/scf-core/src/libs";
//  static SCFClassLoader classLoader;
//  static ApplicationClassLoader applicationClassLoader;
//
//  @BeforeClass
//  public static void beforeAll() throws Exception {
//    classLoader = new SCFClassLoader();
//    classLoader.addURL(new URL("file://" + baseURL + "/lib1/classloader-example-0.0.1-SNAPSHOT.jar"));
//
//    applicationClassLoader = new ApplicationClassLoader(classLoader);
//    applicationClassLoader.addURL(new URL("file://" + baseURL + "/lib2/classloader-example-0.0.1-SNAPSHOT.jar"));
//
//  }
//
//  @Test
//  public void testSCFClassLoader() throws Exception {
////    Class<?> userService = classLoader.loadClass("com.ly.traffic.classloader.UserService");
////
////    Assert.assertNotNull(userService);
////    String ret = hello(userService);
////    System.out.println("服务1:" + ret);
////    Assert.assertEquals("SCFClassLoader: Hello world", hello(userService));
//
//
//    Class<?> userService2 = applicationClassLoader.loadClass("com.ly.traffic.classloader.UserService");
//   String ret2 = hello(userService2);
//    System.out.println("服务2:" + ret2);
//    Assert.assertEquals("ApplicationClassLoader: Hello world", ret2);
//  }
//
//
//  private String hello(Class<?> clazz) throws Exception {
//    Object userService = clazz.newInstance();
//
//    Method helloMethod = clazz.getMethod("hello", new Class[] {String.class});
//    return (String) helloMethod.invoke(userService, "world");
//  }
//
//}
