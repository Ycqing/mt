import request from '@/utils/request'

export function getSocialAccounts(merchantId) {
  return request({
    url: `/social-account/merchant/${merchantId}`,
    method: 'get'
  })
}

export function addSocialAccount(merchantId, data) {
  return request({
    url: `/social-account/merchant/${merchantId}`,
    method: 'post',
    data
  })
}

export function updateSocialAccount(id, data) {
  return request({
    url: `/social-account/${id}`,
    method: 'put',
    data
  })
}

export function deleteSocialAccount(id) {
  return request({
    url: `/social-account/${id}`,
    method: 'delete'
  })
}

export function syncSocialAccount(merchantId, platform) {
  return request({
    url: `/social-account/sync/${merchantId}`,
    method: 'post',
    params: { platform }
  })
}
