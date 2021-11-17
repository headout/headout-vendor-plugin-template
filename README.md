# headout-vendor-plugin-template

## For updated architecture doc check this [doc](https://headout.atlassian.net/wiki/external/984154121/ZGMzMTIwMzhhZDU4NDVkOGJkZGNjNDZiMDU3NDMxYzI?atlOrigin=eyJpIjoiY2Q0MGU0Mzk5ZmFiNGQ5OWJjMmU1YjQ2YjkxMzYxYTEiLCJwIjoiYyJ9): 

Tech Stack:
Kotlin

Headout Vendor Plugins
Headout Vendor Plugins provide an easy way to enable a plug-and-play implementations for a Vendor API to be integrated with the rest of the Headout infrastructure.

To integrate a new Vendor via Vendor API automation, we have abstracted out the handling of its APIs using the concept of Vendor Plugins. Our codebase has a Gradle Module, vendor-plugins under package name, com.headout.vendor.plugins

To add a new Plugin, you need to create a Vendor Specific package under the Vendor Plugins package.

You can find a reference implementation of the Vendor Plugin in the following example codebase in the vendor-plugins gradle module with package com.headout.vendor.plugins.ho

You may refer to that with this document to better understand the concepts and apply them to your plugin.

The help relate to implementation is available on email and Vendor corresponding Slack Channel (preferred).

Building and running the project
This project uses Gradle as a build system. You need to have JAVA 11 (OpenJDK), installed in your system and set to default.

Steps:
Clone this repository

Run ./gradlew build -x test in the root directory

Import it as a gradle project in a supported IDE, like IntelliJ IDEA

Add new plugin by creating a new package by the name of the vendor/ reservation management system being integrated in the com.headout.vendor.plugins package. See reference implementation here

You may write tests to validate the implementation, see reference tests here (You’ll be able to access this once permission for private GitHub repo is given)

Note: The tests don't work in the reference implementation so don't try to execute them. They are just meant as a reference to be seen.

Nomenclature
You need to know the following nomenclature for creating a plugin.

In the following statements, T is a generic parameter that denotes the Vendor-specific Product Code. Each vendor needs some product-specific information that is not included in our booking fields, you need to create a Vendor Product Code class to contain all that information. You are encouraged to refer to existing implementations for more clarity on this

IVendorPlugin<T>: This is the base interface that every Vendor Plugin needs to implement. But since this is parent interface for following interfaces, you don't need to implement this directly.


interface IVendorPlugin<T> {
    fun getProductCodeClass(): Class<T>
}
Here you need to implement the getProductCodeClass() function to return the Class file of the data class you are using to save Vendor Specific information. Refer to other plugins for more details.

Booking Plugin
IVendorBookingPlugin<T>: This extends from IVendorPlugin<T>. This needs to be implemented if the Vendor API supports booking operation and we plan to support it.


interface IVendorBookingPlugin<T> : IVendorPlugin<T> {

    @Throws(VendorBookingException::class)
    fun book(booking: IVendorBooking<T>): Result<BookingResponse>

    fun cancel(booking: CancellationRequest<T>): Result<CancellationResponse>
}

Here there are two main methods which are clear from their name. The VendorAPIAutomator calls these methods to make a booking or cancel it. The return type is a Result object in both which is meant to indicate success or failure in the operation.

For booking, you receive an IVendorBooking<T> object that you may use to get booking specific details. You have the following primary data exposed through getters in the IVendorBooking to get booking details.

Booking Id : The unique id of the booking.

Inventory Data : The date for which the ticket has to be booked.

Inventory Time : The time for which the ticket has to be booked.

Product Name: Product Name as available on the vendor site. Information may be null. Not advised to use this to create a booking.

Product Code : Product Code, an object of Class T which is accepted as a generic parameter. Use this to store all the vendor-specific information you need to create a booking. Refer to other Vendor Plugin implementations for this.

Threshold Price USD :

Threshold Price Local :

Guest Numbers Map: It is the mapping of number of person per PersonType

Primary Guest: It contains details of the primary guest who filled in the details. The reservation if needs a name, has to be registered from details in this object.

Guest List: Some reservations might need data of each guest, you may get it from this object then. Otherwise, this would be null.

Hotel Name: Optional information that might be needed for some reservations.

Refer to existing plugin implementation for further clarity.

Inventory Plugin
IVendorInventoryPlugin<T>: This extends from IVendorPlugin<T>. This needs to be implemented if the Vendor API supports getting inventory updates. A Vendor may support only booking or inventory fetch or both.


interface IVendorInventoryPlugin<T> : IVendorPlugin<T> {
    fun checkAndUpdate(inventoryQuery: IInventoryQuery<T>)
    fun checkAndUpdate(inventoryQueries: List<IInventoryQuery<T>>)
}
The checkAndUpdate method provides either a query or a list of inventory queries. Each inventory query contains the following information.

Inventory Id: Unique identifier for that inventory. Need to specify the same in the result object.

Product Name

Product Code: Same as in Vendor Booking.

Inventory Date: Date for which inventory has to be checked.

Inventory Time: Time for which inventory has to be checked.

Seatmap Inventory Plugin
IVendorSeatmapInventoryPlugin<T>: This also extends from IVendorPlugin<T>. Need to implement this if the Vendor has Seatmap type inventory, for example in a theatre where each seat's availability needs to be known by us.


interface IVendorSeatmapInventoryPlugin<T> : IVendorPlugin<T> {
    fun getInventory(seatmapInventoryQueries: List<ISeatmapInventoryQuery<T>>): List<ISeatmapInventoryResponse<T>>
}
The seatmapInventoryQueries is a list of ISeatMapInventoryQuery<T> objects for which inventory needs to be checked. The information available is similar to Inventory Plugin but the result needs to include Seat specific inventory. Check the Response class to get a hold of how to construct the response object. Check LTD Plugin to better figure out the usage.

 

TODO:
(Contracts for full inventory sync and Price need to be updated)
