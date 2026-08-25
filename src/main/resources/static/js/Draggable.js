/**
 * 
 */
export default class Draggable {

	/**
	 * 
	 */
	static scroll(evnt, options) {
		const mouseY = evnt.clientY;
		const windowHeight = window.innerHeight;

		if (!options.dragging) {
			return;
		};

		if (mouseY < options.threshold) {
			window.scrollBy({ "top": -options.speed, "behavior": "smooth" });
		} else if (windowHeight - mouseY < options.threshold) {
			window.scrollBy({ "top": options.speed, "behavior": "smooth" });
		}

		return;
	}


	options;

	/**
	 * Constructor
	 */
	constructor(options) {
		this.options = options;
	}

}