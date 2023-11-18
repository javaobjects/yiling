package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.enterprise.dto.request.CreateTagsRequest;
import com.yiling.user.enterprise.dto.request.QueryTagsRequest;
import com.yiling.user.enterprise.dto.request.UpdateTagsRequest;
import com.yiling.user.enterprise.entity.EnterpriseTagDO;

/**
 * <p>
 * 企业标签信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-10-14
 */
public interface EnterpriseTagService extends BaseService<EnterpriseTagDO> {

    /**
     * 获取所有标签
     *
     * @param statusEnum 状态枚举
     * @return
     */
    List<EnterpriseTagDO> listAll(EnableStatusEnum statusEnum);

    /**
     * 获取企业标签列表
     *
     * @param eid
     * @return
     */
    List<EnterpriseTagDO> listByEid(Long eid);

    /**
     * 创建标签
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
     * 批量删除
     *
     * @param idList
     * @param opUserId
     * @return
     */
    Boolean batchDeleteTags(List<Long> idList, Long opUserId);

    /**
     * 分页查询标签
     *
     * @param request
     * @return
     */
    Page<EnterpriseTagDTO> queryTagsListPage(QueryTagsRequest request);

    /**
     * 根据批量企业ID获取企业标签列表
     *
     * @param eidList
     * @return
     */
    Map<Long, List<EnterpriseTagDTO>> listByEidList(List<Long> eidList);

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
     * @param tagNameList
     * @return
     */
    List<Long> getEidListByTagsNameList(List<String> tagNameList);

}
