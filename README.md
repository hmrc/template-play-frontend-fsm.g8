A [Giter8](http://www.foundweekends.org/giter8/) template for creating HMRC Digital Scala Play 2.7 Stateful Frontend Microservice
==

How to create a new project based on the template?
--

* Install g8 commandline tool (http://www.foundweekends.org/giter8/setup.html)
* Go to the directory where you want to create the template
* Decide your service name (the hardest part :))
* Run the command

    `g8 {GITHUB_USER}/template-play-27-frontend-fsm --serviceName="Trader Services Route One Frontend" --serviceUrlPrefix="send-documents-for-customs-check" --serviceTargetPort="9379" --authorisedIdentifierKey="EORINumber" --serviceTitle="Send Documents For Customs Check" --authorisedServiceName="HMRC-CUS-ORG" --servicePrefix="Trader Services" --package="uk.gov.hmrc.traderservices" -o trader-services-route-one-frontend`
    
and then
    
    cd trader-services-route-one-frontend
    git init
	git add .
	git commit -m start
  
* Test generated project using command 

    `sbt test it:test`
    

How to test the template and generate an example project?
--

* Run `./test.sh` 

An example project will be then created and tested in `target/sandbox/trader-services-route-one-frontend`

How to modify the template?
--

 * review template sources in `/src/main/g8`
 * modify files as you need, but be careful about placeholders, paths and so on
 * run `./test.sh` in template root to validate your changes
 
or (safer) ...

* run `./test.sh` first
* open `target/sandbox/trader-services-route-one-frontend` in your preferred IDE, 
* modify the generated example project as you wish, 
* build and test it as usual, you can run `sbt test it:test`,
* when you are done switch back to the template root
* run `./update-g8.sh` in order to port your changes back to the template.
* run `./test.sh` again to validate your changes

What is in the template?
--

Assuming the command above 
the template will supply the following values for the placeholders:

    $packaged$ -> uk/gov/hmrc/traderservices
	$package$ -> uk.gov.hmrc.traderservices
	$serviceNameCamel$ -> TraderServicesRouteOneFrontend
	$serviceNamecamel$ -> traderServicesRouteOneFrontend
	$serviceNameNoSpaceLowercase$ -> traderservicesrouteonefrontend
	$serviceNameNoSpaceUppercase$ -> TRADERSERVICESROUTEONEFRONTEND
	$serviceNamesnake$ -> trader_services_route_one_frontend
	$serviceNameSnake$ -> TRADER_SERVICES_ROUTE_ONE_FRONTEND
	$serviceNamePackage$ -> Trader.Services.Route.One.Frontend
	$serviceNamePackageLowercase$ -> trader.services.route.one.frontend
	$serviceNamePackaged$ -> Trader/Services/Route/One/Frontend
	$serviceNamePackagedLowercase$ -> trader/services/route/one/frontend
	$serviceNameHyphen$ -> trader-services-route-one-frontend
	$serviceNameLowercase$ -> trader services route one frontend
	$serviceNameUppercase$ -> TRADER SERVICES ROUTE ONE FRONTEND
	$serviceName$ -> Trader Services Route One Frontend
	$serviceUrlPrefixCamel$ -> SendDocumentsForCustomsCheck
	$serviceUrlPrefixcamel$ -> sendDocumentsForCustomsCheck
	$serviceUrlPrefixNoSpaceLowercase$ -> senddocumentsforcustomscheck
	$serviceUrlPrefixNoSpaceUppercase$ -> SENDDOCUMENTSFORCUSTOMSCHECK
	$serviceUrlPrefixsnake$ -> send_documents_for_customs_check
	$serviceUrlPrefixSnake$ -> SEND_DOCUMENTS_FOR_CUSTOMS_CHECK
	$serviceUrlPrefixPackage$ -> send.documents.for.customs.check
	$serviceUrlPrefixPackageLowercase$ -> send.documents.for.customs.check
	$serviceUrlPrefixPackaged$ -> send/documents/for/customs/check
	$serviceUrlPrefixPackagedLowercase$ -> send/documents/for/customs/check
	$serviceUrlPrefixHyphen$ -> send-documents-for-customs-check
	$serviceUrlPrefixLowercase$ -> send documents for customs check
	$serviceUrlPrefixUppercase$ -> SEND DOCUMENTS FOR CUSTOMS CHECK
	$serviceUrlPrefix$ -> send-documents-for-customs-check
	$serviceTitleCamel$ -> SendDocumentsForCustomsCheck
	$serviceTitlecamel$ -> sendDocumentsForCustomsCheck
	$serviceTitleNoSpaceLowercase$ -> senddocumentsforcustomscheck
	$serviceTitleNoSpaceUppercase$ -> SENDDOCUMENTSFORCUSTOMSCHECK
	$serviceTitlesnake$ -> send_documents_for_customs_check
	$serviceTitleSnake$ -> SEND_DOCUMENTS_FOR_CUSTOMS_CHECK
	$serviceTitlePackage$ -> Send.Documents.For.Customs.Check
	$serviceTitlePackageLowercase$ -> send.documents.for.customs.check
	$serviceTitlePackaged$ -> Send/Documents/For/Customs/Check
	$serviceTitlePackagedLowercase$ -> send/documents/for/customs/check
	$serviceTitleHyphen$ -> send-documents-for-customs-check
	$serviceTitleLowercase$ -> send documents for customs check
	$serviceTitleUppercase$ -> SEND DOCUMENTS FOR CUSTOMS CHECK
	$serviceTitle$ -> Send Documents For Customs Check
	$servicePrefixCamel$ -> TraderServices
	$servicePrefixcamel$ -> traderServices
	$servicePrefixNoSpaceLowercase$ -> traderservices
	$servicePrefixNoSpaceUppercase$ -> TRADERSERVICES
	$servicePrefixsnake$ -> trader_services
	$servicePrefixSnake$ -> TRADER_SERVICES
	$servicePrefixPackage$ -> Trader.Services
	$servicePrefixPackageLowercase$ -> trader.services
	$servicePrefixPackaged$ -> Trader/Services
	$servicePrefixPackagedLowercase$ -> trader/services
	$servicePrefixHyphen$ -> trader-services
	$servicePrefixLowercase$ -> trader services
	$servicePrefixUppercase$ -> TRADER SERVICES
	$servicePrefix$ -> Trader Services
	$authorisedServiceNameCamel$ -> HmrcCusOrg
	$authorisedServiceNamecamel$ -> hmrcCusOrg
	$authorisedServiceNameNoSpaceLowercase$ -> hmrccusorg
	$authorisedServiceNameNoSpaceUppercase$ -> HMRCCUSORG
	$authorisedServiceNamesnake$ -> hmrc_cus_org
	$authorisedServiceNameSnake$ -> HMRC_CUS_ORG
	$authorisedServiceNamePackage$ -> HMRC.CUS.ORG
	$authorisedServiceNamePackageLowercase$ -> hmrc.cus.org
	$authorisedServiceNamePackaged$ -> HMRC/CUS/ORG
	$authorisedServiceNamePackagedLowercase$ -> hmrc/cus/org
	$authorisedServiceNameHyphen$ -> hmrc-cus-org
	$authorisedServiceNameLowercase$ -> hmrc cus org
	$authorisedServiceNameUppercase$ -> HMRC CUS ORG
	$authorisedServiceName$ -> HMRC-CUS-ORG
	$authorisedIdentifierKeyCamel$ -> Eorinumber
	$authorisedIdentifierKeycamel$ -> eorinumber
	$authorisedIdentifierKeyLowercase$ -> eorinumber
	$authorisedIdentifierKeyUppercase$ -> EORINUMBER
	$authorisedIdentifierKey$ -> EORINumber
	$serviceTargetPort$ -> 9379

and produce the folders and files as shown below:

    ├── .description
	├── .gitignore
	├── .scalafmt.conf
	├── app
	│   ├── assets
	│   │   ├── javascripts
	│   │   │   ├── .eslintrc
	│   │   │   ├── components
	│   │   │   │   ├── component.ts
	│   │   │   │   └── file-upload.ts
	│   │   │   │
	│   │   │   ├── index.ts
	│   │   │   ├── init.ts
	│   │   │   ├── legacy
	│   │   │   │   └── research-banner.js
	│   │   │   │
	│   │   │   ├── load-components.ts
	│   │   │   └── tsconfig.json
	│   │   │
	│   │   ├── karma.conf.js
	│   │   ├── package-lock.json
	│   │   ├── package.json
	│   │   ├── stylesheets
	│   │   │   ├── _autocomplete.scss
	│   │   │   ├── _currency-input.scss
	│   │   │   ├── _js-hidden.scss
	│   │   │   ├── _language-toggle.scss
	│   │   │   ├── _overrides.scss
	│   │   │   ├── _panel.scss
	│   │   │   ├── _percentage-input.scss
	│   │   │   ├── _print-preview.scss
	│   │   │   ├── _research-banner.scss
	│   │   │   ├── _responsive-table.scss
	│   │   │   ├── _summary-list.scss
	│   │   │   ├── _task-list.scss
	│   │   │   ├── _timeout-dialog.scss
	│   │   │   ├── application.scss
	│   │   │   ├── print-styles
	│   │   │   │   ├── _print-claim-details.scss
	│   │   │   │   └── _print.scss
	│   │   │   │
	│   │   │   ├── print.scss
	│   │   │   └── upload-loading.scss
	│   │   │
	│   │   ├── webpack.config.js
	│   │   ├── webpack.dev.config.js
	│   │   └── webpack.prod.config.js
	│   │
	│   ├── FrontendModule.scala
	│   └── uk
	│       └── gov
	│           └── hmrc
	│               └── traderservices
	│                   ├── connectors
	│                   │   ├── ApiError.scala
	│                   │   ├── AverageResponseTimer.scala
	│                   │   ├── FrontendAuthConnector.scala
	│                   │   ├── HttpAPIMonitor.scala
	│                   │   ├── HttpErrorRateMeter.scala
	│                   │   ├── ReadSuccessOrFailure.scala
	│                   │   ├── TraderServicesApiConnector.scala
	│                   │   ├── TraderServicesCaseResponse.scala
	│                   │   └── TraderServicesCreateCaseRequest.scala
	│                   │
	│                   ├── controllers
	│                   │   ├── AccessibilityStatementController.scala
	│                   │   ├── AuthActions.scala
	│                   │   ├── ContactFieldHelper.scala
	│                   │   ├── DateFieldHelper.scala
	│                   │   ├── FormFieldMappings.scala
	│                   │   ├── LanguageSwitchController.scala
	│                   │   ├── SessionController.scala
	│                   │   ├── SignOutController.scala
	│                   │   ├── Time12FieldHelper.scala
	│                   │   ├── Time24FieldHelper.scala
	│                   │   └── TraderServicesJourneyController.scala
	│                   │
	│                   ├── filters
	│                   │   ├── AuditFilter.scala
	│                   │   └── CustomFrontendFilters.scala
	│                   │
	│                   ├── journeys
	│                   │   ├── TraderServicesJourneyModel.scala
	│                   │   └── TraderServicesJourneyStateFormats.scala
	│                   │
	│                   ├── models
	│                   │   ├── DeclarationDetails.scala
	│                   │   ├── EntryNumber.scala
	│                   │   ├── EnumerationFormats.scala
	│                   │   ├── EPU.scala
	│                   │   ├── ExampleQuestions.scala
	│                   │   ├── ExampleQuestionsStateModel.scala
	│                   │   ├── ExampleRequestType.scala
	│                   │   ├── ExampleRouteType.scala
	│                   │   ├── QuestionsAnswers.scala
	│                   │   ├── SealedTraitFormats.scala
	│                   │   └── SimpleStringFormat.scala
	│                   │
	│                   ├── repository
	│                   │   └── JourneyCacheRepository.scala
	│                   │
	│                   ├── services
	│                   │   ├── AuditService.scala
	│                   │   ├── MongoDBCachedJourneyService.scala
	│                   │   ├── SessionCache.scala
	│                   │   └── TraderServicesJourneyService.scala
	│                   │
	│                   ├── support
	│                   │   └── CallOps.scala
	│                   │
	│                   ├── views
	│                   │   ├── AccessibilityStatementView.scala.html
	│                   │   ├── CaseAlreadyExistsView.scala.html
	│                   │   ├── CheckboxItemsHelper.scala
	│                   │   ├── CommonUtilsHelper.scala
	│                   │   ├── components
	│                   │   │   ├── autocomplete.scala.html
	│                   │   │   ├── bullets.scala.html
	│                   │   │   ├── button.scala.html
	│                   │   │   ├── details.scala.html
	│                   │   │   ├── errorSummary.scala.html
	│                   │   │   ├── fieldset.scala.html
	│                   │   │   ├── FooterLinks.scala
	│                   │   │   ├── forms.scala
	│                   │   │   ├── govukTimeInput.scala.html
	│                   │   │   ├── h1.scala.html
	│                   │   │   ├── h1NoMargin.scala.html
	│                   │   │   ├── h2.scala.html
	│                   │   │   ├── h3.scala.html
	│                   │   │   ├── html.scala
	│                   │   │   ├── inputCheckboxes.scala.html
	│                   │   │   ├── inputCurrency.scala.html
	│                   │   │   ├── inputDate.scala.html
	│                   │   │   ├── inputHidden.scala.html
	│                   │   │   ├── inputNumber.scala.html
	│                   │   │   ├── inputNumberSubHeading.scala.html
	│                   │   │   ├── inputPercentage.scala.html
	│                   │   │   ├── inputRadio.scala.html
	│                   │   │   ├── inputText.scala.html
	│                   │   │   ├── inputTime.scala.html
	│                   │   │   ├── languageSelection.scala.html
	│                   │   │   ├── link.scala.html
	│                   │   │   ├── multiFieldError.scala.html
	│                   │   │   ├── multiLineSummary.scala.html
	│                   │   │   ├── multiLineSummaryWithoutAction.scala.html
	│                   │   │   ├── orderedList.scala.html
	│                   │   │   ├── p.scala.html
	│                   │   │   ├── package.scala
	│                   │   │   ├── pageHeading.scala.html
	│                   │   │   ├── panelIndent.scala.html
	│                   │   │   ├── phaseBanner.scala.html
	│                   │   │   ├── researchBanner.scala.html
	│                   │   │   ├── simpleInputText.scala.html
	│                   │   │   ├── siteHeader.scala.html
	│                   │   │   ├── strong.scala.html
	│                   │   │   ├── subheading.scala.html
	│                   │   │   ├── subheadingP.scala.html
	│                   │   │   ├── summaryList.scala.html
	│                   │   │   ├── textarea.scala.html
	│                   │   │   ├── warningText.scala.html
	│                   │   │   ├── yesNoRadio.scala.html
	│                   │   │   └── yesNoRadioSubHeading.scala.html
	│                   │   │
	│                   │   ├── DateTimeFormatHelper.scala
	│                   │   ├── DeclarationDetailsEntryView.scala.html
	│                   │   ├── DeclarationDetailsHelper.scala
	│                   │   ├── EnterCaseReferenceNumberView.scala.html
	│                   │   ├── EnterResponseTextView.scala.html
	│                   │   ├── ExampleQuestionsRequestTypeView.scala.html
	│                   │   ├── ExampleQuestionsRouteTypeView.scala.html
	│                   │   ├── ExampleQuestionsSummaryView.scala.html
	│                   │   ├── ExampleQuestionsViewContext.scala
	│                   │   ├── InternalErrorView.scala.html
	│                   │   ├── PageNotFoundErrorView.scala.html
	│                   │   ├── RadioItemsHelper.scala
	│                   │   ├── SummaryListRowHelper.scala
	│                   │   ├── templates
	│                   │   │   ├── ErrorTemplate.scala.html
	│                   │   │   ├── GovukLayout2.scala.html
	│                   │   │   ├── GovukLayoutWrapper.scala.html
	│                   │   │   ├── GovukLayoutWrapper2.scala.html
	│                   │   │   ├── GtmBodySnippet.scala.html
	│                   │   │   └── GtmHeadSnippet.scala.html
	│                   │   │
	│                   │   ├── TimedOutView.scala.html
	│                   │   ├── TraderServicesConfirmationView.scala.html
	│                   │   ├── TraderServicesViews.scala
	│                   │   ├── ViewHelpers.scala
	│                   │   └── viewmodels
	│                   │       └── TimeInput.scala
	│                   │
	│                   └── wiring
	│                       ├── AppConfig.scala
	│                       └── ErrorHandler.scala
	│
	├── build.sbt
	├── conf
	│   ├── app.routes
	│   ├── application-json-logger.xml
	│   ├── application.conf
	│   ├── logback.xml
	│   ├── messages.cy
	│   ├── messages.en
	│   └── prod.routes
	│
	├── it
	│   └── uk
	│       └── gov
	│           └── hmrc
	│               └── traderservices
	│                   ├── connectors
	│                   │   └── TraderServicesApiConnectorISpec.scala
	│                   │
	│                   ├── controllers
	│                   │   ├── AccessibilityStatementControllerISpec.scala
	│                   │   ├── AuthActionsISpec.scala
	│                   │   ├── LanguageSwitchControllerISpec.scala
	│                   │   ├── SessionControllerISpec.scala
	│                   │   └── TraderServicesJourneyISpec.scala
	│                   │
	│                   ├── stubs
	│                   │   ├── AuthStubs.scala
	│                   │   ├── DataStreamStubs.scala
	│                   │   └── TraderServicesApiStubs.scala
	│                   │
	│                   └── support
	│                       ├── AppISpec.scala
	│                       ├── BaseISpec.scala
	│                       ├── InMemoryJourneyService.scala
	│                       ├── MetricsTestSupport.scala
	│                       ├── NonAuthPageISpec.scala
	│                       ├── Port.scala
	│                       ├── ServerISpec.scala
	│                       ├── TestAppConfig.scala
	│                       ├── TestData.scala
	│                       ├── TestJourneyService.scala
	│                       ├── UnitSpec.scala
	│                       └── WireMockSupport.scala
	│
	├── LICENSE
	├── project
	│   ├── .bloop
	│   │   ├── bloop.settings.json
	│   │   └── trader-services-route-one-frontend-build.json
	│   │
	│   ├── build.properties
	│   ├── JavaScriptBuild.scala
	│   ├── metals.sbt
	│   ├── Npm.scala
	│   ├── plugins.sbt
	│   └── Webpack.scala
	│
	├── README.md
	├── repository.yaml
	└── test
	    └── uk
	        └── gov
	            └── hmrc
	                └── traderservices
	                    ├── controllers
	                    │   ├── CommonUtilsHelperSpec.scala
	                    │   ├── ContactFieldHelperSpec.scala
	                    │   ├── DateFieldHelperSpec.scala
	                    │   ├── DeclarationDetailsFormSpec.scala
	                    │   ├── ExampleQuestionsFormSpec.scala
	                    │   ├── FormFieldMappingsSpec.scala
	                    │   ├── Time12FieldHelperSpec.scala
	                    │   └── Time24FieldHelperSpec.scala
	                    │
	                    ├── journey
	                    │   ├── CreateCaseJourneyModelSpec.scala
	                    │   └── CreateCaseJourneyStateFormatsSpec.scala
	                    │
	                    ├── model
	                    │   ├── DeclarationDetailsSpec.scala
	                    │   └── ExampleQuestionsFormatSpec.scala
	                    │
	                    └── support
	                        ├── CallOpsSpec.scala
	                        ├── FormMappingMatchers.scala
	                        ├── FormMatchers.scala
	                        ├── FormValidator.scala
	                        ├── InMemoryStore.scala
	                        ├── JsonFormatTest.scala
	                        ├── StateMatchers.scala
	                        └── UnitSpec.scala