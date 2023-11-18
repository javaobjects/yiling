package com.yiling.goods.standard.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.dto.request.SaveStandardGoodsTagsRequest;
import com.yiling.goods.standard.entity.StandardGoodsTagRelDO;

/**
 * @author shichen
 * @类名 StandardGoodsTagRelService
 * @描述
 * @创建时间 2022/10/19
 * @修改人 shichen
 * @修改时间 2022/10/19
 **/
public interface StandardGoodsTagRelService extends BaseService<StandardGoodsTagRelDO> {
    /**
     * 获取标准库商品标签ID列表
     *
     * @param StandardId 标准库ID
     * @return
     */
    List<Long> listByStandardId(Long StandardId);

    /**
     * 根据批量企业ID获取企业标签列表
     * @param standardIdList
     * @return
     */
    Map<Long,List<Long>> listByStandardIdList(List<Long> standardIdList);

    /**
     * 根据标签ID获取标准库id
     * @param tagId
     * @return
     */
    List<Long> getStandardIdListByTagId(Long tagId);

    /**
     * 根据批量标签ID获取标准库ID
     * @param tagIdList
     * @return
     */
    List<Long> getStandardIdListByTagIdList(List<Long> tagIdList);

    /**
     * 保存单个标准库标签信息
     *
     * @param request
     * @return
     */
    boolean saveStandardGoodsTags(SaveStandardGoodsTagsRequest request);

    /**
     * 移除企业标签关联信息
     *
     * @param standardId 标准库ID
     * @param tagIds 标签ID列表
     * @param opUserId 操作人ID
     * @return
     */
    boolean removeStandardGoodsTags(Long standardId, List<Long> tagIds, Long opUserId);

    /**
     * 添加企业标签关联信息
     *
     * @param standardId 标准库ID
     * @param tagIds 标签ID列表
     * @param type 类型
     * @param opUserId 操作人ID
     * @return
     */
    boolean addStandardGoodsTags(Long standardId, List<Long> tagIds, Integer type, Long opUserId);
}
