package com.yiling.mall.openposition.service.impl;

import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.openposition.dto.OpenPositionDTO;
import com.yiling.mall.openposition.dto.request.QueryOpenPositionPageRequest;
import com.yiling.mall.openposition.dto.request.SaveOpenPositionRequest;
import com.yiling.mall.openposition.dto.request.UpdateOpenPositionStatusRequest;
import com.yiling.mall.openposition.entity.OpenPositionDO;
import com.yiling.mall.openposition.dao.OpenPositionMapper;
import com.yiling.mall.openposition.enums.OpenPositionStatusEnum;
import com.yiling.mall.openposition.service.OpenPositionService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.common.util.WrapperUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * B2B-开屏位表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-06
 */
@Slf4j
@Service
public class OpenPositionServiceImpl extends BaseServiceImpl<OpenPositionMapper, OpenPositionDO> implements OpenPositionService {

    private static final String BD = "BD";

    @Override
    public Page<OpenPositionDTO> queryListPage(QueryOpenPositionPageRequest request) {
        QueryWrapper<OpenPositionDO> wrapper = WrapperUtils.getWrapper(request);
        wrapper.lambda().orderByDesc(OpenPositionDO::getStatus, OpenPositionDO::getUpdateTime);
        return PojoUtils.map(this.page(request.getPage(), wrapper), OpenPositionDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOpenPosition(SaveOpenPositionRequest request) {
        OpenPositionDO positionDO = PojoUtils.map(request, OpenPositionDO.class);

        if (request.getStatus().equals(OpenPositionStatusEnum.HAVE_RELEASE.getCode())) {
            // 如果为发布操作，同时将其它已发布的下线
            this.updateOtherStatus(request.getStatus(), request.getPlatform(), request.getOpUserId());
        }

        if (Objects.isNull(request.getId()) || request.getId() == 0) {
            // 生成编号：ID生成规则为：大写字母“BD”+三位数字，初始ID组合样式为“BD001",当ID＞999时，自动变成四位数，自增规则以此类推
            Integer serialNo = this.generateSerialNo(request.getPlatform());
            String format = String.format("%03d", serialNo);
            log.info("生成开屏位的编号={}", BD+format);
            positionDO.setNo(BD + format);
            positionDO.setSerialNo(serialNo);
            this.save(positionDO);

        } else {
            this.updateById(positionDO);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOpenPosition(Long id, Long opUserId) {
        OpenPositionDO positionDO = new OpenPositionDO();
        positionDO.setId(id);
        positionDO.setOpUserId(opUserId);
        return this.deleteByIdWithFill(positionDO) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(UpdateOpenPositionStatusRequest request) {
        // 如果为发布操作，同时将其它已发布的下线
        this.updateOtherStatus(request.getStatus(), request.getPlatform(), request.getOpUserId());

        OpenPositionDO positionDO = PojoUtils.map(request, OpenPositionDO.class);
        return this.updateById(positionDO);
    }

    @Override
    public OpenPositionDTO getOpenPositionPicture(Integer platform) {
        LambdaQueryWrapper<OpenPositionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OpenPositionDO::getPlatform, platform);
        wrapper.eq(OpenPositionDO::getStatus, OpenPositionStatusEnum.HAVE_RELEASE.getCode());
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), OpenPositionDTO.class);
    }

    private void updateOtherStatus(Integer status, Integer platform, Long opUserId) {
        // 如果为发布操作，同时将其它已发布的下线
        if (OpenPositionStatusEnum.getByCode(status) == OpenPositionStatusEnum.HAVE_RELEASE) {
            LambdaQueryWrapper<OpenPositionDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OpenPositionDO::getPlatform, platform);
            wrapper.eq(OpenPositionDO::getStatus, OpenPositionStatusEnum.HAVE_RELEASE.getCode());
            wrapper.last("limit 1");
            OpenPositionDO openPositionDO = this.getOne(wrapper);
            if (Objects.nonNull(openPositionDO)) {
                OpenPositionDO positionDO = new OpenPositionDO();
                positionDO.setId(openPositionDO.getId());
                positionDO.setStatus(OpenPositionStatusEnum.NOT_RELEASE.getCode());
                positionDO.setOpUserId(opUserId);
                this.updateById(positionDO);
            }
        }
    }


    /**
     * 生成序号
     *
     * @return 最新的序列号
     */
    public Integer generateSerialNo(Integer platform){
        QueryWrapper<OpenPositionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OpenPositionDO::getPlatform, platform);
        wrapper.select("max(serial_no) as serial_no");
        OpenPositionDO positionDO = this.getOne(wrapper);

        if (Objects.isNull(positionDO)) {
            return 1;
        } else {
            return positionDO.getSerialNo() + 1;
        }
    }


}
