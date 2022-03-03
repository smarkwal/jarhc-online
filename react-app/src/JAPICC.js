import JapiccForm from "./components/japicc/JapiccForm";
import Navigation from "./components/Navigation";
import React from "react";

function JAPICC() {
	return (<>
		<Navigation/>
		<div className="container">
			<h2>JAPICC - Java API Compliance Checker</h2>
			<JapiccForm/>
		</div>
	</>);
}

export default JAPICC;
