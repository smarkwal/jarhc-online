import React, {useEffect, useState} from 'react'
import {DebounceInput} from 'react-debounce-input';
import Artifacts from './Artifacts'

function ArtifactInput({
						   version,
						   onUpdate
					   }) {

	const [state, setState] = useState(version)

	const VERSION_REGEX = "^[^:]+:[^:]+:[^:]*[^.]$"

	// on every change in the input field ...
	function onChange(event) {

		const value = event.target.value.trim()
		//console.log("onChange: input:", inputVersion)

		// remember new version
		//console.log("onChange: setVersion:", inputVersion)
		setState(value)
	}

	useEffect(() => {

		// check if artifact is cached
		if (!Artifacts.isCached(state)) {
			if (state.match(VERSION_REGEX)) {
				Artifacts.startSearch(state, () => {
					onUpdate(state)
				})
				return;
			}
		}

		onUpdate(state)

	}, [state])

	function getClassNames() {

		if (state === undefined || state.length === 0) {
			// input field is empty
			return ""
		}

		if (!state.match(VERSION_REGEX)) {
			// input field contains an invalid value
			return "is-invalid"
		}

		if (Artifacts.isCached(state)) {
			if (Artifacts.isValid(state)) {
				return "is-valid"
			} else {
				return "is-invalid"
			}
		} else {
			return ""
		}

	}

	return (<DebounceInput value={state} className={`form-control ${getClassNames()}`} minLength={5} debounceTimeout={500} onChange={onChange} placeholder="Group:Artifact:Version" spellCheck="false"/>);
}

export default ArtifactInput;