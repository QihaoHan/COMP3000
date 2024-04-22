import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/css/global.css';
import '@/assets/css/theme/index.css';
import request from "@/utils/request";
import VueI18n from 'vue-i18n';
import enLocale from 'element-ui/lib/locale/lang/en'; // Import English locale

Vue.config.productionTip = false;

Vue.prototype.$request = request;
Vue.prototype.$baseUrl = process.env.VUE_APP_BASEURL;

// Register VueI18n
Vue.use(VueI18n);

// Configure VueI18n
const i18n = new VueI18n({
    locale: 'en', // Set locale to English
    messages: {
        en: enLocale
    }
});

// Use ElementUI with i18n
Vue.use(ElementUI, {
    i18n: (key, value) => i18n.t(key, value),
    size: "small"
});

new Vue({
    router,
    i18n, // Include i18n instance in Vue instance
    render: h => h(App)
}).$mount('#app');
