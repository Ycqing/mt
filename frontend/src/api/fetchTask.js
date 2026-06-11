import request from '@/utils/request'

export function getFetchTaskList(params) {
  return request({
    url: '/fetch-task',
    method: 'get',
    params
  })
}

export function getFetchTaskDetail(id) {
  return request({
    url: `/fetch-task/${id}`,
    method: 'get'
  })
}

export function createFetchTask(data) {
  return request({
    url: '/fetch-task',
    method: 'post',
    data
  })
}

export function startFetchTask(id) {
  return request({
    url: `/fetch-task/${id}/start`,
    method: 'post'
  })
}

export function pauseFetchTask(id) {
  return request({
    url: `/fetch-task/${id}/pause`,
    method: 'post'
  })
}

export function deleteFetchTask(id) {
  return request({
    url: `/fetch-task/${id}`,
    method: 'delete'
  })
}

export function batchStartFetchTasks(ids) {
  return request({
    url: '/fetch-task/batch-start',
    method: 'post',
    data: ids
  })
}

export function executeFetchTask(id) {
  return request({
    url: `/fetch-task/execute/${id}`,
    method: 'post'
  })
}

export function fetchWebsite(data) {
  return request({
    url: '/fetch/website',
    method: 'post',
    data
  })
}

export function fetchFinancial(data) {
  return request({
    url: '/fetch/financial',
    method: 'post',
    data
  })
}

export function fetchReviews(data) {
  return request({
    url: '/fetch/reviews',
    method: 'post',
    data
  })
}
