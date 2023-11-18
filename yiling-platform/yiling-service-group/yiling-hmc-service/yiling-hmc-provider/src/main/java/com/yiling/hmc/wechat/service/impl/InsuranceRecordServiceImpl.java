package com.yiling.hmc.wechat.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsSignatureEnum;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.pojo.vo.WxConstant;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.config.WxTemplateConfig;
import com.yiling.hmc.insurance.bo.InsuranceRecordBO;
import com.yiling.hmc.insurance.dto.request.QueryBackInsuranceRecordPageRequest;
import com.yiling.hmc.insurance.dto.request.SaveClaimInformationRequest;
import com.yiling.hmc.insurance.entity.InsuranceCompanyDO;
import com.yiling.hmc.insurance.entity.InsuranceDO;
import com.yiling.hmc.insurance.service.InsuranceCompanyService;
import com.yiling.hmc.insurance.service.InsuranceService;
import com.yiling.hmc.patient.service.PatientService;
import com.yiling.hmc.wechat.constant.Constants;
import com.yiling.hmc.wechat.dao.InsuranceRecordMapper;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.MiniProgram;
import com.yiling.hmc.wechat.dto.WxMsgDTO;
import com.yiling.hmc.wechat.dto.WxMssData;
import com.yiling.hmc.wechat.dto.request.InsuranceJoinNotifyContext;
import com.yiling.hmc.wechat.dto.request.InsurancePayNotifyContext;
import com.yiling.hmc.wechat.dto.request.QueryInsuranceRecordPageRequest;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceFetchPlanRequest;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRecordRequest;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRetreatRequest;
import com.yiling.hmc.wechat.dto.request.UpdateInsuranceRecordRequest;
import com.yiling.hmc.wechat.entity.InsuranceFetchPlanDO;
import com.yiling.hmc.wechat.entity.InsuranceRecordDO;
import com.yiling.hmc.wechat.entity.InsuranceRecordPayDO;
import com.yiling.hmc.wechat.enums.HmcPolicyEndTypeEnum;
import com.yiling.hmc.wechat.enums.HmcPolicyStatusEnum;
import com.yiling.hmc.wechat.enums.InsuranceFetchStatusEnum;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanDetailService;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanService;
import com.yiling.hmc.wechat.service.InsuranceRecordPayPlanService;
import com.yiling.hmc.wechat.service.InsuranceRecordPayService;
import com.yiling.hmc.wechat.service.InsuranceRecordRetreatService;
import com.yiling.hmc.wechat.service.InsuranceRecordService;
import com.yiling.ih.patient.api.HmcPatientApi;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * C端参保记录表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-28
 */
@Slf4j
@Service
public class InsuranceRecordServiceImpl extends BaseServiceImpl<InsuranceRecordMapper, InsuranceRecordDO> implements InsuranceRecordService {

    @DubboReference
    protected NoApi noApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    SmsApi smsApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    GzhUserApi gzhUserApi;

    @Autowired
    private InsuranceFetchPlanService insuranceFetchPlanService;

    @Autowired
    private InsuranceFetchPlanDetailService insuranceFetchPlanDetailService;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private InsuranceCompanyService insuranceCompanyService;

    @Autowired
    private InsuranceRecordPayService insuranceRecordPayService;

    @Autowired
    private InsuranceRecordPayPlanService insuranceRecordPayPlanService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private InsuranceRecordRetreatService retreatService;

    @DubboReference
    private HmcPatientApi hmcPatientApi;

    /**
     * 小程序服务类
     */
    @Autowired
    WxMaService wxMaService;

    /**
     * 公众号服务类
     */
    @Autowired
    WxMpService wxMpService;

    @Autowired
    WxTemplateConfig templateConfig;

    @Autowired
    FileService fileService;

