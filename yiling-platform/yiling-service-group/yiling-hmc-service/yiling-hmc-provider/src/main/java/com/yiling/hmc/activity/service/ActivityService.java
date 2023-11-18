package com.yiling.hmc.activity.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.activity.dto.ActivityDTO;
import com.yiling.hmc.activity.dto.ActivityDocToPatientDTO;
import com.yiling.hmc.activity.dto.request.BaseActivityRequest;
import com.yiling.hmc.activity.dto.request.QueryActivityRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityDocPatientRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityGoodsPromoteRequest;
import com.yiling.hmc.activity.entity.ActivityDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * C端活动 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2023-01-13
 */
public interface ActivityService extends BaseService<ActivityDO> {

    /**
     * 查询医带患活动列表
     *
     * @param request
     * @return
     */
    Page<ActivityDTO> pageList(QueryActivityRequest request);

    /**
     * 保存医带患
     *
     * @param request
     * @return
     */
    Long saveOrUpdateDocToPatient(SaveActivityDocPatientRequest request);

    /**
     * 查询医带患活动
     *
     * @param id
     * @return
     */
    ActivityDocToPatientDTO queryActivityById(Long id);

    /**
     * 停用活动
     *
     * @param request
     * @return
     */
    Boolean stopActivity(BaseActivityRequest request);

    /**
     * 根据活动id批量查询活动信息
     *
     * @param idList
     * @return
     */
    List<ActivityDocToPatientDTO> queryActivityByIdList(List<Long> idList);

    /**
     * 查询活动
     *
     * @param request
     * @return
     */
    List<ActivityDTO> queryActivity(QueryActivityRequest request);

    /**
     * 查询活动
     *
     * @param id
     * @return
     */
    ActivityDTO queryActivity(Long id);

    /**
     * 保存更新商品推广活动
     *
     * @param request
     * @return
     */
    Long saveOrUpdateGoodsPromote(SaveActivityGoodsPromoteRequest request);

    /**
     * 删除活动
     * @param request
     * @return
     */
    Boolean delActivity(BaseActivityRequest request);
}
