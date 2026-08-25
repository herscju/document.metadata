/**
 * 
 */
export class GeoLocationMap {

	static GOOGLE = "Google"
	static OSM = "OpenStreetMap"

	options;
	type;
	key;
	version;
	point;
	zoom;
	width;
	height;
	language;

	map;

	/**
	 * Constructor
	 */
	constructor(options) {
		this.options = options || { "type": GeoLocationMap.OSM, "key": null, "version": "", "point": new GeoPoint("0N", "0W"), "zoom": 10, "width": "200px", "height": "200px", "language": "en" }

		this.type = (this.options.type) ? this.options.type : options.type;
		this.key = (this.options.key) ? this.options.key : options.key;
		this.version = (this.options.version) ? this.options.version : options.version;
		this.point = (this.options.point) ? this.options.point : options.point;
		this.zoom = (this.options.zoom) ? this.options.zoom : options.zoom;
		this.width = (this.options.width) ? this.options.width : options.width;
		this.height = (this.options.height) ? this.options.height : options.height;
		this.language = (this.options.language) ? this.options.language : options.language;
	}

	/**
	 * 
	 */
	init() {
		var id = this.type + "Script";
		var styles = [];
		var scripts = [];

		if (!document.getElementById(id)) {
			switch (this.type) {
				case GeoLocationMap.GOOGLE:
					scripts.push("https://maps.googleapis.com/maps/api/js?key=" + this.key + "&libraries=maps,marker&language=" + this.language + "&v=" + this.version);
					scripts.forEach(src => {
						const node = document.createElement("script");
						node.id = id;
						node.src = src;
						node.async = false;
						node.defer = true;
						node.charset = "utf-8";
						node.onload = console.log("Load externally hosted JavaScript file dynamically from URL '" + src + "'.");
						document.body.append(node);
					});
					break;

				case GeoLocationMap.OSM:
				default:
					//					styles.push("https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.min.css");
					styles.forEach(href => {
						const node = document.createElement("link");
						node.id = this.type + "Style";
						node.href = href;
						node.rel = "stylesheet";
						node.onload = console.log("Load externally hosted CSS file dynamically from URL '" + href + "'.");
						document.head.append(node);
					});
					//					scripts.push("https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.min.js");
					scripts.forEach(src => {
						const node = document.createElement("script");
						node.id = id;
						node.src = src;
						node.async = false;
						node.defer = true;
						node.charset = "utf-8";
						node.onload = console.log("Load externally hosted JavaScript file dynamically from URL '" + src + "'.");
						document.body.append(node);
					});

					break;
			}
		}

		return this;
	}

