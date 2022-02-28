#!/bin/bash

# see https://docs.aws.amazon.com/cli/latest/reference/s3/sync.html
aws s3 sync build/. s3://online.jarhc.org --delete --exclude reports/* # --dryrun
