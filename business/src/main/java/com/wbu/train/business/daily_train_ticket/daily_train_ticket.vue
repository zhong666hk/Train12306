<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="daily_train_tickets"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="余票信息" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="daily_train_ticket" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="daily_train_ticket.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
      </a-form-item>
      <a-form-item label="车次编号">
        <a-input v-model:value="daily_train_ticket.trainCode" />
      </a-form-item>
      <a-form-item label="出发站">
        <a-input v-model:value="daily_train_ticket.start" />
      </a-form-item>
      <a-form-item label="出发站拼音">
        <a-input v-model:value="daily_train_ticket.startPinyin" />
      </a-form-item>
      <a-form-item label="出发时间">
        <a-time-picker v-model:value="daily_train_ticket.startTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
      </a-form-item>
      <a-form-item label="出发站序">
        <a-input v-model:value="daily_train_ticket.startIndex" />
      </a-form-item>
      <a-form-item label="到达站">
        <a-input v-model:value="daily_train_ticket.end" />
      </a-form-item>
      <a-form-item label="到达站拼音">
        <a-input v-model:value="daily_train_ticket.endPinyin" />
      </a-form-item>
      <a-form-item label="到站时间">
        <a-time-picker v-model:value="daily_train_ticket.endTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
      </a-form-item>
      <a-form-item label="到站站序">
        <a-input v-model:value="daily_train_ticket.endIndex" />
      </a-form-item>
      <a-form-item label="一等座余票">
        <a-input v-model:value="daily_train_ticket.ydz" />
      </a-form-item>
      <a-form-item label="一等座票价">
        <a-input v-model:value="daily_train_ticket.ydzPrice" />
      </a-form-item>
      <a-form-item label="二等座余票">
        <a-input v-model:value="daily_train_ticket.edz" />
      </a-form-item>
      <a-form-item label="二等座票价">
        <a-input v-model:value="daily_train_ticket.edzPrice" />
      </a-form-item>
      <a-form-item label="软卧余票">
        <a-input v-model:value="daily_train_ticket.rw" />
      </a-form-item>
      <a-form-item label="软卧票价">
        <a-input v-model:value="daily_train_ticket.rwPrice" />
      </a-form-item>
      <a-form-item label="硬卧余票">
        <a-input v-model:value="daily_train_ticket.yw" />
      </a-form-item>
      <a-form-item label="硬卧票价">
        <a-input v-model:value="daily_train_ticket.ywPrice" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";

export default defineComponent({
  name: "daily_train_ticket-view",
  setup() {
    const visible = ref(false);
    let daily_train_ticket = ref({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      start: undefined,
      startPinyin: undefined,
      startTime: undefined,
      startIndex: undefined,
      end: undefined,
      endPinyin: undefined,
      endTime: undefined,
      endIndex: undefined,
      ydz: undefined,
      ydzPrice: undefined,
      edz: undefined,
      edzPrice: undefined,
      rw: undefined,
      rwPrice: undefined,
      yw: undefined,
      ywPrice: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const daily_train_tickets = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = [
    {
      title: '日期',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: '车次编号',
      dataIndex: 'trainCode',
      key: 'trainCode',
    },
    {
      title: '出发站',
      dataIndex: 'start',
      key: 'start',
    },
    {
      title: '出发站拼音',
      dataIndex: 'startPinyin',
      key: 'startPinyin',
    },
    {
      title: '出发时间',
      dataIndex: 'startTime',
      key: 'startTime',
    },
    {
      title: '出发站序',
      dataIndex: 'startIndex',
      key: 'startIndex',
    },
    {
      title: '到达站',
      dataIndex: 'end',
      key: 'end',
    },
    {
      title: '到达站拼音',
      dataIndex: 'endPinyin',
      key: 'endPinyin',
    },
    {
      title: '到站时间',
      dataIndex: 'endTime',
      key: 'endTime',
    },
    {
      title: '到站站序',
      dataIndex: 'endIndex',
      key: 'endIndex',
    },
    {
      title: '一等座余票',
      dataIndex: 'ydz',
      key: 'ydz',
    },
    {
      title: '一等座票价',
      dataIndex: 'ydzPrice',
      key: 'ydzPrice',
    },
    {
      title: '二等座余票',
      dataIndex: 'edz',
      key: 'edz',
    },
    {
      title: '二等座票价',
      dataIndex: 'edzPrice',
      key: 'edzPrice',
    },
    {
      title: '软卧余票',
      dataIndex: 'rw',
      key: 'rw',
    },
    {
      title: '软卧票价',
      dataIndex: 'rwPrice',
      key: 'rwPrice',
    },
    {
      title: '硬卧余票',
      dataIndex: 'yw',
      key: 'yw',
    },
    {
      title: '硬卧票价',
      dataIndex: 'ywPrice',
      key: 'ywPrice',
    },
    {
      title: '操作',
      dataIndex: 'operation'
    }
    ];

    const onAdd = () => {
      daily_train_ticket.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      daily_train_ticket.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/daily_train_ticket/admin/daily_train_ticket/delete/" + record.id).then((response) => {
        if (response.code===200) {
          notification.success({description: response.message});
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          notification.error({description: response.message});
        }
      });
    };

    const handleOk = () => {
      axios.post("/daily_train_ticket/admin/daily_train_ticket/save", daily_train_ticket.value).then((response) => {
        if (response.code===200) {
          notification.success({description: response.message});
          visible.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          notification.error({description: response.message});
        }
      });
    };

    const handleQuery = (param) => {
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("/daily_train_ticket/admin/daily_train_ticket/query-list", {
        params: {
          page: param.page,
          size: param.size
        }
      }).then((response) => {
        loading.value = false;
        if (response.code===200) {
          daily_train_tickets.value = response.data.records;
          // 设置分页控件的值
          pagination.value.current = param.page;
          pagination.value.total = response.data.total;
        } else {
          notification.error({description: response.message});
        }
      });
    };

    const handleTableChange = (page) => {
      // console.log("看看自带的分页参数都有啥：" + JSON.stringify(page));
      pagination.value.pageSize = page.pageSize;
      handleQuery({
        page: page.current,
        size: page.pageSize
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      daily_train_ticket,
      visible,
      daily_train_tickets,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete
    };
  },
});
</script>
