// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addRegistrationTask POST /api/registrationTask/add */
export async function addRegistrationTaskUsingPost1(
  body: API.RegistrationTaskAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/registrationTask/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteRegistrationTask POST /api/registrationTask/delete */
export async function deleteRegistrationTaskUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/registrationTask/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** editRegistrationTask POST /api/registrationTask/edit */
export async function editRegistrationTaskUsingPost1(
  body: API.RegistrationTaskEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/registrationTask/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** activeRegistrationTaskUsingPost POST /api/registrationTask/edit/active */
export async function activeRegistrationTaskUsingPostUsingPost1(
  body: API.RegistrationTaskEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/registrationTask/edit/active', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getRegistrationTaskVOById GET /api/registrationTask/get/vo */
export async function getRegistrationTaskVoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRegistrationTaskVOByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseRegistrationTaskVO_>('/api/registrationTask/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listRegistrationTaskByPage POST /api/registrationTask/list/page */
export async function listRegistrationTaskByPageUsingPost1(
  body: API.RegistrationTaskQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageRegistrationTaskVO_>('/api/registrationTask/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateRegistrationTask POST /api/registrationTask/update */
export async function updateRegistrationTaskUsingPost1(
  body: API.RegistrationTaskUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/registrationTask/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