	/**
	 * 
	 */
	appendMap(parentTag, uuid, title) {

		const id = "geo-map-" + uuid;

		switch (this.type) {
			case GeoLocationMap.GOOGLE:
				const geoLayer = document.createElement("gmp-map");
				geoLayer.setAttribute("center", this.point.latDec + "," + this.point.lonDec);
				geoLayer.setAttribute("zoom", this.zoom);
				geoLayer.setAttribute("map-id", id);
				geoLayer.setAttribute("style", "height:" + this.height);

				const geoMarker = document.createElement("gmp-advanced-marker");
				geoMarker.setAttribute("position", this.point.latDec + "," + this.point.lonDec);
				geoMarker.setAttribute("title", title);
				geoLayer.appendChild(geoMarker);
				parentTag.appendChild(geoLayer);

				this.map = null;
				break;

			case GeoLocationMap.OSM:
			default:
				const geoChart = document.createElement("div");
				geoChart.setAttribute("id", id);
				geoChart.setAttribute("class", "osm-chart");
				geoChart.setAttribute("style", "width:" + this.width + "; height:" + this.height);
				parentTag.appendChild(geoChart);
				console.log("Set center and marker to '" + this.point.toString() + "'.");

				const target = new L.LatLng(this.point.latDec, this.point.lonDec);
				const mapTiles = new L.TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", { "maxZoom": this.zoom + 4, "attribution": '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>' });
				const hotTiles = new L.TileLayer("https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png", { "maxZoom": this.zoom + 4, "attribution": '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Tiles style by Humanitarian OpenStreetMap Team hosted by OpenStreetMap France' });
				const satTiles = new L.TileLayer("https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}", { "attribution": 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community' });

				this.map = new L.Map(geoChart, { "center": target, "zoom": this.zoom, "layers": [ mapTiles ] });

				const marker = new L.Marker(target, { "icon": new L.Icon.Default() });
				marker.addTo(this.map);

				// See 'https://stackoverflow.com/a/78370992/6487382' and 'https://jonnymccullagh.github.io/leaflet-examples/map-satellite.html'
				const layers = L.control.layers({ "OSM Map": mapTiles, "OSM.HOT": hotTiles, "Satellite": satTiles }, { "Marker": marker }, { "position": "topright" });
				layers.addTo(this.map);

				break;
		}

		return this;
	}

	/**
	 * 
	 */
	finishMapLoading() {

		return this;
	}

	/**
	 * See 'https://stackoverflow.com/a/43068409/6487382' and 'https://stackoverflow.com/a/1818513/6487382'
	 */
	updateMap() {
		window.setTimeout(() => {
			switch (this.type) {
				case GeoLocationMap.GOOGLE:
					break;

				case GeoLocationMap.OSM:
				default:
					window.dispatchEvent(new Event("resize"));

					this.map.invalidateSize();
					this.map.fitBounds(this.map.getBounds(), { "maxZoom": this.zoom });
					break;

			}
		}, 100);
	}

	/**
	 * 
	 */
	async showMap() {
		switch (this.type) {
			case GeoLocationMap.GOOGLE:
				console.log("Google Maps JavaScript API loaded.");
				break;

			case GeoLocationMap.OSM:
			default:
				console.log("OpenStreetMap (OSM) JavaScript API loaded.");
				break;
		}
	}


}

/**
 * 
 */
export class GeoPoint {

	static CHAR_SEP = "\u0020";
	static CHAR_DEG = "\u00B0";
	static CHAR_MIN = "\u0027";
	static CHAR_SEC = "\u0022";

	static MAX_LON = 180;
	static MAX_LAT = 90;

	lonDeg = 0;
	lonDec = 0;
	latDeg = 0;
	latDec = 0;

	/**
	 * Constructor
	 */
	constructor(longitude, latitude) {

		switch (typeof (longitude)) {
			case 'number':
				this.lonDeg = this.dec2deg(longitude, GeoPoint.MAX_LON);
				this.lonDec = longitude;
				break;

			case 'string':
				if (this.decode(longitude)) {
					this.lonDeg = longitude;
				}

				this.lonDec = this.deg2dec(longitude, GeoPoint.MAX_LON);
				break;
		}

		switch (typeof (latitude)) {

			case 'number':
				this.latDeg = this.dec2deg(latitude, GeoPoint.MAX_LAT);
				this.latDec = latitude;
				break;

			case 'string':
				if (this.decode(latitude)) {
					this.latDeg = latitude;
				}

				this.latDec = this.deg2dec(latitude, GeoPoint.MAX_LAT);
				break;

		}
	}

	/**
	 * 
	 */
	dec2deg(value, max) {
		var sign = value < 0 ? -1 : 1;
		var abs = Math.abs(Math.round(value * 1000000));

		if (abs > (max * 1000000)) {
			return NaN;
		}

		var dec = abs % 1000000 / 1000000;
		var deg = Math.floor(abs / 1000000) * sign;
		var min = Math.floor(dec * 60);
		var sec = (dec - min / 60) * 3600;
		var result = "";

		result += deg;
		result += GeoPoint.CHAR_DEG;
		result += GeoPoint.CHAR_SEP;
		result += min;
		result += GeoPoint.CHAR_MIN;
		result += GeoPoint.CHAR_SEP;
		result += sec.toFixed(2);
		result += GeoPoint.CHAR_SEC;

		return result;
	}

	/**
	 * 
	 */
	deg2dec(value) {
		var matches = this.decode(value);

		if (!matches) {
			return NaN;
		}

		var deg = parseFloat(matches[1]);
		var min = parseFloat(matches[2]);
		var sec = parseFloat(matches[3]);

		if (isNaN(deg) || isNaN(min) || isNaN(sec)) {
			return NaN;
		}

		return deg + (min / 60.0) + (sec / 3600);
	}

	/**
	 * 
	 */
	decode(value) {
		var pattern = "";

		// Degree
		pattern += "(-?\\d+)";
		pattern += GeoPoint.CHAR_DEG;
		pattern += "\\s*";

		// Minunte
		pattern += "(\\d+)";
		pattern += GeoPoint.CHAR_MIN;
		pattern += "\\s*";

		// Second
		pattern += "(\\d+(?:\\.\\d+)?)";
		pattern += GeoPoint.CHAR_SEC;

		return value.match(new RegExp(pattern));
	}

	/**
	 * 
	 */
	getLonDec() {
		return this.lonDec;
	}

	/**
	 * 
	 */
	getLatDec() {
		return this.latDec;
	}

	/**
	 * 
	 */
	getLonDeg() {
		return this.lonDeg;
	}

	/**
	 * 
	 */
	getLatDeg() {
		return this.latDeg;
	}

	/**
	 * 
	 */
	toString() {
		return "Lat / Lon = [" + this.latDec + ", " + this.lonDec + "]";
	}
}