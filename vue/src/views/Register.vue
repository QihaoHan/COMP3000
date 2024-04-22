<template>
  <div class="container">
    <!-- Removed inline styles and added class for background and centering -->
    <div class="login-container">
      <div class="login-box">
        <div style="text-align: center; font-size: 22px; margin-bottom: 30px; font-weight: bold; color: #2A60C9">Welcome to Register</div>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" placeholder="Please enter username" v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" placeholder="Please enter password" show-password v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input prefix-icon="el-icon-lock" placeholder="Please confirm password" show-password v-model="form.confirmPassword"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100%; background-color: #2A60C9; border-color: #2A60C9; color: white" @click="register">Register</el-button>
          </el-form-item>
          <div style="display: flex; justify-content: flex-end">
            Already have an account? Please <a href="/login">Log in</a>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  name: "Register",
  data() {
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('Please confirm password'))
      } else if (confirmPass !== this.form.password) {
        callback(new Error('Passwords do not match'))
      } else {
        callback()
      }
    }
    return {
      form: { role: 'USER' },
      rules: {
        username: [
          { required: true, message: 'Please enter username', trigger: 'blur' },
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    register() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          this.$request.post('/register', this.form).then(res => {
            if (res.code === '200') {
              this.$router.push('/login')  // Navigate to login
              this.$message.success('Registration successful')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: url('@/assets/imgs/background.jpg') no-repeat center center fixed;
  background-size: cover;
}

.login-container {
  width: 100%;
  max-width: 400px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.login-box {
  padding: 20px;
}

a {
  color: #2A60C9;
  margin-left: 5px;
}
</style>
