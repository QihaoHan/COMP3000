<template>
  <div>
    <div class="search">
      <el-input placeholder="Please enter the title to search" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">Add</el-button>
      <el-button type="danger" plain @click="delBatch">BatchDelete</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="Serial Number" width="120" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="Name"></el-table-column>
        <el-table-column prop="cover" label="Cover">
          <template v-slot="scope">
            <el-image :preview-src-list="[scope.row.cover]" :src="scope.row.cover" style="width: 60px; height: 40px;"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="descr" label="Introduction" show-overflow-tooltip></el-table-column>
        <el-table-column prop="start" label="Start Date"></el-table-column>
        <el-table-column prop="end" label="End Date"></el-table-column>
        <el-table-column label="View Details">
          <template v-slot="scope">
            <el-button @click="preview(scope.row.content)">View Content</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="Address"></el-table-column>
        <el-table-column label="Actions" align="center" width="180">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">Edit</el-button>
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

    <el-dialog name="PublicActivity" :visible.sync="fromVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="250px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" placeholder="Name"></el-input>
        </el-form-item>
        <el-form-item label="Cover" prop="cover">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleCoverSuccess"
          >
            <el-button type="primary">Upload Cover</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Introduction" prop="descr">
          <el-input type="textarea" v-model="form.descr" placeholder="Introduction"></el-input>
        </el-form-item>
        <el-form-item label="Address" prop="address">
          <el-input type="textarea" v-model="form.address" placeholder="Address"></el-input>
        </el-form-item>
        <el-form-item label="Start Date" prop="start">
          <el-date-picker style="width: 100%" value-format="yyyy-MM-dd" format="yyyy-MM-dd" v-model="form.start" placeholder="Start Date"></el-date-picker>
        </el-form-item>
        <el-form-item label="End Date" prop="end">
          <el-date-picker style="width: 100%" value-format="yyyy-MM-dd" format="yyyy-MM-dd" v-model="form.end" placeholder="End Date"></el-date-picker>
        </el-form-item>
        <el-form-item label="Activity Details" prop="content">
          <div id="editor"></div>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Article Content" :visible.sync="fromVisible1" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div class="w-e-text">
        <div v-html="content"></div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible1 = false">Close</el-button>
      </div>
    </el-dialog>

  </div>
</template>




<script>
import E from "wangeditor"

export default {
  name: "Activity",
  data() {
    return {
      tableData: [],  // All data
      pageNum: 1,   // Current page number
      pageSize: 10,  // Number of items per page
      total: 0,
      name: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: 'Please enter the title', trigger: 'blur'},
        ],
        descr: [
          {required: true, message: 'Please enter a brief description', trigger: 'blur'},
        ],
        address: [
          {required: true, message: 'Please enter the address', trigger: 'blur'},
        ],
        start: [
          {required: true, message: 'Please select a start date', trigger: 'blur'},
        ],
        end: [
          {required: true, message: 'Please select an end date', trigger: 'blur'},
        ]
      },
      ids: [],
      content: '',
      fromVisible1: false
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleAdd() {   // Add data
      this.form = {}  // Clear data when adding
      this.fromVisible = true   // Open the dialog
      this.setRichText('')
    },
    handleEdit(row) {   // Edit data
      this.form = JSON.parse(JSON.stringify(row))  // Assign value to form object, deep copy
      this.fromVisible = true   // Open the dialog
      this.setRichText(this.form.content)
    },
    save() {   // Logic triggered by the Save button, for add or update
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.content = this.editor.txt.html()
          this.$request({
            url: this.form.id ? '/activity/update' : '/activity/add',
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
      this.$confirm('Are you sure you want to delete this?', 'Confirm Deletion', {type: "warning"}).then(response => {
        this.$request.delete('/activity/delete/' + id).then(res => {
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
        this.$request.delete('/activity/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/activity/selectPage', {
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
    handleCoverSuccess(res) {
      this.form.cover = res.data
    },
    setRichText(html) {
      this.$nextTick(() => {
        this.editor = new E(`#editor`)
        this.editor.config.uploadImgServer = this.$baseUrl + '/files/editor/upload'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgHeaders = {
          token: this.user.token
        }
        this.editor.config.uploadImgParams = {
          type: 'img',
        }
        this.editor.create()  // Create
        this.editor.txt.html(html)
      })
    },
    preview(content) {
      this.content = content
      this.fromVisible1 = true
    },
  }
}
</script>




<style scoped>

</style>
