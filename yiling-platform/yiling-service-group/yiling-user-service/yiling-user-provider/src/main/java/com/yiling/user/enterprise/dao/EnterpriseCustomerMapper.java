package com.yiling.user.enterprise.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.enterprise.bo.ChannelCustomerBO;
import com.yiling.user.enterprise.bo.GroupCustomerNumBO;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.enterprise.dto.request.QueryChannelCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListByContactRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;
import com.yiling.user.enterprise.entity.EnterpriseDO;

/**
 * <p>
 * 企业客户信息 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-21
 */
@Repository
public interface EnterpriseCustomerMapper extends BaseMapper<EnterpriseCustomerDO> {

    /**
     * 企业客户分页列表
     * @param page
     * @param request
     * @return
     */
    Page<EnterpriseCustomerDO> pageList(Page page, @Param("request")QueryCustomerPageListRequest request);

    /**
     * 查询企业客户列表
     * @param request
     * @return
     */
    List<EnterpriseCustomerDO> pageList(@Param("request")QueryCustomerPageListRequest request);

    /**
     * 根据分组id获取企业客户
     * @param groupIds
     * @return
     */
    List<GroupCustomerNumBO> countGroupCustomers(@Param("groupIds")List<Long> groupIds);

    /**
     * 企业列表排序规则：
     * 1、未设置采购关系 > 未设置支付方式 > 未设置商务联系人
     * 2、按照创建时间倒序排序，即创建时越晚的排序靠上
     * @param page
     * @param request
     * @return
     */
    Page<ChannelCustomerBO> pageChannelCustomerList(Page page, @Param("request") QueryChannelCustomerPageListRequest request);

    /**
     * 企业列表排序规则：(不分页)
     * 1、未设置采购关系 > 未设置支付方式 > 未设置商务联系人
     * 2、按照创建时间倒序排序，即创建时越晚的排序靠上
     * @param request
     * @return
     */
    List<ChannelCustomerBO> pageChannelCustomerList(@Param("request") QueryChannelCustomerPageListRequest request);

    /**
     * 根据eid和customEId获取企业渠道商
     * @param request
     * @return
     */
    ChannelCustomerBO getChannelCustomer(@Param("request")QueryChannelCustomerPageListRequest request);

    /**
     * 企业客户分页列表
     * @param page
     * @param request
     * @return
     */
    Page<EnterpriseDO> queryCustomerPageListByContact(Page page, @Param("request") QueryCustomerPageListByContactRequest request);

    /**
     * 通过客户eid查询建采eid 通过最后采购时间排序
     * @param request
     * @return
     */
    List<Long> getEidListByCustomerEid(@Param("request")QueryCanBuyEidRequest request);
}
