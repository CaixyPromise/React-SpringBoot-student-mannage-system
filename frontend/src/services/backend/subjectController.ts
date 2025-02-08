// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addSubjects POST /api/subject/add */
export async function addSubjectsUsingPost1(
  body: API.SubjectsAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/subject/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteSubjects POST /api/subject/delete */
export async function deleteSubjectsUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/subject/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** editSubjects POST /api/subject/edit */
export async function editSubjectsUsingPost1(
  body: API.SubjectsEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/subject/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getAllSubjectsVO GET /api/subject/get/all */
export async function getAllSubjectsVoUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListSubjectsVO_>('/api/subject/get/all', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getSubjectsVOById GET /api/subject/get/vo */
export async function getSubjectsVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSubjectsVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseSubjectsVO_>('/api/subject/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listSubjectsByPage POST /api/subject/list/page */
export async function listSubjectsByPageUsingPost1(
  body: API.SubjectsQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageSubjects_>('/api/subject/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listSubjectsVOByPage POST /api/subject/list/page/vo */
export async function listSubjectsVoByPageUsingPost1(
  body: API.SubjectsQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageSubjectsVO_>('/api/subject/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** searchSubjectsVOByPage POST /api/subject/search/page/vo */
export async function searchSubjectsVoByPageUsingPost1(
  body: API.SubjectsQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageSubjectsVO_>('/api/subject/search/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateSubjects POST /api/subject/update */
export async function updateSubjectsUsingPost1(
  body: API.SubjectsUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/subject/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
