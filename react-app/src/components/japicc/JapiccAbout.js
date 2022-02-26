import React from 'react';
import JapiccVersion from "./JapiccVersion";

const JapiccAbout = () => {
	return (<div className="alert alert-secondary mt-5">
		<h5 className="alert-heading">About Java API Compliance Checker</h5>
		<hr/>
		<JapiccVersion/>
		<hr/>
		<a className="alert-link" href="https://github.com/lvc/japi-compliance-checker">https://github.com/lvc/japi-compliance-checker</a>
	</div>);
};

export default JapiccAbout;
