import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
	routes: [
		{
			path: '/init',
			name: 'init',
			component: () => import('./components/Index.vue')
		},
		{
			path: '*',
			name: 'error',
			component: () => import('./pages/Access.vue')
		}
	],
	scrollBehavior() {
		return {x: 0, y: 0};
	}
});
