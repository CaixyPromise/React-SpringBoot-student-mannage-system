// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addStudentCourseSelection POST /api/studentCourseSelection/add */
export async function addStudentCourseSelectionUsingPost1(
  body: API.StudentCourseSelectionAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/studentCourseSelection/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listAvailableSubjects GET /api/studentCourseSelection/availableSubjects */
export async function listAvailableSubjectsUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listAvailableSubjectsUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListStudentCourseSubjectVO_>(
    '/api/studentCourseSelection/availableSubjects',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    },
  );
}

/** cancelCourse POST /api/studentCourseSelection/cancel */
export async function cancelCourseUsingPost1(
  body: API.StudentSelectCourseRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/studentCourseSelection/cancel', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteStudentCourseSelection POST /api/studentCourseSelection/delete */
export async function deleteStudentCourseSelectionUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/studentCourseSelection/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** editStudentCourseSelection POST /api/studentCourseSelection/edit */
export async function editStudentCourseSelectionUsingPost1(
  body: API.StudentCourseSelectionEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/studentCourseSelection/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getStudentCourseSelectionVOById GET /api/studentCourseSelection/get/vo */
export async function getStudentCourseSelectionVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getStudentCourseSelectionVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseStudentCourseSubjectVO_>('/api/studentCourseSelection/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listStudentCourseSelectionByPage POST /api/studentCourseSelection/list/page */
export async function listStudentCourseSelectionByPageUsingPost1(
  body: API.StudentCourseSelectionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageStudentCourseSelection_>(
    '/api/studentCourseSelection/list/page',
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      data: body,
      ...(options || {}),
    },
  );
}

/** listStudentCourseSelectionVOByPage POST /api/studentCourseSelection/list/page/vo */
export async function listStudentCourseSelectionVoByPageUsingPost1(
  body: API.StudentCourseSelectionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageStudentCourseSubjectVO_>(
    '/api/studentCourseSelection/list/page/vo',
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      data: body,
      ...(options || {}),
    },
  );
}

/** listMyStudentCourseSelectionVOByPage POST /api/studentCourseSelection/my/list/page/vo */
export async function listMyStudentCourseSelectionVoByPageUsingPost1(
  body: API.StudentCourseSelectionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageStudentCourseSubjectVO_>(
    '/api/studentCourseSelection/my/list/page/vo',
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      data: body,
      ...(options || {}),
    },
  );
}

/** selectCourse POST /api/studentCourseSelection/select */
export async function selectCourseUsingPost1(
  body: API.StudentSelectCourseRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/studentCourseSelection/select', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateStudentCourseSelection POST /api/studentCourseSelection/update */
export async function updateStudentCourseSelectionUsingPost1(
  body: API.StudentCourseSelectionUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/studentCourseSelection/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
