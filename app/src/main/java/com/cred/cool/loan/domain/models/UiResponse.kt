data class UiResponse(
    val items: List<Item>
)

data class Item(
    val open_state: OpenState, // Change to match the JSON key
    val closed_state: ClosedState, // Change to match the JSON key
    val cta_text: String // Change to match the JSON key
)

data class OpenState(
    val body: OpenStateBody
)

data class ClosedState(
    val body: ClosedBody
)

data class OpenStateBody(
    val title: String,
    val subtitle: String,
    val card: Card? = null,
    val items: List<RepaymentItem>? = null,
    val footer: String
)

data class Card(
    val header: String,
    val description: String,
    val max_range: Int,
    val min_range: Int
)

data class RepaymentItem(
    val emi: String? = null,
    val duration: String? = null,
    val title: String,
    val subtitle: String,
    val tag: String? = null,
    val icon: String? = null
)

data class ClosedBody(
    val key1: String? = null,
    val key2: String? = null // Added to match the second closed_state's body
)