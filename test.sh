#!/usr/bin/env bash

if [[ -d ./src/main/g8 ]]; then

    if ! command -v g8 &> /dev/null
    then
        echo "[ERROR] g8 command cannot be found, please install g8 following http://www.foundweekends.org/giter8/setup.html"
        exit -1
    fi

    export TEMPLATE=`pwd | xargs basename`

    echo "Creating new project target/sandbox/trader-services-route-one-frontend from the ${TEMPLATE} template ..."
    
    mkdir -p target/sandbox
    cd target/sandbox
    find . -not -name .git -delete

    g8 file://../../../${TEMPLATE} --serviceName="Trader Services Route One Frontend" --serviceUrlPrefix="send-documents-for-customs-check" --serviceTargetPort="9379" --authorisedIdentifierKey="EORINumber" --serviceTitle="Send Documents For Customs Check" --authorisedServiceName="HMRC-CUS-ORG" --servicePrefix="Trader Services" --package="uk.gov.hmrc.traderservices" -o trader-services-route-one-frontend "$@"

    if [[ -d ./trader-services-route-one-frontend ]]; then
        cd trader-services-route-one-frontend
        git init
	git add .
	git commit -m start
        sbt test it:test
        echo "Done, created new project in target/sandbox/trader-services-route-one-frontend"
        exit 0
    else
        echo "[ERROR] something went wrong, project has not been created in target/sandbox/trader-services-route-one-frontend"
        exit -1
    fi

else

    echo "[ERROR] run the script ./test.sh in the template's root folder"
    exit -1

fi