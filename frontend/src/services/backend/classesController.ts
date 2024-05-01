// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addClassesInfo POST /api/classes/add */
export async function addClassesInfoUsingPOST(
  body: API.ClassesInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/classes/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteClassesInfo POST /api/classes/delete */
export async function deleteClassesInfoUsingPOST(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/classes/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** editClassesInfo POST /api/classes/edit */
export async function editClassesInfoUsingPOST(
  body: API.ClassesInfoEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/classes/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getClassesOptionDataVOByPage POST /api/classes/get/classes */
export async function getClassesOptionDataVoByPageUsingPOST(
  body: API.ClassesInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListAllClassesOptionDataVO_>('/api/classes/get/classes', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listClassesInfoVOByPageUnderMajor POST /api/classes/get/classes/under-major */
export async function listClassesInfoVoByPageUnderMajorUsingPOST(
  body: API.ClassesInfoQueryUnderMajorRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListClassesInfoVO_>('/api/classes/get/classes/under-major', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getClassesInfoVOById GET /api/classes/get/vo */
export async function getClassesInfoVoByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getClassesInfoVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseClassesInfoVO_>('/api/classes/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listClassesInfoByPage POST /api/classes/list/page */
export async function listClassesInfoByPageUsingPOST(
  body: API.ClassesInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageClassesInfo_>('/api/classes/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listClassesInfoVOByPage POST /api/classes/list/page/vo */
export async function listClassesInfoVoByPageUsingPOST(
  body: API.ClassesInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageClassesInfoVO_>('/api/classes/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyClassesInfoVOByPage POST /api/classes/my/list/page/vo */
export async function listMyClassesInfoVoByPageUsingPOST(
  body: API.ClassesInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageClassesInfoVO_>('/api/classes/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** searchClassesInfoVOByPage POST /api/classes/search/page/vo */
export async function searchClassesInfoVoByPageUsingPOST(
  body: API.ClassesInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageClassesInfoVO_>('/api/classes/search/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateClassesInfo POST /api/classes/update */
export async function updateClassesInfoUsingPOST(
  body: API.ClassesInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/classes/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
