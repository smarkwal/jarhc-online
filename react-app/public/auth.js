function storeTokens() {
	console.log("store tokens ...")
	const tokens = getTokensFromURL()
	if (tokens) {
		removeTokensFromURL()
		const storage = window.sessionStorage;
		let value = JSON.stringify(tokens);
		storage.setItem("tokens", value)
	}
}

function getTokensFromURL() {
	console.log("get tokens from URL ...")
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
	console.log("tokens =", tokens)
	return tokens
}

function removeTokensFromURL() {
	console.log("remove tokens from URL ...")

	// remove fragment as much as it can go without adding an entry in browser history
	window.location.replace("#");

	// slice off the remaining "#" in HTML5
	if (typeof window.history.replaceState == "function") {
		history.replaceState({}, "", window.location.href.slice(0, -1));
	}

	console.log("new URL =", window.location.href)
}

function clearTokens() {
	console.log("clear tokens ...")
	const storage = window.sessionStorage;
	storage.removeItem("tokens")
}

function redirectToMainApp() {
	console.log("redirect to main app ...")
	window.location.href = "/"
}
