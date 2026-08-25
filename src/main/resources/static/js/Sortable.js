/**
 * 
 */
export default class Sortable {

	/**
	 * 
	 */
	static removeEntries(selector) {
		document.querySelectorAll(selector).forEach(e => e.remove());
	}

	options;

	/**
	 * Constructor
	 */
	constructor(options) {
		this.options = options;
	}

}