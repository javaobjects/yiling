package com.yiling.export.imports.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author fan.shen
 * @类名 ImportActivityDocPatientDoctorExcel
 * @描述 C端活动医生模型
 * @创建时间 2023-02-03
 * @修改人 fan.shen
 * @修改时间 2023-02-03
 **/
@Data
public class ImportActivityDocPatientDoctorExcel extends BaseImportModel {

    @ExcelRepet(groupName = "id_doctorId")
    @Excel(name = "*活动ID", orderNum = "0")
    @NotNull(message = "不能为空")
    @ExcelShow
    private Long id;

    @ExcelRepet(groupName = "id_doctorId")
    @Excel(name = "*医生ID", orderNum = "1")
    @NotNull(message = "不能为空")
    @ExcelShow
    private Integer doctorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ImportActivityDocPatientDoctorExcel that = (ImportActivityDocPatientDoctorExcel) o;
        return Objects.equals(id, that.id) && Objects.equals(doctorId, that.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, doctorId);
    }
}
