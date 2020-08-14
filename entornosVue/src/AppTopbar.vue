<template>
	<div class="layout-topbar clearfix">
		<button class="layout-topbar-logo p-link">
			<img id="layout-topbar-logo" :src="logo"/>
		</button>

		<button class="layout-menu-button p-link" @click="onMenuButtonClick">
			<i class="pi pi-bars"></i>
		</button>

		<button id="topbar-menu-button" class="p-link" @click="onTopbarMenuButtonClick">
			<i class="pi pi-ellipsis-v"></i>
		</button>

		<ul :class="topbarItemsClass">
			<li v-if="profileMode === 'popup' || horizontal"
				:class="['user-profile', {'active-topmenuitem': activeTopbarItem === 'profile'}]"
				@click="$emit('topbar-item-click',{originalEvent:$event,item:'profile'})">
				<button class="p-link">
					<img alt="babylon-layout" src="assets/layout/images/avatar.png"/>
					<span class="topbar-item-name">{{ usuario }}</span>
				</button>

				<transition name="layout-submenu-container">
					<ul class="fadeInDown" v-show="activeTopbarItem === 'profile'">
						<li role="menuitem">
							<button class="p-link">
								<i class="pi pi-user"></i>
								<span>Profile</span>
							</button>
						</li>
						<li role="menuitem">
							<button class="p-link">
								<i class="pi pi-cog"></i>
								<span>Settings</span>
							</button>
						</li>
					</ul>
				</transition>
			</li>

			<li :class="[{'active-topmenuitem': activeTopbarItem === 'settings'}]"
				@click="$emit('topbar-item-click',{originalEvent:$event,item:'settings'})">
				<button class="p-link" @click="index">
					<i class="topbar-icon pi pi-home"></i>
					<span class="topbar-item-name">Index</span>
				</button>
			</li>

			<li :class="[{'active-topmenuitem': activeTopbarItem === 'settings'}]"
				@click="$emit('topbar-item-click',{originalEvent:$event,item:'settings'})">
				<button class="p-link" @click="logout">
					<i class="topbar-icon pi pi-sign-out"></i>
					<span class="topbar-item-name">Logout</span>
				</button>
			</li>
		</ul>
	</div>
</template>

<script>

import LoggerService from './service/LoggerService';

	export default {
		data() {
			return {
				usuario: null
			}
		},
		loggerService: null,
		props: {
			topbarMenuActive: Boolean,
			activeTopbarItem: String,
			profileMode: String,
			horizontal: Boolean,
			titulo: String,
			logo: String,
			imgSource: String
		},
		components: {
		},
		methods: {
			index() {
				window.location = "/#/init"
			},
			logout() {
				window.location = "/#/"
			},
			onMenuButtonClick(event) {
				this.$emit('menubutton-click', event);
			},
			onTopbarMenuButtonClick(event) {
				this.$emit('topbar-menubutton-click', event);
			}
		},
		computed: {
			topbarItemsClass() {
				return ['topbar-menu fadeInDown', {
					'topbar-menu-visible': this.topbarMenuActive
				}];
			}
		},
		created() {
			this.loggerService = new LoggerService();
			this.loggerService.debug('AppTopbar created');
			this.loggerService.debug('AppTopbar created end');
		},
		async mounted() {
			this.loggerService.debug('AppTopbar mounted');
			this.loggerService.debug('AppTopbar mounted end');
		},
	}

</script>

<style scoped>

	#layout-menu-logo {
		margin: auto;
		vertical-align: middle;
		max-height: 50px;
		max-width: 100%;
	}

</style>
