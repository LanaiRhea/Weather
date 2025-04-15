import request from '@/utils/request'

export function login(data){
    return request({
        url: '/user/login',
        method: 'POST',
        data
    })
}

export function editPwd(data){
    return request({
        url: '/user/editPwd',
        method: 'POST',
        data
    })
}

export function editInfo(data){
    return request({
        url: '/user/editInfo',
        method: 'POST',
        data
    })
}

export function addLike(name,id){//添加喜欢
    return request({
        url: '/user/addLike/'+name+'/'+id,
        method: 'get'
    })
}

export function delLike(name,id){//移除喜欢
    return request({
        url: '/user/delLike/'+name+'/'+id,
        method: 'get'
    })
}

export function addUser(data){//添加用户
    return request({
        url: '/user/add',
        method: 'POST',
        data
    })
}

export function delUser(id){//删除用户
    return request({
        url: '/user/del/'+id,
        method: 'DELETE'
    })
}

export function getAllUser(){//查询所有用户
    return request({
        url: '/user/getAll',
        method: 'GET'
    })
}

export function getAllAddress(){
    return request({
        url: '/weather/getAddress',
        method: 'GET'
    })
}
export function delAddress(id){
    return request({
        url: '/weather/delAddress/'+id,
        method: 'DELETE'
    })
}
export function putAddress(data){
    return request({
        url: '/weather/putAddress',
        method: 'put',
        data
    })
}
export function addAddress(data){
    return request({
        url: '/weather/addAddress',
        method: 'POST',
        data: {address: data}
    })
}