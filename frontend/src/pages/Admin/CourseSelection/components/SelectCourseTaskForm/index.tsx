import React, {useEffect, useMemo, useState, forwardRef, useImperativeHandle} from "react";
import {
  Button,
  Cascader,
  Col,
  DatePicker,
  Form,
  FormInstance,
  Input,
  InputNumber,
  message,
  Row,
  Select,
  Space,
  Spin,
} from "antd";
import dayjs from "dayjs";
import {ClockCircleOutlined, PlusOutlined} from "@ant-design/icons";
import {getClassesOptionDataVoByPageUsingPost1} from "@/services/backend/classesController";
import {getAllSubjectsVoUsingGet1} from "@/services/backend/subjectController";
import TeacherSelect from "@/components/TeacherSelect";
import {period_option, weeks_option} from "@/pages/Admin/CourseSelection/components/SelectCourseTaskForm/config";
import SemesterSelect from "@/components/SemesterSelect";

export interface CourseSetting {
  courseId: number;
  maxStudents: number;
  classTeacher: number;
  classRoom: string;
  classTimes: Array<{
    dayOfWeek: number;
    period: number;
  }>;
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

const classTimesRules = [
  {
    validator: async (_, classTimes) => {
      if (!classTimes || classTimes.length === 0) {
        return Promise.reject(new Error('请至少添加一个上课时间！'));
      }

      // 校验时间冲突
      const seenTimes = new Set();
      for (const time of classTimes) {
        const { dayOfWeek, period } = time || {};
        if (!dayOfWeek || !period) {
          return Promise.reject(new Error('请完整填写每一行的上课时间！'));
        }

        const key = `${dayOfWeek}-${period}`;
        if (seenTimes.has(key)) {
          return Promise.reject(new Error(`上课时间冲突：${weeks_option.find(w => w.value === dayOfWeek)?.label} ${period_option.find(p => p.value === period)?.label}`));
        }
        seenTimes.add(key);
      }

      return Promise.resolve();
    },
  },
];


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
    const [gradeOption, setGradeOption] = useState<API.AllClassesOptionDataVO[]>([]);
    const [subjectOption, setSubjectOption] = useState<
      Array<{ label: string; value: number }>
    >([]);
    const [subjectMap, setSubjectMap] = useState<Record<number, API.SubjectsVO>>({});

    const querySubject = async () => {
      try {
        const {code, data} = await getAllSubjectsVoUsingGet1();
        if (code === 0) {
          const newSubjectMap: Record<number, API.SubjectsVO> = {};
          const option = data
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
      if (!courseSettingsValues || courseSettingsValues.length === 0) return 1;
      let minCredit = Infinity;
      courseSettingsValues.forEach(cs => {
        const credit = subjectMap[cs?.courseId]?.gradeCredit;
        if (credit !== undefined && credit < minCredit) {
          minCredit = credit;
        }
      });
      return isFinite(minCredit) ? minCredit : 1;
    }, [courseSettingsValues, subjectMap]);

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
          courseSettings,
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
      Promise.all([queryDistinctGradeList(), querySubject()]).finally(() =>
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
            classTimes: [],
            classRoom: [],
            classTeacher: ""
          }}
        >
          <Form.Item name="semesterId" label="选课学期" rules={[{required: true}]}>
            <SemesterSelect placeholder="请选择学期" />
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
              {(fields, { add, remove }, { errors }) => (
                <>
                  {fields.map((field) => {
                    // 用于排除已选课程
                    const currentSettings: CourseSetting[] = form.getFieldValue('courseSettings') || [];
                    const alreadySelected = new Set<number>();
                    currentSettings.forEach((item: CourseSetting, idx: number) => {
                      if (idx !== field.name && item?.courseId) {
                        alreadySelected.add(item.courseId);
                      }
                    });

                    return (
                      <div
                        key={field.key}
                        style={{ marginBottom: 16, border: '1px solid #f0f0f0', padding: 16 }}
                      >
                        <Row gutter={16}>
                          {/* 课程 */}
                          <Col span={12}>
                            <Form.Item
                              {...field}
                              label="课程"
                              name={[field.name, 'courseId']}
                              rules={[{ required: true, message: '请选择课程' }]}
                            >
                              <Select
                                placeholder="请选择课程"
                                style={{ width: '100%' }}
                                options={subjectOption.filter(opt => !alreadySelected.has(opt.value))}
                              />
                            </Form.Item>
                          </Col>

                          {/* 课程老师 */}
                          <Col span={12}>
                            <Form.Item
                              label="课程老师"
                              name={[field.name, 'classTeacher']}
                              rules={[{ required: true, message: '请选择老师' }]}
                            >
                              <TeacherSelect />
                            </Form.Item>
                          </Col>
                        </Row>
                        <Row gutter={16}>
                          {/* 最大人数 */}
                          <Col span={12}>
                            <Form.Item
                              {...field}
                              label="最大人数"
                              name={[field.name, 'maxStudents']}
                              rules={[{ required: true, message: '请输入最大人数' }]}
                            >
                              <InputNumber
                                min={1}
                                style={{ width: '100%' }}
                                placeholder="最大人数"
                              />
                            </Form.Item>
                          </Col>

                          {/* 上课地点 */}
                          <Col span={12}>
                            <Form.Item
                              label="上课地点"
                              name={[field.name, 'classRoom']}
                              rules={[
                                { required: true, message: '请填写上课地点' },
                                { max: 20, message: '上课地点长度不能超过20个字符' },
                              ]}
                            >
                              <Input placeholder="请填写上课地点" />
                            </Form.Item>
                          </Col>
                        </Row>

                        <Row gutter={16}>
                          <Col span={24}>
                            {/* 上课时间（嵌套 Form.List） */}
                            <Form.Item label="上课时间" required>
                              <Form.List
                                name={[field.name, 'classTimes']}
                                rules={classTimesRules}
                              >
                                {(timeFields, { add: addTime, remove: removeTime }) => (
                                  <>
                                    {timeFields.map(({ key, name, ...restField }) => (
                                      <Space key={key} align="baseline" style={{ marginBottom: 8 }}>
                                        <Form.Item
                                          {...restField}
                                          name={[name, 'dayOfWeek']}
                                          rules={[{ required: true, message: '请选择星期几' }]}
                                        >
                                          <Select
                                            placeholder="选择星期"
                                            style={{ width: 270 }}
                                            options={weeks_option}
                                          />
                                        </Form.Item>

                                        <Form.Item
                                          {...restField}
                                          name={[name, 'period']}
                                          rules={[{ required: true, message: '选择上课课时' }]}
                                        >
                                          <Select
                                            placeholder="选择上课课时"
                                            style={{ width: 270 }}
                                            options={period_option}
                                          />
                                        </Form.Item>

                                        <Button type="link" danger onClick={() => removeTime(name)}>
                                          删除
                                        </Button>
                                      </Space>
                                    ))}

                                    <Form.Item>
                                      <Button
                                        type="dashed"
                                        onClick={() => addTime()}
                                        icon={<PlusOutlined />}
                                      >
                                        添加上课时间
                                      </Button>
                                    </Form.Item>
                                  </>
                                )}
                              </Form.List>
                            </Form.Item>
                          </Col>
                        </Row>

                        <Button type="link" danger onClick={() => remove(field.name)}>
                          删除本条课程设置
                        </Button>
                      </div>
                    );
                  })}
                  <Form.Item>
                    <Button
                      type="dashed"
                      onClick={() => add()}
                      block
                      icon={<PlusOutlined />}
                    >
                      添加课程设置
                    </Button>
                    <Form.ErrorList errors={errors} />
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
