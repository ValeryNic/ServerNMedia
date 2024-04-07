package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.viewmodel.ViewModelFactory

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }
    private val dependencyContainer = DependencyContainer.getInstance()
    private val viewModel: PostViewModel by viewModels(
    //private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment,
        factoryProducer = {
            ViewModelFactory(dependencyContainer.repository, dependencyContainer.appAuth)
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg
            ?.let(binding.edit::setText)

        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
        }
        viewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        return binding.root
    }
}