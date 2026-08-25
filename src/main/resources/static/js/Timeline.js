/**
 * 
 */
import WebSocketClient from './WebSocketClient.js';
import Converter from './Converter.js';

/**
 * 
 */
export default class Timeline {

	static MIN_NUMBER_OF_NODES = 0;
	static MAX_NUMBER_OF_NODES = 999;

	/**
	 * 
	 */
	static getDateRange(range) {
		const now = Date.now();
		const from = new Date(now);
		from.setDate(from.getDate() - range);
		const until = new Date(now);

		return { "from": from, "until": until };
	}

	/**
	 * 
	 */
	static cleanUp(selector) {
		document.querySelectorAll(selector).forEach(e => e.remove());
	}

	options = {};
	lightbox = null;
	fromNode = null;
	untilNode = null;

	numberOfNodes = 0;
	nodes = new Map();

	numberFormatter = null;

	/**
	 * Constructor
	 */
	constructor(options) {
		this.options = options;

		this.options.locales = this.options.locales || "de-CH";
		this.options.maximumSignificantDigits = this.options.maximumSignificantDigits || 12;
		this.options.dateTimeFullFormatOptions = this.options.dateTimeFullFormatOptions || {
			"weekday": "short",
			"day": "2-digit",
			"month": "2-digit",
			"year": "numeric",
			"hour": "2-digit",
			"minute": "2-digit",
			"second": "2-digit",
			"fractionalSecondDigits": 3,
			"timeZone": "CET",
			"timeZoneName": "short",
			"hour12": false
		};
		this.options.dateTimeFormatOptions = this.options.dateTimeFormatOptions || {
			"weekday": "short",
			"day": "2-digit",
			"month": "2-digit",
			"year": "numeric",
			"hour": "2-digit",
			"minute": "2-digit",
			"second": "2-digit",
			"timeZoneName": "short",
			"hour12": false
		};
		this.options.dateFormatOptions = this.options.dateFormatOptions || {
			"weekday": "short",
			"day": "2-digit",
			"month": "2-digit",
			"year": "numeric"
		};

		this.numberFormatter = new Intl.NumberFormat(this.options.locales, { "maximumSignificantDigits": this.options.maximumSignificantDigits });

	}

	/**
	 * 
	 */
	setLightBox(lightbox) {
		this.lightbox = lightbox;
	}

	/**
	 * 
	 */
	setNode(node) {
		this.numberOfNodes++;
		this.nodes.set(this.numberOfNodes, node);

		return this.nodes.get(this.numberOfNodes);
	}
	
	/**
	 * 
	 */
	getNode(key) {
		return this.nodes.get(key);
	}
	
	/**
	 * https://stackoverflow.com/a/60467108/6487382
	 */
	getNodes() {
		return new Map([...this.nodes.entries()].sort((a, b) => {
			if (a[0] > b[0]) { return 1 };
			if (a[0] == b[0]) { return 0 };
			if (a[0] < b[0]) { return -1 };
		}));
	}
	
	/**
	 * 
	 */
	getNodesSize() {
		return this.nodes.size;
	}

	/**
	 * 
	 */
	getCardsSize() {
		return this.getNodesSize() - 2;
	}
	
	/**
	 * 
	 */
	setFromNode(node) {
		this.fromNode = node;
		this.numberOfNodes++;
		this.nodes.set(Timeline.MIN_NUMBER_OF_NODES, this.fromNode);

		return this.fromNode;
	}

	/**
	 * 
	 */
	setUntilNode(node) {
		this.untilNode = node;
		this.numberOfNodes++;
		this.nodes.set(Timeline.MAX_NUMBER_OF_NODES, this.untilNode);

		return this.untilNode;
	}

	/**
	 * 
	 */
	getFromNode() {
		return this.fromNode;
	}

	/**
	 * 
	 */
	getUntilNode() {
		return this.untilNode;
	}
	
	/**
	 * 
	 */
	generateCardNodeMainTimeLine(download, uuid, name, type, func) {
		const node = document.createElement("div");
		node.setAttribute("id", "date_" + uuid);
		switch (type) {
			case Converter.STANDARD_IMAGE:
				node.setAttribute("class", "timeline timeline-card timeline-2 left-2 " + uuid);
				break;

			case Converter.STANDARD_VIDEO:
				node.setAttribute("class", "timeline timeline-card timeline-2 left-2 " + uuid);
				break;

			case Converter.WHATSAPP_IMAGE:
				node.setAttribute("class", "timeline timeline-card timeline-2 right-2 " + uuid);
				break;
				
			case Converter.WHATSAPP_VIDEO:
				node.setAttribute("class", "timeline timeline-card timeline-2 right-2 " + uuid);
				break;
				
			default:
				node.setAttribute("class", "timeline timeline-card timeline-2 left-2 " + uuid);
				break;
		}
		node.setAttribute("title", download);
		node.setAttribute("data-uuid", uuid);
		node.setAttribute("data-download", download);
		node.setAttribute("data-filename", name);
		node.setAttribute("data-filetype", type);
		node.addEventListener("dragover", func);

		return node;
	}
	
