// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** activateTaskById GET /api/selection/activate-task */
export async function activateTaskByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.activateTaskByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/selection/activate-task', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** addCourseSelectionInfo POST /api/selection/add */
export async function addCourseSelectionInfoUsingPost1(
  body: API.CreateCourseSelectionRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/selection/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteCourseSelectionInfo POST /api/selection/delete */
export async function deleteCourseSelectionInfoUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/selection/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** editCourseSelectionInfo POST /api/selection/edit */
export async function editCourseSelectionInfoUsingPost1(
  body: API.CourseSelectionInfoEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/selection/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getCourseSelectionInfoBySemesterId GET /api/selection/get/bySemesterId */
export async function getCourseSelectionInfoBySemesterIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCourseSelectionInfoBySemesterIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListCourseSelectionInfoVO_>('/api/selection/get/bySemesterId', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getSelectTaskCoursesByTaskId GET /api/selection/get/select-task/courses */
export async function getSelectTaskCoursesByTaskIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSelectTaskCoursesByTaskIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListCourseSelectSubjectVO_>(
    '/api/selection/get/select-task/courses',
    {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    },
  );
}

/** getUserCourseSelectionInfo GET /api/selection/get/student/available-selection-task */
export async function getUserCourseSelectionInfoUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListCourseSelectionInfoVO_>(
    '/api/selection/get/student/available-selection-task',
    {
      method: 'GET',
      ...(options || {}),
    },
  );
}

/** getCourseSelectSubjectByTaskId GET /api/selection/get/subject/by-taskId */
export async function getCourseSelectSubjectByTaskIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCourseSelectSubjectByTaskIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListSubjectsVO_>('/api/selection/get/subject/by-taskId', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getCourseSelectionInfoVOById GET /api/selection/get/vo */
export async function getCourseSelectionInfoVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCourseSelectionInfoVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseCourseSelectionInfoVO_>('/api/selection/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listCourseSelectionInfoByPage POST /api/selection/list/page */
export async function listCourseSelectionInfoByPageUsingPost1(
  body: API.CourseSelectionInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageCourseSelectionInfo_>('/api/selection/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listCourseSelectionInfoVOByPage POST /api/selection/list/page/vo */
export async function listCourseSelectionInfoVoByPageUsingPost1(
  body: API.CourseSelectionInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageCourseSelectionInfoVO_>('/api/selection/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyCourseSelectionInfoVOByPage POST /api/selection/my/list/page/vo */
export async function listMyCourseSelectionInfoVoByPageUsingPost1(
  body: API.CourseSelectionInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageCourseSelectionInfoVO_>('/api/selection/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** pageCourseSelection GET /api/selection/page/selection-task-admin */
export async function pageCourseSelectionUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageCourseSelectionUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageCourseSelectionInfoVO_>(
    '/api/selection/page/selection-task-admin',
    {
      method: 'GET',
      params: {
        // pageNum has a default value: 1
        pageNum: '1',
        // pageSize has a default value: 10
        pageSize: '10',

        ...params,
      },
      ...(options || {}),
    },
  );
}

/** putTaskHoldById GET /api/selection/put-task-hold */
export async function putTaskHoldByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.putTaskHoldByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/selection/put-task-hold', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** updateCourseSelectionInfo POST /api/selection/update */
export async function updateCourseSelectionInfoUsingPost1(
  body: API.CourseSelectionInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/selection/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
