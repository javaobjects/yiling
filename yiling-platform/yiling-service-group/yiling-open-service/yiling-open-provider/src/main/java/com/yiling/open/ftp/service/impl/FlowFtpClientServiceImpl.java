package com.yiling.open.ftp.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.imports.ExcelImportService;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.order.api.FlowGoodsBatchApi;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpHeartApi;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpGoodsBatchFlowMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpFlowControlDTO;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.dto.ErpSaleFlowDTO;
import com.yiling.open.erp.dto.ErpShopSaleFlowDTO;
import com.yiling.open.erp.dto.SysHeartBeatDTO;
import com.yiling.open.erp.dto.request.UpdateHeartBeatTimeRequest;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.ftp.dao.FlowFtpCacheMapper;
import com.yiling.open.ftp.dao.FlowFtpClientMapper;
import com.yiling.open.ftp.dto.LocalCompareDTO;
import com.yiling.open.ftp.entity.FlowFtpCacheDO;
import com.yiling.open.ftp.entity.FlowFtpClientDO;
import com.yiling.open.ftp.entity.FlowFtpFileTaskDO;
import com.yiling.open.ftp.excel.ErpGoodsBatchFlowExcel;
import com.yiling.open.ftp.excel.ErpPurchaseFlowExcel;
import com.yiling.open.ftp.excel.ErpSaleFlowExcel;
import com.yiling.open.ftp.excel.ErpShopSaleFlowExcel;
import com.yiling.open.ftp.handler.ErpGoodsBatchFlowExcelReadHandler;
import com.yiling.open.ftp.handler.ErpPurchaseFlowExcelReadHandler;
import com.yiling.open.ftp.handler.ErpSaleFlowExcelReadHandler;
import com.yiling.open.ftp.handler.ErpShopSaleFlowExcelReadHandler;
import com.yiling.open.ftp.service.FlowFtpCacheService;
import com.yiling.open.ftp.service.FlowFtpClientService;
import com.yiling.open.ftp.service.FlowFtpFileTaskService;
import com.yiling.open.third.enums.FlowFtpAnalyticStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-23
 */
@Slf4j
@Service
public class FlowFtpClientServiceImpl extends BaseServiceImpl<FlowFtpClientMapper, FlowFtpClientDO> implements FlowFtpClientService {

    @Autowired
    private FileService             fileService;
    @Autowired
    private FlowFtpCacheMapper      flowFtpCacheMapper;
    @Autowired
    private FlowFtpCacheService     flowFtpCacheService;
    @Autowired
    private FlowFtpFileTaskService  flowFtpFileTaskService;
    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;
    @Autowired
    private ErpClientService        erpClientService;
    @Autowired
    private ErpGoodsBatchFlowMapper erpGoodsBatchFlowMapper;
    @Autowired
    private RedisDistributedLock    redisDistributedLock;
    @Resource
    private StringRedisTemplate     stringRedisTemplate;
    @DubboReference(timeout = 1000 * 60)
    private FlowGoodsBatchApi       flowGoodsBatchApi;
    @DubboReference
    private ErpClientApi            erpClientApi;
    @DubboReference
    private ErpHeartApi             erpHeartApi;

    @Value("${env.name}")
    private String envName;

    private static final String purchaseFlow = "purchase-template";
    private static final String saleFlow     = "sale-template";

    private static final String shopSaleFlow   = "shop-sale-template";
    private static final String goodsBatchFlow = "batch-template";
    public static final  String key            = "erp-delete-goods-batch-flow";


    @Override
    public void readFlowExcel() {
        List<FlowFtpClientDO> flowFtpClientDOList = this.list();
        Ftp ftp = null;
        for (FlowFtpClientDO flowFtpClientDO : flowFtpClientDOList) {
            Date nowDate = new Date();
            StringBuffer sb = new StringBuffer();
            flowFtpClientDO.setStartTime(nowDate);
            flowFtpClientDO.setSyncStatus(1);
            this.updateById(flowFtpClientDO);
            //匿名登录（无需帐号密码的FTP服务器）
            try {
                ftp = getConnectFtp(flowFtpClientDO);

                for (int i = -1; i < 1; i++) {
                    FTPFile[] fs = ftp.lsFiles("/");
                    Date date = new Date();

                    List<BaseErpEntity> erpPurchaseFlowList = new ArrayList<>();
                    List<BaseErpEntity> erpSaleFlowList = new ArrayList<>();
                    List<BaseErpEntity> erpGoodsBatchFlowList = new ArrayList<>();
                    List<BaseErpEntity> erpShopSaleFlowList = new ArrayList<>();

                    for (FTPFile ftpFile : fs) {
                        if (!ftpFile.isFile()) {
                            // 直接跳过文件夹
                            continue;
                        }

                        if (ftpFile.isFile() && !verificationFileName(ftpFile.getName())) {
                            // 检验文件格式，目前仅支持excel以及csv文件格式，其余文件直接跳过
                            sb.append("文件类型不对").append(";");
                            continue;
                        }

                        String purchaseFilePreFix = purchaseFlow + "-" + DateUtil.format(DateUtil.offsetDay(new Date(), i), "yyyyMMdd");
                        String saleFilePreFix = saleFlow + "-" + DateUtil.format(DateUtil.offsetDay(new Date(), i), "yyyyMMdd");
                        String goodsBatchFilePreFix = goodsBatchFlow + "-" + DateUtil.format(DateUtil.offsetDay(new Date(), i), "yyyyMMdd");
                        String shopSaleFlowPreFix = shopSaleFlow + "-" + DateUtil.format(DateUtil.offsetDay(new Date(), i), "yyyyMMdd");

                        String fileName = ftpFile.getName();

                        // 比较文件前缀
                        fileName = fileName.replaceAll("_", "-");
                        if (!fileName.startsWith(purchaseFilePreFix) && !fileName.startsWith(saleFilePreFix)
                                && !fileName.startsWith(goodsBatchFilePreFix) && !fileName.startsWith(shopSaleFlowPreFix)) {
                            continue;
                        }

                        //  创建任务
                        FlowFtpFileTaskDO flowFtpFileTaskDO = new FlowFtpFileTaskDO();
                        flowFtpFileTaskDO.setSuId(flowFtpClientDO.getSuId());
                        flowFtpFileTaskDO.setSourceFileName(ftpFile.getName());
                        flowFtpFileTaskDO.setAnalyticStatus(FlowFtpAnalyticStatusEnum.NOT_STARTED.getCode());
                        flowFtpFileTaskDO.setInstallEmployee(flowFtpClientDO.getInstallEmployee());
                        flowFtpFileTaskService.save(flowFtpFileTaskDO);

                        if (fileName.startsWith(purchaseFilePreFix)) {
                            List<ErpPurchaseFlowExcel> erpPurchaseFlowExcelList = new ArrayList<>();
                            List<ErpPurchaseFlowExcel> purchaseFailList = new ArrayList<>();
                            handleExcelOrCsvData(ftp, flowFtpClientDO, ftpFile, flowFtpFileTaskDO, sb, erpPurchaseFlowList, erpPurchaseFlowExcelList, purchaseFailList,
                                    ErpPurchaseFlowExcel.class, new ErpPurchaseFlowExcelReadHandler(purchaseFailList, erpPurchaseFlowExcelList));
                        }

                        if (fileName.startsWith(saleFilePreFix)) {
                            List<ErpSaleFlowExcel> erpSaleFlowExcelList = new ArrayList<>();
                            List<ErpSaleFlowExcel> saleFailList = new ArrayList<>();
                            handleExcelOrCsvData(ftp, flowFtpClientDO, ftpFile, flowFtpFileTaskDO, sb, erpSaleFlowList, erpSaleFlowExcelList, saleFailList,
                                    ErpSaleFlowExcel.class, new ErpSaleFlowExcelReadHandler(saleFailList, erpSaleFlowExcelList));
                        }

                        if (fileName.startsWith(goodsBatchFilePreFix)) {
                            List<ErpGoodsBatchFlowExcel> erpGoodsBatchFlowExcelList = new ArrayList<>();
                            List<ErpGoodsBatchFlowExcel> goodsBatchFailList = new ArrayList<>();
                            handleExcelOrCsvData(ftp, flowFtpClientDO, ftpFile, flowFtpFileTaskDO, sb, erpGoodsBatchFlowList, erpGoodsBatchFlowExcelList, goodsBatchFailList,
                                    ErpGoodsBatchFlowExcel.class, new ErpGoodsBatchFlowExcelReadHandler(goodsBatchFailList, erpGoodsBatchFlowExcelList));
                        }

                        if (fileName.startsWith(shopSaleFlowPreFix)) {
                            List<ErpShopSaleFlowExcel> erpShopSaleFlowExcelList = new ArrayList<>();
                            List<ErpShopSaleFlowExcel> shopSaleFlowFailList = new ArrayList<>();
                            handleExcelOrCsvData(ftp, flowFtpClientDO, ftpFile, flowFtpFileTaskDO, sb, erpShopSaleFlowList, erpShopSaleFlowExcelList, shopSaleFlowFailList,
                                    ErpShopSaleFlowExcel.class, new ErpShopSaleFlowExcelReadHandler(shopSaleFlowFailList, erpShopSaleFlowExcelList));
                        }

                        // 记录心跳最后时间
                        lastHeartBeatTime(flowFtpClientDO.getSuId());
                    }

                    handleErpPurchaseFlowExcelList(erpPurchaseFlowList, flowFtpClientDO);
                    handleErpSaleFlowExcelList(erpSaleFlowList, flowFtpClientDO);
                    handleErpGoodsBatchFlowExcelList(erpGoodsBatchFlowList, flowFtpClientDO, date);
                    handleErpShopSaleFlowExcelList(erpShopSaleFlowList, flowFtpClientDO);
                }

                sb.append("同步成功").append(";");
                flowFtpClientDO.setSyncStatus(2);
                flowFtpClientDO.setSyncMsg(StrUtil.subPre(sb.toString(), 1000));
                flowFtpClientDO.setEndTime(new Date());
                this.updateById(flowFtpClientDO);
            } catch (Exception e) {
                log.info("ftp流向对接报错su_id={}", flowFtpClientDO.getSuId(), e);
                flowFtpClientDO.setSyncStatus(3);
                flowFtpClientDO.setSyncMsg(ExceptionUtil.stacktraceToOneLineString(e, 1000));
                flowFtpClientDO.setEndTime(new Date());
                this.updateById(flowFtpClientDO);
            } finally {
                if (ftp != null) {
                    try {
                        ftp.close();
                    } catch (IOException e) {
                        log.error("ftp流向对接ftp流报错su_id={}", flowFtpClientDO.getSuId(), e);
                    }
                }
            }
        }

    }

