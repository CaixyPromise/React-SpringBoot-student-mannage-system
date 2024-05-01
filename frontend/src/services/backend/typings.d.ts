declare namespace API {
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

  type BaseResponseInt_ = {
    code?: number;
    data?: number;
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

  type BaseResponseListDepartmentWithMajorsVO_ = {
    code?: number;
    data?: DepartmentWithMajorsVO[];
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

  type BaseResponsePagePost_ = {
    code?: number;
    data?: PagePost_;
    message?: string;
  };

  type BaseResponsePagePostVO_ = {
    code?: number;
    data?: PagePostVO_;
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

  type BaseResponsePageStudentInfo_ = {
    code?: number;
    data?: PageStudentInfo_;
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

  type BaseResponsePostVO_ = {
    code?: number;
    data?: PostVO;
    message?: string;
  };

  type BaseResponseString_ = {
    code?: number;
    data?: string;
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
    id?: number;
    majorId?: number;
    name?: string;
  };

  type ClassesInfoVO = {
    createTime?: string;
    creatorId?: number;
    departName?: string;
    id?: number;
    majorName?: string;
    name?: string;
    updateTime?: string;
  };

  type DeleteRequest = {
    id?: number;
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

  type getClassesInfoVOByIdUsingGET1Params = {
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

  type getPostVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getStudentGradesVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getStudentInfoVOByIdUsingGET1Params = {
    /** id */
    id?: number;
  };

  type getSubjectsVOByIdUsingGET1Params = {
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

  type LoginUserVO = {
    createTime?: string;
    id?: number;
    updateTime?: string;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
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

  type PagePost_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: Post[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PagePostVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: PostVO[];
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

  type PageStudentInfo_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: StudentInfo[];
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

  type Post = {
    content?: string;
    createTime?: string;
    favourNum?: number;
    id?: number;
    isDelete?: number;
    tags?: string;
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    userId?: number;
  };

  type PostAddRequest = {
    content?: string;
    tags?: string[];
    title?: string;
  };

  type PostEditRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type PostFavourAddRequest = {
    postId?: number;
  };

  type PostFavourQueryRequest = {
    current?: number;
    pageSize?: number;
    postQueryRequest?: PostQueryRequest;
    sortField?: string;
    sortOrder?: string;
    userId?: number;
  };

  type PostQueryRequest = {
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

  type PostThumbAddRequest = {
    postId?: number;
  };

  type PostUpdateRequest = {
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type PostVO = {
    content?: string;
    createTime?: string;
    favourNum?: number;
    hasFavour?: boolean;
    hasThumb?: boolean;
    id?: number;
    tagList?: string[];
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    user?: UserVO;
    userId?: number;
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
    content?: string;
    tags?: string[];
    title?: string;
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
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type StudentGradesVO = {
    content?: string;
    createTime?: string;
    favourNum?: number;
    hasFavour?: boolean;
    hasThumb?: boolean;
    id?: number;
    tagList?: string[];
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    user?: UserVO;
    userId?: number;
  };

  type StudentInfo = {
    id?: number;
    stuClassId?: number;
    stuDeptId?: number;
    stuMajorId?: number;
    stuName?: string;
    stuSex?: number;
  };

  type StudentInfoAddRequest = {
    stuClassId?: number;
    stuDeptId?: number;
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
    content?: string;
    id?: number;
    tags?: string[];
    title?: string;
  };

  type StudentInfoVO = true;

  type Subjects = {
    createTime?: string;
    creatorId?: number;
    id?: number;
    isDelete?: number;
    name?: string;
    updateTime?: string;
  };

  type SubjectsAddRequest = {
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
    id?: number;
    name?: string;
  };

  type SubjectsVO = {
    id?: string;
    name?: string;
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
