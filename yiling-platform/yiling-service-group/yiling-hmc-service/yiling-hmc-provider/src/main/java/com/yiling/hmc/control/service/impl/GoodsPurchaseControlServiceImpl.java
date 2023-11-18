package com.yiling.hmc.control.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.control.bo.GoodsPurchaseControlBO;
import com.yiling.hmc.control.dao.GoodsPurchaseControlMapper;
import com.yiling.hmc.control.dto.GoodsPurchaseControlDTO;
import com.yiling.hmc.control.dto.request.AddGoodsPurchaseRequest;
import com.yiling.hmc.control.dto.request.QueryGoodsPurchaseControlPageRequest;
import com.yiling.hmc.control.dto.request.UpdateGoodsPurchaseRequest;
import com.yiling.hmc.control.entity.GoodsPurchaseControlDO;
import com.yiling.hmc.control.service.GoodsPurchaseControlService;
import com.yiling.hmc.goods.entity.GoodsControlDO;
import com.yiling.hmc.goods.service.GoodsControlService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 药品进货渠道管控 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-03-31
 */
@Service
public class GoodsPurchaseControlServiceImpl extends BaseServiceImpl<GoodsPurchaseControlMapper, GoodsPurchaseControlDO> implements GoodsPurchaseControlService {

    @Autowired
    private GoodsControlService goodsControlService;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @Override
    public void add(AddGoodsPurchaseRequest addGoodsPurchaseRequest) {
        LambdaQueryWrapper<GoodsPurchaseControlDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GoodsPurchaseControlDO::getGoodsControlId,addGoodsPurchaseRequest.getGoodsControlId());
        wrapper.eq(GoodsPurchaseControlDO::getSellerEid,addGoodsPurchaseRequest.getSellerEid()).eq(GoodsPurchaseControlDO::getChannelType,addGoodsPurchaseRequest.getChannelType());
        wrapper.last("limit 1");
        GoodsPurchaseControlDO controlDO = this.getOne(wrapper);
        if(Objects.nonNull(controlDO)){
            throw new BusinessException(ResultCode.FAILED,"同类型下，同一渠道不允许重复添加");
        }
        GoodsPurchaseControlDO goodsPurchaseControlDO = new GoodsPurchaseControlDO();
        PojoUtils.map(addGoodsPurchaseRequest,goodsPurchaseControlDO);
        this.save(goodsPurchaseControlDO);
    }

    @Override
    public GoodsPurchaseControlDTO getOneById(Long id) {
        GoodsPurchaseControlDO controlDO = this.getById(id);
        GoodsPurchaseControlDTO goodsPurchaseControlDTO = new GoodsPurchaseControlDTO();
        PojoUtils.map(controlDO,goodsPurchaseControlDTO);
        return goodsPurchaseControlDTO;
    }

    @Override
    public void update(UpdateGoodsPurchaseRequest request) {
        GoodsPurchaseControlDO goodsPurchaseControlDO = new GoodsPurchaseControlDO();
        PojoUtils.map(request,goodsPurchaseControlDO);
        this.updateById(goodsPurchaseControlDO);
    }

    @Override
    public Page<GoodsPurchaseControlDTO> queryPage(QueryGoodsPurchaseControlPageRequest request) {
        LambdaQueryWrapper<GoodsPurchaseControlDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GoodsPurchaseControlDO::getGoodsControlId,request.getGoodsControlId());
        if(CollUtil.isNotEmpty(request.getEidList())){
            wrapper.in(GoodsPurchaseControlDO::getSellerEid,request.getEidList());
        }
        wrapper.orderByDesc(GoodsPurchaseControlDO::getId);
        Page<GoodsPurchaseControlDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page,GoodsPurchaseControlDTO.class);
    }

    @Override
    public Integer getByGoodControlId(Long controlId) {
        LambdaQueryWrapper<GoodsPurchaseControlDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GoodsPurchaseControlDO::getGoodsControlId,controlId).eq(GoodsPurchaseControlDO::getControlStatus,1).last("limit 1");
        GoodsPurchaseControlDO purchaseControlDO = this.getOne(wrapper);
        if(Objects.isNull(purchaseControlDO)){
            return 0;
        }
        return 1;
    }

    @Override
    public List<GoodsPurchaseControlDTO> queryByControlIds(List<Long> controlIdList) {
        LambdaQueryWrapper<GoodsPurchaseControlDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(GoodsPurchaseControlDO::getGoodsControlId,controlIdList).eq(GoodsPurchaseControlDO::getControlStatus,1);
        List<GoodsPurchaseControlDO> controlDOS = this.list(wrapper);
        if(CollUtil.isEmpty(controlDOS)){
            return Lists.newArrayList();
        }

        List<GoodsPurchaseControlDTO> purchaseControlDTOList = PojoUtils.map(controlDOS, GoodsPurchaseControlDTO.class);
        List<Long> eidList = purchaseControlDTOList.stream().map(GoodsPurchaseControlDTO::getSellerEid).collect(Collectors.toList());
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseApi.getMapByIds(eidList);
        purchaseControlDTOList.forEach(goodsPurchaseControlDTO -> {
            goodsPurchaseControlDTO.setLicenseNumber(enterpriseDTOMap.get(goodsPurchaseControlDTO.getSellerEid()).getLicenseNumber());
            goodsPurchaseControlDTO.setEnterpriseName(enterpriseDTOMap.get(goodsPurchaseControlDTO.getSellerEid()).getName());
        });
        return purchaseControlDTOList;
    }

    @Override
    public List<GoodsPurchaseControlBO> queryGoodsPurchaseControlList(List<Long> sellSpecificationsIdList) {
        LambdaQueryWrapper<GoodsControlDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(GoodsControlDO::getSellSpecificationsId,sellSpecificationsIdList);
        List<GoodsControlDO> goodsControlDOS = goodsControlService.list(wrapper);
        if(CollUtil.isEmpty(goodsControlDOS)){
            return Lists.newArrayList();
        }
        List<GoodsPurchaseControlBO> purchaseControlBOS = Lists.newArrayList();
        List<Long> collect = goodsControlDOS.stream().map(GoodsControlDO::getId).collect(Collectors.toList());
        List<GoodsPurchaseControlDTO> goodsPurchaseControlDTOS = this.queryByControlIds(collect);
        if(CollUtil.isEmpty(goodsPurchaseControlDTOS)){
            return Lists.newArrayList();
        }
        Map<Long, List<GoodsPurchaseControlDTO>> listMap = goodsPurchaseControlDTOS.stream().collect(Collectors.groupingBy(GoodsPurchaseControlDTO::getGoodsControlId));
        goodsControlDOS.forEach(goodsControlDO -> {
            GoodsPurchaseControlBO controlBO = new GoodsPurchaseControlBO();
            controlBO.setSellSpecificationsId(goodsControlDO.getSellSpecificationsId());
            List<GoodsPurchaseControlDTO> purchaseControlDTOS = listMap.get(goodsControlDO.getId());
            if(CollUtil.isNotEmpty(purchaseControlDTOS)){
                controlBO.setChannelNameList(purchaseControlDTOS.stream().map(GoodsPurchaseControlDTO::getEnterpriseName).collect(Collectors.toList()));
                controlBO.setControlStatus(1);
            }else{
                controlBO.setControlStatus(0);
            }
            purchaseControlBOS.add(controlBO);
        });
        return purchaseControlBOS;
    }

    @Override
    public List<GoodsPurchaseControlDTO> queryControlListBySpecificationsId(List<Long> sellSpecificationsIdList) {
        LambdaQueryWrapper<GoodsControlDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(GoodsControlDO::getSellSpecificationsId,sellSpecificationsIdList);
        List<GoodsControlDO> goodsControlDOS = goodsControlService.list(wrapper);
        if(CollUtil.isEmpty(goodsControlDOS)){
            return Lists.newArrayList();
        }
        Map<Long, Long> map = goodsControlDOS.stream().collect(Collectors.toMap(GoodsControlDO::getId, GoodsControlDO::getSellSpecificationsId));
        List<Long> collect = goodsControlDOS.stream().map(GoodsControlDO::getId).collect(Collectors.toList());
        LambdaQueryWrapper<GoodsPurchaseControlDO> controlWrapper = Wrappers.lambdaQuery();
        controlWrapper.in(GoodsPurchaseControlDO::getGoodsControlId,collect).eq(GoodsPurchaseControlDO::getControlStatus,1);
        List<GoodsPurchaseControlDO> controlDOS = this.list(controlWrapper);
        if(CollUtil.isEmpty(controlDOS)){
            return Lists.newArrayList();
        }
        List<GoodsPurchaseControlDTO> purchaseControlDTOList = PojoUtils.map(controlDOS, GoodsPurchaseControlDTO.class);

        purchaseControlDTOList.forEach(goodsPurchaseControlDTO -> {
            goodsPurchaseControlDTO.setSellSpecificationsId(map.get(goodsPurchaseControlDTO.getGoodsControlId()));
        });
        return purchaseControlDTOList;
    }
}
