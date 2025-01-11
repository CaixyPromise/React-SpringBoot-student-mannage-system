// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addTeacherInfo POST /api/teacher/add */
export async function addTeacherInfoUsingPost1(
  body: API.TeacherInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/teacher/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteTeacherInfo POST /api/teacher/delete */
export async function deleteTeacherInfoUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/teacher/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** editTeacherInfo POST /api/teacher/edit */
export async function editTeacherInfoUsingPost1(
  body: API.TeacherInfoEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/teacher/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getTeacherInfoVOById GET /api/teacher/get/vo */
export async function getTeacherInfoVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTeacherInfoVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseTeacherInfoVO_>('/api/teacher/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listTeacherInfoByPage POST /api/teacher/list/page */
export async function listTeacherInfoByPageUsingPost1(
  body: API.TeacherInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageTeacherInfoVO_>('/api/teacher/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyTeacherInfoVOByPage POST /api/teacher/my/list/page/vo */
export async function listMyTeacherInfoVoByPageUsingPost1(
  body: API.TeacherInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageTeacherInfoVO_>('/api/teacher/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateTeacherInfo POST /api/teacher/update */
export async function updateTeacherInfoUsingPost1(
  body: API.TeacherInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/teacher/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
