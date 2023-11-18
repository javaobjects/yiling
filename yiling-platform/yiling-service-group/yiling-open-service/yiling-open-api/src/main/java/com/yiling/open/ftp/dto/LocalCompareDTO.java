package com.yiling.open.ftp.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.open.erp.bo.BaseErpEntity;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/5/24
 */
@Data
public class LocalCompareDTO extends BaseDTO {
    private static final long serialVersionUID = -7631547950698054406L;
    private List<BaseErpEntity> dataList;
    private Long suId;
    private Date time;
}
