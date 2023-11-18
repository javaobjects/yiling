package com.yiling.mall.openposition.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.Eq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存B2B-开屏位 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOpenPositionRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 配置链接
     */
    private String link;

    /**
     * 图片
     */
    private String picture;


    /**
     * 发布状态：1-暂不发布 2-立即发布
     */
    private Integer status;


    /**
     * 平台：1-大运河 2-销售助手
     */
    private Integer platform;

}
