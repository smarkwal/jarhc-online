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

function storeTokens() {
	const tokens = getTokensFromURL()
	if (tokens) {
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
			method: 'GET',
			headers: {'Content-Type': 'text/plain'}
		};

		fetch("https://api.jarhc.org/auth/validate", requestOptions)
			.then(res => {
				if (res.ok) {
					res.data().then(data => {
						alert(res.statusCode + ": " + data)
					})
				} else {
					alert(res.statusCode + ": " + res.statusMessage)
				}
			})
	} else {
		alert(type + " not found")
	}
}
