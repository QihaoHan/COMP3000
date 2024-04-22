<template>
  <div>
    <div class="search">
      <el-input placeholder="Please enter a name to search" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">Add New</el-button>
      <el-button type="danger" plain @click="delBatch">Delete Batch</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="Serial Number" width="120" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="Name"></el-table-column>
        <el-table-column prop="cover" label="Cover">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px; border-radius: 5px" v-if="scope.row.cover"
                        :src="scope.row.cover" :preview-src-list="[scope.row.cover]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="descr" label="Description"></el-table-column>
        <el-table-column prop="date" label="Publication Date"></el-table-column>
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

    <el-dialog title="Information" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
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
        <el-form-item label="Description" prop="descr">
          <el-input type="textarea" v-model="form.descr" placeholder="Description"></el-input>
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
  name: "Serve",
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
          {required: true, message: 'Please enter the name', trigger: 'blur'},
        ],
        descr: [
          {required: true, message: 'Please enter the description', trigger: 'blur'},
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
      this.form = {};  // Clear data when adding new
      this.fromVisible = true;   // Open the dialog
    },
    handleEdit(row) {   // Edit data
      this.form = JSON.parse(JSON.stringify(row));  // Assign value to form object, ensure deep copy
      this.fromVisible = true;   // Open the dialog
    },
    save() {   // Save logic triggered by the Save button, for add or update
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/serve/update' : '/serve/add',
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
        this.$request.delete('/serve/delete/' + id).then(res => {
          if (res.code === '200') {   // Indicates successful operation
            this.$message.success('Operation successful')
            this.load(1);
          } else {
            this.$message.error(res.msg);  // Display error message
          }
        });
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // Currently selected rows
      this.ids = rows.map(v => v.id);
    },
    delBatch() {   // Batch delete
      if (!this.ids.length) {
        this.$message.warning('Please select data');
        return;
      }
      this.$confirm('Are you sure you want to delete these items in batch?', 'Confirm Deletion', {type: "warning"}).then(response => {
        this.$request.delete('/serve/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // Indicates successful operation
            this.$message.success('Operation successful');
            this.load(1);
          } else {
            this.$message.error(res.msg);  // Display error message
          }
        });
      }).catch(() => {
      });
    },
    load(pageNum) {  // Paginated query
      if (pageNum) this.pageNum = pageNum;
      this.$request.get('/serve/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list;
        this.total = res.data?.total;
      });
    },
    reset() {
      this.name = null;
      this.load(1);
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
    handleCoverSuccess(res) {
      this.form.cover = res.data
    }
  }
}
</script>

<style scoped>

</style>
