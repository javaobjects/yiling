package com.yiling.hmc.insurance.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.dao.InsuranceMapper;
import com.yiling.hmc.insurance.dto.InsuranceDetailDTO;
import com.yiling.hmc.insurance.dto.InsuranceGoodsDTO;
import com.yiling.hmc.insurance.dto.InsurancePageDTO;
import com.yiling.hmc.insurance.dto.request.InsuranceDetailSaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceListRequest;
import com.yiling.hmc.insurance.dto.request.InsurancePageRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceSaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceStatusRequest;
import com.yiling.hmc.insurance.entity.InsuranceDO;
import com.yiling.hmc.insurance.entity.InsuranceDetailDO;
import com.yiling.hmc.insurance.enums.HmcInsuranceErrorCode;
import com.yiling.hmc.insurance.enums.InsuranceStatusEnum;
import com.yiling.hmc.insurance.service.InsuranceDetailService;
import com.yiling.hmc.insurance.service.InsuranceService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import io.seata.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * C端保险保险表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InsuranceServiceImpl extends BaseServiceImpl<InsuranceMapper, InsuranceDO> implements InsuranceService {

    private final InsuranceDetailService insuranceDetailService;

    @Override
    public boolean saveInsuranceAndDetail(InsuranceSaveRequest request) {

        // 校验请求参数是否正确
        checkSaveInsuranceAndDetailRequest(request);

        // 1.保存保险信息
        InsuranceDO insuranceDO = saveInsurance(request);

        // 2.保存保险明细信息
        if (null == request.getId()) {
            request.setId(insuranceDO.getId());
            return insuranceDetailService.saveInsuranceDetail(request);
        } else {
            return insuranceDetailService.saveEditInsuranceDetail(request);
        }
    }

    /**
     * 保险新增时的数据校验
     *
     * @param request 保险新增请求参数
     */
    private void checkSaveInsuranceAndDetailRequest(InsuranceSaveRequest request) {
        // 保险的定额付费类型 季度和年不能重复
        checkIdentification(request.getId(), request.getInsuranceCompanyId(), request.getQuarterIdentification(), request.getYearIdentification());
        // 保险商品不能已经参与过企业其它保险
        List<InsuranceDetailSaveRequest> insuranceDetailSaveList = request.getInsuranceDetailSaveList();
        List<Long> controlIdList = insuranceDetailSaveList.stream().map(InsuranceDetailSaveRequest::getControlId).collect(Collectors.toList());

        List<InsuranceDetailDO> detailDOList = insuranceDetailService.listByControlIdAndCompanyAndInsuranceStatus(controlIdList, request.getInsuranceCompanyId(), InsuranceStatusEnum.ENABLE.getCode());
        List<Long> insuranceIdList = detailDOList.stream().map(InsuranceDetailDO::getInsuranceId).collect(Collectors.toList());
        // 这些商品在此保险公司某些启用的保险中存在
        if (CollUtil.isNotEmpty(insuranceIdList)) {
            if (null == request.getId()) {
                // 保险信息新增
                throw new BusinessException(HmcInsuranceErrorCode.INSURANCE_DETAIL_EXISTS);
            } else {
                // 保险信息修改
                List<Long> idList = insuranceIdList.stream().filter(e -> !request.getId().equals(e)).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(idList)) {
                    throw new BusinessException(HmcInsuranceErrorCode.INSURANCE_DETAIL_EXISTS);
                }
            }
        }
    }

    /**
     * 校验保险的定额标识是否重复
     *
     * @param insuranceId 保险id
     * @param insuranceCompanyId 保险公司id
     * @param quarterIdentification 定额类型季度标识
     * @param yearIdentification 定额类型年标识
     */
    private void checkIdentification(Long insuranceId, Long insuranceCompanyId, String quarterIdentification, String yearIdentification) {
        QueryWrapper<InsuranceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceDO::getInsuranceCompanyId, insuranceCompanyId);
        wrapper.lambda().eq(InsuranceDO::getStatus, 1);
        if (null != insuranceId) {
            wrapper.lambda().ne(InsuranceDO::getId, insuranceId);
        }
        wrapper.lambda().and(wrapperOr -> wrapperOr.eq(InsuranceDO::getQuarterIdentification, quarterIdentification).or().eq(InsuranceDO::getQuarterIdentification, yearIdentification).or().eq(InsuranceDO::getYearIdentification, quarterIdentification).or().eq(InsuranceDO::getYearIdentification, yearIdentification));
        List<InsuranceDO> doList = this.list(wrapper);
        if (CollUtil.isNotEmpty(doList)) {
            throw new BusinessException(HmcInsuranceErrorCode.INSURANCE_IDENTIFICATION_REPEAT);
        }
    }

    @Override
    public InsuranceDO saveInsurance(InsuranceSaveRequest request) {
        InsuranceDO insuranceDO = PojoUtils.map(request, InsuranceDO.class);
        if (null == request.getId()) {
            this.save(insuranceDO);
        } else {
            this.updateById(insuranceDO);
        }
        return insuranceDO;
    }

    @Override
    public Page<InsurancePageDTO> pageList(InsurancePageRequest request) {
        QueryWrapper<InsuranceDO> wrapper = new QueryWrapper<>();
        if (null != request.getInsuranceCompanyId()) {
            wrapper.lambda().eq(InsuranceDO::getInsuranceCompanyId, request.getInsuranceCompanyId());
        }
        if (StringUtils.isNotBlank(request.getInsuranceName())) {
            wrapper.lambda().like(InsuranceDO::getInsuranceName, request.getInsuranceName());
        }
        if (null != request.getStartTime() && null != request.getStopTime()) {
            wrapper.lambda().ge(InsuranceDO::getCreateTime, DateUtil.beginOfDay(request.getStartTime()));
            wrapper.lambda().le(InsuranceDO::getCreateTime, DateUtil.endOfDay(request.getStopTime()));
        }
        wrapper.lambda().orderByDesc(InsuranceDO::getCreateTime);
        Page<InsuranceDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<InsurancePageDTO> dtoPage = PojoUtils.map(page, InsurancePageDTO.class);
        dtoPage.getRecords().forEach(item -> {
            List<InsuranceDetailDO> detailDOList = insuranceDetailService.listByInsuranceId(item.getId());
            List<InsuranceDetailDTO> detailDTOList = PojoUtils.map(detailDOList, InsuranceDetailDTO.class);
            item.setInsuranceDetailDTOList(detailDTOList);
        });
        return dtoPage;
    }

    @Override
    public List<InsuranceDO> listByCondition(InsuranceListRequest request) {
        QueryWrapper<InsuranceDO> wrapper = new QueryWrapper<>();
        if (null != request.getInsuranceCompanyId()) {
            wrapper.lambda().eq(InsuranceDO::getInsuranceCompanyId, request.getInsuranceCompanyId());
        }
        if (StringUtils.isNotBlank(request.getInsuranceName())) {
            wrapper.lambda().like(InsuranceDO::getInsuranceName, request.getInsuranceName());
        }
        if (null != request.getStatus() && 0 != request.getStatus()) {
            wrapper.lambda().eq(InsuranceDO::getStatus, request.getStatus());
        }
        if (StringUtils.isNotBlank(request.getQuarterIdentification())) {
            wrapper.lambda().eq(InsuranceDO::getQuarterIdentification, request.getQuarterIdentification());
        }
        if (StringUtils.isNotBlank(request.getYearIdentification())) {
            wrapper.lambda().eq(InsuranceDO::getYearIdentification, request.getYearIdentification());
        }
        return this.list(wrapper);
    }

    @Override
    public List<InsuranceDO> listByCompanyIdAndStatus(Long insuranceCompanyId, InsuranceStatusEnum insuranceStatusEnum) {
        QueryWrapper<InsuranceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceDO::getInsuranceCompanyId, insuranceCompanyId);
        if (null != insuranceStatusEnum) {
            wrapper.lambda().eq(InsuranceDO::getStatus, insuranceStatusEnum.getCode());
        }
        return this.list(wrapper);
    }

    @Override
    public boolean modifyStatus(InsuranceStatusRequest request) {
        InsuranceDO insuranceDO = PojoUtils.map(request, InsuranceDO.class);

        // 启用的时候要判断这个保险商品是否在别的保险中也使用了
        checkModifyStatusRequest(request);

        return this.updateById(insuranceDO);
    }

    /**
     * 启用的时候要判断这个保险商品是否在别的保险中也使用了
     *
     * @param request 请求参数
     */
    private void checkModifyStatusRequest(InsuranceStatusRequest request) {
        if (InsuranceStatusEnum.ENABLE == InsuranceStatusEnum.getByCode(request.getStatus())) {
            InsuranceDO insurance = this.getById(request.getId());
            // 校验保司标识(检测到有相同保司标识的服务项开启中，不允许开启)
            checkIdentification(insurance.getId(), insurance.getInsuranceCompanyId(), insurance.getQuarterIdentification(), insurance.getYearIdentification());

            // 启用的时候要判断这个保险商品是否在别的保险中也使用了
            List<InsuranceDetailDO> insuranceDetailDOList = insuranceDetailService.listByInsuranceId(request.getId());
            List<Long> controlIdList = insuranceDetailDOList.stream().map(InsuranceDetailDO::getControlId).collect(Collectors.toList());
            List<InsuranceDetailDO> detailDOList = insuranceDetailService.listByControlIdAndCompanyAndInsuranceStatus(controlIdList, insurance.getInsuranceCompanyId(), InsuranceStatusEnum.ENABLE.getCode());
            if (CollUtil.isNotEmpty(detailDOList)) {
                throw new BusinessException(HmcInsuranceErrorCode.INSURANCE_DETAIL_EXISTS_ERROR);
            }
        }
    }

    @Override
    public List<InsurancePageDTO> getAll() {
        QueryWrapper<InsuranceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByDesc(InsuranceDO::getCreateTime);
        wrapper.lambda().last("limit 10");
        List<InsuranceDO> insuranceDOList = this.list(wrapper);
        List<InsurancePageDTO> dtoList = PojoUtils.map(insuranceDOList, InsurancePageDTO.class);
        dtoList.forEach(item -> {
            List<InsuranceDetailDO> detailDOList = insuranceDetailService.listByInsuranceId(item.getId());
            List<InsuranceDetailDTO> detailDTOList = PojoUtils.map(detailDOList, InsuranceDetailDTO.class);
            item.setInsuranceDetailDTOList(detailDTOList);
        });
        return dtoList;
    }

    @Override
    public List<InsuranceDO> listByIdListAndCompanyAndStatus(List<Long> insuranceIdList, Long insuranceCompanyId, InsuranceStatusEnum insuranceStatusEnum) {
        QueryWrapper<InsuranceDO> wrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(insuranceIdList)) {
            wrapper.lambda().in(InsuranceDO::getId, insuranceIdList);
        }
        if (null != insuranceCompanyId) {
            wrapper.lambda().eq(InsuranceDO::getInsuranceCompanyId, insuranceCompanyId);
        }
        if (null != insuranceStatusEnum) {
            wrapper.lambda().eq(InsuranceDO::getStatus, insuranceStatusEnum.getCode());
        }
        return this.list(wrapper);
    }

    @Override
    public List<InsuranceGoodsDTO> queryGoods() {
        return this.getBaseMapper().queryGoods();
    }
}
