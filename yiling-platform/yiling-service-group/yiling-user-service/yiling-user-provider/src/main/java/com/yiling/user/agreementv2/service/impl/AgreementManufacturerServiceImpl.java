package com.yiling.user.agreementv2.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dao.AgreementManufacturerMapper;
import com.yiling.user.agreementv2.dto.request.AddAgreementManufacturerRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerRequest;
import com.yiling.user.agreementv2.entity.AgreementMainTermsDO;
import com.yiling.user.agreementv2.entity.AgreementManufacturerDO;
import com.yiling.user.agreementv2.entity.AgreementManufacturerGoodsDO;
import com.yiling.user.agreementv2.enums.AgreementEffectStatusEnum;
import com.yiling.user.agreementv2.service.AgreementMainTermsService;
import com.yiling.user.agreementv2.service.AgreementManufacturerGoodsService;
import com.yiling.user.agreementv2.service.AgreementManufacturerService;
import com.yiling.user.common.UserErrorCode;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 厂家表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/22
 */
@Slf4j
@Service
public class AgreementManufacturerServiceImpl extends BaseServiceImpl<AgreementManufacturerMapper, AgreementManufacturerDO> implements AgreementManufacturerService {

    @Autowired
    private AgreementMainTermsService agreementMainTermsService;
    @Autowired
    private AgreementManufacturerGoodsService manufacturerGoodsService;


    @Override
    public Page<AgreementManufacturerDO> queryListPage(QueryAgreementManufacturerRequest request) {
        LambdaQueryWrapper<AgreementManufacturerDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getEid()) && request.getEid() != 0) {
            wrapper.eq(AgreementManufacturerDO::getEid, request.getEid());
        }
        if (StrUtil.isNotEmpty(request.getEname())) {
            wrapper.like(AgreementManufacturerDO::getEname, request.getEname());
        }

        return this.page(request.getPage(), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addManufacturer(AddAgreementManufacturerRequest request) {
        LambdaQueryWrapper<AgreementManufacturerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementManufacturerDO::getEid, request.getEid());
        AgreementManufacturerDO manufacturerDO = this.getOne(wrapper);
        if (Objects.nonNull(manufacturerDO)) {
            throw new BusinessException(UserErrorCode.HAD_ADD_MANUFACTURER);
        }

        AgreementManufacturerDO manufacturer = PojoUtils.map(request, AgreementManufacturerDO.class);
        return this.save(manufacturer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteManufacturer(Long id, Long opUserId) {
        AgreementManufacturerDO manufacturerDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.MANUFACTURER_NOT_EXIST));
        // 校验：如果厂家关联了协议，不可以删除
        Long eid = manufacturerDO.getEid();

        LambdaQueryWrapper<AgreementMainTermsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgreementMainTermsDO::getEid, eid);
        queryWrapper.notIn(AgreementMainTermsDO::getEffectStatus, ListUtil.toList(AgreementEffectStatusEnum.STOP.getCode(), AgreementEffectStatusEnum.INVALID.getCode()));
        List<AgreementMainTermsDO> mainTermsDOList = agreementMainTermsService.list(queryWrapper);
        if (CollUtil.isNotEmpty(mainTermsDOList)) {
            mainTermsDOList.forEach(agreementMainTermsDO -> {
                boolean flag = DateUtil.isIn(new Date(), agreementMainTermsDO.getStartTime(), agreementMainTermsDO.getEndTime());
                if (flag) {
                    throw new BusinessException(UserErrorCode.MANUFACTURER_HAD_RELATION_AGREEMENT);
                }
            });
        }

        AgreementManufacturerDO manufacturer = new AgreementManufacturerDO();
        manufacturer.setId(id);
        manufacturer.setOpUserId(opUserId);
        this.deleteByIdWithFill(manufacturer);

        // 删除厂家关联商品
        AgreementManufacturerGoodsDO manufacturerGoodsDO = new AgreementManufacturerGoodsDO();
        manufacturerGoodsDO.setOpUserId(opUserId);

        LambdaQueryWrapper<AgreementManufacturerGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementManufacturerGoodsDO::getManufacturerId, id);
        this.manufacturerGoodsService.batchDeleteWithFill(manufacturerGoodsDO, wrapper);

        return true;
    }
}
