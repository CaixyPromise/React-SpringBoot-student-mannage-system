// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addSemesters POST /api/semesters/add */
export async function addSemestersUsingPost1(
  body: API.SemestersAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/semesters/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteSemesters POST /api/semesters/delete */
export async function deleteSemestersUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/semesters/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getCurrentSemester GET /api/semesters/get/current */
export async function getCurrentSemesterUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseSemestersVO_>('/api/semesters/get/current', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getSemesters GET /api/semesters/get/semesters */
export async function getSemestersUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListSemestersVO_>('/api/semesters/get/semesters', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getSemestersVOById GET /api/semesters/get/vo */
export async function getSemestersVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSemestersVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseSemestersVO_>('/api/semesters/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listSemestersByPage POST /api/semesters/list/page */
export async function listSemestersByPageUsingPost1(
  body: API.SemestersQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageSemestersVO_>('/api/semesters/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** setActiveSemester POST /api/semesters/set/active */
export async function setActiveSemesterUsingPost1(
  body: API.SemestersSetActiveRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/semesters/set/active', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateSemesters POST /api/semesters/update */
export async function updateSemestersUsingPost1(
  body: API.SemestersUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/semesters/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
