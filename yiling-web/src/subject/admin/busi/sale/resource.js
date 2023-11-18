import { getDict } from '@/subject/admin/utils';
// 资料审核状态
export function accompanyBillAuditStatus() {
  return getDict('accompany_bill_audit_status');
}
// 流向核对结果状态
export function accompanyBillMatchStatus() {
  return getDict('accompany_bill_match_status');
}