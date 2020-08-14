<template>
	<div class="exception-body access-denied">
		<div class="exception-panel"></div>

		<div class="exception-content">
			<img :src="logo" alt="babylon-layout"/>
			<h1><span class="exception-name">ACCESS</span> DENIED</h1>
			<p>You don't have the necessary permissions.</p>
			<a @click="goLogin">Back to Login</a>
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