declare namespace API {
  type activateTaskByIdUsingGET1Params = {
    /** taskId */
    taskId: number;
  };

  type AllClassesOptionDataVO = {
    children?: AllClassesOptionDataVO[];
    label?: string;
    value?: string;
  };

  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseClassesInfoVO_ = {
    code?: number;
    data?: ClassesInfoVO;
    message?: string;
  };

  type BaseResponseCourseSelectionInfoVO_ = {
    code?: number;
    data?: CourseSelectionInfoVO;
    message?: string;
  };

  type BaseResponseDepartmentInfoVO_ = {
    code?: number;
    data?: DepartmentInfoVO;
    message?: string;
  };

  type BaseResponseDepartmentWithMajorsVO_ = {
    code?: number;
    data?: DepartmentWithMajorsVO;
    message?: string;
  };

  type BaseResponseListAllClassesOptionDataVO_ = {
    code?: number;
    data?: AllClassesOptionDataVO[];
    message?: string;
  };

  type BaseResponseListClassesInfoVO_ = {
    code?: number;
    data?: ClassesInfoVO[];
    message?: string;
  };

  type BaseResponseListCourseSelectionInfoVO_ = {
    code?: number;
    data?: CourseSelectionInfoVO[];
    message?: string;
  };

  type BaseResponseListDepartMajorClassTreeVO_ = {
    code?: number;
    data?: DepartMajorClassTreeVO[];
    message?: string;
  };

  type BaseResponseListDepartmentWithMajorsVO_ = {
    code?: number;
    data?: DepartmentWithMajorsVO[];
    message?: string;
  };

  type BaseResponseListSemestersVO_ = {
    code?: number;
    data?: SemestersVO[];
    message?: string;
  };

  type BaseResponseListStudentCourseSubjectVO_ = {
    code?: number;
    data?: StudentCourseSubjectVO[];
    message?: string;
  };

  type BaseResponseListSubjectAnalysis_ = {
    code?: number;
    data?: SubjectAnalysis[];
    message?: string;
  };

  type BaseResponseListSubjectsVO_ = {
    code?: number;
    data?: SubjectsVO[];
    message?: string;
  };

  type BaseResponseLoginUserVO_ = {
    code?: number;
    data?: LoginUserVO;
    message?: string;
  };

  type BaseResponseLong_ = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseMajorInfoVO_ = {
    code?: number;
    data?: MajorInfoVO;
    message?: string;
  };

  type BaseResponsePageClassesInfo_ = {
    code?: number;
    data?: PageClassesInfo_;
    message?: string;
  };

  type BaseResponsePageClassesInfoVO_ = {
    code?: number;
    data?: PageClassesInfoVO_;
    message?: string;
  };

  type BaseResponsePageCourseSelectionInfo_ = {
    code?: number;
    data?: PageCourseSelectionInfo_;
    message?: string;
  };

  type BaseResponsePageCourseSelectionInfoVO_ = {
    code?: number;
    data?: PageCourseSelectionInfoVO_;
    message?: string;
  };

  type BaseResponsePageDepartmentInfo_ = {
    code?: number;
    data?: PageDepartmentInfo_;
    message?: string;
  };

  type BaseResponsePageMajorInfoWithDepartmentQueryVO_ = {
    code?: number;
    data?: PageMajorInfoWithDepartmentQueryVO_;
    message?: string;
  };

  type BaseResponsePageSemestersVO_ = {
    code?: number;
    data?: PageSemestersVO_;
    message?: string;
  };

  type BaseResponsePageStudentCourseSelection_ = {
    code?: number;
    data?: PageStudentCourseSelection_;
    message?: string;
  };

  type BaseResponsePageStudentCourseSubjectVO_ = {
    code?: number;
    data?: PageStudentCourseSubjectVO_;
    message?: string;
  };

  type BaseResponsePageStudentGrades_ = {
    code?: number;
    data?: PageStudentGrades_;
    message?: string;
  };

  type BaseResponsePageStudentGradesVO_ = {
    code?: number;
    data?: PageStudentGradesVO_;
    message?: string;
  };

