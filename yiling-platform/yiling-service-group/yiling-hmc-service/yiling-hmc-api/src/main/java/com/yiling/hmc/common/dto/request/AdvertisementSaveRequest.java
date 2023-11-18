package com.yiling.hmc.common.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author: yong.zhang
 * @date: 2022/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdvertisementSaveRequest extends BaseRequest {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片地址
     */
    private String pic;

    /**
     * 跳转类型 1-h5跳转，2-小程序内部跳转
     */
    private Integer redirectType;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 有效起始时间
     */
    private Date startTime;

    /**
     * 有效截止时间
     */
    private Date stopTime;

    /**
     * 投放位置:1-C端用户侧首页 2-C端用户侧我的(多选，逗号隔开)
     */
    private String position;

    /**
     * 显示顺序
     */
    private Integer sort;
}
