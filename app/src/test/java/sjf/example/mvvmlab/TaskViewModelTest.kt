package sjf.example.mvvmlab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
class TaskViewModelTest {
    // A fake repository gives us control over the starting data
    private class FakeRepository : TaskRepository() {
        override fun loadInitialTasks(): List<Task> = listOf(
            Task(1, "First"),
            Task(2, "Second", done = true)
        )
    }
    @Test
    fun toggleDone_flipsTheTaskState() {
        val viewModel = TaskViewModel(FakeRepository())
        viewModel.toggleDone(1)
        assertTrue(viewModel.tasks.value.first { it.id == 1 }.done)
    }
    @Test
    fun toggleDone_twice_returnsToOriginalState() {
        val viewModel = TaskViewModel(FakeRepository())
        viewModel.toggleDone(2)
        viewModel.toggleDone(2)
        assertTrue(viewModel.tasks.value.first { it.id == 2 }.done)
    }
    @Test
    fun addTask_appendsWithNextId() {
        val viewModel = TaskViewModel(FakeRepository())
        viewModel.addTask("Third")
        assertEquals(3, viewModel.tasks.value.size)
        assertEquals(3, viewModel.tasks.value.last().id)
        assertEquals("Third", viewModel.tasks.value.last().title)
    }
    @Test
    fun addTask_ignoresBlankTitle() {
        val viewModel = TaskViewModel(FakeRepository())
        viewModel.addTask("")
                    assertEquals(2, viewModel.tasks.value.size)
    }
    @Test
    fun deleteTask_removesTaskById() {
        val viewModel = TaskViewModel(FakeRepository())
        assertEquals(2, viewModel.tasks.value.size)
        viewModel.deleteTask(1)
        assertEquals(1, viewModel.tasks.value.size)
        assertFalse(viewModel.tasks.value.any { it.id == 1 })
        assertTrue(viewModel.tasks.value.any { it.id == 2 })
    }

    @Test
    fun emptyState_whenAllTasksDeleted_tasksIsEmpty() {
        val viewModel = TaskViewModel(FakeRepository())
        viewModel.deleteTask(1)
        viewModel.deleteTask(2)
        assertEquals(0, viewModel.tasks.value.size)
        assertTrue(viewModel.tasks.value.isEmpty())
    }
}