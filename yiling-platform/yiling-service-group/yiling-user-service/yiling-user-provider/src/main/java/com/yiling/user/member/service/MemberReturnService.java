package com.yiling.user.member.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.member.dto.MemberReturnDTO;
import com.yiling.user.member.dto.request.AddMemberReturnRequest;
import com.yiling.user.member.dto.request.QueryMemberReturnPageRequest;
import com.yiling.user.member.dto.request.UpdateMemberAuthReturnRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnStatusRequest;
import com.yiling.user.member.entity.MemberReturnDO;

/**
 * <p>
 * B2B-会员退款表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
public interface MemberReturnService extends BaseService<MemberReturnDO> {

    /**
     * 新增会员退款
     *
     * @param request
     * @return
     */
    boolean addMemberReturn(AddMemberReturnRequest request);

    /**
     * 查询会员退款分页列表
     *
     * @param request
     * @return
     */
    Page<MemberReturnDTO> queryMemberReturnPage(QueryMemberReturnPageRequest request);

    /**
     * 根据会员订单编号查询退款申请
     *
     * @param orderNo
     * @return
     */
    List<MemberReturnDTO> getReturnListByOrderNo(String orderNo);

    /**
     * 同意/驳回 退款
     *
     * @param request
     * @return
     */
    boolean updateAuthStatus(UpdateMemberAuthReturnRequest request);

    /**
     * 更新退款状态
     *
     * @param request
     * @return
     */
    boolean updateReturnStatus(UpdateMemberReturnStatusRequest request);
}
