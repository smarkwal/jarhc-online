const issuerURL = "https://login.jarhc.org";
const clientId = "7qa0i0lnqnii4rr6pbjh0etu1a"
const callbackBaseURL = window.location.origin

function isSignedIn() {
	const tokens = getTokens()
	return tokens.length > 0
}

function getSignUpURL() {
	return issuerURL + "/signup?response_type=token&client_id=" + clientId + "&redirect_uri=" + callbackBaseURL + "/login.html";
}

function openSignUpURL() {
	window.location.href = getSignUpURL()
}

function getSignInURL() {
	return issuerURL + "/login?response_type=token&client_id=" + clientId + "&redirect_uri=" + callbackBaseURL + "/login.html";
}

function openSignInURL() {
	window.location.href = getSignInURL()
}

function getSignOutURL() {
	return issuerURL + "/logout?client_id=" + clientId + "&logout_uri=" + callbackBaseURL + "/logout.html";
}

function openSignOutURL() {
	window.location.href = getSignOutURL()
}

function getTokensFromURL() {
	const tokens = {}
	const fragment = window.location.hash
	if (fragment) {
		const params = fragment.substring(1) // remove leading hashtag
		const pairs = params.split("&")
		for (const pair of pairs) {
			if (pair.startsWith("id_token=")) {
				tokens["id_token"] = pair.substring(9)
			} else if (pair.startsWith("access_token=")) {
				tokens["access_token"] = pair.substring(13)
			}
		}
	}
	return tokens
}

function removeTokensFromURL() {
	// remove fragment as much as it can go without adding an entry in browser history
	window.location.replace("#");

	// slice off the remaining "#" in HTML5
	if (typeof window.history.replaceState == "function") {
		history.replaceState({}, "", window.location.href.slice(0, -1));
	}
}

function storeTokens() {
	const tokens = getTokensFromURL()
	if (tokens) {
		removeTokensFromURL()
		const storage = window.sessionStorage;
		let value = JSON.stringify(tokens);
		storage.setItem("tokens", value)
	}
}

function getTokens() {
	const storage = window.sessionStorage;
	const value = storage.getItem("tokens");
	if (value) {
		return JSON.parse(value);
	} else {
		return {}
	}
}

function getToken(type) {
	const tokens = getTokens()
	return tokens[type]
}

function getAccessToken() {
	return getToken("access_token")
}

function getIdToken() {
	return getToken("id_token")
}

function clearTokens() {
	const storage = window.sessionStorage;
	storage.removeItem("tokens")
}

function validateToken(type) {
	const token = getToken(type)
	if (token) {

		// prepare API request
		const requestOptions = {
			method: "GET",
			withCredentials: true,
			credentials: "include",
			headers: {
				"Authorization": "Bearer " + token
			}
		};

		fetch("https://api.jarhc.org/auth/validate", requestOptions)
			.then(res => {
				const status = res.status
				if (res.ok) {
					res.text().then(data => {
						alert(status + ": " + data)
					})
				} else {
					alert(status + ": " + res.statusText)
				}
			})
			.catch(error => {
				alert(error)
			})

	} else {
		alert(type + " not found")
	}
}

function searchInMaven(coordinates) {

	fetch("https://api.jarhc.org/maven/search?coordinates=" + coordinates)
		.then(res => {
			const status = res.status
			if (res.ok) {
				res.json().then(data => {
					alert(status + ": " + JSON.stringify(data))
				})
			} else {
				alert(status + ": " + res.statusText)
			}
		})

}
