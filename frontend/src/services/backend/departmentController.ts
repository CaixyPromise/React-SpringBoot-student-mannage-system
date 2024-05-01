// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addDepartmentInfo POST /api/department/add */
export async function addDepartmentInfoUsingPOST(
  body: API.DepartmentInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/department/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteDepartmentInfo POST /api/department/delete */
export async function deleteDepartmentInfoUsingPOST(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/department/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getDepartmentInfoVOById GET /api/department/get/vo */
export async function getDepartmentInfoVoByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDepartmentInfoVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseDepartmentInfoVO_>('/api/department/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getMajorUnderDepartment GET /api/department/get/vo/department-major */
export async function getMajorUnderDepartmentUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMajorUnderDepartmentUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseDepartmentWithMajorsVO_>(
    '/api/department/get/vo/department-major',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    },
  );
}

/** listDepartmentInfoByPage POST /api/department/list/page */
export async function listDepartmentInfoByPageUsingPOST(
  body: API.DepartmentInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageDepartmentInfo_>('/api/department/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateDepartmentInfo POST /api/department/update */
export async function updateDepartmentInfoUsingPOST(
  body: API.DepartmentInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/department/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
