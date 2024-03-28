import java.util.Scanner

class Menu(private var type: Option) {
    enum class Option(val message: String) {
        ARCHIVES(message = "Введите команду цифрой."),
        CREATE_ARCHIVE(message = "Введите название архива или 0, чтобы вернуться назад."),
        ARCHIVE(message = "Введите команду цифрой."),
        CREATE_NOTE(message = "Введите название заметки:"),
        NOTE(message = "Введите любой символ, чтобы вернуться назад. "),
        CLOSE(message = "Работа завершена.")
    }

    companion object {
        private const val EXIT_MENU_OPTION = "0"
        private val scanner = Scanner(System.`in`)
        private var archives = mutableListOf<Archive>()
        private var lastArchiveIndex: Int = 0
        private var lastNoteIndex: Int = 0
        fun start() = Menu(Option.ARCHIVES).also { it.readCommand(it.type) }
        fun input(): String = scanner.nextLine()
        fun close() = scanner.close()
        fun open(item: Openable) = item.open()
        fun create(item: Openable) {
            if (item is Archive) {
                archives.add(item)
                println("Архив ${item.name} добавлен")
                printArchives()
            } else if (item is Note) {
                archives[lastArchiveIndex].notes.add(item)
                println("Заметка ${item.name} добавлена в архив ${archives[lastArchiveIndex].name}")
            }
        }
        fun printArchives(): String {
            val stringBuilder: StringBuilder = java.lang.StringBuilder()
            stringBuilder.append("Архивы заметок:\n")
            var count = 0
            stringBuilder.append("${count++} - создать\n")
            archives.all { stringBuilder.append("${count++} - ${it.name}\n").isNotEmpty() }
            stringBuilder.append("$count - выход")
            return stringBuilder.toString()
        }
    }

    private fun readCommand(type: Option) {
        this.type = type
        println(type.message)
        when (type) {
            Option.ARCHIVES -> {
                println(printArchives())
                readItemCommand(input())
            }

            Option.CREATE_ARCHIVE -> {
                createItemCommand(input())
            }

            Option.ARCHIVE -> {
                open(archives[lastArchiveIndex])
                readItemCommand(input())
            }

            Option.CREATE_NOTE -> {
                createItemCommand(input())
            }

            Option.NOTE -> {
                open(archives[lastArchiveIndex].notes[lastNoteIndex])
                readItemCommand(input())
            }

            Option.CLOSE -> close()
        }
    }

    private fun readItemCommand(command: String?) {
        if (type == Option.NOTE) {
            readCommand(Option.ARCHIVE)
        } else {
            val number =
                if (type == Option.ARCHIVES) archives.size else archives[lastArchiveIndex].notes.size

            try {
                if (command.isNullOrBlank()) {
                    readCommand(type)
                } else
                    if (command.toInt() < 0) {
                        showErrorMessage()
                    } else if (command.toInt() == 0) {
                        val option =
                            if (type == Option.ARCHIVES) Option.CREATE_ARCHIVE else Option.CREATE_NOTE
                        readCommand(option)
                    } else if (command.toInt() == number + 1) {
                        val option = if (type == Option.ARCHIVES) Option.CLOSE else Option.ARCHIVES
                        readCommand(option)
                    } else if (command.toInt() <= number) {
                        val option = if (type == Option.ARCHIVES) Option.ARCHIVE else Option.NOTE
                        if (type == Option.ARCHIVES) {
                            lastArchiveIndex = command.toInt() - 1
                        } else {
                            lastNoteIndex = command.toInt() - 1
                        }
                        readCommand(option)
                    } else {
                        showErrorMessage()
                    }
            } catch (e: NumberFormatException) {
                showErrorMessage()
            }
        }
    }

    private fun createItemCommand(name: String?) {
        if (name.isNullOrBlank()) {
            readCommand(type)
            return
        } else if (type == Option.CREATE_ARCHIVE) {
            if (name != EXIT_MENU_OPTION) {
                create(Archive(name))
            }
        } else {
            println("Введите текст заметки")
            val text = input()
            if (text.isBlank()) {
                createItemCommand(name)
                return
            } else {
                create(Note(name, text))
            }
        }
        val option = if (type == Option.CREATE_ARCHIVE) Option.ARCHIVES else Option.ARCHIVE
        readCommand(option)
    }

    private fun showErrorMessage() {
        println("Ошибка. Введите корректную команду.")
        readCommand(type)
    }
}

fun main() {
    Menu.start()
}