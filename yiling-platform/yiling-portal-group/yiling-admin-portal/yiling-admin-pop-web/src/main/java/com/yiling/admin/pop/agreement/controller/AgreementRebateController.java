package com.yiling.admin.pop.agreement.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.pop.agreement.form.QueryApplyPageListForm;
import com.yiling.admin.pop.agreement.form.QueryFinanceUseDetailPageListForm;
import com.yiling.admin.pop.agreement.form.QueryFinanceUseListPageListForm;
import com.yiling.admin.pop.agreement.form.QueryFinancialRebateEntPageListForm;
import com.yiling.admin.pop.agreement.form.QueryRebateApplyDetailPageListForm;
import com.yiling.admin.pop.agreement.vo.FinancialRebateEntPageListItemVO;
import com.yiling.admin.pop.agreement.vo.RebateApplyDetailPageListItemVO;
import com.yiling.admin.pop.agreement.vo.RebateApplyOrderDetailPageListItemVO;
import com.yiling.admin.pop.agreement.vo.RebateApplyPageListItemVO;
import com.yiling.admin.pop.agreement.vo.RebateApplyPageVO;
import com.yiling.admin.pop.agreement.vo.UseDetailPageListItemVO;
import com.yiling.admin.pop.agreement.vo.UseDetailPageVO;
import com.yiling.admin.pop.agreement.vo.UsePageListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.api.AgreementApplyApi;
import com.yiling.user.agreement.api.AgreementApplyDetailApi;
import com.yiling.user.agreement.api.AgreementRebateLogApi;
import com.yiling.user.agreement.api.AgreementRebateOrderApi;
import com.yiling.user.agreement.api.AgreementRebateOrderDetailApi;
import com.yiling.user.agreement.api.ApplyOrderApi;
import com.yiling.user.agreement.api.UseApi;
import com.yiling.user.agreement.api.UseDetailApi;
import com.yiling.user.agreement.dto.AgreementApplyDetailDTO;
import com.yiling.user.agreement.dto.AgreementRebateApplyDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.ApplyOrderDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.dto.UseDTO;
import com.yiling.user.agreement.dto.UseDetailDTO;
import com.yiling.user.agreement.dto.request.PageListByIdRequest;
import com.yiling.user.agreement.dto.request.QueryApplyOrderPageListRequest;
import com.yiling.user.agreement.dto.request.QueryApplyPageListRequest;
import com.yiling.user.agreement.dto.request.QueryRebateApplyDetailPageListRequest;
import com.yiling.user.agreement.dto.request.QueryUseDetailListPageRequest;
import com.yiling.user.agreement.dto.request.QueryUseListPageRequest;
import com.yiling.user.agreement.dto.request.RebateApplyPageListItemDTO;
import com.yiling.user.agreement.enums.AgreementApplyStatusEnum;
import com.yiling.user.agreement.enums.ApplyDetailTypeEnum;
import com.yiling.user.agreement.enums.RebateOrderTypeEnum;
import com.yiling.user.agreement.util.AgreementUtils;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerEasDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListRequest;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/8/18
 */
@Api(tags = "返利模块")
@RestController
@RequestMapping("/rebate")
@Slf4j
public class AgreementRebateController {

    @DubboReference
    EnterpriseApi                 enterpriseApi;
    @DubboReference
    AgreementApi                  agreementApi;
    @DubboReference
    AgreementRebateLogApi         agreementRebateLogApi;
    @DubboReference
    CustomerApi                   customerApi;
    @DubboReference
    AgreementRebateOrderApi       agreementRebateOrderApi;
    @DubboReference
    AgreementRebateOrderDetailApi agreementRebateOrderDetailApi;
    @DubboReference
    OrderApi                      orderApi;
    @DubboReference
    UserApi                       userApi;
    @DubboReference
    GoodsApi                      goodsApi;
    @DubboReference
    AgreementApplyApi             rebateApplyApi;
    @DubboReference
    AgreementApplyDetailApi       rebateApplyDetailApi;
    @DubboReference
    ApplyOrderApi                 applyOrderApi;
    @DubboReference
    UseApi                        useApi;
    @DubboReference
    UseDetailApi                  useDetailApi;
    @DubboReference
    OrderDetailApi                orderDetailApi;
    @DubboReference
	DataPermissionsApi dataPermissionsApi;

