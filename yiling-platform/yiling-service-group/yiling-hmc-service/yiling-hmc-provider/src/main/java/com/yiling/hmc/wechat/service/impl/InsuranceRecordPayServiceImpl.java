package com.yiling.hmc.wechat.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.bo.InsuranceRecordPayBO;
import com.yiling.hmc.insurance.dto.request.QueryInsuranceRecordPayPageRequest;
import com.yiling.hmc.insurance.entity.InsuranceCompanyDO;
import com.yiling.hmc.insurance.entity.InsuranceDO;
import com.yiling.hmc.insurance.service.InsuranceCompanyService;
import com.yiling.hmc.insurance.service.InsuranceService;
import com.yiling.hmc.wechat.dao.InsuranceRecordPayMapper;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRecordPayRequest;
import com.yiling.hmc.wechat.entity.InsuranceFetchPlanDO;
import com.yiling.hmc.wechat.entity.InsuranceRecordPayDO;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanDetailService;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanService;
import com.yiling.hmc.wechat.service.InsuranceRecordPayService;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.enums.NoEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * C端参保记录支付流水表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-28
 */
@Service
public class InsuranceRecordPayServiceImpl extends BaseServiceImpl<InsuranceRecordPayMapper, InsuranceRecordPayDO> implements InsuranceRecordPayService {


    @DubboReference
    protected NoApi noApi;

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

    @Override
    public Long saveInsuranceRecordPay(SaveInsuranceRecordPayRequest request) {
        InsuranceRecordPayDO payDO = PojoUtils.map(request, InsuranceRecordPayDO.class);
        payDO.setOrderNo(noApi.gen(NoEnum.ORDER_NO));
        this.save(payDO);
        return payDO.getId();
    }