    @Override
    public Long saveInsuranceRecord(SaveInsuranceRecordRequest request) {
        InsuranceRecordDO insuranceRecord = PojoUtils.map(request, InsuranceRecordDO.class);
        insuranceRecord.setOrderNo(noApi.gen(NoEnum.ORDER_NO));
        this.save(insuranceRecord);
        return insuranceRecord.getId();
    }

    @Override
    public InsuranceRecordDTO getByPolicyNo(String policyNo) {
        QueryWrapper<InsuranceRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceRecordDO::getPolicyNo, policyNo);
        InsuranceRecordDO recordDO = getOne(wrapper, false);
        return PojoUtils.map(recordDO, InsuranceRecordDTO.class);
    }

    @Override
    public Boolean updatePolicyStatus(String policyNo, Integer endPolicyType) {
        InsuranceRecordDO recordDO = new InsuranceRecordDO();
        HmcPolicyEndTypeEnum match = HmcPolicyEndTypeEnum.match(endPolicyType);
        recordDO.setPolicyStatus(match.getCode());

        UpdateWrapper<InsuranceRecordDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(InsuranceRecordDO::getPolicyNo, policyNo);

        return this.update(recordDO, updateWrapper);
    }

    @Override
    public Boolean updatePolicyEndTime(UpdateInsuranceRecordRequest request) {
        InsuranceRecordDO recordDO = new InsuranceRecordDO();
        recordDO.setCurrentEndTime(request.getCurrentEndTime());

        UpdateWrapper<InsuranceRecordDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(InsuranceRecordDO::getId, request.getId());

        return this.update(recordDO, updateWrapper);

    }

    @Override
    public Page<InsuranceRecordDTO> pageList(QueryInsuranceRecordPageRequest request) {

        QueryWrapper<InsuranceRecordDO> wrapper = new QueryWrapper<>();

        wrapper.lambda().eq(InsuranceRecordDO::getUserId, request.getCurrentUserId());
        if (StrUtil.isNotBlank(request.getNameOrPhone())) {

            wrapper.lambda().and(nest -> nest.or().like(InsuranceRecordDO::getIssueName, request.getNameOrPhone()).or().like(InsuranceRecordDO::getIssuePhone, request.getNameOrPhone()));

        }

        // 创建时间，将序排列
        wrapper.lambda().orderByDesc(InsuranceRecordDO::getCreateTime);

        Page<InsuranceRecordDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page, InsuranceRecordDTO.class);
    }

    @Override
    public boolean hasInsurance(Long userId) {
        QueryWrapper<InsuranceRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceRecordDO::getUserId, userId);
        wrapper.lambda().eq(InsuranceRecordDO::getPolicyStatus, HmcPolicyStatusEnum.PROCESSING.getType());
        InsuranceRecordDO one = this.getOne(wrapper, false);
        if (Objects.nonNull(one)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Page<InsuranceRecordBO> queryPage(QueryBackInsuranceRecordPageRequest recordPageRequest) {
        LambdaQueryWrapper<InsuranceRecordDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtils.isNotEmpty(recordPageRequest.getPolicyNo()), InsuranceRecordDO::getPolicyNo, recordPageRequest.getPolicyNo());
        wrapper.eq(StringUtils.isNotEmpty(recordPageRequest.getIssueName()), InsuranceRecordDO::getIssueName, recordPageRequest.getIssueName());
        wrapper.eq(StringUtils.isNotEmpty(recordPageRequest.getHolderName()), InsuranceRecordDO::getHolderName, recordPageRequest.getHolderName());
        // wrapper.eq(StringUtils.isNotEmpty(recordPageRequest.getIssuePhone()), InsuranceRecordDO::getIssuePhone, recordPageRequest.getIssuePhone());
        if (StringUtils.isNotEmpty(recordPageRequest.getIssuePhone())) {
            wrapper.and(w -> w.eq(InsuranceRecordDO::getIssuePhone, recordPageRequest.getIssuePhone()).or().eq(InsuranceRecordDO::getHolderPhone, recordPageRequest.getIssuePhone()));
        }

        if (Objects.nonNull(recordPageRequest.getSourceType())) {
            if (recordPageRequest.getSourceType() == 0) {
                wrapper.eq(InsuranceRecordDO::getSellerUserId, 0);
            } else {
                wrapper.gt(InsuranceRecordDO::getSellerUserId, 0);
            }

        }
        wrapper.in(CollUtil.isNotEmpty(recordPageRequest.getSellerUserIdList()), InsuranceRecordDO::getSellerUserId, recordPageRequest.getSellerUserIdList());
        wrapper.in(CollUtil.isNotEmpty(recordPageRequest.getSellerEidList()), InsuranceRecordDO::getSellerEid, recordPageRequest.getSellerEidList());
        wrapper.in(CollUtil.isNotEmpty(recordPageRequest.getInsuranceIdList()), InsuranceRecordDO::getInsuranceId, recordPageRequest.getInsuranceIdList());

        wrapper.eq(Objects.nonNull(recordPageRequest.getBillType()), InsuranceRecordDO::getBillType, recordPageRequest.getBillType());
        wrapper.ge(Objects.nonNull(recordPageRequest.getStartProposalTime()), InsuranceRecordDO::getProposalTime, recordPageRequest.getStartProposalTime());
        wrapper.eq(Objects.nonNull(recordPageRequest.getInsuranceCompanyId()), InsuranceRecordDO::getInsuranceCompanyId, recordPageRequest.getInsuranceCompanyId());
        wrapper.eq(Objects.nonNull(recordPageRequest.getPolicyStatus()), InsuranceRecordDO::getPolicyStatus, recordPageRequest.getPolicyStatus());
        if (Objects.nonNull(recordPageRequest.getEndProposalTime())) {
            recordPageRequest.setEndProposalTime(DateUtil.endOfDay(recordPageRequest.getEndProposalTime()));
            wrapper.le(InsuranceRecordDO::getProposalTime, recordPageRequest.getEndProposalTime());
        }
        Page<InsuranceRecordDO> insuranceRecordDOPage = this.page(recordPageRequest.getPage(), wrapper);
        List<InsuranceRecordDO> insuranceRecordDOS = insuranceRecordDOPage.getRecords();
        if (CollUtil.isEmpty(insuranceRecordDOS)) {
            return recordPageRequest.getPage();
        }

        List<Long> idList = insuranceRecordDOS.stream().map(InsuranceRecordDO::getId).collect(toList());
        //查询兑付次数
        LambdaQueryWrapper<InsuranceFetchPlanDO> fetchWrapper = Wrappers.lambdaQuery();
        fetchWrapper.in(InsuranceFetchPlanDO::getFetchStatus, 1, 3);
        fetchWrapper.in(InsuranceFetchPlanDO::getInsuranceRecordId, idList).select(InsuranceFetchPlanDO::getId, InsuranceFetchPlanDO::getInsuranceRecordId);
        List<InsuranceFetchPlanDO> list = insuranceFetchPlanService.list(fetchWrapper);
        Map<Long, List<InsuranceFetchPlanDO>> map = Maps.newHashMap();
        if (CollUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.groupingBy(InsuranceFetchPlanDO::getInsuranceRecordId));
        }
        Page<InsuranceRecordBO> insuranceRecordBOPage = PojoUtils.map(insuranceRecordDOPage, InsuranceRecordBO.class);
        Map<Long, List<InsuranceFetchPlanDO>> finalMap = map;
        //累计支付金额
        LambdaQueryWrapper<InsuranceRecordPayDO> payWrapper = Wrappers.lambdaQuery();
        List<String> policyNoList = insuranceRecordDOS.stream().map(InsuranceRecordDO::getPolicyNo).collect(toList());
        payWrapper.in(InsuranceRecordPayDO::getPolicyNo, policyNoList).eq(InsuranceRecordPayDO::getPayStatus, 1).select(InsuranceRecordPayDO::getAmount, InsuranceRecordPayDO::getPolicyNo);
        List<InsuranceRecordPayDO> payDOList = insuranceRecordPayService.list(payWrapper);
        Map<String, List<InsuranceRecordPayDO>> moneyMap = Maps.newHashMap();
        if (CollUtil.isNotEmpty(payDOList)) {
            moneyMap = payDOList.stream().collect(Collectors.groupingBy(InsuranceRecordPayDO::getPolicyNo));
        }
        Map<String, List<InsuranceRecordPayDO>> finalMoneyMap = moneyMap;
        //保险信息
        List<Long> insuranceIdList = insuranceRecordDOS.stream().map(InsuranceRecordDO::getInsuranceId).distinct().collect(toList());
        List<InsuranceDO> insuranceDOS = insuranceService.listByIdListAndCompanyAndStatus(insuranceIdList, null, null);
        Map<Long, InsuranceDO> insuranceDOMap = insuranceDOS.stream().collect(Collectors.toMap(InsuranceDO::getId, Function.identity()));
        //保险公司
        List<Long> companyIdList = insuranceRecordDOS.stream().map(InsuranceRecordDO::getInsuranceCompanyId).distinct().collect(toList());
        LambdaQueryWrapper<InsuranceCompanyDO> companyWrapper = Wrappers.lambdaQuery();
        companyWrapper.in(InsuranceCompanyDO::getId, companyIdList);
        List<InsuranceCompanyDO> companyDOS = insuranceCompanyService.list(companyWrapper);
        Map<Long, InsuranceCompanyDO> companyDOMap = companyDOS.stream().collect(Collectors.toMap(InsuranceCompanyDO::getId, Function.identity()));
        Map<Long, Long> fetchPlanCountMap = null;
        Map<Long, List<Long>> perMonthCountMap = null;
        if (recordPageRequest.isExport()) {
            fetchPlanCountMap = insuranceFetchPlanService.getFetchPlanCountMap(idList, null);
            perMonthCountMap = insuranceFetchPlanDetailService.getPerMonthCountMap(idList, null);
        }
        Map<Long, Long> finalFetchPlanCountMap = fetchPlanCountMap;
        Map<Long, List<Long>> finalPerMonthCountMap = perMonthCountMap;
        insuranceRecordBOPage.getRecords().forEach(insuranceRecordBO -> {
            if (!finalMap.isEmpty()) {
                List<InsuranceFetchPlanDO> insuranceFetchPlanDOS = finalMap.get(insuranceRecordBO.getId());
                if (CollUtil.isNotEmpty(insuranceFetchPlanDOS)) {
                    insuranceRecordBO.setCashTimes(insuranceFetchPlanDOS.size());
                } else {
                    insuranceRecordBO.setCashTimes(0);
                }
            } else {
                insuranceRecordBO.setCashTimes(0);
            }
            if (!finalMoneyMap.isEmpty()) {
                List<InsuranceRecordPayDO> recordPayDOS = finalMoneyMap.get(insuranceRecordBO.getPolicyNo());
                if (CollUtil.isNotEmpty(recordPayDOS)) {
                    Long reduce = recordPayDOS.stream().map(InsuranceRecordPayDO::getAmount).reduce(0L, Long::sum);
                    if (reduce > 0) {
                        insuranceRecordBO.setTotalPayMoney(BigDecimal.valueOf(reduce).divide(new BigDecimal(100)).setScale(2));
                    }

                } else {
                    insuranceRecordBO.setTotalPayMoney(BigDecimal.ZERO);
                }
            } else {
                insuranceRecordBO.setTotalPayMoney(BigDecimal.ZERO);
            }
            if (Objects.nonNull(insuranceDOMap.get(insuranceRecordBO.getInsuranceId()))) {
                insuranceRecordBO.setInsuranceName(insuranceDOMap.get(insuranceRecordBO.getInsuranceId()).getInsuranceName());
            }
            InsuranceCompanyDO insuranceCompanyDO = companyDOMap.get(insuranceRecordBO.getInsuranceCompanyId());
            if (Objects.nonNull(insuranceCompanyDO)) {
                insuranceRecordBO.setCompanyName(insuranceCompanyDO.getCompanyName());
            }
            if (Objects.nonNull(finalFetchPlanCountMap) && Objects.nonNull(finalPerMonthCountMap)) {
                List<Long> monthList = finalPerMonthCountMap.getOrDefault(insuranceRecordBO.getId(), null);
                if (CollUtil.isEmpty(monthList)) {
                    return;
                }
                Long planCount = finalFetchPlanCountMap.getOrDefault(insuranceRecordBO.getId(), 0L);
                List<Long> mul = Lists.newArrayList();
                monthList.forEach(m -> {
                    mul.add(m * planCount);
                });
                insuranceRecordBO.setTotalCashCount(mul.stream().mapToLong(Long::longValue).sum());
                List<Long> mulOfCashed = Lists.newArrayList();
                monthList.forEach(m -> {
                    mulOfCashed.add(m * insuranceRecordBO.getCashTimes());
                });
                insuranceRecordBO.setCashedTotal(mulOfCashed.stream().mapToLong(Long::longValue).sum());
            }

        });
        return insuranceRecordBOPage;
    }

    @Override
    public void expireNotify() {
        log.info("保单过期提醒定时任务开始执行");
        DateTime endOfDay = DateUtil.endOfDay(new Date());
        DateTime dateTime = DateUtil.offsetDay(endOfDay, 15);
        DateTime beginOfDay = DateUtil.beginOfDay(dateTime);
        DateTime onedateTime = DateUtil.offsetDay(endOfDay, 1);
        DateTime onebeginOfDay = DateUtil.beginOfDay(onedateTime);
        LambdaQueryWrapper<InsuranceRecordDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(InsuranceRecordDO::getPolicyStatus, HmcPolicyStatusEnum.PROCESSING.getType());
        wrapper.and((w) -> {
            w.between(InsuranceRecordDO::getCurrentEndTime, beginOfDay, dateTime).or().between(InsuranceRecordDO::getCurrentEndTime, onebeginOfDay, onedateTime);
        });
        List<InsuranceRecordDO> insuranceRecordDOS = this.list(wrapper);
        if (CollUtil.isEmpty(insuranceRecordDOS)) {
            log.info("无进行中保单");
            return;
        }
        List<Long> idList = insuranceRecordDOS.stream().map(InsuranceRecordDO::getId).collect(toList());
        LambdaQueryWrapper<InsuranceFetchPlanDO> planWrapper = Wrappers.lambdaQuery();
        planWrapper.eq(InsuranceFetchPlanDO::getFetchStatus, InsuranceFetchStatusEnum.WAIT.getType());
        planWrapper.in(InsuranceFetchPlanDO::getInsuranceRecordId, idList).select(InsuranceFetchPlanDO::getInsuranceRecordId);
        List<Object> objects = insuranceFetchPlanService.listObjs(planWrapper);
        if (CollUtil.isEmpty(objects)) {
            log.info("无未拿状态的拿药计划");
            return;
        }
        List<Long> ids = PojoUtils.map(objects, Long.class).stream().distinct().collect(toList());
        insuranceRecordDOS = insuranceRecordDOS.stream().filter(insuranceRecordDO -> CollUtil.contains(ids, insuranceRecordDO.getId())).collect(toList());
        if (CollUtil.isEmpty(insuranceRecordDOS)) {
            log.info("进行中的保单暂无待兑付的数据");
            return;
        }
        List<Long> eidList = insuranceRecordDOS.stream().map(InsuranceRecordDO::getEid).collect(toList());
        //查询药店名称
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(eidList);
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        List<Long> userIdList = insuranceRecordDOS.stream().map(InsuranceRecordDO::getUserId).distinct().collect(toList());
        List<HmcUser> hmcUserDTOList = hmcUserApi.listByIds(userIdList);
        Map<Long, String> userMap = hmcUserDTOList.stream().collect(Collectors.toMap(HmcUser::getUserId, HmcUser::getUnionId));
        List<String> unionIdList = hmcUserDTOList.stream().map(HmcUser::getUnionId).collect(toList());
        List<GzhUserDTO> gzhUserDTOList = gzhUserApi.getByUnionIdList(unionIdList);
        Map<String, String> openIdMap = gzhUserDTOList.stream().collect(Collectors.toMap(GzhUserDTO::getUnionId, GzhUserDTO::getGzhOpenId));
        log.info("保单过期定时任务执行数据insuranceRecordDOS={},hmcUserDTOList={},gzhUserDTOList={}", insuranceRecordDOS.toString(), hmcUserDTOList.toString(), gzhUserDTOList.toString());
        insuranceRecordDOS.forEach(insuranceRecordDO -> {
            String expireDate = DateUtil.format(insuranceRecordDO.getCurrentEndTime(), DatePattern.NORM_DATETIME_FORMAT);

            if (StrUtil.isNotEmpty(insuranceRecordDO.getHolderPhone())) {
                //给投保人发送短信提醒
                String content = String.format(Constants.INSURANC_EEXPIRE_TEMPLATE, insuranceRecordDO.getIssueName(), expireDate);
                smsApi.send(insuranceRecordDO.getHolderPhone(), content, SmsTypeEnum.INSURANCE_EXPIRE, SmsSignatureEnum.YILING_HEALTH);
            }
            String unionId = userMap.get(insuranceRecordDO.getUserId());
            if (StrUtil.isNotEmpty(unionId)) {
                String openId = openIdMap.get(unionId);
                if (StrUtil.isNotBlank(openId)) {
                    // 发送模板消息
                    String remark = String.format(Constants.INSURANC_EEXPIRE_WX_TEMPLATE, expireDate);
                    this.send(insuranceRecordDO, expireDate, enterpriseDTOMap.get(insuranceRecordDO.getEid()).getName(), remark, openId);
                }
            }
        });
    }

    private void send(InsuranceRecordDO insuranceRecordDO, String expireDate, String ename, String remark, String openId) {
        WxMsgDTO wxMsgDTO = WxMsgDTO.builder().touser(openId).template_id(templateConfig.getPolicyExpired()).build();
        Map<String, WxMssData> data = new HashMap<>();
        data.put("first", WxMssData.builder().value("待兑付药品提醒").build());
        data.put("keyword1", WxMssData.builder().value(insuranceRecordDO.getIssueName()).build());
        data.put("keyword2", WxMssData.builder().value("请尽快兑付取药，过期时间为" + expireDate).build());
        data.put("keyword3", WxMssData.builder().value(ename).build());
        data.put("remark", WxMssData.builder().value(remark).build());

        String pagePath = Constants.INSURANCE_DETAIL_PATH + insuranceRecordDO.getId();
        MiniProgram miniProgram = MiniProgram.builder().appid(wxMaService.getWxMaConfig().getAppid()).pagepath(pagePath).build();
        wxMsgDTO.setData(data);
        wxMsgDTO.setMiniprogram(miniProgram);
        try {
            String url = String.format(WxConstant.URL_TEMPLATE_SEND_POST, wxMpService.getAccessToken());
            String result = HttpUtil.post(url, JSONUtil.toJsonStr(wxMsgDTO));
            log.info("保单过期提醒响应结果{}", result);
        } catch (WxErrorException e) {
            log.error("推送保单过期消息出错：{}", e.getMessage(), e);
        }
    }

    @Override
    public InsuranceRecordDTO getById(Long insuranceRecordId) {
        QueryWrapper<InsuranceRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceRecordDO::getId, insuranceRecordId);
        InsuranceRecordDO recordDO = getOne(wrapper);
        return PojoUtils.map(recordDO, InsuranceRecordDTO.class);
    }

    @Override
    public boolean checkInsuranceRecord(String policyNo, Long insuranceCompanyId) {
        QueryWrapper<InsuranceRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceRecordDO::getPolicyNo, policyNo);
        wrapper.lambda().eq(InsuranceRecordDO::getInsuranceCompanyId, insuranceCompanyId);
        InsuranceRecordDO recordDO = getOne(wrapper);
        if (Objects.nonNull(recordDO)) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Long joinNotify(InsuranceJoinNotifyContext context) {

        // 1、参保记录
        Long insuranceRecordId = this.saveInsuranceRecord(context.getInsuranceRecordRequest());

        // 2、赋值参保记录id
        context.buildInsuranceRecordId(insuranceRecordId);

        // 3、参保缴费计划
        this.insuranceRecordPayPlanService.saveInsuranceRecordPayPlan(context.getPayPlanRequestList());

        // 4、首次缴费记录
        Long recordPayId = this.insuranceRecordPayService.saveInsuranceRecordPay(context.getPayRequest());

        // 5、拿药计划
        context.getFetchPlanRequestList().stream().forEach(item -> item.setRecordPayId(recordPayId));
        this.insuranceFetchPlanService.saveFetchPlan(context.getFetchPlanRequestList());

        // 6、拿药计划明细
        context.getFetchPlanDetailRequestList().stream().forEach(item -> item.setRecordPayId(recordPayId));
        this.insuranceFetchPlanDetailService.saveFetchPlanDetail(context.getFetchPlanDetailRequestList());

        return insuranceRecordId;
    }

    @Override
    @Transactional
    public void statusAsync(SaveInsuranceRetreatRequest request) {

        // 更新保单状态
        this.updatePolicyStatus(request.getPolicyNo(), request.getEndPolicyType());

        // 保存退保记录
        this.retreatService.saveInsuranceRecordRetreat(request);

    }

    @Override
    @Transactional
    public void payNotify(InsurancePayNotifyContext payNotifyContext) {

        // 保存支付记录
        Long recordPayId = this.insuranceRecordPayService.saveInsuranceRecordPay(payNotifyContext.getPayRequest());

        // 更新保单状态、效期
        this.updatePolicyEndTime(payNotifyContext.getUpdateInsuranceRequest());

        // 保存拿药计划
        List<SaveInsuranceFetchPlanRequest> fetchPlanRequestList = payNotifyContext.getFetchPlanRequestList();
        fetchPlanRequestList.stream().forEach(item -> item.setRecordPayId(recordPayId));
        this.insuranceFetchPlanService.saveFetchPlan(fetchPlanRequestList);

    }

    @Override
    public void updateSfzAndSignature(Long insuranceRecordId, SaveClaimInformationRequest request) {
        InsuranceRecordDO recordDO = new InsuranceRecordDO();
        recordDO.setIdCardFront(request.getIdCardFront());
        recordDO.setIdCardBack(request.getIdCardBack());
        recordDO.setHandSignature(request.getHandSignature());

        UpdateWrapper<InsuranceRecordDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(InsuranceRecordDO::getId, insuranceRecordId);

        this.update(recordDO, updateWrapper);
    }

    @Override
    public String uploadPolicyFile(InsuranceRecordDTO insuranceRecordDTO) {
        byte[] bytes = HttpUtil.downloadBytes(insuranceRecordDTO.getPolicyUrl());
        try {
            FileInfo file = fileService.upload(bytes, insuranceRecordDTO.getPolicyNo() + ".pdf", FileTypeEnum.POLICY_FILE, null);
            return file.getUrl();
        } catch (Exception e) {
            log.error("上传电子保单出错，{}", ExceptionUtils.getFullStackTrace(e), e);
        }
        return null;

    }
}
