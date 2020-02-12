package mx.edu.ittepic.ladm_u1_practica2_oscarramos

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var radioSeleccionado =""
        radioGP.setOnCheckedChangeListener{ d, i->
            var R : RadioButton = findViewById(i)
            radioSeleccionado = R.text.toString()+""
        }

        button.setOnClickListener {
            var seleccion : Int = radioGP.checkedRadioButtonId
            if(noEstaSeleccionado(seleccion)){

                mensaje("No esta selecionado el almacenamiento")

            }else{
                if(radioSeleccionado.equals("Archivo memoria INTERNA")){
                    guardarArchivoInterno()
                }else{
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
                    }else{

                    }
                    guardarArchivosSD()
                }
            }
        }

        button2.setOnClickListener {
            var seleccion : Int = radioGP.checkedRadioButtonId
            if(noEstaSeleccionado(seleccion)){

                mensaje("No esta selecionado el almacenamiento")

            }else{
                if(radioSeleccionado.equals("Archivo memoria INTERNA")){
                    leerArchivoInterno()
                }else{
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
                    }else{

                    }

                    leerArchivoSD()
                }
            }
        }
    }


    fun guardarArchivoInterno(){
        var nombre = editText4.text.toString()
         try {
             var flujoSalida =
                 OutputStreamWriter(openFileOutput(nombre+".txt", Context.MODE_PRIVATE))
             var data = editText3.text.toString()

             flujoSalida.write(data)
             flujoSalida.flush()
             flujoSalida.close()
             mensaje("¡EXITO! Se guardó correctamente")
             editText3.setText("")
             editText4.setText("")
         }catch (error:IOException){
             mensaje(error.message.toString())
         }
    }


    private fun leerArchivoInterno(){
        var nombre = editText4.text.toString()
        try {
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(nombre+".txt")))
            var data = flujoEntrada.readLine()
            var vector = data.split("&")
            ponerTexto(vector[0])
            flujoEntrada.close()
        }catch (error:IOException){

        }
    }


    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(m)
            .setPositiveButton("OK"){d, i->}
            .show()
    }


    fun ponerTexto(t1:String){
        editText3.setText(t1)
    }


    fun noEstaSeleccionado(x : Int):Boolean{

        if(x==-1){
            return true
        }
        return false

    }


    fun nosSD():Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true

        }

        return false
    }


    fun leerArchivoSD(){

        if(nosSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }

        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivos = File(rutaSD.absolutePath, editText4.text.toString()+".txt" )

            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivos)))
            var data = flujoEntrada.readLine()
            var vector = data.split("&")
            ponerTexto(vector[0])
            flujoEntrada.close()

        }catch (error : IOException){

        }
    }


    fun guardarArchivosSD(){

        if(nosSD()){

            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }

        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,editText4.text.toString()+".txt")

            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = editText3.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("EXITO! Se guardó correctamente")
            editText3.setText("")
            editText4.setText("")
        }catch ( error :IOException){
            mensaje(error.message.toString())
        }
    }






}