  type BaseResponsePageStudentInfoVO_ = {
    code?: number;
    data?: PageStudentInfoVO_;
    message?: string;
  };

  type BaseResponsePageSubjects_ = {
    code?: number;
    data?: PageSubjects_;
    message?: string;
  };

  type BaseResponsePageSubjectsVO_ = {
    code?: number;
    data?: PageSubjectsVO_;
    message?: string;
  };

  type BaseResponsePageTeacherInfoVO_ = {
    code?: number;
    data?: PageTeacherInfoVO_;
    message?: string;
  };

  type BaseResponsePageUser_ = {
    code?: number;
    data?: PageUser_;
    message?: string;
  };

  type BaseResponsePageUserVO_ = {
    code?: number;
    data?: PageUserVO_;
    message?: string;
  };

  type BaseResponseSemestersVO_ = {
    code?: number;
    data?: SemestersVO;
    message?: string;
  };

  type BaseResponseString_ = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponseStudentAnalysisVO_ = {
    code?: number;
    data?: StudentAnalysisVO;
    message?: string;
  };

  type BaseResponseStudentCourseSubjectVO_ = {
    code?: number;
    data?: StudentCourseSubjectVO;
    message?: string;
  };

  type BaseResponseStudentGradesVO_ = {
    code?: number;
    data?: StudentGradesVO;
    message?: string;
  };

  type BaseResponseStudentInfoVO_ = {
    code?: number;
    data?: StudentInfoVO;
    message?: string;
  };

  type BaseResponseSubjectsVO_ = {
    code?: number;
    data?: SubjectsVO;
    message?: string;
  };

  type BaseResponseTeacherInfoVO_ = {
    code?: number;
    data?: TeacherInfoVO;
    message?: string;
  };

  type BaseResponseUser_ = {
    code?: number;
    data?: User;
    message?: string;
  };

  type BaseResponseUserVO_ = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type checkUsingGET1Params = {
    /** echostr */
    echostr?: string;
    /** nonce */
    nonce?: string;
    /** signature */
    signature?: string;
    /** timestamp */
    timestamp?: string;
  };

  type ClassesInfo = {
    createTime?: string;
    creatorId?: number;
    departId?: number;
    id?: number;
    isDelete?: number;
    majorId?: number;
    name?: string;
    updateTime?: string;
  };

  type ClassesInfoAddRequest = {
    departId?: number;
    grade?: number;
    majorId?: number;
    name?: string;
  };

