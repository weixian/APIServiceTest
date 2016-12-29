

package com.wesai.test.WebService;

import com.wesai.Util.ConfigHelper;

public class TestConfigHelper {

  public static void main(String[] args){
    System.out.println(ConfigHelper.GetServiceRunInfo("./config/Config.xml", "UserRpcWorker"));
  }



}
