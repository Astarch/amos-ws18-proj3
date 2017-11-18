#!/bin/bash
if [ "${CIRCLE_BRANCH}" == "master" ]
then
  export AWS_BUCKET_NAME=pretrendrfrontend
  aws s3 sync ~/repo/web/big-data-ui/dist s3://pretrendrfrontend/ --region us-east-2 --acl public-read --delete 
else
  export AWS_BUCKET_NAME=pretrendrfrontendstaging
  aws s3 sync ~/repo/web/big-data-ui/dist s3://pretrendrfrontendstaging/ --region us-east-2 --acl public-read --delete 
fi

echo "Sucessfull deployed frontend on: ${AWS_BUCKET_NAME}"

exit 0
