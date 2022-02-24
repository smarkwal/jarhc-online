import React from 'react'
import Logo from "./Logo";
import "./Header.css"

function Header() {
	return (<header>
		<div className="Header-Row">
			<div>
				<div className="Header-Title-Line-1">JAR</div>
				<div className="Header-Title-Line-2">Health Check</div>
			</div>
			<div className="Header-Logo-Box">
				<Logo/>
			</div>
		</div>
	</header>);
}

export default Header;