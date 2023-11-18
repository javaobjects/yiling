package com.yiling.dataflow.crm.api.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.crm.api.CrmManorRepresentativeApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationManorDTO;
import com.yiling.dataflow.crm.dto.CrmManorRepresentativeDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorRepresentativePageRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmManorRepresentativeRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.entity.CrmManorRepresentativeDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationManorService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmManorRepresentativeService;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 辖区代表
 */
@DubboService
@Slf4j
public class CrmManorRepresentativeApiImpl implements CrmManorRepresentativeApi {
    @Resource
    private CrmManorRepresentativeService crmManorRepresentativeService;
    @Resource
    private CrmEnterpriseRelationManorService crmEnterpriseRelationManorService;
    @Resource
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Override
    public Page<CrmManorRepresentativeDTO> pageList(QueryCrmManorRepresentativePageRequest request) {
        if (ObjectUtil.isNotEmpty(request.getBeginTime())) {
            request.setBeginTime(DateUtil.parse(DateUtil.format(request.getBeginTime(), "yyyy-MM-dd 00:00:00")));
        }
        if (ObjectUtil.isNotEmpty(request.getEndTime())) {
            request.setEndTime(DateUtil.parse(DateUtil.format(request.getEndTime(), "yyyy-MM-dd 23:59:59")));
        }
        return crmManorRepresentativeService.pageList(request);
    }

    @Override
    public CrmManorRepresentativeDTO getByManorId(Long manorId) {
        return crmManorRepresentativeService.getByManorId(manorId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Long saveOrUpdate(SaveCrmManorRepresentativeRequest request) {
        CrmManorRepresentativeDTO byManorId = crmManorRepresentativeService.getByManorId(request.getManorId());
        CrmManorRepresentativeDO crmManorRepresentativeDO = new CrmManorRepresentativeDO();
        //不存在保存
        if (Objects.isNull(byManorId)) {
            PojoUtils.map(request, crmManorRepresentativeDO);
        } else {
            PojoUtils.map(byManorId, crmManorRepresentativeDO);
            //岗位编码一致的情况不更新
            crmManorRepresentativeDO.setRepresentativePostCode(request.getRepresentativePostCode());
        }
        crmManorRepresentativeService.saveOrUpdate(crmManorRepresentativeDO);
        //插入三者关系更新
        List<CrmEnterpriseRelationManorDTO> byRelations = crmEnterpriseRelationManorService.getByManorId(request.getManorId());
        //三者关系分组
        if (CollUtil.isNotEmpty(byRelations)) {
            List<Long> crmEIds = byRelations.stream().map(CrmEnterpriseRelationManorDTO::getCrmEnterpriseId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            List<CrmEnterpriseRelationShipDO> relationShipDOS = crmEnterpriseRelationShipService.listByEidsAndRole(crmEIds, 2);
            Map<String, CrmEnterpriseRelationShipDO> keyManorRelationMap = Optional.ofNullable(relationShipDOS.stream().collect(Collectors.toMap(this::keyManorRelation, m -> m, (v1, v2) -> v1))).orElse(MapUtil.newHashMap());
            List<CrmEnterpriseRelationShipDO> saveCrmRelationshipRequestList = new ArrayList<>();
            byRelations.stream().forEach(item -> {
                CrmEnterpriseRelationShipDO b = keyManorRelationMap.get(String.format("%s%s%s", item.getCrmManorId(), item.getCategoryId(), item.getCrmEnterpriseId()));
                if (b == null) {
                    b = new CrmEnterpriseRelationShipDO();
                    b.setCreateUser(request.getOpUserId());
                    b.setCreateTime(request.getOpTime());
                    b.setRemark("辖区编辑岗位添加");
                }
                b.setPostCode(request.getRepresentativePostCode());
                b.setPostName(request.getRepresentativePostName());
                b.setCrmEnterpriseId(item.getCrmEnterpriseId());
                b.setCustomerName(item.getCrmEnterpriseName());
                b.setManorId(item.getCrmManorId());
                b.setCategoryId(item.getCategoryId());
                b.setUpdateTime(request.getOpTime());
                b.setUpdateUser(request.getOpUserId());
                b.setSupplyChainRoleType(AgencySupplyChainRoleEnum.HOSPITAL.getCode());
                b.setSupplyChainRole(AgencySupplyChainRoleEnum.HOSPITAL.getName());
                saveCrmRelationshipRequestList.add(b);
            });
            if (CollUtil.isNotEmpty(saveCrmRelationshipRequestList)) {
                log.info("辖区保存三者关系:{}", saveCrmRelationshipRequestList);
                crmEnterpriseRelationShipService.saveOrUpdateBatch(saveCrmRelationshipRequestList);

            }
        }
        return crmManorRepresentativeDO.getId();
    }

    //辖区ID+品类+机构编码 唯一  三者关系 医院类型
    public String keyManorRelation(CrmEnterpriseRelationShipDO relationShipDO) {
        return String.format("%s%s%s", relationShipDO.getManorId(), relationShipDO.getCategoryId(), relationShipDO.getCrmEnterpriseId());
    }
}
