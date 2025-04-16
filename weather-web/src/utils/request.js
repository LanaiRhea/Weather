import axios from 'axios'
import { Message } from 'element-ui'
// import store from '@/store'
// import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API || '/api', // 设置baseURL为/api
  timeout: 15000 // 请求超时时间设置为15秒
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data

    // 如果是文件下载，直接返回
    if (response.config.responseType === 'blob') {
      return res
    }

    // 兼容服务端返回的字符串数据
    if (typeof res === 'string') {
      try {
        return JSON.parse(res)
      } catch (e) {
        return res
      }
    }

    // 统一处理业务状态
    if (res.code === 200 || res.status === 200) {
      return res
    } else {
      Message({
        message: res.msg || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
  },
  error => {
    console.error('响应错误:', error)
    Message({
      message: error.message || '网络错误，请稍后重试',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
