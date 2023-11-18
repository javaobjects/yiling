package com.yiling.sjms.gb.api;

import java.util.List;
import java.util.Map;


/**
 * 机构orgmange api
 *
 * @author: shixing.sun
 * @date: 2023/02/14
 */
public interface GbOrgMangerApi {

    /**
     * 获取所有业务部负责人关系
     * @return
     */
    Map<Long,String> getGBFormList();


    /**
     * 获取所有业务部负责人关系
     * @return
     */
    List<Long> listOrgIds();
}
