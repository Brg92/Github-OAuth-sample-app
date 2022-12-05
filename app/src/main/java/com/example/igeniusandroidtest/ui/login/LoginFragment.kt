package com.example.igeniusandroidtest.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.igeniusandroidtest.BuildConfig
import com.example.igeniusandroidtest.R
import com.example.igeniusandroidtest.databinding.FragmentLoginBinding
import com.example.igeniusandroidtest.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url =
            "${BuildConfig.BASE_URL}${BuildConfig.AUTH_ENDPOINT}?client_id=${BuildConfig.CLIENT_ID}&scope=read:user,repo&redirect_url=${BuildConfig.REDIRECT_URL}"

        binding.webview.apply {
            webViewClient = CustomWebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(url)
        }
        subscribeApiOnSuccessEvent()
        subscribeApiOnFailureEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeApiOnSuccessEvent() {
        lifecycleScope.launch {
            viewModel.onSuccessEvent.collect { event -> event?.let { findNavController().navigate(R.id.to_loading) } }
        }
    }

    private fun subscribeApiOnFailureEvent() {
        lifecycleScope.launch {
            viewModel.onFailureEvent.collect { event ->
                event?.let {
                    Toast.makeText(requireActivity(), "Api failure. It needs token to access.", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().finish()
                }
            }
        }
    }

    inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            /*
             This is where the user get its authorization (is logged) so it has to redirect to the Loading.
             The user is logged if there is no path in the request url, so it has to be overridden the current url.
             */
            val uri = request?.url ?: return super.shouldOverrideUrlLoading(view, request)
            Timber.d("url $uri")
            return if (uri.toString().startsWith(BuildConfig.REDIRECT_URL)) {
                if (viewModel.hasAccessToken) {
                    findNavController().navigate(R.id.to_loading)
                    return true
                }
                val code = uri.getQueryParameter("code")
                code?.let { viewModel.getAccessToken(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, it) }
                true
            } else {
                super.shouldOverrideUrlLoading(view, request)
            }
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            /*
            * The way to start offline-app.
            * */
            if (error?.errorCode == Constants.ERR_INTERNET_DISCONNECTED && viewModel.hasAccessToken)
                findNavController().navigate(R.id.to_loading)
        }
    }
}