import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userinfo: {},
    like: [],
    cityName:'北京',
    role:''
    // isLike:[]
  },
  getters: {
  },
  mutations: {
    setUserinfo(state,val){
      state.userinfo = val
    },
    setLike(state,val){
      state.like = val
    },
    setCityName(state,val){
      state.cityName = val
    },
    appendLike(state,val){
      state.like.push(val)
    },
    setRole(state,val){
      state.role = val
    }
  },
  actions: {
  },
  modules: {
  }
})
