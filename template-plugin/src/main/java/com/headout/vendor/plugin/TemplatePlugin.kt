
import com.headout.vendor.api.IHeadoutInventoryApi
import com.headout.vendor.api.IVendorBooking
import com.headout.vendor.api.IVendorBookingPlugin
import com.headout.vendor.api.IVendorInventoryPlugin
import com.headout.vendor.api.Result
import com.headout.vendor.api.models.BookingResponse
import com.headout.vendor.api.models.CancellationRequest
import com.headout.vendor.api.models.CancellationResponse
import com.headout.vendor.api.models.InventoryRangeQuery
import com.headout.vendor.plugin.TemplatePluginHelper
import com.headout.vendor.plugin.TemplateProductCode
import com.headout.vendor.plugin.ho.api.TemplateApi
import com.headout.vendor.plugin.utils.ITemplateCredentials
import com.headout.vendor.plugin.utils.wrapError
import java.util.logging.Logger


class TemplatePlugin private constructor(
    private val hoApi: TemplateApi,
    private val headoutInventoryService: IHeadoutInventoryApi
) : IVendorBookingPlugin<TemplateProductCode>, IVendorInventoryPlugin<TemplateProductCode> {

    private val logger = Logger.getLogger(TemplatePlugin::class.java.name)

    override fun getProductCodeClass() = TemplateProductCode::class.java

    companion object {
        fun create(credentials: ITemplateCredentials, headoutInventoryService: IHeadoutInventoryApi): TemplatePlugin {
            val pluginHelper = TemplatePluginHelper(credentials)
            return TemplatePlugin(pluginHelper.getTemplateApi(), headoutInventoryService)
        }
    }

    override suspend fun book(booking: IVendorBooking<TemplateProductCode>): Result<BookingResponse> = wrapError {
            TODO("Implement book method")
        }

    override suspend fun cancel(cancellationRequest: CancellationRequest<TemplateProductCode>): Result<CancellationResponse> = wrapError {
        TODO("Implement cancel method")
    }

    override fun updateInventoryForRangeQueries(inventoryRangeQueries: List<InventoryRangeQuery<TemplateProductCode>>)  {
        TODO("Imlement updateInventoryForRangeQueries method")
    }
}