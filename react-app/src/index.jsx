import React from 'react';
import {createRoot} from 'react-dom/client';
import {HashRouter, Route, Routes} from 'react-router-dom';

import App from './App.jsx';
import JAPICC from './JAPICC.jsx';
import JarHC from './JarHC.jsx';
import JarDiff from './JarDiff.jsx';
import SrcDiff from './SrcDiff.jsx';

import './App.css';

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
