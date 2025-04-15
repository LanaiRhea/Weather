import request from "@/utils/request";

export function getWeather(cityName){
    return request({
        url: '/weather/get',
        method: 'GET',
        params: {cityName: cityName}
    })
}