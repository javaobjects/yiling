package com.yiling.open.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.yiling.open.erp.dao.ErpSaleFlowMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpGoodsBatchFlowMapper;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.dto.ErpSaleFlowDTO;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.entity.ErpSaleFlowDO;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpSaleFlowService;
import com.yiling.open.ftp.excel.ErpGoodsBatchFlowExcel;
import com.yiling.open.ftp.excel.ErpPurchaseFlowExcel;
import com.yiling.open.ftp.excel.ErpSaleFlowExcel;
import com.yiling.open.ftp.handler.ErpPurchaseFlowExcelReadHandler;
import com.yiling.open.ftp.handler.ErpSaleFlowExcelReadHandler;
import com.yiling.open.ftp.service.FlowFtpClientService;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.imports.ExcelImportService;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/2/24
 */
@Slf4j
public class FlowFtpTest extends BaseTest {

    final static String yl = "yl";

    @Autowired
    private FlowFtpClientService flowFtpClientService;
    @Autowired
    private ErpGoodsBatchFlowMapper erpGoodsBatchFlowMapper;
    @Autowired
    private ErpClientService erpClientService;

    @Autowired
    private ErpSaleFlowService erpSaleFlowService;

    @Autowired
    private ErpSaleFlowMapper erpSaleFlowMapper;

    @Test
    public void Test1() throws Exception {
        flowFtpClientService.readFlowExcel();
    }

    @Test
    public void Test21() throws Exception {
        InputStream inputStream = new FileInputStream(new File("/Users/baifc/Downloads/purchase-template-20220803 .xlsx"));
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setHeadRows(1);
        params.setStartSheetIndex(0);
        params.setKeyIndex(0);
        List<ErpPurchaseFlowExcel> list = new ArrayList<>();
        ExcelImportUtil.importExcelBySax(inputStream, ErpPurchaseFlowExcel.class, params, new IReadHandler<ErpPurchaseFlowExcel>() {
            @Override
            public void handler(ErpPurchaseFlowExcel o) {
//                String date = ExcelDoubleToDate(o.getPoTime());
                list.add(o);
            }

            @Override
            public void doAfterAll() {
//                System.out.println(list.size());
//                System.out.println(JSON.toJSONString(list.get(0)));
//                System.out.println("全部执行完毕了--------------------------------");
            }
        });
        System.out.println(list.size());
    }

    @Test
    public void Test2() throws Exception {
        InputStream inputStream = new FileInputStream(new File("/Users/baifc/Downloads/batch-template-20220803 .xlsx"));
        ImportParams params = new ImportParams();
        params.setKeyIndex(0);
        params.setNeedVerify(true);
        ExcelImportResult<ErpSaleFlowExcel> result = new ExcelImportService().importExcelByIs(inputStream, ErpSaleFlowExcel.class, params, true);
        // 查询数据
        if (CollUtil.isNotEmpty(result.getFailList())) {
            log.error("[ftp流向对接] 商业公司su_id={}库存文件格式不正确");
        }
        //通过上传文件返回结果在处理返回结果
        List<BaseErpEntity> dataMap = new ArrayList<>();
        for (ErpSaleFlowExcel erpGoodsBatchFlowExcel : result.getList()) {
            ErpGoodsBatchFlowDTO erpGoodsBatchFlowDTO = PojoUtils.map(erpGoodsBatchFlowExcel, ErpGoodsBatchFlowDTO.class);
            //            erpGoodsBatchFlowDTO.setOperType(1);
            dataMap.add(erpGoodsBatchFlowDTO);
        }
    }

    @Test
    public void Test22() throws Exception {
        ExcelUtil.readBySax("D://purchase-template-20220601.xlsx", -1, createRowHandler());
    }

