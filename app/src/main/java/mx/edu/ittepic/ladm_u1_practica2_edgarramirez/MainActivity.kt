package mx.edu.ittepic.ladm_u1_practica2_edgarramirez

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //BOTON PARA ASIGNAR PERMISOS
        boton1.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
            }else{
                mensaje("LOS PERMISOS YA FUERON OTORGADOS")
            }
        }//boton1 - PERMISOS

        //BOTON PARA GUARDAR EN MEMORIA INTERNA O EXTERNA
        boton2.setOnClickListener {
            if(!radio1.isChecked && !radio2.isChecked){
                mensaje("SELECCION DE OPCION OBLIGATORIA: ARCHIVO INTERNO O EXTERNO")
                return@setOnClickListener
            }
            else if(radio1.isChecked){
                guardarArchivoInterno()
                return@setOnClickListener
            }
            else if(radio2.isChecked){
                guardarArchivoSD()
                return@setOnClickListener
            }
        }//boton2 - GUARDAR

        //BOTON PARA LEER LOS ARCHIVOS DE MEMORIA INTERNA O EXTERNA
        boton3.setOnClickListener {
            if(!radio1.isChecked && !radio2.isChecked){
                mensaje("SELECCION DE OPCION OBLIGATORIA: ARCHIVO INTERNO O EXTERNO")
                return@setOnClickListener
            }
            else if(radio1.isChecked){
                leerArchivoInterno()
                return@setOnClickListener
            }
            else if(radio2.isChecked){
                leerArchivoSD()
                return@setOnClickListener
            }
        }//boton3 - ABRIR


    }//onCreate

    //FUNCION QUE VERIFICA LA EXISTENCIA DE UNA SD
    fun noSD() : Boolean{
        var estado = Environment.getExternalStorageState()
        if (estado!=Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    //FUNCION PARA LEER ARCHIVOS DESDE LA SD
    fun leerArchivoSD(){
        var nombreArchivo = texto2.text.toString()
        if(noSD()){
            mensaje("NO EXISTE MEMORIA EXTERNA")
            return
        }
        try {

            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nombreArchivo)

            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var data = flujoEntrada.readLine()

            multipleTexto(data)
            flujoEntrada.close()
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }//leerArchivoSD

    //FUNCION PARA GUARDAR ARCHIVOS EN LA SD
    fun guardarArchivoSD(){
        var nombreArchivo = texto2.text.toString()
        if(noSD()){
            mensaje("NO EXISTE MEMORIA EXTERNA SD")
            return
        }
        try {

            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, nombreArchivo)

            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = texto1.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("¡EXITO! Se guardo el archivo correctamente en MEMORIA EXTERNA SD")
            multipleTexto("")
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }//guardarArchivoSD

    //FUNCION PARA GUARDAR UN ARCHIVO EN LA MEMORIA INTERNA
    fun guardarArchivoInterno(){
        var nombreArchivo = texto2.text.toString()
        try {

            var flujoSalida = OutputStreamWriter(openFileOutput(nombreArchivo, Context.MODE_PRIVATE))
            var data = texto1.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("¡EXITO! Se guardo el archivo correctamente en MEMORIA INTERNA")
            multipleTexto("")

        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }//guardarArchivoInterno

    //FUNCION PARA LEER ARCHIV0S DESDE MEMORIA INTERNA
    private fun leerArchivoInterno(){
        var nombreArchivo = texto2.text.toString()
        try {

            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(nombreArchivo)))
            var data = flujoEntrada.readLine()

            multipleTexto(data)
            flujoEntrada.close()

        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }//leerArchivoInterno

    //FUNCION MENSAJE
    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage((m))
            .setPositiveButton("OK"){d,i->}
            .show()
    }

    //FUNCION PARA TEXTO MULTILINEA
    fun multipleTexto(t:String){
        texto1.setText(t)
    }

}//main
