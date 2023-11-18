package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.enterprise.entity.EnterpriseHighQualitySupplierDO;
import com.yiling.user.enterprise.dao.EnterpriseHighQualitySupplierMapper;
import com.yiling.user.enterprise.service.EnterpriseHighQualitySupplierService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 优质商家表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-10
 */
@Slf4j
@Service
public class EnterpriseHighQualitySupplierServiceImpl extends BaseServiceImpl<EnterpriseHighQualitySupplierMapper, EnterpriseHighQualitySupplierDO> implements EnterpriseHighQualitySupplierService {

    @Override
    public List<Long> getAllSupplier() {
        List<EnterpriseHighQualitySupplierDO> list = this.list();
        return list.stream().map(EnterpriseHighQualitySupplierDO::getEid).distinct().collect(Collectors.toList());
    }

    @Override
    public List<Long> getByEidList(List<Long> eidList) {
        if (CollUtil.isEmpty(eidList)) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<EnterpriseHighQualitySupplierDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EnterpriseHighQualitySupplierDO::getEid, eidList);
        List<EnterpriseHighQualitySupplierDO> list = this.list(wrapper);
        return list.stream().map(EnterpriseHighQualitySupplierDO::getEid).distinct().collect(Collectors.toList());
    }

    @Override
    public boolean getHighQualitySupplierFlag(Long eid) {
        LambdaQueryWrapper<EnterpriseHighQualitySupplierDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseHighQualitySupplierDO::getEid, eid);
        wrapper.last("limit 1");
        return Objects.nonNull(this.getOne(wrapper));
    }

    @Override
    public List<Long> getByProvinceCode(String provinceCode) {
        LambdaQueryWrapper<EnterpriseHighQualitySupplierDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseHighQualitySupplierDO::getProvinceCode, provinceCode);
        List<EnterpriseHighQualitySupplierDO> list = this.list(wrapper);
        return list.stream().map(EnterpriseHighQualitySupplierDO::getEid).distinct().collect(Collectors.toList());
    }

}
