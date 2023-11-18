package com.yiling.hmc.tencent.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取群组信息
 */
@NoArgsConstructor
@Data
public class GetGroupInfoRequest implements java.io.Serializable{

    /**
     * groupIdList
     */
    private List<String> GroupIdList;
}
