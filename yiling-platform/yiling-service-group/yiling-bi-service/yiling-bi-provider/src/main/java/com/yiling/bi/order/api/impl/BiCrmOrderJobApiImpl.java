package com.yiling.bi.order.api.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.yiling.bi.order.api.BiCrmOrderJobApi;
import com.yiling.bi.order.entity.BiCrmOrderDO;
import com.yiling.bi.order.entity.BiOrderBackupTaskDO;
import com.yiling.bi.order.excel.BiCrmOrderExcel;
import com.yiling.bi.order.service.BiCrmOrderService;
import com.yiling.bi.order.service.BiOrderBackupTaskService;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.imports.ExcelImportService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/9/20
 */
@DubboService
@Slf4j
public class BiCrmOrderJobApiImpl implements BiCrmOrderJobApi {

    @Autowired
    private BiCrmOrderService biCrmOrderService;


    @Async("asyncExecutor")
    @Override
    public void handleFtpCrmOrderData() {
        biCrmOrderService.handleFtpCrmOrderData();
    }





}
