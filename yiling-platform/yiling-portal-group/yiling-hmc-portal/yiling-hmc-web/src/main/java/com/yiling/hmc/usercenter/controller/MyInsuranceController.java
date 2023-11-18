package com.yiling.hmc.usercenter.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.YlStrUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.dto.InsuranceDTO;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.usercenter.form.CheckInsuranceRecordForm;
import com.yiling.hmc.usercenter.form.InsuranceRetreatDetailForm;
import com.yiling.hmc.usercenter.form.QueryInsuranceRecordDetailForm;
import com.yiling.hmc.usercenter.form.QueryInsuranceRecordForm;
import com.yiling.hmc.usercenter.vo.*;
import com.yiling.hmc.wechat.api.*;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDTO;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDetailDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordRetreatDTO;
import com.yiling.hmc.wechat.dto.request.QueryInsuranceRecordDetailRequest;
import com.yiling.hmc.wechat.dto.request.QueryInsuranceRecordPageRequest;
import com.yiling.hmc.wechat.enums.InsuranceFetchStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 我的参保 Controller
 *
 * @author: fan.shen
 * @date: 2022/4/6
 */
@RestController
@RequestMapping("/my_insurance/")
@Api(tags = "我的参保")
@Slf4j
public class MyInsuranceController extends BaseController {

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @DubboReference
    InsuranceFetchPlanApi insuranceFetchPlanApi;

    @DubboReference
    InsuranceFetchPlanDetailApi insuranceFetchPlanDetailApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    InsuranceApi insuranceApi;

    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;

    @DubboReference
    InsuranceRecordRetreatApi retreatApi;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    InsuranceRecordPayPlanApi payPlanApi;

    @DubboReference
    GoodsApi goodsApi;

    @Autowired
    FileService fileService;

    /**
     * 参保列表
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "参保列表")
    @PostMapping("/page_list")
    @Log(title = "我的参保", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<InsuranceRecordPageItemVO>> pageList(@CurrentUser CurrentUserInfo currentUser, @RequestBody QueryInsuranceRecordForm form) {

        QueryInsuranceRecordPageRequest request = PojoUtils.map(form, QueryInsuranceRecordPageRequest.class);
        request.setCurrentUserId(currentUser.getCurrentUserId());

        Page<InsuranceRecordDTO> page = insuranceRecordApi.pageList(request);

        List<InsuranceRecordDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(request.getPage());
        }

        Page<InsuranceRecordPageItemVO> result = PojoUtils.map(page, InsuranceRecordPageItemVO.class);
        result.getRecords().stream().forEach(item -> {

            item.setIssueCredentialNo(IdcardUtil.hide(item.getIssueCredentialNo(),6,16));
            item.setIssuePhone(IdcardUtil.hide(item.getIssuePhone(),3,8));
        });
        return Result.success(result);
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
        List<Long> goodsIdList = fetchPlanDetailDTOList.stream().map(InsuranceFetchPlanDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsIdList);
        Map<Long, GoodsDTO> goodsMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, o -> o, (k1, k2) -> k1));

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


        // 已拿次数
        long tookTimes = fetchPlanDTOList.stream().filter(item -> InsuranceFetchStatusEnum.TOOK.getType().equals(item.getFetchStatus())).count();

        // 剩余次数
        long leftTimes = fetchPlanDTOList.stream().filter(item -> InsuranceFetchStatusEnum.WAIT.getType().equals(item.getFetchStatus())).count();

        InsuranceRecordVO insuranceRecordVO = PojoUtils.map(insuranceRecordDTO, InsuranceRecordVO.class);
        List<InsuranceFetchPlanVO> fetchPlanList = PojoUtils.map(fetchPlanDTOList, InsuranceFetchPlanVO.class);
        List<InsuranceFetchPlanDetailVO> fetchPlanDetailList = PojoUtils.map(fetchPlanDetailDTOList, InsuranceFetchPlanDetailVO.class);

        // 构建剩余盒数、总共多少盒
        fetchPlanDetailList.forEach(item -> {

            // 总共多少盒
            item.setTotalCount(item.getPerMonthCount() * fetchPlanDTOList.size());

            // 剩余盒数
            item.setLeftTotalCount(item.getTotalCount() - item.getPerMonthCount() * tookTimes);

            // 商品图片
            String pic = Optional.ofNullable(goodsMap.get(item.getGoodsId())).map(GoodsDTO::getPic).orElse(null);
            item.setPic(fileService.getUrl(pic, FileTypeEnum.GOODS_PICTURE));

        });

        InsuranceRecordDetailVO detailVO = new InsuranceRecordDetailVO();
        detailVO.setInsuranceRecord(insuranceRecordVO);
        detailVO.setFetchPlanList(fetchPlanList);
        detailVO.setFetchPlanDetailList(fetchPlanDetailList);

        // 设置剩余次数
        detailVO.setLeftTimes(leftTimes);

        // 设置订单来源
        detailVO.setOrderSource(enterpriseDTO.getName());

        // 设置保险名称
        detailVO.setInsuranceName(insuranceDTO.getInsuranceName());

        // 设置保司名称
        detailVO.setInsuranceCompanyName(insuranceCompanyDTO.getCompanyName());

        // 设置退保
        detailVO.setRetreatUrl(insuranceCompanyDTO.getCancelInsuranceAddress());

        // 设置续保
        detailVO.setReNewUrl(insuranceCompanyDTO.getRenewInsuranceAddress());

        // 设置待确认订单
        detailVO.setProcessingOrder(PojoUtils.map(orderApi.getProcessingOrder(currentUser.getCurrentUserId()), OrderVO.class));

        // 设置是否有过兑付记录
        detailVO.setHasOrder(orderApi.hasOrder(insuranceRecordDTO.getId()));

        // 设置是否有续费计划
        detailVO.setHasPayPlan(payPlanApi.hasPayPlan(insuranceRecordDTO.getId()));

        // 设置下次拿药时间
        Optional<InsuranceFetchPlanDTO> firstOption = fetchPlanDTOList.stream().filter(item -> InsuranceFetchStatusEnum.WAIT.getType().equals(item.getFetchStatus())).findFirst();
        firstOption.ifPresent(planDTO -> detailVO.setNextFetchTime(planDTO.getInitFetchTime()));

        // 设置退保电话
        detailVO.setRetreatTelephone(insuranceCompanyDTO.getCancelInsuranceTelephone());

        // 设置员工编号
        String sellerUserNo = YlStrUtils.buildSellerUserNo(insuranceRecordDTO.getSellerUserId());
        detailVO.getInsuranceRecord().setSellerUserNO(sellerUserNo);

        // 设置问诊地址
        detailVO.setInternetConsultationUrl(insuranceCompanyDTO.getInternetConsultationUrl());

        // 处理被保人身份证
        detailVO.getInsuranceRecord().setIssueCredentialNo(buildIssueCredentialNo(detailVO.getInsuranceRecord().getIssueCredentialNo()));

        // 处理被保人手机号
        detailVO.getInsuranceRecord().setIssuePhone(buildMobile(detailVO.getInsuranceRecord().getIssuePhone()));


        return Result.success(detailVO);
    }

    /**
     * 处理被保人手机号
     * @param issuePhone
     */
    private String buildMobile(String issuePhone) {

        if (StrUtil.isBlank(issuePhone)) {
            return "";
        }

        int length = issuePhone.length();
        int starCount = length - 6;
        String star = "";
        for (int i = 0; i < starCount; i++) {
            star += "*";
        }
        String no = issuePhone.substring(0, 3) + star + issuePhone.substring(length-3);
        return no;
    }

