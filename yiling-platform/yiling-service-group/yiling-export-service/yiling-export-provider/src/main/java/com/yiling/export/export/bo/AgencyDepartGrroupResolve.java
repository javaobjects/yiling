package com.yiling.export.export.bo;

import lombok.Builder;
import lombok.Data;

import javax.sql.rowset.serial.SerialException;

@Data
@Builder
public class AgencyDepartGrroupResolve  {
    //
    private String sourceFiled;
    private String tagetFiled;
}
