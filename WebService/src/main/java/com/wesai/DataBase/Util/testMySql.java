package com.wesai.DataBase.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





public class testMySql {

  static DBConnection db = new DBConnection();


  // ȡ������Ŀ�Ŀ����
  static long GetNoSeatItemStockNum(String itemid) {
    long num = 0L;

    db.prepareStatement("SELECT SUM(stock_num) as totalnum FROM item_basis_prices WHERE item_id=?");
    db.setString(1, itemid);
    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {
        num += rs.getLong("totalnum");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return num;

  }

  // ȡ�������۵�����
  static long GetNoSeatItemSaledNum(String itemid) {
    long num = 0L;

    db.prepareStatement("SELECT count(*) as totalnum FROM order_ticket WHERE item_id=?");
    db.setString(1, itemid);
    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {
        num += rs.getLong("totalnum");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return num;

  }

  static JSONArray GetNoSeatItemScreenInfo(String ScreenId) {

    JSONArray ScreenInfo = new JSONArray();


    ArrayList<String> PriceList = GetPriceListByScreenId(ScreenId);
    int i = 0;
    for (String str : PriceList) {


      try {
        ScreenInfo.put(i, GetNoSeatItemPriceInfo(str));
        i++;
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return ScreenInfo;

  }

  static JSONArray GetHasSeatItemScreenInfo(String ScreenId) {

    JSONArray ScreenInfo = new JSONArray();


    ArrayList<String> PriceList = GetPriceListByScreenId(ScreenId);
    int i = 0;
    for (String str : PriceList) {


      try {
        ScreenInfo.put(i, GetHasSeatItemPriceInfo(str));
        i++;
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return ScreenInfo;

  }

  static JSONObject GetNoSeatItemInfo(String ItemId) {

    JSONObject ItemInfo = new JSONObject();

    ArrayList<String> ScreenList = GetScreensList(ItemId);

    for (String str : ScreenList) {

      try {
        ItemInfo.put(GetScreenNameById(str), GetNoSeatItemScreenInfo(str));
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return ItemInfo;

  }


  static JSONObject GetHasSeatItemInfo(String ItemId) {

    JSONObject ItemInfo = new JSONObject();

    ArrayList<String> ScreenList = GetScreensList(ItemId);
    int i = 1;
    System.out.println(ScreenList);
    for (String str : ScreenList) {

      System.out.println("���Σ�" + str);
      System.out.println(GetHasSeatItemScreenInfo(str));
      try {
        ItemInfo.put(i + ": " + GetScreenNameById(str), GetHasSeatItemScreenInfo(str));

        i++;
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return ItemInfo;

  }

  static JSONObject GetNoSeatItemPriceInfo(String PriceId) {

    JSONObject PriceInfo = new JSONObject();

    db.prepareStatement(
        "SELECT id,stock_num,show_name,bp_type,rmb_online_price  FROM item_basis_prices WHERE id=?");
    db.setString(1, PriceId);
    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {
        PriceInfo.put("PricID", PriceId);
        PriceInfo.put("Ʊ������", rs.getString("show_name"));
        PriceInfo.put("����Ʊ��", rs.getString("stock_num"));
        PriceInfo.put("Ʊ��", (rs.getInt("rmb_online_price")) / 100.0);
        // System.out.println((float) rs.getInt("rmb_online_price"));
      }

    } catch (SQLException | JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


    db.prepareStatement(
        "SELECT count(*) as totalnum FROM order_ticket WHERE price_id=? and ticket_status!=5");
    db.setString(1, PriceId);

    rs = db.executeQuery();

    try {
      while (rs.next()) {


        PriceInfo.put("��������", rs.getString("totalnum"));
        Float slaedPrice = Float.parseFloat(PriceInfo.getString(("Ʊ��"))) * rs.getInt("totalnum");
        PriceInfo.put("���۽��", slaedPrice);

        // System.out.println(PriceId);
        // System.out.println("�����ܶ" + slaedPrice);
        // System.out.println("Ʊ�ۣ�" + Float.parseFloat(PriceInfo.getString(("Ʊ��"))));
        // System.out.println("���ۣ�" + PriceInfo.getInt("��������"));
        // System.out.println("�ܺͣ�" + (PriceInfo.getInt("��������") + PriceInfo.getInt("����Ʊ��")));
      }


      String SaledRate =
          String
              .valueOf(PriceInfo.getInt("��������")
                  / ((float) PriceInfo.getInt("��������") + (float) PriceInfo.getInt("����Ʊ��")) * 100)
          + "%";

      PriceInfo.put("���۱���", SaledRate);

    } catch (SQLException | JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


    return PriceInfo;

  }


  // seat_status: 0 ������1 ���� ��2 Ԥ�� ��3 ����
  // ticket_status:1 ��ѡ , 2 ��ѡ, 3 �ѳ�Ʊ ,4,�ش�Ʊ 5,��Ʊ
  static JSONObject GetHasSeatItemPriceInfo(String PriceId) {

    JSONObject PriceInfo = new JSONObject();

    db.prepareStatement(
        "SELECT id,stock_num,show_name,bp_type,rmb_online_price  FROM item_basis_prices WHERE id=?");
    db.setString(1, PriceId);
    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {
        PriceInfo.put("PricID", PriceId);

        PriceInfo.put("Ʊ������", rs.getString("show_name"));

        // PriceInfo.put("����Ʊ��", rs.getString("stock_num"));

        PriceInfo.put("Ʊ��", (rs.getInt("rmb_online_price")) / 100.0);
      }

    } catch (SQLException | JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


    db.prepareStatement(
        "SELECT COUNT(*) AS SaledSeatsNum  FROM item_seats WHERE basis_prices_id=? and seat_status=3");
    db.setString(1, PriceId);

    rs = db.executeQuery();

    try {
      while (rs.next()) {

        // System.out.println(PriceId);


        PriceInfo.put("��������", rs.getInt("SaledSeatsNum"));

        Float slaedPrice =
            Float.parseFloat(PriceInfo.getString(("Ʊ��"))) * rs.getInt("SaledSeatsNum");

        // System.out
        // .println(rs.getInt("SaledSeatsNum") * Float.parseFloat(PriceInfo.getString(("Ʊ��"))));


        PriceInfo.put("���۽��", slaedPrice);
      }
    } catch (SQLException | JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    db.prepareStatement(
        "SELECT COUNT(*) AS UnSaledSeatsNum  FROM item_seats WHERE basis_prices_id=? and seat_status=1");
    db.setString(1, PriceId);

    rs = db.executeQuery();

    try {
      while (rs.next()) {

        // System.out.println(PriceId);


        PriceInfo.put("��������", rs.getInt("UnSaledSeatsNum"));

      }
    } catch (SQLException | JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }



    db.prepareStatement("SELECT COUNT(*) AS SeatsNum  FROM item_seats WHERE basis_prices_id=?");
    db.setString(1, PriceId);

    rs = db.executeQuery();

    int SeatsNum = 0;

    try {
      while (rs.next()) {

        // System.out.println(PriceId);

        SeatsNum = rs.getInt("SeatsNum");

      }

      // System.out.println("���ۣ�" + (float) PriceInfo.getInt("��������"));
      // System.out
      // .println("�ܺͣ�" + ((float) PriceInfo.getInt("��������") + (float) PriceInfo.getInt("����Ʊ��")));

      String SaledRate =
          String.valueOf((float) PriceInfo.getInt("��������") / (float) SeatsNum * 100) + "%";

      PriceInfo.put("���۱���", SaledRate);

    } catch (SQLException | JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


    return PriceInfo;

  }

  static String GetItemVoteType(String itemid) {
    String type = null;

    db.prepareStatement("SELECT vote_type  FROM item_info WHERE id=?");
    db.setString(1, itemid);
    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {
        type = rs.getString("vote_type");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return type;

  }

  // ������Ŀ��Ʊ�����ڿ��+����
  static long GetTicketTotalNum(String itemid) {

    if (!GetItemVoteType(itemid).equals("1")) {
      return GetNoSeatItemSaledNum(itemid) + GetNoSeatItemStockNum(itemid);
    }
    return GetSeatsNums(itemid);

  }



  static long GetSeatsNums(String itemid) {

    long num = 0L;

    ArrayList<String> screens = GetScreensList(itemid);

    for (String str : screens) {

      db.prepareStatement(
          "select count(*) from item_seats where screenings_id=? and seat_status!=0");

      db.setString(1, str);

      ResultSet rs = db.executeQuery();

      try {
        while (rs.next()) {

          System.out.println(str);

          System.out.println(rs.getLong("count(*)"));
          num += rs.getLong("count(*)");

        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }

    return num;

  }


  static int GetScreensNum(String itemid) {


    return GetScreensList(itemid).size();

  }

  static ArrayList<String> GetScreensList(String itemid) {


    ArrayList<String> screens = new ArrayList<String>();

    db.prepareStatement("select id from item_screenings where item_id=? and is_visible!=0");

    db.setString(1, itemid);


    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {

        screens.add(rs.getString("id"));

      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return screens;

  }

  static String GetScreenNameById(String ScreenId) {


    String title_cn = null;

    db.prepareStatement("select title_cn from item_screenings where id=?");

    db.setString(1, ScreenId);


    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {

        title_cn = rs.getString("title_cn");

      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return title_cn;

  }



  static ArrayList<String> GetPriceListByScreenId(String ScreenId) {


    ArrayList<String> PriceList = new ArrayList<String>();

    db.prepareStatement("select id from item_basis_prices where screenings_id=?");

    db.setString(1, ScreenId);


    ResultSet rs = db.executeQuery();

    try {
      while (rs.next()) {

        PriceList.add(rs.getString("id"));

      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return PriceList;
  }

  public static void main(String[] args) {

    // DBConnection db=new DBConnection();
    //
    System.out.println(GetTicketTotalNum("0507d1617beca95ab5ab93ae4071d88d"));
    //
    // System.out.println(GetScreensList("ff8080814e471386014e49909dd9053a"));

    // System.out.println(GetNoSeatItemStockNum("000000004f4a278c014f4a546493005f"));
    //
    // System.out.println(GetNoSeatItemSaledNum("000000004f4a278c014f4a546493005f"));
    //
    // System.out.println(GetHasSeatItemPriceInfo("00000000517a1f11015184b1646a0617"));


    // System.out.println(GetPriceListByScreenId("10022678451597365"));
    //
    // System.out.println(GetNoSeatItemScreenInfo("10022678451597365"));

    // System.out.println(GetNoSeatItemInfo("ff8080814e471386014e49a9fd200684"));

    // System.out.println(GetHasSeatItemInfo("ed327a4a4134220c14be185a1f45c26b"));

  }


}
