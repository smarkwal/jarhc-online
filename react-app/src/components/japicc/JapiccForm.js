import React, {useState} from 'react';
import JapiccVersion from "./JapiccVersion";

const JapiccForm = () => {

	const [state, setState] = useState({
		oldVersion: "",
		newVersion: "",
		reportURL: "",
		errorMessage: ""
	});

	function onChangeVersion(event) {
		const name = event.target.name;
		const value = event.target.value
		const newState = {...state}
		newState[name] = value
		setState(newState)
	}

	function onSubmit(event) {
		event.preventDefault()
		console.log(`Check: ${state.oldVersion} vs ${state.newVersion}`)
		// TODO: submit to server

		setState({
			...state,
			errorMessage: "Artifact not found: " + state.oldVersion,
			reportURL: "http://localhost:8080/japicc/version"
		})
	}

	function validate(name) {
		const value = state[name];
		if (value !== undefined && value.length > 0) {
			if (value.match("[^:]+:[^:]+:[^:]+")) {
				return "is-valid"
			} else {
				return "is-invalid"
			}
		}
		return ""
	}

	return (<div>
		<h2>Java API Compliance Checker</h2>
		<div>
			Check API source and binary compatibility of two versions of a Java library.
		</div>
		<div className="mt-2">
			Enter the Maven artifact coordinates of two versions of <strong>the same Java library</strong> in the form <code>Group:Artifact:Version</code>.
		</div>
		<div>
			Examples:
			<ul>
				<li><code>org.springframework:spring-core:4.3.30.RELEASE</code> and <code>org.springframework:spring-core:5.3.16</code></li>
				<li><code>commons-io:commons-io:2.7</code> and <code>commons-io:commons-io:2.11.0</code></li>
				<li><code>org.owasp.esapi:esapi:2.1.0.1</code> and <code>org.owasp.esapi:esapi:2.2.3.1</code></li>
			</ul>
		</div>
		<form onSubmit={onSubmit}>
			<div className="row align-items-end mb-3">
				<div className="col-12 col-md-5 mt-3">
					<label className="form-label">Old Version</label>
					<input type="text" className={`form-control ${validate('oldVersion')}`} name="oldVersion" value={state.oldVersion} onChange={onChangeVersion} placeholder="Group:Artifact:Version"/>
				</div>
				<div className="col-12 col-md-5 mt-3">
					<label className="form-label">New Version</label>
					<input type="text" className={`form-control ${validate('newVersion')}`} name="newVersion" value={state.newVersion} onChange={onChangeVersion} placeholder="Group:Artifact:Version"/>
				</div>
				<div className="col-12 col-md-2 mt-3">
					<button type="submit" className="btn btn-primary w-100">Check</button>
				</div>
			</div>
		</form>
		{state.errorMessage.length > 0 && <div className="border border-danger border-1 mt-5">
			<div className="alert alert-danger mb-0">
				{state.errorMessage}
			</div>
		</div>}
		{state.reportURL.length > 0 && <div className="border border-success border-1 mt-5">
			<div className="alert alert-success mb-0">
				Java API Compliance Checker report is ready:
				<span className="align-vertical-middle float-end">
				Download: <a href={state.reportURL} target="_blank" title="Download">
					<i className="bi bi-cloud-download-fill text-success"/>
				</a>
				</span>
			</div>
			<iframe src={state.reportURL} className=" w-100 mt-0" style={{height: "500px"}}/>
		</div>}
		<div className="alert alert-secondary mt-5">
			<h5 className="alert-heading">About Java API Compliance Checker</h5>
			<hr/>
			<JapiccVersion/>
			<hr/>
			<a className="alert-link" href="https://github.com/lvc/japi-compliance-checker">https://github.com/lvc/japi-compliance-checker</a>
		</div>
	</div>)
}

export default JapiccForm;
