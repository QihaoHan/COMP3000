<template>
  <div class="main-content">

    <div style="font-size: 20px; margin-bottom: 20px">My Registered Activities</div>

    <div class="table">
      <el-table size="medium" :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column label="Activity" width="300" show-overflow-tooltip>
          <template v-slot="scope">
            <a :href="'/front/activityDetail?id=' + scope.row.activityId" target="_blank">{{ scope.row.activityName }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="Contact"></el-table-column>
        <el-table-column prop="date" label="Registration Date"></el-table-column>
        <el-table-column prop="status" label="Review Status">
          <template v-slot="scope">
            <el-tag type="primary" v-if="scope.row.status === 'Pending'">Pending</el-tag>
            <el-tag type="success" v-if="scope.row.status === 'Approved'">Approved</el-tag>
            <el-tag type="danger" v-if="scope.row.status === 'Rejected'">Rejected</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="Review Comments"></el-table-column>
        <el-table-column label="Actions" align="center" width="180">
          <template v-slot="scope">
            <el-button v-if="scope.row.status !== 'Approved'" size="mini" type="danger" plain @click="del(scope.row.id)">Cancel Registration</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin: 10px 0">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "FrontActivitySign",
  data() {
    return {
      tableData: [],  // All data
      pageNum: 1,   // Current page number
      pageSize: 10,  // Items per page
      total: 0,
      userName: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    del(id) {   // Delete single entry
      this.$confirm('Are you sure you want to cancel?', 'Confirm Cancellation', {type: "warning"}).then(response => {
        this.$request.delete('/activitySign/delete/' + id).then(res => {
          if (res.code === '200') {   // Operation successful
            this.$message.success('Operation successful')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // Display error message
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // Data of all selected rows
      this.ids = rows.map(v => v.id)
    },
    delBatch() {   // Batch delete
      if (!this.ids.length) {
        this.$message.warning('Please select data')
        return
      }
      this.$confirm('Are you sure you want to delete these records?', 'Confirm Deletion', {type: "warning"}).then(response => {
        this.$request.delete('/activitySign/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // Operation successful
            this.$message.success('Operation successful')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // Display error message
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // Pagination query
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/activitySign/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userName: this.userName,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.userName = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>