	/**
	 * 
	 */
	generateCardNodeMainTimeClock(download, id, date) {
		const numberFormatted = (this.numberFormatter.format(download));
		const mainTime = document.createElement("p");
		mainTime.setAttribute("id", "time_" + id);
		mainTime.setAttribute("class", "text-muted mb-4");
		const mainClock = document.createElement("i");
		mainClock.setAttribute("class", "far fa-clock");
		mainClock.setAttribute("aria-hidden", "true");
		mainTime.appendChild(mainClock);
		mainTime.appendChild(document.createTextNode(" " + date));
		const mainDate = document.createElement("small");
		mainDate.appendChild(document.createTextNode(" " + this.toLocaleDateTimeFull(download, this.options.locales) + " "));
		mainDate.appendChild(document.createTextNode(" (" + numberFormatted + ")"));
		mainTime.appendChild(mainDate);

		return mainTime;
	}

	/**
	 * 
	 */
	generateCardNode(download, thumbnail, showLinks, nodeFunc) {
		//const href = WebSocketClient.IMAGES_PATH + "image/" + encodeURIComponent(thumbnail.name);

		const card = document.createElement("div");
		card.setAttribute("id", "card_" + thumbnail.id);
		switch (thumbnail.imageType) {
			case Converter.STANDARD_IMAGE:
				card.setAttribute("class", "card draggable left-drag container-fluid");
				break;

			case Converter.WHATSAPP_IMAGE:
				card.setAttribute("class", "card draggable right-drag container-fluid");
				break;
				
			case Converter.WHATSAPP_VIDEO:
				card.setAttribute("class", "card draggable right-drag container-fluid");
				break;
				
			default:
				card.setAttribute("class", "card draggable left-drag container-fluid");
				break;
		}
		card.setAttribute("title", download);
		card.setAttribute("data-uuid", thumbnail.id);
		card.setAttribute("data-download", download);
		card.setAttribute("data-filename", thumbnail.name);
		card.setAttribute("data-filetype", thumbnail.imageType);
		card.setAttribute("draggable", "true");
		const row = document.createElement("div");
		row.setAttribute("class", "card-top row");
		card.appendChild(row);
		const picture = document.createElement("div");
		picture.setAttribute("class", ((showLinks) ? "col-10" : "col-12") + " d-flex justify-content-start");
		const image = document.createElement("img");
		image.setAttribute("src", "data:" + thumbnail.mimetype + ";base64," + thumbnail.content);
		image.setAttribute("id", "pict_" + thumbnail.id);
		image.setAttribute("class", "card-img-top");
		image.setAttribute("width", "150");
		image.setAttribute("height", "150");
		image.setAttribute("title", thumbnail.name);
		picture.appendChild(image);
		row.appendChild(picture);
		if (showLinks) {
			const syncHolder = document.createElement("div");
			syncHolder.setAttribute("class", "col-1 d-flex justify-content-end");
			const sync = document.createElement("button");
			sync.setAttribute("id", "main-safefile-" + thumbnail.id);
			sync.setAttribute("class", "btn-safefile align-top");
			sync.setAttribute("type", "button");
			sync.setAttribute("title", "Download updated multimedia file.");
			sync.addEventListener("click", (evnt) => { return WebSocketClient.downloadImageFromCache(evnt, thumbnail.name); });
			syncHolder.appendChild(sync);
			row.appendChild(syncHolder);
			const removeHolder = document.createElement("div");
			removeHolder.setAttribute("class", "col-1 d-flex justify-content-end");
			const remove = document.createElement("button");
			remove.setAttribute("id", "main-remove-" + thumbnail.id);
			remove.setAttribute("class", "btn-remove align-top");
			remove.setAttribute("type", "button");
			remove.setAttribute("title", "Remove card and delete file");
			remove.addEventListener("click", (evnt) => { return WebSocketClient.deleteImageFromRepository(evnt, thumbnail.action.toLowerCase(), thumbnail.name); });
			removeHolder.appendChild(remove);
			row.appendChild(removeHolder);
		}
		const body = document.createElement("div");
		body.setAttribute("class", "card-body p-4");
		const mainTitle = document.createElement("h5");
		mainTitle.setAttribute("class", "card-title mb-4");
		mainTitle.appendChild(document.createTextNode(thumbnail.name));
		body.appendChild(mainTitle);
		const mainTime = this.generateCardNodeMainTimeClock(download, thumbnail.id, thumbnail.date);
		body.appendChild(mainTime);
		card.appendChild(body);

		const mainTimeLine = this.generateCardNodeMainTimeLine(download, thumbnail.id, thumbnail.name, thumbnail.imageType, nodeFunc);
		mainTimeLine.appendChild(card);
		
		// Adds node to map
		this.setNode(mainTimeLine);

		return mainTimeLine;
	}
	
