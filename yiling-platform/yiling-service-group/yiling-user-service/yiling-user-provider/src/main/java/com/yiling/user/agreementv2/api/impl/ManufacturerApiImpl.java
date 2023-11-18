package com.yiling.user.agreementv2.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.api.AgreementManufacturerApi;
import com.yiling.user.agreementv2.dto.AgreementManufacturerDTO;
import com.yiling.user.agreementv2.dto.AgreementManufacturerGoodsDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementManufacturerGoodsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementManufacturerRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerGoodsRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerRequest;
import com.yiling.user.agreementv2.entity.AgreementManufacturerDO;
import com.yiling.user.agreementv2.service.AgreementManufacturerGoodsService;
import com.yiling.user.agreementv2.service.AgreementManufacturerService;

import lombok.extern.slf4j.Slf4j;

/**
 * 厂家管理 API 实现
 *
 * @author: lun.yu
 * @date: 2022/2/22
 */
@Slf4j
@DubboService
public class ManufacturerApiImpl implements AgreementManufacturerApi {

    @Autowired
    private AgreementManufacturerService manufacturerService;
    @Autowired
    private AgreementManufacturerGoodsService manufacturerGoodsService;


    @Override
    public Page<AgreementManufacturerDTO> queryManufacturerListPage(QueryAgreementManufacturerRequest request) {
        return PojoUtils.map(manufacturerService.queryListPage(request), AgreementManufacturerDTO.class);
    }

    @Override
    public Boolean addManufacturer(AddAgreementManufacturerRequest request) {
        return manufacturerService.addManufacturer(request);
    }

    @Override
    public List<AgreementManufacturerDTO> listByIds(List<Long> ids) {
        return PojoUtils.map(manufacturerService.listByIds(ids), AgreementManufacturerDTO.class);
    }

    @Override
    public String getNameBySpecificationAndType(Long specificationGoodsId, Integer type) {
        return manufacturerGoodsService.getNameBySpecificationAndType(specificationGoodsId, type);
    }

    @Override
    public AgreementManufacturerDTO listById(Long id) {
        return PojoUtils.map(manufacturerService.getById(id), AgreementManufacturerDTO.class);
    }

    @Override
    public AgreementManufacturerDTO getByEid(Long eid) {
        LambdaQueryWrapper<AgreementManufacturerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementManufacturerDO::getEid, eid);
        return PojoUtils.map(manufacturerService.getOne(wrapper), AgreementManufacturerDTO.class);
    }

    @Override
    public Boolean deleteManufacturer(Long id, Long opUserId) {
        return manufacturerService.deleteManufacturer(id,opUserId);
    }

    @Override
    public Boolean addManufacturerGoods(List<AddAgreementManufacturerGoodsRequest> request) {
        return manufacturerGoodsService.addManufacturerGoods(request);
    }

    @Override
    public Page<AgreementManufacturerGoodsDTO> queryManufacturerGoodsListPage(QueryAgreementManufacturerGoodsRequest request) {
        return manufacturerGoodsService.queryManufacturerGoodsListPage(request);
    }

    @Override
    public Boolean deleteManufacturerGoods(List<Long> idList, Long opUserId) {
        Assert.notEmpty(idList, "error: idList must not be empty");
        return manufacturerGoodsService.deleteManufacturerGoods(idList, opUserId);
    }

    @Override
    public Map<Long, List<AgreementManufacturerGoodsDTO>> getGoodsBySpecificationId(List<Long> specificationId) {
        return manufacturerGoodsService.getGoodsBySpecificationId(specificationId);
    }

    @Override
    public Integer getManufactureGoodsNumberByEid(Long eid) {
        return manufacturerGoodsService.getManufactureGoodsNumberByEid(eid);
    }
}
