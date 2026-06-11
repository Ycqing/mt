import request from '@/utils/request'

export function getReportList(params) {
  return request({
    url: '/report',
    method: 'get',
    params
  })
}

export function getReportDetail(id) {
  return request({
    url: `/report/${id}`,
    method: 'get'
  })
}

export function generateReport(data) {
  return request({
    url: '/report/generate',
    method: 'post',
    data
  })
}

export function deleteReport(id) {
  return request({
    url: `/report/${id}`,
    method: 'delete'
  })
}

export function downloadReport(id) {
  return request({
    url: `/report/${id}/download`,
    method: 'get'
  })
}
