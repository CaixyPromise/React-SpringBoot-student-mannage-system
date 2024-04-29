// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addMajorInfo POST /api/major/add */
export async function addMajorInfoUsingPOST(
  body: API.MajorInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/major/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteMajorInfo POST /api/major/delete */
export async function deleteMajorInfoUsingPOST(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/major/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** downloadTemplate GET /api/major/download/template/major */
export async function downloadTemplateUsingGET(options?: { [key: string]: any }) {
  return request<any>('/api/major/download/template/major', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getAllDepartmentAndMajor GET /api/major/get/all */
export async function getAllDepartmentAndMajorUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponseListDepartmentWithMajorsVO_>('/api/major/get/all', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getMajorInfoVOById GET /api/major/get/vo */
export async function getMajorInfoVoByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMajorInfoVOByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseMajorInfoVO_>('/api/major/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listMajorInfoByPage POST /api/major/list/page */
export async function listMajorInfoByPageUsingPOST(
  body: API.MajorInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageMajorInfoWithDepartmentQueryVO_>('/api/major/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateMajorInfo POST /api/major/update */
export async function updateMajorInfoUsingPOST(
  body: API.MajorInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/major/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** upload POST /api/major/upload */
export async function uploadUsingPOST(body: {}, file?: File, options?: { [key: string]: any }) {
  const formData = new FormData();

  if (file) {
    formData.append('file', file);
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele];

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''));
        } else {
          formData.append(ele, JSON.stringify(item));
        }
      } else {
        formData.append(ele, item);
      }
    }
  });

  return request<API.BaseResponseString_>('/api/major/upload', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}
