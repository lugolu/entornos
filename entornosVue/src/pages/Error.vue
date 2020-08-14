<template>
	<div class="exception-body  error">
		<div class="exception-panel"></div>

		<div class="exception-content">
			<img :src="logo" alt="babylon-layout"/>
			<h1><span class="exception-name">ERROR</span> OCCURRED</h1>
			<p>Something went wrong.</p>
			<a @click="goDashboard">Back to Login</a>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				logo: "assets/images/logoTS.svg"
			}
		},
		methods: {
			goLogin(){
				window.location = "/#/"
			}
		}
	}
</script>

<style scoped>

</style>