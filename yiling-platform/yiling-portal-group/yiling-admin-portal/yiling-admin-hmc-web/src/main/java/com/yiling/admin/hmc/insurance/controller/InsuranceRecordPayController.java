package com.yiling.admin.hmc.insurance.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.hmc.insurance.form.QueryInsuranceRecordPayDetailForm;
import com.yiling.admin.hmc.insurance.form.QueryInsuranceRecordPayPageForm;
import com.yiling.admin.hmc.insurance.vo.InsuranceFetchPlanDetailVO;
import com.yiling.admin.hmc.insurance.vo.InsuranceFetchPlanVO;
import com.yiling.admin.hmc.insurance.vo.InsuranceRecordPayDetailVO;
import com.yiling.admin.hmc.insurance.vo.InsuranceRecordPayHisVO;
import com.yiling.admin.hmc.insurance.vo.InsuranceRecordPayVO;
import com.yiling.admin.hmc.insurance.vo.InsuranceRecordVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.YlStrUtils;
import com.yiling.hmc.insurance.api.BackInsuranceRecordPayApi;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.bo.InsuranceRecordPayBO;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.dto.InsuranceDTO;
import com.yiling.hmc.insurance.dto.InsurancePageDTO;
import com.yiling.hmc.insurance.dto.request.InsurancePageRequest;
import com.yiling.hmc.insurance.dto.request.QueryInsuranceRecordPayPageRequest;
import com.yiling.hmc.order.enums.HmcHolderTypeEnum;
import com.yiling.hmc.order.enums.HmcSourceTypeEnum;
import com.yiling.hmc.wechat.api.InsuranceFetchPlanApi;
import com.yiling.hmc.wechat.api.InsuranceFetchPlanDetailApi;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.hmc.wechat.api.InsuranceRecordPayApi;
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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/4/13
 */
@Api(tags = "保险销售交易记录")
@RestController
@RequestMapping("/insurance/pay")
@Slf4j
public class InsuranceRecordPayController extends BaseController {

    @DubboReference
    BackInsuranceRecordPayApi backInsuranceRecordPayApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    InsuranceApi insuranceApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;

    @DubboReference
    InsuranceRecordPayApi insuranceRecordPayApi;

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @DubboReference
    InsuranceFetchPlanApi insuranceFetchPlanApi;

    @DubboReference
    InsuranceFetchPlanDetailApi insuranceFetchPlanDetailApi;

    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;


    @ApiOperation(value = "保险销售交易记录")
    @GetMapping("/queryPage")
    public Result<Page<InsuranceRecordPayVO>> queryPage(QueryInsuranceRecordPayPageForm form) {
        QueryInsuranceRecordPayPageRequest recordPageRequest = new QueryInsuranceRecordPayPageRequest();
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
            recordPageRequest.setSellerUserIds(staffList.stream().map(Staff::getId).collect(Collectors.toList()));
        }
        //按销售员手机号查询
        if (StrUtil.isNotEmpty(form.getSellerMobile())) {
            Staff staff = staffApi.getByMobile(form.getSellerMobile());
            if (Objects.isNull(staff)) {
                return Result.success(form.getPage());
            }
            List<Long> userIdList = Lists.newArrayList();
            userIdList.add(staff.getId());
            recordPageRequest.setSellerUserIds(userIdList);
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
        Page<InsuranceRecordPayBO> recordPayBOPage = backInsuranceRecordPayApi.queryPage(recordPageRequest);
        if (recordPayBOPage.getTotal() == 0) {
            return Result.success(form.getPage());
        }
        Page<InsuranceRecordPayVO> recordPayVOPage = PojoUtils.map(recordPayBOPage, InsuranceRecordPayVO.class);
        List<InsuranceRecordPayVO> records = recordPayVOPage.getRecords();
        List<Long> sellerUserIdList = records.stream().map(InsuranceRecordPayVO::getSellerUserId).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(sellerUserIdList);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        List<Long> eidList = records.stream().map(InsuranceRecordPayVO::getEid).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(eidList);
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        records.stream().forEach(insuranceRecordVO -> {
            UserDTO userDTO = userDTOMap.get(insuranceRecordVO.getSellerUserId());
            if (Objects.nonNull(userDTO)) {
                insuranceRecordVO.setSellerUserName(userDTO.getName());
            }
            EnterpriseDTO enterpriseDTO = enterpriseDTOMap.get(insuranceRecordVO.getEid());
            if (Objects.nonNull(enterpriseDTO)) {
                insuranceRecordVO.setSellerEName(enterpriseDTO.getName());
            }
        });
        return Result.success(recordPayVOPage);
    }


    /**
     * 保单支付记录详情
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "保单支付记录详情")
    @PostMapping("/detail")
    @Log(title = "保单支付记录详情", businessType = BusinessTypeEnum.OTHER)
    public Result<InsuranceRecordPayDetailVO> recordPayDetail(@CurrentUser CurrentUserInfo currentUser, @RequestBody QueryInsuranceRecordPayDetailForm form) {

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
        payHisList.stream().forEach(item -> item.setAmount(item.getAmount().divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP)));

        // 已拿次数
        Long tookTimes = fetchPlanDTOList.stream().filter(item -> InsuranceFetchStatusEnum.TOOK.getType().equals(item.getFetchStatus())).count();

        // 剩余次数
        long leftTimes = fetchPlanDTOList.size() - tookTimes;

        InsuranceRecordVO insuranceRecordVO = PojoUtils.map(insuranceRecordDTO, InsuranceRecordVO.class);
        InsuranceRecordPayVO recordPayVO = PojoUtils.map(payDTO, InsuranceRecordPayVO.class);
        List<InsuranceFetchPlanVO> fetchPlanList = PojoUtils.map(fetchPlanDTOList, InsuranceFetchPlanVO.class);
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

        InsuranceRecordPayDetailVO detailVO = new InsuranceRecordPayDetailVO();
        detailVO.setInsuranceRecord(insuranceRecordVO);
        detailVO.setFetchPlanList(fetchPlanList);
        detailVO.setFetchPlanDetailList(fetchPlanDetailList);
        detailVO.setPayHisList(payHisList);
        detailVO.setCurrentPayStage(payHisList.size());
        detailVO.setRecordPayVO(recordPayVO);

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

        log.info("查询支付单详情返回：{}", detailVO);
        return Result.success(detailVO);
    }


    /**
     * 处理手机号
     *
     * @param detailVO
     */
    private void buildMobile(InsuranceRecordPayDetailVO detailVO, Integer code) {
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
        detailVO.getInsuranceRecord().setIssuePhone(no);
    }

    /**
     * 处理身份证号
     *
     * @param detailVO
     */
    private void buildCredentialNo(InsuranceRecordPayDetailVO detailVO, Integer code) {

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
        detailVO.getInsuranceRecord().setIssueCredentialNo(no);

    }
}