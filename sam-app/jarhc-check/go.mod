module github.com/smarkwal/jarhc-online/sam-app/jarhc-check

go 1.17

require (
	github.com/aws/aws-lambda-go v1.28.0
	github.com/smarkwal/jarhc-online/sam-app/common v0.0.0-20220323182958-7631a7fe07e3
	github.com/stretchr/testify v1.7.0
)

require (
	github.com/aws/aws-sdk-go v1.43.12 // indirect
	github.com/davecgh/go-spew v1.1.1 // indirect
	github.com/jmespath/go-jmespath v0.4.0 // indirect
	github.com/pmezard/go-difflib v1.0.0 // indirect
	gopkg.in/yaml.v3 v3.0.0-20200615113413-eeeca48fe776 // indirect
)

replace gopkg.in/yaml.v2 => gopkg.in/yaml.v2 v2.2.8
