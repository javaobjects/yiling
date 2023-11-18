package com.yiling.admin.data.center.report.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.settlement.report.enums.*;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.report.bo.ReportLadderGoodsBO;
import com.yiling.admin.data.center.report.form.ImportOrderIdentForm;
import com.yiling.admin.data.center.report.form.QueryB2bOrderPageForm;
import com.yiling.admin.data.center.report.form.QueryFlowOrderPageForm;
import com.yiling.admin.data.center.report.form.UpdateB2bOrderIdenForm;
import com.yiling.admin.data.center.report.form.UpdateFlowOrderIdenForm;
import com.yiling.admin.data.center.report.form.UpdateProporIdentificaForm;
import com.yiling.admin.data.center.report.handler.ImportOrderIdentHandler;
import com.yiling.admin.data.center.report.vo.FlowOrderRebateReportPageItemVO;
import com.yiling.admin.data.center.report.vo.OrderDeliveryReportVO;
import com.yiling.admin.data.center.report.vo.ReportCommonPageVO;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.relation.api.FlowGoodsRelationEditTaskApi;
import com.yiling.dataflow.relation.dto.FlowGoodsRelationEditTaskDTO;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskSyncStatusEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.settlement.report.api.ReportOrderSyncApi;
import com.yiling.settlement.report.api.ReportParamApi;
import com.yiling.settlement.report.dto.ReportB2bOrderSyncDTO;
import com.yiling.settlement.report.dto.ReportFlowSaleOrderSyncDTO;
import com.yiling.settlement.report.dto.ReportLadderGoodsInfoDTO;
import com.yiling.settlement.report.dto.ReportParamSubGoodsDTO;
import com.yiling.settlement.report.dto.ReportPriceParamNameDTO;
import com.yiling.settlement.report.dto.ReportYlGoodsCategoryDTO;
import com.yiling.settlement.report.dto.request.CreateReportB2bRequest;
import com.yiling.settlement.report.dto.request.CreateReportFlowRequest;
import com.yiling.settlement.report.dto.request.QueryFlowOrderPageListRequest;
import com.yiling.settlement.report.dto.request.QueryGoodsCategoryRequest;
import com.yiling.settlement.report.dto.request.QueryGoodsInfoRequest;
import com.yiling.settlement.report.dto.request.QueryOrderSyncPageListRequest;
import com.yiling.settlement.report.dto.request.UpdateB2bOrderIdenRequest;
import com.yiling.settlement.report.dto.request.UpdateFlowOrderIdenRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单返利报表
 *
 * @author: gxl
 * @date: 2022/2/24
 */
@RestController
@RequestMapping("/report/order")
@Api(tags = "订单返利报表")
@Slf4j
public class OrderRebateReportController extends BaseController {

    @Autowired
    ImportOrderIdentHandler importOrderIdentHandler;


    @DubboReference
    ReportParamApi reportParamApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference(timeout = 1000 * 60)
    ReportOrderSyncApi reportOrderSyncApi;
    @DubboReference
    DictApi dictApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    GoodsYilingPriceApi goodsYilingPriceApi;
    @DubboReference
    FlowGoodsRelationEditTaskApi flowGoodsRelationEditTaskApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @DubboReference
    CustomerApi customerApi;

    public static Integer[] otherOrderSource=new Integer[]{4,5,6,7,8,9,10,11,12,13,14,15,16,17};


