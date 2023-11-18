package com.yiling.bi.order;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.yiling.bi.BaseTest;
import com.yiling.bi.order.excel.BiCrmOrderExcel;
import com.yiling.bi.order.service.BiCrmOrderService;
import com.yiling.framework.common.util.PojoUtils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/9/22
 */
@Slf4j
public class BiCrmOrderTest extends BaseTest {

    @Autowired
    private BiCrmOrderService biCrmOrderService;

    @Test
    public void handleFtpCrmOrderDataTest() {
        biCrmOrderService.handleFtpCrmOrderData();
    }

    private static final List<String> CSV_PROPERTIES = Arrays.asList(
            "soTime", "soYear", "soMonth", "sellerEcode", "sellerEname", "sellerEid", "buyerEname", "terminalArea", "terminalDept", "businessType", "satrapNo", "satrapName", "representativeNo", "representativeName",
            "chainRole", "province", "city", "county", "variety", "goodsId", "goodsName", "goodsSpec", "goodsPrice", "quantity", "salesVolume", "spec", "batchNo", "flowType", "lockFlag", "remark"
    );

    public static void main(String[] args) throws Exception {
//        BiCrmOrderTest biCrmOrderTest = new BiCrmOrderTest();
//        biCrmOrderTest.readCsv("/Users/baifc/workfile/yiling/需求文档/FTP上传CRM模板/crm模板数据.csv");

        BiCrmOrderTest biCrmOrderTest = new BiCrmOrderTest();
//        biCrmOrderTest.readExcel();

        biCrmOrderTest.readBigCsv("/Users/baifc/Downloads/z-temp/flow202212.csv");

    }



    @AllArgsConstructor
    @Data
    public static  class Person {
        private String name;
        private int age;
        private String group;
    }

    private static BiCrmOrderExcel covertFlowCsvData(String[] valueArr) {
        Class<BiCrmOrderExcel> clazz = BiCrmOrderExcel.class;
        Method[] methods = clazz.getDeclaredMethods();

        BiCrmOrderExcel biCrmOrderExcel = null;
        try {
            biCrmOrderExcel = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int index = 0;
        for (String property : CSV_PROPERTIES) {
            String methodName = "set" + upperCaseFirst(property);
            if (Arrays.stream(methods).noneMatch(m -> m.getName().equals(methodName))) {
                continue;
            }
            Method method = null;
            try {
                method = clazz.getMethod(methodName, String.class);
                method.invoke(biCrmOrderExcel, valueArr.length > index ? valueArr[index] : "");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            index++;
        }
        return biCrmOrderExcel;
    }

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    private void readExcel() throws Exception {
        File file = new File("/Users/baifc/Downloads/z-temp/flow202212.xlsx");
        InputStream inputStream = Files.newInputStream(file.toPath());

        List<BiCrmOrderExcel> list = new ArrayList<>();
        List<BiCrmOrderExcel> saleFailList = new ArrayList<>();

        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setKeyIndex(2);
        params.setNeedVerify(true);

        ExcelImportUtil.importExcelBySax(inputStream, BiCrmOrderExcel.class, params, new TestHandler(saleFailList, list));
        System.out.println(list.size());
    }

    public void readBigCsv(String finalPath) throws Exception {
        InputStream inputStream = Files.newInputStream(Paths.get(finalPath));

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
        String line = null;

        int i = 0;
        int count = 0;
        int lineNum = 0;
        boolean isTitleLine = true;
        while ((line = br.readLine()) != null) {
            lineNum++;
            if (lineNum%10000 == 0) {
                System.out.println("line=" + lineNum);
            }
            if (isTitleLine) {   // 标题行跳过
                isTitleLine = false;
                continue;
            }
            String[] valueArr = line.split(",");
            if (i >= 10000) {
                i = 0;
                System.out.println(count++);
                continue;
            }
            i++;

        }
        br.close();
    }

    public void readCsv(String finalPath) throws Exception {

        List<BiCrmOrderExcel> biCrmOrderExcelList = new ArrayList<>();
        Reader reader = new InputStreamReader(Files.newInputStream(Paths.get(finalPath)), "GBK");
        CSVReader csvReader = new CSVReaderBuilder(reader).build();

        boolean isTitleLine = true;
        Iterator<String[]> iterator = csvReader.iterator();
        while (iterator.hasNext()) {
            String[] valueArr = iterator.next();
            if (isTitleLine) {
                isTitleLine = false;
                continue;
            }

            BiCrmOrderExcel biCrmOrderExcel = covertFlowCsvData(valueArr);
            biCrmOrderExcelList.add(biCrmOrderExcel);
        }

        System.out.println(biCrmOrderExcelList.size());
    }
}
