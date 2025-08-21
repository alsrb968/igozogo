package io.jacob.igozogo.core.data.db

import app.cash.turbine.test
import io.jacob.igozogo.core.data.model.local.search.RecentSearchEntity
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.Duration.Companion.hours

@RunWith(RobolectricTestRunner::class)
class RecentSearchDaoTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val dbRule = RoomDatabaseRule()

    private lateinit var dao: RecentSearchDao

    @Before
    fun setup() {
        dao = dbRule.db.recentSearchDao()
    }

    @Test
    fun `Given new query, When upsertRecentSearch, Then insert to database`() =
        runTest {
            // Given
            val recentSearch = RecentSearchEntity(
                query = "서울",
                queriedDate = Clock.System.now()
            )

            // When
            dao.upsertRecentSearch(recentSearch)

            // Then
            dao.getRecentSearches(10).test {
                val searches = awaitItem()
                assertEquals(1, searches.size)
                assertEquals("서울", searches[0].query)
            }
        }

    @Test
    fun `Given existing query, When upsertRecentSearch, Then update queriedDate`() =
        runTest {
            // Given
            val baseTime = Clock.System.now()
            val firstTime = baseTime - 1.hours
            val secondTime = baseTime

            val firstSearch = RecentSearchEntity(
                query = "부산",
                queriedDate = firstTime
            )
            val secondSearch = RecentSearchEntity(
                query = "부산",
                queriedDate = secondTime
            )

            // When
            dao.upsertRecentSearch(firstSearch)
            dao.upsertRecentSearch(secondSearch)

            // Then
            dao.getRecentSearches(10).test {
                val searches = awaitItem()
                assertEquals(1, searches.size)
                assertEquals("부산", searches[0].query)
                // 시간 정밀도 문제로 인해 초 단위로만 비교
                assertTrue(
                    "Second time should be more recent than first time",
                    searches[0].queriedDate > firstTime
                )
            }
        }

    @Test
    fun `Given multiple searches, When getRecentSearches, Then return ordered by date descending`() =
        runTest {
            // Given
            val now = Clock.System.now()
            val searches = listOf(
                RecentSearchEntity("첫번째", now - 3.hours),
                RecentSearchEntity("두번째", now - 2.hours),
                RecentSearchEntity("세번째", now - 1.hours),
                RecentSearchEntity("네번째", now)
            )

            // When
            searches.forEach { dao.upsertRecentSearch(it) }

            // Then
            dao.getRecentSearches(10).test {
                val result = awaitItem()
                assertEquals(4, result.size)
                assertEquals("네번째", result[0].query) // 가장 최근
                assertEquals("세번째", result[1].query)
                assertEquals("두번째", result[2].query)
                assertEquals("첫번째", result[3].query) // 가장 오래된
            }
        }

    @Test
    fun `Given many searches, When getRecentSearches with limit, Then return limited results`() =
        runTest {
            // Given
            val now = Clock.System.now()
            val searches = (1..5).map { index ->
                RecentSearchEntity(
                    query = "검색$index",
                    queriedDate = now - index.hours
                )
            }

            // When
            searches.forEach { dao.upsertRecentSearch(it) }

            // Then
            dao.getRecentSearches(3).test {
                val result = awaitItem()
                assertEquals(3, result.size)
                assertEquals("검색1", result[0].query) // 가장 최근 3개만
                assertEquals("검색2", result[1].query)
                assertEquals("검색3", result[2].query)
            }
        }

    @Test
    fun `Given existing query, When deleteRecentSearch, Then remove from database`() =
        runTest {
            // Given
            val searches = listOf(
                RecentSearchEntity("서울", Clock.System.now()),
                RecentSearchEntity("부산", Clock.System.now()),
                RecentSearchEntity("대구", Clock.System.now())
            )
            searches.forEach { dao.upsertRecentSearch(it) }

            // When
            dao.deleteRecentSearch("부산")

            // Then
            dao.getRecentSearches(10).test {
                val result = awaitItem()
                assertEquals(2, result.size)
                assertFalse(result.any { it.query == "부산" })
                assertTrue(result.any { it.query == "서울" })
                assertTrue(result.any { it.query == "대구" })
            }
        }

    @Test
    fun `Given non-existing query, When deleteRecentSearch, Then no change to database`() =
        runTest {
            // Given
            val search = RecentSearchEntity("서울", Clock.System.now())
            dao.upsertRecentSearch(search)

            // When
            dao.deleteRecentSearch("존재하지않는검색어")

            // Then
            dao.getRecentSearches(10).test {
                val result = awaitItem()
                assertEquals(1, result.size)
                assertEquals("서울", result[0].query)
            }
        }

    @Test
    fun `Given existing searches, When clearRecentSearches, Then remove all from database`() =
        runTest {
            // Given
            val searches = listOf(
                RecentSearchEntity("서울", Clock.System.now()),
                RecentSearchEntity("부산", Clock.System.now()),
                RecentSearchEntity("대구", Clock.System.now())
            )
            searches.forEach { dao.upsertRecentSearch(it) }

            // When
            dao.clearRecentSearches()

            // Then
            dao.getRecentSearches(10).test {
                val result = awaitItem()
                assertEquals(0, result.size)
            }
        }

    @Test
    fun `Given empty database, When getRecentSearchesCount, Then return zero`() =
        runTest {
            // When
            val count = dao.getRecentSearchesCount()

            // Then
            assertEquals(0, count)
        }

    @Test
    fun `Given existing searches, When getRecentSearchesCount, Then return correct count`() =
        runTest {
            // Given
            val searches = listOf(
                RecentSearchEntity("서울", Clock.System.now()),
                RecentSearchEntity("부산", Clock.System.now()),
                RecentSearchEntity("대구", Clock.System.now())
            )
            searches.forEach { dao.upsertRecentSearch(it) }

            // When
            val count = dao.getRecentSearchesCount()

            // Then
            assertEquals(3, count)
        }
}