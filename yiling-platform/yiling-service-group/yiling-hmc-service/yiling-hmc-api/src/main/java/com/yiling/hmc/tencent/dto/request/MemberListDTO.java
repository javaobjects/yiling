package com.yiling.hmc.tencent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MemberListDTO
 */
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class MemberListDTO implements java.io.Serializable{
    /**
     * memberAccount
     */
    private String Member_Account;
}
