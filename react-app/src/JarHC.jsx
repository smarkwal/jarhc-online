import React, {useReducer, useState} from 'react';

import Navigation from './components/Navigation.jsx';
import ArtifactInput from './components/ArtifactInput.jsx';
import Report from './components/Report.jsx';

import Auth from './components/Auth.js';
import Artifacts from './components/Artifacts.js';

function JarHC() {
	return (<>
		<Navigation/>
		<div className="container">
			<h2>JarHC - JAR Health Check</h2>
			<JarHCForm/>
		</div>
	</>);
}

const JarHCForm = () => {

	const [state, setState] = useState({
		version: '',
		loading: false,
		reportURL: '',
		errorMessage: ''
	});

	// create a helper hook to force a rerender
	const [, forceUpdate] = useReducer(x => x + 1, 0, (x) => x);

	const onSubmit = function(event) {
		event.preventDefault();

		// get ID token
		const token = Auth.getIdToken();
		if (!token) {
			// TODO: error handling
			console.error('ID token not found. Please sign in first.');
			return;
		}

		// show loading wheel
		setState({
			...state,
			loading: true,
			reportURL: '',
			errorMessage: ''
		});

		// prepare API request
		const requestOptions = {
			method: 'POST',
			credentials: 'include',
			headers: {
				'Content-Type': 'application/json',
				'Authorization': 'Bearer ' + token
			},
			body: JSON.stringify({
				classpath: [state.version]
			})
		};

		// run JAPICC check
		const requestURL = import.meta.env.VITE_API_URL + '/jarhc/submit';
		fetch(requestURL, requestOptions)
			.then(response => response.json())
			.then(data => {

				let reportURL = data.reportURL;
				if (reportURL) {
					// add timestamp to prevent loading report from cache
					reportURL += '?timestamp=' + Date.now();
				}

				// show report or error message
				setState({
					...state,
					loading: false,
					reportURL: reportURL,
					errorMessage: data.errorMessage
				});
			})
			.catch(error => {
				console.error('API error:', error);

				// show error message
				setState({
					...state,
					loading: false,
					errorMessage: '' + error
				});
			});
	};

	const isSubmitButtonEnabled = function() {
		return Auth.isSignedIn() && Artifacts.isValid(state.version) && !state.loading;
	};

	const getSubmitButtonClass = function() {
		return isSubmitButtonEnabled() ? 'btn-primary' : 'btn-secondary';
	};

	const doSubmitExample = function(version) {
		if (!Artifacts.isCached(version)) {
			Artifacts.searchAsync(version).then(forceUpdate);
		}
		setState({
			...state,
			version: version,
		});
	};

	const setVersion = function(version) {
		setState({
			...state,
			version: version
		});
	};

	const closeReport = function() {
		setState({
			...state,
			reportURL: ''
		});
	};

	return (<div className="mb-4">
		<div>
			Analyze a set of Java libraries for compatibility at binary level. Find missing dependencies, duplicate classes, dangerous code, and much more.
		</div>
		<div className="mt-2">
			Enter the Maven artifact coordinates of a Java library in the form <code>Group:Artifact:Version</code>.
		</div>
		<div>
			Examples:
			<ul>
				<Example version="org.springframework:spring-core:5.3.16" onClick={doSubmitExample}/>
				<Example version="commons-io:commons-io:2.11.0" onClick={doSubmitExample}/>
				<Example version="org.owasp.esapi:esapi:2.2.3.1" onClick={doSubmitExample}/>
				<Example version="jakarta.xml.bind:jakarta.xml.bind-api:2.3.2" onClick={doSubmitExample}/>
			</ul>
		</div>
		<form onSubmit={onSubmit} className="mb-0">
			<div className="row align-items-md-top mb-3">
				<div className="col-12 col-md-5 mt-3">
					<label className="form-label">Library</label>
					<ArtifactInput version={state.version} onUpdate={setVersion} onRefresh={forceUpdate}/>
				</div>
				<div className="col-12 col-md-2 mt-3">
					<label className="form-label d-none d-md-block">&nbsp;</label>
					<button type="submit" disabled={!isSubmitButtonEnabled()} className={`btn ${getSubmitButtonClass()} w-100`}>
						{state.loading && <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"/>}
						Check
					</button>
					{!Auth.isSignedIn() && <div className="text-secondary text-center">Please sign in first</div>}
				</div>
			</div>
		</form>
		{state.errorMessage && state.errorMessage.length > 0 && <div className="alert alert-danger">
			{state.errorMessage}
		</div>}
		{state.reportURL && state.reportURL.length > 0 && <Report title="JAR Health Check Report" reportURL={state.reportURL} onClose={closeReport}/>}
	</div>);
};

function Example({
					 version,
					 onClick
				 }) {
	return <li>
		<span role="button" onClick={() => onClick(version)}>
			<code>{version}</code>
			<i className="bi bi-box-arrow-in-down-right text-primary ms-1"/>
		</span>
	</li>;
}

export default JarHC;
