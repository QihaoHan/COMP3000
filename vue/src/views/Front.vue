<template>
  <div style="background-color: #f6f6f6; min-height: 100vh">
    <div class="front-notice"><i class="el-icon-bell" style="margin-right: 2px"></i>Announcement: {{ top }}</div>
    <!-- Header -->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">NHMS</div>
      </div>
      <div class="front-header-center">
        <div class="front-header-nav">
          <el-menu :default-active="$route.path" mode="horizontal" router>
            <el-menu-item index="/front/home">Home</el-menu-item>
            <el-menu-item index="/front/activity">Public Activities</el-menu-item>
            <el-menu-item index="/front/serve">Elderly Services</el-menu-item>
            <el-menu-item index="/front/nurseHouse">Nursing Home</el-menu-item>
            <el-menu-item index="/front/person">Personal Center</el-menu-item>
          </el-menu>
        </div>
      </div>
      <div class="front-header-right">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">Log In</el-button>
          <el-button @click="$router.push('/register')">Register</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="front-header-dropdown">
              <img :src="user.avatar" alt="">
              <div style="margin-left: 10px">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div @click="$router.push('/front/activitySign')">My Registered Activities</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/reserve')">My Reservations</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="logout">Log Out</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!-- Main Body -->
    <div class="main-body">
      <router-view ref="child" @update:user="updateUser" />
    </div>

    <Footer />
  </div>
</template>

<script>
import Footer from "@/components/Footer";
export default {
  name: "FrontLayout",
  components: {Footer},
  data() {
    return {
      top: '',
      notice: [],
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
    }
  },

  mounted() {
    this.loadNotice()
  },
  methods: {
    loadNotice() {
      this.$request.get('/notice/selectAll').then(res => {
        this.notice = res.data
        let i = 0
        if (this.notice && this.notice.length) {
          this.top = this.notice[0].content
          setInterval(() => {
            this.top = this.notice[i].content
            i++
            if (i === this.notice.length) {
              i = 0
            }
          }, 2500)
        }
      })
    },
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    },
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
  }
}
</script>

<style scoped>
@import "@/assets/css/front.css";
</style>
