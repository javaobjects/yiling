package com.yiling.user.enterprise.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.api.EnterprisePromotionDownloadRecordApi;
import com.yiling.user.enterprise.bo.EnterprisePromotionDownloadRecordBO;
import com.yiling.user.enterprise.dto.request.QueryPromotionDownloadRecordPageRequest;
import com.yiling.user.enterprise.dto.request.SavePromotionDownloadRecordRequest;
import com.yiling.user.enterprise.service.EnterprisePromotionDownloadRecordService;

/**
 * 企业推广下载记录 API 实现
 *
 * @author: lun.yu
 * @date: 2023-06-01
 */
@DubboService
public class EnterprisePromotionDownloadRecordApiImpl implements EnterprisePromotionDownloadRecordApi {

    @Autowired
    private EnterprisePromotionDownloadRecordService promotionDownloadRecordService;

    @Override
    public Page<EnterprisePromotionDownloadRecordBO> queryListPage(QueryPromotionDownloadRecordPageRequest request) {
        return promotionDownloadRecordService.queryListPage(request);
    }

    @Override
    public boolean insertDownloadRecord(SavePromotionDownloadRecordRequest request) {
        return promotionDownloadRecordService.insertDownloadRecord(request);
    }

}
