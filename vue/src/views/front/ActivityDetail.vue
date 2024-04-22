<template>
  <div class="main-content" style="width: 60%">
    <div class="card">
      <div style="display: flex; gap: 20px;">
        <img :src="activity.cover" alt="" style="width: 50%">
        <div style="flex: 1">
          <div style="font-size: 22px; margin-bottom: 40px">{{ activity.name }}</div>
          <div class="item"><span style="color: #333">Activity Brief:</span>{{ activity.descr }}</div>
          <div class="item"><span style="color: #333">Activity Date:</span>{{ activity.start }} ~ {{ activity.end }}</div>
          <div class="item"><span style="color: #333">Activity Address:</span>{{ activity.address }}</div>
          <div>
            <el-button @click="handleSign" size="medium" type="primary" v-if="activity.status === 'Not Registered'">Start Registration</el-button>
            <el-button size="medium" type="primary" v-if="activity.status === 'Signed Up'" disabled key="signed">Signed Up</el-button>
            <el-button size="medium" type="primary" v-if="activity.status === 'Not Started'" disabled key="notStarted">Not Started</el-button>
            <el-button size="medium" type="primary" v-if="activity.status === 'Ended'" disabled key="ended">Ended</el-button>
          </div>
        </div>
      </div>

      <div style="margin: 50px 0">
        <div style="margin-bottom: 30px; font-size: 22px; font-weight: bold; text-align: center">- Activity Details -</div>
        <div class="w-e-text" style="line-height: 26px">
          <div v-html="activity.content"></div>
        </div>
      </div>

    </div>

    <Comment :fid="id" module="activity" />

    <el-dialog userName="Registration Info" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="300px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="phone" label="Mobile Number">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="sign">Confirm</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import Comment from "@/components/Comment";
export default {
  name: "ActivityDetail",
  components: {Comment},
  data() {
    return {
      id: this.$route.query.id,
      activity: {},
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        phone: [
          {required: true, message: 'Mobile number is required', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    sign() {
      this.$request.post('/activitySign/add', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('Registration successful')
          this.fromVisible = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleSign() {
      this.form = {}
      this.form.userId = this.user.id
      this.form.activityId = this.id
      this.fromVisible = true
    },
    load() {
      this.$request.get('/activity/selectById/' + this.id).then(res => {
        this.activity = res.data || {}
      })
    }
  }
}
</script>

<style scoped>
.item {
  color: #666;
  margin-bottom: 40px
}
</style>
