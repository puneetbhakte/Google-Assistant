package com.example.googleassistant.functions

import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import java.util.concurrent.Executor

interface TextRecognizerOptionsInterface {
    companion object : TextRecognizerOptionsInterface {
        override fun getLoggingEventId(): Int {
            TODO("Not yet implemented")
        }

        override fun getLoggingLanguageOption(): Int {
            TODO("Not yet implemented")
        }

        override fun getConfigLabel(): String {
            TODO("Not yet implemented")
        }

        override fun getLanguageHint(): String {
            TODO("Not yet implemented")
        }

        override fun getLoggingLibraryName(): String {
            TODO("Not yet implemented")
        }

        override fun getLoggingLibraryNameForOptionalModule(): String {
            TODO("Not yet implemented")
        }

        override fun getModuleId(): String {
            TODO("Not yet implemented")
        }

        override fun getExecutor(): Executor? {
            TODO("Not yet implemented")
        }

        override fun getIsThickClient(): Boolean {
            TODO("Not yet implemented")
        }

    }
}