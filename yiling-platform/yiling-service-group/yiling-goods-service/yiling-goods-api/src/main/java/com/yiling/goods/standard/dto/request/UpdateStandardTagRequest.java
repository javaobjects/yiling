package com.yiling.goods.standard.dto.request;

import lombok.Data;

/**
 * @author shichen
 * @类名 UpdateStandardTagRequest
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@Data
public class UpdateStandardTagRequest extends CreateStandardTagRequest{
    /**
     * ID
     */
    private Long id;
}
