package com.example.demo;

import com.mastercard.merchant.checkout.PaymentDataApi;
import com.mastercard.merchant.checkout.model.PaymentData;
import com.mastercard.sdk.core.ApiConfig;
import com.mastercard.sdk.core.MasterCardApiConfig;
import com.mastercard.sdk.core.util.QueryParams;

public class TestPaymant extends PaymentDataApi {
  static private   ApiConfig apiConfig;

    public static PaymentData showTest(String transactionId, QueryParams queryParams) {
        apiConfig=MasterCardApiConfig.config();
        System.out.println(apiConfig.toString()+"error");
        return show(transactionId, queryParams, apiConfig);
    }
}
