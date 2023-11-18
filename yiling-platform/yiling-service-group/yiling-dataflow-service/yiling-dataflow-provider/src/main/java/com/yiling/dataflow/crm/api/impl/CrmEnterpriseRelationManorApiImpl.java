package com.yiling.dataflow.crm.api.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationManorApi;
import com.yiling.dataflow.crm.bo.CrmRelationManorBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationManorDTO;
import com.yiling.dataflow.crm.dto.CrmManorRepresentativeDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationManorPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.dataflow.crm.dto.request.RemoveManorRelationRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateManorRelationRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationManorDO;
import com.yiling.dataflow.crm.enums.CrmEnterpriseRelationManorErrorCode;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationManorService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmManorRepresentativeService;
import com.yiling.dataflow.crm.service.CrmManorService;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

@DubboService
@Slf4j
public class CrmEnterpriseRelationManorApiImpl implements CrmEnterpriseRelationManorApi {
    @Resource
    private CrmEnterpriseRelationManorService crmEnterpriseRelationManorService;
    @Resource
    private CrmManorService crmManorService;
    @Resource
    private CrmManorRepresentativeService crmManorRepresentativeService;
    @Resource
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Override
    public Page<CrmEnterpriseRelationManorDTO> pageListByManorId(QueryCrmEnterpriseRelationManorPageRequest request) {
        return crmEnterpriseRelationManorService.pageListByManorId(request);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Long saveOrUpdate(SaveOrUpdateManorRelationRequest request) {
        CrmEnterpriseRelationManorDO relationManorDO = new CrmEnterpriseRelationManorDO();
        PojoUtils.map(request, relationManorDO);
        //检查是否重复同一个机构、同一个品种 ，只能关联到一个辖区
        boolean check = crmEnterpriseRelationManorService.checkDuplicate(relationManorDO.getCrmEnterpriseId(), relationManorDO.getCategoryId(), relationManorDO.getId());
        if (check) {
            throw new BusinessException(CrmEnterpriseRelationManorErrorCode.REPEAT_ERROR_CODE, relationManorDO.getCrmEnterpriseName() + relationManorDO.getCategoryName());
        }
        crmEnterpriseRelationManorService.saveOrUpdate(relationManorDO);
        // 操作三者关系新增或修改
        //插入三者关系更新
        CrmManorRepresentativeDTO crmManorRepresentativeDTO=crmManorRepresentativeService.getByManorId(request.getCrmManorId());
        if(Objects.nonNull(crmManorRepresentativeDTO)){
            List<CrmEnterpriseRelationShipDO> relationShipDOS = crmEnterpriseRelationShipService.listByEidsAndRole(Arrays.asList(relationManorDO.getCrmEnterpriseId()), 2);
            Map<String, CrmEnterpriseRelationShipDO> keyManorRelationMap = Optional.ofNullable(relationShipDOS.stream().filter(e->e.getManorId().longValue()>0L&&e.getCategoryId().longValue()>0L).collect(Collectors.toMap(this::keyManorRelation, m -> m, (v1, v2) -> v1))).orElse(MapUtil.newHashMap());
            List<CrmEnterpriseRelationShipDO> saveCrmRelationshipRequestList = new ArrayList<>();
            CrmEnterpriseRelationShipDO b = keyManorRelationMap.get(String.format("%s%s%s", relationManorDO.getCrmManorId(), relationManorDO.getCategoryId(), relationManorDO.getCrmEnterpriseId()));
            if (b == null) {
                b = new CrmEnterpriseRelationShipDO();
                b.setCreateUser(request.getOpUserId());
                b.setCreateTime(request.getOpTime());
                b.setCrmEnterpriseId(relationManorDO.getCrmEnterpriseId());
                b.setRemark("辖区新增机构添加");
            }
            b.setPostCode(crmManorRepresentativeDTO.getRepresentativePostCode());
            b.setPostName(crmManorRepresentativeDTO.getRepresentativePostName());
            b.setCustomerName(relationManorDO.getCrmEnterpriseName());
            b.setManorId(relationManorDO.getCrmManorId());
            b.setCategoryId(relationManorDO.getCategoryId());
            b.setUpdateTime(request.getOpTime());
            b.setUpdateUser(request.getOpUserId());
            b.setSupplyChainRoleType(AgencySupplyChainRoleEnum.HOSPITAL.getCode());
            b.setSupplyChainRole(AgencySupplyChainRoleEnum.HOSPITAL.getName());
            saveCrmRelationshipRequestList.add(b);
            if (CollUtil.isNotEmpty(saveCrmRelationshipRequestList)) {
                log.info("辖区保存三者关系:{}", saveCrmRelationshipRequestList);
                crmEnterpriseRelationShipService.saveOrUpdateBatch(saveCrmRelationshipRequestList);

            }
        }

        return relationManorDO.getId();
    }

    @Override
    public int removeById(RemoveManorRelationRequest request) {
        CrmEnterpriseRelationManorDO relationManorDO = crmEnterpriseRelationManorService.getById(request.getId());
        if (Objects.isNull(relationManorDO)) {
            return 0;
        }
        //线判断是否配置岗位
        CrmManorRepresentativeDTO crmManorRepresentativeDO = crmManorRepresentativeService.getByManorId(relationManorDO.getCrmManorId());
        if (Objects.nonNull(crmManorRepresentativeDO)) {
            crmEnterpriseRelationShipService.batchDeleteWithCrmEnterIds(Arrays.asList(relationManorDO.getCrmEnterpriseId()), request.getOpUserId(), "辖区关联机构删除" + request.getOpUserId(),relationManorDO.getCrmManorId());
        }
        return crmEnterpriseRelationManorService.batchDeleteWithIds(Arrays.asList(request.getId()), request.getOpUserId(), "删除");
    }

    @Override
    public boolean checkDuplicate(Long crmEnId, Long categoryId, Long id) {
        return crmEnterpriseRelationManorService.checkDuplicate(crmEnId, categoryId, id);
    }

    @Override
    public List<CrmRelationManorBO> queryList(Long crmEnterpriseId) {

        return crmEnterpriseRelationManorService.queryList(crmEnterpriseId);
    }

    @Override
    public Page<CrmEnterpriseRelationManorDTO> pageExportList(QueryCrmManorPageRequest request) {
        return crmEnterpriseRelationManorService.pageExportList(request);
    }

    @Override
    public CrmEnterpriseRelationManorDTO getById(Long id) {
        return PojoUtils.map(crmEnterpriseRelationManorService.getById(id),CrmEnterpriseRelationManorDTO.class);
    }

    @Override
    public Boolean updateBatch(List<SaveOrUpdateManorRelationRequest> request) {
        return crmEnterpriseRelationManorService.updateBatch(request);
    }
    //辖区ID+品类+机构编码 唯一  三者关系 医院类型
    public String keyManorRelation(CrmEnterpriseRelationShipDO relationShipDO) {
        return String.format("%s%s%s", relationShipDO.getManorId(), relationShipDO.getCategoryId(), relationShipDO.getCrmEnterpriseId());
    }
}
