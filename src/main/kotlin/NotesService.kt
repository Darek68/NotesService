
fun main(){
    val notesService = NotesService()

    val note0 = Note("Заголовок 0","Текст заметки 0")
    val note1 = Note("Заголовок 1","Текст заметки 1")

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

    val com0 = Comment(note0.id,"Жуткий коммент на заметку..",2)
    val com1 = Comment(note0.id,"Еще более жуткий оммент на заметку..",2)
    val com2 = Comment(note0.id,"Еще более жуткий оммент на заметку..",2)
    println("Успешно создана заметка с идентификатором ${notesService.add(note1)}")
    // val comId = note0?.createCom(com0)
   // println("\nНовый коммент получил идентификар ${note0.createCom(Comment(note0.id,"Жуткий коммент на заметку..",2))}")
    println("\nНовый коммент получил идентификар ${notesService.createCom(com0)}   ${com0.id}")
    println("\nНовый коммент получил идентификар ${notesService.createCom(com1)}   ${com1.id}")

    println("\nВсе комментарии заметки $idx0 : ")
    println(notesService.getCom(idx0))

    println("\nПопытка удаления несуществующего коментария..")
    if (notesService.delRestCom(com2,true) == 1)println("Коментарий успешно удален") else println("Коментарий не найден")
    println("Попытка удалить существующий коментарий..")
    if (notesService.delRestCom(com0,true) == 1)println("Коментарий успешно удален") else println("Коментарий не найден")

    println("\nВсе комментарии заметки $idx0 : ")
    println(notesService.getCom(idx0))

    println("\nПопытка восстановить несуществующий коментарий..")
    if (notesService.delRestCom(com2,false) == 1)println("Коментарий успешно восстановлен") else println("Коментарий не найден")
    println("\nПопытка восстановить существующий коментарий..")
    if (notesService.delRestCom(com0,false) == 1)println("Коментарий успешно восстановлен") else println("Коментарий не найден")

    println("\nВсе комментарии заметки $idx0 : ")
    println(notesService.getCom(idx0))
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

    fun createCom(element: Comment): Int? {
        println("001  ${this.id}  ${element.id}")
        element.id = this.comments.size
        this.comments.add(element)
        println("002  ${this.comments.size}  ${element.id}")
        return this.comments.last().id
    }
// логика одинаковая для удаления\восстановления
    fun delRestCom(id: Int?,del:Boolean):Int {
        for ((indexCm, comment) in this.comments.withIndex()){//проход по комментам
            if (comment.id == id) {
                //comments.removeAt(indexCm)
                comments.get(indexCm).del = del
                return 1
            }
        }
        return 0
    }
}

class Comment (val noteId: Int,val message:String,val replyTo:Int){
    var id:Int? = null
    var del:Boolean = false
}

class NotesService: CrudService<Note>{
    var notes: MutableList<Note> = mutableListOf()

    fun findNote(noteId: Int):Note?{ //for (note in notes)
        for (note in notes){
            if (note.id == noteId) {
                return note
            }
        }
        return null
    }
  // логика одинаковая для удаления\восстановления
  // del = true  удаление false - восстановление
  fun delRestCom(element: Comment,del:Boolean): Int {
      val note = findNote(element.noteId)
      return if (note == null) 0 else note.delRestCom(element.id,del)
  }

  fun getCom(noteId: Int):String {
      val note = findNote(noteId)
      return if (note == null) "" else {
          var retValue = ""
          val comments =note.comments
          for (comment in comments){//проход по комментам
              if (! comment.del) retValue += (comment.message + "\n")
          }
          retValue
      }
  }

  fun createCom(element: Comment):Int? {
      val note = findNote(element.noteId)
      return if (note == null) null else {
          return note.createCom(element)
      }
  }

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
