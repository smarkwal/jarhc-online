import React, {useEffect, useState} from 'react';

function JapiccVersion() {

	const [output, setOutput] = useState('')

	useEffect(() => {
		fetchVersion()
	}, [])

	function fetchVersion() {
		fetch("http://localhost:8080/japicc/version")
			.then(res => showVersion(res))
	}

	function showVersion(res) {
		if (!res.ok) {
			setOutput("[unknown]")
			return
		}
		res.text().then(text => setOutput(text))
	}

	return (<>
		<pre>{output}</pre>
	</>)
}

export default JapiccVersion;