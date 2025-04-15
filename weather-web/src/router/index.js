import Vue from 'vue'
import VueRouter from 'vue-router'


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
    children:[
      {
        path:'user',
        name: 'user',
        component: () => import('@/views/User')
      },
      {
        path:'address',
        name: 'address',
        component: () => import('@/views/Address')
      }


    ]
  }
  
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