	/**
	 * 
	 */
	generateGoalNode(download, date, uuid, filename, filetype) {
		const numberFormatted = (this.numberFormatter.format(download));
		const goal = document.createElement("div");
		goal.setAttribute("id", "goal_" + Date.now());
		goal.setAttribute("class", "timeline timeline-goal goalline-2 goalright-2 droppable " + uuid);
		goal.setAttribute("title", ((date) ? date + " " : "") + "(" + numberFormatted + ")");
		goal.setAttribute("data-uuid", uuid);
		goal.setAttribute("data-download", download);
		goal.setAttribute("data-filename", (filename) ? filename : "");
		goal.setAttribute("data-filetype", (filetype) ? filetype : "");

		return goal;
	}
	
	/**
	 * 
	 */
	getData(node, selector) {
		if (!node) {
			return Date.now();			
		}

		if (selector.startsWith("data-", 0)) {
			return node.getAttribute(selector);
		}
		
		return node.getAttribute("data-" + selector);
	}
	
	/**
	 * https://gomakethings.com/finding-the-next-and-previous-sibling-elements-that-match-a-selector-with-vanilla-js/
	 */
	getPreviousSibling(node, selector, alternative) {
		// Get the next sibling element
		let sibling = node.previousElementSibling;

		// If there's no selector, return the first sibling
		if (!selector) return sibling;

		// If the sibling matches our selector, use it
		// If not, jump to the previous sibling and continue the loop
		while (sibling) {
			if (sibling.matches(selector)) {
				return sibling;
			}
			
			if (sibling.matches(alternative) && sibling.previousElementSibling == null) {
				return sibling;
			}

			sibling = sibling.previousElementSibling;
		}
	}
	
	/**
	 * https://gomakethings.com/finding-the-next-and-previous-sibling-elements-that-match-a-selector-with-vanilla-js/
	 */
	getNextSibling(node, selector, alternative) {
		// Get the next sibling element
		let sibling = node.nextElementSibling;

		// If there's no selector, return the first sibling
		if (!selector) return sibling;

		// If the sibling matches our selector, use it
		// If not, jump to the next sibling and continue the loop
		while (sibling) {
			if (sibling.matches(selector)) {
				return sibling
			};

			if (sibling.matches(alternative) && sibling.nextElementSibling == null) {
				return sibling
			};

			sibling = sibling.nextElementSibling;
		}
	}

	/**
	 * 
	 */
	calculateDownload(prev, next) {
		//const step = Math.round((next - prev) / 10);

		//return prev + Math.round((next - prev) / 2) + step;

		return prev + Math.round((next - prev) / 2);
	}
	
	/**
	 * 
	 */
	updateFromUntilNodes(minDownload, maxDownload) {
		this.updateGoalNode(this.fromNode, minDownload);
		this.updateGoalNode(this.untilNode, maxDownload);
	}
	
	/**
	 * 
	 */
	updateGoalNode(goal, download, date, uuid, filename, filetype) {
		const numberFormatted = (this.numberFormatter.format(download));
		goal.replaceChildren();
		goal.setAttribute("id", "goal_" + Date.now());
		goal.setAttribute("class", "timeline timeline-goal goalline-2 goalright-2 droppable " + uuid);
		goal.setAttribute("title", ((date) ? date + " " : "") + "(" + numberFormatted + ")");
		goal.setAttribute("data-uuid", uuid);
		goal.setAttribute("data-download", download);
		goal.setAttribute("data-filename", (filename) ? filename : "");
		goal.setAttribute("data-filetype", (filetype) ? filetype : "");

		return goal;
	}

	/**
	 * 
	 */
	toLocaleDate(date, locale) {
		const formatter = new Intl.DateTimeFormat(locale, this.options.dateFormatOptions);

		return formatter.format(date);
	}

	/**
	 * 
	 */
	toLocaleDateTime(date, locale) {
		const formatter = new Intl.DateTimeFormat(locale, this.options.dateTimeFormatOptions);

		return formatter.format(date);
	}

	/**
	 * 
	 */
	toLocaleDateTimeFull(date, locale) {
		const formatter = new Intl.DateTimeFormat(locale, this.options.dateTimeFullFormatOptions);

		return formatter.format(date);
	}
	
	/**
	 * 
	 */
	datePickerDatesToMilliseconds(dates)
	{
		const from = new Date(dates[0]);
		const until = new Date(dates[1]);
		
		return { "from": from.valueOf(), "until": until.valueOf() };
	}
}




/**
 * 
 */
export class TimelineNode {

	/**
	 * Constructor
	 */
	constructor() {
		
	}

	generateGoalNode(){
		
	}
}