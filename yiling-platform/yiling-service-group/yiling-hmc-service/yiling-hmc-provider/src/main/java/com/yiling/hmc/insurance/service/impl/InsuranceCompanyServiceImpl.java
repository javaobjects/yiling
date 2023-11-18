package com.yiling.hmc.insurance.service.impl;

import java.util.List;

import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.enums.InsuranceCompanyStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.dao.InsuranceCompanyMapper;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyDeleteRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyListRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyPageRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanySaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyStatusRequest;
import com.yiling.hmc.insurance.entity.InsuranceCompanyDO;
import com.yiling.hmc.insurance.enums.HmcInsuranceCompanyErrorCode;
import com.yiling.hmc.insurance.service.InsuranceCompanyService;

/**
 * <p>
 * 保险公司表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Service
public class InsuranceCompanyServiceImpl extends BaseServiceImpl<InsuranceCompanyMapper, InsuranceCompanyDO> implements InsuranceCompanyService {

    @DubboReference
    LocationApi locationApi;

    @Override
    public List<InsuranceCompanyDO> listByCondition(InsuranceCompanyListRequest request) {
        QueryWrapper<InsuranceCompanyDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(request.getCompanyName())) {
            wrapper.lambda().eq(InsuranceCompanyDO::getCompanyName, request.getCompanyName());
        }
        return this.list(wrapper);
    }

    @Override
    public boolean saveInsuranceCompany(InsuranceCompanySaveRequest request) {
        // 校验省市区
        boolean flag = locationApi.validateCode(request.getProvinceCode(), request.getCityCode(), request.getRegionCode());
        if (!flag) {
            throw new BusinessException(HmcInsuranceCompanyErrorCode.SAVE_INSURANCE_COMPANY_AREA_ERROR);
        }
        String[] locations = locationApi.getNamesByCodes(request.getProvinceCode(), request.getCityCode(), request.getRegionCode());
        request.setProvinceName(locations[0]);
        request.setCityName(locations[1]);
        request.setRegionName(locations[2]);

        InsuranceCompanyDO insuranceCompanyDO = PojoUtils.map(request, InsuranceCompanyDO.class);
        if (null == request.getId()) {
            return this.save(insuranceCompanyDO);
        } else {
            return this.updateById(insuranceCompanyDO);
        }
    }

    @Override
    public Page<InsuranceCompanyDO> pageList(InsuranceCompanyPageRequest request) {
        QueryWrapper<InsuranceCompanyDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(request.getCompanyName())) {
            wrapper.lambda().like(InsuranceCompanyDO::getCompanyName, request.getCompanyName());
        }
        wrapper.lambda().orderByDesc(InsuranceCompanyDO::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public boolean deleteInsuranceCompany(InsuranceCompanyDeleteRequest request) {
        InsuranceCompanyDO insuranceCompanyDO = PojoUtils.map(request, InsuranceCompanyDO.class);
        return this.deleteByIdWithFill(insuranceCompanyDO) > 0;
    }

    @Override
    public boolean modifyStatus(InsuranceCompanyStatusRequest request) {
        InsuranceCompanyDO insuranceCompanyDO = PojoUtils.map(request, InsuranceCompanyDO.class);
        return this.updateById(insuranceCompanyDO);
    }

    @Override
    public List<InsuranceCompanyDTO> listByIdList(List<Long> insuranceCompanyIdList) {
        QueryWrapper<InsuranceCompanyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(InsuranceCompanyDO::getId, insuranceCompanyIdList);
        wrapper.lambda().eq(InsuranceCompanyDO::getStatus, InsuranceCompanyStatusEnum.ENABLE.getCode());
        List<InsuranceCompanyDO> list = this.list(wrapper);
        return PojoUtils.map(list, InsuranceCompanyDTO.class);
    }
}
