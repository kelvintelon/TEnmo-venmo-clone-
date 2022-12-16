<template>
<div>
  <div class="home">
    <side-menu></side-menu>
    <header-logo></header-logo>
    <div class="sideMenu"></div>
    
    <img id="logoutimage" src="../../img/image.png" v-if="$store.state.token == ''"/>
    <div v-else class="user-list" ><user-list :newList="newList" ></user-list></div>
    
    
    
    
    





   
  </div>
  </div>
</template>

<script>
import SideMenu from "../components/SideMenu.vue";
import HeaderLogo from "../components/HeaderLogo.vue";
import UserList from "../components/UserList.vue"
import accountService from "../services/AccountService";
import userService from "../services/AuthService";



export default {
  name: "home",
  data() {
    return {
        newList: [],
    }
  },
  components: {
    SideMenu,
    HeaderLogo,
    UserList,
  },
  methods: {
    
  },
  mounted(){
        
    },
   beforeCreate() {
     accountService.getAllAccounts().then((response2) => {
       
       this.$store.commit("SET_LIST", response2.data);
        this.newList = this.$store.state.listOfUsers;
        this.newList.forEach((user) => { 
          userService.getUsernameByUserId((user.userId)).then((response3) => {
        user.username =  response3.data
        
        });
          })
          this.$store.commit("SET_LIST", this.newList);
          
      });

      // UploadFileService.getFiles().then((response) => {
      //   this.$store.commit("SET_IMAGEPOSTS", response.data);
      // });
  //   // accountService.getBalance().then((response) => {
  //   //   this.balance= response.data;
  //   // });
  this.$store.commit("SET_LIST", this.newList);
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