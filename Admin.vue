<template>
  <div>
    <h2 style="margin-bottom:20px;">后台管理</h2>
    <el-tabs v-model="activeTab" type="card">

      <!-- 数据看板 -->
      <el-tab-pane label="数据看板" name="dashboard">
        <el-row :gutter="20">
          <el-col :span="4" v-for="(item, idx) in dashboardCards" :key="idx">
            <el-card shadow="hover" class="dash-card">
              <div class="dash-num" :style="{ color: item.color }">{{ item.value }}</div>
              <div class="dash-label">{{ item.label }}</div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <!-- 社团审核 -->
      <el-tab-pane label="社团审核" name="clubReview">
        <el-table :data="pendingClubs" v-loading="clubLoading" stripe>
          <el-table-column prop="name" label="社团名称" />
          <el-table-column prop="category" label="类别" />
          <el-table-column prop="contactName" label="联系人" />
          <el-table-column prop="description" label="介绍" show-overflow-tooltip />
          <el-table-column label="状态">
            <template #default="{ row }">
              <el-tag :type="{ PENDING:'warning', APPROVED:'success', REJECTED:'danger' }[row.status]">
                {{ { PENDING:'待审核', APPROVED:'已通过', REJECTED:'已拒绝' }[row.status] }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <template v-if="row.status === 'PENDING'">
                <el-button type="success" size="small" @click="handleApproveClub(row.id, true)">通过</el-button>
                <el-button type="danger" size="small" @click="handleApproveClub(row.id, false)">拒绝</el-button>
              </template>
              <span v-else style="color:#909399;">已处理</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 报名终审 -->
      <el-tab-pane label="报名终审" name="adminReview">
        <el-table :data="leaderPassedApps" v-loading="appLoading" stripe>
          <el-table-column prop="studentName" label="姓名" />
          <el-table-column prop="studentIdNo" label="学号" />
          <el-table-column prop="college" label="学院" />
          <el-table-column prop="club.name" label="申请社团" />
          <el-table-column prop="leaderRemark" label="初审意见" />
          <el-table-column label="操作" width="240">
            <template #default="{ row }">
              <el-button type="success" size="small" @click="handleAdminReview(row.id, true)">终审通过</el-button>
              <el-button type="danger" size="small" @click="handleAdminReview(row.id, false)">终审拒绝</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!appLoading && leaderPassedApps.length === 0" description="暂无待终审申请" />
      </el-tab-pane>

      <!-- 社团管理 -->
      <el-tab-pane label="社团管理" name="clubs">
        <el-table :data="allClubs" v-loading="allClubLoading" stripe>
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="name" label="社团名称" />
          <el-table-column prop="category" label="类别" />
          <el-table-column prop="leader.realName" label="负责人" />
          <el-table-column label="状态">
            <template #default="{ row }">
              <el-tag :type="{ PENDING:'warning', APPROVED:'success', REJECTED:'danger' }[row.status]">
                {{ { PENDING:'待审核', APPROVED:'已通过', REJECTED:'已拒绝' }[row.status] }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" />
        </el-table>
      </el-tab-pane>

      <!-- 用户管理 -->
      <el-tab-pane label="用户管理" name="users">
        <el-table :data="users" v-loading="userLoading" stripe>
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="realName" label="姓名" />
          <el-table-column prop="studentId" label="学号" />
          <el-table-column prop="college" label="学院" />
          <el-table-column label="角色">
            <template #default="{ row }">
              <el-tag :type="{ STUDENT:'', LEADER:'success', ADMIN:'danger' }[row.role]">
                {{ { STUDENT:'学生', LEADER:'负责人', ADMIN:'管理员' }[row.role] }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-select v-model="row.role" size="small" style="width:100px;" @change="handleRoleChange(row)">
                <el-option label="学生" value="STUDENT" />
                <el-option label="负责人" value="LEADER" />
                <el-option label="管理员" value="ADMIN" />
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getDashboard, getAdminClubs, approveClub, getLeaderPassedApplications,
         adminReview, getUsers, updateUserRole } from '../api'
import { ElMessage } from 'element-plus'

const activeTab = ref('dashboard')

// 看板
const dashboard = ref({})
const dashboardCards = computed(() => [
  { label: '社团总数', value: dashboard.value.totalClubs || 0, color: '#409eff' },
  { label: '学生总数', value: dashboard.value.totalStudents || 0, color: '#67c23a' },
  { label: '申请总数', value: dashboard.value.totalApplications || 0, color: '#e6a23c' },
  { label: '待终审', value: dashboard.value.pendingApplications || 0, color: '#f56c6c' },
  { label: '活动总数', value: dashboard.value.totalActivities || 0, color: '#909399' },
  { label: '正式成员', value: dashboard.value.totalMembers || 0, color: '#67c23a' }
])

// 社团审核
const pendingClubs = ref([])
const clubLoading = ref(false)

// 终审
const leaderPassedApps = ref([])
const appLoading = ref(false)

// 全部社团
const allClubs = ref([])
const allClubLoading = ref(false)

// 用户
const users = ref([])
const userLoading = ref(false)

onMounted(async () => {
  try {
    const res = await getDashboard()
    dashboard.value = res.data || {}
  } catch (e) {}
  loadClubs()
  loadApps()
  loadUsers()
})

async function loadClubs() {
  clubLoading.value = true; allClubLoading.value = true
  try {
    const res = await getAdminClubs()
    allClubs.value = res.data || []
    pendingClubs.value = allClubs.value.filter(c => c.status === 'PENDING')
  } catch (e) {} finally { clubLoading.value = false; allClubLoading.value = false }
}

async function loadApps() {
  appLoading.value = true
  try {
    const res = await getLeaderPassedApplications()
    leaderPassedApps.value = res.data || []
  } catch (e) {} finally { appLoading.value = false }
}

async function loadUsers() {
  userLoading.value = true
  try {
    const res = await getUsers()
    users.value = res.data || []
  } catch (e) {} finally { userLoading.value = false }
}

async function handleApproveClub(id, pass) {
  try {
    await approveClub(id, pass)
    ElMessage.success(pass ? '已通过' : '已拒绝')
    loadClubs()
    const dRes = await getDashboard()
    dashboard.value = dRes.data || {}
  } catch (e) {}
}

async function handleAdminReview(id, pass) {
  try {
    await adminReview(id, pass, pass ? '终审通过' : '终审拒绝')
    ElMessage.success(pass ? '终审通过，已成为社团成员' : '已拒绝')
    loadApps()
    const dRes = await getDashboard()
    dashboard.value = dRes.data || {}
  } catch (e) {}
}

async function handleRoleChange(user) {
  try {
    await updateUserRole(user.id, user.role)
    ElMessage.success('角色更新成功')
  } catch (e) { loadUsers() }
}
</script>

<style scoped>
.dash-card { text-align: center; padding: 20px 0; }
.dash-num { font-size: 36px; font-weight: 700; }
.dash-label { color: #909399; margin-top: 8px; }
</style>
