import java.io.PrintWriter
import play.sbt.PlayRunHook
import sbt._

import scala.io.Source
import scala.sys.process.Process
import scala.util.Using

object PlayDevRunHook {
  def apply(base: File): PlayRunHook = {
    val frontendBase = base / "frontend"
    val packageJsonPath = frontendBase / "package.json"
    // フロントエンドのビルド時に vite_assets にあるファイルが消されてしまうので、package.json と同じディレクトリに置いておく
    val packageJsonHashPath = frontendBase / "package.json.hash"

    object FrontendBuildProcess extends PlayRunHook {
      var process: Option[Process] = None

      override def beforeStarted(): Unit = {
        println("Hook to Play Framework dev run -- beforeStarted")

        // 警告「Source is not closed 」が出たため、Usingを使って閉じる
        // https://stackoverflow.com/questions/4458864/whats-the-right-way-to-use-scala-io-source
        // また、Usingは Try[T] を返すので、後々比較できるよう getOrElse() で値を取り出す
        // https://qiita.com/ka2kama/items/cd846b15fbb56cdbc9ea
        // val currPackageJsonHash = Source.fromFile(packageJsonPath).getLines().mkString.hashCode().toString()
        val currPackageJsonHash = Using(Source.fromFile(packageJsonPath)) { source =>
          source.getLines().mkString.hashCode().toString
        }.getOrElse("")

        val oldPackageJsonHash = getStoredPackageJsonHash

        // frontend/package.json が変更されたら、もう一度 'yarn install` コマンドを実行する
        if (!oldPackageJsonHash.contains(currPackageJsonHash)) {
          println(s"Found new/changed package.json. Run yarn install ...")

          // 同期的にインストールを実行
          Process("yarn install", frontendBase).!

          updateStoredPackageJsonHash(currPackageJsonHash)
        }
      }

      override def afterStarted(): Unit = {
        println(s"> Watching frontend changes in $frontendBase")
        // フロントエンドのビルド用のプロセスを立ち上げ、非同期で実行する
        process = Option(Process("yarn build --watch", frontendBase).run)
      }

      override def afterStopped(): Unit = {
        // フロントエンドのビルド用のプロセスを停止する
        process.foreach(_.destroy)
        process = None
      }

      private def getStoredPackageJsonHash: Option[String] = {
        if (packageJsonHashPath.exists()) {
          val hash = Using(Source.fromFile(packageJsonHashPath)) { source =>
            source.getLines().mkString
          }
          Some(hash.getOrElse(""))
        } else {
          None
        }
      }

      private def updateStoredPackageJsonHash(hash: String): Unit = {
        Using(new PrintWriter(packageJsonHashPath)) { writer =>
          writer.write(hash)
        }
      }
    }

    FrontendBuildProcess
  }
}
