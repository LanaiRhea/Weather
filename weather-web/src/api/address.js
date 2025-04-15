import request from '@/utils/request'

// 获取所有地区
export function getAllAddress() {
  return request({
    url: '/address/list',
    method: 'get'
  })
}

// 新增地区
export function addAddress(data) {
  return request({
    url: '/address/add',
    method: 'post',
    data
  })
}

// 编辑地区
export function editAddress(data) {
  return request({
    url: '/address/edit',
    method: 'post',
    data
  })
}

// 删除地区
export function delAddress(id) {
  return request({
    url: `/address/delete/${id}`,
    method: 'delete'
  })
} 