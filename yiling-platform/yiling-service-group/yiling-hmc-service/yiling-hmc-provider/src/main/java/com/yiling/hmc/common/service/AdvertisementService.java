package com.yiling.hmc.common.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.common.dto.request.AdvertisementDeleteRequest;
import com.yiling.hmc.common.dto.request.AdvertisementListRequest;
import com.yiling.hmc.common.dto.request.AdvertisementPageRequest;
import com.yiling.hmc.common.dto.request.AdvertisementSaveRequest;
import com.yiling.hmc.common.entity.AdvertisementDO;

/**
 * <p>
 * 广告表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
public interface AdvertisementService extends BaseService<AdvertisementDO> {

    /**
     * 广告新增或修改保存
     *
     * @param request 新增请求参数
     * @return 成功/失败
     */
    boolean saveAdvertisement(AdvertisementSaveRequest request);

    /**
     * 广告删除
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    boolean deleteAdvertisement(AdvertisementDeleteRequest request);

    /**
     * 广告分页查询
     *
     * @param request 查询条件
     * @return 广告信息
     */
    Page<AdvertisementDO> pageList(AdvertisementPageRequest request);

    /**
     * list查询广告(按照权重排序)
     *
     * @param request 查询条件请求参数
     * @return 广告信息
     */
    List<AdvertisementDO> listByCondition(AdvertisementListRequest request);

}
