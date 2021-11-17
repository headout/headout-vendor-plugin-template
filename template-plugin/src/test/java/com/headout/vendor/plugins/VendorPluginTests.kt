package com.headout.vendor.plugins

import com.headout.vendor.api.IVendorPlugin

interface VendorPluginTests<PC, VP: IVendorPlugin<PC>> {
    val vendorPlugin: VP
    val productCodes: List<PC>
}