package com.twowaystyle.loafdash

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.util.Locale

class ReadOut(context: Context): TextToSpeech.OnInitListener{

    private var tts : TextToSpeech? = null

    init {
        // TextToSpeech初期化
        tts = TextToSpeech(context,this)
    }

    var speechState: MutableLiveData<String> = MutableLiveData<String>()

    override fun onInit(status: Int){
        if(status == TextToSpeech.SUCCESS){
            Log.d("tts","TextToSpeech初期化成功")

            val listener = object : UtteranceProgressListener(){
                var tag : String = "TTS"
                override fun onDone(utteranceId: String?) {
                    speechState.postValue("onDone")
                    Log.d(tag,"音声再生が完了しました。")
                }
                override fun onError(utteranceId: String?) {
                    Log.d(tag,"音声再生中にエラーが発生しました。")
                }
                override fun onError(utteranceId: String?, errorCode: Int) {
                    Log.d(tag,"音声再生中にエラーが発生しました。")
                }
                override fun onStart(utteranceId: String?) {
                    speechState.postValue("onStart")
                    Log.d(tag,"音声再生を開始します。")
                }
                override fun onStop(utteranceId: String?, interrupted: Boolean) {
                    Log.d(tag,"音声再生を中止します。")
                }
                override fun onBeginSynthesis(utteranceId: String?, sampleRateInHz: Int, audioFormat: Int, channelCount: Int) {
                    Log.d(tag,"音声の合成を開始します。")
                }
                override fun onAudioAvailable(utteranceId: String?, audio: ByteArray?) {
                    Log.d(tag,"音声が利用可能になりました。")
                }
            }
            // イベントリスナを登録
            tts?.setOnUtteranceProgressListener(listener)
        }else{
            Log.d("tts","TextToSpeech初期化失敗")
        }
    }

    fun speechText(text:String){
        tts?.language = Locale.JAPANESE
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH,null,"ID")
    }

    fun speechStop(){
        tts?.stop()
    }

}