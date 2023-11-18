package com.yiling.hmc.gzh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.gzh.dto.GzhGreetingDTO;
import com.yiling.hmc.gzh.dto.request.PublishGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.QueryGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.SaveGzhGreetingRequest;
import com.yiling.hmc.gzh.entity.GzhGreetingDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 公众号欢迎语 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-28
 */
public interface GzhGreetingService extends BaseService<GzhGreetingDO> {

    /**
     * 列表查询
     * @param request
     * @return
     */
    Page<GzhGreetingDTO> pageList(QueryGzhGreetingRequest request);

    /**
     * 保存
     * @param request
     * @return
     */
    Long saveGreetings(SaveGzhGreetingRequest request);

    /**
     * 发布
     * @param request
     * @return
     */
    Boolean publishGreetings(PublishGzhGreetingRequest request);

    /**
     * 校验场景是否存在
     * @param sceneId
     * @return
     */
    GzhGreetingDTO checkIsExists(Integer sceneId);

    /**
     *
     * @param sceneId
     * @return
     */
    GzhGreetingDTO getDetailBySceneId(Long sceneId);

    /**
     * 更新触发数量
     * @param id
     */
    void updateTriggerCount(Long id);
}
