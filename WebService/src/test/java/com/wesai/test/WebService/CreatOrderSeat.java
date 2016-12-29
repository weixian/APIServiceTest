package com.wesai.test.WebService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wesai.DataBase.Util.DBConnection;
import com.wesai.Util.JSONParser;
import com.wesai.Util.Md5Util;
import com.wesai.Util.ResponseData;
import com.wesai.WebService.ServiceRpc;




public class CreatOrderSeat implements Runnable {

  static long totaltime = 0L;

  volatile static int SuccessNum = 0;
  static int num = 10;
  private static PrintStream mOut;

  private String name;

  // volatile static String AreaId = "ce7a99f2c0017afe831b71d5c5751479";
  // volatile static String PriceId = "5b9a8752afec460e64c0acb45984bee1";
  // volatile static String OnlineId = "0507d1617beca95ab5ab93ae4071d88d";
  // volatile static String ShowId = "10003328145982601";

  volatile static String AreaId = "ce7a99f2c0017afe831b71d5c5751479";
  volatile static String PriceId = "eeada841a89785832f9e0fc3e4f941de";
  volatile static String OnlineId = "42c6ce94797be6c6ed377e3d562021ba";
  volatile static String ShowId = "10008927145612286";

  // volatile static String AreaId = "4e7a27b962a24dbb0091735619cf79c5";
  // volatile static String PriceId = "0cee8f3f0fee9cc3a204a38b5a6e5bdf";
  // volatile static String OnlineId = "a0014d51ec1a0716b0dabc7056056dd6";
  // volatile static String ShowId = "10006379145647199";

  static String userid = "1454577257909978";

  static String SeatId = null;

  static String Locksign = null;

  static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

  public CreatOrderSeat(String name) {
    this.name = name;
  }


  public static Map<String, String> GetPassportId() {
    Map<String, String> userlist = new HashMap<String, String>();

    DBConnection db = new DBConnection();
    db.prepareStatement("SELECT id,mobile FROM passport_user");

    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {
        userlist.put(rs.getString("id"), rs.getString("mobile"));
      }
      rs.close();
      db.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally{

    }


    return userlist;

  }


  public static String GetOneSeat(String AreaId)
  {
    // return SeatId =
    // getAreaValidSeats(AreaId).get(new Random().nextInt(getAreaValidSeats(AreaId).size()));
    return SeatId = getAreaValidSeats(AreaId).get(0);
  }

  public HashMap<String, String> TempLockOneArea(String areaId) {



    HashMap<String, String> locklist = new HashMap<String, String>();

    ArrayList<String> validSeatList = getAreaValidSeats(areaId);

    int i = new Random().nextInt(5) + 1;

    System.out.println(i);
    int j = 0;

    for (String str : validSeatList) {

      if (j < i) {
        String tmpsign = TempLockOneSeat(str);
        if (tmpsign != null) {
          locklist.put(str, tmpsign);
          j++;
        }
      }


      // System.out.println(str);
    }

    return locklist;
  }



  public void LockOneAreaSeats(String userid, HashMap<String, String> seatList) {
    JSONArray para = new JSONArray();

    JSONArray seatinfo = new JSONArray();


    para.put(OnlineId);
    para.put(ShowId);
    para.put(AreaId);

    Iterator<Entry<String, String>> iter = seatList.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<String, String> entry = iter.next();
      JSONObject tmp = new JSONObject();
      tmp.put("seatId", entry.getKey());
      tmp.put("tempLockSign", entry.getValue());
      seatinfo.put(tmp);
    }

    // para.put(seatinfo);
    para.put(seatinfo);
    String response;
    try {

      response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "lock", userid, para);

      // response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "cancelTempLock", para);

