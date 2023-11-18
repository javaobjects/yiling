package com.yiling.ih.user.api;

import com.yiling.ih.user.dto.IHUserDTO;

import java.util.List;

/**
 * 互联网医院用户API
 *
 * @author: fan.shen
 * @date: 2022-11-18
 */
public interface IHUserApi {

    /**
     * 根据用户名称获取用户
     *
     * @param name
     * @return
     */
    List<IHUserDTO> getUserListByName(String name);

    /**
     * 根据用户id获取用户
     *
     * @param ids
     * @return
     */
    List<IHUserDTO> getUserListByIds(List<Integer> ids);


}
