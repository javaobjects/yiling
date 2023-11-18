package com.yiling.sales.assistant.userteam.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.enums.AssistantErrorCode;
import com.yiling.sales.assistant.enums.UserTeamInviteTypeEnum;
import com.yiling.sales.assistant.enums.UserTeamStatusEnum;
import com.yiling.sales.assistant.task.dto.request.UpdateUserTaskNoLimitRequest;
import com.yiling.sales.assistant.userteam.dao.TeamMapper;
import com.yiling.sales.assistant.userteam.dao.UserTeamMapper;
import com.yiling.sales.assistant.userteam.dto.MyLeaderDTO;
import com.yiling.sales.assistant.userteam.dto.MyTeamDTO;
import com.yiling.sales.assistant.userteam.dto.TeamDTO;
import com.yiling.sales.assistant.userteam.dto.TeamDetailDTO;
import com.yiling.sales.assistant.userteam.dto.UserTeamDTO;
import com.yiling.sales.assistant.userteam.dto.request.AcceptInviteRequest;
import com.yiling.sales.assistant.userteam.dto.request.QueryUserTeamRequest;
import com.yiling.sales.assistant.userteam.entity.TeamDO;
import com.yiling.sales.assistant.userteam.entity.UserTeamDO;
import com.yiling.sales.assistant.userteam.service.UserTeamService;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 团队管理Service实现
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/26
 */
@Slf4j
@Service
public class UserTeamServiceImpl extends BaseServiceImpl<UserTeamMapper, UserTeamDO> implements UserTeamService {

    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;

    @Autowired
    TeamMapper teamMapper;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Lazy
    @Autowired
    UserTeamServiceImpl _this;

    @Override
    public Page<UserTeamDTO> getMyTeamListPage(QueryUserTeamRequest request) {
        QueryWrapper<UserTeamDO> queryWrapper = getUserTeamQueryWrapper(request);

        Page<UserTeamDO> page = this.page(request.getPage(), queryWrapper);
        return PojoUtils.map(page,UserTeamDTO.class);
    }

