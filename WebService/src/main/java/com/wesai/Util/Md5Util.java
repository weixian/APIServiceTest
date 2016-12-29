package com.wesai.Util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ClassName:Md5Util <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016��1��21�� ����10:27:56 <br/>
 *
 * @author weixian
 * @version
 * @see
 */
/**
 * ClassName: Md5Util <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: 2016��1��21�� ����10:27:56 <br/>
 *
 * @author weixian
 * @version
 */
public class Md5Util {

  public static String getMd5(String plainText) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(plainText.getBytes());
      byte b[] = md.digest();

      int i;

      StringBuffer buf = new StringBuffer("");
      for (int offset = 0; offset < b.length; offset++) {
        i = b[offset];
        if (i < 0) {
          i += 256;
        }
        if (i < 16) {
          buf.append("0");
        }
        buf.append(Integer.toHexString(i));
      }
      // 32λ����
      return buf.toString();
      // 16λ�ļ���
      // return buf.toString().substring(8, 24);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String get16bitMd5(String plainText) {

    return getMd5(plainText).toString().substring(8, 24);

  }

}
