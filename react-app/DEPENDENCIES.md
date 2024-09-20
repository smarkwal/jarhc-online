
# Dependencies

## Production

### Direct

`npm ls --omit=dev`

```
react-app@0.1.0 /home/markwalder/Dev/IntelliJ/jarhc-online/react-app
├── react-debounce-input@3.3.0
├── react-dom@18.3.1
├── react-router-dom@6.26.2
└── react@18.3.1
```

### Transitive

`npm ls --omit=dev --all`

```
react-app@0.1.0 /home/markwalder/Dev/IntelliJ/jarhc-online/react-app
├─┬ react-debounce-input@3.3.0
│ ├── lodash.debounce@4.0.8
│ ├─┬ prop-types@15.8.1
│ │ ├── loose-envify@1.4.0 deduped
│ │ ├── object-assign@4.1.1
│ │ └── react-is@16.13.1
│ └── react@18.3.1 deduped
├─┬ react-dom@18.3.1
│ ├─┬ loose-envify@1.4.0
│ │ └── js-tokens@4.0.0
│ ├── react@18.3.1 deduped
│ └─┬ scheduler@0.23.2
│   └── loose-envify@1.4.0 deduped
├─┬ react-router-dom@6.26.2
│ ├── @remix-run/router@1.19.2
│ ├── react-dom@18.3.1 deduped
│ ├─┬ react-router@6.26.2
│ │ ├── @remix-run/router@1.19.2 deduped
│ │ └── react@18.3.1 deduped
│ └── react@18.3.1 deduped
└─┬ react@18.3.1
  └── loose-envify@1.4.0 deduped
```

## Development

### Direct

`npm ls --include=dev`

```
react-app@0.1.0 /home/markwalder/Dev/IntelliJ/jarhc-online/react-app
├── @babel/plugin-proposal-private-property-in-object@7.21.11
├── react-debounce-input@3.3.0
├── react-dom@18.3.1
├── react-router-dom@6.26.2
├── react-scripts@5.0.1
└── react@18.3.1
```

### Transitive

`npm ls --include=dev --all`

