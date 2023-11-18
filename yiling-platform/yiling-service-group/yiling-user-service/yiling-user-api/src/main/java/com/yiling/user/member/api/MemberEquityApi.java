package com.yiling.user.member.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.member.dto.MemberEquityDTO;
import com.yiling.user.member.dto.request.CreateMemberEquityRequest;
import com.yiling.user.member.dto.request.QueryMemberEquityRequest;
import com.yiling.user.member.dto.request.UpdateMemberEquityRequest;

/**
 * 会员权益 API
 *
 * @author: lun.yu
 * @date: 2021/10/26
 */
public interface MemberEquityApi {

    /**
     * 权益分页列表
     * @param request
     * @return
     */
    Page<MemberEquityDTO> queryListPage(QueryMemberEquityRequest request);

    /**
     * 修改状态
     * @param id
     * @param opUserId
     * @return
     */
    Boolean updateStatus(Long id, Long opUserId);

    /**
     * 新增权益
     * @param request
     * @return
     */
    Boolean createEquity(CreateMemberEquityRequest request);

    /**
     * 修改权益
     * @param request
     * @return
     */
    Boolean updateEquity(UpdateMemberEquityRequest request);

    /**
     * 删除权益
     * @param id
     * @param opUserId
     * @return
     */
    Boolean deleteEquity(Long id, Long opUserId);

    /**
     * 权益列表
     * @param request
     * @return
     */
    List<MemberEquityDTO> queryList(QueryMemberEquityRequest request);

    /**
     * 获取会员权益详情
     * @param id
     * @return
     */
    MemberEquityDTO getEquity(Long id);
}
