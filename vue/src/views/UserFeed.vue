<template>
  <div>
    <div class="sideMenu"><side-menu></side-menu></div>
    <header-logo></header-logo>
    <!-- <div class="currentUsernameBox">
      <h1 class="UsernameFont">{{ username }}</h1>
      <div class="numOfPics">{{ photoCount }} Pictures Uploaded</div>
    </div> -->
    
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
    
    };
  },
  mounted() {
   
  },
  methods: {
    
    },
};
</script>

<style>

</style>