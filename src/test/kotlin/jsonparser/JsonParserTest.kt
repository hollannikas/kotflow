package jsonparser

import processbuilder.ProcessExecutor
import java.io.File
import kotlin.test.Test

class JsonParserTest {
    @Test
    fun `executes tasks in success path when condition is true`() {
        val process = processFromFile(File("src/test/resources/test.json"))
        ProcessExecutor(process).execute()
    }
}
