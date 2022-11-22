<template>
<div>
  <div class="home">
    <side-menu></side-menu>
    <header-logo></header-logo>
    <div class="sideMenu"></div>
    <img id="logoutimage" src="../../img/image.png" v-if="$store.state.token == ''"/>
    <div v-else class="user-list"><user-list></user-list></div>
    
    
    
    
    





   
  </div>
  </div>
</template>

<script>
import SideMenu from "../components/SideMenu.vue";
import HeaderLogo from "../components/HeaderLogo.vue";
import UserList from "../components/UserList.vue"


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
    UserList,
  },
  
};
</script>
<style>
.user-list{
  display: flex;
  justify-content: center;
}
.image {
    width:30%;
    margin: auto;
    display: block;
}

body {
  background-color: #b49019;
  margin: 0px;
}

.title {
  font-size: 40px;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;

  color: aliceblue;
  
}



#logoutimage{
  z-index: -100;
  margin:0;
  border-radius: 30px;
  width: 100%;
}


</style>