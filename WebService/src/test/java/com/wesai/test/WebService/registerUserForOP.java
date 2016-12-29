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

public class registerUserForOP implements Runnable {

  static long totaltime = 0L;

  volatile static int SuccessNum = 0;
  static int num = 1;
  private static PrintStream mOut;

  private String name;


  static String userid = "1454462863899954";


  static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

  public registerUserForOP(String name) {
    this.name = name;
  }


  public synchronized void increase() {

    // System.out.println(Thread.currentThread().getName() + " is locking!");

    SuccessNum++;

    // System.out.println(Thread.currentThread().getName() + " is unlocking!");

  }

  public void registerUserforOp(String businessId, String mobile)
 {
    JSONArray para = new JSONArray();

    String response;
    para.put(businessId);
    para.put(mobile);

      try {
        response =
            ServiceRpc.getComtDetail("UserRpcWorker", "User", "registerUserForOp", userid, para);

        ResponseData res = JSONParser.ParseReturn(response);
      synchronized (this) {
        if (res.getCode() == 200) {
          increase();
        }

        System.out.println(Thread.currentThread().getName() + " "
            + simpleDateFormat.format(System.currentTimeMillis()) + " " + response);
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

  public void registerUser(String businessId, String mobile) {
    JSONArray para = new JSONArray();
    JSONObject tmp = new JSONObject();
    String response;
    tmp.put("business_id", businessId);
    tmp.put("mobile", mobile);

    para.put(tmp);
    synchronized (this) {
      try {
        response =
            ServiceRpc.getComtDetail("UserRpcWorker", "WxUser", "registerUser", userid, para);

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


  @Override
  public void run() {

    registerUserforOp("da9ec6d22d33d86ef72268c05e0cbf97", this.name);

    // registerUser("da9ec6d22d33d86ef72268c05e0cbf97", this.name);
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

    Long mobile = 15000000002L;

    for (int j = 0; j < num; j++) {
      registerUserForOP Thread = new registerUserForOP(String.valueOf(("11111111111")));

      // Thread.registerUserforOp("da9ec6d22d33d86ef72268c05e0cbf97", String.valueOf((mobile + j)));
      new Thread(Thread).start();


          // java.lang.Thread.sleep(1000);
        }





    //
    // for (int j = 0; j < num; j++) {
    // CancelOrder Thread = new CancelOrder("1 2 3");
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
