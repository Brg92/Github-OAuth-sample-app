package com.example.igeniusandroidtest.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.igeniusandroidtest.databinding.FragmentLoginBinding
import com.example.igeniusandroidtest.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webview.apply {
            webViewClient = WebViewClient()
            //settings.javaScriptEnabled = true
            loadUrl(Constants.GITHUB_LOGIN_URL)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}