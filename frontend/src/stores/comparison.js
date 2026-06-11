import { defineStore } from 'pinia'
import { ref } from 'vue'
import { compareMerchants, getCompareResult, getCompareHistory, deleteCompare } from '@/api/analysis'

export const useComparisonStore = defineStore('comparison', () => {
  const comparisonList = ref([])
  const currentComparison = ref(null)
  const loading = ref(false)

  async function createComparison(data) {
    loading.value = true
    try {
      const res = await compareMerchants(data)
      currentComparison.value = res
      return res
    } finally {
      loading.value = false
    }
  }

  async function fetchComparison(id) {
    loading.value = true
    try {
      currentComparison.value = await getCompareResult(id)
    } finally {
      loading.value = false
    }
  }

  async function fetchHistory(pageNum, pageSize) {
    loading.value = true
    try {
      const res = await getCompareHistory({ pageNum, pageSize })
      comparisonList.value = res
    } finally {
      loading.value = false
    }
  }

  async function removeComparison(id) {
    await deleteCompare(id)
  }

  return {
    comparisonList,
    currentComparison,
    loading,
    createComparison,
    fetchComparison,
    fetchHistory,
    removeComparison
  }
})
