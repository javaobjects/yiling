package com.yiling.user.agreementv2.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dao.AgreementManufacturerGoodsMapper;
import com.yiling.user.agreementv2.dto.AgreementManufacturerGoodsDTO;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesGoodsDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementManufacturerGoodsRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementMainTermsRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerGoodsRequest;
import com.yiling.user.agreementv2.entity.AgreementMainTermsDO;
import com.yiling.user.agreementv2.entity.AgreementManufacturerDO;
import com.yiling.user.agreementv2.entity.AgreementManufacturerGoodsDO;
import com.yiling.user.agreementv2.enums.AgreementAuthStatusEnum;
import com.yiling.user.agreementv2.enums.AgreementEffectStatusEnum;
import com.yiling.user.agreementv2.service.AgreementMainTermsService;
import com.yiling.user.agreementv2.service.AgreementManufacturerGoodsService;
import com.yiling.user.agreementv2.service.AgreementManufacturerService;
import com.yiling.user.agreementv2.service.AgreementSupplySalesGoodsService;
import com.yiling.user.common.UserErrorCode;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 厂家商品表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/22
 */
@Slf4j
@Service
public class AgreementManufacturerGoodsServiceImpl extends BaseServiceImpl<AgreementManufacturerGoodsMapper, AgreementManufacturerGoodsDO> implements AgreementManufacturerGoodsService {

    @Autowired
    private AgreementManufacturerService manufacturerService;
    @Autowired
    private AgreementMainTermsService agreementMainTermsService;
    @Autowired
    private AgreementSupplySalesGoodsService agreementSupplySalesGoodsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addManufacturerGoods(List<AddAgreementManufacturerGoodsRequest> list) {
        // 当前厂家
        AgreementManufacturerDO manufacturerDO = Optional.ofNullable(manufacturerService.getById(list.get(0).getManufacturerId())).orElseThrow(() -> new BusinessException(UserErrorCode.MANUFACTURER_NOT_EXIST));

        // 校验：一个SKU只能关联一个生产厂家和一个品牌厂家
        for (AddAgreementManufacturerGoodsRequest request : list) {
            // 根据商品查询出关联的所有厂家
            LambdaQueryWrapper<AgreementManufacturerGoodsDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AgreementManufacturerGoodsDO::getGoodsId, request.getGoodsId());
            wrapper.eq(AgreementManufacturerGoodsDO::getSpecificationGoodsId, request.getSpecificationGoodsId());
            List<Long> manufacturerIdList = this.list(wrapper).stream().map(AgreementManufacturerGoodsDO::getManufacturerId).collect(Collectors.toList());

            if (CollUtil.isNotEmpty(manufacturerIdList)) {
                List<AgreementManufacturerDO> manufacturerDOList = manufacturerService.listByIds(manufacturerIdList);
                List<Integer> typeList = manufacturerDOList.stream().map(AgreementManufacturerDO::getType).collect(Collectors.toList());

                if (typeList.contains(manufacturerDO.getType())) {
                    throw new BusinessException(UserErrorCode.HAD_RELATION_MANUFACTURER);
                }
            }
        }

        List<AgreementManufacturerGoodsDO> manufacturerGoodsList = PojoUtils.map(list, AgreementManufacturerGoodsDO.class);

