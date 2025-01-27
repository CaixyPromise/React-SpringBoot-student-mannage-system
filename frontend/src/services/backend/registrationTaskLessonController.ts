// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addRegistrationTaskLesson POST /api/registrationTaskLesson/add */
export async function addRegistrationTaskLessonUsingPost1(
  body: API.RegistrationTaskLessonAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/registrationTaskLesson/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteRegistrationTaskLesson POST /api/registrationTaskLesson/delete */
export async function deleteRegistrationTaskLessonUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/registrationTaskLesson/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** editRegistrationTaskLesson POST /api/registrationTaskLesson/edit */
export async function editRegistrationTaskLessonUsingPost1(
  body: API.RegistrationTaskLessonEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/registrationTaskLesson/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getHasTasksBySubjectIdAndCourseTaskIds POST /api/registrationTaskLesson/get/hasTasks */
export async function getHasTasksBySubjectIdAndCourseTaskIdsUsingPost1(
  body: API.HasRegistrationTaskRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListHasRegistrationTaskVO_>(
    '/api/registrationTaskLesson/get/hasTasks',
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

/** listRegistrationTaskLessonVOByPage GET /api/registrationTaskLesson/get/registrationTaskLessonVO/${param0} */
export async function listRegistrationTaskLessonVoByPageUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listRegistrationTaskLessonVOByPageUsingGET1Params,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseListRegistrationTaskLessonVO_>(
    `/api/registrationTaskLesson/get/registrationTaskLessonVO/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    },
  );
}

/** listRegistrationTaskLessonByPage POST /api/registrationTaskLesson/list/page */
export async function listRegistrationTaskLessonByPageUsingPost1(
  body: API.RegistrationTaskLessonQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageRegistrationTaskLesson_>(
    '/api/registrationTaskLesson/list/page',
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

/** listRegistrationTaskLessonVOByPage POST /api/registrationTaskLesson/list/page/vo */
export async function listRegistrationTaskLessonVoByPageUsingPost1(
  body: API.RegistrationTaskLessonQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageRegistrationTaskLessonVO_>(
    '/api/registrationTaskLesson/list/page/vo',
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

/** listMyRegistrationTaskLessonVOByPage POST /api/registrationTaskLesson/my/list/page/vo */
export async function listMyRegistrationTaskLessonVoByPageUsingPost1(
  body: API.RegistrationTaskLessonQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageRegistrationTaskLessonVO_>(
    '/api/registrationTaskLesson/my/list/page/vo',
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

/** updateRegistrationTaskLesson POST /api/registrationTaskLesson/update */
export async function updateRegistrationTaskLessonUsingPost1(
  body: API.RegistrationTaskLessonUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/registrationTaskLesson/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updatePublication POST /api/registrationTaskLesson/update/publication */
export async function updatePublicationUsingPost1(
  body: API.RegistrationTaskLessonPublicationRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/registrationTaskLesson/update/publication', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
