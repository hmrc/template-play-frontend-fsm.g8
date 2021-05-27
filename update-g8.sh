#!/usr/bin/env bash

if [[ -d ./src/main/g8 ]]; then

   if ! command -v git &> /dev/null
   then
     echo "[ERROR] git command cannot be found, please install git first"
     exit -1
   fi

   if ! command -v sbt &> /dev/null
   then
     echo "[ERROR] sbt command cannot be found, please install sbt first"
     exit -1
   fi

   mkdir -p target
   cd target
   if [[ -d .makeitg8 ]] && [[ -d .makeitg8/.git ]] ; then
        cd .makeitg8
        git pull origin master
   else
        rm -r .makeitg8
        git clone https://github.com/arturopala/make-it-g8.git .makeitg8
        cd .makeitg8
   fi

   sbt "run --noclear --force --source ../../target/sandbox/new-shiny-frontend --target ../.. --name template-play-frontend-fsm.g8  --package uk.gov.hmrc.newshiny --description HMRC+Digital+Scala+Play+2.8+Stateful+Frontend+Microservice  -K serviceName=New+Shiny+Frontend serviceUrlPrefix=new-shiny-service serviceTargetPort=9379 templateGithubUser=hmrc authorisedIdentifierKey=FooNumber authorisedServiceName=HMRC-NEW-SHINY servicePrefix=NewShiny" -Dbuild.test.command="sbt test it:test" 

   echo "Done, updated the template based on target/sandbox/new-shiny-frontend"
   exit 0

else

    echo "[ERROR] run the script ./update-g8.sh in the template's root folder"
    exit -1

fi
