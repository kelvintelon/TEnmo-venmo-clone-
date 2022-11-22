<template>
  <div class="wholePage">
    <div class="sideMenu"><side-menu></side-menu></div>
    <header-logo></header-logo>
    
    <div class="currentUsernameBox">
      <h1 class="UsernameFont">{{ username }}</h1>
      
    </div>
    
  </div>
</template>

<script>
import HeaderLogo from "../components/HeaderLogo.vue";
import SideMenu from "../components/SideMenu.vue";


export default {
  components: {
    HeaderLogo,
    SideMenu,
    
  },
  data() {
    return {
      
      username: "",
      
    };
  },
  mounted() {
    
  },
  methods: {
    checkUser() {
      const UserString = localStorage.getItem("user");
      let firstIndex = UserString.indexOf("username");
      let secondIndex = UserString.indexOf("password");
      this.username = UserString.substring(firstIndex + 11, secondIndex - 3);
    },
    
     
    
  },
};
</script>

<style>
.currentUsernameBox {
  border-bottom: 1px solid black;
  border-bottom-style: none;
  padding-left: 20px;
  display: flex;
  justify-content: space-between;
}

.UsernameFont {
  margin-top: 10px;
  margin-bottom: 10px;
  font-size: 55px;
  font-family: Arial, Helvetica, sans-serif;
  color: #efe3ef;

}


</style>