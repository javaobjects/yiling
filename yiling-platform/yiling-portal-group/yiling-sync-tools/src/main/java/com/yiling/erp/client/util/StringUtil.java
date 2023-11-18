package com.yiling.erp.client.util;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil
{
    private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static final String SPACE_REGEX = "\\s*|\t|\r|\n";

    private static String byteArrayToHexString(byte[] b)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(byteToHexString(b[i]));
        }

        return sb.toString();
    }

    public static String byteToHexString(byte b)
    {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;

        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String original)
    {
        String ret = null;
        try
        {
            ret = new String(original);
            MessageDigest md = MessageDigest.getInstance("MD5");
            ret = byteArrayToHexString(md.digest(ret.getBytes()));
        }
        catch (Exception localException)
        {
        }
        return ret;
    }

    public static String getRandomNumber(int length)
    {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < length; i++) {
            buffer.append(random.nextInt(10));
        }
        return buffer.toString();
    }

    public static String getRandomChar(int length)
    {
        char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(62)]);
        }

        return buffer.toString();
    }

    public static boolean isContains(String substring, String[] source)
    {
        if ((source == null) || (source.length == 0)) {
            return false;
        }

        for (int i = 0; i < source.length; i++) {
            String aSource = source[i];
            if (aSource.equals(substring)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isContainsPart(String substring, String[] source)
    {
        if ((source == null) || (source.length == 0)) {
            return false;
        }

        for (int i = 0; i < source.length; i++) {
            String aSource = source[i];
            if (aSource.contains(substring)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmpty(String str)
    {
        return (str == null) || (str.trim().length() == 0);
    }

    public static boolean isNotEmpty(String str) {
        return (str != null) && (str.trim().length() > 0);
    }

    public static String upFirstChar(String str)
    {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String lowerFirstChar(String str)
    {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static void main(String[] args) {
        System.out.println(MD5Encode("abc"));
    }

    public static String Jsdecode(String str, String code)
            throws Exception
    {
        if ((str != null) && (!"".equals(str))) {
            str = new String(str.getBytes("ISO8859-1"), code);
            str = URLDecoder.decode(str, code);
        }

        return str;
    }

    public static String trim(String str) {
        String result = "";
        if (str != null) {
            result = str.trim();
        }
        return result;
    }

    public static List<String> getResult(String sql)
    {
        List results = new ArrayList();

        String sql1 = sql.replace("CREATE", "  @CREATE  ").replace("ALTER", "  @ALTER  ");
        String[] sqls = sql1.split("@");
        for (String s : sqls) {
            if (!isEmpty(s)) {
                results.add(s);
            }
        }
        return results;
    }

    /**
     * 去除字符串中的全部空格、回车、换行符、制表符
     *
     * @param source
     * @return
     */
    public static String clearAllSpace(String source) {
        Pattern p = Pattern.compile(SPACE_REGEX);
        Matcher m = p.matcher(source);
        return m.replaceAll("");
    }
}