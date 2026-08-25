/**
 * 
 */
import Converter from './Converter.js';
import Downloader from './Downloader.js';
import Draggable from './Draggable.js';
import Sortable from './Sortable.js';
import Timeline from './Timeline.js';
import { GeoLocationMap, GeoPoint } from './GeoLocationMap.js';
//import { Calendar, Options } from '../vendor/calendar/calendar-3.1.0.js';

/**
 * 
 */
export default class WebSocketClient {

    static OBJ_CONSTR = ({}).constructor;
    static ID_FILTER = new RegExp("\\W", "gm");

    static SUBSCRIPTION_CERTIFICATES = "certificates";
	static SUBSCRIPTION_LDAP_OBJECTS = "objects";
    static SUBSCRIPTION_MESSAGES = "messages";
    static TYPE_STOMP = "Stomp";
    static TYPE_SOCKETIO = "Socket.IO";

    static API_PATH = "../api/";
    static CERTIFICATE_PATH = WebSocketClient.API_PATH + "v1/certificates/";
    static IMAGES_PATH = WebSocketClient.API_PATH + "v1/images/";
    static IMAGES_ARCHIVE = WebSocketClient.IMAGES_PATH + "image/download/archive/";
	static LDAP_PATH = WebSocketClient.API_PATH + "v1/ldap/";

    //	static DIFFERENCE = (1000 * 60 * 60 * 24);
    static DIFFERENCE = (1000 * 2);
    static ANONYMUS = "Anonymus";
    static JOIN = "Join";
    static CHAT = "Chat";
    static LEAVE = "Leave";
    static THRESHOLD = 400;
    static SCROLL_SPEED = 100;
	
	static TOAST_TIMEOUT_SUCCESS = 40000;
	static TOAST_TIMEOUT_WARNING = 50000;
	static TOAST_TIMEOUT_DANGER = 60000;

    /**
     * 
     */
    static stringToUUID(str) {

        if (str === undefined || !str.length) {
            str = "" + Math.random() * new Date().getTime() + Math.random();
        }

        let c = 0;
        let r = "";

        const max = 0xfffffffffffff;
        const hi = 0x80000000;
        const lo = 0x7FFFFFFF;
        const and = (a, b) => (~~(a / hi) & ~~(b / hi)) * hi + ((a & lo) & (b & lo));

        for (let i = 0;i < str.length;i++) {
            c = c + (str.charCodeAt(i) * (i + 1) - 1);
            if (c > max) {
                c = and(c, max);
            }
        }

        str = str.substr(str.length / 2) + c.toString(16) + str.substr(0, str.length / 2);
        for (let i = 0, p = c + str.length;i < 32;i++) {
            if (i == 8 || i == 12 || i == 16 || i == 20) {
                r += "-";
            }

            c = p = (str[(i ** i + p + 1) % str.length]).charCodeAt(0) + p + i;
            if (i == 12) {
                c = (c % 5) + 1; //1-5
            } else if (i == 16) {
                c = (c % 4) + 8; //8-B
            } else {
                c %= 16; //0-F
            }

            r += c.toString(16);
        }

        // Make sure UUID starts always with a letter. Other it can not be used as CSS class name. See 'https://stackoverflow.com/a/47447224/6487382'
        let uuid = r.replace(r.charAt(0), String.fromCharCode(97 + Math.floor(Math.random() * 26)));

        return uuid;
    }

    /**
     * 
     */
    static stringToDateTime(input) {

    }

    /**
     * 
     */
    static checkForBeingObject(arg) {
        return (arg !== null && arg !== undefined && arg.constructor === WebSocketClient.OBJ_CONSTR);
    }

    /**
     * 
     */
    static checkForBeingEmpty(arg) {
        return (arg !== null && arg.constructor === Object && Object.keys(arg).length === 0);
    }

    /**
     * 
     */
    static navigation(evnt, id, uuid) {
        evnt.preventDefault();

        // Navigation
        const activeTab = document.querySelector("li.nav-item a.nav-link." + uuid + ".active");
        activeTab.classList.remove("active");

        const targetTab = evnt.target;
        targetTab.classList.add("active");

        // Content
        const activePane = document.querySelector("div#main-body-" + uuid + " div.tab-pane.active");
        activePane.style.display = "none";
        activePane.classList.remove("active");

        const targetPane = document.getElementById(id + "-" + uuid);
        targetPane.style.display = "block";
        targetPane.classList.add("active");

        return false;
    }

    /**
     * 
     */
    static fadeTo(element, toValue = 0, duration = 200) {
        const fromValue = parseFloat(element.style.opacity) || 1;
        const startTime = Date.now();
        const framerate = 1000 / 60; // 60fps

        let interval = setInterval(() => {
            const currentTime = Date.now();
            const timeDiff = (currentTime - startTime) / duration;
            const value = fromValue - (fromValue - toValue) * timeDiff;

            if (timeDiff >= 1) {
                clearInterval(interval);
                interval = 0;
            }

            element.style.opacity = value.toString();
        }, framerate);
    }

    /**
     * 
     */
    static synchronizeDateTimeValues(evnt, path, name, date) {
		if (evnt) {
	        evnt.preventDefault();
		}

        const uri = WebSocketClient.IMAGES_PATH + ((path) ? path + "/" : "") + "image/metadata/synchronize/" + encodeURIComponent(name) + "/" + encodeURIComponent(date);
        console.log("Going to synchronize metadata of file '" + name + "'. Use URI '" + uri + "'.");

        const options = {};
        options.method = "PUT";
        options.headers = { "Accept": "*/*", "Content-Type": "text/plain; charset=UTF-8" };

        (async () => {
            await fetch(uri, options).then((response) => {
                if (response.ok && (response.status === 200 || response.status === 201 || response.status === 202)) {
                    Toast.create({ "title": "Notice", "message": "Multimedia file '" + name + "' has been updated successfully.", "status": TOAST_STATUS.SUCCESS, "timeout": WebSocketClient.TOAST_TIMEOUT_SUCCESS });
				} else if (response.ok && (response.status === 204)) {
					Toast.create({ "title": "Notice", "message": "Multimedia file '" + name + "' has been adjusted successfully.", "status": TOAST_STATUS.SUCCESS, "timeout": WebSocketClient.TOAST_TIMEOUT_SUCCESS });
                } else {
                    Toast.create({ "title": "Error", "message": "An error has been occured while synchronizing the image metadata. Error code is '" + response.status + "'.", "status": TOAST_STATUS.DANGER, "timeout": WebSocketClient.TOAST_TIMEOUT_DANGER });
                }
            }).catch((error) => {
                Toast.create({ "title": "Error", "message": "An error has been occured while synchronizing the image metadata. (" + JSON.stringify(error) + ").", "status": TOAST_STATUS.DANGER, "timeout": WebSocketClient.TOAST_TIMEOUT_DANGER });
            }).finally(() => {
                // 
            });
        })();

        return false;
    }

    /**
     * 
     */
    static editImageOnCache(evnt, path, name) {
        evnt.preventDefault();

        const target = evnt.target;
        const uri = WebSocketClient.IMAGES_PATH + "image/update/dng/" + encodeURIComponent(path) + "/" + encodeURIComponent(name);
        console.log("Going to update/modify) file '" + name + "' in directory '" + path + "'. Use URI '" + uri + "'.");

    }

    /**
     * 
     */
    static deleteAllFromCache(evnt, path, action) {
        evnt.preventDefault();

        const uri = WebSocketClient.IMAGES_PATH + "delete/all/" + encodeURIComponent(path);
        console.log("Going to delete all files in directory '" + path + "'. Use URI '" + uri + "'.");

        const xhr = new XMLHttpRequest();
        xhr.open("DELETE", uri, true);
        xhr.setRequestHeader("Content-Type", "text/text; charset=UTF-8");
        xhr.addEventListener("abort", (evnt) => {
            Toast.create({ "title": "Abort", "message": "Request has been aborted. Current status is '" + evnt.currentTarget.status + "'.", "status": TOAST_STATUS.WARNING, "timeout": WebSocketClient.TOAST_TIMEOUT_WARNING });
            console.log("Delete picture: Abort! Status is '" + evnt.currentTarget.status + "'.");

            return false;
        });
        xhr.addEventListener("error", (evnt) => {
            Toast.create({ "title": "Abort", "message": "Deleting image occured an error. Current status is '" + evnt.currentTarget.status + "'.", "status": TOAST_STATUS.WARNING, "timeout": WebSocketClient.TOAST_TIMEOUT_WARNING });
            console.error("Delete image: Error! Status is '" + evnt.currentTarget.status + "'.");

            return false;
        });
        xhr.addEventListener("load", (evnt) => {
            const response = JSON.parse(evnt.target.response);
            if (evnt.currentTarget.readyState === 4 && evnt.currentTarget.status === 200) {
                Toast.create({ "title": "Notice", "message": response.message, "status": TOAST_STATUS.SUCCESS, "timeout": WebSocketClient.TOAST_TIMEOUT_SUCCESS });

                // Remove all cards, etc.
                Sortable.removeEntries(".grid-item");
                Timeline.cleanUp(".timeline");

                // Reset counter
                const process = document.getElementById("process-nbr");
                if (process) {
                    const textNode = process.firstChild;
                    textNode.firstChild.remove();
                    textNode.appendChild(document.createTextNode(0));
                }
            } else {
                Toast.create({ "title": "Error", "message": "An error has been occured. Detail message: '" + response.message + "'. Current status is '" + evnt.currentTarget.status + "'.", "status": TOAST_STATUS.DANGER, "timeout": WebSocketClient.TOAST_TIMEOUT_DANGER });
            }

            return false;
        });
        xhr.addEventListener("loadend", (evnt) => {
            console.log("Delete all entries from cache: Load end. Status is '" + evnt.currentTarget.status + "'.");
            document.getElementById("overlay").style.display = "none";
            //const overlay = document.getElementById("overlay");
            //overlay.style.display = "none";
            //WebSocketClient.fadeTo(overlay, 0, 500);

            if (action) {
                document.getElementById(action).style = "display:none;";
            }

            return false;
        });
        xhr.addEventListener("loadstart", (evnt) => {
            console.log("Delete all entries from cache: Load start. Status is '" + evnt.currentTarget.status + "'.");
            document.getElementById("overlay").style.display = "block";
            //const overlay = document.getElementById("overlay");
            //overlay.style.display = "block";
            //WebSocketClient.fadeTo(overlay, 0.8, 500);

            return false;
        });
        xhr.addEventListener("progress", (evnt) => { /*console.log("Delete all entries from cache: Load progress. Status is '" + evnt.currentTarget.status + "'.");*/ return false; });
        xhr.addEventListener("timeout", (evnt) => { /*console.error("Delete all entries from cache: Timeout! Status is '" + evnt.currentTarget.status + "'.");*/ return false; });
        xhr.send();

        return false;
    }

