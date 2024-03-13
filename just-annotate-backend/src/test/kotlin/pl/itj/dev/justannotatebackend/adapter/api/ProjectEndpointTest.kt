package pl.itj.dev.justannotatebackend.adapter.api

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.itj.dev.justannotatebackend.adapter.api.exceptions.ObjectNotFound
import pl.itj.dev.justannotatebackend.domain.Project
import pl.itj.dev.justannotatebackend.domain.ports.ProjectRepository

@ExtendWith(MockitoExtension::class)
class ProjectEndpointTest {

    private val projectRepository: ProjectRepository = mock()

}
