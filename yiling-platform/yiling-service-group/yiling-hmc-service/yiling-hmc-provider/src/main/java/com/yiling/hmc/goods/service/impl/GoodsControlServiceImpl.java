package com.yiling.hmc.goods.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.hmc.goods.bo.GoodsControlBO;
import com.yiling.hmc.goods.dao.GoodsControlMapper;
import com.yiling.hmc.goods.dto.GoodsControlDTO;
import com.yiling.hmc.goods.dto.request.GoodsControlPageRequest;
import com.yiling.hmc.goods.dto.request.GoodsControlSaveRequest;
import com.yiling.hmc.goods.entity.GoodsControlDO;
import com.yiling.hmc.goods.service.GoodsControlService;

/**
 * @author shichen
 * @类名 GoodsControlServiceImpl
 * @描述 管控商品实现类
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
@Service
public class GoodsControlServiceImpl extends BaseServiceImpl<GoodsControlMapper, GoodsControlDO> implements GoodsControlService {

    @DubboReference
    private GoodsHmcApi goodsHmcApi;

    @Override
    public List<GoodsControlDTO> batchGetGoodsControlBySpecificationsIds(List<Long> specificationsIds) {
        if(CollectionUtils.isEmpty(specificationsIds)){
            return Lists.newArrayList();
        }
        QueryWrapper<GoodsControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GoodsControlDO::getSellSpecificationsId, specificationsIds);
        List<GoodsControlDO> list = this.list(queryWrapper);
        return PojoUtils.map(list, GoodsControlDTO.class);
    }

    @Override
    public Page<GoodsControlBO> pageList(GoodsControlPageRequest request) {
        QueryWrapper<GoodsControlDO> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(request.getName())){
            queryWrapper.lambda().like(GoodsControlDO::getName,request.getName());
        }
        queryWrapper.lambda().orderByDesc(GoodsControlDO::getCreateTime);
        Page<GoodsControlDO> page = this.page(request.getPage(), queryWrapper);
        Page<GoodsControlBO> boPage = PojoUtils.map(page, GoodsControlBO.class);
        this.completeGoodsControlInfo(boPage.getRecords());
        return boPage;
    }

    @Override
    public Long saveOrUpdateGoodsControl(GoodsControlSaveRequest request) {
        GoodsControlDO goodsControlDO = null;
        if(null==request.getId()||request.getId()==0){
            GoodsControlDTO goodControlDTO = this.findGoodsControlBySpecificationsId(request.getSellSpecificationsId());
            if(null!=goodControlDTO){
                throw new BusinessException(ResultCode.FAILED,"此规格的药品已存在不允许重复添加");
            }
            StandardGoodsSpecificationDTO specification = goodsHmcApi.getStandardGoodsSpecification(request.getSellSpecificationsId());
            if(null==specification){
                throw new BusinessException(ResultCode.FAILED,"选择的标准库规格不存在");
            }
            goodsControlDO =PojoUtils.map(request, GoodsControlDO.class);
            goodsControlDO.setName(specification.getName());
            goodsControlDO.setLicenseNo(specification.getLicenseNo());
            goodsControlDO.setStandardId(specification.getStandardId());
        }else {
            goodsControlDO =new GoodsControlDO();
            goodsControlDO.setId(request.getId());
            goodsControlDO.setInsurancePrice(request.getInsurancePrice());
            goodsControlDO.setMarketPrice(request.getMarketPrice());
            goodsControlDO.setOpUserId(request.getOpUserId());
        }
        this.saveOrUpdate(goodsControlDO);
        return goodsControlDO.getId();
    }

    @Override
    public GoodsControlDTO findGoodsControlBySpecificationsId(Long specificationsId) {
        QueryWrapper<GoodsControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsControlDO::getSellSpecificationsId,specificationsId);
        return PojoUtils.map(this.getOne(queryWrapper), GoodsControlDTO.class);
    }

    @Override
    public List<GoodsControlBO> getGoodsControlInfoByIds(List<Long> controlIds) {
        List<GoodsControlDO> controlDOList = this.listByIds(controlIds);
        List<GoodsControlBO> boList = PojoUtils.map(controlDOList, GoodsControlBO.class);
        this.completeGoodsControlInfo(boList);
        return boList;
    }

    @Override
    public Page<GoodsControlDTO> queryUpGoodsControlPageList(GoodsControlPageRequest request) {
        Page<GoodsControlDO> controlDOPage = this.baseMapper.queryUpGoodsControlPageList(request.getPage(), request);
        return PojoUtils.map(controlDOPage,GoodsControlDTO.class);
    }

    @Override
    public Page<GoodsControlBO> queryInsuranceGoodsControlPageList(GoodsControlPageRequest request) {
        Page<GoodsControlDO> page = this.baseMapper.queryInsuranceGoodsControlPageList(request.getPage(), request);
        Page<GoodsControlBO> boPage = PojoUtils.map(page, GoodsControlBO.class);
        this.completeGoodsControlInfo(boPage.getRecords());
        return boPage;
    }

    /**
     * 完善管控商品信息
     * @param goodsControlBOList
     */
    private void completeGoodsControlInfo(List<GoodsControlBO> goodsControlBOList){
        if(CollectionUtils.isNotEmpty(goodsControlBOList)){
            List<Long> specificationIds = goodsControlBOList.stream().map(GoodsControlBO::getSellSpecificationsId).collect(Collectors.toList());
            List<StandardGoodsSpecificationDTO> specifications = goodsHmcApi.getListStandardGoodsSpecificationByIds(specificationIds);
            Map<Long, StandardGoodsSpecificationDTO> specificationDTOMap = specifications.stream().collect(Collectors.toMap(StandardGoodsSpecificationDTO::getId, Function.identity()));
            goodsControlBOList.forEach(goodsControlBO -> {
                StandardGoodsSpecificationDTO specification = specificationDTOMap.get(goodsControlBO.getSellSpecificationsId());
                if(null!=specification){
                    goodsControlBO.setSellSpecifications(specification.getSellSpecifications());
                    goodsControlBO.setUnit(specification.getUnit());
                }
            });
        }
    }
}
