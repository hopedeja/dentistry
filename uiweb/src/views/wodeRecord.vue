<template>
<div class="wdr">
  <!-- 顶部标题 -->
  <div class="tit">
    <van-nav-bar title="我的报告" left-text="返回" left-arrow @click-left="wdRe"/>
  </div>

  <div class="buyde" v-for='(item, index) in reportList' :key='index' v-show='isxs' @click='wmbg(item.id)'>
    <p class="wode">您{{item.appointDate.split('T')[0]}}预约的【{{item.productName}}】的报告 已生成，请查收。</p>
    <p class="xiangq">{{item.lastModifiedDate | timeFormat}}</p>
    <span class='rjt'><van-icon name="arrow" /></span>
  </div>

  <div v-show='!isxs'>
    <div class="emp_yy">
      <img src="../assets/img/emp_yy.png">
      <p>暂无相关订单</p>
    </div>
    <div class="login_btn">
      <button>去购买</button>
    </div>
  </div>

</div>
</template>

<style scoped>
/* 头部开始 */
  .wdr{
    background: #e8e8e8;
    height:177.7vw;
  }
  .wdr .tit .van-nav-bar{
      background: #000;
    }
  .wdr .tit .van-nav-bar__text,.tit .van-nav-bar .van-icon,.tit .van-nav-bar__title{
    color:#fff;
  }
/* 头部结束 */

.wdr .buyde{
  width:90%;margin:5vw auto;
  background: #fff;
  border-radius: 0.8rem;
  overflow: hidden;
  position: relative;
}
.wdr .buyde p.xiangq{
  margin:0;padding:0;
  margin-left:5vw;
  margin-bottom: 3vw;
  font-size: 0.8rem;
  color:#939393;
  position: relative;
}
.wdr .buyde p.wode{
  font-size: 1rem;
  color:black;
  padding: 0 5vw;
}
.rjt{
  position:absolute;
  right:3%;
  top:40%;
}
.wdr .emp_yy{
  margin:10vw auto;
  text-align: center;
}
.wdr .login_btn{
  margin:20vw auto;
  text-align:center;
}
.wdr .login_btn button{
  border:0;
  outline: 0;
  width:75vw;
  height:13vw;
  border-radius: 30px;
  background:rgb(102 205 170);
  color:#fff;
}

</style>

<script>
import {mapState} from 'vuex';
// import qs from 'qs';
export default {
  computed:{
    ...mapState([
        'username','iphone','id','sex'
    ])
  },
  data(){
    return {
      isxs:true,
      reportList: [],
    }
  },
  methods:{
    wdRe(){
      if(this.isxs){
        this.$router.push('/');
      }
      this.isxs=true;
    },
    wmbg(id){
      this.$router.push('/mr?id='+id);
    }
  },
  mounted(){

    this.axios.get('api/frontdesk/report/list').then(({data})=>{
      this.reportList = data.data
    })
  }
}
</script>