#!/usr/bin/bash

if [ -z "$PROJECT_ID" ]
then
    echo "PROJECT_ID environment variable not set"
    exit 1
fi

if [ -z "$API_KEY" ]
then
    echo "API_KEY environment variable not set"
    exit 1
fi

if [ -e payload.json ]
then
    rm -f payload.json
    echo "Deleted existing payload.json"
fi

./gradlew cyclonedxBom
echo "Generated  application.cdx.json file"
cat << EOF > payload.json
{
   "project": "$PROJECT_ID",
   "bom": "$(cat ./build/reports/application.cdx.json |base64 -w 0)"
}
EOF
echo "Created new payload.json"

echo "Pushing the sbom to Dependency tracker"
curl -X "PUT" "http://localhost:8081/api/v1/bom" \
     -H "Content-Type: application/json" \
     -H "X-Api-Key: $API_KEY" \
     -d @payload.json && echo ""
echo "Done"
