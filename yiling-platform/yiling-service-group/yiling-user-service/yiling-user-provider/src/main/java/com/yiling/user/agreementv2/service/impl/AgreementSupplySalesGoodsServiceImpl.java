package com.yiling.user.agreementv2.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesGoodsDTO;
import com.yiling.user.agreementv2.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesGoodsDO;
import com.yiling.user.agreementv2.dao.AgreementSupplySalesGoodsMapper;
import com.yiling.user.agreementv2.service.AgreementSupplySalesGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 协议供销商品表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementSupplySalesGoodsServiceImpl extends BaseServiceImpl<AgreementSupplySalesGoodsMapper, AgreementSupplySalesGoodsDO> implements AgreementSupplySalesGoodsService {

    @Override
    public Integer getSupplyGoodsNumber(Long agreementId) {
        LambdaQueryWrapper<AgreementSupplySalesGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementSupplySalesGoodsDO::getAgreementId, agreementId);
        List<AgreementSupplySalesGoodsDO> list = this.list(wrapper);

        long count = list.stream().map(AgreementSupplySalesGoodsDO::getSpecificationGoodsId).distinct().count();
        return (int) count;
    }

    @Override
    public Page<AgreementSupplySalesGoodsDTO> getSupplyGoodsByAgreementId(QueryAgreementGoodsPageRequest request) {
        LambdaQueryWrapper<AgreementSupplySalesGoodsDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getId()) && request.getId() != 0) {
            wrapper.eq(AgreementSupplySalesGoodsDO::getAgreementId, request.getId());
        }
        if (Objects.nonNull(request.getGoodsId()) && request.getGoodsId() != 0) {
            wrapper.eq(AgreementSupplySalesGoodsDO::getGoodsId, request.getGoodsId());
        }
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            wrapper.like(AgreementSupplySalesGoodsDO::getGoodsName, request.getGoodsName());
        }

        return PojoUtils.map(this.page(request.getPage(), wrapper), AgreementSupplySalesGoodsDTO.class);
    }

    @Override
    public List<AgreementSupplySalesGoodsDTO> getSupplyGoodsList(Long controlGoodsGroupId) {
        LambdaQueryWrapper<AgreementSupplySalesGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementSupplySalesGoodsDO::getControlGoodsGroupId, controlGoodsGroupId);
        return PojoUtils.map(this.list(wrapper), AgreementSupplySalesGoodsDTO.class);
    }

    @Override
    public List<AgreementSupplySalesGoodsDTO> getSupplyGoodsByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementSupplySalesGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementSupplySalesGoodsDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementSupplySalesGoodsDTO.class);
    }
}
