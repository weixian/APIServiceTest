package com.wesai.Util;

import org.json.JSONObject;



/**
 * ClassName:JSONParser <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年3月11日 下午4:03:17 <br/>
 *
 * @author weixian
 * @version
 * @see
 */
/**
 * ClassName: JSONParser <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: 2016年3月11日 下午4:03:17 <br/>
 *
 * @author weixian
 * @version
 */
public class JSONParser {


  public static ResponseData ParseReturn(String ReturnInfo) {
    ResponseData result = new ResponseData();
    JSONObject json = new JSONObject(ReturnInfo);

      result.setCode(Integer.parseInt(json.getString("code")));
      result.setMessage(json.getString("message"));
      result.setData(json.getString("data"));

    return result;
  }


}
