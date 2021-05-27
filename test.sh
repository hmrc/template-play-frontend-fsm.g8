#!/usr/bin/env bash

if [[ -d ./src/main/g8 ]]; then

    if ! command -v g8 &> /dev/null
    then
        echo "[ERROR] g8 command cannot be found, please install g8 following http://www.foundweekends.org/giter8/setup.html"
        exit -1
    fi

    export TEMPLATE=`pwd | xargs basename`

    echo "Creating new project target/sandbox/new-shiny-frontend from the ${TEMPLATE} template ..."
    
    mkdir -p target/sandbox
    cd target/sandbox
    find . -not -name .git -delete

    g8 file://../../../${TEMPLATE} --serviceName="New Shiny Frontend" --serviceUrlPrefix="new-shiny-service" --serviceTargetPort="9379" --authorisedIdentifierKey="FooNumber" --authorisedServiceName="HMRC-NEW-SHINY" --servicePrefix="NewShiny" --package="uk.gov.hmrc.newshiny" -o new-shiny-frontend "$@"

    if [[ -d ./new-shiny-frontend ]]; then
        cd new-shiny-frontend
        git init
	git add .
	git commit -m start
        sbt test it:test
        echo "Done, created new project in target/sandbox/new-shiny-frontend"
        exit 0
    else
        echo "[ERROR] something went wrong, project has not been created in target/sandbox/new-shiny-frontend"
        exit -1
    fi

else

    echo "[ERROR] run the script ./test.sh in the template's root folder"
    exit -1

fi