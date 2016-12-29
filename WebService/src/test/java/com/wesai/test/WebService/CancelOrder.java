package com.wesai.test.WebService;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wesai.Util.JSONParser;
import com.wesai.Util.ResponseData;
import com.wesai.WebService.ServiceRpc;

public class CancelOrder implements Runnable {

  static long totaltime = 0L;

  volatile static int SuccessNum = 0;
  static int num = 10;
  private static PrintStream mOut;

  private String name;


  static String userid = "1454462863899954";
  static String businessid = "da9ec6d22d33d86ef72268c05e0cbf97";

  static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

  public CancelOrder(String name) {
    this.name = name;
  }


  public synchronized void increase() {

    // System.out.println(Thread.currentThread().getName() + " is locking!");

    SuccessNum++;

    // System.out.println(Thread.currentThread().getName() + " is unlocking!");

  }


  public void CancelOrderById(String orderid, String userid)
 {
    JSONArray para = new JSONArray();

    String response;
    para.put(orderid);

      try {
        response =
            ServiceRpc.getComtDetail("OrderRpcWorker", "Order", "userCancelOrder", userid, para);

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


  public void GetOrderDetail(String orderid, String userid) {
    JSONArray para = new JSONArray();

    String response;
    para.put(orderid);

    try {
      response =
 ServiceRpc.getComtDetail("OrderRpcWorker", "WechatOrder", "detail", userid, para);

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

  public static JSONArray getUserOrderList(String userid, int page) {
    JSONArray para = new JSONArray();
    JSONArray orderList = new JSONArray();
    String response;
    // para.put(userid);
    para.put(page);
    para.put("100");
    para.put("0");


      try {
        response =
 ServiceRpc.getComtDetail("OrderRpcWorker", "WechatOrder", "getUserOrderList",
            userid, para);

        ResponseData res = JSONParser.ParseReturn(response);

        if (res.getCode() == 200) {
          System.out.println(Thread.currentThread().getName() + " "
              + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
        JSONObject tmp = new JSONObject(res.getData());

        orderList = tmp.getJSONArray("lists");



        // increase();
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

    return orderList;
  }

  public static JSONArray getUserUnpaidOrderList(String userid, int page) {
    JSONArray para = new JSONArray();
    JSONArray orderList = new JSONArray();
    String response;
    // para.put(userid);
    para.put(page);
    para.put("100");
    para.put("2");


    try {
      response = ServiceRpc.getComtDetail("OrderRpcWorker", "WechatOrder", "getUserOrderList",
          userid, para);

      ResponseData res = JSONParser.ParseReturn(response);

      if (res.getCode() == 200) {
        System.out.println(Thread.currentThread().getName() + " "
            + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
        JSONObject tmp = new JSONObject(res.getData());

        JSONArray tmporderList = tmp.getJSONArray("lists");

        for(int i=0;i<tmporderList.length();i++)
        {
          if (tmporderList.getJSONObject(i).getString("paymentStatus").equals("0")
              && tmporderList.getJSONObject(i).getString("orderStatus").equals("0"))
 {
            orderList.put(tmporderList.getJSONObject(i));
          }


        }



        // increase();
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

    return orderList;
  }

  @Override
  public void run() {

    CancelOrderById(this.name, userid);
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MMdd_HH_mm_ss");
    Date currentTime = new Date();
    String dateString = formatter.format(currentTime);
    // mOut = new PrintStream(new FileOutputStream(dateString + "logs.txt"));

    // System.setOut(mOut);
    //
    // System.setErr(mOut);

    for (int k = 0; k < 300; k++) {
      JSONArray order = getUserUnpaidOrderList(userid, k);

      System.out.println(order);

      try {

        for (int j = 0; j < order.length(); j++) {
          CancelOrder Thread = new CancelOrder(order.getJSONObject(j).getString("orderId"));
          // new Thread(Thread).start();

          Thread.CancelOrderById(order.getJSONObject(j).getString("orderId"), userid);


          // Thread.GetOrderDetail("2016032909124575635", userid);

          // java.lang.Thread.sleep(1000);
        }



      } catch (JSONException e) {
        e.printStackTrace();
      }
    }


    // for (int j = 0; j < num; j++) {
    // CancelOrder Thread = new CancelOrder("2016042614423067025");
    // new Thread(Thread).start();
    // }


    while (Thread.activeCount() > 1)
      {

      // System.out.println(Thread.activeCount());
        Thread.yield();

      }
      System.out.println("The success num is :" + SuccessNum);

    System.out.println("The failed num is :" + (num - SuccessNum));

    // Thread.sleep(30000);
    }


}
