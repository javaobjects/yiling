package com.yiling.user.agreementv2.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateControlAreaDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateControlAreaRequest;
import com.yiling.user.agreementv2.entity.AgreementRebateControlAreaDO;
import com.yiling.user.agreementv2.dao.AgreementRebateControlAreaMapper;
import com.yiling.user.agreementv2.entity.AgreementRebateControlAreaDetailDO;
import com.yiling.user.agreementv2.service.AgreementRebateControlAreaDetailService;
import com.yiling.user.agreementv2.service.AgreementRebateControlAreaService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.service.impl.EnterpriseSalesAreaServiceImpl;
import com.yiling.user.shop.dto.AreaChildrenDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 协议返利范围控销区域表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
@Service
public class AgreementRebateControlAreaServiceImpl extends BaseServiceImpl<AgreementRebateControlAreaMapper, AgreementRebateControlAreaDO> implements AgreementRebateControlAreaService {

    @Autowired
    private EnterpriseSalesAreaServiceImpl enterpriseSalesAreaService;
    @Autowired
    private AgreementRebateControlAreaDetailService agreementRebateControlAreaDetailService;

    @Override
    public Boolean saveArea(AddAgreementRebateControlAreaRequest rebateControlArea) {

        // 保存协议返利返利范围控销区域
        LambdaQueryWrapper<AgreementRebateControlAreaDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgreementRebateControlAreaDO::getRebateScopeId, rebateControlArea.getRebateScopeId());
        AgreementRebateControlAreaDO tempControlAreaDO = this.getOne(queryWrapper);

        if (Objects.nonNull(tempControlAreaDO)) {
            AgreementRebateControlAreaDO controlAreaDO = new AgreementRebateControlAreaDO();
            controlAreaDO.setId(tempControlAreaDO.getId());
            controlAreaDO.setJsonContent(rebateControlArea.getJsonContent());
            controlAreaDO.setDescription(enterpriseSalesAreaService.getDescription(rebateControlArea.getJsonContent(), 2));
            controlAreaDO.setOpUserId(rebateControlArea.getOpUserId());
            this.updateById(controlAreaDO);
        } else {
            AgreementRebateControlAreaDO controlAreaDO = PojoUtils.map(rebateControlArea, AgreementRebateControlAreaDO.class);
            controlAreaDO.setDescription(enterpriseSalesAreaService.getDescription(rebateControlArea.getJsonContent(), 2));
            controlAreaDO.setOpUserId(rebateControlArea.getOpUserId());
            this.save(controlAreaDO);
        }

        // 设置控销区域详情
        List<AgreementRebateControlAreaDetailDO> saleAreaDetailList = agreementRebateControlAreaDetailService.getControlAreaDetailList(rebateControlArea.getRebateScopeId());

        // 已经存在的区域编码
        List<String> areaCodeList = saleAreaDetailList.stream().map(AgreementRebateControlAreaDetailDO::getAreaCode).distinct().collect(Collectors.toList());

        List<AreaChildrenDTO> areaChildrenList = JSONObject.parseArray(rebateControlArea.getJsonContent(), AreaChildrenDTO.class);

        List<String> controlAreaCodeList = ListUtil.toList();
        for (AreaChildrenDTO provinceDTO : areaChildrenList) {
            for (AreaChildrenDTO cityDTO : provinceDTO.getChildren()) {
                controlAreaCodeList.add(cityDTO.getCode());
            }
        }

        // 移除
        List<String> removeCodes = areaCodeList.stream().filter(code -> !controlAreaCodeList.contains(code)).distinct().collect(Collectors.toList());
        removeControlAreaCode(removeCodes, rebateControlArea.getRebateScopeId(), rebateControlArea.getOpUserId());

        // 新增
        List<String> addCodes = controlAreaCodeList.stream().filter(code -> !areaCodeList.contains(code)).distinct().collect(Collectors.toList());
        addControlAreaCode(addCodes, rebateControlArea.getRebateScopeId(), rebateControlArea.getOpUserId());

        return true;
    }

    @Override
    public Map<Long, AgreementRebateControlAreaDTO> getRebateControlAreaMap(List<Long> rebateScopeIdList) {
        if (CollUtil.isEmpty(rebateScopeIdList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<AgreementRebateControlAreaDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AgreementRebateControlAreaDO::getRebateScopeId, rebateScopeIdList);
        List<AgreementRebateControlAreaDTO> controlAreaDTOList = PojoUtils.map(this.list(wrapper), AgreementRebateControlAreaDTO.class);
        return controlAreaDTOList.stream().collect(Collectors.toMap(AgreementRebateControlAreaDTO::getRebateScopeId, Function.identity()));
    }

    /**
     * 添加控销关联区域
     * @param addCodes 要添加的区域代码
     * @param rebateScopeId 参数
     * @param opUserId
     */
    private void addControlAreaCode(List<String> addCodes , Long rebateScopeId, Long opUserId){
        if(CollUtil.isEmpty(addCodes)){
            return;
        }

        List<AgreementRebateControlAreaDetailDO> list = addCodes.stream().map(code -> {
            AgreementRebateControlAreaDetailDO controlAreaDetailDO = new AgreementRebateControlAreaDetailDO();
            controlAreaDetailDO.setRebateScopeId(rebateScopeId);
            controlAreaDetailDO.setAreaCode(code);
            controlAreaDetailDO.setOpUserId(opUserId);
            return controlAreaDetailDO;
        }).collect(Collectors.toList());

        this.agreementRebateControlAreaDetailService.saveBatch(list);
    }

    /**
     * 移除关联区域代码
     * @param removeCodes 要移除的区域代码
     * @param rebateScopeId 参数
     * @param opUserId
     */
    public void removeControlAreaCode(List<String> removeCodes , Long rebateScopeId, Long opUserId) {
        if (CollUtil.isEmpty(removeCodes)) {
            return;
        }

        LambdaQueryWrapper<AgreementRebateControlAreaDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgreementRebateControlAreaDetailDO::getRebateScopeId, rebateScopeId);
        queryWrapper.in(AgreementRebateControlAreaDetailDO::getAreaCode, removeCodes);

        AgreementRebateControlAreaDetailDO entity = new AgreementRebateControlAreaDetailDO();
        entity.setOpUserId(opUserId);

        agreementRebateControlAreaDetailService.batchDeleteWithFill(entity, queryWrapper);
    }

}
