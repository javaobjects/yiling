package com.yiling.basic.location.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yiling.basic.location.dto.RegionFullViewDTO;
import com.yiling.basic.location.entity.LocationDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 行政区划字典表 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-17
 */
@Repository
public interface LocationMapper extends BaseMapper<LocationDO> {

    List<RegionFullViewDTO> getAllProvinceCityRegionList();

}
