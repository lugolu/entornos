<template>
	<div class="exception-body notfound">
		<div class="exception-panel"></div>

		<div class="exception-content">
			<img :src="logo" alt="babylon-layout"/>
			<h1><span class="exception-name">PAGE</span> NOT FOUND</h1>
			<p>Requested resource is not available.</p>
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