<template>
	<div :class="['layout-profile', {'layout-profile-active': this.expanded}]">
		<button class="layout-profile-button p-link" @click="onClick">
			<img src="assets/layout/images/avatar.png" alt="babylon-layout"/>
			<div class="layout-profile-userinfo">
				<span class="layout-profile-name">{{ usuario }}</span>
			</div>
			<i class="layout-profile-icon pi pi-angle-down"></i>
		</button>

		<transition name="layout-profile-menu">
			<ul class="layout-profile-menu" v-show="expanded">
				<li role="menuitem">
					<button class="p-link" :tabIndex="expanded ? null : '-1'">
						<i class="pi pi-user"></i>
						<span>Profile</span>
					</button>
				</li>
				<li role="menuitem">
					<button class="p-link" :tabIndex="expanded ? null : '-1'">
						<i class="pi pi-cog"></i>
						<span>Settings</span>
					</button>
				</li>
			</ul>
		</transition>
	</div>
</template>

<script>


	export default {
		data() {
			return {
				usuario: null
			}
		},
		props: {
			expanded: {
				type: Boolean,
				default: null
			}
		},
		methods: {
			onClick(event) {
				this.$emit('profile-click', event);
				event.preventDefault();
			}
		},
		mounted() {
		},
		name: "AppInlineProfile"
	}
</script>

<style scoped>

</style>
