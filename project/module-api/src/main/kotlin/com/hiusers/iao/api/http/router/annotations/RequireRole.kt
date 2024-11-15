package com.hiusers.iao.api.http.router.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireRole(vararg val values: String)