      ResponseData res = JSONParser.ParseReturn(response);
      synchronized (this) {
        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
          increase();

        } else {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
        }
      }

    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  public void CancelLockOneSeat(String seatId) {
    JSONArray para = new JSONArray();

    JSONArray seatinfo = new JSONArray();

    JSONObject tmp = new JSONObject();
    // para.put(OnlineId);
    para.put(ShowId);
    para.put(AreaId);



    tmp.put("seatId", seatId);
    // tmp.put("tempLockSign", "");
    seatinfo.put(tmp);

    para.put(seatinfo);
    String response;
    try {

      response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "cancelLock", "userid", para);

      ResponseData res = JSONParser.ParseReturn(response);
      synchronized (this) {
        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
          increase();

        } else {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
        }
      }

    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void CancelLockOneArea(String areaId) {


    ArrayList<String> validSeatList = getAreaSeats(areaId);

    for (String str : validSeatList) {

      CancelLockOneSeat(str);

      // System.out.println(str);
    }


  }

  public static String TempLockOneSeat(String seatId) {

    JSONArray para = new JSONArray();

    JSONArray seatinfo = new JSONArray();

    JSONObject tmp = new JSONObject();
    para.put(OnlineId);
    para.put(ShowId);
    para.put(AreaId);

    tmp.put("seatId", seatId);
    tmp.put("tempLockSign", "");
    seatinfo.put(tmp);

    para.put(seatinfo);
    String response;
    try {
      response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "tempLock", "userid", para);

      ResponseData res = JSONParser.ParseReturn(response);
      if (res.getCode() == 200) {
        System.out.println(Thread.currentThread().getName() + "    "
            + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);

        seatinfo = new JSONObject(res.getData()).getJSONArray("seats");

        Locksign = seatinfo.getJSONObject(0).getString("tempLockSign");

        // increase();


      } else {
        System.out.println(Thread.currentThread().getName() + "    "
            + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
      }

    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return Locksign;
  }

  public synchronized void increase() {

    // System.out.println(Thread.currentThread().getName() + " is locking!");

    SuccessNum++;

    // System.out.println(Thread.currentThread().getName() + " is unlocking!");

  }

  public static ArrayList<String> getAreaValidSeats(String AreaId) {


    JSONArray para = new JSONArray();
    JSONArray seatlist = new JSONArray();
    ArrayList<String> seats = new ArrayList<String>();
    para.put(OnlineId);
    para.put(PriceId);
    para.put(AreaId);
    String response;
    try {
      response =
          ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "getAreaValidSeats", userid, para);

      ResponseData res = JSONParser.ParseReturn(response);
      if (res.getCode() == 200) {
        System.out.println(Thread.currentThread().getName() + " "
            + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
      } else {
        System.out.println(Thread.currentThread().getName() + "    "
            + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
      }

      seatlist = new JSONObject(response).getJSONArray("data");

      for (int i = 0; i < seatlist.length(); i++) {
        seats.add(seatlist.getJSONObject(i).getString("id"));
      }


      // System.out.println(seats);

    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      // increase();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      // increase();
    }
    return seats;
  }


  public void SeatTest(String userId) {
    JSONArray para = new JSONArray();
    // String uniodId = "1455775505526320";
    long timestamp = System.currentTimeMillis();

    String sign = Md5Util.getMd5(timestamp + userId);
    para.put(OnlineId);
    para.put(ShowId);
    para.put(AreaId);
    para.put(SeatId);
    para.put(true);
    para.put(userId);
    para.put(timestamp);
    para.put(sign);

    String response;
    try {
      response = ServiceRpc.getComtDetail("CartRpcWorker", "Cart", "mtSeatTicket", userId, para);

      ResponseData res = JSONParser.ParseReturn(response);
      synchronized (this) {
        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);

          increase();
        } else {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
        }
      }

    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }





  public void NoSeatTest(String userId) {


    JSONArray para = new JSONArray();

    JSONArray Priceinfo = new JSONArray();

    JSONObject tmp = new JSONObject();
    long timestamp = System.currentTimeMillis();

    String sign = Md5Util.getMd5(timestamp + userId);
    para.put(OnlineId);
    para.put(ShowId);
    tmp.put("priceId", PriceId);
    tmp.put("num", "2147483647");
    Priceinfo.put(tmp);
    para.put(Priceinfo);
    para.put(userId);
    para.put(timestamp);
    para.put(sign);
    String response;
    try {
      response =
          ServiceRpc.getComtDetail("CartRpcWorker", "Cart", "addNoSeatTickets", userId, para);

        ResponseData res = JSONParser.ParseReturn(response);
      synchronized (this) {
        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + " "
              + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
          increase();

        } else {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
          // System.out.println(serialId);
        }
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      // increase();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  public static void GetCart(String userid) {


    JSONArray para = new JSONArray();
    // String uniodId = "1455775505526320";
    para.put(userid);
    String response;
    try {
      response = ServiceRpc.getComtDetail("CartRpcWorker", "Cart", "getCartDetail", userid, para);

      ResponseData res = JSONParser.ParseReturn(response);

        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + " "
              + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);

        } else {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
          // System.out.println(serialId);
        }

    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      // increase();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void CreatOrder(String userid, String seatid, String locksign) {


    JSONArray para = new JSONArray();
    JSONObject OrderForm = new JSONObject();
    JSONObject PayMent = new JSONObject();
    JSONObject delivery = new JSONObject();
    JSONArray orderSeats = new JSONArray();


    delivery.put("deliveryMethod", "3");
    delivery.put("receiveDeliveryPerson", "weixian");
    delivery.put("receiveDeliveryMobile", "15328050604");
    delivery.put("deliveryAddress", "test address");
    PayMent.put("paymentType","1");
    PayMent.put("moneyBaseFen", "10000");


    OrderForm.put("onlineId", OnlineId);
    OrderForm.put("showId", ShowId);
    OrderForm.put("order_sale_type", "0");
    OrderForm.put("business_id", "da9ec6d22d33d86ef72268c05e0cbf97");
    OrderForm.put("itemId", OnlineId);
    OrderForm.put("passport_user_mobile", "15328050604");
    OrderForm.put("passport_user_name", "test");
    OrderForm.put("isPackage", false);
    OrderForm.put("userId", userid);

    for (int i = 0; i < 1; i++) {
      JSONObject tmp = new JSONObject();
      tmp.put("seatId", seatid);
      tmp.put("areaId", AreaId);
      tmp.put("priceId", PriceId);
      tmp.put("packAgePriceId", "");
      tmp.put("tempLockSign", locksign);
      orderSeats.put(tmp);
    }

    // JSONObject tmp = new JSONObject();
    // tmp.put("seatId", "56c430a3ac8c7e3615f73efc905975a2r1c1");
    // tmp.put("areaId", "56c430a3ac8c7e3615f73efc905975a2");
    // tmp.put("priceId", "88a97f60f15f433a429c2abde6ffc368");
    // tmp.put("packAgePriceId", "");
    // tmp.put("tempLockSign", "iCrI2");
    // orderSeats.put(tmp);

    para.put(OrderForm);
    para.put(orderSeats);
    para.put(PayMent);
    para.put(delivery);

    // String uniodId = "1455775505526320";


    para.put(OrderForm);
    String response;

    try {
      response =
          ServiceRpc.getComtDetail("OrderRpcWorker", "WechatOrder", "createOrder", userid, para);

      ResponseData res = JSONParser.ParseReturn(response);
      synchronized (this) {
      if (res.getCode() == 200) {
        System.out.println(Thread.currentThread().getName() + " "
            + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
        increase();
      } else {
        System.out.println(Thread.currentThread().getName() + "    "
            + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
        // System.out.println(serialId);

          // System.exit(99);
      }
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      // increase();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public void CreatOrder(String userid, int num) {


    JSONArray para = new JSONArray();
    JSONObject OrderForm = new JSONObject();
    JSONObject PayMent = new JSONObject();
    JSONObject delivery = new JSONObject();
    JSONArray orderSeats = new JSONArray();


    delivery.put("deliveryMethod", "3");
    delivery.put("receiveDeliveryPerson", "weixian");
    delivery.put("receiveDeliveryMobile", "15328050604");
    delivery.put("deliveryAddress", "test address");

    PayMent.put("paymentType", "1");
    PayMent.put("moneyBaseFen", "10000");


    OrderForm.put("onlineId", OnlineId);
    OrderForm.put("showId", ShowId);
    OrderForm.put("order_sale_type", "0");
    OrderForm.put("order_src", "6");
    OrderForm.put("business_id", "da9ec6d22d33d86ef72268c05e0cbf97");
    OrderForm.put("itemId", OnlineId);
    OrderForm.put("passport_user_mobile", "15328050604");
    OrderForm.put("passport_user_name", "test");
    OrderForm.put("isPackage", false);
    OrderForm.put("userId", userid);

    for (int i = 0; i < num; i++) {
      JSONObject tmp = new JSONObject();
      tmp.put("seatId", "");
      tmp.put("areaId", "");
      tmp.put("priceId", PriceId);
      tmp.put("packAgePriceId", "");
      tmp.put("tempLockSign", "");
      orderSeats.put(tmp);
    }

    // JSONObject tmp = new JSONObject();
    // tmp.put("seatId", "8dd7b935e61e444673930c50002d4811r1c1");
    // tmp.put("areaId", AreaId);
    // tmp.put("priceId", PriceId);
    // tmp.put("packAgePriceId", "");
    // tmp.put("tempLockSign", "QKEBu");
    // orderSeats.put(tmp);

    para.put(OrderForm);
    para.put(orderSeats);
    para.put(PayMent);
    para.put(delivery);

    // String uniodId = "1455775505526320";


    // para.put(OrderForm);
    String response = null;
    try {
      response =
          ServiceRpc.getComtDetail("OrderRpcWorker", "WechatOrder", "createOrder", userid, para);
    } catch (JSONException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    System.out.println(response);

    ResponseData res = JSONParser.ParseReturn(response);

    synchronized (this) {
      try {

        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + " "
              + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
          increase();
        } else {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
          // System.out.println(serialId);
        }

      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        // increase();

      }
    }
  }

  public void CreatOrderByOp(String userid,String mobile, int num) {


    JSONArray para = new JSONArray();
    JSONObject OrderForm = new JSONObject();
    JSONObject PayMent = new JSONObject();
    JSONObject delivery = new JSONObject();
    JSONArray orderSeats = new JSONArray();


    delivery.put("deliveryMethod", "3");
    delivery.put("receiveDeliveryPerson", "weixian");
    delivery.put("receiveDeliveryMobile", "15328050604");
    delivery.put("deliveryAddress", "test address");

    PayMent.put("paymentType", "1");
    PayMent.put("moneyBaseFen", "10000");
    PayMent.put("paymentNo", "");


    OrderForm.put("onlineId", OnlineId);
    OrderForm.put("itemId", OnlineId);
    OrderForm.put("showId", ShowId);
    OrderForm.put("order_sale_type", "0");
    OrderForm.put("business_id", "da9ec6d22d33d86ef72268c05e0cbf97");
    OrderForm.put("businessUserId", "6bae7113a1daa9fecb9f9d9e6408cf7d");
    OrderForm.put("passport_user_mobile", mobile);
    OrderForm.put("passport_user_name", "test");
    OrderForm.put("isPackage", false);


    for (int i = 0; i < num; i++) {
      JSONObject tmp = new JSONObject();
      tmp.put("seatId", "");
      tmp.put("areaId", "");
      tmp.put("priceId", PriceId);
      tmp.put("packAgePriceId", "");
      tmp.put("tempLockSign", "");
      orderSeats.put(tmp);
    }

    // JSONObject tmp = new JSONObject();
    // tmp.put("seatId", "8dd7b935e61e444673930c50002d4811r1c1");
    // tmp.put("areaId", AreaId);
    // tmp.put("priceId", PriceId);
    // tmp.put("packAgePriceId", "");
    // tmp.put("tempLockSign", "QKEBu");
    // orderSeats.put(tmp);

    para.put(OrderForm);
    para.put(orderSeats);
    para.put(PayMent);
    // para.put(delivery);

    // String uniodId = "1455775505526320";


    // para.put(OrderForm);
    String response;

      try {
        response =
            ServiceRpc.getComtDetail("OrderRpcWorker", "OpOrder", "createOrder", userid, para);

        ResponseData res = JSONParser.ParseReturn(response);
        synchronized (this) {
        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + " "
              + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
          increase();
        } else {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
          // System.out.println(serialId);
        }
      }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        // increase();

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

  }


  public void CreatOrder(String userid, HashMap<String, String> seatList) {


    JSONArray para = new JSONArray();
    JSONObject OrderForm = new JSONObject();
    JSONObject PayMent = new JSONObject();
    JSONObject delivery = new JSONObject();
    JSONArray orderSeats = new JSONArray();


    delivery.put("deliveryMethod", "1");
    delivery.put("receiveDeliveryPerson", "weixian");
    delivery.put("receiveDeliveryMobile", "15328050604");
    delivery.put("deliveryAddress", "test address");

    PayMent.put("paymentType", "1");
    PayMent.put("moneyBaseFen", "10000");


    OrderForm.put("onlineId", OnlineId);
    OrderForm.put("showId", ShowId);
    OrderForm.put("order_sale_type", "0");
    OrderForm.put("business_id", "da9ec6d22d33d86ef72268c05e0cbf97");
    OrderForm.put("itemId", OnlineId);
    OrderForm.put("passport_user_mobile", "15328050604");
    OrderForm.put("passport_user_name", "test");
    OrderForm.put("isPackage", false);
    OrderForm.put("userId", userid);


    Iterator<Entry<String, String>> iter = seatList.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<String, String> entry = iter.next();
      JSONObject tmp = new JSONObject();
      tmp.put("seatId", entry.getKey());
      tmp.put("areaId", AreaId);
      tmp.put("priceId", PriceId);
      tmp.put("packAgePriceId", "");
      tmp.put("tempLockSign", entry.getValue());
      orderSeats.put(tmp);

    }


    // JSONObject tmp = new JSONObject();
    // tmp.put("seatId", "8dd7b935e61e444673930c50002d4811r1c1");
    // tmp.put("areaId", AreaId);
    // tmp.put("priceId", PriceId);
    // tmp.put("packAgePriceId", "");
    // tmp.put("tempLockSign", "QKEBu");
    // orderSeats.put(tmp);

    para.put(OrderForm);
    para.put(orderSeats);
    para.put(PayMent);
    para.put(delivery);

    // String uniodId = "1455775505526320";


    // para.put(OrderForm);
    String response;
    synchronized (this) {
      try {
        response =
            ServiceRpc.getComtDetail("OrderRpcWorker", "WechatOrder", "createOrder", userid, para);

        ResponseData res = JSONParser.ParseReturn(response);

        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + " "
              + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
          increase();
        } else {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
          // System.out.println(serialId);
        }

      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        // increase();

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void CreatOrderByOp(String userid, String mobile, HashMap<String, String> seatList) {

    JSONArray para = new JSONArray();
    JSONObject OrderForm = new JSONObject();
    JSONObject PayMent = new JSONObject();
    JSONObject delivery = new JSONObject();
    JSONArray orderSeats = new JSONArray();


    delivery.put("deliveryMethod", "1");
    delivery.put("receiveDeliveryPerson", "weixian");
    delivery.put("receiveDeliveryMobile", "15328050604");
    delivery.put("deliveryAddress", "test address");

    PayMent.put("paymentType", "1");
    PayMent.put("moneyBaseFen", "10000");


    OrderForm.put("onlineId", OnlineId);
    OrderForm.put("itemId", OnlineId);
    OrderForm.put("showId", ShowId);
    OrderForm.put("order_sale_type", "0");
    OrderForm.put("business_id", "da9ec6d22d33d86ef72268c05e0cbf97");
    OrderForm.put("businessUserId", "6bae7113a1daa9fecb9f9d9e6408cf7d");
    OrderForm.put("passport_user_mobile", mobile);
    OrderForm.put("passport_user_name", "test");
    OrderForm.put("isPackage", false);


    Iterator<Entry<String, String>> iter = seatList.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<String, String> entry = iter.next();
      JSONObject tmp = new JSONObject();
      tmp.put("seatId", entry.getKey());
      tmp.put("areaId", AreaId);
      tmp.put("priceId", PriceId);
      tmp.put("packAgePriceId", "");
      tmp.put("tempLockSign", entry.getValue());
      orderSeats.put(tmp);

    }

    // JSONObject tmp = new JSONObject();
    // tmp.put("seatId", "8dd7b935e61e444673930c50002d4811r1c1");
    // tmp.put("areaId", AreaId);
    // tmp.put("priceId", PriceId);
    // tmp.put("packAgePriceId", "");
    // tmp.put("tempLockSign", "QKEBu");
    // orderSeats.put(tmp);

    para.put(OrderForm);
    para.put(orderSeats);
    para.put(PayMent);
    para.put(delivery);

    // String uniodId = "1455775505526320";


    String response;
    synchronized (this) {
      try {
        response =
            ServiceRpc.getComtDetail("OrderRpcWorker", "OpOrder", "createOrder", userid, para);

        ResponseData res = JSONParser.ParseReturn(response);

        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + " "
              + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
          increase();
        } else {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
          // System.out.println(serialId);
        }

      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        // increase();

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }


  public static ArrayList<String> getAreaSeats(String AreadId) {


    JSONArray para = new JSONArray();
    JSONArray seatlist = new JSONArray();
    ArrayList<String> seats = new ArrayList<String>();
    para.put(AreadId);

    String response;
    try {
      response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "getAreaSeats", userid, para);

      ResponseData res = JSONParser.ParseReturn(response);
        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + " "
              + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);

        } else {
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
        }

        seatlist = new JSONObject(response).getJSONArray("data");

        for (int i = 0; i < seatlist.length(); i++) {
          seats.add(seatlist.getJSONObject(i).getString("id"));
        }


      // System.out.println(seats);

    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      // increase();
    }
    return seats;
  }
  @Override
  public void run() {
    // NoSeatTest(this.name);
    // GetCart(this.name);
    // CancelLockOneArea(AreaId);
    // String tmpsign = "";
    // tmpsign = TempLockOneSeat(this.name);

    // CreatOrder(userid, this.name, TempLockOneSeat(this.name));

    // LockOneAreaSeats(userid, TempLockOneArea(AreaId));

    // ArrayList<String> usesr = GetPassportId();

    // CancelLockOneArea(AreaId);

    // CreatOrderByOp(this.name, "15328050604", 1);
    // CreatOrderByOp(userid, "15328050604", TempLockOneArea(AreaId));
    CreatOrder(this.name, 1);
    // CreatOrder(userid, TempLockOneArea(AreaId));

  }

  public static void main(String[] args) throws FileNotFoundException, InterruptedException {
    // TODO Auto-generated method stub
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MMdd_HH_mm_ss");
    Date currentTime = new Date();
    String dateString = formatter.format(currentTime);
    // mOut = new PrintStream(new FileOutputStream(dateString + "logs.txt"));

    // System.setOut(mOut);
    //
    // System.setErr(mOut);



    // TempLockOneSeat(GetOneSeat(AreaId));

    Map<String, String> users = GetPassportId();
    // // System.out.println(GetOneSeat(AreaId));
    //
    for (int i = 0; i < num; i++) {
      CreatOrderSeat thread = new CreatOrderSeat(userid);

      new Thread(thread).start();

      // Thread.sleep(1000);

    }
    // CreatOrderSeat thread = new CreatOrderSeat("1");
    // Iterator<Entry<String, String>> iter = users.entrySet().iterator();
    // while (iter.hasNext()) {
    // Entry<String, String> entry = iter.next();
    // // CreatOrderSeat Thread = new CreatOrderSeat("123");
    // // thread.CreatOrderByOp(entry.getKey(), entry.getValue(), 20);
    //
    // thread.CreatOrder(userid, 20);
    //
    // }


    // ArrayList<String> Seats = getAreaValidSeats(AreaId);
    //
    // for (String str : Seats) {
    // CreatOrderSeat Thread = new CreatOrderSeat(str);
    // new Thread(Thread).start();
    // java.lang.Thread.sleep(500);
    // }
    //
    while (Thread.activeCount() > 2) {

      // System.out.println(Thread.currentThread());
      Thread.yield();

    }

    System.out.println("The success num is :" + SuccessNum);
    System.out.println("The failed num is :" + (num - SuccessNum));


    // GetCart(users.get(0));

  }
}
