package com.yiling.user.esb.api.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.yiling.user.esb.EsbBackUpApi;
import com.yiling.user.esb.dto.EsbBackUpRecordDTO;
import com.yiling.user.esb.dto.request.EsbBackUpRequest;
import com.yiling.user.esb.service.EsbBackUpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@DubboService
@Slf4j
public class EsbBackUpApiImpl implements EsbBackUpApi {
    public static final String FORMATE_YEAR_MONTH = "yyyyMM";
    public static final String FORMATE_YEAR = "yyyy";
    public static final String FORMATE_MONTH = "MM";
    private final static List<String> esb_backup_tables_prefix = Arrays.asList(
            "esb_employee",
            "esb_job",
            "esb_organization");
    private final static String backup_tables_format = "%s_wash_%s";
    @Resource
    private EsbBackUpService esbBackUpService;
    @Override
    public List<EsbBackUpRecordDTO> esbInfoBackup(EsbBackUpRequest request) {
        //备份月计算
        //备份月计算
        DateTime lastMonth = DateUtil.offsetMonth(new Date(), request.getOffsetMonth()==null?0:request.getOffsetMonth());
        // yyyy-MM
        String monthBackupStr = (Objects.isNull(request.getYearMonthFormat())||request.getYearMonthFormat().intValue()<=0) ?DateUtil.format(lastMonth, FORMATE_YEAR_MONTH):request.getYearMonthFormat()+"";
        //获取当前前缀的表
        List<String> esbTablesNames =esbBackUpService.getTableNames("esb");
        //判断是否存在
        //需要备份的表明=backup_tables_prefix_yyyy-MM
        List<EsbBackUpRecordDTO> result=new ArrayList<>();
        for (int i = 0; i < esb_backup_tables_prefix.size(); i++) {
            String tableName = esb_backup_tables_prefix.get(i);
            String backupTableName = String.format(backup_tables_format, tableName, monthBackupStr);
            //存在需要先删除表
            if (checkTablesNameExist(esbTablesNames, backupTableName)) {
                String newTablesName = String.format(backup_tables_format, tableName, new Date().getTime());
                esbBackUpService.renameBackupTable(newTablesName, backupTableName);
            }
            esbBackUpService.createBackupTable(tableName, backupTableName);
            esbBackUpService.insertBackupTableData(tableName,backupTableName);
            Long backupTableCount= esbBackUpService.getBackupTableCount(backupTableName);
            EsbBackUpRecordDTO recordDO=new EsbBackUpRecordDTO();
            recordDO.setInfo(esb_backup_tables_prefix.get(i));
            recordDO.setName(esb_backup_tables_prefix.get(i));
            recordDO.setYearMonths(Long.valueOf(monthBackupStr));
            recordDO.setStatus(1);
            recordDO.setCreateUser(0L);
            recordDO.setCreateTime(new Date());
            recordDO.setUpdateUser(0L);
            recordDO.setUpdateTime(new Date());
            recordDO.setCount(backupTableCount);
            result.add(recordDO);
            log.info("record:{}",recordDO);
        }
        return result;
    }
    private boolean checkTablesNameExist(List<String> crmTablesNames, String crmBackupTable) {
        return crmTablesNames.contains(crmBackupTable);
    }
}