    @Override
    public Page<InsuranceRecordPayBO> queryPage(QueryInsuranceRecordPayPageRequest recordPageRequest) {
        if(Objects.nonNull(recordPageRequest.getEndPayTime())){
            recordPageRequest.setEndPayTime(DateUtil.endOfDay(recordPageRequest.getEndPayTime()));
        }
        Page<InsuranceRecordPayBO> insuranceRecordPayBOPage = this.baseMapper.queryPage(recordPageRequest.getPage(), recordPageRequest);
        List<InsuranceRecordPayBO> insuranceRecordDOS = insuranceRecordPayBOPage.getRecords();
        if(CollUtil.isEmpty(insuranceRecordDOS)){
            return recordPageRequest.getPage();
        }
        List<Long> idList = insuranceRecordDOS.stream().map(InsuranceRecordPayBO::getId).collect(Collectors.toList());
        Map<Long, List<InsuranceFetchPlanDO>> map = Maps.newHashMap();
        if(CollUtil.isNotEmpty(idList)){
            //查询兑付次数
            LambdaQueryWrapper<InsuranceFetchPlanDO> fetchWrapper = Wrappers.lambdaQuery();
            fetchWrapper.in(InsuranceFetchPlanDO::getFetchStatus,1,3);
            fetchWrapper.in(InsuranceFetchPlanDO::getRecordPayId,idList).select(InsuranceFetchPlanDO::getId,InsuranceFetchPlanDO::getInsuranceRecordId);
            List<InsuranceFetchPlanDO> list = insuranceFetchPlanService.list(fetchWrapper);
            if(CollUtil.isNotEmpty(list)){
                map = list.stream().collect(Collectors.groupingBy(InsuranceFetchPlanDO::getInsuranceRecordId));
            }
        }
        Map<Long, List<InsuranceFetchPlanDO>> finalMap = map;
        //累计支付金额
        LambdaQueryWrapper<InsuranceRecordPayDO> payWrapper = Wrappers.lambdaQuery();
        List<String> policyNoList = insuranceRecordDOS.stream().map(InsuranceRecordPayBO::getPolicyNo).collect(Collectors.toList());
        payWrapper.in(InsuranceRecordPayDO::getPolicyNo,policyNoList).eq(InsuranceRecordPayDO::getPayStatus,1).select(InsuranceRecordPayDO::getAmount,InsuranceRecordPayDO::getPolicyNo);
        List<InsuranceRecordPayDO> payDOList = insuranceRecordPayService.list(payWrapper);
        Map<String, List<InsuranceRecordPayDO>> moneyMap = Maps.newHashMap();
        if(CollUtil.isNotEmpty(payDOList)){
            moneyMap = payDOList.stream().collect(Collectors.groupingBy(InsuranceRecordPayDO::getPolicyNo));
        }
        Map<String, List<InsuranceRecordPayDO>> finalMoneyMap = moneyMap;
        //保险信息
        List<Long> insuranceIdList = insuranceRecordDOS.stream().map(InsuranceRecordPayBO::getInsuranceId).distinct().collect(Collectors.toList());
        List<InsuranceDO> insuranceDOS = insuranceService.listByIdListAndCompanyAndStatus(insuranceIdList, null, null);
        Map<Long, InsuranceDO> insuranceDOMap = insuranceDOS.stream().collect(Collectors.toMap(InsuranceDO::getId, Function.identity()));
        //保险公司
        List<Long> companyIdList = insuranceRecordDOS.stream().map(InsuranceRecordPayBO::getInsuranceCompanyId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<InsuranceCompanyDO> companyWrapper = Wrappers.lambdaQuery();
        companyWrapper.in(InsuranceCompanyDO::getId,companyIdList);
        List<InsuranceCompanyDO> companyDOS = insuranceCompanyService.list(companyWrapper);
        Map<Long, InsuranceCompanyDO> companyDOMap = companyDOS.stream().collect(Collectors.toMap(InsuranceCompanyDO::getId, Function.identity()));
        //导出字段增加
        Map<Long, Long> fetchPlanCountMap = null;
        Map<Long, List<Long>> perMonthCountMap = null;
        if(recordPageRequest.isExport()){
            fetchPlanCountMap = insuranceFetchPlanService.getFetchPlanCountMap(null,idList);
            perMonthCountMap = insuranceFetchPlanDetailService.getPerMonthCountMap(null,idList);
        }
        Map<Long, Long> finalFetchPlanCountMap = fetchPlanCountMap;
        Map<Long, List<Long>> finalPerMonthCountMap = perMonthCountMap;
        insuranceRecordDOS.forEach(insuranceRecordBO -> {
            if(!finalMap.isEmpty()){
                List<InsuranceFetchPlanDO> insuranceFetchPlanDOS = finalMap.get(insuranceRecordBO.getInsuranceRecordId());
                if(CollUtil.isNotEmpty(insuranceFetchPlanDOS)){
                    insuranceRecordBO.setCashTimes(insuranceFetchPlanDOS.size());
                }else{
                    insuranceRecordBO.setCashTimes(0);
                }
            }else{
                insuranceRecordBO.setCashTimes(0);
            }
            if(Objects.nonNull(insuranceDOMap.get(insuranceRecordBO.getInsuranceId()))){
                insuranceRecordBO.setInsuranceName(insuranceDOMap.get(insuranceRecordBO.getInsuranceId()).getInsuranceName());
            }
            InsuranceCompanyDO insuranceCompanyDO = companyDOMap.get(insuranceRecordBO.getInsuranceCompanyId());
            if(Objects.nonNull(insuranceCompanyDO)){
                insuranceRecordBO.setCompanyName(insuranceCompanyDO.getCompanyName());
            }
            insuranceRecordBO.setAmount(BigDecimal.valueOf(insuranceRecordBO.getPreAmount()).divide(new BigDecimal(100)).setScale(2));
            if(Objects.nonNull(finalFetchPlanCountMap) && Objects.nonNull(finalPerMonthCountMap)){
                List<Long> monthList = finalPerMonthCountMap.getOrDefault(insuranceRecordBO.getInsuranceRecordId(), null);
                if(CollUtil.isEmpty(monthList)){
                    return;
                }
                Long planCount = finalFetchPlanCountMap.getOrDefault(insuranceRecordBO.getInsuranceRecordId(), 0L);
                List<Long> mul = Lists.newArrayList();
                monthList.forEach(m->{
                    mul.add(m*planCount) ;
                });
                insuranceRecordBO.setTotalCashCount(mul.stream().mapToLong(Long::longValue).sum());
                List<Long> mulOfCashed = Lists.newArrayList();
                monthList.forEach(m->{
                    mulOfCashed.add(m*insuranceRecordBO.getCashTimes()) ;
                });
                insuranceRecordBO.setCashedTotal(mulOfCashed.stream().mapToLong(Long::longValue).sum());
            }
        });
        return insuranceRecordPayBOPage;
    }

    @Override
    public List<InsuranceRecordPayDTO> queryByInsuranceRecordId(Long insuranceRecordId) {
        QueryWrapper<InsuranceRecordPayDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceRecordPayDO::getInsuranceRecordId, insuranceRecordId);
        List<InsuranceRecordPayDO> list = this.list(wrapper);
        return PojoUtils.map(list, InsuranceRecordPayDTO.class);
    }

    @Override
    public InsuranceRecordPayDTO queryById(Long recordPayId) {
        QueryWrapper<InsuranceRecordPayDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceRecordPayDO::getId, recordPayId);
        InsuranceRecordPayDO recordPayDO = this.getOne(wrapper);
        return PojoUtils.map(recordPayDO, InsuranceRecordPayDTO.class);
    }
}
