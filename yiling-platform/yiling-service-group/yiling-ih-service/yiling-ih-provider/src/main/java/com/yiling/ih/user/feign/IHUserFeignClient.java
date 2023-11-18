package com.yiling.ih.user.feign;

import com.yiling.ih.common.ApiResult;
import com.yiling.ih.user.feign.dto.response.IHUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 互联网医院 用户模块接口调用
 *
 * @author: fan.shen
 * @date: 2022-11-18
 */
@FeignClient(name = "ihUserFeignClient", url = "${ih.service.baseUrl}")
public interface IHUserFeignClient {

    /**
     * 根据名称模糊查询账号信息
     *
     * @param name 用户名称
     * @return
     */
    @GetMapping("/cms/adminuser/getUserListByName")
    ApiResult<List<IHUserResponse>> getUserListByName(@RequestParam("name") String name);

    /**
     * 根据id集合模糊查询账号信息
     *
     * @param ids idList
     * @return
     */
    @GetMapping("/cms/adminuser/getUserListByIds")
    ApiResult<List<IHUserResponse>> getUserListByIds(@RequestParam("ids") List<Integer> ids);

}
