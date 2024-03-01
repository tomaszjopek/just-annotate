package pl.itj.dev.justannotatebackend.domain.ports

import kotlinx.coroutines.flow.Flow
import pl.itj.dev.justannotatebackend.domain.Project

interface ProjectRepository {

    suspend fun findAll(): Flow<Project>

}