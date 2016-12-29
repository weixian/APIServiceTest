package com.wesai.RpcClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wesai.Util.ConfigHelper;
import com.wesai.Util.Md5Util;
import com.wesai.WebService.ServiceRpc;


public class Packet {
  /*
   * 封装发送数据体。文本协议就4行：命令长度\n命令\n数据长度\n数据
   *
   * @param command: 命令
   *
   * @param databody: 数据体
   *
   * @return data: 返回数据
   */
  public static JSONObject getDataBody(String user, String password, String userId,
      String className, String methodName, JSONArray args) throws JSONException {

    // 初始化jsonObject
    JSONObject jsonObject = new JSONObject();
    JSONObject AppInfo = new JSONObject();
    JSONObject accessdata = new JSONObject();

    long timestamp = System.currentTimeMillis();
    accessdata.put("user", user);
    accessdata.put("password", Md5Util.getMd5(user + password + timestamp));
    accessdata.put("timestamp", timestamp);
    AppInfo.put("id", ConfigHelper.GetAppAccessId(ServiceRpc.configpath));
    accessdata.put("app", AppInfo);



    JSONObject env = new JSONObject();
    env.put("X_CLIENT_IP", "127.0.0.1");
    env.put("X_USER_ID", userId);
    env.put("X_OPEN_ID", "xman");
    env.put("X_MOVIE_OPEN_ID", "xman");
    // env.put("X_IS_REFLECT_GETOR", true);
    // env.put("X_WECHAT_TOKEN", "d0a171c89b63d87c75835ddc74f37619");

    // 添加json


    jsonObject.put("version", "1.0");
    jsonObject.put("access", accessdata);
    jsonObject.put("class", className);
    jsonObject.put("method", methodName);
    jsonObject.put("param_array", args);
    jsonObject.put("env", env);


    return jsonObject;
  }

  /*
   * 格式化pkg
   *
   * @param command: 协议命令
   *
   * @param pkg: 协议中数据体
   *
   * @return data: 格式化后的字符串
   */
  public static String formatPkg(String command, String pkg) {
    StringBuilder stringBuilder = new StringBuilder();
    // stringBuilder.append(3 + "\n");
    // stringBuilder.append(command + "\n");
    // stringBuilder.append(pkg.length() + "\n");
    stringBuilder.append(pkg);

    stringBuilder.append(".");
    return stringBuilder.toString();
  }

  /*
   * 将pkg转换成jsonObject
   *
   * @param pkg: 协议中数据体
   *
   * @return: json对象
   */
  public static JSONObject getJsonObject(String pkg) throws JSONException {
    JSONObject jsonObject = new JSONObject(pkg);

    return jsonObject;
  }

}
