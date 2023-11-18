package com.yiling.f2b.admin.flow.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowGoodsBatchApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.api.FlowShopSaleApi;
import com.yiling.dataflow.order.bo.FlowPermissionsBO;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDTO;
import com.yiling.dataflow.order.dto.FlowGoodsRelationDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleListPageRequest;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.f2b.admin.flow.form.QueryFlowEnterpriseListForm;
import com.yiling.f2b.admin.flow.form.QueryFlowGoodsBatchPageForm;
import com.yiling.f2b.admin.flow.form.QueryFlowPurchasePageForm;
import com.yiling.f2b.admin.flow.form.QueryFlowShopListForm;
import com.yiling.f2b.admin.flow.form.QueryFlowShopSalePageForm;
import com.yiling.f2b.admin.flow.vo.FlowEnterpriseInfoVO;
import com.yiling.f2b.admin.flow.vo.FlowEnterpriseTagOptionVO;
import com.yiling.f2b.admin.flow.vo.FlowGoodsBatchPageVO;
import com.yiling.f2b.admin.flow.vo.FlowPurchasePageVO;
import com.yiling.f2b.admin.flow.vo.FlowSalePageVO;
import com.yiling.f2b.admin.flow.vo.FlowShopEnterpriseInfoVO;
import com.yiling.f2b.admin.flow.vo.FlowShopSalePageVO;
import com.yiling.f2b.admin.flow.vo.GoodsInfoVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.goods.ylprice.dto.request.QueryGoodsYilingPriceRequest;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * erp流向
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
@Api(tags = "erp流向模块接口")
@Slf4j
@RestController
@RequestMapping("flow")
public class ErpFlowController extends BaseController {

    @DubboReference(timeout = 60 * 1000)
    FlowGoodsBatchApi flowGoodsBatchApi;
    @DubboReference(timeout = 60 * 1000)
    FlowPurchaseApi flowPurchaseApi;
    @DubboReference(timeout = 60 * 1000)
    FlowSaleApi flowSaleApi;
    @DubboReference(timeout = 60 * 1000)
    EnterpriseApi enterpriseApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference(timeout = 60 * 1000)
    EnterpriseTagApi enterpriseTagApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @DubboReference
    GoodsYilingPriceApi goodsYilingPriceApi;
    @DubboReference
    CustomerContactApi customerContactApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    FlowGoodsRelationApi flowGoodsRelationApi;
    @DubboReference(timeout = 60 * 1000)
    FlowShopSaleApi flowShopSaleApi;

