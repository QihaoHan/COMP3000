import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// Solves the issue of vue-router 3.0+ versions throwing errors on frequent menu clicks.
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',  // Redirect to homepage
    children: [
      { path: '403', name: 'NoAuth', meta: { name: 'No Permission' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: 'System Home Page' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: 'Admin Info' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: 'Personal Info' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: 'Change Password' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: 'Announcement Info' }, component: () => import('../views/manager/Notice') },
      { path: 'user', name: 'User', meta: { name: 'User Info' }, component: () => import('../views/manager/User') },
      { path: 'category', name: 'Category', meta: { name: 'News Category' }, component: () => import('../views/manager/Category') },
      { path: 'news', name: 'News', meta: { name: 'News Info' }, component: () => import('../views/manager/News') },
      { path: 'activity', name: 'Activity', meta: { name: 'Public Activities' }, component: () => import('../views/manager/Activity') },
      { path: 'serve', name: 'Serve', meta: { name: 'Elderly Care Service' }, component: () => import('../views/manager/Serve') },
      { path: 'nurseHouse', name: 'NurseHouse', meta: { name: 'Nursing Home Info' }, component: () => import('../views/manager/NurseHouse') },
      { path: 'comment', name: 'Comment', meta: { name: 'Comment Info' }, component: () => import('../views/manager/Comment') },
      { path: 'activitySign', name: 'ActivitySign', meta: { name: 'Activity Registration' }, component: () => import('../views/manager/ActivitySign') },
      { path: 'reserve', name: 'Reserve', meta: { name: 'Service Reservation' }, component: () => import('../views/manager/Reserve') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'FHome', meta: { name: 'System Home Page' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'FPerson', meta: { name: 'Personal Info' }, component: () => import('../views/front/Person') },
      { path: 'newsDetail', name: 'FNewsDetail', component: () => import('../views/front/NewsDetail') },
      { path: 'activityDetail', name: 'FActivityDetail', component: () => import('../views/front/ActivityDetail') },
      { path: 'activity', name: 'FActivity', component: () => import('../views/front/Activity') },
      { path: 'activitySign', name: 'FActivitySign', component: () => import('../views/front/ActivitySign') },
      { path: 'serve', name: 'FServe', component: () => import('../views/front/Serve') },
      { path: 'reserve', name: 'FReserve', component: () => import('../views/front/Reserve') },
      { path: 'nurseHouse', name: 'FNurseHouse', component: () => import('../views/front/NurseHouse') },
      { path: 'houseDetail', name: 'HouseDetail', component: () => import('../views/front/HouseDetail') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: 'Login' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: 'Register' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: 'Page Not Found' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
