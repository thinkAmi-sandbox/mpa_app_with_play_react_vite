import com.typesafe.sbt.web.Import.pipelineStages

import scala.sys.process.Process

name := """mpa_app_with_play_react_vite"""
organization := "com.example.thinkami"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(SbtWeb)
  // ポート変更を追加
  // https://stackoverflow.com/a/47696298
  .settings(
    PlayKeys.playDefaultPort := 9000
  )

// 開発環境でもフィンガープリントがつくかを確認したいので設定
// https://qiita.com/yoshihir/items/f4723c0d0ab0cd3af6be
// ただし sbt 1.5 より書き方が変わってる
//pipelineStages in Assets := Seq(digest)
Assets / pipelineStages := Seq(digest)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.thinkami.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.thinkami.binders._"

// sbt run 時に、フロントエンドもコンパイルする
PlayKeys.playRunHooks += PlayDevRunHook(baseDirectory.value)


// 同梱プロセスに統合
lazy val frontEndBuild = taskKey[Unit]("Execute dashboard frontend build command")

val frontendPath = "frontend"
val frontEndFile = file(frontendPath)

frontEndBuild := {
  println(Process("yarn install", frontEndFile).!!)
  println(Process("yarn build", frontEndFile).!!)
}

dist := (dist dependsOn frontEndBuild).value
stage := (stage dependsOn dist).value
