package com.yiling.user.member.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.member.dto.MemberReturnDTO;
import com.yiling.user.member.dto.request.QueryMemberReturnPageRequest;
import com.yiling.user.member.dto.request.UpdateMemberAuthReturnRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnStatusRequest;

/**
 * 会员退款 API
 *
 * @author: lun.yu
 * @date: 2022-04-18
 */
public interface MemberReturnApi {

    /**
     * 会员提交退款
     *
     * @param request
     */
    boolean submitReturn(UpdateMemberReturnRequest request);

    /**
     * 会员退款审核列表
     *
     * @param request
     * @return
     */
    Page<MemberReturnDTO> queryMemberReturnPage(QueryMemberReturnPageRequest request);

    /**
     * 同意/驳回 退款
     *
     * @param request
     */
    boolean updateAuthStatus(UpdateMemberAuthReturnRequest request);

    /**
     * 更新退款状态
     *
     * @param request
     */
    boolean updateReturnStatus(UpdateMemberReturnStatusRequest request);
}
