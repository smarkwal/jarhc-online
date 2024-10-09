import React from 'react';

import ExternalLink from '../components/ExternalLink.jsx';

const JapiccAbout = () => {
	return (<div className="bg-light border rounded-3 p-3 mt-5">
		<h5 className="alert-heading">About Java API Compliance Checker</h5>
		<hr/>
		<pre>{`Java API Compliance Checker (JAPICC) 2.4
Copyright (C) 2018 Andrey Ponomarenko's ABI Laboratory
License: LGPLv2.1+ <http://www.gnu.org/licenses/>
This program is free software: you can redistribute it and/or modify it.

Written by Andrey Ponomarenko.`}</pre>
		<hr/>
		<ExternalLink className="alert-link" href="https://github.com/lvc/japi-compliance-checker">https://github.com/lvc/japi-compliance-checker</ExternalLink>
	</div>);
};

export default JapiccAbout;
