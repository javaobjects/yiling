package com.yiling.hmc.activity.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.activity.dto.ActivityDTO;
import com.yiling.hmc.activity.dto.ActivityDocToPatientDTO;
import com.yiling.hmc.activity.dto.request.BaseActivityRequest;
import com.yiling.hmc.activity.dto.request.QueryActivityRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityDocPatientRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityGoodsPromoteRequest;

import java.util.List;

/**
 * C端活动API
 *
 * @author: fan.shen
 * @date: 2023-01-13
 */
public interface HMCActivityApi {

    /**
     * 活动列表
     *
     * @param request
     * @return
     */
    Page<ActivityDTO> pageList(QueryActivityRequest request);

    /**
     * 保存医带患活动信息
     *
     * @param request
     * @return
     */
    Long saveOrUpdateDocToPatient(SaveActivityDocPatientRequest request);

    /**
     * 根据id查询医带患活动
     *
     * @param id
     * @return
     */
    ActivityDocToPatientDTO queryActivityById(Long id);

    /**
     * 根据id查询医带患活动
     *
     * @param idList
     * @return
     */
    List<ActivityDocToPatientDTO> queryActivityByIdList(List<Long> idList);

    /**
     * 停用活动
     *
     * @param request
     * @return
     */
    Boolean stopActivity(BaseActivityRequest request);

    /**
     * 删除活动
     * @param request
     * @return
     */
    Boolean delActivity(BaseActivityRequest request);

    /**
     * 查询活动
     *
     * @param build
     * @return
     */
    List<ActivityDTO> queryActivity(QueryActivityRequest build);

    /**
     * 构建导入数据
     *
     * @param activityId
     * @param doctorId
     * @return
     */
    String getQrCodeUrl(Long activityId, Integer doctorId);

    /**
     * 获取商品推广二维码
     *
     * @param activityId
     * @param doctorId
     * @return
     */
    String getGoodsPromoteQrCodeUrl(Integer activityId, Integer doctorId) throws Exception;

    /**
     * 保存商品推广活动
     *
     * @param request
     * @return
     */
    Long saveOrUpdateGoodsPromote(SaveActivityGoodsPromoteRequest request);

    /**
     * 查询商品推广活动
     *
     * @param id
     * @return
     */
    ActivityDTO queryActivityGoodsPromoteById(Long id);
}
