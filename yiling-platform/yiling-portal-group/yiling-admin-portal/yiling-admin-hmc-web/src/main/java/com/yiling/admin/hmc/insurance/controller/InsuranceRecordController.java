package com.yiling.admin.hmc.insurance.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.hmc.insurance.form.QueryCashPageForm;
import com.yiling.admin.hmc.insurance.form.QueryInsuranceRecordDetailForm;
import com.yiling.admin.hmc.insurance.form.QueryInsuranceRecordPageForm;
import com.yiling.admin.hmc.insurance.vo.*;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.YlStrUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.hmc.insurance.api.BackInsuranceRecordApi;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.bo.InsuranceRecordBO;
import com.yiling.hmc.insurance.bo.InsuranceRecordRetreatBO;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.dto.InsuranceDTO;
import com.yiling.hmc.insurance.dto.InsurancePageDTO;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyPageRequest;
import com.yiling.hmc.insurance.dto.request.InsurancePageRequest;
import com.yiling.hmc.insurance.dto.request.QueryBackInsuranceRecordPageRequest;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.bo.OrderBO;
import com.yiling.hmc.order.dto.request.QueryCashPageRequest;
import com.yiling.hmc.order.enums.HmcHolderTypeEnum;
import com.yiling.hmc.order.enums.HmcSourceTypeEnum;
import com.yiling.hmc.wechat.api.*;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDTO;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDetailDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayDTO;
import com.yiling.hmc.wechat.dto.request.QueryInsuranceRecordDetailRequest;
import com.yiling.hmc.wechat.enums.InsuranceFetchStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentUserInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.system.enums.UserStatusEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: gxl
 * @date: 2022/4/11
 */
@Api(tags = "保险销售记录")
@RestController
@RequestMapping("/insurance/record")
@Slf4j
public class InsuranceRecordController extends BaseController {

    @DubboReference
    BackInsuranceRecordApi backInsuranceRecordApi;
    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    InsuranceApi insuranceApi;

    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    InsuranceRecordRetreatApi insuranceRecordRetreatApi;

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @DubboReference
    InsuranceFetchPlanApi insuranceFetchPlanApi;

    @DubboReference
    InsuranceFetchPlanDetailApi insuranceFetchPlanDetailApi;

    @DubboReference
    InsuranceRecordPayApi insuranceRecordPayApi;

    @ApiOperation(value = "保险提供商下拉列表")
    @GetMapping("/companyList")
    public Result<CollectionObject<List<InsuranceCompanyListVO>>> companyList() {
        InsuranceCompanyPageRequest request = new InsuranceCompanyPageRequest();
        request.setSize(50);
        Page<InsuranceCompanyDTO> dtoPage = insuranceCompanyApi.pageList(request);
        Page<InsuranceCompanyListVO> voPage = PojoUtils.map(dtoPage, InsuranceCompanyListVO.class);
        CollectionObject<List<InsuranceCompanyListVO>> listCollectionObject = new CollectionObject(voPage.getRecords());
        return Result.success(listCollectionObject);
    }

