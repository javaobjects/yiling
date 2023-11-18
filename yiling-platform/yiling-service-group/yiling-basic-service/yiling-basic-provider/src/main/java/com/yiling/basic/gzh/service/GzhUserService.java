package com.yiling.basic.gzh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.gzh.entity.GzhUserDO;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.basic.wx.dto.request.CreateGzhUserRequest;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import java.util.List;

/**
 * <p>
 * 公众号用户表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-26
 */
public interface GzhUserService extends BaseService<GzhUserDO> {

    /**
     * 根据unionId查询
     * @param unionId
     * @return
     */
    GzhUserDO getByUnionId(String unionId);

    /**
     * 获取公众号用户信息
     *
     * @param appId 公众号appId
     * @param openId 用户openId
     * @return com.yiling.basic.gzh.entity.GzhUserDO
     * @author xuan.zhou
     * @date 2022/9/21
     **/
    GzhUserDO getByOpenId(String appId, String openId);

    /**
     * 创建用户
     * @param request
     * @return
     */
    GzhUserDTO createGzhUser(CreateGzhUserRequest request);

    /**
     * 根据unionId查询
     * @param unionIdList
     * @return
     */
    List<GzhUserDTO> getByUnionIdList(List<String> unionIdList);

    /**
     * 获取公众号
     * @param unionId
     * @param appId
     * @return
     */
    GzhUserDTO getByUnionIdAndAppId(String unionId, String appId);

    /**
     * 获取公众号
     * @param openId
     * @return
     */
    GzhUserDTO getByGzhOpenId(String openId);

    void updateGzhUser(GzhUserDTO gzhUserDTO);

    /**
     * @param request
     * @return
     */
    Page<GzhUserDTO> pageList(QueryPageListRequest request);

    /**
     * 批量查询
     * @param idList
     * @return
     */
    List<GzhUserDTO> getByIdList(List<Long> idList);
}
