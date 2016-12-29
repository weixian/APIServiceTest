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
import com.wesai.Util.ResponseData;
import com.wesai.WebService.ServiceRpc;

public class MulitThreadTest implements Runnable {

  static long totaltime = 0L;

  volatile static int SuccessNum = 0;
  static int num = 1;
  private static PrintStream mOut;

  private String name;



  volatile static String AreaId = "ee7e8e908c8aab625cade247d1794379";
  volatile static String PriceId = "88ba0077a6a786aa8a0094fe5a449b98";
  volatile static String OnlineId = "250310b4568dcf54cd258a28bbe491f6";
  volatile static String ShowId = "10009439145931834";


  // volatile static String AreaId = "f1f26f71cf5eee6c7f46307d0a6f9c23";
  // volatile static String PriceId = "e0bc08e4659c4650d07f2bd70c3665ab";
  // volatile static String OnlineId = "250310b4568dcf54cd258a28bbe491f6";
  // volatile static String ShowId = "10006140145863078";

  static String SeatId = null;

  static String Locksign = null;

  static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

  public MulitThreadTest(String name) {
    this.name = name;
  }


  public static String GetOneSeat(String AreaId)
  {
    return SeatId =
        getAreaValidSeats(AreaId).get(new Random().nextInt(getAreaValidSeats(AreaId).size()));
    // return SeatId =
    // getAreaValidSeats(AreaId).get(0);
  }

  public static void TempLockOneSeat(String seatId) {

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


  public void SeatTest() {
    JSONArray para = new JSONArray();

    JSONArray seatinfo = new JSONArray();

    JSONObject tmp = new JSONObject();
    para.put(OnlineId);
    para.put(ShowId);
    para.put(AreaId);



    tmp.put("seatId", SeatId);
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
          System.out.println(Thread.currentThread().getName() + "    "
              + simpleDateFormat.format(System.currentTimeMillis()) + "  " + response);
          increase();
          seatinfo = new JSONObject(res.getData()).getJSONArray("seats");

          Locksign = seatinfo.getJSONObject(0).getString("tempLockSign");
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

  public void NoSeatTest() {


    JSONArray para = new JSONArray();

    JSONArray Priceinfo = new JSONArray();

    JSONObject tmp = new JSONObject();

    String serialId =
        System.currentTimeMillis() + String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

    para.put("2016032909281560356");

    para.put(ShowId);
    tmp.put("priceId", PriceId);
    tmp.put("qty", "2");
    Priceinfo.put(tmp);


    para.put(Priceinfo);
    String response;
    try {
      response =
          ServiceRpc.getComtDetail("ItemRpcWorker", "Price", "increasePriceStock", "userid", para);

      // System.out.println(Thread.currentThread().getName() + " "
      // + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);


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
      increase();
    }
  }

  public void PriceInfoTest() {


    JSONArray para = new JSONArray();

    JSONArray Priceinfo = new JSONArray();

    JSONObject tmp = new JSONObject();


    // para.put(System.currentTimeMillis() + String.valueOf((int) ((Math.random() * 9 + 1) *
    // 100000)));

    para.put("40a96062b0ce3518ba6a2ccf7cbed1b8");
    para.put("10004572145440819");
    // para.put("543f7a146a07f50d5729098c1ad3f10e");

    Priceinfo.put(tmp);


    // para.put(Priceinfo);
    String response;
    try {
      response =
          ServiceRpc.getComtDetail("ItemRpcWorker", "Price", "getItemPrices", "userid", para);

      // response = ServiceRpc.getComtDetail("ItemRpcWorker", "Seat", "getAreaValidSeats", para);

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
      increase();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      increase();
    }
  }

  public static ArrayList<String> getAreaSeats(String AreadId) {


    JSONArray para = new JSONArray();
    JSONArray seatlist = new JSONArray();
    ArrayList<String> seats = new ArrayList<String>();
    para.put(AreadId);

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
    // CancelTempLock("c963bfdb49922cbf8fbda1d6067257bdr2c18", "vVxgO");
    // NoSeatTest();

    // CancelLock("ee7e8e908c8aab625cade247d1794379r7c8");
    PriceInfoTest();

    // getAreaSeats();
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


    // Thread.sleep(10000);

    // for (int j = 0; j < 1; j++) {
    // ArrayList<String> Seats = getAreaValidSeats(AreaId);
    //
    // for (String str : Seats) {
    //
    //
    // SeatId = str;
    // for (int i = 0; i < num; i++) {
    // MulitThreadTest Thread = new MulitThreadTest("Thread--" + i);
    // new Thread(Thread).start();
    //
    // }
    //
    // while (Thread.activeCount() > 1) {
    // Thread.yield();
    // }
    // System.out.println("The success num is :" + SuccessNum);
    // System.out.println("The failed num is :" + (num - SuccessNum));
    // }
    //
    // }

      for (int i = 0; i < num; i++)

      {
        MulitThreadTest Thread = new MulitThreadTest("Thread--" + i);
        new Thread(Thread).start();

      }


      while (Thread.activeCount() > 1)
      {
        Thread.yield();

      }
      System.out.println("The success num is :" + SuccessNum);

      System.out.println("The failed num is :" + (num - SuccessNum));
    }


}
