package com.example.googleassistant.assistant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.googleassistant.data.Assistant
import com.example.googleassistant.data.AssistantDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AssistantViewModel(private val dataSource: AssistantDao, application: Application)
    : AndroidViewModel(application) {

    private var viewModeJob= Job()

    override fun onCleared() {
        super.onCleared()
        viewModeJob.cancel()
    }
    private val  uiScope = CoroutineScope(Dispatchers.Main + viewModeJob)
    private var currentMessage = MutableLiveData<Assistant?>()
    val messages = dataSource.getAllMessages()
    init {
        initializeCurrentMessage()
    }
    //each message and command for each msg answers of the question
    private fun initializeCurrentMessage() {
        uiScope.launch {
            currentMessage.value = getCurrentMessageFromDtaabase()
        }
    }

    private suspend fun getCurrentMessageFromDtaabase(): Assistant? {
        return  withContext(Dispatchers.IO) {
            var message = dataSource.getCurrentMessage()
            //Intializing default message
            if (message?.assistant_message == "DEFAULT_MESSAGE" || message?.human_message == "DEFAULT_MESSAGE") {
                message= null
            }
            message
        }
    }
    //couroutunes are weel suited familiar task events iterators means for multitasking , rx java , we can do asynchrionous task
    fun sendMessageToDatabase(assistantMessage : String, humanMessage: String){
        uiScope.launch {
            val newAssistant = Assistant()
            newAssistant.assistant_message= assistantMessage
            newAssistant.human_message=humanMessage
            insert(newAssistant)
            currentMessage.value=getCurrentMessageFromDtaabase()
        }
    }
    //CRUDE OPERATION READ , DELETE , UPDATE , INSERT
    //save msgs to database and handle multitasking couroutines here
    private suspend fun insert(message : Assistant)
    {
        withContext(Dispatchers.IO){

            dataSource.insert(message)
        }
    }
    private suspend fun update(message : Assistant)
    {
        withContext(Dispatchers.IO){

            dataSource.update(message)
        }
    }
    fun onClear(){
        uiScope.launch {
            clear()
            currentMessage.value=null
        }
    }
    private suspend fun clear()
    {
        withContext(Dispatchers.IO){

            dataSource.clear()
        }
    }


}