package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.entity.EnterpriseSalesAreaDetailDO;

/**
 * <p>
 * 企业销售区域详情 服务类
 * </p>
 *
 * @author zhouxuan
 * @date 2021-10-29
 */
public interface EnterpriseSalesAreaDetailService extends BaseService<EnterpriseSalesAreaDetailDO> {

    /**
     * 获取企业销售区域详情
     *
     * @param eid
     * @return
     */
    List<EnterpriseSalesAreaDetailDO> getEnterpriseSaleAreaDetail(Long eid);

    /**
     * 批量获取企业销售区域详情
     *
     * @param eidList 企业ID集合
     * @return key表示企业ID，value表示根据企业ID分组后的销售区域集合
     */
    Map<Long, List<EnterpriseSalesAreaDetailDO>> getEnterpriseSaleAreaDetailByList(List<Long> eidList);

    /**
     * 根据批量企业ID和区域编码，获取可销售该区域的企业
     *
     * @param eidList 指定的企业ID集合
     * @param areaCode 区域编码
     * @return 可销售该区域编码的企业
     */
    List<Long> getEnterpriseSaleAreaDetailByListAndArea(List<Long> eidList, String areaCode);

    /**
     * 新增销售区域详情
     *
     * @param enterpriseSalesAreaDetailDoList
     * @return
     */
    Boolean insertEnterpriseSaleAreaDetail(List<EnterpriseSalesAreaDetailDO> enterpriseSalesAreaDetailDoList);

}
