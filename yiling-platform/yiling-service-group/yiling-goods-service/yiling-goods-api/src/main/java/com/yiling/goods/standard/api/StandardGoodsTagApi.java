package com.yiling.goods.standard.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.goods.standard.dto.StandardGoodsTagDTO;
import com.yiling.goods.standard.dto.request.CreateStandardTagRequest;
import com.yiling.goods.standard.dto.request.QueryStandardGoodsTagsRequest;
import com.yiling.goods.standard.dto.request.SaveStandardGoodsTagsRequest;
import com.yiling.goods.standard.dto.request.UpdateStandardTagRequest;

/**
 * @author shichen
 * @类名 StandardGoodsTagApi
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
public interface StandardGoodsTagApi {

    /**
     * 获取所有标签
     *
     * @param statusEnum 状态枚举
     * @return
     */
    List<StandardGoodsTagDTO> listAll(EnableStatusEnum statusEnum);

    /**
     * 获取标准库标签列表
     *
     * @param standardId
     * @return
     */
    List<StandardGoodsTagDTO> listByStandardId(Long standardId);

    /**
     * 根据批量标准库ID获取标准库标签列表
     *
     * @param standardIdList 标准库ID集合
     * @return key为标准库ID，value为标准库标签集合
     */
    Map<Long, List<StandardGoodsTagDTO>> listByStandardIdList(List<Long> standardIdList);

    /**
     * 根据标签ID获取标准库
     *
     * @param tagId
     * @return
     */
    List<Long> getStandardIdListByTagId(Long tagId);

    /**
     * 根据标签ID集合获取标准库
     *
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
     * 新增标签
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
    Page<StandardGoodsTagDTO> queryTagsListPage(QueryStandardGoodsTagsRequest request);

    /**
     * 根据标签名称查询标签对应的标准库ID
     *
     * @param name 标签名称
     * @return
     */
    List<Long> getStandardIdListByTagsName(String name);

    /**
     * 根据多个标签名称查询标签对应的标准库ID
     *
     * @param tagNameList 标签名称集合
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
