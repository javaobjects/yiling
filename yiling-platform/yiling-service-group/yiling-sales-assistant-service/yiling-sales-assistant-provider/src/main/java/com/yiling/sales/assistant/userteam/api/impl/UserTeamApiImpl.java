package com.yiling.sales.assistant.userteam.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sales.assistant.userteam.api.UserTeamApi;
import com.yiling.sales.assistant.userteam.dto.TeamDTO;
import com.yiling.sales.assistant.userteam.dto.TeamDetailDTO;
import com.yiling.sales.assistant.userteam.dto.UserTeamDTO;
import com.yiling.sales.assistant.userteam.dto.request.AcceptInviteRequest;
import com.yiling.sales.assistant.userteam.dto.request.QueryUserTeamRequest;
import com.yiling.sales.assistant.userteam.service.UserTeamService;

/**
 * 销售助手-团队管理API impl
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/26
 */
@DubboService
public class UserTeamApiImpl implements UserTeamApi {

    @Autowired
    UserTeamService userTeamService;

    @Override
    public Page<UserTeamDTO> getMyTeamListPage(QueryUserTeamRequest request) {
        return userTeamService.getMyTeamListPage(request);
    }

    @Override
    public UserTeamDTO getUserTeamByUserId(Long userId) {
        return userTeamService.getUserTeamByUserId(userId);
    }

    @Override
    public boolean inviteMember(Long parentId ,String mobilePhone) {
        return userTeamService.inviteMember(parentId,mobilePhone);
    }

    @Override
    public boolean exitTeam(Long currentUserId) {
        return userTeamService.exitTeam(currentUserId);
    }

    @Override
    public TeamDetailDTO getTeamInfo(Long currentUserId) {
        return userTeamService.getTeamInfo(currentUserId);
    }

    @Override
    public List<UserTeamDTO> getMemberList(Long parentId) {
        return userTeamService.getMemberList(parentId);
    }

    @Override
    public List<TeamDTO> getTeamList(List<Long> parentIdList) {
        return userTeamService.getTeamList(parentIdList);
    }

    @Override
    public Boolean acceptInvite(AcceptInviteRequest request) {
        return  userTeamService.acceptInvite(request);
    }

    @Override
    public void inviteSendMq(Long parentId ,String mobilePhone) {
        userTeamService.inviteSendMq(parentId,mobilePhone);
    }

    @Override
    public boolean checkUserTeamStatus(String mobilePhone) {
        return userTeamService.checkUserTeamStatus(mobilePhone);
    }
}
