
# Dependencies

## Production

### Direct

`npm ls --omit=dev`

```
react-app@1.0.0 /home/markwalder/Dev/IntelliJ/jarhc-online/react-app
├── react-debounce-input@3.3.0
├── react-dom@18.3.1
├── react-router-dom@6.26.2
└── react@18.3.1
```

### Transitive

`npm ls --omit=dev --all`

```
react-app@1.0.0 /home/markwalder/Dev/IntelliJ/jarhc-online/react-app
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
react-app@1.0.0 /home/markwalder/Dev/IntelliJ/jarhc-online/react-app
├── @vitejs/plugin-react@4.3.2
├── react-debounce-input@3.3.0
├── react-dom@18.3.1
├── react-router-dom@6.26.2
├── react@18.3.1
└── vite@5.4.8
```

### Transitive

`npm ls --include=dev --all`

```
react-app@1.0.0 /home/markwalder/Dev/IntelliJ/jarhc-online/react-app
├─┬ @vitejs/plugin-react@4.3.2
│ ├─┬ @babel/core@7.25.7
│ │ ├─┬ @ampproject/remapping@2.3.0
│ │ │ ├─┬ @jridgewell/gen-mapping@0.3.5
│ │ │ │ ├── @jridgewell/set-array@1.2.1
│ │ │ │ ├── @jridgewell/sourcemap-codec@1.5.0
│ │ │ │ └── @jridgewell/trace-mapping@0.3.25 deduped
│ │ │ └─┬ @jridgewell/trace-mapping@0.3.25
│ │ │   ├── @jridgewell/resolve-uri@3.1.2
│ │ │   └── @jridgewell/sourcemap-codec@1.5.0 deduped
│ │ ├─┬ @babel/code-frame@7.25.7
│ │ │ ├─┬ @babel/highlight@7.25.7
│ │ │ │ ├── @babel/helper-validator-identifier@7.25.7 deduped
│ │ │ │ ├─┬ chalk@2.4.2
│ │ │ │ │ ├─┬ ansi-styles@3.2.1
│ │ │ │ │ │ └─┬ color-convert@1.9.3
│ │ │ │ │ │   └── color-name@1.1.3
│ │ │ │ │ ├── escape-string-regexp@1.0.5
│ │ │ │ │ └─┬ supports-color@5.5.0
│ │ │ │ │   └── has-flag@3.0.0
│ │ │ │ ├── js-tokens@4.0.0 deduped
│ │ │ │ └── picocolors@1.1.0 deduped
│ │ │ └── picocolors@1.1.0 deduped
│ │ ├─┬ @babel/generator@7.25.7
│ │ │ ├── @babel/types@7.25.7 deduped
│ │ │ ├── @jridgewell/gen-mapping@0.3.5 deduped
│ │ │ ├── @jridgewell/trace-mapping@0.3.25 deduped
│ │ │ └── jsesc@3.0.2
│ │ ├─┬ @babel/helper-compilation-targets@7.25.7
│ │ │ ├── @babel/compat-data@7.25.7
│ │ │ ├── @babel/helper-validator-option@7.25.7
│ │ │ ├─┬ browserslist@4.24.0
│ │ │ │ ├── caniuse-lite@1.0.30001667
│ │ │ │ ├── electron-to-chromium@1.5.33
│ │ │ │ ├── node-releases@2.0.18
│ │ │ │ └─┬ update-browserslist-db@1.1.1
│ │ │ │   ├── browserslist@4.24.0 deduped
│ │ │ │   ├── escalade@3.2.0
│ │ │ │   └── picocolors@1.1.0 deduped
│ │ │ ├─┬ lru-cache@5.1.1
│ │ │ │ └── yallist@3.1.1
│ │ │ └── semver@6.3.1 deduped
│ │ ├─┬ @babel/helper-module-transforms@7.25.7
│ │ │ ├── @babel/core@7.25.7 deduped
│ │ │ ├─┬ @babel/helper-module-imports@7.25.7
│ │ │ │ ├── @babel/traverse@7.25.7 deduped
│ │ │ │ └── @babel/types@7.25.7 deduped
│ │ │ ├─┬ @babel/helper-simple-access@7.25.7
│ │ │ │ ├── @babel/traverse@7.25.7 deduped
│ │ │ │ └── @babel/types@7.25.7 deduped
│ │ │ ├── @babel/helper-validator-identifier@7.25.7
│ │ │ └── @babel/traverse@7.25.7 deduped
│ │ ├─┬ @babel/helpers@7.25.7
│ │ │ ├── @babel/template@7.25.7 deduped
│ │ │ └── @babel/types@7.25.7 deduped
│ │ ├─┬ @babel/parser@7.25.7
│ │ │ └── @babel/types@7.25.7 deduped
│ │ ├─┬ @babel/template@7.25.7
│ │ │ ├── @babel/code-frame@7.25.7 deduped
│ │ │ ├── @babel/parser@7.25.7 deduped
│ │ │ └── @babel/types@7.25.7 deduped
│ │ ├─┬ @babel/traverse@7.25.7
│ │ │ ├── @babel/code-frame@7.25.7 deduped
│ │ │ ├── @babel/generator@7.25.7 deduped
│ │ │ ├── @babel/parser@7.25.7 deduped
│ │ │ ├── @babel/template@7.25.7 deduped
│ │ │ ├── @babel/types@7.25.7 deduped
│ │ │ ├── debug@4.3.7 deduped
│ │ │ └── globals@11.12.0
│ │ ├─┬ @babel/types@7.25.7
│ │ │ ├── @babel/helper-string-parser@7.25.7
│ │ │ ├── @babel/helper-validator-identifier@7.25.7 deduped
│ │ │ └── to-fast-properties@2.0.0
│ │ ├── convert-source-map@2.0.0
│ │ ├─┬ debug@4.3.7
│ │ │ └── ms@2.1.3
│ │ ├── gensync@1.0.0-beta.2
│ │ ├── json5@2.2.3
│ │ └── semver@6.3.1
│ ├─┬ @babel/plugin-transform-react-jsx-self@7.25.7
│ │ ├── @babel/core@7.25.7 deduped
│ │ └── @babel/helper-plugin-utils@7.25.7
│ ├─┬ @babel/plugin-transform-react-jsx-source@7.25.7
│ │ ├── @babel/core@7.25.7 deduped
│ │ └── @babel/helper-plugin-utils@7.25.7 deduped
│ ├─┬ @types/babel__core@7.20.5
│ │ ├── @babel/parser@7.25.7 deduped
│ │ ├── @babel/types@7.25.7 deduped
│ │ ├─┬ @types/babel__generator@7.6.8
│ │ │ └── @babel/types@7.25.7 deduped
│ │ ├─┬ @types/babel__template@7.4.4
│ │ │ ├── @babel/parser@7.25.7 deduped
│ │ │ └── @babel/types@7.25.7 deduped
│ │ └─┬ @types/babel__traverse@7.20.6
│ │   └── @babel/types@7.25.7 deduped
│ ├── react-refresh@0.14.2
│ └── vite@5.4.8 deduped
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
├─┬ react@18.3.1
│ └── loose-envify@1.4.0 deduped
└─┬ vite@5.4.8
  ├── UNMET OPTIONAL DEPENDENCY @types/node@^18.0.0 || >=20.0.0
  ├─┬ esbuild@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/aix-ppc64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/android-arm@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/android-arm64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/android-x64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/darwin-arm64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/darwin-x64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/freebsd-arm64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/freebsd-x64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/linux-arm@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/linux-arm64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/linux-ia32@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/linux-loong64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/linux-mips64el@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/linux-ppc64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/linux-riscv64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/linux-s390x@0.21.5
  │ ├── @esbuild/linux-x64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/netbsd-x64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/openbsd-x64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/sunos-x64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/win32-arm64@0.21.5
  │ ├── UNMET OPTIONAL DEPENDENCY @esbuild/win32-ia32@0.21.5
  │ └── UNMET OPTIONAL DEPENDENCY @esbuild/win32-x64@0.21.5
  ├── UNMET OPTIONAL DEPENDENCY fsevents@~2.3.3
  ├── UNMET OPTIONAL DEPENDENCY less@*
  ├── UNMET OPTIONAL DEPENDENCY lightningcss@^1.21.0
  ├─┬ postcss@8.4.47
  │ ├── nanoid@3.3.7
  │ ├── picocolors@1.1.0
  │ └── source-map-js@1.2.1
  ├─┬ rollup@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-android-arm-eabi@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-android-arm64@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-darwin-arm64@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-darwin-x64@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-linux-arm-gnueabihf@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-linux-arm-musleabihf@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-linux-arm64-gnu@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-linux-arm64-musl@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-linux-powerpc64le-gnu@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-linux-riscv64-gnu@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-linux-s390x-gnu@4.24.0
  │ ├── @rollup/rollup-linux-x64-gnu@4.24.0
  │ ├── @rollup/rollup-linux-x64-musl@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-win32-arm64-msvc@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-win32-ia32-msvc@4.24.0
  │ ├── UNMET OPTIONAL DEPENDENCY @rollup/rollup-win32-x64-msvc@4.24.0
  │ ├── @types/estree@1.0.6
  │ └── UNMET OPTIONAL DEPENDENCY fsevents@~2.3.2
  ├── UNMET OPTIONAL DEPENDENCY sass-embedded@*
  ├── UNMET OPTIONAL DEPENDENCY sass@*
  ├── UNMET OPTIONAL DEPENDENCY stylus@*
  ├── UNMET OPTIONAL DEPENDENCY sugarss@*
  └── UNMET OPTIONAL DEPENDENCY terser@^5.4.0
```

