<template>
  <div>
    <h2 style="margin-bottom:20px;">社团管理</h2>
    <el-tabs v-model="activeTab" type="card">

      <!-- 社团信息管理 -->
      <el-tab-pane label="我的社团" name="clubs">
        <el-button type="primary" style="margin-bottom:16px;" @click="showCreateClub = true">创建新社团</el-button>
        <el-card v-for="club in myClubs" :key="club.id" style="margin-bottom:16px;">
          <el-descriptions :title="club.name" :column="3" border>
            <el-descriptions-item label="类别">{{ club.category }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="club.status === 'APPROVED' ? 'success' : club.status === 'PENDING' ? 'warning' : 'danger'">
                {{ { PENDING: '待审核', APPROVED: '已通过', REJECTED: '已拒绝' }[club.status] }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="联系人">{{ club.contactName }}</el-descriptions-item>
            <el-descriptions-item label="介绍" :span="3">{{ club.description }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-tab-pane>

      <!-- 报名审核 -->
      <el-tab-pane label="报名审核" name="review">
        <el-select v-model="reviewClubId" placeholder="选择社团" style="margin-bottom:16px;" @change="loadPending">
          <el-option v-for="c in myClubs" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
        <el-table :data="pendingApps" v-loading="reviewLoading" stripe>
          <el-table-column prop="studentName" label="姓名" />
          <el-table-column prop="studentIdNo" label="学号" />
          <el-table-column prop="college" label="学院" />
          <el-table-column prop="major" label="专业" />
          <el-table-column prop="phone" label="手机号" />
          <el-table-column prop="reason" label="报名理由" show-overflow-tooltip />
          <el-table-column label="操作" width="240">
            <template #default="{ row }">
              <el-button type="success" size="small" @click="handleReview(row.id, true)">通过</el-button>
              <el-button type="danger" size="small" @click="handleReview(row.id, false)">拒绝</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!reviewLoading && pendingApps.length === 0" description="暂无待审核申请" />
      </el-tab-pane>

      <!-- 成员管理 -->
      <el-tab-pane label="成员管理" name="members">
        <el-select v-model="memberClubId" placeholder="选择社团" style="margin-bottom:16px;" @change="loadMembers">
          <el-option v-for="c in myClubs" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
        <el-button type="success" style="margin-bottom:16px;margin-left:10px;" @click="handleExport">导出成员名单</el-button>
        <el-table :data="members" v-loading="memberLoading" stripe>
          <el-table-column prop="studentName" label="姓名" />
          <el-table-column prop="studentIdNo" label="学号" />
          <el-table-column prop="college" label="学院" />
          <el-table-column prop="major" label="专业" />
          <el-table-column prop="phone" label="手机号" />
          <el-table-column prop="createTime" label="申请时间" />
        </el-table>
      </el-tab-pane>

      <!-- 活动管理 -->
      <el-tab-pane label="活动管理" name="activities">
        <el-select v-model="actClubId" placeholder="选择社团" style="margin-bottom:16px;" @change="loadClubActivities">
          <el-option v-for="c in myClubs" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
        <el-button type="primary" style="margin-bottom:16px;margin-left:10px;" @click="showCreateAct = true"
                   :disabled="!actClubId">发布活动</el-button>
        <el-table :data="clubActivities" v-loading="actLoading" stripe>
          <el-table-column prop="title" label="活动名称" />
          <el-table-column prop="location" label="地点" />
          <el-table-column prop="startTime" label="开始时间" />
          <el-table-column label="参与人数">
            <template #default="{ row }">{{ row.currentParticipants }}/{{ row.maxParticipants || '不限' }}</template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button size="small" @click="viewRegistrations(row)">查看报名</el-button>
              <el-button type="danger" size="small" @click="handleDeleteAct(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

    </el-tabs>

    <!-- 创建社团弹窗 -->
    <el-dialog v-model="showCreateClub" title="创建社团" width="500px">
      <el-form :model="clubForm" label-width="80px">
        <el-form-item label="社团名称"><el-input v-model="clubForm.name" /></el-form-item>
        <el-form-item label="类别">
          <el-select v-model="clubForm.category" style="width:100%;">
            <el-option v-for="c in ['学术','文艺','体育','科技','公益','其他']" :key="c" :label="c" :value="c" />
          </el-select>
        </el-form-item>
        <el-form-item label="介绍"><el-input v-model="clubForm.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="招新要求"><el-input v-model="clubForm.requirement" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="联系人"><el-input v-model="clubForm.contactName" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="clubForm.contactPhone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="clubForm.contactEmail" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateClub = false">取消</el-button>
        <el-button type="primary" @click="handleCreateClub">提交</el-button>
      </template>
    </el-dialog>

    <!-- 发布活动弹窗 -->
    <el-dialog v-model="showCreateAct" title="发布活动" width="500px">
      <el-form :model="actForm" label-width="80px">
        <el-form-item label="活动名称"><el-input v-model="actForm.title" /></el-form-item>
        <el-form-item label="介绍"><el-input v-model="actForm.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="地点"><el-input v-model="actForm.location" /></el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="actForm.startTime" type="datetime" style="width:100%;" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="actForm.endTime" type="datetime" style="width:100%;" /></el-form-item>
        <el-form-item label="人数上限"><el-input-number v-model="actForm.maxParticipants" :min="1" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateAct = false">取消</el-button>
        <el-button type="primary" @click="handleCreateAct">发布</el-button>
      </template>
    </el-dialog>

    <!-- 报名列表弹窗 -->
    <el-dialog v-model="showRegistrations" title="活动报名列表" width="700px">
      <el-table :data="registrations" stripe>
        <el-table-column prop="user.realName" label="姓名" />
        <el-table-column prop="user.studentId" label="学号" />
        <el-table-column prop="user.college" label="学院" />
        <el-table-column label="签到状态">
          <template #default="{ row }">
            <el-tag :type="row.checkedIn ? 'success' : 'info'">{{ row.checkedIn ? '已签到' : '未签到' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button type="success" size="small" @click="handleCheckIn(row)" :disabled="row.checkedIn">签到</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getMyClubs, createClub, getPendingApplications, leaderReview, getClubApplications,
         getClubActivities, createActivity, deleteActivity, getActivityRegistrations, checkIn, exportMembersUrl } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('clubs')
const myClubs = ref([])

// 社团相关
const showCreateClub = ref(false)
const clubForm = ref({ name: '', category: '', description: '', requirement: '', contactName: '', contactPhone: '', contactEmail: '' })

// 审核相关
const reviewClubId = ref(null)
const pendingApps = ref([])
const reviewLoading = ref(false)

// 成员相关
const memberClubId = ref(null)
const members = ref([])
const memberLoading = ref(false)

// 活动相关
const actClubId = ref(null)
const clubActivities = ref([])
const actLoading = ref(false)
const showCreateAct = ref(false)
const actForm = ref({ title: '', description: '', location: '', startTime: '', endTime: '', maxParticipants: 50 })
const showRegistrations = ref(false)
const registrations = ref([])

onMounted(loadMyClubs)

// 切换到审核tab时，自动选中第一个社团并加载待审核申请
watch(activeTab, (val) => {
  if (val === 'review' && myClubs.value.length > 0 && !reviewClubId.value) {
    reviewClubId.value = myClubs.value[0].id
    loadPending()
  }
})

async function loadMyClubs() {
  try {
    const res = await getMyClubs()
    myClubs.value = res.data || []
    // 如果当前在审核tab且有社团，自动选中第一个并加载待审核申请
    if (activeTab.value === 'review' && myClubs.value.length > 0 && !reviewClubId.value) {
      reviewClubId.value = myClubs.value[0].id
      loadPending()
    }
  } catch (e) {}
}

async function handleCreateClub() {
  try {
    await createClub(clubForm.value)
    ElMessage.success('社团创建成功，等待管理员审核')
    showCreateClub.value = false
    loadMyClubs()
  } catch (e) {}
}

async function loadPending() {
  if (!reviewClubId.value) return
  reviewLoading.value = true
  try {
    const res = await getPendingApplications(reviewClubId.value)
    pendingApps.value = res.data || []
  } catch (e) {} finally { reviewLoading.value = false }
}

async function handleReview(id, pass) {
  try {
    const remark = pass ? '同意加入' : '暂不符合要求'
    await leaderReview(id, pass, remark)
    ElMessage.success(pass ? '已通过' : '已拒绝')
    loadPending()
  } catch (e) {}
}

async function loadMembers() {
  if (!memberClubId.value) return
  memberLoading.value = true
  try {
    const res = await getClubApplications(memberClubId.value)
    members.value = (res.data || []).filter(a => a.status === 'ADMIN_PASS')
  } catch (e) {} finally { memberLoading.value = false }
}

function handleExport() {
  if (!memberClubId.value) return
  window.open(exportMembersUrl(memberClubId.value), '_blank')
}

async function loadClubActivities() {
  if (!actClubId.value) return
  actLoading.value = true
  try {
    const res = await getClubActivities(actClubId.value)
    clubActivities.value = res.data || []
  } catch (e) {} finally { actLoading.value = false }
}

async function handleCreateAct() {
  try {
    await createActivity(actClubId.value, actForm.value)
    ElMessage.success('活动发布成功')
    showCreateAct.value = false
    loadClubActivities()
  } catch (e) {}
}

async function handleDeleteAct(id) {
  await ElMessageBox.confirm('确定删除该活动？')
  try {
    await deleteActivity(id)
    ElMessage.success('已删除')
    loadClubActivities()
  } catch (e) {}
}

async function viewRegistrations(act) {
  try {
    const res = await getActivityRegistrations(act.id)
    registrations.value = res.data || []
    showRegistrations.value = true
  } catch (e) {}
}

async function handleCheckIn(reg) {
  try {
    await checkIn(reg.id)
    ElMessage.success('签到成功')
    reg.checkedIn = true
  } catch (e) {}
}
</script>
