import React, { useState } from 'react';
import { Tree, Spin, Space } from 'antd';
import { useCourseSelection } from '../contexts/CourseSelectionContext';

const CourseSelectionTree: React.FC<{
  taskId: number
}> = ({taskId}) => {
  const { treeData, loading, departments } = useCourseSelection();
  const [selectedKeys, setSelectedKeys] = useState<React.Key[]>([]);
  const [checkedKeys, setCheckedKeys] = useState<React.Key[]>([]);
  const {setCourseId, setSelectedClassIds, setCourseModalVisible} = useCourseSelection();

  const getAllClassIds = () => {
    const classIds = new Set<string>();
    departments.forEach(dept => {
      dept.children.forEach(major => {
        major.children.forEach(cls => {
          classIds.add(cls.classId);
        });
      });
    });
    return classIds;
  };

  return (
    <div>
      <Spin spinning={loading}>
        <div>
          <h3>班级列表</h3>
          <Space style={{marginBottom: '16px'}}>
          </Space>
        </div>
        <Tree
          checkable
          showLine
          showIcon={false}
          treeData={treeData}
          defaultExpandAll
          selectedKeys={selectedKeys}
          checkedKeys={checkedKeys}
          onSelect={(keys) => setSelectedKeys(keys)}
          onCheck={(keys) => setCheckedKeys(keys as React.Key[])}
          // className="w-1/2"
        />
      </Spin>
    </div>
  );
};

export default CourseSelectionTree;

