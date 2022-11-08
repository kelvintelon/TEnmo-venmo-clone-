<template>
<div>
  <div class="home">
    <div class="sideMenu"><side-menu></side-menu></div>
    <header-logo></header-logo>
    
    <img id="logoutimage" src="../../img/image.png" v-if="$store.state.token == ''"/>
 
 





   
  </div>
  </div>
</template>

<script>
import SideMenu from "../components/SideMenu.vue";
import HeaderLogo from "../components/HeaderLogo.vue";


export default {
  name: "home",
  data() {
    return {
        
    }
  },
  mounted(){
        
    },
  components: {
    SideMenu,
    HeaderLogo,
  },
  
};
</script>
<style>

.image {
    width:30%;
    margin: auto;
    display: block;
}

body {
  background-color: #000000;
  margin: 0px;
}

.title {
  font-size: 40px;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;

  color: aliceblue;
  
}
.sideMenu{
  z-index: 0;
}

#logoutimage{
  z-index: -100;
  margin:0;
  border-radius: 30px;
  width: 100%;
}


</style>