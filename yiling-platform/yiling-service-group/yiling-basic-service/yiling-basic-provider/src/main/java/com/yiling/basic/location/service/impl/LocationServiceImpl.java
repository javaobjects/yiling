package com.yiling.basic.location.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.yiling.basic.location.service.LocationService;
import com.yiling.framework.common.mybatis.config.DataSourceConfiguration;

/**
 * <p>
 * 行政区划字典表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-17
 */
@Service("locationService")
@DS(DataSourceConfiguration.MASTER_DATA_SOURCE_NAME)
public class LocationServiceImpl extends LocationReaderServiceImpl implements LocationService {

}
