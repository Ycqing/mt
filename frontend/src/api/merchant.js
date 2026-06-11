import request from '@/utils/request'

export function getMerchantList(params) {
  return request({
    url: '/merchant',
    method: 'get',
    params
  })
}

export function getMerchantDetail(id) {
  return request({
    url: `/merchant/${id}`,
    method: 'get'
  })
}

export function getAllMerchants() {
  return request({
    url: '/merchant/list',
    method: 'get'
  })
}

export function addMerchant(data) {
  return request({
    url: '/merchant',
    method: 'post',
    data
  })
}

export function updateMerchant(id, data) {
  return request({
    url: `/merchant/${id}`,
    method: 'put',
    data
  })
}

export function deleteMerchant(id) {
  return request({
    url: `/merchant/${id}`,
    method: 'delete'
  })
}

export function batchDeleteMerchants(ids) {
  return request({
    url: '/merchant/batch-delete',
    method: 'post',
    data: ids
  })
}

export function updateMerchantStatus(id, dataStatus) {
  return request({
    url: `/merchant/${id}/data-status`,
    method: 'put',
    params: { dataStatus }
  })
}
