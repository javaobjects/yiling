package com.yiling.user.lockcustomer.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.lockcustomer.dao.LockCustomerMapper;
import com.yiling.user.lockcustomer.dto.LockCustomerDTO;
import com.yiling.user.lockcustomer.dto.request.AddLockCustomerRequest;
import com.yiling.user.lockcustomer.dto.request.ImportLockCustomerRequest;
import com.yiling.user.lockcustomer.dto.request.QueryLockCustomerPageRequest;
import com.yiling.user.lockcustomer.dto.request.UpdateLockCustomerStatusRequest;
import com.yiling.user.lockcustomer.entity.LockCustomerDO;
import com.yiling.user.lockcustomer.service.LockCustomerService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.common.util.WrapperUtils;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 销售助手-锁定用户表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-02
 */
@Service
public class LockCustomerServiceImpl extends BaseServiceImpl<LockCustomerMapper, LockCustomerDO> implements LockCustomerService {

    @DubboReference
    LocationApi locationApi;

    @Override
    public Page<LockCustomerDTO> queryListPage(QueryLockCustomerPageRequest request) {
        QueryWrapper<LockCustomerDO> wrapper = WrapperUtils.getWrapper(request);
        return PojoUtils.map(this.page(request.getPage(), wrapper), LockCustomerDTO.class);
    }

    @Override
    public List<LockCustomerDTO> queryList(QueryLockCustomerPageRequest request) {
        QueryWrapper<LockCustomerDO> wrapper = WrapperUtils.getWrapper(request);
        return PojoUtils.map(this.list(wrapper), LockCustomerDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addLockCustomer(AddLockCustomerRequest request) {
        // 根据信用代码校验是否存在
        LambdaQueryWrapper<LockCustomerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockCustomerDO::getLicenseNumber, request.getLicenseNumber());
        wrapper.last("limit 1");
        LockCustomerDO customerDO = this.getOne(wrapper);
        if (Objects.nonNull(customerDO)) {
            throw new BusinessException(UserErrorCode.LOCK_CUSTOMER_EXIST);
        }

        // 校验省市区
        boolean flag = locationApi.validateCode(request.getProvinceCode(), request.getCityCode(), request.getRegionCode());
        if (!flag) {
            throw new BusinessException(UserErrorCode.ADD_LOCK_CUSTOMER_AREA_ERROR);
        }
        String[] locations = locationApi.getNamesByCodes(request.getProvinceCode(), request.getCityCode(), request.getRegionCode());
        request.setProvinceName(locations[0]);
        request.setCityName(locations[1]);
        request.setRegionName(locations[2]);

        LockCustomerDO lockCustomerDO = PojoUtils.map(request, LockCustomerDO.class);
        return this.save(lockCustomerDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(UpdateLockCustomerStatusRequest request) {
        LockCustomerDO lockCustomerDO = new LockCustomerDO();
        lockCustomerDO.setStatus(request.getStatus());
        lockCustomerDO.setOpUserId(request.getOpUserId());

        LambdaQueryWrapper<LockCustomerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BaseDO::getId, request.getIdList());

        return this.update(lockCustomerDO, wrapper);
    }

    @Override
    public LockCustomerDTO getByLicenseNumber(String licenseNumber) {
        LambdaQueryWrapper<LockCustomerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockCustomerDO::getLicenseNumber, licenseNumber);
        wrapper.last("limit 1");

        return PojoUtils.map(this.getOne(wrapper), LockCustomerDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importData(ImportLockCustomerRequest request) {
        LockCustomerDO lockCustomerDO = PojoUtils.map(request, LockCustomerDO.class);
        return this.save(lockCustomerDO);
    }

    @Override
    public Map<String, Integer> batchQueryByLicenseNumber(List<String> licenseNumberList) {
        if (CollUtil.isEmpty(licenseNumberList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<LockCustomerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(LockCustomerDO::getLicenseNumber, licenseNumberList);
        List<LockCustomerDO> list = this.list(wrapper);

        return list.stream().collect(Collectors.toMap(LockCustomerDO::getLicenseNumber, LockCustomerDO::getStatus, (k1,k2) -> k2));
    }

}
