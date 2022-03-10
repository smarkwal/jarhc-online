import React, {useEffect, useState} from 'react';

const JapiccReport = ({
						  reportURL,
						  onClose
					  }) => {

	const [state, setState] = useState({
		waiting: true
	});

	let pollReportTimeout
	let pollReportDelay = 0

	useEffect(() => {
		if (pollReportTimeout) {
			window.clearTimeout(pollReportTimeout)
		}
		if (state.waiting) {
			pollReportStart()
		}
	});

	function pollReportStart() {
		pollReportDelay += 1000 // increase delay by 1 second for every iteration
		pollReportTimeout = window.setTimeout(pollReport, pollReportDelay)
	}

	function pollReport() {

		const fetchOptions = {
			method: "GET"
		}

		fetch(reportURL, fetchOptions)
			.then(res => {
				if (res.ok) {
					// report found -> stop polling and show report
					setState({
						...state,
						waiting: false
					})
				} else {
					// TODO: check if status code is 404
					// report does not exist
					// -> schedule another polling request
					pollReportStart()
				}
			})
			.catch(err => {
				console.error(err)
			})

	}

	return (<div className="border border-success border-1 mt-5">
		<div className="alert alert-success mb-0">
			Java API Compliance Checker Report
			{!state.waiting && <span className="align-vertical-middle float-end">
				<a href={reportURL} target="_blank" rel="noreferrer" title="Open">
					<i className="bi bi-box-arrow-up-right text-success"/>
				</a>
				<span className="ms-3" title="Close" onClick={onClose} role="button">
					<i className="bi bi-x-lg text-success"/>
				</span>
			</span>}
		</div>
		{state.waiting ? <WaitMessage/> : <ReportFrame src={reportURL}/>}
	</div>);
};

function WaitMessage() {
	return <div className="p-3">
		<span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"/>
		Waiting for report ...<br/>
		<div className="ms-4">This may take up to a minute.</div>
	</div>;
}

function ReportFrame(props) {
	return <iframe src={props.src} className="w-100 mt-0" style={{height: "500px"}} title="JAPICC Report"/>;
}

export default JapiccReport;
