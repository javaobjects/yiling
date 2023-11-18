package com.yiling.goods.standard.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.goods.standard.dto.StandardGoodsTagDTO;
import com.yiling.goods.standard.dto.request.CreateStandardTagRequest;
import com.yiling.goods.standard.dto.request.QueryStandardGoodsTagsRequest;
import com.yiling.goods.standard.dto.request.UpdateStandardTagRequest;
import com.yiling.goods.standard.entity.StandardGoodsTagDO;

/**
 * @author shichen
 * @类名 StandardGoodsTagService
 * @描述
 * @创建时间 2022/10/19
 * @修改人 shichen
 * @修改时间 2022/10/19
 **/
public interface StandardGoodsTagService extends BaseService<StandardGoodsTagDO> {

    /**
     * 获取所有标签
     *
     * @param statusEnum 状态枚举
     * @return
     */
    List<StandardGoodsTagDTO> listAll(EnableStatusEnum statusEnum);

    /**
     * 获取标准库商品标签列表
     *
     * @param standardId
     * @return
     */
    List<StandardGoodsTagDTO> listByStandardId(Long standardId);

    /**
     * 根据批量标准库ID获取标准库标签列表
     *
     * @param standardIdList
     * @return
     */
    Map<Long, List<StandardGoodsTagDTO>> listByStandardIdList(List<Long> standardIdList);

    /**
     * 分页查询标签
     *
     * @param request
     * @return
     */
    Page<StandardGoodsTagDTO> queryTagsListPage(QueryStandardGoodsTagsRequest request);

    /**
     * 创建标签
     *
     * @param request
     * @return
     */
    Boolean createTags(CreateStandardTagRequest request);

    /**
     * 更新标签
     *
     * @param request
     * @return
     */
    Boolean updateTags(UpdateStandardTagRequest request);

    /**
     * 批量删除
     *
     * @param idList
     * @param opUserId
     * @return
     */
    Boolean batchDeleteTags(List<Long> idList, Long opUserId);

    /**
     * 根据标签名称查询标签对应的标准库商品ID
     *
     * @param name 标签名称
     * @return
     */
    List<Long> getStandardIdListByTagsName(String name);

    /**
     * 根据多个标签名称查询标签对应的标准库商品ID
     *
     * @param tagNameList
     * @return
     */
    List<Long> getStandardIdListByTagsNameList(List<String> tagNameList);


    /**
     * 根据标签名称查询标签
     *
     * @param name 标签名称
     * @return
     */
    StandardGoodsTagDTO getTagByTagsName(String name);
}
