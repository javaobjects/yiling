package com.yiling.user.esb.service.impl;

import com.yiling.user.esb.dao.EsbBackUpMapper;
import com.yiling.user.esb.service.EsbBackUpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class EsbBackUpServiceImpl implements EsbBackUpService {
    @Resource
    private EsbBackUpMapper esbBackUpMapper;

    @Override
    public Boolean createBackupTable(String tableName, String backupTableName) {
        return esbBackUpMapper.createBackupTable(tableName, backupTableName);
    }

    @Override
    public Boolean addColumn(String backupTableName) {
        return esbBackUpMapper.addColumn(backupTableName);
    }

    @Override
    public Boolean renameBackupTable(String tableName, String backupTableName) {
        return esbBackUpMapper.renameBackupTable(tableName, backupTableName);
    }

    @Override
    public Long getBackupTableCount(String backupTableName) {
        return esbBackUpMapper.getBackupTableCount(backupTableName);
    }

    @Override
    public Boolean insertBackupTableData(String tableName, String backupTableName) {
        return esbBackUpMapper.insertBackupTableData(tableName, backupTableName);
    }

    @Override
    public List<String> getTableNames(String esb) {
        return esbBackUpMapper.getTableNames(esb);
    }
}
