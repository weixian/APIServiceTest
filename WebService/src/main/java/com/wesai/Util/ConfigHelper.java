package com.wesai.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigHelper {

  public static BufferedReader bufread;
  private static String readStr = "";

  public static List<String> GetPathByTag(String configfile, String tag) {


    try {
      List<String> nodes = new ArrayList<String>();
      File fXmlFile = new File(configfile);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);
      doc.getDocumentElement().normalize();
      NodeList nList = doc.getElementsByTagName(tag);

      for (int temp = 0; temp < nList.getLength(); temp++) {
        Node nNode = nList.item(temp);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) nNode;
          NodeList list = eElement.getElementsByTagName("option");

          for (int i = 0; i < list.getLength(); i++) {
            nodes.add(list.item(i).getTextContent());
          }

          return nodes;

          // return eElement.getElementsByTagName("option").item(0)
          // .getTextContent();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static List<String> GetLogPath(String configfile) {

    return GetPathByTag(configfile, "logpath");
  }

  public static String GetAppAccessId(String configfile) {

    return GetPathByTag(configfile, "app_access").get(0);
  }


  public static JSONObject GetServiceRunInfo(String configfile, String ServiceName) {

    JSONObject ServiceInfo = new JSONObject();
    List<String> ServiceList = GetPathByTag(configfile, "Service");

    for (String str : ServiceList) {

      String cur[] = str.split(":");

      if (cur[0].equals(ServiceName)) {
        ServiceInfo.put("name", cur[0]);
        ServiceInfo.put("host", cur[1]);
        ServiceInfo.put("port", cur[2]);
      }

    }

    return ServiceInfo;
  }



  private static String readFile(String configfile) {
    String read;
    FileReader fileread;
    try {
      fileread = new FileReader(configfile);
      bufread = new BufferedReader(fileread);
      try {
        while ((read = bufread.readLine()) != null) {
          readStr = readStr + read + "\r\n";
        }
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      bufread.close();
      fileread.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // System.out.println("File content is :"+ "\r\n" + readStr);

    return readStr;
  }


  private static int WriteFile(String configfile, String str) {
    FileWriter filewrite;

    try {
      filewrite = new FileWriter(configfile);
      filewrite.write(str);
      filewrite.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return -1;
    }

    return 0;
  }


}
