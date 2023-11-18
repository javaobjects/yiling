package com.yiling.mall.banner.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.banner.dto.B2bAppVajraPositionDTO;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionPageRequest;
import com.yiling.mall.banner.dto.request.SaveB2bAppVajraPositionRequest;
import com.yiling.mall.banner.entity.B2bAppVajraPositionDO;

/**
 * <p>
 * 金刚位表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-22
 */
public interface B2bAppVajraPositionService extends BaseService<B2bAppVajraPositionDO> {

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
     * @param id
     * @param sort
     * @param currentUserId
     * @return
     */
    boolean editWeight(Long id, Integer sort, Long currentUserId);

    /**
     * 金刚位修改启用状态
     *
     * @param id
     * @param vajraStatus   状态：1-启用 2-停用
     * @param currentUserId
     * @return
     */
    boolean editStatus(Long id, Integer vajraStatus, Long currentUserId);

    /**
     * 金刚位删除
     *
     * @param id
     * @param currentUserId
     * @return
     */
    boolean deleteById(Long id, Long currentUserId);

    /**
     * 分页列表查询金刚位-运营后台
     *
     * @param request
     * @return
     */
    Page<B2bAppVajraPositionDTO> pageList(B2bAppVajraPositionPageRequest request);

    /**
     * 根据状态查询出所有金刚位，按照权重和时间排序
     *
     * @param vajraStatus
     * @param source
     * @return
     */
    List<B2bAppVajraPositionDTO> listByStatus(Integer vajraStatus, int source);
}
