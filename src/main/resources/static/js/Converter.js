/**
 * 
 */
import Editor from './Editor.js';

/**
 * 
 */
export default class Converter {

	static OBJ_CONSTR = ({}).constructor;
	static DATETIME_FILTER = new RegExp("^[A-Za-z ]*(\\d{4}-\\d{2}-\\d{2}) [a-z]* (\\d{2}\\.\\d{2}\\.\\d{2})(\\(\\d*\\))?\\.[A-Za-z]{1,5}$", "gi");
	static STANDARD_SCAN = "STANDARD_SCAN";
	static STANDARD_IMAGE = "STANDARD_IMAGE";
	static STANDARD_VIDEO = "STANDARD_VIDEO";
	static WHATSAPP_IMAGE = "WHATSAPP_IMAGE";
	static WHATSAPP_VIDEO = "WHATSAPP_VIDEO";

	/**
	 * https://stackoverflow.com/a/31841864
	 */
	static updateDropZoneIcon(file) {

		let node = Array.from(document.querySelectorAll('.dz-image img')).pop();
		node.style = "width:100%;height:100%;";

		let ext = file.name.split('.').pop();
		switch(ext) {
			case "mp4":
				node.src = "./../img/icons-mp4.svg"; 
				break;

			case "pdf":
				node.src = "./../img/icons-pdf.svg"; 
				break;

			default:
				break;
		}

	}

	/**
	 * 
	 */
	static checkForBeingObject(arg){
		return (arg !== null && arg !== undefined && arg.constructor === Converter.OBJ_CONSTR);
	}

	/**
	 * 
	 */
	static checkForBeingEmpty(arg){
		return (arg !== null && arg.constructor === Object && Object.keys(arg).length === 0);
	}
	
	/**
	 * 
	 */
	static convertDetails2LinkList(list, data) {

		for (const k in data) {
			let value = data[k];
			let isArray = Array.isArray(value);
			let isObject = (value !== null && value !== undefined && value.constructor === Converter.OBJ_CONSTR);
			// console.log(">>> " + k + "-->" + (isObject) ? JSON.stringify(value) : value + " (" + isArray + ") (" + isObject + ")");

			if (isArray) {
				console.error("Implement missing function!");
			} else if (isObject) {
				if ((!value.href && !value.templated) || (value.href && !value.templated)) {
					Converter.convertDetails2LinkList(list, value);
				}
			} else {
				if (k === "href") {
					const item = document.createElement("li");
					item.setAttribute("class", "value-item");
					const link = document.createElement("a");
					link.setAttribute("href", value);
					link.setAttribute("target", "actuator");
					link.appendChild(document.createTextNode(value));
					item.appendChild(link);
					
					list.appendChild(item);
				}
			}
		}
	}
	
	/**
	 * https://www.geeksforgeeks.org/how-to-iterate-json-object-in-javascript/
	 * https://stackoverflow.com/a/47436359/6487382
	 * https://stackoverflow.com/a/11183002/6487382
	 */
	static convertJson2DefaultTable(data) {

		if (!data) {
			return { "table": document.createElement("span"), "dateTime": [] };
		}

		let table = document.createElement("table");
		for (const k in data) {
			let value = data[k];
			let isArray = Array.isArray(value);
			let isObject = (value !== undefined && value !== null && value.constructor === Converter.OBJ_CONSTR);
			//console.log(">>> " + k + "-->" + value + " (" + isArray + ") (" + isObject + ")");

			let row = document.createElement("tr");
			let key = document.createElement("td");
			let content = document.createElement("td");

			key.setAttribute("class", "label");
			key.appendChild(document.createTextNode(k));
			content.setAttribute("class", "value");
			if (isArray) {
				value.forEach((v) => content.appendChild(Converter.convertJson2DefaultTable(v).table));
			} else if (isObject) {
				content.appendChild(Converter.convertJson2DefaultTable(value).table);
			} else {
				content.appendChild(document.createTextNode((value) ? value : "No data"));
			}

			// Bring all together
			row.appendChild(key)
			row.appendChild(content)
			table.appendChild(row);
		}

		return { "table": table, "dateTime": [] };
	}	

