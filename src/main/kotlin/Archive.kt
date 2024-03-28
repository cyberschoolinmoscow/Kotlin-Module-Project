class Archive(var name: String, var notes: MutableList<Note> = mutableListOf()) : Openable {
    override fun open() {
        val stringBuilder: StringBuilder = java.lang.StringBuilder()
        stringBuilder.append("Архив $name\n")
        stringBuilder.append("0 - создать заметку\n")
        var count = 1
        notes.all { stringBuilder.append("${count++} - ${it.name}\n").isNotEmpty() }
        stringBuilder.append("$count - назад")
        println(stringBuilder.toString())
    }
}
