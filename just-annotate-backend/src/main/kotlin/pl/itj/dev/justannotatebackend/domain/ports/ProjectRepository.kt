package pl.itj.dev.justannotatebackend.domain.ports

import kotlinx.coroutines.flow.Flow
import pl.itj.dev.justannotatebackend.domain.Project

interface ProjectRepository {

    fun findAll(): Flow<Project>

    fun findAllByOwner(owner: String): Flow<Project>

    suspend fun findById(id: String): Project?

    suspend fun save(project: Project): Project

    suspend fun deleteById(id: String)

}