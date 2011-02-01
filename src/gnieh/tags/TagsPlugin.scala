package gnieh.tags

import scala.tools.nsc
import nsc.Global
import nsc.Phase
import nsc.plugins.Plugin
import nsc.plugins.PluginComponent

class TagsPlugin(val global: Global) extends Plugin {

  val name = "scalatags"
  val description = "generates a tags file"
    
  

  val phase = new TagsTraverser() {
    val global = TagsPlugin.this.global
    val runsAfter = List("typer")
  }

  val components = List[PluginComponent](phase)
  
  override def processOptions(options: List[String], error: String => Unit) {
    for (option <- options) {
      option match {
        case tagFile(file) =>
          global.settings.stop.value = List("superaccessors")
          phase.outputFile = file
        case _ => error("Option not understood: " + option)
      }
    }
  }
  
  override val optionsHelp: Option[String] = 
    Some("  -P:scalatags:file=<path>        generate the tag file only (no class file generated)")
  
  object tagFile {
    def unapply(option: String): Option[String] = {
      if(option.startsWith("file="))
        Some(option.substring(5))
      else
        None
    }
  }

}