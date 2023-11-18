package com.yiling.erp.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.yiling.erp.client.common.Constants;
import com.yiling.erp.client.dao.SQLiteHelper;
import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.ErpErrorEntity;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.dto.ErpGoodsCustomerPriceDTO;
import com.yiling.open.erp.dto.ErpGoodsDTO;
import com.yiling.open.erp.dto.ErpGoodsGroupPriceDTO;
import com.yiling.open.erp.dto.ErpOrderSendDTO;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.util.OpenConstants;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 基础数据抽取功能开发 对比，发送，重试
 *
 * @author shuan
 */
@Slf4j
@Service
public class InitErpSysData {


    @Autowired
    private SQLiteHelper sqliteHelper;
    @Autowired
    private TaskConfigDao taskConfigDao;
    @Autowired
    private InitErpConfig initErpConfig;

    //1.初始化
    public void initDataBase(String methodName) throws Exception {
        boolean boolSys = sqliteHelper.isExistTable(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, methodName);
        if (!boolSys) {
            String insertSysSql = "CREATE TABLE \"" + methodName + "\" (\n" + "\"key\"  TEXT(64) NOT NULL,\n" + "\"md5\"  TEXT(64) NOT NULL\n" + ");";
            sqliteHelper.executeUpdate(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, insertSysSql);
        }
    }

