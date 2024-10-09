import React, {useReducer, useState} from 'react';

import ArtifactInput from '../components/ArtifactInput.jsx';
import Report from '../components/Report.jsx';
import JapiccAbout from './JapiccAbout.jsx';

import Auth from '../components/Auth.js';
import Artifacts from '../components/Artifacts.js';

const JapiccForm = () => {

	const [state, setState] = useState({
		oldVersion: '',
		newVersion: '',
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
				oldVersion: state.oldVersion,
				newVersion: state.newVersion
			})
		};

		// run JAPICC check
		const requestURL = import.meta.env.VITE_API_URL + '/japicc/submit';
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
		return Auth.isSignedIn() && Artifacts.isValid(state.oldVersion) && Artifacts.isValid(state.newVersion) && !state.loading;
	};

	const getSubmitButtonClass = function() {
		return isSubmitButtonEnabled() ? 'btn-primary' : 'btn-secondary';
	};

	const doSubmitExample = function(oldVersion, newVersion) {
		if (!Artifacts.isCached(oldVersion)) {
			Artifacts.searchAsync(oldVersion).then(forceUpdate);
		}
		if (!Artifacts.isCached(newVersion)) {
			Artifacts.searchAsync(newVersion).then(forceUpdate);
		}
		setState({
			...state,
			oldVersion: oldVersion,
			newVersion: newVersion,
		});
	};

	const setOldVersion = function(version) {
		setState({
			...state,
			oldVersion: version
		});
	};

	const setNewVersion = function(version) {
		setState({
			...state,
			newVersion: version
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
			Check API source and binary compatibility of two versions of a Java library.
		</div>
		<div className="mt-2">
			Enter the Maven artifact coordinates of two versions of <strong>the same Java library</strong> in the form <code>Group:Artifact:Version</code>.
		</div>
		<div>
			Examples:
			<ul>
				<Example oldVersion="org.springframework:spring-core:5.3.0" newVersion="org.springframework:spring-core:5.3.16" onClick={doSubmitExample}/>
				<Example oldVersion="commons-io:commons-io:2.10.0" newVersion="commons-io:commons-io:2.11.0" onClick={doSubmitExample}/>
				<Example oldVersion="org.owasp.esapi:esapi:2.2.2.0" newVersion="org.owasp.esapi:esapi:2.2.3.1" onClick={doSubmitExample}/>
				<Example oldVersion="javax.xml.bind:jaxb-api:2.3.0" newVersion="jakarta.xml.bind:jakarta.xml.bind-api:2.3.2" onClick={doSubmitExample}/>
			</ul>
		</div>
		<form onSubmit={onSubmit} className="mb-0">
			<div className="row align-items-md-top mb-3">
				<div className="col-12 col-md-5 mt-3">
					<label className="form-label">Old version</label>
					<ArtifactInput version={state.oldVersion} onUpdate={setOldVersion} onRefresh={forceUpdate}/>
				</div>
				<div className="col-12 col-md-5 mt-3">
					<label className="form-label">New version</label>
					<ArtifactInput version={state.newVersion} onUpdate={setNewVersion} onRefresh={forceUpdate}/>
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
		{state.reportURL && state.reportURL.length > 0 ? <Report title="Java API Compliance Checker Report" reportURL={state.reportURL} onClose={closeReport}/> : <JapiccAbout/>}
	</div>);
};

function Example({
					 oldVersion,
					 newVersion,
					 onClick
				 }) {
	return <li>
		<span role="button" onClick={() => onClick(oldVersion, newVersion)}>
			<code>{oldVersion}</code> and <code>{newVersion}</code>
			<i className="bi bi-box-arrow-in-down-right text-primary ms-1"/>
		</span>
	</li>;
}

export default JapiccForm;
