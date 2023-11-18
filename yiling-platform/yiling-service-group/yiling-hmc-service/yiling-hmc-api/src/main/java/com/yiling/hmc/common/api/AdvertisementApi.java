package com.yiling.hmc.common.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.common.dto.AdvertisementDTO;
import com.yiling.hmc.common.dto.request.AdvertisementDeleteRequest;
import com.yiling.hmc.common.dto.request.AdvertisementListRequest;
import com.yiling.hmc.common.dto.request.AdvertisementPageRequest;
import com.yiling.hmc.common.dto.request.AdvertisementSaveRequest;

/**
 * C端广告API
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
public interface AdvertisementApi {

    /**
     * 通过id查询广告信息
     *
     * @param id 主键id
     * @return 广告信息
     */
    AdvertisementDTO queryById(Long id);

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
    Page<AdvertisementDTO> pageList(AdvertisementPageRequest request);

    /**
     * list查询广告(按照权重排序)
     *
     * @param request 查询条件请求参数
     * @return 广告信息
     */
    List<AdvertisementDTO> listByCondition(AdvertisementListRequest request);
}
