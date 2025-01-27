// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addStudentInfo POST /api/student/add */
export async function addStudentInfoUsingPost1(
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
export async function deleteStudentInfoUsingPost1(
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
export async function editStudentInfoUsingPost1(
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

/** getStudentsByCourseSelectionAndSubject GET /api/student/get/by-selection-lesson */
export async function getStudentsByCourseSelectionAndSubjectUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getStudentsByCourseSelectionAndSubjectUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListStudentInfoVO_>('/api/student/get/by-selection-lesson', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getStudentInfoVOById GET /api/student/get/vo */
export async function getStudentInfoVoByIdUsingGet1(
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
export async function listStudentInfoByPageUsingPost1(
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
export async function listMyStudentInfoVoByPageUsingPost1(
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
export async function searchStudentInfoVoByPageUsingPost1(
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
export async function updateStudentInfoUsingPost1(
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