    @ApiOperation("企业返利入账申请-运营")
    @PostMapping("/queryApplyPageList")
    public Result<Page<RebateApplyPageListItemVO>> queryApplyPageList(@RequestBody @Valid QueryApplyPageListForm form,
                                                                      @CurrentUser CurrentStaffInfo staffInfo) {
        Page<RebateApplyPageListItemVO> result;

        QueryApplyPageListRequest request = PojoUtils.map(form, QueryApplyPageListRequest.class);
//        //如果是商务查询---由于b2b_v1.0新增了数据权限此参数作废
//        Integer queryType = 1;
//        if (queryType.equals(form.getQueryType())) {
//            request.setCreateUser(staffInfo.getCurrentUserId());
//        }
		//查询数据权限
		List<Long> authorizedUserIds = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.ADMIN, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());

		//设置数据权限
		request.setCreateUserList(authorizedUserIds);
        Page<RebateApplyPageListItemDTO> page = rebateApplyApi.queryRebateApplyPageList(request);
        result = PojoUtils.map(page, RebateApplyPageListItemVO.class);

        if (CollUtil.isEmpty(result.getRecords())) {
            return Result.success(result);
        }
        //创建人id
        List<Long> userId = result.getRecords().stream().map(RebateApplyPageListItemVO::getCreateUser).collect(Collectors.toList());
        List<Long> eidList = result.getRecords().stream().map(RebateApplyPageListItemVO::getEid).collect(Collectors.toList());
        Map<Long, UserDTO> userDTOMap = userApi.listByIds(userId).stream().collect(Collectors.toMap(UserDTO::getId, e -> e));
        Map<Long, EnterpriseDTO> entMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
        //补全操作人
        result.getRecords().forEach(e -> {
            e.setTotalAmount(e.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            e.setCreateUserName(userDTOMap.get(e.getCreateUser()).getName());
            EnterpriseDTO enterpriseDTO = entMap.get(e.getEid());
            if (enterpriseDTO != null) {
                EnterpriseDTO enterprise = entMap.get(e.getEid());
                e.setName(enterprise.getName());
                e.setEid(enterprise.getId());
                e.setChannelId(enterprise.getChannelId());
                e.setEntStatus(enterpriseDTO.getStatus());
            }
        });
        return Result.success(result);
    }

    @ApiOperation("已申请入账返利信息")
    @PostMapping("/queryRebateApplyPageList")
    public Result<RebateApplyPageVO<RebateApplyDetailPageListItemVO>> queryRebateApplyPageList(@RequestBody @Valid QueryRebateApplyDetailPageListForm form) {
        RebateApplyPageVO<RebateApplyDetailPageListItemVO> result = new RebateApplyPageVO<>();
        //分页列表
        List<RebateApplyDetailPageListItemVO> applyDetailList = ListUtil.toList();
        //查询返利申请
        List<AgreementRebateApplyDTO> rebateApplyList = rebateApplyApi.queryRebateApplyList(ListUtil.toList(form.getApplyId()));
        if (CollUtil.isEmpty(rebateApplyList)) {
            return Result.success(result);
        }
        AgreementRebateApplyDTO rebateApplyDTO = rebateApplyList.get(0);
        //查询企业信息
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(rebateApplyDTO.getEid());
        result = PojoUtils.map(enterpriseDTO, RebateApplyPageVO.class);

        //分页查询申请明细或
        QueryRebateApplyDetailPageListRequest pageRequest = PojoUtils.map(form, QueryRebateApplyDetailPageListRequest.class);
        Page<AgreementApplyDetailDTO> page = rebateApplyDetailApi.queryRebateApplyDetailPageList(pageRequest);
        PojoUtils.map(page, result);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(result);
        }
        result.setApplyStatus(rebateApplyDTO.getStatus());
        result.setCode(rebateApplyDTO.getCode());
        result.setEasCode(rebateApplyDTO.getEasCode());
        result.setRecords(PojoUtils.map(page.getRecords(), RebateApplyDetailPageListItemVO.class));
        result.setTotalAmount(rebateApplyDTO.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        result.setCreateTime(rebateApplyDTO.getCreateTime());
        //补全数据
        //查询协议
        List<Long> agreementIdList = page.getRecords().stream().filter(e -> ApplyDetailTypeEnum.AGREEMENT.getCode().equals(e.getDetailType()))
            .map(AgreementApplyDetailDTO::getAgreementId).collect(Collectors.toList());
        Map<Long, SupplementAgreementDetailDTO> agreementMap = agreementApi.querySupplementAgreementsDetailList(agreementIdList).stream()
            .collect(Collectors.toMap(SupplementAgreementDetailDTO::getId, e -> e));
        result.getRecords().forEach(e -> {
            if (ApplyDetailTypeEnum.AGREEMENT.getCode().equals(e.getDetailType())) {
                SupplementAgreementDetailDTO agreementDTO = agreementMap.get(e.getAgreementId());
                e.setContent(AgreementUtils.getAgreementText(agreementDTO));
                e.setName(agreementDTO.getName());
            }

        });
        return Result.success(result);
    }

