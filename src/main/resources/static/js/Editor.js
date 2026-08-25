/**
 * 
 */
export default class Editor {

	/**
	 * 
	 */
	static editValue(evnt, name, sectionLabel, sectionId, uuid) {
		evnt.preventDefault();

		const target = evnt.target;
		const label = target.getAttribute("data-label");
		const value = target.getAttribute("data-value");
		const directory = target.getAttribute("data-directory");
		const tagTypeHex = target.getAttribute("data-tagTypeHex");

		// Hide elements
		document.getElementById(tagTypeHex + "-" + sectionId + "-span-" + uuid).style.display = "none";
		document.getElementById(tagTypeHex + "-" + sectionId + "-link-" + uuid).style.display = "none";

		let form = document.getElementById(tagTypeHex + "-" + sectionId + "-form-" + uuid);
		if (!form) {
			// Create form
			form = document.createElement("form");
			form.setAttribute("id", tagTypeHex + "-" + sectionId + "-form-" + uuid);
			form.setAttribute("class", "form-inline");
			form.setAttribute("action", "#");
			form.setAttribute("method", "post");
			form.addEventListener("submit", (evnt) => { return Editor.updateMetadata(evnt, name, sectionLabel, directory, tagTypeHex, sectionId, uuid); });
			form.addEventListener("reset", (evnt) => { return Editor.resetForm(evnt, tagTypeHex, sectionId, uuid); });
			const dataInput = document.createElement("input");
			dataInput.setAttribute("id", tagTypeHex + "-" + sectionId + "-data-" + uuid);
			dataInput.setAttribute("class", "form-control");
			dataInput.setAttribute("type", "text");
			dataInput.setAttribute("name", "data");
			dataInput.setAttribute("value", value);
			dataInput.setAttribute("placeholder", label);
			const formSubmit = document.createElement("button");
			formSubmit.setAttribute("id", tagTypeHex + "-" + sectionId + "-send-" + uuid);
			formSubmit.setAttribute("class", "btn btn-apply btn-outline-secondary");
			formSubmit.setAttribute("type", "submit");
			const formCancel = document.createElement("button");
			formCancel.setAttribute("id", tagTypeHex + "-" + sectionId + "-canc-" + uuid);
			formCancel.setAttribute("class", "btn btn-abort btn-outline-warning");
			formCancel.setAttribute("type", "reset");

			// Put all together
			form.appendChild(dataInput);
			form.appendChild(formSubmit);
			form.appendChild(formCancel);

			// Insert into DOM
			target.insertAdjacentElement("beforebegin", form);
		} else {
			form.style.display = "block";
		}

		return false;
	}

	/**
	 * 
	 */
	static updateMetadata(evnt, name, sectionLabel, directory, tagTypeHex, sectionId, uuid) {
		evnt.preventDefault();
		
		const input = document.getElementById(tagTypeHex + "-" + sectionId + "-data-" + uuid);
		const value = input.getAttribute("value");

		let uri = "../api/v1/images/image/metadata/update/";
		let options = {};
		if (value.length < 100) {
			// See 'https://stackoverflow.com/a/37456192/6487382'
			uri += encodeURIComponent(name) + "/" + encodeURIComponent(sectionLabel) + "/" + encodeURIComponent(directory) + "/" + encodeURIComponent(tagTypeHex) + "/" + encodeURIComponent(encodeURIComponent(value));

			options.method = "PUT";
			options.headers = { "Accept": "*/*", "Content-Type": "text/plain; charset=UTF-8" };
		} else {
			uri += encodeURIComponent(name) + "/" + encodeURIComponent(sectionLabel) + "/" + encodeURIComponent(directory) + "/" + encodeURIComponent(tagTypeHex);

			options.method = "POST";
			options.headers = { "Accept": "*/*", "Content-Type": "text/plain; charset=UTF-8" };
			options.body = value;
		}
		console.log("Going to update file '" + name + "'. Use URI '" + uri + "' ('" + JSON.stringify(options) + "').");
		
		(async () => {
			await fetch(uri, options).then((response) => {
				console.log(response)
				if (response.status === 200 || response.status === 201 || response.status === 202) {
					Toast.create({ "title": "Notice", "message": "Multimedia file '" + name + "' has been updated successfully.", "status": TOAST_STATUS.SUCCESS, "timeout": 5000 });
				} else {
					Toast.create({ "title": "Error", "message": "An error has been occured.", "status": TOAST_STATUS.DANGER, "timeout": 7500 });
				}
			}).catch((error) => {
				//
			}).finally(() => {
				// Reset form
				document.getElementById(tagTypeHex + "-" + sectionId + "-form-" + uuid).reset();
			});
		})();

		return false;
	}

	/**
	 * 
	 */
	static resetForm(evnt, tagTypeHex, sectionId, uuid) {
		evnt.preventDefault();
		//console.log(tagTypeHex + " | " + sectionId + " | " + uuid);

		// Hide and show elements
		document.getElementById(tagTypeHex + "-" + sectionId + "-form-" + uuid).style.display = "none";
		document.getElementById(tagTypeHex + "-" + sectionId + "-span-" + uuid).style.display = "inline";
		document.getElementById(tagTypeHex + "-" + sectionId + "-link-" + uuid).style.display = "inline";

		return false;
	}

	/**
	 * Constructor
	 */
	constructor() {
		//
	}

}