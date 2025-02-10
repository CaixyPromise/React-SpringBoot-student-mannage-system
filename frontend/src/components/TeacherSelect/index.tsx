import React, {useEffect, useState} from 'react';
import {Empty, Select, Spin} from 'antd';
import debounce from 'lodash/debounce';
import {getTeacherInfoOptionVoUsingPost1} from "@/services/backend/teacherInfoController";

const {Option} = Select;

const TeacherSelect = ({value, onChange}:{
  value?: number;
  onChange?: (value: number) => void;
}) => {
  const [optionData, setOptionData] = useState([]); // 存储选项数据
  const [page, setPage] = useState(1); // 当前页数
  const [loading, setLoading] = useState(false); // 加载状态
  const [hasMore, setHasMore] = useState<boolean>(true); // 是否还有更多数据
  const [keyword, setKeyword] = useState<string>(''); // 搜索关键词

  // 获取教师数据的函数
  const fetchTeachers = debounce(async (newPage = 1, search = '') => {
    setLoading(true);
    try {
      const {code, data} = await getTeacherInfoOptionVoUsingPost1({
        current: newPage,
        pageSize: 20,
        teacherName: search
      })
      if (code === 0 && data?.records ) {
          const newData = [...new Map([...optionData, ...data?.records ?? []].map(item => [item.id, item])).values()];
          setOptionData(newData);
          setHasMore( newPage < (data?.pages ?? 0) ); // 后端返回是否还有更多数据
          setPage(newPage); // 更新当前页
      }
    } catch (error) {
      console.error('Failed to load data:', error);
    }
    setLoading(false);
  }, 300);

  // 滚动到底部触发加载更多
  const handleScroll = (e) => {
    const {scrollTop, scrollHeight, clientHeight} = e.target;
    if (scrollHeight - scrollTop === clientHeight && !loading && hasMore) {
      fetchTeachers(page + 1, keyword);
    }
  };

  // 搜索时触发
  const handleSearch = (value: string) => {
    setKeyword(value);
    setOptionData([]); // 清空已有数据
    fetchTeachers(1, value); // 重置为第一页加载
  };

  // 初始化加载数据
  useEffect(() => {
    fetchTeachers(page, keyword);
  }, []);

  return (
    <Select
      showSearch
      allowClear
      placeholder="请选择教师"
      notFoundContent={loading ? <Spin size="small"/> : <Empty />}
      filterOption={false}
      onSearch={handleSearch}
      onPopupScroll={handleScroll}
      style={{width: '100%'}}
      virtual
      value={value}
      onChange={onChange}
    >
      {optionData
        .sort((a: API.TeacherInfoVO, b: API.TeacherInfoVO) => {
          // 先按 teacherDeptId 排序
          const deptDiff = (a.teacherDeptId ?? 0) - (b.teacherDeptId ?? 0);
          if (deptDiff !== 0) return deptDiff; // 如果部门不同，直接返回部门排序结果

          // 如果部门相同，再按 teacherMajorId 排序
          return (a.teacherMajorId ?? 0) - (b.teacherMajorId ?? 0);
        })
        .map((item: API.TeacherInfoVO) => (
          <Option key={item.id} value={item.id}>
            {item?.teacherName} - {item?.teacherDepart} - {item?.teacherMajor} - {item?.teacherId}
          </Option>
        ))}
    </Select>
  );
};

export default TeacherSelect;
