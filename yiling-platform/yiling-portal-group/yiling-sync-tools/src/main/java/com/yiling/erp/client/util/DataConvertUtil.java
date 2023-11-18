package com.yiling.erp.client.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.yiling.erp.client.common.Constants;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.dto.ErpGoodsCustomerPriceDTO;
import com.yiling.open.erp.dto.ErpGoodsDTO;
import com.yiling.open.erp.dto.ErpGoodsGroupPriceDTO;
import com.yiling.open.erp.dto.ErpOrderPurchaseDeliveryDTO;
import com.yiling.open.erp.dto.ErpOrderSendDTO;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.dto.ErpSaleFlowDTO;
import com.yiling.open.erp.dto.ErpShopSaleFlowDTO;
import com.yiling.open.erp.dto.SysConfig;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author shuan
 */
public class DataConvertUtil {
    private static final Log logger = LogFactory.getLog(DataConvertUtil.class);

    public static List<BaseErpEntity> getGoodsList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpGoodsDTO erpGoods = new ErpGoodsDTO();
                        String su_dept_no = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_dept_no"));
                        String inSn = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"in_sn"));
                        String sn = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"sn"));
                        String name = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"name"));
                        String commonName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"common_name"));
                        String aliasName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"alias_name"));
                        String licenseNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"license_no"));
                        String specifications = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"specifications"));
                        String unit = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"unit"));
                        Integer middlePackage = CommonConstants.getInteger(Utils.getObjectIgnoreCase(map,"middle_package"));
                        Integer bigPackage = CommonConstants.getInteger(Utils.getObjectIgnoreCase(map,"big_package"));
                        String manufacturer = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"manufacturer"));
                        String manufacturerCode = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"manufacturer_code"));
                        BigDecimal price = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"price"));
                        Integer canSplit = CommonConstants.getInteger(Utils.getObjectIgnoreCase(map,"can_split"));
                        Integer goodsStatus = CommonConstants.getInteger(Utils.getObjectIgnoreCase(map,"goods_status"));
                        erpGoods.setSuDeptNo(su_dept_no);
                        erpGoods.setPrice(price);
                        erpGoods.setSn(RkStringUtil.trim(sn));
                        erpGoods.setAliasName(RkStringUtil.trim(aliasName));
                        erpGoods.setUnit(RkStringUtil.trim(unit));
                        erpGoods.setSpecifications(RkStringUtil.trim(specifications));
                        erpGoods.setName(RkStringUtil.trim(name));
                        erpGoods.setMiddlePackage(middlePackage);
                        erpGoods.setManufacturerCode(RkStringUtil.trim(manufacturerCode));
                        erpGoods.setManufacturer(RkStringUtil.trim(manufacturer));
                        erpGoods.setLicenseNo(RkStringUtil.trim(licenseNo));
                        erpGoods.setInSn(RkStringUtil.trim(inSn));
                        erpGoods.setCommonName(RkStringUtil.trim(commonName));
                        erpGoods.setBigPackage(bigPackage);
                        erpGoods.setCanSplit(canSplit);
                        erpGoods.setGoodsStatus(goodsStatus);
                        //                        if (latestValid != null) {
                        //                            String latestValidStr = CommonConstants.getDate(latestValid, "yyyy-MM-dd");
                        //                            vo.setLatestValid(latestValidStr);
                        //                        }
                        //                        if (farVildDate != null) {
                        //                            String farValidStr = CommonConstants.getDate(farVildDate, "yyyy-MM-dd");
                        //                            vo.setFarValid(farValidStr);
                        //                        }

                        list.add(erpGoods);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }

    public static List<BaseErpEntity> getGoodsBatchList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpGoodsBatchDTO erpGoodsBatch = new ErpGoodsBatchDTO();
                        String su_dept_no = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_dept_no"));
                        String inSn = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"in_sn"));
                        BigDecimal gb_number = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"gb_number"));
                        String gb_id_no = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_id_no"));
                        String gb_batch_no = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_batch_no"));
                        String gb_produce_time = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"gb_produce_time"), "yyyy-MM-dd HH:mm:ss");
                        String gb_end_time = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"gb_end_time"), "yyyy-MM-dd HH:mm:ss");
                        String gb_produce_address = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_produce_address"));
                        erpGoodsBatch.setGbIdNo(RkStringUtil.trim(gb_id_no));
                        erpGoodsBatch.setSuDeptNo(su_dept_no);
                        erpGoodsBatch.setGbBatchNo(RkStringUtil.trim(gb_batch_no));
                        erpGoodsBatch.setGbEndTime(gb_end_time != null ? DateUtil.convertString2Date(gb_end_time, "yyyy-MM-dd HH:mm:ss") : null);
                        erpGoodsBatch
                            .setGbProduceTime(gb_produce_time != null ? DateUtil.convertString2Date(gb_produce_time, "yyyy-MM-dd HH:mm:ss") : null);
                        erpGoodsBatch.setGbProduceAddress(RkStringUtil.trim(gb_produce_address));
                        erpGoodsBatch.setGbNumber(gb_number);
                        erpGoodsBatch.setInSn(RkStringUtil.trim(inSn));
                        list.add(erpGoodsBatch);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }

    public static List<BaseErpEntity> getOrderSendList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpOrderSendDTO erpOrderSend = new ErpOrderSendDTO();
                        String su_dept_no = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_dept_no"));
                        String osi_id = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"osi_id"));
                        BigDecimal send_num = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"send_num"));
                        Long order_id = CommonConstants.getLong(Utils.getObjectIgnoreCase(map,"order_id"));
                        Long order_detail_id = CommonConstants.getLong(Utils.getObjectIgnoreCase(map,"order_detail_id"));
                        String send_batch_no = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"send_batch_no"));
                        String delivery_number = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"delivery_number"));
                        String eas_send_order_id = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"eas_send_order_id"));
                        String effective_time = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"effective_time"), "yyyy-MM-dd HH:mm:ss");
                        String product_time = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"product_time"), "yyyy-MM-dd HH:mm:ss");
                        Integer sendType = CommonConstants.getInteger(Utils.getObjectIgnoreCase(map,"send_type"));
                        String send_time = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"send_time"), "yyyy-MM-dd HH:mm:ss");
                        erpOrderSend.setOsiId(RkStringUtil.trim(osi_id));
                        erpOrderSend.setOrderId(order_id);
                        erpOrderSend.setSuDeptNo(su_dept_no);
                        erpOrderSend.setOrderDetailId(order_detail_id);
                        erpOrderSend.setEffectiveTime(effective_time != null ? DateUtil.convertString2Date(effective_time, "yyyy-MM-dd HH:mm:ss") : null);
                        erpOrderSend.setProductTime(product_time != null ? DateUtil.convertString2Date(product_time, "yyyy-MM-dd HH:mm:ss") : null);
                        erpOrderSend.setSendBatchNo(RkStringUtil.trim(send_batch_no));
                        erpOrderSend.setDeliveryNumber(RkStringUtil.trim(delivery_number));
                        erpOrderSend.setEasSendOrderId(RkStringUtil.trim(eas_send_order_id));
                        erpOrderSend.setSendNum(send_num);
                        erpOrderSend.setSendType(sendType);
                        erpOrderSend.setSendTime(send_time != null ? DateUtil.convertString2Date(send_time, "yyyy-MM-dd HH:mm:ss") : null);
                        list.add(erpOrderSend);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }

    public static List<BaseErpEntity> getErpCustomerList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpCustomerDTO erpCustomer = new ErpCustomerDTO();
                        String su_dept_no = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "su_dept_no"));
                        String innerCode = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "inner_code"));
                        String sn = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "sn"));
                        String name = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "name"));
                        String groupName = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "group_name"));
                        String licenseNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "license_no"));
                        String customerType = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "customer_type"));
                        String contact = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "contact"));
                        String phone = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "phone"));
                        String province = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "province"));
                        String city = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "city"));
                        String region = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "region"));
                        String address = CommonConstants.getString(Utils.getObjectIgnoreCase(map, "address"));
                        erpCustomer.setAddress(RkStringUtil.trim(address));
                        erpCustomer.setCity(RkStringUtil.trim(city));
                        erpCustomer.setContact(RkStringUtil.trim(contact));
                        erpCustomer.setCustomerType(RkStringUtil.trim(customerType));
                        erpCustomer.setGroupName(RkStringUtil.trim(groupName));
                        erpCustomer.setInnerCode(RkStringUtil.trim(innerCode));
                        erpCustomer.setSn(RkStringUtil.trim(sn));
                        erpCustomer.setRegion(RkStringUtil.trim(region));
                        erpCustomer.setPhone(RkStringUtil.trim(phone));
                        erpCustomer.setProvince(RkStringUtil.trim(province));
                        erpCustomer.setPhone(RkStringUtil.trim(phone));
                        erpCustomer.setName(RkStringUtil.trim(name));
                        erpCustomer.setLicenseNo(RkStringUtil.trim(licenseNo));
                        erpCustomer.setSuDeptNo(RkStringUtil.trim(su_dept_no));
                        list.add(erpCustomer);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        Map<Object, Object> map = new HashMap<>();