	/**
	 * 
	 */
	static convertJson2ValueTable(table, dateTime, name, label, sectionId, uuid, data, exclude) {

			if (!table || !data) {
				return document.createElement("span");
			}

			let isLabel = false;
			let isValue = false;
			let dataObj = {};

			for (const k in data) {
				let dataValue = data[k];
				let isArray = Array.isArray(dataValue);
				//let isEmpty = Converter.checkForBeingEmpty(dataValue);
				let isObject = Converter.checkForBeingObject(dataValue);
				let isExclude = exclude.includes(k);
				if (k.toLocaleLowerCase() === "directory" || k.toLocaleLowerCase() === "tagtypehex" || k.toLocaleLowerCase() === "tagtypeint") {
					dataObj[k] = dataValue;
				}
				isLabel = (k.toLocaleLowerCase() === "label") || isLabel;
				if (isLabel && !dataObj.label) {
					dataObj.label = dataValue;
				}
				isValue = (k.toLocaleLowerCase() === "value");
				if (isValue) {
					dataObj.value = dataValue;
				}
				isValue = (k.toLocaleLowerCase() === "isodate");
				if (isValue) {
					dataObj.isoDate = dataValue;
				}
				isValue = (k.toLocaleLowerCase() === "isotime");
				if (isValue) {
					dataObj.isoTime = dataValue;
				}
				
				//console.log(">>> " + k + " --> " + JSON.stringify(dataValue) + " (" + isArray + ") (" + isObject + ") (" + isExclude + ")");
				//console.log(">>>" + k + " --> " + isLabel + " = " + label + " / " + isValue + " = " + value + " --> " + JSON.stringify(dataObj))

				// Skip excluded members
				if (!isExclude) {
					if (isArray) {
						dataValue.forEach((val) => { 
							//console.log("+++ " + k + "-->" + JSON.stringify(val)); 
							Converter.convertJson2ValueTable(table, dateTime, name, label, sectionId, uuid, val, exclude);
						});
					} else if (isObject) {
						//console.log("--- " + k + "-->" + JSON.stringify(dataValue));
						Converter.convertJson2ValueTable(table, dateTime, name, label, sectionId, uuid, dataValue, exclude);
					} else {
						if (isLabel && isValue) {
							let row = document.createElement("tr");
							let key = document.createElement("td");
							key.setAttribute("class", "label");
							let content = document.createElement("td");
							content.setAttribute("class", "value");
							let editText = document.createElement("span");
							editText.setAttribute("id", dataObj.tagTypeHex + "-" + sectionId + "-span-" + uuid);
							editText.setAttribute("class", "value");
							editText.setAttribute("title", "Type codes: " + dataObj.tagTypeInt + " / " + dataObj.tagTypeHex);
							let editIcon = document.createElement("a");
							editIcon.setAttribute("id", dataObj.tagTypeHex + "-" + sectionId + "-link-" + uuid);
							editIcon.setAttribute("class", "fa fa-edit");
							editIcon.setAttribute("href", "#");
							editIcon.setAttribute("data-label", dataObj.label);
							editIcon.setAttribute("data-value", dataObj.value);
							editIcon.setAttribute("data-directory", dataObj.directory);
							editIcon.setAttribute("data-tagTypeHex", dataObj.tagTypeHex);
							editIcon.setAttribute("data-tagTypeInt", dataObj.tagTypeInt);
							editIcon.addEventListener("click", (evnt) => { return Editor.editValue(evnt, name, label, sectionId, uuid); });
						
							key.appendChild(document.createTextNode(dataObj.label));
							editText.appendChild(document.createTextNode((dataObj.value) ? dataObj.value.split(";").sort().join(", ") : "No data"));
							content.appendChild(editText);
							content.appendChild(editIcon);

							/**
							 * Get 'Date/Time', 'Date/Time Original' and 'Date/Time Digitized' as sorted array.
							 */							
							const typeInt = parseInt(dataObj.tagTypeInt, 10);
							if (typeInt === 306 || typeInt === 36867 || typeInt === 36868) {
								dateTime.set(typeInt, dataObj);
							}
							
							if (dataObj.label && dataObj.value) {
								isLabel = false;
								isValue = false;
								dataObj = {};
							}

							// Bring all together
							row.appendChild(key)
							row.appendChild(content)
							table.appendChild(row);
						}
					}
				}
			}

			return { "table": table, "dateTime": dateTime };
		}

		/**
		 * 
		 */
		static convertImageType2DateTime(type, dateTime) {

			let date = new Date(dateTime);

			if (type === Converter.WHATSAPP_IMAGE) {
				
				return { "date": date.toLocaleDateString('de-CH'), "time": date.toLocaleTimeString('de-CH'), "dateTime": date.toJSON(), "value": date.getTime() };
			}

			return { "date": date.toLocaleDateString('de-CH'), "time": date.toLocaleTimeString('de-CH'), "dateTime": date.toJSON(), "value": date.getTime() };
		}


		/**
		 * Constructor
		 */
		constructor() {
			//
		}

}