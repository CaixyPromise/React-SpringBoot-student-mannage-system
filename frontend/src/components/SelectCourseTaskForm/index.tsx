// SelectCourseTaskForm.tsx
import React, {useEffect, useMemo, useState, forwardRef, useImperativeHandle} from "react";
import {
  Button,
  Cascader,
  DatePicker,
  Form,
  FormInstance,
  Input,
  InputNumber,
  message,
  Select,
  Space,
  Spin,
} from "antd";
import dayjs from "dayjs";
import {ClockCircleOutlined, PlusOutlined} from "@ant-design/icons";
import {getSemestersUsingGet1} from "@/services/backend/semestersController";
import {getClassesOptionDataVoByPageUsingPost1} from "@/services/backend/classesController";
import {getAllSubjectsVoUsingGet1} from "@/services/backend/subjectController";

export interface CourseSetting {
  courseId: number;
  maxStudents: number;
}

export interface SelectCourseFormProps {
  semesterId: number;
  taskName: string;
  selectedGrade: string[][];
  courseTime: [dayjs.Dayjs, dayjs.Dayjs];
  minCredit: number;
  classIds: string[];
  startDate: string;
  endDate: string;
  courseSettings: CourseSetting[];
}

export interface SelectCourseTaskFormRef {
  submit: () => Promise<void>;
  getValues: () => Promise<SelectCourseFormProps | undefined>;
}

const {RangePicker} = DatePicker;

interface SelectCourseTaskFormProps {
  form?: FormInstance<SelectCourseFormProps>;
  submitCallback?: (value: SelectCourseFormProps) => void;
  excludeClassIds?: Array<number>;
}

interface GradeNode {
  value: string;
  label: string;
  children?: GradeNode[];
}

/** 1. 递归构建 value->node 的映射，便于后面通过 value 快速找到节点 */
const buildNodeMap = (nodes: GradeNode[], map = new Map<string, GradeNode>()) => {
  nodes.forEach((node) => {
    map.set(node.value, node);
    if (node.children && node.children.length > 0) {
      buildNodeMap(node.children, map);
    }
  });
  return map;
};

/** 2. 递归收集某个节点下的所有“叶子” (即真正的班级) */
const collectAllLeafValues = (node: GradeNode, result: Set<string>) => {
  if (!node.children || node.children.length === 0) {
    // 没有 children，认为这个节点本身就是“班级”
    result.add(node.value);
  } else {
    // 有 children，继续向下递归
    node.children.forEach((child) => collectAllLeafValues(child, result));
  }
};