    @ApiOperation("已申请入账返利信息-协议订单商品明细")
    @PostMapping("/queryRebateApplyOrderDetailPageList")
    public Result<RebateApplyPageVO<RebateApplyOrderDetailPageListItemVO>> queryRebateApplyOrderDetailPageList(@RequestBody @Valid QueryRebateApplyDetailPageListForm form) {
        RebateApplyPageVO<RebateApplyOrderDetailPageListItemVO> result =new RebateApplyPageVO<>();
        //分页列表
        List<RebateApplyOrderDetailPageListItemVO> applyOrderDetailList = ListUtil.toList();
        //查询返利申请
        List<AgreementRebateApplyDTO> rebateApplyList = rebateApplyApi.queryRebateApplyList(ListUtil.toList(form.getApplyId()));
        if (CollUtil.isEmpty(rebateApplyList)) {
            return Result.failed("申请单不存在");
        }
        AgreementRebateApplyDTO rebateApplyDTO = rebateApplyList.get(0);
        //查询企业信息
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(rebateApplyDTO.getEid());
        if(ObjectUtil.isNull(enterpriseDTO)){
            return Result.success(result);
        }
        result = PojoUtils.map(enterpriseDTO, RebateApplyPageVO.class);

        QueryApplyOrderPageListRequest pageListRequest = PojoUtils.map(form, QueryApplyOrderPageListRequest.class);

        List<ApplyOrderDTO> applyDetailDTOS = applyOrderApi.queryApplyOrderList(ListUtil.toList(form.getApplyId()));
        result.setApplyStatus(rebateApplyDTO.getStatus());
        result.setCode(rebateApplyDTO.getCode());
        result.setEasCode(rebateApplyDTO.getEasCode());
        result.setTotalAmount(rebateApplyDTO.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        if (CollUtil.isEmpty(applyDetailDTOS)) {
            return Result.success(result);
        }
        List<Long> rebateOrderDetailId = ListUtil.toList();
        applyDetailDTOS.forEach(e -> {
            long[] ids = StrUtil.splitToLong(e.getRebateOrderDetailId(), ",");
            rebateOrderDetailId.addAll(Arrays.stream(ids).boxed().collect(Collectors.toList()));
        });
        PageListByIdRequest request = PojoUtils.map(form, PageListByIdRequest.class);
        request.setIdList(rebateOrderDetailId);
        Page<AgreementRebateOrderDetailDTO> page = agreementRebateOrderDetailApi.pageListById(request);
        PojoUtils.map(page, result);
        result.setCreateTime(rebateApplyDTO.getCreateTime());
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(result);
        }
        //补全数据
        List<Long> agreementIdList = page.getRecords().stream().map(AgreementRebateOrderDetailDTO::getAgreementId).collect(Collectors.toList());
        //查询协议
        Map<Long, SupplementAgreementDetailDTO> agreementMap = agreementApi.querySupplementAgreementsDetailList(agreementIdList).stream()
            .collect(Collectors.toMap(SupplementAgreementDetailDTO::getId, e -> e));

        page.getRecords().forEach(e -> {
            SupplementAgreementDetailDTO agreementDTO = agreementMap.get(e.getAgreementId());
            OrderDetailDTO orderDetail = orderDetailApi.getOrderDetailById(e.getOrderDetailId());
            RebateApplyOrderDetailPageListItemVO vo = PojoUtils.map(agreementDTO, RebateApplyOrderDetailPageListItemVO.class);
            vo.setContent(AgreementUtils.getAgreementText(agreementDTO));
            vo.setOrderCode(orderDetail.getOrderNo());
            vo.setGoodsId(e.getGoodsId());
            vo.setGoodsName(orderDetail.getGoodsName());
            vo.setErpCode(orderDetail.getGoodsErpCode());
            vo.setOrderType(e.getType());
            vo.setGoodsQuantity(e.getGoodsQuantity());
            vo.setPrice(e.getGoodsAmount());
            BigDecimal amount = e.getDiscountAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
            //如果是退款单转为负数
            if (RebateOrderTypeEnum.REFUND.getCode().equals(e.getType())) {
                vo.setDiscountAmount(amount.negate());
            } else {
                vo.setDiscountAmount(amount);
            }
            applyOrderDetailList.add(vo);
        });
        result.setRecords(applyOrderDetailList);
        return Result.success(result);
    }

