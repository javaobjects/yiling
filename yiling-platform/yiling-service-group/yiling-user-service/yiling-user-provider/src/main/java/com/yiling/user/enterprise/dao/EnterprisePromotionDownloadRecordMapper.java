package com.yiling.user.enterprise.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.bo.EnterprisePromotionDownloadRecordBO;
import com.yiling.user.enterprise.dto.request.QueryPromotionDownloadRecordPageRequest;
import com.yiling.user.enterprise.entity.EnterprisePromotionDownloadRecordDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 企业推广下载记录表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-01
 */
@Repository
public interface EnterprisePromotionDownloadRecordMapper extends BaseMapper<EnterprisePromotionDownloadRecordDO> {

    /**
     * 查询企业推广下载记录分页列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<EnterprisePromotionDownloadRecordBO> queryListPage(Page page, @Param("request") QueryPromotionDownloadRecordPageRequest request);
}
