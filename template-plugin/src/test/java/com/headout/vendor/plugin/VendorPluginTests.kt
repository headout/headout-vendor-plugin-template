package com.headout.vendor.plugin

import com.headout.vendor.api.IVendorPlugin

interface VendorPluginTests<PC, VP: IVendorPlugin<PC>> {
    val vendorPlugin: VP
    val productCodes: List<PC>
}