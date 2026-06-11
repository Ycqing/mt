<template>
  <div class="merchant-form">
    <div class="page-header">
      <h2 class="page-title">{{ isEdit ? '编辑商家' : '添加商家' }}</h2>
      <el-button @click="$router.push('/merchant/list')">
        <el-icon><ArrowLeft /></el-icon> 返回列表
      </el-button>
    </div>

    <div class="card-container">
      <el-steps :active="stepActive" finish-status="success" align-center class="step-bar">
        <el-step title="基础信息" />
        <el-step title="社交账号" />
        <el-step title="确认提交" />
      </el-steps>

      <div class="form-content">
        <!-- Step 1: 基础信息 -->
        <div v-show="stepActive === 0">
          <el-form ref="basicFormRef" :model="basicForm" :rules="basicRules" label-width="120px">
            <el-form-item label="商家名称" prop="name">
              <el-input v-model="basicForm.name" placeholder="请输入商家名称" maxlength="100" />
            </el-form-item>
            <el-form-item label="官网地址" prop="website">
              <el-input v-model="basicForm.website" placeholder="请输入官网地址，如 https://www.example.com" />
            </el-form-item>
            <el-form-item label="所属行业" prop="industryId">
              <el-select v-model="basicForm.industryId" placeholder="请选择行业" style="width: 100%;">
                <el-option v-for="cat in industryList" :key="cat.id" :label="cat.name" :value="cat.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="是否上市" prop="isListed">
              <el-radio-group v-model="basicForm.isListed">
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
            <template v-if="basicForm.isListed === 1">
              <el-form-item label="上市交易所">
                <el-select v-model="basicForm.listingExchange" placeholder="请选择交易所" style="width: 100%;">
                  <el-option label="上交所" value="上交所" />
                  <el-option label="深交所" value="深交所" />
                  <el-option label="纽交所" value="纽交所" />
                  <el-option label="纳斯达克" value="纳斯达克" />
                  <el-option label="港交所" value="港交所" />
                </el-select>
              </el-form-item>
              <el-form-item label="股票代码">
                <el-input v-model="basicForm.stockCode" placeholder="请输入股票代码" />
              </el-form-item>
            </template>
            <el-form-item label="企业规模">
              <el-select v-model="basicForm.companyScale" placeholder="请选择企业规模" style="width: 100%;">
                <el-option label="50人以下" value="50人以下" />
                <el-option label="50-99人" value="50-99人" />
                <el-option label="100-499人" value="100-499人" />
                <el-option label="500-999人" value="500-999人" />
                <el-option label="1000-9999人" value="1000-9999人" />
                <el-option label="10000人以上" value="10000人以上" />
              </el-select>
            </el-form-item>
            <el-form-item label="员工人数">
              <el-input-number v-model="basicForm.employeeCount" :min="1" style="width: 100%;" />
            </el-form-item>
            <el-form-item label="成立日期">
              <el-date-picker v-model="basicForm.establishedDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width: 100%;" />
            </el-form-item>
            <el-form-item label="注册资本(万)">
              <el-input-number v-model="basicForm.registeredCapital" :precision="2" :min="0" style="width: 100%;" />
            </el-form-item>
            <el-form-item label="总部地址">
              <el-input v-model="basicForm.headquarters" placeholder="请输入总部地址" />
            </el-form-item>
            <el-form-item label="商家介绍">
              <el-input v-model="basicForm.businessDescription" type="textarea" :rows="4" placeholder="请输入商家介绍" />
            </el-form-item>
            <el-form-item label="核心优势">
              <el-input v-model="basicForm.advantages" type="textarea" :rows="3" placeholder="请输入核心优势，多个优势用换行分隔" />
            </el-form-item>
            <el-form-item label="产品信息">
              <el-input v-model="basicForm.productsInfo" type="textarea" :rows="3" placeholder="请输入主要产品信息" />
            </el-form-item>
          </el-form>
        </div>

        <!-- Step 2: 社交账号 -->
        <div v-show="stepActive === 1">
          <div class="social-accounts">
            <div v-for="(account, index) in socialAccounts" :key="index" class="account-item">
              <el-divider content-position="left">{{ getPlatformName(account.platform) }}</el-divider>
              <el-form label-width="100px">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="平台">
                      <el-select v-model="account.platform" placeholder="选择平台">
                        <el-option label="抖音" value="douyin" />
                        <el-option label="小红书" value="xiaohongshu" />
                        <el-option label="微博" value="weibo" />
                        <el-option label="微信" value="weixin" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="账号名称">
                      <el-input v-model="account.accountName" placeholder="账号名称" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="24">
                    <el-form-item label="账号主页">
                      <el-input v-model="account.accountUrl" placeholder="账号主页URL" />
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
              <el-button type="danger" link @click="removeAccount(index)">删除</el-button>
            </div>
            <el-button type="dashed" style="width: 100%; margin-top: 16px;" @click="addAccount">
              <el-icon><Plus /></el-icon> 添加账号
            </el-button>
          </div>
        </div>

        <!-- Step 3: 确认提交 -->
        <div v-show="stepActive === 2">
          <el-alert title="确认商家信息" type="info" :closable="false" style="margin-bottom: 20px;" />
          <el-descriptions :column="2" border>
            <el-descriptions-item label="商家名称">{{ basicForm.name }}</el-descriptions-item>
            <el-descriptions-item label="官网">{{ basicForm.website || '-' }}</el-descriptions-item>
            <el-descriptions-item label="所属行业">{{ getIndustryName(basicForm.industryId) }}</el-descriptions-item>
            <el-descriptions-item label="是否上市">{{ basicForm.isListed === 1 ? '是' : '否' }}</el-descriptions-item>
            <el-descriptions-item label="企业规模">{{ basicForm.companyScale || '-' }}</el-descriptions-item>
            <el-descriptions-item label="总部地址">{{ basicForm.headquarters || '-' }}</el-descriptions-item>
            <el-descriptions-item label="社交账号" :span="2">
              {{ socialAccounts.filter(a => a.platform && a.accountName).map(a => getPlatformName(a.platform) + ':' + a.accountName).join('；') || '无' }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>

      <div class="form-footer">
        <el-button v-if="stepActive > 0" @click="stepActive--">上一步</el-button>
        <el-button v-if="stepActive < 2" type="primary" @click="nextStep">下一步</el-button>
        <el-button v-if="stepActive === 2" type="primary" :loading="submitting" @click="handleSubmit">
          {{ isEdit ? '确认修改' : '确认添加' }}
        </el-button>
        <el-button v-if="stepActive === 0" @click="saveDraft">保存草稿</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useMerchantStore } from '@/stores/merchant'
