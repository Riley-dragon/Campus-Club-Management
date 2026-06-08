<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 style="text-align:center;margin-bottom:30px;">学生注册</h2>
      <el-form :model="form" label-width="80px" @submit.prevent="handleRegister">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="form.studentId" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="学院">
          <el-input v-model="form.college" placeholder="请输入学院" />
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="form.major" placeholder="请输入专业" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width:100%;" native-type="submit">注 册</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align:center;">
        已有账号？<el-link type="primary" @click="router.push('/login')">返回登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const form = ref({
  username: '', password: '', realName: '', studentId: '',
  college: '', major: '', email: '', phone: ''
})

const handleRegister = async () => {
  if (!form.value.username || !form.value.password || !form.value.realName) {
    ElMessage.warning('请填写必填项')
    return
  }
  loading.value = true
  try {
    await register(form.value)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (e) {} finally { loading.value = false }
}
</script>

<style scoped>
.register-container { display: flex; justify-content: center; align-items: center; min-height: calc(100vh - 120px); }
.register-card { width: 480px; padding: 20px; }
</style>