    @GetMapping("queryPage")
    @ApiOperation(value = "按保单查")
    public Result<Page<InsuranceRecordVO>> queryPage(QueryInsuranceRecordPageForm form) {
        QueryBackInsuranceRecordPageRequest recordPageRequest = new QueryBackInsuranceRecordPageRequest();
        PojoUtils.map(form, recordPageRequest);
        //按销售员姓名查询
        if (StrUtil.isNotEmpty(form.getSellerName())) {
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameEq(form.getSellerName());
            queryStaffListRequest.setStatusNe(UserStatusEnum.DEREGISTER.getCode());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            if (CollUtil.isEmpty(staffList)) {
                return Result.success(form.getPage());
            }
            recordPageRequest.setSellerUserIdList(staffList.stream().map(Staff::getId).collect(Collectors.toList()));
        }
        //按销售员手机号查询
        if (StrUtil.isNotEmpty(form.getSellerMobile())) {
            Staff staff = staffApi.getByMobile(form.getSellerMobile());
            if (Objects.isNull(staff)) {
                return Result.success(form.getPage());
            }
            List<Long> userIdList = Lists.newArrayList();
            userIdList.add(staff.getId());
            recordPageRequest.setSellerUserIdList(userIdList);
        }

        //按保单来源终端查询
        if (StrUtil.isNotEmpty(form.getTerminalName())) {
            QueryEnterprisePageListRequest request = new QueryEnterprisePageListRequest();
            List<Integer> typeList = Lists.newArrayList();
            typeList.add(EnterpriseTypeEnum.CHAIN_BASE.getCode());
            typeList.add(EnterpriseTypeEnum.HOSPITAL.getCode());
            typeList.add(EnterpriseTypeEnum.CLINIC.getCode());
            typeList.add(EnterpriseTypeEnum.CHAIN_DIRECT.getCode());
            typeList.add(EnterpriseTypeEnum.CHAIN_JOIN.getCode());
            typeList.add(EnterpriseTypeEnum.PHARMACY.getCode());
            request.setHmcType(2);
            request.setInTypeList(typeList);
            request.setName(form.getTerminalName());
            Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(request);
            if (enterpriseDTOPage.getTotal() == 0) {
                return Result.success(form.getPage());
            }
            recordPageRequest.setSellerEidList(enterpriseDTOPage.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList()));
        }
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
        Page<InsuranceRecordBO> insuranceRecordBOPage = backInsuranceRecordApi.queryPage(recordPageRequest);
        if (insuranceRecordBOPage.getTotal() == 0) {
            return Result.success(form.getPage());
        }
        Page<InsuranceRecordVO> insuranceRecordVOPage = PojoUtils.map(insuranceRecordBOPage, InsuranceRecordVO.class);
        List<InsuranceRecordVO> records = insuranceRecordVOPage.getRecords();
        List<Long> sellerUserIdList = records.stream().map(InsuranceRecordVO::getSellerUserId).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(sellerUserIdList);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        List<Long> eidList = records.stream().map(InsuranceRecordVO::getEid).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(eidList);
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        records.stream().forEach(insuranceRecordVO -> {
            UserDTO userDTO = userDTOMap.get(insuranceRecordVO.getSellerUserId());
            if (Objects.nonNull(userDTO)) {
                insuranceRecordVO.setSellerUserName(userDTO.getName());
            }
            EnterpriseDTO enterpriseDTO = enterpriseDTOMap.get(insuranceRecordVO.getSellerEid());
            if (Objects.nonNull(enterpriseDTO)) {
                insuranceRecordVO.setSellerEName(enterpriseDTO.getName());
            }
        });
        return Result.success(insuranceRecordVOPage);
    }

    @ApiOperation(value = "保单兑付记录")
    @GetMapping("/queryCashPage")
    public Result<Page<CashVO>> queryCashPage(@Valid QueryCashPageForm form) {
        QueryCashPageRequest request = new QueryCashPageRequest();
        PojoUtils.map(form, request);
        Page<OrderBO> orderBOPage = orderApi.queryCashPage(request);
        Page<CashVO> cashVOPage = PojoUtils.map(orderBOPage, CashVO.class);
        return Result.success(cashVOPage);
    }

    @ApiOperation(value = "退保详情")
    @GetMapping("/getRetreatDetail")
    public Result<InsuranceRecordRetreatVO> getRetreatDetail(@RequestParam Long insuranceRecordId) {
        InsuranceRecordRetreatBO detail = insuranceRecordRetreatApi.getRetreatDetail(insuranceRecordId);
        InsuranceRecordRetreatVO insuranceRecordRetreatVO = new InsuranceRecordRetreatVO();
        PojoUtils.map(detail, insuranceRecordRetreatVO);
        return Result.success(insuranceRecordRetreatVO);
    }


    /**
     * 保单详情
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "保单详情")
    @PostMapping("/detail")
    @Log(title = "保单详情", businessType = BusinessTypeEnum.OTHER)
    public Result<InsuranceRecordDetailVO> detail(@CurrentUser CurrentUserInfo currentUser, @RequestBody QueryInsuranceRecordDetailForm form) {

        QueryInsuranceRecordDetailRequest request = PojoUtils.map(form, QueryInsuranceRecordDetailRequest.class);

        // 1、获取参保记录
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getById(request.getId());
        if (Objects.isNull(insuranceRecordDTO)) {
            return Result.failed("根据参数未获取到参保记录");
        }

        // 2、获取拿药计划
        List<InsuranceFetchPlanDTO> fetchPlanDTOList = insuranceFetchPlanApi.getByInsuranceRecordId(request.getId());

        // 3、获取拿药计划详情
        List<InsuranceFetchPlanDetailDTO> fetchPlanDetailDTOList = insuranceFetchPlanDetailApi.getByInsuranceRecordId(request.getId());

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
        List<InsuranceRecordPayDTO> insuranceRecordPayList = insuranceRecordPayApi.queryByInsuranceRecordId(request.getId());
        List<InsuranceRecordPayHisVO> payHisList = PojoUtils.map(insuranceRecordPayList, InsuranceRecordPayHisVO.class);
        payHisList.stream().forEach(item -> item.setAmount(item.getAmount().divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP)));

        // 已拿次数
        Long tookTimes = fetchPlanDTOList.stream().filter(item -> InsuranceFetchStatusEnum.TOOK.getType().equals(item.getFetchStatus())).count();

        // 剩余次数
        long leftTimes = fetchPlanDTOList.size() - tookTimes;

        InsuranceRecordVO insuranceRecordVO = PojoUtils.map(insuranceRecordDTO, InsuranceRecordVO.class);
        List<InsuranceFetchPlanDetailVO> fetchPlanDetailList = PojoUtils.map(fetchPlanDetailDTOList, InsuranceFetchPlanDetailVO.class);

        // 构建剩余盒数、总共多少盒
        fetchPlanDetailList.forEach(item -> {

            // 总共多少盒
            item.setTotalCount(item.getPerMonthCount() * fetchPlanDTOList.size());

            // 剩余盒数
            item.setLeftTotalCount(item.getTotalCount() - item.getPerMonthCount() * tookTimes);

            // 共兑几次
            item.setTotalTimes(fetchPlanDTOList.size());

            // 还剩几次
            item.setLeftTimes(leftTimes);

            // 已兑多少盒
            item.setTookTotalCount(tookTimes * item.getPerMonthCount());

        });

        InsuranceRecordDetailVO detailVO = new InsuranceRecordDetailVO();
        detailVO.setInsuranceRecord(insuranceRecordVO);
        detailVO.setFetchPlanDetailList(fetchPlanDetailList);
        detailVO.setPayHisList(payHisList);

        // 设置订单来源
        detailVO.getInsuranceRecord().setEname(enterpriseDTO.getName());

        // 设置保险名称
        detailVO.setInsuranceName(insuranceDTO.getInsuranceName());

        // 设置保司名称
        detailVO.setInsuranceCompanyName(insuranceCompanyDTO.getCompanyName());

        // 设置员工编号、员工姓名、线上来源
        if (Objects.nonNull(insuranceRecordDTO.getSellerUserId()) && insuranceRecordDTO.getSellerUserId() > 0) {
            String sellerUserNo = YlStrUtils.buildSellerUserNo(insuranceRecordDTO.getSellerUserId());
            detailVO.getInsuranceRecord().setSellerUserNO(sellerUserNo);

            UserDTO userdto = userApi.getById(insuranceRecordDTO.getSellerUserId());
            detailVO.getInsuranceRecord().setSellerUserName(userdto.getName());

            detailVO.setSourceType(HmcSourceTypeEnum.OFFLINE.getCode());
        }

        // 设置员工所属企业
        if (Objects.nonNull(insuranceRecordDTO.getSellerEid())) {
            EnterpriseDTO sellerEnterprise = enterpriseApi.getById(insuranceRecordDTO.getSellerEid());
            detailVO.getInsuranceRecord().setSellerEName(Optional.ofNullable(sellerEnterprise).map(EnterpriseDTO::getName).orElse(null));
        }

        // 处理被保人身份证
        buildCredentialNo(detailVO, HmcHolderTypeEnum.ISSUE.getCode());

        // 处理被保人手机号
        buildMobile(detailVO, HmcHolderTypeEnum.ISSUE.getCode());

        // 处理投保人身份证
        buildCredentialNo(detailVO, HmcHolderTypeEnum.HOLDER.getCode());

        // 处理投保人手机号
        buildMobile(detailVO, HmcHolderTypeEnum.HOLDER.getCode());

        log.info("查询保单详情返回：{}", detailVO);
        return Result.success(detailVO);
    }


    /**
     * 处理手机号
     *
     * @param detailVO
     */
    private void buildMobile(InsuranceRecordDetailVO detailVO, Integer code) {
        String phone = null;

        if (HmcHolderTypeEnum.ISSUE.getCode().equals(code)) {
            phone = detailVO.getInsuranceRecord().getIssuePhone();
        }

        if (HmcHolderTypeEnum.HOLDER.getCode().equals(code)) {
            phone = detailVO.getInsuranceRecord().getIssuePhone();
        }

        if (StrUtil.isBlank(phone)) {
            return;
        }

        int length = phone.length();
        int starCount = length - 6;
        String star = "";
        for (int i = 0; i < starCount; i++) {
            star += "*";
        }
        String no = phone.substring(0, 3) + star + phone.substring(length - 3);
        if (HmcHolderTypeEnum.ISSUE.getCode().equals(code)) {
            detailVO.getInsuranceRecord().setIssuePhone(no);
        }
        if (HmcHolderTypeEnum.HOLDER.getCode().equals(code)) {
            detailVO.getInsuranceRecord().setHolderPhone(no);
        }

    }

    /**
     * 处理身份证号
     *
     * @param detailVO
     */
    private void buildCredentialNo(InsuranceRecordDetailVO detailVO, Integer code) {

        String credentialNo = null;

        if (HmcHolderTypeEnum.ISSUE.getCode().equals(code)) {
            credentialNo = detailVO.getInsuranceRecord().getIssueCredentialNo();
        }

        if (HmcHolderTypeEnum.HOLDER.getCode().equals(code)) {
            credentialNo = detailVO.getInsuranceRecord().getHolderCredentialNo();
        }

        if (StrUtil.isBlank(credentialNo)) {
            return;
        }

        int length = credentialNo.length();
        int starCount = length - 8;
        String star = "";
        for (int i = 0; i < starCount; i++) {
            star += "*";
        }
        String no = credentialNo.substring(0, 6) + star + credentialNo.substring(length - 2);

        if (HmcHolderTypeEnum.ISSUE.getCode().equals(code)) {
            detailVO.getInsuranceRecord().setIssueCredentialNo(no);
        }
        if (HmcHolderTypeEnum.HOLDER.getCode().equals(code)) {
            detailVO.getInsuranceRecord().setHolderCredentialNo(no);
        }

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