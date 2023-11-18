package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 内容问答
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddQaRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;


    /**
     * 问答来源 1-内容管理
     */
    private Integer qaSource;

    /**
     * 问答类型 1-提问，2-解答
     */
    private Integer qaType;

    /**
     * cms_content主键
     */
    private Long contentId;

    /**
     * 引用业务线id
     */
    private Integer lineId;

    /**
     * 展示状态 1-展示，2-关闭
     */
    private Integer showStatus;

    /**
     * 问答内容
     */
    private String content;

    /**
     * cms_qa问答表主键
     */
    private Long qaId;
}
