resolvers += "HMRC-open-artefacts-maven" at "https://open.artefacts.tax.service.gov.uk/maven2"
resolvers += Resolver.url("HMRC-open-artefacts-ivy", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
  Resolver.ivyStylePatterns
)

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "3.0.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.8")

addSbtPlugin("uk.gov.hmrc" % "sbt-distributables" % "2.1.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")

addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.11")

addSbtPlugin("net.ground5hark.sbt" % "sbt-concat" % "0.2.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-uglify" % "2.0.0")
