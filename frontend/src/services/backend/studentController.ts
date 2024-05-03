// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addStudentInfo POST /api/student/add */
export async function addStudentInfoUsingPOST(
  body: API.StudentInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/student/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteStudentInfo POST /api/student/delete */
export async function deleteStudentInfoUsingPOST(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/student/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** editStudentInfo POST /api/student/edit */
export async function editStudentInfoUsingPOST(
  body: API.StudentInfoEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/student/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getStudentInfoVOById GET /api/student/get/vo */
export async function getStudentInfoVoByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getStudentInfoVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseStudentInfoVO_>('/api/student/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listStudentInfoByPage POST /api/student/list/page */
export async function listStudentInfoByPageUsingPOST(
  body: API.StudentInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageStudentInfoVO_>('/api/student/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyStudentInfoVOByPage POST /api/student/my/list/page/vo */
export async function listMyStudentInfoVoByPageUsingPOST(
  body: API.StudentInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageStudentInfoVO_>('/api/student/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** searchStudentInfoVOByPage POST /api/student/search/page/vo */
export async function searchStudentInfoVoByPageUsingPOST(
  body: API.StudentInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageStudentInfoVO_>('/api/student/search/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateStudentInfo POST /api/student/update */
export async function updateStudentInfoUsingPOST(
  body: API.StudentInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/student/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
