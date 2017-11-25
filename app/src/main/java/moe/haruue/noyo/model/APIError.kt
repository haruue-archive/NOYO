package moe.haruue.noyo.model

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
data class APIError(
        var code: Int = 0,
        var status: String = "",
        var message: String = "",
        var errno: Int = 0
)

object APIErrorList {
    const val mobileMalformed = 10001
    const val mobileUsed = 10002
    const val emailMalformed = 10003
    const val emailUsed = 10004
    const val roleNotExist = 10005
    const val usernameUsed = 10006
    const val passwordEmpty = 20001
    const val passwordTooShort = 20002
    const val passwordTooLong = 20003
    const val passwordWeak1 = 20011
    const val passwordWeak2 = 20012
    const val unexpectedDatabaseError = 30000
    const val usernameEmpty = 40001
    const val accountNotExist = 40002
    const val accountDisabled = 40003
    const val passwordError = 40004
    const val authorizationRequired = 50000
    const val informationNotComplete = 50001
    const val unknownWhatToVerify = 50002
    const val noSuchUid = 50003
    const val noMobile = 50004
    const val noEmail = 50005
    const val invalidOp = 50006
    const val failedSendMail = 50007
    const val failedSendSMS = 50008
    const val invalidWhat = 50009
    const val unexpectedVerifyCheckError = 50010
    const val verifyFailed = 50011
    const val emailHasVerified = 50012
    const val mobileHasVerified = 50013
    const val unsupportedOpWithWhat = 50014
    const val errorType = 80001
    const val permissionDeniedModifyGoods = 80002
    const val noSuchGoods = 80003
    const val negativeValue = 80004
    const val noSuchGoodsQueryField = 80005
    const val permissionDeniedModifyOrder = 90000
    const val noSuchOrder = 90001
    const val errorOrderStatus = 90002
}

