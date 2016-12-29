package com.wesai.test.WebService;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wesai.DataBase.Util.DBConnection;
import com.wesai.Util.JSONParser;
import com.wesai.Util.ResponseData;
import com.wesai.WebService.ServiceRpc;

public class RefundlOrder implements Runnable {

  static long totaltime = 0L;

  volatile static int SuccessNum = 0;
  static int num = 20;
  private static PrintStream mOut;

  private String name;

  private JSONArray json;
  static String userid = "1454462863899954";
  static String businessid = "da9ec6d22d33d86ef72268c05e0cbf97";

  // static String orderId = null;
  static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

  public RefundlOrder(String name) {
    this.name = name;
  }

  public RefundlOrder(JSONArray json) {
    this.json = json;
  }

  public RefundlOrder(String name, JSONArray json) {
    this.json = json;
    this.name = name;
  }

  public synchronized void increase() {

    // System.out.println(Thread.currentThread().getName() + " is locking!");

    SuccessNum++;

    // System.out.println(Thread.currentThread().getName() + " is unlocking!");

  }

  public static ArrayList<String> GetTicketsByOrderId(String orderId) {
    ArrayList<String> TicketsList = new ArrayList<String>();

    DBConnection db = new DBConnection();
    db.prepareStatement("SELECT ticket_id FROM order_ticket where order_id=? and ticket_status=2");

    db.setString(1, orderId);

    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {
        TicketsList.add(rs.getString("ticket_id"));
      }
      rs.close();
      db.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {

    }


    return TicketsList;

  }

  public static ArrayList<String> GetPaidOrdersByuserId(String userId) {
    ArrayList<String> TicketsList = new ArrayList<String>();

    DBConnection db = new DBConnection();
    db.prepareStatement(
        "SELECT order_id FROM order_form where passport_user_id=? and payment_status=1 and order_status=0 order by create_time desc");

    db.setString(1, userId);

    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {
        TicketsList.add(rs.getString("order_id"));
      }
      rs.close();
      db.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {

    }


    return TicketsList;

  }



  public String RefundTickets(String orderid, JSONArray seatlist, String userid) {
    JSONArray para = new JSONArray();
    String batchId = null;
    String response;
    para.put(businessid);
    para.put(orderid);
    para.put(seatlist);
    para.put("退一把试试");
    para.put("weixian_test@wepiao.com");
    para.put("6bae7113a1daa9fecb9f9d9e6408cf7d");
    para.put("172.16.129.83");
    try {
      response =
          ServiceRpc.getComtDetail("OrderRpcWorker", "Refund", "refundTickets", userid, para);

      ResponseData res = JSONParser.ParseReturn(response);
      synchronized (this) {
        if (res.getCode() == 200) {

          increase();

          batchId = new JSONObject(res.getData()).getString("batchId");
        }
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

    return batchId;

  }

  public void RefundFee(String batchId, String userid) {
    JSONArray para = new JSONArray();

    String response;
    para.put(businessid);
    para.put(batchId);
    para.put("1");
    para.put("1");
    // para.put("如果我的长度太长的话,你说加奥会不会奔溃呢，同时我还有xss在里面哟,我真的只是来看你你处理了没有？<script>alter\"xss\"</script>");
    para.put("weixian_test@wepiao.com");
    para.put("6bae7113a1daa9fecb9f9d9e6408cf7d");
    para.put("172.16.129.83");
    try {
      response =
 ServiceRpc.getComtDetail("OrderRpcWorker", "Refund", "refundFee", userid, para);

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

  public static JSONArray getUserPaidOrderList(String userid, int page) {
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
          if (tmporderList.getJSONObject(i).getString("paymentStatus").equals("1")
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


    // RefundTickets(orderId, this.json, userid);
    RefundTickets(this.name, this.json, userid);

    // RefundFee(RefundTickets(this.name, this.json, userid), userid);
    // RefundTickets(orderId, tickets, userid);
  }

  public static void main(String[] args) throws InterruptedException {
    // TODO Auto-generated method stub
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MMdd_HH_mm_ss");
    Date currentTime = new Date();
    String dateString = formatter.format(currentTime);
    // mOut = new PrintStream(new FileOutputStream(dateString + "logs.txt"));

    // System.setOut(mOut);
    //
    // System.setErr(mOut);

    //

    ArrayList<String> orderList = GetPaidOrdersByuserId(userid);

    System.out.println(orderList);
    for (int i = 0; i < num; i++) {
      String orderId = orderList.get(i);
      JSONArray tickets = new JSONArray();
      ArrayList<String> ticketsList = GetTicketsByOrderId(orderId);

      for (String str : ticketsList) {
        tickets.put(str);
        // JSONArray tmp = new JSONArray();
        // tmp.put(str);

      }

      RefundlOrder thread = new RefundlOrder(orderId, tickets);
      // thread.RefundFee(thread.RefundTickets(orderId, tmp, userid), userid);
      new Thread(thread).start();

      // Thread.sleep(3000);
    }


// RefundlOrder thread = new RefundlOrder(tickets);
    //
    // // thread.RefundFee(thread.RefundTickets(orderId, tmp, userid), userid);
    // new Thread(thread).start();
    // try {
    // Thread.sleep(3000);
    // } catch (InterruptedException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

    // new Thread(thread).start();


    // String batchId = "201604181026439670";
    //
    // for (int j = 0; j < num; j++) {
    //
    // RefundlOrder Thread = new RefundlOrder(batchId);
    //
    // new Thread(Thread).start();
    //
    // // Thread.RefundFee("201604181026434354", userid);
    //
    // }


    while (Thread.activeCount() > 2)
      {

      // System.out.println(Thread.activeCount());
        Thread.yield();

      }
      System.out.println("The success num is :" + SuccessNum);

    System.out.println("The failed num is :" + (num - SuccessNum));

    // Thread.sleep(30000);
    }


}
