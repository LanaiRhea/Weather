<template>
  <div id="box1">
      <div>
        <h1>未来七天天气预报</h1>
        <div style="margin-top:30px">
          <ul style="display:flex;text-align:center;">
            <li id="titBox" v-for="(e,index) in daily" :key="index">
              <h2>{{e.week}}</h2>

            </li>
          </ul>
        </div>
        <div style="margin-top:15px">
          <ul id="ulbox" style="display:flex;text-align:center;">
            <li id="box2" v-for="(e,index) in daily" :key="index">
              <img style="margin-top:15px" width="30" :src="'assets/weathercn/'+e.day.img+'.png'" alt="破损">
              <p>{{e.night.templow}}~{{e.day.temphigh}}°C</p>
              <p>{{e.day.weather}}</p>
              <p>{{e.day.winddirect}}</p>
              <p>{{e.day.windpower}}</p>
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
    },
}
</script>

<style scoped>
#box2{
  width: 14%;
  height: 140px;
  /* border: 1px solid black; */
  border-left: 1px solid  rgba(255, 255, 255, 0.1);
  /* border-top: 1px solid rgba(255, 255, 255, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1); */
  font-size: 15px;
  font-family: '微软雅黑';
}
#ulbox :last-child{
  border-right: 1px solid  rgba(255, 255, 255, 0.1);
}
/* #box2 :hover{
  box-shadow: 0px 0px 10px white;
  transition:2s;
} */
#box2 p{
  margin-top: 8px;
 
}
#box1 {
    margin-top:30px;
    
    height: 200px;
    width: 799px;
    margin-bottom: 30px;
    
    margin-left: auto;
    margin-right: auto;
    color: white;
    font-family: '微软雅黑';
    padding: 30px;
    background-color: rgba(0, 0,0,0.1);
    /* padding: 30px; */
    /* background-color: rgb(163, 39, 142); */
}
#titBox{
  width: 14%;
}
</style>