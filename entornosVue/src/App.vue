<template>
	<div :class="containerClass" @click="onDocumentClick">
		<AppTopBar :topbarMenuActive="topbarMenuActive" :activeTopbarItem="activeTopbarItem" :horizontal="isHorizontal()" :profileMode="profileMode"
					@menubutton-click="onMenuButtonClick" @topbar-menubutton-click="onTopbarMenuButtonClick" @topbar-item-click="onTopbarItemClick"
					:titulo="titulo" :logo="logo"></AppTopBar>

		<transition name="layout-menu-container">
			<div class="layout-menu-container" @click="onMenuClick" v-show="isMenuVisible()">
				<div class="layout-menu-logo">
					<button class="p-link">
						<img id="layout-menu-logo" :src="logo" alt="logo"/>
					</button>
				</div>
				<div class="layout-menu-wrapper">
					<div class="menu-scroll-content">
						<AppInlineProfile v-if="displayInlineProfile" @profile-click="onProfileClick" :expanded="profileExpanded"></AppInlineProfile>
						<AppMenu :model="grouped ? menuGrouped : menuUngrouped" :layoutMode="layoutMode" :active="menuActive" @menuitem-click="onMenuItemClick" @root-menuitem-click="onRootMenuItemClick"></AppMenu>
					</div>
				</div>
			</div>
		</transition>

		<div class="layout-main">
			<AppBreadcrumb></AppBreadcrumb>

			<div class="layout-content">
				<router-view @title-change="onTitleChange" />
			</div>
		</div>

		<AppFooter/>

		<div v-if="staticMenuMobileActive" class="layout-mask"></div>
	</div>
</template>

<script>
import AppTopBar from './AppTopbar.vue';
import AppInlineProfile from './AppInlineProfile.vue';
import AppMenu from './AppMenu.vue';
import AppBreadcrumb from './AppBreadcrumb.vue';
import AppFooter from './AppFooter.vue';
import EventBus from './event-bus';

import LoggerService from './service/LoggerService';

