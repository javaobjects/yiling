package com.yiling.hmc.tencent.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CreateGroupRequest implements java.io.Serializable {


    /**
     * name
     */
    private String Name;
    /**
     * type
     */
    private String Type;

    /**
     * 自定义数据
     */
    private List<AppDefinedDTO> AppDefinedData;

    /**
     * memberList
     */
    private List<MemberListDTO> MemberList;
}