    /**
     * 记录心跳最后时间
     *
     * @param suId
     */
    private void lastHeartBeatTime(Long suId) {
        // 保存心跳最新时间
        UpdateHeartBeatTimeRequest erpClientRequest = new UpdateHeartBeatTimeRequest();
        erpClientRequest.setSuId(suId);
        erpClientRequest.setOpUserId(0L);
        erpClientRequest.setVersions("");
        erpClientApi.updateHeartBeatTimeBySuid(erpClientRequest);
        // 保存心跳
        SysHeartBeatDTO sysHeartBeat = new SysHeartBeatDTO();
        sysHeartBeat.setProcessId("");
        sysHeartBeat.setRunPath("");
        String taskIds = ErpTopicName.ErpPurchaseFlow.getMethod().concat(",").concat(ErpTopicName.ErpSaleFlow.getMethod()).concat(",").concat(ErpTopicName.ErpGoodsBatchFlow.getMethod());
        sysHeartBeat.setRuntaskIds(taskIds);
        sysHeartBeat.setVersions("");
        sysHeartBeat.setMac("");
        sysHeartBeat.setIp("");
        sysHeartBeat.setSuId(suId);
        sysHeartBeat.setSuName("");
        erpHeartApi.insertErpHeart(sysHeartBeat);
    }

