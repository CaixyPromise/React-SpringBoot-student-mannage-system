// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addStudentGrades POST /api/score/add */
export async function addStudentGradesUsingPost1(
  body: API.StudentGradesAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/score/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** batchAddStudentGrades POST /api/score/add/batch */
export async function batchAddStudentGradesUsingPost1(
  body: API.StudentGradesAddRequest[],
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/score/add/batch', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteStudentGrades POST /api/score/delete */
export async function deleteStudentGradesUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/score/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** editStudentGrades POST /api/score/edit */
export async function editStudentGradesUsingPost1(
  body: API.StudentGradesEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/score/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getStudentGradesVOByStuId GET /api/score/get/stu/grade */
export async function getStudentGradesVoByStuIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getStudentGradesVOByStuIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseStudentGradesVO_>('/api/score/get/stu/grade', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getStudentGradesVOById GET /api/score/get/vo */
export async function getStudentGradesVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getStudentGradesVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseStudentGradesVO_>('/api/score/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listStudentGradesByPage POST /api/score/list/page */
export async function listStudentGradesByPageUsingPost1(
  body: API.StudentGradesQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageStudentGrades_>('/api/score/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listStudentGradesVOByPage POST /api/score/list/page/vo */
export async function listStudentGradesVoByPageUsingPost1(
  body: API.StudentGradesQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageStudentGradesVO_>('/api/score/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyStudentGradesVOByPage POST /api/score/my/list/page/vo */
export async function listMyStudentGradesVoByPageUsingPost1(
  body: API.StudentGradesQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageStudentGradesVO_>('/api/score/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** searchStudentGradesVOByPage POST /api/score/search/page/vo */
export async function searchStudentGradesVoByPageUsingPost1(
  body: API.StudentGradesQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageStudentGradesVO_>('/api/score/search/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateStudentGrades POST /api/score/update */
export async function updateStudentGradesUsingPost1(
  body: API.StudentGradesUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/score/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
