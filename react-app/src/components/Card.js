import React from 'react';
import {Link} from "react-router-dom";

const Card = ({
				  imageUrl,
				  title,
				  text,
				  linkUrl,
				  linkText,
				  enabled = true
			  }) => {

	function scrollToTop() {
		window.scrollTo(0, 0);
	}

	return (<div className="card">
		<img className="card-img-top" src={imageUrl} alt=""/>
		<div className="card-body">
			<h5 className="card-title">{title}</h5>
			<p className="card-text">{text}</p>
			<div className="text-end">
				{enabled ? <Link to={linkUrl}>
					<button className="btn btn-primary" onClick={scrollToTop}>{linkText}</button>
				</Link> : <button className="btn btn-outline-primary disabled">{linkText}</button>}
			</div>
		</div>
	</div>);
};

export default Card;