export default {
	data() {
		return {
			user: null,
			titulo: 'Entornos',
			logo: null,//"assets/images/logoTS.SVG",
			layoutMode: 'static',
			staticMenuDesktopInactive: false,
			staticMenuMobileActive: false,
			overlayMenuActive: false,
			topbarMenuActive: false,
			activeTopbarItem: null,
			menuActive: false,
			darkMenu: true,
			profileMode: 'inline',
			profileExpanded: false,
			grouped: true,
			theme: ['cyan', 'accent'],
			themeColors: [
				{name:'Amber Accent', color:'amber', file:'accent', image:'amber-accent.svg'},
				{name:'Amber Light', color:'amber', file:'light', image:'amber-light.svg'},
				{name:'Amber Dark', color:'amber', file:'dark', image:'amber-dark.svg'},
				{name:'Blue Accent', color:'blue', file:'accent', image:'blue-accent.svg'},
				{name:'Blue Light', color:'blue', file:'light', image:'blue-light.svg'},
				{name:'Blue Dark', color:'blue', file:'dark', image:'blue-dark.svg'},
				{name:'Blue Grey Accent', color:'bluegrey', file:'accent', image:'bluegrey-accent.svg'},
				{name:'Blue Grey Light', color:'bluegrey', file:'light', image:'bluegrey-light.svg'},
				{name:'Blue Grey Dark', color:'bluegrey', file:'dark', image:'bluegrey-dark.svg'},
				{name:'Brown Accent', color:'brown', file:'accent', image:'brown-accent.svg'},
				{name:'Brown Light', color:'brown', file:'light', image:'brown-light.svg'},
				{name:'Brown Dark', color:'brown', file:'dark', image:'brown-dark.svg'},
				{name:'Cyan Accent', color:'cyan', file:'accent', image:'cyan-accent.svg'},
				{name:'Cyan Light', color:'cyan', file:'light', image:'cyan-light.svg'},
				{name:'Cyan Dark', color:'cyan', file:'dark', image:'cyan-dark.svg'},
				{name:'Deep Orange Accent', color:'deeporange', file:'accent', image:'deeporange-accent.svg'},
				{name:'Deep Orange Light', color:'deeporange', file:'light', image:'deeporange-light.svg'},
				{name:'Deep Orange Dark', color:'deeporange', file:'dark', image:'deeporange-dark.svg'},
				{name:'Deep Purple Accent', color:'deeppurple', file:'accent', image:'deeppurple-accent.svg'},
				{name:'Deep Purple Light', color:'deeppurple', file:'light', image:'deeppurple-light.svg'},
				{name:'Deep Purple Dark', color:'deeppurple', file:'dark', image:'deeppurple-dark.svg'},
				{name:'Green Accent', color:'green', file:'accent', image:'green-accent.svg'},
				{name:'Green Light', color:'green', file:'light', image:'green-light.svg'},
				{name:'Green Dark', color:'green', file:'dark', image:'green-dark.svg'},
				{name:'Indigo Accent', color:'indigo', file:'accent', image:'indigo-accent.svg'},
				{name:'Indigo Light', color:'indigo', file:'light', image:'indigo-light.svg'},
				{name:'Indigo Dark', color:'indigo', file:'dark', image:'indigo-dark.svg'},
				{name:'Light Blue Accent', color:'lightblue', file:'accent', image:'lightblue-accent.svg'},
				{name:'Light Blue Light', color:'lightblue', file:'light', image:'lightblue-light.svg'},
				{name:'Light Blue Dark', color:'lightblue', file:'dark', image:'lightblue-dark.svg'},
				{name:'Light Green Accent', color:'lightgreen', file:'accent', image:'lightgreen-accent.svg'},
				{name:'Light Green Light', color:'lightgreen', file:'light', image:'lightgreen-light.svg'},
				{name:'Light Green Dark', color:'lightgreen', file:'dark', image:'lightgreen-dark.svg'},
				{name:'Lime Accent', color:'lime', file:'accent', image:'lime-accent.svg'},
				{name:'Lime Light', color:'lime', file:'light', image:'lime-light.svg'},
				{name:'Lime Dark', color:'lime', file:'dark', image:'lime-dark.svg'},
				{name:'Orange Accent', color:'orange', file:'accent', image:'orange-accent.svg'},
				{name:'Orange Light', color:'orange', file:'light', image:'orange-light.svg'},
				{name:'Orange Dark', color:'orange', file:'dark', image:'orange-dark.svg'},
				{name:'Pink Accent', color:'pink', file:'accent', image:'pink-accent.svg'},
				{name:'Pink Light', color:'pink', file:'light', image:'pink-light.svg'},
				{name:'Pink Dark', color:'pink', file:'dark', image:'pink-dark.svg'},
				{name:'Purple Accent', color:'purple', file:'accent', image:'purple-accent.svg'},
				{name:'Purple Light', color:'purple', file:'light', image:'purple-light.svg'},
				{name:'Purple Dark', color:'purple', file:'dark', image:'purple-dark.svg'},
				{name:'Teal Accent', color:'teal', file:'accent', image:'teal-accent.svg'},
				{name:'Teal Light', color:'teal', file:'light', image:'teal-light.svg'},
				{name:'Teal Dark', color:'teal', file:'dark', image:'teal-dark.svg'},
				{name:'Yellow Accent', color:'yellow', file:'accent', image:'yellow-accent.svg'},
				{name:'Yellow Light', color:'yellow', file:'light', image:'yellow-light.svg'},
				{name:'Yellow Dark', color:'yellow', file:'dark', image:'yellow-dark.svg'},
			],
			menu: Array,
			menuGrouped: Array,
			menuUngrouped: Array
		}
	},
	loggerService: null,
	props: {
	},
	created: async function(){
		this.loggerService = new LoggerService();
		this.loggerService.info('App created');
		this.menu = [];

		this.menuGrouped = [
			{
                label: 'Entornos', icon: 'pi pi-fw pi-home',
				items: this.menu
			},
			{
                label: 'Customization', icon: 'pi pi-fw pi-cog',
				items: [
					{	label: 'Menu Layouts', icon: 'pi pi-fw pi-th-large', badge: 2,
						items: [
							{label: 'Static Menu', icon: 'pi pi-fw pi-bars', command: () => this.layoutMode = 'static'},
							{label: 'Overlay Menu', icon: 'pi pi-fw pi-bars', command: () => this.layoutMode = 'overlay'},
							{label: 'Slim Menu', icon: 'pi pi-fw pi-bars', command: () => this.layoutMode = 'slim'},
							{label: 'Horizontal Menu', icon: 'pi pi-fw pi-bars', command: () => this.layoutMode = 'horizontal'},
							{label: 'Grouped Menu', icon: 'pi pi-fw pi-bars', command: () => this.grouped = true},
							{label: 'Ungrouped Menu', icon: 'pi pi-fw pi-bars', command: () => this.grouped = false}
						]
					},
					{
						label: 'Menu Colors', icon: 'pi pi-fw pi-list', badge: 2,
						items: [
							{ label: 'Light', icon: 'pi pi-fw pi-circle-off', command: () => this.darkMenu = false},
							{ label: 'Dark', icon: 'pi pi-fw pi-circle-on', command: () => this.darkMenu = true}
						]
					},
					{
                        label: 'User Profile', icon: 'pi pi-fw pi-user', badge: 2,
						items: [
							{label: 'Popup Profile', icon: 'pi pi-fw pi-user',  command: () => this.profileMode = 'popup'},
							{label: 'Inline Profile', icon: 'pi pi-fw pi-user',  command: () => this.profileMode = 'inline'}
						]
					},
					{
						label: 'Themes', icon: 'pi pi-fw pi-pencil', badge: 17,
						items: [
							{
								label: 'Blue', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('blue', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('blue', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('blue', 'dark')
									}
								]
							},
							{
								label: 'Blue Grey', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('bluegrey', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('bluegrey', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('bluegrey', 'dark')
									}
								]
							},
							{
								label: 'Light Blue', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('lightblue', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('lightblue', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('lightblue', 'dark')
									}
								]
							},
							{
								label: 'Indigo', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('indigo', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('indigo', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('indigo', 'dark')
									}
								]
							},
							{
								label: 'Pink', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('pink', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('pink', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('pink', 'dark')
									}
								]
							},
							{
								label: 'Green', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('green', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('green', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('green', 'dark')
									}
								]
							},
							{
								label: 'Light Green', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('lightgreen', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('lightgreen', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('lightgreen', 'dark')
									}
								]
							},
							{
								label: 'Teal', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('teal', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('teal', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('teal', 'dark')
									}
								]
							},
							{
								label: 'Cyan', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('cyan', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('cyan', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('cyan', 'dark')
									}
								]
							},
							{
								label: 'Lime', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('lime', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('lime', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('lime', 'dark')
									}
								]
							},
							{
								label: 'Amber', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('amber', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('amber', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('amber', 'dark')
									}
								]
							},
							{
								label: 'Orange', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('orange', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('orange', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('orange', 'dark')
									}
								]
							},
							{
								label: 'Deep Orange', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('deeporange', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('deeporange', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('deeporange', 'dark')
									}
								]
							},
							{
								label: 'Yellow', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('yellow', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('yellow', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('yellow', 'dark')
									}
								]
							},
							{
								label: 'Purple', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('purple', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('purple', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('purple', 'dark')
									}
								]
							},
							{
								label: 'Deep Purple', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('deeppurple', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('deeppurple', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('deeppurple', 'dark')
									}
								]
							},
							{
								label: 'Brown', icon: 'pi pi-fw pi-pencil',
								items: [
									{
										label: 'Accent', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('brown', 'accent')
									},
									{
										label: 'Light', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('brown', 'light')
									},
									{
										label: 'Dark', icon: 'pi pi-fw pi-pencil',
										command: () => this.changeTheme('brown', 'dark')
									}
								]
							}
						]
					}
				]
			}
		];
		this.menuUngrouped = [
			{
				label: 'Main Menu',
				icon: 'pi pi-fw pi-home',
				items: this.menuGrouped
			}
		]
		this.loggerService.info('App created end');
	},
	mounted: function() {
		this.loggerService.debug('App mounted');
		this.loggerService.debug('App mounted end');
	},
	watch: {
		$route() {
			this.menuActive = false;
			this.$toast.removeAllGroups();
		}
	},
	methods: {
		accion(accion) {
			window.location = "/#/" + accion;
		},
		onTitleChange(title) {
			this.titulo = title;
		},
		onLogoChange(logo) {
			this.logo = logo;
		},
		onDocumentClick() {
			if (!this.topbarItemClick) {
				this.activeTopbarItem = null;
				this.topbarMenuActive = false;
			}

			if (!this.menuClick) {
				if (this.isHorizontal() || this.isSlim()) {
					EventBus.$emit('reset_active_index');
					this.menuActive = false;
				}
				this.hideOverlayMenu();
			}

			if(!this.profileClick) {
				if (this.isSlim()) {
					this.profileExpanded = false;
				}
			}

			this.topbarItemClick = false;
			this.menuClick = false;
			this.profileClick = false;
		},
		onMenuButtonClick(event) {
			this.menuClick = true;
			this.topbarMenuActive = false;

			if (this.layoutMode === 'overlay') {
				if (this.isDesktop())
					this.overlayMenuActive = !this.overlayMenuActive;
				else
					this.staticMenuMobileActive = !this.staticMenuMobileActive;
			}
			else {
				if (this.isDesktop())
					this.staticMenuDesktopInactive = !this.staticMenuDesktopInactive;
				else
					this.staticMenuMobileActive = !this.staticMenuMobileActive;
			}

			event.preventDefault();
		},
		onTopbarMenuButtonClick(event) {
			this.topbarItemClick = true;
			this.topbarMenuActive = !this.topbarMenuActive;
			this.hideOverlayMenu();
			event.preventDefault();
		},
		onTopbarItemClick(event) {
			this.topbarItemClick = true;

			if (this.activeTopbarItem === event.item)
				this.activeTopbarItem = null;
			else
				this.activeTopbarItem = event.item;

			event.originalEvent.preventDefault();
		},
		onMenuClick() {
			this.menuClick = true;
		},
		isMenuVisible() {
			if (this.isDesktop()) {
				if (this.layoutMode === 'static')
					return !this.staticMenuDesktopInactive;
				else if (this.layoutMode === 'overlay')
					return this.overlayMenuActive;
				else
					return true;
			} else {
				return true;
			}
		},
		onProfileClick(event) {
			this.profileClick = true;
			this.profileExpanded = !this.profileExpanded;
			if(this.isHorizontal() || this.isSlim()) {
				EventBus.$emit('reset_active_index');
			}

			event.preventDefault();
		},
		onLayoutChange(layoutMode) {
			this.layoutMode = layoutMode;
			this.staticMenuDesktopInactive = false;
			this.overlayMenuActive = false;

			if(this.isHorizontal()) {
				this.profileMode = 'popup'
			}
		},
		onMenuTypeChange(menuType) {
			this.grouped = menuType;
		},
		onMenuColorChange(menuColor) {
			this.darkMenu = menuColor;
		},
		onProfileModeChange(profileMode) {
			this.profileMode = profileMode;
		},
		changeTheme(theme, scheme) {
			this.changeStyleSheetUrl('layout-css', theme, 'layout', scheme);
			this.changeStyleSheetUrl('theme-css', theme, 'theme', scheme);
		},
		changeStyleSheetUrl(id, value, prefix, scheme) {
			let element = document.getElementById(id);
			let urlTokens = element.getAttribute('href').split('/');

			if(id.localeCompare('layout-css') === 0) {
				urlTokens[urlTokens.length - 1] = prefix + '-' + value + '.css';
			}
			else {
				urlTokens[urlTokens.length - 2] = value ;
				urlTokens[urlTokens.length - 1] = 'theme-' + scheme +  '.css' ;
			}
			let newURL = urlTokens.join('/');
			this.replaceLink(element, newURL);

			if (scheme === 'dark') {
				this.darkMenu = true;
			} else if (scheme === 'light') {
				this.darkMenu = false;
			}

			this.theme = [value, scheme];
		},
		replaceLink(linkElement, href) {
			const id = linkElement.getAttribute('id');
			const cloneLinkElement = linkElement.cloneNode(true);

			cloneLinkElement.setAttribute('href', href);
			cloneLinkElement.setAttribute('id', id + '-clone');

			linkElement.parentNode.insertBefore(cloneLinkElement, linkElement.nextSibling);

			cloneLinkElement.addEventListener('load', () => {
				linkElement.remove();
				cloneLinkElement.setAttribute('id', id);
			});
		},
		isHorizontal() {
			return this.layoutMode === 'horizontal';
		},
		isSlim() {
			return this.layoutMode === 'slim';
		},
		hideOverlayMenu() {
			this.overlayMenuActive = false;
			this.staticMenuMobileActive = false;
		},
		isDesktop() {
			return window.innerWidth > 1024;
		},
		isMobile() {
			return window.innerWidth <= 640;
		},
		onMenuItemClick(event) {
			if (!event.item.items) {
				EventBus.$emit('reset_active_index');
				this.hideOverlayMenu();
			}
			if (!event.item.items && (this.isHorizontal() || this.isSlim())) {
				this.menuActive = false;
			}
		},
		onRootMenuItemClick() {
			this.menuActive = !this.menuActive;
		},
		getTitle() {
			this.titulo = parent.$titulo;
			if (this.titulo) {
				return this.titulo;
			}
			else {
				return "Entornos";
			}
		}
	},
	computed: {
		containerClass() {
			return ['layout-wrapper', {
				'layout-horizontal': this.layoutMode === 'horizontal',
				'layout-overlay': this.layoutMode === 'overlay',
				'layout-static': this.layoutMode === 'static',
				'layout-slim': this.layoutMode === 'slim',
				'layout-static-inactive': this.staticMenuDesktopInactive,
				'layout-mobile-active': this.staticMenuMobileActive,
				'layout-overlay-active': this.overlayMenuActive,
				'layout-menu-dark': this.darkMenu,
				'layout-menu-light':!this.darkMenu
			}];
        },
        displayInlineProfile() {
            return this.profileMode === 'inline' && this.layoutMode !== 'horizontal'
        }
	},
	components: {
		'AppTopBar': AppTopBar,
		'AppInlineProfile': AppInlineProfile,
		'AppMenu': AppMenu,
		'AppBreadcrumb': AppBreadcrumb,
		'AppFooter': AppFooter
	}
}
</script>

<style lang="scss">
	@import 'App.scss';
</style>

<style scoped>

	#layout-menu-logo {
		margin: auto;
		vertical-align: middle;
		max-height: 50px;
		max-width: 100%;
	}

</style>
