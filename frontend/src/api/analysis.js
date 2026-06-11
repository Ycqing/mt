import request from '@/utils/request'

export function compareMerchants(data) {
  return request({
    url: '/analysis/compare',
    method: 'post',
    data
  })
}

export function getCompareResult(id) {
  return request({
    url: `/analysis/compare/${id}`,
    method: 'get'
  })
}

export function getCompareHistory(params) {
  return request({
    url: '/analysis/compare/history',
    method: 'get',
    params
  })
}

export function deleteCompare(id) {
  return request({
    url: `/analysis/compare/${id}`,
    method: 'delete'
  })
}

export function getMerchantProfile(merchantId) {
  return request({
    url: `/analysis/profile/${merchantId}`,
    method: 'get'
  })
}
