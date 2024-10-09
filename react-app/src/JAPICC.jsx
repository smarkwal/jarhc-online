import React from 'react';

import JapiccForm from './japicc/JapiccForm.jsx';
import Navigation from './components/Navigation.jsx';

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