//        map.put("po_quantity", 1);
//        map.put("po_price", 2);
        List<Map<Object, Object>> result = new ArrayList<>();
        result.add(map);

        try {
            for (Map ma : result) {
                BigDecimal poQuantity = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(ma,"po_quantity"));
                BigDecimal poPrice = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(ma,"po_price"));

                System.out.println(">>>>> poQuantity：" + poQuantity);
                System.out.println(">>>>> poPrice：" + poPrice);
            }
        } catch (Exception e) {
            System.out.println(">>>>> 异常：" + e);
        }
    }

    public static List<BaseErpEntity> getErpPurchaseFlowList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            logger.debug("数据成功返回：" + JSON.toJSONString(result));
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpPurchaseFlowDTO erpPurchaseFlow = new ErpPurchaseFlowDTO();
                        String suId = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_id"));
                        String suDeptNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_dept_no"));
                        String poId = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"po_id"));
                        String poNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"po_no"));
                        String poTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"po_time"), Constants.FORMATE_DAY_TIME);
                        String orderTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"order_time"), Constants.FORMATE_DAY);
                        String enterpriseInnerCode = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"enterprise_inner_code"));
                        String enterpriseName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"enterprise_name"));
                        String poBatchNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"po_batch_no"));
                        BigDecimal poQuantity = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"po_quantity"));
                        String poProductTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"po_product_time"), Constants.FORMATE_DAY_TIME);
                        String poEffectiveTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"po_effective_time"), Constants.FORMATE_DAY_TIME);
                        BigDecimal poPrice = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"po_price"));
                        String goodsInSn = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"goods_in_sn"));
                        String goodsName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"goods_name"));
                        String poLicense = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"po_license"));
                        String poSpecifications = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"po_specifications"));
                        String poUnit = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"po_unit"));
                        String poManufacturer = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"po_manufacturer"));
                        String poSource = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"po_source"));
                        if (StringUtils.isNotBlank(suId)) {
                            erpPurchaseFlow.setSuId(Long.parseLong(suId));
                        }
                        erpPurchaseFlow.setSuDeptNo(RkStringUtil.trim(suDeptNo));
                        erpPurchaseFlow.setPoId(poId);
                        erpPurchaseFlow.setPoNo(RkStringUtil.trim(poNo));
                        erpPurchaseFlow.setPoTime(DateUtil.convertString2Date(poTime, Constants.FORMATE_DAY_TIME));
                        erpPurchaseFlow.setOrderTime(DateUtil.convertString2Date(orderTime, Constants.FORMATE_DAY));
                        erpPurchaseFlow.setEnterpriseInnerCode(RkStringUtil.trim(enterpriseInnerCode));
                        erpPurchaseFlow.setEnterpriseName(RkStringUtil.trim(enterpriseName));
                        erpPurchaseFlow.setPoBatchNo(RkStringUtil.trim(poBatchNo));
                        erpPurchaseFlow.setPoProductTime(DateUtil.convertString2Date(poProductTime, Constants.FORMATE_DAY_TIME));
                        erpPurchaseFlow.setPoEffectiveTime(DateUtil.convertString2Date(poEffectiveTime, Constants.FORMATE_DAY_TIME));
                        if(poQuantity!=null) {
                            erpPurchaseFlow.setPoQuantity(poQuantity);
                        }else{
                            erpPurchaseFlow.setPoQuantity(BigDecimal.ZERO);
                        }
                        if(poPrice!=null) {
                            erpPurchaseFlow.setPoPrice(poPrice);
                        }else{
                            erpPurchaseFlow.setPoPrice(BigDecimal.ZERO);
                        }
                        erpPurchaseFlow.setGoodsInSn(RkStringUtil.trim(goodsInSn));
                        erpPurchaseFlow.setGoodsName(RkStringUtil.trim(goodsName));
                        erpPurchaseFlow.setPoLicense(RkStringUtil.trim(poLicense));
                        erpPurchaseFlow.setPoSpecifications(RkStringUtil.trim(poSpecifications));
                        erpPurchaseFlow.setPoUnit(RkStringUtil.trim(poUnit));
                        erpPurchaseFlow.setPoManufacturer(RkStringUtil.trim(poManufacturer));
                        erpPurchaseFlow.setPoSource(RkStringUtil.trim(poSource));
                        list.add(erpPurchaseFlow);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }

    public static List<BaseErpEntity> getErpSaleFlowList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            logger.debug("数据成功返回：" + JSON.toJSONString(result));
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpSaleFlowDTO erpSaleFlow = new ErpSaleFlowDTO();
                        String suId = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_id"));
                        String suDeptNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_dept_no"));
                        String soId = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_id"));
                        String soNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_no"));
                        String soTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"so_time"), Constants.FORMATE_DAY_TIME);
                        String orderTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"order_time"), Constants.FORMATE_DAY);
                        String enterpriseInnerCode = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"enterprise_inner_code"));
                        String enterpriseName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"enterprise_name"));
                        String licenseNumber = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"license_number"));
                        String soBatchNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_batch_no"));
                        BigDecimal soQuantity = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"so_quantity"));
                        String soProductTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"so_product_time"), Constants.FORMATE_DAY_TIME);
                        String soEffectiveTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"so_effective_time"), Constants.FORMATE_DAY_TIME);
                        BigDecimal soPrice = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"so_price"));
                        String goodsInSn = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"goods_in_sn"));
                        String goodsName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"goods_name"));
                        String soLicense = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_license"));
                        String soSpecifications = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_specifications"));
                        String soUnit = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_unit"));
                        String soManufacturer = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_manufacturer"));
                        String soSource = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_source"));
                        if (StringUtils.isNotBlank(suId)) {
                            erpSaleFlow.setSuId(Long.parseLong(suId));
                        }
                        erpSaleFlow.setSuDeptNo(RkStringUtil.trim(suDeptNo));
                        erpSaleFlow.setSoId(soId);
                        erpSaleFlow.setSoNo(RkStringUtil.trim(soNo));
                        erpSaleFlow.setSoTime(DateUtil.convertString2Date(soTime, Constants.FORMATE_DAY_TIME));
                        erpSaleFlow.setOrderTime(DateUtil.convertString2Date(orderTime, Constants.FORMATE_DAY));
                        erpSaleFlow.setEnterpriseInnerCode(RkStringUtil.trim(enterpriseInnerCode));
                        erpSaleFlow.setEnterpriseName(RkStringUtil.trim(enterpriseName));
                        erpSaleFlow.setLicenseNumber(RkStringUtil.trim(licenseNumber));
                        erpSaleFlow.setSoBatchNo(RkStringUtil.trim(soBatchNo));
                        erpSaleFlow.setSoProductTime(DateUtil.convertString2Date(soProductTime, Constants.FORMATE_DAY_TIME));
                        erpSaleFlow.setSoEffectiveTime(DateUtil.convertString2Date(soEffectiveTime, Constants.FORMATE_DAY_TIME));
                        if(soQuantity!=null) {
                            erpSaleFlow.setSoQuantity(soQuantity);
                        }else{
                            erpSaleFlow.setSoQuantity(BigDecimal.ZERO);
                        }
                        if(soPrice!=null) {
                            erpSaleFlow.setSoPrice(soPrice);
                        }else{
                            erpSaleFlow.setSoPrice(BigDecimal.ZERO);
                        }
                        erpSaleFlow.setGoodsInSn(RkStringUtil.trim(goodsInSn));
                        erpSaleFlow.setGoodsName(RkStringUtil.trim(goodsName));
                        erpSaleFlow.setSoLicense(RkStringUtil.trim(soLicense));
                        erpSaleFlow.setSoSpecifications(RkStringUtil.trim(soSpecifications));
                        erpSaleFlow.setSoUnit(RkStringUtil.trim(soUnit));
                        erpSaleFlow.setSoManufacturer(RkStringUtil.trim(soManufacturer));
                        erpSaleFlow.setSoSource(RkStringUtil.trim(soSource));
                        list.add(erpSaleFlow);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }


    public static List<BaseErpEntity> getErpGoodsBatchFlowList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpGoodsBatchFlowDTO erpGoodsBatchFlowDTO = new ErpGoodsBatchFlowDTO();
                        String suId = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_id"));
                        String suDeptNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_dept_no"));
                        String gbIdNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_id_no"));
                        String gbTime =  CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"gb_time"), Constants.FORMATE_DAY_TIME);
                        String inSn = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"in_sn"));
                        String gbBatchNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_batch_no"));
                        String gbProduceTime = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_produce_time"));
                        String gbEndTime = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_end_time"));
                        String gbProduceAddress = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_produce_address"));
                        BigDecimal gbNumber = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"gb_number"));
                        String gbName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_name"));
                        String gbLicense = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_license"));
                        String gbSpecifications = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_specifications"));
                        String gbUnit = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_unit"));
                        String gbManufacturer = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gb_manufacturer"));

                        if (StringUtils.isNotBlank(suId)) {
                            erpGoodsBatchFlowDTO.setSuId(Long.parseLong(suId));
                        }
                        erpGoodsBatchFlowDTO.setSuDeptNo(RkStringUtil.trim(suDeptNo));
                        erpGoodsBatchFlowDTO.setGbIdNo(RkStringUtil.trim(gbIdNo));
                        erpGoodsBatchFlowDTO.setGbTime(DateUtil.convertString2Date(gbTime, Constants.FORMATE_DAY_TIME));
                        erpGoodsBatchFlowDTO.setInSn(RkStringUtil.trim(inSn));
                        erpGoodsBatchFlowDTO.setGbBatchNo(RkStringUtil.trim(gbBatchNo));
                        // 生产日期、有效期
                        Date gbProduceTimeDate = StrUtil.isBlank(gbProduceTime) ? null : cn.hutool.core.date.DateUtil.parse(gbProduceTime.trim());
                        Date gbEndTimeDate = StrUtil.isBlank(gbEndTime) ? null : cn.hutool.core.date.DateUtil.parse(gbEndTime.trim());
                        erpGoodsBatchFlowDTO.setGbProduceTime(ObjectUtil.isNull(gbProduceTimeDate) ? "" : cn.hutool.core.date.DateUtil.format(gbProduceTimeDate, "yyyy-MM-dd HH:mm:ss"));
                        erpGoodsBatchFlowDTO.setGbEndTime(ObjectUtil.isNull(gbEndTimeDate) ? "" : cn.hutool.core.date.DateUtil.format(gbEndTimeDate, "yyyy-MM-dd HH:mm:ss"));
                        erpGoodsBatchFlowDTO.setGbProduceAddress(RkStringUtil.trim(gbProduceAddress));

                        if(gbNumber!=null) {
                            erpGoodsBatchFlowDTO.setGbNumber(gbNumber);
                        }else{
                            erpGoodsBatchFlowDTO.setGbNumber(BigDecimal.ZERO);
                        }
                        erpGoodsBatchFlowDTO.setGbName(RkStringUtil.trim(gbName));
                        erpGoodsBatchFlowDTO.setGbLicense(RkStringUtil.trim(gbLicense));
                        erpGoodsBatchFlowDTO.setGbSpecifications(RkStringUtil.trim(gbSpecifications));
                        erpGoodsBatchFlowDTO.setGbUnit(RkStringUtil.trim(gbUnit));
                        erpGoodsBatchFlowDTO.setGbManufacturer(RkStringUtil.trim(gbManufacturer));
                        list.add(erpGoodsBatchFlowDTO);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }

    public static List<BaseErpEntity> getErpGoodsCustomerPriceList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpGoodsCustomerPriceDTO erpGoodsCustomerPriceDTO = new ErpGoodsCustomerPriceDTO();
                        String suDeptNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_dept_no"));
                        String gcpIdNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"gcp_id_no"));
                        String innerCode = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"inner_code"));
                        String inSn = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"in_sn"));
                        BigDecimal price = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"price"));
                        erpGoodsCustomerPriceDTO.setGcpIdNo(RkStringUtil.trim(gcpIdNo));
                        erpGoodsCustomerPriceDTO.setPrice(price);
                        erpGoodsCustomerPriceDTO.setInnerCode(RkStringUtil.trim(innerCode));
                        erpGoodsCustomerPriceDTO.setInSn(RkStringUtil.trim(inSn));
                        erpGoodsCustomerPriceDTO.setSuDeptNo(RkStringUtil.trim(suDeptNo));
                        list.add(erpGoodsCustomerPriceDTO);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }

    public static List<BaseErpEntity> getErpGoodsGroupPriceList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpGoodsGroupPriceDTO erpGoodsGroupPriceDTO = new ErpGoodsGroupPriceDTO();
                        String suDeptNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_dept_no"));
                        String ggpIdNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"ggp_id_no"));
                        String groupName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"group_name"));
                        String inSn = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"in_sn"));
                        BigDecimal price = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"price"));
                        erpGoodsGroupPriceDTO.setGgpIdNo(RkStringUtil.trim(ggpIdNo));
                        erpGoodsGroupPriceDTO.setPrice(price);
                        erpGoodsGroupPriceDTO.setGroupName(RkStringUtil.trim(groupName));
                        erpGoodsGroupPriceDTO.setInSn(RkStringUtil.trim(inSn));
                        erpGoodsGroupPriceDTO.setSuDeptNo(RkStringUtil.trim(suDeptNo));
                        list.add(erpGoodsGroupPriceDTO);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }

    public static List<BaseErpEntity> getErpOrderPurchaseDeliveryList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpOrderPurchaseDeliveryDTO erpOrderPurchaseDeliveryDTO = new ErpOrderPurchaseDeliveryDTO();
                        String suId = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_id"));
                        String suDeptNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_dept_no"));
                        String deliveryNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"delivery_no"));
                        BigDecimal deliveryQuantity = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"delivery_quantity"));
                        String orderDeliveryErpId = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"order_delivery_erp_id"));

                        if (StringUtils.isNotBlank(suId)) {
                            erpOrderPurchaseDeliveryDTO.setSuId(Long.parseLong(suId));
                        }
                        if (StringUtils.isNotBlank(orderDeliveryErpId)) {
                            erpOrderPurchaseDeliveryDTO.setOrderDeliveryErpId(Long.parseLong(orderDeliveryErpId));
                        }
                        erpOrderPurchaseDeliveryDTO.setSuDeptNo(RkStringUtil.trim(suDeptNo));
                        erpOrderPurchaseDeliveryDTO.setDeliveryNo(RkStringUtil.trim(deliveryNo));
                        erpOrderPurchaseDeliveryDTO.setDeliveryQuantity(deliveryQuantity);
                        list.add(erpOrderPurchaseDeliveryDTO);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }

    public static List<BaseErpEntity> getErpShopSaleFlowList(SysConfig sysConfig, String sql) throws Exception {
        logger.debug("根据sql获取数据：" + sql);
        List list = new ArrayList();
        if ((sql != null) && (!sql.trim().equals(""))) {
            List<Map<Object, Object>> result = DataBaseConnection.getInstance().executeQuery(sysConfig, sql);
            logger.debug("数据成功返回：" + JSON.toJSONString(result));
            try {
                if ((result != null) && (result.size() > 0)) {
                    for (Map map : result) {
                        ErpShopSaleFlowDTO erpShopSaleFlow = new ErpShopSaleFlowDTO();
                        String suId = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_id"));
                        String suDeptNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"su_dept_no"));
                        String shopNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"shop_no"));
                        String shopName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"shop_name"));
                        String soId = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_id"));
                        String soNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_no"));
                        String soTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"so_time"), Constants.FORMATE_DAY_TIME);
                        String orderTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"order_time"), Constants.FORMATE_DAY);
                        String enterpriseName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"enterprise_name"));
                        String soBatchNo = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_batch_no"));
                        BigDecimal soQuantity = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"so_quantity"));
                        String soProductTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"so_product_time"), Constants.FORMATE_DAY_TIME);
                        String soEffectiveTime = CommonConstants.getDate(Utils.getObjectIgnoreCase(map,"so_effective_time"), Constants.FORMATE_DAY_TIME);
                        BigDecimal soPrice = CommonConstants.getBigDecimal(Utils.getObjectIgnoreCase(map,"so_price"));
                        String goodsInSn = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"goods_in_sn"));
                        String goodsName = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"goods_name"));
                        String soLicense = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_license"));
                        String soSpecifications = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_specifications"));
                        String soUnit = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_unit"));
                        String soManufacturer = CommonConstants.getString(Utils.getObjectIgnoreCase(map,"so_manufacturer"));
                        if (StringUtils.isNotBlank(suId)) {
                            erpShopSaleFlow.setSuId(Long.parseLong(suId));
                        }
                        erpShopSaleFlow.setSuDeptNo(RkStringUtil.trim(suDeptNo));
                        erpShopSaleFlow.setSoId(soId);
                        erpShopSaleFlow.setShopNo(RkStringUtil.trim(shopNo));
                        erpShopSaleFlow.setShopName(RkStringUtil.trim(shopName));
                        erpShopSaleFlow.setSoNo(RkStringUtil.trim(soNo));
                        erpShopSaleFlow.setSoTime(DateUtil.convertString2Date(soTime, Constants.FORMATE_DAY_TIME));
                        erpShopSaleFlow.setEnterpriseName(RkStringUtil.trim(enterpriseName));
                        erpShopSaleFlow.setSoBatchNo(RkStringUtil.trim(soBatchNo));
                        erpShopSaleFlow.setSoProductTime(DateUtil.convertString2Date(soProductTime, Constants.FORMATE_DAY_TIME));
                        erpShopSaleFlow.setSoEffectiveTime(DateUtil.convertString2Date(soEffectiveTime, Constants.FORMATE_DAY_TIME));
                        if(soQuantity!=null) {
                            erpShopSaleFlow.setSoQuantity(soQuantity);
                        }else{
                            erpShopSaleFlow.setSoQuantity(BigDecimal.ZERO);
                        }
                        if(soPrice!=null) {
                            erpShopSaleFlow.setSoPrice(soPrice);
                        }else{
                            erpShopSaleFlow.setSoPrice(BigDecimal.ZERO);
                        }
                        erpShopSaleFlow.setGoodsInSn(RkStringUtil.trim(goodsInSn));
                        erpShopSaleFlow.setGoodsName(RkStringUtil.trim(goodsName));
                        erpShopSaleFlow.setSoLicense(RkStringUtil.trim(soLicense));
                        erpShopSaleFlow.setSoSpecifications(RkStringUtil.trim(soSpecifications));
                        erpShopSaleFlow.setSoUnit(RkStringUtil.trim(soUnit));
                        erpShopSaleFlow.setSoManufacturer(RkStringUtil.trim(soManufacturer));
                        list.add(erpShopSaleFlow);
                    }
                }
            } catch (ErpException e) {
                throw e;
            } catch (Exception e) {
                throw new ErpException("返回的结果集与标准的列名无法对应", e);
            }
        }
        return list;
    }
}
