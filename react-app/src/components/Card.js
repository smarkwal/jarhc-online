import React from 'react';
import {Link} from "react-router-dom";

const Card = ({
				  imageUrl,
				  title,
				  text,
				  linkUrl,
				  linkText
			  }) => {
	return (<div className="card">
			<img className="card-img-top" src={imageUrl} alt=""/>
			<div className="card-body">
				<h5 className="card-title">{title}</h5>
				<p className="card-text">{text}</p>
				<Link to={linkUrl}>
					<button className="btn btn-primary">{linkText}</button>
				</Link>
			</div>
		</div>);
};

export default Card;