    //2.查询sqlite原有数据
    public Map<String, String> findSqliteData(String methodName) throws Exception {
        return taskConfigDao.findMapAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, methodName);
    }

    //3.分页查询数据
    public List<BaseErpEntity> findDataAll(String methodNo) throws Exception {
        List<TaskConfig> taskConfigList = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config where taskNo=" + methodNo);
        if (CollectionUtils.isEmpty(taskConfigList)) {
            return Collections.emptyList();
        }
        TaskConfig taskConfig = taskConfigList.get(0);
        return findDataByMethodNoAndSql(methodNo, taskConfig.getTaskSQL(), StrUtil.isNotEmpty(taskConfig.getFlowDateCount()) ? Integer.parseInt(taskConfig.getFlowDateCount()) : 0);
    }

    public List<BaseErpEntity> findDataByMethodNoAndSql(String methodNo, String sql, Integer flowDateCount) throws Exception {
        SysConfig sysConfig = initErpConfig.getSysConfig();
        String dataSql = SqlPaginationUtil.getSearchSql(sql, sysConfig.getDbType());

        // 销售、采购流向数据查询sql
        String flowDataSql = "";
        if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpSaleFlow) || ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpPurchaseFlow)) {
            if (flowDateCount != 0) {
                // 查询开始日期，当天
                Date date = new Date();
                flowDataSql = SqlPaginationUtil.getFlowSearchSql(sql, sysConfig.getDbType(), methodNo, flowDateCount, date);
            }
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpShopSaleFlow)) {
            // 连锁门店销售流向数据查询sql
            if (flowDateCount != 0) {
                // 查询开始日期，当天
//                DateTime yesterday = DateUtil.yesterday();
                Date date = new Date();
                flowDataSql = SqlPaginationUtil.getFlowSearchSql(sql, sysConfig.getDbType(), methodNo, flowDateCount, date);
            }
        }

        List<BaseErpEntity> list = null;
        if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpGoods)) {
            list = DataConvertUtil.getGoodsList(sysConfig, dataSql);
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpGoodsBatch)) {
            list = DataConvertUtil.getGoodsBatchList(sysConfig, dataSql);
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpOrderSend)) {
            list = DataConvertUtil.getOrderSendList(sysConfig, dataSql);
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpCustomer)) {
            list = DataConvertUtil.getErpCustomerList(sysConfig, dataSql);
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpPurchaseFlow)) {
            list = DataConvertUtil.getErpPurchaseFlowList(sysConfig, flowDataSql);
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpSaleFlow)) {
            list = DataConvertUtil.getErpSaleFlowList(sysConfig, flowDataSql);
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpGoodsBatchFlow)) {
            list = DataConvertUtil.getErpGoodsBatchFlowList(sysConfig, dataSql);
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpGoodsPrice)) {
            list = DataConvertUtil.getErpGoodsCustomerPriceList(sysConfig, dataSql);
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpGroupPrice)) {
            list = DataConvertUtil.getErpGoodsGroupPriceList(sysConfig, dataSql);
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpOrderPurchaseDelivery)) {
            list = DataConvertUtil.getErpOrderPurchaseDeliveryList(sysConfig, dataSql);
        } else if (ErpTopicName.getFromCode(methodNo).equals(ErpTopicName.ErpShopSaleFlow)) {
            list = DataConvertUtil.getErpShopSaleFlowList(sysConfig, flowDataSql);
        }
        if (!CollectionUtils.isEmpty(list)) {
            log.debug(methodNo + "执行条数" + list.size());
        }
        return list;
    }

    private Map<String, String> getFlowQuergDate(String methodNo, String sql, SysConfig sysConfig, TaskConfig taskConfig) {
        // 流向数据默认查询范围15天
        Map<String, String> map = new HashMap<>();
        int dateOffset = 15;
        String startDate = "";
        String endDate = "";
        if (ObjectUtil.equal(ErpTopicName.ErpPurchaseFlow.getMethod(), methodNo) || ObjectUtil.equal(ErpTopicName.ErpSaleFlow.getMethod(), methodNo)) {
            if (ObjectUtil.isNotNull(taskConfig) && StringUtils.isNotBlank(taskConfig.getFlowDateCount())) {
                dateOffset = Integer.parseInt(taskConfig.getFlowDateCount());
            }
            Date date = new Date();
            endDate = DateUtil.format(date, Constants.FORMATE_DAY_TIME);
            Date startDateTemp = DateUtil.offset(date, DateField.DAY_OF_MONTH, dateOffset);
            startDate = DateUtil.format(startDateTemp, Constants.FORMATE_DAY_TIME);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
        }
        return map;
    }

    //4.判断查询出来的结果是否有重复是否然后转成Map
    public Map<String, BaseErpEntity> isRepeat(List<BaseErpEntity> baseErpEntityList) {
        Map<String, BaseErpEntity> map = new HashMap<>();
        for (BaseErpEntity baseErpEntity : baseErpEntityList) {
            if (!map.containsKey(baseErpEntity.getErpPrimaryKey())) {
                map.put(baseErpEntity.getErpPrimaryKey(), baseErpEntity);
            } else {
                log.error("数据有重复主键{}", JSON.toJSONString(baseErpEntity));
            }
        }
        if (map.size() == baseErpEntityList.size()) {
            return map;
        }
        return null;
    }

    /**
     * 5.判断是增删改的数据
     *
     * @param sqliteData 客户信息同步数据（本地缓存）
     * @param data ERP系统客户信息数据
     * @return
     */
    public Map<Integer, Set<String>> comparisonData(Map<String, String> sqliteData, Map<String, BaseErpEntity> data) {
        Map<Integer, Set<String>> handlerMap = new HashMap<>();
        // 删除数据
        Set<String> sqliteKey = sqliteData.keySet();
        Set<String> key = data.keySet();
        Set<String> deleteSet = Sets.difference(sqliteKey, key);
        handlerMap.put(OperTypeEnum.DELETE.getCode(), deleteSet);
        // 增加数据
        Set<String> insertSet = Sets.difference(key, sqliteKey);
        handlerMap.put(OperTypeEnum.ADD.getCode(), insertSet);
        // 修改数据
        Set<String> updateSet = new HashSet<>();

        Set<String> andSet = Sets.intersection(key, sqliteKey);
        for (String handleKey : andSet) {
            BaseErpEntity baseErpEntity = data.get(handleKey);
            if (!(sqliteData.get(handleKey).equals(baseErpEntity.sign()))) {
                updateSet.add(handleKey);
            }
        }
        handlerMap.put(OperTypeEnum.UPDATE.getCode(), updateSet);
        return handlerMap;
    }

    /**
     * 7.发送数据
     *
     * @param list 变更数据
     * @param sysConfig 同步配置
     * @param erpTopicName 发送消息topic
     * @param operType 数据变更标识：1-添加，2-修改，3-删除
     * @return
     */
    public List<BaseErpEntity> sendData(List<BaseErpEntity> list, SysConfig sysConfig, ErpTopicName erpTopicName, Integer operType) {
        int sendSize = Integer.valueOf(ReadProperties.getProperties("sendSize")).intValue();
        if (OperTypeEnum.ALL.getCode().equals(operType)) {
            sendSize = list.size();
        }
        log.debug("[" + erpTopicName.getTopicNameDesc() + "]【" + OperTypeEnum.getFromCode(operType).getText() + "】数据发送到服务端总条数为:" + list.size() + "条, 每次发送条数为:" + sendSize + "条, 分" + (list.size() / sendSize + 1) + "次发送完毕");
        dealBySubList(list, sendSize, sysConfig, erpTopicName, operType);
        return null;
    }

    public void sendDataToServer(List<BaseErpEntity> sourList, SysConfig sysConfig, ErpTopicName erpTopicName, Integer operType, Integer j) {
        PopRequest pr = new PopRequest();
        //先发送删除数据
        Map<String, String> headParams = pr.generateHeadMap(erpTopicName.getMethod(), sysConfig.getKey());
        for (int i = 0; i < 3; i++) {
            try {
                String returnJson = pr.getPost(sysConfig.getUrlPath(), JSON.toJSONString(sourList), headParams, sysConfig.getSecret());
                log.debug(erpTopicName.getTopicName() + "返回数据为:returnJson=" + returnJson);
                if ((returnJson != null) && (!returnJson.equals(""))) {
                    Result dtoResponse = JSONObject.parseObject(returnJson, Result.class);
                    Integer code = dtoResponse.getCode();
                    if (200 == code) {
                        List<ErpErrorEntity> errorList = JSON.parseArray((String) dtoResponse.getData(), ErpErrorEntity.class);
                        if (!CollectionUtils.isEmpty(errorList)) {
                            Map<String, BaseErpEntity> baseErpEntityMap = new HashMap<>();
                            for (BaseErpEntity baseErpEntity : sourList) {
                                baseErpEntityMap.put(baseErpEntity.getErpPrimaryKey(), baseErpEntity);
                            }

                            for (ErpErrorEntity erpErrorEntity : errorList) {
                                sourList.remove(baseErpEntityMap.get(erpErrorEntity.getErpPrimaryKey()));
                            }
                        }
                        //处理sqlite数据
                        if (operType.equals(OperTypeEnum.ALL.getCode())) {
                            taskConfigDao.sqliteDataAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, erpTopicName.getTopicName(), sourList);
                        } else {
                            if (operType.equals(OperTypeEnum.ADD.getCode())) {
                                taskConfigDao.addSqliteDataAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, erpTopicName.getTopicName(), sourList);
                            } else if (operType.equals(OperTypeEnum.DELETE.getCode())) {
                                taskConfigDao.deleteSqliteDataAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, erpTopicName.getTopicName(), sourList);
                            } else {
                                taskConfigDao.updateSqliteDataAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, erpTopicName.getTopicName(), sourList);
                            }
                        }
                        Integer errorSize = 0;
                        if (CollUtil.isNotEmpty(errorList)) {
                            errorSize = errorList.size();
                        }
                        Integer succee = sourList.size() - errorSize;
                        log.debug("[" + erpTopicName.getTopicNameDesc() + "]【" + OperTypeEnum.getFromCode(operType).getText() + "重试次数为" + i + "," + "发送批次数为" + j + "的数据返回成功。其中成功" + succee + "条，失败" + errorSize + "条");
                        break;
                    } else {
                        log.error("[" + erpTopicName.getTopicNameDesc() + "]【" + OperTypeEnum.getFromCode(operType).getText() + "重试次数为" + i + "," + "发送批次数为" + j + "的数据返回失败，结果为returnJson=" + returnJson);
                    }
                } else {
                    log.error("[" + erpTopicName.getTopicNameDesc() + "]【" + OperTypeEnum.getFromCode(operType).getText() + "重试次数为" + i + "," + "发送批次数为" + j + "的数据返回结果为空。请立即处理！！！");
                }
            } catch (Exception e) {
                log.error("发送请求数据报错sendList=" + JSON.toJSONString(sourList), e);
            }
        }
    }

    /**
     * 通过list的     subList(int fromIndex, int toIndex)方法实现
     *
     * @param sourList 源list
     * @param batchCount 分组条数
     */
    public void dealBySubList(List<BaseErpEntity> sourList, int batchCount, SysConfig sysConfig, ErpTopicName erpTopicName, Integer operType) {
        int sourListSize = sourList.size();
        int subCount = sourListSize % batchCount == 0 ? sourListSize / batchCount : sourListSize / batchCount + 1;
        int startIndext = 0;
        int stopIndext = 0;
        for (int i = 0; i < subCount; i++) {
            if (i == subCount - 1) {
                if (sourListSize % batchCount == 0) {
                    stopIndext = sourListSize;
                } else {
                    stopIndext = stopIndext + sourListSize % batchCount;
                }
            } else {
                stopIndext = stopIndext + batchCount;
            }
            List<BaseErpEntity> tempList = new ArrayList<>(sourList.subList(startIndext, stopIndext));
            sendDataToServer(tempList, sysConfig, erpTopicName, operType, i + 1);
            startIndext = stopIndext;
        }
    }

    public void eleteCache(SysConfig sysConfig, ErpTopicName erpTopicName) throws Exception {
        List<BaseErpEntity> deleteList = new ArrayList<>();
        Map<String, String> sqliteMap = findSqliteData(erpTopicName.getTopicName());
        for (Map.Entry<String, String> entry : sqliteMap.entrySet()) {
            if (erpTopicName.equals(ErpTopicName.ErpCustomer)) {
                ErpCustomerDTO erpCustomers = new ErpCustomerDTO();
                // 先判断key是否包含$$$
                if (entry.getKey().contains(OpenConstants.SPLITE_SYMBOL)) {
                    String[] keys = entry.getKey().split(OpenConstants.SPLITE_SYMBOL_FALG);
                    erpCustomers.setInnerCode(keys[0]);
                    erpCustomers.setSuDeptNo(keys[1]);
                } else {
                    erpCustomers.setInnerCode(entry.getKey());
                    erpCustomers.setSuDeptNo("");
                }
                erpCustomers.setOperType(OperTypeEnum.DELETE.getCode());
                deleteList.add(erpCustomers);
            } else if (erpTopicName.equals(ErpTopicName.ErpGoodsBatch)) {
                ErpGoodsBatchDTO erpGoodsBatch = new ErpGoodsBatchDTO();
                //先判断key是否包含$$$
                if (entry.getKey().contains(OpenConstants.SPLITE_SYMBOL)) {
                    String[] keys = entry.getKey().split(OpenConstants.SPLITE_SYMBOL_FALG);
                    erpGoodsBatch.setGbIdNo(keys[0]);
                    erpGoodsBatch.setSuDeptNo(keys[1]);
                } else {
                    erpGoodsBatch.setGbIdNo(entry.getKey());
                    erpGoodsBatch.setSuDeptNo("");
                }
                erpGoodsBatch.setOperType(OperTypeEnum.DELETE.getCode());
                deleteList.add(erpGoodsBatch);
            } else if (erpTopicName.equals(ErpTopicName.ErpGoodsBatchFlow)) {
                ErpGoodsBatchFlowDTO erpGoodsBatchFlow = new ErpGoodsBatchFlowDTO();
                //先判断key是否包含$$$
                if (entry.getKey().contains(OpenConstants.SPLITE_SYMBOL)) {
                    String[] keys = entry.getKey().split(OpenConstants.SPLITE_SYMBOL_FALG);
                    erpGoodsBatchFlow.setGbIdNo(keys[0]);
                    erpGoodsBatchFlow.setSuDeptNo(keys[1]);
                } else {
                    erpGoodsBatchFlow.setGbIdNo(entry.getKey());
                    erpGoodsBatchFlow.setSuDeptNo("");
                }
                erpGoodsBatchFlow.setOperType(OperTypeEnum.DELETE.getCode());
                deleteList.add(erpGoodsBatchFlow);
            } else if (erpTopicName.equals(ErpTopicName.ErpGoodsPrice)) {
                ErpGoodsCustomerPriceDTO erpGoods = new ErpGoodsCustomerPriceDTO();
                //先判断key是否包含$$$
                if (entry.getKey().contains(OpenConstants.SPLITE_SYMBOL)) {
                    String[] keys = entry.getKey().split(OpenConstants.SPLITE_SYMBOL_FALG);
                    erpGoods.setGcpIdNo(keys[0]);
                    erpGoods.setSuDeptNo(keys[1]);
                } else {
                    erpGoods.setInSn(entry.getKey());
                    erpGoods.setSuDeptNo("");
                }
                erpGoods.setOperType(OperTypeEnum.DELETE.getCode());
                deleteList.add(erpGoods);
            } else if (erpTopicName.equals(ErpTopicName.ErpGroupPrice)) {
                ErpGoodsGroupPriceDTO erpGoodsGroupPriceDTO=new ErpGoodsGroupPriceDTO();
                //先判断key是否包含$$$
                if (entry.getKey().contains(OpenConstants.SPLITE_SYMBOL)) {
                    String[] keys = entry.getKey().split(OpenConstants.SPLITE_SYMBOL_FALG);
                    erpGoodsGroupPriceDTO.setGgpIdNo(keys[0]);
                    erpGoodsGroupPriceDTO.setSuDeptNo(keys[1]);
                } else {
                    erpGoodsGroupPriceDTO.setGgpIdNo(entry.getKey());
                    erpGoodsGroupPriceDTO.setSuDeptNo("");
                }
                erpGoodsGroupPriceDTO.setOperType(OperTypeEnum.DELETE.getCode());
                deleteList.add(erpGoodsGroupPriceDTO);
            }else if (erpTopicName.equals(ErpTopicName.ErpGoods)) {
                ErpGoodsDTO erpGoods = new ErpGoodsDTO();
                //先判断key是否包含$$$
                if (entry.getKey().contains(OpenConstants.SPLITE_SYMBOL)) {
                    String[] keys = entry.getKey().split(OpenConstants.SPLITE_SYMBOL_FALG);
                    erpGoods.setInSn(keys[0]);
                    erpGoods.setSuDeptNo(keys[1]);
                } else {
                    erpGoods.setInSn(entry.getKey());
                    erpGoods.setSuDeptNo("");
                }
                erpGoods.setOperType(OperTypeEnum.DELETE.getCode());
                deleteList.add(erpGoods);
            }else if (erpTopicName.equals(ErpTopicName.ErpOrderSend)) {
                ErpOrderSendDTO erpOrderSend = new ErpOrderSendDTO();
                //先判断key是否包含$$$
                if (entry.getKey().contains(OpenConstants.SPLITE_SYMBOL)) {
                    String[] keys = entry.getKey().split(OpenConstants.SPLITE_SYMBOL_FALG);
                    erpOrderSend.setOsiId(keys[0]);
                    erpOrderSend.setSuDeptNo(keys[1]);
                } else {
                    erpOrderSend.setOsiId(entry.getKey());
                    erpOrderSend.setSuDeptNo("");
                }
                erpOrderSend.setOperType(OperTypeEnum.DELETE.getCode());
                deleteList.add(erpOrderSend);
            }
        }
        if (!org.springframework.util.CollectionUtils.isEmpty(deleteList)) {
            this.sendData(deleteList, sysConfig, erpTopicName, OperTypeEnum.DELETE.getCode());
        }
    }

    public static void main(String[] args) {
        List<String> sourList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            sourList.add(i + "");
        }
        int sourListSize = sourList.size();
        int batchCount = sourList.size();
        int subCount = sourListSize % batchCount == 0 ? sourListSize / batchCount : sourListSize / batchCount + 1;
        int startIndext = 0;
        int stopIndext = 0;
        for (int i = 0; i < subCount; i++) {
            if (i == subCount - 1) {
                if (sourListSize % batchCount == 0) {
                    stopIndext = sourListSize;
                } else {
                    stopIndext = stopIndext + sourListSize % batchCount;
                }
            } else {
                stopIndext = stopIndext + batchCount;
            }
            List<String> tempList = new ArrayList<>(sourList.subList(startIndext, stopIndext));
            System.out.println(tempList);
            startIndext = stopIndext;
        }
    }
}
