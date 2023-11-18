package com.yiling.hmc.admin.insurance.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import cn.hutool.http.HttpUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.hmc.admin.insurance.form.CreateOrderForm;
import com.yiling.hmc.admin.insurance.form.QueryInsuranceRecordDetailForm;
import com.yiling.hmc.admin.insurance.form.QueryInsuranceRecordPayPageForm;
import com.yiling.hmc.admin.insurance.vo.EnterpriseEmployeeVO;
import com.yiling.hmc.admin.insurance.vo.InsuranceFetchPlanDetailVO;
import com.yiling.hmc.admin.insurance.vo.InsuranceFetchPlanVO;
import com.yiling.hmc.admin.insurance.vo.InsuranceRecordDetailVO;
import com.yiling.hmc.admin.insurance.vo.InsuranceRecordPayHisVO;
import com.yiling.hmc.admin.insurance.vo.InsuranceRecordPayVO;
import com.yiling.hmc.admin.insurance.vo.InsuranceRecordVO;
import com.yiling.hmc.admin.order.vo.OrderVO;
import com.yiling.hmc.insurance.api.BackInsuranceRecordPayApi;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.bo.InsuranceRecordPayBO;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.dto.InsuranceDTO;
import com.yiling.hmc.insurance.dto.InsurancePageDTO;
import com.yiling.hmc.insurance.dto.request.InsurancePageRequest;
import com.yiling.hmc.insurance.dto.request.QueryInsuranceRecordPayPageRequest;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.order.enums.HmcCreateSourceEnum;
import com.yiling.hmc.order.enums.HmcDeliveryTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.enums.HmcOrderTypeEnum;
import com.yiling.hmc.order.enums.HmcPaymentMethodEnum;
import com.yiling.hmc.order.enums.HmcPaymentStatusEnum;
import com.yiling.hmc.order.enums.HmcPrescriptionStatusEnum;
import com.yiling.hmc.wechat.api.InsuranceFetchPlanApi;
import com.yiling.hmc.wechat.api.InsuranceFetchPlanDetailApi;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.hmc.wechat.api.InsuranceRecordPayApi;
import com.yiling.hmc.wechat.api.InsuranceRecordRetreatApi;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDTO;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDetailDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordRetreatDTO;
import com.yiling.hmc.wechat.dto.request.OrderSubmitRequest;
import com.yiling.hmc.wechat.dto.request.QueryInsuranceRecordDetailRequest;
import com.yiling.hmc.wechat.enums.HmcPolicyStatusEnum;
import com.yiling.hmc.wechat.enums.InsuranceFetchStatusEnum;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/4/13
 */
@Slf4j
@Api(tags = "保险销售交易记录")
@RestController
@RequestMapping("/insurance/pay")
public class InsuranceRecordPayController extends BaseController {

    @DubboReference
    BackInsuranceRecordPayApi backInsuranceRecordPayApi;

    @DubboReference
    InsuranceRecordPayApi insuranceRecordPayApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    InsuranceApi insuranceApi;

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @DubboReference
    InsuranceFetchPlanDetailApi insuranceFetchPlanDetailApi;

    @DubboReference
    InsuranceFetchPlanApi insuranceFetchPlanApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;

    @DubboReference
    EmployeeApi employeeApi;

    @DubboReference
    InsuranceFetchPlanApi fetchPlanApi;

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    InsuranceRecordRetreatApi insuranceRecordRetreatApi;

