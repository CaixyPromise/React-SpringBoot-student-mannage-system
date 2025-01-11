import React, {createContext, useContext, useState, useMemo} from 'react';
import {DataNode} from "@umijs/utils/compiled/cheerio/domhandler/lib";

interface ClassInfo {
  classId: string;
  className: string;
}

interface MajorInfo {
  majorId: string;
  majorName: string;
  children: ClassInfo[];
}

interface DepartmentInfo {
  departId: string;
  departName: string;
  children: MajorInfo[];
}

interface CourseSelectionContextType {
  tableDataDict: Record<number, API.CourseSelectionInfoVO>
  departments: DepartmentInfo[];
  setDepartments: (data: DepartmentInfo[]) => void;
  loading: boolean;
  setLoading: (loading: boolean) => void;
  treeData: DataNode[];
  courseModalVisible: boolean
  setCourseModalVisible: (state: boolean) => void;
  courseModalOnCancel: () => void;
  courseId: number;
  setCourseId: (state: number) => void;
  selectedClassIds: Array<number>;
  setSelectedClassIds: (state: Array<string>) => void;
}

const CourseSelectionContext = createContext<CourseSelectionContextType>({
  tableDataDict: {},
  departments: [],
  setDepartments: () => {},
  loading: false,
  setLoading: () => {},
  treeData: [],
  courseModalVisible: false,
  setCourseModalVisible: (state) => {},
  courseModalOnCancel: () => {},
  courseId: 0,
  setCourseId: (state) => {},
  selectedClassIds: [],
  setSelectedClassIds: (state) => {},
});

export const CourseSelectionProvider: React.FC<{
  children: React.ReactNode,
  tableDataDict: Record<number, API.CourseSelectionInfoVO>,
}> = ({
  children,
  tableDataDict,
}) => {
  const [departments, setDepartments] = useState<DepartmentInfo[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [courseModalVisible, setCourseModalVisible] = useState<boolean>(false);
  const [courseId, setCourseId] = useState<number>(0);
  const [selectedClassIds, setSelectedClassIds] = useState<string[]>([]);

  const treeData = useMemo(() => {
    return departments.map(dept => ({
      key: dept.departId,
      title: dept.departName,
      children: dept.children.map(major => ({
        key: major.majorId,
        title: major.majorName,
        children: major.children.map(cls => ({
          key: cls.classId,
          title: cls.className,
          isLeaf: true,
        })),
      })),
    }));
  }, [departments]);

  const courseModalOnCancel = () => {
    setCourseModalVisible(false)
  }

  return (
    <CourseSelectionContext.Provider
      value={{
        departments,
        setDepartments,
        loading,
        setLoading,
        treeData,
        courseModalVisible,
        setCourseModalVisible,
        courseModalOnCancel,
        courseId,
        setCourseId,
        selectedClassIds,
        setSelectedClassIds,
        tableDataDict,
      }}
    >
      {children}
    </CourseSelectionContext.Provider>
  );
};

export const useCourseSelection = () => useContext(CourseSelectionContext);

