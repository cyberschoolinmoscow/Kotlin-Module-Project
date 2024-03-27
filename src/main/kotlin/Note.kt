class Note(name: String, var text: String) : MyFile(name) {
    override fun open() {
        println("Заметка: $name\n Текст: $text ")
    }
}
