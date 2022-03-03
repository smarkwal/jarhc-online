import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import './index.css';
import App from './App';
import JDiff from "./JDiff";
import JAPICC from "./JAPICC";
import JarHC from "./JarHC";
import reportWebVitals from './reportWebVitals';

let rootElement = document.getElementById('root');
ReactDOM.render(<React.StrictMode>
	<BrowserRouter>
		<Routes>
			<Route path="/" element={<App/>}/>
			<Route path="japicc" element={<JAPICC/>}/>
			<Route path="jarhc" element={<JarHC/>}/>
			<Route path="jdiff" element={<JDiff/>}/>
		</Routes>
	</BrowserRouter>
</React.StrictMode>, rootElement);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
