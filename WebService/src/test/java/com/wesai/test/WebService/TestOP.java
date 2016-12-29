/**
 * Project Name:Test File Name:TestOP.java Package Name:com.wepiao Date:2016��1��22������3:30:12
 *
 */
/**
 * Project Name:Test File Name:TestOP.java Package Name:com.wepiao Date:2016��1��22������3:30:12
 *
 */
package com.wesai.test.WebService;

import java.util.ArrayList;

import com.wepiao.OP.OP;
import com.wepiao.WebPC.ResponseData;

/**
 * ClassName:TestOP <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016��1��22�� ����3:30:12 <br/>
 *
 * @author weixian
 * @version
 * @see
 */
/**
 * ClassName: TestOP <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: 2016��1��22�� ����3:30:12 <br/>
 *
 * @author weixian
 * @version
 */
public class TestOP {

  /**
   * main:<br/>
   * (Description) <br/>
   * TODO:
   *
   * @author weixian
   *
   * @version
   *
   * @param
   *
   * @return
   *
   * @exception
   *
   *            @Example
   *
   */
  static OP op = new OP("http://op.o2o.wepiao.com/");

  static String LINEND = "\r\n";


  static String YIIToken = "Content-Disposition: form-data; name=\"YII_CSRF_TOKEN\"" + LINEND
      + LINEND + op.GetYIIToken();

  static String createTime = "Content-Disposition: form-data; name=\"item[createTime]\"" + LINEND
      + LINEND
 + String.valueOf(System.currentTimeMillis());

  static String id = "Content-Disposition: form-data; name=\"item[id]\"" + LINEND + LINEND
      + "46444469ae05885ff3f92e0e47fcf2ed";


  static String titleCn = "Content-Disposition: form-data; name=\"item[titleCn]\"" + LINEND + LINEND
      + "����"
 + String.valueOf(System.currentTimeMillis());

  static String titleEn = "Content-Disposition: form-data; name=\"item[titleEn]\"" + LINEND + LINEND
      + "Test" + String.valueOf(System.currentTimeMillis());

  static String typePid = "Content-Disposition: form-data; name=\"item[typePid]\"" + LINEND + LINEND
      + "ff8080814a7ad6f1014a7adb6b540002";

  static String typeId = "Content-Disposition: form-data; name=\"item[typeId]\"" + LINEND + LINEND
      + "ff8080814aed3ba8014af14fd0260014";

  static String showTime =
      "Content-Disposition: form-data; name=\"item[showTime]\"" + LINEND + LINEND + "2016-06-06";

  static String screeningLength =
      "Content-Disposition: form-data; name=\"item[screeningLength]\"" + LINEND + LINEND + "120";


  static String cityId =
      "Content-Disposition: form-data; name=\"item[cityId]\"" + LINEND + LINEND + "235";

  static String venueId = "Content-Disposition: form-data; name=\"item[venueId]\"" + LINEND + LINEND
      + "0bb54089cb021e4960b5e7731b549827";

  static String priceRange =
      "Content-Disposition: form-data; name=\"item[priceRange]\"" + LINEND + LINEND + "580";

  static String source =
      "Content-Disposition: form-data; name=\"item[source]\"" + LINEND + LINEND + "7";
  static String voteType =
      "Content-Disposition: form-data; name=\"item[voteType]\"" + LINEND + LINEND + "1";
  static String status =
      "Content-Disposition: form-data; name=\"item[status]\"" + LINEND + LINEND + "2";
  static String hostBusinessId = "Content-Disposition: form-data; name=\"item[hostBusinessId]\""
      + LINEND
 + LINEND + "de873ef3bdd919d432b0dfa07d0337e3";


  static String File = "Content-Disposition: form-data; name=\"file\"; filename=\"\"" + LINEND
      + "Content-Type: application/octet-stream" + LINEND + LINEND+"";


  static String addNewImages =
      "Content-Disposition: form-data; name=\"addNewImages\"" + LINEND + LINEND + "";

  static String addNewTicketAreaImages =
      "Content-Disposition: form-data; name=\"addNewTicketAreaImages\"" + LINEND
          + LINEND + "";

  static String xiaoBianSaid =
      "Content-Disposition: form-data; name=\"item[xiaoBianSaid]\"" + LINEND + LINEND + "С��˵";
  static String description =
      "Content-Disposition: form-data; name=\"item[description]\"" + LINEND + LINEND + "��������";
  static String remark =
      "Content-Disposition: form-data; name=\"item[remark]\"" + LINEND + LINEND + "���Ǳ�ע";

  public static void main(String[] args) {
    // TODO Auto-generated method stub



    try {

      // System.out.println(op.GetCookie());
      //
      // System.out.println(op.GetYIIToken());

      for (int i = 0; i < 1; i++) {

        ResponseData lt = op.Login("weixian_test@wepiao.com", "111111");

        System.out.println(op.GetOtOToken());

        // op.LogOut();

        // op.Login("admin_wx@wepiao.com", "Vnak@$^8");

        // System.out.println(op.GetOtOToken());

        op.LogOut();

        if (lt.getCode() != 200) {

          System.out.println("***************************************************" + i);

          System.out.println(lt.getCode() + "\t\t" + lt.getMessage() + "\t\t" + lt.getData());

          // System.out.println(op.GetCookie());

          System.exit(99);

        }


      }
        // System.out.println(lt.getCode() + "\t\t" + lt.getMessage() + "\t\t" + lt.getData());

      // System.out.println(op.GetCookie());

      ArrayList<String> para = new ArrayList<String>();

      // para.add(FBOUNDARY);
      para.add(YIIToken);
      para.add(createTime);
      // para.add(id);
      para.add(titleCn);
      para.add(titleEn);
      para.add(typePid);
      para.add(typeId);
      para.add(showTime);
      para.add(screeningLength);
      para.add(cityId);
      para.add(venueId);
      para.add(priceRange);
      para.add(source);
      para.add(voteType);
      para.add(status);
      para.add(hostBusinessId);
      para.add(File);
      para.add(addNewImages);
      para.add(File);
      para.add(addNewTicketAreaImages);
      para.add(xiaoBianSaid);
      para.add(description);
      para.add(YIIToken);
      para.add(remark);


      // System.out.println(op.CreateItem("http://cd.dev.wepiao.com/op/index.php?r=item/save",
      // para));



    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();

      op.LogOut();
    }

  }

}
