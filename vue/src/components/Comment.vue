<template>
  <div>
    <div class="card" style="margin: 10px 0">
      <div style="font-size: 20px; font-weight: bold; margin-bottom: 15px">Comments {{ commentCount }}</div>
      <div style="margin-bottom: 20px">
        <el-input type="textarea" placeholder="Please enter your comment" v-model="form.content"></el-input>
        <div style="margin-top: 5px; text-align: right">
          <el-button type="primary" @click="addComment(null)">Comment</el-button>
        </div>
      </div>

      <div>
        <div v-for="item in commentList" :key="item.id" style="display: flex; grid-gap: 15px; margin-bottom: 20px">
          <img :src="item.avatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
          <div style="flex: 1;">
            <div style="margin-bottom: 5px; color: #666">{{ item.userName }}</div>
            <div style="margin-bottom: 5px">{{ item.content }}</div>
            <div style="font-size: 13px; color: #666; margin-bottom: 10px">
              <span>{{ item.time }}</span>
              <span style="margin: 0 20px; cursor: pointer" @click="handleReply(item)" :class="{ 'highLight' : item.showReply }">
                <i class="el-icon-s-comment"></i>Reply
              </span>
              <span style="cursor: pointer" @click="del(item.id)" v-if="user.id === item.userId">
                <i class="el-icon-delete"></i>Delete
              </span>
            </div>
            <div style="margin-bottom: 10px" v-if="item.showReply">
              <el-input type="textarea" v-model="item.reply" placeholder="Please enter your reply"></el-input>
              <div style="margin-top: 5px; text-align: right">
                <el-button type="primary" size="mini" @click="addComment(item)">Reply</el-button>
              </div>
            </div>

            <div v-if="item.children.length">
              <div v-for="sub in item.children" :key="sub.id" style="display: flex; grid-gap: 15px; margin-bottom: 10px">
                <img :src="sub.avatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
                <div style="flex: 1;">
                  <div style="margin-bottom: 5px; color: #666">{{ sub.userName }}  <span v-if="sub.replyUser && sub.replyUser !== item.userName">Reply to {{ sub.replyUser }}</span></div>
                  <div style="margin-bottom: 5px">{{ sub.content }}</div>
                  <div style="font-size: 13px; color: #666; margin-bottom: 10px">
                    <span>{{ sub.time }}</span>
                    <span style="margin: 0 20px; cursor: pointer" @click="handleReply(sub)" :class="{ 'highLight' : sub.showReply }">
                      <i class="el-icon-s-comment"></i>Reply
                    </span>
                    <span style="cursor: pointer" @click="del(sub.id)" v-if="user.id === sub.userId">
                      <i class="el-icon-delete"></i>Delete
                    </span>
                  </div>
                  <div style="margin-bottom: 10px" v-if="sub.showReply">
                    <el-input type="textarea" v-model="sub.reply" placeholder="Please enter your reply"></el-input>
                    <div style="margin-top: 5px; text-align: right">
                      <el-button type="primary" size="mini" @click="addComment(sub)">Reply</el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CommentComponent",
  props: {
    fid: null,
    module: null
  },
  data() {
    return {
      commentCount: 0,
      form: {},
      commentList: [],
      user: JSON.parse(localStorage.getItem('xm-user') || '{}')
    }
  },
  created() {
    this.loadComment()
  },
  methods: {
    del(id) {   // Delete a single comment
      this.$confirm('Are you sure you want to delete?', 'Confirm deletion', {type: "warning"}).then(response => {
        this.$request.delete('/comment/deepDelete/' + id).then(res => {
          if (res.code === '200') {   // Indicates success
            this.$message.success('Successfully deleted')
            this.loadComment()
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    handleReply(comment) {
      this.$set(comment, 'showReply', !comment.showReply)
    },
    addComment(pComment) {
      this.form.module = this.module
      this.form.fid = this.fid
      this.form.userId = this.user.id
      if (pComment) {   // Indicates a reply
        this.form.pid = pComment.id
        this.form.content = pComment.reply
      }
      this.$request.post('/comment/add', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('Operation successful')
          this.form = {}
          this.loadComment()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadComment() {
      this.$request.get('/comment/selectTree/' + this.fid + '/' + this.module).then(res => {
        this.commentList = res.data || []
      })

      this.$request.get('/comment/selectCount/' + this.fid + '/' + this.module).then(res => {
        this.commentCount = res.data || 0
      })
    },
  }
}
</script>

<style scoped>
.highLight {
  color: #2A60C9;
}
</style>
