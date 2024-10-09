import React from 'react';
import {DebounceInput} from 'react-debounce-input';

import Artifacts from './Artifacts.js';

function ArtifactInput({
						   version,
						   onUpdate,
						   onRefresh
					   }) {

	const VERSION_REGEX = '^[^:]+:[^:]+:[^:]*[^.]$';

	// on every change in the input field ...
	function onChange(event) {

		const version = event.target.value.trim();

		onUpdate(version);

		// check if artifact is cached
		if (version.match(VERSION_REGEX)) {
			if (!Artifacts.isCached(version)) {
				Artifacts.searchAsync(version).then(onRefresh);
			}
		}

	}

	function getClassNames() {

		if (version === undefined || version.length === 0) {
			// input field is empty
			return '';
		}

		if (!version.match(VERSION_REGEX)) {
			// input field contains an invalid value
			return 'is-invalid';
		}

		if (Artifacts.isCached(version)) {
			if (Artifacts.isValid(version)) {
				return 'is-valid';
			} else {
				return 'is-invalid';
			}
		} else {
			return '';
		}

	}

	return (<DebounceInput value={version} className={`form-control ${getClassNames()}`} minLength={5} debounceTimeout={500} onChange={onChange} placeholder="Group:Artifact:Version" spellCheck="false"/>);
}

export default ArtifactInput;