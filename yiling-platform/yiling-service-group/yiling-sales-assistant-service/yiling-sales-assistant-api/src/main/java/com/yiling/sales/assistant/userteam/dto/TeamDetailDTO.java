package com.yiling.sales.assistant.userteam.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 团队信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Data
@Accessors(chain = true)
public class TeamDetailDTO implements Serializable {

    private static final long serialVersionUID=-7851140500516591200L;

    /**
     * 是否为队员
     */
    private Boolean teamMemberFlag;

    /**
     * 是否为队长
     */
    private Boolean teamLeaderFlag;

    /**
     * 我是队员时个人信息
     */
    private MyTeamDTO myTeamDTO;

    /**
     * 我是队长时个人信息
     */
    private MyLeaderDTO myLeaderDTO;

}
