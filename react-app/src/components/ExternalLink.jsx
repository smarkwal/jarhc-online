import React from 'react';

const ExternalLink = ({
						  children,
						  href
					  }) => {
	return (<a href={href} target="_blank" rel="noreferrer">
		{children} <i className="bi bi-box-arrow-up-right"/>
	</a>);
};

export default ExternalLink;
