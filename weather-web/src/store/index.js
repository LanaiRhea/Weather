import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

// 从 localStorage 获取保存的状态
const savedState = localStorage.getItem('weatherAppState')
const initialState = savedState ? JSON.parse(savedState) : {
  userinfo: {},
  like: [],
  cityName: '北京',
  role: ''
}

export default new Vuex.Store({
  state: initialState,
  getters: {
  },
  mutations: {
    setUserinfo(state, val){
      state.userinfo = val
      // 保存到 localStorage
      localStorage.setItem('weatherAppState', JSON.stringify(state))
    },
    setLike(state, val){
      state.like = val
      localStorage.setItem('weatherAppState', JSON.stringify(state))
    },
    setCityName(state, val){
      state.cityName = val
      localStorage.setItem('weatherAppState', JSON.stringify(state))
    },
    appendLike(state, val){
      state.like.push(val)
      localStorage.setItem('weatherAppState', JSON.stringify(state))
    },
    setRole(state, val){
      state.role = val
      localStorage.setItem('weatherAppState', JSON.stringify(state))
    }
  },
  actions: {
  },
  modules: {
  }
})
