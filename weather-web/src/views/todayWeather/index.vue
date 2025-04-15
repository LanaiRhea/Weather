<template>
  <div v-if="show" id="box1">
      <div id="box2"  ref="box22"> 

      </div>
  </div>
</template>

<script>
import pubsub from 'pubsub-js'
import * as echarts from 'echarts';
export default {
    props:['hourly'],
    data() {
        return {
            time:[],
            temp:[],
            img:[],
            myChart:'',
            show:true,
        }
    },
    methods:{
        flush(){
            // alert('属性')
            for(var i=0;i<this.hourly.length;i++){
            this.time[i] = this.hourly[i].time
            this.temp[i] = this.hourly[i].temp
            this.img[i] = this.hourly[i].img
            }
        },
        showImg(){
            var chartDom = this.$refs.box22;
            this.myChart = echarts.init(chartDom);
            var option;

        option = {
        title:{
            text:'24小时气温变化',
            textStyle:{
                color: 'white'
            }
        },
        //移入显示详细
        tooltip:{
            trigger: 'axis',
            formatter: '时间：{b} <br/> 气温：{c}°C '
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
             axisLine: {
                     lineStyle: {
                         type:  'solid' ,
                         color:  '#fff' , //左边线的颜色
                         width: '2' //坐标线的宽度
                     }
                 },
                 axisLabel: {
                     textStyle: {
                         color:  '#fff' , //坐标值得具体的颜色
 
                     }
                 },
            data: this.time
        },
        yAxis: {
            type: 'value',
            splitNumber: 3,
             axisLine: {
                     lineStyle: {
                         type:  'solid' ,
                         color:  '#fff' , //左边线的颜色
                         width: '2' //坐标线的宽度
                     }
                 },
                 axisLabel: {
                     textStyle: {
                         color:  '#fff' , //坐标值得具体的颜色
 
                     }
                 },
        },
        series: [
            {
            data: this.temp,
            time:this.time,
            type: 'line',
            areaStyle: {}
            }
        ]
        };

        option && this.myChart.setOption(option);

        }
    },
    created(){
        for(var i=0;i<this.hourly.length;i++){
            this.time[i] = this.hourly[i].time
            this.temp[i] = this.hourly[i].temp
            this.img[i] = this.hourly[i].img
        }
    },
    mounted(){
        this.showImg()
        
        pubsub.subscribe('gotoFlush', async()=>{
            await this.myChart.dispose()//销毁视图
            await this.flush()//刷新数据
            this.showImg()//创建视图
        })//订阅刷新
    },
    
}
</script>

<style scoped>
#box1 {
    /* margin-top:30px; */
    width: 799px;
    height: 128px;
    margin-left: auto;
    margin-right: auto;
    
    padding: 30px;
    background-color: rgba(0,0,0,.1);
    /* background-color: rgb(112, 44, 44); */
}
#box2{
    padding: 0;
    width: 100%;
    height:200px;
    /* border: 1px solid black; */
}
</style>