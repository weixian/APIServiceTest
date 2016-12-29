/**
 * Project Name:JmInterfaceTest File Name:ComRpc.java Package Name:controller
 * Date:2016年3月2日下午5:33:08
 *
 */
/**
 * Project Name:JmInterfaceTest File Name:ComRpc.java Package Name:controller
 * Date:2016年3月2日下午5:33:08
 *
 */

package com.wesai.WebService;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wesai.Util.ConfigHelper;


public class ServiceRpc {

  public static String configpath = "./config/Config_ChengduLocal.xml";

  // public static String configpath = "./config/Config.xml";


  public static String getComtDetail(String Service, String classname, String method,
 String userId,
      JSONArray para) throws IOException, JSONException {


    JSONObject ServiceInfo = ConfigHelper.GetServiceRunInfo(configpath, Service);
    // 实例化socket协议客户端
    BasicController basicController =
        new BasicController(ServiceInfo.getString("host"), ServiceInfo.getInt("port"));
    // 调用socket客户端进行通信
    return basicController.getSocketStream("Test", userId, "{1BA09530-F9E6-478D-9965-7EB31A59537E}",
        classname, method, para);

  }

}

