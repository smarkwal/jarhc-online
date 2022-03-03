import React from 'react';
import {NavLink} from "react-router-dom";

const Navigation = () => {
	return (<nav className="navbar navbar-expand navbar-dark bg-dark mb-4" style={{fontSize: "1.5rem"}}>
		<div className="navbar-nav ps-4">
			<NavLink className="nav-item nav-link px-3" activeClassName="active" to="/">Home</NavLink>
			<NavLink className="nav-item nav-link px-3" activeClassName="active" to="/japicc">JAPICC</NavLink>
			<NavLink className="nav-item nav-link px-3" activeClassName="active" to="/jarhc">JarHC</NavLink>
			<NavLink className="nav-item nav-link px-3" activeClassName="active" to="/jdiff">jDiff</NavLink>
		</div>
	</nav>);
};

export default Navigation;
