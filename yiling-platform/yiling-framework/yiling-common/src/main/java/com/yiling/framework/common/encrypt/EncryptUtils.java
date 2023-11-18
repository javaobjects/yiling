package com.yiling.framework.common.encrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class EncryptUtils {

    private static final String ENCRYPT_FILE = "/yl/data/yiling-framework/encrypt.properties";
    private static String key = "";

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(new File(ENCRYPT_FILE)));
            key = properties.getProperty("key");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("秘钥文件未找到，请仔细检查！", e);
        } catch (IOException ioe) {
            throw new RuntimeException("加载秘钥文件出错，请仔细检查！", ioe);
        }
    }

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(tip + ":");
        String inputText = "";
        // 现在有输入数据
        if (scanner.hasNext()) {
            inputText = scanner.next();
        }
        return inputText;
    }

    public static void main(String[] args) {
        int i = 1;
        while (true) {
            String encryptText = encrypt(scanner((i++) + ".请输入需要加密的内容"));
            System.out.println("加密后:" + encryptText);
            String decryptText = decrypt(encryptText);
            System.out.println("解密后:" + decryptText);
            System.out.println("-------------------------------------------------");
        }
    }

    /**
     * 加密
     * @param text 明文
     * @return     密文
     */
    public static String encrypt(String text) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(key);
        return encryptor.encrypt(text);
    }

    /**
     * 解密
     * @param ciphertext 密文
     * @return           明文
     */
    public static String decrypt(String ciphertext) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(key);
        return encryptor.decrypt(ciphertext);
    }

}