    /**
     * 
     */
    static deleteImageFromCache(evnt, path, name) {
        evnt.preventDefault();

        const target = evnt.target;
        const uri = WebSocketClient.IMAGES_PATH + "delete/all/" + encodeURIComponent(path) + "/" + encodeURIComponent(name);
        console.log("Going to delete file '" + name + "' in directory '" + path + "'. Use URI '" + uri + "'.");

        const xhr = new XMLHttpRequest();
        xhr.open("DELETE", uri, true);
        xhr.setRequestHeader("Content-Type", "text/text; charset=UTF-8");
        xhr.addEventListener("abort", (evnt) => {
            Toast.create({ "title": "Abort", "message": "Request has been aborted. Current status is '" + evnt.currentTarget.status + "'.", "status": TOAST_STATUS.WARNING, "timeout": WebSocketClient.TOAST_TIMEOUT_WARNING });
            console.log("Delete picture: Abort! Status is '" + evnt.currentTarget.status + "'.");

            return false;
        });
        xhr.addEventListener("error", (evnt) => {
            Toast.create({ "title": "Abort", "message": "Deleting image occured an error. Current status is '" + evnt.currentTarget.status + "'.", "status": TOAST_STATUS.WARNING, "timeout": WebSocketClient.TOAST_TIMEOUT_WARNING });
            console.error("Delete image: Error! Status is '" + evnt.currentTarget.status + "'.");

            return false;
        });
        xhr.addEventListener("load", (evnt) => {
            const response = JSON.parse(evnt.target.response);
            if (evnt.currentTarget.readyState === 4 && evnt.currentTarget.status === 200) {
                Toast.create({ "title": "Notice", "message": response.message, "status": TOAST_STATUS.SUCCESS, "timeout": WebSocketClient.TOAST_TIMEOUT_SUCCESS });

                // Remove card
                target.closest(".grid-item").remove();
            } else {
                Toast.create({ "title": "Error", "message": "An error has been occured. Detail message: '" + response.message + "'.", "status": TOAST_STATUS.DANGER, "timeout": WebSocketClient.TOAST_TIMEOUT_DANGER });
            }

            return false;
        });
        xhr.addEventListener("loadend", (evnt) => { /*console.log("Delete picture: Load end. Status is '" + evnt.currentTarget.status + "'.");*/ return false; });
        xhr.addEventListener("loadstart", (evnt) => { /*console.log("Delete picture: Load start. Status is '" + evnt.currentTarget.status + "'.");*/ return false; });
        xhr.addEventListener("progress", (evnt) => { /*console.log("Delete picture: Load progress. Status is '" + evnt.currentTarget.status + "'.");*/ return false; });
        xhr.addEventListener("timeout", (evnt) => { /*console.error("Delete picture: Timeout! Status is '" + evnt.currentTarget.status + "'.");*/ return false; });
        xhr.send();

        return false;
    }

    /**
     * 
     */
    static deleteImageFromRepository(evnt, path, name) {
        evnt.preventDefault();

        const target = evnt.target;
        const uri = WebSocketClient.IMAGES_PATH + "image/delete/dng/" + encodeURIComponent(path) + "/" + encodeURIComponent(name);
        console.log("Going to delete file '" + name + "' in directory '" + path + "'. Use URI '" + uri + "'.");

        const xhr = new XMLHttpRequest();
        xhr.open("DELETE", uri, true);
        xhr.setRequestHeader("Content-Type", "text/text; charset=UTF-8");
        xhr.addEventListener("abort", (evnt) => {
            Toast.create({ "title": "Abort", "message": "Request has been aborted. Current status is '" + evnt.currentTarget.status + "'.", "status": TOAST_STATUS.WARNING, "timeout": WebSocketClient.TOAST_TIMEOUT_WARNING });
            console.log("Delete picture: Abort! Status is '" + evnt.currentTarget.status + "'.");

            return false;
        });
        xhr.addEventListener("error", (evnt) => {
            Toast.create({ "title": "Abort", "message": "Deleting image occured an error. Current status is '" + evnt.currentTarget.status + "'.", "status": TOAST_STATUS.WARNING, "timeout": WebSocketClient.TOAST_TIMEOUT_WARNING });
            console.error("Delete image: Error! Status is '" + evnt.currentTarget.status + "'.");

            return false;
        });
        xhr.addEventListener("load", (evnt) => {
            const response = JSON.parse(evnt.target.response);
            if (evnt.currentTarget.readyState === 4 && evnt.currentTarget.status === 200) {
                Toast.create({ "title": "Notice", "message": response.message, "status": TOAST_STATUS.SUCCESS, "timeout": WebSocketClient.TOAST_TIMEOUT_SUCCESS });

                // Remove card
                target.closest(".grid-item").remove();
            } else {
                Toast.create({ "title": "Error", "message": "An error has been occured. Detail message: '" + response.message + "'.", "status": TOAST_STATUS.DANGER, "timeout": WebSocketClient.TOAST_TIMEOUT_DANGER });
            }

            return false;
        });
        xhr.addEventListener("loadend", (evnt) => { /*console.log("Delete picture: Load end. Status is '" + evnt.currentTarget.status + "'.");*/ return false; });
        xhr.addEventListener("loadstart", (evnt) => { /*console.log("Delete picture: Load start. Status is '" + evnt.currentTarget.status + "'.");*/ return false; });
        xhr.addEventListener("progress", (evnt) => { /*console.log("Delete picture: Load progress. Status is '" + evnt.currentTarget.status + "'.");*/ return false; });
        xhr.addEventListener("timeout", (evnt) => { /*console.error("Delete picture: Timeout! Status is '" + evnt.currentTarget.status + "'.");*/ return false; });
        xhr.send();

        return false;
    }

    /**
     * 
     */
    static downloadImageFromCache(evnt, name) {
        evnt.preventDefault();

        const uri = WebSocketClient.IMAGES_PATH + "image/download/all/" + encodeURIComponent(name);
		console.log("Download file from URL '" + uri + "'.");

        const mainLink = document.createElement("a");
        mainLink.setAttribute("href", uri);
        mainLink.setAttribute("download", name);
        document.body.appendChild(mainLink);
        mainLink.click()
		document.body.removeChild(mainLink)

        return false;
    }

    /**
     * 
     */
    static downloadImageFromRepository(evnt, name) {
        evnt.preventDefault();

        const uri = WebSocketClient.IMAGES_PATH + "image/download/dng/" + encodeURIComponent(name);
		console.log("Download file from URL '" + uri + "'.");

        const mainLink = document.createElement("a");
        mainLink.setAttribute("href", uri);
        mainLink.setAttribute("download", name);
        document.body.appendChild(mainLink);
        mainLink.click()
        document.body.removeChild(mainLink)

        return false;
    }

    /**
     * 
     */
    static createInsituForm(evnt, uuid, key, entry) {
        evnt.preventDefault();

        const parent = evnt.target.parentNode;
        parent.setAttribute("style", "display:none;");

        const form = document.getElementById("form-" + uuid + "-" + entry.tagTypeHex);
        form.setAttribute("style", "display:inline-block;");

        return false;
    }


    options;
    destination;
    subscription;

    socket;
    client;

    from = null;
    type = WebSocketClient.TYPE_STOMP;
    user = WebSocketClient.ANONYMUS;
    masonry = null;
    sortable = null;
    position = {};
    lightbox = null;
    timeline = null;
    draggable = null;
    datepicker = null;
    isConnected = false;
    isDragging = false;
    frmChat = null;
    btnSend = null;
    inpFrom = null;
    inpData = null;
    btnConnect = null;
    btnDisconnect = null;
    btnArchive = null;

    dates = new Set();
    formatter = null;

