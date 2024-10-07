import React from 'react';
import {createRoot} from 'react-dom/client';
import {HashRouter, Route, Routes} from 'react-router-dom';
import './index.css';
import App from './App';
import JAPICC from "./JAPICC";
import JarHC from "./JarHC";
import JarDiff from "./JarDiff";
import SrcDiff from "./SrcDiff";

let rootElement = document.getElementById('root');
const root = createRoot(rootElement);
root.render(<React.StrictMode>
	<HashRouter>
		<Routes>
			<Route path="/" element={<App/>}/>
			<Route path="japicc" element={<JAPICC/>}/>
			<Route path="jarhc" element={<JarHC/>}/>
			<Route path="jardiff" element={<JarDiff/>}/>
			<Route path="srcdiff" element={<SrcDiff/>}/>
		</Routes>
	</HashRouter>
</React.StrictMode>);