```
react-app@0.1.0 /home/markwalder/Dev/IntelliJ/jarhc-online/react-app
├─┬ @babel/plugin-proposal-private-property-in-object@7.21.11
│ ├─┬ @babel/core@7.24.7
│ │ ├─┬ @ampproject/remapping@2.3.0
│ │ │ ├─┬ @jridgewell/gen-mapping@0.3.5
│ │ │ │ ├── @jridgewell/set-array@1.2.1
│ │ │ │ ├── @jridgewell/sourcemap-codec@1.4.15 deduped
│ │ │ │ └── @jridgewell/trace-mapping@0.3.25 deduped
│ │ │ └── @jridgewell/trace-mapping@0.3.25 deduped
│ │ ├─┬ @babel/code-frame@7.24.7
│ │ │ ├─┬ @babel/highlight@7.24.7
│ │ │ │ ├── @babel/helper-validator-identifier@7.24.7 deduped
│ │ │ │ ├─┬ chalk@2.4.2
│ │ │ │ │ ├─┬ ansi-styles@3.2.1
│ │ │ │ │ │ └─┬ color-convert@1.9.3
│ │ │ │ │ │   └── color-name@1.1.3
│ │ │ │ │ ├── escape-string-regexp@1.0.5
│ │ │ │ │ └─┬ supports-color@5.5.0
│ │ │ │ │   └── has-flag@3.0.0
│ │ │ │ ├── js-tokens@4.0.0 deduped
│ │ │ │ └── picocolors@1.0.1 deduped
│ │ │ └── picocolors@1.0.1 deduped
│ │ ├─┬ @babel/generator@7.24.7
│ │ │ ├── @babel/types@7.24.7 deduped
│ │ │ ├── @jridgewell/gen-mapping@0.3.5 deduped
│ │ │ ├── @jridgewell/trace-mapping@0.3.25 deduped
│ │ │ └── jsesc@2.5.2
│ │ ├─┬ @babel/helper-compilation-targets@7.24.7
│ │ │ ├── @babel/compat-data@7.24.7
│ │ │ ├── @babel/helper-validator-option@7.24.7
│ │ │ ├── browserslist@4.23.1 deduped
│ │ │ ├─┬ lru-cache@5.1.1
│ │ │ │ └── yallist@3.1.1
│ │ │ └── semver@6.3.1
│ │ ├─┬ @babel/helper-module-transforms@7.24.7
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-environment-visitor@7.24.7 deduped
│ │ │ ├─┬ @babel/helper-module-imports@7.24.7
│ │ │ │ ├── @babel/traverse@7.24.7 deduped
│ │ │ │ └── @babel/types@7.24.7 deduped
│ │ │ ├─┬ @babel/helper-simple-access@7.24.7
│ │ │ │ ├── @babel/traverse@7.24.7 deduped
│ │ │ │ └── @babel/types@7.24.7 deduped
│ │ │ ├── @babel/helper-split-export-declaration@7.24.7 deduped
│ │ │ └── @babel/helper-validator-identifier@7.24.7
│ │ ├─┬ @babel/helpers@7.24.7
│ │ │ ├── @babel/template@7.24.7 deduped
│ │ │ └── @babel/types@7.24.7 deduped
│ │ ├── @babel/parser@7.24.7
│ │ ├─┬ @babel/template@7.24.7
│ │ │ ├── @babel/code-frame@7.24.7 deduped
│ │ │ ├── @babel/parser@7.24.7 deduped
│ │ │ └── @babel/types@7.24.7 deduped
│ │ ├─┬ @babel/traverse@7.24.7
│ │ │ ├── @babel/code-frame@7.24.7 deduped
│ │ │ ├── @babel/generator@7.24.7 deduped
│ │ │ ├── @babel/helper-environment-visitor@7.24.7 deduped
│ │ │ ├── @babel/helper-function-name@7.24.7 deduped
│ │ │ ├─┬ @babel/helper-hoist-variables@7.24.7
│ │ │ │ └── @babel/types@7.24.7 deduped
│ │ │ ├── @babel/helper-split-export-declaration@7.24.7 deduped
│ │ │ ├── @babel/parser@7.24.7 deduped
│ │ │ ├── @babel/types@7.24.7 deduped
│ │ │ ├── debug@4.3.5 deduped
│ │ │ └── globals@11.12.0
│ │ ├─┬ @babel/types@7.24.7
│ │ │ ├── @babel/helper-string-parser@7.24.7
│ │ │ ├── @babel/helper-validator-identifier@7.24.7 deduped
│ │ │ └── to-fast-properties@2.0.0
│ │ ├── convert-source-map@2.0.0
│ │ ├─┬ debug@4.3.5
│ │ │ └── ms@2.1.2
│ │ ├── gensync@1.0.0-beta.2
│ │ ├── json5@2.2.3
│ │ └── semver@6.3.1
│ ├─┬ @babel/helper-annotate-as-pure@7.24.7
│ │ └── @babel/types@7.24.7 deduped
│ ├─┬ @babel/helper-create-class-features-plugin@7.24.7
│ │ ├── @babel/core@7.24.7 deduped
│ │ ├── @babel/helper-annotate-as-pure@7.24.7 deduped
│ │ ├─┬ @babel/helper-environment-visitor@7.24.7
│ │ │ └── @babel/types@7.24.7 deduped
│ │ ├─┬ @babel/helper-function-name@7.24.7
│ │ │ ├── @babel/template@7.24.7 deduped
│ │ │ └── @babel/types@7.24.7 deduped
│ │ ├─┬ @babel/helper-member-expression-to-functions@7.24.7
│ │ │ ├── @babel/traverse@7.24.7 deduped
│ │ │ └── @babel/types@7.24.7 deduped
│ │ ├─┬ @babel/helper-optimise-call-expression@7.24.7
│ │ │ └── @babel/types@7.24.7 deduped
│ │ ├─┬ @babel/helper-replace-supers@7.24.7
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-environment-visitor@7.24.7 deduped
│ │ │ ├── @babel/helper-member-expression-to-functions@7.24.7 deduped
│ │ │ └── @babel/helper-optimise-call-expression@7.24.7 deduped
│ │ ├─┬ @babel/helper-skip-transparent-expression-wrappers@7.24.7
│ │ │ ├── @babel/traverse@7.24.7 deduped
│ │ │ └── @babel/types@7.24.7 deduped
│ │ ├─┬ @babel/helper-split-export-declaration@7.24.7
│ │ │ └── @babel/types@7.24.7 deduped
│ │ └── semver@6.3.1
│ ├── @babel/helper-plugin-utils@7.24.7
│ └─┬ @babel/plugin-syntax-private-property-in-object@7.14.5
│   ├── @babel/core@7.24.7 deduped
│   └── @babel/helper-plugin-utils@7.24.7 deduped
├─┬ react-debounce-input@3.3.0
│ ├── lodash.debounce@4.0.8
│ ├─┬ prop-types@15.8.1
│ │ ├── loose-envify@1.4.0 deduped
│ │ ├── object-assign@4.1.1
│ │ └── react-is@16.13.1
│ └── react@18.3.1 deduped
├─┬ react-dom@18.3.1
│ ├─┬ loose-envify@1.4.0
│ │ └── js-tokens@4.0.0
│ ├── react@18.3.1 deduped
│ └─┬ scheduler@0.23.2
│   └── loose-envify@1.4.0 deduped
├─┬ react-router-dom@6.26.2
│ ├── @remix-run/router@1.19.2
│ ├── react-dom@18.3.1 deduped
│ ├─┬ react-router@6.26.2
│ │ ├── @remix-run/router@1.19.2 deduped
│ │ └── react@18.3.1 deduped
│ └── react@18.3.1 deduped
├─┬ react-scripts@5.0.1
│ ├── @babel/core@7.24.7 deduped
│ ├─┬ @pmmmwh/react-refresh-webpack-plugin@0.5.15
│ │ ├── UNMET OPTIONAL DEPENDENCY @types/webpack@4.x || 5.x
│ │ ├── ansi-html@0.0.9
│ │ ├── core-js-pure@3.37.1
│ │ ├─┬ error-stack-parser@2.1.4
│ │ │ └── stackframe@1.3.4
│ │ ├── html-entities@2.5.2
│ │ ├─┬ loader-utils@2.0.4
│ │ │ ├── big.js@5.2.2
│ │ │ ├── emojis-list@3.0.0
│ │ │ └── json5@2.2.3 deduped
│ │ ├── react-refresh@0.11.0 deduped
│ │ ├─┬ schema-utils@4.2.0
│ │ │ ├── @types/json-schema@7.0.15
│ │ │ ├─┬ ajv-formats@2.1.1
│ │ │ │ └─┬ ajv@8.16.0
│ │ │ │   ├── fast-deep-equal@3.1.3 deduped
│ │ │ │   ├── json-schema-traverse@1.0.0
│ │ │ │   ├── require-from-string@2.0.2 deduped
│ │ │ │   └── uri-js@4.4.1 deduped
│ │ │ ├─┬ ajv-keywords@5.1.0
│ │ │ │ ├── ajv@8.16.0 deduped
│ │ │ │ └── fast-deep-equal@3.1.3 deduped
│ │ │ └─┬ ajv@8.16.0
│ │ │   ├── fast-deep-equal@3.1.3 deduped
│ │ │   ├── json-schema-traverse@1.0.0
│ │ │   ├── require-from-string@2.0.2
│ │ │   └── uri-js@4.4.1 deduped
│ │ ├── UNMET OPTIONAL DEPENDENCY sockjs-client@^1.4.0
│ │ ├── source-map@0.7.4
│ │ ├── type-fest@0.21.3
│ │ ├── webpack-dev-server@4.15.2 deduped
│ │ ├── UNMET OPTIONAL DEPENDENCY webpack-hot-middleware@2.x
│ │ ├── UNMET OPTIONAL DEPENDENCY webpack-plugin-serve@0.x || 1.x
│ │ └── webpack@5.92.1 deduped
│ ├─┬ @svgr/webpack@5.5.0
│ │ ├── @babel/core@7.24.7 deduped
│ │ ├─┬ @babel/plugin-transform-react-constant-elements@7.24.7
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ ├─┬ @babel/preset-env@7.24.7
│ │ │ ├── @babel/compat-data@7.24.7 deduped
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-compilation-targets@7.24.7 deduped
│ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├── @babel/helper-validator-option@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-bugfix-firefox-class-in-computed-class-key@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-environment-visitor@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-bugfix-safari-id-destructuring-collision-in-function-expression@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-bugfix-v8-spread-parameters-in-optional-chaining@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ ├── @babel/helper-skip-transparent-expression-wrappers@7.24.7 deduped
│ │ │ │ └── @babel/plugin-transform-optional-chaining@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-bugfix-v8-static-class-fields-redefine-readonly@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-environment-visitor@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-proposal-private-property-in-object@7.21.0-placeholder-for-preset-env.2
│ │ │ │ └── @babel/core@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-async-generators@7.8.4
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-class-properties@7.12.13
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-class-static-block@7.14.5
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-dynamic-import@7.8.3
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-export-namespace-from@7.8.3
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-import-assertions@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-import-attributes@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-import-meta@7.10.4
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-json-strings@7.8.3
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-logical-assignment-operators@7.10.4
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-nullish-coalescing-operator@7.8.3
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-numeric-separator@7.10.4
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-object-rest-spread@7.8.3
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-optional-catch-binding@7.8.3
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-optional-chaining@7.8.3
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├── @babel/plugin-syntax-private-property-in-object@7.14.5 deduped
│ │ │ ├─┬ @babel/plugin-syntax-top-level-await@7.14.5
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-unicode-sets-regex@7.18.6
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├─┬ @babel/helper-create-regexp-features-plugin@7.24.7
│ │ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ │ ├── @babel/helper-annotate-as-pure@7.24.7 deduped
│ │ │ │ │ ├─┬ regexpu-core@5.3.2
│ │ │ │ │ │ ├── @babel/regjsgen@0.8.0
│ │ │ │ │ │ ├─┬ regenerate-unicode-properties@10.1.1
│ │ │ │ │ │ │ └── regenerate@1.4.2 deduped
│ │ │ │ │ │ ├── regenerate@1.4.2
│ │ │ │ │ │ ├─┬ regjsparser@0.9.1
│ │ │ │ │ │ │ └── jsesc@0.5.0
│ │ │ │ │ │ ├─┬ unicode-match-property-ecmascript@2.0.0
│ │ │ │ │ │ │ ├── unicode-canonical-property-names-ecmascript@2.0.0
│ │ │ │ │ │ │ └── unicode-property-aliases-ecmascript@2.1.0
│ │ │ │ │ │ └── unicode-match-property-value-ecmascript@2.1.0
│ │ │ │ │ └── semver@6.3.1
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-arrow-functions@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-async-generator-functions@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-environment-visitor@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ ├─┬ @babel/helper-remap-async-to-generator@7.24.7
│ │ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ │ ├── @babel/helper-annotate-as-pure@7.24.7 deduped
│ │ │ │ │ ├── @babel/helper-environment-visitor@7.24.7 deduped
│ │ │ │ │ └─┬ @babel/helper-wrap-function@7.24.7
│ │ │ │ │   ├── @babel/helper-function-name@7.24.7 deduped
│ │ │ │ │   ├── @babel/template@7.24.7 deduped
│ │ │ │ │   ├── @babel/traverse@7.24.7 deduped
│ │ │ │ │   └── @babel/types@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-async-generators@7.8.4 deduped
│ │ │ ├─┬ @babel/plugin-transform-async-to-generator@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-module-imports@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/helper-remap-async-to-generator@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-block-scoped-functions@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-block-scoping@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-class-properties@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-create-class-features-plugin@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-class-static-block@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-create-class-features-plugin@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-class-static-block@7.14.5 deduped
│ │ │ ├─┬ @babel/plugin-transform-classes@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-annotate-as-pure@7.24.7 deduped
│ │ │ │ ├── @babel/helper-compilation-targets@7.24.7 deduped
│ │ │ │ ├── @babel/helper-environment-visitor@7.24.7 deduped
│ │ │ │ ├── @babel/helper-function-name@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ ├── @babel/helper-replace-supers@7.24.7 deduped
│ │ │ │ ├── @babel/helper-split-export-declaration@7.24.7 deduped
│ │ │ │ └── globals@11.12.0 deduped
│ │ │ ├─┬ @babel/plugin-transform-computed-properties@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/template@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-destructuring@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-dotall-regex@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-create-regexp-features-plugin@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-duplicate-keys@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-dynamic-import@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-dynamic-import@7.8.3 deduped
│ │ │ ├─┬ @babel/plugin-transform-exponentiation-operator@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├─┬ @babel/helper-builder-binary-assignment-operator-visitor@7.24.7
│ │ │ │ │ ├── @babel/traverse@7.24.7 deduped
│ │ │ │ │ └── @babel/types@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-export-namespace-from@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-export-namespace-from@7.8.3 deduped
│ │ │ ├─┬ @babel/plugin-transform-for-of@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/helper-skip-transparent-expression-wrappers@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-function-name@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-compilation-targets@7.24.7 deduped
│ │ │ │ ├── @babel/helper-function-name@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-json-strings@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-json-strings@7.8.3 deduped
│ │ │ ├─┬ @babel/plugin-transform-literals@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-logical-assignment-operators@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-logical-assignment-operators@7.10.4 deduped
│ │ │ ├─┬ @babel/plugin-transform-member-expression-literals@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-modules-amd@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-module-transforms@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-modules-commonjs@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-module-transforms@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/helper-simple-access@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-modules-systemjs@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-hoist-variables@7.24.7 deduped
│ │ │ │ ├── @babel/helper-module-transforms@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/helper-validator-identifier@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-modules-umd@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-module-transforms@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-named-capturing-groups-regex@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-create-regexp-features-plugin@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-new-target@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-nullish-coalescing-operator@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-nullish-coalescing-operator@7.8.3 deduped
│ │ │ ├─┬ @babel/plugin-transform-numeric-separator@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-numeric-separator@7.10.4 deduped
│ │ │ ├─┬ @babel/plugin-transform-object-rest-spread@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-compilation-targets@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ ├── @babel/plugin-syntax-object-rest-spread@7.8.3 deduped
│ │ │ │ └── @babel/plugin-transform-parameters@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-object-super@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/helper-replace-supers@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-optional-catch-binding@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-optional-catch-binding@7.8.3 deduped
│ │ │ ├─┬ @babel/plugin-transform-optional-chaining@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ ├── @babel/helper-skip-transparent-expression-wrappers@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-optional-chaining@7.8.3 deduped
│ │ │ ├─┬ @babel/plugin-transform-parameters@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-private-methods@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-create-class-features-plugin@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-private-property-in-object@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-annotate-as-pure@7.24.7 deduped
│ │ │ │ ├── @babel/helper-create-class-features-plugin@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/plugin-syntax-private-property-in-object@7.14.5 deduped
│ │ │ ├─┬ @babel/plugin-transform-property-literals@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-regenerator@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └─┬ regenerator-transform@0.15.2
│ │ │ │   └── @babel/runtime@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-reserved-words@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-shorthand-properties@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-spread@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ └── @babel/helper-skip-transparent-expression-wrappers@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-sticky-regex@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-template-literals@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-typeof-symbol@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-unicode-escapes@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-unicode-property-regex@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-create-regexp-features-plugin@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-unicode-regex@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-create-regexp-features-plugin@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-unicode-sets-regex@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-create-regexp-features-plugin@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @babel/preset-modules@0.1.6-no-external-plugins
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ ├── @babel/types@7.24.7 deduped
│ │ │ │ └── esutils@2.0.3 deduped
│ │ │ ├─┬ babel-plugin-polyfill-corejs2@0.4.11
│ │ │ │ ├── @babel/compat-data@7.24.7 deduped
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├─┬ @babel/helper-define-polyfill-provider@0.6.2
│ │ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ │ ├── @babel/helper-compilation-targets@7.24.7 deduped
│ │ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ │ ├── debug@4.3.5 deduped
│ │ │ │ │ ├── lodash.debounce@4.0.8 deduped
│ │ │ │ │ └── resolve@1.22.8 deduped
│ │ │ │ └── semver@6.3.1
│ │ │ ├─┬ babel-plugin-polyfill-corejs3@0.10.4
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-define-polyfill-provider@0.6.2 deduped
│ │ │ │ └── core-js-compat@3.37.1 deduped
│ │ │ ├─┬ babel-plugin-polyfill-regenerator@0.6.2
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-define-polyfill-provider@0.6.2 deduped
│ │ │ ├─┬ core-js-compat@3.37.1
│ │ │ │ └── browserslist@4.23.1 deduped
│ │ │ └── semver@6.3.1
│ │ ├─┬ @babel/preset-react@7.24.7
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├── @babel/helper-validator-option@7.24.7 deduped
│ │ │ ├── @babel/plugin-transform-react-display-name@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-react-jsx-development@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/plugin-transform-react-jsx@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-transform-react-jsx@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/helper-annotate-as-pure@7.24.7 deduped
│ │ │ │ ├── @babel/helper-module-imports@7.24.7 deduped
│ │ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ │ ├── @babel/plugin-syntax-jsx@7.24.7 deduped
│ │ │ │ └── @babel/types@7.24.7 deduped
│ │ │ └─┬ @babel/plugin-transform-react-pure-annotations@7.24.7
│ │ │   ├── @babel/core@7.24.7 deduped
│ │ │   ├── @babel/helper-annotate-as-pure@7.24.7 deduped
│ │ │   └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ ├─┬ @svgr/core@5.5.0
│ │ │ ├── @svgr/plugin-jsx@5.5.0 deduped
│ │ │ ├── camelcase@6.3.0 deduped
│ │ │ └── cosmiconfig@7.1.0 deduped
│ │ ├─┬ @svgr/plugin-jsx@5.5.0
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├─┬ @svgr/babel-preset@5.5.0
│ │ │ │ ├── @svgr/babel-plugin-add-jsx-attribute@5.4.0
│ │ │ │ ├── @svgr/babel-plugin-remove-jsx-attribute@5.4.0
│ │ │ │ ├── @svgr/babel-plugin-remove-jsx-empty-expression@5.0.1
│ │ │ │ ├── @svgr/babel-plugin-replace-jsx-attribute-value@5.0.1
│ │ │ │ ├── @svgr/babel-plugin-svg-dynamic-title@5.4.0
│ │ │ │ ├── @svgr/babel-plugin-svg-em-dimensions@5.4.0
│ │ │ │ ├── @svgr/babel-plugin-transform-react-native-svg@5.4.0
│ │ │ │ └── @svgr/babel-plugin-transform-svg-component@5.5.0
│ │ │ ├─┬ @svgr/hast-util-to-babel-ast@5.5.0
│ │ │ │ └── @babel/types@7.24.7 deduped
│ │ │ └── svg-parser@2.0.4
│ │ ├─┬ @svgr/plugin-svgo@5.5.0
│ │ │ ├── cosmiconfig@7.1.0 deduped
│ │ │ ├── deepmerge@4.3.1
│ │ │ └─┬ svgo@1.3.2
│ │ │   ├── chalk@2.4.2 deduped
│ │ │   ├─┬ coa@2.0.2
│ │ │   │ ├── @types/q@1.5.8
│ │ │   │ ├── chalk@2.4.2 deduped
│ │ │   │ └── q@1.5.1
│ │ │   ├── css-select-base-adapter@0.1.1
│ │ │   ├─┬ css-select@2.1.0
│ │ │   │ ├── boolbase@1.0.0
│ │ │   │ ├── css-what@3.4.2
│ │ │   │ ├─┬ domutils@1.7.0
│ │ │   │ │ ├─┬ dom-serializer@0.2.2
│ │ │   │ │ │ ├── domelementtype@2.3.0 deduped
│ │ │   │ │ │ └── entities@2.2.0 deduped
│ │ │   │ │ └── domelementtype@1.3.1
│ │ │   │ └─┬ nth-check@1.0.2
│ │ │   │   └── boolbase@1.0.0 deduped
│ │ │   ├─┬ css-tree@1.0.0-alpha.37
│ │ │   │ ├── mdn-data@2.0.4
│ │ │   │ └── source-map@0.6.1
│ │ │   ├─┬ csso@4.2.0
│ │ │   │ └─┬ css-tree@1.1.3
│ │ │   │   ├── mdn-data@2.0.14
│ │ │   │   └── source-map@0.6.1
│ │ │   ├─┬ js-yaml@3.14.1
│ │ │   │ ├─┬ argparse@1.0.10
│ │ │   │ │ └── sprintf-js@1.0.3
│ │ │   │ └── esprima@4.0.1
│ │ │   ├─┬ mkdirp@0.5.6
│ │ │   │ └── minimist@1.2.8 deduped
│ │ │   ├── object.values@1.2.0 deduped
│ │ │   ├── sax@1.2.4
│ │ │   ├── stable@0.1.8
│ │ │   ├── unquote@1.1.1
│ │ │   └─┬ util.promisify@1.0.1
│ │ │     ├── define-properties@1.2.1 deduped
│ │ │     ├── es-abstract@1.23.3 deduped
│ │ │     ├── has-symbols@1.0.3 deduped
│ │ │     └─┬ object.getownpropertydescriptors@2.1.8
│ │ │       ├─┬ array.prototype.reduce@1.0.7
│ │ │       │ ├── call-bind@1.0.7 deduped
│ │ │       │ ├── define-properties@1.2.1 deduped
│ │ │       │ ├── es-abstract@1.23.3 deduped
│ │ │       │ ├── es-array-method-boxes-properly@1.0.0
│ │ │       │ ├── es-errors@1.3.0 deduped
│ │ │       │ ├── es-object-atoms@1.0.0 deduped
│ │ │       │ └── is-string@1.0.7 deduped
│ │ │       ├── call-bind@1.0.7 deduped
│ │ │       ├── define-properties@1.2.1 deduped
│ │ │       ├── es-abstract@1.23.3 deduped
│ │ │       ├── es-object-atoms@1.0.0 deduped
│ │ │       ├── gopd@1.0.1 deduped
│ │ │       └── safe-array-concat@1.1.2 deduped
│ │ └── loader-utils@2.0.4 deduped
│ ├─┬ babel-jest@27.5.1
│ │ ├── @babel/core@7.24.7 deduped
│ │ ├─┬ @jest/transform@27.5.1
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ ├── babel-plugin-istanbul@6.1.1 deduped
│ │ │ ├─┬ chalk@4.1.2
│ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │   └── color-name@1.1.4
│ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │   └── has-flag@4.0.0
│ │ │ ├── convert-source-map@1.9.0
│ │ │ ├── fast-json-stable-stringify@2.1.0 deduped
│ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ ├── jest-haste-map@27.5.1 deduped
│ │ │ ├── jest-regex-util@27.5.1
│ │ │ ├── jest-util@27.5.1 deduped
│ │ │ ├── micromatch@4.0.7 deduped
│ │ │ ├── pirates@4.0.6
│ │ │ ├── slash@3.0.0 deduped
│ │ │ ├── source-map@0.6.1
│ │ │ └─┬ write-file-atomic@3.0.3
│ │ │   ├── imurmurhash@0.1.4 deduped
│ │ │   ├── is-typedarray@1.0.0
│ │ │   ├── signal-exit@3.0.7
│ │ │   └─┬ typedarray-to-buffer@3.1.5
│ │ │     └── is-typedarray@1.0.0 deduped
│ │ ├─┬ @jest/types@27.5.1
│ │ │ ├── @types/istanbul-lib-coverage@2.0.6
│ │ │ ├─┬ @types/istanbul-reports@3.0.4
│ │ │ │ └─┬ @types/istanbul-lib-report@3.0.3
│ │ │ │   └── @types/istanbul-lib-coverage@2.0.6 deduped
│ │ │ ├─┬ @types/node@20.14.9
│ │ │ │ └── undici-types@5.26.5
│ │ │ ├─┬ @types/yargs@16.0.9
│ │ │ │ └── @types/yargs-parser@21.0.3
│ │ │ └─┬ chalk@4.1.2
│ │ │   ├─┬ ansi-styles@4.3.0
│ │ │   │ └─┬ color-convert@2.0.1
│ │ │   │   └── color-name@1.1.4
│ │ │   └─┬ supports-color@7.2.0
│ │ │     └── has-flag@4.0.0
│ │ ├─┬ @types/babel__core@7.20.5
│ │ │ ├── @babel/parser@7.24.7 deduped
│ │ │ ├── @babel/types@7.24.7 deduped
│ │ │ ├─┬ @types/babel__generator@7.6.8
│ │ │ │ └── @babel/types@7.24.7 deduped
│ │ │ ├─┬ @types/babel__template@7.4.4
│ │ │ │ ├── @babel/parser@7.24.7 deduped
│ │ │ │ └── @babel/types@7.24.7 deduped
│ │ │ └─┬ @types/babel__traverse@7.20.6
│ │ │   └── @babel/types@7.24.7 deduped
│ │ ├─┬ babel-plugin-istanbul@6.1.1
│ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├─┬ @istanbuljs/load-nyc-config@1.1.0
│ │ │ │ ├── camelcase@5.3.1
│ │ │ │ ├─┬ find-up@4.1.0
│ │ │ │ │ ├─┬ locate-path@5.0.0
│ │ │ │ │ │ └─┬ p-locate@4.1.0
│ │ │ │ │ │   └── p-limit@2.3.0 deduped
│ │ │ │ │ └── path-exists@4.0.0 deduped
│ │ │ │ ├── get-package-type@0.1.0
│ │ │ │ ├── js-yaml@3.14.1 deduped
│ │ │ │ └── resolve-from@5.0.0
│ │ │ ├── @istanbuljs/schema@0.1.3
│ │ │ ├─┬ istanbul-lib-instrument@5.2.1
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/parser@7.24.7 deduped
│ │ │ │ ├── @istanbuljs/schema@0.1.3 deduped
│ │ │ │ ├── istanbul-lib-coverage@3.2.2
│ │ │ │ └── semver@6.3.1
│ │ │ └─┬ test-exclude@6.0.0
│ │ │   ├── @istanbuljs/schema@0.1.3 deduped
│ │ │   ├── glob@7.2.3 deduped
│ │ │   └── minimatch@3.1.2 deduped
│ │ ├─┬ babel-preset-jest@27.5.1
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├─┬ babel-plugin-jest-hoist@27.5.1
│ │ │ │ ├── @babel/template@7.24.7 deduped
│ │ │ │ ├── @babel/types@7.24.7 deduped
│ │ │ │ ├── @types/babel__core@7.20.5 deduped
│ │ │ │ └── @types/babel__traverse@7.20.6 deduped
│ │ │ └─┬ babel-preset-current-node-syntax@1.0.1
│ │ │   ├── @babel/core@7.24.7 deduped
│ │ │   ├── @babel/plugin-syntax-async-generators@7.8.4 deduped
│ │ │   ├─┬ @babel/plugin-syntax-bigint@7.8.3
│ │ │   │ ├── @babel/core@7.24.7 deduped
│ │ │   │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │   ├── @babel/plugin-syntax-class-properties@7.12.13 deduped
│ │ │   ├── @babel/plugin-syntax-import-meta@7.10.4 deduped
│ │ │   ├── @babel/plugin-syntax-json-strings@7.8.3 deduped
│ │ │   ├── @babel/plugin-syntax-logical-assignment-operators@7.10.4 deduped
│ │ │   ├── @babel/plugin-syntax-nullish-coalescing-operator@7.8.3 deduped
│ │ │   ├── @babel/plugin-syntax-numeric-separator@7.10.4 deduped
│ │ │   ├── @babel/plugin-syntax-object-rest-spread@7.8.3 deduped
│ │ │   ├── @babel/plugin-syntax-optional-catch-binding@7.8.3 deduped
│ │ │   ├── @babel/plugin-syntax-optional-chaining@7.8.3 deduped
│ │ │   └── @babel/plugin-syntax-top-level-await@7.14.5 deduped
│ │ ├─┬ chalk@4.1.2
│ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │   └── color-name@1.1.4
│ │ │ └─┬ supports-color@7.2.0
│ │ │   └── has-flag@4.0.0
│ │ ├── graceful-fs@4.2.11
│ │ └── slash@3.0.0
│ ├─┬ babel-loader@8.3.0
│ │ ├── @babel/core@7.24.7 deduped
│ │ ├─┬ find-cache-dir@3.3.2
│ │ │ ├── commondir@1.0.1
│ │ │ ├── make-dir@3.1.0 deduped
│ │ │ └─┬ pkg-dir@4.2.0
│ │ │   └── find-up@4.1.0 deduped
│ │ ├── loader-utils@2.0.4 deduped
│ │ ├─┬ make-dir@3.1.0
│ │ │ └── semver@6.3.1
│ │ ├─┬ schema-utils@2.7.1
│ │ │ ├── @types/json-schema@7.0.15 deduped
│ │ │ ├─┬ ajv-keywords@3.5.2
│ │ │ │ └── ajv@6.12.6 deduped
│ │ │ └── ajv@6.12.6 deduped
│ │ └── webpack@5.92.1 deduped
│ ├─┬ babel-plugin-named-asset-import@0.3.8
│ │ └── @babel/core@7.24.7 deduped
│ ├─┬ babel-preset-react-app@10.0.1
│ │ ├── @babel/core@7.24.7 deduped
│ │ ├─┬ @babel/plugin-proposal-class-properties@7.18.6
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-create-class-features-plugin@7.24.7 deduped
│ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ ├─┬ @babel/plugin-proposal-decorators@7.24.7
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-create-class-features-plugin@7.24.7 deduped
│ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ └─┬ @babel/plugin-syntax-decorators@7.24.7
│ │ │   ├── @babel/core@7.24.7 deduped
│ │ │   └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ ├─┬ @babel/plugin-proposal-nullish-coalescing-operator@7.18.6
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ └── @babel/plugin-syntax-nullish-coalescing-operator@7.8.3 deduped
│ │ ├─┬ @babel/plugin-proposal-numeric-separator@7.18.6
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ └── @babel/plugin-syntax-numeric-separator@7.10.4 deduped
│ │ ├─┬ @babel/plugin-proposal-optional-chaining@7.21.0
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├── @babel/helper-skip-transparent-expression-wrappers@7.24.7 deduped
│ │ │ └── @babel/plugin-syntax-optional-chaining@7.8.3 deduped
│ │ ├─┬ @babel/plugin-proposal-private-methods@7.18.6
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-create-class-features-plugin@7.24.7 deduped
│ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ ├─┬ @babel/plugin-transform-flow-strip-types@7.24.7
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ └─┬ @babel/plugin-syntax-flow@7.24.7
│ │ │   ├── @babel/core@7.24.7 deduped
│ │ │   └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ ├─┬ @babel/plugin-transform-react-display-name@7.24.7
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ ├─┬ @babel/plugin-transform-runtime@7.24.7
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-module-imports@7.24.7 deduped
│ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├── babel-plugin-polyfill-corejs2@0.4.11 deduped
│ │ │ ├── babel-plugin-polyfill-corejs3@0.10.4 deduped
│ │ │ ├── babel-plugin-polyfill-regenerator@0.6.2 deduped
│ │ │ └── semver@6.3.1
│ │ ├── @babel/preset-env@7.24.7 deduped
│ │ ├── @babel/preset-react@7.24.7 deduped
│ │ ├─┬ @babel/preset-typescript@7.24.7
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├── @babel/helper-validator-option@7.24.7 deduped
│ │ │ ├─┬ @babel/plugin-syntax-jsx@7.24.7
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │ ├── @babel/plugin-transform-modules-commonjs@7.24.7 deduped
│ │ │ └─┬ @babel/plugin-transform-typescript@7.24.7
│ │ │   ├── @babel/core@7.24.7 deduped
│ │ │   ├── @babel/helper-annotate-as-pure@7.24.7 deduped
│ │ │   ├── @babel/helper-create-class-features-plugin@7.24.7 deduped
│ │ │   ├── @babel/helper-plugin-utils@7.24.7 deduped
│ │ │   └─┬ @babel/plugin-syntax-typescript@7.24.7
│ │ │     ├── @babel/core@7.24.7 deduped
│ │ │     └── @babel/helper-plugin-utils@7.24.7 deduped
│ │ ├─┬ @babel/runtime@7.24.7
│ │ │ └── regenerator-runtime@0.14.1
│ │ ├─┬ babel-plugin-macros@3.1.0
│ │ │ ├── @babel/runtime@7.24.7 deduped
│ │ │ ├── cosmiconfig@7.1.0 deduped
│ │ │ └── resolve@1.22.8 deduped
│ │ └── babel-plugin-transform-react-remove-prop-types@0.4.24
│ ├─┬ bfj@7.1.0
│ │ ├── bluebird@3.7.2
│ │ ├── check-types@11.2.3
│ │ ├── hoopy@0.1.4
│ │ ├─┬ jsonpath@1.1.1
│ │ │ ├── esprima@1.2.2
│ │ │ ├─┬ static-eval@2.0.2
│ │ │ │ └─┬ escodegen@1.14.3
│ │ │ │   ├── esprima@4.0.1 deduped
│ │ │ │   ├── estraverse@4.3.0
│ │ │ │   ├── esutils@2.0.3 deduped
│ │ │ │   ├─┬ optionator@0.8.3
│ │ │ │   │ ├── deep-is@0.1.4 deduped
│ │ │ │   │ ├── fast-levenshtein@2.0.6 deduped
│ │ │ │   │ ├─┬ levn@0.3.0
│ │ │ │   │ │ ├── prelude-ls@1.1.2 deduped
│ │ │ │   │ │ └── type-check@0.3.2 deduped
│ │ │ │   │ ├── prelude-ls@1.1.2
│ │ │ │   │ ├─┬ type-check@0.3.2
│ │ │ │   │ │ └── prelude-ls@1.1.2 deduped
│ │ │ │   │ └── word-wrap@1.2.5 deduped
│ │ │ │   └── source-map@0.6.1
│ │ │ └── underscore@1.12.1
│ │ └── tryer@1.0.1
│ ├─┬ browserslist@4.23.1
│ │ ├── caniuse-lite@1.0.30001638
│ │ ├── electron-to-chromium@1.4.815
│ │ ├── node-releases@2.0.14
│ │ └─┬ update-browserslist-db@1.0.16
│ │   ├── browserslist@4.23.1 deduped
│ │   ├── escalade@3.1.2
│ │   └── picocolors@1.0.1 deduped
│ ├── camelcase@6.3.0
│ ├── case-sensitive-paths-webpack-plugin@2.4.0
│ ├─┬ css-loader@6.11.0
│ │ ├── UNMET OPTIONAL DEPENDENCY @rspack/core@0.x || 1.x
│ │ ├─┬ icss-utils@5.1.0
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-modules-extract-imports@3.1.0
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-modules-local-by-default@4.0.5
│ │ │ ├── icss-utils@5.1.0 deduped
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-modules-scope@3.2.0
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-modules-values@4.0.0
│ │ │ ├── icss-utils@5.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├── postcss-value-parser@4.2.0
│ │ ├── postcss@8.4.39 deduped
│ │ ├── semver@7.6.2 deduped
│ │ └── webpack@5.92.1 deduped
│ ├─┬ css-minimizer-webpack-plugin@3.4.1
│ │ ├─┬ cssnano@5.1.15
│ │ │ ├─┬ cssnano-preset-default@5.2.14
│ │ │ │ ├─┬ css-declaration-sorter@6.4.1
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ cssnano-utils@3.1.0
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-calc@8.2.4
│ │ │ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-colormin@5.3.1
│ │ │ │ │ ├── browserslist@4.23.1 deduped
│ │ │ │ │ ├─┬ caniuse-api@3.0.0
│ │ │ │ │ │ ├── browserslist@4.23.1 deduped
│ │ │ │ │ │ ├── caniuse-lite@1.0.30001638 deduped
│ │ │ │ │ │ ├── lodash.memoize@4.1.2
│ │ │ │ │ │ └── lodash.uniq@4.5.0
│ │ │ │ │ ├── colord@2.9.3
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-convert-values@5.1.3
│ │ │ │ │ ├── browserslist@4.23.1 deduped
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-discard-comments@5.1.2
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-discard-duplicates@5.1.0
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-discard-empty@5.1.1
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-discard-overridden@5.1.0
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-merge-longhand@5.1.7
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ ├── postcss@8.4.39 deduped
│ │ │ │ │ └─┬ stylehacks@5.1.1
│ │ │ │ │   ├── browserslist@4.23.1 deduped
│ │ │ │ │   ├── postcss-selector-parser@6.1.0 deduped
│ │ │ │ │   └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-merge-rules@5.1.4
│ │ │ │ │ ├── browserslist@4.23.1 deduped
│ │ │ │ │ ├── caniuse-api@3.0.0 deduped
│ │ │ │ │ ├── cssnano-utils@3.1.0 deduped
│ │ │ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-minify-font-values@5.1.0
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-minify-gradients@5.1.1
│ │ │ │ │ ├── colord@2.9.3 deduped
│ │ │ │ │ ├── cssnano-utils@3.1.0 deduped
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-minify-params@5.1.4
│ │ │ │ │ ├── browserslist@4.23.1 deduped
│ │ │ │ │ ├── cssnano-utils@3.1.0 deduped
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-minify-selectors@5.2.1
│ │ │ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-normalize-charset@5.1.0
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-normalize-display-values@5.1.0
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-normalize-positions@5.1.1
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-normalize-repeat-style@5.1.1
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-normalize-string@5.1.0
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-normalize-timing-functions@5.1.0
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-normalize-unicode@5.1.1
│ │ │ │ │ ├── browserslist@4.23.1 deduped
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-normalize-url@5.1.0
│ │ │ │ │ ├── normalize-url@6.1.0
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-normalize-whitespace@5.1.1
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-ordered-values@5.1.3
│ │ │ │ │ ├── cssnano-utils@3.1.0 deduped
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-reduce-initial@5.1.2
│ │ │ │ │ ├── browserslist@4.23.1 deduped
│ │ │ │ │ ├── caniuse-api@3.0.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-reduce-transforms@5.1.0
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ ├─┬ postcss-svgo@5.1.0
│ │ │ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ │ │ ├── postcss@8.4.39 deduped
│ │ │ │ │ └─┬ svgo@2.8.0
│ │ │ │ │   ├── @trysound/sax@0.2.0
│ │ │ │ │   ├── commander@7.2.0
│ │ │ │ │   ├── css-select@4.3.0 deduped
│ │ │ │ │   ├─┬ css-tree@1.1.3
│ │ │ │ │   │ ├── mdn-data@2.0.14
│ │ │ │ │   │ └── source-map@0.6.1
│ │ │ │ │   ├── csso@4.2.0 deduped
│ │ │ │ │   ├── picocolors@1.0.1 deduped
│ │ │ │ │   └── stable@0.1.8 deduped
│ │ │ │ ├─┬ postcss-unique-selectors@5.1.1
│ │ │ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ │ │ └── postcss@8.4.39 deduped
│ │ │ │ └── postcss@8.4.39 deduped
│ │ │ ├── lilconfig@2.1.0 deduped
│ │ │ ├── postcss@8.4.39 deduped
│ │ │ └── yaml@1.10.2
│ │ ├─┬ jest-worker@27.5.1
│ │ │ ├── @types/node@20.14.9 deduped
│ │ │ ├── merge-stream@2.0.0
│ │ │ └─┬ supports-color@8.1.1
│ │ │   └── has-flag@4.0.0
│ │ ├── postcss@8.4.39 deduped
│ │ ├── schema-utils@4.2.0 deduped
│ │ ├─┬ serialize-javascript@6.0.2
│ │ │ └─┬ randombytes@2.1.0
│ │ │   └── safe-buffer@5.2.1 deduped
│ │ ├── source-map@0.6.1
│ │ └── webpack@5.92.1 deduped
│ ├── dotenv-expand@5.1.0
│ ├── dotenv@10.0.0
│ ├─┬ eslint-config-react-app@7.0.1
│ │ ├── @babel/core@7.24.7 deduped
│ │ ├─┬ @babel/eslint-parser@7.24.7
│ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ ├─┬ @nicolo-ribaudo/eslint-scope-5-internals@5.1.1-v1
│ │ │ │ └─┬ eslint-scope@5.1.1
│ │ │ │   ├── esrecurse@4.3.0 deduped
│ │ │ │   └── estraverse@4.3.0
│ │ │ ├── eslint-visitor-keys@2.1.0
│ │ │ ├── eslint@8.57.0 deduped
│ │ │ └── semver@6.3.1
│ │ ├── @rushstack/eslint-patch@1.10.3
│ │ ├─┬ @typescript-eslint/eslint-plugin@5.62.0
│ │ │ ├── @eslint-community/regexpp@4.11.0 deduped
│ │ │ ├── @typescript-eslint/parser@5.62.0 deduped
│ │ │ ├─┬ @typescript-eslint/scope-manager@5.62.0
│ │ │ │ ├── @typescript-eslint/types@5.62.0 deduped
│ │ │ │ └─┬ @typescript-eslint/visitor-keys@5.62.0
│ │ │ │   ├── @typescript-eslint/types@5.62.0 deduped
│ │ │ │   └── eslint-visitor-keys@3.4.3 deduped
│ │ │ ├─┬ @typescript-eslint/type-utils@5.62.0
│ │ │ │ ├── @typescript-eslint/typescript-estree@5.62.0 deduped
│ │ │ │ ├── @typescript-eslint/utils@5.62.0 deduped
│ │ │ │ ├── debug@4.3.5 deduped
│ │ │ │ ├── eslint@8.57.0 deduped
│ │ │ │ └── tsutils@3.21.0 deduped
│ │ │ ├─┬ @typescript-eslint/utils@5.62.0
│ │ │ │ ├── @eslint-community/eslint-utils@4.4.0 deduped
│ │ │ │ ├── @types/json-schema@7.0.15 deduped
│ │ │ │ ├── @types/semver@7.5.8
│ │ │ │ ├── @typescript-eslint/scope-manager@5.62.0 deduped
│ │ │ │ ├── @typescript-eslint/types@5.62.0 deduped
│ │ │ │ ├── @typescript-eslint/typescript-estree@5.62.0 deduped
│ │ │ │ ├─┬ eslint-scope@5.1.1
│ │ │ │ │ ├── esrecurse@4.3.0 deduped
│ │ │ │ │ └── estraverse@4.3.0
│ │ │ │ ├── eslint@8.57.0 deduped
│ │ │ │ └── semver@7.6.2 deduped
│ │ │ ├── debug@4.3.5 deduped
│ │ │ ├── eslint@8.57.0 deduped
│ │ │ ├── graphemer@1.4.0 deduped
│ │ │ ├── ignore@5.3.1 deduped
│ │ │ ├── natural-compare-lite@1.4.0
│ │ │ ├── semver@7.6.2 deduped
│ │ │ └─┬ tsutils@3.21.0
│ │ │   ├── tslib@1.14.1
│ │ │   └── typescript@4.9.5 deduped
│ │ ├─┬ @typescript-eslint/parser@5.62.0
│ │ │ ├── @typescript-eslint/scope-manager@5.62.0 deduped
│ │ │ ├── @typescript-eslint/types@5.62.0
│ │ │ ├─┬ @typescript-eslint/typescript-estree@5.62.0
│ │ │ │ ├── @typescript-eslint/types@5.62.0 deduped
│ │ │ │ ├── @typescript-eslint/visitor-keys@5.62.0 deduped
│ │ │ │ ├── debug@4.3.5 deduped
│ │ │ │ ├── globby@11.1.0 deduped
│ │ │ │ ├── is-glob@4.0.3 deduped
│ │ │ │ ├── semver@7.6.2 deduped
│ │ │ │ └── tsutils@3.21.0 deduped
│ │ │ ├── debug@4.3.5 deduped
│ │ │ └── eslint@8.57.0 deduped
│ │ ├── babel-preset-react-app@10.0.1 deduped
│ │ ├── confusing-browser-globals@1.0.11
│ │ ├─┬ eslint-plugin-flowtype@8.0.3
│ │ │ ├── @babel/plugin-syntax-flow@7.24.7 deduped
│ │ │ ├── @babel/plugin-transform-react-jsx@7.24.7 deduped
│ │ │ ├── eslint@8.57.0 deduped
│ │ │ ├── lodash@4.17.21 deduped
│ │ │ └── string-natural-compare@3.0.1
│ │ ├─┬ eslint-plugin-import@2.29.1
│ │ │ ├─┬ array-includes@3.1.8
│ │ │ │ ├─┬ call-bind@1.0.7
│ │ │ │ │ ├─┬ es-define-property@1.0.0
│ │ │ │ │ │ └── get-intrinsic@1.2.4 deduped
│ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ ├── function-bind@1.1.2 deduped
│ │ │ │ │ ├── get-intrinsic@1.2.4 deduped
│ │ │ │ │ └─┬ set-function-length@1.2.2
│ │ │ │ │   ├── define-data-property@1.1.4 deduped
│ │ │ │ │   ├── es-errors@1.3.0 deduped
│ │ │ │ │   ├── function-bind@1.1.2 deduped
│ │ │ │ │   ├── get-intrinsic@1.2.4 deduped
│ │ │ │ │   ├── gopd@1.0.1 deduped
│ │ │ │ │   └── has-property-descriptors@1.0.2 deduped
│ │ │ │ ├─┬ define-properties@1.2.1
│ │ │ │ │ ├─┬ define-data-property@1.1.4
│ │ │ │ │ │ ├── es-define-property@1.0.0 deduped
│ │ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ │ └── gopd@1.0.1 deduped
│ │ │ │ │ ├── has-property-descriptors@1.0.2 deduped
│ │ │ │ │ └── object-keys@1.1.1
│ │ │ │ ├─┬ es-abstract@1.23.3
│ │ │ │ │ ├─┬ array-buffer-byte-length@1.0.1
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ └── is-array-buffer@3.0.4 deduped
│ │ │ │ │ ├─┬ arraybuffer.prototype.slice@1.0.3
│ │ │ │ │ │ ├── array-buffer-byte-length@1.0.1 deduped
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ │ ├── get-intrinsic@1.2.4 deduped
│ │ │ │ │ │ ├── is-array-buffer@3.0.4 deduped
│ │ │ │ │ │ └── is-shared-array-buffer@1.0.3 deduped
│ │ │ │ │ ├─┬ available-typed-arrays@1.0.7
│ │ │ │ │ │ └── possible-typed-array-names@1.0.0
│ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ ├─┬ data-view-buffer@1.0.1
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ │ └── is-data-view@1.0.1 deduped
│ │ │ │ │ ├─┬ data-view-byte-length@1.0.1
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ │ └── is-data-view@1.0.1 deduped
│ │ │ │ │ ├─┬ data-view-byte-offset@1.0.0
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ │ └── is-data-view@1.0.1 deduped
│ │ │ │ │ ├── es-define-property@1.0.0 deduped
│ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ ├── es-object-atoms@1.0.0 deduped
│ │ │ │ │ ├── es-set-tostringtag@2.0.3 deduped
│ │ │ │ │ ├─┬ es-to-primitive@1.2.1
│ │ │ │ │ │ ├── is-callable@1.2.7 deduped
│ │ │ │ │ │ ├── is-date-object@1.0.5 deduped
│ │ │ │ │ │ └─┬ is-symbol@1.0.4
│ │ │ │ │ │   └── has-symbols@1.0.3 deduped
│ │ │ │ │ ├─┬ function.prototype.name@1.1.6
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ │ │ └── functions-have-names@1.2.3 deduped
│ │ │ │ │ ├── get-intrinsic@1.2.4 deduped
│ │ │ │ │ ├─┬ get-symbol-description@1.0.2
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ │ └── get-intrinsic@1.2.4 deduped
│ │ │ │ │ ├── globalthis@1.0.4 deduped
│ │ │ │ │ ├── gopd@1.0.1 deduped
│ │ │ │ │ ├── has-property-descriptors@1.0.2 deduped
│ │ │ │ │ ├── has-proto@1.0.3 deduped
│ │ │ │ │ ├── has-symbols@1.0.3 deduped
│ │ │ │ │ ├── hasown@2.0.2 deduped
│ │ │ │ │ ├── internal-slot@1.0.7 deduped
│ │ │ │ │ ├─┬ is-array-buffer@3.0.4
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ └── get-intrinsic@1.2.4 deduped
│ │ │ │ │ ├── is-callable@1.2.7
│ │ │ │ │ ├─┬ is-data-view@1.0.1
│ │ │ │ │ │ └── is-typed-array@1.1.13 deduped
│ │ │ │ │ ├── is-negative-zero@2.0.3
│ │ │ │ │ ├── is-regex@1.1.4 deduped
│ │ │ │ │ ├─┬ is-shared-array-buffer@1.0.3
│ │ │ │ │ │ └── call-bind@1.0.7 deduped
│ │ │ │ │ ├── is-string@1.0.7 deduped
│ │ │ │ │ ├─┬ is-typed-array@1.1.13
│ │ │ │ │ │ └── which-typed-array@1.1.15 deduped
│ │ │ │ │ ├─┬ is-weakref@1.0.2
│ │ │ │ │ │ └── call-bind@1.0.7 deduped
│ │ │ │ │ ├── object-inspect@1.13.2
│ │ │ │ │ ├── object-keys@1.1.1 deduped
│ │ │ │ │ ├── object.assign@4.1.5 deduped
│ │ │ │ │ ├── regexp.prototype.flags@1.5.2 deduped
│ │ │ │ │ ├── safe-array-concat@1.1.2 deduped
│ │ │ │ │ ├── safe-regex-test@1.0.3 deduped
│ │ │ │ │ ├─┬ string.prototype.trim@1.2.9
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ │ │ └── es-object-atoms@1.0.0 deduped
│ │ │ │ │ ├─┬ string.prototype.trimend@1.0.8
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ │ │ └── es-object-atoms@1.0.0 deduped
│ │ │ │ │ ├─┬ string.prototype.trimstart@1.0.8
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ │ │ └── es-object-atoms@1.0.0 deduped
│ │ │ │ │ ├─┬ typed-array-buffer@1.0.2
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ │ └── is-typed-array@1.1.13 deduped
│ │ │ │ │ ├─┬ typed-array-byte-length@1.0.1
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├─┬ for-each@0.3.3
│ │ │ │ │ │ │ └── is-callable@1.2.7 deduped
│ │ │ │ │ │ ├── gopd@1.0.1 deduped
│ │ │ │ │ │ ├── has-proto@1.0.3 deduped
│ │ │ │ │ │ └── is-typed-array@1.1.13 deduped
│ │ │ │ │ ├─┬ typed-array-byte-offset@1.0.2
│ │ │ │ │ │ ├── available-typed-arrays@1.0.7 deduped
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── for-each@0.3.3 deduped
│ │ │ │ │ │ ├── gopd@1.0.1 deduped
│ │ │ │ │ │ ├── has-proto@1.0.3 deduped
│ │ │ │ │ │ └── is-typed-array@1.1.13 deduped
│ │ │ │ │ ├─┬ typed-array-length@1.0.6
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── for-each@0.3.3 deduped
│ │ │ │ │ │ ├── gopd@1.0.1 deduped
│ │ │ │ │ │ ├── has-proto@1.0.3 deduped
│ │ │ │ │ │ ├── is-typed-array@1.1.13 deduped
│ │ │ │ │ │ └── possible-typed-array-names@1.0.0 deduped
│ │ │ │ │ ├─┬ unbox-primitive@1.0.2
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── has-bigints@1.0.2
│ │ │ │ │ │ ├── has-symbols@1.0.3 deduped
│ │ │ │ │ │ └── which-boxed-primitive@1.0.2 deduped
│ │ │ │ │ └─┬ which-typed-array@1.1.15
│ │ │ │ │   ├── available-typed-arrays@1.0.7 deduped
│ │ │ │ │   ├── call-bind@1.0.7 deduped
│ │ │ │ │   ├── for-each@0.3.3 deduped
│ │ │ │ │   ├── gopd@1.0.1 deduped
│ │ │ │ │   └── has-tostringtag@1.0.2 deduped
│ │ │ │ ├─┬ es-object-atoms@1.0.0
│ │ │ │ │ └── es-errors@1.3.0 deduped
│ │ │ │ ├─┬ get-intrinsic@1.2.4
│ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ ├── function-bind@1.1.2 deduped
│ │ │ │ │ ├── has-proto@1.0.3 deduped
│ │ │ │ │ ├── has-symbols@1.0.3 deduped
│ │ │ │ │ └── hasown@2.0.2 deduped
│ │ │ │ └─┬ is-string@1.0.7
│ │ │ │   └─┬ has-tostringtag@1.0.2
│ │ │ │     └── has-symbols@1.0.3 deduped
│ │ │ ├─┬ array.prototype.findlastindex@1.2.5
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ ├── es-errors@1.3.0
│ │ │ │ ├── es-object-atoms@1.0.0 deduped
│ │ │ │ └─┬ es-shim-unscopables@1.0.2
│ │ │ │   └── hasown@2.0.2 deduped
│ │ │ ├─┬ array.prototype.flat@1.3.2
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ └── es-shim-unscopables@1.0.2 deduped
│ │ │ ├─┬ array.prototype.flatmap@1.3.2
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ └── es-shim-unscopables@1.0.2 deduped
│ │ │ ├─┬ debug@3.2.7
│ │ │ │ └── ms@2.1.2 deduped
│ │ │ ├─┬ doctrine@2.1.0
│ │ │ │ └── esutils@2.0.3 deduped
│ │ │ ├─┬ eslint-import-resolver-node@0.3.9
│ │ │ │ ├─┬ debug@3.2.7
│ │ │ │ │ └── ms@2.1.2 deduped
│ │ │ │ ├── is-core-module@2.14.0 deduped
│ │ │ │ └── resolve@1.22.8 deduped
│ │ │ ├─┬ eslint-module-utils@2.8.1
│ │ │ │ └─┬ debug@3.2.7
│ │ │ │   └── ms@2.1.2 deduped
│ │ │ ├── eslint@8.57.0 deduped
│ │ │ ├─┬ hasown@2.0.2
│ │ │ │ └── function-bind@1.1.2
│ │ │ ├── is-core-module@2.14.0 deduped
│ │ │ ├── is-glob@4.0.3 deduped
│ │ │ ├── minimatch@3.1.2 deduped
│ │ │ ├─┬ object.fromentries@2.0.8
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ └── es-object-atoms@1.0.0 deduped
│ │ │ ├─┬ object.groupby@1.0.3
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ └── es-abstract@1.23.3 deduped
│ │ │ ├─┬ object.values@1.2.0
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ └── es-object-atoms@1.0.0 deduped
│ │ │ ├── semver@6.3.1
│ │ │ └─┬ tsconfig-paths@3.15.0
│ │ │   ├── @types/json5@0.0.29
│ │ │   ├─┬ json5@1.0.2
│ │ │   │ └── minimist@1.2.8 deduped
│ │ │   ├── minimist@1.2.8
│ │ │   └── strip-bom@3.0.0
│ │ ├─┬ eslint-plugin-jest@25.7.0
│ │ │ ├── @typescript-eslint/eslint-plugin@5.62.0 deduped
│ │ │ ├─┬ @typescript-eslint/experimental-utils@5.62.0
│ │ │ │ ├── @typescript-eslint/utils@5.62.0 deduped
│ │ │ │ └── eslint@8.57.0 deduped
│ │ │ └── eslint@8.57.0 deduped
│ │ ├─┬ eslint-plugin-jsx-a11y@6.9.0
│ │ │ ├─┬ aria-query@5.1.3
│ │ │ │ └─┬ deep-equal@2.2.3
│ │ │ │   ├── array-buffer-byte-length@1.0.1 deduped
│ │ │ │   ├── call-bind@1.0.7 deduped
│ │ │ │   ├─┬ es-get-iterator@1.1.3
│ │ │ │   │ ├── call-bind@1.0.7 deduped
│ │ │ │   │ ├── get-intrinsic@1.2.4 deduped
│ │ │ │   │ ├── has-symbols@1.0.3 deduped
│ │ │ │   │ ├── is-arguments@1.1.1 deduped
│ │ │ │   │ ├── is-map@2.0.3
│ │ │ │   │ ├── is-set@2.0.3
│ │ │ │   │ ├── is-string@1.0.7 deduped
│ │ │ │   │ ├── isarray@2.0.5 deduped
│ │ │ │   │ └─┬ stop-iteration-iterator@1.0.0
│ │ │ │   │   └── internal-slot@1.0.7 deduped
│ │ │ │   ├── get-intrinsic@1.2.4 deduped
│ │ │ │   ├─┬ is-arguments@1.1.1
│ │ │ │   │ ├── call-bind@1.0.7 deduped
│ │ │ │   │ └── has-tostringtag@1.0.2 deduped
│ │ │ │   ├── is-array-buffer@3.0.4 deduped
│ │ │ │   ├─┬ is-date-object@1.0.5
│ │ │ │   │ └── has-tostringtag@1.0.2 deduped
│ │ │ │   ├── is-regex@1.1.4 deduped
│ │ │ │   ├── is-shared-array-buffer@1.0.3 deduped
│ │ │ │   ├── isarray@2.0.5
│ │ │ │   ├─┬ object-is@1.1.6
│ │ │ │   │ ├── call-bind@1.0.7 deduped
│ │ │ │   │ └── define-properties@1.2.1 deduped
│ │ │ │   ├── object-keys@1.1.1 deduped
│ │ │ │   ├── object.assign@4.1.5 deduped
│ │ │ │   ├── regexp.prototype.flags@1.5.2 deduped
│ │ │ │   ├── side-channel@1.0.6 deduped
│ │ │ │   ├─┬ which-boxed-primitive@1.0.2
│ │ │ │   │ ├─┬ is-bigint@1.0.4
│ │ │ │   │ │ └── has-bigints@1.0.2 deduped
│ │ │ │   │ ├─┬ is-boolean-object@1.1.2
│ │ │ │   │ │ ├── call-bind@1.0.7 deduped
│ │ │ │   │ │ └── has-tostringtag@1.0.2 deduped
│ │ │ │   │ ├─┬ is-number-object@1.0.7
│ │ │ │   │ │ └── has-tostringtag@1.0.2 deduped
│ │ │ │   │ ├── is-string@1.0.7 deduped
│ │ │ │   │ └── is-symbol@1.0.4 deduped
│ │ │ │   ├─┬ which-collection@1.0.2
│ │ │ │   │ ├── is-map@2.0.3 deduped
│ │ │ │   │ ├── is-set@2.0.3 deduped
│ │ │ │   │ ├── is-weakmap@2.0.2
│ │ │ │   │ └─┬ is-weakset@2.0.3
│ │ │ │   │   ├── call-bind@1.0.7 deduped
│ │ │ │   │   └── get-intrinsic@1.2.4 deduped
│ │ │ │   └── which-typed-array@1.1.15 deduped
│ │ │ ├── array-includes@3.1.8 deduped
│ │ │ ├── array.prototype.flatmap@1.3.2 deduped
│ │ │ ├── ast-types-flow@0.0.8
│ │ │ ├── axe-core@4.9.1
│ │ │ ├─┬ axobject-query@3.1.1
│ │ │ │ └── deep-equal@2.2.3 deduped
│ │ │ ├── damerau-levenshtein@1.0.8
│ │ │ ├── emoji-regex@9.2.2
│ │ │ ├─┬ es-iterator-helpers@1.0.19
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ ├─┬ es-set-tostringtag@2.0.3
│ │ │ │ │ ├── get-intrinsic@1.2.4 deduped
│ │ │ │ │ ├── has-tostringtag@1.0.2 deduped
│ │ │ │ │ └── hasown@2.0.2 deduped
│ │ │ │ ├── function-bind@1.1.2 deduped
│ │ │ │ ├── get-intrinsic@1.2.4 deduped
│ │ │ │ ├─┬ globalthis@1.0.4
│ │ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ │ └── gopd@1.0.1 deduped
│ │ │ │ ├─┬ has-property-descriptors@1.0.2
│ │ │ │ │ └── es-define-property@1.0.0 deduped
│ │ │ │ ├── has-proto@1.0.3
│ │ │ │ ├── has-symbols@1.0.3
│ │ │ │ ├─┬ internal-slot@1.0.7
│ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ ├── hasown@2.0.2 deduped
│ │ │ │ │ └── side-channel@1.0.6 deduped
│ │ │ │ ├─┬ iterator.prototype@1.1.2
│ │ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ │ ├── get-intrinsic@1.2.4 deduped
│ │ │ │ │ ├── has-symbols@1.0.3 deduped
│ │ │ │ │ ├─┬ reflect.getprototypeof@1.0.6
│ │ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ │ │ ├── get-intrinsic@1.2.4 deduped
│ │ │ │ │ │ ├── globalthis@1.0.4 deduped
│ │ │ │ │ │ └─┬ which-builtin-type@1.1.3
│ │ │ │ │ │   ├── function.prototype.name@1.1.6 deduped
│ │ │ │ │ │   ├── has-tostringtag@1.0.2 deduped
│ │ │ │ │ │   ├─┬ is-async-function@2.0.0
│ │ │ │ │ │   │ └── has-tostringtag@1.0.2 deduped
│ │ │ │ │ │   ├── is-date-object@1.0.5 deduped
│ │ │ │ │ │   ├─┬ is-finalizationregistry@1.0.2
│ │ │ │ │ │   │ └── call-bind@1.0.7 deduped
│ │ │ │ │ │   ├─┬ is-generator-function@1.0.10
│ │ │ │ │ │   │ └── has-tostringtag@1.0.2 deduped
│ │ │ │ │ │   ├── is-regex@1.1.4 deduped
│ │ │ │ │ │   ├── is-weakref@1.0.2 deduped
│ │ │ │ │ │   ├── isarray@2.0.5 deduped
│ │ │ │ │ │   ├── which-boxed-primitive@1.0.2 deduped
│ │ │ │ │ │   ├── which-collection@1.0.2 deduped
│ │ │ │ │ │   └── which-typed-array@1.1.15 deduped
│ │ │ │ │ └── set-function-name@2.0.2 deduped
│ │ │ │ └─┬ safe-array-concat@1.1.2
│ │ │ │   ├── call-bind@1.0.7 deduped
│ │ │ │   ├── get-intrinsic@1.2.4 deduped
│ │ │ │   ├── has-symbols@1.0.3 deduped
│ │ │ │   └── isarray@2.0.5 deduped
│ │ │ ├── eslint@8.57.0 deduped
│ │ │ ├── hasown@2.0.2 deduped
│ │ │ ├─┬ jsx-ast-utils@3.3.5
│ │ │ │ ├── array-includes@3.1.8 deduped
│ │ │ │ ├── array.prototype.flat@1.3.2 deduped
│ │ │ │ ├─┬ object.assign@4.1.5
│ │ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ │ ├── has-symbols@1.0.3 deduped
│ │ │ │ │ └── object-keys@1.1.1 deduped
│ │ │ │ └── object.values@1.2.0 deduped
│ │ │ ├─┬ language-tags@1.0.9
│ │ │ │ └── language-subtag-registry@0.3.23
│ │ │ ├── minimatch@3.1.2 deduped
│ │ │ ├── object.fromentries@2.0.8 deduped
│ │ │ ├─┬ safe-regex-test@1.0.3
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ └─┬ is-regex@1.1.4
│ │ │ │   ├── call-bind@1.0.7 deduped
│ │ │ │   └── has-tostringtag@1.0.2 deduped
│ │ │ └─┬ string.prototype.includes@2.0.0
│ │ │   ├── define-properties@1.2.1 deduped
│ │ │   └── es-abstract@1.23.3 deduped
│ │ ├─┬ eslint-plugin-react-hooks@4.6.2
│ │ │ └── eslint@8.57.0 deduped
│ │ ├─┬ eslint-plugin-react@7.34.3
│ │ │ ├── array-includes@3.1.8 deduped
│ │ │ ├─┬ array.prototype.findlast@1.2.5
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ ├── es-object-atoms@1.0.0 deduped
│ │ │ │ └── es-shim-unscopables@1.0.2 deduped
│ │ │ ├── array.prototype.flatmap@1.3.2 deduped
│ │ │ ├─┬ array.prototype.toreversed@1.1.2
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ └── es-shim-unscopables@1.0.2 deduped
│ │ │ ├─┬ array.prototype.tosorted@1.1.4
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ ├── es-errors@1.3.0 deduped
│ │ │ │ └── es-shim-unscopables@1.0.2 deduped
│ │ │ ├─┬ doctrine@2.1.0
│ │ │ │ └── esutils@2.0.3 deduped
│ │ │ ├── es-iterator-helpers@1.0.19 deduped
│ │ │ ├── eslint@8.57.0 deduped
│ │ │ ├── estraverse@5.3.0
│ │ │ ├── jsx-ast-utils@3.3.5 deduped
│ │ │ ├── minimatch@3.1.2 deduped
│ │ │ ├─┬ object.entries@1.1.8
│ │ │ │ ├── call-bind@1.0.7 deduped
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ └── es-object-atoms@1.0.0 deduped
│ │ │ ├── object.fromentries@2.0.8 deduped
│ │ │ ├─┬ object.hasown@1.1.4
│ │ │ │ ├── define-properties@1.2.1 deduped
│ │ │ │ ├── es-abstract@1.23.3 deduped
│ │ │ │ └── es-object-atoms@1.0.0 deduped
│ │ │ ├── object.values@1.2.0 deduped
│ │ │ ├── prop-types@15.8.1 deduped
│ │ │ ├─┬ resolve@2.0.0-next.5
│ │ │ │ ├── is-core-module@2.14.0 deduped
│ │ │ │ ├── path-parse@1.0.7 deduped
│ │ │ │ └── supports-preserve-symlinks-flag@1.0.0 deduped
│ │ │ ├── semver@6.3.1
│ │ │ └─┬ string.prototype.matchall@4.0.11
│ │ │   ├── call-bind@1.0.7 deduped
│ │ │   ├── define-properties@1.2.1 deduped
│ │ │   ├── es-abstract@1.23.3 deduped
│ │ │   ├── es-errors@1.3.0 deduped
│ │ │   ├── es-object-atoms@1.0.0 deduped
│ │ │   ├── get-intrinsic@1.2.4 deduped
│ │ │   ├─┬ gopd@1.0.1
│ │ │   │ └── get-intrinsic@1.2.4 deduped
│ │ │   ├── has-symbols@1.0.3 deduped
│ │ │   ├── internal-slot@1.0.7 deduped
│ │ │   ├─┬ regexp.prototype.flags@1.5.2
│ │ │   │ ├── call-bind@1.0.7 deduped
│ │ │   │ ├── define-properties@1.2.1 deduped
│ │ │   │ ├── es-errors@1.3.0 deduped
│ │ │   │ └── set-function-name@2.0.2 deduped
│ │ │   ├─┬ set-function-name@2.0.2
│ │ │   │ ├── define-data-property@1.1.4 deduped
│ │ │   │ ├── es-errors@1.3.0 deduped
│ │ │   │ ├── functions-have-names@1.2.3
│ │ │   │ └── has-property-descriptors@1.0.2 deduped
│ │ │   └─┬ side-channel@1.0.6
│ │ │     ├── call-bind@1.0.7 deduped
│ │ │     ├── es-errors@1.3.0 deduped
│ │ │     ├── get-intrinsic@1.2.4 deduped
│ │ │     └── object-inspect@1.13.2 deduped
│ │ ├─┬ eslint-plugin-testing-library@5.11.1
│ │ │ ├── @typescript-eslint/utils@5.62.0 deduped
│ │ │ └── eslint@8.57.0 deduped
│ │ └── eslint@8.57.0 deduped
│ ├─┬ eslint-webpack-plugin@3.2.0
│ │ ├─┬ @types/eslint@8.56.10
│ │ │ ├── @types/estree@1.0.5 deduped
│ │ │ └── @types/json-schema@7.0.15 deduped
│ │ ├── eslint@8.57.0 deduped
│ │ ├─┬ jest-worker@28.1.3
│ │ │ ├── @types/node@20.14.9 deduped
│ │ │ ├── merge-stream@2.0.0 deduped
│ │ │ └─┬ supports-color@8.1.1
│ │ │   └── has-flag@4.0.0
│ │ ├─┬ micromatch@4.0.7
│ │ │ ├─┬ braces@3.0.3
│ │ │ │ └─┬ fill-range@7.1.1
│ │ │ │   └─┬ to-regex-range@5.0.1
│ │ │ │     └── is-number@7.0.0
│ │ │ └── picomatch@2.3.1
│ │ ├── normalize-path@3.0.0
│ │ ├── schema-utils@4.2.0 deduped
│ │ └── webpack@5.92.1 deduped
│ ├─┬ eslint@8.57.0
│ │ ├─┬ @eslint-community/eslint-utils@4.4.0
│ │ │ ├── eslint-visitor-keys@3.4.3 deduped
│ │ │ └── eslint@8.57.0 deduped
│ │ ├── @eslint-community/regexpp@4.11.0
│ │ ├─┬ @eslint/eslintrc@2.1.4
│ │ │ ├── ajv@6.12.6 deduped
│ │ │ ├── debug@4.3.5 deduped
│ │ │ ├── espree@9.6.1 deduped
│ │ │ ├─┬ globals@13.24.0
│ │ │ │ └── type-fest@0.20.2
│ │ │ ├── ignore@5.3.1 deduped
│ │ │ ├─┬ import-fresh@3.3.0
│ │ │ │ ├─┬ parent-module@1.0.1
│ │ │ │ │ └── callsites@3.1.0
│ │ │ │ └── resolve-from@4.0.0
│ │ │ ├─┬ js-yaml@4.1.0
│ │ │ │ └── argparse@2.0.1
│ │ │ ├── minimatch@3.1.2 deduped
│ │ │ └── strip-json-comments@3.1.1
│ │ ├── @eslint/js@8.57.0
│ │ ├─┬ @humanwhocodes/config-array@0.11.14
│ │ │ ├── @humanwhocodes/object-schema@2.0.3
│ │ │ ├── debug@4.3.5 deduped
│ │ │ └── minimatch@3.1.2 deduped
│ │ ├── @humanwhocodes/module-importer@1.0.1
│ │ ├─┬ @nodelib/fs.walk@1.2.8
│ │ │ ├─┬ @nodelib/fs.scandir@2.1.5
│ │ │ │ ├── @nodelib/fs.stat@2.0.5 deduped
│ │ │ │ └─┬ run-parallel@1.2.0
│ │ │ │   └── queue-microtask@1.2.3
│ │ │ └─┬ fastq@1.17.1
│ │ │   └── reusify@1.0.4
│ │ ├── @ungap/structured-clone@1.2.0
│ │ ├─┬ ajv@6.12.6
│ │ │ ├── fast-deep-equal@3.1.3 deduped
│ │ │ ├── fast-json-stable-stringify@2.1.0 deduped
│ │ │ ├── json-schema-traverse@0.4.1
│ │ │ └─┬ uri-js@4.4.1
│ │ │   └── punycode@2.3.1
│ │ ├─┬ chalk@4.1.2
│ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │   └── color-name@1.1.4
│ │ │ └─┬ supports-color@7.2.0
│ │ │   └── has-flag@4.0.0
│ │ ├─┬ cross-spawn@7.0.3
│ │ │ ├── path-key@3.1.1
│ │ │ ├─┬ shebang-command@2.0.0
│ │ │ │ └── shebang-regex@3.0.0
│ │ │ └─┬ which@2.0.2
│ │ │   └── isexe@2.0.0
│ │ ├── debug@4.3.5 deduped
│ │ ├─┬ doctrine@3.0.0
│ │ │ └── esutils@2.0.3 deduped
│ │ ├── escape-string-regexp@4.0.0
│ │ ├─┬ eslint-scope@7.2.2
│ │ │ ├─┬ esrecurse@4.3.0
│ │ │ │ └── estraverse@5.3.0 deduped
│ │ │ └── estraverse@5.3.0 deduped
│ │ ├── eslint-visitor-keys@3.4.3
│ │ ├─┬ espree@9.6.1
│ │ │ ├─┬ acorn-jsx@5.3.2
│ │ │ │ └── acorn@8.12.0 deduped
│ │ │ ├── acorn@8.12.0 deduped
│ │ │ └── eslint-visitor-keys@3.4.3 deduped
│ │ ├─┬ esquery@1.5.0
│ │ │ └── estraverse@5.3.0 deduped
│ │ ├── esutils@2.0.3
│ │ ├── fast-deep-equal@3.1.3
│ │ ├─┬ file-entry-cache@6.0.1
│ │ │ └─┬ flat-cache@3.2.0
│ │ │   ├── flatted@3.3.1
│ │ │   ├─┬ keyv@4.5.4
│ │ │   │ └── json-buffer@3.0.1
│ │ │   └── rimraf@3.0.2 deduped
│ │ ├─┬ find-up@5.0.0
│ │ │ ├─┬ locate-path@6.0.0
│ │ │ │ └─┬ p-locate@5.0.0
│ │ │ │   └─┬ p-limit@3.1.0
│ │ │ │     └── yocto-queue@0.1.0
│ │ │ └── path-exists@4.0.0
│ │ ├─┬ glob-parent@6.0.2
│ │ │ └── is-glob@4.0.3 deduped
│ │ ├─┬ globals@13.24.0
│ │ │ └── type-fest@0.20.2
│ │ ├── graphemer@1.4.0
│ │ ├── ignore@5.3.1
│ │ ├── imurmurhash@0.1.4
│ │ ├─┬ is-glob@4.0.3
│ │ │ └── is-extglob@2.1.1
│ │ ├── is-path-inside@3.0.3
│ │ ├─┬ js-yaml@4.1.0
│ │ │ └── argparse@2.0.1
│ │ ├── json-stable-stringify-without-jsonify@1.0.1
│ │ ├─┬ levn@0.4.1
│ │ │ ├── prelude-ls@1.2.1
│ │ │ └─┬ type-check@0.4.0
│ │ │   └── prelude-ls@1.2.1 deduped
│ │ ├── lodash.merge@4.6.2
│ │ ├─┬ minimatch@3.1.2
│ │ │ └─┬ brace-expansion@1.1.11
│ │ │   ├── balanced-match@1.0.2
│ │ │   └── concat-map@0.0.1
│ │ ├── natural-compare@1.4.0
│ │ ├─┬ optionator@0.9.4
│ │ │ ├── deep-is@0.1.4
│ │ │ ├── fast-levenshtein@2.0.6
│ │ │ ├── levn@0.4.1 deduped
│ │ │ ├── prelude-ls@1.2.1 deduped
│ │ │ ├── type-check@0.4.0 deduped
│ │ │ └── word-wrap@1.2.5
│ │ ├─┬ strip-ansi@6.0.1
│ │ │ └── ansi-regex@5.0.1
│ │ └── text-table@0.2.0
│ ├─┬ file-loader@6.2.0
│ │ ├── loader-utils@2.0.4 deduped
│ │ ├─┬ schema-utils@3.3.0
│ │ │ ├── @types/json-schema@7.0.15 deduped
│ │ │ ├── ajv-keywords@3.5.2 deduped
│ │ │ └── ajv@6.12.6 deduped
│ │ └── webpack@5.92.1 deduped
│ ├─┬ fs-extra@10.1.0
│ │ ├── graceful-fs@4.2.11 deduped
│ │ ├─┬ jsonfile@6.1.0
│ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ └── universalify@2.0.1 deduped
│ │ └── universalify@2.0.1
│ ├── UNMET OPTIONAL DEPENDENCY fsevents@^2.3.2
│ ├─┬ html-webpack-plugin@5.6.0
│ │ ├── UNMET OPTIONAL DEPENDENCY @rspack/core@0.x || 1.x
│ │ ├── @types/html-minifier-terser@6.1.0
│ │ ├─┬ html-minifier-terser@6.1.0
│ │ │ ├─┬ camel-case@4.1.2
│ │ │ │ ├─┬ pascal-case@3.1.2
│ │ │ │ │ ├─┬ no-case@3.0.4
│ │ │ │ │ │ ├─┬ lower-case@2.0.2
│ │ │ │ │ │ │ └── tslib@2.6.3 deduped
│ │ │ │ │ │ └── tslib@2.6.3 deduped
│ │ │ │ │ └── tslib@2.6.3 deduped
│ │ │ │ └── tslib@2.6.3
│ │ │ ├─┬ clean-css@5.3.3
│ │ │ │ └── source-map@0.6.1
│ │ │ ├── commander@8.3.0
│ │ │ ├── he@1.2.0
│ │ │ ├─┬ param-case@3.0.4
│ │ │ │ ├─┬ dot-case@3.0.4
│ │ │ │ │ ├── no-case@3.0.4 deduped
│ │ │ │ │ └── tslib@2.6.3 deduped
│ │ │ │ └── tslib@2.6.3 deduped
│ │ │ ├── relateurl@0.2.7
│ │ │ └── terser@5.31.1 deduped
│ │ ├── lodash@4.17.21
│ │ ├─┬ pretty-error@4.0.0
│ │ │ ├── lodash@4.17.21 deduped
│ │ │ └─┬ renderkid@3.0.0
│ │ │   ├─┬ css-select@4.3.0
│ │ │   │ ├── boolbase@1.0.0 deduped
│ │ │   │ ├── css-what@6.1.0
│ │ │   │ ├─┬ domhandler@4.3.1
│ │ │   │ │ └── domelementtype@2.3.0 deduped
│ │ │   │ ├─┬ domutils@2.8.0
│ │ │   │ │ ├─┬ dom-serializer@1.4.1
│ │ │   │ │ │ ├── domelementtype@2.3.0 deduped
│ │ │   │ │ │ ├── domhandler@4.3.1 deduped
│ │ │   │ │ │ └── entities@2.2.0 deduped
│ │ │   │ │ ├── domelementtype@2.3.0 deduped
│ │ │   │ │ └── domhandler@4.3.1 deduped
│ │ │   │ └─┬ nth-check@2.1.1
│ │ │   │   └── boolbase@1.0.0 deduped
│ │ │   ├─┬ dom-converter@0.2.0
│ │ │   │ └── utila@0.4.0
│ │ │   ├─┬ htmlparser2@6.1.0
│ │ │   │ ├── domelementtype@2.3.0
│ │ │   │ ├── domhandler@4.3.1 deduped
│ │ │   │ ├── domutils@2.8.0 deduped
│ │ │   │ └── entities@2.2.0
│ │ │   ├── lodash@4.17.21 deduped
│ │ │   └── strip-ansi@6.0.1 deduped
│ │ ├── tapable@2.2.1
│ │ └── webpack@5.92.1 deduped
│ ├─┬ identity-obj-proxy@3.0.0
│ │ └── harmony-reflect@1.6.2
│ ├─┬ jest-resolve@27.5.1
│ │ ├── @jest/types@27.5.1 deduped
│ │ ├─┬ chalk@4.1.2
│ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │   └── color-name@1.1.4
│ │ │ └─┬ supports-color@7.2.0
│ │ │   └── has-flag@4.0.0
│ │ ├── graceful-fs@4.2.11 deduped
│ │ ├─┬ jest-haste-map@27.5.1
│ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ ├─┬ @types/graceful-fs@4.1.9
│ │ │ │ └── @types/node@20.14.9 deduped
│ │ │ ├── @types/node@20.14.9 deduped
│ │ │ ├─┬ anymatch@3.1.3
│ │ │ │ ├── normalize-path@3.0.0 deduped
│ │ │ │ └── picomatch@2.3.1 deduped
│ │ │ ├─┬ fb-watchman@2.0.2
│ │ │ │ └─┬ bser@2.1.1
│ │ │ │   └── node-int64@0.4.0
│ │ │ ├── UNMET OPTIONAL DEPENDENCY fsevents@^2.3.2
│ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ ├── jest-regex-util@27.5.1 deduped
│ │ │ ├─┬ jest-serializer@27.5.1
│ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ └── graceful-fs@4.2.11 deduped
│ │ │ ├── jest-util@27.5.1 deduped
│ │ │ ├── jest-worker@27.5.1 deduped
│ │ │ ├── micromatch@4.0.7 deduped
│ │ │ └─┬ walker@1.0.8
│ │ │   └─┬ makeerror@1.0.12
│ │ │     └── tmpl@1.0.5
│ │ ├─┬ jest-pnp-resolver@1.2.3
│ │ │ └── jest-resolve@27.5.1 deduped
│ │ ├─┬ jest-util@27.5.1
│ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ ├── @types/node@20.14.9 deduped
│ │ │ ├─┬ chalk@4.1.2
│ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │   └── color-name@1.1.4
│ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │   └── has-flag@4.0.0
│ │ │ ├── ci-info@3.9.0
│ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ └── picomatch@2.3.1 deduped
│ │ ├─┬ jest-validate@27.5.1
│ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ ├── camelcase@6.3.0 deduped
│ │ │ ├─┬ chalk@4.1.2
│ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │   └── color-name@1.1.4
│ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │   └── has-flag@4.0.0
│ │ │ ├── jest-get-type@27.5.1
│ │ │ ├── leven@3.1.0
│ │ │ └─┬ pretty-format@27.5.1
│ │ │   ├── ansi-regex@5.0.1 deduped
│ │ │   ├── ansi-styles@5.2.0
│ │ │   └── react-is@17.0.2
│ │ ├── resolve.exports@1.1.1
│ │ ├── resolve@1.22.8 deduped
│ │ └── slash@3.0.0 deduped
│ ├─┬ jest-watch-typeahead@1.1.0
│ │ ├─┬ ansi-escapes@4.3.2
│ │ │ └── type-fest@0.21.3 deduped
│ │ ├─┬ chalk@4.1.2
│ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │   └── color-name@1.1.4
│ │ │ └─┬ supports-color@7.2.0
│ │ │   └── has-flag@4.0.0
│ │ ├── jest-regex-util@28.0.2
│ │ ├─┬ jest-watcher@28.1.3
│ │ │ ├─┬ @jest/test-result@28.1.3
│ │ │ │ ├─┬ @jest/console@28.1.3
│ │ │ │ │ ├── @jest/types@28.1.3 deduped
│ │ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ │ ├── chalk@4.1.2 deduped
│ │ │ │ │ ├─┬ jest-message-util@28.1.3
│ │ │ │ │ │ ├── @babel/code-frame@7.24.7 deduped
│ │ │ │ │ │ ├── @jest/types@28.1.3 deduped
│ │ │ │ │ │ ├── @types/stack-utils@2.0.3 deduped
│ │ │ │ │ │ ├── chalk@4.1.2 deduped
│ │ │ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ │ │ ├── micromatch@4.0.7 deduped
│ │ │ │ │ │ ├─┬ pretty-format@28.1.3
│ │ │ │ │ │ │ ├── @jest/schemas@28.1.3 deduped
│ │ │ │ │ │ │ ├── ansi-regex@5.0.1 deduped
│ │ │ │ │ │ │ ├── ansi-styles@5.2.0
│ │ │ │ │ │ │ └── react-is@18.3.1
│ │ │ │ │ │ ├── slash@3.0.0
│ │ │ │ │ │ └── stack-utils@2.0.6 deduped
│ │ │ │ │ ├── jest-util@28.1.3 deduped
│ │ │ │ │ └── slash@3.0.0
│ │ │ │ ├── @jest/types@28.1.3 deduped
│ │ │ │ ├── @types/istanbul-lib-coverage@2.0.6 deduped
│ │ │ │ └── collect-v8-coverage@1.0.2
│ │ │ ├─┬ @jest/types@28.1.3
│ │ │ │ ├─┬ @jest/schemas@28.1.3
│ │ │ │ │ └── @sinclair/typebox@0.24.51
│ │ │ │ ├── @types/istanbul-lib-coverage@2.0.6 deduped
│ │ │ │ ├── @types/istanbul-reports@3.0.4 deduped
│ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ ├─┬ @types/yargs@17.0.32
│ │ │ │ │ └── @types/yargs-parser@21.0.3 deduped
│ │ │ │ └── chalk@4.1.2 deduped
│ │ │ ├── @types/node@20.14.9 deduped
│ │ │ ├── ansi-escapes@4.3.2 deduped
│ │ │ ├── chalk@4.1.2 deduped
│ │ │ ├── emittery@0.10.2
│ │ │ ├─┬ jest-util@28.1.3
│ │ │ │ ├── @jest/types@28.1.3 deduped
│ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ ├── chalk@4.1.2 deduped
│ │ │ │ ├── ci-info@3.9.0 deduped
│ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ └── picomatch@2.3.1 deduped
│ │ │ └─┬ string-length@4.0.2
│ │ │   ├── char-regex@1.0.2
│ │ │   └─┬ strip-ansi@6.0.1
│ │ │     └── ansi-regex@5.0.1 deduped
│ │ ├── jest@27.5.1 deduped
│ │ ├── slash@4.0.0
│ │ ├─┬ string-length@5.0.1
│ │ │ ├── char-regex@2.0.1
│ │ │ └── strip-ansi@7.1.0 deduped
│ │ └─┬ strip-ansi@7.1.0
│ │   └── ansi-regex@6.0.1
│ ├─┬ jest@27.5.1
│ │ ├─┬ @jest/core@27.5.1
│ │ │ ├─┬ @jest/console@27.5.1
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ ├── jest-message-util@27.5.1 deduped
│ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ └── slash@3.0.0 deduped
│ │ │ ├─┬ @jest/reporters@27.5.1
│ │ │ │ ├── @bcoe/v8-coverage@0.2.3
│ │ │ │ ├── @jest/console@27.5.1 deduped
│ │ │ │ ├── @jest/test-result@27.5.1 deduped
│ │ │ │ ├── @jest/transform@27.5.1 deduped
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ ├── collect-v8-coverage@1.0.2 deduped
│ │ │ │ ├── exit@0.1.2 deduped
│ │ │ │ ├── glob@7.2.3 deduped
│ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ ├── istanbul-lib-coverage@3.2.2 deduped
│ │ │ │ ├── istanbul-lib-instrument@5.2.1 deduped
│ │ │ │ ├─┬ istanbul-lib-report@3.0.1
│ │ │ │ │ ├── istanbul-lib-coverage@3.2.2 deduped
│ │ │ │ │ ├─┬ make-dir@4.0.0
│ │ │ │ │ │ └── semver@7.6.2 deduped
│ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ ├─┬ istanbul-lib-source-maps@4.0.1
│ │ │ │ │ ├── debug@4.3.5 deduped
│ │ │ │ │ ├── istanbul-lib-coverage@3.2.2 deduped
│ │ │ │ │ └── source-map@0.6.1
│ │ │ │ ├─┬ istanbul-reports@3.1.7
│ │ │ │ │ ├── html-escaper@2.0.2
│ │ │ │ │ └── istanbul-lib-report@3.0.1 deduped
│ │ │ │ ├── jest-haste-map@27.5.1 deduped
│ │ │ │ ├── jest-resolve@27.5.1 deduped
│ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ ├── jest-worker@27.5.1 deduped
│ │ │ │ ├── UNMET OPTIONAL DEPENDENCY node-notifier@^8.0.1 || ^9.0.0 || ^10.0.0
│ │ │ │ ├── slash@3.0.0 deduped
│ │ │ │ ├── source-map@0.6.1
│ │ │ │ ├─┬ string-length@4.0.2
│ │ │ │ │ ├── char-regex@1.0.2 deduped
│ │ │ │ │ └── strip-ansi@6.0.1 deduped
│ │ │ │ ├─┬ terminal-link@2.1.1
│ │ │ │ │ ├── ansi-escapes@4.3.2 deduped
│ │ │ │ │ └─┬ supports-hyperlinks@2.3.0
│ │ │ │ │   ├── has-flag@4.0.0
│ │ │ │ │   └─┬ supports-color@7.2.0
│ │ │ │ │     └── has-flag@4.0.0 deduped
│ │ │ │ └─┬ v8-to-istanbul@8.1.1
│ │ │ │   ├── @types/istanbul-lib-coverage@2.0.6 deduped
│ │ │ │   ├── convert-source-map@1.9.0
│ │ │ │   └── source-map@0.7.4 deduped
│ │ │ ├─┬ @jest/test-result@27.5.1
│ │ │ │ ├── @jest/console@27.5.1 deduped
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├── @types/istanbul-lib-coverage@2.0.6 deduped
│ │ │ │ └── collect-v8-coverage@1.0.2 deduped
│ │ │ ├── @jest/transform@27.5.1 deduped
│ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ ├── @types/node@20.14.9 deduped
│ │ │ ├── ansi-escapes@4.3.2 deduped
│ │ │ ├─┬ chalk@4.1.2
│ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │   └── color-name@1.1.4
│ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │   └── has-flag@4.0.0
│ │ │ ├── emittery@0.8.1
│ │ │ ├── exit@0.1.2
│ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ ├─┬ jest-changed-files@27.5.1
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├── execa@5.1.1 deduped
│ │ │ │ └── throat@6.0.2
│ │ │ ├─┬ jest-config@27.5.1
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├─┬ @jest/test-sequencer@27.5.1
│ │ │ │ │ ├── @jest/test-result@27.5.1 deduped
│ │ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ │ ├── jest-haste-map@27.5.1 deduped
│ │ │ │ │ └── jest-runtime@27.5.1 deduped
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├── babel-jest@27.5.1 deduped
│ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ ├── ci-info@3.9.0 deduped
│ │ │ │ ├── deepmerge@4.3.1 deduped
│ │ │ │ ├── glob@7.2.3 deduped
│ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ ├─┬ jest-circus@27.5.1
│ │ │ │ │ ├── @jest/environment@27.5.1 deduped
│ │ │ │ │ ├── @jest/test-result@27.5.1 deduped
│ │ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ │ ├── co@4.6.0
│ │ │ │ │ ├── dedent@0.7.0
│ │ │ │ │ ├── expect@27.5.1 deduped
│ │ │ │ │ ├── is-generator-fn@2.1.0
│ │ │ │ │ ├─┬ jest-each@27.5.1
│ │ │ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ │ │ ├── jest-get-type@27.5.1 deduped
│ │ │ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ │ │ └── pretty-format@27.5.1 deduped
│ │ │ │ │ ├── jest-matcher-utils@27.5.1 deduped
│ │ │ │ │ ├── jest-message-util@27.5.1 deduped
│ │ │ │ │ ├── jest-runtime@27.5.1 deduped
│ │ │ │ │ ├── jest-snapshot@27.5.1 deduped
│ │ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ │ ├── pretty-format@27.5.1 deduped
│ │ │ │ │ ├── slash@3.0.0 deduped
│ │ │ │ │ ├── stack-utils@2.0.6 deduped
│ │ │ │ │ └── throat@6.0.2 deduped
│ │ │ │ ├─┬ jest-environment-jsdom@27.5.1
│ │ │ │ │ ├── @jest/environment@27.5.1 deduped
│ │ │ │ │ ├── @jest/fake-timers@27.5.1 deduped
│ │ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ │ ├── jest-mock@27.5.1 deduped
│ │ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ │ └─┬ jsdom@16.7.0
│ │ │ │ │   ├── abab@2.0.6 deduped
│ │ │ │ │   ├─┬ acorn-globals@6.0.0
│ │ │ │ │   │ ├── acorn-walk@7.2.0
│ │ │ │ │   │ └── acorn@7.4.1
│ │ │ │ │   ├── acorn@8.12.0 deduped
│ │ │ │ │   ├── UNMET OPTIONAL DEPENDENCY canvas@^2.5.0
│ │ │ │ │   ├── cssom@0.4.4
│ │ │ │ │   ├─┬ cssstyle@2.3.0
│ │ │ │ │   │ └── cssom@0.3.8
│ │ │ │ │   ├─┬ data-urls@2.0.0
│ │ │ │ │   │ ├── abab@2.0.6 deduped
│ │ │ │ │   │ ├── whatwg-mimetype@2.3.0 deduped
│ │ │ │ │   │ └── whatwg-url@8.7.0 deduped
│ │ │ │ │   ├── decimal.js@10.4.3
│ │ │ │ │   ├─┬ domexception@2.0.1
│ │ │ │ │   │ └── webidl-conversions@5.0.0
│ │ │ │ │   ├─┬ escodegen@2.1.0
│ │ │ │ │   │ ├── esprima@4.0.1 deduped
│ │ │ │ │   │ ├── estraverse@5.3.0 deduped
│ │ │ │ │   │ ├── esutils@2.0.3 deduped
│ │ │ │ │   │ └── source-map@0.6.1
│ │ │ │ │   ├─┬ form-data@3.0.1
│ │ │ │ │   │ ├── asynckit@0.4.0
│ │ │ │ │   │ ├─┬ combined-stream@1.0.8
│ │ │ │ │   │ │ └── delayed-stream@1.0.0
│ │ │ │ │   │ └── mime-types@2.1.35 deduped
│ │ │ │ │   ├─┬ html-encoding-sniffer@2.0.1
│ │ │ │ │   │ └── whatwg-encoding@1.0.5 deduped
│ │ │ │ │   ├─┬ http-proxy-agent@4.0.1
│ │ │ │ │   │ ├── @tootallnate/once@1.1.2
│ │ │ │ │   │ ├─┬ agent-base@6.0.2
│ │ │ │ │   │ │ └── debug@4.3.5 deduped
│ │ │ │ │   │ └── debug@4.3.5 deduped
│ │ │ │ │   ├─┬ https-proxy-agent@5.0.1
│ │ │ │ │   │ ├── agent-base@6.0.2 deduped
│ │ │ │ │   │ └── debug@4.3.5 deduped
│ │ │ │ │   ├── is-potential-custom-element-name@1.0.1
│ │ │ │ │   ├── nwsapi@2.2.10
│ │ │ │ │   ├── parse5@6.0.1
│ │ │ │ │   ├─┬ saxes@5.0.1
│ │ │ │ │   │ └── xmlchars@2.2.0
│ │ │ │ │   ├── symbol-tree@3.2.4
│ │ │ │ │   ├─┬ tough-cookie@4.1.4
│ │ │ │ │   │ ├── psl@1.9.0
│ │ │ │ │   │ ├── punycode@2.3.1 deduped
│ │ │ │ │   │ ├── universalify@0.2.0
│ │ │ │ │   │ └─┬ url-parse@1.5.10
│ │ │ │ │   │   ├── querystringify@2.2.0
│ │ │ │ │   │   └── requires-port@1.0.0 deduped
│ │ │ │ │   ├─┬ w3c-hr-time@1.0.2
│ │ │ │ │   │ └── browser-process-hrtime@1.0.0
│ │ │ │ │   ├─┬ w3c-xmlserializer@2.0.0
│ │ │ │ │   │ └── xml-name-validator@3.0.0 deduped
│ │ │ │ │   ├── webidl-conversions@6.1.0
│ │ │ │ │   ├─┬ whatwg-encoding@1.0.5
│ │ │ │ │   │ └─┬ iconv-lite@0.4.24
│ │ │ │ │   │   └── safer-buffer@2.1.2 deduped
│ │ │ │ │   ├── whatwg-mimetype@2.3.0
│ │ │ │ │   ├─┬ whatwg-url@8.7.0
│ │ │ │ │   │ ├── lodash@4.17.21 deduped
│ │ │ │ │   │ ├─┬ tr46@2.1.0
│ │ │ │ │   │ │ └── punycode@2.3.1 deduped
│ │ │ │ │   │ └── webidl-conversions@6.1.0 deduped
│ │ │ │ │   ├─┬ ws@7.5.10
│ │ │ │ │   │ ├── UNMET OPTIONAL DEPENDENCY bufferutil@^4.0.1
│ │ │ │ │   │ └── UNMET OPTIONAL DEPENDENCY utf-8-validate@^5.0.2
│ │ │ │ │   └── xml-name-validator@3.0.0
│ │ │ │ ├─┬ jest-environment-node@27.5.1
│ │ │ │ │ ├── @jest/environment@27.5.1 deduped
│ │ │ │ │ ├── @jest/fake-timers@27.5.1 deduped
│ │ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ │ ├── jest-mock@27.5.1 deduped
│ │ │ │ │ └── jest-util@27.5.1 deduped
│ │ │ │ ├── jest-get-type@27.5.1 deduped
│ │ │ │ ├─┬ jest-jasmine2@27.5.1
│ │ │ │ │ ├── @jest/environment@27.5.1 deduped
│ │ │ │ │ ├── @jest/source-map@27.5.1 deduped
│ │ │ │ │ ├── @jest/test-result@27.5.1 deduped
│ │ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ │ ├── co@4.6.0 deduped
│ │ │ │ │ ├── expect@27.5.1 deduped
│ │ │ │ │ ├── is-generator-fn@2.1.0 deduped
│ │ │ │ │ ├── jest-each@27.5.1 deduped
│ │ │ │ │ ├── jest-matcher-utils@27.5.1 deduped
│ │ │ │ │ ├── jest-message-util@27.5.1 deduped
│ │ │ │ │ ├── jest-runtime@27.5.1 deduped
│ │ │ │ │ ├── jest-snapshot@27.5.1 deduped
│ │ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ │ ├── pretty-format@27.5.1 deduped
│ │ │ │ │ └── throat@6.0.2 deduped
│ │ │ │ ├── jest-regex-util@27.5.1 deduped
│ │ │ │ ├── jest-resolve@27.5.1 deduped
│ │ │ │ ├── jest-runner@27.5.1 deduped
│ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ ├── jest-validate@27.5.1 deduped
│ │ │ │ ├── micromatch@4.0.7 deduped
│ │ │ │ ├── parse-json@5.2.0 deduped
│ │ │ │ ├── pretty-format@27.5.1 deduped
│ │ │ │ ├── slash@3.0.0 deduped
│ │ │ │ ├── strip-json-comments@3.1.1 deduped
│ │ │ │ └── UNMET OPTIONAL DEPENDENCY ts-node@>=9.0.0
│ │ │ ├── jest-haste-map@27.5.1 deduped
│ │ │ ├─┬ jest-message-util@27.5.1
│ │ │ │ ├── @babel/code-frame@7.24.7 deduped
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├── @types/stack-utils@2.0.3
│ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ ├── micromatch@4.0.7 deduped
│ │ │ │ ├── pretty-format@27.5.1 deduped
│ │ │ │ ├── slash@3.0.0 deduped
│ │ │ │ └─┬ stack-utils@2.0.6
│ │ │ │   └── escape-string-regexp@2.0.0
│ │ │ ├── jest-regex-util@27.5.1 deduped
│ │ │ ├─┬ jest-resolve-dependencies@27.5.1
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├── jest-regex-util@27.5.1 deduped
│ │ │ │ └── jest-snapshot@27.5.1 deduped
│ │ │ ├── jest-resolve@27.5.1 deduped
│ │ │ ├─┬ jest-runner@27.5.1
│ │ │ │ ├── @jest/console@27.5.1 deduped
│ │ │ │ ├─┬ @jest/environment@27.5.1
│ │ │ │ │ ├── @jest/fake-timers@27.5.1 deduped
│ │ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ │ └── jest-mock@27.5.1 deduped
│ │ │ │ ├── @jest/test-result@27.5.1 deduped
│ │ │ │ ├── @jest/transform@27.5.1 deduped
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ ├── emittery@0.8.1 deduped
│ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ ├─┬ jest-docblock@27.5.1
│ │ │ │ │ └── detect-newline@3.1.0
│ │ │ │ ├── jest-environment-jsdom@27.5.1 deduped
│ │ │ │ ├── jest-environment-node@27.5.1 deduped
│ │ │ │ ├── jest-haste-map@27.5.1 deduped
│ │ │ │ ├─┬ jest-leak-detector@27.5.1
│ │ │ │ │ ├── jest-get-type@27.5.1 deduped
│ │ │ │ │ └── pretty-format@27.5.1 deduped
│ │ │ │ ├── jest-message-util@27.5.1 deduped
│ │ │ │ ├── jest-resolve@27.5.1 deduped
│ │ │ │ ├── jest-runtime@27.5.1 deduped
│ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ ├── jest-worker@27.5.1 deduped
│ │ │ │ ├── source-map-support@0.5.21 deduped
│ │ │ │ └── throat@6.0.2 deduped
│ │ │ ├─┬ jest-runtime@27.5.1
│ │ │ │ ├── @jest/environment@27.5.1 deduped
│ │ │ │ ├─┬ @jest/fake-timers@27.5.1
│ │ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ │ ├─┬ @sinonjs/fake-timers@8.1.0
│ │ │ │ │ │ └─┬ @sinonjs/commons@1.8.6
│ │ │ │ │ │   └── type-detect@4.0.8
│ │ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ │ ├── jest-message-util@27.5.1 deduped
│ │ │ │ │ ├── jest-mock@27.5.1 deduped
│ │ │ │ │ └── jest-util@27.5.1 deduped
│ │ │ │ ├─┬ @jest/globals@27.5.1
│ │ │ │ │ ├── @jest/environment@27.5.1 deduped
│ │ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ │ └── expect@27.5.1 deduped
│ │ │ │ ├─┬ @jest/source-map@27.5.1
│ │ │ │ │ ├── callsites@3.1.0 deduped
│ │ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ │ └── source-map@0.6.1
│ │ │ │ ├── @jest/test-result@27.5.1 deduped
│ │ │ │ ├── @jest/transform@27.5.1 deduped
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ ├── cjs-module-lexer@1.3.1
│ │ │ │ ├── collect-v8-coverage@1.0.2 deduped
│ │ │ │ ├── execa@5.1.1 deduped
│ │ │ │ ├── glob@7.2.3 deduped
│ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ ├── jest-haste-map@27.5.1 deduped
│ │ │ │ ├── jest-message-util@27.5.1 deduped
│ │ │ │ ├─┬ jest-mock@27.5.1
│ │ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ │ └── @types/node@20.14.9 deduped
│ │ │ │ ├── jest-regex-util@27.5.1 deduped
│ │ │ │ ├── jest-resolve@27.5.1 deduped
│ │ │ │ ├── jest-snapshot@27.5.1 deduped
│ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ ├── slash@3.0.0 deduped
│ │ │ │ └── strip-bom@4.0.0
│ │ │ ├─┬ jest-snapshot@27.5.1
│ │ │ │ ├── @babel/core@7.24.7 deduped
│ │ │ │ ├── @babel/generator@7.24.7 deduped
│ │ │ │ ├── @babel/plugin-syntax-typescript@7.24.7 deduped
│ │ │ │ ├── @babel/traverse@7.24.7 deduped
│ │ │ │ ├── @babel/types@7.24.7 deduped
│ │ │ │ ├── @jest/transform@27.5.1 deduped
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├── @types/babel__traverse@7.20.6 deduped
│ │ │ │ ├── @types/prettier@2.7.3
│ │ │ │ ├── babel-preset-current-node-syntax@1.0.1 deduped
│ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ ├─┬ expect@27.5.1
│ │ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ │ ├── jest-get-type@27.5.1 deduped
│ │ │ │ │ ├── jest-matcher-utils@27.5.1 deduped
│ │ │ │ │ └── jest-message-util@27.5.1 deduped
│ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ ├─┬ jest-diff@27.5.1
│ │ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ │ ├── diff-sequences@27.5.1
│ │ │ │ │ ├── jest-get-type@27.5.1 deduped
│ │ │ │ │ └── pretty-format@27.5.1 deduped
│ │ │ │ ├── jest-get-type@27.5.1 deduped
│ │ │ │ ├── jest-haste-map@27.5.1 deduped
│ │ │ │ ├─┬ jest-matcher-utils@27.5.1
│ │ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ │ ├── jest-diff@27.5.1 deduped
│ │ │ │ │ ├── jest-get-type@27.5.1 deduped
│ │ │ │ │ └── pretty-format@27.5.1 deduped
│ │ │ │ ├── jest-message-util@27.5.1 deduped
│ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ ├── natural-compare@1.4.0 deduped
│ │ │ │ ├── pretty-format@27.5.1 deduped
│ │ │ │ └── semver@7.6.2 deduped
│ │ │ ├── jest-util@27.5.1 deduped
│ │ │ ├── jest-validate@27.5.1 deduped
│ │ │ ├─┬ jest-watcher@27.5.1
│ │ │ │ ├── @jest/test-result@27.5.1 deduped
│ │ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ ├── ansi-escapes@4.3.2 deduped
│ │ │ │ ├─┬ chalk@4.1.2
│ │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │ │   └── color-name@1.1.4
│ │ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │ │   └── has-flag@4.0.0
│ │ │ │ ├── jest-util@27.5.1 deduped
│ │ │ │ └── string-length@4.0.2 deduped
│ │ │ ├── micromatch@4.0.7 deduped
│ │ │ ├── UNMET OPTIONAL DEPENDENCY node-notifier@^8.0.1 || ^9.0.0 || ^10.0.0
│ │ │ ├── rimraf@3.0.2 deduped
│ │ │ ├── slash@3.0.0 deduped
│ │ │ └── strip-ansi@6.0.1 deduped
│ │ ├─┬ import-local@3.1.0
│ │ │ ├── pkg-dir@4.2.0 deduped
│ │ │ └─┬ resolve-cwd@3.0.0
│ │ │   └── resolve-from@5.0.0 deduped
│ │ ├─┬ jest-cli@27.5.1
│ │ │ ├── @jest/core@27.5.1 deduped
│ │ │ ├── @jest/test-result@27.5.1 deduped
│ │ │ ├── @jest/types@27.5.1 deduped
│ │ │ ├─┬ chalk@4.1.2
│ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │   └── color-name@1.1.4
│ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │   └── has-flag@4.0.0
│ │ │ ├── exit@0.1.2 deduped
│ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ ├── import-local@3.1.0 deduped
│ │ │ ├── jest-config@27.5.1 deduped
│ │ │ ├── jest-util@27.5.1 deduped
│ │ │ ├── jest-validate@27.5.1 deduped
│ │ │ ├── UNMET OPTIONAL DEPENDENCY node-notifier@^8.0.1 || ^9.0.0 || ^10.0.0
│ │ │ ├── prompts@2.4.2 deduped
│ │ │ └─┬ yargs@16.2.0
│ │ │   ├─┬ cliui@7.0.4
│ │ │   │ ├── string-width@4.2.3 deduped
│ │ │   │ ├── strip-ansi@6.0.1 deduped
│ │ │   │ └─┬ wrap-ansi@7.0.0
│ │ │   │   ├─┬ ansi-styles@4.3.0
│ │ │   │   │ └─┬ color-convert@2.0.1
│ │ │   │   │   └── color-name@1.1.4
│ │ │   │   ├── string-width@4.2.3 deduped
│ │ │   │   └── strip-ansi@6.0.1 deduped
│ │ │   ├── escalade@3.1.2 deduped
│ │ │   ├── get-caller-file@2.0.5
│ │ │   ├── require-directory@2.1.1
│ │ │   ├─┬ string-width@4.2.3
│ │ │   │ ├── emoji-regex@8.0.0
│ │ │   │ ├── is-fullwidth-code-point@3.0.0
│ │ │   │ └── strip-ansi@6.0.1 deduped
│ │ │   ├── y18n@5.0.8
│ │ │   └── yargs-parser@20.2.9
│ │ └── UNMET OPTIONAL DEPENDENCY node-notifier@^8.0.1 || ^9.0.0 || ^10.0.0
│ ├─┬ mini-css-extract-plugin@2.9.0
│ │ ├── schema-utils@4.2.0 deduped
│ │ ├── tapable@2.2.1 deduped
│ │ └── webpack@5.92.1 deduped
│ ├─┬ postcss-flexbugs-fixes@5.0.2
│ │ └── postcss@8.4.39 deduped
│ ├─┬ postcss-loader@6.2.1
│ │ ├─┬ cosmiconfig@7.1.0
│ │ │ ├── @types/parse-json@4.0.2
│ │ │ ├── import-fresh@3.3.0 deduped
│ │ │ ├─┬ parse-json@5.2.0
│ │ │ │ ├── @babel/code-frame@7.24.7 deduped
│ │ │ │ ├─┬ error-ex@1.3.2
│ │ │ │ │ └── is-arrayish@0.2.1
│ │ │ │ ├── json-parse-even-better-errors@2.3.1 deduped
│ │ │ │ └── lines-and-columns@1.2.4 deduped
│ │ │ ├── path-type@4.0.0
│ │ │ └── yaml@1.10.2 deduped
│ │ ├── klona@2.0.6
│ │ ├── postcss@8.4.39 deduped
│ │ ├── semver@7.6.2 deduped
│ │ └── webpack@5.92.1 deduped
│ ├─┬ postcss-normalize@10.0.1
│ │ ├── @csstools/normalize.css@12.1.1
│ │ ├── browserslist@4.23.1 deduped
│ │ ├─┬ postcss-browser-comments@4.0.0
│ │ │ ├── browserslist@4.23.1 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├── postcss@8.4.39 deduped
│ │ └── sanitize.css@13.0.0
│ ├─┬ postcss-preset-env@7.8.3
│ │ ├─┬ @csstools/postcss-cascade-layers@1.1.1
│ │ │ ├─┬ @csstools/selector-specificity@2.2.0
│ │ │ │ └── postcss-selector-parser@6.1.0 deduped
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-color-function@1.1.1
│ │ │ ├── @csstools/postcss-progressive-custom-properties@1.3.0 deduped
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-font-format-keywords@1.0.1
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-hwb-function@1.0.2
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-ic-unit@1.0.1
│ │ │ ├── @csstools/postcss-progressive-custom-properties@1.3.0 deduped
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-is-pseudo-class@2.0.7
│ │ │ ├── @csstools/selector-specificity@2.2.0 deduped
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-nested-calc@1.0.0
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-normalize-display-values@1.0.1
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-oklab-function@1.1.1
│ │ │ ├── @csstools/postcss-progressive-custom-properties@1.3.0 deduped
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-progressive-custom-properties@1.3.0
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-stepped-value-functions@1.0.1
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-text-decoration-shorthand@1.0.0
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-trigonometric-functions@1.0.2
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ @csstools/postcss-unset-value@1.0.2
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ autoprefixer@10.4.19
│ │ │ ├── browserslist@4.23.1 deduped
│ │ │ ├── caniuse-lite@1.0.30001638 deduped
│ │ │ ├── fraction.js@4.3.7
│ │ │ ├── normalize-range@0.1.2
│ │ │ ├── picocolors@1.0.1 deduped
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├── browserslist@4.23.1 deduped
│ │ ├─┬ css-blank-pseudo@3.0.3
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ css-has-pseudo@3.0.4
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ css-prefers-color-scheme@6.0.3
│ │ │ └── postcss@8.4.39 deduped
│ │ ├── cssdb@7.11.2
│ │ ├─┬ postcss-attribute-case-insensitive@5.0.2
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-clamp@4.1.0
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-color-functional-notation@4.2.4
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-color-hex-alpha@8.0.4
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-color-rebeccapurple@7.1.1
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-custom-media@8.0.2
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-custom-properties@12.1.11
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-custom-selectors@6.0.3
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-dir-pseudo-class@6.0.5
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-double-position-gradients@3.1.2
│ │ │ ├── @csstools/postcss-progressive-custom-properties@1.3.0 deduped
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-env-function@4.0.6
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-focus-visible@6.0.4
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-focus-within@5.0.4
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-font-variant@5.0.0
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-gap-properties@3.0.5
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-image-set-function@4.0.7
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-initial@4.0.1
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-lab-function@4.2.1
│ │ │ ├── @csstools/postcss-progressive-custom-properties@1.3.0 deduped
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-logical@5.0.4
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-media-minmax@5.0.0
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-nesting@10.2.0
│ │ │ ├── @csstools/selector-specificity@2.2.0 deduped
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-opacity-percentage@1.1.3
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-overflow-shorthand@3.0.4
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-page-break@3.0.4
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-place@7.0.5
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-pseudo-class-any-link@7.1.6
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-replace-overflow-wrap@4.0.0
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-selector-not@6.0.1
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├── postcss-value-parser@4.2.0 deduped
│ │ └── postcss@8.4.39 deduped
│ ├─┬ postcss@8.4.39
│ │ ├── nanoid@3.3.7
│ │ ├── picocolors@1.0.1
│ │ └── source-map-js@1.2.0
│ ├─┬ prompts@2.4.2
│ │ ├── kleur@3.0.3
│ │ └── sisteransi@1.0.5
│ ├─┬ react-app-polyfill@3.0.0
│ │ ├── core-js@3.37.1
│ │ ├── object-assign@4.1.1 deduped
│ │ ├─┬ promise@8.3.0
│ │ │ └── asap@2.0.6
│ │ ├─┬ raf@3.4.1
│ │ │ └── performance-now@2.1.0
│ │ ├── regenerator-runtime@0.13.11
│ │ └── whatwg-fetch@3.6.20
│ ├─┬ react-dev-utils@12.0.1
│ │ ├── @babel/code-frame@7.24.7 deduped
│ │ ├── address@1.2.2
│ │ ├── browserslist@4.23.1 deduped
│ │ ├─┬ chalk@4.1.2
│ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │   └── color-name@1.1.4
│ │ │ └─┬ supports-color@7.2.0
│ │ │   └── has-flag@4.0.0
│ │ ├── cross-spawn@7.0.3 deduped
│ │ ├─┬ detect-port-alt@1.1.6
│ │ │ ├── address@1.2.2 deduped
│ │ │ └─┬ debug@2.6.9
│ │ │   └── ms@2.0.0
│ │ ├── escape-string-regexp@4.0.0
│ │ ├── filesize@8.0.7
│ │ ├─┬ find-up@5.0.0
│ │ │ ├─┬ locate-path@6.0.0
│ │ │ │ └─┬ p-locate@5.0.0
│ │ │ │   └─┬ p-limit@3.1.0
│ │ │ │     └── yocto-queue@0.1.0 deduped
│ │ │ └── path-exists@4.0.0 deduped
│ │ ├─┬ fork-ts-checker-webpack-plugin@6.5.3
│ │ │ ├── @babel/code-frame@7.24.7 deduped
│ │ │ ├── @types/json-schema@7.0.15 deduped
│ │ │ ├─┬ chalk@4.1.2
│ │ │ │ ├─┬ ansi-styles@4.3.0
│ │ │ │ │ └─┬ color-convert@2.0.1
│ │ │ │ │   └── color-name@1.1.4
│ │ │ │ └─┬ supports-color@7.2.0
│ │ │ │   └── has-flag@4.0.0
│ │ │ ├── chokidar@3.6.0 deduped
│ │ │ ├─┬ cosmiconfig@6.0.0
│ │ │ │ ├── @types/parse-json@4.0.2 deduped
│ │ │ │ ├── import-fresh@3.3.0 deduped
│ │ │ │ ├── parse-json@5.2.0 deduped
│ │ │ │ ├── path-type@4.0.0 deduped
│ │ │ │ └── yaml@1.10.2 deduped
│ │ │ ├── deepmerge@4.3.1 deduped
│ │ │ ├── eslint@8.57.0 deduped
│ │ │ ├─┬ fs-extra@9.1.0
│ │ │ │ ├── at-least-node@1.0.0
│ │ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ │ ├── jsonfile@6.1.0 deduped
│ │ │ │ └── universalify@2.0.1 deduped
│ │ │ ├─┬ glob@7.2.3
│ │ │ │ ├── fs.realpath@1.0.0
│ │ │ │ ├─┬ inflight@1.0.6
│ │ │ │ │ ├── once@1.4.0 deduped
│ │ │ │ │ └── wrappy@1.0.2
│ │ │ │ ├── inherits@2.0.4
│ │ │ │ ├── minimatch@3.1.2 deduped
│ │ │ │ ├─┬ once@1.4.0
│ │ │ │ │ └── wrappy@1.0.2 deduped
│ │ │ │ └── path-is-absolute@1.0.1
│ │ │ ├─┬ memfs@3.5.3
│ │ │ │ └── fs-monkey@1.0.6
│ │ │ ├── minimatch@3.1.2 deduped
│ │ │ ├─┬ schema-utils@2.7.0
│ │ │ │ ├── @types/json-schema@7.0.15 deduped
│ │ │ │ ├── ajv-keywords@3.5.2 deduped
│ │ │ │ └── ajv@6.12.6 deduped
│ │ │ ├── semver@7.6.2 deduped
│ │ │ ├── tapable@1.1.3
│ │ │ ├── typescript@4.9.5 deduped
│ │ │ ├── UNMET OPTIONAL DEPENDENCY vue-template-compiler@*
│ │ │ └── webpack@5.92.1 deduped
│ │ ├─┬ global-modules@2.0.0
│ │ │ └─┬ global-prefix@3.0.0
│ │ │   ├── ini@1.3.8
│ │ │   ├── kind-of@6.0.3
│ │ │   └─┬ which@1.3.1
│ │ │     └── isexe@2.0.0 deduped
│ │ ├─┬ globby@11.1.0
│ │ │ ├── array-union@2.1.0
│ │ │ ├─┬ dir-glob@3.0.1
│ │ │ │ └── path-type@4.0.0 deduped
│ │ │ ├── fast-glob@3.3.2 deduped
│ │ │ ├── ignore@5.3.1 deduped
│ │ │ ├── merge2@1.4.1
│ │ │ └── slash@3.0.0 deduped
│ │ ├─┬ gzip-size@6.0.0
│ │ │ └── duplexer@0.1.2
│ │ ├── immer@9.0.21
│ │ ├── is-root@2.1.0
│ │ ├── loader-utils@3.3.1
│ │ ├─┬ open@8.4.2
│ │ │ ├── define-lazy-prop@2.0.0
│ │ │ ├── is-docker@2.2.1
│ │ │ └─┬ is-wsl@2.2.0
│ │ │   └── is-docker@2.2.1 deduped
│ │ ├─┬ pkg-up@3.1.0
│ │ │ └─┬ find-up@3.0.0
│ │ │   └─┬ locate-path@3.0.0
│ │ │     ├─┬ p-locate@3.0.0
│ │ │     │ └─┬ p-limit@2.3.0
│ │ │     │   └── p-try@2.2.0
│ │ │     └── path-exists@3.0.0
│ │ ├── prompts@2.4.2 deduped
│ │ ├── react-error-overlay@6.0.11
│ │ ├─┬ recursive-readdir@2.2.3
│ │ │ └── minimatch@3.1.2 deduped
│ │ ├── shell-quote@1.8.1
│ │ ├── strip-ansi@6.0.1 deduped
│ │ └── text-table@0.2.0 deduped
│ ├── react-refresh@0.11.0
│ ├── react@18.3.1 deduped
│ ├─┬ resolve-url-loader@4.0.0
│ │ ├─┬ adjust-sourcemap-loader@4.0.0
│ │ │ ├── loader-utils@2.0.4 deduped
│ │ │ └── regex-parser@2.3.0
│ │ ├── convert-source-map@1.9.0
│ │ ├── loader-utils@2.0.4 deduped
│ │ ├─┬ postcss@7.0.39
│ │ │ ├── picocolors@0.2.1
│ │ │ └── source-map@0.6.1 deduped
│ │ ├── UNMET OPTIONAL DEPENDENCY rework-visit@1.0.0
│ │ ├── UNMET OPTIONAL DEPENDENCY rework@1.0.1
│ │ └── source-map@0.6.1
│ ├─┬ resolve@1.22.8
│ │ ├─┬ is-core-module@2.14.0
│ │ │ └── hasown@2.0.2 deduped
│ │ ├── path-parse@1.0.7
│ │ └── supports-preserve-symlinks-flag@1.0.0
│ ├─┬ sass-loader@12.6.0
│ │ ├── UNMET OPTIONAL DEPENDENCY fibers@>= 3.1.0
│ │ ├── klona@2.0.6 deduped
│ │ ├── neo-async@2.6.2
│ │ ├── UNMET OPTIONAL DEPENDENCY node-sass@^4.0.0 || ^5.0.0 || ^6.0.0 || ^7.0.0
│ │ ├── UNMET OPTIONAL DEPENDENCY sass-embedded@*
│ │ ├── UNMET OPTIONAL DEPENDENCY sass@^1.3.0
│ │ └── webpack@5.92.1 deduped
│ ├── semver@7.6.2
│ ├─┬ source-map-loader@3.0.2
│ │ ├── abab@2.0.6
│ │ ├─┬ iconv-lite@0.6.3
│ │ │ └── safer-buffer@2.1.2
│ │ ├── source-map-js@1.2.0 deduped
│ │ └── webpack@5.92.1 deduped
│ ├─┬ style-loader@3.3.4
│ │ └── webpack@5.92.1 deduped
│ ├─┬ tailwindcss@3.4.4
│ │ ├── @alloc/quick-lru@5.2.0
│ │ ├── arg@5.0.2
│ │ ├─┬ chokidar@3.6.0
│ │ │ ├── anymatch@3.1.3 deduped
│ │ │ ├── braces@3.0.3 deduped
│ │ │ ├── UNMET OPTIONAL DEPENDENCY fsevents@~2.3.2
│ │ │ ├─┬ glob-parent@5.1.2
│ │ │ │ └── is-glob@4.0.3 deduped
│ │ │ ├─┬ is-binary-path@2.1.0
│ │ │ │ └── binary-extensions@2.3.0
│ │ │ ├── is-glob@4.0.3 deduped
│ │ │ ├── normalize-path@3.0.0 deduped
│ │ │ └─┬ readdirp@3.6.0
│ │ │   └── picomatch@2.3.1 deduped
│ │ ├── didyoumean@1.2.2
│ │ ├── dlv@1.1.3
│ │ ├─┬ fast-glob@3.3.2
│ │ │ ├── @nodelib/fs.stat@2.0.5
│ │ │ ├── @nodelib/fs.walk@1.2.8 deduped
│ │ │ ├─┬ glob-parent@5.1.2
│ │ │ │ └── is-glob@4.0.3 deduped
│ │ │ ├── merge2@1.4.1 deduped
│ │ │ └── micromatch@4.0.7 deduped
│ │ ├── glob-parent@6.0.2 deduped
│ │ ├── is-glob@4.0.3 deduped
│ │ ├── jiti@1.21.6
│ │ ├── lilconfig@2.1.0
│ │ ├── micromatch@4.0.7 deduped
│ │ ├── normalize-path@3.0.0 deduped
│ │ ├── object-hash@3.0.0
│ │ ├── picocolors@1.0.1 deduped
│ │ ├─┬ postcss-import@15.1.0
│ │ │ ├── postcss-value-parser@4.2.0 deduped
│ │ │ ├── postcss@8.4.39 deduped
│ │ │ ├─┬ read-cache@1.0.0
│ │ │ │ └── pify@2.3.0
│ │ │ └── resolve@1.22.8 deduped
│ │ ├─┬ postcss-js@4.0.1
│ │ │ ├── camelcase-css@2.0.1
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-load-config@4.0.2
│ │ │ ├── lilconfig@3.1.2
│ │ │ ├── postcss@8.4.39 deduped
│ │ │ ├── UNMET OPTIONAL DEPENDENCY ts-node@>=9.0.0
│ │ │ └── yaml@2.4.5
│ │ ├─┬ postcss-nested@6.0.1
│ │ │ ├── postcss-selector-parser@6.1.0 deduped
│ │ │ └── postcss@8.4.39 deduped
│ │ ├─┬ postcss-selector-parser@6.1.0
│ │ │ ├── cssesc@3.0.0
│ │ │ └── util-deprecate@1.0.2
│ │ ├── postcss@8.4.39 deduped
│ │ ├── resolve@1.22.8 deduped
│ │ └─┬ sucrase@3.35.0
│ │   ├── @jridgewell/gen-mapping@0.3.5 deduped
│ │   ├── commander@4.1.1
│ │   ├─┬ glob@10.4.2
│ │   │ ├─┬ foreground-child@3.2.1
│ │   │ │ ├── cross-spawn@7.0.3 deduped
│ │   │ │ └── signal-exit@4.1.0
│ │   │ ├─┬ jackspeak@3.4.0
│ │   │ │ ├─┬ @isaacs/cliui@8.0.2
│ │   │ │ │ ├─┬ string-width-cjs@npm:string-width@4.2.3
│ │   │ │ │ │ ├── emoji-regex@8.0.0
│ │   │ │ │ │ ├── is-fullwidth-code-point@3.0.0 deduped
│ │   │ │ │ │ └── strip-ansi@6.0.1 deduped
│ │   │ │ │ ├─┬ string-width@5.1.2
│ │   │ │ │ │ ├── eastasianwidth@0.2.0
│ │   │ │ │ │ ├── emoji-regex@9.2.2 deduped
│ │   │ │ │ │ └── strip-ansi@7.1.0 deduped
│ │   │ │ │ ├─┬ strip-ansi-cjs@npm:strip-ansi@6.0.1
│ │   │ │ │ │ └── ansi-regex@5.0.1 deduped
│ │   │ │ │ ├─┬ strip-ansi@7.1.0
│ │   │ │ │ │ └── ansi-regex@6.0.1
│ │   │ │ │ ├─┬ wrap-ansi-cjs@npm:wrap-ansi@7.0.0
│ │   │ │ │ │ ├─┬ ansi-styles@4.3.0
│ │   │ │ │ │ │ └─┬ color-convert@2.0.1
│ │   │ │ │ │ │   └── color-name@1.1.4
│ │   │ │ │ │ ├── string-width@4.2.3 deduped
│ │   │ │ │ │ └── strip-ansi@6.0.1 deduped
│ │   │ │ │ └─┬ wrap-ansi@8.1.0
│ │   │ │ │   ├── ansi-styles@6.2.1
│ │   │ │ │   ├── string-width@5.1.2 deduped
│ │   │ │ │   └── strip-ansi@7.1.0 deduped
│ │   │ │ └── @pkgjs/parseargs@0.11.0
│ │   │ ├─┬ minimatch@9.0.5
│ │   │ │ └─┬ brace-expansion@2.0.1
│ │   │ │   └── balanced-match@1.0.2 deduped
│ │   │ ├── minipass@7.1.2
│ │   │ ├── package-json-from-dist@1.0.0
│ │   │ └─┬ path-scurry@1.11.1
│ │   │   ├── lru-cache@10.3.0
│ │   │   └── minipass@7.1.2 deduped
│ │   ├── lines-and-columns@1.2.4
│ │   ├─┬ mz@2.7.0
│ │   │ ├── any-promise@1.3.0
│ │   │ ├── object-assign@4.1.1 deduped
│ │   │ └─┬ thenify-all@1.6.0
│ │   │   └─┬ thenify@3.3.1
│ │   │     └── any-promise@1.3.0 deduped
│ │   ├── pirates@4.0.6 deduped
│ │   └── ts-interface-checker@0.1.13
│ ├─┬ terser-webpack-plugin@5.3.10
│ │ ├─┬ @jridgewell/trace-mapping@0.3.25
│ │ │ ├── @jridgewell/resolve-uri@3.1.2
│ │ │ └── @jridgewell/sourcemap-codec@1.4.15
│ │ ├── jest-worker@27.5.1 deduped
│ │ ├─┬ schema-utils@3.3.0
│ │ │ ├── @types/json-schema@7.0.15 deduped
│ │ │ ├── ajv-keywords@3.5.2 deduped
│ │ │ └── ajv@6.12.6 deduped
│ │ ├── serialize-javascript@6.0.2 deduped
│ │ ├─┬ terser@5.31.1
│ │ │ ├─┬ @jridgewell/source-map@0.3.6
│ │ │ │ ├── @jridgewell/gen-mapping@0.3.5 deduped
│ │ │ │ └── @jridgewell/trace-mapping@0.3.25 deduped
│ │ │ ├── acorn@8.12.0 deduped
│ │ │ ├── commander@2.20.3
│ │ │ └─┬ source-map-support@0.5.21
│ │ │   ├── buffer-from@1.1.2
│ │ │   └── source-map@0.6.1
│ │ └── webpack@5.92.1 deduped
│ ├── typescript@4.9.5
│ ├─┬ webpack-dev-server@4.15.2
│ │ ├─┬ @types/bonjour@3.5.13
│ │ │ └── @types/node@20.14.9 deduped
│ │ ├─┬ @types/connect-history-api-fallback@1.5.4
│ │ │ ├─┬ @types/express-serve-static-core@4.19.5
│ │ │ │ ├── @types/node@20.14.9 deduped
│ │ │ │ ├── @types/qs@6.9.15 deduped
│ │ │ │ ├── @types/range-parser@1.2.7
│ │ │ │ └── @types/send@0.17.4 deduped
│ │ │ └── @types/node@20.14.9 deduped
│ │ ├─┬ @types/express@4.17.21
│ │ │ ├─┬ @types/body-parser@1.19.5
│ │ │ │ ├─┬ @types/connect@3.4.38
│ │ │ │ │ └── @types/node@20.14.9 deduped
│ │ │ │ └── @types/node@20.14.9 deduped
│ │ │ ├── @types/express-serve-static-core@4.19.5 deduped
│ │ │ ├── @types/qs@6.9.15
│ │ │ └── @types/serve-static@1.15.7 deduped
│ │ ├─┬ @types/serve-index@1.9.4
│ │ │ └── @types/express@4.17.21 deduped
│ │ ├─┬ @types/serve-static@1.15.7
│ │ │ ├── @types/http-errors@2.0.4
│ │ │ ├── @types/node@20.14.9 deduped
│ │ │ └─┬ @types/send@0.17.4
│ │ │   ├── @types/mime@1.3.5
│ │ │   └── @types/node@20.14.9 deduped
│ │ ├─┬ @types/sockjs@0.3.36
│ │ │ └── @types/node@20.14.9 deduped
│ │ ├─┬ @types/ws@8.5.10
│ │ │ └── @types/node@20.14.9 deduped
│ │ ├── ansi-html-community@0.0.8
│ │ ├─┬ bonjour-service@1.2.1
│ │ │ ├── fast-deep-equal@3.1.3 deduped
│ │ │ └─┬ multicast-dns@7.2.5
│ │ │   ├─┬ dns-packet@5.6.1
│ │ │   │ └── @leichtgewicht/ip-codec@2.0.5
│ │ │   └── thunky@1.1.0
│ │ ├── chokidar@3.6.0 deduped
│ │ ├── colorette@2.0.20
│ │ ├─┬ compression@1.7.4
│ │ │ ├─┬ accepts@1.3.8
│ │ │ │ ├── mime-types@2.1.35 deduped
│ │ │ │ └── negotiator@0.6.3
│ │ │ ├── bytes@3.0.0
│ │ │ ├─┬ compressible@2.0.18
│ │ │ │ └── mime-db@1.52.0 deduped
│ │ │ ├─┬ debug@2.6.9
│ │ │ │ └── ms@2.0.0
│ │ │ ├── on-headers@1.0.2
│ │ │ ├── safe-buffer@5.1.2
│ │ │ └── vary@1.1.2
│ │ ├── connect-history-api-fallback@2.0.0
│ │ ├─┬ default-gateway@6.0.3
│ │ │ └─┬ execa@5.1.1
│ │ │   ├── cross-spawn@7.0.3 deduped
│ │ │   ├── get-stream@6.0.1
│ │ │   ├── human-signals@2.1.0
│ │ │   ├── is-stream@2.0.1
│ │ │   ├── merge-stream@2.0.0 deduped
│ │ │   ├─┬ npm-run-path@4.0.1
│ │ │   │ └── path-key@3.1.1 deduped
│ │ │   ├─┬ onetime@5.1.2
│ │ │   │ └── mimic-fn@2.1.0
│ │ │   ├── signal-exit@3.0.7 deduped
│ │ │   └── strip-final-newline@2.0.0
│ │ ├─┬ express@4.19.2
│ │ │ ├── accepts@1.3.8 deduped
│ │ │ ├── array-flatten@1.1.1
│ │ │ ├─┬ body-parser@1.20.2
│ │ │ │ ├── bytes@3.1.2
│ │ │ │ ├── content-type@1.0.5 deduped
│ │ │ │ ├─┬ debug@2.6.9
│ │ │ │ │ └── ms@2.0.0
│ │ │ │ ├── depd@2.0.0 deduped
│ │ │ │ ├── destroy@1.2.0
│ │ │ │ ├── http-errors@2.0.0 deduped
│ │ │ │ ├─┬ iconv-lite@0.4.24
│ │ │ │ │ └── safer-buffer@2.1.2 deduped
│ │ │ │ ├── on-finished@2.4.1 deduped
│ │ │ │ ├── qs@6.11.0 deduped
│ │ │ │ ├─┬ raw-body@2.5.2
│ │ │ │ │ ├── bytes@3.1.2
│ │ │ │ │ ├── http-errors@2.0.0 deduped
│ │ │ │ │ ├─┬ iconv-lite@0.4.24
│ │ │ │ │ │ └── safer-buffer@2.1.2 deduped
│ │ │ │ │ └── unpipe@1.0.0 deduped
│ │ │ │ ├── type-is@1.6.18 deduped
│ │ │ │ └── unpipe@1.0.0
│ │ │ ├─┬ content-disposition@0.5.4
│ │ │ │ └── safe-buffer@5.2.1 deduped
│ │ │ ├── content-type@1.0.5
│ │ │ ├── cookie-signature@1.0.6
│ │ │ ├── cookie@0.6.0
│ │ │ ├─┬ debug@2.6.9
│ │ │ │ └── ms@2.0.0
│ │ │ ├── depd@2.0.0
│ │ │ ├── encodeurl@1.0.2
│ │ │ ├── escape-html@1.0.3
│ │ │ ├── etag@1.8.1
│ │ │ ├─┬ finalhandler@1.2.0
│ │ │ │ ├─┬ debug@2.6.9
│ │ │ │ │ └── ms@2.0.0
│ │ │ │ ├── encodeurl@1.0.2 deduped
│ │ │ │ ├── escape-html@1.0.3 deduped
│ │ │ │ ├── on-finished@2.4.1 deduped
│ │ │ │ ├── parseurl@1.3.3 deduped
│ │ │ │ ├── statuses@2.0.1 deduped
│ │ │ │ └── unpipe@1.0.0 deduped
│ │ │ ├── fresh@0.5.2
│ │ │ ├─┬ http-errors@2.0.0
│ │ │ │ ├── depd@2.0.0 deduped
│ │ │ │ ├── inherits@2.0.4 deduped
│ │ │ │ ├── setprototypeof@1.2.0 deduped
│ │ │ │ ├── statuses@2.0.1 deduped
│ │ │ │ └── toidentifier@1.0.1
│ │ │ ├── merge-descriptors@1.0.1
│ │ │ ├── methods@1.1.2
│ │ │ ├─┬ on-finished@2.4.1
│ │ │ │ └── ee-first@1.1.1
│ │ │ ├── parseurl@1.3.3
│ │ │ ├── path-to-regexp@0.1.7
│ │ │ ├─┬ proxy-addr@2.0.7
│ │ │ │ ├── forwarded@0.2.0
│ │ │ │ └── ipaddr.js@1.9.1
│ │ │ ├─┬ qs@6.11.0
│ │ │ │ └── side-channel@1.0.6 deduped
│ │ │ ├── range-parser@1.2.1
│ │ │ ├── safe-buffer@5.2.1
│ │ │ ├─┬ send@0.18.0
│ │ │ │ ├─┬ debug@2.6.9
│ │ │ │ │ └── ms@2.0.0
│ │ │ │ ├── depd@2.0.0 deduped
│ │ │ │ ├── destroy@1.2.0 deduped
│ │ │ │ ├── encodeurl@1.0.2 deduped
│ │ │ │ ├── escape-html@1.0.3 deduped
│ │ │ │ ├── etag@1.8.1 deduped
│ │ │ │ ├── fresh@0.5.2 deduped
│ │ │ │ ├── http-errors@2.0.0 deduped
│ │ │ │ ├── mime@1.6.0
│ │ │ │ ├── ms@2.1.3
│ │ │ │ ├── on-finished@2.4.1 deduped
│ │ │ │ ├── range-parser@1.2.1 deduped
│ │ │ │ └── statuses@2.0.1 deduped
│ │ │ ├─┬ serve-static@1.15.0
│ │ │ │ ├── encodeurl@1.0.2 deduped
│ │ │ │ ├── escape-html@1.0.3 deduped
│ │ │ │ ├── parseurl@1.3.3 deduped
│ │ │ │ └── send@0.18.0 deduped
│ │ │ ├── setprototypeof@1.2.0
│ │ │ ├── statuses@2.0.1
│ │ │ ├─┬ type-is@1.6.18
│ │ │ │ ├── media-typer@0.3.0
│ │ │ │ └── mime-types@2.1.35 deduped
│ │ │ ├── utils-merge@1.0.1
│ │ │ └── vary@1.1.2 deduped
│ │ ├── graceful-fs@4.2.11 deduped
│ │ ├── html-entities@2.5.2 deduped
│ │ ├─┬ http-proxy-middleware@2.0.6
│ │ │ ├── @types/express@4.17.21 deduped
│ │ │ ├─┬ @types/http-proxy@1.17.14
│ │ │ │ └── @types/node@20.14.9 deduped
│ │ │ ├─┬ http-proxy@1.18.1
│ │ │ │ ├── eventemitter3@4.0.7
│ │ │ │ ├── follow-redirects@1.15.6
│ │ │ │ └── requires-port@1.0.0
│ │ │ ├── is-glob@4.0.3 deduped
│ │ │ ├── is-plain-obj@3.0.0
│ │ │ └── micromatch@4.0.7 deduped
│ │ ├── ipaddr.js@2.2.0
│ │ ├─┬ launch-editor@2.8.0
│ │ │ ├── picocolors@1.0.1 deduped
│ │ │ └── shell-quote@1.8.1 deduped
│ │ ├── open@8.4.2 deduped
│ │ ├─┬ p-retry@4.6.2
│ │ │ ├── @types/retry@0.12.0
│ │ │ └── retry@0.13.1
│ │ ├─┬ rimraf@3.0.2
│ │ │ └── glob@7.2.3 deduped
│ │ ├── schema-utils@4.2.0 deduped
│ │ ├─┬ selfsigned@2.4.1
│ │ │ ├─┬ @types/node-forge@1.3.11
│ │ │ │ └── @types/node@20.14.9 deduped
│ │ │ └── node-forge@1.3.1
│ │ ├─┬ serve-index@1.9.1
│ │ │ ├── accepts@1.3.8 deduped
│ │ │ ├── batch@0.6.1
│ │ │ ├─┬ debug@2.6.9
│ │ │ │ └── ms@2.0.0
│ │ │ ├── escape-html@1.0.3 deduped
│ │ │ ├─┬ http-errors@1.6.3
│ │ │ │ ├── depd@1.1.2
│ │ │ │ ├── inherits@2.0.3
│ │ │ │ ├── setprototypeof@1.1.0
│ │ │ │ └── statuses@1.5.0
│ │ │ ├── mime-types@2.1.35 deduped
│ │ │ └── parseurl@1.3.3 deduped
│ │ ├─┬ sockjs@0.3.24
│ │ │ ├─┬ faye-websocket@0.11.4
│ │ │ │ └── websocket-driver@0.7.4 deduped
│ │ │ ├── uuid@8.3.2
│ │ │ └─┬ websocket-driver@0.7.4
│ │ │   ├── http-parser-js@0.5.8
│ │ │   ├── safe-buffer@5.2.1 deduped
│ │ │   └── websocket-extensions@0.1.4
│ │ ├─┬ spdy@4.0.2
│ │ │ ├── debug@4.3.5 deduped
│ │ │ ├── handle-thing@2.0.1
│ │ │ ├── http-deceiver@1.2.7
│ │ │ ├── select-hose@2.0.0
│ │ │ └─┬ spdy-transport@3.0.0
│ │ │   ├── debug@4.3.5 deduped
│ │ │   ├── detect-node@2.1.0
│ │ │   ├─┬ hpack.js@2.1.6
│ │ │   │ ├── inherits@2.0.4 deduped
│ │ │   │ ├── obuf@1.1.2 deduped
│ │ │   │ ├─┬ readable-stream@2.3.8
│ │ │   │ │ ├── core-util-is@1.0.3
│ │ │   │ │ ├── inherits@2.0.4 deduped
│ │ │   │ │ ├── isarray@1.0.0
│ │ │   │ │ ├── process-nextick-args@2.0.1
│ │ │   │ │ ├── safe-buffer@5.1.2
│ │ │   │ │ ├─┬ string_decoder@1.1.1
│ │ │   │ │ │ └── safe-buffer@5.1.2 deduped
│ │ │   │ │ └── util-deprecate@1.0.2 deduped
│ │ │   │ └── wbuf@1.7.3 deduped
│ │ │   ├── obuf@1.1.2
│ │ │   ├─┬ readable-stream@3.6.2
│ │ │   │ ├── inherits@2.0.4 deduped
│ │ │   │ ├─┬ string_decoder@1.3.0
│ │ │   │ │ └── safe-buffer@5.2.1 deduped
│ │ │   │ └── util-deprecate@1.0.2 deduped
│ │ │   └─┬ wbuf@1.7.3
│ │ │     └── minimalistic-assert@1.0.1
│ │ ├─┬ webpack-dev-middleware@5.3.4
│ │ │ ├── colorette@2.0.20 deduped
│ │ │ ├── memfs@3.5.3 deduped
│ │ │ ├── mime-types@2.1.35 deduped
│ │ │ ├── range-parser@1.2.1 deduped
│ │ │ ├── schema-utils@4.2.0 deduped
│ │ │ └── webpack@5.92.1 deduped
│ │ ├── webpack@5.92.1 deduped
│ │ └─┬ ws@8.17.1
│ │   ├── UNMET OPTIONAL DEPENDENCY bufferutil@^4.0.1
│ │   └── UNMET OPTIONAL DEPENDENCY utf-8-validate@>=5.0.2
│ ├─┬ webpack-manifest-plugin@4.1.1
│ │ ├── tapable@2.2.1 deduped
│ │ ├─┬ webpack-sources@2.3.1
│ │ │ ├── source-list-map@2.0.1
│ │ │ └── source-map@0.6.1
│ │ └── webpack@5.92.1 deduped
│ ├─┬ webpack@5.92.1
│ │ ├─┬ @types/eslint-scope@3.7.7
│ │ │ ├── @types/eslint@8.56.10 deduped
│ │ │ └── @types/estree@1.0.5 deduped
│ │ ├── @types/estree@1.0.5
│ │ ├─┬ @webassemblyjs/ast@1.12.1
│ │ │ ├─┬ @webassemblyjs/helper-numbers@1.11.6
│ │ │ │ ├── @webassemblyjs/floating-point-hex-parser@1.11.6
│ │ │ │ ├── @webassemblyjs/helper-api-error@1.11.6 deduped
│ │ │ │ └── @xtuc/long@4.2.2
│ │ │ └── @webassemblyjs/helper-wasm-bytecode@1.11.6
│ │ ├─┬ @webassemblyjs/wasm-edit@1.12.1
│ │ │ ├── @webassemblyjs/ast@1.12.1 deduped
│ │ │ ├── @webassemblyjs/helper-buffer@1.12.1
│ │ │ ├── @webassemblyjs/helper-wasm-bytecode@1.11.6 deduped
│ │ │ ├─┬ @webassemblyjs/helper-wasm-section@1.12.1
│ │ │ │ ├── @webassemblyjs/ast@1.12.1 deduped
│ │ │ │ ├── @webassemblyjs/helper-buffer@1.12.1 deduped
│ │ │ │ ├── @webassemblyjs/helper-wasm-bytecode@1.11.6 deduped
│ │ │ │ └── @webassemblyjs/wasm-gen@1.12.1 deduped
│ │ │ ├─┬ @webassemblyjs/wasm-gen@1.12.1
│ │ │ │ ├── @webassemblyjs/ast@1.12.1 deduped
│ │ │ │ ├── @webassemblyjs/helper-wasm-bytecode@1.11.6 deduped
│ │ │ │ ├── @webassemblyjs/ieee754@1.11.6 deduped
│ │ │ │ ├── @webassemblyjs/leb128@1.11.6 deduped
│ │ │ │ └── @webassemblyjs/utf8@1.11.6 deduped
│ │ │ ├─┬ @webassemblyjs/wasm-opt@1.12.1
│ │ │ │ ├── @webassemblyjs/ast@1.12.1 deduped
│ │ │ │ ├── @webassemblyjs/helper-buffer@1.12.1 deduped
│ │ │ │ ├── @webassemblyjs/wasm-gen@1.12.1 deduped
│ │ │ │ └── @webassemblyjs/wasm-parser@1.12.1 deduped
│ │ │ ├── @webassemblyjs/wasm-parser@1.12.1 deduped
│ │ │ └─┬ @webassemblyjs/wast-printer@1.12.1
│ │ │   ├── @webassemblyjs/ast@1.12.1 deduped
│ │ │   └── @xtuc/long@4.2.2 deduped
│ │ ├─┬ @webassemblyjs/wasm-parser@1.12.1
│ │ │ ├── @webassemblyjs/ast@1.12.1 deduped
│ │ │ ├── @webassemblyjs/helper-api-error@1.11.6
│ │ │ ├── @webassemblyjs/helper-wasm-bytecode@1.11.6 deduped
│ │ │ ├─┬ @webassemblyjs/ieee754@1.11.6
│ │ │ │ └── @xtuc/ieee754@1.2.0
│ │ │ ├─┬ @webassemblyjs/leb128@1.11.6
│ │ │ │ └── @xtuc/long@4.2.2 deduped
│ │ │ └── @webassemblyjs/utf8@1.11.6
│ │ ├─┬ acorn-import-attributes@1.9.5
│ │ │ └── acorn@8.12.0 deduped
│ │ ├── acorn@8.12.0
│ │ ├── browserslist@4.23.1 deduped
│ │ ├── chrome-trace-event@1.0.4
│ │ ├─┬ enhanced-resolve@5.17.0
│ │ │ ├── graceful-fs@4.2.11 deduped
│ │ │ └── tapable@2.2.1 deduped
│ │ ├── es-module-lexer@1.5.4
│ │ ├─┬ eslint-scope@5.1.1
│ │ │ ├── esrecurse@4.3.0 deduped
│ │ │ └── estraverse@4.3.0
│ │ ├── events@3.3.0
│ │ ├── glob-to-regexp@0.4.1
│ │ ├── graceful-fs@4.2.11 deduped
│ │ ├── json-parse-even-better-errors@2.3.1
│ │ ├── loader-runner@4.3.0
│ │ ├─┬ mime-types@2.1.35
│ │ │ └── mime-db@1.52.0
│ │ ├── neo-async@2.6.2 deduped
│ │ ├─┬ schema-utils@3.3.0
│ │ │ ├── @types/json-schema@7.0.15 deduped
│ │ │ ├── ajv-keywords@3.5.2 deduped
│ │ │ └── ajv@6.12.6 deduped
│ │ ├── tapable@2.2.1 deduped
│ │ ├── terser-webpack-plugin@5.3.10 deduped
│ │ ├─┬ watchpack@2.4.1
│ │ │ ├── glob-to-regexp@0.4.1 deduped
│ │ │ └── graceful-fs@4.2.11 deduped
│ │ └── webpack-sources@3.2.3
│ └─┬ workbox-webpack-plugin@6.6.0
│   ├── fast-json-stable-stringify@2.1.0
│   ├── pretty-bytes@5.6.0
│   ├── upath@1.2.0
│   ├─┬ webpack-sources@1.4.3
│   │ ├── source-list-map@2.0.1 deduped
│   │ └── source-map@0.6.1
│   ├── webpack@5.92.1 deduped
│   └─┬ workbox-build@6.6.0
│     ├─┬ @apideck/better-ajv-errors@0.3.6
│     │ ├── ajv@8.16.0 deduped
│     │ ├── json-schema@0.4.0
│     │ ├── jsonpointer@5.0.1
│     │ └── leven@3.1.0 deduped
│     ├── @babel/core@7.24.7 deduped
│     ├── @babel/preset-env@7.24.7 deduped
│     ├── @babel/runtime@7.24.7 deduped
│     ├─┬ @rollup/plugin-babel@5.3.1
│     │ ├── @babel/core@7.24.7 deduped
│     │ ├── @babel/helper-module-imports@7.24.7 deduped
│     │ ├─┬ @rollup/pluginutils@3.1.0
│     │ │ ├── @types/estree@0.0.39
│     │ │ ├── estree-walker@1.0.1
│     │ │ ├── picomatch@2.3.1 deduped
│     │ │ └── rollup@2.79.1 deduped
│     │ ├── @types/babel__core@7.20.5 deduped
│     │ └── rollup@2.79.1 deduped
│     ├─┬ @rollup/plugin-node-resolve@11.2.1
│     │ ├── @rollup/pluginutils@3.1.0 deduped
│     │ ├─┬ @types/resolve@1.17.1
│     │ │ └── @types/node@20.14.9 deduped
│     │ ├── builtin-modules@3.3.0
│     │ ├── deepmerge@4.3.1 deduped
│     │ ├── is-module@1.0.0
│     │ ├── resolve@1.22.8 deduped
│     │ └── rollup@2.79.1 deduped
│     ├─┬ @rollup/plugin-replace@2.4.2
│     │ ├── @rollup/pluginutils@3.1.0 deduped
│     │ ├─┬ magic-string@0.25.9
│     │ │ └── sourcemap-codec@1.4.8
│     │ └── rollup@2.79.1 deduped
│     ├─┬ @surma/rollup-plugin-off-main-thread@2.2.3
│     │ ├─┬ ejs@3.1.10
│     │ │ └─┬ jake@10.9.1
│     │ │   ├── async@3.2.5
│     │ │   ├─┬ chalk@4.1.2
│     │ │   │ ├─┬ ansi-styles@4.3.0
│     │ │   │ │ └─┬ color-convert@2.0.1
│     │ │   │ │   └── color-name@1.1.4
│     │ │   │ └─┬ supports-color@7.2.0
│     │ │   │   └── has-flag@4.0.0
│     │ │   ├─┬ filelist@1.0.4
│     │ │   │ └─┬ minimatch@5.1.6
│     │ │   │   └─┬ brace-expansion@2.0.1
│     │ │   │     └── balanced-match@1.0.2 deduped
│     │ │   └── minimatch@3.1.2 deduped
│     │ ├── json5@2.2.3 deduped
│     │ ├── magic-string@0.25.9 deduped
│     │ └── string.prototype.matchall@4.0.11 deduped
│     ├─┬ ajv@8.16.0
│     │ ├── fast-deep-equal@3.1.3 deduped
│     │ ├── json-schema-traverse@1.0.0
│     │ ├── require-from-string@2.0.2 deduped
│     │ └── uri-js@4.4.1 deduped
│     ├── common-tags@1.8.2
│     ├── fast-json-stable-stringify@2.1.0 deduped
│     ├─┬ fs-extra@9.1.0
│     │ ├── at-least-node@1.0.0 deduped
│     │ ├── graceful-fs@4.2.11 deduped
│     │ ├── jsonfile@6.1.0 deduped
│     │ └── universalify@2.0.1 deduped
│     ├── glob@7.2.3 deduped
│     ├── lodash@4.17.21 deduped
│     ├── pretty-bytes@5.6.0 deduped
│     ├─┬ rollup-plugin-terser@7.0.2
│     │ ├── @babel/code-frame@7.24.7 deduped
│     │ ├─┬ jest-worker@26.6.2
│     │ │ ├── @types/node@20.14.9 deduped
│     │ │ ├── merge-stream@2.0.0 deduped
│     │ │ └─┬ supports-color@7.2.0
│     │ │   └── has-flag@4.0.0
│     │ ├── rollup@2.79.1 deduped
│     │ ├─┬ serialize-javascript@4.0.0
│     │ │ └── randombytes@2.1.0 deduped
│     │ └── terser@5.31.1 deduped
│     ├─┬ rollup@2.79.1
│     │ └── UNMET OPTIONAL DEPENDENCY fsevents@~2.3.2
│     ├─┬ source-map@0.8.0-beta.0
│     │ └─┬ whatwg-url@7.1.0
│     │   ├── lodash.sortby@4.7.0
│     │   ├─┬ tr46@1.0.1
│     │   │ └── punycode@2.3.1 deduped
│     │   └── webidl-conversions@4.0.2
│     ├─┬ stringify-object@3.3.0
│     │ ├── get-own-enumerable-property-symbols@3.0.2
│     │ ├── is-obj@1.0.1
│     │ └── is-regexp@1.0.0
│     ├── strip-comments@2.0.1
│     ├─┬ tempy@0.6.0
│     │ ├── is-stream@2.0.1 deduped
│     │ ├── temp-dir@2.0.0
│     │ ├── type-fest@0.16.0
│     │ └─┬ unique-string@2.0.0
│     │   └── crypto-random-string@2.0.0
│     ├── upath@1.2.0 deduped
│     ├─┬ workbox-background-sync@6.6.0
│     │ ├── idb@7.1.1
│     │ └── workbox-core@6.6.0 deduped
│     ├─┬ workbox-broadcast-update@6.6.0
│     │ └── workbox-core@6.6.0 deduped
│     ├─┬ workbox-cacheable-response@6.6.0
│     │ └── workbox-core@6.6.0 deduped
│     ├── workbox-core@6.6.0
│     ├─┬ workbox-expiration@6.6.0
│     │ ├── idb@7.1.1 deduped
│     │ └── workbox-core@6.6.0 deduped
│     ├─┬ workbox-google-analytics@6.6.0
│     │ ├── workbox-background-sync@6.6.0 deduped
│     │ ├── workbox-core@6.6.0 deduped
│     │ ├── workbox-routing@6.6.0 deduped
│     │ └── workbox-strategies@6.6.0 deduped
│     ├─┬ workbox-navigation-preload@6.6.0
│     │ └── workbox-core@6.6.0 deduped
│     ├─┬ workbox-precaching@6.6.0
│     │ ├── workbox-core@6.6.0 deduped
│     │ ├── workbox-routing@6.6.0 deduped
│     │ └── workbox-strategies@6.6.0 deduped
│     ├─┬ workbox-range-requests@6.6.0
│     │ └── workbox-core@6.6.0 deduped
│     ├─┬ workbox-recipes@6.6.0
│     │ ├── workbox-cacheable-response@6.6.0 deduped
│     │ ├── workbox-core@6.6.0 deduped
│     │ ├── workbox-expiration@6.6.0 deduped
│     │ ├── workbox-precaching@6.6.0 deduped
│     │ ├── workbox-routing@6.6.0 deduped
│     │ └── workbox-strategies@6.6.0 deduped
│     ├─┬ workbox-routing@6.6.0
│     │ └── workbox-core@6.6.0 deduped
│     ├─┬ workbox-strategies@6.6.0
│     │ └── workbox-core@6.6.0 deduped
│     ├─┬ workbox-streams@6.6.0
│     │ ├── workbox-core@6.6.0 deduped
│     │ └── workbox-routing@6.6.0 deduped
│     ├── workbox-sw@6.6.0
│     └─┬ workbox-window@6.6.0
│       ├── @types/trusted-types@2.0.7
│       └── workbox-core@6.6.0 deduped
└─┬ react@18.3.1
  └── loose-envify@1.4.0 deduped
```

