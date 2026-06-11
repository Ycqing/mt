import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getReportList, getReportDetail, generateReport, deleteReport } from '@/api/report'

export const useReportStore = defineStore('report', () => {
  const reportList = ref([])
  const currentReport = ref(null)
  const loading = ref(false)

  async function fetchReportList(params) {
    loading.value = true
    try {
      reportList.value = await getReportList(params)
    } finally {
      loading.value = false
    }
  }

  async function fetchReport(id) {
    loading.value = true
    try {
      currentReport.value = await getReportDetail(id)
    } finally {
      loading.value = false
    }
  }

  async function createReport(data) {
    return await generateReport(data)
  }

  async function removeReport(id) {
    await deleteReport(id)
  }

  return {
    reportList,
    currentReport,
    loading,
    fetchReportList,
    fetchReport,
    createReport,
    removeReport
  }
})
