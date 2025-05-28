package io.jacob.igozogo.ui.tooling

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.jacob.igozogo.R
import io.jacob.igozogo.core.domain.model.Place
import kotlinx.coroutines.flow.flowOf

@Composable
fun previewThemeImage(): Painter = painterResource(id = R.drawable.preview_theme)

val PreviewLoremIpsum = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras ullamcorper pharetra massa,
        sed suscipit nunc mollis in. Sed tincidunt orci lacus, vel ullamcorper nibh congue quis.
        Etiam imperdiet facilisis ligula id facilisis. Suspendisse potenti. Cras vehicula neque sed
        nulla auctor scelerisque. Vestibulum at congue risus, vel aliquet eros. In arcu mauris,
        facilisis eget magna quis, rhoncus volutpat mi. Phasellus vel sollicitudin quam, eu
        consectetur dolor. Proin lobortis venenatis sem, in vestibulum est. Duis ac nibh interdum,
        """.trimIndent()

val PreviewPlace = Place(
    placeId = 3375,
    placeLangId = 5093,
    placeCategory = "고궁 스토리텔링",
    addr1 = "서울",
    addr2 = "종로구",
    title = "경복궁",
    mapX = 126.97704,
    mapY = 37.579617,
    langCheck = "1000",
    langCode = "ko",
    imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/sightImage/service/3375.jpg",
    createdTime = "20240125120046",
    modifiedTime = "20240125120100",
)

val PreviewThemes = List(5) { PreviewPlace }

val PreviewCategoryList = List(30) { "Category $it" }

@Composable
fun previewCategoryListLazyPagingItems(): LazyPagingItems<String> {
    return remember { flowOf(PagingData.from(PreviewCategoryList)) }.collectAsLazyPagingItems()
}