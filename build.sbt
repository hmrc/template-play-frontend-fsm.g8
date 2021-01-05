// This build is for this giter8 template.
// To test the template run the script `./test.sh`
// See http://www.foundweekends.org/giter8/testing.html#Using+the+Giter8Plugin for more details.
lazy val root = (project in file(".")).settings(
  name := "template-play-27-frontend-fsm",
  description := "A Giter8 template for creating HMRC Digital Scala Play 2.7 Stateful Frontend Microservice",
  resolvers += Resolver.url("typesafe", url("http://repo.typesafe.com/typesafe/ivy-releases/"))(
    Resolver.ivyStylePatterns)
)
