<template>
  <div class="club-detail" v-loading="loading">
    <el-page-header @back="router.push('/home')" :title="'返回'" :content="club.name || '社团详情'" />

    <el-card style="margin-top:20px;">
      <div class="club-header">
        <el-icon :size="64" color="#409eff"><Collection /></el-icon>
        <div class="club-info">
          <h2>{{ club.name }} <el-tag>{{ club.category }}</el-tag></h2>
          <p><el-icon><User /></el-icon> 联系人：{{ club.contactName }} | {{ club.contactPhone }} | {{ club.contactEmail }}</p>
        </div>
      </div>
    </el-card>

    <el-row :gutter="20" style="margin-top:20px;">
      <el-col :span="16">
        <el-card>
          <template #header><strong>社团介绍</strong></template>
          <p style="line-height:1.8;white-space:pre-line;">{{ club.description }}</p>
        </el-card>
        <el-card style="margin-top:20px;">
          <template #header><strong>招新要求</strong></template>
          <p style="line-height:1.8;white-space:pre-line;">{{ club.requirement }}</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header><strong>报名入口</strong></template>
          <div v-if="userStore.isLoggedIn && userStore.isStudent">
            <el-button type="primary" size="large" style="width:100%;" @click="showApply = true">
              <el-icon><EditPen /></el-icon> 立即报名
            </el-button>
          </div>
          <div v-else-if="!userStore.isLoggedIn">
            <p style="color:#909399;text-align:center;">请先<el-link type="primary" @click="router.push('/login')">登录</el-link>后报名</p>
          </div>
          <div v-else>
            <p style="color:#909399;text-align:center;">当前角色无法报名</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 报名弹窗 -->
    <el-dialog v-model="showApply" title="报名申请" width="500px">
      <el-form :model="applyForm" label-width="80px">
        <el-form-item label="报名理由">
          <el-input v-model="applyForm.reason" type="textarea" :rows="4" placeholder="请简要介绍自己，说明报名理由..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showApply = false">取消</el-button>
        <el-button type="primary" :loading="applyLoading" @click="handleApply">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { getClub, submitApplication } from '../api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const club = ref({})
const showApply = ref(false)
const applyLoading = ref(false)
const applyForm = ref({ reason: '' })

onMounted(async () => {
  loading.value = true
  try {
    const res = await getClub(route.params.id)
    club.value = res.data
  } catch (e) {} finally { loading.value = false }
})

const handleApply = async () => {
  if (!applyForm.value.reason) {
    ElMessage.warning('请填写报名理由')
    return
  }
  applyLoading.value = true
  try {
    // 自动填充当前登录用户的信息
    const userInfo = userStore.user || {}
    const payload = {
      ...applyForm.value,
      studentName: userInfo.realName || '',
      studentIdNo: userInfo.studentId || '',
      college: userInfo.college || '',
      major: userInfo.major || '',
      phone: userInfo.phone || '',
      email: userInfo.email || ''
    }
    await submitApplication(route.params.id, payload)
    ElMessage.success('报名申请提交成功！')
    showApply.value = false
    applyForm.value.reason = ''
  } catch (e) {} finally { applyLoading.value = false }
}
</script>

<style scoped>
.club-header { display: flex; gap: 20px; align-items: center; }
.club-info h2 { margin-bottom: 8px; }
.club-info p { color: #606266; }
</style>
