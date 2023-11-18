package com.yiling.dataflow.flow;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.flow.excel.GoodsBatchFlowExcel;
import com.yiling.dataflow.flow.service.FlowMonthBiJobService;
import com.yiling.dataflow.flow.service.FlowMonthBiTaskService;
import com.yiling.dataflow.utils.DateTransUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/4/7
 */
@Slf4j
public class FlowMonthBiJobServiceTest extends BaseTest {

    @Autowired
    private FlowMonthBiJobService  flowMonthBiJobService;
    @Autowired
    private FlowMonthBiTaskService flowMonthBiTaskService;


    @Test
    public void Test2() throws Exception {
        flowMonthBiTaskService.excelMonthFlowBiTask();
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
    public void test5() {
        try {
            List<GoodsBatchFlowExcel> goodsBatchFlowExcelList = new ArrayList<>();
            GoodsBatchFlowExcel goodsBatchFlowExcel = new GoodsBatchFlowExcel();
            goodsBatchFlowExcel.setDateTime(DateTransUtil.parseDate("2022-06-10"));
            goodsBatchFlowExcel.setGbTime(null);
            goodsBatchFlowExcel.setGbBatchNo("测试");
            goodsBatchFlowExcel.setGbEndTime(DateTransUtil.parseDate("vcxv"));
            goodsBatchFlowExcel.setGbManufacturer("测试");
            goodsBatchFlowExcel.setGbName("测试");
            goodsBatchFlowExcel.setGbProduceTime(DateTransUtil.parseDate("2023-10-31"));
            goodsBatchFlowExcelList.add(goodsBatchFlowExcel);
            CsvExportParams exportParams = new CsvExportParams();
            exportParams.setCreateHeadRows(true);
            exportParams.setEncoding("GBK");
            File excelDir = FileUtil.newFile("D://aa.csv");
            CsvExportUtil.exportCsv(exportParams, GoodsBatchFlowExcel.class, goodsBatchFlowExcelList, new FileOutputStream(excelDir));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
