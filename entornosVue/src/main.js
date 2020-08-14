import Vue from 'vue';
import App from './App.vue';
import router from './router';

import Button from 'primevue/button';
import Calendar from 'primevue/calendar';
import Card from 'primevue/card';
import Checkbox from 'primevue/checkbox';
import InputNumber from 'primevue/inputnumber';
import InputText from 'primevue/inputtext';
import SelectButton from 'primevue/selectbutton';
import Toast from 'primevue/toast';
import ToastService from 'primevue/toastservice';

import 'primevue/resources/primevue.min.css';
import 'primeflex/primeflex.css';
import 'primeicons/primeicons.css';

Vue.use(ToastService);

Vue.config.productionTip = false;

Vue.component('Button', Button);
Vue.component('Calendar', Calendar);
Vue.component('Card', Card);
Vue.component('Checkbox', Checkbox);
Vue.component('InputNumber', InputNumber);
Vue.component('InputText', InputText);
Vue.component('SelectButton', SelectButton);
Vue.component('Toast', Toast);

import Access from './pages/Access';
import Error from './pages/Error';
import Login from './pages/Login';
import NotFound from './pages/NotFound';

new Vue({
	el: '#app',
	computed: {
		ViewComponent () {
			switch (this.$route.path) {
				case '/':
					return Login;
				case '/access':
					return Access;
				case '/error':
					return Error;
				case '/notfound':
					return NotFound;
				default:
					return App;
			}
		}
	},
	router,
	render (h) { return h(this.ViewComponent) }
});
