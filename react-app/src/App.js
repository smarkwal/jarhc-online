import Card from "./components/Card";
import Navigation from "./components/Navigation";
import Auth from "./components/Auth"

function App() {
	return (<>
		<Navigation/>
		<div className="mb-4 fs-4">
			This website is a collection of static analysis tools to help you find your way through &quot;JAR hell&quot; or &quot;classpath hell&quot;.
		</div>
		<div className="row">
			<div className="col-12 col-md-6 col-lg-4 mb-4">
				<Card imageUrl="/img/card-image-japicc.png" title="JAPICC - Java API Compliance Checker" text="Compare two versions of a Java library and get a report with details on source and binary compatibility." linkUrl="/japicc" linkText="Start"/>
			</div>
			<div className="col-12 col-md-6 col-lg-4 mb-4">
				<Card imageUrl="/img/card-image-jarhc.png" title="JarHC - JAR Health Check" text="Analyze a set of Java libraries for their binary compatibility." linkUrl="/jarhc" linkText="Start"/>
			</div>
			<div className="col-12 col-md-6 col-lg-4 mb-4">
				<Card imageUrl="/img/card-image-jdiff.png" title="jDiff - Java API Diff" text="" linkUrl="/jdiff" linkText="Start"/>
			</div>
		</div>

		{Auth.isSignedIn() ? <button className="btn btn-primary" onClick={Auth.signOut}>Sign out</button> : <div>
			<button className="btn btn-primary" onClick={Auth.signIn}>Sign in</button>
			<button className="btn btn-primary" onClick={Auth.signUp}>Sign up</button>
		</div>}

	</>);
}

export default App;