    @ApiOperation(value = "企业返利对账")
    @PostMapping("/queryFinancialRebateEntPageList")
    public Result<Page<FinancialRebateEntPageListItemVO>> queryFinancialRebateEntPageList(@CurrentUser CurrentStaffInfo staffInfo,
                                                                                          @RequestBody @Valid QueryFinancialRebateEntPageListForm form) {
        //返回结果
        Page<FinancialRebateEntPageListItemVO> result;

        //分页查询企业账号
        QueryCustomerEasInfoPageListRequest request = PojoUtils.map(form, QueryCustomerEasInfoPageListRequest.class);
        request.setEid(Constants.YILING_EID);
        Page<EnterpriseCustomerEasDTO> page = customerApi.queryCustomerEasInfoPageList(request);
        result = PojoUtils.map(page, FinancialRebateEntPageListItemVO.class);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(result);
        }
        List<Long> eidList = page.getRecords().stream().map(EnterpriseCustomerEasDTO::getCustomerEid).distinct().collect(Collectors.toList());
        //查询企业信息
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
        //查询企业返利申请
        Map<Long, List<AgreementRebateApplyDTO>> applyMap = rebateApplyApi.queryRebateApplyListByEid(eidList, AgreementApplyStatusEnum.SUCCESS);
        //查询申请返利使用
        Map<Long, List<UseDTO>> useMap = useApi.queryUseListByEid(eidList);
        //组装数据
        result.getRecords().forEach(e -> {
            PojoUtils.map(enterpriseMap.get(e.getCustomerEid()), e);
            BigDecimal totalApply = BigDecimal.ZERO;
            BigDecimal totalUse = BigDecimal.ZERO;
            //返利申请
            List<AgreementRebateApplyDTO> applyList = applyMap.get(e.getCustomerEid());
            if (CollUtil.isNotEmpty(applyList)) {
                applyList = applyMap.get(e.getCustomerEid()).stream().filter(apply -> apply.getEasCode().equals(e.getEasCode()))
                    .collect(Collectors.toList());
                for (AgreementRebateApplyDTO item : applyList) {
                    totalApply = totalApply.add(item.getTotalAmount());
                }
                e.setDiscountCount(applyList.size());
            }
            //返利使用申请
            List<UseDTO> useList = useMap.get(e.getCustomerEid());
            if (CollUtil.isNotEmpty(useList)) {
                useList = useList.stream().filter(use -> use.getEasCode().equals(e.getEasCode())).collect(Collectors.toList());
                for (UseDTO item : useList) {
                    totalUse = totalUse.add(item.getTotalAmount());
                }
                e.setUsedCount(useList.size());
            }
            e.setDiscountAmount(totalApply);
            e.setUsedAmount(totalUse);
        });
        return Result.success(result);
    }

    @ApiOperation(value = "已兑付返利列表-运营")
    @PostMapping("/queryFinanceApplyListPageList")
    public Result<RebateApplyPageVO<RebateApplyPageListItemVO>> queryFinanceApplyListPageList(@RequestBody @Valid QueryFinanceUseListPageListForm form,
                                                                                              @CurrentUser CurrentStaffInfo staffInfo) {
        RebateApplyPageVO<RebateApplyPageListItemVO> result = new RebateApplyPageVO<>();

        //查询企业信息
        EnterpriseDTO enterprise = enterpriseApi.getById(form.getEid());
        if(ObjectUtil.isNull(enterprise)){
            return Result.success(result);
        }
        result = PojoUtils.map(enterprise, RebateApplyPageVO.class);
        //查询返利申请
        QueryApplyPageListRequest request = PojoUtils.map(form, QueryApplyPageListRequest.class);
        Page<RebateApplyPageListItemDTO> page = rebateApplyApi.queryRebateApplyPageList(request);
        List<AgreementRebateApplyDTO> applyList = rebateApplyApi
            .queryRebateApplyListByEid(ListUtil.toList(form.getEid()), AgreementApplyStatusEnum.SUCCESS).get(form.getEid());
        //计算返利总额
        if (CollUtil.isNotEmpty(applyList)) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (AgreementRebateApplyDTO item : applyList) {
                totalAmount = totalAmount.add(item.getTotalAmount());
            }
            result.setTotalAmount(totalAmount);
        }

        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(result);
        }
        List<RebateApplyPageListItemVO> list = PojoUtils.map(page.getRecords(), RebateApplyPageListItemVO.class);
        result.setRecords(list);

        //创建人id
        List<Long> userId = result.getRecords().stream().map(RebateApplyPageListItemVO::getCreateUser).collect(Collectors.toList());
        List<Long> eidList = result.getRecords().stream().map(RebateApplyPageListItemVO::getEid).collect(Collectors.toList());
        Map<Long, UserDTO> userDTOMap = userApi.listByIds(userId).stream().collect(Collectors.toMap(UserDTO::getId, e -> e));
        Map<Long, EnterpriseDTO> entMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
        //补全操作人
        result.getRecords().forEach(e -> {
            e.setCreateUserName(userDTOMap.get(e.getCreateUser()).getName());
            EnterpriseDTO enterpriseDTO = entMap.get(e.getEid());
            e.setName(enterpriseDTO.getName());
            e.setChannelId(enterpriseDTO.getChannelId());
            e.setEntStatus(enterpriseDTO.getStatus());
        });
        return Result.success(result);
    }

    /*@ApiOperation(value = "已兑付返利信息-协议订单商品明细")
    @PostMapping("/queryRebateOrderDetailPageList")
    public Result<RebateOrderDetailPageListVO<RebateOrderDetailPageListItemVO>> queryRebateOrderDetailPageList(@CurrentUser CurrentStaffInfo staffInfo,
                                                                                                               @RequestBody @Valid QueryRebateOrderDetailPageListForm pageListForm) {
        //查询统计数
        AgreementOrderStatisticalDTO statisticalInfo = agreementRebateOrderApi.statisticsOrderCount(pageListForm.getAgreementId());
        RebateOrderDetailPageListVO<RebateOrderDetailPageListItemVO> result = PojoUtils.map(statisticalInfo, RebateOrderDetailPageListVO.class);
        //查询账号下的总兑付金额
        Map<String, BigDecimal> discountAmountMap = agreementRebateLogApi.queryEntAccountDiscountAmount(ListUtil.toList(pageListForm.getAccount()),
            null);
        result.setDiscountAmount(discountAmountMap.getOrDefault(pageListForm.getAccount(), BigDecimal.ZERO));
        //查询协议名称
        SupplementAgreementDetailDTO agreementDetailInfo = agreementApi.querySupplementAgreementsDetail(pageListForm.getAgreementId());
        result.setName(agreementDetailInfo.getName());
        result.setCategory(agreementDetailInfo.getCategory());
    
        //查询订单记录
        QueryRebateOrderPageListRequest pageListRequest = PojoUtils.map(pageListForm, QueryRebateOrderPageListRequest.class);
        Page<AgreementRebateOrderDetailDTO> page = agreementRebateOrderDetailApi.queryRebateOrderPageList(pageListRequest,
            AgreementRebateOrderConditionStatusEnum.getByCode(pageListForm.getConditionStatus()),
            AgreementRebateOrderCashStatusEnum.getByCode(pageListForm.getCashStatus()));
    
        PojoUtils.map(page, result);
        List<RebateOrderDetailPageListItemVO> records = PojoUtils.map(page.getRecords(), RebateOrderDetailPageListItemVO.class);
        result.setRecords(records);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(result);
        }
    
        //订单id列表
        List<Long> orderIdList = page.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getOrderId()))
            .map(AgreementRebateOrderDetailDTO::getOrderId).distinct().collect(Collectors.toList());
        List<OrderDTO> orderList = orderApi.listByIds(orderIdList);
        Map<Long, OrderDTO> orderMap = new HashMap<>(8);
        if (CollUtil.isNotEmpty(orderList)) {
            orderMap = orderApi.listByIds(orderIdList).stream().filter(o -> ObjectUtil.isNotNull(o.getId()))
                .collect(Collectors.toMap(OrderDTO::getId, e -> e));
        }
    
        //商品id列表
        List<Long> goodsIdList = page.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getGoodsId()))
            .map(AgreementRebateOrderDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
        //查询商品信息
        Map<Long, GoodsInfoDTO> goodsInfoMap = new HashMap<>(8);
        if (CollUtil.isNotEmpty(goodsIdList)) {
            goodsInfoMap = goodsApi.batchQueryInfo(goodsIdList).stream().filter(o -> ObjectUtil.isNotNull(o.getId()))
                .collect(Collectors.toMap(GoodsInfoDTO::getId, e -> e));
        }
    
        Map<Long, OrderDTO> finalOrderMap = orderMap;
        Map<Long, GoodsInfoDTO> finalGoodsInfoMap = goodsInfoMap;
        result.getRecords().forEach(e -> {
            //补全列表订单信息
            if (ObjectUtil.isNotNull(finalOrderMap.get(e.getOrderId()))) {
                PojoUtils.map(finalOrderMap.get(e.getOrderId()), e);
            }
            //补全订单列表商品信息
            GoodsInfoDTO goodsInfoDto = finalGoodsInfoMap.get(e.getGoodsId());
            if (ObjectUtil.isNotNull(goodsInfoDto)) {
                e.setGoodsName(goodsInfoDto.getName());
                e.setGoodsLicenseNo(goodsInfoDto.getLicenseNo());
                e.setGoodsSpecification(goodsInfoDto.getSellSpecifications());
            }
        });
        return Result.success(result);
    }*/

    @ApiOperation(value = "已使用返利列表-运营")
    @PostMapping("/queryFinanceUseListPageList")
    public Result<RebateApplyPageVO<UsePageListItemVO>> queryFinanceUseListPageList(@RequestBody @Valid QueryFinanceUseListPageListForm form,
                                                                                    @CurrentUser CurrentStaffInfo staffInfo) {
        RebateApplyPageVO<UsePageListItemVO> result = new RebateApplyPageVO<>();
        EnterpriseDTO enterprise = enterpriseApi.getById(form.getEid());
        if(ObjectUtil.isNull(enterprise)){
            return Result.success(result);
        }
        result = PojoUtils.map(enterprise, RebateApplyPageVO.class);

        QueryUseListPageRequest request = PojoUtils.map(form, QueryUseListPageRequest.class);
        request.setEidList(ListUtil.toList(form.getEid()));
        request.setEasCode(form.getEasCode());
        //查询使用情况
        Page<UseDTO> page = useApi.queryUseListPageList(request);
        if(CollUtil.isEmpty(page.getRecords())){
            return Result.success(result);
        }
        List<UsePageListItemVO> list = PojoUtils.map(page.getRecords(), UsePageListItemVO.class);
        PojoUtils.map(page, result);
        result.setRecords(list);
        return Result.success(result);
    }

    @ApiOperation(value = "已使用返利列表-申请明细")
    @PostMapping("/queryUseDetailListPageList")
    public Result<UseDetailPageVO<UseDetailPageListItemVO>> queryFinanceUseDetailPageList(@RequestBody @Valid QueryFinanceUseDetailPageListForm form,
                                                                                          @CurrentUser CurrentStaffInfo staffInfo) {
        UseDetailPageVO<UseDetailPageListItemVO> result;
        //查询使用申请单
        UseDTO useDTO = useApi.queryById(form.getUseId());
        if (ObjectUtil.isNull(useDTO)) {
            return Result.failed("申请单不存在");
        }
        //查询企业信息
        List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(ListUtil.toList(useDTO.getEid()));
        EnterpriseDTO enterpriseDTO = enterpriseDTOS.get(0);
        result = PojoUtils.map(enterpriseDTO, UseDetailPageVO.class);
        result.setEntStatus(enterpriseDTO.getStatus());
        PojoUtils.map(useDTO, result);
        result.setName(enterpriseDTO.getName());
        QueryUseDetailListPageRequest request = new QueryUseDetailListPageRequest();
        request.setUseIdList(ListUtil.toList(form.getUseId()));
        Page<UseDetailDTO> page = useDetailApi.queryUseDetailListPageList(request);
        PojoUtils.map(page, result);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<UseDetailPageListItemVO> records = PojoUtils.map(page.getRecords(), UseDetailPageListItemVO.class);
            result.setRecords(records);
        }
        return Result.success(result);
    }

}
