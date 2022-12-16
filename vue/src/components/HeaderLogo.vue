<template>
  <div>
    <header>
   
      <div class="mainHeader">
        <router-link v-bind:to="{ name: 'home'}" style="text-decoration: none; color: inherit;">

            <h1 class="title"><span class="highlight">T</span><span class="highlight">E</span>nmo</h1>
          
          
        </router-link>

        <div class="loginUsername">
            <h3 class="loginUser" v-if="this.username != ''">Hi, {{this.username}} </h3>
        </div>

      </div>
      
    </header>
    
  </div>
</template>

<script>
export default {
    name:  "header-logo",
    data(){
        return{
          username:"",
        }
    },
    created(){
      if (this.$store.state.token != '') {
      const UserString= localStorage.getItem("user");
      let firstIndex = UserString.indexOf("username");
      let secondIndex = UserString.indexOf("password");
      this.username = UserString.substring(firstIndex + 11, secondIndex - 3);
      }
    }
};
</script>

<style>
header {
  
  
  text-align: center;
  background-color: #b49019;
 
  width: 100%;
  height: 80px;
  border-bottom: 1px solid black;
}

.mainHeader{
  margin-left: 55%;
  text-decoration: none;
 display: flex;

}

.highlight{
  color: green;
}

.logo{
  height: 100%;
  display: table-cell;

  vertical-align: middle;
  float:left;
  padding: 0px 0px 0px 10px;

  margin-top: 10px;
  margin-bottom: auto;

  width: 60px;
  height: 60px;

}
.loginUsername{
  width: 100%;
  height: 110%;
  display: flex;
  flex-direction: row-reverse;
  align-content: right;
  margin: 0 0 auto auto;
}

.loginUser{
  display: flex;
  color: aliceblue;
  border:1px dashed aliceblue;
  border-radius: 5px;
  padding: 5px;
  margin-right: 20px;
  
}


</style>