    /**
     * 处理被保人身份证号
     *
     * @param issueCredentialNo
     */
    private String buildIssueCredentialNo(String issueCredentialNo) {

        if (StrUtil.isBlank(issueCredentialNo)) {
            return "";
        }

        int length = issueCredentialNo.length();
        int starCount = length - 8;
        String star = "";
        for (int i = 0; i < starCount; i++) {
            star += "*";
        }
        String no = issueCredentialNo.substring(0, 6) + star + issueCredentialNo.substring(length-2);
        return no;
    }

    /**
     * 身份证后六位校验
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "身份证后六位校验")
    @PostMapping("/check")
    @Log(title = "身份证后六位校验", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> check(@CurrentUser CurrentUserInfo currentUser, @RequestBody CheckInsuranceRecordForm form) {

        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getById(form.getId());
        if (Objects.isNull(insuranceRecordDTO)) {
            log.info("根据保单id未查询到保单信息，保单id:{}", form.getId());
            return Result.failed("根据保单id未查询到保单信息");
        }

        // 被保人证件号
        String issueCredentialNo = insuranceRecordDTO.getIssueCredentialNo();

        String last6 = issueCredentialNo.substring(issueCredentialNo.length() - 6);

        if (StrUtil.equals(last6, form.getNumber())) {
            return Result.success();
        }

        return Result.failed("核实保单信息失败");

    }

    /**
     * 退保详情
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "退保详情")
    @PostMapping("/retreat_detail")
    @Log(title = "退保详情", businessType = BusinessTypeEnum.OTHER)
    public Result<InsuranceRetreatDetailVO> retreatDetail(@CurrentUser CurrentUserInfo currentUser, @RequestBody InsuranceRetreatDetailForm form) {

        InsuranceRecordRetreatDTO retreatDTO = retreatApi.getByInsuranceRecordId(form.getId());
        if (Objects.isNull(retreatDTO)) {
            log.info("根据保单id未查询到退保信息，保单id:{}", form.getId());
            return Result.failed("根据保单id未查询到退保信息");
        }

        InsuranceRecordDTO recordDTO = insuranceRecordApi.getById(form.getId());

        InsuranceRetreatDetailVO detailVO = PojoUtils.map(retreatDTO, InsuranceRetreatDetailVO.class);

        // 金额转换 分转元
        String f2y = new BigDecimal(retreatDTO.getRetMoney()).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        detailVO.setRetMoneyYuan(f2y);

        // 被保人名称
        detailVO.setIssueName(recordDTO.getIssueName());

        // 被保人联系方式
        detailVO.setIssuePhone(buildMobile(recordDTO.getIssuePhone()));

        // 处理被保人身份证
        detailVO.setIdNo(buildIssueCredentialNo(detailVO.getIdNo()));

        return Result.success(detailVO);

    }


}
