/*
 * Copyright 2010 www.58.com, Inc.
 * @author Service Platform Architecture Team mail: spat@58.com web: http://www.58.com
 */
package com.chaboshi.scf.client.util.helper;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XMLHelper
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class XMLHelper {

  public static Element GetXmlDoc(String filePath) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      // System.err.println(pce);
      // System.exit(1);
      pce.printStackTrace();
    }
    Document doc = null;
    try {
      File f = new File(filePath);
      doc = db.parse(f);
    } catch (Exception e) {
      // System.err.println(e);
      // System.exit(1);
      e.printStackTrace();
    }
    return (Element) doc.getDocumentElement();
  }

  public static Node selectSingleNode(String express, Object source) {
    Node result = null;
    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xpath = xpathFactory.newXPath();
    try {
      result = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }

    return result;
  }

  public static NodeList selectNodes(String express, Object source) {
    NodeList result = null;
    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xpath = xpathFactory.newXPath();
    try {
      result = (NodeList) xpath.evaluate(express, source, XPathConstants.NODESET);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }

    return result;
  }
}
