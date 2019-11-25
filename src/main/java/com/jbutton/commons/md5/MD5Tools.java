package main.java.com.jbutton.commons.md5;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * MD5加密
 * @author lee
 */
public class MD5Tools {

    public static void main(String[] args) throws Exception{
        if(args.length == 0){
            System.out.println("运行之前先在java命令行中输入私钥");
            return;
        }
        long timeMillis = System.currentTimeMillis();
        String key = args[0];
        String data = key + timeMillis;
        System.out.println("被加密的字符串是:" + data);
        System.out.println("加密密钥是:" + key);
        System.out.println("时间戳是:" + timeMillis);
        getMD5(data);
    }
    public static String getMD5(String data) throws Exception {
        //加密后的字符串
        String encodeStr= DigestUtils.md5Hex(data);
        System.out.println("MD5加密后的字符串为:"+encodeStr);
        return encodeStr;
    }
}
