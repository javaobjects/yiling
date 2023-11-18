package com.yiling.mall.banner.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.banner.dto.B2bAppHotWordsDTO;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsSaveRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsStatusRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsWeightRequest;

/**
 * 热词管理API
 *
 * @author: yong.zhang
 * @date: 2021/10/25
 */
public interface HotWordsApi {

    /**
     * 根据id查询热词信息
     *
     * @param id
     * @return
     */
    B2bAppHotWordsDTO queryById(Long id);

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
     * @param request
     * @return
     */
    boolean editB2bAppHotWordsWeight(B2bAppHotWordsWeightRequest request);

    /**
     * 热词使用状态修改
     *
     * @param request
     * @return
     */
    boolean editB2bAppHotWordsStatus(B2bAppHotWordsStatusRequest request);

    /**
     * 分页列表查询金刚位-运营后台
     *
     * @param request
     * @return
     */
    Page<B2bAppHotWordsDTO> pageList(B2bAppHotWordsPageRequest request);

    /**
     * 热门搜索热词
     *
     * @return
     */
    List<B2bAppHotWordsDTO> listAll(int source);
}
