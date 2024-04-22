<template>
  <div class="main-content">

    <div style="font-size: 20px; margin-bottom: 20px">My Booked Services</div>

    <div class="table">
      <el-table size="medium" :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column label="Service" prop="serveName"></el-table-column>
        <el-table-column prop="time" label="Appointment Time"></el-table-column>
        <el-table-column prop="status" label="Approval Status">
          <template v-slot="scope">
            <el-tag type="primary" v-if="scope.row.status === '待审核'">Pending</el-tag>
            <el-tag type="success" v-if="scope.row.status === '审核通过'">Approved</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '审核拒绝'">Rejected</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="Review Comments"></el-table-column>
        <el-table-column label="Actions" align="center" width="180">
          <template v-slot="scope">
            <el-button v-if="scope.row.status !== '审核通过'" size="mini" type="danger" plain @click="del(scope.row.id)">Cancel Booking</el-button>
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
  name: "FrontReserve",
  data() {
    return {
      tableData: [],  // All data
      pageNum: 1,   // Current page number
      pageSize: 10,  // Number of items per page
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
    del(id) {   // Delete single
      this.$confirm('Are you sure you want to cancel?', 'Confirm Cancellation', {type: "warning"}).then(response => {
        this.$request.delete('/reserve/delete/' + id).then(res => {
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
    handleSelectionChange(rows) {   // Current selected row data
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // Delete batch
      if (!this.ids.length) {
        this.$message.warning('Please select data')
        return
      }
      this.$confirm('Are you sure you want to delete these data?', 'Confirm Deletion', {type: "warning"}).then(response => {
        this.$request.delete('/reserve/delete/batch', {data: this.ids}).then(res => {
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
    load(pageNum) {  // Paging query
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/reserve/selectPage', {
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
