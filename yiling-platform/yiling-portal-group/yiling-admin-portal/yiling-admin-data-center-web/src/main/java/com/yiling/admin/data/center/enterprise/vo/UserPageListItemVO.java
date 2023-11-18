package com.yiling.admin.data.center.enterprise.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户信息分页列表项 VO
 *
 * @author xuan.zhou
 * @date 2022/7/4
 */
@Data
public class UserPageListItemVO {

    @ApiModelProperty("用户信息")
    private UserVO staffInfo;

    @ApiModelProperty("所属企业")
    private List<String> enterpriseNames;
}
