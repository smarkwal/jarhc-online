import React from 'react';

import Card from './components/Card.jsx';
import Navigation from './components/Navigation.jsx';
import PayPalButton from './components/PayPalButton.jsx';
import ExternalLink from './components/ExternalLink.jsx';

function App() {
	return (<>

		<Navigation/>

		<div className="row">

			<div className="col-12 col-md-6 col-lg-4">
				<About/>
				<div className="d-none d-md-block">
					<OpenSource/>
					<Donations/>
					<Support/>
				</div>
			</div>

			<div className="col-12 col-md-6 col-lg-8">

				<div className="row">

					<div className="col-12 col-md-12 col-lg-6 mb-4">
						<Card imageUrl="/img/card-image-japicc.png" title="Java API Compliance Checker" linkUrl="/japicc" linkText="Run JAPICC">
							<span>Compare two versions of a Java library and get a report with details on source and binary compatibility.</span>
						</Card>
					</div>

					<div className="col-12 col-md-12 col-lg-6 mb-4">
						<Card imageUrl="/img/card-image-jarhc.png" title="JAR Health Check" linkUrl="/jarhc" linkText="Run JarHC">
							<span>Analyze a set of Java libraries for compatibility at binary level. Find missing dependencies, duplicate classes, dangerous code, and much more.</span>
						</Card>
					</div>

					<div className="col-12 col-md-12 col-lg-6 mb-4">
						<Card imageUrl="/img/card-image-jardiff.png" title="JAR File Diff" linkUrl="/jardiff" linkText="Coming soon" enabled={false}>
							<span>Compare the JAR files of two versions of a Java library.</span>
						</Card>
					</div>

					<div className="col-12 col-md-12 col-lg-6 mb-4">
						<Card imageUrl="/img/card-image-srcdiff.png" title="Java Source Code Diff" linkUrl="/srcdiff" linkText="Coming soon" enabled={false}>
							<span>Compare the source code of two versions of a Java library and see what has been changed by the developers.</span>
						</Card>
					</div>

					<div className="col-12 col-md-12 col-lg-6 mb-4">
						<Card imageUrl="/img/card-image-soon.png" title="Decompile JAR File" linkUrl="/decompile" linkText="Coming soon" enabled={false}>
							<span>Decompile the Java classes in a JAR file and download or inspect the Java source code.</span>
						</Card>
					</div>

					<div className="col-12 col-md-12 col-lg-6 mb-4">
						<Card imageUrl="/img/card-image-soon.png" title="Dependency Tree" linkUrl="/dependencies" linkText="Coming soon" enabled={false}>
							<span>See the full tree of direct and transitive dependencies of a Java library.</span>
						</Card>
					</div>

					<div className="col-12 col-md-12 col-lg-6 mb-4">
						<Card imageUrl="/img/card-image-soon.png" title="OWASP Dependency-Check" linkUrl="/dependency-check" linkText="Coming soon" enabled={false}>
							<span>Check if a Java library contains known security vulnerabilities.</span>
						</Card>
					</div>

				</div>

			</div>

			<div className="col-12 d-md-none">
				<OpenSource/>
				<Donations/>
				<Support/>
			</div>

		</div>
	</>);
}

function About() {
	return <>
		<div className="p-3 mb-4 bg-sidepanel">
			<h2>About this Website</h2>
			<p>This website is for Java developers.</p>
			<p>It is a collection of online tools to help find your way through &quot;JAR hell&quot; or &quot;classpath hell&quot;.</p>
			<p>All tools fall into the category of static analysis where binary code, source code, or some other kind of software artifact is inspected.</p>
		</div>
	</>;
}

function OpenSource() {
	return <>
		<div className="p-3 mb-4 bg-sidepanel">
			<h3>Open Source</h3>
			<p>Everything in this project is open source.</p>
			<p>The website itself is a <ExternalLink href="https://reactjs.org/">React</ExternalLink> app with a <ExternalLink href="https://dev.java/">Java</ExternalLink> backend running on serverless <ExternalLink href="https://aws.amazon.com/cdk/">AWS CDK</ExternalLink> infrastructure.</p>
			<p>You can find more information on GitHub at <ExternalLink href="https://github.com/smarkwal/jarhc-online">smarkwal/jarhc-online</ExternalLink>.</p>
		</div>
	</>;
}

function Donations() {
	return <>
		<div className="p-3 mb-4 bg-sidepanel">
			<h3>Donations</h3>
			<p>If you want to support this project, you can make a small donation. THANK YOU VERY MUCH!</p>
			<p>If you like it anonymous, use Bitcoin:<br/>
				<ExternalLink href="https://www.blockchain.com/btc/address/3L1V8yGPwSjixTScCpE78fUr16tEWdpD1n">
					<code>3L1V8yGPwSjixTScCpE78fUr16tEWdpD1n</code>
				</ExternalLink>
			</p>
			<p className="mb-1">If you like it comfortable, use PayPal:</p>
			<PayPalButton/>
		</div>
	</>;
}

function Support() {
	return <>
		<div className="p-3 mb-4 bg-sidepanel">
			<h3>Support</h3>
			<p>If you have found a problem with this website, please report it on <ExternalLink href="https://github.com/smarkwal/jarhc-online/issues">GitHub</ExternalLink>.</p>
		</div>
	</>;
}

export default App;
