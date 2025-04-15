import axios from 'axios'
import {  Message } from 'element-ui'
// import store from '@/store'
// import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: '/api', // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})
//request.interceptors.response.use(
service.interceptors.response.use(
  response => {
    
      //统一异常处理
      let status = response.status;
      if(status !== 200) {
        Message({
          message: response.msg || 'Error',
          type: 'error',
          duration: 5 * 1000
        })
      }
      let res = response.data;
      // 如果是返回的文件
      if (response.config.responseType === 'blob') {
          return res
      }
      // 兼容服务端返回的字符串数据
      if (typeof res === 'string') {
          res = res ? JSON.parse(res) : res
      }
      return res;
  },
  
  error => {
      // console.log('err' + error) // for debug
      // return Promise.reject(error)
      Message({
        message: '网路阻塞,请稍后重试！'+error || '网路阻塞,请稍后重试！',
        type: 'warning',
        duration: 5 * 1000
      })
  }
)


export default service
