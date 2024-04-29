
/**
 * 专业
 *
 * @author CAIXYPROMISE
 * @since 2024/3/24 22:30
 * @version 1.0
 */
export interface Major
{
    majorId: string;
    majorName: string;
}
/**
 * 学院与部门组合
 *
 * @author CAIXYPROMISE
 * @since 2024/3/24 22:29
 * @version 1.0
 */
export interface DepartmentAndMajor
{
    departmentId: string
    departmentName: string
    majors: Major[]
}