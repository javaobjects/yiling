package com.yiling.hmc.gzh.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.gzh.dto.GzhGreetingDTO;
import com.yiling.hmc.gzh.dto.request.PublishGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.QueryGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.SaveGzhGreetingRequest;

/**
 * HMC 公众号 API
 *
 * @Author fan.shen
 * @Date 2022-09-15
 */
public interface HmcGzhGreetingApi {


    /**
     * 活动列表
     *
     * @param request
     * @return
     */
    Page<GzhGreetingDTO> pageList(QueryGzhGreetingRequest request);

    /**
     * 获取详情
     * @param id
     * @return
     */
    GzhGreetingDTO getDetailById(Long id);

    /**
     * 获取详情
     * @param sceneId
     * @return
     */
    GzhGreetingDTO getDetailBySceneId(Long sceneId);

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
     * 更新数量
     * @param id
     */
    void updateTriggerCount(Long id);
}
