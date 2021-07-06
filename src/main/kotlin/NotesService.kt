
fun main(){
    val notesService = NotesService()

    val note0 = Note("Заголовок 0","Текст заметки 0")
    // val note1 = Note("Заголовок 1","Текст заметки 1")

    println("Успешно создана заметка с идентификатором ${notesService.add(note0)}")
    val idx0 = 0
    val idx1 = 1
    println("\nПопытка удаления несуществующей заметки..")
    if (notesService.del(idx1) == 1) println("Успешно удалили заметку $idx1") else println("Заметка $idx1 не найдена")
    println("Попытка удаления существующей заметки..")
    if (notesService.del(idx0) == 1) println("Успешно удалили заметку $idx0") else println("Заметка $idx0 не найдена")

    notesService.add(note0)
    println("\nПопытка получения текста из несуществующей заметки..")
    println("Текст заметки с индексом $idx1 : ${notesService.get(idx1)?.text}")
    println("Попытка получения текста из существующей заметки..")
    println("Текст заметки с индексом $idx0 : ${notesService.get(idx0)?.text}")

    println("\nПопытка изменения несуществующей заметки..")
    if (notesService.edit(idx1,"Заголовок изменен","Новый текст заметки") == 1) println("Заметка с индексом $idx1 успешно изменена")
    else println("Заметка с индексом $idx1 не найдена")
    println("Попытка изменения существующей заметки..")
    if (notesService.edit(idx0,"Заголовок изменен","Новый текст заметки") == 1) println("Заметка с индексом $idx0 успешно изменена")
    else println("Заметка с индексом $idx0 не найдена")

    //val com0 = Comment(0,"Жуткий оммент на заметку..",2)
    // val comId = note0?.createCom(com0)
    println("\nНовый коммент получил идентификар ${note0.createCom(Comment(note0.id,"Жуткий коммент на заметку..",2))}")

    println("\nПопытка удаления несуществующего коментария..")
    if (note0.delCom(idx1) == 1)println("Коментарий $idx1 успешно удален") else println("Коментарий $idx1 не найден")
    println("Попытка удалить существующий коментарий..")
    if (note0.delCom(idx0) == 1)println("Коментарий $idx0 успешно удален") else println("Коментарий $idx0 не найден")


}

interface CrudService<T>{
    fun add(element: T):Int
    fun del(id: Int):Int
    fun get(id:Int):T?
    fun edit(id:Int,titl:String,text:String):Int
}

class Note (var title: String,var text: String){
    var id:Int = 0
    var comments:MutableList<Comment> = mutableListOf()

    fun createCom(element: Comment):Int {
        this.comments.add(this.comments.size,element)
        return this.comments.last().id
    }

    fun delCom(id: Int):Int {
        try {
            this.comments.removeAt(id)
            return 1
        }catch (e: IndexOutOfBoundsException){
            return 0
        }

    }
}

class Comment (val noteId: Int,val message:String,val replyTo:Int){
    var id:Int = 0
}

class NotesService: CrudService<Note>{
    var notes: MutableList<Note> = mutableListOf()

    override fun add(element: Note):Int {
        element.id = notes.size
        notes.plusAssign(element)
        return notes.last().id
    }

    override fun del(id: Int):Int {
        val retVal = try {
            notes.removeAt(id)
            1
        } catch (e: IndexOutOfBoundsException) {
            180
        }
        return retVal
    }

    override fun get(id: Int): Note? {
        val retVal = try {
            notes[id]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
        return retVal
    }

    override fun edit(id: Int,titl: String,text: String):Int {
        val element = try {
            notes[id]
        } catch (e: IndexOutOfBoundsException) {
            return 0
        }
        element.title = titl
        element.text = text
        return 1
    }
}
// --------------------------------------------
/*
class CommentService: CrudService<Comment>{
    override fun add(element: Comment) {
        for ((index, note) in notes.withIndex()){
            if (note.id == element.note_id) {
                element.id = notes.get(index).comments.size + 1
                notes.get(index).comments.plusElement(element)
            }
        }
    }

    override fun del(element: Comment) {
        for ((indexNt, note) in notes.withIndex()){ // проход по заметкам
            if (note.id == element.note_id) {
                val comments =notes.get(indexNt).comments
                comments.get(comments.indexOf(element)).remove
                for ((indexCm, comment) in comments.withIndex()){//проход по комментам
                    if (comment.id == element.id) {
                        comments.get(indexCm).remove
                    }
                }
            }
        }
    }

    override fun get(id: Int): Comment? {

    }

    override fun edit(element: Comment) {

    }

} */