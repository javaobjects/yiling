package com.yiling.user.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.bo.EnterprisePromotionDownloadRecordBO;
import com.yiling.user.enterprise.dto.request.QueryPromotionDownloadRecordPageRequest;
import com.yiling.user.enterprise.dto.request.SavePromotionDownloadRecordRequest;
import com.yiling.user.enterprise.entity.EnterprisePromotionDownloadRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 企业推广下载记录表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-01
 */
public interface EnterprisePromotionDownloadRecordService extends BaseService<EnterprisePromotionDownloadRecordDO> {

    /**
     * 查询企业推广下载记录分页列表
     *
     * @param request
     * @return
     */
    Page<EnterprisePromotionDownloadRecordBO> queryListPage(QueryPromotionDownloadRecordPageRequest request);

    /**
     * 保存企业推广下载记录
     *
     * @param request
     * @return
     */
    boolean insertDownloadRecord(SavePromotionDownloadRecordRequest request);
}
