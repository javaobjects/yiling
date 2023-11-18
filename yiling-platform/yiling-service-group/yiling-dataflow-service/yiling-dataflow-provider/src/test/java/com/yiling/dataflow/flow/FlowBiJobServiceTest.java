package com.yiling.dataflow.flow;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.backup.service.RelationShipBackupService;
import com.yiling.dataflow.flow.service.FlowBiJobService;
import com.yiling.dataflow.flow.service.FlowBiTaskService;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/4/7
 */
@Slf4j
public class FlowBiJobServiceTest extends BaseTest {

    @Autowired
    private FlowBiJobService            flowBiJobService;
    @Autowired
    private FlowBiTaskService           flowBiTaskService;
    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;
    @Autowired
    private RelationShipBackupService relationShipBackupService;


    @Test
    public void Test2() throws Exception {
        flowBiTaskService.excelFlowBiTask();
    }

    @Test
    public void Test22() throws Exception {
        flowGoodsBatchDetailService.statisticsFlowGoodsBatch(null);
    }

    @Test
    public void test3(){
        try {
            Ftp ftp = new Ftp("cdmftp.yiling.cn", 21, "142004616", "YL85901247@yiling.com", Charset.forName("GBK"),FtpMode.Passive);
            File excelDir = new File("D://SD_153023577_云南东骏药业有限公司_20220418_YL.xlsx");
            ftp.upload("/", excelDir);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test4(){
        try {
            Ftp ftp = new Ftp("172.16.82.13", 12121, "test01", "1234Abc...", Charset.forName("GBK"),FtpMode.Active);
            File excelDir = new File("D://batch-template-20220428.xlsx");
            ftp.cd("/");
            ftp.upload("/", excelDir);
            ftp.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void Tes1t2() throws Exception {
        AgencyBackRequest agencyBackRequest = new AgencyBackRequest();
        agencyBackRequest.setOffsetMonth(0);
        List<Long> objects = new ArrayList<>();
        objects=Arrays.asList(
        12294L,
        12296L,
        10515L,
        12297L,
        11026L,
        9792L,
        12930L,
        13461L,
        13029L,
        12298L,
        11974L,
        10410L,
        13591L,
        9739L,
        12294L,
        12296L,
        10515L,
        12297L,
        11026L,
        9792L,
        12930L,
        13591L,
        13512L,
        10328L,
        13798L,
        13799L,
        12298L,
        10410L);

        relationShipBackupService.RelationShipBackupByPostcode(agencyBackRequest,objects);
    }

}
