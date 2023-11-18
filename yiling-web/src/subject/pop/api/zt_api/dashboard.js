import request from '@/subject/pop/utils/request'

// 商家后台-中台-首页-获取当前登陆人企业信息
export function queryCurrentEnterpriseInfo() {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/getCurrentEnterpriseInfo',
    method: 'get',
    parmas: {}
  })
}
