// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addClassesToCourseSelection POST /api/courseSelection/addClasses */
export async function addClassesToCourseSelectionUsingPost1(
  body: API.ModifyCourseSelectionClassesRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/courseSelection/addClasses', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
