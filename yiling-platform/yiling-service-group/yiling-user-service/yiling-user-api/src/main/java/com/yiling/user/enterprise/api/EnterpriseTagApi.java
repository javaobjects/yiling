package com.yiling.user.enterprise.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.enterprise.dto.request.CreateTagsRequest;
import com.yiling.user.enterprise.dto.request.QueryTagsRequest;
import com.yiling.user.enterprise.dto.request.SaveEnterpriseTagsRequest;
import com.yiling.user.enterprise.dto.request.UpdateTagsRequest;

/**
 * 企业标签 API
 *
 * @author: xuan.zhou
 * @date: 2021/10/14
 */
public interface EnterpriseTagApi {

    /**
     * 获取所有标签
     *
     * @param statusEnum 状态枚举
     * @return
     */
    List<EnterpriseTagDTO> listAll(EnableStatusEnum statusEnum);

    /**
     * 获取企业标签列表
     *
     * @param eid
     * @return
     */
    List<EnterpriseTagDTO> listByEid(Long eid);

    /**
     * 根据批量企业ID获取企业标签列表
     *
     * @param eidList 企业ID集合
     * @return key为企业ID，value为企业标签集合
     */
    Map<Long, List<EnterpriseTagDTO>> listByEidList(List<Long> eidList);

    /**
     * 根据标签ID获取企业
     *
     * @param tagsId
     * @return
     */
    List<Long> getEidListByTagId(Long tagsId);

    /**
     * 根据标签ID集合获取企业
     *
     * @param tagsIdList
     * @return
     */
    List<Long> getEidListByTagIdList(List<Long> tagsIdList);

    /**
     * 保存单个企业标签信息
     *
     * @param request
     * @return
     */
    boolean saveEnterpriseTags(SaveEnterpriseTagsRequest request);

    /**
     * 新增标签
     *
     * @param request
     * @return
     */
    Boolean createTags(CreateTagsRequest request);

    /**
     * 更新标签
     *
     * @param request
     * @return
     */
    Boolean updateTags(UpdateTagsRequest request);

    /**
     * 删除标签
     *
     * @param tagsIdList
     * @param currentUserId
     * @return
     */
    Boolean batchDeleteTags(List<Long> tagsIdList, Long currentUserId);

    /**
     * 分页查询标签
     *
     * @param request
     * @return
     */
    Page<EnterpriseTagDTO> queryTagsListPage(QueryTagsRequest request);

    /**
     * 根据标签名称查询标签对应的企业ID
     *
     * @param name 标签名称
     * @param fuzzy 是否模糊查询
     * @return
     */
    List<Long> getEidListByTagsName(String name, boolean fuzzy);

    /**
     * 根据多个标签名称查询标签对应的企业ID
     *
     * @param tagNameList 标签名称集合
     * @return
     */
    List<Long> getEidListByTagsNameList(List<String> tagNameList);
}
