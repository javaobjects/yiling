package com.yiling.mall.banner.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.banner.dto.B2bAppHotWordsDTO;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsSaveRequest;
import com.yiling.mall.banner.entity.B2bAppHotWordsDO;

/**
 * <p>
 * 热词管理表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-25
 */
public interface B2bAppHotWordsService extends BaseService<B2bAppHotWordsDO> {

    /**
     * 新建热词
     *
     * @param request
     * @return
     */
    boolean saveB2bAppHotWords(B2bAppHotWordsSaveRequest request);

    /**
     * 热词排序，权重数字修改
     *
     * @param id
     * @param sort
     * @param currentUserId
     * @return
     */
    boolean editWeight(Long id, Integer sort, Long currentUserId);

    /**
     * 热词修改启用状态
     *
     * @param id
     * @param useStatus     状态：1-启用 2-停用
     * @param currentUserId
     * @return
     */
    boolean editStatus(Long id, Integer useStatus, Long currentUserId);

    /**
     * 分页列表查询金刚位-运营后台
     *
     * @param request
     * @return
     */
    Page<B2bAppHotWordsDTO> pageList(B2bAppHotWordsPageRequest request);

    /**
     * 根据状态查询出所有热词，按照权重和时间排序
     *
     * @return
     */
    List<B2bAppHotWordsDTO> listByStatus(Integer useStatus, int source);
}
