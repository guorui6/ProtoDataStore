package com.me.protodatastore

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.me.models.DataModel
import com.me.protodatastore.databinding.ActivityMainBinding
import com.me.protodatastore.pds.DataModelSerializer
import com.me.protodatastore.pds.ProtoDataProvider
import com.me.protodatastore.pds.ProtoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val DATA_STORE_FILE_NAME = "dm.pb"

private val Context.dataStore: DataStore<DataModel> by dataStore(
    DATA_STORE_FILE_NAME,
    serializer = DataModelSerializer
)

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val repository: ProtoRepository by lazy { ProtoRepository(dataStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.save.setOnClickListener {
            val protoData = ProtoDataProvider.dataForTestPds(10)
            lifecycleScope.launch(Dispatchers.IO) {
                repository.saveData(protoData)
            }
        }

        binding.add.setOnClickListener {
            val name = binding.edit.text.toString()
            if (name.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    repository.addPerson(name)
                }
            }
        }

        binding.del.setOnClickListener {
            val name = binding.edit.text.toString()
            if (name.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    repository.delPerson(name)
                }
            }
        }

        binding.update.setOnClickListener {
            val name = binding.edit.text.toString()
            if (name.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    repository.updatePersonByName(name)
                }
            }
        }

        repository.dataModelFlow.asLiveData().observe(this) {
            binding.textView.text =
                it.personList.joinToString(separator = System.lineSeparator()) { it.name }
        }
    }
}