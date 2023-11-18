package com.yiling.dataflow.agency.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.agency.dao.CrmPharmacyMapper;
import com.yiling.dataflow.agency.dto.request.RemoveCrmPharmacyRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmPharmacyRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmRelationshipRequest;
import com.yiling.dataflow.agency.entity.CrmPharmacyDO;
import com.yiling.dataflow.agency.service.CrmPharmacyService;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 零售机构档案扩展表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CrmPharmacyServiceImpl extends BaseServiceImpl<CrmPharmacyMapper, CrmPharmacyDO> implements CrmPharmacyService {

    private final CrmEnterpriseService crmEnterpriseService;

    private final CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Override
    public List<CrmPharmacyDO> listByCrmEnterpriseId(List<Long> crmEnterpriseIdList) {
        LambdaQueryWrapper<CrmPharmacyDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmPharmacyDO::getCrmEnterpriseId, crmEnterpriseIdList);
        return this.list(wrapper);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmPharmacyDO> listSuffixByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList, String tableSuffix) {
        return this.listByCrmEnterpriseId(crmEnterpriseIdList);
    }

    @Override
    public CrmPharmacyDO queryByEnterpriseId(Long crmEnterpriseId) {
        LambdaQueryWrapper<CrmPharmacyDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmPharmacyDO::getCrmEnterpriseId, crmEnterpriseId);
        wrapper.last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public boolean saveCrmPharmacy(SaveCrmPharmacyRequest request) {


        CrmPharmacyDO crmPharmacyDO = PojoUtils.map(request, CrmPharmacyDO.class);

        CrmPharmacyDO pharmacyDO = Objects.isNull(request.getId())?null: this.getById(crmPharmacyDO.getId());

        // 保存三者关系
        // 新增可以编辑，编辑只能新增。
        // 有id的不管，没id的新增
        List<SaveCrmRelationshipRequest> crmRelationShip = request.getCrmRelationShip();
        List<SaveCrmRelationshipRequest> saveCrmRelationshipRequestList = Optional.ofNullable( crmRelationShip.stream().filter(item -> ObjectUtil.isNull(item.getId())).collect(Collectors.toList())).orElse(ListUtil.empty());
        //商业首次锁定时间 2个list相等的情况>0并且长度相等
        if(CollUtil.isNotEmpty(crmRelationShip)&&CollUtil.isNotEmpty(saveCrmRelationshipRequestList)&&saveCrmRelationshipRequestList.size()==crmRelationShip.size()){
            crmPharmacyDO.setFirstLockTime(new Date());
        }
        if (Objects.isNull(pharmacyDO)) {
            this.save(crmPharmacyDO);
        } else {
            this.updateById(crmPharmacyDO);
        }
        if (CollectionUtils.isEmpty(request.getCrmRelationShip())||CollectionUtils.isEmpty(saveCrmRelationshipRequestList)) {
            return true;
        }
        saveCrmRelationshipRequestList.forEach(item -> {
            if(StringUtils.equals(item.getDutyGredeId(),"2")){
                item.setPostCode(item.getSuperiorJob());
            }
            item.setOpUserId(request.getOpUserId());
            item.setCrmEnterpriseId(request.getCrmEnterpriseId());
            item.setCustomerName(request.getName());
            // 1 供应商 2 医院 3 药店
            item.setSupplyChainRole(CrmSupplyChainRoleEnum.PHARMACY.getName());
            item.setSupplyChainRoleType(CrmSupplyChainRoleEnum.PHARMACY.getCode());
        });
        crmEnterpriseRelationShipService.saveBatch(PojoUtils.map(saveCrmRelationshipRequestList, CrmEnterpriseRelationShipDO.class));

        return true;
    }

    @Override
    @Transactional
    public boolean removeByEnterpriseId(RemoveCrmPharmacyRequest request) {
        if (Objects.isNull(request.getCrmEnterpriseId())) {
            return false;
        }

        // 1.删除基础信息
        CrmEnterpriseDO crmEnterpriseDO = PojoUtils.map(request, CrmEnterpriseDO.class);
        crmEnterpriseDO.setId(request.getCrmEnterpriseId());
        if (crmEnterpriseService.deleteByIdWithFill(crmEnterpriseDO) < 1) {
            return false;
        }

        // 2.删除扩展信息
        LambdaQueryWrapper<CrmPharmacyDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmPharmacyDO::getCrmEnterpriseId, request.getCrmEnterpriseId());
        CrmPharmacyDO crmPharmacyDO = new CrmPharmacyDO();
        crmPharmacyDO.setOpUserId(request.getOpUserId());
        crmPharmacyDO.setOpTime(request.getOpTime());
        batchDeleteWithFill(crmPharmacyDO, wrapper) ;
        return true;
    }
}