    private QueryWrapper<UserTeamDO> getUserTeamQueryWrapper(QueryUserTeamRequest request) {
        QueryWrapper<UserTeamDO> queryWrapper = WrapperUtils.getWrapper(request);

        // 姓名
        if(StrUtil.isNotEmpty(request.getName())){
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameEq(request.getName());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            List<Long> userIdList = staffList.stream().map(Staff::getId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(userIdList)) {
                queryWrapper.lambda().in(UserTeamDO::getUserId, userIdList);
            } else {
                queryWrapper.lambda().eq(UserTeamDO::getUserId, 0);
            }
        }

        // 姓名或手机号
        if(StrUtil.isNotEmpty(request.getNameOrPhone())){
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameEq(request.getNameOrPhone());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            List<Long> userIdList = staffList.stream().map(Staff::getId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(userIdList)) {
                queryWrapper.lambda().in(UserTeamDO::getUserId, userIdList).or().eq(UserTeamDO::getMobilePhone, request.getNameOrPhone());
            } else {
                queryWrapper.lambda().eq(UserTeamDO::getMobilePhone, request.getNameOrPhone());
            }
        }

        if(Objects.nonNull(request.getDateOrder())){
            if(request.getDateOrder() == 0){
                queryWrapper.lambda().orderByAsc(UserTeamDO::getRegisterTime);
            }else{
                queryWrapper.lambda().orderByDesc(UserTeamDO::getRegisterTime);
            }
        }
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean inviteMember(Long parentId ,String mobilePhone) {
        //将要邀请的用户
        Staff staff = staffApi.getByMobile(mobilePhone);
        if (staff != null) {
            if (EnableStatusEnum.getByCode(staff.getStatus()) == EnableStatusEnum.DISABLED) {
                throw new BusinessException(AssistantErrorCode.MEMBER_PHONE_STOP);
            }

            if (staff.getId().compareTo(parentId) == 0) {
                throw new BusinessException(AssistantErrorCode.NOT_COULD_INVITE_OWNER);
            }

            LambdaQueryWrapper<UserTeamDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserTeamDO::getParentId, parentId);
            queryWrapper.eq(UserTeamDO::getUserId, staff.getId());
            List<UserTeamDO> userTeamDOList = this.list(queryWrapper);
            if (CollUtil.isNotEmpty(userTeamDOList)) {
                for (UserTeamDO userTeamDO : userTeamDOList) {
                    if (UserTeamStatusEnum.getByCode(userTeamDO.getRegisterStatus()) == UserTeamStatusEnum.PASS) {
                        throw new BusinessException(AssistantErrorCode.REPEAT_INVITE);
                    } else if (UserTeamStatusEnum.getByCode(userTeamDO.getRegisterStatus()) == UserTeamStatusEnum.WAITING) {
                        return true;
                    }
                }
            }
        }

        //判断是否为首次邀请
        TeamDO teamDO = this.getTeamByParentId(parentId);
        if(Objects.isNull(teamDO)){
            //队长的信息
            UserDTO parentUserDto = Optional.ofNullable(userApi.getById(parentId)).orElseThrow(() -> new BusinessException(UserErrorCode.LEADER_NOT_EXIST));

            //首次邀请：创建团队
            teamDO = new TeamDO().setTeamName(parentUserDto.getName()+"的团队").setMemberNum(0).setParentId(parentId);
            teamMapper.insert(teamDO);
        }

        //建立邀请关系
        UserTeamDO userTeamDO = new UserTeamDO();
        userTeamDO.setParentId(parentId);
        if (staff != null) {
            userTeamDO.setUserId(staff.getId());
            userTeamDO.setName(staff.getName());
        }
        userTeamDO.setMobilePhone(mobilePhone);
        userTeamDO.setRegisterStatus(UserTeamStatusEnum.WAITING.getCode());
        userTeamDO.setInviteType(UserTeamInviteTypeEnum.SMS.getCode());
        userTeamDO.setOpUserId(parentId);
        this.save(userTeamDO);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean exitTeam(Long currentUserId) {
        LambdaQueryWrapper<UserTeamDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTeamDO::getUserId,currentUserId);
        queryWrapper.eq(UserTeamDO::getRegisterStatus,UserTeamStatusEnum.PASS.getCode());
        UserTeamDO userTeamDO = Optional.ofNullable(this.getOne(queryWrapper)).orElseThrow(() -> new BusinessException(AssistantErrorCode.ANY_TEAM_MEMBER));
        userTeamDO.setOpUserId(currentUserId);

        //删除团队队员关系
        this.deleteByIdWithFill(userTeamDO);
        //团队内减去该用户
        LambdaQueryWrapper<TeamDO> teamDoQueryWrapper = new LambdaQueryWrapper<>();
        teamDoQueryWrapper.eq(TeamDO::getParentId,userTeamDO.getParentId());
        TeamDO teamDO = Optional.ofNullable(teamMapper.selectOne(teamDoQueryWrapper)).orElseThrow(() -> new BusinessException(UserErrorCode.USER_TEAM_NO_EXIST));

        teamMapper.updateReduceMemberNum(teamDO.getId(),currentUserId);

        return true;
    }

    @Override
    public TeamDetailDTO getTeamInfo(Long currentUserId) {
        //我是队员信息
        UserTeamDTO userTeamDTO = getUserTeamByUserId(currentUserId);

        TeamDetailDTO teamDTO = new TeamDetailDTO();

        if(Objects.nonNull(userTeamDTO)){
            teamDTO.setTeamMemberFlag(true);
            TeamDO teamDO = getTeamByParentId(userTeamDTO.getParentId());

            if(Objects.nonNull(teamDO)){
                UserDTO userDTO = userApi.getById(userTeamDTO.getParentId());
                MyTeamDTO myTeamDTO = new MyTeamDTO();
                myTeamDTO.setTeamName(teamDO.getTeamName());
                myTeamDTO.setMemberNum(teamDO.getMemberNum());
                myTeamDTO.setParentName(userDTO.getName());
                myTeamDTO.setLeaderDeRegisterFlag(UserStatusEnum.getByCode(userDTO.getStatus()) == UserStatusEnum.DEREGISTER ? 1 : 0);
                teamDTO.setMyTeamDTO(myTeamDTO);
            }
        }

        //我是队长时的团队信息
        TeamDO leadDo = getTeamByParentId(currentUserId);
        if(Objects.nonNull(leadDo)){
            teamDTO.setTeamLeaderFlag(true);
            MyLeaderDTO myLeaderDTO = new MyLeaderDTO();
            myLeaderDTO.setTeamName(leadDo.getTeamName());
            myLeaderDTO.setMemberNum(leadDo.getMemberNum());
            myLeaderDTO.setCreateTime(leadDo.getCreateTime());
            teamDTO.setMyLeaderDTO(myLeaderDTO);
        }

        return teamDTO;
    }

