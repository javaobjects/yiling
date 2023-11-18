package com.yiling.admin.data.center.enterprise.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账号详情页 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/11
 */
@Data
public class UserDetaiPageVO {

    @ApiModelProperty("账号信息")
    private UserVO staffVO;

    @ApiModelProperty("身份证附件信息")
    private IdCardPhotoVO idCardPhotoVO;

    @ApiModelProperty("企业信息列表")
    private List<SimpleEnterpriseVO> enterpriseList;

    @Data
    public static class IdCardPhotoVO {

        @ApiModelProperty("身份证正面照")
        private String frontPhoto;

        @ApiModelProperty("身份证反面照")
        private String backPhoto;
    }

    @Data
    public static class SimpleEnterpriseVO {

        @ApiModelProperty("企业ID")
        private Long id;

        @ApiModelProperty("企业名称")
        private String name;

        @ApiModelProperty("角色名称")
        private String roleName;

        @ApiModelProperty("状态：1-启用 2-停用")
        private Integer status;
    }
}