import { addSocialAccount } from '@/api/socialAccount'

const route = useRoute()
const router = useRouter()
const merchantStore = useMerchantStore()

const isEdit = computed(() => !!route.params.id)
const basicFormRef = ref(null)
const stepActive = ref(0)
const submitting = ref(false)

const basicForm = reactive({
  name: '',
  website: '',
  industryId: null,
  isListed: 0,
  listingExchange: '',
  stockCode: '',
  companyScale: '',
  employeeCount: null,
  establishedDate: '',
  registeredCapital: null,
  headquarters: '',
  businessDescription: '',
  advantages: '',
  productsInfo: ''
})

const socialAccounts = ref([
  { platform: 'douyin', accountName: '', accountUrl: '' }
])

const industryList = ref([
  { id: 1, name: '美妆护肤' },
  { id: 2, name: '服装鞋包' },
  { id: 3, name: '食品饮料' },
  { id: 4, name: '数码家电' },
  { id: 5, name: '家居生活' },
  { id: 6, name: '母婴用品' }
])

const basicRules = {
  name: [{ required: true, message: '请输入商家名称', trigger: 'blur' }],
  industryId: [{ required: true, message: '请选择所属行业', trigger: 'change' }]
}

function getPlatformName(code) {
  const map = { douyin: '抖音', xiaohongshu: '小红书', weibo: '微博', weixin: '微信' }
  return map[code] || code
}

function getIndustryName(id) {
  const cat = industryList.value.find(c => c.id === id)
  return cat ? cat.name : '-'
}

function addAccount() {
  socialAccounts.value.push({ platform: 'douyin', accountName: '', accountUrl: '' })
}

function removeAccount(index) {
  socialAccounts.value.splice(index, 1)
}

function nextStep() {
  if (stepActive.value === 0) {
    basicFormRef.value.validate(valid => {
      if (valid) stepActive.value++
    })
  } else {
    stepActive.value++
  }
}

async function handleSubmit() {
  submitting.value = true
  try {
    let merchantId
    if (isEdit.value) {
      await merchantStore.editMerchant(route.params.id, basicForm)
      merchantId = route.params.id
    } else {
      merchantId = await merchantStore.createMerchant(basicForm)
    }

    for (const account of socialAccounts.value) {
      if (account.platform && account.accountName) {
        await addSocialAccount(merchantId, account)
      }
    }

    ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
    router.push('/merchant/list')
  } catch (e) {
    // error handled by interceptor
  } finally {
    submitting.value = false
  }
}

function saveDraft() {
  ElMessage.info('草稿保存功能开发中')
}

onMounted(async () => {
  if (isEdit.value) {
    await merchantStore.fetchMerchantDetail(route.params.id)
    const data = merchantStore.currentMerchant
    if (data) {
      Object.assign(basicForm, {
        name: data.name,
        website: data.website,
        industryId: data.industryId,
        isListed: data.isListed,
        listingExchange: data.listingExchange,
        stockCode: data.stockCode,
        companyScale: data.companyScale,
        employeeCount: data.employeeCount,
        establishedDate: data.establishedDate,
        registeredCapital: data.registeredCapital,
        headquarters: data.headquarters,
        businessDescription: data.businessDescription,
        advantages: data.advantages,
        productsInfo: data.productsInfo
      })
    }
  }
})
</script>

<style scoped>
.merchant-form {
  max-width: 900px;
}

.step-bar {
  margin-bottom: 40px;
}

.form-content {
  min-height: 400px;
}

.form-footer {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.account-item {
  background: #fafafa;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  position: relative;
}
</style>
