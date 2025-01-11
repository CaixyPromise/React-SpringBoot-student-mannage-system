// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** getAllSubjectAnalyses GET /api/analysis/all */
export async function getAllSubjectAnalysesUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListSubjectAnalysis_>('/api/analysis/all', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getGradesAnalysisByFilter POST /api/analysis/depart */
export async function getGradesAnalysisByFilterUsingPost1(
  body: API.GradeAnalysisFilterDTO,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListSubjectAnalysis_>('/api/analysis/depart', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getAllSubjectAnalysesByStudentId GET /api/analysis/student */
export async function getAllSubjectAnalysesByStudentIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAllSubjectAnalysesByStudentIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseStudentAnalysisVO_>('/api/analysis/student', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