  type ClassesInfoEditRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type ClassesInfoQueryRequest = {
    content?: string;
    current?: number;
    favourUserId?: number;
    id?: number;
    notId?: number;
    orTags?: string[];
    pageSize?: number;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type ClassesInfoQueryUnderMajorRequest = {
    departmentId?: number;
    majorId?: number;
  };

  type ClassesInfoUpdateRequest = {
    departId?: number;
    grade?: number;
    id?: number;
    majorId?: number;
    name?: string;
  };

  type ClassesInfoVO = {
    createTime?: string;
    creatorId?: number;
    departName?: string;
    grade?: number;
    id?: number;
    majorName?: string;
    name?: string;
    updateTime?: string;
  };

  type ClassesOptionVORequest = {
    excludeClassIds?: number[];
  };

  type ClassLeafVO = {
    classId?: number;
    className?: string;
  };

  type CourseSelectionInfo = {
    createTime?: string;
    creatorId?: number;
    endDate?: string;
    id?: number;
    isActive?: number;
    isDelete?: number;
    minCredit?: number;
    semesterId?: number;
    startDate?: string;
    taskName?: string;
    updateTime?: string;
  };

  type CourseSelectionInfoEditRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type CourseSelectionInfoQueryRequest = {
    content?: string;
    current?: number;
    id?: number;
    notId?: number;
    pageSize?: number;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type CourseSelectionInfoUpdateRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type CourseSelectionInfoVO = {
    createTime?: string;
    creatorId?: number;
    endDate?: string;
    id?: number;
    isActive?: number;
    isDelete?: number;
    minCredit?: number;
    semesterId?: number;
    semesterName?: string;
    startDate?: string;
    taskName?: string;
    updateTime?: string;
  };

  type CreateCourseSelectionRequest = {
    classIds?: number[];
    courseSettings?: SelectCourseData[];
    endDate?: string;
    minCredit?: number;
    semesterId?: number;
    startDate?: string;
    taskName?: string;
  };

  type DeleteRequest = {
    id?: number;
  };

  type DepartMajorClassTreeVO = {
    children?: MajorClassTreeVO[];
    departId?: number;
    departName?: string;
  };

  type DepartmentInfo = {
    createTime?: string;
    createUserId?: number;
    id?: number;
    isDelete?: number;
    name?: string;
    updateTime?: string;
  };

  type DepartmentInfoAddRequest = {
    name?: string;
  };

  type DepartmentInfoQueryRequest = {
    createTime?: string;
    createUserId?: number;
    current?: number;
    id?: number;
    name?: string;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    updateTime?: string;
  };

  type DepartmentInfoUpdateRequest = {
    id?: number;
    name?: string;
  };

  type DepartmentInfoVO = {
    majors?: string[];
    name?: string;
  };

  type DepartmentWithMajorsVO = {
    departmentId?: number;
    departmentName?: string;
    majors?: MajorInnerInfo[];
  };

  type getAllSelectionClassesTreeUsingGET1Params = {
    /** courseSelectionId */
    courseSelectionId: number;
  };

  type getAllSubjectAnalysesByStudentIdUsingGET1Params = {
    /** studentId */
    studentId: number;
  };

  type getClassesInfoVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getCourseSelectionInfoVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getDepartmentInfoVOByIdUsingGET1Params = {
    /** id */
    id: number;
  };

  type getMajorInfoVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getMajorUnderDepartmentUsingGET1Params = {
    /** departmentId */
    departmentId: number;
  };

  type getSelectTaskCoursesByTaskIdUsingGET1Params = {
    /** taskId */
    taskId: number;
  };

  type getSemestersVOByIdUsingGET1Params = {
    /** id */
    id: number;
  };

  type getStudentCourseSelectionVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getStudentGradesVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getStudentGradesVOByStuIdUsingGET1Params = {
    /** stuId */
    stuId: number;
  };

  type getStudentInfoVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getSubjectsVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getTeacherInfoVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getUserByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getUserVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type GradeAnalysisFilterDTO = {
    classIds?: number[];
    departmentIds?: number[];
    majorIds?: number[];
    stuSex?: number;
    subjectIds?: number[];
  };

  type GradeItem = {
    grade?: number;
    gradeId?: number;
    subjectId?: number;
    subjectName?: string;
  };

  type listAvailableSubjectsUsingGET1Params = {
    /** courseSelectionId */
    courseSelectionId: number;
  };

  type LoginUserVO = {
    createTime?: string;
    id?: number;
    updateTime?: string;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type MajorClassTreeVO = {
    children?: ClassLeafVO[];
    majorId?: number;
    majorName?: string;
  };

  type MajorInfoAddRequest = {
    departmentId?: number;
    name?: string;
  };

  type MajorInfoQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type MajorInfoUpdateRequest = {
    departId?: number;
    id?: number;
    name?: string;
  };

  type MajorInfoVO = {
    departmentId?: number;
    departmentName?: string;
    majorId?: number;
    name?: string;
  };

  type MajorInfoWithDepartmentQueryVO = {
    createTime?: string;
    createUserId?: number;
    departmentId?: string;
    departmentName?: string;
    majorId?: number;
    majorName?: string;
    updateTime?: string;
  };

  type MajorInnerInfo = {
    majorId?: number;
    majorName?: string;
  };

  type ModifyCourseSelectionClassesRequest = {
    classIds?: number[];
    courseSelectionId?: number;
  };

  type OrderItem = {
    asc?: boolean;
    column?: string;
  };

  type PageClassesInfo_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: ClassesInfo[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageClassesInfoVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: ClassesInfoVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageCourseSelectionInfo_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: CourseSelectionInfo[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageCourseSelectionInfoVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: CourseSelectionInfoVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type pageCourseSelectionUsingGET1Params = {
    /** pageNum */
    pageNum?: number;
    /** pageSize */
    pageSize?: number;
    /** semesterId */
    semesterId?: number;
    /** taskName */
    taskName?: string;
  };

  type PageDepartmentInfo_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: DepartmentInfo[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageMajorInfoWithDepartmentQueryVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: MajorInfoWithDepartmentQueryVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageSemestersVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: SemestersVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageStudentCourseSelection_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: StudentCourseSelection[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageStudentCourseSubjectVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: StudentCourseSubjectVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageStudentGrades_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: StudentGrades[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageStudentGradesVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: StudentGradesVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageStudentInfoVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: StudentInfoVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageSubjects_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: Subjects[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageSubjectsVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: SubjectsVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageTeacherInfoVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: TeacherInfoVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUser_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: User[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUserVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: UserVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type putTaskHoldByIdUsingGET1Params = {
    /** taskId */
    taskId: number;
  };

  type SelectCourseData = {
    courseId?: number;
    maxStudents?: number;
  };

  type SemestersAddRequest = {
    endDate?: string;
    name?: string;
    startDate?: string;
  };

  type SemestersQueryRequest = {
    current?: number;
    endDateBegin?: string;
    endDateEnd?: string;
    id?: number;
    isActive?: number;
    name?: string;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    startDateBegin?: string;
    startDateEnd?: string;
  };

  type SemestersSetActiveRequest = {
    id?: number;
  };

  type SemestersUpdateRequest = {
    endDate?: string;
    id?: number;
    name?: string;
    startDate?: string;
  };

  type SemestersVO = {
    endDate?: string;
    id?: number;
    isActive?: number;
    name?: string;
    startDate?: string;
  };

  type StudentAnalysisVO = {
    studentGrade?: StudentGradesVO;
    subjectAnalysis?: SubjectAnalysis[];
  };

  type StudentCourseSelection = {
    courseSelectionId?: number;
    id?: number;
    isDelete?: number;
    selectTime?: string;
    studentId?: number;
    subjectId?: number;
  };

  type StudentCourseSelectionAddRequest = {
    content?: string;
    tags?: string[];
    title?: string;
  };

  type StudentCourseSelectionEditRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type StudentCourseSelectionQueryRequest = {
    content?: string;
    current?: number;
    id?: number;
    notId?: number;
    pageSize?: number;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type StudentCourseSelectionUpdateRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type StudentCourseSubjectVO = {
    enrolledCount?: number;
    full?: boolean;
    gradeCredit?: number;
    maxStudents?: number;
    selected?: boolean;
    subjectId?: number;
    subjectName?: string;
  };

  type StudentGrades = {
    createTime?: string;
    creatorId?: number;
    grade?: number;
    id?: number;
    isDelete?: number;
    stuId?: number;
    subjectId?: number;
    updateTime?: string;
  };

  type StudentGradesAddRequest = {
    score?: number;
    studentId?: number;
    subjectId?: number;
  };

  type StudentGradesEditRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type StudentGradesQueryRequest = {
    content?: string;
    current?: number;
    favourUserId?: number;
    id?: number;
    notId?: number;
    orTags?: string[];
    pageSize?: number;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type StudentGradesUpdateRequest = {
    grade?: number;
    id?: number;
  };

  type StudentGradesVO = {
    gradeItem?: GradeItem[];
    stuId?: number;
    studentInfo?: StudentInfoVO;
  };

  type StudentInfoAddRequest = {
    stuClassId?: number;
    stuDeptId?: number;
    stuId?: string;
    stuMajorId?: number;
    stuName?: string;
    stuSex?: number;
  };

  type StudentInfoEditRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type StudentInfoQueryRequest = {
    content?: string;
    current?: number;
    favourUserId?: number;
    id?: number;
    notId?: number;
    orTags?: string[];
    pageSize?: number;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type StudentInfoUpdateRequest = {
    id?: number;
    stuClassId?: number;
    stuDeptId?: number;
    stuMajorId?: number;
    stuName?: string;
    stuSex?: number;
  };

  type StudentInfoVO = {
    id?: number;
    stuClass?: string;
    stuClassId?: number;
    stuDepart?: string;
    stuDeptId?: number;
    stuMajor?: string;
    stuMajorId?: number;
    stuName?: string;
    stuSex?: number;
  };

  type StudentSelectCourseRequest = {
    courseSelectionId?: number;
    subjectIds?: number[];
  };

  type SubjectAnalysis = {
    averageScore?: number;
    excellentCount?: number;
    excellentRate?: number;
    failureCount?: number;
    failureRate?: number;
    highestScore?: number;
    highestScoreStudentName?: StudentInfoVO;
    subjectExcellentLevel?: number;
    subjectFailureLevel?: number;
    subjectId?: number;
    subjectName?: string;
  };

  type Subjects = {
    courseType?: number;
    createTime?: string;
    creatorId?: number;
    creditHours?: number;
    gradeCredit?: number;
    gradeExcellent?: number;
    gradeFail?: number;
    gradeMax?: number;
    gradeMin?: number;
    id?: number;
    isDelete?: number;
    name?: string;
    updateTime?: string;
  };

  type SubjectsAddRequest = {
    courseType?: number;
    creditHours?: number;
    gradeExcellent?: number;
    gradeFail?: number;
    gradeMax?: number;
    gradeMin?: number;
    name?: string;
  };

  type SubjectsEditRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type SubjectsQueryRequest = {
    content?: string;
    current?: number;
    favourUserId?: number;
    id?: number;
    notId?: number;
    orTags?: string[];
    pageSize?: number;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
    userId?: number;
  };

  type SubjectsUpdateRequest = {
    courseType?: number;
    creditHours?: number;
    gradeCredit?: number;
    gradeExcellent?: number;
    gradeFail?: number;
    gradeMax?: number;
    gradeMin?: number;
    id?: number;
    name?: string;
  };

  type SubjectsVO = {
    courseType?: number;
    creditHours?: number;
    gradeCredit?: number;
    gradeExcellent?: number;
    gradeFail?: number;
    gradeMax?: number;
    gradeMin?: number;
    id?: number;
    name?: string;
  };

  type TeacherInfoAddRequest = {
    teacherDeptId?: number;
    teacherId?: string;
    teacherMajorId?: number;
    teacherName?: string;
    teacherSex?: number;
  };

  type TeacherInfoEditRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type TeacherInfoQueryRequest = {
    content?: string;
    creatorId?: number;
    current?: number;
    id?: number;
    notId?: number;
    pageSize?: number;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    tags?: string[];
    title?: string;
  };

  type TeacherInfoUpdateRequest = {
    id?: number;
    teacherDeptId?: number;
    teacherMajorId?: number;
    teacherName?: string;
    teacherSex?: number;
  };

  type TeacherInfoVO = {
    id?: number;
    teacherDepart?: string;
    teacherDeptId?: number;
    teacherId?: string;
    teacherMajor?: string;
    teacherMajorId?: number;
    teacherName?: string;
    teacherSex?: number;
  };

  type uploadFileUsingPOST1Params = {
    biz?: string;
  };

  type User = {
    createTime?: string;
    id?: number;
    isDelete?: number;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userClass?: number;
    userDepartment?: number;
    userEmail?: string;
    userMajor?: number;
    userName?: string;
    userPassword?: string;
    userPhone?: string;
    userProfile?: string;
    userRole?: string;
    userRoleLevel?: number;
    userSex?: number;
    userTags?: string;
  };

  type UserAddRequest = {
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type UserLoginRequest = {
    userAccount?: string;
    userPassword?: string;
  };

  type UserQueryRequest = {
    current?: number;
    id?: number;
    mpOpenId?: string;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    unionId?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserRegisterRequest = {
    checkPassword?: string;
    userAccount?: string;
    userPassword?: string;
  };

  type UserUpdateMyRequest = {
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
  };

  type UserUpdateRequest = {
    id?: number;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserVO = {
    createTime?: string;
    id?: number;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };
}