        return this.saveBatch(manufacturerGoodsList);
    }

    @Override
    public Page<AgreementManufacturerGoodsDTO> queryManufacturerGoodsListPage(QueryAgreementManufacturerGoodsRequest request) {
        LambdaQueryWrapper<AgreementManufacturerGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementManufacturerGoodsDO::getManufacturerId, request.getManufacturerId());
        if (Objects.nonNull(request.getGoodsId()) && request.getGoodsId() != 0) {
            wrapper.eq(AgreementManufacturerGoodsDO::getGoodsId, request.getGoodsId());
        }
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            wrapper.like(AgreementManufacturerGoodsDO::getGoodsName, request.getGoodsName());
        }

        Page<AgreementManufacturerGoodsDO> doPage = this.page(request.getPage(), wrapper);
        return PojoUtils.map(doPage, AgreementManufacturerGoodsDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteManufacturerGoods(List<Long> idList, Long opUserId) {
        // 校验如果该商品已经关联厂家信息有有效的协议数据，则弹出错误提示，不允许删除
        List<AgreementManufacturerGoodsDO> manufacturerGoodsDOList = this.listByIds(idList);
        List<Long> specificationIdList = manufacturerGoodsDOList.stream().map(AgreementManufacturerGoodsDO::getSpecificationGoodsId).distinct().collect(Collectors.toList());
        AgreementManufacturerDO manufacturerDO = Optional.ofNullable(manufacturerService.getById(manufacturerGoodsDOList.get(0).getManufacturerId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.MANUFACTURER_NOT_EXIST));

        QueryAgreementMainTermsRequest request = new QueryAgreementMainTermsRequest();
        request.setEid(manufacturerDO.getEid());
        request.setAuthStatus(AgreementAuthStatusEnum.PASS.getCode());
        List<AgreementMainTermsDO> agreementMainTerms = agreementMainTermsService.getAgreementMainTerms(request);
        agreementMainTerms.forEach(agreementMainTermsDO -> {
            if (AgreementEffectStatusEnum.getByCode(agreementMainTermsDO.getEffectStatus()) != AgreementEffectStatusEnum.STOP
                    && AgreementEffectStatusEnum.getByCode(agreementMainTermsDO.getEffectStatus()) != AgreementEffectStatusEnum.INVALID) {

                List<Long> supplySalesGoodsList = agreementSupplySalesGoodsService.getSupplyGoodsByAgreementId(agreementMainTermsDO.getId())
                        .stream().map(AgreementSupplySalesGoodsDTO::getSpecificationGoodsId).distinct().collect(Collectors.toList());

                boolean flag = DateUtil.isIn(new Date(), agreementMainTermsDO.getStartTime(), agreementMainTermsDO.getEndTime());
                // 先看当前协议是否在有效期，在有效期就看商品是否已经在使用中
                if (flag) {
                    specificationIdList.forEach(goodsId -> {
                        if (supplySalesGoodsList.contains(goodsId)) {
                            throw new BusinessException(UserErrorCode.MANUFACTURER_GOODS_HAD_RELATION_AGREEMENT);
                        }
                    });
                }

            }
        });


        AgreementManufacturerGoodsDO manufacturerGoodsDO = new AgreementManufacturerGoodsDO();
        manufacturerGoodsDO.setOpUserId(opUserId);

        LambdaQueryWrapper<AgreementManufacturerGoodsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AgreementManufacturerGoodsDO::getId, idList);
        this.batchDeleteWithFill(manufacturerGoodsDO, queryWrapper);

        return true;
    }

    @Override
    public String getNameBySpecificationAndType(Long specificationGoodsId, Integer type) {
        LambdaQueryWrapper<AgreementManufacturerGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementManufacturerGoodsDO::getSpecificationGoodsId, specificationGoodsId);
        List<AgreementManufacturerGoodsDO> manufacturerGoodsDOList = this.list(wrapper);

        List<Long> manufactureIdList = manufacturerGoodsDOList.stream().map(AgreementManufacturerGoodsDO::getManufacturerId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(manufactureIdList)) {
            AgreementManufacturerDO manufacturerDO = manufacturerService.listByIds(manufactureIdList).stream().filter(
                    agreementManufacturerDO -> agreementManufacturerDO.getType().equals(type)).findAny().orElse(new AgreementManufacturerDO());

            return manufacturerDO.getEname();
        }

        return null;
    }

    @Override
    public Map<Long, List<AgreementManufacturerGoodsDTO>> getGoodsBySpecificationId(List<Long> specificationGoodsIdList) {
        LambdaQueryWrapper<AgreementManufacturerGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AgreementManufacturerGoodsDO::getSpecificationGoodsId, specificationGoodsIdList);
        List<AgreementManufacturerGoodsDTO> goodsDTOList = PojoUtils.map(this.list(wrapper), AgreementManufacturerGoodsDTO.class);
        return goodsDTOList.stream().collect(Collectors.groupingBy(AgreementManufacturerGoodsDTO::getSpecificationGoodsId));
    }

    @Override
    public Integer getManufactureGoodsNumberByEid(Long eid) {
        LambdaQueryWrapper<AgreementManufacturerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgreementManufacturerDO::getEid, eid);
        queryWrapper.last("limit 1");
        AgreementManufacturerDO manufacturerDO = manufacturerService.getOne(queryWrapper);
        if (Objects.isNull(manufacturerDO)) {
            return 0;
        }

        LambdaQueryWrapper<AgreementManufacturerGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementManufacturerGoodsDO::getManufacturerId, manufacturerDO.getId());
        long count = this.list(wrapper).stream().map(AgreementManufacturerGoodsDO::getSpecificationGoodsId).distinct().count();

        return (int) count;
    }

    @Override
    public List<AgreementManufacturerGoodsDTO> getManufactureGoodsListByEid(Long eid) {
        LambdaQueryWrapper<AgreementManufacturerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgreementManufacturerDO::getEid, eid);
        queryWrapper.last("limit 1");
        AgreementManufacturerDO manufacturerDO = manufacturerService.getOne(queryWrapper);
        if (Objects.isNull(manufacturerDO)) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<AgreementManufacturerGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementManufacturerGoodsDO::getManufacturerId, manufacturerDO.getId());
        return PojoUtils.map(this.list(wrapper), AgreementManufacturerGoodsDTO.class);
    }
}
