import Card from "./components/Card";
import Navigation from "./components/Navigation";

function App() {
	return (<>
		<Navigation/>
		<div className="mb-4 fs-4">
			This website is a collection of static analysis tools to help you find your way through &quot;JAR hell&quot; or &quot;classpath hell&quot;.
		</div>
		<div className="row">
			<div className="col-12 col-md-6 col-lg-4 mb-4">
				<Card imageUrl="/img/card-image-japicc.png" title="JAPICC - Java API Compliance Checker" text="Compare two versions of a Java library and get a report with details on source and binary compatibility." linkUrl="/japicc" linkText="Run JAPICC"/>
			</div>
			<div className="col-12 col-md-6 col-lg-4 mb-4">
				<Card imageUrl="/img/card-image-jarhc.png" title="JarHC - JAR Health Check" text="Analyze a set of Java libraries for compatibility at binary level. Find missing dependencies, duplicate classes, dangerous code, and much more." linkUrl="/jarhc" linkText="Coming soon" enabled={false}/>
			</div>
			<div className="col-12 col-md-6 col-lg-4 mb-4">
				<Card imageUrl="/img/card-image-srcdiff.png" title="SrcDiff - Java Source Code Diff" text="Compare the source code of two versions of a Java library and see what has been changed by the developers." linkUrl="/srcdiff" linkText="Coming soon" enabled={false}/>
			</div>
		</div>
	</>);
}

export default App;
