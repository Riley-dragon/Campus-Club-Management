<template>
  <div class="home">
    <div class="banner">
      <h1>校园社团数字化管理与招新服务平台</h1>
      <p>发现你的兴趣，加入你喜欢的社团</p>
    </div>
    <div class="search-bar">
      <el-input v-model="keyword" placeholder="搜索社团名称..." prefix-icon="Search" clearable
                style="width:300px;" @clear="loadClubs" @keyup.enter="loadClubs" />
      <el-select v-model="category" placeholder="社团类别" clearable style="width:150px;" @change="loadClubs">
        <el-option label="学术" value="学术" />
        <el-option label="文艺" value="文艺" />
        <el-option label="体育" value="体育" />
        <el-option label="科技" value="科技" />
        <el-option label="公益" value="公益" />
        <el-option label="其他" value="其他" />
      </el-select>
    </div>
    <el-row :gutter="20" v-loading="loading">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="club in clubs" :key="club.id" style="margin-bottom:20px;">
        <el-card shadow="hover" class="club-card" @click="router.push(`/club/${club.id}`)">
          <div class="club-icon">
            <el-icon :size="48" color="#409eff"><Collection /></el-icon>
          </div>
          <h3>{{ club.name }}</h3>
          <el-tag size="small">{{ club.category }}</el-tag>
          <p class="desc">{{ club.description?.substring(0, 80) }}...</p>
          <div class="club-meta">
            <span><el-icon><User /></el-icon> {{ club.contactName }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-empty v-if="!loading && clubs.length === 0" description="暂无社团数据" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getClubs } from '../api'

const router = useRouter()
const loading = ref(false)
const clubs = ref([])
const keyword = ref('')
const category = ref('')

const loadClubs = async () => {
  loading.value = true
  try {
    const params = {}
    if (keyword.value) params.keyword = keyword.value
    if (category.value) params.category = category.value
    const res = await getClubs(params)
    clubs.value = res.data || []
  } catch (e) {} finally { loading.value = false }
}

onMounted(loadClubs)
</script>

<style scoped>
.banner {
  text-align: center; padding: 40px 20px;
  background: linear-gradient(135deg, #409eff, #67c23a);
  border-radius: 12px; color: #fff; margin-bottom: 24px;
}
.banner h1 { font-size: 28px; margin-bottom: 8px; }
.banner p { font-size: 16px; opacity: 0.9; }
.search-bar { display: flex; gap: 12px; margin-bottom: 24px; justify-content: center; }
.club-card { cursor: pointer; transition: transform 0.2s; text-align: center; min-height: 220px; }
.club-card:hover { transform: translateY(-4px); }
.club-icon { margin: 12px 0; }
.club-card h3 { margin: 8px 0; font-size: 18px; }
.desc { color: #909399; font-size: 13px; margin: 10px 0; line-height: 1.5; }
.club-meta { color: #909399; font-size: 13px; display: flex; justify-content: center; gap: 16px; }
</style>
