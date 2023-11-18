package com.yiling.mall.openposition.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.mall.openposition.dto.OpenPositionDTO;
import com.yiling.mall.openposition.dto.request.QueryOpenPositionPageRequest;
import com.yiling.mall.openposition.dto.request.SaveOpenPositionRequest;
import com.yiling.mall.openposition.dto.request.UpdateOpenPositionStatusRequest;
import com.yiling.mall.openposition.entity.OpenPositionDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * B2B-开屏位表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-06
 */
public interface OpenPositionService extends BaseService<OpenPositionDO> {

    /**
     * 分页查询开屏位
     *
     * @param request
     * @return
     */
    Page<OpenPositionDTO> queryListPage(QueryOpenPositionPageRequest request);

    /**
     * 保存开屏位
     *
     * @param request
     * @return
     */
    boolean saveOpenPosition(SaveOpenPositionRequest request);

    /**
     * 删除开屏位
     *
     * @param id
     * @param opUserId
     * @return
     */
    boolean deleteOpenPosition(Long id, Long opUserId);

    /**
     * 更新状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateOpenPositionStatusRequest request);

    /**
     * 获取开屏位图
     *
     * @return
     */
    OpenPositionDTO getOpenPositionPicture(Integer platform);

}
