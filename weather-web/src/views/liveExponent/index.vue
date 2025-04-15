<template>
  <div id="box1">
      <ul id="box2">
        <li id="everyOne" v-for="i in 4" :key="i">
          <h2>{{index[i].iname}}</h2>
          <p style="font-size:15px;text-indent: 2em;margin-top:10px;">{{index[i].detail}}</p>
          <h3 style="height:16px;position:absolute;top:100px; line-height:16px;border-radius:15px;padding:2px;background-color: rgb(16, 194, 25);display:inline-block;">{{index[i].ivalue}}</h3>
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
    /* margin-top:30px; */
    width: 799px;
    height: 128px;
    margin-left: auto;
    margin-right: auto;
    color: #fff;
    font-family: '微软雅黑';
    background-color: rgba(0, 0, 0,0.1);
    
    
    
    
    
    padding: 30px;
    /* padding: 30px; */
    
}
#box2 {
  display: flex;
}
#everyOne{
  position: relative;
  width: 25%;
  height: 130px ;
  /* background-color: rgba(0, 0,0,0.1); */
  /* border: 1px solid black; */
  margin-right: 10px;

}

</style>