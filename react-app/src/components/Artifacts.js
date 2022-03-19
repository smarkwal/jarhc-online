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

	get: function(version) {
		const artifacts = Artifacts.cache[version];
		console.log("Artifacts.get:", version, artifacts)
		return artifacts
	},

	put: function(version, artifacts) {
		console.log("Artifacts.put:", version, artifacts)
		Artifacts.cache[version] = artifacts
	},

	startSearch: function(version, callback) {
		console.log("Artifacts.startSearch:", version)

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
		fetch(url, requestOptions)
			.then(response => Artifacts.handleValidationResponse(response, callback))
			.catch(Artifacts.handleValidationError)
	},

	handleValidationResponse: function(response, callback) {
		console.log("Artifacts.handleValidationResponse:", response)

		if (response.ok) {
			response.json()
				.then(Artifacts.handleValidationResult)
				.then(callback)
				.catch(Artifacts.handleValidationError)
		} else {
			Artifacts.handleValidationError(response)
		}
	},

	handleValidationResult: function(result) {
		console.log("Artifacts.handleValidationResult:", result)

		// cache result
		Artifacts.put(result.coordinates, result.artifacts)
	},

	handleValidationError: function(error) {
		// TODO: error handling
		console.error(error)
	},

}

export default Artifacts
