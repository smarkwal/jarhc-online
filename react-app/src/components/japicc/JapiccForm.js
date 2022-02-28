import React, {useState} from 'react';
import JapiccReport from "./JapiccReport";
import JapiccAbout from "./JapiccAbout";

const JapiccForm = () => {

	const [state, setState] = useState({
		oldVersion: "",
		oldVersionValid: null,
		newVersion: "",
		newVersionValid: null,
		loading: false,
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
		const response = await fetch(process.env.REACT_APP_API_URL + "/maven/search?coordinates=" + coordinates);
		if (!response.ok) {
			return null;
		}
		const json = await response.json()
		return json.length > 0
	}

	function onSubmit(event) {
		event.preventDefault()

		// show loading wheel
		setState({
			...state,
			loading: true,
			reportURL: "",
			errorMessage: ""
		})

		// prepare API request
		const requestOptions = {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify({
				oldVersion: state.oldVersion,
				newVersion: state.newVersion
			})
		};

		// run JAPICC check
		fetch(process.env.REACT_APP_API_URL + '/japicc/check', requestOptions)
			.then(response => response.json())
			.then(data => {

				// show report or error message
				setState({
					...state,
					loading: false,
					reportURL: data.reportURL,
					errorMessage: data.errorMessage
				})
			});
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
		return state.oldVersionValid && state.newVersionValid && !state.loading;
	}

	function getSubmitButtonClass() {
		return isSubmitButtonEnabled() ? "btn-primary" : "btn-secondary";
	}

	function doSubmitExample(oldVersion, newVersion) {
		setState({
			...state,
			oldVersion: oldVersion,
			oldVersionValid: true,
			newVersion: newVersion,
			newVersionValid: true
		})
	}

	function closeReport() {
		setState({
			...state,
			reportURL: ""
		})
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
				<li>
					<span role="button" onClick={() => doSubmitExample("org.springframework:spring-core:5.3.0", "org.springframework:spring-core:5.3.16")}>
						<code>org.springframework:spring-core:5.3.0</code> and <code>org.springframework:spring-core:5.3.16</code>
						<i className="bi bi-box-arrow-in-down-right text-primary ms-1"/>
					</span>
				</li>
				<li>
					<span role="button" onClick={() => doSubmitExample("commons-io:commons-io:2.10.0", "commons-io:commons-io:2.11.0")}>
						<code>commons-io:commons-io:2.10.0</code> and <code>commons-io:commons-io:2.11.0</code>
						<i className="bi bi-box-arrow-in-down-right text-primary ms-1"/>
					</span>
				</li>
				<li>
					<span role="button" onClick={() => doSubmitExample("org.owasp.esapi:esapi:2.2.2.0", "org.owasp.esapi:esapi:2.2.3.1")}>
						<code>org.owasp.esapi:esapi:2.2.2.0</code> and <code>org.owasp.esapi:esapi:2.2.3.1</code>
						<i className="bi bi-box-arrow-in-down-right text-primary ms-1"/>
					</span>
				</li>
				<li>
					<span role="button" onClick={() => doSubmitExample("javax.xml.bind:jaxb-api:2.3.0", "jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")}>
						<code>javax.xml.bind:jaxb-api:2.3.0</code> and <code>jakarta.xml.bind:jakarta.xml.bind-api:2.3.2</code>
						<i className="bi bi-box-arrow-in-down-right text-primary ms-1"/>
					</span>
				</li>
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
					<button type="submit" disabled={!isSubmitButtonEnabled()} className={`btn ${getSubmitButtonClass()} w-100`}>
						{state.loading && <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"/>}
						Check
					</button>
				</div>
			</div>
		</form>
		{state.errorMessage.length > 0 && <div className="border border-danger border-1 mt-5">
			<div className="alert alert-danger mb-0">
				{state.errorMessage}
			</div>
		</div>}
		{state.reportURL.length > 0 ? <JapiccReport reportURL={state.reportURL} onClose={closeReport}/> : <JapiccAbout/>}
	</div>)
}

export default JapiccForm;
