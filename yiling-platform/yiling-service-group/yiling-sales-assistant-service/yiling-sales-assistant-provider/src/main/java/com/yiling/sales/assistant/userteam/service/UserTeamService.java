package com.yiling.sales.assistant.userteam.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sales.assistant.userteam.dto.TeamDTO;
import com.yiling.sales.assistant.userteam.dto.TeamDetailDTO;
import com.yiling.sales.assistant.userteam.dto.UserTeamDTO;
import com.yiling.sales.assistant.userteam.dto.request.AcceptInviteRequest;
import com.yiling.sales.assistant.userteam.dto.request.QueryUserTeamRequest;

/**
 * 团队管理Service
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/26
 */
public interface UserTeamService {

    /**
     * 我的成员列表
     * @param request
     * @return
     */
    Page<UserTeamDTO> getMyTeamListPage(QueryUserTeamRequest request);

    /**
     * 邀请成员
     * @param parentId
     * @param mobilePhone
     * @return
     */
    boolean inviteMember(Long parentId ,String mobilePhone);

    /**
     * 退出团队
     * @param currentUserId
     * @return
     */
    boolean exitTeam(Long currentUserId);

    /**
     * 查询个人团队信息
     * @param currentUserId
     * @return
     */
    TeamDetailDTO getTeamInfo(Long currentUserId);

    /**
     * 查询用户成员列表
     * @param parentId
     * @return
     */
    List<UserTeamDTO> getMemberList(Long parentId);

    /**
     * 查询团队
     * @param parentIdList
     * @return
     */
    List<TeamDTO> getTeamList(List<Long> parentIdList);

    /**
     * 根据用户ID获取用户团队信息
     * @param userId
     * @return
     */
    UserTeamDTO getUserTeamByUserId(Long userId);

    /**
     * 接受微信/短信邀请
     * @param request
     * @return
     */
    Boolean acceptInvite(AcceptInviteRequest request);

    /**
     * 注册成功发送MQ通知
     * @param parentId
     * @param mobilePhone
     */
    void inviteSendMq(Long parentId ,String mobilePhone);

    /**
     * 检查用户当前是否有团队
     *
     * @param mobilePhone
     * @return
     */
    boolean checkUserTeamStatus(String mobilePhone);
}
