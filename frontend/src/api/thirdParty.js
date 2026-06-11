import request from '@/utils/request'

export function getThirdPartyQuota(platform) {
  return request({
    url: `/third-party/quota/${platform}`,
    method: 'get'
  })
}

export function getProductRankings(platform, params) {
  return request({
    url: `/third-party/products/${platform}`,
    method: 'get',
    params
  })
}

export function getBrandRankings(platform, params) {
  return request({
    url: `/third-party/brands/${platform}`,
    method: 'get',
    params
  })
}

export function getSalesData(platform, params) {
  return request({
    url: `/third-party/sales/${platform}`,
    method: 'get',
    params
  })
}

export function searchReports(keyword) {
  return request({
    url: '/third-party/reports',
    method: 'get',
    params: { keyword }
  })
}

export function syncOperatingData(merchantId, platform) {
  return request({
    url: `/third-party/sync/${merchantId}`,
    method: 'post',
    params: { platform }
  })
}
