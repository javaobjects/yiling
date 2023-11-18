package com.yiling.dataflow.flow;

import java.nio.charset.Charset;

import org.apache.commons.net.ftp.FTPFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.flow.api.FlowCrmSaleApi;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/2/2
 */
@Slf4j
public class FlowCrmSaleApiTest extends BaseTest {

    @Autowired
    private FlowCrmSaleApi flowCrmSaleApi;

    @Test
    public void handleCrmSaleFileTest() {
        flowCrmSaleApi.handleCrmSaleFile();
    }

    public static void main(String[] args) throws Exception {
        Ftp ftp = getConnectFtp();
        ftp.setMode(FtpMode.Active);

        System.out.println(ftp.existFile("/flow202212-2.csv"));
        System.out.println(ftp.exist("/bakup"));
        FTPFile[] ftpFiles = ftp.lsFiles("/");
        System.out.println(ftpFiles.length);
        ftp.close();
    }

    private static Ftp getConnectFtp() {
        FtpConfig ftpConfig = new FtpConfig();
        ftpConfig.setUser("chanpinftp4");
        ftpConfig.setPassword("chanpinftp43A21");

        ftpConfig.setHost("172.16.82.13");
        //        ftpConfig.setHost("8.142.87.52");
        ftpConfig.setPort(12121);

        ftpConfig.setSoTimeout(1000 * 60 * 60);
        ftpConfig.setConnectionTimeout(1000 * 60 * 60);
        ftpConfig.setCharset(Charset.forName("GBK"));
        //        return new Ftp(ftpConfig, FtpMode.Passive);
        return new Ftp(ftpConfig, FtpMode.Active);
    }
}
