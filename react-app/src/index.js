import React from 'react';
import ReactDOM from 'react-dom';
import {HashRouter, Route, Routes} from "react-router-dom";
import './index.css';
import App from './App';
import JAPICC from "./JAPICC";
import JarHC from "./JarHC";
import SrcDiff from "./SrcDiff";
import reportWebVitals from './reportWebVitals';

let rootElement = document.getElementById('root');
ReactDOM.render(<React.StrictMode>
	<HashRouter>
		<Routes>
			<Route path="/" element={<App/>}/>
			<Route path="japicc" element={<JAPICC/>}/>
			<Route path="jarhc" element={<JarHC/>}/>
			<Route path="srcdiff" element={<SrcDiff/>}/>
		</Routes>
	</HashRouter>
</React.StrictMode>, rootElement);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
