package com.yiling.hmc.order.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * 泰康工具类
 */
public class TkUtil {


    public static final String KEY_ALGORITHM = "RSA";
    public static String TOP_AES_KEY = "UUrKQ209r2c14ATWPk1cWy8G6dQ656Kd";
    /**
     * "RSA/ECB/NoPadding" == "算法/模式/填充"
     */
    private static final String RSANOPADDING = "RSA/ECB/PKCS1Padding";
    protected static Cipher cipher;
    /**
     * RSA最大加密明文长度(密钥长度 / 8 - 11)
     *
     * @see "http://blog.csdn.net/lvxiangan/article/details/45487943"
     * @see "http://blog.csdn.net/defonds/article/details/42775183"
     * @see "http://blog.csdn.net/u011991249/article/details/51106656"
     */
    private static int MAX_ENCRYPT_BLOCK = 2048 / 8 - 11;

    /**
     * RSA最大解密密文长度(密钥长度 / 8)
     *
     * @see "http://blog.csdn.net/lvxiangan/article/details/45487943"
     * @see "http://blog.csdn.net/defonds/article/details/42775183"
     * @see "http://blog.csdn.net/u011991249/article/details/51106656"
     */
    private static int MAX_DECRYPT_BLOCK = 2048 / 8;

    public static final Logger logger = LoggerFactory.getLogger(TkUtil.class);
    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";


    /**
     * aes加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String encrypt(String content, String key) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] byteResult = cipher.doFinal(byteContent);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteResult.length; i++) {
                String hex = Integer.toHexString(byteResult[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成签名
     *
     * @param param
     * @return
     */
    public static String generateSign(Map<String, String> param, String secretKey) {
        String signStr = getStrExcludeSign(param);
        signStr = signStr + "key=" + secretKey;
        return Hex.encodeHexString(DigestUtils.md5(signStr));
    }

    /**
     * 排序字段，生产签名字符串
     *
     * @param map
     * @return
     */
    private static String getStrExcludeSign(Map<String, String> map) {
        Set<String> keySet = map.keySet();
        List<String> list = new ArrayList();
        Iterator var3 = keySet.iterator();
        while (var3.hasNext()) {
            String key = (String) var3.next();
            if (!"sign".equalsIgnoreCase(key) && !"thirdSign".equals(key)) {
                list.add(key);
            }
        }
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object a, Object b) {
                return a.toString().toLowerCase().compareTo(b.toString().toLowerCase());
            }
        });
        // 3、拼接字符串
        StringBuffer sb = new StringBuffer();
        for (String key : list) {
            String value = map.get(key);
            if (value == null || value.equals("")) {
                continue;
            }
            sb.append(key + "=");
            sb.append(value + "&");
        }
        return sb.toString();
    }
}
