package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.export.export.bo.HmcExportMarketOrderBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.order.api.MarketOrderDetailApi;
import com.yiling.hmc.order.dto.AdminMarketOrderDTO;
import com.yiling.hmc.order.dto.MarketOrderDetailDTO;
import com.yiling.hmc.order.dto.request.QueryAdminMarkerOrderPageRequest;
import com.yiling.hmc.order.enums.HmcDeliveryTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.enums.HmcPaymentStatusEnum;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcDoctorInfoDTO;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.request.QueryHmcUserPageListRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * 健康中心市场订单列表导出-商家后台
 *
 * @author: benben.jia
 * @data: 2023/03/06
 */
@Service("hmcB2BMarketOrderExportServiceImpl")
public class HmcB2BMarketOrderExportServiceImpl implements BaseExportQueryDataService<QueryAdminMarkerOrderPageRequest> {

    @DubboReference
    MarketOrderApi marketOrderApi;

    @DubboReference
    MarketOrderDetailApi marketOrderDetailApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    DoctorApi doctorApi;

    @DubboReference
    DictApi dictApi;


    private static final LinkedHashMap<String, String> ORDER_SHEET = new LinkedHashMap<String, String>() {

        {
            put("orderNo", "对应订单号");
            put("goodsInfo", "全部商品信息");
            put("goodsTotalAmount", "商品合计金额(元)");
            put("freightAmount", "运费");
            put("orderTotalAmount", "订单金额(元)");
            put("thirdPayAmount", "订单实际金额(元)");
            put("paymentStatus", "用户支付状态");
            put("thirdPayNo", "外部支付流水号");
            put("deliverType", "订单配送方式");
            put("orderStatus", "订单状态");
            put("name", "收货人/提货人");
            put("mobile", "收货人手机号/提货人手机号");
            put("provinceName", "收货人省份");
            put("cityName", "收货人城市");
            put("regionName", "收货人地区");
            put("address", "收货人详细地址/提货地址");
            put("deliverCompanyName", "快递公司");
            put("deliverNo", "快递单号");
            put("remark", "买家备注");
            put("platformRemark", "运营备注");
            put("merchantRemark", "商家备注");
            put("doctor", "邀请医生");
            put("createTime", "下单时间");
            put("payTime", "支付时间");
            put("deliverTime", "发货时间");
            put("receiveTime", "确认收货时间");
            put("cancelTime", "取消时间");

        }
    };
    private static final LinkedHashMap<String, String> ORDER_DETAIL_SHEET = new LinkedHashMap<String, String>() {

        {
            put("orderNo", "对应订单号");
            put("goodsName", "商品名称");
            put("goodsSpecification", "规格");
            put("goodsQuantity", "销售数量");
            put("goodsPrice", "销售单价");
        }
    };

