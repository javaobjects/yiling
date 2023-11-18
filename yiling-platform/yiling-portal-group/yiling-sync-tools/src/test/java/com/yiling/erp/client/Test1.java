package com.yiling.erp.client;

import java.io.File;
import java.io.IOException;

/**
 * @author: shuang.zhang
 * @date: 2022/8/18
 */
public class Test1 {

    public static void main(String[] args) {
        File file = new File("D:\\测试");
        file.setReadable(true);//设置可读权限
        file.setWritable(true);//设置可写权限
    }
}
