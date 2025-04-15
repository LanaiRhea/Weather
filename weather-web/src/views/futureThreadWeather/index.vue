<template>
  <div id="box1">
      <div class="forecast-container">
        <h1 class="forecast-title">未来七天天气预报</h1>
        <div class="week-container">
          <ul class="week-list">
            <li class="week-item" v-for="(e,index) in daily" :key="index">
              <h2 class="week-day">{{e.week}}</h2>
            </li>
          </ul>
        </div>
        <div class="weather-container">
          <ul class="weather-list">
            <li class="weather-item" v-for="(e,index) in daily" :key="index">
              <img class="weather-icon" :src="'assets/weathercn/'+e.day.img+'.png'" alt="天气图标">
              <p class="temp-range">{{e.night.templow}}~{{e.day.temphigh}}°C</p>
              <p class="weather-desc">{{e.day.weather}}</p>
              <p class="wind-info">{{e.day.winddirect}}</p>
              <p class="wind-power">{{e.day.windpower}}</p>
            </li>
          </ul>
        </div>
      </div>
  </div>
</template>

<script>
export default {
    props:['daily'],
    data() {
      return {
        daysTitle: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
      };
    }
}
</script>

<style scoped>
#box1 {
    margin: 30px auto;
    width: 799px;
    color: white;
    font-family: '微软雅黑';
    background-color: rgba(0, 0, 0, 0.1);
    padding: 30px;
    border-radius: 8px;
    transition: all 0.3s ease;
}

#box1:hover {
    background-color: rgba(0, 0, 0, 0.15);
    transform: translateY(-2px);
}

.forecast-container {
    padding: 10px 0;
}

.forecast-title {
    font-size: 20px;
    font-weight: normal;
    margin-bottom: 25px;
    color: rgba(255, 255, 255, 0.9);
}

.week-list, .weather-list {
    display: flex;
    justify-content: space-between;
    padding: 0;
    margin: 0;
    list-style: none;
}

.week-item, .weather-item {
    width: 14%;
    text-align: center;
    padding: 10px 5px;
    position: relative;
}

.week-item:not(:last-child)::after {
    content: '';
    position: absolute;
    right: 0;
    top: 10%;
    height: 80%;
    width: 1px;
    background: rgba(255, 255, 255, 0.1);
}

.week-day {
    font-size: 16px;
    font-weight: normal;
    margin: 0;
    color: rgba(255, 255, 255, 0.9);
}

.weather-icon {
    width: 30px;
    height: 30px;
    margin: 15px 0;
    transition: transform 0.3s ease;
}

.weather-item:hover .weather-icon {
    transform: scale(1.1);
}

.temp-range {
    font-size: 15px;
    margin: 10px 0;
    color: rgba(255, 255, 255, 0.9);
}

.weather-desc {
    font-size: 14px;
    margin: 8px 0;
    color: rgba(255, 255, 255, 0.8);
}

.wind-info, .wind-power {
    font-size: 13px;
    margin: 6px 0;
    color: rgba(255, 255, 255, 0.7);
}

.weather-container {
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
}
</style>