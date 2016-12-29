package com.wesai.test.WebService;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wesai.Util.JSONParser;
import com.wesai.Util.Md5Util;
import com.wesai.Util.ResponseData;
import com.wesai.WebService.ServiceRpc;

public class MulitThreadTestCart implements Runnable {

  static long totaltime = 0L;

  volatile static int SuccessNum = 0;
  static int num = 5;
  private static PrintStream mOut;

  private String name;



  // volatile static String AreaId = "f1f26f71cf5eee6c7f46307d0a6f9c23";
  // volatile static String PriceId = "e0bc08e4659c4650d07f2bd70c3665ab";
  // volatile static String OnlineId = "250310b4568dcf54cd258a28bbe491f6";
  // volatile static String ShowId = "10006140145863078";

  volatile static String AreaId = "ce7a99f2c0017afe831b71d5c5751479";
  volatile static String PriceId = "593172c79482a5920c1856e9508b2d03";
  volatile static String OnlineId = "42c6ce94797be6c6ed377e3d562021ba";
  volatile static String ShowId = "10004920146165183";

  static String userid = "1454462863899954";

  static String SeatId = null;

  static String Locksign = null;

  static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

  public MulitThreadTestCart(String name) {
    this.name = name;
  }


  public static String GetOneSeat(String AreaId)
  {
    // return SeatId =
    // getAreaValidSeats(AreaId).get(new Random().nextInt(getAreaValidSeats(AreaId).size()));
    return SeatId = getAreaValidSeats(AreaId).get(0);
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
    para.put(userId);
    para.put(OnlineId);
    para.put(ShowId);
    para.put(PriceId);
    para.put(new Random().nextInt(999));
    // Priceinfo.put(tmp);
    // para.put(Priceinfo);
    // para.put(userId);
    // para.put(timestamp);
    // para.put(sign);
    String response;
    try {
      response =
          ServiceRpc.getComtDetail("CartRpcWorker", "WechatCart", "addNoSeatTickets", userId, para);

        ResponseData res = JSONParser.ParseReturn(response);
      synchronized (this) {
        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + " "
              + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
          // increase();

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

  public void CreatOrder(String userid) {


    JSONArray para = new JSONArray();
    JSONObject OrderForm = new JSONObject();
    JSONObject PayMent = new JSONObject();
    JSONObject delivery = new JSONObject();
    JSONArray orderSeats = new JSONArray();


    delivery.put("deliveryMethod", "1");
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

    for (int i = 0; i < 5000; i++) {
      JSONObject tmp = new JSONObject();
      tmp.put("seatId", "");
      tmp.put("areaId", "");
      tmp.put("priceId", PriceId);
      tmp.put("packAgePriceId", "");
      orderSeats.put(tmp);
    }


    para.put(OrderForm);
    para.put(orderSeats);
    para.put(PayMent);
    para.put(delivery);

    // String uniodId = "1455775505526320";


    para.put(OrderForm);
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


    // SeatTest();

    NoSeatTest(this.name);
    // GetCart(userid);
    // CreatOrder(userid);

    // GetCart(userid);
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



    // GetOneSeat(AreaId);
    long phNum = 13000000000L;

      for (int i = 0; i < num; i++)

      {
      MulitThreadTestCart Thread = new MulitThreadTestCart(userid);
        new Thread(Thread).start();



      }


      while (Thread.activeCount() > 1)
      {
        Thread.yield();

      }

    GetCart(userid);
      System.out.println("The success num is :" + SuccessNum);

      System.out.println("The failed num is :" + (num - SuccessNum));


    }


}
