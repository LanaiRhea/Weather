<template>
  <div id="box1">
      <ul id="box2">
        <li id="everyOne" v-for="i in 4" :key="i">
          <h2 class="index-title">{{index[i].iname}}</h2>
          <p class="index-detail">{{index[i].detail}}</p>
          <div class="index-value">{{index[i].ivalue}}</div>
        </li>
      </ul>
  </div>
</template>

<script>
export default {
    props:['index']
}
</script>

<style scoped>
#box1 {
    margin-top: 25px;
    width: 799px;
    height: auto;
    min-height: 128px;
    margin-left: auto;
    margin-right: auto;
    color: #fff;
    font-family: '微软雅黑';
    background-color: rgba(0, 0, 0, 0.1);
    padding: 30px;
}

#box2 {
    display: flex;
    justify-content: space-between;
    margin: 0;
    padding: 0;
}

#everyOne {
    position: relative;
    width: 23%;
    min-height: 130px;
    padding: 15px;
    background-color: rgba(0, 0, 0, 0.05);
    border-radius: 8px;
    transition: all 0.3s ease;
}

#everyOne:hover {
    background-color: rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
}

.index-title {
    font-size: 18px;
    margin: 0 0 15px 0;
    color: rgba(255, 255, 255, 0.9);
}

.index-detail {
    font-size: 14px;
    line-height: 1.6;
    margin: 10px 0;
    color: rgba(255, 255, 255, 0.8);
    text-align: justify;
}

.index-value {
    position: absolute;
    bottom: 15px;
    left: 15px;
    padding: 4px 12px;
    background-color: rgb(16, 194, 25);
    border-radius: 15px;
    font-size: 14px;
    color: white;
    display: inline-block;
    transition: all 0.3s ease;
}

.index-value:hover {
    transform: scale(1.05);
    box-shadow: 0 2px 8px rgba(16, 194, 25, 0.3);
}
</style>