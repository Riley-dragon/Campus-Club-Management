<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 style="text-align:center;margin-bottom:30px;">用户登录</h2>
      <el-form :model="form" label-width="70px" @submit.prevent="handleLogin">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width:100%;" native-type="submit">登 录</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align:center;">
        还没有账号？<el-link type="primary" @click="router.push('/register')">立即注册</el-link>
      </div>
      <el-divider>测试账号</el-divider>
      <div class="test-accounts">
        <el-tag @click="fillAccount('admin','admin123')" style="cursor:pointer">管理员 admin</el-tag>
        <el-tag type="success" @click="fillAccount('leader01','leader123')" style="cursor:pointer">负责人 leader01</el-tag>
        <el-tag type="warning" @click="fillAccount('student01','student123')" style="cursor:pointer">学生 student01</el-tag>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const form = ref({ username: '', password: '' })

const fillAccount = (u, p) => { form.value.username = u; form.value.password = p }

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    await userStore.login(form.value.username, form.value.password)
    ElMessage.success('登录成功')
    router.push('/home')
  } catch (e) {
    // error handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex; justify-content: center; align-items: center;
  min-height: calc(100vh - 120px);
}
.login-card { width: 420px; padding: 20px; }
.test-accounts { display: flex; justify-content: center; gap: 10px; flex-wrap: wrap; }
</style>
