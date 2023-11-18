package com.yiling.sjms.manor.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 医院辖区变更表单
 * </p>
 *
 * @author gxl
 * @date 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HospitalManorChangeRequest extends BaseRequest {


    private static final long serialVersionUID = 1326608629404472891L;


    private Long id;

    /**
     * 品种id
     */
    private Long categoryId;

    /**
     * 旧辖区id
     */
    private Long manorId;

    /**
     * 新辖区id
     */
    private Long newManorId;




}
