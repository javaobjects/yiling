package com.yiling.framework.common.base;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.Data;

/**
 * @author shichen
 * @类名 EsBaseEntity
 * @描述
 * @创建时间 2022/8/25
 * @修改人 shichen
 * @修改时间 2022/8/25
 **/
@Data
public class EsBaseEntity implements Serializable {

    @Id
    private Long id;
}
