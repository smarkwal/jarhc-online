import React, {useEffect, useState} from 'react';

const JapiccReport = ({
						  reportURL,
						  onClose
					  }) => {

	const [state, setState] = useState({
		polling: true,
		timeout: false
	});

	let pollReportTimeout
	let pollReportDelay = 0
	const pollReportDelayMax = 10

	useEffect(() => {
		if (pollReportTimeout) {
			window.clearTimeout(pollReportTimeout)
		}
		if (state.polling && !state.timeout) {
			pollReportStart()
		}
	});

	function pollReportStart() {
		pollReportDelay += 1 // increase delay by 1 second for every iteration
		pollReportTimeout = window.setTimeout(pollReport, pollReportDelay * 1000)
	}

	function pollReport() {

		const fetchOptions = {
			method: "HEAD"
		}

		fetch(reportURL, fetchOptions)
			.then(res => {
				if (res.ok) {
					// report found -> stop polling and show report
					setState({
						...state,
						polling: false
					})
				} else {
					// TODO: check if status code is 404
					// report does not exist
					if (pollReportDelay < pollReportDelayMax) {
						// schedule another polling request
						pollReportStart()
					} else {
						// show timeout message and retry button
						setState({
							...state,
							timeout: true
						})
					}
				}
			})
			.catch(err => {
				console.error(err)
			})

	}

	function pollRequestRetry() {
		// restart polling with 5 seconds delay
		pollReportDelay = 5
		// clear timeout state
		// -> this trigger useEffect and will restart polling
		setState({
			...state,
			timeout: false
		})
	}

	return (<div className="border border-success border-1 mt-5">
		<div className="alert alert-success mb-0">
			Java API Compliance Checker Report
			{!state.polling && <span className="align-vertical-middle float-end">
				<a href={reportURL} target="_blank" rel="noreferrer" title="Open">
					<i className="bi bi-box-arrow-up-right text-success"/>
				</a>
				<span className="ms-3" title="Close" onClick={onClose} role="button">
					<i className="bi bi-x-lg text-success"/>
				</span>
			</span>}
		</div>
		{state.polling ? <WaitMessage timeout={state.timeout} onRetry={pollRequestRetry}/> : <ReportFrame src={reportURL}/>}
	</div>);
};

function WaitMessage(props) {
	return <div className="p-3">
		{props.timeout ? <>
			<span className="me-3">There has been a timeout.</span>
			<button className="btn btn-primary btn-sm" onClick={props.onRetry}>Retry</button>
		</> : <>
			<span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"/>
			Waiting for report ...<br/>
			<div className="ms-4">This may take up to a minute.</div>
		</>}
	</div>;
}

function ReportFrame(props) {
	return <iframe src={props.src} className="w-100 mt-0" style={{height: "500px"}} title="JAPICC Report"/>;
}

export default JapiccReport;
