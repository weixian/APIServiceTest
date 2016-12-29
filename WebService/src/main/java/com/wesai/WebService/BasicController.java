package com.wesai.WebService;

import java.io.IOException;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wesai.RpcClient.Packet;
import com.wesai.RpcClient.RpcClient;

/*
 * 根据文本协议，封装socket通信类
 */
public class BasicController {

  RpcClient rpcClient = null;

  /*
   * 构造函数
   */
  public BasicController(String host, int port) throws UnknownHostException, IOException {
    rpcClient = new RpcClient(host, port);
    rpcClient.connect();

  }

  /*
   * 根据文本协议构造数据，调用socket客户端，发送数据流，并接收输出数据流
   */
  public String getSocketStream(String user, String userId, String password, String className,
      String methodName,
      JSONArray value) throws IOException, JSONException {

    // 判断是否有中文或标点,是的话，进行unicode转码
    // for (int i = 0; i < value.length(); i++) {
    // if (judgeType(value.get(i))) {
    // StringBuffer result = new StringBuffer();
    // char[] ch = value.get(i).toString().toCharArray();
    // for (int j = 0; j < ch.length; j++) {
    // char c = ch[j];
    // if (isChinese(c)) {
    // result.append("\\u" + Integer.toHexString(c));
    // } else {
    // result.append(c);
    // }
    // }
    // // System.out.println(result.toString());
    // value.put(i, result);
    // } else {
    // continue;
    // }
    // }

    JSONObject jsonObject =
        Packet.getDataBody(user, password, userId, className, methodName, value);
    String dataString = jsonObject.toString() + "\n";
    System.out.println(Thread.currentThread().getName() + " " + dataString);

    rpcClient.send(dataString);
    // rpcClient.send(dataString);
    // 设置超时
    // rpcClient.getResponse(10000);
    // String context = rpcClient.getResponseStr();


    String context = rpcClient.getResponse(60000 * 1);
    rpcClient.close();

    return context;
  }

  /*
   * 转换成unicode字符
   */
  public static String chinaToUnicode(String strName) {
    StringBuffer result = new StringBuffer();
    char[] ch = strName.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (isChinese(c)) {
        result.append("\\u" + Integer.toHexString(c));
      } else {
        result.append(c);
      }
    }

    return result.toString();
  }

  /*
   * 判断是否为中文或标点
   */
  private static final boolean isChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
      return true;
    }
    return false;
  }

  /*
   * 判断变量类似是否为string
   */
  public static boolean judgeType(Object object) {
    if (object instanceof String) {
      return true;
    } else {
      return false;
    }
  }

}