    /**
     * Constructor
     */
    constructor(destination, subscription, from) {
        this.destination = destination;
        this.subscription = subscription || "";
        this.from = from || WebSocketClient.ANONYMUS;

        this.options = {};
        this.options.locales = "de-CH";
        this.options.localDateTimeFormatOptions = {
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
        this.options.localDateFormatOptions = {
            "weekday": "short",
            "day": "2-digit",
            "month": "2-digit",
            "year": "numeric"
        };
        this.options.dateTimeFormatOptions = {
            "day": "2-digit",
            "month": "short",
            "year": "numeric",
            "hour": "2-digit",
            "minute": "2-digit",
            "second": "2-digit",
            "hour12": false
        };
    }

    /**
     * 
     */
    setConnected(isConnected) {
        this.isConnected = isConnected;

        let connect = document.getElementById("connect");
        if (connect) {
            connect.disabled = this.isConnected;
        }

        let disconnect = document.getElementById("disconnect");
        if (disconnect) {
            disconnect.disabled = !this.isConnected;
        }

        let text = document.getElementById("text");
        if (text) {
            text.disabled = !this.isConnected;
        }

        let send = document.getElementById("send").disabled;
        if (send) {
            send = !this.isConnected;
        }

        //		document.getElementById("conversation").style.visibility = this.isConnected ? "visible" : "hidden";

        return this;
    }

    /**
     * 
     */
    setSocketType(type) {
        this.type = type;

        return this;
    }

    /**
     * 
     */
    setCertificateListener(form, from, send, host, port) {

        this.frmChat = form;
        this.frmChat.addEventListener("submit", (evnt) => { evnt.preventDefault(); return false; });

        this.inpFrom = from;
        this.inpFrom.setAttribute("value", this.from);

        this.btnSend = send;
        if (this.btnSend) {
            this.btnSend.addEventListener("click", (evnt) => {
                evnt.preventDefault();

				document.getElementById("response").replaceChildren();
                this.sendCertificateHostPortData(evnt, ((host.value) ? host.value : host.placeholder), ((port.value) ? port.value : port.placeholder));

                return false;
            });
        }

        this.connect(WebSocketClient.SUBSCRIPTION_CERTIFICATES, this.subscription);

        return this;
    }

    /**
     * 
     */
    setChatListener(form, from, send, data) {

        this.frmChat = form;
        this.frmChat.addEventListener("submit", (evnt) => { evnt.preventDefault(); return false; });

        this.inpFrom = from;
        this.inpFrom.setAttribute("value", this.from);

        this.btnConnect = document.getElementById("connect");
        if (this.btnConnect) {
            this.btnConnect.addEventListener("click", (evnt) => {
                evnt.preventDefault();

                this.connect(WebSocketClient.SUBSCRIPTION_MESSAGES, this.subscription);

                return false;
            });
        }

        this.btnDisconnect = document.getElementById("disconnect");
        if (this.btnDisconnect) {
            this.btnDisconnect.addEventListener("click", (evnt) => {
                evnt.preventDefault();

                this.disconnect();

                return false;
            });
        }

        this.btnSend = send;
        if (this.btnSend) {
            this.btnSend.addEventListener("click", (evnt) => {
                evnt.preventDefault();

                this.sendMessage();

                return false;
            });
        }

        this.inpData = data;
        if (this.inpData) {
            this.inpData.addEventListener("keyup", (evnt) => {
                evnt.preventDefault();

                const keycode = (evnt.keyCode ? evnt.keyCode : evnt.which);
                if (keycode === 13 || evnt.key === "Enter") {
                    this.btnSend.click();
                }

                return false;
            });
        }

        return this;
    }

	/**
	 * 
	 */
	setLdapListener(form, from, send, protocol, host, port) {

		this.frmChat = form;
		this.frmChat.addEventListener("submit", (evnt) => { evnt.preventDefault(); return false; });

		this.inpFrom = from;
		this.inpFrom.setAttribute("value", this.from);

		this.btnSend = send;
		if (this.btnSend) {
		    this.btnSend.addEventListener("click", (evnt) => {
		        evnt.preventDefault();

		        this.sendLdapHostPortData(evnt, protocol.value, ((host.value) ? host.value : host.placeholder), ((port.value) ? port.value : port.placeholder));

		        return false;
		    });
		}

		this.connect(WebSocketClient.SUBSCRIPTION_LDAP_OBJECTS, this.subscription);

		return this;
	}

    /**
     * 
     */
    setUser(user) {
        this.user = user || WebSocketClient.ANONYMUS;

        return this;
    }

    /**
     * 
     */
    connect(subscriptionType, subscription) {
        this.subscription = subscription || "";

        /**
         * 
         */
        if (this.type === WebSocketClient.TYPE_STOMP) {

            this.socket = new SockJS(this.destination);
            this.client = Stomp.over(this.socket);
            this.client.debug = null; // See 'https://stackoverflow.com/a/25685340/6487382'
            console.log("Connect to destination '" + this.destination + "' with subscription '" + subscription + "' (type '" + subscriptionType + "').");

            this.client.connect({}, (frame) => {

                this.setConnected(true);
                console.log("Chat client connected: " + frame);
                this.client.subscribe(this.subscription, (message) => {

                    console.log("Got push message '" + message.body + "'.");

                    switch (subscriptionType) {
                        case WebSocketClient.SUBSCRIPTION_CERTIFICATES:
                            this.showCertificateMessageOutput("response", {}, WebSocketClient.stringToUUID(message.from), JSON.parse(message.body));
                            break;

						case WebSocketClient.SUBSCRIPTION_LDAP_OBJECTS:
							this.showLdapObjectMessageOutput("response", {}, WebSocketClient.stringToUUID(message.from), JSON.parse(message.body));
							break;

                        default:
                        case WebSocketClient.SUBSCRIPTION_MESSAGES:
                            this.showChatMessageOutput("response", {}, WebSocketClient.stringToUUID(message.from), JSON.parse(message.body));
                            break;
                    }
                });

                const from = document.getElementById("from").value;
                this.client.send("/app" + this.destination, {}, JSON.stringify({ "type": WebSocketClient.JOIN, "user": this.user, "from": from, "text": "«" + this.user + "» joined the chat." }));
            }, (error) => {
                const message = "Could not connect to WebSocket server. Please refresh this page to try again! (" + error + ")";
                console.error(message);
                Toast.create({ "title": "Error", "message": message, "status": TOAST_STATUS.DANGER, "timeout": WebSocketClient.TOAST_TIMEOUT_DANGER });
                this.setConnected(false);
            });
        }

        /**
         * 
         */
        if (this.type === WebSocketClient.TYPE_SOCKETIO) {
            this.socket = io.connect("wss://athene.hersche.name:8444", { "secure": true, "credentials": true });
            this.client = Stomp.over(this.socket);
            alert("???");
        }

        return this;
    }

    /**
     * 
     */
    disconnect() {
        if (this.client != null) {
            const from = document.getElementById("from").value;
            this.client.send("/app" + this.destination, {}, JSON.stringify({ "type": WebSocketClient.LEAVE, "user": this.user, "from": from, "text": "«" + this.user + "» leaves the chat." }));

            const timeOut = setTimeout(() => this.client.disconnect(), 1000);
            clearTimeout(timeOut);
        }

        this.setConnected(false);
        console.log("Disconnected from destination '" + this.destination + "'.");
    }

    /**
     * 
     */
    sendMessage() {
        this.client.send("/app" + this.destination, {}, JSON.stringify({ "type": WebSocketClient.CHAT, "user": this.user, "from": this.inpFrom.value, "text": this.inpData.value }));
    }

    /**
     * 
     */
    sendCertificateHostPortData(evnt, host, port) {
        evnt.preventDefault();

        const uri = WebSocketClient.CERTIFICATE_PATH + "certificate/" + encodeURIComponent(host) + "/" + encodeURIComponent(port);
        console.log("Going to get certificate(s) of host and port '" + host + ":" + port + "'. Use URI '" + uri + "'.");

        const options = {};
        options.method = "PUT";
        options.headers = { "Accept": "*/*", "Content-Type": "text/plain; charset=UTF-8" };

        (async () => {
            await fetch(uri, options).then((response) => {
                if (response.ok && (response.status === 200 || response.status === 201 || response.status === 202)) {
                    Toast.create({ "title": "Notice", "message": "Get certificates of host:port '" + host + ":" + port + "'.", "status": TOAST_STATUS.SUCCESS, "timeout": WebSocketClient.TOAST_TIMEOUT_SUCCESS });
				} else if (response.ok && (response.status === 204)) {
					Toast.create({ "title": "Notice", "message": "View certificate data of host:port '" + host + ":" + port + "'.", "status": TOAST_STATUS.SUCCESS, "timeout": WebSocketClient.TOAST_TIMEOUT_SUCCESS });
                } else {
                    Toast.create({ "title": "Error", "message": "An error has been occured while getting the certificates. Error code is '" + response.status + "'", "status": TOAST_STATUS.DANGER, "timeout": WebSocketClient.TOAST_TIMEOUT_DANGER });
                }
            }).catch((error) => {
                Toast.create({ "title": "Error", "message": "An error has been occured while getting the certificates. (" + JSON.stringify(error) + ").", "status": TOAST_STATUS.DANGER, "timeout": WebSocketClient.TOAST_TIMEOUT_DANGER });
            }).finally(() => {
                // 
            });
        })();

        return false;
    }

	/**
	 * 
	 */
	sendLdapHostPortData(evnt, protocol, host, port) {
		evnt.preventDefault();

		const uri = WebSocketClient.LDAP_PATH + "mgmt/" + encodeURIComponent(protocol) + "/" + encodeURIComponent(host) + "/" + encodeURIComponent(port);
		console.log("Going to get LDAP data of host and port '" + host + ":" + port + "'. Use URI '" + uri + "'.");
		
	}

    /**
     * 
     */
    initMasonry(id) {
        this.masonry = new Masonry(document.querySelector(id), {
            "resize": true,
            "originTop": true,
            "originLeft": true,
            "initLayout": true,
            "horizontalOrder": true,
            "percentPosition": true,
            "itemSelector": ".grid-item",
            "columnWidth": 600,
            "transitionDuration": 0.8
        });

        return this;
    }

    /**
     * 
     */
    initDatepicker(datepicker) {
        this.datepicker = datepicker;

        return this;
    }

    /**
     * 
     */
    initDraggable(draggable) {
        this.draggable = draggable;

        return this;
    }

    /**
     * 
     */
    initLightBox(lightbox) {
        this.lightbox = lightbox;

        return this;
    }

    /**
     * 
     */
    initSortable(sortable) {
        this.sortable = sortable;

        return this;
    }

    /**
     * 
     */
    initTimeline(timeline) {
        this.timeline = timeline;

        return this;
    }

    /**
     * 
     */
    showCertificateMessageOutput(id, options, uuid, message) {
        const text = document.createElement("li");
        text.setAttribute("class", "chat-item list-style-none");
        const content = document.createElement("pre");
        content.appendChild(document.createTextNode(message.text));
        text.appendChild(content);
        const response = document.getElementById(id);
        response.appendChild(text);
    }
	
	/**
	 * 
	 */
	showLdapObjectMessageOutput(id, options, uuid, message) {
		console.log("-----------------------------------------")
		console.log("ID      " + id)
		console.log("Options " + options)
		console.log("UUID    " + uuid)
		console.log("Message " + message)
	}

    /**
     * 
     */
    showChatMessageOutput(id, options, uuid, message) {
        const localeDateTime = this.toLocaleDateTime((message.time) ? new Date(message.time) : Date.now(), this.options.locales);

        const webUser = ((message.user) ? message.user.toLowerCase() : this.user);
        const text = document.createElement("li");
        text.setAttribute("class", "chat-item list-style-none mt-3");
        const photo = document.createElement("div");
        photo.setAttribute("class", "chat-img d-inline-block");
        const image = document.createElement("img");
        image.setAttribute("src", "../img/" + webUser + ".jpg");
        image.setAttribute("class", "rounded-circle")
        image.setAttribute("with", "45px")
        image.setAttribute("title", "User '" + message.user + "'.")
        photo.appendChild(image);
        text.appendChild(photo);
        const holder = document.createElement("div");
        holder.setAttribute("class", "chat-content d-inline-block ps-3");
        const user = document.createElement("h6");
        user.setAttribute("class", "font-weight-medium");
        user.appendChild(document.createTextNode(webUser));
        holder.appendChild(user);
        const content = document.createElement("div");
        content.setAttribute("class", "msg p-2 d-inline-block mb-1");
        content.appendChild(document.createTextNode("'" + message.text + "' (from '" + message.from + "' with UUID '" + uuid + "')"));
        holder.appendChild(content);
        const time = document.createElement("div");
        time.setAttribute("class", "chat-time d-block font-10 mt-1 mr-0 mb-3");
        time.appendChild(document.createTextNode(localeDateTime));
        holder.appendChild(time);
        text.appendChild(holder);
        const response = document.getElementById(id);
        response.appendChild(text);
        response.parentElement.scrollTop = response.scrollHeight
    }

    /**
     * 
     */
    showDocumentImageOutput(id, action, options, uuid, message, date) {
        const response = document.getElementById(id);
        const pictures = response.children;
        console.log(message.data)
        console.log("Options:    " + JSON.stringify(options));
        console.log("UUID:       " + uuid);
        console.log("Message:    " + JSON.stringify(message));
        console.log("Date:       " + JSON.stringify(date));
        console.log("Pictures:   " + pictures.length);

        if (message.data) {
            // Handle image download
            if (message.data.image.content) {
                console.log("Node UUID:  " + message.data.image.id);
                console.log("Image type: " + message.data.image.imageType);

                const picture = document.createElement("div");
                picture.setAttribute("id", "pict_" + message.data.image.id);

                const image = document.createElement("img");
                image.setAttribute("id", "main-image-" + message.data.image.id);
                image.setAttribute("class", "figure-img img-fluid rounded align-top");
                image.setAttribute("src", "data:" + message.data.image.mimetype + ";base64," + message.data.image.content);
                picture.appendChild(image);

                const grid = document.createElement("div");
                grid.appendChild(picture);
                response.insertBefore(grid, response.firstElementChild);
            }

            // Handle thumbnail plus metadata
            if (message.data.thumbnail.content) {
                console.log("Node UUID:  " + message.data.thumbnail.id);
                console.log("Thumbnail type: " + message.data.thumbnail.imageType);

                const picture = document.createElement("div");
                picture.setAttribute("id", "pict_" + message.data.thumbnail.id);

                const image = document.createElement("img");
                image.setAttribute("id", "main-thumbnail-" + message.data.thumbnail.id);
                image.setAttribute("class", "figure-img img-fluid rounded align-top");
                image.setAttribute("src", "data:" + message.data.thumbnail.mimetype + ";base64," + message.data.thumbnail.content);
                picture.appendChild(image);

                const grid = document.createElement("div");
                grid.appendChild(picture);
                response.insertBefore(grid, response.firstElementChild);
            }
        }
    }

    /**
     * 
     */
    showImageLightBoxOutput(id, options, uuid, message, date) {
        const response = document.getElementById(id);
        const pictures = response.children;
        //		console.log("Options:    " + JSON.stringify(options));
        //		console.log("UUID:       " + uuid);
        //		console.log("Message:    " + JSON.stringify(message));
        //		console.log("Date:       " + JSON.stringify(date));
        //		console.log("Pictures:   " + pictures.length);

        if (message.data) {
            // Handle image download
            if (message.data.image.content) {
                //
            }

            // Handle thumbnail plus metadata
            if (message.data.thumbnail.content) {
                //				console.log("Node UUID:  " + message.data.thumbnail.id);
                //				console.log("Image type: " + message.data.thumbnail.imageType);

                const dateNode = document.getElementById("grid_" + message.data.thumbnail.id);

                if (!dateNode) {
                    const dateRange = this.timeline.datePickerDatesToMilliseconds(this.datepicker.getDates("yyyy-mm-dd"));
                    const localDateTime = this.toLocaleDateTime(new Date(date.value), this.options.locales);
                    const href = WebSocketClient.IMAGES_PATH + "image/" + encodeURIComponent(message.data.thumbnail.name);

                    const picture = document.createElement("div");
                    picture.setAttribute("id", "pict_" + message.data.thumbnail.id);
                    picture.setAttribute("class", "gallery-picture");
                    picture.setAttribute("data-uuid", message.data.thumbnail.id);
                    picture.setAttribute("data-download", date.value);
                    picture.setAttribute("data-filename", message.data.thumbnail.name);
                    picture.setAttribute("data-filetype", message.data.thumbnail.imageType);
                    const figure = document.createElement("figure");
                    figure.setAttribute("class", "figure");
                    picture.appendChild(figure);
                    const link = document.createElement("a");
                    link.setAttribute("id", "link_" + message.data.thumbnail.id);
                    link.setAttribute("class", "col-sm-4");
                    link.setAttribute("href", href);
                    link.setAttribute("data-toggle", "lightbox");
                    link.setAttribute("data-gallery", "gallery");
                    link.setAttribute("data-caption", "The image '" + message.data.thumbnail.name + "' has been taken at " + localDateTime);
                    link.addEventListener("click", (evnt) => { evnt.preventDefault(); this.lightbox.build(link).show(); });
                    figure.appendChild(link);
                    const image = document.createElement("img");
                    image.setAttribute("id", "main-thumbnail-" + message.data.thumbnail.id);
                    image.setAttribute("class", "figure-img img-fluid rounded align-top");
                    image.setAttribute("src", "data:" + message.data.thumbnail.mimetype + ";base64," + message.data.thumbnail.content);
                    image.setAttribute("width", "150");
                    image.setAttribute("height", "150");
                    image.setAttribute("title", message.data.thumbnail.name + " (" + localDateTime + ")");
                    link.appendChild(image);
                    const caption = document.createElement("figcaption");
                    caption.setAttribute("class", "figure-caption");
                    caption.appendChild(document.createTextNode(this.toDateTime(new Date(date.value), this.options.locales)));
                    figure.appendChild(caption);

                    // Add complete card to response
                    const gridNode = document.createElement("div");
                    gridNode.setAttribute("id", "grid_" + message.data.thumbnail.id);
                    gridNode.setAttribute("class", "grid-item");
                    gridNode.setAttribute("data-download", date.value);
                    gridNode.appendChild(picture);

                    if (!pictures.length) {
                        response.insertBefore(gridNode, response.firstElementChild);
                    }

                    // Iterate over gallery collection
                    for (let i = 0;i < pictures.length;i++) {
                        const currentPict = pictures[i];
                        const currentData = parseInt(currentPict.getAttribute("data-download"), 10);
                        const newDownload = parseInt(gridNode.getAttribute("data-download"), 10);

                        // Compare value of new card node with the value of the current node
                        if (newDownload < currentData) {
                            // Insert new node before the current node
                            response.insertBefore(gridNode, currentPict);
                            break;
                        }
                    }

                    if (!gridNode.parentNode) {
                        response.appendChild(gridNode);
                    }

                } else {
                    // Picture exists alread, update it
                }

                // Update time range selector
                let minDownload = Number.MAX_VALUE;
                let maxDownload = Number.MIN_VALUE;

                Array.from(document.getElementsByClassName("gallery-picture")).forEach((node, idx) => {
                    const download = parseInt(node.getAttribute("data-download"), 10);

                    minDownload = Math.min(minDownload, download);
                    maxDownload = Math.max(maxDownload, download);
                });

                // Update date range picker
                this.datepicker.setDates(minDownload, maxDownload);
            }
        }

        // Update image counter
        const textNode = document.getElementById("process-nbr").firstChild;
        textNode.firstChild.remove();
        textNode.appendChild(document.createTextNode(pictures.length));
    }

    /**
     * 
     */
    showImageMessageOutput(id, action, options, uuid, message) {
        const response = document.getElementById(id);

        if (message.data) {
            // Handle image download
            if (message.data.image.content) {
                //
            }

            // Handle thumbnail plus metadata
            if (message.data.thumbnail.content) {

                // Configure geo location
                let locationMap = null;
                const location = message.data.thumbnail.metadata.geoLocation;

                if (location.configured) {
                    const point = new GeoPoint(location.longitudeVal + location.longitudeRef, location.latitudeVal + location.latitudeRef);

                    /*
                    const locationOptions = {
                        "type": GeoLocationMap.GOOGLE,
                        "key": options.googleApiKey,
                        "version": "beta",
                        "point": point,
                        "zoom": options.zoom,,
                        "height": options.height,
                        "language": options.language
                    };
                    */
                    const locationOptions = {
                        "point": point,
                        "zoom": options.zoom,
                        "width": options.width,
                        "height": options.height
                    };

                    locationMap = new GeoLocationMap(locationOptions);
                }

                // Start building cards
                const card = document.createElement("div");
                card.setAttribute("id", message.data.thumbnail.name);
                card.setAttribute("class", "card");
                card.setAttribute("style", "width: 600px;");
                // Navigation
                const header = document.createElement("div");
                header.setAttribute("class", "card-header");
                card.appendChild(header);
                const tabs = document.createElement("ul");
                tabs.setAttribute("class", "nav nav-tabs card-header-tabs");
                header.appendChild(tabs);
                // Default navigation tab 'Main'
                const itemMain = document.createElement("li");
                itemMain.setAttribute("class", "nav-item");
                tabs.appendChild(itemMain);
                const itemMainLink = document.createElement("a");
                itemMainLink.setAttribute("href", "#main");
                itemMainLink.setAttribute("id", "main-nvgt-" + uuid);
                itemMainLink.setAttribute("class", "nav-link " + uuid + " active ");
                itemMainLink.setAttribute("role", "tab");
                itemMainLink.setAttribute("data-toggle", "tab");
                itemMainLink.setAttribute("data-target", "main");
                itemMainLink.setAttribute("aria-controls", "main");
                itemMainLink.setAttribute("aria-selected", "true");
                itemMainLink.appendChild(document.createTextNode("Main"));
                itemMainLink.addEventListener("click", (evnt) => { return WebSocketClient.navigation(evnt, "main", uuid); });
                itemMain.appendChild(itemMainLink);
                tabs.appendChild(itemMain);
                // Navigation tab 'Info'
                const itemInfo = document.createElement("li");
                itemInfo.setAttribute("class", "nav-item");
                const itemInfoLink = document.createElement("a");
                itemInfoLink.setAttribute("href", "#info");
                itemInfoLink.setAttribute("id", "info-nvgt-" + uuid);
                itemInfoLink.setAttribute("class", "nav-link " + uuid + " fa-duotone fa-solid fa-circle-info information");
                itemInfoLink.setAttribute("role", "tab");
                itemInfoLink.setAttribute("data-toggle", "tab");
                itemInfoLink.setAttribute("data-target", "info");
                itemInfoLink.setAttribute("aria-controls", "info");
                itemInfoLink.setAttribute("aria-selected", "false");
                itemInfoLink.addEventListener("click", (evnt) => { return WebSocketClient.navigation(evnt, "info", uuid); });
                itemInfo.appendChild(itemInfoLink);
                tabs.appendChild(itemInfo);
                // Navigation tab 'Location'
                const itemGeo = document.createElement("li");
                itemGeo.setAttribute("class", "nav-item");
                const itemGeoLink = document.createElement("a");
                itemGeoLink.setAttribute("href", "#geo");
                itemGeoLink.setAttribute("id", "geo-nvgt-" + uuid);
                itemGeoLink.setAttribute("class", "nav-link " + uuid + " fa-duotone fa-solid fa-location-dot location");
                itemGeoLink.setAttribute("role", "tab");
                itemGeoLink.setAttribute("data-toggle", "tab");
                itemGeoLink.setAttribute("data-target", "geo");
                itemGeoLink.setAttribute("aria-controls", "geo");
                itemGeoLink.setAttribute("aria-selected", "false");
                itemGeoLink.addEventListener("click", (evnt) => { if (locationMap) { locationMap.updateMap(); } return WebSocketClient.navigation(evnt, "geo", uuid); });
                itemGeo.appendChild(itemGeoLink);
                tabs.appendChild(itemGeo);
                // Section navigation tabs
                for (const sectionKey in message.data.thumbnail.metadata) {
                    // Get all sections
                    if (sectionKey.toLocaleLowerCase !== "info") {
                        const sectionObject = message.data.thumbnail.metadata[sectionKey];
                        if (sectionObject && sectionObject.details && sectionObject.details.constructor === Object && Object.keys(sectionObject.details).length > 0) {

                            // Section navigation tab
                            const sectionId = sectionObject.section.toLowerCase().replaceAll(WebSocketClient.ID_FILTER, "_");
                            const itemSection = document.createElement("li");
                            itemSection.setAttribute("class", "nav-item");
                            const itemSectionLink = document.createElement("a");
                            itemSectionLink.setAttribute("href", "#" + sectionId);
                            itemSectionLink.setAttribute("id", sectionId + "-nvgt-" + uuid);
                            itemSectionLink.setAttribute("class", "nav-link " + uuid);
                            itemSectionLink.setAttribute("role", "tab");
                            itemSectionLink.setAttribute("data-toggle", "tab");
                            itemSectionLink.setAttribute("data-target", sectionId);
                            itemSectionLink.setAttribute("aria-controls", sectionId);
                            itemSectionLink.setAttribute("aria-selected", "false");
                            itemSectionLink.appendChild(document.createTextNode(sectionObject.section));
                            itemSectionLink.addEventListener("click", (evnt) => { return WebSocketClient.navigation(evnt, sectionId, uuid); });
                            itemSection.appendChild(itemSectionLink);
                            tabs.appendChild(itemSection);
                        }
                    } else {
                        //
                    }
                }

                const action = message.data.thumbnail.action.toLowerCase();
                const localeDateTime = this.toLocaleDateTime(new Date(message.time), this.options.locales);

                // Image content
                const container = document.createElement("div");
                container.setAttribute("class", "container-fluid");
                const row = document.createElement("div");
                row.setAttribute("class", "row");
                const imageStart = document.createElement("div");
                imageStart.setAttribute("class", "col-9 d-flex justify-content-start");
                const image = document.createElement("img");
                image.setAttribute("id", "main-thumbnail-" + uuid);
                image.setAttribute("class", "card-img-top align-top");
                image.setAttribute("src", "data:" + message.data.thumbnail.mimetype + ";base64," + message.data.thumbnail.content);
                image.setAttribute("width", "150");
                image.setAttribute("height", "150");
                image.setAttribute("title", message.text);
                imageStart.appendChild(image);
                row.appendChild(imageStart);
                if (action) {
                    const editHolder = document.createElement("div");
                    editHolder.setAttribute("class", "col-1 d-flex justify-content-end");
                    const sync = document.createElement("button");
                    sync.setAttribute("id", "main-editfile-" + uuid);
                    sync.setAttribute("class", "btn-editfile align-top");
                    sync.setAttribute("type", "button");
                    sync.setAttribute("title", "Update/modify uploaded multimedia file.");
                    sync.addEventListener("click", (evnt) => { return WebSocketClient.editImageOnCache(evnt, action, message.data.thumbnail.name); });
                    editHolder.appendChild(sync);
                    row.appendChild(editHolder);
                }
                if (action === Downloader.UPLOAD) {
                    const syncHolder = document.createElement("div");
                    syncHolder.setAttribute("class", "col-1 d-flex justify-content-end");
                    const sync = document.createElement("button");
                    sync.setAttribute("id", "main-syncronize-" + uuid);
                    sync.setAttribute("class", "btn-syncronize align-top");
                    sync.setAttribute("type", "button");
                    sync.setAttribute("title", "Synchronize tag 'Date/Time' (if available)");
                    sync.addEventListener("click", (evnt) => { return WebSocketClient.synchronizeDateTimeValues(evnt, null, message.data.thumbnail.name, message.data.thumbnail.date); });
                    syncHolder.appendChild(sync);
                    row.appendChild(syncHolder);
                    const removeHolder = document.createElement("div");
                    removeHolder.setAttribute("class", "col-1 d-flex justify-content-end");
                    const remove = document.createElement("button");
                    remove.setAttribute("id", "main-remove-" + uuid);
                    remove.setAttribute("class", "btn-remove align-top");
                    remove.setAttribute("type", "button");
                    remove.setAttribute("title", "Remove card and delete file");
                    remove.addEventListener("click", (evnt) => { return WebSocketClient.deleteImageFromRepository(evnt, action, message.data.thumbnail.name); });
                    removeHolder.appendChild(remove);
                    row.appendChild(removeHolder);
                }
                if (action === Downloader.DOWNLOAD) {
                    const syncHolder = document.createElement("div");
                    syncHolder.setAttribute("class", "col-1 d-flex justify-content-end");
                    const sync = document.createElement("button");
                    sync.setAttribute("id", "main-download-" + uuid);
                    sync.setAttribute("class", "btn-download align-top");
                    sync.setAttribute("type", "button");
                    sync.setAttribute("title", "Download updated multimedia file.");
                    sync.addEventListener("click", (evnt) => { return WebSocketClient.downloadImageFromRepository(evnt, message.data.thumbnail.name); });
                    syncHolder.appendChild(sync);
                    row.appendChild(syncHolder);
                    const removeHolder = document.createElement("div");
                    removeHolder.setAttribute("class", "col-1 d-flex justify-content-end");
                    const remove = document.createElement("button");
                    remove.setAttribute("id", "main-remove-" + uuid);
                    remove.setAttribute("class", "btn-remove align-top");
                    remove.setAttribute("type", "button");
                    remove.setAttribute("title", "Remove card and delete file");
                    remove.addEventListener("click", (evnt) => { return WebSocketClient.deleteImageFromRepository(evnt, action, message.data.thumbnail.name); });
                    removeHolder.appendChild(remove);
                    row.appendChild(removeHolder);
                }
                if (action === Downloader.MEMORY) {
                    const syncHolder = document.createElement("div");
                    syncHolder.setAttribute("class", "col-1 d-flex justify-content-end");
                    const sync = document.createElement("button");
                    sync.setAttribute("id", "main-safefile-" + uuid);
                    sync.setAttribute("class", "btn-safefile align-top");
                    sync.setAttribute("type", "button");
                    sync.setAttribute("title", "Download updated multimedia file.");
                    sync.addEventListener("click", (evnt) => { return WebSocketClient.downloadImageFromCache(evnt, message.data.thumbnail.name); });
                    syncHolder.appendChild(sync);
                    row.appendChild(syncHolder);
                    const removeHolder = document.createElement("div");
                    removeHolder.setAttribute("class", "col-1 d-flex justify-content-end");
                    const remove = document.createElement("button");
                    remove.setAttribute("id", "main-remove-" + uuid);
                    remove.setAttribute("class", "btn-remove align-top");
                    remove.setAttribute("type", "button");
                    remove.setAttribute("title", "Remove card and delete file");
                    remove.addEventListener("click", (evnt) => { return WebSocketClient.deleteImageFromRepository(evnt, action, message.data.thumbnail.name); });
                    removeHolder.appendChild(remove);
                    row.appendChild(removeHolder);
                }
                container.appendChild(row);
                card.appendChild(container);

                // Body
                const body = document.createElement("div");
                body.setAttribute("id", "main-body-" + uuid);
                body.setAttribute("class", "tab-content card-body");
                card.appendChild(body);

                // Main content
                const main = document.createElement("div");
                main.setAttribute("id", "main-" + uuid);
                main.setAttribute("class", "tab-pane active");
                const mainIcon = document.createElement("i");
                if (message.data.thumbnail.imageType === Converter.STANDARD_SCAN || message.data.thumbnail.imageType === Converter.STANDARD_IMAGE || message.data.thumbnail.imageType === Converter.WHATSAPP_IMAGE) {
                    mainIcon.setAttribute("class", "bi bi-image");
                } else {
                    mainIcon.setAttribute("class", "bi bi-film");
                }
                const mainTitle = document.createElement("h5");
                mainTitle.setAttribute("id", "main-title-" + uuid);
                mainTitle.setAttribute("class", "card-title");
                mainTitle.setAttribute("title", "File name: '" + message.data.thumbnail.name + "'.");
                mainTitle.appendChild(mainIcon);
                mainTitle.appendChild(document.createTextNode(" " + message.data.thumbnail.name));
                main.appendChild(mainTitle);
                const mainText = document.createElement("p");
                mainText.setAttribute("id", "main-text-" + uuid);
                mainText.setAttribute("class", "card-text");
                //mainText.setAttribute("title", "Date/Time: '" + localeDateTime + "'.");
                mainText.appendChild(document.createTextNode(message.text));
                main.appendChild(mainText);
                body.appendChild(main);

                // Info content
                const info = document.createElement("div");
                info.setAttribute("id", "info-" + uuid);
                info.setAttribute("class", "tab-pane");
                info.setAttribute("style", "display:none;");
                const infoTitle = document.createElement("h5");
                infoTitle.setAttribute("id", "info-title-" + uuid);
                infoTitle.setAttribute("class", "card-title");
                infoTitle.setAttribute("title", "File name: '" + message.data.thumbnail.name + "'.");
                infoTitle.appendChild(document.createTextNode(message.data.thumbnail.name + ": Basic picture properties"));
                info.appendChild(infoTitle);
                const infoText = document.createElement("div");
                infoText.setAttribute("id", "info-text-" + uuid);
                infoText.setAttribute("class", "card-text");
                infoText.setAttribute("title", "Date/Time: '" + localeDateTime + "'.");
                const infoDetails = Converter.convertJson2DefaultTable(message.data.thumbnail.metadata.info);
                infoText.appendChild(infoDetails.table);
                info.appendChild(infoText);
                body.appendChild(info);

                // Geo content
                const geo = document.createElement("div");
                geo.setAttribute("id", "geo-" + uuid);
                geo.setAttribute("class", "tab-pane");
                geo.setAttribute("style", "display:none;");
                const geoTitle = document.createElement("h5");
                geoTitle.setAttribute("id", "geo-title-" + uuid);
                geoTitle.setAttribute("class", "card-title");
                geoTitle.setAttribute("title", "File name: '" + message.data.thumbnail.name + "'.");
                geoTitle.appendChild(document.createTextNode(message.data.thumbnail.name + ": Geo location properties"));
                geo.appendChild(geoTitle);
                const geoText = document.createElement("div");
                geoText.setAttribute("id", "geo-text-" + uuid);
                geoText.setAttribute("class", "card-text");
                geoText.setAttribute("title", "Date/Time: '" + localeDateTime + "'.");
                geo.appendChild(geoText);

                body.appendChild(geo);

                if (locationMap) {
                    locationMap.init().appendMap(geo, uuid, this.showTitle(message.data.thumbnail.metadata)).finishMapLoading();

                    window.initMap = locationMap.showMap();
                }

                const dateTime = new Map();
                for (const sectionKey in message.data.thumbnail.metadata) {
                    // Get all sections
                    if (sectionKey.toLocaleLowerCase !== "info" && sectionKey.toLocaleLowerCase !== "geoLocation") {

                        let sectionObject = message.data.thumbnail.metadata[sectionKey];
                        if (sectionObject && sectionObject.details && sectionObject.details.constructor === Object && Object.keys(sectionObject.details).length > 0) {

                            // Section content
                            const sectionId = sectionObject.section.toLowerCase().replaceAll(WebSocketClient.ID_FILTER, "_");
                            const section = document.createElement("div");
                            section.setAttribute("id", sectionId + "-" + uuid);
                            section.setAttribute("class", "tab-pane");
                            section.setAttribute("style", "display:none;");
                            const sectionTitle = document.createElement("h5");
                            sectionTitle.setAttribute("id", sectionId + "-title-" + uuid);
                            sectionTitle.setAttribute("class", "card-title");
                            sectionTitle.setAttribute("title", "Section name: '" + sectionObject.section + "'.");
                            sectionTitle.appendChild(document.createTextNode(message.data.thumbnail.name + ": " + sectionObject.section));
                            section.appendChild(sectionTitle);
                            const sectionText = document.createElement("div");
                            sectionText.setAttribute("id", sectionId + "-text-" + uuid);
                            sectionText.setAttribute("class", "card-text");
                            sectionText.setAttribute("title", "Date/Time: '" + localeDateTime + "'.");
                            const sectionDetails = Converter.convertJson2ValueTable(document.createElement("table"), dateTime, message.data.thumbnail.name, sectionObject.label, sectionId, uuid, sectionObject, ["section"]);
                            sectionText.appendChild(sectionDetails.table);
                            section.appendChild(sectionText);
                            body.appendChild(section);
                        }
                    }
                }

                // Add date(s) to main tab
                const mainDate = document.createElement("div");
                mainDate.setAttribute("id", "main-text-" + uuid);
                mainDate.setAttribute("class", "card-text editable");
                mainDate.setAttribute("title", "Date/Time: '" + localeDateTime + "'.");
                const mainList = document.createElement("ul");
                const dateList = new Map([...dateTime.entries()].sort());
				console.log(dateList)
                for (let [key, entry] of dateList) {
                    const mainItem = document.createElement("li");
                    const mainClock = document.createElement("i");
                    mainClock.setAttribute("class", "far fa-clock");
                    mainClock.setAttribute("aria-hidden", "true");
                    const mainLabel = document.createElement("div");
                    mainLabel.setAttribute("id", "label-" + uuid + "-" + entry.tagTypeHex);
                    mainLabel.setAttribute("class", "label");
                    mainLabel.setAttribute("title", "Directory: '" + entry.directory + "', Tag ID: '" + entry.tagTypeHex + "'.");
                    mainLabel.appendChild(mainClock);
                    mainLabel.appendChild(document.createTextNode(" " + entry.label));
                    mainItem.appendChild(mainLabel);
                    const mainEdit = document.createElement("i");
                    mainEdit.setAttribute("class", "far fa-pen-to-square");
                    mainEdit.setAttribute("aria-hidden", "true");
                    mainEdit.addEventListener("click", (evnt) => { return WebSocketClient.createInsituForm(evnt, uuid, key, entry); });
                    const mainValue = document.createElement("div");
                    mainValue.setAttribute("id", "value-" + uuid + "-" + entry.tagTypeHex);
                    mainValue.setAttribute("class", "value");
                    //mainValue.setAttribute("data-value", entry.value);
                    mainValue.appendChild(mainEdit);
                    mainValue.appendChild(document.createTextNode(" " + entry.value));
                    mainItem.appendChild(mainValue);
                    mainList.appendChild(mainItem);
                    mainItem.appendChild(this.generateDatePicker(uuid, key, entry, message.data.thumbnail.name));

                    // Register event handlers
                    window.addEventListener("onDateChange" + uuid + key, (evnt) => {
						//console.log('Date change:', evnt.detail, " --> " + "value-" + uuid + "-" + entry.tagTypeHex);
                        const input = document.getElementById("datetime-" + uuid + "-" + entry.tagTypeHex);
                        const value = input.getAttribute("value").split(" ");
                        value[0] = evnt.detail.date.split(" ")[0];
                        input.setAttribute("value", value.join(" "));
                    });
                    window.addEventListener("onTimeChange" + uuid + key, (evnt) => {
                        //console.log('Time change:', evnt.detail);
                        const input = document.getElementById("datetime-" + uuid + "-" + entry.tagTypeHex);
                        const value = input.getAttribute("value").split(" ");
						const seconds = value[1].split(":")[2];
                        value[1] = evnt.detail.time;
                        input.setAttribute("value", value.join(" ") + ":" + seconds);
                    });

                }
                mainDate.appendChild(mainList);
                main.appendChild(mainDate);
                body.appendChild(main);

                // Add complete card to response
                const grid = document.createElement("div");
                grid.setAttribute("class", "grid-item");
                grid.appendChild(card);
                response.insertBefore(grid, response.firstElementChild);
            }
        }

        // Update image counter
        const textNode = document.getElementById("process-nbr").firstChild;
        textNode.firstChild.remove();
        textNode.appendChild(document.createTextNode(response.children.length));

        if (action) {
            document.getElementById(action).style = "display:block;";
        }
    }

    /**
     * https://www.blackbox.ai/chat/raZ96OM
     * https://jsfiddle.net/herscju/t0gcafbv/195/
     * https://medium.com/codex/drag-n-drop-with-vanilla-javascript-75f9c396ecd
     * 
     * Insert before / after / replace: https://stackoverflow.com/a/8856959/6487382
     */
    showImageTimelineOutput(id, action, options, uuid, message, date) {
        // Get container HTMLCollection and available dates
        const timeaxis = document.getElementById(id);
        const cards = timeaxis.children;

        this.formatter = new Intl.NumberFormat(this.options.locales, { "maximumSignificantDigits": options.maximumSignificantDigits });

        if (message.data) {

            // Handle image download
            if (message.data.image.content) {
                //
            }

            // Handle thumbnail plus metadata
            if (message.data.thumbnail.content) {
                const dateNode = document.getElementById("date_" + message.data.thumbnail.id);

                // No card with UUID exists --> create and add new one
                if (!dateNode) {
                    const dateRange = this.timeline.datePickerDatesToMilliseconds(this.datepicker.getDates("yyyy-mm-dd"));
                    const cardNode = this.timeline.generateCardNode(date.value, message.data.thumbnail, false, (evnt) => { this.cardDragOver(evnt); });
                    const fromNode = this.addGoalEventListener(this.timeline.setFromNode(this.timeline.generateGoalNode(dateRange.from, null, message.data.thumbnail.id, this.toDateTime(date.value), null)));
                    const goalNode = this.addGoalEventListener(this.timeline.generateGoalNode(date.value, null, message.data.thumbnail.id, this.toDateTime(date.value), null));
                    const untilNode = this.addGoalEventListener(this.timeline.setUntilNode(this.timeline.generateGoalNode(dateRange.until, null, message.data.thumbnail.id, this.toDateTime(date.value), null)));

                    // If image type is TRUE, add event handler to the first node of the card
                    if (message.data.thumbnail.imageType === Converter.WHATSAPP_IMAGE || message.data.thumbnail.imageType === Converter.WHATSAPP_VIDEO) {
                        this.addCardEventListener(cardNode.firstElementChild);
                    }

                    if (!cards.length) {
                        timeaxis.appendChild(fromNode);
                    }

                    // Iterate over cards collection
                    for (let i = 0;i < cards.length;i++) {
                        const currentCard = cards[i];
                        const currentData = parseInt(currentCard.getAttribute("data-download"), 10);
                        const newDownload = parseInt(cardNode.getAttribute("data-download"), 10);

                        // Compare value of new card node with the value of the current node
                        if (newDownload < currentData) {
                            // Insert new node before the current node
                            timeaxis.insertBefore(goalNode, currentCard);
                            timeaxis.insertBefore(cardNode, currentCard);
                            break;
                        }

                        if (!cardNode.parentNode) {
                            timeaxis.appendChild(cardNode);
                            timeaxis.appendChild(goalNode);
                        }
                    }

                    // Update goal nodes (just to make sure... ;-)
                    let minDownload = 0;
                    let maxDownload = 0;
                    const cardNodes = document.getElementsByClassName("timeline-card");

                    Array.from(cardNodes).forEach((cardNode, idx) => {
                        const download = parseInt(cardNode.getAttribute("data-download"), 10);

                        if (!idx) {
                            minDownload = download - WebSocketClient.DIFFERENCE;
                            this.timeline.updateGoalNode(cardNode.previousElementSibling, minDownload, null, message.data.thumbnail.id);
                        } else {
                            this.timeline.updateGoalNode(cardNode.previousElementSibling, download - WebSocketClient.DIFFERENCE, null, message.data.thumbnail.id);
                        }

                        if (idx === cardNodes.length - 1) {
                            maxDownload = download + WebSocketClient.DIFFERENCE;
                            this.timeline.updateGoalNode(cardNode.nextElementSibling, maxDownload, null, message.data.thumbnail.id);
                        }
                    });

                    // Update date range picker
                    this.datepicker.setDates(minDownload, maxDownload);

                } else {
                    // Card exists alread, update it
                    const cardNode = this.timeline.generateCardNode(date.value, message.data.thumbnail, true, (evnt) => { this.cardDragOver(evnt); });
                    this.addCardEventListener(cardNode.firstElementChild);
                    dateNode.replaceWith(cardNode);
                }
            }

        }

        // Update image counter
        const textNode = document.getElementById("process-nbr").firstChild;
        textNode.firstChild.remove();
        textNode.appendChild(document.createTextNode(this.timeline.getCardsSize()));

        if (action) {
            document.getElementById(action).style = "display: block;";
        }

        /*
        const rect = timeaxis.getBoundingClientRect();
        console.log("===============================================")
        console.log(rect);
        */
    }

    /**
     * 
     */
    addCardEventListener(cardNode) {
        cardNode.addEventListener("drag", (evnt) => { this.cardDrag(evnt); });
        cardNode.addEventListener("dragstart", (evnt) => { this.cardDragStart(evnt); });
        cardNode.addEventListener("dragend", (evnt) => { this.cardDragEnd(evnt); });

        return cardNode;
    }

    /**
     * 
     */
    addGoalEventListener(goalNode) {
        goalNode.addEventListener("dragleave", (evnt) => { this.goalDragLeave(evnt); });
        goalNode.addEventListener("dragover", (evnt) => { this.goalDragOver(evnt); });
        goalNode.addEventListener("drop", (evnt) => { this.goalDrop(evnt); });

        return goalNode;
    }

    /**
     * 
     */
    cardDrag(evnt) {
        /* */
    }

    /**
     * 
     */
    cardDragStart(evnt) {
        evnt.stopPropagation();
        evnt.dataTransfer.clearData();
        evnt.dataTransfer.setData("text/plain", JSON.stringify({ "id": evnt.target.id, "uuid": evnt.target.getAttribute("data-uuid"), "download": evnt.target.getAttribute("data-download"), "filename": evnt.target.getAttribute("data-filename"), "filetype": evnt.target.getAttribute("data-filetype") }));
        evnt.dataTransfer.effectAllowed = "move";
        evnt.target.classList.add("card-dragging");

        this.isDragging = true;
    }

    /**
     * 
     */
    cardDragEnd(evnt) {
        evnt.preventDefault();
        evnt.target.classList.remove("card-dragging");

        this.isDragging = false;
    }

    /**
     * 
     */
    cardDragOver(evnt) {
        evnt.preventDefault();
        evnt.stopPropagation();

        return Draggable.scroll(evnt, { "dragging": this.isDragging, "threshold": WebSocketClient.THRESHOLD, "speed": WebSocketClient.SCROLL_SPEED });
    }

    /**
     * 
     */
    goalDragOver(evnt) {
        evnt.preventDefault();
        evnt.stopPropagation();
        evnt.dataTransfer.dropEffect = "move";
        evnt.target.classList.add("card-dropping");

        return Draggable.scroll(evnt, { "dragging": this.isDragging, "threshold": WebSocketClient.THRESHOLD, "speed": WebSocketClient.SCROLL_SPEED });
    }

    /**
     * 
     */
    goalDragLeave(evnt) {
        evnt.preventDefault();
        evnt.stopPropagation();
        evnt.target.classList.remove("card-dropping");
    }

    /**
     * https://stackabuse.com/drag-and-drop-in-vanilla-javascript/
     * https://dev.to/devrohit0/how-to-create-a-drag-and-drop-interface-using-vanilla-js-9bi
     */
    goalDrop(evnt) {
        evnt.preventDefault();
        evnt.stopPropagation();

        // Get data as JSON object
        const data = JSON.parse(evnt.dataTransfer.getData("text/plain"));

        const fromNode = this.timeline.getFromNode();
        const untilNode = this.timeline.getUntilNode();

        // Get download time of from and until goal node
        const minDownload = parseInt(fromNode.getAttribute("data-download"), 10);
        const maxDownload = parseInt(untilNode.getAttribute("data-download"), 10);

        // Get container node
        const dateNode = document.getElementById("date_" + data.uuid);

        // Create new card containing dragged content
        const cardNode = this.timeline.generateCardNodeMainTimeLine(data.download, data.uuid, data.filename, data.filetype);
        cardNode.appendChild(document.getElementById(data.id));

        // Replace selected goal with new card
        evnt.target.replaceWith(cardNode);

        // Remove old container and goal node from DOM
        dateNode.previousSibling.remove();
        dateNode.remove();

        // Insert new goal nodes to the DOM above and below of the card
        cardNode.before(this.addGoalEventListener(this.timeline.updateGoalNode(this.timeline.generateGoalNode(minDownload, null, data.uuid), minDownload, null, data.uuid)));
        cardNode.after(this.addGoalEventListener(this.timeline.updateGoalNode(this.timeline.generateGoalNode(maxDownload, null, data.uuid), maxDownload, null, data.uuid)));

        // Calculate and update date and time of moved card...
        const prev = parseInt(this.timeline.getData(this.timeline.getPreviousSibling(cardNode, ".timeline-card", ".timeline-goal"), "data-download"), 10);
        const next = parseInt(this.timeline.getData(this.timeline.getNextSibling(cardNode, ".timeline-card", ".timeline-goal"), "data-download"), 10);
        const download = this.timeline.calculateDownload(prev, next);
        const dateTime = this.formatDateTime(download);

        // Create new date & time node and replace original
        const timeNode = document.getElementById("time_" + data.uuid);
        if (timeNode) {
            timeNode.replaceWith(this.timeline.generateCardNodeMainTimeClock(download, data.uuid, dateTime));
        }

        // Update original image
        WebSocketClient.synchronizeDateTimeValues(evnt, "whatsapp", data.filename, download);
    }

    /**
     * 
     */
    showTitle(metadata) {
        let value = "";

        if (metadata && metadata.iptcDetails && metadata.iptcDetails.details && metadata.iptcDetails.details.IPTC) {
            metadata.iptcDetails.details.IPTC.some(iptc => {
                if (iptc.tagTypeHex === "0x0205") {
                    value = iptc.value;
                }
            });
        }

        return value;
    }

    /**
     * 
     */
    generateDatePicker(uuid, key, entry, name) {

        const itemForm = document.createElement("form");
        itemForm.setAttribute("id", "form-" + uuid + "-" + entry.tagTypeHex);
        itemForm.setAttribute("class", "datetime");
        itemForm.setAttribute("action", "#");
        itemForm.setAttribute("style", "display:none;");
        const itemPicker = document.createElement("div");
        itemPicker.setAttribute("id", "datepicker-" + uuid + "-" + entry.tagTypeHex);
        itemPicker.setAttribute("class", "input-group");
        itemForm.appendChild(itemPicker);
        const itemDate = document.createElement("div");
        itemDate.setAttribute("class", "justify-content-start datepicker-input");
        itemPicker.appendChild(itemDate);
        const itemIcon = document.createElement("i");
        itemIcon.setAttribute("class", "input-group-text far fa-calendar");
        itemDate.appendChild(itemIcon);
        const itemInput = document.createElement("input");
        itemInput.setAttribute("id", "datetime-" + uuid + "-" + entry.tagTypeHex);
        itemInput.setAttribute("name", "datetime");
        itemInput.setAttribute("class", "form-control date");
        itemInput.setAttribute("type", "text");
        itemInput.setAttribute("value", entry.value);
        itemDate.appendChild(itemInput);
        //const dateTimePicker = this.createDateTimePicker(itemInput);
        const calendar = this.createCalendarPicker(itemInput, uuid, key, entry, name);

        return itemForm;
    }

    /**
     * 
     */
    createCalendarPicker(itemInput, uuid, key, entry, name) {

        const { Calendar } = window.VanillaCalendarPro;
        const isoDateTime = new Date(entry.isoDate + " " + entry.isoTime);
        const options = {
            "type": "default",
            "locale": "de-CH",
            "inputMode": true,
            "positionToInput": "auto",
            "enableWeekNumbers": true,
            "dateToday": isoDateTime,
            "selectionTimeMode": 24,
            "selectedTime": isoDateTime.getHours() + ":" + isoDateTime.getMinutes(),
            "onChangeToInput": (self) => {
                //console.log(self.context.selectedDates);
                if (!self.context.inputElement) {
                    return;
                }
                if (self.context.selectedDates[0]) {
                    const dateEvent = new CustomEvent("onDateChange" + uuid + key, {
                        "detail": { "date": this.toStringWithoutTimezone(new Date(self.context.selectedDates[0])) }
                    });
                    window.dispatchEvent(dateEvent);
                }
            },
            "onChangeTime": (self) => {
                //console.log(self.context.selectedTime);
                if (!self.context.inputElement) {
                    return;
                }
                if (self.context.selectedTime) {
                    const timeEvent = new CustomEvent("onTimeChange" + uuid + key, {
                        "detail": { "time": self.context.selectedTime }
                    });
                    window.dispatchEvent(timeEvent);
                }
            },
            "onHide": () => {
				const form = document.getElementById("form-" + uuid + "-" + entry.tagTypeHex);
				form.setAttribute("style", "display:none;");
				const value = document.getElementById("value-" + uuid + "-" + entry.tagTypeHex);
				value.removeAttribute("style");
				const input = document.getElementById("datetime-" + uuid + "-" + entry.tagTypeHex);
				const text = Array.from(value.childNodes).find(node => node.nodeType == Node.TEXT_NODE);
				text.nodeValue = " " + input.getAttribute("value");
				WebSocketClient.synchronizeDateTimeValues(null, "adjust", name, input.getAttribute("value"));				
            }
        };
        const calendar = new Calendar(itemInput, options);
        calendar.init();

        return calendar;
    }

    /**
     * 
     */
    createDateTimePicker(itemInput) {

        const datepicker = new Datepicker(itemInput, {
            "buttonClass": "btn",
            "autohide": true,
            "format": "yyyy:mm:dd HH:mm:SS",
            "language": "de",
            "maxDate": Date.now(),
            "showOnClick": true,
            "showOnFocus": true,
            "clearButton": true,
            "updateOnBlur": true,
            "todayHighlight": true,
            "todayButton": true,
            "todayButtonMode": 0
        });

        return datepicker;
    }

    /**
     *
     */
    formatDateTime(input) {
        return this.toStringWithoutTimezone(new Date(input));
    }

    /**
     * 
     */
    toDateTime(date, locale) {
        const formatter = new Intl.DateTimeFormat(locale, this.options.dateTimeFormatOptions);

        return formatter.format(date);
    }

    /**
     * 
     */
    toLocaleDateTime(date, locale) {
        const formatter = new Intl.DateTimeFormat(locale, this.options.localDateTimeFormatOptions);

        return formatter.format(date);
    }

    /**
     * 
     */
    toLocaleDate(date, locale) {
        const formatter = new Intl.DateTimeFormat(locale, this.options.localDateFormatOptions);

        return formatter.format(date);
    }

    /**
     * 
     */
    toISOStringWithTimezone(date) {
        return date.getFullYear() + '-' + this.pad(date.getMonth() + 1) + '-' + this.pad(date.getDate()) + 'T' + this.pad(date.getHours()) + ':' + this.pad(date.getMinutes()) + ':' + this.pad(date.getSeconds()) + this.getTimezoneOffset(date);
    }

    /**
     * 
     */
    toStringWithoutTimezone(date) {
        return date.getFullYear() + ':' + this.pad(date.getMonth() + 1) + ':' + this.pad(date.getDate()) + ' ' + this.pad(date.getHours()) + ':' + this.pad(date.getMinutes()) + ':' + this.pad(date.getSeconds());
    }

    /**
     * 
     */
    getTimezoneOffset(date) {
        const offset = -date.getTimezoneOffset();
        const sign = offset >= 0 ? '+' : '-';

        return sign + this.pad(offset / 60) + ':' + this.pad(offset % 60);
    }

    /**
     * 
     */
    pad(n) {
        return new String(Math.floor(Math.abs(n))).padStart(2, '0');
    }
}