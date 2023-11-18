package com.yiling.dataflow.flow.api.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.yiling.dataflow.flow.api.FlowCrmSaleApi;
import com.yiling.dataflow.flow.entity.FlowCrmSaleDO;
import com.yiling.dataflow.flow.excel.FlowCrmSaleExcel;
import com.yiling.dataflow.flow.service.FlowCrmSaleService;
import com.yiling.framework.common.annotations.CsvOrder;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/2/1
 */
@DubboService
@Slf4j
public class FlowCrmSaleApiImpl implements FlowCrmSaleApi {

    @Autowired
    private FlowCrmSaleService flowCrmSaleService;

    @Async("asyncExecutor")
    @Override
    public void handleCrmSaleFile() {
        //  获取ftp文件
        Ftp ftp = getConnectFtp();

        // 只读取上月和上上月的crm流向数据文件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        String monthStr1 = sdf.format(DateUtil.offsetMonth(new Date(), -1));
        String monthStr2 = sdf.format(DateUtil.offsetMonth(new Date(), -2));

        FTPFile[] ftpFiles = ftp.lsFiles("/");
        for (FTPFile ftpFile : ftpFiles) {

            String fileName = ftpFile.getName();
            if (!fileName.endsWith(".csv")) {
                // 仅支持csv文件
                continue;
            }
            if (!fileName.startsWith("flow" + monthStr1) && !fileName.startsWith("flow" + monthStr2)) {
                // 只读取近两个月的文件
                continue;
            }

            File fileDir =  FileUtil.getTmpDir();
            if (!fileDir.isDirectory()) {
                fileDir.mkdirs();
            }

            log.info("crm销售流向数据导入，从ftp下载原始数据文件：" + fileName);
            File file = FileUtil.newFile(fileDir.getPath() + File.separator + ftpFile.getName());
            //  download至本地
            if (file.exists()) {
                FileUtil.del(file);
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            ftp.download("/", ftpFile.getName(), file);
            log.info("crm销售流向数据导入，开始解析原始数据文件：" + fileName);
            // 自测用
//            File file = new File("/Users/baifc/Downloads/z-temp/flow202212.csv");

            // 解析文件
            InputStream inputStream = null;
            BufferedReader br = null;
            try {
                inputStream = Files.newInputStream(file.toPath());
                br = new BufferedReader(new InputStreamReader(inputStream, "GBK"));

                String line = null;
                boolean isTitleLine = true;

                //  10000条插入一次
                List<FlowCrmSaleDO> flowCrmSaleDOList = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    if (isTitleLine) {
                        isTitleLine = false;
                        continue;
                    }
                    String[] valueArr = line.split(",");

                    Class<FlowCrmSaleExcel> clazz = FlowCrmSaleExcel.class;
                    Field[] fields = clazz.getDeclaredFields();
                    FlowCrmSaleExcel crmSaleExcel = clazz.newInstance();

                    for (Field field : fields) {
                        if (!field.isAnnotationPresent(CsvOrder.class)) {
                            continue;
                        }

                        CsvOrder csvOrder = field.getDeclaredAnnotation(CsvOrder.class);
                        int index = csvOrder.value();
                        if (valueArr.length < index) {
                            //  csv文件模板有误
                            throw new BusinessException(ResultCode.FAILED, "csv文件模板有误");
                        }

                        String value = valueArr[index];
                        String methodName = "set" + upperCaseFirst(field.getName());
                        Method method = clazz.getDeclaredMethod(methodName, String.class);
                        method.invoke(crmSaleExcel, value);
                    }

//                    covertExcelDataToDO(crmSaleExcel);
                    FlowCrmSaleDO flowCrmSaleDO = new FlowCrmSaleDO();
                    flowCrmSaleDO.setSoTime(DateUtil.parseDate(excelDoubleToDate(crmSaleExcel.getSoTime())));
                    flowCrmSaleDO.setYear(Integer.parseInt(crmSaleExcel.getYear()));
                    flowCrmSaleDO.setMonth(Integer.parseInt(crmSaleExcel.getMonth()));
                    flowCrmSaleDO.setEname(crmSaleExcel.getEname());
                    flowCrmSaleDO.setEnterpriseName(crmSaleExcel.getEnterpriseName());
                    flowCrmSaleDO.setCrmGoodsCode(crmSaleExcel.getCrmGoodsCode());
                    flowCrmSaleDO.setCrmGoodsName(crmSaleExcel.getCrmGoodsName());
                    flowCrmSaleDO.setSoQuantity(new BigDecimal(crmSaleExcel.getSoQuantity()));
                    flowCrmSaleDO.setSoBatchNo(crmSaleExcel.getSoBatchNo());
                    flowCrmSaleDO.setSourceFile(file.getAbsolutePath());
                    flowCrmSaleDO.setStatus(1);
                    flowCrmSaleDO.setDelFlag(0);
                    flowCrmSaleDOList.add(flowCrmSaleDO);

                    if (flowCrmSaleDOList.size() >= 10000) {    // 每超过10000条插入一次
                        flowCrmSaleService.batchInsert(flowCrmSaleDOList);
                        flowCrmSaleDOList.clear();
                    }
                }

                if (flowCrmSaleDOList.size() > 0) {
                    flowCrmSaleService.batchInsert(flowCrmSaleDOList);
                }

                //  修改数据导入状态
                flowCrmSaleService.updateBySourceFile(file.getAbsolutePath(), 2);
                log.info("crm销售流向数据导入，原始数据文件：" + fileName + "，导入完成！");

                // 将源文件移动至bakup目录下
                if (!ftp.exist("bakup")) {
                    ftp.mkdir("bakup");
                }
                ftp.upload("/bakup", fileName, file);
                ftp.cd("/");
                ftp.delFile(fileName);
            } catch (Exception e) {
                log.error("crm销售流向数据导入报错, fileName = {}", file.getName(), e);
                flowCrmSaleService.deleteBySourceFile(file.getAbsolutePath(), 1);

            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        if (ftp != null) {
            try {
                ftp.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private Ftp getConnectFtp() {
        FtpConfig ftpConfig = new FtpConfig();
        ftpConfig.setUser("chanpinftp4");
        ftpConfig.setPassword("chanpinftp43A21");

        ftpConfig.setHost("172.16.82.13");
        ftpConfig.setPort(12121);

        ftpConfig.setSoTimeout(1000 * 60 * 60);
        ftpConfig.setConnectionTimeout(1000 * 60 * 60);
        ftpConfig.setCharset(Charset.forName("GBK"));
        return new Ftp(ftpConfig, FtpMode.Active);
    }

    private String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    public String excelDoubleToDate(String strDate) {
        if (strDate == null) {
            return null;
        }
        try {
            if (!isNumeric(strDate)) {
                return strDate;
            }

            if (strDate.contains(".")) {
                if (strDate.indexOf(".") != 5) {
                    return strDate;
                }
            } else {
                if (strDate.length() != 5) {
                    return strDate;
                }
            }
            if (org.apache.poi.ss.usermodel.DateUtil.isValidExcelDate(Double.parseDouble(strDate))) {
                Date tDate = DoubleToDate(Double.parseDouble(strDate));
                return DateUtil.format(tDate, "yyyy-MM-dd");
            }
        } catch (Exception e) {
            return strDate;
        }
        return strDate;
    }

    public Date DoubleToDate(Double dVal) {
        Date tDate = new Date();
        long localOffset = tDate.getTimezoneOffset() * 60000L; //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        tDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));
        return tDate;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[+-]?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
