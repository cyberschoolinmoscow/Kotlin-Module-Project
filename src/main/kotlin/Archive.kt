import java.io.File

class Archive(name: String) : MyFile(name) {
    var notes = mutableListOf<Note>()
    override fun open() {
        println("Архив $name")
        println("0 - создать заметку")
        var count = 1
        for (note in notes) {
            println("${count++} - ${note.name}")
        }
        println("${count++} - назад")
    }
}
