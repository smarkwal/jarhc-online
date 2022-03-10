const Auth = {

	// TODO: set via process.env variables
	issuerURL: "https://login.jarhc.org",
	clientId: "7qa0i0lnqnii4rr6pbjh0etu1a",
	callbackBaseURL: window.location.origin,

	isSignedIn: function() {
		// console.log("is signed in ...")
		const tokens = Auth.getTokens()
		const count = Object.keys(tokens).length;
		// console.log("count =", count)
		return count
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
		return Auth.issuerURL + "/login?response_type=token&client_id=" + Auth.clientId + "&redirect_uri=" + Auth.callbackBaseURL + "/login.html";
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
