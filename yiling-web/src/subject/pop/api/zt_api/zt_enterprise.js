// 企业信息管理模块
import request from '@/subject/pop/utils/request'

// 获取客户企业信息
export function getEnterpriseInfo() {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/getEnterpriseInfo',
    method: 'get',
    params: {}
  })
}
// 修改企业信息
export function updateEnterpriseInfo(
  address, //详细地址
	certificateFormList, //企业资质
	cityCode, //所属城市编码
	cityName, //所属城市名称
	contactor, //	联系人
	contactorPhone, //联系人电话
	eid, //企业ID
	licenseNumber, //执业许可证号/社会信用统一代码
	name, //企业名称
	provinceCode, //所属省份编码
	provinceName, //所属省份名称
	regionCode, //所属区域编码
	regionName //所属区域名称
) {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/updateEnterpriseInfo',
    method: 'post',
    data: {
      address,
      certificateFormList,
      cityCode,
      cityName,
      contactor,
      contactorPhone,
      eid,
      licenseNumber,
      name,
      provinceCode,
      provinceName,
      regionCode,
      regionName 
    }
  })
}