import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'
import { Message } from 'element-ui'


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'index',
    component: () => import('@/components/home') 
  },
  {
    path: '/adminAfter',
    name: 'adminAfter',
    component: () => import('@/components/adminAfter'),
    meta: {
      requiresAuth: true,  // 需要登录
      requiresAdmin: true  // 需要管理员权限
    },
    children:[
      {
        path:'user',
        name: 'user',
        component: () => import('@/views/User'),
        meta: {
          requiresAuth: true,
          requiresAdmin: true
        }
      },
      {
        path:'address',
        name: 'address',
        component: () => import('@/views/Address'),
        meta: {
          requiresAuth: true,
          requiresAdmin: true
        }
      }
    ]
  },
  {
    path: '/rainfall-map',
    name: 'RainfallMap',
    component: () => import('@/views/RainfallMap'),
    meta: {
      requiresAuth: true
    }
  }
  
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  // 检查路由是否需要认证
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 检查用户是否已登录
    if (!store.state.userinfo.id) {
      Message.warning('请先登录')
      next('/')
      return
    }
    
    // 检查是否需要管理员权限
    if (to.matched.some(record => record.meta.requiresAdmin)) {
      // 检查用户是否是管理员
      if (store.state.role !== '1') {
        Message.error('您没有管理员权限')
        next('/')
        return
      }
    }
    next()
  } else {
    next()
  }
})

export default router
