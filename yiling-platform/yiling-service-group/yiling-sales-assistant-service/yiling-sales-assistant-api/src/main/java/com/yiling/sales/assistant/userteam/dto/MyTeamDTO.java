package com.yiling.sales.assistant.userteam.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 我的队员时个人信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Data
@Accessors(chain = true)
public class MyTeamDTO implements Serializable {

    private static final long serialVersionUID=-7851140500516591200L;

    /**
     * 团队名称
     */
    private String teamName;

    /**
     * 队长姓名
     */
    private String parentName;

    /**
     * 队员数量
     */
    private Integer memberNum;

    /**
     * 队长是否已经注销：0-否 1-是
     */
    private Integer leaderDeRegisterFlag;


}
