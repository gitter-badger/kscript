#!/usr/bin/env kscript

//DEPS com.github.holgerbrandl:kscript:1.2.2

import kscript.stopIfNot
import kscript.text.linesFrom
import java.io.File
import java.io.FileNotFoundException
import java.net.URL
import kotlin.system.exitProcess


// resolve_includes.kts ${KSCRIPT_HOME}/test/resources/includes/include_variations.kts $(tty)
///Users/brandl/projects/kotlin/kscript/test/resources/includes/rel_includes/include_1.kts
//val args = arrayOf("org.docopt:docopt:0.6.0-SNAPSHOT", "log4j:log4j:1.2.14")

if (args.isEmpty() || listOf("--help", "-help", "-h").contains(args[0])) {
    System.err.println("""
resolve_includes.kts resolves //INCLUDE directives

For details see https://github.com/holgerbrandl/kscript

## Example

resdeps.kts template_with_directives.kts resolved.kts

## Copyright

2017 Holger Brandl
""".trim())

    exitProcess(0)
}


val template = File(args[0])
val output = File(args[1])

stopIfNot(template.isFile) { "input file does not exist" }

//stopIfNot(output.isFile) { "output file does already exist" }


val outWriter = java.io.PrintWriter(output)


linesFrom(template).forEach {
    if (!it.startsWith("//INCLUDE")) {
        outWriter.println(it)
    } else {

        val include = it.split("[ ]+".toRegex()).last()

        val includURL = when {
            include.startsWith("http") -> URL(include)
        //            include.startsWith("./") || include.startsWith("../") -> File(template.parentFile, include)
            include.startsWith("/") -> File(include).toURI().toURL()
            else -> File(template.parentFile, include).toURI().toURL()
        }

        try {
            outWriter.println(includURL.readText())
        }catch(e:FileNotFoundException){
            System.err.println("[ERROR] while resolving //INCLUDE: ${e.message}")
            exitProcess(1)
        }
    }
}

outWriter.close()
