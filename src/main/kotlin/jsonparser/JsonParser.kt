package jsonparser

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import processbuilder.Process
import java.io.File

val mapper = jacksonObjectMapper()

fun processFromFile(file: File) = mapper.readValue(file, Process::class.java)
