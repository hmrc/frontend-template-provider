import uk.gov.hmrc.playcrosscompilation.AbstractPlayCrossCompilation
import uk.gov.hmrc.playcrosscompilation.PlayVersion.{Play26, Play27}

object PlayCrossCompilation extends AbstractPlayCrossCompilation(defaultPlayVersion = Play26) {
  def version: String = playVersion match {
    case Play26 => "2.6.11"
    case Play27 => "2.7.5"
  }
  override def playCrossScalaBuilds(scalaVersions: Seq[String]): Seq[String] =
    playVersion match {
      case Play26 => scalaVersions
      case Play27 => scalaVersions.filter(version => version.startsWith("2.12"))
    }
}
