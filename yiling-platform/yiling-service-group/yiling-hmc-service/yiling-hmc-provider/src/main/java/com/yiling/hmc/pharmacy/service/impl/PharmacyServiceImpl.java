package com.yiling.hmc.pharmacy.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.pharmacy.dto.PharmacyDTO;
import com.yiling.hmc.pharmacy.dto.request.PharmacyPageRequest;
import com.yiling.hmc.pharmacy.dto.request.SubmitPharmacyRequest;
import com.yiling.hmc.pharmacy.dto.request.UpdatePharmacyStatusRequest;
import com.yiling.hmc.pharmacy.entity.PharmacyDO;
import com.yiling.hmc.pharmacy.dao.PharmacyMapper;
import com.yiling.hmc.pharmacy.enums.HmcPharmacyStatusEnum;
import com.yiling.hmc.pharmacy.service.PharmacyService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.ih.pharmacy.api.IHPharmacyApi;
import com.yiling.ih.pharmacy.dto.IHPharmacyDTO;
import com.yiling.ih.pharmacy.dto.request.SyncPharmacyRequest;
import com.yiling.ih.pharmacy.dto.request.UpdateIHPharmacyStatusRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.OpenPlatformRequest;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 终端药店 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2023-05-06
 */
@Service
public class PharmacyServiceImpl extends BaseServiceImpl<PharmacyMapper, PharmacyDO> implements PharmacyService {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    IHPharmacyApi ihPharmacyApi;

    @Override
    public Long savePharmacy(SubmitPharmacyRequest request) {
        // 1、若是次商家没有开通C端药+险业务，进行创建的时候会给其开通药+险的销售与兑付功能
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
        EnterpriseHmcTypeEnum enterpriseHmcTypeEnum = EnterpriseHmcTypeEnum.getByCode(enterpriseDTO.getHmcType());
        if (null == enterpriseHmcTypeEnum) {
            OpenPlatformRequest openPlatformRequest = new OpenPlatformRequest();
            openPlatformRequest.setEid(request.getEid());
            openPlatformRequest.setEnterpriseHmcTypeEnum(EnterpriseHmcTypeEnum.MEDICINE_INSURANCE_CHECK);
            List<PlatformEnum> platformEnumList = CollUtil.newArrayList();
            platformEnumList.add(PlatformEnum.HMC);
            openPlatformRequest.setPlatformEnumList(platformEnumList);
            openPlatformRequest.setOpUserId(request.getOpUserId());
            openPlatformRequest.setOpTime(request.getOpTime());
            enterpriseApi.openPlatform(openPlatformRequest);
        }

        // 2、保存
        PharmacyDO pharmacyDO = PojoUtils.map(request, PharmacyDO.class);
        pharmacyDO.setStatus(HmcPharmacyStatusEnum.ENABLED.getType());
        SyncPharmacyRequest syncRequest = new SyncPharmacyRequest();
        syncRequest.setHmcEid(request.getEid());
        syncRequest.setPharmacyName(request.getEname());
        syncRequest.setContactPersonnel(enterpriseDTO.getContactor());
        syncRequest.setMobile(enterpriseDTO.getContactorPhone());
        String address = enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() + enterpriseDTO.getRegionName() + " " + enterpriseDTO.getAddress();
        syncRequest.setAddress(address);
        IHPharmacyDTO ihPharmacyDTO = ihPharmacyApi.syncPharmacy(syncRequest);
        pharmacyDO.setIhEid(ihPharmacyDTO.getId());
        pharmacyDO.setIhEname(ihPharmacyDTO.getPharmacyName());
        this.save(pharmacyDO);
        return pharmacyDO.getId();
    }

    @Override
    public Boolean updatePharmacyStatus(UpdatePharmacyStatusRequest request) {
        PharmacyDO pharmacyDO = this.getById(request.getId());
        pharmacyDO.setStatus(request.getStatus());
        boolean result = this.updateById(pharmacyDO);
        if (result) {
            UpdateIHPharmacyStatusRequest updateIHPharmacyStatusRequest = new UpdateIHPharmacyStatusRequest();
            updateIHPharmacyStatusRequest.setId(pharmacyDO.getIhEid().intValue());
            // 状态：1-启用，2-停用
            if (request.getStatus() == 1) {
                // 合作状态 0：开启 1：关闭
                updateIHPharmacyStatusRequest.setCooperationState(0);
            }
            if (request.getStatus() == 2) {
                updateIHPharmacyStatusRequest.setCooperationState(1);
            }
            ihPharmacyApi.updatePharmacyStatus(updateIHPharmacyStatusRequest);
        }
        return result;
    }

    @Override
    public void updatePharmacyQrCode(PharmacyDTO pharmacyDTO) {
        PharmacyDO pharmacyDO = new PharmacyDO();
        pharmacyDO.setQrCode(pharmacyDTO.getQrCode());
        QueryWrapper<PharmacyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PharmacyDO::getId, pharmacyDTO.getId());
        this.update(pharmacyDO, wrapper);
    }

    @Override
    public Page<PharmacyDTO> pharmacyPageList(PharmacyPageRequest request) {
        QueryWrapper<PharmacyDO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(request.getEid()) && request.getEid() != 0L) {
            wrapper.lambda().eq(PharmacyDO::getEid, request.getEid());
        }
        wrapper.lambda().like(StrUtil.isNotBlank(request.getEname()), PharmacyDO::getEname, request.getEname());
        wrapper.lambda().eq(Objects.nonNull(request.getStatus()), PharmacyDO::getStatus, request.getStatus());
        if (Objects.nonNull(request.getStartTime())) {
            wrapper.lambda().ge(PharmacyDO::getCreateTime, DateUtil.beginOfDay(request.getStartTime()));
        }
        if (Objects.nonNull(request.getEndTime())) {
            wrapper.lambda().le(PharmacyDO::getCreateTime, DateUtil.endOfDay(request.getEndTime()));
        }
        Page<PharmacyDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page, PharmacyDTO.class);
    }

    @Override
    public List<PharmacyDTO> pharmacyList() {
        QueryWrapper<PharmacyDO> wrapper = new QueryWrapper<>();
        List<PharmacyDO> list = this.list(wrapper);
        return PojoUtils.map(list, PharmacyDTO.class);
    }

    @Override
    public PharmacyDTO findByIhEid(Long ihEid) {
        QueryWrapper<PharmacyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PharmacyDO::getIhEid, ihEid);
        PharmacyDO one = this.getOne(wrapper);
        return PojoUtils.map(one, PharmacyDTO.class);
    }

    @Override
    public boolean check(SubmitPharmacyRequest request) {
        QueryWrapper<PharmacyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PharmacyDO::getEid, request.getEid());
        PharmacyDO one = this.getOne(wrapper);
        return Objects.nonNull(one);
    }
}
