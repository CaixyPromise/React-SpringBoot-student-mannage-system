declare namespace API {
  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
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

  type BaseResponseListDepartmentWithMajorsVO_ = {
    code?: number;
    data?: DepartmentWithMajorsVO[];
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

  type BaseResponseStudentInfoVO_ = {
    code?: number;
    data?: StudentInfoVO;
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

  type checkUsingGETParams = {
    /** echostr */
    echostr?: string;
    /** nonce */
    nonce?: string;
    /** signature */
    signature?: string;
    /** timestamp */
    timestamp?: string;
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

  type getDepartmentInfoVOByIdUsingGETParams = {
    /** id */
    id: number;
  };

  type getMajorInfoVOByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getMajorUnderDepartmentUsingGETParams = {
    /** departmentId */
    departmentId: number;
  };

  type getPostVOByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getPostVOByIdUsingGET3Params = {
    /** id */
    id?: number;
  };

  type getStudentInfoVOByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getUserByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getUserVOByIdUsingGETParams = {
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

  type StudentInfo = {
    id?: number;
    stuClassId?: number;
    stuDeptId?: number;
    stuMajorId?: number;
    stuName?: string;
    stuSex?: number;
  };

  type StudentInfoAddRequest = {
    content?: string;
    tags?: string[];
    title?: string;
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

  type uploadFileUsingPOSTParams = {
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