    @ApiOperation(value = "采购流向信息列表分页", httpMethod = "POST")
    @PostMapping("/queryPurchaseListPage")
    public Result<Page<FlowPurchasePageVO>> queryPurchaseListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryFlowPurchasePageForm form) {
        // 查询条件校验
        if (StrUtil.isBlank(form.getEname()) && StrUtil.isBlank(form.getProvinceCode()) && StrUtil.isBlank(form.getCityCode()) && StrUtil.isBlank(form.getRegionCode()) && StrUtil.isBlank(form.getEnterpriseName()) && StrUtil.isBlank(form.getGoodsName()) && ObjectUtil.isNull(form.getSellSpecificationsId()) && StrUtil.isBlank(form.getLicense()) && ObjectUtil.isNull(form.getStartTime()) && ObjectUtil.isNull(form.getEndTime()) && CollUtil.isEmpty(form.getSourceList()) && CollUtil.isEmpty(form.getEnterpriseTagIdList()) && ObjectUtil.isNull(form.getSpecificationIdFlag())) {
            return Result.success(new Page<>());
        }

        if (form.getTimeType().equals(1) && ObjectUtil.isNull(form.getEndTime())) {
            // 选择6个月以前的查询，则只会传开始时间
            Date startTime = form.getStartTime();
            if (startTime == null) {
                return Result.success(new Page<>());
            }
            int year = DateUtil.year(startTime);
            if (year < 2020) {  // 年份不可小于2020
                return Result.success(new Page<>());
            }
            // 判断是否6个月以外的时间
            Date date = DateUtil.offsetMonth(DateUtil.beginOfMonth(new Date()), -5);
            if (!startTime.before(date)) {
                return Result.success(new Page<>());
            }

            Date endTime = DateUtil.endOfMonth(startTime);  // 获取当月结束时间
            form.setEndTime(endTime);
        }

        // 时间范围校验，最多90天
        checkStartAndEndDate(form.getStartTime(), form.getEndTime());

        // 权限校验，获取当前用户负责的公司
        Map<Long, List<EnterpriseDTO>> contactUserMap = contactUserMap(staffInfo);
        List<Long> eidList = new ArrayList<>();
        if (MapUtil.isNotEmpty(contactUserMap) && CollUtil.isNotEmpty(contactUserMap.get(staffInfo.getCurrentUserId())) && contactUserMap.get(staffInfo.getCurrentUserId()).get(0).getId() == -99L) {
            eidList = getEidListByNameProvinceCityRegion(form.getEname(), form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
            if (CollUtil.isEmpty(eidList)) {
                return Result.success(new Page<>());
            }
        } else {
            FlowPermissionsBO flowPermissionsBO = PojoUtils.map(form, FlowPermissionsBO.class);
            eidList = getResponsibleEidList(staffInfo.getCurrentUserId(), contactUserMap, flowPermissionsBO, 2);
            if (CollUtil.isEmpty(eidList)) {
                return Result.success(new Page<>());
            }
        }

        // 企业标签筛选
        eidList = filterEidTagList(eidList, form.getEnterpriseTagIdList());
        if (CollUtil.isEmpty(eidList)) {
            return Result.success(new Page<>());
        }

        QueryFlowPurchaseListPageRequest request = PojoUtils.map(form, QueryFlowPurchaseListPageRequest.class);
        request.setEidList(eidList);
        request.setSpecificationId(form.getSellSpecificationsId());
        if (request.getTimeType().equals(1)) {
            Integer year = DateUtil.year(request.getStartTime()); // 获取年份
            request.setSelectYear(year);
        }

        Page<FlowPurchasePageVO> page = PojoUtils.map(flowPurchaseApi.page(request), FlowPurchasePageVO.class);
        // 总金额
        if (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())) {
            List<FlowPurchasePageVO> records = page.getRecords();
            List<Long> specificationIdList = records.stream().filter(o -> ObjectUtil.isNotNull(o.getSpecificationId()) && o.getSpecificationId().intValue() != 0).map(FlowPurchasePageVO::getSpecificationId).distinct().collect(Collectors.toList());
            // 规格
            Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap = getStandardGoodsSpecification(specificationIdList);
            for (FlowPurchasePageVO record : page.getRecords()) {
                buildFlowPurchasePageVo(record, standardGoodsMap);
            }
        }
        return Result.success(page);
    }

    private void buildFlowPurchasePageVo(FlowPurchasePageVO record, Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap){
        BigDecimal totalAmount = record.getPoQuantity().multiply(record.getPoPrice());
        record.setPoTotalAmount(totalAmount);
        // 规格id
        Long specificationId = record.getSpecificationId();
        if (ObjectUtil.isNotNull(specificationId) && specificationId.intValue() != 0) {
            // 规格
            StandardGoodsSpecificationDTO standardGoodsSpecificationDTO = standardGoodsMap.get(specificationId);
            if (ObjectUtil.isNotNull(standardGoodsSpecificationDTO)) {
                record.setStandardName(standardGoodsSpecificationDTO.getName());
                record.setStandardSellSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
            }
        }
    }


    @ApiOperation(value = "销售流向信息列表分页", httpMethod = "POST")
    @PostMapping("/querySaleListPage")
    public Result<Page<FlowSalePageVO>> querySaleListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryFlowPurchasePageForm form) {
        // 查询条件校验
        if (StrUtil.isBlank(form.getEname()) && StrUtil.isBlank(form.getProvinceCode()) && StrUtil.isBlank(form.getCityCode()) && StrUtil.isBlank(form.getRegionCode()) && StrUtil.isBlank(form.getEnterpriseName()) && StrUtil.isBlank(form.getGoodsName()) && ObjectUtil.isNull(form.getSellSpecificationsId()) && StrUtil.isBlank(form.getLicense()) && ObjectUtil.isNull(form.getStartTime()) && ObjectUtil.isNull(form.getEndTime()) && CollUtil.isEmpty(form.getSourceList()) && CollUtil.isEmpty(form.getEnterpriseTagIdList()) && ObjectUtil.isNull(form.getSpecificationIdFlag())) {
            return Result.success(new Page<>());
        }

        if (form.getTimeType().equals(1) && ObjectUtil.isNull(form.getEndTime())) {
            // 选择6个月以前的查询，则只会传开始时间
            Date startTime = form.getStartTime();
            if (startTime == null) {
                return Result.success(new Page<>());
            }
            int year = DateUtil.year(startTime);
            if (year < 2020) {  // 年份不可小于2020
                return Result.success(new Page<>());
            }
            // 判断是否6个月以外的时间
            Date date = DateUtil.offsetMonth(DateUtil.beginOfMonth(new Date()), -5);
            if (!startTime.before(date)) {
                return Result.success(new Page<>());
            }

            Date endTime = DateUtil.endOfMonth(startTime);  // 获取当月结束时间
            form.setEndTime(endTime);
        }

        // 时间范围校验，最多90天
        checkStartAndEndDate(form.getStartTime(), form.getEndTime());

        // 权限校验，获取当前用户负责的公司
        Map<Long, List<EnterpriseDTO>> contactUserMap = contactUserMap(staffInfo);
        List<Long> eidList = new ArrayList<>();
        if (MapUtil.isNotEmpty(contactUserMap) && CollUtil.isNotEmpty(contactUserMap.get(staffInfo.getCurrentUserId())) && contactUserMap.get(staffInfo.getCurrentUserId()).get(0).getId() == -99L) {
            eidList = getEidListByNameProvinceCityRegion(form.getEname(), form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
            if (CollUtil.isEmpty(eidList)) {
                return Result.success(new Page<>());
            }
        } else {
            FlowPermissionsBO flowPermissionsBO = PojoUtils.map(form, FlowPermissionsBO.class);
            eidList = getResponsibleEidList(staffInfo.getCurrentUserId(), contactUserMap, flowPermissionsBO, 2);
            if (CollUtil.isEmpty(eidList)) {
                return Result.success(new Page<>());
            }
        }

        // 企业标签筛选
        eidList = filterEidTagList(eidList, form.getEnterpriseTagIdList());
        if (CollUtil.isEmpty(eidList)) {
            return Result.success(new Page<>());
        }

        QueryFlowPurchaseListPageRequest request = PojoUtils.map(form, QueryFlowPurchaseListPageRequest.class);
        request.setEidList(eidList);
        request.setSpecificationId(form.getSellSpecificationsId());
        if (request.getTimeType().equals(1)) {
            Integer year = DateUtil.year(request.getStartTime()); // 获取年份
            request.setSelectYear(year);
        }

        Page<FlowSalePageVO> page = PojoUtils.map(flowSaleApi.page(request), FlowSalePageVO.class);
        if (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())) {
            List<FlowSalePageVO> records = page.getRecords();
            List<Long> specificationIdList = records.stream().filter(o -> ObjectUtil.isNotNull(o.getSpecificationId()) && o.getSpecificationId().intValue() != 0)
                    .map(FlowSalePageVO::getSpecificationId).distinct().collect(Collectors.toList());
            // 规格
            Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap = getStandardGoodsSpecification(specificationIdList);
            // 商销价
            List<Date> soTimeList = records.stream().map(FlowSalePageVO::getSoTime).distinct().collect(Collectors.toList());
            Map<Long, List<GoodsYilingPriceDTO>> goodsYilingPriceMap = getGoodsYilingPrice(specificationIdList, soTimeList);
            // 商业标签
            List<Long> eids = records.stream().map(FlowSalePageVO::getEid).distinct().collect(Collectors.toList());
            Map<Long, List<EnterpriseTagDTO>> enterpriseTagMap = enterpriseTagApi.listByEidList(eids);
            // 商务负责人
            Map<Long, List<UserDTO>> enterpriseCustomerContactMap = customerContactApi.listByEidAndCustomerEidList( Constants.YILING_EID, eids);
            // 以岭品映射关系
            List<String> goodsInSnList = records.stream().filter(o -> StrUtil.isNotBlank(o.getGoodsInSn())).map(FlowSalePageVO::getGoodsInSn).distinct().collect(Collectors.toList());
            Map<String, FlowGoodsRelationDTO> flowGoodsRelationMap = getFlowGoodsRelationMap(eids, goodsInSnList);
            for (FlowSalePageVO record : records) {
                buildFlowSalePageVo(record, standardGoodsMap, goodsYilingPriceMap, enterpriseTagMap, enterpriseCustomerContactMap, flowGoodsRelationMap);
            }
        }
        return Result.success(page);
    }

    @ApiOperation(value = "查询总店门店", httpMethod = "POST")
    @PostMapping("/queryFlowShopEnterpriseList")
    public Result<List<FlowShopEnterpriseInfoVO>> queryFlowShopEnterpriseList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryFlowShopListForm form) {
        if (ObjectUtil.isNull(form.getType()) || StrUtil.isBlank(form.getName())) {
            return Result.success(ListUtil.empty());
        }
        // 查询企业信息
        List<EnterpriseDTO> enterpriseList = getShopEnterpriseListByName(form.getType(), form.getName());
        if (CollUtil.isEmpty(enterpriseList)) {
            return Result.success(ListUtil.empty());
        }
        List<FlowShopEnterpriseInfoVO> list = new ArrayList<>();
        FlowShopEnterpriseInfoVO flowEnterprise;
        for (EnterpriseDTO enterpriseDTO : enterpriseList) {
            flowEnterprise = new FlowShopEnterpriseInfoVO();
            flowEnterprise.setEid(enterpriseDTO.getId());
            flowEnterprise.setEname(enterpriseDTO.getName());
            // 与查询名称相同的放在第一个
            if (ObjectUtil.equal(form.getName(), enterpriseDTO.getName())) {
                list.add(0, flowEnterprise);
            } else {
                list.add(flowEnterprise);
            }
        }
        return Result.success(list);
    }

    @ApiOperation(value = "连锁纯销流向信息列表分页", httpMethod = "POST")
    @PostMapping("/queryShopSaleListPage")
    public Result<Page<FlowShopSalePageVO>> queryShopSaleListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryFlowShopSalePageForm form) {
        // 查询条件校验
        if (ObjectUtil.isNull(form.getEid()) && ObjectUtil.isNull(form.getShopEid()) && StrUtil.isBlank(form.getProvinceCode()) && StrUtil.isBlank(form.getCityCode()) && StrUtil.isBlank(form.getRegionCode()) && StrUtil.isBlank(form.getGoodsName()) && StrUtil.isBlank(form.getLicense()) && ObjectUtil.isNull(form.getStartTime()) && ObjectUtil.isNull(form.getEndTime()) && StrUtil.isBlank(form.getManufacturer()) && CollUtil.isEmpty(form.getEnterpriseTagIdList())) {
            return Result.success(new Page<>());
        }

        // 时间范围校验，最多90天
        checkStartAndEndDate(form.getStartTime(), form.getEndTime());

        // 权限校验，获取当前用户负责的公司
        // 数据权限，总店
        // 商业标签，总店
        Map<Long, List<EnterpriseDTO>> contactUserMap = contactUserMap(staffInfo);
        List<Long> eidList;
        if (MapUtil.isNotEmpty(contactUserMap) && CollUtil.isNotEmpty(contactUserMap.get(staffInfo.getCurrentUserId())) && contactUserMap.get(staffInfo.getCurrentUserId()).get(0).getId() == -99L) {
//            List<Integer> inTypeList = new ArrayList<>();
//            inTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
//            inTypeList.add(EnterpriseTypeEnum.CHAIN_BASE.getCode());
//            eidList = getAllBusinessEnterprise(inTypeList);
            // 全部权限，查询所有
            eidList= new ArrayList<>();
        } else {
            eidList = getShopResponsibleEidList(staffInfo.getCurrentUserId(), contactUserMap);
        }
        if (null == eidList) {
            return Result.success(new Page<>());
        }
        // 企业标签筛选，总店
        eidList = filterEidTagListForShopSale(eidList, form.getEnterpriseTagIdList());
        if (null == eidList) {
            return Result.success(new Page<>());
        }
        if (CollUtil.isNotEmpty(eidList) && ObjectUtil.isNotNull(form.getEid())) {
            if (!eidList.contains(form.getEid())) {
                return Result.success(new Page<>());
            }
            eidList.clear();
            eidList.add(form.getEid());
        }
        // 区域查询，门店
        List<Long> shopEidList  = getShopEidListByShopProvinceCityRegion(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
        if (null == shopEidList) {
            return Result.success(new Page<>());
        }
        if (CollUtil.isNotEmpty(shopEidList) && ObjectUtil.isNotNull(form.getShopEid())) {
            if (!shopEidList.contains(form.getShopEid())) {
                return Result.success(new Page<>());
            }
            shopEidList.clear();
            shopEidList.add(form.getShopEid());
        }

        QueryFlowShopSaleListPageRequest request = PojoUtils.map(form, QueryFlowShopSaleListPageRequest.class);
        if (CollUtil.isNotEmpty(eidList)) {
            request.setEidList(eidList);
        }
        if (CollUtil.isNotEmpty(shopEidList)) {
            request.setShopEidList(shopEidList);
        }
        request.setSpecificationId(form.getSellSpecificationsId());

        Page<FlowShopSalePageVO> page = PojoUtils.map(flowShopSaleApi.page(request), FlowShopSalePageVO.class);
        if (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())) {
            List<FlowShopSalePageVO> records = page.getRecords();
            for (FlowShopSalePageVO record : records) {
                buildFlowShopSalePageVo(record);
            }
        }
        return Result.success(page);
    }

    /**
     * 根据企业id、商品内码获取以岭品映射关系
     *
     * @param eids 企业id列表
     * @param goodsInSnList 商品内码列表
     * @return
     */
    private Map<String, FlowGoodsRelationDTO> getFlowGoodsRelationMap(List<Long> eids, List<String> goodsInSnList) {
        Map<String, FlowGoodsRelationDTO> flowGoodsRelationMap = new HashMap<>();
        if (CollUtil.isEmpty(eids) || CollUtil.isEmpty(goodsInSnList)) {
            return flowGoodsRelationMap;
        }
        List<String> goodsInSnTrimList = new ArrayList<>();
        goodsInSnList.forEach(o -> {goodsInSnTrimList.add(o.trim());});
        // 以岭品关系
        List<FlowGoodsRelationDTO> flowGoodsRelationList = flowGoodsRelationApi.getByEidAndGoodsInSn(eids, goodsInSnTrimList);
        if (CollUtil.isNotEmpty(flowGoodsRelationList)) {
            flowGoodsRelationMap = flowGoodsRelationList.stream().collect(Collectors.toMap(o -> o.getEid() + "_" + o.getGoodsInSn(), o -> o, (k1, k2) -> k2));
        }
        return flowGoodsRelationMap;
    }

    @ApiOperation(value = "查询商品规格")
    @GetMapping("/queryGoodsSpecification")
    public Result<CollectionObject<List<GoodsInfoVO>>> queryGoodsSpecification(@RequestParam @Valid @NotEmpty String key) {
        // 商销价
        List<GoodsInfoVO> list = new ArrayList<>();
        // 标准库规格
        StandardSpecificationPageRequest specificationRequest = new StandardSpecificationPageRequest();
        specificationRequest.setSize(100);
        specificationRequest.setStandardGoodsName(key);
        Page<StandardGoodsSpecificationDTO> specificationPage = standardGoodsSpecificationApi.getSpecificationPageByGoods(specificationRequest);
        if (ObjectUtil.isNull(specificationPage) || CollUtil.isEmpty(specificationPage.getRecords())) {
            return Result.success(new CollectionObject(list));
        }
        for (StandardGoodsSpecificationDTO standardGoodsSpecificationDTO : specificationPage.getRecords()) {
            GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
            goodsInfoVO.setName(standardGoodsSpecificationDTO.getName());
            goodsInfoVO.setSellSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
            goodsInfoVO.setSellSpecificationsId(standardGoodsSpecificationDTO.getId());
            goodsInfoVO.setManufacturer(standardGoodsSpecificationDTO.getManufacturer());
            goodsInfoVO.setSpecifications("");
            goodsInfoVO.setId(0L);
            list.add(goodsInfoVO);
        }
        // 以岭品
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setSize(200);
        request.setName(key);
        request.setEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        Page<GoodsListItemBO> ylGoodsPage = goodsApi.queryPageListGoods(request);
        if (ObjectUtil.isNull(ylGoodsPage) || CollUtil.isEmpty(ylGoodsPage.getRecords())) {
            return Result.success(new CollectionObject(list));
        }
        Map<Long, GoodsListItemBO> ylGoodsMap = ylGoodsPage.getRecords().stream().collect(Collectors.toMap(GoodsListItemBO::getSellSpecificationsId, Function.identity(), (v1, v2) -> v1));
        for (GoodsInfoVO goodsInfoVO : list) {
            GoodsListItemBO goodsListItemBO = ylGoodsMap.get(goodsInfoVO.getSellSpecificationsId());
            if (ObjectUtil.isNotNull(goodsListItemBO)) {
                goodsInfoVO.setId(goodsListItemBO.getId());
            }
        }

        CollectionObject<List<GoodsInfoVO>> result = new CollectionObject(list);
        return Result.success(result);
    }

    private void buildFlowShopSalePageVo(FlowShopSalePageVO record) {
        // 总金额
        BigDecimal totalAmount = record.getSoQuantity().multiply(record.getSoPrice());
        record.setSoTotalAmount(totalAmount);
        // 销售日期
        Date soTime1 = record.getSoTime();
        if(ObjectUtil.isNotNull(soTime1)){
            record.setSoTimeStr(DateUtil.format(soTime1, "yyyy-MM-dd"));
        }
    }

    private void buildFlowSalePageVo(FlowSalePageVO record, Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap, Map<Long, List<GoodsYilingPriceDTO>> goodsYilingPriceMap,
                                     Map<Long, List<EnterpriseTagDTO>> enterpriseTagMap, Map<Long, List<UserDTO>> enterpriseCustomerContactMap, Map<String, FlowGoodsRelationDTO> flowGoodsRelationMap) {
        // 总金额
        BigDecimal totalAmount = record.getSoQuantity().multiply(record.getSoPrice());
        record.setSoTotalAmount(totalAmount);
        // 销售日期
        Date soTime1 = record.getSoTime();
        if(ObjectUtil.isNotNull(soTime1)){
            record.setSoTimeStr(DateUtil.format(soTime1, "yyyy-MM-dd"));
        }
        // 以岭品id
        Long ylGoodsId = 0L;
        String ylGoodsIdStr = "";
        FlowGoodsRelationDTO flowGoodsRelationDTO = flowGoodsRelationMap.get(record.getEid() + "_" + record.getGoodsInSn().trim());
        if (ObjectUtil.isNotNull(flowGoodsRelationDTO)) {
            ylGoodsId = flowGoodsRelationDTO.getYlGoodsId();
            ylGoodsIdStr = ylGoodsId.toString();
        }
        record.setYlGoodsId(ylGoodsId);
        record.setYlGoodsIdStr(ylGoodsIdStr);
        // 规格id
        Long specificationId = record.getSpecificationId();
        if (ObjectUtil.isNotNull(specificationId) && specificationId.intValue() != 0) {
            // 规格
            StandardGoodsSpecificationDTO standardGoodsSpecificationDTO = standardGoodsMap.get(specificationId);
            if (ObjectUtil.isNotNull(standardGoodsSpecificationDTO)) {
                record.setStandardName(standardGoodsSpecificationDTO.getName());
                record.setStandardSellSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
            }
            // 商销价
            List<GoodsYilingPriceDTO> goodsYilingPriceDTOS = goodsYilingPriceMap.get(specificationId);
            if (CollUtil.isNotEmpty(goodsYilingPriceDTOS)) {
                for (GoodsYilingPriceDTO goodsYilingPriceDTO : goodsYilingPriceDTOS) {
                    long soTime = record.getSoTime().getTime();
                    if (soTime >= goodsYilingPriceDTO.getStartTime().getTime() && soTime <= goodsYilingPriceDTO.getEndTime().getTime()) {
                        record.setYlPrice(goodsYilingPriceDTO.getPrice());
                        BigDecimal totalYlPrice = record.getSoQuantity().multiply(goodsYilingPriceDTO.getPrice());
                        record.setTotalYlPrice(totalYlPrice);
                    }
                }
            }
        }
        // 商业标签
        List<EnterpriseTagDTO> enterpriseTagDTOS = enterpriseTagMap.get(record.getEid());
        if (CollUtil.isNotEmpty(enterpriseTagDTOS)) {
            List<String> tagNameList = enterpriseTagDTOS.stream().map(EnterpriseTagDTO::getName).distinct().collect(Collectors.toList());
            record.setTagNames(String.join(",", tagNameList));
        }
        // 商务负责人
        List<UserDTO> enterpriseCustomerContactList = enterpriseCustomerContactMap.get(record.getEid());
        if (CollUtil.isNotEmpty(enterpriseCustomerContactList)) {
            List<String> contactUserNameList = enterpriseCustomerContactList.stream().map(UserDTO::getName).distinct().collect(Collectors.toList());
            record.setCustomerContact(String.join(",", contactUserNameList));
        }
    }

    private Map<Long, List<GoodsYilingPriceDTO>> getGoodsYilingPrice(List<Long> specificationIdList, List<Date> soTimeList) {
        Map<Long, List<GoodsYilingPriceDTO>> goodsYilingPriceMap = new HashMap<>();
        if (CollUtil.isEmpty(specificationIdList) || CollUtil.isEmpty(soTimeList)) {
            return goodsYilingPriceMap;
        }
        QueryGoodsYilingPriceRequest goodsYilingPriceRequest = new QueryGoodsYilingPriceRequest();
        goodsYilingPriceRequest.setSpecificationIdList(specificationIdList);
        goodsYilingPriceRequest.setTimeList(soTimeList);
        List<GoodsYilingPriceDTO> goodsYilingPriceList = goodsYilingPriceApi.listBySpecificationIdAndDate(goodsYilingPriceRequest);
        if (CollUtil.isNotEmpty(goodsYilingPriceList)) {
            goodsYilingPriceMap = goodsYilingPriceList.stream().collect(Collectors.groupingBy(GoodsYilingPriceDTO::getSpecificationId));
        }
        return goodsYilingPriceMap;
    }

    private Map<Long, StandardGoodsSpecificationDTO> getStandardGoodsSpecification(List<Long> specificationIdList) {
        Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap = new HashMap<>();
        if (CollUtil.isEmpty(specificationIdList)) {
            return standardGoodsMap;
        }
        List<StandardGoodsSpecificationDTO> standardGoodsList = standardGoodsSpecificationApi.getListStandardGoodsSpecificationByIds(specificationIdList);
        if (CollUtil.isNotEmpty(standardGoodsList)) {
            standardGoodsMap = standardGoodsList.stream().collect(Collectors.toMap(StandardGoodsSpecificationDTO::getId, Function.identity(), (k1, k2) -> k2));
        }
        return standardGoodsMap;
    }

    @ApiOperation(value = "库存流向信息列表分页", httpMethod = "POST")
    @PostMapping("/queryGoodsBatchListPage")
    public Result<Page<FlowGoodsBatchPageVO>> queryGoodsBatchListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryFlowGoodsBatchPageForm form) {
        // 查询条件校验
        if (StrUtil.isBlank(form.getEname()) && StrUtil.isBlank(form.getProvinceCode()) && StrUtil.isBlank(form.getCityCode()) && StrUtil.isBlank(form.getRegionCode()) && StrUtil.isBlank(form.getGoodsName()) && ObjectUtil.isNull(form.getSellSpecificationsId()) && StrUtil.isBlank(form.getLicense()) && CollUtil.isEmpty(form.getSourceList()) && CollUtil.isEmpty(form.getEnterpriseTagIdList()) && ObjectUtil.isNull(form.getSpecificationIdFlag())) {
            return Result.success(new Page<>());
        }
        // 权限校验，获取当前用户负责的公司
        Map<Long, List<EnterpriseDTO>> contactUserMap = contactUserMap(staffInfo);
        List<Long> eidList = new ArrayList<>();
        if (MapUtil.isNotEmpty(contactUserMap) && CollUtil.isNotEmpty(contactUserMap.get(staffInfo.getCurrentUserId())) && contactUserMap.get(staffInfo.getCurrentUserId()).get(0).getId() == -99L) {
            eidList = getEidListByNameProvinceCityRegion(form.getEname(), form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
            if (CollUtil.isEmpty(eidList)) {
                return Result.success(new Page<>());
            }
        } else {
            FlowPermissionsBO flowPermissionsBO = PojoUtils.map(form, FlowPermissionsBO.class);
            eidList = getResponsibleEidList(staffInfo.getCurrentUserId(), contactUserMap, flowPermissionsBO, 2);
            if (CollUtil.isEmpty(eidList)) {
                return Result.success(new Page<>());
            }
        }

        // 企业标签筛选
        eidList = filterEidTagList(eidList, form.getEnterpriseTagIdList());
        if (CollUtil.isEmpty(eidList)) {
            return Result.success(new Page<>());
        }

        QueryFlowGoodsBatchListPageRequest request = PojoUtils.map(form, QueryFlowGoodsBatchListPageRequest.class);
        request.setEidList(eidList);
        request.setSpecificationId(form.getSellSpecificationsId());
        Page<FlowGoodsBatchDTO> tempPage = flowGoodsBatchApi.page(request);
        if(ObjectUtil.isNull(tempPage) || CollUtil.isEmpty(tempPage.getRecords())){
            return Result.success(new Page<>());
        }
        List<FlowGoodsBatchPageVO> list = new ArrayList<>();
        List<FlowGoodsBatchDTO> records = tempPage.getRecords();
        List<Long> specificationIdList = records.stream().filter(o -> ObjectUtil.isNotNull(o.getSpecificationId()) && o.getSpecificationId().intValue() != 0).map(FlowGoodsBatchDTO::getSpecificationId).distinct().collect(Collectors.toList());
        // 规格
        Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap = getStandardGoodsSpecification(specificationIdList);
        for (FlowGoodsBatchDTO dto : records) {
            buildFlowGoodsBatchPageVo(list, dto, standardGoodsMap);
        }

        Page<FlowGoodsBatchPageVO> page = new Page<>();
        page.setSize(tempPage.getSize());
        page.setCurrent(tempPage.getCurrent());
        page.setTotal(tempPage.getTotal());
        page.setRecords(list);
        return Result.success(page);
    }

    private void buildFlowGoodsBatchPageVo(List<FlowGoodsBatchPageVO> list, FlowGoodsBatchDTO dto, Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap){
        String gbProduceTime = dto.getGbProduceTime();
        String gbEndTime = dto.getGbEndTime();
        dto.setGbProduceTime(null);
        dto.setGbEndTime(null);
        FlowGoodsBatchPageVO vo = PojoUtils.map(dto, FlowGoodsBatchPageVO.class);
        Date gbProduceTimeDate = StrUtil.isBlank(gbProduceTime) ? null : DateUtil.parse(gbProduceTime);
        Date gbEndTimeDate = StrUtil.isBlank(gbEndTime) ? null : DateUtil.parse(gbEndTime);
        vo.setGbProduceTime(gbProduceTimeDate);
        vo.setGbEndTime(gbEndTimeDate);
        // 规格id
        Long specificationId = dto.getSpecificationId();
        if (ObjectUtil.isNotNull(specificationId) && specificationId.intValue() != 0) {
            // 规格
            StandardGoodsSpecificationDTO standardGoodsSpecificationDTO = standardGoodsMap.get(specificationId);
            if (ObjectUtil.isNotNull(standardGoodsSpecificationDTO)) {
                vo.setStandardName(standardGoodsSpecificationDTO.getName());
                vo.setStandardSellSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
            }
        }
        list.add(vo);
    }

    @ApiOperation(value = "查询商业公司", httpMethod = "POST")
    @PostMapping("/queryFlowEnterpriseList")
    public Result<List<FlowEnterpriseInfoVO>> queryFlowEnterpriseList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryFlowEnterpriseListForm form) {
        // 权限校验，获取当前用户负责的公司
        Map<Long, List<EnterpriseDTO>> contactUserMap = contactUserMap(staffInfo);
        List<Long> eidList = new ArrayList<>();
        if (MapUtil.isNotEmpty(contactUserMap) && CollUtil.isNotEmpty(contactUserMap.get(staffInfo.getCurrentUserId())) && contactUserMap.get(staffInfo.getCurrentUserId()).get(0).getId() == -99L) {
            eidList = new ArrayList() {{
                add(-99L);
            }};
        } else {
            FlowPermissionsBO flowPermissionsBO = PojoUtils.map(form, FlowPermissionsBO.class);
            eidList = getResponsibleEidList(staffInfo.getCurrentUserId(), contactUserMap, flowPermissionsBO, 1);
            if (CollUtil.isEmpty(eidList)) {
                return Result.success(ListUtil.empty());
            }
        }
        // 查询企业信息
        List<EnterpriseDTO> enterpriseList = null;
        if (CollUtil.isEmpty(eidList) || eidList.get(0) == -99L) {
            if (StrUtil.isNotBlank(form.getEname())) {
                enterpriseList = getEnterpriseListByName(form.getEname());
            }
        } else {
            enterpriseList = enterpriseApi.listByIds(eidList);
        }

        if (CollUtil.isEmpty(enterpriseList)) {
            return Result.success(ListUtil.empty());
        }
        List<FlowEnterpriseInfoVO> list = new ArrayList<>();
        FlowEnterpriseInfoVO flowEnterprise;
        for (EnterpriseDTO enterpriseDTO : enterpriseList) {
            flowEnterprise = new FlowEnterpriseInfoVO();
            flowEnterprise.setEid(enterpriseDTO.getId());
            flowEnterprise.setEname(enterpriseDTO.getName());
            list.add(flowEnterprise);
        }
        return Result.success(list);
    }

    @ApiOperation(value = "获取企业标签选择项列表")
    @GetMapping("/getEnterpriseTagList")
    public Result<CollectionObject<FlowEnterpriseTagOptionVO>> getEnterpriseTagList() {
        List<EnterpriseTagDTO> list = enterpriseTagApi.listAll(EnableStatusEnum.ENABLED);
        List<FlowEnterpriseTagOptionVO> voList = PojoUtils.map(list, FlowEnterpriseTagOptionVO.class);
        return Result.success(new CollectionObject<>(voList));
    }

    private List<EnterpriseDTO> getEnterpriseListByIds(List<Long> eids) {
        if (CollUtil.isEmpty(eids)) {
            return ListUtil.empty();
        }
        List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(eids);
        if (CollUtil.isEmpty(enterpriseDTOS)) {
            return ListUtil.empty();
        }
        return enterpriseDTOS;
    }

    private List<Long> getShopEidListByShopProvinceCityRegion(String provinceCode, String cityCode, String regionCode) {
        if (StrUtil.isNotBlank(provinceCode) || StrUtil.isNotBlank(cityCode) || StrUtil.isNotBlank(regionCode)) {
            List<Integer> inTypeList = new ArrayList<>();
            inTypeList.add(EnterpriseTypeEnum.CHAIN_DIRECT.getCode());
            inTypeList.add(EnterpriseTypeEnum.CHAIN_JOIN.getCode());

            QueryEnterpriseListRequest eRequest = new QueryEnterpriseListRequest();
            eRequest.setProvinceCode(provinceCode);
            eRequest.setCityCode(cityCode);
            eRequest.setRegionCode(regionCode);
            eRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            eRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
            eRequest.setTypeList(inTypeList);
            List<EnterpriseDTO> enterpriseRegionList = enterpriseApi.queryListByArea(eRequest);
            if (CollUtil.isEmpty(enterpriseRegionList)) {
                return null;
            }
            return enterpriseRegionList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        }
        return ListUtil.empty();
    }

    /**
     * 企业标签筛选
     *
     * @param eidList 企业id列表
     * @param enterpriseTagIdList 企业标签id列表
     * @return
     */
    private List<Long> filterEidTagListForShopSale(List<Long> eidList, List<Long> enterpriseTagIdList) {
        if (null == eidList) {
            return null;
        }
        if (CollUtil.isEmpty(enterpriseTagIdList)) {
            return eidList;
        }
        // 根据标签idl列表 查询企业id列表
        List<Long> eidTagList = enterpriseTagApi.getEidListByTagIdList(enterpriseTagIdList);
        if (CollUtil.isEmpty(eidTagList)) {
            return null;
        }

        if (CollUtil.isNotEmpty(eidList)) {
            eidList = eidList.stream().filter(eid -> eidTagList.contains(eid)).distinct().collect(Collectors.toList());
            if (CollUtil.isEmpty(eidList)) {
                return null;
            }
            return eidList;
        }
        return eidTagList;
    }

    /**
     * 企业标签筛选
     *
     * @param eidList 企业id列表
     * @param enterpriseTagIdList 企业标签id列表
     * @return
     */
    private List<Long> filterEidTagList(List<Long> eidList, List<Long> enterpriseTagIdList) {
        if (CollUtil.isEmpty(eidList)) {
            return ListUtil.empty();
        }
        if (CollUtil.isEmpty(enterpriseTagIdList)) {
            return eidList;
        }
        // 根据标签idl列表 查询企业id列表
        List<Long> eidTagList = enterpriseTagApi.getEidListByTagIdList(enterpriseTagIdList);
        if (CollUtil.isEmpty(eidTagList)) {
            return ListUtil.empty();
        }
        return eidList.stream().filter(eid -> eidTagList.contains(eid)).distinct().collect(Collectors.toList());
    }

    /**
     * 查询日期范围校验，最多93天（3个月）
     *
     * @param startTime
     * @param endTime
     */
    private void checkStartAndEndDate(Date startTime, Date endTime) {
        DateTime startDate = DateUtil.beginOfDay(startTime);
        DateTime endDate = DateUtil.endOfDay(endTime);
        long offset = DateUtil.betweenDay(startDate, endDate, false) + 1;
        if (offset > 93) {
            throw new BusinessException(ResultCode.FAILED, "开始日期、结束日期，时间范围不能大于90天");
        }
    }

    /**
     * 校验用户权限
     *
     * @param staffInfo
     * @return Map<Long ,   List < EnterpriseDTO>>，
     */
    private Map<Long, List<EnterpriseDTO>> contactUserMap(CurrentStaffInfo staffInfo) {
        Long currentUserId = staffInfo.getCurrentUserId();
        // 当前用户权限
        List<Long> userIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, Constants.YILING_EID, staffInfo.getCurrentUserId());
        if (CollUtil.isNotEmpty(userIdList) && !userIdList.contains(currentUserId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        Map<Long, List<EnterpriseDTO>> contactUserMap = new HashMap<>();
        if (CollUtil.isEmpty(userIdList)) {
            EnterpriseDTO enterpriseDTO = new EnterpriseDTO();
            enterpriseDTO.setId(-99L);
            contactUserMap.put(currentUserId, new ArrayList() {{
                add(enterpriseDTO);
            }});
            return contactUserMap;
        }
        // 当前用户负责的企业
        contactUserMap = enterpriseApi.listByContactUserIds(Constants.YILING_EID, userIdList);
        return contactUserMap;
    }

    /**
     * 获取负责的公司
     *
     * @param currentUserId
     * @param contactUserMap
     * @return
     */
    public List<Long> getShopResponsibleEidList(Long currentUserId, Map<Long, List<EnterpriseDTO>> contactUserMap) {
        // 用户没有负责企业
        if (MapUtil.isEmpty(contactUserMap) || !contactUserMap.keySet().contains(currentUserId) || CollUtil.isEmpty(contactUserMap.get(currentUserId))) {
            return null;
        }
        // 有负责企业
        List<EnterpriseDTO> enterpriseList = contactUserMap.get(currentUserId);
        if (CollUtil.isEmpty(enterpriseList)) {
            return null;
        } else {
            return enterpriseList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        }
    }

    /**
     * 获取负责的公司
     *
     * @param currentUserId
     * @param contactUserMap
     * @param flowPermissionsBO
     * @param type 1-模糊查询 2-精确匹配
     * @return
     */
    public List<Long> getResponsibleEidList(Long currentUserId, Map<Long, List<EnterpriseDTO>> contactUserMap, FlowPermissionsBO flowPermissionsBO, int type) {
        // 用户没有负责企业
        if (MapUtil.isEmpty(contactUserMap) || !contactUserMap.keySet().contains(currentUserId) || CollUtil.isEmpty(contactUserMap.get(currentUserId))) {
            return ListUtil.empty();
        }

        // 有负责企业
        String ename = flowPermissionsBO.getEname();
        String provinceCode = flowPermissionsBO.getProvinceCode();
        String cityCode = flowPermissionsBO.getCityCode();
        String regionCode = flowPermissionsBO.getRegionCode();
        List<EnterpriseDTO> enterpriseList = contactUserMap.get(currentUserId);

        // 无商业公司相关的搜索条件，返回所有负责的公司id
        if (StrUtil.isBlank(ename) && StrUtil.isBlank(provinceCode) && StrUtil.isBlank(cityCode) && StrUtil.isBlank(regionCode)) {
            return enterpriseList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
        }

        // 有商业公司相关的搜索条件，进行条件过滤
        if (StrUtil.isNotBlank(ename)) {
            if (type == 1) {
                enterpriseList = enterpriseList.stream().filter(e -> e.getName().contains(ename)).collect(Collectors.toList());
            } else if (type == 2) {
                enterpriseList = enterpriseList.stream().filter(e -> ObjectUtil.equal(ename, e.getName())).collect(Collectors.toList());
            }
        }
        if (StrUtil.isNotBlank(provinceCode)) {
            enterpriseList = enterpriseList.stream().filter(e -> ObjectUtil.equal(provinceCode, e.getProvinceCode())).collect(Collectors.toList());
        }
        if (StrUtil.isNotBlank(cityCode)) {
            enterpriseList = enterpriseList.stream().filter(e -> ObjectUtil.equal(cityCode, e.getCityCode())).collect(Collectors.toList());
        }
        if (StrUtil.isNotBlank(regionCode)) {
            enterpriseList = enterpriseList.stream().filter(e -> ObjectUtil.equal(regionCode, e.getRegionCode())).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(enterpriseList)) {
            return ListUtil.empty();
        } else {
            return enterpriseList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
        }
    }

    private List<EnterpriseDTO> getShopEnterpriseListByName(int type, String name) {
        List<Integer> typeList = new ArrayList<>();
        if (type == 1) {
            // 总店企业类型，商业、连锁总店
            typeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
            typeList.add(EnterpriseTypeEnum.CHAIN_BASE.getCode());
        } else if (type == 2) {
            // 门店企业类型，连锁直营、连锁加盟
            typeList.add(EnterpriseTypeEnum.CHAIN_DIRECT.getCode());
            typeList.add(EnterpriseTypeEnum.CHAIN_JOIN.getCode());
        }

        QueryEnterpriseByNameRequest request = new QueryEnterpriseByNameRequest();
        request.setName(name);
        request.setTypeList(typeList);
        return enterpriseApi.getEnterpriseListByName(request);
    }

    private List<EnterpriseDTO> getEnterpriseListByName(String name) {
        QueryEnterpriseByNameRequest request = new QueryEnterpriseByNameRequest();
        request.setName(name);
        request.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
        return enterpriseApi.getEnterpriseListByName(request);
    }

    private List<Long> getEidListByNameProvinceCityRegion(String name, String provinceCode, String cityCode, String regionCode) {
        List<EnterpriseDTO> enterpriseList = new ArrayList<>();
        List<EnterpriseDTO> enterpriseNameList = null;
        List<EnterpriseDTO> enterpriseRegionList = null;
        boolean nameFlag = false;
        boolean regionFlag = false;

        List<Integer> inTypeList = new ArrayList<>();
        inTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());

        if (StrUtil.isNotBlank(name)) {
            nameFlag = true;
            enterpriseNameList = getEnterpriseListByName(name);
        }
        if (StrUtil.isNotBlank(provinceCode) || StrUtil.isNotBlank(cityCode) || StrUtil.isNotBlank(regionCode)) {
            regionFlag = true;
            QueryEnterpriseListRequest eRequest = new QueryEnterpriseListRequest();
            eRequest.setProvinceCode(provinceCode);
            eRequest.setCityCode(cityCode);
            eRequest.setRegionCode(regionCode);
            eRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            eRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
            eRequest.setTypeList(inTypeList);
            enterpriseRegionList = enterpriseApi.queryListByArea(eRequest);
        }

        if (!nameFlag && !regionFlag) {
            // 查询所有POP商业类型的企业信息
            return getAllBusinessEnterprise(inTypeList);
        } else if (nameFlag && regionFlag) {
            if (CollUtil.isNotEmpty(enterpriseNameList) && CollUtil.isNotEmpty(enterpriseRegionList)) {
                List<Long> eidListTemp = enterpriseRegionList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
                enterpriseList = enterpriseNameList.stream().filter(o -> eidListTemp.contains(o.getId())).collect(Collectors.toList());
            }
        } else {
            if (CollUtil.isNotEmpty(enterpriseNameList)) {
                enterpriseList.addAll(enterpriseNameList);
            }
            if (CollUtil.isNotEmpty(enterpriseRegionList)) {
                enterpriseList.addAll(enterpriseRegionList);
            }
        }

        if (CollUtil.isNotEmpty(enterpriseList)) {
            return enterpriseList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        }
        return ListUtil.empty();
    }

    private List<Long> getAllBusinessEnterprise(List<Integer> inTypeList) {
        List<Long> eidList = new ArrayList<>();
        Page<EnterpriseDTO> enterpriseDTOPage;
        int current = 1;
        int size = 500;
        do {
            QueryEnterprisePageListRequest enterprisePageListRequest = new QueryEnterprisePageListRequest();
            enterprisePageListRequest.setCurrent(current);
            enterprisePageListRequest.setSize(size);
            enterprisePageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            enterprisePageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
            enterprisePageListRequest.setInTypeList(inTypeList);

            enterpriseDTOPage = enterpriseApi.pageList(enterprisePageListRequest);
            if (ObjectUtil.isNull(enterpriseDTOPage) || CollUtil.isEmpty(enterpriseDTOPage.getRecords())) {
                break;
            }
            List<EnterpriseDTO> records = enterpriseDTOPage.getRecords();
            for (EnterpriseDTO record : records) {
                eidList.add(record.getId());
            }
            current = current + 1;
        } while (enterpriseDTOPage != null && CollectionUtils.isNotEmpty(enterpriseDTOPage.getRecords()));
        return eidList;
    }

}
