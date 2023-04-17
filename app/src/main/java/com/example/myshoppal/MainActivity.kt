package com.example.myshoppal

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class MainActivity : AppCompatActivity() {

   // private lateinit var binding : ActivityMainBinding
    //private lateinit var database : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        }
        setContentView(R.layout.activity_main)

        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("Heal", DownloadType.LOCAL_MODEL, conditions)
            .addOnCompleteListener {
                // Download complete. Depending on your app, you could enable the ML
                // feature, or switch from the local model to the remote model, etc.
            }

        // get reference to button
        val btn = findViewById<Button>(R.id.button)
        val textInput = findViewById<EditText>(R.id.editTextTextMultiLine)
        //calling model
        val interpreter = loadModelFromAsset( "my_model.tflite")

        // set on-click listener
        btn.setOnClickListener {
            // your code to perform when the user clicks on the button
            // Write a message to the database
            //val database = Firebase.database
            //val myRef = database.getReference("journal")

            val database=FirebaseDatabase.getInstance();
            val myRef = database.getReference()
            //storing your text to a variable
            val data=textInput.text.toString()
            val mood=predict(interpreter as Interpreter,data)
           // print(mood)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS")
            val current = LocalDateTime.now().format(formatter)
            myRef.child("datas").child(current.toString()).setValue(data)
            textInput.text.clear()
            val conditions = CustomModelDownloadConditions.Builder()
                .requireWifi()
                .build()
//            FirebaseModelDownloader.getInstance()
//                .getModel("Heal", DownloadType.LOCAL_MODEL, conditions)
//                .addOnCompleteListener {
//                    // Download complete. Depending on your app, you could enable the ML
//                    // feature, or switch from the local model to the remote model, etc.
//
//                }

        }


//       //These codes are written for connecting the application with the model
//
//
//        // Load a TensorFlow Lite model from the assets directory
//        fun loadModelFromAsset(context: Context, filename: String): ByteBuffer {
//            // Load the model from the assets directory
//            val assetManager = context.assets
//            val model = assetManager.openFd(filename)
//            val modelByteBuffer = model.createByteBuffer()
//            return modelByteBuffer
//
//        }
//
//        // Load a TensorFlow Lite model from a byte array in memory
//        fun loadModelFromMemory(modelByteArray: ByteArray): ByteBuffer {
//            // Create a ByteBuffer from the byte array
//            val modelByteBuffer = ByteBuffer.allocateDirect(modelByteArray.size)
//            modelByteBuffer.order(ByteOrder.nativeOrder())
//            modelByteBuffer.put(modelByteArray)
//            modelByteBuffer.rewind()
//            return modelByteBuffer
//        }
//
//        // Run inference on a TensorFlow Lite model
//        fun runInference(modelByteBuffer: ByteBuffer, inputData: FloatArray, outputData: FloatArray) {
//            // Create the TensorFlow Lite interpreter
//            val interpreter = Interpreter(modelByteBuffer)
//
//            // Get input and output tensors
//            val inputTensor = interpreter.getInputTensor(0)
//            val outputTensor = interpreter.getOutputTensor(0)
//
//            // Prepare input buffer
//            val inputBuffer = inputTensor.buffer
//            inputBuffer.rewind()
//            for (input in inputData) {
//                inputBuffer.putFloat(input)
//            }
//
//            // Run inference
//            interpreter.run()
//
//            // Prepare output buffer
//            val outputBuffer = outputTensor.buffer
//            outputBuffer.rewind()
//            outputBuffer.asFloatBuffer().get(outputData)
//        }


    }

    //predicting the model
    fun predict(interpreter: Interpreter, inputData: String): FloatArray {
        // Get input and output tensors
        val inputTensor = interpreter.getInputTensor(0)
        val outputTensor = interpreter.getOutputTensor(0)
        // Prepare input data

//        inputTensor.buffer().rewind()
//        inputTensor.buffer().put(inputData)

        // Run inference
        interpreter.run(inputTensor, outputTensor)
        // Get output data
        val outputData = FloatArray(outputTensor.shape()[0])
//        outputTensor.buffer().rewind()
//
//        outputTensor.buffer.get(outputData)



        return outputData
      //  return outputData
//        val model = MyModel.newInstance(context)
//
//// Creates inputs for reference.
//        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 300), DataType.FLOAT32)
//        inputFeature0.loadBuffer(byteBuffer)
//
//// Runs model inference and gets result.
//        val outputs = model.process(inputFeature0)
//        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//// Releases model resources if no longer used.
//        model.close()
//

    }
//loading the model to the application
   fun loadModelFromAsset( assetFileName: String): Any {
       try {
           val assetFileDescriptor = assets.openFd(assetFileName)
           val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
           val fileChannel = inputStream.channel
           val startOffset = assetFileDescriptor.startOffset
           val declaredLength = assetFileDescriptor.declaredLength
           val buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
           val modelByteBuffer = ByteBuffer.allocateDirect(declaredLength.toInt())
           modelByteBuffer.order(ByteOrder.nativeOrder())
           modelByteBuffer.put(buffer)
           val interpreter = Interpreter(modelByteBuffer)
           return interpreter
       }catch (e:java.lang.Exception){
           return e
       }

    }
}