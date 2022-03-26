const Auth = {

	// TODO: set via process.env variables
	issuerURL: "https://login.jarhc.org",
	clientId: "7qa0i0lnqnii4rr6pbjh0etu1a",
	callbackBaseURL: window.location.origin,

	isSignedIn: function() {

		// get ID token
		const token = Auth.getIdToken()
		if (token) {

			// try to decode token
			let payload = {}
			try {
				const base64Url = token.split('.')[1]
				const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
				const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
					return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
				}).join(''))
				payload = JSON.parse(jsonPayload)
				// console.log(payload)
			} catch (e) {
				console.log("Error decoding JWT token:", e)
				return false
			}

			// if token contains 'exp' claim
			if ("exp" in payload) {

				// compare expiration date with current date
				const exp = payload["exp"] * 1000
				// TODO: cache expiration date
				// console.log("ID token expires:", new Date(exp))
				const now = new Date().getTime()
				return exp > now
			}
		}

		return false
	},

	signUp: function() {
		Auth.openSignUpURL()
	},

	openSignUpURL: function() {
		window.location.href = Auth.getSignUpURL()
	},

	getSignUpURL: function() {
		return Auth.issuerURL + "/signup?response_type=token&client_id=" + Auth.clientId + "&redirect_uri=" + Auth.callbackBaseURL + "/login.html";
	},

	signIn: function() {
		Auth.openSignInURL()
	},

	openSignInURL: function() {
		window.location.href = Auth.getSignInURL()
	},

	getSignInURL: function() {
		return Auth.issuerURL + "/login?response_type=token&client_id=" + Auth.clientId + "&redirect_uri=" + Auth.callbackBaseURL + "/login.html" + Auth.getStateParam();
	},

	signOut: function() {
		Auth.clearTokens()
		Auth.openSignOutURL()
	},

	openSignOutURL: function() {
		window.location.href = Auth.getSignOutURL()
	},

	getSignOutURL: function() {
		return Auth.issuerURL + "/logout?client_id=" + Auth.clientId + "&logout_uri=" + Auth.callbackBaseURL + "/logout.html";
	},

	getStateParam: function() {
		const fragment = window.location.hash
		if (fragment) {
			return "&state=" + encodeURIComponent(fragment)
		}
		return "";
	},

	getAccessToken: function() {
		return Auth.getToken("access_token")
	},

	getIdToken: function() {
		return Auth.getToken("id_token")
	},

	getToken: function(type) {
		const tokens = Auth.getTokens()
		const token = tokens[type];
		// console.log(type, "=", token)
		return token
	},

	getTokens: function() {
		const storage = window.sessionStorage;
		const value = storage.getItem("tokens");
		if (value) {
			return JSON.parse(value);
		} else {
			return {}
		}
	},

	clearTokens: function() {
		// console.log("clear tokens ...")
		const storage = window.sessionStorage;
		storage.removeItem("tokens")
	}

}

export default Auth
