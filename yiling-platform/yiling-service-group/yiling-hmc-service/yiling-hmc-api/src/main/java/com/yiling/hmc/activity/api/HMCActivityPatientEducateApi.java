package com.yiling.hmc.activity.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.activity.dto.ActivityPatientEducateDTO;
import com.yiling.hmc.activity.dto.request.QueryActivityPatientEducateRequest;
import com.yiling.hmc.activity.dto.request.QueryActivityRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityPatientEducationRequest;

import java.util.List;

/**
 * 患教活动API
 * @author: fan.shen
 * @date: 2022/09/01
 */
public interface HMCActivityPatientEducateApi {

    /**
     * 患教活动列表
     * @param request
     * @return
     */
    Page<ActivityPatientEducateDTO> pageList(QueryActivityPatientEducateRequest request);

    /**
     * 保存患教活动
     * @param request
     * @return
     */
    ActivityPatientEducateDTO saveActivityPatientEducate(SaveActivityPatientEducationRequest request);

    /**
     * 根据id查询患教活动
     * @param id
     * @return
     */
    ActivityPatientEducateDTO queryActivityById(Long id);

    /**
     * 查询患教活动
     * @param request
     * @return
     */
    List<ActivityPatientEducateDTO> queryActivity(QueryActivityRequest request);

    /**
     * 删除患教活动
     * @param id
     * @return
     */
    boolean delActivityById(Long id);

    /**
     * 获取所有患教活动
     * @return
     */
    List<ActivityPatientEducateDTO> getActivityPatientEducate(List<Long> idList);
}
