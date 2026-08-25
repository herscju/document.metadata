/**
 * 
 */
export default class LightBox {

	options;

	/**
	 * Constructor
	 */
	constructor(options) {
		this.options = options;
	}

	/**
	 * 
	 */
	build(node) {
		return new Lightbox(node, this.options);
	}

}