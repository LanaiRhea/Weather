import request from '@/utils/request'

export function getWeatherDataList(params) {
  return request({
    url: '/daily-weather/page',
    method: 'get',
    params
  })
} 