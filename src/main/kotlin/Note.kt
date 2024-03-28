class Note(var name: String, private var text: String) : Openable {
    override fun open() {
        println("Заметка: $name\n Текст: $text ")
    }
}
