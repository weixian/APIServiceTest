/**
 * Project Name:WebService File Name:ServiceRpcTest.java Package Name:com.wesai.WebService
 * Date:2016年3月4日下午4:08:28
 *
 */
/**
 * Project Name:WebService File Name:ServiceRpcTest.java Package Name:com.wesai.WebService
 * Date:2016年3月4日下午4:08:28
 *
 */

package com.wesai.test.WebService;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wesai.WebService.ServiceRpc;

/**
 * ClassName:ServiceRpcTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年3月4日 下午4:08:28 <br/>
 *
 * @author weixian
 * @version
 * @see
 */
/**
 * ClassName: ServiceRpcTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: 2016年3月4日 下午4:08:28 <br/>
 *
 * @author weixian
 * @version
 */
public class ServiceRpcTest {

  /**
   * main:<br/>(Description) <br/>
   * TODO:

   * @author weixian

   * @version

   * @param

   * @return

   * @exception

   * @Example

   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    JSONArray para = new JSONArray();

    JSONArray seatinfo = new JSONArray();

    JSONObject tmp = new JSONObject();

//    para.put("0507d1617beca95ab5ab93ae4071d88d");
//    // para.put("10007057145551712");
//    para.put("7a003761027d431da6a16d72cd99ef06");
//
//    tmp.put("seatId", "7a003761027d431da6a16d72cd99ef06r1c6");
//    tmp.put("tempLockSign", "BiTvI");
//    seatinfo.put(tmp);
//
//

    tmp.put("doType", 0);
    tmp.put("itemId", "42c6ce94797be6c6ed377e3d562021ba");
    tmp.put("showId", "10004920146165183");
    // tmp.put(" businessId", "da9ec6d22d33d86ef72268c05e0cbf97");

    seatinfo.put(tmp);
    para.put(seatinfo);
    // para.put("10004920146165183");
    // para.put("false");
    try {
      String response1 =
          ServiceRpc.getComtDetail("ItemRpcWorker", "Item", "getItems", "userid", new JSONArray());

      String response =
          ServiceRpc.getComtDetail("ItemRpcWorker", "Show", "showDoPause", "userid", para);

      System.out.println(response);
      System.out.println(response1);

      // response = ServiceRpc.getComtDetail("ComRpcWorker", "Dictionary", "getDicByGroupName",
      // para);

      // System.out.println(response);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
