<template>
  <div class="container">
    <!-- Background image is applied via CSS for the entire container -->
    <div class="login-container">
      <div class="login-box">
        <div style="text-align: center; font-size: 22px; margin-bottom: 30px; font-weight: bold; color: #2A60C9">Welcome</div>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" placeholder="Enter your username" v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" placeholder="Enter your password" show-password v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item prop="role">
            <el-select style="width: 100%" v-model="form.role">
              <el-option value="ADMIN" label="Administrator"></el-option>
              <el-option value="USER" label="Regular User"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100%; background-color: #2A60C9; border-color: #2A60C9; color: white" @click="login">Login</el-button>
          </el-form-item>
          <div style="display: flex; justify-content: flex-end">
            Not registered? <a href="/register">Sign Up</a>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: { role: 'ADMIN' },
      rules: {
        username: [
          { required: true, message: 'Please enter your username', trigger: 'blur' },
        ],
        password: [
          { required: true, message: 'Please enter your password', trigger: 'blur' },
        ]
      }
    }
  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data));
              this.$message.success('Login successful');
              setTimeout(() => {
                location.href = res.data.role === 'ADMIN' ? '/home' : '/front/home';
              }, 500);
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
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
  width: 100%; /* Adjust this as needed */
  max-width: 400px; /* Adjust this as needed */
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
