<template>
	<div class="p-grid">
		<div class="p-col-12">
			<div class="card">
				Index
			</div>
		</div>
	</div>
</template>

<script>

import LoggerService from '../service/LoggerService';

export default {
	data() {
		return {
		}
	},
	loggerService: null,
	created() {
		this.loggerService = new LoggerService();
		this.loggerService.debug('Index created');
		this.loggerService.debug('Index created end');
	},
	mounted() {
		this.loggerService.debug('Index mounted');
		this.loggerService.debug('Index mounted end');
	},
	methods: {
	}
}
</script>

<style scoped>

</style>