    @Override
    public QueryExportDataDTO queryData(QueryAdminMarkerOrderPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> orderData = new ArrayList<>();
        List<Map<String, Object>> orderDetailData = new ArrayList<>();
        Page<AdminMarketOrderDTO> page;
        int current = 1;

        List goodsIdList = Collections.emptyList();
        String goodsName = request.getGoodsName();
        if (StringUtils.isNotBlank(goodsName)) {
            List<MarketOrderDetailDTO> marketOrderDetailDTOS = marketOrderDetailApi.queryByGoodsNameList(goodsName);
            if (CollectionUtils.isEmpty(marketOrderDetailDTOS)) {
                return result;
            }
            goodsIdList = marketOrderDetailDTOS.stream().map(MarketOrderDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
        }
        List userList = Collections.emptyList();
        String nickName = request.getNickName();
        if (StringUtils.isNotBlank(nickName)) {
            QueryHmcUserPageListRequest queryHmcUserPageListRequest = new QueryHmcUserPageListRequest();
            queryHmcUserPageListRequest.setCurrent(1).setSize(1000);
            queryHmcUserPageListRequest.setNickName(nickName);
            Page<HmcUser> hmcUserPage = hmcUserApi.pageList(queryHmcUserPageListRequest);
            if (hmcUserPage.getTotal() == 0) {
                return result;
            }
            userList = hmcUserPage.getRecords().stream().map(HmcUser::getUserId).distinct().collect(Collectors.toList());
        }
        request.setGoodsIdList(goodsIdList);
        request.setUserList(userList);

        DictBO dict = dictApi.getDictByName("hmc_market_order_deliver_company");
        Map<String, String> dictMap = dict.getDataList().stream().collect(Collectors.toMap(DictBO.DictData::getValue, DictBO.DictData::getLabel));

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = marketOrderApi.queryAdminMarketOrderPage(request);
            if (CollUtil.isEmpty(page.getRecords())) {
                continue;
            }
            //查询药品信息
            List<Long> orderIds = page.getRecords().stream().map(AdminMarketOrderDTO::getId).distinct().collect(Collectors.toList());
            List<MarketOrderDetailDTO> detailDTOS = marketOrderDetailApi.queryByOrderIdList(orderIds);
            detailDTOS.sort(Comparator.comparing(MarketOrderDetailDTO::getOrderId).reversed());
            if (CollUtil.isNotEmpty(detailDTOS)) {
                detailDTOS.forEach(e -> {
                    e.setGoodsPrice(e.getGoodsPrice().setScale(2));
                    Map<String, Object> beanToMap = BeanUtil.beanToMap(e);
                    orderDetailData.add(beanToMap);
                });
            }
            Map<Long, List<MarketOrderDetailDTO>> detailMap = detailDTOS.stream().collect(Collectors.groupingBy(MarketOrderDetailDTO::getOrderId));
            //查询用户信息
            List<Long> userIdList = page.getRecords().stream().map(AdminMarketOrderDTO::getCreateUser).distinct().collect(Collectors.toList());
            List<HmcUser> hmcUsers = hmcUserApi.listByIds(userIdList);
            Map<Long, HmcUser> userMap = hmcUsers.stream().collect(Collectors.toMap(HmcUser::getUserId, Function.identity()));

            //查询医生信息
            List<Integer> doctorIdList = page.getRecords().stream().map(AdminMarketOrderDTO::getDoctorId).distinct().collect(Collectors.toList());
            List<HmcDoctorInfoDTO> doctorInfoByIds = doctorApi.getDoctorInfoByIds(doctorIdList);
            Map<Integer, HmcDoctorInfoDTO> doctorMap = doctorInfoByIds.stream().collect(Collectors.toMap(HmcDoctorInfoDTO::getId, Function.identity()));
            page.getRecords().forEach(e -> {
                HmcExportMarketOrderBO bo = PojoUtils.map(e, HmcExportMarketOrderBO.class);
                bo.setOrderStatus(HmcOrderStatusEnum.getByCode(e.getOrderStatus()).getName());
                bo.setPaymentStatus(HmcPaymentStatusEnum.getByCode(e.getPaymentStatus()).getName());
                bo.setDeliverType(HmcDeliveryTypeEnum.getByCode(e.getDeliverType()).getName());
                if (StringUtils.isNotBlank(e.getAddress())) {
                    String[] s = e.getAddress().split(" ");
                    bo.setProvinceName(s[0]);
                    bo.setCityName(s[1]);
                    bo.setRegionName(s[2]);
                    bo.setAddress(s[3]);
                }
                bo.setDeliverCompanyName(dictMap.get(e.getDeliverCompanyName()));
                if (userMap.containsKey(e.getCreateUser())) {
                    bo.setNickName(userMap.get(e.getCreateUser()).getNickName());
                    bo.setUserMobile(userMap.get(e.getCreateUser()).getMobile());
                }
                if (doctorMap.containsKey(e.getDoctorId())) {
                    HmcDoctorInfoDTO hmcDoctorInfoDTO = doctorMap.get(e.getDoctorId());
                    bo.setDoctor(hmcDoctorInfoDTO.getDoctorName() + "(" + hmcDoctorInfoDTO.getHospitalName() + ")");
                }

                if (detailMap.containsKey(e.getId())) {
                    List<MarketOrderDetailDTO> marketOrderDetailDTOS = detailMap.get(e.getId());
                    StringBuffer buffer = new StringBuffer();
                    for (MarketOrderDetailDTO marketOrderDetailDTO : marketOrderDetailDTOS) {
                        buffer.append("【商品名称:").append(marketOrderDetailDTO.getGoodsName()).append(",规格:").append(marketOrderDetailDTO.getGoodsSpecification()).append(",数量:").append(marketOrderDetailDTO.getGoodsQuantity()).append(",销售单价:").append(marketOrderDetailDTO.getGoodsPrice().setScale(2)).append("】").append("\n");
                    }
                    bo.setGoodsInfo(buffer.toString());
                }
                bo.setCreateTime(DateUtil.format(e.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                if (e.getPayTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
                    bo.setPayTime(null);
                } else {
                    bo.setPayTime(DateUtil.format(e.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                }
                if (e.getDeliverTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
                    bo.setDeliverTime(null);
                } else {
                    bo.setDeliverTime(DateUtil.format(e.getDeliverTime(), "yyyy-MM-dd HH:mm:ss"));
                }
                if (e.getCancelTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
                    bo.setCancelTime(null);
                } else {
                    bo.setCancelTime(DateUtil.format(e.getCancelTime(), "yyyy-MM-dd HH:mm:ss"));
                }
                if (e.getReceiveTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
                    bo.setReceiveTime(null);
                } else {
                    bo.setReceiveTime(DateUtil.format(e.getReceiveTime(), "yyyy-MM-dd HH:mm:ss"));
                }
                Map<String, Object> beanToMap = BeanUtil.beanToMap(bo);
                orderData.add(beanToMap);
            });
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        ExportDataDTO orderDataDTO = new ExportDataDTO();
        orderDataDTO.setSheetName("订单表");
        // 页签字段
        orderDataDTO.setFieldMap(ORDER_SHEET);
        // 页签数据
        orderDataDTO.setData(orderData);

        ExportDataDTO orderDetailDataDTO = new ExportDataDTO();
        orderDetailDataDTO.setSheetName("订单商品表");
        // 页签字段
        orderDetailDataDTO.setFieldMap(ORDER_DETAIL_SHEET);
        // 页签数据
        orderDetailDataDTO.setData(orderDetailData);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(orderDataDTO);
        sheets.add(orderDetailDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public QueryAdminMarkerOrderPageRequest getParam(Map<String, Object> map) {
//        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        QueryAdminMarkerOrderPageRequest request = PojoUtils.map(map, QueryAdminMarkerOrderPageRequest.class);
//        request.setEid(eid);
        return request;
    }
}
