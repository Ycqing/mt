<template>
  <div class="review-fetch">
    <div class="page-header">
      <h2 class="page-title">产品评价采集</h2>
    </div>
    <div class="card-container">
      <el-form label-width="120px" style="max-width: 600px;">
        <el-form-item label="关键词">
          <el-input v-model="keyword" placeholder="请输入产品关键词" />
        </el-form-item>
        <el-form-item label="评价平台">
          <el-checkbox-group v-model="platforms">
            <el-checkbox label="douyin">抖音</el-checkbox>
            <el-checkbox label="xiaohongshu">小红书</el-checkbox>
            <el-checkbox label="taobao">淘宝</el-checkbox>
            <el-checkbox label="jd">京东</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="采集数量">
          <el-input-number v-model="limit" :min="10" :max="500" :step="10" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleFetch">
            <el-icon><Search /></el-icon> 开始采集
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchReviews } from '@/api/fetchTask'

const keyword = ref('')
const platforms = ref(['douyin'])
const limit = ref(100)
const loading = ref(false)

async function handleFetch() {
  if (!keyword.value) {
    ElMessage.warning('请输入关键词')
    return
  }
  loading.value = true
  try {
    for (const p of platforms.value) {
      await fetchReviews({ keyword: keyword.value, platform: p, pageNum: 1 })
    }
    ElMessage.success('评价采集任务已创建')
  } finally {
    loading.value = false
  }
}
</script>