    @ApiOperation(value = "销售员下拉框选择")
    @GetMapping("/queryEmployeeList")
    public Result<CollectionObject<List<EnterpriseEmployeeVO>>> queryEmployeeList(@CurrentUser CurrentStaffInfo staffInfo) {
        List<EnterpriseEmployeeVO> enterpriseEmployeeVOS = Lists.newArrayList();
        CollectionObject<List<EnterpriseEmployeeVO>> listCollectionObject = new CollectionObject(enterpriseEmployeeVOS);
        List<EnterpriseEmployeeDTO> enterpriseEmployeeDTOS = employeeApi.listByEid(staffInfo.getCurrentEid(), EnableStatusEnum.ALL);
        if (CollUtil.isEmpty(enterpriseEmployeeDTOS)) {
            return Result.success(listCollectionObject);
        }
        List<Long> sellerUserIdList = enterpriseEmployeeDTOS.stream().map(EnterpriseEmployeeDTO::getUserId).collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(sellerUserIdList);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity(), (x, y) -> x));
        enterpriseEmployeeDTOS.forEach(enterpriseEmployeeDTO -> {
            EnterpriseEmployeeVO enterpriseEmployeeVO = new EnterpriseEmployeeVO();
            enterpriseEmployeeVO.setSellerUserId(enterpriseEmployeeDTO.getUserId());
            UserDTO userDTO = userDTOMap.get(enterpriseEmployeeDTO.getUserId());
            if (Objects.nonNull(userDTO)) {
                enterpriseEmployeeVO.setSellerUserName(userDTO.getName());
            }
            enterpriseEmployeeVOS.add(enterpriseEmployeeVO);
        });
        return Result.success(listCollectionObject);
    }

    @ApiOperation(value = "保险销售交易记录")
    @GetMapping("/queryPage")
    public Result<Page<InsuranceRecordPayVO>> queryPage(@CurrentUser CurrentStaffInfo staffInfo, QueryInsuranceRecordPayPageForm form) {
        QueryInsuranceRecordPayPageRequest recordPageRequest = new QueryInsuranceRecordPayPageRequest();
        recordPageRequest.setEid(staffInfo.getCurrentEid());
        PojoUtils.map(form, recordPageRequest);
        //按保险名称查询
        if (StrUtil.isNotEmpty(form.getInsuranceName())) {
            InsurancePageRequest request = new InsurancePageRequest();
            request.setInsuranceName(form.getInsuranceName());
            Page<InsurancePageDTO> insurancePageDTOPage = insuranceApi.pageList(request);
            if (insurancePageDTOPage.getTotal() == 0) {
                return Result.success(form.getPage());
            }
            recordPageRequest.setInsuranceIdList(insurancePageDTOPage.getRecords().stream().map(InsurancePageDTO::getId).collect(Collectors.toList()));
        }
        if(Objects.nonNull(form.getSellerUserId())){
            List<Long> userIdList = Lists.newArrayList();
            userIdList.add(form.getSellerUserId());
            recordPageRequest.setSellerUserIds(userIdList);
        }
        Page<InsuranceRecordPayBO> recordPayBOPage = backInsuranceRecordPayApi.queryPage(recordPageRequest);
        if (recordPayBOPage.getTotal() == 0) {
            return Result.success(form.getPage());
        }
        Page<InsuranceRecordPayVO> recordPayVOPage = PojoUtils.map(recordPayBOPage, InsuranceRecordPayVO.class);
        List<InsuranceRecordPayVO> records = recordPayVOPage.getRecords();
        List<Long> sellerUserIdList = records.stream().map(InsuranceRecordPayVO::getSellerUserId).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(sellerUserIdList);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        List<Long> eidList = recordPayBOPage.getRecords().stream().map(InsuranceRecordPayBO::getSellerEid).distinct().collect(Collectors.toList());

        Map<Long, EnterpriseDTO> enterpriseDTOMap = Maps.newHashMap();
        if(CollUtil.isNotEmpty(eidList)){
            eidList = eidList.stream().filter(e->e>0).distinct().collect(Collectors.toList());
            if(CollUtil.isNotEmpty(eidList)){
                enterpriseDTOMap = enterpriseApi.getMapByIds(eidList);
            }
        }
        Map<Long, EnterpriseDTO> finalEnterpriseDTOMap = enterpriseDTOMap;
        records.stream().forEach(insuranceRecordVO -> {
            UserDTO userDTO = userDTOMap.get(insuranceRecordVO.getSellerUserId());
            if (Objects.nonNull(userDTO)) {
                insuranceRecordVO.setSellerUserName(userDTO.getName());
            }
            EnterpriseDTO enterpriseDTO = finalEnterpriseDTOMap.get(insuranceRecordVO.getSellerEid());
            if(Objects.nonNull(enterpriseDTO)){
                insuranceRecordVO.setTerminalName(enterpriseDTO.getName());
            }else{
                insuranceRecordVO.setTerminalName(Constants.SEPARATOR_MIDDLELINE);
            }
        });
        return Result.success(recordPayVOPage);
    }

    /**
     * 保单支付记录详情
     * @param staffInfo
     * @param form
     * @return
     */
    @ApiOperation(value = "保单支付记录详情")
    @GetMapping("/detail")
    public Result<InsuranceRecordDetailVO> detail(@CurrentUser CurrentStaffInfo staffInfo,@Valid QueryInsuranceRecordDetailForm form) {
        QueryInsuranceRecordDetailRequest request = PojoUtils.map(form, QueryInsuranceRecordDetailRequest.class);
        // 0、获取支付记录
        InsuranceRecordPayDTO payDTO = insuranceRecordPayApi.queryById(request.getRecordPayId());
        if (Objects.isNull(payDTO)) {
            return Result.failed("根据参数未获取到保单支付记录");
        }
        // 1、获取参保记录
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getById(payDTO.getInsuranceRecordId());
        // 2、获取拿药计划
        List<InsuranceFetchPlanDTO> fetchPlanDTOList = insuranceFetchPlanApi.getByRecordPayId(payDTO.getId());
        // 3、获取拿药计划详情
        List<InsuranceFetchPlanDetailDTO> fetchPlanDetailDTOList = insuranceFetchPlanDetailApi.getByInsuranceRecordId(payDTO.getInsuranceRecordId());
        if(CollUtil.isNotEmpty(fetchPlanDetailDTOList)){
            fetchPlanDetailDTOList= fetchPlanDetailDTOList.stream().filter(detail -> detail.getRecordPayId().equals(form.getRecordPayId())).collect(Collectors.toList());
        }
        // 4、获取企业信息
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(insuranceRecordDTO.getEid());
        if (Objects.isNull(enterpriseDTO)) {
            log.info("未查询到企业信息，企业id：{}", insuranceRecordDTO.getEid());
            return Result.failed("未查询到企业信息");
        }
        // 5、获取保险信息
        InsuranceDTO insuranceDTO = insuranceApi.queryById(insuranceRecordDTO.getInsuranceId());
        if (Objects.isNull(insuranceDTO)) {
            log.info("未查询到保险信息，保险id：{}", insuranceRecordDTO.getInsuranceId());
            return Result.failed("未查询到保险信息");
        }
        // 6、获取保司信息
        InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyApi.queryById(insuranceDTO.getInsuranceCompanyId());
        if (Objects.isNull(insuranceCompanyDTO)) {
            log.info("未查询到保司信息，保司id：{}", insuranceDTO.getInsuranceCompanyId());
            return Result.failed("未查询到保司信息");
        }
        // 7、获取保单交费明细
        List<InsuranceRecordPayDTO> insuranceRecordPayList = insuranceRecordPayApi.queryByInsuranceRecordId(payDTO.getInsuranceRecordId());
        List<InsuranceRecordPayHisVO> payHisList = PojoUtils.map(insuranceRecordPayList, InsuranceRecordPayHisVO.class);
        InsuranceRecordVO insuranceRecordVO = PojoUtils.map(insuranceRecordDTO, InsuranceRecordVO.class);
        List<InsuranceFetchPlanVO> fetchPlanList = PojoUtils.map(fetchPlanDTOList, InsuranceFetchPlanVO.class);
        boolean cash = true;
        for (InsuranceFetchPlanVO insuranceFetchPlanVO : fetchPlanList) {
            if (staffInfo.getCurrentEid().equals(insuranceRecordDTO.getEid()) && insuranceRecordDTO.getPolicyStatus().equals(HmcPolicyStatusEnum.PROCESSING.getType())) {
                if (insuranceFetchPlanVO.getFetchStatus().equals(InsuranceFetchStatusEnum.WAIT.getType())) {
                    if(cash){
                        insuranceFetchPlanVO.setCashable(true);
                    }else{
                        insuranceFetchPlanVO.setCashable(false);
                    }
                    cash = false;
                }
            } else {
                insuranceFetchPlanVO.setCashable(false);
            }

        }
        List<InsuranceFetchPlanDetailVO> fetchPlanDetailList = PojoUtils.map(fetchPlanDetailDTOList, InsuranceFetchPlanDetailVO.class);
        BigDecimal total = BigDecimal.ZERO;
        for (InsuranceFetchPlanDetailVO planDetail : fetchPlanDetailList) {
            planDetail.setSubTotal(NumberUtil.mul(planDetail.getPerMonthCount(), planDetail.getTerminalSettlePrice()));
            total = NumberUtil.add(total, planDetail.getSubTotal());
        }
        InsuranceRecordDetailVO detailVO = new InsuranceRecordDetailVO();
        detailVO.setTotal(total);
        detailVO.setFetchPlanDetailList(fetchPlanDetailList);
        insuranceRecordVO.setCurrentPayStage(payHisList.size());
        InsuranceRecordRetreatDTO recordRetreatDTO = insuranceRecordRetreatApi.getByInsuranceRecordId(payDTO.getInsuranceRecordId());
        insuranceRecordVO.setIsRetreat(Optional.ofNullable(recordRetreatDTO).isPresent());
        PojoUtils.map(payDTO,insuranceRecordVO);
        insuranceRecordVO.setAmount(BigDecimal.valueOf(payDTO.getAmount()).divide(new BigDecimal(100)).setScale(2));
        // 设置订单来源
        insuranceRecordVO.setHolderCredentialNo(IdcardUtil.hide(insuranceRecordVO.getHolderCredentialNo(),3,14));
        insuranceRecordVO.setIssueCredentialNo(IdcardUtil.hide(insuranceRecordVO.getIssueCredentialNo(),3,14));
        insuranceRecordVO.setOrderSource(enterpriseDTO.getName());
        // 设置保险名称
        insuranceRecordVO.setInsuranceName(insuranceDTO.getInsuranceName());
        // 设置保司名称
        insuranceRecordVO.setCompanyName(insuranceCompanyDTO.getCompanyName());
        // 设置员工编号、员工姓名、线上来源
        if (Objects.nonNull(insuranceRecordDTO.getSellerUserId())) {
            UserDTO userdto = userApi.getById(insuranceRecordDTO.getSellerUserId());
            insuranceRecordVO.setSellerUserName(Optional.ofNullable(userdto).map(UserDTO::getName).orElse(Constants.SEPARATOR_MIDDLELINE));
        }
        // 设置员工所属企业
        if (Objects.nonNull(insuranceRecordDTO.getSellerEid())) {
            EnterpriseDTO sellerEnterprise = enterpriseApi.getById(insuranceRecordDTO.getSellerEid());
            insuranceRecordVO.setTerminalName(Optional.ofNullable(sellerEnterprise).map(EnterpriseDTO::getName).orElse(Constants.SEPARATOR_MIDDLELINE));
        }
        detailVO.setInsuranceRecord(insuranceRecordVO);
        detailVO.setFetchPlanList(fetchPlanList);
        return Result.success(detailVO);
    }


    /**
     * 商家后台-兑付订单
     *
     * @param staffInfo
     * @param form
     * @return
     */
    @ApiOperation(value = "商家后台-兑付订单")
    @PostMapping("/create_order")
    @Log(title = "商家后台-兑付订单", businessType = BusinessTypeEnum.OTHER)
    public Result<OrderVO> createOrder(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody CreateOrderForm form) {

        log.info("[create_order]参数form：{}", form);

        InsuranceRecordPayDTO payDTO = insuranceRecordPayApi.queryById(form.getRecordPayId());
        if (Objects.isNull(payDTO)) {
            log.info("未获取到参保支付记录，参保支付记录id：{}", form.getRecordPayId());
            return Result.failed("未获取到参保支付记录");
        }
        OrderSubmitRequest request = new OrderSubmitRequest();
        request.setInsuranceRecordId(payDTO.getInsuranceRecordId());
        request.setUserId(staffInfo.getCurrentUserId());
        request.setOpUserId(staffInfo.getCurrentUserId());

        // 如果处方图片不为空，则开方状态 已开方
        if (CollUtil.isNotEmpty(form.getPrescriptionSnapshotUrlList())) {
            request.setPrescriptionStatus(HmcPrescriptionStatusEnum.HAVE_PRESCRIPTION);
        }

        request.setHmcOrderStatus(HmcOrderStatusEnum.UN_PAY);
        request.setPaymentStatusEnum(HmcPaymentStatusEnum.UN_PAY);
        request.setPaymentMethodEnum(HmcPaymentMethodEnum.INSURANCE_PAY);
        request.setPaymentTime(request.getOpTime());
        request.setOrderType(HmcOrderTypeEnum.MEDICINE);
        request.setDeliveryType(HmcDeliveryTypeEnum.SELF_PICKUP);
        request.setCreateSource(HmcCreateSourceEnum.ADMIN_HMC);
        request.setDoctor(form.getDoctor());
        request.setInterrogationResult(form.getInterrogationResult());
        request.setPrescriptionSnapshotUrl(String.join(",", form.getPrescriptionSnapshotUrlList()));
        request.setRemark(form.getRemark());
        request.setReceiptDate(new Date());
        request.setOrderReceipts(form.getOrderReceipts());


        // 1、判断是否还有可拿药次数
        InsuranceFetchPlanDTO latestPlan = fetchPlanApi.getLatestPlan(payDTO.getInsuranceRecordId());
        if (Objects.isNull(latestPlan)) {
            log.info("当前保单已经兑付完，参保支付记录id：{}", form.getRecordPayId());
            return Result.failed("当前保单已经兑付完");
        }

        // 2、判断是否有待自提的订单
        OrderDTO unPickUPOrder = orderApi.getUnPickUPOrder(request);
        if (Objects.nonNull(unPickUPOrder)) {
            log.info("当前保单存在待自提订单，订单：{}", JSONUtil.toJsonStr(unPickUPOrder));
            return Result.failed("当前保单存在待自提订单");
        }

        // 3、创建订单
        log.info("[create_order]参数request:{}", request);
        OrderDTO orderDTO = orderApi.createOrder(request);
        log.info("创建订单完成：{}", orderDTO);

        return Result.success(PojoUtils.map(orderDTO, OrderVO.class));
    }
    /**
     * 下载电子保单
     *
     * @param insuranceRecordId
     */
    @ApiOperation(value = "下载电子保单")
    @GetMapping("/downloadPolicyFile")
    @Log(title = "下载电子保单", businessType = BusinessTypeEnum.OTHER)
    public Result<String> downloadPolicyFile(@RequestParam  Long insuranceRecordId) {

        // 获取参保记录
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getById(insuranceRecordId);
        if (Objects.isNull(insuranceRecordDTO)) {
            log.info("根据参数未获取到参保记录");
            return Result.failed("根据参数未获取到参保记录");
        }

        log.info("电子保单下载完成...");
        return Result.success(insuranceRecordApi.uploadPolicyFile(insuranceRecordDTO));
    }


}