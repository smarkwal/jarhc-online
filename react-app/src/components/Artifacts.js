const Artifacts = {

	cache: {},

	isCached: function(version) {
		return Artifacts.cache[version] !== undefined
	},

	isValid: function(version) {
		const artifacts = Artifacts.cache[version];
		if (artifacts !== undefined) {
			return artifacts.length > 0
		} else {
			return null
		}
	},

	getFromCache: function(version) {
		const artifacts = Artifacts.cache[version];
		console.log("Artifacts.getFromCache:", version, artifacts)
		return artifacts
	},

	putInCache: function(version, artifacts) {
		console.log("Artifacts.putInCache:", version, artifacts)
		Artifacts.cache[version] = artifacts
	},

	searchAsync: function(version) { // TODO: return promise instead of taking callback parameter
		console.log("Artifacts.searchAsync:", version)

		// use REST API to validate version
		const requestOptions = {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				coordinates: version
			})
		}
		const url = process.env.REACT_APP_API_URL + "/maven/search"
		return fetch(url, requestOptions)
			.then(Artifacts.handleResponse)
			.catch(Artifacts.handleError)
	},

	handleResponse: function(response) {
		console.log("Artifacts.handleResponse:", response)

		if (response.ok) {
			return response.json().then(Artifacts.handleResult)
		} else {
			Artifacts.handleError(response)
		}
	},

	handleResult: function(result) {
		console.log("Artifacts.handleResult:", result)

		// cache result
		Artifacts.putInCache(result.coordinates, result.artifacts)
	},

	handleError: function(error) {
		console.error("Artifacts.handleError:", error)
	},

}

export default Artifacts