    @ApiOperation(value = "b2b订单返利列表")
    @GetMapping("/queryB2bOrderRebateReportPage")
    public Result<ReportCommonPageVO<OrderDeliveryReportVO>> queryB2bOrderRebateReportPage(@Valid QueryB2bOrderPageForm form) {
        //校验起止时间
        checkTimeSlot(form.getStartReceiveTime(), form.getEndReceiveTime());

        QueryOrderSyncPageListRequest request = PojoUtils.map(form, QueryOrderSyncPageListRequest.class);

        //返利状态
        if (ObjectUtil.isNull(form.getReportStatus()) || ObjectUtil.equal(form.getReportStatus(), -1)) {
            request.setReportStatusList(ListUtil.toList(0, ReportStatusEnum.OPERATOR_REJECT.getCode(), ReportStatusEnum.FINANCE_REJECT.getCode(), ReportStatusEnum.ADMIN_REJECT.getCode()));
        } else {
            request.setReportStatusList(ListUtil.toList(form.getReportStatus()));
        }

        if (StringUtils.isNotEmpty(form.getProvinceCode()) || StringUtils.isNotEmpty(form.getCityCode()) || StringUtils.isNotEmpty(form.getRegionCode())) {
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(form.getCityCode());
            eidRequest.setRegionCode(form.getRegionCode());
            eidRequest.setProvinceCode(form.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.queryListByArea(eidRequest);
            List<Long> eids = enterpriseDTOS.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(eids)) {
                request.setSellerEidList(eids);
            } else {
                return Result.success(new ReportCommonPageVO<>());
            }
        }
        if (ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.notEqual(form.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            if (ObjectUtil.isNull(enterpriseDTO)) {
                return Result.success(new ReportCommonPageVO<>());
            }
            if (CollUtil.isNotEmpty(request.getSellerEidList()) && !request.getSellerEidList().contains(enterpriseDTO.getId())) {
                return Result.success(new ReportCommonPageVO<>());
            }
            request.setSellerEidList(ListUtil.toList(enterpriseDTO.getId()));
        }
        if (CollUtil.isEmpty(request.getSellerEidList())) {
            return Result.success(new ReportCommonPageVO<>());
        }
        Page<ReportB2bOrderSyncDTO> orderPage = reportOrderSyncApi.queryB2bOrderSyncInfoPageList(request);
        if (CollUtil.isEmpty(orderPage.getRecords())) {
            return Result.success(new ReportCommonPageVO<>());
        }
        ReportCommonPageVO<OrderDeliveryReportVO> result = new ReportCommonPageVO<>();
        PojoUtils.map(orderPage, result);
        List<OrderDeliveryReportVO> records = PojoUtils.map(orderPage.getRecords(), OrderDeliveryReportVO.class);
        result.setRecords(records);
        //查询企业信息
        List<Long> eidList = orderPage.getRecords().stream().map(ReportB2bOrderSyncDTO::getSellerEid).collect(Collectors.toList());
        eidList.addAll(orderPage.getRecords().stream().map(ReportB2bOrderSyncDTO::getBuyerEid).collect(Collectors.toList()));
        eidList = eidList.stream().distinct().collect(Collectors.toList());
        Map<Long, EnterpriseDTO> entMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
        //查询orderChange
        List<Long> orderIdList = orderPage.getRecords().stream().map(ReportB2bOrderSyncDTO::getOrderId).distinct().collect(Collectors.toList());
        List<OrderDetailChangeDTO> changeList = orderDetailChangeApi.listByOrderIds(orderIdList);
        Map<Long, List<OrderDetailChangeDTO>> orderChangeMap = changeList.stream().collect(Collectors.groupingBy(OrderDetailChangeDTO::getOrderId));
        //查询order明细的内码
        Map<Long, List<ReportB2bOrderSyncDTO>> orderSyncMap = orderPage.getRecords().stream().collect(Collectors.groupingBy(ReportB2bOrderSyncDTO::getOrderId));
        //查询商品参数
        Map<Long, List<ReportPriceParamNameDTO>> orderGoodsPriceMap = MapUtil.newHashMap();
        Map<Long, List<ReportYlGoodsCategoryDTO>> orderGoodsCatMap = MapUtil.newHashMap();

        orderSyncMap.forEach((k, v) -> {
            Date orderCreateTime = v.stream().findAny().get().getOrderCreateTime();
            List<Long> goodsId = v.stream().filter(e -> ObjectUtil.equal(e.getOrderGoodsType(), 1)).map(ReportB2bOrderSyncDTO::getGoodsId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(goodsId)) {
                //查询价格信息
                List<ReportPriceParamNameDTO> priceParamNameList = PojoUtils.map(goodsYilingPriceApi.getPriceParamNameList(goodsId, orderCreateTime), ReportPriceParamNameDTO.class);
                orderGoodsPriceMap.put(k, priceParamNameList);
                //查询商品分类
                QueryGoodsCategoryRequest queryGoodsCategoryRequest = new QueryGoodsCategoryRequest();
                queryGoodsCategoryRequest.setDate(orderCreateTime);
                queryGoodsCategoryRequest.setGoodsIds(goodsId);
                List<ReportYlGoodsCategoryDTO> reportParamSubGoodsDTOS = reportParamApi.queryCategoryByYlGoodsIds(queryGoodsCategoryRequest);
                orderGoodsCatMap.put(k, reportParamSubGoodsDTOS);
            }
        });

        Map<Long, List<OrderDetailChangeDTO>> finalOrderChangeMap = orderChangeMap;
        result.getRecords().forEach(e -> {
            e.setSellerEname(entMap.get(e.getSellerEid()).getName());
            e.setBuyerEname(entMap.get(e.getBuyerEid()).getName());
            EnterpriseCustomerDTO customerDTO = customerApi.get(e.getSellerEid(), e.getBuyerEid());
            if (ObjectUtil.isNotNull(customerDTO)){
                e.setErpCustomerName(customerDTO.getCustomerName());
            }
            e.setProvinceName(entMap.get(e.getBuyerEid()).getProvinceName());
            e.setCityName(entMap.get(e.getBuyerEid()).getCityName());
            e.setRegionName(entMap.get(e.getBuyerEid()).getRegionName());
            //设置参数信息如出货价
            if (CollUtil.isNotEmpty(orderGoodsPriceMap.get(e.getOrderId())) && ObjectUtil.isNotNull(orderGoodsCatMap.get(e.getOrderId()))) {
                setReportDetailB2bPrice(e, orderGoodsPriceMap.get(e.getOrderId()), orderGoodsCatMap.get(e.getOrderId()).stream().collect(Collectors.toMap(ReportYlGoodsCategoryDTO::getYlGoodsId, Function.identity())));
            } else {
                setReportDetailB2bPrice(e, orderGoodsPriceMap.get(e.getOrderId()), MapUtil.newHashMap());
            }
            //判断如果是非以岭品或者是失效订单则就此结束
            if (ObjectUtil.equal(e.getOrderGoodsType(), 0)) {
                return;
            }
            Map<Long, OrderDetailChangeDTO> changeDTOMap = finalOrderChangeMap.get(e.getOrderId()).stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, c -> c));
            List<String> goodsErpCodeList = orderSyncMap.get(e.getOrderId()).stream().map(ReportB2bOrderSyncDTO::getGoodsErpCode).collect(Collectors.toList());
            //计算奖励
            calculateRewardB2b(e, e.getOutPrice(), changeDTOMap.get(e.getDetailId()), goodsErpCodeList);
        });
        result.setEname(enterpriseApi.getById(request.getSellerEidList().get(0)).getName());
        result.setOrderCount(reportOrderSyncApi.queryB2bOrderCount(request));
        result.setGoodsCount(result.getTotal());
        return Result.success(result);
    }


    @ApiOperation(value = "生成b2b返利报表")
    @PostMapping("/calculateB2bReward")
    public Result<Boolean> calculateB2bReward(@RequestBody @Valid QueryB2bOrderPageForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        QueryOrderSyncPageListRequest request = PojoUtils.map(form, QueryOrderSyncPageListRequest.class);

        //返利状态
        if (ObjectUtil.isNull(form.getReportStatus()) || ObjectUtil.equal(form.getReportStatus(), -1)) {
            request.setReportStatusList(ListUtil.toList(0, ReportStatusEnum.OPERATOR_REJECT.getCode(), ReportStatusEnum.FINANCE_REJECT.getCode(), ReportStatusEnum.ADMIN_REJECT.getCode()));
        } else {
            request.setReportStatusList(ListUtil.toList(form.getReportStatus()));
        }

        if (StringUtils.isNotEmpty(form.getProvinceCode()) || StringUtils.isNotEmpty(form.getCityCode()) || StringUtils.isNotEmpty(form.getRegionCode())) {
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(form.getCityCode());
            eidRequest.setRegionCode(form.getRegionCode());
            eidRequest.setProvinceCode(form.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.queryListByArea(eidRequest);
            List<Long> eids = enterpriseDTOS.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(eids)) {
                request.setSellerEidList(eids);
            } else {
                return Result.failed("需要计算的返利订单为空，请重置筛选项");
            }
        }
        if (ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.notEqual(form.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            if (ObjectUtil.isNull(enterpriseDTO)) {
                return Result.failed("需要计算的返利订单为空，请重置筛选项");
            }
            if (CollUtil.isNotEmpty(request.getSellerEidList()) && !request.getSellerEidList().contains(enterpriseDTO.getId())) {
                return Result.failed("需要计算的返利订单为空，请重置筛选项");
            }
            request.setSellerEidList(ListUtil.toList(enterpriseDTO.getId()));
        }
        if (CollUtil.isEmpty(request.getSellerEidList())) {
            return Result.failed("需要计算的返利订单为空，请重置筛选项");
        }
        AtomicReference<Integer> relationTaskCount = new AtomicReference<>();
        relationTaskCount.set(0);

        request.getSellerEidList().forEach(e -> {
            //校验以岭品修改关系任务
            Boolean isExit = checkReviseRelationTask(e);
            if (isExit){
                Integer i = relationTaskCount.get();
                relationTaskCount.set(++i);
                return;
            }
            CreateReportB2bRequest createReportB2bRequest = new CreateReportB2bRequest();
            createReportB2bRequest.setSellerEid(e);
            createReportB2bRequest.setReportStatusList(request.getReportStatusList());
            createReportB2bRequest.setStartReceiveTime(request.getStartReceiveTime());
            createReportB2bRequest.setEndReceiveTime(DateUtil.endOfDay(request.getEndReceiveTime()));
            createReportB2bRequest.setIdentificationStatus(request.getIdentificationStatus());
            createReportB2bRequest.setOpUserId(adminInfo.getCurrentUserId());
            //按企业 发送消息 异步生成
            sendMq(Constants.TOPIC_REPORT_REWARD_B2B_SEND, Constants.TAG_REPORT_REWARD_B2B_SEND, JSON.toJSONString(createReportB2bRequest));
        });
        if(relationTaskCount.get()==0){
            return Result.success(Boolean.TRUE);
        }
        if(relationTaskCount.get()==1){
            return Result.failed("此企业存正在修改以岭品对应关系，暂不能生成报表，请稍后重试");
        }else {
            return Result.failed("此次生成的报表任务有"+relationTaskCount.get()+"个企业正在修改以岭品关系，暂时不能生成报表，其余企业报表已生成");
        }
    }

    @ApiOperation(value = "流向订单返利列表")
    @GetMapping("/queryFlowOrderRebateReportPage")
    public Result<ReportCommonPageVO<FlowOrderRebateReportPageItemVO>> queryFlowOrderRebateReportPage(QueryFlowOrderPageForm form) {
        QueryFlowOrderPageListRequest request = PojoUtils.map(form, QueryFlowOrderPageListRequest.class);

        //校验起止时间
        checkTimeSlot(form.getStartSoTime(), form.getEndSoTime());

        //返利状态
        if (ObjectUtil.isNull(form.getReportStatus()) || ObjectUtil.equal(form.getReportStatus(), -1)) {
            request.setReportStatusList(ListUtil.toList(0, ReportStatusEnum.OPERATOR_REJECT.getCode(), ReportStatusEnum.FINANCE_REJECT.getCode(), ReportStatusEnum.ADMIN_REJECT.getCode()));
        } else {
            request.setReportStatusList(ListUtil.toList(form.getReportStatus()));
        }

        if (StringUtils.isNotEmpty(form.getProvinceCode()) || StringUtils.isNotEmpty(form.getCityCode()) || StringUtils.isNotEmpty(form.getRegionCode())) {
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(form.getCityCode());
            eidRequest.setRegionCode(form.getRegionCode());
            eidRequest.setProvinceCode(form.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.queryListByArea(eidRequest);
            List<Long> eids = enterpriseDTOS.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(eids)) {
                request.setEidList(eids);
            } else {
                return Result.success(new ReportCommonPageVO<>());
            }
        }
        if (ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.notEqual(form.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            if (ObjectUtil.isNull(enterpriseDTO)) {
                return Result.success(new ReportCommonPageVO<>());
            }
            if (CollUtil.isNotEmpty(request.getEidList()) && !request.getEidList().contains(enterpriseDTO.getId())) {
                return Result.success(new ReportCommonPageVO<>());
            }
            request.setEidList(ListUtil.toList(enterpriseDTO.getId()));
        }
        if (CollUtil.isEmpty(request.getEidList())) {
            return Result.success(new ReportCommonPageVO<>());
        }
        Page<ReportFlowSaleOrderSyncDTO> orderPage = reportOrderSyncApi.queryFlowOrderPageList(request);
        if (CollUtil.isEmpty(orderPage.getRecords())) {
            return Result.success(new ReportCommonPageVO<>());
        }
        ReportCommonPageVO result = new ReportCommonPageVO();

        Page<FlowOrderRebateReportPageItemVO> page = PojoUtils.map(orderPage, FlowOrderRebateReportPageItemVO.class);
        List<FlowOrderRebateReportPageItemVO> records = page.getRecords();
        PojoUtils.map(page, result);
        result.setRecords(records);

        Map<String, List<FlowOrderRebateReportPageItemVO>> noMap = records.stream().collect(Collectors.groupingBy(FlowOrderRebateReportPageItemVO::getSoNo));

        Set<String> noList = noMap.keySet();
        for (String no : noList) {
            //订单下的所有商品明细
            List<FlowOrderRebateReportPageItemVO> orderList = noMap.get(no);
            List<Long> goodsIdList = orderList.stream().map(FlowOrderRebateReportPageItemVO::getYlGoodsId).collect(Collectors.toList());
            List<String> goodsInSnList = orderList.stream().map(FlowOrderRebateReportPageItemVO::getGoodsInSn).collect(Collectors.toList());
            List<ReportPriceParamNameDTO> priceParamNameList = PojoUtils.map(goodsYilingPriceApi.getPriceParamNameList(goodsIdList, orderList.stream().findAny().get().getSoTime()), ReportPriceParamNameDTO.class);
            //查询阶梯参数
            QueryGoodsInfoRequest queryRequest = new QueryGoodsInfoRequest();
            queryRequest.setEid(orderList.stream().findAny().get().getEid());
            //goods后期使用是否以岭标志判断
            queryRequest.setGoodsInSns(goodsInSnList);
            List<ReportLadderGoodsInfoDTO> ladderParList = reportParamApi.queryLadderInfo(queryRequest);
            //查询商品参数
            List<ReportParamSubGoodsDTO> activityParList = reportParamApi.queryActivityGoodsInfo(queryRequest);
            //查询以岭商品名称
            Map<Long, GoodsDTO> goodsMap = goodsApi.batchQueryInfo(goodsIdList).stream().collect(Collectors.toMap(GoodsDTO::getId, e -> e));

            //计算返利数据
            orderList.forEach(orderListItem -> {
                //供货价
                BigDecimal supplyPrice = BigDecimal.ZERO;
                //出货价
                BigDecimal outPrice = BigDecimal.ZERO;
                //查询以岭品名称
                GoodsDTO goodsInfo = goodsMap.get(orderListItem.getYlGoodsId());
                if (ObjectUtil.isNotNull(goodsInfo)) {
                    orderListItem.setYlGoodsName(goodsInfo.getName());
                    orderListItem.setYlSpecifications(goodsInfo.getSellSpecifications());
                }

                //设置渠道
                //查询字典
                DictBO dict = dictApi.getDictByName("erp_sale_flow_source");
                Map<String, String> dictMap = dict.getDataList().stream().collect(Collectors.toMap(DictBO.DictData::getValue, DictBO.DictData::getLabel));
                orderListItem.setSoSourceStr(dictMap.get(orderListItem.getSoSource()));

                //设置供货价
                ReportPriceParamNameDTO s = this.getPriceByParam(2L, orderListItem.getYlGoodsId(), priceParamNameList);
                if (null != s) {
                    supplyPrice = NumberUtil.round(s.getPrice(), 2, RoundingMode.HALF_UP);
                }
                //设置出货价
                ReportPriceParamNameDTO o = this.getPriceByParam(1L, orderListItem.getYlGoodsId(), priceParamNameList);
                if (null != o) {
                    outPrice = NumberUtil.round(o.getPrice(), 2, RoundingMode.HALF_UP);
                }
                orderListItem.setSupplyPrice(supplyPrice);
                orderListItem.setOutPrice(outPrice);

                //计算奖励
                calculateFlowReward(orderListItem, outPrice, ladderParList, activityParList);
            });
        }
        result.setEname(enterpriseApi.getById(request.getEidList().get(0)).getName());
        result.setOrderCount(reportOrderSyncApi.queryFlowOrderCount(request));
        result.setGoodsCount(page.getTotal());
        return Result.success(result);
    }

    @ApiOperation(value = "生成流向返利报表")
    @PostMapping("/calculateFlowReward")
    public Result<Boolean> calculateFlowReward(@RequestBody @Valid QueryFlowOrderPageForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        QueryFlowOrderPageListRequest request = PojoUtils.map(form, QueryFlowOrderPageListRequest.class);

        //返利状态
        if (ObjectUtil.isNull(form.getReportStatus()) || ObjectUtil.equal(form.getReportStatus(), -1)) {
            request.setReportStatusList(ListUtil.toList(0, ReportStatusEnum.OPERATOR_REJECT.getCode(), ReportStatusEnum.FINANCE_REJECT.getCode(), ReportStatusEnum.ADMIN_REJECT.getCode()));
        } else {
            request.setReportStatusList(ListUtil.toList(form.getReportStatus()));
        }

        if (StringUtils.isNotEmpty(form.getProvinceCode()) || StringUtils.isNotEmpty(form.getCityCode()) || StringUtils.isNotEmpty(form.getRegionCode())) {
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(form.getCityCode());
            eidRequest.setRegionCode(form.getRegionCode());
            eidRequest.setProvinceCode(form.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.queryListByArea(eidRequest);
            List<Long> eids = enterpriseDTOS.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(eids)) {
                request.setEidList(eids);
            } else {
                return Result.failed("需要计算的返利订单为空，请重置筛选项");
            }
        }
        if (ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.notEqual(form.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            if (ObjectUtil.isNull(enterpriseDTO)) {
                return Result.failed("需要计算的返利订单为空，请重置筛选项");
            }
            if (CollUtil.isNotEmpty(request.getEidList()) && !request.getEidList().contains(enterpriseDTO.getId())) {
                return Result.failed("需要计算的返利订单为空，请重置筛选项");
            }
            request.setEidList(ListUtil.toList(enterpriseDTO.getId()));
        }
        if (CollUtil.isEmpty(request.getEidList())) {
            return Result.failed("需要计算的返利订单为空，请重置筛选项");
        }

        AtomicReference<Integer> relationTaskCount = new AtomicReference<>();
        relationTaskCount.set(0);

        request.getEidList().forEach(e -> {
            //校验以岭品修改关系任务
            Boolean isExit = checkReviseRelationTask(e);
            if (isExit){
                Integer i = relationTaskCount.get();
                relationTaskCount.set(++i);
                return;
            }
            CreateReportFlowRequest createReportFlowRequest = new CreateReportFlowRequest();
            createReportFlowRequest.setEid(e);
            createReportFlowRequest.setReportStatusList(request.getReportStatusList());
            createReportFlowRequest.setSoSourceList(request.getSoSourceList());
            createReportFlowRequest.setStartSoTime(request.getStartSoTime());
            createReportFlowRequest.setEndSoTime(DateUtil.endOfDay(request.getEndSoTime()));
            createReportFlowRequest.setIdentificationStatus(request.getIdentificationStatus());
            createReportFlowRequest.setOpUserId(adminInfo.getCurrentUserId());
            //按企业 发送消息 异步生成
            sendMq(Constants.TOPIC_REPORT_REWARD_FLOW_SEND, Constants.TAG_REPORT_REWARD_FLOW_SEND, JSON.toJSONString(createReportFlowRequest));
        });

        if(relationTaskCount.get()==0){
            return Result.success(Boolean.TRUE);
        }
        if(relationTaskCount.get()==1){
            return Result.failed("此企业存正在修改以岭品对应关系，暂不能生成报表，请稍后重试");
        }else {
            return Result.failed("此次生成的报表任务有"+relationTaskCount.get()+"个企业正在修改以岭品关系，暂时不能生成报表，其余企业报表已生成");
        }
    }

    @ApiOperation(value = "修改订单标识(部分)")
    @PostMapping("/updateProportionIdentification")
    public Result<Boolean> updateProportionIdentification(@RequestBody @Valid UpdateProporIdentificaForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        if (CollUtil.isEmpty(form.getIdList())) {
            return Result.success(Boolean.TRUE);
        }
        if (ObjectUtil.equal(ReportOrderIdenEnum.ABNORMAL.getCode(), form.getUpdateIdenStatus())) {
            if (ObjectUtil.isNull(form.getAbnormalReason()) || ObjectUtil.equal(form.getAbnormalReason(), 0)) {
                return Result.failed("请手动填写异常情况");
            }
            if (ObjectUtil.equal(ReportOrderaAnormalReasonEnum.OTHER.getCode(), form.getAbnormalReason()) && StrUtil.isBlank(form.getAbnormalDescribed())) {
                return Result.failed("请手动填写异常原因");
            }
        }
        if (ObjectUtil.equal(ReportTypeEnum.B2B.getCode(), form.getType())) {
            UpdateB2bOrderIdenRequest request = PojoUtils.map(form, UpdateB2bOrderIdenRequest.class);
            request.setOpUserId(adminInfo.getCurrentUserId());
            Boolean isSuccess = reportOrderSyncApi.updateB2bOrderIdentification(request);
            return Result.success(isSuccess);
        }
        if (ObjectUtil.equal(ReportTypeEnum.FLOW.getCode(), form.getType())) {
            UpdateFlowOrderIdenRequest request = PojoUtils.map(form, UpdateFlowOrderIdenRequest.class);
            request.setOpUserId(adminInfo.getCurrentUserId());
            Boolean isSuccess = reportOrderSyncApi.updateFlowOrderIdentification(request);
            return Result.success(isSuccess);
        }
        return Result.failed("订单类型不正确");
    }

    @ApiOperation(value = "修改B2B订单标识(全部)")
    @PostMapping("/updateB2bIdentification")
    public Result<Boolean> updateB2bIdentification(@RequestBody @Valid UpdateB2bOrderIdenForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        if (ObjectUtil.equal(ReportOrderIdenEnum.ABNORMAL.getCode(), form.getUpdateIdenStatus())) {
            if (ObjectUtil.equal(ReportOrderaAnormalReasonEnum.OTHER.getCode(), form.getAbnormalReason()) && StrUtil.isBlank(form.getAbnormalDescribed())) {
                return Result.failed("请手动填写异常原因");
            }
        }
        UpdateB2bOrderIdenRequest request = PojoUtils.map(form, UpdateB2bOrderIdenRequest.class);

        //返利状态
        if (ObjectUtil.isNull(form.getReportStatus()) || ObjectUtil.equal(form.getReportStatus(), -1)) {
            request.setReportStatusList(ListUtil.toList(0, ReportStatusEnum.OPERATOR_REJECT.getCode(), ReportStatusEnum.FINANCE_REJECT.getCode(), ReportStatusEnum.ADMIN_REJECT.getCode()));
        } else {
            request.setReportStatusList(ListUtil.toList(form.getReportStatus()));
        }

        if (!(StringUtils.isEmpty(form.getProvinceCode()) && StringUtils.isEmpty(form.getCityCode()) && StringUtils.isEmpty(form.getRegionCode()))) {
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(form.getCityCode());
            eidRequest.setRegionCode(form.getRegionCode());
            eidRequest.setProvinceCode(form.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.queryListByArea(eidRequest);
            List<Long> eids = enterpriseDTOS.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(eids)) {
                request.setSellerEidList(eids);
            } else {
                return Result.failed("查询到的订单为空，请重置筛选项");
            }
        }
        if (ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.notEqual(form.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            if (ObjectUtil.isNull(enterpriseDTO)) {
                return Result.failed("查询到的订单为空，请重置筛选项");
            }
            if (CollUtil.isNotEmpty(request.getSellerEidList()) && !request.getSellerEidList().contains(enterpriseDTO.getId())) {
                return Result.failed("查询到的订单为空，请重置筛选项");
            }
            request.setSellerEidList(ListUtil.toList(enterpriseDTO.getId()));
        }
        if (CollUtil.isEmpty(request.getSellerEidList())) {
            return Result.failed("请至少选一个企业");
        }
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean isSuccess = reportOrderSyncApi.updateB2bOrderIdentification(request);

        return Result.success(isSuccess);
    }

    @ApiOperation(value = "修改流向订单标识(全部)")
    @PostMapping("/updateFlowIdentification")
    public Result<Boolean> updateFlowIdentification(@RequestBody @Valid UpdateFlowOrderIdenForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        if (ObjectUtil.equal(ReportOrderIdenEnum.ABNORMAL.getCode(), form.getUpdateIdenStatus())) {
            if (ObjectUtil.equal(ReportOrderaAnormalReasonEnum.OTHER.getCode(), form.getAbnormalReason()) && StrUtil.isBlank(form.getAbnormalDescribed())) {
                return Result.failed("请手动填写异常原因");
            }
        }
        UpdateFlowOrderIdenRequest request = PojoUtils.map(form, UpdateFlowOrderIdenRequest.class);

        //返利状态
        if (ObjectUtil.isNull(form.getReportStatus()) || ObjectUtil.equal(form.getReportStatus(), -1)) {
            request.setReportStatusList(ListUtil.toList(0, ReportStatusEnum.OPERATOR_REJECT.getCode(), ReportStatusEnum.FINANCE_REJECT.getCode(), ReportStatusEnum.ADMIN_REJECT.getCode()));
        } else {
            request.setReportStatusList(ListUtil.toList(form.getReportStatus()));
        }

        if (!(StringUtils.isEmpty(form.getProvinceCode()) && StringUtils.isEmpty(form.getCityCode()) && StringUtils.isEmpty(form.getRegionCode()))) {
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(form.getCityCode());
            eidRequest.setRegionCode(form.getRegionCode());
            eidRequest.setProvinceCode(form.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.queryListByArea(eidRequest);
            List<Long> eids = enterpriseDTOS.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(eids)) {
                request.setEidList(eids);
            } else {
                return Result.failed("查询到的订单为空，请重置筛选项");
            }
        }
        if (ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.notEqual(form.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            if (ObjectUtil.isNull(enterpriseDTO)) {
                return Result.failed("查询到的订单为空，请重置筛选项");
            }
            if (CollUtil.isNotEmpty(request.getEidList()) && !request.getEidList().contains(enterpriseDTO.getId())) {
                return Result.failed("查询到的订单为空，请重置筛选项");
            }
            request.setEidList(ListUtil.toList(enterpriseDTO.getId()));
        }
        if (CollUtil.isEmpty(request.getEidList())) {
            return Result.failed("请至少选一个企业");
        }
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean isSuccess = reportOrderSyncApi.updateFlowOrderIdentification(request);
        return Result.success(isSuccess);
    }

    @ApiOperation(value = "导入b2b订单修改订单状态")
    @PostMapping("/importB2bOrderIdent")
    @Log(title = "导入b2b订单修改订单状态", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importB2bOrderIdent(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importOrderIdentHandler.initPar(ReportTypeEnum.B2B);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importOrderIdentHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportOrderIdentForm.class, params, importOrderIdentHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "导入流向订单修改订单状态")
    @PostMapping("/importFlowOrderIdent")
    @Log(title = "导入流向订单修改订单状态", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importFlowOrderIdent(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importOrderIdentHandler.initPar(ReportTypeEnum.FLOW);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importOrderIdentHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportOrderIdentForm.class, params, importOrderIdentHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }


    /**
     * 设置出货价相关
     *
     * @param detailB2b
     * @param priceParamNameList
     * @param categoryDTOMap
     */
    private void setReportDetailB2bPrice(OrderDeliveryReportVO detailB2b, List<ReportPriceParamNameDTO> priceParamNameList, Map<Long, ReportYlGoodsCategoryDTO> categoryDTOMap) {
        ReportYlGoodsCategoryDTO categoryDTO = categoryDTOMap.get(detailB2b.getGoodsId());
        if (Objects.nonNull(categoryDTO)) {
            detailB2b.setCategory(categoryDTO.getName());
        }
        //供货价
        BigDecimal supplyPrice = BigDecimal.ZERO;
        //出货价
        BigDecimal outPrice = BigDecimal.ZERO;
        //商销价
        BigDecimal goodsSellPrice = BigDecimal.ZERO;

        ReportPriceParamNameDTO s = this.getPriceByParam(2L, detailB2b.getGoodsId(), priceParamNameList);
        if (null != s) {
            supplyPrice = NumberUtil.round(s.getPrice(), 2, RoundingMode.HALF_UP);
        }
        ReportPriceParamNameDTO o = this.getPriceByParam(1L, detailB2b.getGoodsId(), priceParamNameList);
        if (null != o) {
            outPrice = NumberUtil.round(o.getPrice(), 2, RoundingMode.HALF_UP);
        }
        ReportPriceParamNameDTO b = this.getPriceByParam(3L, detailB2b.getGoodsId(), priceParamNameList);
        if (ObjectUtil.isNotNull(b)) {
            goodsSellPrice = NumberUtil.round(b.getPrice(), 2, RoundingMode.HALF_UP);
        }
        detailB2b.setSupplyPrice(supplyPrice);
        detailB2b.setOutPrice(outPrice);
        detailB2b.setGoodsSellPrice(goodsSellPrice);
    }


    /**
     * 查询参数价格
     *
     * @param paramId
     * @param goodsId
     * @param priceParamNameList
     * @return
     */
    private ReportPriceParamNameDTO getPriceByParam(Long paramId, Long goodsId, List<ReportPriceParamNameDTO> priceParamNameList) {
        if (CollUtil.isEmpty(priceParamNameList)) {
            return null;
        }
        ReportPriceParamNameDTO supplyParam = priceParamNameList.stream().filter(param -> {
            if (param.getGoodsId().equals(goodsId) && param.getParamId().equals(paramId)) {
                return true;
            }
            return false;
        }).findAny().orElse(null);
        return supplyParam;
    }

    /**
     * 查询阶梯活动信息
     *
     * @param goodsId 商品id
     * @param receiveCount 收货数量
     * @param receiveTime 收货日期
     * @param ladderParList
     * @param sourceEnum 订单来源
     * @return
     */
    private ReportLadderGoodsInfoDTO queryLadderRewardInfo(Long goodsId, Integer receiveCount, Date receiveTime, List<ReportLadderGoodsInfoDTO> ladderParList,ReportParSubGoodsOrderSourceEnum sourceEnum) {
        if (ObjectUtil.isNull(sourceEnum)){
            return null;
        }
        if (receiveCount < 0) {
            receiveCount = receiveCount * -1;
        }
        List<ReportLadderGoodsInfoDTO> infoDTOList = ladderParList.stream().filter(e -> ObjectUtil.equal(goodsId, e.getYlGoodsId()) && DateUtil.compare(e.getStartTime(), receiveTime) <= 0 && DateUtil.compare(e.getEndTime(), receiveTime) >= 0).collect(Collectors.toList());
        infoDTOList=infoDTOList.stream().filter(e->ObjectUtil.equal(e.getOrderSource(),sourceEnum.getCode())||ObjectUtil.equal(e.getOrderSource(),ReportParSubGoodsOrderSourceEnum.ALL.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(infoDTOList)) {
            return null;
        }
        List<ReportLadderGoodsBO> tempRange = ListUtil.toList();

        Integer finalReceiveCount = receiveCount;
        infoDTOList.forEach(e -> {
            //收货数小于阶梯起步数忽略
            if (finalReceiveCount < e.getThresholdCount()) {
                return;
            }
            ReportLadderGoodsBO var = new ReportLadderGoodsBO();
            var.setId(e.getId());
            var.setDifference(finalReceiveCount - e.getThresholdCount());
            tempRange.add(var);
        });
        if (CollUtil.isEmpty(tempRange)) {
            return null;
        }
        ReportLadderGoodsBO goodsBO = tempRange.stream().sorted(Comparator.comparing(ReportLadderGoodsBO::getDifference)).collect(Collectors.toList()).stream().findFirst().get();
        Map<Long, ReportLadderGoodsInfoDTO> map = infoDTOList.stream().collect(Collectors.toMap(ReportLadderGoodsInfoDTO::getId, e -> e));
        return map.get(goodsBO.getId());
    }

    /**
     * 查询活动信息
     *
     * @param goodsId 商品id
     * @param receiveTime 收货日期
     * @param activityParList
     * @param sourceEnum
     * @return
     */
    private Map<Long, ReportParamSubGoodsDTO> queryActivityRewardInfo(Long goodsId, Date receiveTime, List<ReportParamSubGoodsDTO> activityParList,ReportParSubGoodsOrderSourceEnum sourceEnum) {
        if (ObjectUtil.isNull(sourceEnum)){
            return MapUtil.newHashMap();
        }
        Map<Long, ReportParamSubGoodsDTO> result = MapUtil.newHashMap();

        List<ReportParamSubGoodsDTO> infoDTOList = activityParList.stream().filter(e -> ObjectUtil.equal(goodsId, e.getYlGoodsId()) && DateUtil.compare(e.getStartTime(), receiveTime) <= 0 && DateUtil.compare(e.getEndTime(), receiveTime) >= 0).collect(Collectors.toList());
        infoDTOList=infoDTOList.stream().filter(e->ObjectUtil.equal(e.getOrderSource(),sourceEnum.getCode())||ObjectUtil.equal(e.getOrderSource(),ReportParSubGoodsOrderSourceEnum.ALL.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(infoDTOList)) {
            return result;
        }
        result = infoDTOList.stream().collect(Collectors.toMap(ReportParamSubGoodsDTO::getParamSubId, e -> e));
        return result;
    }

    /**
     * 计算返利信息
     *
     * @param detailB2b
     * @param outPrice
     * @param changeDTO
     * @param goodsInSnList
     */
    private void calculateRewardB2b(OrderDeliveryReportVO detailB2b, BigDecimal outPrice, OrderDetailChangeDTO changeDTO, List<String> goodsInSnList) {
        //查询阶梯参数
        QueryGoodsInfoRequest request = new QueryGoodsInfoRequest();
        request.setEid(detailB2b.getSellerEid());
        request.setGoodsInSns(goodsInSnList);
        List<ReportLadderGoodsInfoDTO> ladderParList = reportParamApi.queryLadderInfo(request);
        //查询商品参数
        List<ReportParamSubGoodsDTO> activityParList = reportParamApi.queryActivityGoodsInfo(request);
        BigDecimal subRebate = BigDecimal.ZERO;

        //本发货单收货数量
        Integer receiveQuantity = detailB2b.getReceiveQuantity();
        //计算阶梯奖励
        ReportLadderGoodsInfoDTO ladderRewardInfo = queryLadderRewardInfo(detailB2b.getGoodsId(), changeDTO.getReceiveQuantity(), detailB2b.getOrderCreateTime(), ladderParList,ReportParSubGoodsOrderSourceEnum.B2B);
        if (ObjectUtil.isNotNull(ladderRewardInfo)) {
            detailB2b.setThresholdCount(ladderRewardInfo.getThresholdCount());

            BigDecimal amount = ladderRewardInfo.getRewardAmount();
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(ladderRewardInfo.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比=出货价*收货数*百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(ladderRewardInfo.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }

            detailB2b.setLadderName(ladderRewardInfo.getName());
            detailB2b.setLadderAmount(reward);
            detailB2b.setLadderStartTime(ladderRewardInfo.getStartTime());
            detailB2b.setLadderEndTime(ladderRewardInfo.getEndTime());
            subRebate = subRebate.add(reward);
        }
        Map<Long, ReportParamSubGoodsDTO> activityMap = queryActivityRewardInfo(detailB2b.getGoodsId(), detailB2b.getOrderCreateTime(), activityParList,ReportParSubGoodsOrderSourceEnum.B2B);
        ReportParamSubGoodsDTO xsy = activityMap.get(ReportActivityIdEnum.XSY.getId());
        ReportParamSubGoodsDTO first = activityMap.get(ReportActivityIdEnum.FIRST.getId());
        ReportParamSubGoodsDTO second = activityMap.get(ReportActivityIdEnum.SECOND.getId());
        ReportParamSubGoodsDTO third = activityMap.get(ReportActivityIdEnum.THIRD.getId());
        ReportParamSubGoodsDTO fourth = activityMap.get(ReportActivityIdEnum.FOURTH.getId());
        ReportParamSubGoodsDTO fifth = activityMap.get(ReportActivityIdEnum.FIFTH.getId());
        //计算小三员奖励
        if (ObjectUtil.isNotNull(xsy)) {
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(xsy.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = xsy.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
                detailB2b.setXsyPrice(amount);
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(xsy.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                detailB2b.setXsyPrice(xsy.getRewardPercentage());
            }
            detailB2b.setXsyRewardType(xsy.getRewardType());
            detailB2b.setXsyName(xsy.getActivityName());
            detailB2b.setXsyAmount(reward);
            detailB2b.setXsyStartTime(xsy.getStartTime());
            detailB2b.setXsyEndTime(xsy.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动一奖励
        if (ObjectUtil.isNotNull(first)) {
            detailB2b.setActFirstName(first.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(first.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = first.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(first.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailB2b.setActFirstAmount(reward);
            detailB2b.setActFirstStartTime(first.getStartTime());
            detailB2b.setActFirstEndTime(first.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动二奖励
        if (ObjectUtil.isNotNull(second)) {
            detailB2b.setActSecondName(second.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(second.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = second.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(second.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailB2b.setActSecondAmount(reward);
            detailB2b.setActSecondStartTime(second.getStartTime());
            detailB2b.setActSecondEndTime(second.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动三奖励
        if (ObjectUtil.isNotNull(third)) {
            detailB2b.setActThirdName(third.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(third.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = third.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(third.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailB2b.setActThirdAmount(reward);
            detailB2b.setActThirdStartTime(third.getStartTime());
            detailB2b.setActThirdEndTime(third.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动四奖励
        if (ObjectUtil.isNotNull(fourth)) {
            detailB2b.setActFourthName(fourth.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(fourth.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = fourth.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(fourth.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailB2b.setActFourthAmount(reward);
            detailB2b.setActFourthStartTime(fourth.getStartTime());
            detailB2b.setActFourthEndTime(fourth.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动五奖励
        if (ObjectUtil.isNotNull(fifth)) {
            detailB2b.setActFifthName(fifth.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(fifth.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = fifth.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(fifth.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailB2b.setActFifthAmount(reward);
            detailB2b.setActFifthStartTime(fifth.getStartTime());
            detailB2b.setActFifthEndTime(fifth.getEndTime());
            subRebate = subRebate.add(reward);
        }
    }

    /**
     * 计算流向返利信息
     *
     * @param orderListItem
     * @param outPrice
     * @param ladderParList
     * @param activityParList
     */
    private void calculateFlowReward(FlowOrderRebateReportPageItemVO orderListItem, BigDecimal outPrice, List<ReportLadderGoodsInfoDTO> ladderParList, List<ReportParamSubGoodsDTO> activityParList) {

        //计算阶梯奖励
        ReportLadderGoodsInfoDTO ladderRewardInfo = queryLadderRewardInfo(orderListItem.getYlGoodsId(), orderListItem.getSoQuantity().intValue(), orderListItem.getSoTime(), ladderParList,getFlowSourceByFlowOrderSource(orderListItem.getSoSource()));
        if (ObjectUtil.isNotNull(ladderRewardInfo)) {
            orderListItem.setThresholdCount(ladderRewardInfo.getThresholdCount());

            BigDecimal amount = ladderRewardInfo.getRewardAmount();
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(ladderRewardInfo.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                reward = amount.multiply(orderListItem.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(orderListItem.getSoQuantity()).multiply(ladderRewardInfo.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }

            orderListItem.setLadderStartTime(ladderRewardInfo.getStartTime());
            orderListItem.setLadderEndTime(ladderRewardInfo.getEndTime());
            orderListItem.setLadderAmount(reward);
            orderListItem.setLadderName(ladderRewardInfo.getName());
        }
        Map<Long, ReportParamSubGoodsDTO> activityMap = queryActivityRewardInfo(orderListItem.getYlGoodsId(), orderListItem.getSoTime(), activityParList,getFlowSourceByFlowOrderSource(orderListItem.getSoSource()));
        ReportParamSubGoodsDTO xsy = activityMap.get(ReportActivityIdEnum.XSY.getId());
        ReportParamSubGoodsDTO first = activityMap.get(ReportActivityIdEnum.FIRST.getId());
        ReportParamSubGoodsDTO second = activityMap.get(ReportActivityIdEnum.SECOND.getId());
        ReportParamSubGoodsDTO third = activityMap.get(ReportActivityIdEnum.THIRD.getId());
        ReportParamSubGoodsDTO fourth = activityMap.get(ReportActivityIdEnum.FOURTH.getId());
        ReportParamSubGoodsDTO fifth = activityMap.get(ReportActivityIdEnum.FIFTH.getId());
        //计算小三员奖励
        if (ObjectUtil.isNotNull(xsy)) {
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(xsy.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = xsy.getRewardAmount();
                reward = amount.multiply(orderListItem.getSoQuantity());
                orderListItem.setXsyPrice(amount);
            } else {
                //如果是百分比
                reward = outPrice.multiply(orderListItem.getSoQuantity()).multiply(xsy.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                orderListItem.setXsyPrice(xsy.getRewardPercentage());
            }
            orderListItem.setXsyRewardType(xsy.getRewardType());
            orderListItem.setXsyAmount(reward);
            orderListItem.setXsyName(xsy.getActivityName());
            orderListItem.setXsyStartTime(xsy.getStartTime());
            orderListItem.setXsyEndTime(xsy.getEndTime());
        }
        //计算活动一奖励
        if (ObjectUtil.isNotNull(first)) {
            orderListItem.setActFirstName(first.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(first.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = first.getRewardAmount();
                reward = amount.multiply(orderListItem.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(orderListItem.getSoQuantity()).multiply(first.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            orderListItem.setActFirstAmount(reward);
            orderListItem.setActFirstStartTime(first.getStartTime());
            orderListItem.setActFirstEndTime(first.getEndTime());
        }
        //计算活动二奖励
        if (ObjectUtil.isNotNull(second)) {
            orderListItem.setActSecondName(second.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(second.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = second.getRewardAmount();
                reward = amount.multiply(orderListItem.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(orderListItem.getSoQuantity()).multiply(second.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            orderListItem.setActSecondAmount(reward);
            orderListItem.setActSecondStartTime(second.getStartTime());
            orderListItem.setActSecondEndTime(second.getEndTime());
        }
        //计算活动三奖励
        if (ObjectUtil.isNotNull(third)) {
            orderListItem.setActThirdName(third.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(third.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = third.getRewardAmount();
                reward = amount.multiply(orderListItem.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(orderListItem.getSoQuantity()).multiply(third.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            orderListItem.setActThirdAmount(reward);
            orderListItem.setActThirdStartTime(third.getStartTime());
            orderListItem.setActThirdEndTime(third.getEndTime());
        }
        //计算活动四奖励
        if (ObjectUtil.isNotNull(fourth)) {
            orderListItem.setActFourthName(fourth.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(fourth.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = fourth.getRewardAmount();
                reward = amount.multiply(orderListItem.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(orderListItem.getSoQuantity()).multiply(fourth.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            orderListItem.setActFourthAmount(reward);
            orderListItem.setActFourthStartTime(fourth.getStartTime());
            orderListItem.setActFourthEndTime(fourth.getEndTime());
        }
        //计算活动五奖励
        if (ObjectUtil.isNotNull(fifth)) {
            orderListItem.setActFifthName(fifth.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(fifth.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = fifth.getRewardAmount();
                reward = amount.multiply(orderListItem.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(orderListItem.getSoQuantity()).multiply(fifth.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            orderListItem.setActFifthAmount(reward);
            orderListItem.setActFifthStartTime(fifth.getStartTime());
            orderListItem.setActFifthEndTime(fifth.getEndTime());
        }
    }

    /**
     * 校验报表计算周期
     *
     * @param date1
     * @param date2
     */
    public static void checkTimeSlot(Date date1, Date date2) {
        long between = DateUtil.between(date1, date2, DateUnit.DAY);
        if (between > 90) {
            throw new BusinessException(ReportErrorCode.REPORT_TIME_SLOT_INVALID);
        }
    }

    /**
     * 根据企业id校验是否有正在修改以岭品关系的任务
     *
     * @param eid
     * @return
     */
    public Boolean checkReviseRelationTask(Long eid){
        //校验是否有在修改以岭品关系的任务
        List<FlowGoodsRelationEditTaskDTO> reviseTaskList = flowGoodsRelationEditTaskApi.getListByEid(eid).stream().filter(e->ObjectUtil.equal(e.getSyncStatus(), FlowGoodsRelationEditTaskSyncStatusEnum.WAIT.getCode())).collect(Collectors.toList());
        return CollUtil.isNotEmpty(reviseTaskList);
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = sendPrepare(topic, topicTag, msg);

        mqMessageSendApi.send(mqMessageBO);

        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg) {

        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }

    /**
     * 获取订单来源
     *
     * @param soSource
     * @return
     */
    public ReportParSubGoodsOrderSourceEnum getFlowSourceByFlowOrderSource(String soSource){
        if (ObjectUtil.equal(soSource,"3")){
            return ReportParSubGoodsOrderSourceEnum.OWN_SELF;
        }

        try {
            ArrayList<Integer> list = ListUtil.toList(otherOrderSource);
            if (list.contains(Integer.valueOf(soSource))){
                return ReportParSubGoodsOrderSourceEnum.OTHER;
            }
        } catch (NumberFormatException e) {
            log.error("流向订单来源不正确，来源={}",soSource);
            return null;
        }
        return null;
    }

}