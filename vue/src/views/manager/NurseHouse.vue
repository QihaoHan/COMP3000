<template>
  <div>
    <div class="search">
      <el-input placeholder="Please enter a name to search" style="width: 200px" v-model="name"></el-input>
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
        <el-table-column prop="name" label="Name"></el-table-column>
        <el-table-column prop="price" label="Price"></el-table-column>
        <el-table-column prop="img" label="Image">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px; border-radius: 5px" v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="Address" show-overflow-tooltip></el-table-column>
        <el-table-column prop="count" label="Views"></el-table-column>
        <el-table-column prop="tags" label="Tags" show-overflow-tooltip></el-table-column>
        <el-table-column prop="beds" label="Number of Beds"></el-table-column>
        <el-table-column prop="nature" label="Nature"></el-table-column>
        <el-table-column prop="type" label="Type"></el-table-column>
        <el-table-column label="View Details" width="100">
          <template v-slot="scope">
            <el-button @click="preview(scope.row.content)">View Content</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="area" label="Area"></el-table-column>
        <el-table-column prop="date" label="Establishment Date"></el-table-column>
        <el-table-column prop="phone" label="Contact"></el-table-column>
<!--        <el-table-column prop="longitude" label="Longitude"></el-table-column>-->
<!--        <el-table-column prop="latitude" label="Latitude"></el-table-column>-->
        <el-table-column label="Actions" align="center" width="180" fixed="right">
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

    <el-dialog title="Information" :visible.sync="fromVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="300px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" placeholder="Name"></el-input>
        </el-form-item>
        <el-form-item label="Price" prop="price">
          <el-input v-model="form.price" placeholder="Price"></el-input>
        </el-form-item>
        <el-form-item label="Image" prop="img">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleImgSuccess"
          >
            <el-button type="primary">Upload Image</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Address" prop="address">
          <el-input v-model="form.address" placeholder="Address"></el-input>
        </el-form-item>
        <el-form-item label="Tags" prop="tags">
          <el-select allow-create filterable multiple v-model="tags" style="width: 100%">
            <el-option v-for="(item, index) in []" :key="index" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Number of Beds" prop="beds">
          <el-input v-model="form.beds" placeholder="Number of Beds"></el-input>
        </el-form-item>
        <el-form-item label="Nature" prop="nature">
          <el-input v-model="form.nature" placeholder="Nature"></el-input>
        </el-form-item>
        <el-form-item label="Type" prop="type">
          <el-input v-model="form.type" placeholder="Type"></el-input>
        </el-form-item>
        <el-form-item label="Area" prop="area">
          <el-input v-model="form.area" placeholder="Area"></el-input>
        </el-form-item>
        <el-form-item label="Establishment Date" prop="date">
          <el-input v-model="form.date" placeholder="Establishment Date"></el-input>
        </el-form-item>
        <el-form-item label="Contact" prop="phone">
          <el-input v-model="form.phone" placeholder="Contact"></el-input>
        </el-form-item>
<!--        <el-form-item label="Longitude" prop="longitude">-->
<!--          <el-input v-model="form.longitude" placeholder="Longitude" style="width: 80%"></el-input>-->
<!--          <a style="margin-left: 10px" target="_blank" href="https://api.map.baidu.com/lbsapi/getpoint/index.html">Coordinate Picker</a>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="Latitude" prop="latitude">-->
<!--          <el-input v-model="form.latitude" placeholder="Latitude"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="Details" prop="content">
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
  name: "NurseHouse",
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
        price: [
          {required: true, message: 'Please enter the price', trigger: 'blur'},
        ],
        address: [
          {required: true, message: 'Please enter the address', trigger: 'blur'},
        ],
        phone: [
          {required: true, message: 'Please enter the phone number', trigger: 'blur'},
        ],
        longitude: [
          {required: true, message: 'Please enter the longitude', trigger: 'blur'},
        ],
        latitude: [
          {required: true, message: 'Please enter the latitude', trigger: 'blur'},
        ]
      },
      ids: [],
      tags: [],
      content: '',
      fromVisible1: false
    }
  },
  created() {
    this.load(1);
  },
  methods: {
    preview(content) {
      this.content = content;
      this.fromVisible1 = true;
    },
    handleAdd() {   // Add new data
      this.form = {};  // Clear data when adding new
      this.fromVisible = true;   // Open the dialog
      this.setRichText('');
    },
    handleEdit(row) {   // Edit data
      this.form = JSON.parse(JSON.stringify(row));  // Assign value to form object, ensure deep copy
      this.fromVisible = true;   // Open the dialog
      this.setRichText(this.form.content);
    },
    save() {   // Save logic triggered by the Save button, for add or update
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.tags = JSON.stringify(this.tags);   // Convert array to JSON string for storage
          this.form.content = this.editor.txt.html();
          this.$request({
            url: this.form.id ? '/nurseHouse/update' : '/nurseHouse/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // Indicates successful saving
              this.$message.success('Saved successfully');
              this.load(1);
              this.fromVisible = false;
            } else {
              this.$message.error(res.msg);  // Display error message
            }
          });
        }
      });
    },
    del(id) {   // Delete single item
      this.$confirm('Are you sure you want to delete?', 'Confirm Deletion', {type: "warning"}).then(response => {
        this.$request.delete('/nurseHouse/delete/' + id).then(res => {
          if (res.code === '200') {   // Indicates successful operation
            this.$message.success('Successfully deleted');
            this.load(1);
          } else {
            this.$message.error(res.msg);  // Display error message
          }
        });
      }).catch(() => {
      });
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
        this.$request.delete('/nurseHouse/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/nurseHouse/selectPage', {
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
    handleImgSuccess(res) {
      this.form.img = res.data;
    },
    setRichText(html) {
      this.$nextTick(() => {
        this.editor = new E(`#editor`);
        this.editor.config.uploadImgServer = this.$baseUrl + '/files/editor/upload';
        this.editor.config.uploadFileName = 'file';
        this.editor.config.uploadImgHeaders = {
          token: this.user.token
        };
        this.editor.config.uploadImgParams = {
          type: 'img',
        };
        this.editor.create();  // Create
        this.editor.txt.html(html);
      });
    },
  }
}
</script>

<style scoped>

</style>
