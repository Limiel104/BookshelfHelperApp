package com.example.bookshelfhelper.comicbook

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bookshelfhelper.BuildConfig
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import com.example.bookshelfhelper.databinding.FragmentAddEditComicBookBinding
import java.io.File

class AddEditComicBookFragment : Fragment() {

    private lateinit var addEditComicBookItemViewModel: AddEditComicBookViewModel
    private lateinit var binding: FragmentAddEditComicBookBinding
    private val passedData: AddEditComicBookFragmentArgs by navArgs()
    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_comic_book, container,false)

        val dao = BookshelfDatabase.getInstance(requireContext()).comicBookDao
        val repository = ComicBookRepository(dao)
        val factory = AddEditComicBookViewModelFactory(repository)
        addEditComicBookItemViewModel = ViewModelProvider(this, factory)[AddEditComicBookViewModel::class.java]
        binding.addEditComicBookItemViewModel = addEditComicBookItemViewModel
        binding.lifecycleOwner = this

        prepareAutocompleteFields()
        prepareOnClickListeners()

        if(passedData.comicBookToUpdate != null){
            passedData.comicBookToUpdate?.let { Log.i("TAG", it.title.toString()) }
            addEditComicBookItemViewModel.comicBookToUpdate = passedData.comicBookToUpdate!!
            addEditComicBookItemViewModel.prepareUpdateLayout()
            addEditComicBookItemViewModel.updateMode = true
        }

        prepareObservers()

        return  binding.root
    }

    private fun prepareAutocompleteFields(){

        val genres = resources.getStringArray(R.array.genres)
        val formats = resources.getStringArray(R.array.formats)
        val types = resources.getStringArray(R.array.types)
        val languages = resources.getStringArray(R.array.languages)
        val statuses = resources.getStringArray(R.array.statuses)
        val pagesColor = resources.getStringArray(R.array.pagesColor)

        val genresArrayAdapter = context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,genres) }
        val  formatsArrayAdapter =
            context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,formats) }
        val  typesArrayAdapter = context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,types) }
        val  languagesArrayAdapter =
            context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,languages) }
        val statusesArrayAdapter = context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,statuses) }
        val  pagesColorArrayAdapter =
            context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,pagesColor) }

        binding.addGenre.setAdapter(genresArrayAdapter)
        binding.addFormat.setAdapter(formatsArrayAdapter)
        binding.addType.setAdapter(typesArrayAdapter)
        binding.addLanguage.setAdapter(languagesArrayAdapter)
        binding.addStatus.setAdapter(statusesArrayAdapter)
        binding.addPagesColor.setAdapter(pagesColorArrayAdapter)
    }

    private fun prepareOnClickListeners(){

        binding.addImage.setOnClickListener {
            takeImage()
        }
    }

    private fun prepareObservers(){

        addEditComicBookItemViewModel.isDone.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })

        addEditComicBookItemViewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context,it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun takeImage(){
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        addEditComicBookItemViewModel.inputImagePath = tmpFile.path
        Log.i("TAG",tmpFile.path)

        return context?.let { FileProvider.getUriForFile(it, "${BuildConfig.APPLICATION_ID}.provider", tmpFile) }!!
    }
}