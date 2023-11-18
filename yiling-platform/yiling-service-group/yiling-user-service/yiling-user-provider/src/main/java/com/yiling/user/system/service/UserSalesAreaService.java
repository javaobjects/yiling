package com.yiling.user.system.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.dto.request.SaveUserSalesAreaRequest;
import com.yiling.user.system.entity.UserSalesAreaDO;

/**
 * <p>
 * 用户销售区域 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-09-28
 */
public interface UserSalesAreaService extends BaseService<UserSalesAreaDO> {

    /**
     * 获取用户设置的销售区域编码列表
     *
     * @param userId 用户ID
     * @return
     */
    List<String> listSalesAreaCodeByUserId(Long userId);

    /**
     * 保存用户销售区域设置
     *
     * @param request
     * @return
     */
    boolean saveUserSalesArea(SaveUserSalesAreaRequest request);

    /**
     * 判断用户是否设置销售区域
     *
     * @param userId 用户ID
     * @return
     */
    boolean hasUserSalesAreaSetting(Long userId);

    /**
     * 根据用户ID获取销售区域
     * @param userId
     * @return
     */
    UserSalesAreaDO getSaleAreaByUserId(Long userId);

    /**
     * 根据用户ID获取用户销售区域编码，可获取城市和省份级别
     * @param userId 用户ID
     * @param level 根据Level指定，若要获取市级编码则level传入2，若要获取省级编码则level传入1，不传默认获取市级编码
     * @return 返回市级编码
     */
    List<String> getSaleAreaDetailByUserId(Long userId ,Integer level);
}