    private File downloadFtpFile(Ftp ftp, FlowFtpClientDO flowFtpClientDO, FTPFile ftpFile, String erpTopicNameMethod) {
        String fileName = ftpFile.getName();

        File tmpDir = FileUtil.getTmpDir();
        File dir = FileUtil.newFile(tmpDir.getPath() + File.separator + flowFtpClientDO.getSuId() + File.separator + erpTopicNameMethod);
        File dirBak = FileUtil.newFile(tmpDir.getPath() + File.separator + flowFtpClientDO.getSuId() + File.separator + erpTopicNameMethod + File.separator + "bakup");

        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        if (!dirBak.isDirectory()) {
            dirBak.mkdirs();
        }
        File excelDir = FileUtil.newFile(dir.getPath() + File.separator + fileName);
        if (excelDir.exists()) {
            FileUtil.del(excelDir);
        } else {
            try {
                excelDir.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        ftp.download("/", fileName, excelDir);

        return excelDir;
    }

    private <T> void handleExcelOrCsvData(Ftp ftp, FlowFtpClientDO flowFtpClientDO, FTPFile ftpFile, FlowFtpFileTaskDO flowFtpFileTaskDO, StringBuffer sb,
                                          List<BaseErpEntity> erpEntityFlowList, List<T> erpFlowDataList, List<T> failList, Class<T> clazz, IReadHandler<T> handler) {
        // 下载ftp文件
        File localFile = downloadFtpFile(ftp, flowFtpClientDO, ftpFile, ErpTopicName.ErpOrderPurchase.getMethod());

        // 当前文件错误信息
        StringBuilder errMsg = new StringBuilder();
        try {
            // 开始解析，修改任务状态
            flowFtpFileTaskDO.setAnalyticStatus(FlowFtpAnalyticStatusEnum.ANALYZING.getCode());
            flowFtpFileTaskDO.setAnalyticStartTime(new Date());
            flowFtpFileTaskService.updateById(flowFtpFileTaskDO);

            InputStream inputStream = Files.newInputStream(localFile.toPath());
            ImportParams params = new ImportParams();
            params.setNeedVerify(true);
            params.setHeadRows(1);
            if (clazz == ErpPurchaseFlowExcel.class || clazz == ErpSaleFlowExcel.class) {    // 设置为空时，直接跳过的列
                params.setKeyIndex(2);  // 销售和采购第三列为空时直接跳过
            } else if (clazz == ErpGoodsBatchFlowExcel.class) {
                params.setKeyIndex(3);  // 库存第四列为空时直接跳过
            } else {
                params.setKeyIndex(4);  // 纯销第五列为空时直接跳过
            }

            if (ftpFile.getName().toLowerCase().endsWith(".csv")) {     // csv
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                String line = null;
                boolean isTitleLine = true;
                while ((line = br.readLine()) != null) {
                    if (isTitleLine) {   // 标题行跳过
                        isTitleLine = false;
                        continue;
                    }
                    String[] valueArr = line.split(",");

                    covertFlowCsvData(valueArr, clazz, handler);
                }
                br.close();

            } else {    // excel
                if (flowFtpClientDO.getFileType() == 1) {
                    ExcelImportUtil.importExcelBySax(inputStream, clazz, params, handler);
                } else {
                    if (localFile.length() > 10 * 1024 * 1024) {
                        //  文件大小大于10M
                        sb.append("文件大小不可大于10M").append(";");   // 历史遗留
                        errMsg.append("文件大小不可大于10M").append(";");
                        throw new BusinessException(ResultCode.EXCEL_PARSING_ERROR, "FTP流向数据文件importExcelByIs大小不可大于10M");
                    }
                    ExcelImportResult<T> result = new ExcelImportService().importExcelByIs(inputStream, clazz, params, true);
                    if (CollUtil.isNotEmpty(result.getFailList())) {
                        failList.addAll(result.getFailList());
                    }
                    erpFlowDataList.addAll(result.getList());
                }
            }

            if (CollUtil.isNotEmpty(failList)) {
                IExcelModel erpFlowData = (IExcelModel) failList.get(0);
                sb.append(erpFlowData.getErrorMsg()).append(";");
                errMsg.append(erpFlowData.getErrorMsg()).append(";");
            } else {
                //  将解析的文件数据转化为对象
                if (clazz == ErpPurchaseFlowExcel.class) {
                    List<ErpPurchaseFlowDTO> erpPurchaseFlowDTOList = covertToErpPurchaseFlowDTOList(PojoUtils.map(erpFlowDataList, ErpPurchaseFlowExcel.class));
                    erpEntityFlowList.addAll(erpPurchaseFlowDTOList);
                } else if (clazz == ErpSaleFlowExcel.class) {
                    List<ErpSaleFlowDTO> erpSaleFlowDTOList = covertToErpSaleFlowDTOList(PojoUtils.map(erpFlowDataList, ErpSaleFlowExcel.class));
                    erpEntityFlowList.addAll(erpSaleFlowDTOList);
                } else if (clazz == ErpGoodsBatchFlowExcel.class) {
                    List<ErpGoodsBatchFlowDTO> erpGoodsBatchFlowDTOList = covertToErpGoodsBatchFlowDTOList(PojoUtils.map(erpFlowDataList, ErpGoodsBatchFlowExcel.class), flowFtpClientDO);
                    erpEntityFlowList.addAll(erpGoodsBatchFlowDTOList);
                } else if (clazz == ErpShopSaleFlowExcel.class) {
                    List<ErpShopSaleFlowDTO> erpShopSaleFlowDTOList = covertToErpShopSaleFlowDTOList(PojoUtils.map(erpFlowDataList, ErpShopSaleFlowExcel.class));
                    erpEntityFlowList.addAll(erpShopSaleFlowDTOList);
                }

                // 将源文件移动至bakup目录下
                if (!ftp.exist("bakup")) {
                    ftp.mkdir("bakup");
                }
                ftp.upload("/bakup", ftpFile.getName(), localFile);
                ftp.cd("/");
                ftp.delFile(ftpFile.getName());
                sb.append(ftpFile.getName()).append(";");
            }

            //  修改任务状态
            flowFtpFileTaskDO.setSuccessCount(erpFlowDataList.size());
            flowFtpFileTaskDO.setFailCount(failList.size());
            flowFtpFileTaskDO.setErrMsg(errMsg.toString());
            flowFtpFileTaskDO.setAnalyticEndTime(new Date());
            if (flowFtpFileTaskDO.getFailCount() > 0) {
                // 若存在解析失败的条数，则该文件算作解析失败
                flowFtpFileTaskDO.setAnalyticStatus(FlowFtpAnalyticStatusEnum.FAIL.getCode());
            } else {
                flowFtpFileTaskDO.setAnalyticStatus(FlowFtpAnalyticStatusEnum.FINISH.getCode());
            }

            flowFtpFileTaskService.updateById(flowFtpFileTaskDO);

        } catch (Exception e) {
            log.info("ftp采购流向对接报错su_id={}", flowFtpClientDO.getSuId(), e);
            String excMsgStr = ExceptionUtil.stacktraceToOneLineString(e, 1000);
            sb.append(excMsgStr).append(";");
            errMsg.append(excMsgStr);

            flowFtpFileTaskDO.setSuccessCount(erpFlowDataList.size());
            flowFtpFileTaskDO.setFailCount(failList.size());
            flowFtpFileTaskDO.setAnalyticStatus(FlowFtpAnalyticStatusEnum.FAIL.getCode());
            flowFtpFileTaskDO.setErrMsg(errMsg.toString());
            flowFtpFileTaskDO.setAnalyticEndTime(new Date());
            flowFtpFileTaskService.updateById(flowFtpFileTaskDO);

        } finally {
            // 清空临时文件
            FileUtil.del(localFile.getAbsolutePath());

        }
    }

    @Override
    public boolean saleFlowCompare(Long suId, Integer flowDay, List<ErpSaleFlowDTO> erpSaleFlowList) {
        String lockName = RedisKey.generate("ftp", "lock", "sale", String.valueOf(suId));
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            return saleFlowCompareLock(suId, flowDay, erpSaleFlowList);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    private boolean saleFlowCompareLock(Long suId, Integer flowDay, List<ErpSaleFlowDTO> erpSaleFlowList) {
        File tmpDir = FileUtil.getTmpDir();
        File dir = FileUtil.newFile(tmpDir.getPath() + File.separator + suId + File.separator + ErpTopicName.ErpPurchaseFlow.getMethod());
        try {
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            //通过上传文件返回结果在处理返回结果
            QueryWrapper<FlowFtpCacheDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FlowFtpCacheDO::getSuId, suId).eq(FlowFtpCacheDO::getType, ErpTopicName.ErpSaleFlow.getMethod());
            List<FlowFtpCacheDO> flowFtpCacheDOList = flowFtpCacheMapper.selectList(queryWrapper);
            // sqlite缓存的数据
            Map<String, String> sqliteData = flowFtpCacheDOList.stream().collect(Collectors.toMap(FlowFtpCacheDO::getErpKey, FlowFtpCacheDO::getMd5));
            log.info("[ERP销售订单流向信息同步] 本地缓存销售订单流向信息条数:" + sqliteData.size() + " | ERP系统销售订单流向信息条数:" + erpSaleFlowList.size());

            // 数据按照部门分组
            Map<String, List<ErpSaleFlowDTO>> deptMap = erpSaleFlowList.stream().collect(Collectors.groupingBy(e -> StrUtil.nullToEmpty(e.getSuDeptNo())));
            log.info("[ERP销售订单流向信息同步] 部门:" + deptMap.keySet());

            // 每个部门的数据再按照日期分组处理
            for (Map.Entry<String, List<ErpSaleFlowDTO>> entry : deptMap.entrySet()) {
                String suDeptNo = entry.getKey();
                List<ErpSaleFlowDTO> deptList = entry.getValue();

                // 数据按照日期分组
                Map<String, List<ErpSaleFlowDTO>> dateMap = deptList.stream().collect(Collectors.groupingBy(e -> DateUtil.format(e.getSoTime(), "yyyy-MM-dd")));
                // 日期范围列表
                List<String> dateStrList = getSaleListByDate(dateMap, flowDay);
                log.info("[ERP销售订单流向信息同步] 部门:" + suDeptNo + ", 查询日期:" + dateStrList.get(0) + "至" + dateStrList.get(dateStrList.size() - 1));

                // 待同步数据
                List<ErpFlowControlDTO> addList = new ArrayList<>();
                List<ErpFlowControlDTO> updateList = new ArrayList<>();

                // 每天的数据进行处理
                boolean handlerFlag = handlerSaleByDate(sqliteData, suDeptNo, dateMap, dateStrList, addList, updateList, suId, dir);
                if (!handlerFlag) {
                    continue;
                }

                // 同步数据
                if (CollUtil.isNotEmpty(addList)) {
                    log.info("[ERP销售订单流向信息同步], 发送消息，上传ERP销售订单流向数据压缩包，新增");
                    //发送mq
                    rocketMqProducerService.sendSync(ErpTopicName.ErpFlowControl.getTopicName(), suId + "", DateUtil.formatDate(new Date()), JSON.toJSONString(addList));
                    List<BaseErpEntity> erpFlowControlDTOList = new ArrayList<>();
                    for (ErpFlowControlDTO erpFlowControlDTO : addList) {
                        erpFlowControlDTOList.add(erpFlowControlDTO);
                    }
                    flowFtpCacheService.addCache(erpFlowControlDTOList, suId, ErpTopicName.ErpSaleFlow.getMethod());
                }
                if (CollUtil.isNotEmpty(updateList)) {
                    log.info("[ERP销售订单流向信息同步], 发送消息，上传ERP销售订单流向数据压缩包，更新");
                    //发送mq
                    rocketMqProducerService.sendSync(ErpTopicName.ErpFlowControl.getTopicName(), suId + "", DateUtil.formatDate(new Date()), JSON.toJSONString(updateList));
                    List<BaseErpEntity> erpFlowControlDTOList = new ArrayList<>();
                    for (ErpFlowControlDTO erpFlowControlDTO : updateList) {
                        erpFlowControlDTOList.add(erpFlowControlDTO);
                    }
                    flowFtpCacheService.updateCache(erpFlowControlDTOList, suId, ErpTopicName.ErpSaleFlow.getMethod());
                }
            }
            return true;
        } catch (Exception e) {
            log.error("ERP销售订单流向本地缓存对比上传报错su_id={}", suId, e);
        } finally {
            FileUtil.del(dir);
        }
        return false;
    }

    @Override
    public boolean purchaseFlowCompare(Long suId, Integer flowDay, List<ErpPurchaseFlowDTO> erpPurchaseFlowList) {
        String lockName = RedisKey.generate("ftp", "lock", "purchase", String.valueOf(suId));
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            return purchaseFlowCompareLock(suId, flowDay, erpPurchaseFlowList);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    private boolean purchaseFlowCompareLock(Long suId, Integer flowDay, List<ErpPurchaseFlowDTO> erpPurchaseFlowList) {
        File tmpDir = FileUtil.getTmpDir();
        File dir = FileUtil.newFile(tmpDir.getPath() + File.separator + suId + File.separator + ErpTopicName.ErpPurchaseFlow.getMethod());
        try {
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            //通过上传文件返回结果在处理返回结果
            QueryWrapper<FlowFtpCacheDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FlowFtpCacheDO::getSuId, suId).eq(FlowFtpCacheDO::getType, ErpTopicName.ErpPurchaseFlow.getMethod());
            List<FlowFtpCacheDO> flowFtpCacheDOList = flowFtpCacheMapper.selectList(queryWrapper);
            // sqlite缓存的数据
            Map<String, String> sqliteData = flowFtpCacheDOList.stream().collect(Collectors.toMap(FlowFtpCacheDO::getErpKey, FlowFtpCacheDO::getMd5));

            log.info("[ERP采购订单流向信息同步] 本地缓存采购订单流向信息条数:" + sqliteData.size() + " | ERP系统采购订单流向信息条数:" + erpPurchaseFlowList.size());

            // 数据按照部门分组
            Map<String, List<ErpPurchaseFlowDTO>> deptMap = erpPurchaseFlowList.stream().collect(Collectors.groupingBy(e -> StrUtil.nullToEmpty(e.getSuDeptNo())));
            log.info("[ERP采购订单流向信息同步] 部门:" + deptMap.keySet());

            // 每个部门的数据再按照日期分组处理
            for (Map.Entry<String, List<ErpPurchaseFlowDTO>> entry : deptMap.entrySet()) {
                String suDeptNo = entry.getKey();
                List<ErpPurchaseFlowDTO> deptList = entry.getValue();

                // 数据按照日期分组
                Map<String, List<ErpPurchaseFlowDTO>> dateMap = deptList.stream().collect(Collectors.groupingBy(e -> DateUtil.format(e.getPoTime(), "yyyy-MM-dd")));
                // 日期范围列表
                List<String> dateStrList = getPurchaseListByDate(dateMap, flowDay);
                log.info("[ERP采购订单流向信息同步] 部门:" + suDeptNo + ", 查询日期:" + dateStrList.get(0) + "至" + dateStrList.get(dateStrList.size() - 1));

                // 待同步数据
                List<ErpFlowControlDTO> addList = new ArrayList<>();
                List<ErpFlowControlDTO> updateList = new ArrayList<>();

                // 每天的数据进行处理
                boolean handlerFlag = handlerPurchaseByDate(sqliteData, suDeptNo, dateMap, dateStrList, addList, updateList, suId, dir);
                if (!handlerFlag) {
                    continue;
                }

                // 同步数据
                if (CollUtil.isNotEmpty(addList)) {
                    log.info("[ERP采购订单流向信息同步], 发送消息，上传ERP采购订单流向数据压缩包，新增");
                    //发送mq
                    rocketMqProducerService.sendSync(ErpTopicName.ErpFlowControl.getTopicName(), suId + "", DateUtil.formatDate(new Date()), JSON.toJSONString(addList));
                    List<BaseErpEntity> erpFlowControlDTOList = new ArrayList<>();
                    for (ErpFlowControlDTO erpFlowControlDTO : addList) {
                        erpFlowControlDTOList.add(erpFlowControlDTO);
                    }
                    flowFtpCacheService.addCache(erpFlowControlDTOList, suId, ErpTopicName.ErpPurchaseFlow.getMethod());
                }
                if (CollUtil.isNotEmpty(updateList)) {
                    log.info("[ERP采购订单流向信息同步], 发送消息，上传ERP采购订单流向数据压缩包，更新");
                    //发送mq
                    rocketMqProducerService.sendSync(ErpTopicName.ErpFlowControl.getTopicName(), suId + "", DateUtil.formatDate(new Date()), JSON.toJSONString(updateList));
                    List<BaseErpEntity> erpFlowControlDTOList = new ArrayList<>();
                    for (ErpFlowControlDTO erpFlowControlDTO : updateList) {
                        erpFlowControlDTOList.add(erpFlowControlDTO);
                    }
                    flowFtpCacheService.updateCache(erpFlowControlDTOList, suId, ErpTopicName.ErpPurchaseFlow.getMethod());
                }
            }
            return true;
        } catch (Exception e) {
            log.error("ERP采购订单流向本地缓存对比上传报错su_id={}", suId, e);
        } finally {
            FileUtil.del(dir);
        }
        return false;
    }


    public boolean shopSaleFlowSync(Long suId, Integer shopSaleDay, List<ErpShopSaleFlowDTO> erpShopSaleFlowList) {
        String lockName = RedisKey.generate("ftp", "lock", "shopSale", String.valueOf(suId));
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            return shopSaleFlowSyncLock(suId, shopSaleDay, erpShopSaleFlowList);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    private boolean shopSaleFlowSyncLock(Long suId, Integer shopSaleDay, List<ErpShopSaleFlowDTO> erpShopSaleFlowList) {
        File tmpDir = FileUtil.getTmpDir();
        File dir = FileUtil.newFile(tmpDir.getPath() + File.separator + suId + File.separator + ErpTopicName.ErpShopSaleFlow.getMethod());
        try {
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }

            //通过上传文件返回结果在处理返回结果
            QueryWrapper<FlowFtpCacheDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FlowFtpCacheDO::getSuId, suId).eq(FlowFtpCacheDO::getType, ErpTopicName.ErpShopSaleFlow.getMethod());
            List<FlowFtpCacheDO> flowFtpCacheDOList = flowFtpCacheMapper.selectList(queryWrapper);
            // sqlite缓存的数据
            Map<String, String> sqliteData = flowFtpCacheDOList.stream().collect(Collectors.toMap(FlowFtpCacheDO::getErpKey, FlowFtpCacheDO::getMd5));

            log.info("[ERP纯销流向信息同步] 本地缓存纯销流向信息条数:" + sqliteData.size() + " | ERP系统纯销流向信息条数:" + erpShopSaleFlowList.size());

            // 数据按照部门分组
            Map<String, List<ErpShopSaleFlowDTO>> deptMap = erpShopSaleFlowList.stream().collect(Collectors.groupingBy(e -> StrUtil.nullToEmpty(e.getSuDeptNo())));
            log.info("[ERP纯销订单流向信息同步] 部门:" + deptMap.keySet());

            for (Map.Entry<String, List<ErpShopSaleFlowDTO>> entry : deptMap.entrySet()) {
                String suDeptNo = entry.getKey();
                List<ErpShopSaleFlowDTO> deptList = entry.getValue();

                // 数据按照日期分组
                Map<String, List<ErpShopSaleFlowDTO>> dateMap = deptList.stream().collect(Collectors.groupingBy(e -> DateUtil.format(e.getSoTime(), "yyyy-MM-dd")));
                // 日期范围列表
                List<String> dateStrList = getShopSaleListByDate(dateMap, shopSaleDay);
                log.info("[ERP纯销流向信息同步] 部门:" + suDeptNo + ", 查询日期:" + dateStrList.get(0) + "至" + dateStrList.get(dateStrList.size() - 1));

                // 待同步数据
                List<ErpFlowControlDTO> addList = new ArrayList<>();
                List<ErpFlowControlDTO> updateList = new ArrayList<>();

                // 每天的数据进行处理
                boolean handlerFlag = handlerShopSaleByDate(sqliteData, suDeptNo, dateMap, dateStrList, addList, updateList, suId, dir);
                if (!handlerFlag) {
                    return false;
                }

                // 同步数据
                if (CollUtil.isNotEmpty(addList)) {
                    log.info("[ERP纯销流向信息同步], 发送消息，上传ERP纯销流向数据压缩包，新增");
                    //发送mq
                    rocketMqProducerService.sendSync(ErpTopicName.ErpFlowControl.getTopicName(), suId + "", DateUtil.formatDate(new Date()), JSON.toJSONString(addList));
                    List<BaseErpEntity> erpFlowControlDTOList = new ArrayList<>();
                    for (ErpFlowControlDTO erpFlowControlDTO : addList) {
                        erpFlowControlDTOList.add(erpFlowControlDTO);
                    }
                    flowFtpCacheService.addCache(erpFlowControlDTOList, suId, ErpTopicName.ErpShopSaleFlow.getMethod());
                }
                if (CollUtil.isNotEmpty(updateList)) {
                    log.info("[ERP纯销流向信息同步], 发送消息，上传ERP纯销流向数据压缩包，更新");
                    //发送mq
                    rocketMqProducerService.sendSync(ErpTopicName.ErpFlowControl.getTopicName(), suId + "", DateUtil.formatDate(new Date()), JSON.toJSONString(updateList));
                    List<BaseErpEntity> erpFlowControlDTOList = new ArrayList<>();
                    for (ErpFlowControlDTO erpFlowControlDTO : updateList) {
                        erpFlowControlDTOList.add(erpFlowControlDTO);
                    }
                    flowFtpCacheService.updateCache(erpFlowControlDTOList, suId, ErpTopicName.ErpShopSaleFlow.getMethod());
                }
            }

            return true;
        } catch (Exception e) {
            log.error("ERP纯销流向上传报错su_id={}", suId, e);
        } finally {
            FileUtil.del(dir);
        }
        return false;
    }


    public List<String> getPurchaseListByDate(Map<String, List<ErpPurchaseFlowDTO>> handlerMap, Integer flowDateCount) {
        List<String> dateStrList = dateList(flowDateCount);
        for (String dateStr : dateStrList) {
            if (!handlerMap.containsKey(dateStr)) {
                // 没有数据的，填补日期
                handlerMap.put(dateStr, new ArrayList<>());
            }
        }
        return dateStrList;
    }

    public List<String> getSaleListByDate(Map<String, List<ErpSaleFlowDTO>> handlerMap, Integer flowDateCount) {
        List<String> dateStrList = dateList(flowDateCount);
        for (String dateStr : dateStrList) {
            if (!handlerMap.containsKey(dateStr)) {
                // 没有数据的，填补日期
                handlerMap.put(dateStr, new ArrayList<>());
            }
        }
        return dateStrList;
    }

    public List<String> getShopSaleListByDate(Map<String, List<ErpShopSaleFlowDTO>> handlerMap, Integer flowDateCount) {
        List<String> dateStrList = dateList(flowDateCount);
        for (String dateStr : dateStrList) {
            if (!handlerMap.containsKey(dateStr)) {
                // 没有数据的，填补日期
                handlerMap.put(dateStr, new ArrayList<>());
            }
        }
        return dateStrList;
    }


    public List<String> dateList(Integer flowDateCount) {
        Integer count = flowDateCount - 1;
        List<String> dateStrList = new ArrayList<>();
        Date endDate = new Date();
        Date startDate = cn.hutool.core.date.DateUtil.offset(endDate, DateField.DAY_OF_MONTH, count * -1);
        List<DateTime> dateTimeList = cn.hutool.core.date.DateUtil.rangeToList(startDate, endDate, DateField.DAY_OF_MONTH);
        for (DateTime dateTime : dateTimeList) {
            String dateStr = cn.hutool.core.date.DateUtil.format(dateTime.toJdkDate(), "yyyy-MM-dd");
            dateStrList.add(dateStr);
        }
        return dateStrList;
    }


    private boolean handlerPurchaseByDate(Map<String, String> sqliteData, String suDeptNo, Map<String, List<ErpPurchaseFlowDTO>> dateMap, List<String> dateStrList, List<ErpFlowControlDTO> addList, List<ErpFlowControlDTO> updateList, Long suId, File dir) throws Exception {
        for (String time : dateStrList) {
            List<ErpPurchaseFlowDTO> list = dateMap.getOrDefault(time, new ArrayList<>());
            String jsonList = JSON.toJSONString(list);
            String sign = list.stream().map(e -> e.getErpPrimaryKey()).collect(Collectors.joining());
            String md5 = SecureUtil.md5(sign);
            // 校验md5
            String md5Cash = sqliteData.get(this.getSuDeptNoKey(time, suDeptNo));

            if (ObjectUtil.equal(md5, md5Cash)) {
                continue;
            }
            // 写入文件
            String textPath = dir.getPath() + File.separator + time + ".txt";
            FileUtil.writeUtf8String(jsonList, textPath);
            // 压缩文件
            String zipPath = dir.getPath() + File.separator + time + ".zip";
            ZipUtil.zip(textPath, zipPath);
            // 获取md5
            File file = new File(zipPath);
            // 上传压缩包
            String dateDir = time.replace("-", "/");
            FileInfo fileInfo = fileService.flowUpload(file, FileTypeEnum.ERP_PURCHASE_FLOW, envName, String.valueOf(suId), suDeptNo, dateDir);
            // 组装待同步数据
            ErpFlowControlDTO erpFlowControl;
            if (StringUtils.isBlank(md5Cash)) {
                erpFlowControl = buildErpFlowControl(time, md5, Long.valueOf(suId), suDeptNo, fileInfo, OperTypeEnum.ADD.getCode(), Integer.parseInt(ErpTopicName.ErpPurchaseFlow.getMethod()));
                addList.add(erpFlowControl);
            } else {
                erpFlowControl = buildErpFlowControl(time, md5, Long.valueOf(suId), suDeptNo, fileInfo, OperTypeEnum.UPDATE.getCode(), Integer.parseInt(ErpTopicName.ErpPurchaseFlow.getMethod()));
                updateList.add(erpFlowControl);
            }
        }
        return true;
    }

    private boolean handlerSaleByDate(Map<String, String> sqliteData, String suDeptNo, Map<String, List<ErpSaleFlowDTO>> dateMap, List<String> dateStrList, List<ErpFlowControlDTO> addList, List<ErpFlowControlDTO> updateList, Long suId, File dir) throws Exception {
        for (String time : dateStrList) {
            List<ErpSaleFlowDTO> list = dateMap.getOrDefault(time, new ArrayList<>());
            String jsonList = JSON.toJSONString(list);
            String sign = list.stream().map(e -> e.toString()).collect(Collectors.joining());
            String md5 = SecureUtil.md5(sign);
            // 校验md5
            String md5Cash = sqliteData.get(this.getSuDeptNoKey(time, suDeptNo));

            if (ObjectUtil.equal(md5, md5Cash)) {
                continue;
            }
            // 写入文件
            String textPath = dir.getPath() + File.separator + time + ".txt";
            FileUtil.writeUtf8String(jsonList, textPath);
            // 压缩文件
            String zipPath = dir.getPath() + File.separator + time + ".zip";
            ZipUtil.zip(textPath, zipPath);
            // 获取md5
            File file = new File(zipPath);
            // 上传压缩包
            String dateDir = time.replace("-", "/");
            FileInfo fileInfo = fileService.flowUpload(file, FileTypeEnum.ERP_SALE_FLOW, envName, String.valueOf(suId), suDeptNo, dateDir);
            // 组装待同步数据
            ErpFlowControlDTO erpFlowControl;
            if (StringUtils.isBlank(md5Cash)) {
                erpFlowControl = buildErpFlowControl(time, md5, Long.valueOf(suId), suDeptNo, fileInfo, OperTypeEnum.ADD.getCode(), Integer.parseInt(ErpTopicName.ErpSaleFlow.getMethod()));
                addList.add(erpFlowControl);
            } else {
                erpFlowControl = buildErpFlowControl(time, md5, Long.valueOf(suId), suDeptNo, fileInfo, OperTypeEnum.UPDATE.getCode(), Integer.parseInt(ErpTopicName.ErpSaleFlow.getMethod()));
                updateList.add(erpFlowControl);
            }
        }
        return true;
    }

    private boolean handlerShopSaleByDate(Map<String, String> sqliteData, String suDeptNo, Map<String, List<ErpShopSaleFlowDTO>> dateMap, List<String> dateStrList, List<ErpFlowControlDTO> addList, List<ErpFlowControlDTO> updateList, Long suId, File dir) throws Exception {
        for (String time : dateStrList) {
            List<ErpShopSaleFlowDTO> list = dateMap.getOrDefault(time, new ArrayList<>());
            String jsonList = JSON.toJSONString(list);
            String sign = list.stream().map(e -> e.toString()).collect(Collectors.joining());
            String md5 = SecureUtil.md5(sign);

            String md5Cash = sqliteData.get(this.getSuDeptNoKey(time, suDeptNo));

            if (ObjectUtil.equal(md5, md5Cash)) {
                continue;
            }

            // 写入文件
            String textPath = dir.getPath() + File.separator + time + ".txt";
            FileUtil.writeUtf8String(jsonList, textPath);
            // 压缩文件
            String zipPath = dir.getPath() + File.separator + time + ".zip";
            ZipUtil.zip(textPath, zipPath);
            // 获取md5
            File file = new File(zipPath);
            // 上传压缩包
            String dateDir = time.replace("-", "/");
            FileInfo fileInfo = fileService.flowUpload(file, FileTypeEnum.ERP_SHOP_SALE_FLOW, envName, String.valueOf(suId), "", dateDir);
            // 组装待同步数据
            ErpFlowControlDTO erpFlowControl;
            if (StringUtils.isBlank(md5Cash)) {
                erpFlowControl = buildErpFlowControl(time, md5, suId, suDeptNo, fileInfo, OperTypeEnum.ADD.getCode(), Integer.parseInt(ErpTopicName.ErpShopSaleFlow.getMethod()));
                addList.add(erpFlowControl);
            } else {
                erpFlowControl = buildErpFlowControl(time, md5, suId, suDeptNo, fileInfo, OperTypeEnum.UPDATE.getCode(), Integer.parseInt(ErpTopicName.ErpShopSaleFlow.getMethod()));
                updateList.add(erpFlowControl);
            }

        }
        return true;
    }

    private ErpFlowControlDTO buildErpFlowControl(String time, String cacheFileMd5, Long suId, String suDeptNo, FileInfo fileInfo, Integer operType, Integer taskCode) {
        ErpFlowControlDTO erpFlowControl = new ErpFlowControlDTO();
        erpFlowControl.setSuId(suId);
        erpFlowControl.setTaskCode(taskCode);
        erpFlowControl.setFileTime(DateUtil.parse(time, "yyyy-MM-dd"));
        erpFlowControl.setFileMd5(fileInfo.getMd5());
        erpFlowControl.setFileKey(fileInfo.getKey());
        erpFlowControl.setOperType(operType);
        erpFlowControl.setCacheFilemd5(cacheFileMd5);
        erpFlowControl.setSuDeptNo(suDeptNo);
        return erpFlowControl;
    }

    @Override
    public void localCompare(LocalCompareDTO localCompareDTO) {
        //1.先通过suId查询分公司
        Date time = localCompareDTO.getTime();
        List<ErpClientDTO> erpClientDOList = erpClientService.selectBySuId(localCompareDTO.getSuId());
        if (CollUtil.isNotEmpty(erpClientDOList)) {
            List<Long> eids = erpClientDOList.stream().map(e -> e.getRkSuId()).collect(Collectors.toList());
            //2.删除op库流向库存数据
            if (time == null) {
                time = new Date();
            }
            log.info("suid={}库存删除时间为{}", localCompareDTO.getSuId(), DateUtil.formatDateTime(time));
            //加上并发控制
            String lockName = "open-localCompare-suId-" + localCompareDTO.getSuId();
            String lockId = "";
            try {
                lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
                if (StringUtils.isEmpty(lockId)) {
                    throw new BusinessException(OpenErrorCode.ERP_LOCALCOMPARE_MULTIT_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
                }
                erpGoodsBatchFlowMapper.deleteGoodsBatchFlowBySuId(localCompareDTO.getSuId(), time);
                flowGoodsBatchApi.deleteFlowGoodsBatchByEids(eids, time);
                stringRedisTemplate.opsForHash().put(key, String.valueOf(localCompareDTO.getSuId()), DateUtil.formatDateTime(time));
                if (CollUtil.isNotEmpty(localCompareDTO.getDataList())) {
                    List<List<BaseErpEntity>> groupErpGoodsBatchFlowDTOList = this.groupList(localCompareDTO.getDataList(), 2000);
                    for (List<BaseErpEntity> erpGoodsBatchFlowList : groupErpGoodsBatchFlowDTOList) {
                        rocketMqProducerService.sendSync(ErpTopicName.ErpGoodsBatchFlow.getTopicName(), localCompareDTO.getSuId() + "", DateUtil.formatDateTime(new Date()), JSON.toJSONString(erpGoodsBatchFlowList));
                    }
                }
            } finally {
                redisDistributedLock.releaseLock(lockName, lockId);
            }
        }
    }

    public String getSuDeptNoKey(String time, String suDeptNo) {
        if (org.springframework.util.StringUtils.isEmpty(suDeptNo)) {
            return time;
        }
        return time + OpenConstants.SPLITE_SYMBOL + suDeptNo;
    }

    /**
     * @param list
     * @return map
     */
    @Override
    public List<List<BaseErpEntity>> groupList(List<BaseErpEntity> list, Integer index) {
        //listSize为集合长度
        int listSize = list.size();
        //用map存起来新的分组后数据
        List<List<BaseErpEntity>> returnList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += index) {
            //作用为Index最后没有1000条数据，则剩余的条数newList中就装几条
            if (i + index > listSize) {
                index = listSize - i;
            }
            //使用subList方法，keyToken用来记录循环了多少次或者每个map数据的键值
            List newList = list.subList(i, i + index);
            //每取一次放到map集合里，然后
            returnList.add(newList);
        }
        return returnList;
    }

    //解析Excel日期格式
    public String ExcelDoubleToDate(String strDate) {
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

    //解析Excel日期格式
    public Date DoubleToDate(Double dVal) {
        Date tDate = new Date();
        long localOffset = tDate.getTimezoneOffset() * 60000; //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        tDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));
        return tDate;
    }

    public boolean verificationFileName(String fileName) {
        if (fileName.toLowerCase().endsWith(".xlsx") || fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".csv")) {
            return true;
        }
        return false;
    }

    public boolean verificationFile2007(String fileName) {
        if (fileName.toLowerCase().endsWith(".xlsx")) {
            return true;
        }
        return false;
    }

    // 获取ftp连接
    private Ftp getConnectFtp(FlowFtpClientDO flowFtpClientDO) {
        FtpConfig ftpConfig = new FtpConfig();
        ftpConfig.setUser(flowFtpClientDO.getFtpUserName());
        ftpConfig.setHost(flowFtpClientDO.getFtpIp());
        ftpConfig.setPort(flowFtpClientDO.getFtpPort());
        ftpConfig.setPassword(flowFtpClientDO.getFtpPassWord());
        ftpConfig.setSoTimeout(1000 * 60 * 60);
        ftpConfig.setConnectionTimeout(1000 * 60 * 60);
        ftpConfig.setCharset(Charset.forName("GBK"));
        return new Ftp(ftpConfig, FtpMode.Active);
    }

    private <T> void covertFlowCsvData(String[] valueArr, Class<T> clazz, IReadHandler<T> readHandler) {
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

    public String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }


    private void handleErpPurchaseFlowExcelList(List<BaseErpEntity> list, FlowFtpClientDO flowFtpClientDO) {
        if (CollUtil.isEmpty(list)) {
            log.info("[ftp流向对接] 商业公司su_id={} 采购流向数据为空", flowFtpClientDO.getSuId());
            return;
        }
        List<ErpPurchaseFlowDTO> erpPurchaseFlowList = PojoUtils.map(list, ErpPurchaseFlowDTO.class);
        purchaseFlowCompare(flowFtpClientDO.getSuId(), flowFtpClientDO.getFlowDay(), erpPurchaseFlowList);
    }

    private void handleErpSaleFlowExcelList(List<BaseErpEntity> list, FlowFtpClientDO flowFtpClientDO) {
        if (CollUtil.isEmpty(list)) {
            log.info("[ftp流向对接]商业公司su_id={} 销售流向数据为空", flowFtpClientDO.getSuId());
            return;
        }
        List<ErpSaleFlowDTO> erpSaleFlowList = PojoUtils.map(list, ErpSaleFlowDTO.class);
        saleFlowCompare(flowFtpClientDO.getSuId(), flowFtpClientDO.getFlowDay(), erpSaleFlowList);
    }


    private void handleErpGoodsBatchFlowExcelList(List<BaseErpEntity> list, FlowFtpClientDO flowFtpClientDO, Date date) {
        if (CollUtil.isEmpty(list)) {
            log.info("[ftp流向对接]商业公司su_id={} 库存流向数据为空", flowFtpClientDO.getSuId());
            return;
        }

        LocalCompareDTO localCompareDTO = new LocalCompareDTO();
        localCompareDTO.setDataList(list);
        localCompareDTO.setSuId(flowFtpClientDO.getSuId());
        localCompareDTO.setTime(date);
        localCompare(localCompareDTO);

    }

    private void handleErpShopSaleFlowExcelList(List<BaseErpEntity> list, FlowFtpClientDO flowFtpClientDO) {
        if (CollUtil.isEmpty(list)) {
            log.info("[ftp流向对接]商业公司su_id={} 门店纯销数据为空", flowFtpClientDO.getSuId());
            return;
        }
        List<ErpShopSaleFlowDTO> erpShopSaleFlowList = PojoUtils.map(list, ErpShopSaleFlowDTO.class);
        shopSaleFlowSync(flowFtpClientDO.getSuId(), flowFtpClientDO.getShopSaleDay(), erpShopSaleFlowList);
    }


    private List<ErpPurchaseFlowDTO> covertToErpPurchaseFlowDTOList(List<ErpPurchaseFlowExcel> erpPurchaseFlowExcelList) throws Exception {
        List<ErpPurchaseFlowDTO> list = new ArrayList<>();
        for (ErpPurchaseFlowExcel erpPurchaseFlowExcel : erpPurchaseFlowExcelList) {
            ErpPurchaseFlowDTO erpPurchaseFlowDTO = new ErpPurchaseFlowDTO();
            erpPurchaseFlowDTO.setPoNo(erpPurchaseFlowExcel.getPoNo());
            erpPurchaseFlowDTO.setPoBatchNo(erpPurchaseFlowExcel.getPoBatchNo());
            if (StrUtil.isNotEmpty(erpPurchaseFlowExcel.getPoPrice())) {
                erpPurchaseFlowDTO.setPoPrice(new BigDecimal(erpPurchaseFlowExcel.getPoPrice()));
            }
            if (StrUtil.isNotEmpty(erpPurchaseFlowExcel.getPoQuantity())) {
                erpPurchaseFlowDTO.setPoQuantity(new BigDecimal(erpPurchaseFlowExcel.getPoQuantity()));
            }
            erpPurchaseFlowDTO.setEnterpriseInnerCode(erpPurchaseFlowExcel.getEnterpriseInnerCode());
            erpPurchaseFlowDTO.setEnterpriseName(erpPurchaseFlowExcel.getEnterpriseName());
            erpPurchaseFlowDTO.setGoodsInSn(erpPurchaseFlowExcel.getGoodsInSn());
            erpPurchaseFlowDTO.setGoodsName(erpPurchaseFlowExcel.getGoodsName());
            erpPurchaseFlowDTO.setPoLicense(erpPurchaseFlowExcel.getPoLicense());
            erpPurchaseFlowDTO.setPoManufacturer(erpPurchaseFlowExcel.getPoManufacturer());
            erpPurchaseFlowDTO.setPoSpecifications(erpPurchaseFlowExcel.getPoSpecifications());
            erpPurchaseFlowDTO.setPoUnit(erpPurchaseFlowExcel.getPoUnit());
            erpPurchaseFlowDTO.setSuDeptNo(erpPurchaseFlowExcel.getSuDeptNo());

            erpPurchaseFlowDTO.setPoTime(DateUtil.parse(ExcelDoubleToDate(erpPurchaseFlowExcel.getPoTime())));
            erpPurchaseFlowDTO.setOrderTime(DateUtil.parse(ExcelDoubleToDate(erpPurchaseFlowExcel.getOrderTime())));
            erpPurchaseFlowDTO.setPoEffectiveTime(DateUtil.parse(ExcelDoubleToDate(erpPurchaseFlowExcel.getPoEffectiveTime())));
            erpPurchaseFlowDTO.setPoProductTime(DateUtil.parse(ExcelDoubleToDate(erpPurchaseFlowExcel.getPoProductTime())));
            if (ObjectUtil.isNull(erpPurchaseFlowDTO.getPoTime())) {
                throw new BusinessException(ResultCode.FAILED, "采购流向数据采集，po_time转化后有为空");
            }

            list.add(erpPurchaseFlowDTO);
        }
        return list;
    }

    private List<ErpSaleFlowDTO> covertToErpSaleFlowDTOList(List<ErpSaleFlowExcel> erpSaleFlowExcelList) {
        List<ErpSaleFlowDTO> list = new ArrayList<>();
        for (ErpSaleFlowExcel erpSaleFlowExcel : erpSaleFlowExcelList) {
            ErpSaleFlowDTO erpSaleFlowDTO = new ErpSaleFlowDTO();
            erpSaleFlowDTO.setSoNo(erpSaleFlowExcel.getSoNo());
            erpSaleFlowDTO.setSoBatchNo(erpSaleFlowExcel.getSoBatchNo());
            if (StrUtil.isNotEmpty(erpSaleFlowExcel.getSoPrice())) {
                erpSaleFlowDTO.setSoPrice(new BigDecimal(erpSaleFlowExcel.getSoPrice()));
            }
            if (StrUtil.isNotEmpty(erpSaleFlowExcel.getSoQuantity())) {
                erpSaleFlowDTO.setSoQuantity(new BigDecimal(erpSaleFlowExcel.getSoQuantity()));
            }
            erpSaleFlowDTO.setEnterpriseInnerCode(erpSaleFlowExcel.getEnterpriseInnerCode());
            erpSaleFlowDTO.setEnterpriseName(erpSaleFlowExcel.getEnterpriseName());
            erpSaleFlowDTO.setGoodsInSn(erpSaleFlowExcel.getGoodsInSn());
            erpSaleFlowDTO.setGoodsName(erpSaleFlowExcel.getGoodsName());
            erpSaleFlowDTO.setSoLicense(erpSaleFlowExcel.getSoLicense());
            erpSaleFlowDTO.setSoManufacturer(erpSaleFlowExcel.getSoManufacturer());
            erpSaleFlowDTO.setSoSpecifications(erpSaleFlowExcel.getSoSpecifications());
            erpSaleFlowDTO.setSoUnit(erpSaleFlowExcel.getSoUnit());
            erpSaleFlowDTO.setSuDeptNo(erpSaleFlowExcel.getSuDeptNo());
            erpSaleFlowDTO.setSoSource(erpSaleFlowExcel.getSoSource());
            erpSaleFlowDTO.setSoTime(DateUtil.parse(ExcelDoubleToDate(erpSaleFlowExcel.getSoTime())));
            erpSaleFlowDTO.setOrderTime(DateUtil.parse(ExcelDoubleToDate(erpSaleFlowExcel.getOrderTime())));
            erpSaleFlowDTO.setSoEffectiveTime(DateUtil.parse(ExcelDoubleToDate(erpSaleFlowExcel.getSoEffectiveTime())));
            erpSaleFlowDTO.setSoProductTime(DateUtil.parse(ExcelDoubleToDate(erpSaleFlowExcel.getSoProductTime())));
            erpSaleFlowDTO.setLicenseNumber(erpSaleFlowExcel.getLicenseNumber());

            if (ObjectUtil.isNull(erpSaleFlowDTO.getSoTime())) {
                throw new BusinessException(ResultCode.FAILED, "销售流向数据采集，so_time转化后有为空");
            }
            list.add(erpSaleFlowDTO);
        }
        return list;
    }

    private List<ErpGoodsBatchFlowDTO> covertToErpGoodsBatchFlowDTOList(List<ErpGoodsBatchFlowExcel> erpGoodsBatchFlowExcelList, FlowFtpClientDO flowFtpClientDO) {
        List<ErpGoodsBatchFlowDTO> list = new ArrayList<>();
        for (ErpGoodsBatchFlowExcel erpGoodsBatchFlowExcel : erpGoodsBatchFlowExcelList) {
            ErpGoodsBatchFlowDTO erpGoodsBatchFlowDTO = new ErpGoodsBatchFlowDTO();
            erpGoodsBatchFlowDTO.setGbTime(DateUtil.parse(ExcelDoubleToDate(erpGoodsBatchFlowExcel.getGbTime())));
            erpGoodsBatchFlowDTO.setGbManufacturer(erpGoodsBatchFlowExcel.getGbManufacturer());
            erpGoodsBatchFlowDTO.setGbUnit(erpGoodsBatchFlowExcel.getGbUnit());
            erpGoodsBatchFlowDTO.setGbSpecifications(erpGoodsBatchFlowExcel.getGbSpecifications());
            erpGoodsBatchFlowDTO.setGbLicense(erpGoodsBatchFlowExcel.getGbLicense());
            erpGoodsBatchFlowDTO.setGbName(erpGoodsBatchFlowExcel.getGbName());
            if (StrUtil.isNotEmpty(erpGoodsBatchFlowExcel.getGbNumber())) {
                erpGoodsBatchFlowDTO.setGbNumber(new BigDecimal(erpGoodsBatchFlowExcel.getGbNumber()));
            }
            erpGoodsBatchFlowDTO.setGbProduceAddress(erpGoodsBatchFlowExcel.getGbProduceAddress());
            erpGoodsBatchFlowDTO.setGbEndTime(ExcelDoubleToDate(erpGoodsBatchFlowExcel.getGbEndTime()));
            erpGoodsBatchFlowDTO.setGbBatchNo(erpGoodsBatchFlowExcel.getGbBatchNo());
            erpGoodsBatchFlowDTO.setInSn(erpGoodsBatchFlowExcel.getInSn());
            erpGoodsBatchFlowDTO.setGbProduceTime(ExcelDoubleToDate(erpGoodsBatchFlowExcel.getGbProduceTime()));
            erpGoodsBatchFlowDTO.setGbIdNo(IdUtil.fastSimpleUUID());
            erpGoodsBatchFlowDTO.setSuDeptNo(erpGoodsBatchFlowExcel.getSuDeptNo());
            erpGoodsBatchFlowDTO.setSuId(flowFtpClientDO.getSuId());
            erpGoodsBatchFlowDTO.setOperType(1);
            list.add(erpGoodsBatchFlowDTO);
        }
        return list;
    }

    private List<ErpShopSaleFlowDTO> covertToErpShopSaleFlowDTOList(List<ErpShopSaleFlowExcel> erpShopSaleFlowExcelList) {
        List<ErpShopSaleFlowDTO> list = new ArrayList<>();
        for (ErpShopSaleFlowExcel erpShopSaleFlowExcel : erpShopSaleFlowExcelList) {
            ErpShopSaleFlowDTO erpShopSaleFlowDTO = new ErpShopSaleFlowDTO();
            erpShopSaleFlowDTO.setSoNo(erpShopSaleFlowExcel.getSoNo());
            erpShopSaleFlowDTO.setShopNo(erpShopSaleFlowExcel.getShopNo());
            erpShopSaleFlowDTO.setShopName(erpShopSaleFlowExcel.getShopName());
            erpShopSaleFlowDTO.setSuDeptNo(erpShopSaleFlowExcel.getSuDeptNo());
            erpShopSaleFlowDTO.setSoTime(DateUtil.parse(ExcelDoubleToDate(erpShopSaleFlowExcel.getSoTime())));
            erpShopSaleFlowDTO.setEnterpriseName(erpShopSaleFlowExcel.getEnterpriseName());
            erpShopSaleFlowDTO.setSoBatchNo(erpShopSaleFlowExcel.getSoBatchNo());
            erpShopSaleFlowDTO.setSoProductTime(DateUtil.parse(ExcelDoubleToDate(erpShopSaleFlowExcel.getSoProductTime())));
            erpShopSaleFlowDTO.setSoEffectiveTime(DateUtil.parse(ExcelDoubleToDate(erpShopSaleFlowExcel.getSoEffectiveTime())));
            if (StrUtil.isNotEmpty(erpShopSaleFlowExcel.getSoPrice())) {
                erpShopSaleFlowDTO.setSoPrice(new BigDecimal(erpShopSaleFlowExcel.getSoPrice()));
            }
            if (StrUtil.isNotEmpty(erpShopSaleFlowExcel.getSoQuantity())) {
                erpShopSaleFlowDTO.setSoQuantity(new BigDecimal(erpShopSaleFlowExcel.getSoQuantity()));
            }
            erpShopSaleFlowDTO.setGoodsInSn(erpShopSaleFlowExcel.getGoodsInSn());
            erpShopSaleFlowDTO.setGoodsName(erpShopSaleFlowExcel.getGoodsName());
            erpShopSaleFlowDTO.setSoLicense(erpShopSaleFlowExcel.getSoLicense());
            erpShopSaleFlowDTO.setSoSpecifications(erpShopSaleFlowExcel.getSoSpecifications());
            erpShopSaleFlowDTO.setSoUnit(erpShopSaleFlowExcel.getSoUnit());
            erpShopSaleFlowDTO.setSoManufacturer(erpShopSaleFlowExcel.getSoManufacturer());

            if (ObjectUtil.isNull(erpShopSaleFlowDTO.getSoTime())) {
                throw new BusinessException(ResultCode.FAILED, "纯销流向数据采集，so_time转化后有为空");
            }
            list.add(erpShopSaleFlowDTO);
        }

        return list;
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
