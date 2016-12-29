package com.wesai.test.WebService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wesai.Util.JSONParser;
import com.wesai.Util.ResponseData;
import com.wesai.WebService.ServiceRpc;

public class LockAllSeats implements Runnable {

  static long totaltime = 0L;

  volatile static int SuccessNum = 0;
  static int num = 1;

  private String name;

  volatile static String AreaId = "18bd1e02d60261fa1b2ffe9acda4fd41";
  volatile static String PriceId = "03edb33ea024e54a5a8e7e50abc595d5";
  volatile static String OnlineId = "357e789568139e77067c98f16437d5b7";
  volatile static String ShowId = "10007964145826917";


  // volatile static String AreaId = "4fccb236b9f33f1b77effcc769388cc4";
  // volatile static String PriceId = "9b43b380314bbee056eae39753bec79e";
  // volatile static String OnlineId = "56e3915e14694060f721fe0c3c35dffb";
  // volatile static String ShowId = "10006393145647064";
  static String Locksign = null;
  static String SeatId = null;
  static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

  public LockAllSeats(String name) {
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

    JSONObject tmp = new JSONObject();
    para.put(OnlineId);
    para.put(ShowId);
    para.put(AreaId);

    tmp.put("seatId", this.name);
    tmp.put("tempLockSign", "");
    seatinfo.put(tmp);


    para.put(seatinfo);
    String response;


      try {
      response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "tempLock", "userid", para);
        // response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "cancelLock", para);
        // response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "lock", para);

        // response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "cancelTempLock", para);

        ResponseData res = JSONParser.ParseReturn(response);
        synchronized (this) {
          if (res.getCode() == 200) {
            System.out.println(Thread.currentThread().getName() + " "
                + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
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

  public void LockOneSeat(String seatId, String sign) {

    JSONArray para = new JSONArray();

    JSONArray seatinfo = new JSONArray();

    JSONObject tmp = new JSONObject();
    para.put(OnlineId);
    para.put(ShowId);
    para.put(AreaId);

    tmp.put("seatId", seatId);
    tmp.put("tempLockSign", sign);
    seatinfo.put(tmp);

    para.put(seatinfo);
    String response;
    try {
      response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "lock", "userid", para);

      ResponseData res = JSONParser.ParseReturn(response);
      if (res.getCode() == 200) {
        System.out.println(Thread.currentThread().getName() + "    "
            + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);

        increase();
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

  public static HashMap<String, String> TempLockOneArea(String areaId) {



    HashMap<String, String> locklist = new HashMap<String, String>();

    ArrayList<String> validSeatList = getAreaValidSeats(areaId);

    for (String str : validSeatList) {

      locklist.put(str, TempLockOneSeat(str));

    }

    return locklist;
  }



  public void CancelTempLock(String seatId, String sign) {
    JSONArray para = new JSONArray();

    JSONArray seatinfo = new JSONArray();

    JSONObject tmp = new JSONObject();
    para.put(OnlineId);
    para.put(ShowId);
    para.put(AreaId);



    tmp.put("seatId", seatId);
    tmp.put("tempLockSign", sign);
    seatinfo.put(tmp);

    para.put(seatinfo);
    String response;
    try {

      response =
          ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "cancelTempLock", "userid", para);

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

  public void CancelLock(String seatId) {
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
  @Override
  public void run() {

    // SeatTest();

    // LockOneSeat(SeatId, Locksign);


     String tmpsign = TempLockOneSeat(this.name);

    LockOneSeat(this.name, tmpsign);

    // TempLockOneSeat("ee7e8e908c8aab625cade247d1794379r13c1");

    // CancelTempLock(SeatId, Locksign);


    // CancelLock(this.name);
  }

  public static void main(String[] args) throws FileNotFoundException {
    // TODO Auto-generated method stub


    ArrayList<String> Seats = getAreaValidSeats(AreaId);
    SeatId = Seats.get(0);
    // TempLockOneSeat(SeatId);

    for (int i = 0; i < num; i++) {
      LockAllSeats Thread = new LockAllSeats(SeatId);
      new Thread(Thread).start();
      // try {
      // java.lang.Thread.sleep(100);
      // } catch (InterruptedException e) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }
    }


    while (Thread.activeCount() > 1) {
      Thread.yield();

    }
    System.out.println("The success  num is :" + SuccessNum);

    System.out.println("The failed num is :" + (num - SuccessNum));
    }




}
