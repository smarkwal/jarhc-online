import React from 'react';

const JapiccReport = ({
						  reportURL,
						  onClose
					  }) => {
	return (<div className="border border-success border-1 mt-5">
		<div className="alert alert-success mb-0">
			Java API Compliance Checker Report
			<span className="align-vertical-middle float-end">
				<a href={reportURL} target="_blank" rel="noreferrer" title="Open">
					<i className="bi bi-box-arrow-up-right text-success"/>
				</a>
				<a href="#" className="ms-3" title="Close" onClick={onClose}>
					<i className="bi bi-x-lg text-success"/>
				</a>
				</span>
		</div>
		<iframe src={reportURL} className="w-100 mt-0" style={{height: "500px"}} title="JAPICC Report"/>
	</div>);
};

export default JapiccReport;
