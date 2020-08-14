<template>
	<ul v-if="items" role="menu">
		<li v-for="(item,i) of items" :key="item.label || i" :class="[item.badgeStyle, {'layout-root-menuitem': root, 'active-menuitem': activeIndex === i}]" role="none">
            <div class='arrow' v-if="item.items && root===true"></div>
            <div v-if="root">
                <span class="layout-menuitem-text">{{item.label}}</span>
            </div>
            <router-link v-if="item.to" :to="item.to" :class="[item.styleClass]" @click.native="onMenuItemClick($event,item,i)"
                    :target="item.target" exact @mouseenter.native="onMenuItemMouseEnter(i)" role="menuitem">
                <i :class="['layout-menuitem-icon', item.icon]"></i>
                <span class="layout-menuitem-text">{{item.label}}</span>
                <i v-if="item.items" class="pi pi-fw pi-angle-down layout-submenu-toggler"></i>
                <span v-if="item.badge" class="menuitem-badge">{{item.badge}}</span>
            </router-link>
            <a v-if="!item.to" :href="item.url||'#'" @click="onMenuItemClick($event,item,i)" :class="item.styleClass" target="item.target" @mouseenter="onMenuItemMouseEnter(i)" role="menuitem">
                <i :class="['layout-menuitem-icon', item.icon]"></i>
                <span class="layout-menuitem-text">{{item.label}}</span>
                <i v-if="item.items" class="pi pi-fw pi-angle-down layout-submenu-toggler"></i>
                <span v-if="item.badge" class="menuitem-badge">{{item.badge}}</span>
            </a>
            <div class="layout-menu-tooltip" v-if="root">
                <div class="layout-menu-tooltip-arrow"></div>
                <div class="layout-menu-tooltip-text">{{item.label}}</div>
            </div>
            <transition name="layout-submenu-container">
                <AppSubmenu v-show="item.items && (root ? true : activeIndex === i)" :items="item.items" :menuActive="menuActive" :layoutMode="layoutMode" @menuitem-click="$emit('menuitem-click', $event)"></AppSubmenu>
            </transition>
        </li>
	</ul>
</template>
<script>
import EventBus from './event-bus';

export default {
	name: 'AppSubmenu',
	props: {
		items: Array,
		layoutMode: String,
		menuActive: Boolean,
		root: {
			type: Boolean,
			default: false
		}
	},
	data() {
		return {
			activeIndex : null
		}
	},
	mounted() {
		EventBus.$on('reset_active_index', () => {
			if((this.layoutMode === 'horizontal' || this.layoutMode === 'slim')) {
				this.activeIndex = null;
			}
		});
	},
	methods: {
		onMenuItemClick(event, item, index) {
			if (item.disabled) {
				event.preventDefault();
				return;
			}

			//execute command
			if (item.command) {
                item.command({originalEvent: event, item: item});
                event.preventDefault();
			}

            if (item.items) {
                event.preventDefault();
            }

			if (this.root) {
				this.$emit('root-menuitem-click', {
					originalEvent: event
				});
			}

            if (item.items) {
                this.activeIndex = index === this.activeIndex ? null : index;
            }

			this.$emit('menuitem-click', {
				originalEvent: event,
				item: item
			});
		},
		isMobile() {
			return window.innerWidth <= 640;
		},
		onMenuItemMouseEnter(index) {
			if(this.root && this.menuActive && (this.layoutMode === 'horizontal' || this.layoutMode === 'slim') && !this.isMobile()) {
				this.activeIndex =  index;
			}
        }
	},
	components: {
		"AppSubmenu": this
	}
}
</script>

<style scoped>

</style>
