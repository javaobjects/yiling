package com.yiling.user.agreementv2.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementControlAreaDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementControlAreaRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementControlAreaDetailRequest;
import com.yiling.user.agreementv2.entity.AgreementControlAreaDO;
import com.yiling.user.agreementv2.dao.AgreementControlAreaMapper;
import com.yiling.user.agreementv2.entity.AgreementControlAreaDetailDO;
import com.yiling.user.agreementv2.service.AgreementControlAreaDetailService;
import com.yiling.user.agreementv2.service.AgreementControlAreaService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.service.impl.EnterpriseSalesAreaServiceImpl;
import com.yiling.user.shop.dto.AreaChildrenDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 协议控销区域表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementControlAreaServiceImpl extends BaseServiceImpl<AgreementControlAreaMapper, AgreementControlAreaDO> implements AgreementControlAreaService {

    @Autowired
    private AgreementControlAreaDetailService agreementControlAreaDetailService;
    @Autowired
    private EnterpriseSalesAreaServiceImpl enterpriseSalesAreaService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveControlAreaList(List<AddAgreementControlAreaRequest> allControlAreaList) {

        //根据业务：最多可能设置6组区域
        allControlAreaList.forEach(controlAreaRequest -> {
            if (Objects.isNull(controlAreaRequest)) {
                return;
            }
            QueryAgreementControlAreaDetailRequest areaDetailRequest = PojoUtils.map(controlAreaRequest, QueryAgreementControlAreaDetailRequest.class);
            // 保存控销区域
            LambdaQueryWrapper<AgreementControlAreaDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AgreementControlAreaDO::getAgreementId, areaDetailRequest.getAgreementId());
            queryWrapper.eq(AgreementControlAreaDO::getControlGoodsGroupId, areaDetailRequest.getControlGoodsGroupId());
            AgreementControlAreaDO tempControlAreaDO = this.getOne(queryWrapper);

            if (Objects.nonNull(tempControlAreaDO)) {
                AgreementControlAreaDO controlAreaDO = new AgreementControlAreaDO();
                controlAreaDO.setId(tempControlAreaDO.getId());
                controlAreaDO.setJsonContent(controlAreaRequest.getJsonContent());
                controlAreaDO.setDescription(enterpriseSalesAreaService.getDescription(controlAreaRequest.getJsonContent(), 2));
                controlAreaDO.setOpUserId(controlAreaRequest.getOpUserId());
                this.updateById(controlAreaDO);
            } else {
                AgreementControlAreaDO controlAreaDO = PojoUtils.map(areaDetailRequest, AgreementControlAreaDO.class);
                controlAreaDO.setJsonContent(controlAreaRequest.getJsonContent());
                controlAreaDO.setDescription(enterpriseSalesAreaService.getDescription(controlAreaRequest.getJsonContent(), 2));
                controlAreaDO.setOpUserId(controlAreaRequest.getOpUserId());
                this.save(controlAreaDO);
            }

            // 设置控销区域详情
            List<AgreementControlAreaDetailDO> saleAreaDetailList = agreementControlAreaDetailService.getControlAreaDetailList(areaDetailRequest);

            // 已经存在的区域编码
            List<String> areaCodeList = saleAreaDetailList.stream().map(AgreementControlAreaDetailDO::getAreaCode).distinct().collect(Collectors.toList());

            List<AreaChildrenDTO> areaChildrenList = JSONObject.parseArray(controlAreaRequest.getJsonContent(), AreaChildrenDTO.class);

            List<String> controlAreaCodeList = ListUtil.toList();
            getControlAreaCodeList(areaChildrenList , controlAreaCodeList);

            // 移除
            List<String> removeCodes = areaCodeList.stream().filter(code -> !controlAreaCodeList.contains(code)).distinct().collect(Collectors.toList());
            removeControlAreaCode(removeCodes,areaDetailRequest);

            // 新增
            List<String> addCodes = controlAreaCodeList.stream().filter(code -> !areaCodeList.contains(code)).distinct().collect(Collectors.toList());
            addControlAreaCode(addCodes,areaDetailRequest);
        });

        return true;
    }

    @Override
    public AgreementControlAreaDTO getControlArea(Long controlGoodsGroupId) {
        LambdaQueryWrapper<AgreementControlAreaDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementControlAreaDO::getControlGoodsGroupId, controlGoodsGroupId);
        return PojoUtils.map(this.getOne(wrapper), AgreementControlAreaDTO.class);
    }

    /**
     * 添加控销关联区域
     * @param addCodes 要添加的区域代码
     * @param areaDetailRequest 参数
     */
    private void addControlAreaCode(List<String> addCodes , QueryAgreementControlAreaDetailRequest areaDetailRequest){
        if(CollUtil.isEmpty(addCodes)){
            return;
        }

        List<AgreementControlAreaDetailDO> list = addCodes.stream().map(code -> {
            AgreementControlAreaDetailDO controlAreaDetailDO = new AgreementControlAreaDetailDO();
            controlAreaDetailDO.setAgreementId(areaDetailRequest.getAgreementId());
            controlAreaDetailDO.setControlGoodsGroupId(areaDetailRequest.getControlGoodsGroupId());
            controlAreaDetailDO.setAreaCode(code);
            controlAreaDetailDO.setOpUserId(areaDetailRequest.getOpUserId());
            return controlAreaDetailDO;
        }).collect(Collectors.toList());

        agreementControlAreaDetailService.saveBatch(list);
    }

    /**
     * 移除关联区域代码
     * @param removeCodes 要移除的区域代码
     * @param areaDetailRequest 参数
     */
    public void removeControlAreaCode(List<String> removeCodes , QueryAgreementControlAreaDetailRequest areaDetailRequest) {
        if (CollUtil.isEmpty(removeCodes)) {
            return;
        }

        LambdaQueryWrapper<AgreementControlAreaDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgreementControlAreaDetailDO::getAgreementId, areaDetailRequest.getAgreementId());
        queryWrapper.eq(AgreementControlAreaDetailDO::getControlGoodsGroupId, areaDetailRequest.getControlGoodsGroupId());
        queryWrapper.in(AgreementControlAreaDetailDO::getAreaCode, removeCodes);

        AgreementControlAreaDetailDO entity = new AgreementControlAreaDetailDO();
        entity.setOpUserId(areaDetailRequest.getOpUserId());

        agreementControlAreaDetailService.batchDeleteWithFill(entity, queryWrapper);
    }

    /**
     * 获取控销区域编码
     * @param areaChildrenList
     * @param controlAreaCodeList
     */
    private void getControlAreaCodeList(List<AreaChildrenDTO> areaChildrenList , List<String> controlAreaCodeList ) {
        if(CollUtil.isEmpty(areaChildrenList)) {
            return;
        }

        for (AreaChildrenDTO provinceDTO : areaChildrenList) {
            for (AreaChildrenDTO cityDTO : provinceDTO.getChildren()) {
                    controlAreaCodeList.add(cityDTO.getCode());
                }
        }
    }

}
