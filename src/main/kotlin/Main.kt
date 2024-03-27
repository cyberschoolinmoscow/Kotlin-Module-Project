import java.util.Scanner

var archives = mutableListOf<Archive>()

class Window(var type: Option) {
    enum class Option(val message: String) {
        ARCHIVES(message = "Введите команду цифрой."),
        CREATE_ARCHIVE(message = "Введите название архива или ${archives.size ?: 1}, чтобы вернуться назад."),
        ARCHIVE(message = "Введите команду цифрой."),
        CREATE_NOTE(message = "Введите название заметки:"),
        NOTE(message = "Введите любой символ, чтобы вернуться назад. "),
        CLOSE(message = "Работа завершена.")
    }

    companion object {
        private var lastArchiveIndex: Int = 0
        private var lastNoteIndex: Int = 0
        fun open(file: MyFile) {
            file.open()
        }

        fun create(file: MyFile) {
            if (file is Archive) {
                archives.add(file)
                println("Архив ${file.name} добавлен")
                printArchives()
            } else if (file is Note) {
                archives[lastArchiveIndex].notes.add(file)
                println("Заметка ${file.name} добавлена в архив ${archives[lastArchiveIndex].name}")
            }
        }

        fun printArchives(): String {
            var s = "Архивы заметок:\n"
            var count = 0
            s += "${count++} - создать\n"
            for (arch in archives) {
                s += "${count++} - ${arch.name}\n"
            }
            s += "${count++} - выход"
            return s
        }

        fun start() {
            Window(Option.ARCHIVES).readCommand()
        }
    }

    private fun readCommand(type: Option=this.type) {
        this.type = type
        println(type.message)
        when (type) {
            Option.ARCHIVES -> {
                println(printArchives())
                readItemCommand(Scanner(System.`in`).nextLine())
            }

            Option.CREATE_ARCHIVE -> {
                createArchiveCommand(Scanner(System.`in`).nextLine())
            }

            Option.ARCHIVE -> {
                open(archives[lastArchiveIndex])
                readItemCommand(Scanner(System.`in`).nextLine())
            }

            Option.CREATE_NOTE -> {
                createNoteCommand(Scanner(System.`in`).nextLine())
            }

            Option.NOTE -> {
                open(archives[lastArchiveIndex].notes[lastNoteIndex])
                readItemCommand(Scanner(System.`in`).nextLine())
            }

            Option.CLOSE -> println()
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
                    readCommand()
                } else
                    if (command.toInt() == 0) {
                        var option =
                            if (type == Option.ARCHIVES) Option.CREATE_ARCHIVE else Option.CREATE_NOTE
                        readCommand(option)
                    } else if (command.toInt() == number + 1) {
                        var option = if (type == Option.ARCHIVES) Option.CLOSE else Option.ARCHIVES

                        readCommand(option)
                    } else if (command.toInt() <= number) {
                        var option = if (type == Option.ARCHIVES) Option.ARCHIVE else Option.NOTE
                        if (type == Option.ARCHIVES) {
                            lastArchiveIndex = command.toInt() - 1
                        } else {
                            lastNoteIndex = command.toInt() - 1
                        }
                        readCommand(option)
                    } else {
                        showErrorMessage()
                    }
            } catch (e: java.lang.Exception) {
                showErrorMessage()
            }
        }
    }

    private fun createArchiveCommand(command: String?) {
        if (command.isNullOrBlank()) {
            readCommand()
        } else
            if (command == "0") {
                readCommand(Option.ARCHIVES)
            } else {
                val archive = Archive(command)
                create(archive)
                readCommand(Option.ARCHIVES)
            }
    }

    private fun createNoteCommand(name: String?) {
        if (name.isNullOrBlank()) {
            readCommand()
        } else {
            println("Введите текст заметки")
            var text = Scanner(System.`in`).nextLine()
            if (text.isNullOrBlank()) {
                createNoteCommand(name)
            } else {
                var note = Note(name, text)
                create(note)
                readCommand(Option.ARCHIVE)
            }
        }
    }

    private fun showErrorMessage() {
        println("Ошибка. Введите корректную команду.")
        readCommand()
    }
}

fun main(args: Array<String>) {
    Window.start()
}