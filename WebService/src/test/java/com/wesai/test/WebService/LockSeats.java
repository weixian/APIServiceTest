package com.wesai.test.WebService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wesai.Util.JSONParser;
import com.wesai.Util.ResponseData;
import com.wesai.WebService.ServiceRpc;

public class LockSeats implements Runnable {

  static long totaltime = 0L;

  volatile static int SuccessNum = 0;
  static int num = 1;

  private String name;


  volatile static String AreaId = "745f66bdd09179f5093680d966b88834";
  volatile static String PriceId = "4226064ea8e60e3c4492ef5bffcc4c9d";
  volatile static String OnlineId = "357e789568139e77067c98f16437d5b7";
  volatile static String ShowId = "10007964145826917";

  String userid = "1454462863899954";
  static ArrayList<String> Seats = new ArrayList<String>();

  static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

  public LockSeats(String name) {
    this.name = name;
  }

  public synchronized void increase() {

    // System.out.println(Thread.currentThread().getName() + " is locking!");

    SuccessNum++;

    // System.out.println(Thread.currentThread().getName() + " is unlocking!");

  }
  public void SeatTest() {
    JSONArray para = new JSONArray();

    JSONArray seatinfo = new JSONArray();


    para.put(OnlineId);
    para.put(ShowId);
    para.put(AreaId);

    for (String str : Seats) {
      JSONObject tmp = new JSONObject();

      tmp.put("seatId", str);
      // tmp.put("tempLockSign", "");
      seatinfo.put(tmp);
    }



    System.out.println(seatinfo);

    // para.put(seatinfo);
    para.put(seatinfo);
    String response;
    try {

      response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "tempLock", userid, para);
      // response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "cancelLock", "userid", para);
      // response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "lock", para);

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



  public static ArrayList<String> getAreaSeats(String AreaId) {


    JSONArray para = new JSONArray();
    JSONArray seatlist = new JSONArray();
    ArrayList<String> seats = new ArrayList<String>();
    para.put(AreaId);

    String response;
    try {
      response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "getAreaSeats", "userid", para);

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
          ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "getAreaValidSeats", "userid", para);

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

  public void LockTest() {
    JSONArray para = new JSONArray();



    para.put("d0e4ffa5b45dcf85809eeb613364391er1c2");
    para.put(this.name);

    // para.put(seatinfo);
    String response;
    try {
      response = ServiceRpc.getComtDetail("TestLockcWorker", "Seat", "testLock", "userid", para);
      // response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "cancelLock", para);
      // response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "lock", para);

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

  @Override
  public void run() {

    SeatTest();

    // LockTest();
  }

  public static void main(String[] args) throws FileNotFoundException {
    // TODO Auto-generated method stub


    // String AreaId = "a3e35f5c1101c427755d71198ced96f3";

    Seats = getAreaValidSeats(AreaId);

    // System.out.println(Seats);

    for (int i = 0; i < num; i++) {
      LockSeats Thread = new LockSeats("Thread--" + i);
      new Thread(Thread).start();

    }


    while (Thread.activeCount() > 1) {
      Thread.yield();

    }
    System.out.println("The success  num is :" + SuccessNum);

    System.out.println("The failed num is :" + (num - SuccessNum));
  }




}
