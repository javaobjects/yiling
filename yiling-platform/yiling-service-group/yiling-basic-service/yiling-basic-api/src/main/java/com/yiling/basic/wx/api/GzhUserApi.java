package com.yiling.basic.wx.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.basic.wx.dto.request.CreateGzhUserRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;

/**
 * 公众号用户API
 * @Author fan.shen
 * @Date 2022/3/26
 */
public interface GzhUserApi {

    /**
     * 根据unionId查询
     * @param unionId
     * @return
     */
    GzhUserDTO getByUnionId(String unionId);

    /**
     * 根据unionId查询
     * @param unionId
     * @param appId
     * @return
     */
    GzhUserDTO getByUnionIdAndAppId(String unionId, String appId);
    /**
     * 根据unionId查询
     * @param openId
     * @return
     */
    GzhUserDTO getByGzhOpenId(String openId);

    /**
     * 根据unionId查询
     * @param unionIdList
     * @return
     */
    List<GzhUserDTO> getByUnionIdList(List<String> unionIdList);


    /**
     * 创建用户
     * @param request
     * @return
     */
    GzhUserDTO createGzhUser(CreateGzhUserRequest request);

    /**
     * 更新公众号用户
     * @param gzhUserDTO
     */
    void updateGzhUser(GzhUserDTO gzhUserDTO);

    /**
     * 查询用户
     * @param valueOf
     * @return
     */
    GzhUserDTO getById(Long valueOf);

    /**
     * 查询用户
     * @param idList
     * @return
     */
    List<GzhUserDTO> getByIdList(List<Long> idList);

    /**
     * 分页获取用户
     * @param request
     * @return
     */
    Page<GzhUserDTO> pageList(QueryPageListRequest request);
}
