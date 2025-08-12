package io.jacob.igozogo.core.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import kotlin.math.absoluteValue

@Composable
fun CategoryItemList(
    modifier: Modifier = Modifier,
    categories: List<String>,
    onItemClick: (String) -> Unit,
) {
    val padding = 16.dp

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp + padding),
        horizontalArrangement = Arrangement.spacedBy(padding),
        verticalArrangement = Arrangement.spacedBy(padding),
        contentPadding = PaddingValues(horizontal = padding)
    ) {
        items(
            count = categories.size,
            key = { categories[it] },
        ) { index ->
            CategoryItem(
                modifier = Modifier.width(200.dp),
                category = categories[index],
                onClick = { onItemClick(categories[index]) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: String,
    onClick: () -> Unit
) {
    val icon = remember(category) { pickIconFor(category) }
    val bgColor = remember(category) { pastelColorForCategory(category) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        color = bgColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                text = category,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2
            )

            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Top),
                imageVector = icon,
                contentDescription = category
            )
        }
    }
}

private fun pickIconFor(text: String): ImageVector {
    val t = text.lowercase()
    return when {
        listOf("역사", "history", "연표", "기록", "유적", "유물").any { t.contains(it) } ->
            Icons.Outlined.AccountBalance

        listOf("사찰", "temple", "절", "사당").any { t.contains(it) } ->
            Icons.Outlined.TempleBuddhist

        listOf("시장", "market", "마켓").any { t.contains(it) } ->
            Icons.Outlined.Storefront

        listOf("박물관", "museum", "미술관").any { t.contains(it) } ->
            Icons.Outlined.Museum

        listOf("다리", "bridge", "교량").any { t.contains(it) } ->
            Icons.Outlined.Looks

        listOf("물", "water", "강", "호수").any { t.contains(it) } ->
            Icons.Outlined.Water

        listOf("골목", "alley", "거리", "길", "로드").any { t.contains(it) } ->
            Icons.Outlined.ForkRight

        listOf("성", "castle", "성곽").any { t.contains(it) } ->
            Icons.Outlined.Castle

        listOf("음악", "music", "노래", "뮤직").any { t.contains(it) } ->
            Icons.Outlined.LibraryMusic

        listOf("영화", "movie", "video", "비디오", "시네마").any { t.contains(it) } ->
            Icons.Outlined.Movie

        listOf("책", "book", "독서", "서재").any { t.contains(it) } ->
            Icons.AutoMirrored.Outlined.MenuBook

        listOf("뉴스", "news", "기사").any { t.contains(it) } ->
            Icons.AutoMirrored.Outlined.Article

        listOf("여행", "travel", "trip", "관광").any { t.contains(it) } ->
            Icons.Outlined.Flight

        listOf("지도", "map", "내비", "navigation", "지역").any { t.contains(it) } ->
            Icons.Outlined.Map

        listOf("역", "station", "터미널").any { t.contains(it) } ->
            Icons.Outlined.Subway

        listOf("버스", "bus").any { t.contains(it) } ->
            Icons.Outlined.DirectionsBus

        listOf("습지", "grass", "풀").any { t.contains(it) } ->
            Icons.Outlined.Grass

        listOf("미스터 션샤인").any { t.contains(it) } ->
            Icons.Outlined.Man

        listOf("사진", "photo", "이미지", "갤러리").any { t.contains(it) } ->
            Icons.Outlined.Photo

        listOf("스포츠", "sport", "운동", "fitness").any { t.contains(it) } ->
            Icons.Outlined.SportsSoccer

        listOf("음식", "food", "맛집", "레스토랑", "식당").any { t.contains(it) } ->
            Icons.Outlined.Restaurant

        listOf("쇼핑", "shopping", "스토어", "마켓").any { t.contains(it) } ->
            Icons.Outlined.ShoppingBag

        listOf("설정", "세팅", "setting", "환경").any { t.contains(it) } ->
            Icons.Outlined.Settings

        else ->
            Icons.Outlined.Category // 기본값
    }
}

private val pastelColors = listOf(
    Color(0xFF8FBF88), // 어두운 민트그린
    Color(0xFF6AA3C1), // 어두운 하늘
    Color(0xFFB48291), // 말린 장미
    Color(0xFF9B8FC3), // 어두운 라벤더
    Color(0xFFA57B63), // 브라운 오렌지
    Color(0xFF5B8F8A), // 어두운 청록
    Color(0xFF7F8C8D), // 그레이시 블루
    Color(0xFFC27D7C), // 코랄 브라운
    Color(0xFF7A6F91), // 그레이 퍼플
    Color(0xFF6E7B8B), // 블루 그레이
    Color(0xFF8C8A6D), // 카키 베이지
    Color(0xFF567D46), // 짙은 올리브그린
    Color(0xFF3D6B7B), // 다크 블루그린
    Color(0xFF9E5A63), // 말린 로즈
    Color(0xFF7D5E57), // 브라운
    Color(0xFF5C6E91), // 네이비 톤 블루
    Color(0xFF826A8B), // 퍼플 브라운
    Color(0xFF6F4E37), // 커피 브라운
    Color(0xFF8A6552), // 초콜릿 브라운
    Color(0xFF4B6D76)  // 다크 틸
)

private fun pastelColorForCategory(category: String): Color {
    val index = category.hashCode().absoluteValue % pastelColors.size
    return pastelColors[index]
}

@DevicePreviews
@Composable
private fun CategoryItemPreview() {
    IgozogoTheme {
        CategoryItem(
            category = "백제역사여행",
            onClick = {}
        )
    }
}