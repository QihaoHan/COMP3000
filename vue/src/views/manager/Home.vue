<template>
  <div>
    <div class="card" style="padding: 15px">
      Hello, {{ user?.name }}! Welcome to the system.
    </div>

    <!-- Removed the div elements containing the bar and pie charts -->

    <div style="display: flex; margin: 10px 0">
      <div style="width: 50%;" class="card">
        <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold">Announcement List</div>
        <div >
          <el-timeline  reverse slot="reference">
            <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time">
              <el-popover
                  placement="right"
                  width="200"
                  trigger="hover"
                  :content="item.content">
                <span slot="reference">{{ item.title }}</span>
              </el-popover>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import * as echarts from 'echarts'

const barOption = {
  title: {
    text: 'Statistics of Registration Numbers for Various Activities\n',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLabel: {
      show: true,
      interval: 0,
      rotate: -60,
      inside: false,
      margin: 6,
    },
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'bar',
      itemStyle: {
        normal: {
          color: function(params) {
            let colorList = ['#bcea12','#1041e1','#fa4c4c','#05f119','#FFDB5C','#ff9f7f','#fb7293','#E062AE','#E690D1','#e7bcf3']
            return colorList[params.dataIndex];
          }
        }
      },
    }
  ],
}

const pieOption = {
  title: {
    text: 'Statistics of Service Reservations by Type\n',
    subtext: 'scale drawing',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      type: 'pie',
      center: ['50%', '60%'],
      radius: '50%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + ' (' + param.percent + '%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}

export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: []
    }
  },
  created() {
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    })
  },
  mounted() {
    let barDom = document.getElementById('bar');
    let barChart = echarts.init(barDom);

    this.$request.get('/activitySign/selectCount').then(res => {
      barOption.xAxis.data =  res.data?.map(v => v.name)
      barOption.series[0].data =  res.data?.map(v => v.value)
      barChart.setOption(barOption)
    })

    let pieDom = document.getElementById('pie');
    let pieChart = echarts.init(pieDom);

    this.$request.get('/reserve/selectCount').then(res => {
      pieOption.series[0].data = res.data || []
      pieChart.setOption(pieOption)
    })

  }
}
</script>
