package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.request.SaveEnterpriseTagsRequest;
import com.yiling.user.enterprise.entity.EnterpriseTagRelDO;

/**
 * <p>
 * 企业关联的标签信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-10-14
 */
public interface EnterpriseTagRelService extends BaseService<EnterpriseTagRelDO> {

    /**
     * 获取企业标签ID列表
     *
     * @param eid 企业ID
     * @return
     */
    List<Long> listByEid(Long eid);

    /**
     * 根据批量企业ID获取企业标签列表
     * @param eidList
     * @return
     */
    Map<Long,List<Long>> listByEidList(List<Long> eidList);

    /**
     * 添加企业标签关联信息
     *
     * @param eid 企业ID
     * @param tagIds 标签ID列表
     * @param type 类型
     * @param opUserId 操作人ID
     * @return
     */
    boolean addEnterpriseTags(Long eid, List<Long> tagIds, Integer type, Long opUserId);

    /**
     * 移除企业标签关联信息
     *
     * @param eid 企业ID
     * @param tagIds 标签ID列表
     * @param opUserId 操作人ID
     * @return
     */
    boolean removeEnterpriseTags(Long eid, List<Long> tagIds, Long opUserId);

    /**
     * 保存单个企业标签信息
     *
     * @param request
     * @return
     */
    boolean saveEnterpriseTags(SaveEnterpriseTagsRequest request);

    /**
     * 根据标签ID获取企业
     * @param tagsId
     * @return
     */
    List<Long> getEidListByTagId(Long tagsId);

    /**
     * 根据批量标签ID获取企业ID
     * @param tagsIdList
     * @return
     */
    List<Long> getEidListByTagIdList(List<Long> tagsIdList);
}
