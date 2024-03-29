<template>
  <div>
    <a-form-model @submit.prevent="handleQuerySubmit" layout="inline" >
      <a-form-model-item label='到店时间：' class="date-start">
        <a-date-picker v-model="appointRecordListForm.startArrivedDate" style="width: 120px"></a-date-picker>
      </a-form-model-item>
      <a-form-model-item label='-' :colon="false">
        <a-date-picker v-model="appointRecordListForm.endArrivedDate" style="width: 120px"></a-date-picker>
      </a-form-model-item>
      <a-form-model-item label='用户名称'>
        <a-input v-model="appointRecordListForm.realName" style="width: 120px"/>
      </a-form-model-item>
      <a-form-model-item label='用户手机'>
        <a-input v-model="appointRecordListForm.phoneNumber" style="width: 120px"/>
      </a-form-model-item>
      <a-form-model-item label='店面名称'>
        <a-input v-model="appointRecordListForm.shopName" style="width: 120px"/>
      </a-form-model-item>
      <a-form-model-item label='预约状态'>
        <a-select v-model="appointRecordListForm.appointState" style="width: 80px" >
          <a-select-option value="">
            全部
          </a-select-option>
          <a-select-option :value="0">
            预约中
          </a-select-option>
          <a-select-option :value="1">
            预约完成
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item>
        <a-button type='primary' htmlType="submit" >
          <a-icon type="search"/>
          查询
        </a-button>
      </a-form-model-item>
    </a-form-model>

    <div class="actions">
      <router-link to="">
        <a-button type="primary">
          <a-icon type="plus-circle"/>
          导出表格
        </a-button>
      </router-link>
    </div>

    <a-table
      :columns="columns"
      rowKey="id"
      :dataSource="data"
      :pagination="pagination"
      :loading="loading"
      bordered
      @change="handleTableChange">
      <template slot="appointDate" slot-scope="text,record">
        {{record.appointDate | filterDate('YYYY-MM-DD')}} / {{record.timePeriod===0?'上午':'下午'}}
      </template>
      <template slot="arrivedDate" slot-scope="text,record">
        {{record.arrivedDate | filterDate('YYYY-MM-DD HH:mm:ss')}}
      </template>
      <template slot="appointState" slot-scope="appointState">
        <a-tag color="green" v-if="appointState===0">预约中</a-tag>
        <a-tag color="red" v-if="appointState===1">已完成</a-tag>
      </template>
      <template slot="operation" slot-scope="record">
        <a-popconfirm
          title="确定预约已完成？"
          ok-text="是"
          cancel-text="否"
          @confirm="appointCompleted(record.id)"
        >
          <a-button type="primary" v-if="record.appointState===0">完成</a-button>
        </a-popconfirm>
        <a-button type="primary" v-if="record.appointState===1" disabled>已完成</a-button>
      </template>
    </a-table>
  </div>
</template>

<script>
import {
  listAppointRecord,
  appointCompleted,
} from "../../api/backstage"
import moment from "moment"

const columns = [
  {
    title: '预约时间',
    dataIndex: 'appointDate',
    scopedSlots: {customRender: 'appointDate'}
  },
  {
    title: '到店时间',
    dataIndex: 'arrivedDate',
    scopedSlots: {customRender: 'arrivedDate'}
  },
  {
    title: '订单总次数',
    dataIndex: 'totalNum',
  },
  {
    title: '订单使用次数',
    dataIndex: 'appointNum',
  },
  {
    title: '产品名称',
    dataIndex: 'productName',
  },
  {
    title: '门店名称',
    dataIndex: 'shopName',
  },
  {
    title: '用户名称',
    dataIndex: 'realName',
  },
  {
    title: '用户手机',
    dataIndex: 'phoneNumber',
  },
  {
    title: '状态',
    dataIndex: 'appointState',
    scopedSlots: {customRender: 'appointState'},
  },
  {
    title: '操作',
    scopedSlots: {customRender: 'operation'},
  }
]

export default {
  data() {
    return {
      columns,
      data: [],
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
        showTotal(total) {
          return `共 ${total} 项`
        }
      },
      loading: false,
      filters: {},
      labelCol: { span: 5 },
      wrapperCol: { span: 10 },
      appointRecordListForm: {
        sortField: 'createdDate',
        sortOrder: 'descend',
      },
    }
  },
  created() {
    this.fetch()
  },
  methods: {
    appointCompleted(id){
      appointCompleted({id:id})
        .then(() => {
          this.fetch()
          this.$message.success('预约已完成')
        }).catch(({message}) => {
          this.$message.error(message)
      })
    },
    handleQuerySubmit() {
      this.pagination = Object.assign({}, this.pagination, {
        current: 1
      })
      this.fetch()
    },
    handleTableChange(pagination, filters, sorter) {
      this.pagination.current = pagination.current
      this.sortField = sorter.field
      this.sortOrder = sorter.order
      this.filters = filters
      this.fetch()
    },
    fetch() {
      const {startArrivedDate,endArrivedDate} = this.appointRecordListForm
      this.appointRecordListForm = Object.assign({}, this.appointRecordListForm, {
        page: this.pagination.current,
        startArrivedDate: startArrivedDate && moment(startArrivedDate).format('YYYY-MM-DD 00:00:00'),
        endArrivedDate: endArrivedDate && moment(endArrivedDate).format('YYYY-MM-DD 23:59:59')
      })
      this.loading = true
      listAppointRecord(this.appointRecordListForm).then(({data, rows}) => {
        this.data = data
        this.pagination.total = rows
      }).catch(({message}) => {
        this.$message.error(message)
      }).then(() => {
        this.loading = false
      })
    }
  }
}
</script>
