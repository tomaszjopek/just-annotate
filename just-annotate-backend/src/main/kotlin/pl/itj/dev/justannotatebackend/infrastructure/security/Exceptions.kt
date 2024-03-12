package pl.itj.dev.justannotatebackend.infrastructure.security

sealed class  SecurityException : RuntimeException()

class UsernameNotPresent : SecurityException()