<template>
  <div>
    <div class="search">
      <el-input placeholder="Please enter name to search" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">Add New</el-button>
      <el-button type="danger" plain @click="delBatch">Batch Delete</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="Serial Number" width="120" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="Name" show-overflow-tooltip></el-table-column>

        <el-table-column label="Actions" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">Edit</el-button>
            <el-button plain type="danger" size="mini" @click="del(scope.row.id)">Delete</el-button>
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

    <el-dialog title="Information" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="250px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="name" label="Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Category",
  data() {
    return {
      tableData: [],  // All data
      pageNum: 1,   // Current page number
      pageSize: 10,  // Items per page
      total: 0,
      name: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: 'Please enter category name', trigger: 'blur'},
        ]
      },
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleAdd() {   // Add new data
      this.form = {}  // Clear form data when adding new
      this.fromVisible = true   // Open the dialog
    },
    handleEdit(row) {   // Edit data
      this.form = JSON.parse(JSON.stringify(row))  // Assign value to form object, ensure deep copy
      this.fromVisible = true   // Open the dialog
    },
    save() {   // Save logic triggered by the Save button, for add or update
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/category/update' : '/category/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // Indicates successful saving
              this.$message.success('Saved successfully')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // Display error message
            }
          })
        }
      })
    },
    del(id) {   // Delete single item
      this.$confirm('Are you sure you want to delete?', 'Confirm Deletion', {type: "warning"}).then(response => {
        this.$request.delete('/category/delete/' + id).then(res => {
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
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // Batch delete
      if (!this.ids.length) {
        this.$message.warning('Please select data')
        return
      }
      this.$confirm('Are you sure you want to delete these items in batch?', 'Confirm Deletion', {type: "warning"}).then(response => {
        this.$request.delete('/category/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/category/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
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
