package com.example.igeniusandroidtest.data.source.remote

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ) =
        Converter<ResponseBody, Any> {
            if (it.contentLength() == 0L) null else retrofit.nextResponseBodyConverter<Any?>(
                this,
                type,
                annotations
            ).convert(it)
        }
}