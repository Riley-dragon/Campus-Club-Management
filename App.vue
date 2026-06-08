<template>
  <el-config-provider :locale="zhCn">
    <div class="app-container">
      <el-header class="app-header">
        <div class="header-left" @click="router.push('/home')">
          <el-icon :size="24"><School /></el-icon>
          <span class="title">校园社团管理平台</span>
        </div>
        <div class="header-nav">
          <el-menu mode="horizontal" :router="true" :default-active="route.path" :ellipsis="false">
            <el-menu-item index="/home">首页</el-menu-item>
            <el-menu-item index="/activities">活动中心</el-menu-item>
            <el-menu-item v-if="userStore.isLoggedIn" index="/my-applications">我的申请</el-menu-item>
            <el-menu-item v-if="userStore.isLeader || userStore.isAdmin" index="/leader">社团管理</el-menu-item>
            <el-menu-item v-if="userStore.isAdmin" index="/admin">后台管理</el-menu-item>
          </el-menu>
        </div>
        <div class="header-right">
          <template v-if="userStore.isLoggedIn">
            <el-dropdown>
              <span class="user-info">
                <el-avatar :size="32" icon="User" />
                <span>{{ userStore.user?.realName }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item disabled>{{ roleLabel }}</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="router.push('/login')">登录</el-button>
            <el-button @click="router.push('/register')">注册</el-button>
          </template>
        </div>
      </el-header>
      <el-main class="app-main">
        <router-view />
      </el-main>
    </div>
  </el-config-provider>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from './store/user'
import { ElMessage } from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const roleLabel = computed(() => {
  const map = { STUDENT: '学生', LEADER: '社团负责人', ADMIN: '管理员' }
  return map[userStore.role] || ''
})

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { background: #f5f7fa; }
.app-container { min-height: 100vh; }
.app-header {
  display: flex; align-items: center; justify-content: space-between;
  background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  padding: 0 24px; height: 60px; position: sticky; top: 0; z-index: 100;
}
.header-left { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.header-left .title { font-size: 18px; font-weight: 600; color: #409eff; }
.header-nav .el-menu { border-bottom: none; }
.header-right { display: flex; align-items: center; gap: 12px; }
.user-info { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.app-main { max-width: 1200px; margin: 0 auto; padding: 20px; width: 100%; }
</style>
