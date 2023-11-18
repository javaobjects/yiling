package com.yiling.user.enterprise.service.impl;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.bo.EnterprisePromotionDownloadRecordBO;
import com.yiling.user.enterprise.dto.request.QueryPromotionDownloadRecordPageRequest;
import com.yiling.user.enterprise.dto.request.SavePromotionDownloadRecordRequest;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.entity.EnterprisePromotionDownloadRecordDO;
import com.yiling.user.enterprise.dao.EnterprisePromotionDownloadRecordMapper;
import com.yiling.user.enterprise.service.EnterprisePromotionDownloadRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.service.EnterpriseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 企业推广下载记录表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-01
 */
@Slf4j
@Service
public class EnterprisePromotionDownloadRecordServiceImpl extends BaseServiceImpl<EnterprisePromotionDownloadRecordMapper, EnterprisePromotionDownloadRecordDO> implements EnterprisePromotionDownloadRecordService {

    @Autowired
    EnterpriseService enterpriseService;

    @Override
    public Page<EnterprisePromotionDownloadRecordBO> queryListPage(QueryPromotionDownloadRecordPageRequest request) {
        if (Objects.nonNull(request.getStartDownloadTime())) {
            request.setStartDownloadTime(DateUtil.beginOfDay(request.getStartDownloadTime()));
        }
        if (Objects.nonNull(request.getEndDownloadTime())) {
            request.setEndDownloadTime(DateUtil.endOfDay(request.getEndDownloadTime()));
        }
        return this.baseMapper.queryListPage(request.getPage(), request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDownloadRecord(SavePromotionDownloadRecordRequest request) {
        EnterpriseDO enterpriseDO = Optional.ofNullable(enterpriseService.getById(request.getPromoterId())).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        EnterprisePromotionDownloadRecordDO downloadRecordDO = new EnterprisePromotionDownloadRecordDO();
        downloadRecordDO.setPromoterId(enterpriseDO.getId());
        downloadRecordDO.setDownloadTime(new Date());
        return this.save(downloadRecordDO);
    }


}
