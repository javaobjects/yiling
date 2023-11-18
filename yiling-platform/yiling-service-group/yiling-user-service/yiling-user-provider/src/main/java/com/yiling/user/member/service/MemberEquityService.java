package com.yiling.user.member.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.member.dto.MemberEquityDTO;
import com.yiling.user.member.dto.request.CreateMemberEquityRequest;
import com.yiling.user.member.dto.request.QueryMemberEquityRequest;
import com.yiling.user.member.dto.request.UpdateMemberEquityRequest;
import com.yiling.user.member.entity.MemberEquityDO;

/**
 * <p>
 * 权益表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
public interface MemberEquityService extends BaseService<MemberEquityDO> {

    /**
     * 权分页益列表
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
     * 创建权益
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
