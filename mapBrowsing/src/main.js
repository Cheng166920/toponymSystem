import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// import print from 'vue3-print-nb'

const app = createApp(App);
app.use(ElementPlus);
// app.use(print);

app.mount('#app')
app.config.globalProperties.$axios = axios
axios.defaults.baseURL = '/' 