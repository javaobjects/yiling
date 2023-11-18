package com.yiling.sjms.agency.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.dao.AgencyLockRelationShipMapper;
import com.yiling.sjms.agency.dto.AgencyLockRelationShipDTO;
import com.yiling.sjms.agency.entity.AgencyLockRelationShipDO;
import com.yiling.sjms.agency.service.AgencyLockRelationShipService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 机构锁定三者关系表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-02-23
 */
@Service
public class AgencyLockRelationShipServiceImpl extends BaseServiceImpl<AgencyLockRelationShipMapper, AgencyLockRelationShipDO> implements AgencyLockRelationShipService {

    @Override
    public List<AgencyLockRelationShipDTO> listByForm(Long agencyFormId, Long crmEnterpriseId) {
        if (ObjectUtil.isNull(agencyFormId) && ObjectUtil.isNull(agencyFormId)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<AgencyLockRelationShipDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(agencyFormId), AgencyLockRelationShipDO::getAgencyFormId, agencyFormId);
        wrapper.eq(ObjectUtil.isNotNull(crmEnterpriseId), AgencyLockRelationShipDO::getCrmEnterpriseId, crmEnterpriseId);
        return PojoUtils.map(list(wrapper), AgencyLockRelationShipDTO.class);
    }

    @Override
    public Map<Long, List<AgencyLockRelationShipDTO>> queryRelationListByAgencyFormId(List<Long> agencyFormIdList) {
        if (CollUtil.isEmpty(agencyFormIdList)) {
            return MapUtil.newHashMap();
        }
        LambdaQueryWrapper<AgencyLockRelationShipDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(AgencyLockRelationShipDO::getAgencyFormId, agencyFormIdList);
        List<AgencyLockRelationShipDO> list = list();
        List<AgencyLockRelationShipDTO> shipDTOList = PojoUtils.map(list, AgencyLockRelationShipDTO.class);

        return shipDTOList.stream().collect(Collectors.groupingBy(AgencyLockRelationShipDTO::getAgencyFormId));
    }
}
