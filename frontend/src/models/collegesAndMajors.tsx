import {getAllDepartmentAndMajorUsingGET} from "@/services/backend/majorInfoController";
import {useEffect, useState} from "react";
import LocalStorageUtils from "@/utils/LocalStorageUtils";
import {DepartmentAndMajor} from "@/constants/departmentAndMajor";

const useCollegesAndMajors = () =>
{
    const [ collegesAndMajors, setCollegesAndMajors ] = useState<DepartmentAndMajor>({});

    useEffect(() =>
    {
        const fetchData = async () =>
        {
            // 先查是否有湖安村
            const getStrogeData = LocalStorageUtils.getItem("collegesAndMajors");
            if (getStrogeData !== null)
            {
                setCollegesAndMajors(JSON.parse(getStrogeData));
                return;
            }
            const response = await getAllDepartmentAndMajorUsingGET();
            if (response.data)
            {
                const transformedData: DepartmentAndMajor = response.data.reduce((acc, department) =>
                {
                    // 专业信息majors转换为键值对
                    // @ts-ignore
                    const majorsObject = department.majors.reduce(
                        (majAcc, major) =>
                        {
                            // @ts-ignore
                            majAcc[String(major.majorId)] = major.majorName;
                            return majAcc;
                        }, {});

                    // 学院信息添加到累加器对象
                    // @ts-ignore
                    acc[String(department.departmentId)] =
                        {
                            name: department.departmentName,
                            majors: majorsObject,
                        };

                    return acc;
                }, {});
                // 缓存到本地
                LocalStorageUtils.setItem("collegesAndMajors", JSON.stringify(transformedData));
                setCollegesAndMajors(transformedData);
            }
            else
            {
                setCollegesAndMajors({
                    departmentId: "加载失败",
                    departmentName: "加载失败",
                    majors: []
                });
            }

        };
        fetchData();
    }, []);

    return {
        collegesAndMajors,
    };
};

export default useCollegesAndMajors;