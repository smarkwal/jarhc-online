import React from 'react';
import {Link} from "react-router-dom";

const Card = ({
				  children,
				  imageUrl,
				  title,
				  linkUrl,
				  linkText,
				  enabled = true
			  }) => {

	function scrollToTop() {
		window.scrollTo(0, 0);
	}

	return (<div className="card border-3 h-100">
		{imageUrl && <img className="card-img-top" src={imageUrl} alt=""/>}
		<div className="card-body">
			{title && <h4 className="card-title">{title}</h4>}
			<p className="card-text">{children}</p>
			{linkText && <div className="text-end">
				{enabled ? <Link to={linkUrl}>
					<button className="btn btn-primary" onClick={scrollToTop}>{linkText}</button>
				</Link> : <button className="btn btn-outline-primary disabled">{linkText}</button>}
			</div>}
		</div>
	</div>);
};

export default Card;
