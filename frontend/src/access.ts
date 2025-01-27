/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { currentUser?: API.LoginUserVO } | undefined) {
  const { currentUser } = initialState ?? {};
  return {
    canUser: currentUser,
    canAdmin: currentUser && currentUser.userRole === 'admin',
    isStudent: currentUser && currentUser.userRole === "student",
    isTeacher: currentUser && currentUser.userRole === 'teacher'
  };
}
