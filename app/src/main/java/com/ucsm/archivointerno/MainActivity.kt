package com.ucsm.archivointerno

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ucsm.archivointerno.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val FILE_NAME = "budgetme.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSave.setOnClickListener { onClickSave() }
        binding.btnLoad.setOnClickListener { onClickLoad() }
    }

    private fun onClickSave() {
        val titulo = binding.txtTitulo.text.toString()
        val monto = binding.txtMonto.text.toString()
        val tipo = if (binding.radioIngreso.isChecked) "INGRESO" else "GASTO"
        val categoria = binding.txtCategoria.text.toString()

        if (titulo.isEmpty() || monto.isEmpty() || categoria.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val texto = "$tipo | $titulo | S/ $monto | $categoria"

        try {
            openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { fos ->
                fos.write(texto.toByteArray())
            }
            Toast.makeText(this, "Transacción guardada satisfactoriamente!",
                Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar: ${e.message}",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickLoad() {
        try {
            val contenido = openFileInput(FILE_NAME).bufferedReader().use { it.readText() }
            binding.txtTitulo.setText(contenido)
            Toast.makeText(this, "Archivo cargado satisfactoriamente!",
                Toast.LENGTH_SHORT).show()
        } catch (e: java.io.FileNotFoundException) {
            Toast.makeText(this, "El archivo no existe aún",
                Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al cargar: ${e.message}",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        binding.txtTitulo.setText("")
        binding.txtMonto.setText("")
        binding.txtCategoria.setText("")
        binding.radioIngreso.isChecked = true
    }
}