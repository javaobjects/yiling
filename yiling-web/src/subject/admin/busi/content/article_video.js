import { getDict } from '@/subject/admin/utils';

// 关联会议
export function meetingActivityType() {
  return getDict('meeting_activity_type');
}
// 会议活动展示状态
export function meetingShowStatus() {
  return getDict('meeting_show_status');
}
// 健康管理中心 下 的板块
export function hmcModule() {
  return getDict('hmc_module');
}
//医生端 下的 板块
export function ihModule() {
  return getDict('ih_doc_module');
}
//IH患者端下的 板块 
export function ihPatientModule() {
  return getDict('ih_patient_module');
}
// 创建来源
export function cmsContentCreateSource() {
  return getDict('cms_content_create_source');
}
// 量表类型
export function hmcHealthEvaluate() {
  return getDict('hmc_health_evaluate');
}
//销售助手下 的板块
export function saModule() {
  return getDict('sa_module');
}
//大运河下 的板块
export function popModule() {
  return getDict('b2b_module');
}