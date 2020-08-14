import axios from 'axios'

import Configuration from '../../config/configuration';

const querystring = require('querystring');

export default class LoggerService {

	async error(mje) {
		if (Configuration.value('API_LOGGER_ENABLED') == 'true') {
			console.error(mje);
			let send = {level: 'error', time: this.getNow(), application: 'Entornos', mensaje: mje, stack: mje.stack};
			await axios({
				method: 'post',
				url: Configuration.value('API_LOGGER') + '/log/error',
				data: querystring.stringify(send)
				});
		}
	}

	async debug(mje) {
		if (Configuration.value('API_LOGGER_DEBUG') == 'true') {
			console.debug(mje);
			let send = {level: 'debug', time: this.getNow(), application: 'Entornos', mensaje: mje};
			await axios({
				method: 'post',
				url: Configuration.value('API_LOGGER') + '/log/debug',
				data: querystring.stringify(send)
				});
		}
	}

	async info(mje) {
		if (Configuration.value('API_LOGGER_ENABLED') == 'true') {
			console.info(mje);
			let send = {level: 'info', time: this.getNow(), application: 'Entornos', mensaje: mje};
			await axios({
				method: 'post',
				url: Configuration.value('API_LOGGER') + '/log/info',
				data: querystring.stringify(send)
				});
		}
	}

	getNow() {
		const today = new Date();
		const date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
		const time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
		const dateTime = date +' '+ time;
		return dateTime;
	}

	async showMje(toast, severity, title, mje) {
		if (severity == 'error') {
			toast.add({severity:'warn', summary: title, detail: mje, life: 3000});
			await this.error(mje);
		}
		else if (severity == 'info') {
			toast.add({severity:'info', summary: title, detail: mje, life: 3000});
			await this.info(mje);
		}
	}

}