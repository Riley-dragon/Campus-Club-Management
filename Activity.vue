<template>
  <div>
    <h2 style="margin-bottom:20px;">活动中心</h2>
    <el-row :gutter="20" v-loading="loading">
      <el-col :span="8" v-for="act in activities" :key="act.id" style="margin-bottom:20px;">
        <el-card shadow="hover">
          <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center;">
              <strong>{{ act.title }}</strong>
              <el-tag size="small" :type="act.status === 'UPCOMING' ? 'success' : 'info'">
                {{ statusMap[act.status] }}
              </el-tag>
            </div>
          </template>
          <p style="color:#606266;margin-bottom:8px;">{{ act.description?.substring(0, 100) }}...</p>
          <div style="font-size:13px;color:#909399;">
            <p><el-icon><Location /></el-icon> {{ act.location }}</p>
            <p><el-icon><Clock /></el-icon> {{ act.startTime }} ~ {{ act.endTime }}</p>
            <p><el-icon><User /></el-icon> {{ act.currentParticipants }}/{{ act.maxParticipants || '不限' }} 人</p>
          </div>
          <el-button type="primary" style="margin-top:12px;width:100%;" @click="handleRegister(act)"
                     :disabled="act.status !== 'UPCOMING'">
            报名参加
          </el-button>
        </el-card>
      </el-col>
    </el-row>
    <el-empty v-if="!loading && activities.length === 0" description="暂无活动" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getActivities, registerActivity } from '../api'
import { useUserStore } from '../store/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const loading = ref(false)
const activities = ref([])
const statusMap = { UPCOMING: '即将开始', ONGOING: '进行中', COMPLETED: '已结束', CANCELLED: '已取消' }

onMounted(async () => {
  loading.value = true
  try {
    const res = await getActivities()
    activities.value = res.data || []
  } catch (e) {} finally { loading.value = false }
})

const handleRegister = async (act) => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    await registerActivity(act.id)
    ElMessage.success('报名成功')
    act.currentParticipants++
  } catch (e) {}
}
</script>
