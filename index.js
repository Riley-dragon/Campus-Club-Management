import request from './request'

// ==================== 认证 ====================
export const login = (data) => request.post('/auth/login', data)
export const register = (data) => request.post('/auth/register', data)
export const getCurrentUser = () => request.get('/auth/me')

// ==================== 社团 ====================
export const getClubs = (params) => request.get('/clubs', { params })
export const getClub = (id) => request.get(`/clubs/${id}`)
export const getMyClubs = () => request.get('/clubs/my')
export const createClub = (data) => request.post('/clubs', data)
export const updateClub = (id, data) => request.put(`/clubs/${id}`, data)

// ==================== 报名申请 ====================
export const submitApplication = (clubId, data) => request.post(`/applications?clubId=${clubId}`, data)
export const getMyApplications = () => request.get('/applications/my')
export const getPendingApplications = (clubId) => request.get(`/applications/pending/${clubId}`)
export const getClubApplications = (clubId) => request.get(`/applications/club/${clubId}`)
export const leaderReview = (id, pass, remark) =>
  request.put(`/applications/${id}/leader-review?pass=${pass}&remark=${encodeURIComponent(remark || '')}`)
export const getLeaderPassedApplications = () => request.get('/applications/leader-passed')
export const adminReview = (id, pass, remark) =>
  request.put(`/applications/${id}/admin-review?pass=${pass}&remark=${encodeURIComponent(remark || '')}`)

// ==================== 活动 ====================
export const getActivities = () => request.get('/activities')
export const getActivity = (id) => request.get(`/activities/${id}`)
export const getClubActivities = (clubId) => request.get(`/activities/club/${clubId}`)
export const createActivity = (clubId, data) => request.post(`/activities?clubId=${clubId}`, data)
export const updateActivity = (id, data) => request.put(`/activities/${id}`, data)
export const deleteActivity = (id) => request.delete(`/activities/${id}`)
export const registerActivity = (id) => request.post(`/activities/${id}/register`)
export const checkIn = (registrationId) => request.put(`/activities/registrations/${registrationId}/checkin`)
export const getActivityRegistrations = (id) => request.get(`/activities/${id}/registrations`)
export const getMyRegistrations = () => request.get('/activities/my-registrations')

// ==================== 管理员 ====================
export const getDashboard = () => request.get('/admin/dashboard')
export const getAdminClubs = () => request.get('/admin/clubs')
export const approveClub = (id, pass) => request.put(`/admin/clubs/${id}/approve?pass=${pass}`)
export const getUsers = () => request.get('/admin/users')
export const updateUserRole = (id, role) => request.put(`/admin/users/${id}/role?role=${role}`)
export const deleteUser = (id) => request.delete(`/admin/users/${id}`)
export const exportMembersUrl = (clubId) => `/api/admin/export/members/${clubId}`
