package cn.studyjams.s1.sj46.tablayoutdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncoderUtils {
    /**
     * Function:字符串MD5加密 lym
     *
     * @param password
     * @return 2015-12-10下午2:10:05
     */
    public static String encodeMd5(String password) {
        try {
            // 获取到数字消息的摘要器
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 执行加密操作
            byte[] result = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            // 将每个byte字节的数据转换成16进制的数据
            for (int i = 0; i < result.length; i++) {
                int number = result[i] & 0xff;// 加盐
                String str = Integer.toHexString(number);// 将十进制的number转换成十六进制数据
                if (str.length() == 1) {// 判断加密后的字符的长度，如果长度为1，则在该字符前面补0
                    sb.append("0");
                    sb.append(str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString();// 将加密后的字符转成字符串返回
        } catch (NoSuchAlgorithmException e) {// 加密器没有被找到，该异常不可能发生。因为我们填入的“MD5”是正确的
            e.printStackTrace();
            // CNA'T REACH;
            return "";
        }
    }

    /**
     * Function:文件MD5加密 lym
     *
     * @param file
     * @return 2015-12-10下午2:10:22
     */
    public static String encodeMD5File(File file) {
        FileInputStream fis = null;

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            fis = new FileInputStream(file);

            byte[] buffer = new byte[2048];

            int length = -1;

            while ((length = fis.read(buffer)) != -1) {

                md.update(buffer, 0, length);

            }

            byte[] result = md.digest();
            StringBuilder sb = new StringBuilder();
            // 将每个byte字节的数据转换成16进制的数据
            for (int i = 0; i < result.length; i++) {
                int number = result[i] & 0xff;// 加盐
                String str = Integer.toHexString(number);// 将十进制的number转换成十六进制数据
                if (str.length() == 1) {// 判断加密后的字符的长度，如果长度为1，则在该字符前面补0
                    sb.append("0");
                    sb.append(str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString();// 将加密后的字符转成字符串返回

            // 16位加密

            // return buf.toString().substring(8, 24);

        } catch (Exception ex) {

            ex.printStackTrace();

            return null;

        } finally {

            try {

                fis.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }

    /**
     * Function 获取MD5
     *
     * @param source
     * @return 2016-3-17 下午2:35:26 by lym
     */
    public static String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 获取md5码
     *
     * @param source
     * @return
     */
    public static String getMD5(String source) {
        return getMD5(source.getBytes());
    }

//	public static void main(String[] args) {
//		String s="12345";
//		System.out.println(getMD5(s));
//		System.out.println(getMD5(s.getBytes()));
//	}
}
