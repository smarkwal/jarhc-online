import React from 'react';
import {NavLink} from "react-router-dom";
import Auth from "./Auth";

const Navigation = () => {
	return (<nav className="navbar navbar-expand navbar-dark bg-dark mb-4" style={{fontSize: "1.5rem"}}>
		<div className="navbar-nav me-auto ps-4">
			<NavLink className="nav-item nav-link px-3" activeclassname="active" to="/">Home</NavLink>
			<NavLink className="nav-item nav-link px-3" activeclassname="active" to="/japicc">JAPICC</NavLink>
			<NavLink className="nav-item nav-link px-3" activeclassname="active" to="/jarhc">JarHC</NavLink>
			<NavLink className="nav-item nav-link px-3" activeclassname="active" to="/jardiff">JarDiff</NavLink>
			<NavLink className="nav-item nav-link px-3" activeclassname="active" to="/srcdiff">SrcDiff</NavLink>
		</div>
		<div className="pe-4">
			{Auth.isSignedIn() ? <button className="btn btn-primary" onClick={Auth.signOut}>Sign out</button> : <div>
				<button className="btn btn-primary me-3" onClick={Auth.signIn}>Sign in</button>
				<button className="btn btn-primary" onClick={Auth.signUp}>Sign up</button>
			</div>}
		</div>
	</nav>);
};

export default Navigation;
