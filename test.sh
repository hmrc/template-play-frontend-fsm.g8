#!/usr/bin/env bash

if [[ -f ./build.sbt ]] && [[ -d ./src/main/g8 ]]; then

    export TEMPLATE=`pwd | xargs basename`
    echo ${TEMPLATE}
    mkdir -p target/sandbox
    cd target/sandbox
    sudo rm -r trader-services-route-one-frontend
    g8 file://../../../${TEMPLATE} --serviceName="Trader Services Route One Frontend" --serviceUrlPrefix="send-documents-for-customs-check" --serviceTargetPort="9379" --authorisedIdentifierKey="EORINumber" --serviceTitle="Send Documents For Customs Check" --authorisedServiceName="HMRC-CUS-ORG" --servicePrefix="Trader Services" --package="uk.gov.hmrc.traderservices" -o trader-services-route-one-frontend "$@"
    cd trader-services-route-one-frontend
    git init
	git add .
	git commit -m start
    sbt test it:test

else

    echo "WARNING: run the script ./test.sh in the template root folder"
    exit -1

fi