    @Override
    public List<UserTeamDTO> getMemberList(Long parentId) {
        LambdaQueryWrapper<UserTeamDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTeamDO::getParentId,parentId);
        queryWrapper.eq(UserTeamDO::getRegisterStatus,UserTeamStatusEnum.PASS.getCode());
        return PojoUtils.map(this.list(queryWrapper),UserTeamDTO.class);
    }

    @Override
    public List<TeamDTO> getTeamList(List<Long> parentIdList) {
        LambdaQueryWrapper<TeamDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TeamDO::getParentId,parentIdList);

        return PojoUtils.map(teamMapper.selectList(queryWrapper), TeamDTO.class);
    }

    /**
     * 根据队长ID获取团队信息
     * @param parentId
     * @return
     */
    private TeamDO getTeamByParentId(Long parentId){
        LambdaQueryWrapper<TeamDO> teamDoQueryWrapper = new LambdaQueryWrapper<>();
        teamDoQueryWrapper.eq(TeamDO::getParentId,parentId);
        return teamMapper.selectOne(teamDoQueryWrapper);
    }

    /**
     * 根据用户ID获取我是队员时的信息
     * @param userId
     */
    @Override
    public UserTeamDTO getUserTeamByUserId(Long userId){
        LambdaQueryWrapper<UserTeamDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTeamDO::getUserId,userId);
        queryWrapper.eq(UserTeamDO::getRegisterStatus,UserTeamStatusEnum.PASS.getCode());
        return PojoUtils.map(this.getOne(queryWrapper),UserTeamDTO.class);
    }

    /**
     * 接受微信/短信邀请
     * @param request 邀请人
     * @return 邀请处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean acceptInvite(AcceptInviteRequest request) {
        if (Objects.isNull(request.getParentId()) || Objects.isNull(request.getInviteType()) || StrUtil.isEmpty(request.getMobilePhone())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        // 邀请的成员信息
        Staff staff = Optional.ofNullable(staffApi.getByMobile(request.getMobilePhone())).orElseThrow(() -> new BusinessException(UserErrorCode.INVITE_MEMBER_NOT_EXIST));

        // 校验当前被邀请用户，是否可以被邀请
        LambdaQueryWrapper<UserTeamDO> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(UserTeamDO::getMobilePhone,request.getMobilePhone());
        checkWrapper.eq(UserTeamDO::getRegisterStatus,1);
        int count = this.count(checkWrapper);
        // 没有注销且没有被邀请为已注册状态的用户，才能接受邀请
        if(count > 0 && UserStatusEnum.getByCode(staff.getStatus()) != UserStatusEnum.DEREGISTER){
            throw new BusinessException(AssistantErrorCode.REPEAT_INVITE);
        }

        if (UserTeamInviteTypeEnum.getByCode(request.getInviteType()) == UserTeamInviteTypeEnum.SMS) {
            // 短信邀请
            LambdaQueryWrapper<UserTeamDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserTeamDO::getParentId, request.getParentId());
            queryWrapper.eq(UserTeamDO::getMobilePhone,request.getMobilePhone());
            queryWrapper.eq(UserTeamDO::getRegisterStatus, 0);
            UserTeamDO userTeamDO = Optional.ofNullable(this.getOne(queryWrapper)).orElseThrow(() -> new BusinessException(UserErrorCode.INVITE_MEMBER_NOT_EXIST));

            //更新状态为已注册，即加入团队成功
            UserTeamDO temp = new UserTeamDO();
            temp.setId(userTeamDO.getId());
            temp.setUserId(staff.getId());
            temp.setName(staff.getName());
            temp.setRegisterStatus(UserTeamStatusEnum.PASS.getCode());
            temp.setRegisterTime(new Date());
            temp.setOpUserId(staff.getId());
            this.updateById(temp);

            // 更新已经存在的其它未注册邀请，此数据可直接删除
            this.updateExistInvite(request);

            //团队人数增加
            LambdaQueryWrapper<TeamDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TeamDO::getParentId,request.getParentId());
            TeamDO teamDO = Optional.ofNullable(teamMapper.selectOne(wrapper)).orElseThrow(() -> new BusinessException(UserErrorCode.USER_TEAM_NO_EXIST));

            teamMapper.updateAddMemberNum(teamDO.getId(),staff.getId());
        } else {
            // 如果为微信邀请：先看是否为首次：首次则此时创建团队、建立邀请关系，否则更新团队
            LambdaQueryWrapper<TeamDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TeamDO::getParentId,request.getParentId());
            TeamDO team =  teamMapper.selectOne(wrapper);
            if (Objects.isNull(team)) {
                // 创建团队
                UserDTO parentUserDto = Optional.ofNullable(userApi.getById(request.getParentId())).orElseThrow(() -> new BusinessException(UserErrorCode.LEADER_NOT_EXIST));
                TeamDO teamDO = new TeamDO().setTeamName(parentUserDto.getName()+"的团队").setMemberNum(1).setParentId(request.getParentId());
                teamMapper.insert(teamDO);
            } else {
                teamMapper.updateAddMemberNum(team.getId(),staff.getId());
            }

            // 建立邀请关系
            UserTeamDO userTeamDO = new UserTeamDO();
            userTeamDO.setParentId(request.getParentId());
            userTeamDO.setUserId(staff.getId());
            userTeamDO.setMobilePhone(request.getMobilePhone());
            userTeamDO.setName(staff.getName());
            userTeamDO.setRegisterStatus(UserTeamStatusEnum.PASS.getCode());
            userTeamDO.setRegisterTime(new Date());
            userTeamDO.setInviteType(UserTeamInviteTypeEnum.WECHAT.getCode());
            userTeamDO.setOpUserId(request.getOpUserId());
            this.save(userTeamDO);

            // 更新已经存在的其它未注册邀请，此数据可直接删除
            this.updateExistInvite(request);
        }

        return true;
    }

    /**
     * 更新已经存在的其它未注册邀请，此数据可直接删除
     *
     * @param request
     */
    private void updateExistInvite(AcceptInviteRequest request) {
        LambdaQueryWrapper<UserTeamDO> wrapperExist = new LambdaQueryWrapper<>();
        wrapperExist.eq(UserTeamDO::getMobilePhone, request.getMobilePhone());
        wrapperExist.eq(UserTeamDO::getRegisterStatus, UserTeamStatusEnum.WAITING.getCode());
        wrapperExist.notIn(UserTeamDO::getParentId, request.getParentId());

        UserTeamDO userTeamExist = new UserTeamDO();
        userTeamExist.setOpUserId(request.getOpUserId());
        this.batchDeleteWithFill(userTeamExist, wrapperExist);
    }

    @Override
    public void inviteSendMq(Long parentId ,String mobilePhone) {
        // 队长邀请的新人注册成功后发送MQ消息
        Staff staff = Optional.ofNullable(staffApi.getByMobile(mobilePhone)).orElse(new Staff());

        LambdaQueryWrapper<UserTeamDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTeamDO::getParentId, parentId);
        queryWrapper.eq(UserTeamDO::getMobilePhone, mobilePhone);
        queryWrapper.orderByDesc(UserTeamDO::getCreateTime);
        queryWrapper.last("limit 1");
        UserTeamDO userTeamDO = this.getOne(queryWrapper);

        UpdateUserTaskNoLimitRequest limitRequest = new UpdateUserTaskNoLimitRequest();
        limitRequest.setUserId(staff.getId());
        limitRequest.setInviterUserId(parentId);
        limitRequest.setMobile(mobilePhone);
        limitRequest.setName(staff.getName());
        limitRequest.setRegTime(new Date());
        // 短信邀请取创建时间，微信邀请取当前时间(确认邀请的时间)
        limitRequest.setCreateTime(Objects.nonNull(userTeamDO) ? userTeamDO.getCreateTime() : new Date());
        log.info("销售助手邀请新人注册成功后发送MQ消息，消息内容：{}", JSONObject.toJSONString(limitRequest));

        MqMessageBO mqMessageBO = _this.sendPrepare(limitRequest);
        mqMessageSendApi.send(mqMessageBO);

    }

    @GlobalTransactional
    public MqMessageBO sendPrepare(UpdateUserTaskNoLimitRequest limitRequest) {
        // 销售助手队长邀请的新人注册成功后发送通知
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_SALES_INVITE_REGISTER_SUCCESS_NOTIFY, "", JSONObject.toJSONString(limitRequest));
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }

    /**
     * 检查用户当前是否有团队
     *
     * @param mobilePhone
     * @return
     */
    public boolean checkUserTeamStatus(String mobilePhone) {
        // 校验当前被邀请用户，是否可以被邀请
        LambdaQueryWrapper<UserTeamDO> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(UserTeamDO::getMobilePhone, mobilePhone);
        checkWrapper.eq(UserTeamDO::getRegisterStatus, 1);
        int count = this.count(checkWrapper);
        if(count > 0){
            Staff staff = staffApi.getByMobile(mobilePhone);
            // 如果为空说明此账号已经注销或不存在
            if (Objects.isNull(staff)) {
                return false;
            }
            return true;
        }

        return false;
    }


}