const SelectCourseTaskForm = forwardRef<SelectCourseTaskFormRef, SelectCourseTaskFormProps>(
  ({form: externalForm, submitCallback, excludeClassIds = []}, ref) => {
    const [internalForm] = Form.useForm<SelectCourseFormProps>();
    const form = externalForm || internalForm; // 使用外部传入的 form 或内部创建的 form

    const [loading, setLoading] = useState<boolean>(false);
    const [semesterOption, setSemesterOption] = useState<API.SemestersVO[]>([]);
    const [gradeOption, setGradeOption] = useState<API.AllClassesOptionDataVO[]>([]);
    const [subjectOption, setSubjectOption] = useState<
      Array<{ label: string; value: number }>
    >([]);
    const [subjectMap, setSubjectMap] = useState<Record<number, API.SubjectsVO>>({});

    const colorSemester = useMemo(() => {
      return {
        1: "green",
        0: "#ff0000",
      };
    }, []);

    const querySubject = async () => {
      try {
        const {code, data} = await getAllSubjectsVoUsingGet1();
        if (code === 0) {
          const newSubjectMap: Record<number, API.SubjectsVO> = {};
          const option = data
            .filter((subj) => subj.courseType === 1)
            .map((subject) => {
              newSubjectMap[subject.id ?? 0] = subject;
              return {
                label: `${subject.name}-${subject.gradeCredit}学分`,
                value: subject.id,
              };
            });
          setSubjectMap(newSubjectMap);
          setSubjectOption(option);
        } else {
          message.error(`获取课程列表失败: ${data.message}`);
        }
      } catch (error) {
        message.error(`获取课程列表异常: ${error}`);
      }
    };
    // 监听 courseSettings 字段的变化
    const courseSettingsValues = Form.useWatch('courseSettings', form) as CourseSetting[] | undefined;

    // 计算已设置课程的总学分
    const selectedCoursesCredits = useMemo(() => {
      if (!courseSettingsValues || courseSettingsValues.length === 0) return 0;
      return courseSettingsValues.reduce((sum, cs) =>
        sum + (subjectMap[cs?.courseId]?.gradeCredit ?? 0), 0);
    }, [courseSettingsValues, subjectMap]);

    // 计算已设置课程中的最小学分
    const minSelectedCredit = useMemo(() => {
      if (!courseSettingsValues || courseSettingsValues.length === 0) return 0;
      let minCredit = Infinity;
      courseSettingsValues.forEach(cs => {
        const credit = subjectMap[cs?.courseId]?.gradeCredit;
        if (credit !== undefined && credit < minCredit) {
          minCredit = credit;
        }
      });
      return isFinite(minCredit) ? minCredit : 1;
    }, [courseSettingsValues, subjectMap]);


    const querySemesters = async () => {
      try {
        const response = await getSemestersUsingGet1();
        if (response.code === 0 && response.data) {
          const sortedSemesters = response.data.sort((a: API.SemestersVO, b: API.SemestersVO) => {
            // 优先判断 isActive
            if (a.isActive === 1 && b.isActive !== 1) return -1;
            if (b.isActive === 1 && a.isActive !== 1) return 1;

            // 比较日期范围，最近的日期靠前
            const dateA = a.startDate ? new Date(a.startDate).getTime() : 0;
            const dateB = b.startDate ? new Date(b.startDate).getTime() : 0;
            return dateB - dateA; // 日期越近越前
          });

          setSemesterOption(sortedSemesters);
        } else {
          message.error(`获取学期列表失败: ${response.message}`);
        }
      } catch (error) {
        message.error(`获取学期列表异常: ${error}`);
      }
    };

    const queryDistinctGradeList = async () => {
      try {
        const response = await getClassesOptionDataVoByPageUsingPost1({
          excludeClassIds,
        });
        if (response.code === 0) {
          setGradeOption(response.data);
        } else {
          message.error(`获取年级列表失败: ${response.message}`);
          return Promise.reject();
        }
      } catch (error) {
        message.error(`获取年级列表异常: ${error}`);
        return Promise.reject();
      }
    };

    const getSubmitPayload = async () => {
      try {
        const values = await form.validateFields();
        console.log("表单验证成功，表单值:", values);

        const {selectedGrade = []} = values;

        if (!selectedGrade.length) {
          message.warning("请至少选择一个学院/专业/班级！");
          return;
        }

        // 1. 建立节点映射( value -> node )
        const nodeMap = buildNodeMap(gradeOption);

        // 2. 收集所有选中的最后一级节点
        const selectedValues = new Set<string>();
        selectedGrade.forEach((path: string[]) => {
          const lastVal = path[path.length - 1];
          selectedValues.add(lastVal);
        });

        // 3. 对每个被选中的“最后一级节点”做递归，把它下面所有叶子(班级)都收集出来
        const allClassIds = new Set<string>();
        selectedValues.forEach((val) => {
          const node = nodeMap.get(val);
          if (node) {
            collectAllLeafValues(node, allClassIds);
          }
        });

        const {
          taskName,
          minCredit,
          semesterId,
          courseTime,
          courseSettings
        } = values;

        // 把 dayjs 对象格式化成字符串
        const startStr = dayjs(courseTime[0]).format("YYYY-MM-DD HH:mm:ss");
        const endStr = dayjs(courseTime[1]).format("YYYY-MM-DD HH:mm:ss");

        const payload: SelectCourseFormProps = {
          semesterId,
          taskName,
          selectedGrade,
          courseTime,
          minCredit,
          classIds: Array.from(allClassIds),
          startDate: startStr,
          endDate: endStr,
          courseSettings,  // 使用新字段
        };
        console.log("form Payload: ", payload);
        return payload;
      } catch (error) {
        console.error("表单提交失败:", error);
        return null;
      }
    };

    useImperativeHandle(ref, () => ({
      submit: async () => {
        if (submitCallback) {
          const payload = await getSubmitPayload();
          if (payload) {
            await submitCallback(payload);
          }
        }
      },
      getValues: async () => {
        try {
          return await getSubmitPayload();
        } catch (error) {
          console.error("获取表单值失败:", error);
          return undefined;
        }
      }
    }));

    useEffect(() => {
      setLoading(true);
      Promise.all([querySemesters(), queryDistinctGradeList(), querySubject()]).finally(() =>
        setLoading(false)
      );
    }, [excludeClassIds]);

    return (
      <Spin spinning={loading}>
        <Form
          form={form}
          layout="vertical"
          onFinish={getSubmitPayload}
          initialValues={{
            semesterId: undefined,
            taskName: "",
            selectedGrade: [],
            courseTime: [null, null],
            minCredit: 1,
            courseSettings: [],
          }}
        >
          <Form.Item name="semesterId" label="选课学期" rules={[{required: true}]}>
            <Select placeholder="请选择学期">
              {semesterOption.map((semester) => (
                <Select.Option key={semester.id} value={semester.id}>
                  <span style={{color: colorSemester[semester.isActive]}}>
                    {semester.name} [
                    {`${dayjs(semester.startDate).format("YYYY-MM-DD")} ~ ${dayjs(
                      semester.endDate
                    ).format("YYYY-MM-DD")}`}
                    ] {semester.isActive === 1 && "（当前学期）"}
                  </span>
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item
            name="taskName"
            label="选课任务名称"
            rules={[
              {required: true, message: "请输入选课任务名称！"},
              {
                validator: (_, value) => {
                  if (value && value.length > 20) {
                    return Promise.reject("任务名称不能超过20个字符！");
                  }
                  return Promise.resolve();
                },
              },
            ]}
          >
            <Input placeholder="请输入选课任务名称"/>
          </Form.Item>
          {/* 添加带标签的 Form.List */}
          <Form.Item label="选课课程设置" required>
            <Form.List
              name="courseSettings"
              rules={[
                {
                  validator: async (_, names) => {
                    if (!names || names.length < 1) {
                      return Promise.reject(new Error('请至少设置一个课程'));
                    }
                  },
                },
              ]}
            >
              {(fields, {add, remove}, {errors}) => (
                <>
                  {fields.map((field) => {
                    // 获取当前已选择的课程（除当前行外）
                    const currentSettings: CourseSetting[] = form.getFieldValue('courseSettings') || [];
                    const alreadySelected = new Set<number>();
                    currentSettings.forEach((item: CourseSetting, index: number) => {
                      if (index !== field.name && item?.courseId) {
                        alreadySelected.add(item.courseId);
                      }
                    });

                    return (
                      <Space key={field.key} align="baseline" style={{display: 'flex', marginBottom: 8}}>
                        <Form.Item
                          {...field}
                          name={[field.name, 'courseId']}
                          rules={[{required: true, message: '请选择课程'}]}
                        >
                          <Select
                            placeholder="请选择课程"
                            options={subjectOption.filter(opt => !alreadySelected.has(opt.value))}
                            style={{width: 200}}
                          />
                        </Form.Item>
                        <Form.Item
                          {...field}
                          name={[field.name, 'maxStudents']}
                          rules={[{required: true, message: '请输入最大选课人数'}]}
                        >
                          <InputNumber min={1} placeholder="最大人数"/>
                        </Form.Item>
                        <Button type="link" danger onClick={() => remove(field.name)}>
                          删除
                        </Button>
                      </Space>
                    );
                  })}
                  <Form.Item>
                    <Button type="dashed" onClick={() => add()} block icon={<PlusOutlined/>}>
                      添加课程设置
                    </Button>
                    <Form.ErrorList errors={errors}/>
                  </Form.Item>
                </>
              )}
            </Form.List>
          </Form.Item>

          <Form.Item name="selectedGrade" label="选课专业年级" rules={[{required: true}]}>
            <Cascader
              options={gradeOption}
              placeholder="请选择专业和年级"
              showCheckedStrategy={Cascader.SHOW_PARENT}
              multiple
              changeOnSelect
              allowClear
            />
          </Form.Item>

          <Form.Item
            name="courseTime"
            label="选课时间"
            rules={[{required: true, message: "请选择选课开始和结束时间！"}]}
          >
            <RangePicker
              showTime
              format="YYYY-MM-DD HH:mm:ss"
              placeholder={["开始时间", "结束时间"]}
              suffixIcon={<ClockCircleOutlined/>}
            />
          </Form.Item>

          <Space size="large" align="start">
            <Form.Item
              name="minCredit"
              label={`学生最小选课学分  最大${selectedCoursesCredits} 最小:${minSelectedCredit}`}
              tooltip={(
                <div>
                  <span>学生选课时，要满足以下学分要求：</span>
                  <div>
                    <span>最大为选课学科的学分总和：{selectedCoursesCredits}</span><br/>
                    <span>最小为已选课程中的最小学分：{minSelectedCredit}</span>
                  </div>
                </div>
              )}
              style={{width: "250px"}}
              rules={[
                {required: true, message: "请输入最小选课学分！"},
                {
                  validator: (_, value) => {
                    if (value === undefined) return Promise.reject("请输入最小选课学分！");
                    if (value < minSelectedCredit)
                      return Promise.reject(`学分不能小于已选课程中的最小学分 ${minSelectedCredit}`);
                    if (value > selectedCoursesCredits)
                      return Promise.reject(`学分不能大于已选课程总学分 ${selectedCoursesCredits}`);
                    return Promise.resolve();
                  },
                },
              ]}
            >
              <InputNumber
                style={{width: "100%"}}
                min={minSelectedCredit}
                max={selectedCoursesCredits}
                placeholder="请输入学分"
              />
            </Form.Item>
          </Space>
        </Form>
      </Spin>
    );
  }
);

export default SelectCourseTaskForm;
