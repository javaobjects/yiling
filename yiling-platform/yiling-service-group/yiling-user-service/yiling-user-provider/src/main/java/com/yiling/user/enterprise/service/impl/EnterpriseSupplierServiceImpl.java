package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.enterprise.bo.EnterpriseSupplierBO;
import com.yiling.user.enterprise.dto.EnterpriseSupplierDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.dto.request.QuerySupplierPageRequest;
import com.yiling.user.enterprise.dto.request.SaveOrDeleteSupplierRequest;
import com.yiling.user.enterprise.dto.request.UpdateSupplierRequest;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.entity.EnterpriseSupplierDO;
import com.yiling.user.enterprise.dao.EnterpriseSupplierMapper;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.enterprise.service.EnterpriseSupplierService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 供应商管理表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-15
 */
@Slf4j
@Service
public class EnterpriseSupplierServiceImpl extends BaseServiceImpl<EnterpriseSupplierMapper, EnterpriseSupplierDO> implements EnterpriseSupplierService {

    @Autowired
    EnterpriseService enterpriseService;

    @Override
    public Page<EnterpriseSupplierBO> queryListPage(QuerySupplierPageRequest request) {
        QueryWrapper<EnterpriseSupplierDO> wrapper = WrapperUtils.getWrapper(request);

        if (StrUtil.isNotEmpty(request.getLicenseNumber())) {
            EnterpriseDO enterpriseDO = enterpriseService.getByLicenseNumber(request.getLicenseNumber());
            if (Objects.isNull(enterpriseDO)) {
                return new Page<>(request.getCurrent(), request.getSize());
            } else {
                if (Objects.nonNull(request.getEid()) && request.getEid() != 0) {
                    if (enterpriseDO.getId().compareTo(request.getEid()) != 0) {
                        return new Page<>(request.getCurrent(), request.getSize());
                    }
                } else {
                    wrapper.lambda().eq(EnterpriseSupplierDO::getEid, enterpriseDO.getId());
                }
            }
        }
        wrapper.lambda().orderByDesc(EnterpriseSupplierDO::getCreateTime);

        Page<EnterpriseSupplierDO> supplierDOPage = this.page(request.getPage(), wrapper);
        Page<EnterpriseSupplierBO> boPage = PojoUtils.map(supplierDOPage, EnterpriseSupplierBO.class);

        List<Long> eidList = boPage.getRecords().stream().map(EnterpriseSupplierBO::getEid).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(eidList)) {
            return boPage;
        }

        Map<Long, EnterpriseDO> enterpriseDOMap = enterpriseService.listByIds(eidList).stream().collect(Collectors.toMap(BaseDO::getId, Function.identity()));
        boPage.getRecords().forEach(enterpriseSupplierBO -> {
            EnterpriseDO enterpriseDO = enterpriseDOMap.getOrDefault(enterpriseSupplierBO.getEid(), new EnterpriseDO());
            enterpriseSupplierBO.setEname(enterpriseDO.getName());
            enterpriseSupplierBO.setLicenseNumber(enterpriseDO.getLicenseNumber());
        });

        return boPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertSupplier(SaveOrDeleteSupplierRequest request) {
        EnterpriseSupplierDTO supplierDTO = this.getSupplier(request.getCustomerEid(), request.getEid());
        if (Objects.nonNull(supplierDTO)) {
            return true;
        }

        EnterpriseSupplierDO supplierDO = PojoUtils.map(request, EnterpriseSupplierDO.class);
        return this.save(supplierDO);
    }

    @Override
    public EnterpriseSupplierDTO getSupplier(Long customerEid, Long eid) {
        LambdaQueryWrapper<EnterpriseSupplierDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseSupplierDO::getCustomerEid, customerEid);
        wrapper.eq(EnterpriseSupplierDO::getEid, eid);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), EnterpriseSupplierDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSupplier(SaveOrDeleteSupplierRequest request) {
        EnterpriseSupplierDTO supplierDTO = this.getSupplier(request.getCustomerEid(), request.getEid());
        if (Objects.isNull(supplierDTO)) {
            return false;
        }

        EnterpriseSupplierDO supplierDO = PojoUtils.map(supplierDTO, EnterpriseSupplierDO.class);
        return this.deleteByIdWithFill(supplierDO) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSupplier(UpdateSupplierRequest request) {
        EnterpriseSupplierDO enterpriseSupplierDO = PojoUtils.map(request, EnterpriseSupplierDO.class);
        return this.updateById(enterpriseSupplierDO);
    }

    @Override
    public EnterpriseSupplierBO get(Long id) {
        EnterpriseSupplierDO supplierDO = this.getById(id);
        EnterpriseSupplierBO supplierBO = PojoUtils.map(supplierDO, EnterpriseSupplierBO.class);

        EnterpriseDO enterpriseDO = enterpriseService.getById(supplierBO.getEid());
        if (Objects.nonNull(enterpriseDO)) {
            supplierBO.setEname(enterpriseDO.getName());
            supplierBO.setLicenseNumber(enterpriseDO.getLicenseNumber());
        }
        return supplierBO;
    }

}