    private RowHandler createRowHandler() {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, long rowIndex, List<Object> rowlist) {
                System.out.println(JSON.toJSONString(rowlist));
            }
        };
    }

    @Test
    public void Test3() throws Exception {
        String name = "batch-template-20220420.xls";
        String purchaseFlowExcel = "batch-template" + "-" + DateUtil.format(new Date(), "yyyyMMdd");
        String purchaseFlowExcel1 = "batch-template" + "-" + DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyyMMdd");
        if ((name.startsWith(purchaseFlowExcel) || name.startsWith(purchaseFlowExcel1))) {
            purchaseFlowExcel = name;
        }
    }

    @Test
    public void localCompareTest() {
        Long suId = 73L;
        Date time = DateUtil.parse("2022-05-24 18:55:48", "yyyy-MM-dd HH:mm:ss");
        //1.先通过suId查询分公司
        List<ErpClientDTO> erpClientDOList = erpClientService.selectBySuId(suId);
        if (CollUtil.isNotEmpty(erpClientDOList)) {
            List<Long> eids = erpClientDOList.stream().map(e -> e.getRkSuId()).collect(Collectors.toList());
            //2.删除op库流向库存数据
            if (time == null) {
                time = new Date();
            }
            erpGoodsBatchFlowMapper.deleteGoodsBatchFlowBySuId(suId, time);
        }
    }

    @Test
    public void test5() {
        ErpSaleFlowDO erpSaleFlowDO =  erpSaleFlowMapper.selectById(59901L);
        erpSaleFlowService.onlineData(erpSaleFlowDO);
    }

    public static void main(String[] args) throws Exception{
//        FtpConfig ftpConfig = new FtpConfig();
//        ftpConfig.setUser("test01");
//        //                ftpConfig.setHost(flowFtpClientDO.getFtpIp());
//        ftpConfig.setHost("172.16.82.13");
//        ftpConfig.setPort(12121);
//        ftpConfig.setPassword("1234Abc...");
//        ftpConfig.setSoTimeout(1000 * 60 * 60);
//        ftpConfig.setConnectionTimeout(1000 * 60 * 60);
//        ftpConfig.setCharset(Charset.forName("GBK"));
//        Ftp ftp = new Ftp(ftpConfig, FtpMode.Active);
//
//        FTPFile[] fs = ftp.lsFiles("/");
//        System.out.println("fs.length=" + fs.length);
//
//        for (FTPFile ftpFile : fs) {
//            System.out.println("ftpFile.getName() = " + ftpFile.getName());
//        }
        File file = new File("/Users/baifc/Downloads/purchase-template-20230508-1.xlsx");
        InputStream inputStream = Files.newInputStream(file.toPath());
        // TODO excel Test
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setKeyIndex(2);
        params.setNeedVerify(true);
        List<ErpPurchaseFlowExcel> list = new ArrayList<>();
        List<ErpPurchaseFlowExcel> saleFailList = new ArrayList<>();
//        ExcelImportResult<ErpPurchaseFlowExcel> result = new ExcelImportService().importExcelByIs(inputStream, ErpPurchaseFlowExcel.class, params, true);
//        System.out.println(result.getList().size());

        ExcelImportUtil.importExcelBySax(inputStream, ErpPurchaseFlowExcel.class, params, new ErpPurchaseFlowExcelReadHandler(saleFailList, list));
        System.out.println(list.size());
        System.out.println(saleFailList.size());



        // TODO csv Test
//        List<ErpSaleFlowExcel> saleFailList = new ArrayList<>();
//        List<ErpSaleFlowExcel> erpSaleFlowExcelList = new ArrayList<>();
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
//        String line = null;
//        boolean isTitleLine = true;
//        while ((line = br.readLine()) != null) {
//            if (isTitleLine) {   // 标题行跳过
//                isTitleLine = false;
//                continue;
//            }
//            String[] valueArr = line.split(",");
//
//            covertFlowCsvData(valueArr, ErpSaleFlowExcel.class, new ErpSaleFlowExcelReadHandler(saleFailList, erpSaleFlowExcelList));
//            System.out.println(valueArr);
//        }
//        br.close();

    }

    private static <T> void covertFlowCsvData(String[] valueArr, Class<T> clazz, IReadHandler<T> readHandler) {
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int index = 0;
        for (Field field : fields) {
            if (field.getType() != String.class) {
                continue;
            }

            String methodName = "set" + upperCaseFirst(field.getName());
            if (Arrays.stream(methods).noneMatch(m -> m.getName().equals(methodName))) {
                continue;
            }
            Method method = null;
            try {
                method = clazz.getMethod(methodName, String.class);
                method.invoke(obj, valueArr.length > index ? valueArr[index] : "");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            index++;
        }
        readHandler.handler(obj);
    }

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    public static String ExcelDoubleToDate(String strDate) {
        if (strDate == null) {
            return null;
        }
        if (strDate.length() == 5) {
            try {
                Date tDate = DoubleToDate(Double.parseDouble(strDate));
                return DateUtil.format(tDate, "yyyy-MM-dd");
            } catch (Exception e) {
                return strDate;
            }
        }
        return strDate;
    }

    public static Date DoubleToDate(Double dVal) {
        Date tDate = new Date();
        long localOffset = tDate.getTimezoneOffset() * 60000; //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        tDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));
        return tDate;
    }

}
