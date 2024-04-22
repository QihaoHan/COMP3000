<template>
  <div>
    <div class="search">
      <el-input placeholder="Please enter content keywords to search" style="width: 200px" v-model="content"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="operation">
      <el-button type="danger" plain @click="delBatch">Batch Delete</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="Serial Number" width="120" align="center" sortable></el-table-column>
        <el-table-column prop="content" label="Content"></el-table-column>
        <el-table-column prop="userId" label="Commenter"></el-table-column>
        <el-table-column prop="pid" label="Parent ID"></el-table-column>
        <el-table-column prop="rootId" label="Root ID"></el-table-column>
        <el-table-column prop="time" label="Comment Time"></el-table-column>
        <el-table-column prop="fid" label="Related ID"></el-table-column>
        <el-table-column prop="module" label="Module"></el-table-column>
        <el-table-column label="Actions" align="center" width="180">
          <template v-slot="scope">
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
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
  name: "Comment",
  data() {
    return {
      tableData: [],  // All data
      pageNum: 1,   // Current page number
      pageSize: 10,  // Number of items per page
      total: 0,
      content: null,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    del(id) {   // Delete single item
      this.$confirm('Are you sure you want to delete?', 'Confirm Deletion', {type: "warning"}).then(response => {
        this.$request.delete('/comment/delete/' + id).then(res => {
          if (res.code === '200') {   // Indicates successful operation
            this.$message.success('Successfully deleted')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // Display error message
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // Currently selected rows
      this.ids = rows.map(v => v.id)   // [1,2]
    },
    delBatch() {   // Batch delete
      if (!this.ids.length) {
        this.$message.warning('Please select data')
        return
      }
      this.$confirm('Are you sure you want to delete these items in batch?', 'Confirm Deletion', {type: "warning"}).then(response => {
        this.$request.delete('/comment/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // Indicates successful operation
            this.$message.success('Operation successful')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // Display error message
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // Paginated query
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/comment/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          content: this.content,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.content = null
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
