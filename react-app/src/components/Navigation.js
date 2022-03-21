import React from 'react';
import {NavLink} from "react-router-dom";
import Auth from "./Auth";

const Navigation = () => {
	return (<nav className="navbar navbar-expand-md navbar-dark bg-navbar mb-4" style={{fontSize: "1.5rem"}}>
		<button className="navbar-toggler ms-2" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
			<span className="navbar-toggler-icon"/>
		</button>
		<div className="collapse navbar-collapse" id="navbarSupportedContent">
			<div className="navbar-nav me-auto ps-1">
				<NavLink className="nav-item nav-link px-3" activeclassname="active" to="/">Home</NavLink>
				<NavLink className="nav-item nav-link px-3" activeclassname="active" to="/japicc">JAPICC</NavLink>
				<NavLink className="nav-item nav-link px-3 d-none" activeclassname="active" to="/jarhc">JarHC</NavLink>
				<NavLink className="nav-item nav-link px-3 d-none" activeclassname="active" to="/jardiff">JarDiff</NavLink>
				<NavLink className="nav-item nav-link px-3 d-none" activeclassname="active" to="/srcdiff">SrcDiff</NavLink>
			</div>
		</div>
		<div className="d-flex pe-4">
			{Auth.isSignedIn() ? <button className="btn btn-primary" onClick={Auth.signOut}>Sign out</button> : <div>
				<button className="btn btn-primary ms-3 me-3" onClick={Auth.signIn}>Sign in</button>
				<button className="btn btn-primary" onClick={Auth.signUp}>Sign up</button>
			</div>}
		</div>
	</nav>);
};

export default Navigation;
