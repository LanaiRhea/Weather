import request from '@/utils/request'

// 获取预警列表
export function getWarningList(params) {
  return request({
    url: '/warning/list',
    method: 'get',
    params
  })
}

// 添加预警
export function addWarning(data) {
  return request({
    url: '/warning/create',
    method: 'post',
    data
  })
}

// 更新预警
export function updateWarning(data) {
  return request({
    url: '/warning/update',
    method: 'put',
    data
  })
}

// 删除预警
export function deleteWarning(id) {
  return request({
    url: `/warning/delete/${id}`,
    method: 'delete'
  })
}

// 更新预警状态
export function updateWarningStatus(id, status) {
  return request({
    url: '/warning/update',
    method: 'put',
    data: {
      id,
      status
    }
  })
}