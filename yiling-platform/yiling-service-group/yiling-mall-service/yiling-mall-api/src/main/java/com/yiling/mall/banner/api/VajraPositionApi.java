package com.yiling.mall.banner.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.banner.dto.B2bAppVajraPositionDTO;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionDeleteRequest;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionStatusRequest;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionWeightRequest;
import com.yiling.mall.banner.dto.request.SaveB2bAppVajraPositionRequest;

/**
 * @author: yong.zhang
 * @date: 2021/10/22
 */
public interface VajraPositionApi {

    /**
     * 根据id查询金刚位信息
     *
     * @param id
     * @return
     */
    B2bAppVajraPositionDTO queryById(Long id);

    /**
     * 新增和编辑金刚位信息
     *
     * @param request
     * @return
     */
    boolean saveB2bAppVajraPosition(SaveB2bAppVajraPositionRequest request);

    /**
     * 金刚位排序，权重数字修改
     *
     * @param request
     * @return
     */
    boolean editB2bAppVajraPositionWeight(B2bAppVajraPositionWeightRequest request);

    /**
     * 金刚位使用状态修改
     *
     * @param request
     * @return
     */
    boolean editB2bAppVajraPositionStatus(B2bAppVajraPositionStatusRequest request);

    /**
     * 删除金刚位
     *
     * @param request
     * @return
     */
    boolean deleteB2bAppVajraPosition(B2bAppVajraPositionDeleteRequest request);

    /**
     * 分页列表查询金刚位-运营后台
     *
     * @param request
     * @return
     */
    Page<B2bAppVajraPositionDTO> pageList(B2bAppVajraPositionPageRequest request);

    /**
     * app首页查询金刚位
     *
     * @return
     */
    List<B2bAppVajraPositionDTO> listAll(int source);
}
