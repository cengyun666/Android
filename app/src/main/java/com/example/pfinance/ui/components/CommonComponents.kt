package com.example.pfinance.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AmountText(
    amount: Double,
    modifier: Modifier = Modifier,
    isExpense: Boolean = false,
    currencySymbol: String = "¥",
    showSign: Boolean = true
) {
    val color = when {
        !showSign -> MaterialTheme.colorScheme.onSurface
        isExpense -> MaterialTheme.colorScheme.error
        else -> Color(0xFF4CAF50)
    }
    val sign = if (showSign) {
        if (isExpense) "-" else "+"
    } else ""
    val fmt = DecimalFormat("#,##0.00")
    Text(
        text = "$sign$currencySymbol${fmt.format(kotlin.math.abs(amount))}",
        color = color,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
}

@Composable
fun CategoryIcon(
    icon: String,
    color: Int,
    modifier: Modifier = Modifier,
    size: Int = 40
) {
    Surface(
        modifier = modifier.size(size.dp),
        shape = MaterialTheme.shapes.small,
        color = Color(color).copy(alpha = 0.12f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = iconNameToVector(icon),
                contentDescription = null,
                tint = Color(color),
                modifier = Modifier.size((size * 0.6).dp)
            )
        }
    }
}

fun iconNameToVector(name: String): ImageVector {
    return when (name) {
        "restaurant" -> Icons.Default.Restaurant
        "directions_car" -> Icons.Default.DirectionsCar
        "shopping_cart" -> Icons.Default.ShoppingCart
        "home" -> Icons.Default.Home
        "sports_esports" -> Icons.Default.SportsEsports
        "local_hospital" -> Icons.Default.LocalHospital
        "school" -> Icons.Default.School
        "phone" -> Icons.Default.Phone
        "checkroom" -> Icons.Default.Checkroom
        "work" -> Icons.Default.Work
        "emoji_events" -> Icons.Default.EmojiEvents
        "trending_up" -> Icons.Default.TrendingUp
        "handyman" -> Icons.Default.Handyman
        "redeem" -> Icons.Default.Redeem
        "savings" -> Icons.Default.Savings
        "account_balance_wallet" -> Icons.Default.AccountBalanceWallet
        "account_balance" -> Icons.Default.AccountBalance
        "credit_card" -> Icons.Default.CreditCard
        "payments" -> Icons.Default.Payments
        "currency_bitcoin" -> Icons.Default.CurrencyBitcoin
        "store" -> Icons.Default.Store
        "card_giftcard" -> Icons.Default.CardGiftcard
        "more_horiz" -> Icons.Default.MoreHoriz
        "flight" -> Icons.Default.Flight
        "pets" -> Icons.Default.Pets
        "fitness_center" -> Icons.Default.FitnessCenter
        "local_cafe" -> Icons.Default.LocalCafe
        "local_gas_station" -> Icons.Default.LocalGasStation
        "local_grocery_store" -> Icons.Default.LocalGroceryStore
        "local_pharmacy" -> Icons.Default.LocalPharmacy
        "local_laundry_service" -> Icons.Default.LocalLaundryService
        "celebration" -> Icons.Default.Celebration
        "category" -> Icons.Default.Category
        else -> Icons.Default.Category
    }
}

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String = "",
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (subtitle.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
        if (action != null) {
            Spacer(Modifier.height(24.dp))
            action()
        }
    }
}

@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    LinearProgressIndicator(
        progress = progress.coerceIn(0f, 1f),
        modifier = modifier.fillMaxWidth().height(8.dp),
        color = if (progress > 1f) MaterialTheme.colorScheme.error else color,
        trackColor = trackColor
    )
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        action?.invoke()
    }
}

fun formatDateTime(dateTime: LocalDateTime): String {
    val today = LocalDateTime.now().toLocalDate()
    val date = dateTime.toLocalDate()
    val formatter = when {
        date == today -> DateTimeFormatter.ofPattern("今天 HH:mm")
        date == today.minusDays(1) -> DateTimeFormatter.ofPattern("昨天 HH:mm")
        date.year == today.year -> DateTimeFormatter.ofPattern("MM月dd日 HH:mm")
        else -> DateTimeFormatter.ofPattern("yyyy年MM月dd日")
    }
    return dateTime.format(formatter)
}

@Composable
fun DetailRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

fun formatCurrency(amount: Double): String {
    val fmt = DecimalFormat("#,##0.00")
    return "¥${fmt.format(amount)}"
}

fun formatCurrencyShort(amount: Double): String {
    return when {
        amount >= 10000 -> {
            val wan = amount / 10000
            val fmt = DecimalFormat("#,##0.0")
            "¥${fmt.format(wan)}万"
        }
        else -> {
            val fmt = DecimalFormat("#,##0")
            "¥${fmt.format(amount)}"
        }
    }
}
