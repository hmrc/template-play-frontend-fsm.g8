![GitHub release (latest by date)](https://img.shields.io/github/v/release/hmrc/$serviceNameHyphen$) ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/hmrc/$serviceNameHyphen$) ![GitHub last commit](https://img.shields.io/github/last-commit/hmrc/$serviceNameHyphen$)

# $serviceNameHyphen$

HMRC Digital frontend microservice.

## Running the tests

    sbt test it:test

## Running the tests with coverage

    sbt clean coverageOn test it:test coverageReport

## Running the app locally

    sm --start $servicePrefixSnake$_ALL
    sm --stop $serviceNameSnake$ 
    sbt run

It should then be listening on port $serviceTargetPort$

    browse http://localhost:$serviceTargetPort$/$serviceUrlPrefix$

### License


This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
