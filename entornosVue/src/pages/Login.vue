<template>
	<div class="login-body">
        <Toast />

		<div class="login-panel"></div>

		<div class="login-content">
			<img :src="logo" alt="babylon-layout"/>

			<h1><span>SIGN IN</span> TO Pagos</h1>
			<p>Welcome, please use the form to sign-in.</p>

			<div class="login-input-wrapper">
				<InputText placeholder="Username" v-model="user" />
			</div>

			<div class="login-input-wrapper">
				<InputText type="password" placeholder="Password" v-model="pass" />
			</div>

			<Button label="Sign In" icon="pi pi-check" />
		</div>
	</div>
</template>

<script>

import LoggerService from '../service/LoggerService';

export default {
	data() {
		return {
			logo: "assets/images/logoTS.SVG",
			user: 'demo',
			pass: 'test'
		}
	},
	loggerService: null,
	computed: {
	},
	created() {
		this.loggerService = new LoggerService();
		this.loggerService.info('Login created');
		this.loggerService.info('Login created end');
	},
	async mounted() {
		this.loggerService.info('Login mounted');
		this.loggerService.info('Login mounted end');
	},
	methods: {
	}
}
</script>

<style scoped>

</style>