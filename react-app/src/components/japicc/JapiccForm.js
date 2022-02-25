import React, {useState} from 'react';
import JapiccVersion from "./JapiccVersion";

const JapiccForm = () => {

	const [state, setState] = useState({
		oldVersion: "",
		oldVersionValid: null,
		newVersion: "",
		newVersionValid: null,
		reportURL: "",
		errorMessage: ""
	});

	async function onChangeVersion(event) {
		const name = event.target.name;
		const coordinates = event.target.value.trim()
		const newState = {...state}
		newState[name] = coordinates
		newState[name + "Valid"] = await validateCoordinates(coordinates);
		setState(newState)
	}

	async function validateCoordinates(coordinates) {
		if (coordinates === undefined || coordinates.length === 0) {
			return null
		} else if (!coordinates.match("[^:]+:[^:]+:[^:]+")) {
			return false
		}
		const response = await fetch("http://localhost:8080/maven/search?coordinates=" + coordinates);
		if (!response.ok) {
			return null;
		}
		const json = await response.json()
		return json.length > 0
	}

	function onSubmit(event) {
		event.preventDefault()

		// TODO: submit to server
		console.log(`Check: ${state.oldVersion} vs ${state.newVersion}`)

		setState({
			...state,
			errorMessage: "Artifact not found: " + state.oldVersion,
			reportURL: "http://localhost:8080/japicc/version"
		})
	}

	function getInputFieldClass(name) {
		const value = state[name];
		if (value !== undefined && value.length > 0) {
			if (value.match("[^:]+:[^:]+:[^:]+")) {
				const valid = state[name + "Valid"]
				if (valid === true) {
					return "is-valid"
				} else if (valid === false) {
					return "is-invalid"
				} else {
					return ""
				}
			} else {
				return "is-invalid"
			}
		}
		return ""
	}

	function isSubmitButtonEnabled() {
		return state.oldVersionValid && state.newVersionValid;
	}

	function getSubmitButtonClass() {
		return isSubmitButtonEnabled() ? "btn-primary" : "btn-secondary";
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
					<label className="form-label">Old version</label>
					<input type="text" className={`form-control ${getInputFieldClass('oldVersion')}`} name="oldVersion" value={state.oldVersion} onChange={onChangeVersion} placeholder="Group:Artifact:Version"/>
				</div>
				<div className="col-12 col-md-5 mt-3">
					<label className="form-label">New version</label>
					<input type="text" className={`form-control ${getInputFieldClass('newVersion')}`} name="newVersion" value={state.newVersion} onChange={onChangeVersion} placeholder="Group:Artifact:Version"/>
				</div>
				<div className="col-12 col-md-2 mt-3">
					<button type="submit" disabled={!isSubmitButtonEnabled()} className={`btn ${getSubmitButtonClass()} w-100`}>Check</button>
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
			<iframe src={state.reportURL} className="w-100 mt-0" style={{height: "500px"}}/>
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
