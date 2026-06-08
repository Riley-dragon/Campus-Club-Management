<template>
  <div>
    <h2 style="margin-bottom:20px;">我的申请</h2>
    <el-card>
      <el-table :data="applications" v-loading="loading" stripe>
        <el-table-column prop="club.name" label="社团名称" />
        <el-table-column prop="studentName" label="姓名" />
        <el-table-column prop="college" label="学院" />
        <el-table-column label="状态">
          <template #default="{ row }">
            <el-tag :type="statusType[row.status]">{{ statusMap[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="leaderRemark" label="初审意见" />
        <el-table-column prop="adminRemark" label="终审意见" />
        <el-table-column prop="createTime" label="申请时间" width="180" />
      </el-table>
      <el-empty v-if="!loading && applications.length === 0" description="暂无申请记录" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyApplications } from '../api'

const loading = ref(false)
const applications = ref([])
const statusMap = {
  PENDING: '待初审', LEADER_PASS: '初审通过', LEADER_REJECT: '初审拒绝',
  ADMIN_PASS: '终审通过(已录取)', ADMIN_REJECT: '终审拒绝'
}
const statusType = {
  PENDING: 'warning', LEADER_PASS: 'primary', LEADER_REJECT: 'danger',
  ADMIN_PASS: 'success', ADMIN_REJECT: 'danger'
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await getMyApplications()
    applications.value = res.data || []
  } catch (e) {} finally { loading.value = false }
})
</script>
